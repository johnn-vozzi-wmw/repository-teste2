package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.BoletoConfig;
import br.com.wmw.lavenderepda.integration.dao.pdbx.BoletoConfigDao;
import totalcross.util.Vector;

public class BoletoConfigService extends CrudService {

    private static BoletoConfigService instance;
    
    private BoletoConfigService() {
        //--
    }
    
    public static BoletoConfigService getInstance() {
        if (instance == null) {
            instance = new BoletoConfigService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return BoletoConfigDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException { 
    	//--
    }
    
    public BoletoConfig getBoletoConfig(final String cdBoletoConfig) throws SQLException {
		BoletoConfig boletoConfigFilter = new BoletoConfig();
		boletoConfigFilter.cdBoletoConfig = cdBoletoConfig;
		BoletoConfig boletoConfig = (BoletoConfig) findByRowKey(boletoConfigFilter.getRowKey());
		if (boletoConfig == null) {
			boletoConfig = new BoletoConfig();
		}
		return boletoConfig;
	}
    
    public String getNuBanco(final String cdBoletoConfig)  {
    	try {
			return getBoletoConfig(cdBoletoConfig).nuBanco;
		} catch (SQLException e) {
			return "";
		}
    }

	public Vector findAllDistinct() throws SQLException {
		return BoletoConfigDao.getInstance().findAllDistinct();
	}

}