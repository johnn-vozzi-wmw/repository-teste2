package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoRelacionado;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoRelacionadoDbxDao;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class ProdutoRelacionadoService extends CrudService {

    private static ProdutoRelacionadoService instance;

    private ProdutoRelacionadoService() {
    }

    public static ProdutoRelacionadoService getInstance() {
        if (instance == null) {
            instance = new ProdutoRelacionadoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ProdutoRelacionadoDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

	public void loadProdutosRelacionadosNaoContemplados(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isUsaVendaRelacionada()) {
			ProdutoRelacionado produtoRelacionadoFilter = new ProdutoRelacionado();
			produtoRelacionadoFilter.cdEmpresa = pedido.cdEmpresa;
			produtoRelacionadoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			Vector produtoRelacionadoList = findAllByExample(produtoRelacionadoFilter);
			if (ValueUtil.isNotEmpty(produtoRelacionadoList)) {
				Vector produtosNaoAtendidos = new Vector();
				Hashtable produtoRelacionadoHash = new Hashtable("");
				int size = produtoRelacionadoList.size();
				for (int i = 0; i < size; i++) {
					ProdutoRelacionado produtoRelacionado = (ProdutoRelacionado) produtoRelacionadoList.items[i];
					Vector prodRelacionadoList = (Vector) produtoRelacionadoHash.get(produtoRelacionado.cdProduto);
					if (ValueUtil.isEmpty(prodRelacionadoList)) {
						prodRelacionadoList = new Vector();
					}
					prodRelacionadoList.addElement(produtoRelacionado);
					produtoRelacionadoHash.put(produtoRelacionado.cdProduto, prodRelacionadoList);
				}
				int sizeItensPedido = pedido.itemPedidoList.size();
				for (int i = 0; i < sizeItensPedido; i++) {
					ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
					if (!ValueUtil.VALOR_SIM.equals(itemPedido.flLiberadoVendaRelacionada)) {
						Vector produtoRelacionadoItemList = (Vector) produtoRelacionadoHash.get(itemPedido.getProduto().cdProduto);
						Object[] produtosNaoContemplados = getProdutosNaoContempladosRelacionadosItem(itemPedido, produtoRelacionadoItemList);
						int sizeProdNaoContemplados = ValueUtil.isNotEmpty(produtosNaoContemplados) ? produtosNaoContemplados.length : 0;
						for (int j = 0; j < sizeProdNaoContemplados; j++) {
							Produto produto = (Produto) produtosNaoContemplados[j];
							int index = produtosNaoAtendidos.indexOf(produto);
							if (index != -1) {
								Produto produtoAux = (Produto) produtosNaoAtendidos.items[index];
								if (produto.qtFaltanteProdutoRelacionado > produtoAux.qtFaltanteProdutoRelacionado) {
									produtosNaoAtendidos.removeElement(produtoAux);
									produtosNaoAtendidos.addElement(produto);
								}
							} else {
								produtosNaoAtendidos.addElement(produto);
							}
						}
					}
				}
				pedido.prodRelacionadosNaoContempladosList = produtosNaoAtendidos;
			}
		}
	}


	private Object[] getProdutosNaoContempladosRelacionadosItem(ItemPedido itemPedido, Vector produtoRelacionadoItemList) throws SQLException {
		double qtItemPedidoBaseCalculado = LavenderePdaConfig.validaVendaRelacionadaUnidadeFaturamento && LavenderePdaConfig.usaConversaoUnidadesMedida ? itemPedido.qtItemFaturamento : itemPedido.getQtItemFisico();
		Vector produtosNaoAtendidos = new Vector(0);
		if (ValueUtil.isNotEmpty(produtoRelacionadoItemList)) {
			int sizeProdutoRelacionadoItemList = produtoRelacionadoItemList.size();
			for (int i = 0; i < sizeProdutoRelacionadoItemList; i++) {
				ProdutoRelacionado produtoRelacionado = (ProdutoRelacionado) produtoRelacionadoItemList.items[i];
				//-- verifica se o produto relacionado ja foi vendido no mesmo pedido
				ItemPedido itemPedidoRelacionado = null;
				for (int j = 0; j < itemPedido.pedido.itemPedidoList.size(); j++) {
					ItemPedido itemPedidoTemp = (ItemPedido) itemPedido.pedido.itemPedidoList.items[j];
					if (produtoRelacionado.cdProdutoRelacionado.equals(itemPedidoTemp.cdProduto)) {
						itemPedidoRelacionado = itemPedidoTemp;
						break;
					}
				}
				if (itemPedidoRelacionado != null) {
					double qtItemPedidoRelacionadoBaseCalculado = LavenderePdaConfig.validaVendaRelacionadaUnidadeFaturamento && LavenderePdaConfig.usaConversaoUnidadesMedida ? itemPedidoRelacionado.qtItemFaturamento : itemPedidoRelacionado.getQtItemFisico();
					int qtItemRelacionadoNecessario = ValueUtil.getIntegerValueRoundUp(ValueUtil.round(produtoRelacionado.vlPctVendaMin / 100 * qtItemPedidoBaseCalculado));
					if (qtItemRelacionadoNecessario > qtItemPedidoRelacionadoBaseCalculado) {
						itemPedidoRelacionado.getProduto().qtFaltanteProdutoRelacionado = qtItemRelacionadoNecessario - qtItemPedidoRelacionadoBaseCalculado;
						itemPedidoRelacionado.getProduto().cdProdutoPrincipal = itemPedido.getProduto().cdProduto;
						itemPedidoRelacionado.getProduto().vlPctVendaMin = produtoRelacionado.vlPctVendaMin;
						produtosNaoAtendidos.addElement(itemPedidoRelacionado.getProduto());
					}
				} else {
					Produto produto = ProdutoService.getInstance().getProduto(produtoRelacionado.cdProdutoRelacionado);
					if (!ValueUtil.isEmpty(produto.cdProduto)) {
						produto.cdEmpresa = itemPedido.cdEmpresa;
						produto.cdRepresentante = itemPedido.cdRepresentante;
						produto.qtFaltanteProdutoRelacionado = ValueUtil.getIntegerValueRoundUp(ValueUtil.round(produtoRelacionado.vlPctVendaMin / 100 * qtItemPedidoBaseCalculado));
						produto.cdProdutoPrincipal = itemPedido.getProduto().cdProduto;
						produtosNaoAtendidos.addElement(produto);
					}
				}
			}
		}
		return produtosNaoAtendidos.toObjectArray();
	}

	public Vector getProdutosRelacionados(Produto produtoPrincipal) throws SQLException {
		if (produtoPrincipal != null) {
			ProdutoRelacionado produtoRelacionadoFilter = new ProdutoRelacionado();
			produtoRelacionadoFilter.cdEmpresa = produtoPrincipal.cdEmpresa;
			produtoRelacionadoFilter.cdRepresentante = produtoPrincipal.cdRepresentante;
			produtoRelacionadoFilter.cdProduto = produtoPrincipal.cdProduto;
			return findAllByExample(produtoRelacionadoFilter);
		}
		return null;
	}
}