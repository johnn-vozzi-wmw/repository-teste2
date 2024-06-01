package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseToolTip;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.ItemGrade;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.service.ItemGradeService;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.ProdutoGradeService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.TipoItemGradeService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemTabelaPrecoPdbxDao;
import br.com.wmw.lavenderepda.presentation.ui.combo.CondicaoComercialComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.CondicaoPagamentoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.ItemGradeComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.UnidadeFederalComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class InfoPrecoProdutoForm extends LavendereCrudListForm {
	
	private Produto produto;
	private CondicaoPagamentoComboBox cbCondicaoPagamento;
	private ItemGradeComboBox cbItemGrade1;
	private UnidadeFederalComboBox cbUf;
	private LabelName lbItemGrade1;
	private CondicaoComercialComboBox cbCondicaoComercial;
	private LabelValue lbDsProduto;
	private BaseToolTip tipDsProduto;
	private SessionContainer sessionContainer;

	public InfoPrecoProdutoForm(Produto produto) throws SQLException {
		super(Messages.PRODUTO_INFO_PRECO);
		this.produto = produto;
		sessionContainer = new SessionContainer();
		lbDsProduto = new LabelValue(LavenderePdaConfig.ocultaColunaCdProduto ? produto.dsProduto : produto.toString());
		tipDsProduto = new BaseToolTip(lbDsProduto, "");
		tipDsProduto.setText(MessageUtil.quebraLinhas(lbDsProduto.getValue()));
		listContainer = new GridListContainer(getItemCount(), 2, false, LavenderePdaConfig.usaScroolLateralListasProdutos);
		if (getItemCount() > 3) {
			listContainer.setColPosition(3, RIGHT);
		}
		if (getItemCount() > 5) {
			listContainer.setColPosition(5, RIGHT);
		}
		singleClickOn = false;
		configListContainer("CDTABELAPRECO", ValueUtil.VALOR_SIM);
        listContainer.setColsSort(configureColumnsSort());
        barBottomContainer.setVisible(false);
		cbCondicaoPagamento = new CondicaoPagamentoComboBox(Messages.COND_PAGTO);
		cbCondicaoComercial = new CondicaoComercialComboBox();
		cbItemGrade1 = new ItemGradeComboBox();
		cbItemGrade1.drawBackgroundWhenDisabled = true;
		cbUf = new UnidadeFederalComboBox();
		cbUf.load();
		cbUf.setSelectedIndex(0);
		lbItemGrade1 = new LabelName(" ");
		if (LavenderePdaConfig.isUsaGradeProduto1A4()) {
			loadComboGradeProduto();
		}
	}

	private String[][] configureColumnsSort() {
		int itemCount = getItemCount() - 1;
		if (LavenderePdaConfig.mostraPrecoUnidadeItem) {
			itemCount -= 1;
		}
		if (LavenderePdaConfig.usaCotacaoMoedaProduto) {
			itemCount -= 1;
		}
		String[][] colsSort = new String[itemCount][2];
		if (LavenderePdaConfig.mostrarPercDescontoMaximo) {
			colsSort[itemCount - 1][0] = Messages.DESC_PROMO;
			colsSort[itemCount - 1][1] = "VLPCTDESCPROMOCIONAL";
			itemCount -= 2;
			colsSort[itemCount][0] = Messages.MAX_DESC;
			colsSort[itemCount][1] = "VLPCTMAXDESCONTO";
		}
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
			itemCount -= 1;
			colsSort[itemCount][0] = Messages.PRODUTO_INFO_LABEL_QTD;
			colsSort[itemCount][1] = "QTITEM";
		}
		colsSort[0][0] = Messages.TABELA_PRECO;
		colsSort[0][1] = "CDTABELAPRECO";
		colsSort[1][0] = Messages.PRODUTO_LABEL_PRECO;
		colsSort[1][1] = "VLUNITARIO";
		
		if (LavenderePdaConfig.isMostraPrecoItemStNoDetalheDoProduto()) {
			colsSort[2][0] = Messages.PRODUTO_VALOR_ST;
			colsSort[2][1] = "VLEMBALAGEMST";
			colsSort[3][0] = Messages.PRODUTO_VALOR_UNIDADE_ST;
			colsSort[3][1] = "";
		}
		
		return colsSort;
	}
	
	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		ItemTabelaPreco itemTabelaPrecoFilter = new ItemTabelaPreco();
		itemTabelaPrecoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		itemTabelaPrecoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		itemTabelaPrecoFilter.cdProduto = produto.cdProduto;
		if (LavenderePdaConfig.isUsaGradeProduto1A4() && cbItemGrade1.size() > 0) {
			itemTabelaPrecoFilter.cdItemGrade1 = cbItemGrade1.getValue();
		} else {
			itemTabelaPrecoFilter.cdItemGrade1 = ItemTabelaPreco.CDUNIDADE_VALOR_PADRAO;
		}
		if (LavenderePdaConfig.usaPrecoPorUf) {
			itemTabelaPrecoFilter.cdUf = cbUf.getValue();
		} else {
			itemTabelaPrecoFilter.cdUf = ItemTabelaPreco.CDUF_VALOR_PADRAO;
		}
		if (LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial && cbCondicaoComercial.size() > 0) {
			String cdCondicaoComercial = cbCondicaoComercial.getValue();
			if (ValueUtil.isNotEmpty(cdCondicaoComercial)) {
				itemTabelaPrecoFilter.condComercialExcec = itemTabelaPrecoFilter.getCondComercialExcec(cdCondicaoComercial, itemTabelaPrecoFilter.cdItemGrade1);
			}
		}
		String cdUnidade = ItemTabelaPreco.CDUNIDADE_VALOR_PADRAO;
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
    		if (LavenderePdaConfig.usaUnidadeAlternativa) {
    			if (ValueUtil.isNotEmpty(produto.cdUnidade)) {
    				cdUnidade = produto.cdUnidade;
    			}
    		}
		}
		itemTabelaPrecoFilter.cdUnidade = cdUnidade;
		int cdPrazoPagtoPreco = ItemTabelaPreco.CDPRAZOPAGTOPRECO_VALOR_PADRAO; 
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
    		CondicaoPagamento condPagto = (CondicaoPagamento) cbCondicaoPagamento.getSelectedItem();
    		if (condPagto != null) {
    			if (condPagto.cdPrazoPagtoPreco == 0) {
    				cdPrazoPagtoPreco = 0;
    			} else {
    				cdPrazoPagtoPreco = condPagto.cdPrazoPagtoPreco;
    			}
        	} 
    		itemTabelaPrecoFilter.cdPrazoPagtoPreco = cdPrazoPagtoPreco;
		}
		return itemTabelaPrecoFilter;
	}
	
	@Override
	protected CrudService getCrudService() throws SQLException {
		return ItemTabelaPrecoService.getInstance();
	}
	
	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, sessionContainer, LEFT, getTop() + HEIGHT_GAP, FILL, UiUtil.getControlPreferredHeight() * 3 / 2);
		UiUtil.add(sessionContainer, new LabelName(Messages.PRODUTO), lbDsProduto, LEFT + WIDTH_GAP_BIG, HEIGHT_GAP, FILL - WIDTH_GAP);
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
			UiUtil.add(this, new LabelName(Messages.PEDIDO_LABEL_CDCONDICAOPAGAMENTO), cbCondicaoPagamento, getLeft(), AFTER + HEIGHT_GAP);
			cbCondicaoPagamento.loadCondicoesPagamento("", false, 0);
			cbCondicaoPagamento.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.usaPrecoPorUf) {
			UiUtil.add(this, new LabelName(Messages.UF), cbUf, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (LavenderePdaConfig.usaCondicaoComercialPedido) {
			UiUtil.add(this, new LabelName(Messages.PEDIDO_LABEL_CONDICAOCOMERCIAL), cbCondicaoComercial, getLeft(), AFTER + HEIGHT_GAP);
			cbCondicaoComercial.carregaCondicoesComerciaisForListProduto();
			cbCondicaoComercial.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.isUsaGradeProduto1A4() && cbItemGrade1.size() > 0) {
    		UiUtil.add(this, lbItemGrade1, cbItemGrade1, getLeft(), AFTER + HEIGHT_GAP);
    	}
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
	}
	
	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		Vector itemTabelaPrecoList = new Vector();
		if (LavenderePdaConfig.usaCondicaoComercialPedido && LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial) {
			try {
				ItemTabelaPrecoPdbxDao.getInstance().setInitializingCache(true);
				itemTabelaPrecoList = getCrudService().findAllByExample(domain);
			} finally {
				ItemTabelaPrecoPdbxDao.getInstance().setInitializingCache(false);
			}
		} else {
			itemTabelaPrecoList = getCrudService().findAllByExample(domain);
		}
		int size = itemTabelaPrecoList.size();
		for (int i = 0; i < size; i++) {
			ItemTabelaPreco itemTabelaPreco = (ItemTabelaPreco) itemTabelaPrecoList.items[i];
			if (itemTabelaPreco.vlUnitario != 0) {
	    		aplicaDescPromocional(itemTabelaPreco);
	    	}
		}
		return itemTabelaPrecoList;
	}
	
	private StringBuilder itens = new StringBuilder();
	
	@Override
	protected String[] getItem(Object domain) throws SQLException {
		ItemTabelaPreco itemTabelaPreco = (ItemTabelaPreco) domain;
		produto.itemTabelaPreco = itemTabelaPreco;
		int itemCount = getItemCount();
		String[] s = new String[itemCount];
        itens.setLength(0);
        
        if (LavenderePdaConfig.isMostraPrecoItemStNoDetalheDoProduto()) {
        	double valorUnitarioUnidadeST;
        	if (produto.nuConversaoUnidadesMedida == 0.0) {
        		valorUnitarioUnidadeST = produto.itemTabelaPreco.vlEmbalagemSt;
        	} else {
        		valorUnitarioUnidadeST = produto.nuConversaoUnidadesMedida != 0 ? produto.itemTabelaPreco.vlEmbalagemSt / produto.nuConversaoUnidadesMedida : 0;
        	}
			s[--itemCount] = MessageUtil.getMessage(Messages.PRODUTO_VALOR_UNIDADE_ST, StringUtil.getStringValueToInterface(valorUnitarioUnidadeST));
			s[--itemCount] = MessageUtil.getMessage(Messages.PRODUTO_VALOR_ST, Messages.MOEDA + " " + StringUtil.getStringValueToInterface(produto.itemTabelaPreco.vlEmbalagemSt));
		}
        
		if (LavenderePdaConfig.usaCotacaoMoedaProduto) {
			itens.append(Messages.ITEMPEDIDO_LABEL_VLCOTACAOMOEDA).append(" - ");
			if (ValueUtil.isNotEmpty(produto.dsMoedaProduto)) {
				itens.append(produto.dsMoedaProduto).append(" ");
			}
			itens.append(StringUtil.getStringValueToInterface(itemTabelaPreco.vlCotacaoMoeda));
			s[--itemCount] = itens.toString();
			itens.setLength(0);
		}
        if (LavenderePdaConfig.mostrarPercDescontoMaximo) {
        	itens.append(Messages.DESC_PROMO).append(" - ").append(StringUtil.getStringValueToInterface(itemTabelaPreco.vlPctDescPromocional));
        	s[--itemCount] = itens.toString();
        	itens.setLength(0);

        	itens.append(Messages.MAX_DESC).append(" - ").append(StringUtil.getStringValueToInterface(itemTabelaPreco.getVlPctMaxDescontoItemTabelaPreco(produto)));
        	s[--itemCount] = itens.toString();
        	itens.setLength(0);
        }
        double vlPrecoProduto;
		if (LavenderePdaConfig.usaCondicaoComercialPedido && LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial && !ValueUtil.isRowkeyEmpty(itemTabelaPreco.condComercialExcec.getPrimaryKey())) {
			vlPrecoProduto = itemTabelaPreco.condComercialExcec.vlUnitario;
		} else {
			vlPrecoProduto = ProdutoService.getInstance().getPrecoProduto(produto, cbCondicaoComercial.getCondicaoComercial(), itemTabelaPreco.cdTabelaPreco, itemTabelaPreco.cdUf);
		}
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
			CondicaoPagamento condicaoPagamentoSelecionada = (CondicaoPagamento) cbCondicaoPagamento.getSelectedItem();
			if (condicaoPagamentoSelecionada != null && condicaoPagamentoSelecionada.vlIndiceFinanceiro > 0) {
				vlPrecoProduto = vlPrecoProduto * condicaoPagamentoSelecionada.vlIndiceFinanceiro;
			}
		}
		if (LavenderePdaConfig.mostraPrecoUnidadeItem) {
			double nuConversaoUnidadesMedida = produto.nuConversaoUnidadesMedida;
			if (nuConversaoUnidadesMedida == 0 && itemTabelaPreco.nuConversaoUnidade != 0) {
				nuConversaoUnidadesMedida = itemTabelaPreco.nuConversaoUnidade;
			} else if (nuConversaoUnidadesMedida == 0) {
				nuConversaoUnidadesMedida = 1;
			}
			s[--itemCount] = MessageUtil.getMessage(Messages.PRODUTO_LABEL_PRECO_UNID_RS, StringUtil.getStringValueToInterface(vlPrecoProduto / nuConversaoUnidadesMedida));
			s[--itemCount] = MessageUtil.getMessage(Messages.PRODUTO_LABEL_PRECO_RS, StringUtil.getStringValueToInterface(vlPrecoProduto));
		} else {
			itens.append(Messages.MOEDA + " ").append(StringUtil.getStringValueToInterface(vlPrecoProduto));
			s[--itemCount] = itens.toString();
			itens.setLength(0);
		}
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
        	itens.append(Messages.PRODUTO_INFO_LABEL_QTD).append(" - ").append(StringUtil.getStringValueToInterface(itemTabelaPreco.qtItem));
        	s[--itemCount] = itens.toString();
        	itens.setLength(0);
        }
        itens.append(ValueUtil.VALOR_NI);
        s[--itemCount] = itens.toString();
        itens.setLength(0);
        
        itens.append(StringUtil.getStringValue(itemTabelaPreco.getTabelaPreco().cdTabelaPreco)).append(" - ").append(StringUtil.getStringValue(itemTabelaPreco.getTabelaPreco().dsTabelaPreco));
        s[--itemCount] = itens.toString();
        itens.setLength(0);
		return s;
	}
	
	private int getItemCount() {
		int itemCount = 3;
		itemCount += LavenderePdaConfig.isMostraPrecoItemStNoDetalheDoProduto() ? 2 : 0;
		itemCount += LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo() ? 1 : 0;
		itemCount += LavenderePdaConfig.mostrarPercDescontoMaximo ? 2 : 0;
		itemCount += LavenderePdaConfig.mostraPrecoUnidadeItem ? 1 : 0;
		itemCount += LavenderePdaConfig.usaCotacaoMoedaProduto ? 1 : 0;
		return itemCount;
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == cbCondicaoPagamento || event.target == cbItemGrade1 || event.target == cbUf || event.target == cbCondicaoComercial) {
					list();
				}
				break;
			default:
				break;
		}
	}

	private void aplicaDescPromocional(ItemTabelaPreco itemTabelaPreco) {
    	if (LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco) {
    		double vlPctDesc = itemTabelaPreco.vlPctDescPromocional;
	    	if (vlPctDesc > 0) {
	    		itemTabelaPreco.vlUnitario = ValueUtil.round(itemTabelaPreco.vlUnitario * (1 - (vlPctDesc / 100)));
    		}
    	}
    }
	
	private void loadComboGradeProduto() throws SQLException {
    	lbItemGrade1.setValue("-");
		cbItemGrade1.removeAll();
		//--
		Vector produtoGradeList = ProdutoGradeService.getInstance().findProdutoGradeList(produto.cdProduto);
		//--
		int size = produtoGradeList.size();
		if (size > 0) {
			ProdutoGrade produtGrade = (ProdutoGrade)produtoGradeList.items[0];
			lbItemGrade1.setValue(TipoItemGradeService.getInstance().getDsTipoItemGrade(produtGrade.cdTipoItemGrade1));
			cbItemGrade1.popupTitle = lbItemGrade1.getValue();
		}
		//--
		Vector itensGrade1List = new Vector();
		for (int i = 0; i < size; i++) {
			ProdutoGrade produtGrade = (ProdutoGrade)produtoGradeList.items[i];
			ItemGrade itemGrade1 = ItemGradeService.getInstance().getItemGrade(produtGrade.cdTipoItemGrade1, produtGrade.cdItemGrade1);
			if (itemGrade1 != null) {
				if (itensGrade1List.indexOf(itemGrade1) == -1) {
					itensGrade1List.addElement(itemGrade1);
				}
			}
		}
		itensGrade1List.qsort();
		cbItemGrade1.add(itensGrade1List);
		cbItemGrade1.setSelectedIndex(0);
	}

}
