package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.GiroProduto;
import br.com.wmw.lavenderepda.business.domain.GrupoCliPermProd;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoComplemento;
import br.com.wmw.lavenderepda.business.domain.ProdutoCreditoDesc;
import br.com.wmw.lavenderepda.business.domain.ProdutoSimilar;
import br.com.wmw.lavenderepda.business.domain.SugestaoVendaProd;
import br.com.wmw.lavenderepda.integration.dao.pdbx.GrupoCliPermProdDbxDao;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class GrupoCliPermProdService extends CrudService {

    private static GrupoCliPermProdService instance;
    
    private GrupoCliPermProdService() {
        //--
    }
    
    public static GrupoCliPermProdService getInstance() {
        if (instance == null) {
            instance = new GrupoCliPermProdService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return GrupoCliPermProdDbxDao.getInstance();
    }

	@Override
	public void validate(BaseDomain domain) throws java.sql.SQLException {
		//--
	}
	
	public Vector restringeProdutosByGrupoCliPermProd(String cdGrupoCliPermProd, Vector produtoList) throws SQLException {
		Vector produtoFinalList = new Vector();
		if (ValueUtil.isEmpty(produtoList)) {
			return produtoFinalList;
		}
		Hashtable grupoCliPermProdHash = findAllByGrupoCliente(cdGrupoCliPermProd);
		if (ValueUtil.isNotEmpty(grupoCliPermProdHash)) {
			for (int i = 0; i < produtoList.size(); i++) {
				ProdutoBase produtoBase = (ProdutoBase) produtoList.items[i];
				GrupoCliPermProd grupoCliPermProdExample = new GrupoCliPermProd();
				grupoCliPermProdExample.cdGrupoCliente = cdGrupoCliPermProd;
				grupoCliPermProdExample.cdProduto = produtoBase.cdProduto;
				if (grupoCliPermProdHash.get(grupoCliPermProdExample.getRowKey()) != null) {
					produtoFinalList.addElement(produtoBase);
				}
			}
		}
		return produtoFinalList;
	}
	
	public Vector restringeProdutoComplementoByGrupoCliPermProd(Vector produtoComplementoList) throws SQLException {
		Vector produtoComplementoFinalList = new Vector();
		if (ValueUtil.isEmpty(produtoComplementoList)) {
			return produtoComplementoFinalList;
		}
		Hashtable grupoCliPermProdHash = findAllByGrupoCliente(SessionLavenderePda.getCliente().cdGrupoPermProd);
		if (ValueUtil.isNotEmpty(grupoCliPermProdHash)) {
			for (int i = 0; i < produtoComplementoList.size(); i++) {
				ProdutoComplemento produtoComplementoExample = (ProdutoComplemento) produtoComplementoList.items[i];
				GrupoCliPermProd grupoCliPermProdExample = new GrupoCliPermProd();
				grupoCliPermProdExample.cdGrupoCliente = SessionLavenderePda.getCliente().cdGrupoPermProd;
				grupoCliPermProdExample.cdProduto = produtoComplementoExample.cdProdutoComplemento;
				if (grupoCliPermProdHash.get(grupoCliPermProdExample.getRowKey()) != null) {
					produtoComplementoFinalList.addElement(produtoComplementoExample);
				}
			}
		}
		return produtoComplementoFinalList;
	}
	
	public Vector restringeProdutoCreditoDescByGrupoCliPermProd(Vector produtoCreditoDescList) throws SQLException {
		Vector produtoCreditoDescFinalList = new Vector();
		if (ValueUtil.isEmpty(produtoCreditoDescList)) {
			return produtoCreditoDescFinalList;
		}
		Hashtable grupoCliPermProdHash = findAllByGrupoCliente(SessionLavenderePda.getCliente().cdGrupoPermProd);
		if (grupoCliPermProdHash != null) {
			for (int i = 0; i < produtoCreditoDescList.size(); i++) {
				ProdutoCreditoDesc produtoCreditoDesc = (ProdutoCreditoDesc) produtoCreditoDescList.items[i];
				GrupoCliPermProd grupoCliPermProdExample = new GrupoCliPermProd();
				grupoCliPermProdExample.cdGrupoCliente = SessionLavenderePda.getCliente().cdGrupoPermProd;
				grupoCliPermProdExample.cdProduto = produtoCreditoDesc.cdProduto;
				if (grupoCliPermProdHash.get(grupoCliPermProdExample.getRowKey()) != null) {
					produtoCreditoDescFinalList.addElement(produtoCreditoDesc);
				}
			}
		}
		return produtoCreditoDescFinalList;
	}
	
	public Vector restringeSugestaoVendaProdByGrupoCliPermProd(Vector sugestaoVendaProdList) throws SQLException {
		Vector sugestaoVendaProdFinalList = new Vector();
		if (ValueUtil.isEmpty(sugestaoVendaProdList)) {
			return sugestaoVendaProdFinalList;
		}
		Hashtable grupoCliPermProdHash = findAllByGrupoCliente(SessionLavenderePda.getCliente().cdGrupoPermProd);
		if (ValueUtil.isNotEmpty(grupoCliPermProdHash)) {
			for (int i = 0; i < sugestaoVendaProdList.size(); i++) {
				SugestaoVendaProd sugestaoVendaProd = (SugestaoVendaProd) sugestaoVendaProdList.items[i];
				GrupoCliPermProd grupoCliPermProdExample = new GrupoCliPermProd();
				grupoCliPermProdExample.cdGrupoCliente = SessionLavenderePda.getCliente().cdGrupoPermProd;
				grupoCliPermProdExample.cdProduto = sugestaoVendaProd.cdProduto;
				if (grupoCliPermProdHash.get(grupoCliPermProdExample.getRowKey()) != null) {
					sugestaoVendaProdFinalList.addElement(sugestaoVendaProd);
				}
			}
		}
		return sugestaoVendaProdFinalList;
	}
	
	public Vector restringeProdutoSimilarByGrupoCliPermProd(Vector produtoSimilarList) throws SQLException {
		Vector produtoSimilarFinalList = new Vector();
		if (ValueUtil.isEmpty(produtoSimilarList)) {
			return produtoSimilarFinalList;
		}
		Hashtable grupoCliPermProdHash = findAllByGrupoCliente(SessionLavenderePda.getCliente().cdGrupoPermProd);
		if (ValueUtil.isNotEmpty(grupoCliPermProdHash)) {
			for (int i = 0; i < produtoSimilarList.size(); i++) {
				ProdutoSimilar produtoSimilar = (ProdutoSimilar) produtoSimilarList.items[i];
				GrupoCliPermProd grupoCliPermProdExample = new GrupoCliPermProd();
				grupoCliPermProdExample.cdGrupoCliente = SessionLavenderePda.getCliente().cdGrupoPermProd;
				grupoCliPermProdExample.cdProduto = produtoSimilar.cdProdutoSimilar;
				if (grupoCliPermProdHash.get(grupoCliPermProdExample.getRowKey()) != null) {
					produtoSimilarFinalList.addElement(produtoSimilar);
				}
			}
		}
		return produtoSimilarFinalList;
	}
	
	private Hashtable findAllByGrupoCliente(String cdGrupoCliente) throws SQLException {
		if (ValueUtil.isNotEmpty(cdGrupoCliente)) {
			GrupoCliPermProd grupoCliPermProdFilter = new GrupoCliPermProd();
			grupoCliPermProdFilter.cdGrupoCliente = cdGrupoCliente;
			return findAllByExampleInInternalCache(grupoCliPermProdFilter);
		}
		return new Hashtable(1);
	}
	
	public void validatePermissaoGrupoProduto(Pedido pedido, ItemPedido itemPedido) throws SQLException {
    	if (LavenderePdaConfig.filtraProdutoPorGrupoCliente && ValueUtil.isNotEmpty(pedido.getCliente().cdGrupoPermProd)) {
			GrupoCliPermProd grupoCliPermProdFilter = new GrupoCliPermProd();
			grupoCliPermProdFilter.cdProduto = itemPedido.cdProduto;
			grupoCliPermProdFilter.cdGrupoCliente = pedido.getCliente().cdGrupoPermProd;
			if ((GrupoCliPermProd) GrupoCliPermProdService.getInstance().findByRowKey(grupoCliPermProdFilter.getRowKey()) == null) {
				throw new ValidationException(Messages.ITEMPEDIDO_MSG_VALIDACAO_HISTORICO_ITEM); 
			}
		}
	}
	
}