package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.SugestaoVenda;
import br.com.wmw.lavenderepda.business.domain.SugestaoVendaProd;
import br.com.wmw.lavenderepda.integration.dao.pdbx.SugestaoVendaProdDbxDao;
import totalcross.util.Vector;

public class SugestaoVendaProdService extends CrudService {

    private static SugestaoVendaProdService instance;

    private SugestaoVendaProdService() {
        //--
    }

    public static SugestaoVendaProdService getInstance() {
        if (instance == null) {
            instance = new SugestaoVendaProdService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return SugestaoVendaProdDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public Vector findAllSugestaoVendaProdSemQtdPendenteNoPedido(Vector sugestaVendaList, Pedido pedido) throws SQLException {
    	Vector sugestaoVendaProdValidas = new Vector(sugestaVendaList.size());
    	if (ValueUtil.isNotEmpty(sugestaVendaList)) {
    		int size = sugestaVendaList.size();
    		for (int i = 0; i < size; i++) {
    			SugestaoVenda sugestaoVenda = (SugestaoVenda) sugestaVendaList.items[i];
    			Vector sugestaoVendaProdList = findAllSugestaoVendaProdSemQtdPendenteNoPedido(sugestaoVenda, pedido);
    			if (sugestaoVendaProdList.size() > 0) {
    				sugestaoVendaProdValidas.addElements(sugestaoVendaProdList.toObjectArray());
    			}
    		}
    	}
    	return sugestaoVendaProdValidas;
    }

	public boolean isHasSugestaoVendaSemQtdPendenteNoPedido(SugestaoVenda sugestaoVenda, Pedido pedido) throws SQLException {
		return findAllSugestaoVendaProdSemQtdPendenteNoPedido(sugestaoVenda, pedido).size() > 0;
	}

	public Vector findAllSugestaoVendaProdSemQtdPendenteNoPedido(SugestaoVenda sugestaoVenda, Pedido pedido) throws SQLException {
		if (sugestaoVenda == null) {
			return new Vector(0);
		}
		Vector sugestaoProdutosList = findAllSugestaoVendaProdBySugestaoVenda(sugestaoVenda, pedido);
		Vector listFinal = new Vector(0);
		forSugestoes: for (int i = 0; i < sugestaoProdutosList.size(); i++) {
			SugestaoVendaProd sugestaoVendaProd = (SugestaoVendaProd)sugestaoProdutosList.items[i];
			for (int j = 0; j < pedido.itemPedidoList.size(); j++) {
				ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[j];
				if (sugestaoVendaProd.cdProduto.equals(itemPedido.cdProduto) && sugestaoVendaProd.cdEmpresa.equals(itemPedido.cdEmpresa)) {
					continue forSugestoes;
				}
			}
			listFinal.addElement(sugestaoVendaProd);
		}
		return listFinal;
	}

	public Vector findAllSugestaoVendaProdComQtdPendenteNoPedido(SugestaoVenda sugestaoVenda, Pedido pedido) throws SQLException {
		if (sugestaoVenda == null || ValueUtil.isEmpty(sugestaoVenda.cdSugestaoVenda)) {
			return new Vector(0);
		}
		Vector listFinal = new Vector();
		Vector sugestaoProdutosList = findAllSugestaoVendaProdBySugestaoVenda(sugestaoVenda, pedido);
		forSugestoes: for (int i = 0; i < sugestaoProdutosList.size(); i++) {
			SugestaoVendaProd sugestaoVendaProd = (SugestaoVendaProd) sugestaoProdutosList.items[i];
			for (int j = 0; j < pedido.itemPedidoList.size(); j++) {
				ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[j];
				if (sugestaoVendaProd.cdProduto.equals(itemPedido.cdProduto) && sugestaoVendaProd.cdEmpresa.equals(itemPedido.cdEmpresa)) {
					int qtVendida = ValueUtil.getIntegerValue(((ItemPedido) pedido.itemPedidoList.items[j]).getQtItemFisico());
					if (LavenderePdaConfig.ocultaQtItemFisico && LavenderePdaConfig.usaConversaoUnidadesMedida) {
						qtVendida = ValueUtil.getIntegerValue(((ItemPedido) pedido.itemPedidoList.items[j]).qtItemFaturamento);
					}
					int qtVendidaByFrequencia = qtVendida;
					if (sugestaoVendaProd.qtUnidadesVenda <= qtVendidaByFrequencia) {
						continue forSugestoes;
					}
					sugestaoVendaProd.qtVendida += qtVendida;
				}
			}
			listFinal.addElement(sugestaoVendaProd);
		}
		//--
		return listFinal;
	}

    public Vector findAllSugestaoVendaProdBySugestaoVenda(SugestaoVenda sugestaoVenda, Pedido pedido) throws SQLException {
		SugestaoVendaProd sugestaoVendaProd = new SugestaoVendaProd();
		sugestaoVendaProd.cdEmpresa = sugestaoVenda.cdEmpresa;
		sugestaoVendaProd.cdSugestaoVenda = sugestaoVenda.cdSugestaoVenda;
		Vector sugestaoVendaProdList = findAllByExample(sugestaoVendaProd);
		removeProdutosSemPreco(sugestaoVendaProdList, pedido);
		return sugestaoVendaProdList;
    }

    public void removeProdutosSemPreco(Vector produtoList, Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.ignoraProdutoSemPreco() || pedido == null || ValueUtil.isEmpty(produtoList)) return;
		
		int size = produtoList.size();
		for (int i = 0; i < size; i++) {
			Produto produto = null;
			if (produtoList.items[i] instanceof SugestaoVendaProd) {
				SugestaoVendaProd sugestaoVendaProd = (SugestaoVendaProd) produtoList.items[i];
				produto = ProdutoService.getInstance().getProduto(sugestaoVendaProd.cdProduto);
			} else {
				produto = (Produto) produtoList.items[i];
			}
			if (produto == null) continue;
			
			double precoProduto = ItemTabelaPrecoService.getInstance().getPrecoProduto(produto, pedido);
			if (precoProduto > 0) continue;
			
			produtoList.removeElementAt(i);
			size = produtoList.size();
			i--;
		}
	}
    
   
	public Vector findAllSugestaoVendaProdBySugestaoVendaList(Vector sugestaoVendaList) throws SQLException {
    	if (ValueUtil.isEmpty(sugestaoVendaList)) {
    		return new Vector(0);
    	}
		SugestaoVendaProd sugestaoVendaProd = new SugestaoVendaProd();
		sugestaoVendaProd.cdSugestaoVenda = "";
		int size = sugestaoVendaList.size();
		for (int i = 0; i < size; i++) {
			sugestaoVendaProd.cdSugestaoVenda += ((SugestaoVenda) sugestaoVendaList.items[i]).cdSugestaoVenda;
			if (i + 1 < size) {
				sugestaoVendaProd.cdSugestaoVenda += ",";
			} else {
				sugestaoVendaProd.cdEmpresa = ((SugestaoVenda) sugestaoVendaList.items[i]).cdEmpresa;
			}
		}
		return SugestaoVendaProdService.getInstance().findAllByExample(sugestaoVendaProd);
    }
	
	public void adicionaPrecoNaSugestaoVendaProduto(Vector sugestaoVendaProdList, Pedido pedido) throws SQLException {
		int size = sugestaoVendaProdList.size();
		for (int i = 0; i < size; i++) {
			SugestaoVendaProd sugestaoVendaProd = (SugestaoVendaProd) sugestaoVendaProdList.items[i];
			Produto produto = ProdutoService.getInstance().getProduto(sugestaoVendaProd.cdProduto);
			sugestaoVendaProd.produto = produto;
			if (produto == null) return;
			
			sugestaoVendaProd.vlPrecoProduto = ItemTabelaPrecoService.getInstance().getPrecoProduto(produto, pedido);
		}
	}

}