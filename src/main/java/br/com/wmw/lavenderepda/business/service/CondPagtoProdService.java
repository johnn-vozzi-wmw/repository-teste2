package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CondPagtoProd;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CondPagtoProdDbxDao;

public class CondPagtoProdService extends CrudService {

    private static CondPagtoProdService instance;
    
    private CondPagtoProdService() {
        //--
    }
    
    public static CondPagtoProdService getInstance() {
        if (instance == null) {
            instance = new CondPagtoProdService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CondPagtoProdDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    	
    }
    
    public double findVlIndiceFinanceiroCondPagtoPorProduto(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaIndiceEspecialCondPagtoProd && pedido != null && itemPedido != null) {
			CondPagtoProd condPagtoProd = new CondPagtoProd();
			condPagtoProd.cdEmpresa = pedido.cdEmpresa;
			condPagtoProd.cdRepresentante = pedido.cdRepresentante;
			condPagtoProd.cdCondicaoPagamento = pedido.cdCondicaoPagamento;
			condPagtoProd.cdProduto = itemPedido.cdProduto;
			condPagtoProd = (CondPagtoProd) findByRowKey(condPagtoProd.getRowKey());
			if (condPagtoProd != null && condPagtoProd.vlIndiceFinanceiro > 0) {
				return condPagtoProd.vlIndiceFinanceiro;
			}
		}
		return 0d;
	}

}