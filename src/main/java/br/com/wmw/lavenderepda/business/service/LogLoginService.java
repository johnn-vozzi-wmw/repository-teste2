package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.lavenderepda.business.domain.LogLogin;
import br.com.wmw.lavenderepda.integration.dao.pdbx.LogLoginDbxDao;
import totalcross.sys.Settings;

public class LogLoginService extends CrudService {

    private static LogLoginService instance;
    
    private LogLoginService() {
        //--
    }
    
    public static LogLoginService getInstance() {
        if (instance == null) {
            instance = new LogLoginService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return LogLoginDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {   
    	//Nao precisa de validacao
    }

	public void insertLogErroLogin(String cdUsuario, String dsMsgErro) throws SQLException {
		LogLogin logLogin = getLogLogin(cdUsuario);
		logLogin.dsStatus = "Error";
		logLogin.dsMotivo = getMessageErro(dsMsgErro);
		getCrudDao().insert(logLogin);
	}

	private LogLogin getLogLogin(String cdUsuario) {
		LogLogin logLogin = new LogLogin();
		logLogin.dsEquipamento = Settings.deviceId;
		logLogin.cdUsuarioLogin = cdUsuario;
		logLogin.flAmbiente = "S";
		logLogin.dtLogin = DateUtil.getCurrentDate();
		logLogin.hrLogin = TimeUtil.getCurrentTimeHHMMSS();
		logLogin.cdUsuario = cdUsuario;
		logLogin.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_INSERIDO;
		return logLogin;
	}

	private String getMessageErro(String dsErro) {
		if (dsErro != null && dsErro.length() > 4000) {
			return dsErro.substring(0, 4000);
		}
		return dsErro;
	}

	public void insertLogSucessoLogin(String cdUsuario) throws SQLException {
		LogLogin logLogin = getLogLogin(cdUsuario);
		logLogin.dsStatus = "Success";
		getCrudDao().insert(logLogin);
	}
}