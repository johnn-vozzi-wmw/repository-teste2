package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ConfigIntegWebTc;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ConfigIntegWebTcDbxDao;
import totalcross.util.Vector;

public class ConfigIntegWebTcService extends CrudService {

    private static ConfigIntegWebTcService instance;

    private ConfigIntegWebTcService() {
    }

    public static ConfigIntegWebTcService getInstance() {
        if (instance == null) {
            instance = new ConfigIntegWebTcService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ConfigIntegWebTcDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
    public Vector findAllRetorno() throws SQLException {
    	ConfigIntegWebTc filter = new ConfigIntegWebTc();
    	filter.flRetorno = ValueUtil.VALOR_SIM;
    	filter.flAtivo = ValueUtil.VALOR_SIM;
    	return findAllByExample(filter);
    }
    
    public Vector findAllRemessa() throws SQLException {
    	ConfigIntegWebTc filter = new ConfigIntegWebTc();
    	filter.flRemessa = ValueUtil.VALOR_SIM;
    	filter.flAtivo = ValueUtil.VALOR_SIM;
    	return findAllByExample(filter);
    }

}