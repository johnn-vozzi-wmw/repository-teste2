package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.business.domain.TipoAnaliseCliente;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoAnaliseClienteDbxDao;

public class TipoAnaliseClienteService extends CrudService {

    private static TipoAnaliseClienteService instance = null;
    
    private TipoAnaliseClienteService() {
        //--
    }
    
    public static TipoAnaliseClienteService getInstance() {
        if (instance == null) {
            instance = new TipoAnaliseClienteService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return TipoAnaliseClienteDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {
    }

	public String getTipoAnalise(String cdTipoAnalise) throws SQLException {
		TipoAnaliseCliente tipoAnaliseClienteFilter = new TipoAnaliseCliente();
		tipoAnaliseClienteFilter.cdTipoAnalise = cdTipoAnalise;
		tipoAnaliseClienteFilter = (TipoAnaliseCliente) findByPrimaryKey(tipoAnaliseClienteFilter);
		return StringUtil.getStringValue(tipoAnaliseClienteFilter);
	}

}