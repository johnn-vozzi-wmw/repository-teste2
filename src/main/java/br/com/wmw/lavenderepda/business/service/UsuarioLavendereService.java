package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.Usuario;
import br.com.wmw.framework.business.service.UsuarioService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.exception.UsuarioBloqueadoException;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.RepresentanteEmp;
import br.com.wmw.lavenderepda.business.domain.UsuarioPdaRep;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import br.com.wmw.lavenderepda.business.security.LoginFailureControl;
import br.com.wmw.lavenderepda.integration.dao.pdbx.UsuarioLavendereDbxDao;
import br.com.wmw.lavenderepda.sync.LavendereTc2Web;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.util.Vector;

public class UsuarioLavendereService extends UsuarioService {
	
	private static UsuarioLavendereService instance;

    private UsuarioLavendereService() {
        super();
    }

    public static UsuarioLavendereService getInstance() {
        if (instance == null) {
            instance = new UsuarioLavendereService();
        }
        return instance;
    }

    protected CrudDao getCrudDao() {
    	return UsuarioLavendereDbxDao.getInstance();
    }
    
    public UsuarioPdaRep getUsuarioPdaRepByCdRep(String cdRep) throws SQLException {
    	return validateLogin(null, cdRep, true);
    }

    public UsuarioPdaRep validateLogin(String dsSenha, String cdRep) throws SQLException {
    	return validateLogin(dsSenha, cdRep, false);
    }
    
    public UsuarioPdaRep validateLogin(String dsSenha, String cdRep, boolean forceSenhaValida) throws SQLException {
		Usuario usuario = getUsuario();
    	String dsSenhaUsuario = "";
    	if (usuario.dsSenha != null) {
    		dsSenhaUsuario = usuario.dsSenha;
    	}
    	//--
    	if (dsSenha == null) {
    		dsSenha = "";
    	}
    	//--
    	boolean senhaValida = validatePassword(dsSenha, dsSenhaUsuario);
    	if (Session.isModoSuporte) {
    		senhaValida = true;
    	}
		if (!senhaValida || usuario.isBloqueado()) {
			LogPdaService.getInstance().warn(LogPda.LOG_CATEGORIA_LOGIN, FrameworkMessages.LOGIN_MSG_LOG_CREDENCIAIS_INCORRETAS);
			if (LoginFailureControl.getInstance().control(usuario, dsSenha)) {
				usuario = getUsuario();
				usuario.envioUsuarioServidorBackGround = true;
				senhaValida = true;
			}
		} else {
			LoginFailureControl.getInstance().resetUser(usuario);
		}
		if (senhaValida) {
			LogPdaService.getInstance().info(LogPda.LOG_CATEGORIA_LOGIN, FrameworkMessages.LOGIN_LOG_INFO_USUARIO_LOGADO);
		}
   		if (senhaValida || forceSenhaValida) {
   			//Senha válida. Verifica se o usuário está relacionado a um representante
   			if (!ValueUtil.isEmpty(cdRep)) {
   				//Sim está relacionado a um representante. Verificar se o cadastro deste representante está presente.
   				Representante representanteFilter = new Representante();
   				representanteFilter.cdRepresentante = cdRep;
   				Representante representante = (Representante)RepresentanteService.getInstance().findByRowKey(representanteFilter.getRowKey());
   				if (representante != null) {
       				//Sim o cadastro do representante existe. Verifica se o representante está relacionado a alguma empresa
	       			RepresentanteEmp representanteEmpFilter = new RepresentanteEmp();
	       			representanteEmpFilter.cdRepresentante = cdRep;
	       			if (!ValueUtil.isEmpty(RepresentanteEmpService.getInstance().findAllByExample(representanteEmpFilter))) {
	       				//Sim o representante está relaciona a alguma empresa. Confirma se o representante é representante ou supervisor então permite acesso
		       			if (representante.isSupervisor() || representante.isRepresentante()) {
		       				//OK - login aceito
		       				UsuarioPdaRep usuarioPdaRep = new UsuarioPdaRep();
		       				usuarioPdaRep.cdUsuario = usuario.cdUsuario;
		       				usuarioPdaRep.cdRepresentante = cdRep;
		       				usuarioPdaRep.representante = representante;
		       				usuarioPdaRep.usuario = usuario;
		       				usuarioPdaRep.usuario.setUsuarioSistema(UsuarioSistemaLavendereService.getInstance().getUsuarioSistemaByUsuario(usuario.cdUsuario));
		       				return usuarioPdaRep;
		       			} else {
		       				throw new ApplicationException(Messages.USUARIO_MSG_LOGIN_REPRESENTANTE_DIFERENTE_SUP_REP);
		       			}
	       			} else {
	   					throw new ApplicationException(Messages.USUARIO_MSG_LOGIN_REPRESENTANTEEMP_INEXISTENTE);
	       			}
       			} else {
   					throw new ApplicationException(Messages.USUARIO_MSG_LOGIN_REPRESENTANTE_INEXISTENTE);
       			}
   			} else {
				throw new ApplicationException(Messages.USUARIO_MSG_LOGIN_REPRESENTANTE_VAZIO);
   			}
   		}
   		return null;
    }

