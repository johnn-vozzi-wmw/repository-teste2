package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MarcadorClienteDbxDao;

public class MarcadorClienteService extends CrudService {

    private static MarcadorClienteService instance;
    
    private MarcadorClienteService() {
        //--
    }
    
    public static MarcadorClienteService getInstance() {
        if (instance == null) {
            instance = new MarcadorClienteService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return MarcadorClienteDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
    }

	public String findColorLastMarcadorCli(Cliente cliente) throws SQLException {
		return MarcadorClienteDbxDao.getInstance().findColorLastMarcadorCli(cliente);
	}
	
}