package br.com.wmw.lavenderepda.business.security;

import br.com.wmw.framework.business.domain.Usuario;
import br.com.wmw.framework.business.service.UsuarioService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.service.LogPdaService;
import br.com.wmw.lavenderepda.business.validation.LoginFailureControlException;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.service.UsuarioLavendereService;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.sys.InvalidNumberException;
import totalcross.sys.Time;

import java.sql.SQLException;

public class LoginFailureControl {
	private static LoginFailureControl instance;

	private LoginFailureControl() {
	}

	public static LoginFailureControl getInstance() {
		if (instance == null) {
			instance = new LoginFailureControl();
		}
		return instance;
	}

	public boolean control(final Usuario usuario, String dsSenha) {
		try {
			UiUtil.showLoadingScreen();
			final int maxTentativas = LavenderePdaConfig.nuTentativasLogin;
			if (maxTentativas > 0) {
				if (isLoginLiberado(usuario, dsSenha)) {
					return true;
				}
				String dsLogin = usuario.dsLogin;
				User user = User.get(dsLogin);
				if (user.getLastTry() > 0) {
					resetByNuMinutosTentativasLogin(user);
				}
				if (user.getNuTentativas() == 1) {
					user.setLastTryTime(new Time());
				}
				if (user.getNuTentativas() >= maxTentativas && !usuario.isBloqueado()) {
					enviaUsuarioBloqueado(usuario);
					validateBloqueado(usuario);
				}
				user.addNuTentativas();
				user.save();
				throw new LoginFailureControlException(FrameworkMessages.LOGIN_MSG_LOG_CREDENCIAIS_INCORRETAS);
			}
		} catch (LoginFailureControlException e) {
			throw e;
		} catch (Exception e) {
			ExceptionUtil.handle(e, "erro ao fazer controle de login");
		} finally {
			UiUtil.closeLoadingScreen();
		}
		return false;
	}
	
	private boolean isLoginLiberado(Usuario usuario, String dsSenha) throws Exception {
		if (controlOnline(usuario, dsSenha)) {
			resetUser(usuario);
			return true;
		} else {
			if (valideResetBloqueioUsuario(usuario)) {
				resetBloqueioUsuario(usuario);
			}
			validateBloqueado(usuario);
		}
		return false;
	}

	private void resetBloqueioUsuario(Usuario usuario) throws Exception {
		Session.setCdUsuario(usuario.cdUsuario);
		LogPdaService.getInstance().warn(LogPda.LOG_CATEGORIA_LOGIN, FrameworkMessages.LOGIN_LOG_WARN_USUARIO_DESBLOQUEADO);
		UsuarioService.getInstance().desbloqueia(usuario);
		UsuarioLavendereService.getInstance().enviaUsuarioControleLoginServidor();
		User.get(usuario).reset();
	}

	private boolean valideResetBloqueioUsuario(Usuario usuario) throws InvalidNumberException {
		final int nuMinutosResetBloqueioUsuario = LavenderePdaConfig.nuMinutosResetBloqueioUsuario;
		if (nuMinutosResetBloqueioUsuario > 0 && usuario.isBloqueado() && usuario.dtBloqueio != null) {
			int diffTempo = TimeUtil.getMinutesRealBetween(new Time(), TimeUtil.toTime(usuario.dtBloqueio + " " + usuario.hrBloqueio));
			int minutosRestante = nuMinutosResetBloqueioUsuario - diffTempo;
			if (minutosRestante > 0) {
				throw new LoginFailureControlException(FrameworkMessages.LOGIN_MSG_INFO_USUARIO_BLOQUEADO_SUPORTE);
			}
			return true;
		}
		return false;
	}

	private void enviaUsuarioBloqueado(Usuario usuario) {
		try {
			Session.setCdUsuario(usuario.cdUsuario);
			UsuarioService.getInstance().bloqueia(usuario);
			UsuarioLavendereService.getInstance().enviaUsuarioControleLoginServidor();
			User.get(usuario).reset();
			SyncManager.processaLoginBloqueadoOnline(usuario.cdUsuario);
			LogPdaService.getInstance().warn(LogPda.LOG_CATEGORIA_LOGIN, FrameworkMessages.LOGIN_LOG_WARN_USUARIO_BLOQUEADO);
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	public void resetUser(final Usuario usuario) throws SQLException {
		final int maxTentativas = LavenderePdaConfig.nuTentativasLogin;
		if (maxTentativas > 0) {
			User user = User.get(usuario.dsLogin);
			user.reset();
		}
	}

	private void resetByNuMinutosTentativasLogin(User user) throws Exception {
		int nuMinutosTentativasLogin = LavenderePdaConfig.nuMinutosTentativasLogin;
		if (TimeUtil.getMinutesRealBetween(new Time(), new Time(user.getLastTry())) > nuMinutosTentativasLogin) {
			user.reset();
		} else {
			user.setLastTryTime(new Time());
			user.save();
		}
	}

	public void validateBloqueado(Usuario usuario) throws InvalidNumberException {
		if (usuario.isBloqueado()) {
			if (LavenderePdaConfig.nuMinutosResetBloqueioUsuario == 0) {
				throw new LoginFailureControlException(FrameworkMessages.LOGIN_MSG_INFO_USUARIO_BLOQUEADO_SUPORTE);
			} else {
				valideResetBloqueioUsuario(usuario);
			}
		}
	}

	private boolean controlOnline(Usuario usuario, String dsSenha) {
		if (LavenderePdaConfig.usaControleOnlineUsuariosInativos) {
			AsyncUIControleSenha asyncUIControleSenha = new AsyncUIControleSenha(usuario, dsSenha);
			asyncUIControleSenha.open();
			if (asyncUIControleSenha.isBloqueadoLogin() && LavenderePdaConfig.nuMinutosResetBloqueioUsuario == 0) {
				throw new LoginFailureControlException(FrameworkMessages.LOGIN_MSG_INFO_USUARIO_BLOQUEADO_SUPORTE);
			}
			return asyncUIControleSenha.isLoginValido();
		}
		return false;
	}
	
}