	private Usuario getUsuario() throws SQLException {
		Vector usuarios = findAll();
		if (ValueUtil.isEmpty(usuarios)) {
			throw new ValidationException(Messages.USUARIO_MSG_LOGIN_USUARIOPDA_INEXISTENTE);
		}
		return (Usuario) usuarios.items[0];
	}

	@Override
	public boolean isUsaSenhaDoUsuarioSensivelAoCase() throws SQLException {
    	String usaSenhaDoUsuarioPdaSensivelAoCase = LavenderePdaConfig.getValorParametroPorSistema(ValorParametro.USASENHADOUSUARIOPDASENSIVELAOCASE);
    	try {
			List<String> valorParametroList = Arrays.asList(usaSenhaDoUsuarioPdaSensivelAoCase.split(";"));
			String vlParametro = valorParametroList.get(valorParametroList.size() - 1);
			return ValueUtil.VALOR_SIM.equals(vlParametro);
		} catch (Throwable ex) {
			return false;
		}
    }
    
    public void enviaUsuarioServidor() {
    	try {
			SyncManager.envieDados(HttpConnectionManager.getDefaultParamsSync(), SyncManager.getInfoAtualizacaoUsuario());
		} catch (Throwable e) {
			throw new ValidationException(Messages.SINCRONIZACAO_MSG_ENVIO_INCOMPLETO);
		}
    }
    
    @Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    	super.validate(domain);
    	Usuario usuario = (Usuario) domain;
		if (LavenderePdaConfig.isUsaExpressaoRegularValidacaoSenhaUsuario()) {
			String regex = LavenderePdaConfig.usaExpressaoRegularValidacaoSenhaUsuario;
			boolean senhaValida = false;
			try {
				senhaValida = usuario.dsSenhaNova.matches(regex);
			} catch (Throwable e) {
				senhaValida = true;
			}
			if (!senhaValida) {
				throw new ValidationException(Messages.MSG_SENHA_INVALIDA_EXPRESSAO_REGULAR);
			}
		}
		UsuarioHistAlteracaoLavendereService.getInstance().validaListaRestricaoSenhaUsuario(usuario.dsSenhaNova);
		UsuarioHistAlteracaoLavendereService.getInstance().validaControleRepeticaoSenhaUsuario(usuario.cdUsuario, usuario.dsSenhaNova);
		
    }

    public boolean validaUsuarioAtivo(ParamsSync sync) throws SQLException {
		if (LavenderePdaConfig.usaControleOnlineUsuariosInativos && !Session.isModoSuporte) {
			try {
				if (!isUsuarioAtivo(sync)) {
					LogSync.error(Messages.SISTEMA_MSG_USUARIO_INATIVO);
					throw new UsuarioBloqueadoException(Messages.SISTEMA_MSG_USUARIO_INATIVO);
				}
			} finally {
				ConfigInternoService.getInstance().addValueGeral(ConfigInterno.CONFIG_USUARIO_ATIVO, StringUtil.getStringValue(SessionLavenderePda.isUsuarioAtivo));
			}
		}
		return SessionLavenderePda.isUsuarioAtivo;
    }

	private boolean isUsuarioAtivo(ParamsSync sync) throws SQLException {
		try {
			LavendereWeb2Tc lavendereWeb2Tc = new LavendereWeb2Tc(sync);
			String retorno = lavendereWeb2Tc.validaUsuarioAtivoNoSistema();
			if (ValueUtil.isNotEmpty(retorno)) {
				if (ValueUtil.VALOR_SIM.equalsIgnoreCase(retorno)) {
					SessionLavenderePda.isUsuarioAtivo = true;
				} else if (ValueUtil.VALOR_NAO.equalsIgnoreCase(retorno)) {
					SessionLavenderePda.isUsuarioAtivo = false;
				}
			} else {
				LogSync.error(Messages.MSG_NAO_FOI_POSSIVEL_VALIDAR_USUARIO_SERVIDOR);
			}
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
			LogSync.error(Messages.MSG_NAO_FOI_POSSIVEL_VALIDAR_USUARIO_SERVIDOR);
		}
		return SessionLavenderePda.isUsuarioAtivo;
	}

	public void enviaUsuarioControleLoginServidor() throws Exception {
		LavendereTc2Web pdaToErp = new LavendereTc2Web(HttpConnectionManager.getDefaultParamsSync());
		pdaToErp.conectaEnviaDadosServidor(SyncManager.getInfoAtualizacaoUsuario());
	}
}
