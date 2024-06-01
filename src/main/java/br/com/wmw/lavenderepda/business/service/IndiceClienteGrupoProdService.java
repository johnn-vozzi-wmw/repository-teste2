package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.IndiceClienteGrupoProd;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.IndiceClienteGrupoProdDbxDao;
import totalcross.util.Hashtable;

public class IndiceClienteGrupoProdService extends CrudService {

    private static IndiceClienteGrupoProdService instance;
    
    private IndiceClienteGrupoProdService() {
        //--
    }
    
    public static IndiceClienteGrupoProdService getInstance() {
        if (instance == null) {
            instance = new IndiceClienteGrupoProdService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return IndiceClienteGrupoProdDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
    }

	public double getIndiceByItemPedido(ItemPedido itemPedido) throws SQLException {
		Hashtable indiceCliGrupProdHash = new Hashtable(128);
		IndiceClienteGrupoProd clienteGrupoProdFilter = new IndiceClienteGrupoProd();
		clienteGrupoProdFilter.cdEmpresa = itemPedido.cdEmpresa;
		clienteGrupoProdFilter.cdRepresentante = itemPedido.cdRepresentante;
		clienteGrupoProdFilter.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		indiceCliGrupProdHash = findAllByExampleInInternalCache(clienteGrupoProdFilter);
		if (indiceCliGrupProdHash.size() != 0) {
			//Regra1
			IndiceClienteGrupoProd clienteGrupoProd;
			if (itemPedido.getProduto().cdGrupoProduto1 != null && itemPedido.getProduto().cdGrupoProduto2 != null) {
				clienteGrupoProd = getIndiceClienteGrupoProd(indiceCliGrupProdHash, itemPedido, itemPedido.getProduto().cdGrupoProduto1, itemPedido.getProduto().cdGrupoProduto2, itemPedido.getProduto().cdProduto);
				if (clienteGrupoProd != null) {
					return clienteGrupoProd.vlIndiceFinanceiro;
				}
			}
			//Regra2
			if (itemPedido.getProduto().cdGrupoProduto1 != null && itemPedido.getProduto().cdGrupoProduto2 != null) {
				clienteGrupoProd = getIndiceClienteGrupoProd(indiceCliGrupProdHash, itemPedido, itemPedido.getProduto().cdGrupoProduto1, itemPedido.getProduto().cdGrupoProduto2, IndiceClienteGrupoProd.CD_CHAVE_VAZIO);
				if (clienteGrupoProd != null) {
					return clienteGrupoProd.vlIndiceFinanceiro;
				}
			}
			//Regra3
			if (itemPedido.getProduto().cdGrupoProduto1 != null) {
				clienteGrupoProd = getIndiceClienteGrupoProd(indiceCliGrupProdHash, itemPedido, itemPedido.getProduto().cdGrupoProduto1, IndiceClienteGrupoProd.CD_CHAVE_VAZIO, IndiceClienteGrupoProd.CD_CHAVE_VAZIO);
				if (clienteGrupoProd != null) {
					return clienteGrupoProd.vlIndiceFinanceiro;
				}
			}
			//Regra4
			clienteGrupoProd = getIndiceClienteGrupoProd(indiceCliGrupProdHash, itemPedido, IndiceClienteGrupoProd.CD_CHAVE_VAZIO, IndiceClienteGrupoProd.CD_CHAVE_VAZIO, IndiceClienteGrupoProd.CD_CHAVE_VAZIO);
			if (clienteGrupoProd != null) {
				return clienteGrupoProd.vlIndiceFinanceiro;
			}
		}
		return 1;
	}

	private IndiceClienteGrupoProd getIndiceClienteGrupoProd(Hashtable indiceCliGrupProdHash, ItemPedido itemPedido, String cdGrupoProduto1, String cdGrupoProduto2, String cdProduto) {
		IndiceClienteGrupoProd clienteGrupoProdFilter = new IndiceClienteGrupoProd();
		clienteGrupoProdFilter.cdEmpresa = itemPedido.cdEmpresa;
		clienteGrupoProdFilter.cdRepresentante = itemPedido.cdRepresentante;
		clienteGrupoProdFilter.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		clienteGrupoProdFilter.cdGrupoProduto1 = cdGrupoProduto1;
		clienteGrupoProdFilter.cdGrupoProduto2 = cdGrupoProduto2;
		clienteGrupoProdFilter.cdProduto = cdProduto;
		return (IndiceClienteGrupoProd) indiceCliGrupProdHash.get(clienteGrupoProdFilter.getRowKey());
	}
	
	public void clearCache() {
		if (LavenderePdaConfig.usaIndiceClienteGrupoProd) {
			IndiceClienteGrupoProdDbxDao.getInstance().clearCache();
		}
	}
}