package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SQLiteDriver;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.SyncInfo;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.ProdutoTabPreco;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemTabelaPrecoPdbxDao;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Date;
import totalcross.util.Hashtable;
import totalcross.util.InvalidDateException;
import totalcross.util.Vector;

public class ItemTabelaPrecoService extends CrudPersonLavendereService {

    private static ItemTabelaPrecoService instance;

    private ItemTabelaPrecoService() {
        //--
    }

    public static ItemTabelaPrecoService getInstance() {
        if (instance == null) {
            instance = new ItemTabelaPrecoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ItemTabelaPrecoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
    public double getPrecoProduto(Produto produto, Pedido pedido) throws SQLException {
    	if (produto == null || ValueUtil.isEmpty(produto.cdProduto)) return 0;
		
	    return getVlVendaProdutoToListaAdicionarItens(pedido, produto, ValueUtil.isNotEmpty(pedido.cdTabelaPreco) ? pedido.cdTabelaPreco : pedido.getCliente().cdTabelaPreco, LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo(), false);
	}

    
    public double getVlVendaProdutoToListaAdicionarItens(Pedido pedido, ProdutoBase produto, String cdTabelaPreco, boolean usaPrecoPorUnidadeQuantidadePrazo, boolean isCalculandoValorComSTMostraGridItemPedido) throws SQLException {
		if (!isCalculandoValorComSTMostraGridItemPedido && (LavenderePdaConfig.realizaCalculoIndicesPrecoProdutoListaAdiconarItensPedido || LavenderePdaConfig.isPermiteEditarValorUnitarioMultiplaInsercao())) {
    		return getVlVendaProdutoToGrid(pedido, produto, cdTabelaPreco, usaPrecoPorUnidadeQuantidadePrazo);
    	} else {
    		double vlBrutoItem = 0;
    		if (produto != null) {
    			if (produto.itemTabelaPreco == null || !ValueUtil.valueEquals(cdTabelaPreco, produto.itemTabelaPreco.cdTabelaPreco)) {
    				produto.itemTabelaPreco = getItemTabelaPreco(cdTabelaPreco, produto.cdProduto, pedido.getCliente().dsUfPreco, produto.cdUnidade, getCdPrazoPagtoPreco(pedido), 0); 
    			}
				if (produto.itemTabelaPreco != null) {
	    			vlBrutoItem = produto.itemTabelaPreco.vlUnitario;
				}
				if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem()) {
					vlBrutoItem = getVlUnitarioTaxaAntecipacao(pedido, produto.itemTabelaPreco, false);
					vlBrutoItem = vlBrutoItem < 0 ? 0 : vlBrutoItem;
				}
				if (LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao()) {
					produto.vlBrutoItem = vlBrutoItem;
				}
				if (!usaPrecoPorUnidadeQuantidadePrazo && LavenderePdaConfig.isPermiteEditarValorUnitarioMultiplaInsercao()) {
					ItemPedido itemPedidoFilter = new ItemPedido();
					itemPedidoFilter.cdEmpresa = pedido.cdEmpresa;
					itemPedidoFilter.cdRepresentante = pedido.cdRepresentante;
					itemPedidoFilter.pedido = pedido;
					itemPedidoFilter.setProdutoBase(produto);
					itemPedidoFilter.cdTabelaPreco = cdTabelaPreco;
					itemPedidoFilter.cdLocal = itemPedidoFilter.getCdLocalEstoque();
					DescPromocionalService.getInstance().loadDescPromocional(itemPedidoFilter, produto);
					if (itemPedidoFilter.descPromocional != null) {
						double vlPctDescontoProduto = itemPedidoFilter.descPromocional.vlPctDescontoProduto;
						if (vlPctDescontoProduto > 0) {
							vlBrutoItem = vlBrutoItem - ((vlPctDescontoProduto / 100) * vlBrutoItem);
							produto.vlBrutoItem = vlBrutoItem;
						}
					}
				}
    		}
    		return vlBrutoItem;
    	}
    }
    
    public double getVlPctComissaoToListaAdicionarItens(Pedido pedido, ProdutoBase produto, String cdTabelaPreco) throws SQLException {
		if (produto.itemTabelaPreco == null) {
			produto.itemTabelaPreco = getItemTabelaPreco(pedido.cdTabelaPreco, produto.cdProduto, pedido.getCliente().dsUfPreco, produto.cdUnidade, pedido.getCondicaoPagamento().cdPrazoPagtoPreco, 0);
		}
  		return produto.itemTabelaPreco.vlPctComissao;
    }    
    
	public double getVlUnitarioTaxaAntecipacao(Pedido pedido, ItemTabelaPreco itemTabelaPreco, boolean isVlBase) {
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoIntegralNoItem()) {
			return itemTabelaPreco.vlUnitario + (itemTabelaPreco.vlUnitario * (DateUtil.getDaysBetween(pedido.dtPagamento, itemTabelaPreco.dtVencimentoPreco) * (itemTabelaPreco.vlPctTaxa/100)));
		} else if(LavenderePdaConfig.isAplicaTaxaAntecipacaoMensalNoItem()) {
			Double vlUnitarioDescontoAcumulado = isVlBase ? itemTabelaPreco.vlBase : itemTabelaPreco.vlUnitario;
			if (itemTabelaPreco.vlPctTaxa != 0) {
				if (itemTabelaPreco.dtVencimentoPreco == null) {
					if (pedido.showMessageDtVencimentoPrecoItemTabelaPreco) {
						pedido.showMessageDtVencimentoPrecoItemTabelaPreco = UiUtil.showConfirmYesNoMessage(MessageUtil.quebraLinhas(Messages.ITEMTABELAPRECO_MSG_SEM_DTVENCIMENTO + " " + Messages.ITEMTABELAPRECO_MSG_CONTINUAR_ALERTA));
					}					
					return ValueUtil.round(vlUnitarioDescontoAcumulado, LavenderePdaConfig.nuCasasDecimais);
				}
				Integer nuDiaPagamento = pedido.dtPagamento.getDay();
				Double vlTaxaAntecipacao = itemTabelaPreco.vlPctTaxa * (pedido.dtPagamento.isBefore(itemTabelaPreco.dtVencimentoPreco)? -1 : 1);
				Integer qtMessesIntervaloData = qtMesesTaxaAntecipacao(DateUtil.getDateValue(pedido.dtPagamento), DateUtil.getDateValue(itemTabelaPreco.dtVencimentoPreco), nuDiaPagamento, false);
				qtMessesIntervaloData *= (qtMessesIntervaloData < 0 ? -1 : 1);
				for (int i = 0; i < qtMessesIntervaloData; i++) {
					vlUnitarioDescontoAcumulado += vlUnitarioDescontoAcumulado * (vlTaxaAntecipacao/100);
				}
				Integer qtdDiasFaltantes = qtMesesTaxaAntecipacao(DateUtil.getDateValue(pedido.dtPagamento), DateUtil.getDateValue(itemTabelaPreco.dtVencimentoPreco), nuDiaPagamento, true);
				qtdDiasFaltantes *= (qtdDiasFaltantes < 0 ? -1 : 1);
				if (qtdDiasFaltantes > 0) {
					vlUnitarioDescontoAcumulado += vlUnitarioDescontoAcumulado * (((vlTaxaAntecipacao/100)/30) * qtdDiasFaltantes);
				}
			}
			return ValueUtil.round(vlUnitarioDescontoAcumulado, LavenderePdaConfig.nuCasasDecimais);
		}
		return itemTabelaPreco.vlUnitario;
	}
	
