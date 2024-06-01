package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Logradouro;
import br.com.wmw.lavenderepda.integration.dao.pdbx.LogradouroDbxDao;

public class LogradouroService extends CrudService {

    private static LogradouroService instance;
    
    private LogradouroService() {
        //--
    }
    
    public static LogradouroService getInstance() {
        if (instance == null) {
            instance = new LogradouroService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return LogradouroDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*    
        Logradouro logradouro = (Logradouro) domain;

        //cdLogradouro
        if (ValueUtil.isEmpty(logradouro.cdLogradouro)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.LOGRADOURO_LABEL_CDLOGRADOURO);
        }
        //dsLogradouro
        if (ValueUtil.isEmpty(logradouro.dsLogradouro)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.LOGRADOURO_LABEL_DSLOGRADOURO);
        }
        //dsTipologradouro
        if (ValueUtil.isEmpty(logradouro.dsTipoLogradouro)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.LOGRADOURO_LABEL_DSTIPOLOGRADOURO);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(logradouro.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.LOGRADOURO_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(logradouro.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.LOGRADOURO_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(logradouro.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.LOGRADOURO_LABEL_CDUSUARIO);
        }
*/
    }
    
    public String getDsLogradouro(String cdLogradouro) throws SQLException {
    	if (ValueUtil.isNotEmpty(cdLogradouro)) {
        	Logradouro log = getLogradouro(cdLogradouro);
        	if (log != null && ValueUtil.isNotEmpty(log.dsLogradouro) &&  !"0".equals(log.dsLogradouro.trim())) {
        		return log.dsLogradouro;
        	}
    	}
    	return "";
    }
    
    public String getDsTipoLogradouro(String cdLogradouro) throws SQLException {
    	if (ValueUtil.isNotEmpty(cdLogradouro)) {
    		Logradouro log = getLogradouro(cdLogradouro);
    		if (log != null && ValueUtil.isNotEmpty(log.dsTipoLogradouro) &&  !"0".equals(StringUtil.getStringValue(log.dsLogradouro).trim())) {
    			return log.dsTipoLogradouro;
    		}
    	}
    	return "";
    }

	private Logradouro getLogradouro(String cdLogradouro) throws SQLException {
		Logradouro log = new Logradouro();
		log.cdLogradouro = cdLogradouro;
		return (Logradouro) LogradouroService.getInstance().findByRowKey(log.getRowKey());
	}

}