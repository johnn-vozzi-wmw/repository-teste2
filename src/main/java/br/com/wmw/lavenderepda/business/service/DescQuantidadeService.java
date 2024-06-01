package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescQuantidade;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescQuantidadePdbxDao;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class DescQuantidadeService extends CrudService {

    private static DescQuantidadeService instance;

    private DescQuantidadeService() {
        //--
    }

    public static DescQuantidadeService getInstance() {
        if (instance == null) {
            instance = new DescQuantidadeService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return DescQuantidadePdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public void loadDescQuantidadeItemPedido(ItemPedido itemPedido) throws SQLException {
    	if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
    		ItemTabelaPreco itemTabPreco = itemPedido.getItemTabelaPrecoAtual() != null ? itemPedido.getItemTabelaPrecoAtual() : itemPedido.getItemTabelaPreco();
	    	if (ValueUtil.isNotEmpty(itemTabPreco.descontoQuantidadeList) && itemPedido.permiteAplicarDesconto()) {
		    	int size = itemTabPreco.descontoQuantidadeList.size();
	    		//Pega a menor quantidade cadastrada
			    itemPedido.qtItemPedidoMinimo = ((DescQuantidade)itemTabPreco.descontoQuantidadeList.items[size - 1]).qtItem;
	    		//Verifica qual a faixa de quantidade a ser usada
	    		double qtItemVendido = 0d;
	    		if (!LavenderePdaConfig.isUsaKitBaseadoNoProduto() || !itemPedido.getProduto().isKit()) {
	    			qtItemVendido = itemPedido.getQtItemFisico();
	    		}
	    		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
	    			String cdProdutoDesc = ((DescQuantidade)itemTabPreco.descontoQuantidadeList.items[size - 1]).cdProduto;
					qtItemVendido += DescQtdeAgrSimilarService.getInstance().getSumQtItemFisicoSimilares(itemPedido, cdProdutoDesc);
	    			if (LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
	    				qtItemVendido += ItemKitPedidoService.getInstance().findQtItemKitDescQtdSimilares(itemPedido, cdProdutoDesc);
	    			}
	    		}
	    		DescQuantidade descontoQtd;
	    		boolean descQtdFound = false;
	    		for (int i = 0; i < size; i++) {
	    			descontoQtd = (DescQuantidade)itemTabPreco.descontoQuantidadeList.items[i];
	    			if (qtItemVendido >= descontoQtd.qtItem) {
	    				itemPedido.descQuantidade = descontoQtd;
						if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco && LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex && !itemPedido.pedido.isIgnoraControleVerba()) {
							double vlCalculo = itemPedido.vlBaseItemTabelaPreco;
							if (LavenderePdaConfig.usaUnidadeAlternativa) {
								vlCalculo = itemPedido.vlUnidadePadrao;
							}
							if (LavenderePdaConfig.usaDescontoPorQuantidadeValor) {
								double vlIndiceFinanceiro = 1;
								if (LavenderePdaConfig.isIndiceFinanceiroCondPagtoVlItemPedido() && LavenderePdaConfig.indiceFinanceiroCondPagtoVlBase && !LavenderePdaConfig.isAplicaDescontoCategoria()) {
									vlIndiceFinanceiro = ItemPedidoService.getInstance().getIndiceFinanceiroCondPagtoVlItemPedido(itemPedido.pedido, itemPedido);
									if (vlIndiceFinanceiro == 0) {
										vlIndiceFinanceiro = 1;
									}
								}
								itemTabPreco.vlBase = ValueUtil.round((vlCalculo - itemPedido.descQuantidade.vlDesconto) / vlIndiceFinanceiro) ;
							} else {
								itemTabPreco.vlBase = ValueUtil.round(vlCalculo * (1 - (itemPedido.descQuantidade.vlPctDesconto / 100)));
							}
						}
						itemPedido.vlPctFaixaDescQtd = descontoQtd.vlPctDesconto;
						descQtdFound = true;
	    				break;
	    			}
	    		}
	    		if (!descQtdFound) {
	    			itemPedido.descQuantidade = null;
	    			itemPedido.vlPctFaixaDescQtd = 0;
	    			itemPedido.cancelouDescQtd = true;
	    			if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco && LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex && !itemPedido.pedido.isIgnoraControleVerba()) {
	    				itemPedido.reloadItemTabelaPreco();
	    			}
    			}
	    	} else {
				itemPedido.descQuantidade = null;
				itemPedido.vlPctFaixaDescQtd = 0;
	    	}
    	}
    }

    public Vector getDescontoQuantidadeList(String cdTabelaPreco, String cdProduto) throws SQLException {
    	return getDescontoQuantidadeList(SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, cdTabelaPreco, cdProduto);
    }

    public Vector getDescontoQuantidadeList(String cdEmpresa, String cdRepresentante, String cdTabelaPreco, String cdProduto) throws SQLException {
    	DescQuantidade descontoQuantidadeFilter = new DescQuantidade();
    	descontoQuantidadeFilter.setCdDesconto(cdEmpresa, cdRepresentante, cdTabelaPreco, cdProduto);
    	Vector descQtdList = findAllByCdDesconto(descontoQuantidadeFilter.cdDesconto);
    	Produto produto = ProdutoService.getInstance().getProduto(cdEmpresa, cdRepresentante, cdProduto);
    	if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && !produto.isKit() && ValueUtil.isEmpty(descQtdList)) {
    		descontoQuantidadeFilter.cdEmpresa = cdEmpresa;
    		descontoQuantidadeFilter.cdRepresentante = cdRepresentante;
    		descontoQuantidadeFilter.cdTabelaPreco = cdTabelaPreco;
    		descontoQuantidadeFilter.cdProduto = cdProduto;
    		return DescQuantidadePdbxDao.getInstance().findAllByDescSimilar(descontoQuantidadeFilter);
    	}
    	return descQtdList;
    }

    public Vector findAllByCdDesconto(String cdDesconto) throws SQLException {
    	return ((DescQuantidadePdbxDao)getCrudDao()).findAllByCdDesconto(cdDesconto);
    }

    public Vector getDescontoQuantidadeListTabelaMaiorDesconto(Vector tabelaPrecoList, ItemPedido itemPedido) throws SQLException {
    	double vlPct = -1000;
		//--
		Hashtable tabelaPrecoHash = new Hashtable(tabelaPrecoList.size() * 2);
		int size = tabelaPrecoList.size();
		for (int i = 0; i < size; i++) {
			TabelaPreco tabelaPreco = (TabelaPreco)tabelaPrecoList.items[i];
			tabelaPrecoHash.put(StringUtil.getStringValue(tabelaPreco.cdTabelaPreco), tabelaPreco);
		}
    	//--
		Vector descQtdListMaior = new Vector();
		Vector descQtdList;
		for (int i = 0; i < size; i++) {
			TabelaPreco tabelaPreco = (TabelaPreco)tabelaPrecoList.items[i];

			descQtdList = getDescontoQuantidadeList(itemPedido.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, tabelaPreco.cdTabelaPreco, itemPedido.cdProduto);
//    		descQtdList = (Vector)descQtdHash.get(StringUtil.getStringValue(tabelaPreco.cdTabelaPreco));
    		if (!ValueUtil.isEmpty(descQtdList)) {
    			DescQuantidade descontoQuantidade = (DescQuantidade) descQtdList.items[descQtdList.size() - 1];
    			if (tabelaPreco != null) {
    				if (tabelaPreco.vlPctMaxDescAdicionalItem > descontoQuantidade.vlPctDesconto) {
    					if (vlPct < descontoQuantidade.vlPctDesconto) {
    						vlPct = tabelaPreco.vlPctMaxDescAdicionalItem;
    						itemPedido.cdTabelaPreco = tabelaPreco.cdTabelaPreco;
    						descQtdListMaior = descQtdList;
    					}
    				} else {
    					if (vlPct < descontoQuantidade.vlPctDesconto) {
    						vlPct = descontoQuantidade.vlPctDesconto;
    						itemPedido.cdTabelaPreco = tabelaPreco.cdTabelaPreco;
    						descQtdListMaior = descQtdList;
    					}
    				}
    			}
    		}
    	}
		return descQtdListMaior;
    }

	public Vector calcDescQuantidadeUnidadeAlternativa(Vector descontoQuantidadeList, ItemPedido itemPedido) throws SQLException {
		Vector descQtdeListFinal = new Vector();
		if (descontoQuantidadeList != null) {
			//Se a lista estiver vazia retorna vazio
			if (descontoQuantidadeList.isEmpty()) {
				return descQtdeListFinal;
			}
			ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
			if (LavenderePdaConfig.usaUnidadeAlternativa && produtoUnidade != null) {
				for (int i = 0; i < descontoQuantidadeList.size(); i++) {
					calcDescQuantidadeUnidadeAlternativa((DescQuantidade) descontoQuantidadeList.items[i], produtoUnidade);
				}
				//Mostra apenas a faixa com maior percentual de desconto caso a conversão das faixas por unidade resultem na mesma quantidade
				int size = descontoQuantidadeList.size();
				for (int i = 0; i < size; i++) {
					DescQuantidade descQuantidadeItem = (DescQuantidade) descontoQuantidadeList.items[i];
					boolean inseridoVetorFinal = false;
					int sizeFinal = descQtdeListFinal.size();
					for (int j = 0; j < sizeFinal; j++) {
						DescQuantidade descQuantidadeTemp = (DescQuantidade) descQtdeListFinal.items[j];
						if (descQuantidadeItem.qtItem == descQuantidadeTemp.qtItem) {
							if (descQuantidadeItem.vlPctDesconto > descQuantidadeTemp.vlPctDesconto) {
								descQtdeListFinal.removeElement(descQuantidadeTemp);
							} else {
								inseridoVetorFinal = true;
							}
						}
					}
					if (!inseridoVetorFinal) {
						descQtdeListFinal.addElement(descQuantidadeItem);
					}
				}
			} else {
				descQtdeListFinal = descontoQuantidadeList;
			}
		}
		return descQtdeListFinal;
	}

	protected void calcDescQuantidadeUnidadeAlternativa(DescQuantidade descQuantidade, ProdutoUnidade produtoUnidade) {
		double qtItem;
		double precisaoDecimal;
		if (produtoUnidade.isMultiplica()) {
			qtItem = descQuantidade.qtItem / produtoUnidade.nuConversaoUnidade;
		} else {
			qtItem = descQuantidade.qtItem * produtoUnidade.nuConversaoUnidade;
		}
		descQuantidade.qtItem = ValueUtil.getIntegerValue(ValueUtil.round(qtItem));
		precisaoDecimal = ValueUtil.round(qtItem) - ValueUtil.getIntegerValue(ValueUtil.round(qtItem));
		if (ValueUtil.round(precisaoDecimal) > 0) {
			descQuantidade.qtItem += 1;
		}
	}

	public boolean hasDescontoQuantidade(ItemPedido itemPedido) throws SQLException {
		DescQuantidade descQuantidade = new DescQuantidade();
		descQuantidade.cdEmpresa = itemPedido.cdEmpresa;
		descQuantidade.cdRepresentante = itemPedido.cdRepresentante;
		descQuantidade.cdTabelaPreco = itemPedido.cdTabelaPreco;
		descQuantidade.cdProduto = itemPedido.getProduto().cdProduto;
		return LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido && ValueUtil.isNotEmpty(findAllByExample(descQuantidade));
	}
	
	public String getCdProdutoDescQtd(String cdTabelaPreco, String cdProduto, ItemPedido itemPedido) throws SQLException {
		DescQuantidade descontoQuantidadeFilter = new DescQuantidade();
    	descontoQuantidadeFilter.setCdDesconto(SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, cdTabelaPreco, cdProduto);
    	String cdProdutoDesc = DescQuantidadePdbxDao.getInstance().findCdProdutoCdDesconto(descontoQuantidadeFilter.cdDesconto);
    	if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && ValueUtil.isEmpty(cdProdutoDesc)) {
    		descontoQuantidadeFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		descontoQuantidadeFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    		descontoQuantidadeFilter.cdTabelaPreco = cdTabelaPreco;
    		descontoQuantidadeFilter.cdProduto = cdProduto;
    		cdProdutoDesc = DescQuantidadePdbxDao.getInstance().findCdProdutoDescSimilar(descontoQuantidadeFilter, null);
    	}
    	if (LavenderePdaConfig.isUsaKitBaseadoNoProduto() && ValueUtil.isEmpty(cdProdutoDesc)) {
    		cdProdutoDesc = DescQuantidadePdbxDao.getInstance().findCdProdutoDescSimilar(descontoQuantidadeFilter, itemPedido);
    	}
		return cdProdutoDesc;
	}
	
	public boolean isProdutoPossuiSimilares(String cdTabelaPreco, String cdProduto) throws SQLException {
		DescQuantidade descontoQuantidadeFilter = new DescQuantidade();
    	descontoQuantidadeFilter.setCdDesconto(SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, cdTabelaPreco, cdProduto);
    	Vector descQtdList = findAllByCdDesconto(descontoQuantidadeFilter.cdDesconto);
    	if (ValueUtil.isNotEmpty(descQtdList)) return ((DescQuantidade)descQtdList.items[0]).isFlAgrupadorSimilaridade();
    	descontoQuantidadeFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		descontoQuantidadeFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		descontoQuantidadeFilter.cdTabelaPreco = cdTabelaPreco;
		descontoQuantidadeFilter.cdProduto = cdProduto;
		return ValueUtil.isNotEmpty(DescQuantidadePdbxDao.getInstance().findCdProdutoDescSimilar(descontoQuantidadeFilter, null));
	}


}