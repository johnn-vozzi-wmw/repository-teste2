package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.PrazoEntrega;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PrazoEntregaPdbxDao;
import totalcross.util.Vector;

public class PrazoEntregaService extends CrudService {

	private static final String DEFAULT_PRAZO_ENTREGA_CDPRODUTO = "0";
	
    private static PrazoEntregaService instance;
    
    private PrazoEntregaService() {
    }
    
    public static PrazoEntregaService getInstance() {
        return (instance == null) ? new PrazoEntregaService() : instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return PrazoEntregaPdbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain prazoEntregaValidate) throws java.sql.SQLException {
    }
    
    public Vector findPrazoEntregaByItemPedido(String cdProduto, double qtItem) throws SQLException {	
    	if(cdProduto == null || qtItem == 0) {
    		return new Vector();
    	}
    	Vector prazoEntrega = findAllByExample(getPrazoEntregaFilter(cdProduto, qtItem, false));
    	if (ValueUtil.isNotEmpty(prazoEntrega)) {
    		return prazoEntrega;
    	}
    	return findAllByExample(getPrazoEntregaFilter(cdProduto, qtItem, true));
	}

	private PrazoEntrega getPrazoEntregaFilter(String cdProduto, double qtItem, boolean isDefaultPrazo) {
		PrazoEntrega prazoEntregafilter = new PrazoEntrega();
    	prazoEntregafilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	prazoEntregafilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	prazoEntregafilter.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
    	prazoEntregafilter.cdProduto = (!isDefaultPrazo) ? cdProduto : DEFAULT_PRAZO_ENTREGA_CDPRODUTO;
    	prazoEntregafilter.qtItem = qtItem;
    	return prazoEntregafilter;
	}
    
}