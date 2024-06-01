package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RelVendasProdutoPorClientePdbxDao;
import totalcross.util.Vector;

public class RelVendasProdutoPorClienteService extends CrudService {

	private static RelVendasProdutoPorClienteService instance;

	protected CrudDao getCrudDao() {
		return RelVendasProdutoPorClientePdbxDao.getInstance();
	}

	public void validate(BaseDomain domain) throws java.sql.SQLException {
		
	}
	
    public static RelVendasProdutoPorClienteService getInstance() {
        if (instance == null) {
            instance  = new RelVendasProdutoPorClienteService();
        }
        return instance;
    }

	public Vector findAllByExample(BaseDomain domain) throws java.sql.SQLException {
		Vector result = getCrudDao().findAllByExample(domain);
		result.addElements(((RelVendasProdutoPorClientePdbxDao)getCrudDao()).findAllByExampleErp(domain).items);
		return result;
	}

}