	public Integer qtMesesTaxaAntecipacao(Date dataInicial, Date dataFinal, Integer nuDiaPagamento, Boolean isDiasRestantes) {
		Integer qtMeses = 0; 
		Date dataAntecipacaoAnterior = DateUtil.getDateValue(dataInicial);
		Date dataMenor;
		Date dataMaior;
		if (dataInicial.isBefore(dataFinal)) {
			dataMenor = DateUtil.getDateValue(dataInicial);
			dataMaior = DateUtil.getDateValue(dataFinal);
		} else {
			dataMenor = DateUtil.getDateValue(dataFinal);
			dataMaior = DateUtil.getDateValue(dataInicial);			
		}
		DateUtil.incrementaUmMes(dataMenor);
		while (dataMenor.isBefore(dataMaior) || dataMenor.equals(dataMaior)) {
			qtMeses += 1;
			dataAntecipacaoAnterior = DateUtil.getDateValue(dataMenor);
			if (nuDiaPagamento > dataMenor.getDaysInMonth()) {
				dataMenor.advance(nuDiaPagamento - dataMenor.getDaysInMonth());
				try {
					dataMenor.set(nuDiaPagamento, dataMenor.getMonth(), dataMenor.getYear());
				} catch (InvalidDateException e) {
					dataMenor.advance(-1);
					ExceptionUtil.handle(e);
				}
			} else {
				DateUtil.incrementaUmMes(dataMenor);
			}
		}		
		if (isDiasRestantes) {
			return DateUtil.getDaysBetween(dataAntecipacaoAnterior, dataMaior);
		}		
		return qtMeses;
	}
	
    public double getVlVendaProdutoToGrid(Pedido pedido, ProdutoBase produto, String cdTabelaPreco, boolean usaPrecoPorUnidadeQuantidadePrazo) throws SQLException {
    	return getItemPedidoComValores(pedido, produto, cdTabelaPreco, usaPrecoPorUnidadeQuantidadePrazo, null, false).vlItemPedido;
    }
	
	public ItemPedido getItemPedidoGradeComValores(Pedido pedido, ProdutoBase produto, String cdTabelaPreco, boolean usaPrecoPorUnidadeQuantidadePrazo, String cdUnidade) throws SQLException {
		return getItemPedidoComValores(pedido, produto, cdTabelaPreco, usaPrecoPorUnidadeQuantidadePrazo, cdUnidade, true);
	}
    
