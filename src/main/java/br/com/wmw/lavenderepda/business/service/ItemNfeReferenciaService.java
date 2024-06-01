package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.ItemNfeReferencia;
import br.com.wmw.lavenderepda.business.domain.Nfe;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemNfeReferenciaDao;
import totalcross.util.Vector;

public class ItemNfeReferenciaService extends CrudService {

    private static ItemNfeReferenciaService instance;
    
    private ItemNfeReferenciaService() {
        //--
    }
    
    public static ItemNfeReferenciaService getInstance() {
        if (instance == null) {
            instance = new ItemNfeReferenciaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ItemNfeReferenciaDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    	//--
    }
    
    public Vector getItemNfeReferenciaList(final Nfe nfe) throws SQLException {
    	if (nfe != null) {
			ItemNfeReferencia itemNfeReferenciaFilter = new ItemNfeReferencia();
			itemNfeReferenciaFilter.cdEmpresa = nfe.cdEmpresa;
			itemNfeReferenciaFilter.cdRepresentante = nfe.cdRepresentante;
			itemNfeReferenciaFilter.nuPedido = nfe.nuPedido;
			itemNfeReferenciaFilter.flOrigemPedido = nfe.flOrigemPedido;
			return findAllByExample(itemNfeReferenciaFilter);
    	}
    	return null;
    }
    
}