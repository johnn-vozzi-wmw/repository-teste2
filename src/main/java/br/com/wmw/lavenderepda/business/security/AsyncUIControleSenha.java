package br.com.wmw.lavenderepda.business.security;

import br.com.wmw.framework.business.domain.Usuario;
import br.com.wmw.framework.business.service.UsuarioService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.sync.async.AsyncUIControl;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.TabelaDb;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import br.com.wmw.lavenderepda.business.domain.dto.RetValidaSenhaDTO;
import br.com.wmw.lavenderepda.business.service.UsuarioLavendereService;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.util.Vector;

public class AsyncUIControleSenha extends AsyncUIControl {
	private final Usuario usuario;
	private final String dsSenha;
	private boolean loginValido;
	private boolean bloqueadoLogin;

	public AsyncUIControleSenha(Usuario usuario, String dsSenha) {
		super(true);
		this.usuario = usuario;
		this.dsSenha = dsSenha;
	}

	@Override
	public void execute() {
		try {
			LogSync.logSection("Início verificação de senha online");
			Session.loadSesssion(usuario);
			UsuarioLavendereService.getInstance().enviaUsuarioControleLoginServidor();
			RetValidaSenhaDTO ret = SyncManager.validaLoginOnline(usuario.cdUsuario, dsSenha);
			if (ret != null) {
				bloqueadoLogin = ret.isBloqueadoLogin();
				receberDados();
				if (ret.isLoginValido()) {
					String dsSenhaBanco = UsuarioService.getInstance().findColumnByRowKey(usuario.rowKey, Usuario.NMCOLUNADSSENHA);
					loginValido = UsuarioService.getInstance().validatePassword(dsSenhaBanco, dsSenhaBanco);
					if (loginValido) {
						LogSync.sucess(FrameworkMessages.LOGIN_LOG_INFO_USUARIO_LOGADO);
					} else {
						LogSync.error(FrameworkMessages.LOGIN_MSG_LOG_CREDENCIAIS_INCORRETAS);
					}
				}
			}
		} catch (Exception e) {
			LogSync.error(e.getMessage());
		} finally {
			LogSync.logSection("Fim Verificando senha.");
		}
	}

	private void receberDados() throws Exception {
		Vector tabelaList = new Vector();
		tabelaList.addElement(new TabelaDb(Usuario.TABLE_NAME));
		tabelaList.addElement(new TabelaDb(ValorParametro.TABLE_NAME));
		Session.loadSesssion(usuario);
		SyncManager.recebeAtualizacaoTabelas(tabelaList);
		LavenderePdaConfig.loadControleLoginUsuario();
	}

	public boolean isLoginValido() {
		return loginValido;
	}

	public void setLoginValido(boolean loginValido) {
		this.loginValido = loginValido;
	}

	public boolean isBloqueadoLogin() {
		return bloqueadoLogin;
	}

	public void setBloqueadoLogin(boolean bloqueadoLogin) {
		this.bloqueadoLogin = bloqueadoLogin;
	}
}