    public ItemPedido getItemPedidoComValores(Pedido pedido, ProdutoBase produto, String cdTabelaPreco, boolean usaPrecoPorUnidadeQuantidadePrazo, String cdUnidade, boolean itemGrade) throws SQLException {
    	ItemPedido itemPedido = new ItemPedido();
    	itemPedido.pedido = pedido;
    	if (produto.itemTabelaPreco == null) {
    		if (usaPrecoPorUnidadeQuantidadePrazo) {
    			itemPedido.setItemTabelaPreco(getItemTabelaPreco(cdTabelaPreco, produto.cdProduto, pedido.getCliente().dsUfPreco, produto.cdUnidade, getCdPrazoPagtoPreco(pedido), 0));
    		} else {
    			itemPedido.setItemTabelaPreco(getItemTabelaPreco(cdTabelaPreco, produto.cdProduto, pedido.getCliente().dsUfPreco));
    		}
    	} else {
    		itemPedido.setItemTabelaPreco(produto.itemTabelaPreco);
    	}
    	itemPedido.cdEmpresa = pedido.cdEmpresa;
    	itemPedido.cdRepresentante = pedido.cdRepresentante;
    	itemPedido.nuPedido = pedido.nuPedido;
    	itemPedido.flOrigemPedido = pedido.flOrigemPedido;
    	itemPedido.cdTabelaPreco = cdTabelaPreco;
    	itemPedido.cdProduto = produto.cdProduto;
    	itemPedido.vlIndiceVolume = produto.vlIndiceVolume;
    	itemPedido.flTipoItemPedido = pedido.isPedidoBonificacao() ? TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO : TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
    	loadCdUnidadeItem(pedido, produto, cdUnidade, itemPedido);
	    if (LavenderePdaConfig.showInformacoesComplementaresInsercMultipla() && ValueUtil.isNotEmpty(produto.produtoUnidadeList)) {
    		itemPedido.cdUnidade = ((ProdutoUnidade)produto.produtoUnidadeList.items[0]).cdUnidade;
    	}
	    //Constroi produto manualmente para não buscar no banco durante a busca pelo preço (itemPedido.getProduto())
	    Produto produtoNew = new Produto();
	    produtoNew.cdEmpresa = produto.cdEmpresa;
	    produtoNew.cdRepresentante = produto.cdRepresentante;
	    produtoNew.cdProduto = produto.cdProduto;
	    produtoNew.cdUnidade = produto.cdUnidade;
	    produtoNew.cdGrupoDestaque = produto.cdGrupoDestaque;
	    produtoNew.cdGrupoDescProd = produto.cdGrupoDescProd;
	    produtoNew.cdFamiliaDescProg = produto.cdFamiliaDescProg;
	    produtoNew.setDsAgrupadorGrade(produto.getDsAgrupadorGrade());
	    produtoNew.dsProduto = produto.dsProduto;
	    produtoNew.cdLinha = produto.cdLinha;
	    produtoNew.cdClasse = produto.cdClasse;
	    produtoNew.cdGrupoProduto1 = produto.cdGrupoProduto1;
	    produtoNew.cdConjunto = produto.cdConjunto;
	    produtoNew.cdMarca = produto.cdMarca;
	    produtoNew.cdColecao = produto.cdColecao;
	    produtoNew.cdStatusColecao = produto.cdStatusColecao;
	    produtoNew.cdFamiliaPadrao = produto.cdFamiliaPadrao;
	    produtoNew.dsReferencia = produto.dsReferencia;
	    produtoNew.flVendaEncerrada = produto.flVendaEncerrada;
	    produtoNew.cdMarcadores = produto.cdMarcadores;
	    produtoNew.marcadores = produto.marcadores;
	    produtoNew.nuMultiploEspecial = produto.nuMultiploEspecial;
	    if (LavenderePdaConfig.usaIndiceGrupoProdutoTabPrecoCondPagto) {
		    produtoNew.cdGrupoProduto1 = produto.cdGrupoProduto1;
		    produtoNew.cdGrupoProduto2 = produto.cdGrupoProduto2;
		    produtoNew.cdGrupoProduto3 = produto.cdGrupoProduto3;
		    produtoNew.cdGrupoProduto4 = produto.cdGrupoProduto4;
		    produtoNew.cdGrupoProduto5 = produto.cdGrupoProduto5;
	    }
	    if (LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto) {
		    produtoNew.nuFracao = produto.nuFracao;
		    produtoNew.cdUnidadeFracao = produto.cdUnidadeFracao;
		    produtoNew.flUsaUnidadeBaseDsFracao = produto.flUsaUnidadeBaseDsFracao;
	    }
	    if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorItensFormulaJuros()) {
		    produtoNew.flFormulaCalculoDeflator = produto.flFormulaCalculoDeflator;
		    produtoNew.vlPctJurosMensal = produto.vlPctJurosMensal;
		    produtoNew.qtDiasCarenciaJuros = produto.qtDiasCarenciaJuros;
	    }
	    itemPedido.setProduto(produtoNew);
    	itemPedido.setProdutoBase(produto);
		if (itemGrade) {
			ItemPedidoService.getInstance().loadPoliticaComercial(itemPedido, itemPedido.pedido);
		}
    	PedidoService.getInstance().loadValorBaseItemPedido(pedido, itemPedido);
    	if (LavenderePdaConfig.showInformacoesComplementaresInsercMultipla() && itemPedido.getProdutoBase() != null) {
    		produto.qtEmbalagemElementar = itemPedido.qtEmbalagemElementar;
    		produto.vlEmbalagemElementar = itemPedido.vlEmbalagemElementar;
    		produto.vlBaseFlex = itemPedido.getItemTabelaPreco().getVlBaseFlex(pedido, itemPedido);
		}
    	if (LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao()) {
    		produto.vlBrutoItem = itemPedido.vlManualBrutoItem;
    	}
    	return itemPedido;
    }

	public void loadCdUnidadeItem(Pedido pedido, ProdutoBase produto, String cdUnidade, ItemPedido itemPedido) {
		if (cdUnidade == null) {
    		itemPedido.cdUnidade = LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido() ? pedido.cdUnidade : produto.cdUnidade;
    	} else {
    		itemPedido.cdUnidade = cdUnidade;
    	}
	}

	private int getCdPrazoPagtoPreco(Pedido pedido) throws SQLException {
		if (ValueUtil.isEmpty(pedido.cdCondicaoPagamento)) return 0;
		
		CondicaoPagamento condicaoPagamentoFilter = new CondicaoPagamento();
		condicaoPagamentoFilter.cdCondicaoPagamento = pedido.cdCondicaoPagamento;
		condicaoPagamentoFilter.cdEmpresa = pedido.cdEmpresa;
		condicaoPagamentoFilter.cdRepresentante = pedido.cdRepresentante;
		CondicaoPagamento condicaoPagamento = (CondicaoPagamento) CondicaoPagamentoService.getInstance().findByRowKey(condicaoPagamentoFilter.getRowKey());
		int cdPrazoPagtoPreco = condicaoPagamento == null ? 0 : ValueUtil.getIntegerValue(condicaoPagamento.cdPrazoPagtoPreco);
		return cdPrazoPagtoPreco;
	}

	public ItemTabelaPreco getTabelaPrecoFilter(String cdTabelaPreco, String cdProduto, String cdUf, String cdUnidade, int cdPrazoPagtoPreco, int qtItem) {
		ItemTabelaPreco itemTabelaPrecoFilter = new ItemTabelaPreco();
    	itemTabelaPrecoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	itemTabelaPrecoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	itemTabelaPrecoFilter.cdTabelaPreco = cdTabelaPreco;
    	itemTabelaPrecoFilter.cdProduto = cdProduto;
    	itemTabelaPrecoFilter.cdUf = "0";
    	if (LavenderePdaConfig.usaPrecoPorUf) {
    		itemTabelaPrecoFilter.cdUf = cdUf;
    	}
		itemTabelaPrecoFilter.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		itemTabelaPrecoFilter.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		itemTabelaPrecoFilter.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		itemTabelaPrecoFilter.cdUnidade = ValueUtil.isNotEmpty(cdUnidade) ? cdUnidade : ItemTabelaPreco.CDUNIDADE_VALOR_PADRAO;
		itemTabelaPrecoFilter.qtItem = qtItem;
		itemTabelaPrecoFilter.cdPrazoPagtoPreco = cdPrazoPagtoPreco;
		return itemTabelaPrecoFilter;
	}
	
	public ItemTabelaPreco getTabelaPrecoFilterListaProdutos(String cdTabelaPreco, String cdUf, int cdPrazoPagtoPreco, int qtItem) {
		ItemTabelaPreco itemTabelaPrecoFilter = new ItemTabelaPreco();
		itemTabelaPrecoFilter.cdTabelaPreco = cdTabelaPreco;
		itemTabelaPrecoFilter.cdUf = LavenderePdaConfig.usaPrecoPorUf ? cdUf : ItemTabelaPreco.CDUF_VALOR_PADRAO;
		itemTabelaPrecoFilter.cdPrazoPagtoPreco = cdPrazoPagtoPreco;
		itemTabelaPrecoFilter.qtItem = qtItem;
		return itemTabelaPrecoFilter;
	}

    public ItemTabelaPreco getItemTabelaPreco(String cdTabelaPreco, String cdProduto, String cdUf) throws SQLException {
    	return getItemTabelaPrecoGradeNivel1(cdTabelaPreco, cdProduto, cdUf, ProdutoGrade.CD_ITEM_GRADE_PADRAO);
    }
    
    public ItemTabelaPreco getItemTabelaPrecoPrazoPagtoPreco(String cdTabelaPreco, String cdProduto, String cdUf, int cdPrazoPagtoPreco) throws SQLException {
    	return getItemTabelaPreco(cdTabelaPreco, cdProduto, cdUf, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ItemTabelaPreco.CDUNIDADE_VALOR_PADRAO, ItemTabelaPreco.QTITEM_VALOR_PADRAO, cdPrazoPagtoPreco);
    }

    public ItemTabelaPreco getItemTabelaPrecoGradeNivel1(String cdTabelaPreco, String cdProduto, String cdUf, String cdItemGrade1) throws SQLException {
    	if (cdItemGrade1 == null) {
    		cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
    	}
    	return getItemTabelaPrecoComGrade(cdTabelaPreco, cdProduto, cdUf, cdItemGrade1, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO);
    }

    public ItemTabelaPreco getItemTabelaPrecoGradeNivel1UnidadePrazo(String cdTabelaPreco, String cdProduto, String cdUf, String cdItemGrade1, String cdUnidade, int cdPrazoPagtoPreco) throws SQLException {
    	if (cdItemGrade1 == null) {
    		cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
    	}
    	if (ValueUtil.isEmpty(cdUnidade)) {
    		cdUnidade = ItemTabelaPreco.CDUNIDADE_VALOR_PADRAO;
    	}
    	return getItemTabelaPreco(cdTabelaPreco, cdProduto, cdUf, cdItemGrade1, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, cdUnidade, ItemTabelaPreco.QTITEM_VALOR_PADRAO, cdPrazoPagtoPreco);
    }

    public ItemTabelaPreco getItemTabelaPrecoComGrade(String cdTabelaPreco, String cdProduto, String cdUf, String cdItemGrade1, String cdItemGrade2, String cdItemGrade3) throws SQLException {
    	return getItemTabelaPreco(cdTabelaPreco, cdProduto, cdUf, cdItemGrade1, cdItemGrade2, cdItemGrade3, ItemTabelaPreco.CDUNIDADE_VALOR_PADRAO, ItemTabelaPreco.QTITEM_VALOR_PADRAO, ItemTabelaPreco.CDPRAZOPAGTOPRECO_VALOR_PADRAO);
    }
    
    public ItemTabelaPreco getItemTabelaPrecoComGradeUnidade(String cdTabelaPreco, String cdProduto, String cdUf, String cdItemGrade1, String cdItemGrade2, String cdItemGrade3, String cdUnidade) throws SQLException {
    	return getItemTabelaPreco(cdTabelaPreco, cdProduto, cdUf, cdItemGrade1, cdItemGrade2, cdItemGrade3, cdUnidade, ItemTabelaPreco.QTITEM_VALOR_PADRAO, ItemTabelaPreco.CDPRAZOPAGTOPRECO_VALOR_PADRAO);
    }
    
    public double getQtMaxVendaLista(Pedido pedido, ProdutoBase produto, String cdTabelaPreco) throws SQLException {
    	ItemTabelaPreco itemTabelaPreco = getItemTabelaPreco(cdTabelaPreco, produto.cdProduto, pedido.getCliente().dsUfPreco); 
		return itemTabelaPreco != null ? itemTabelaPreco.qtMaxVenda : 0;
    }
    
	public ItemTabelaPreco getItemTabelaPreco(String cdTabelaPreco, String cdProduto, String cdUf, String cdUnidade, int cdPrazoPagtoPreco, int qtItem) throws SQLException {
    	ItemTabelaPreco itemTabelaPreco = findItemTabelaPreco(getTabelaPrecoFilter(cdTabelaPreco, cdProduto, cdUf, cdUnidade, cdPrazoPagtoPreco, qtItem));
		double vlUnitarioCondComercialExcec = itemTabelaPreco.condComercialExcec.vlUnitario;
		if (vlUnitarioCondComercialExcec > 0) {
			itemTabelaPreco.vlUnitario = vlUnitarioCondComercialExcec;
		}
    	return itemTabelaPreco;
    }

	private ItemTabelaPreco findItemTabelaPreco(ItemTabelaPreco itemTabelaPrecoFilter) throws SQLException {
		ItemTabelaPreco itemTabelaPreco = (ItemTabelaPreco) findByPrimaryKey(itemTabelaPrecoFilter);
    	if (itemTabelaPreco == null && !ValueUtil.valueEquals(ItemTabelaPreco.CDUNIDADE_VALOR_PADRAO, itemTabelaPrecoFilter.cdUnidade)) {
    		itemTabelaPreco = (ItemTabelaPreco) findByPrimaryKey(getTabelaPrecoFilter(itemTabelaPrecoFilter.cdTabelaPreco, itemTabelaPrecoFilter.cdProduto, itemTabelaPrecoFilter.cdUf, ItemTabelaPreco.CDUNIDADE_VALOR_PADRAO, itemTabelaPrecoFilter.cdPrazoPagtoPreco, itemTabelaPrecoFilter.qtItem));
    	}
		return (itemTabelaPreco != null) ? itemTabelaPreco : new ItemTabelaPreco();
	}

   	public ItemTabelaPreco getItemTabelaPreco(String cdTabelaPreco, String cdProduto, String cdUf, String cdItemGrade1, String cdItemGrade2, String cdItemGrade3, String cdUnidade, int qtItem, int cdPrazoPagtoPreco) throws SQLException {
		ItemTabelaPreco itemTabelaPrecoFilter = new ItemTabelaPreco();
		itemTabelaPrecoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		itemTabelaPrecoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		itemTabelaPrecoFilter.cdTabelaPreco = cdTabelaPreco;
		if (LavenderePdaConfig.usaPrecoPorUf) {
			itemTabelaPrecoFilter.cdUf = cdUf;
		}
		itemTabelaPrecoFilter.cdProduto = cdProduto;
		if (LavenderePdaConfig.usaGradeProduto2()) {
			itemTabelaPrecoFilter.cdItemGrade1 = ItemTabelaPreco.CDUNIDADE_VALOR_PADRAO;
			itemTabelaPrecoFilter.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
			itemTabelaPrecoFilter.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		} else if (LavenderePdaConfig.usaGradeProduto4() || LavenderePdaConfig.usaGradeProduto5()) {
			itemTabelaPrecoFilter.cdItemGrade1 = cdItemGrade1;
			itemTabelaPrecoFilter.cdItemGrade2 = cdItemGrade2;
			itemTabelaPrecoFilter.cdItemGrade3 = cdItemGrade3;
		} else {
			if (ValueUtil.isEmpty(cdItemGrade1)) return new ItemTabelaPreco();
			
			itemTabelaPrecoFilter.cdItemGrade1 = cdItemGrade1;
			itemTabelaPrecoFilter.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
			itemTabelaPrecoFilter.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		}
		itemTabelaPrecoFilter.cdUnidade = cdUnidade;
		itemTabelaPrecoFilter.cdPrazoPagtoPreco = cdPrazoPagtoPreco;
		if (qtItem == 0) {
			ItemTabelaPreco itemTabelaPreco = new ItemTabelaPreco();
			if (itemTabelaPrecoFilter.cdEmpresa != null && itemTabelaPrecoFilter.cdRepresentante != null && itemTabelaPrecoFilter.cdTabelaPreco != null
				&& itemTabelaPrecoFilter.cdProduto != null && itemTabelaPrecoFilter.cdItemGrade1 != null && itemTabelaPrecoFilter.cdItemGrade2 != null 
				&& itemTabelaPrecoFilter.cdItemGrade3 != null && itemTabelaPrecoFilter.cdUnidade != null) {
					itemTabelaPreco = (ItemTabelaPreco)findByRowKey(itemTabelaPrecoFilter.getRowKey());
			}
			itemTabelaPrecoFilter.qtItem = qtItem;
			itemTabelaPrecoFilter = null;
			return itemTabelaPreco != null ? itemTabelaPreco : new ItemTabelaPreco();		
		} else {
			Vector itemTabPrecosList = findAllByExample(itemTabelaPrecoFilter);
 			ItemTabelaPreco itemTabelaPreco = new ItemTabelaPreco();
			for (int i = 0; i < itemTabPrecosList.size(); i++) {
				ItemTabelaPreco itemTabPrecoTemp = (ItemTabelaPreco)itemTabPrecosList.items[i];
				if ((itemTabPrecoTemp.qtItem <= qtItem) && (itemTabPrecoTemp.qtItem >= itemTabelaPreco.qtItem)) {
					itemTabelaPreco = itemTabPrecoTemp;
				}
			}
			itemTabelaPreco.qtItemFisico = qtItem;
			return itemTabelaPreco;
		}
    }

    public boolean isTabelaPrecoPromocional(String  cdProduto, String cdTabelaPreco) throws SQLException {
    	ProdutoTabPreco produtoTabPreco = ProdutoTabPrecoService.getInstance().getProdutoTabPreco(cdProduto);
		if (ValueUtil.isNotEmpty(produtoTabPreco.dsTabPrecoPromoList)) {
			String[] tabPrecoPromoList = StringUtil.split(produtoTabPreco.dsTabPrecoPromoList, '|');
			for (int i = 0; i < tabPrecoPromoList.length; i++) {
				if (tabPrecoPromoList[i].equalsIgnoreCase(cdTabelaPreco)) {
					return  true;
				}
			}
		}
		return false;
    }

    public boolean isItemComPrecoEmQueda(String cdTabelaPreco, String cdProduto, String cdUf) throws SQLException {
    	ItemTabelaPreco itemTabelaPreco = getItemTabelaPreco(cdTabelaPreco, cdProduto, cdUf);
    	return ValueUtil.VALOR_SIM.equals(itemTabelaPreco.flPrecoQueda);
    }
    
    public Vector getItemSugestaoRentabilidadeIdealList(final Pedido pedido) throws SQLException {
    	Vector itemSugestaoRentabilidadeIdealFinalList = new Vector();
    	if (pedido != null && !pedido.isPedidoBonificacao()) {
    		ItemTabelaPreco itemTabelaPrecoFilter = new ItemTabelaPreco();
    		itemTabelaPrecoFilter.cdEmpresa = pedido.cdEmpresa;
    		itemTabelaPrecoFilter.cdRepresentante = pedido.cdRepresentante;
    		if (LavenderePdaConfig.isUsaTabelaPrecoPedido()) {
    			itemTabelaPrecoFilter.cdTabelaPreco = pedido.cdTabelaPreco;
    		} else {
    			itemTabelaPrecoFilter.cdTabelaPreco = pedido.getCliente().cdTabelaPreco;
    		}
    		if (LavenderePdaConfig.usaPrecoPorUf) {
    			itemTabelaPrecoFilter.cdUf = pedido.getCliente().dsUfPreco;
    		}
    		itemTabelaPrecoFilter.sortAtributte = ItemTabelaPreco.NOMECOLUNA_VLPCTPREVISAORENTABILIDADE;
    		itemTabelaPrecoFilter.sortAsc = ValueUtil.VALOR_NAO;
    		Vector itemSugestaoRentabilidadeIdealList = findAllByExample(itemTabelaPrecoFilter);
    		itemSugestaoRentabilidadeIdealList = ProdutoService.getInstance().findProdutosPorTipoPedido(pedido.getTipoPedido(), itemSugestaoRentabilidadeIdealList);
    		if (ValueUtil.isNotEmpty(itemSugestaoRentabilidadeIdealList)) {
    			double vlPctRentabilidadeIdeal = RentabilidadeFaixaService.getInstance().getVlPctRentabilidadeFaixaIdeal(pedido.getRentabilidadeFaixaList());
    			if (vlPctRentabilidadeIdeal > 0) {
    				int qtItensTabPreco = itemSugestaoRentabilidadeIdealList.size();
    				int qtdMaxItens = qtItensTabPreco < LavenderePdaConfig.qtdItensRentabilidadeIdealSugeridos ? qtItensTabPreco : LavenderePdaConfig.qtdItensRentabilidadeIdealSugeridos;
    				for (int i = 0; i < qtdMaxItens; i++) {
						ItemTabelaPreco itemTabelaPreco = (ItemTabelaPreco) itemSugestaoRentabilidadeIdealList.items[i];
						if (itemTabelaPreco.vlPctPrevisaRentabilidade >= vlPctRentabilidadeIdeal) {
							itemSugestaoRentabilidadeIdealFinalList.addElement(itemTabelaPreco);
						}
					}
    			}
    		}
    	}
    	return itemSugestaoRentabilidadeIdealFinalList;
    }

    public boolean isRentabalidadeMaiorEspBaseadoItemTabelaPreco(ItemPedido itemPedido) throws SQLException {
    	return itemPedido.getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false) >= itemPedido.getItemTabelaPreco().vlPctRentabilidadeEsp;
    }
    
    public boolean isRentabalidadeMenorMinBaseadoItemTabelaPreco(ItemPedido itemPedido) throws SQLException {
    	return itemPedido.getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false) < itemPedido.getItemTabelaPreco().vlPctRentabilidadeMin;
    }
    
    public Image getIconRentabilidadeItem(final ItemPedido itemPedido) throws SQLException {
		int corFaixa = getCorRentabilidade(itemPedido);
		return UiUtil.getIconButtonAction("images/rentabilidade.png", corFaixa);
	}
    
    public int getCorRentabilidade(final ItemPedido itemPedido) throws SQLException {
    	if (isRentabalidadeMaiorEspBaseadoItemTabelaPreco(itemPedido)) {
    		return Color.getRGB(30, 210, 0);
    	}
    	if (isRentabalidadeMenorMinBaseadoItemTabelaPreco(itemPedido)) {
    		return Color.getRGB(220, 20, 30);
    	}
    	return Color.getRGB(255, 180, 0);
    }
    
    public int getCorRentabilidadeLista(final ItemPedido itemPedido) throws SQLException {
    	if (isRentabalidadeMaiorEspBaseadoItemTabelaPreco(itemPedido)) {
    		return LavendereColorUtil.formsBackColor;
    	}
    	if (isRentabalidadeMenorMinBaseadoItemTabelaPreco(itemPedido)) {
    		return Color.getRGB(255, 200, 200);
    	}
    	return Color.getRGB(255, 255, 200);
    	
    }
    
    public boolean isAlgumItemRentabilidadeAbaixoEsperado(Pedido pedido) throws SQLException {
    	Vector itemPedidoList = pedido.itemPedidoList;
    	for (int i = 0; i < itemPedidoList.size(); i++) {
    		ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
    		if (itemPedido.getItemTabelaPreco().vlPctRentabilidadeEsp >= 0 && !isRentabalidadeMaiorEspBaseadoItemTabelaPreco(itemPedido)) {
    			return true;
    		}
    	}
    	return false;
    }

    public boolean isAlgumItemRentabilidadeAbaixoMinimo(Pedido pedido) throws SQLException {
    	Vector itemPedidoList = pedido.itemPedidoList;
    	for (int i = 0; i < itemPedidoList.size(); i++) {
    		ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
    		if (isRentabalidadeMenorMinBaseadoItemTabelaPreco(itemPedido)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public int countItemTabelaPrecoQualquerGrade(String cdProduto, String[] cdsTabelaPreco) throws SQLException {
    	return ItemTabelaPrecoPdbxDao.getInstance().countItemTabelaPrecoQualquerGrade(cdProduto, cdsTabelaPreco);
    }
    
	public String findCdTabelaPrecoComMaiorDescontoPromocional(String[] cdsTabelasPreco, String cdProduto) throws SQLException {
		ItemTabelaPreco itemTabelaPreco = ItemTabelaPrecoPdbxDao.getInstance().findTabelaPrecoComMaiorDescontoPromocional(cdsTabelasPreco,  cdProduto);
		return itemTabelaPreco.cdTabelaPreco;
	}
	
	public double findVlTabelaPrecoComMaiorDescontoPromocional(String[] cdsTabelasPreco, String cdProduto) throws SQLException {
		ItemTabelaPreco itemTabelaPreco = ItemTabelaPrecoPdbxDao.getInstance().findTabelaPrecoComMaiorDescontoPromocional(cdsTabelasPreco,  cdProduto);
		return itemTabelaPreco.vlPctMaxDesconto;
	}

	public double aplicaIndiceFinanceiroSupRep(double vlBaseCalculo) throws SQLException {
		double vlIndiceFinanceiro;
		if (SessionLavenderePda.getRepresentante().vlIndiceFinanceiro >= 0) {
			vlIndiceFinanceiro = SessionLavenderePda.getRepresentante().vlIndiceFinanceiro;
		} else {
			vlIndiceFinanceiro = ValueUtil.getDoubleSimpleValue(RepresentanteService.getInstance().findColumnByRowKey(SessionLavenderePda.getRepresentante().getRowKey(), Representante.NMCOLUNA_VLINDICEFINANCEIRO));
			SessionLavenderePda.getRepresentante().vlIndiceFinanceiro = vlIndiceFinanceiro;
		}
		vlIndiceFinanceiro = vlIndiceFinanceiro > 0 ? vlIndiceFinanceiro : 1;
		vlBaseCalculo *= vlIndiceFinanceiro;
		return vlBaseCalculo;
	}
	

	public boolean hasItemTabelaPrecoZero() throws SQLException {
		return ItemTabelaPrecoPdbxDao.getInstance().hasItemTabelaPrecoZero();
	}

	public void processaItemTabPrecoZero(String columns, Hashtable infoList) throws SQLException {
		SQLiteDriver driver = null;
		try {
			driver = CrudDbxDao.getCurrentDriver();
			driver.startTransaction();
			String[] columnList = columns.split(",");
			StringBuilder sqlInsert = new StringBuilder();
			sqlInsert.append("INSERT OR REPLACE INTO ").append(ItemTabelaPreco.TABLE_NAME).append(" (").append(columns)
			.append(") SELECT ");
			int size = columnList.length;
			for (int i = 0; i < size; i++) {
				String column = columnList[i].trim();
				if (ItemTabelaPreco.NOMECOLUNA_CDTABELAPRECO.equalsIgnoreCase(column)) {
					sqlInsert.append("TAB.").append(column);
				} else if (ItemTabelaPreco.NOMECOLUNA_ROWKEY.equalsIgnoreCase(column)) {
					addRowkeyValueItemTabPrecoZero(sqlInsert, infoList);
				} else {
					sqlInsert.append("TB.").append(column);
				}
				sqlInsert.append(",");
			}
			sqlInsert.deleteCharAt(sqlInsert.length() - 1);
			sqlInsert.append(" FROM ").append(ItemTabelaPreco.TABLE_NAME).append(" TB, ").append(TabelaPreco.TABLE_NAME).append(" TAB ")
			.append(" WHERE ")
			.append(" TAB.CDEMPRESA = TB.CDEMPRESA ") 
			.append(" AND TAB.CDREPRESENTANTE = TB.CDREPRESENTANTE "); 
			driver.execute(sqlInsert.toString());
			String sqlDelete = "DELETE FROM TBLVPITEMTABELAPRECO WHERE CDTABELAPRECO = '0' OR FLTIPOALTERACAO = 'E'";
			driver.execute(sqlDelete);
			driver.commit();
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			driver.rollback();
			throw e;
		} finally {
			driver.finishTransaction();
			driver = null;
		}
	}

	private void addRowkeyValueItemTabPrecoZero(StringBuilder sqlBase, Hashtable infoList) {
		SyncInfo syncInfo = (SyncInfo) infoList.get(ItemTabelaPreco.TABLE_NAME);
		String[] keys = syncInfo.keys;
		int size = keys.length;
		for (int i = 0; i < size; i++) {
			String key = keys[i];
			if (ItemTabelaPreco.NOMECOLUNA_CDTABELAPRECO.equals(key.toUpperCase())) {
				sqlBase.append(" TAB.CDTABELAPRECO || ';' ||");
				continue;
			}
			sqlBase.append("TB.").append(key).append(" || ';' || ");
		}
		int length = sqlBase.length();
		sqlBase.delete(length - 3, length);
		sqlBase.append(" AS ROWKEY");
	}

	public String findPrimeiroCdTabelaPrecoPromocional(ItemTabelaPreco itemTabelaPrecoFilter) throws SQLException {
    	return ItemTabelaPrecoPdbxDao.getInstance().findPrimeiroCdTabelaPrecoPromocional(itemTabelaPrecoFilter);
	}
	
}
