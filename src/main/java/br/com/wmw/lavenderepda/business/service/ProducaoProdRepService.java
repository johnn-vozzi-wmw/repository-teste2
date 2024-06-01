package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.ProducaoProd;
import br.com.wmw.lavenderepda.business.domain.ProducaoProdRep;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProducaoProdRepDbxDao;

public class ProducaoProdRepService extends CrudService {

    private static ProducaoProdRepService instance;
    
    private ProducaoProdRepService() {
        //--
    }
    
    public static ProducaoProdRepService getInstance() {
        if (instance == null) {
            instance = new ProducaoProdRepService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ProducaoProdRepDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {
    }

	public ProducaoProdRep findByProducaoProd(ProducaoProd producaoProd, String cdRepresentante) throws SQLException {
		ProducaoProdRep producaoProdRepFilter = new ProducaoProdRep();
		producaoProdRepFilter.cdEmpresa = producaoProd.cdEmpresa;
		producaoProdRepFilter.cdRepresentante = cdRepresentante;
		producaoProdRepFilter.cdProduto = producaoProd.cdProduto;
		producaoProdRepFilter.dtInicial = producaoProd.dtInicial;
		producaoProdRepFilter.dtFinal = producaoProd.dtFinal;
		return (ProducaoProdRep) findByRowKey(producaoProdRepFilter.getRowKey());
	}

}