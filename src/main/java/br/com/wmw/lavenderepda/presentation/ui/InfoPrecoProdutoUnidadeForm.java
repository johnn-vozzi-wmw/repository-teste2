package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.ItemGrade;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.domain.Unidade;
import br.com.wmw.lavenderepda.business.service.CondicaoComercialService;
import br.com.wmw.lavenderepda.business.service.ItemGradeService;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.ProdutoGradeService;
import br.com.wmw.lavenderepda.business.service.ProdutoUnidadeService;
import br.com.wmw.lavenderepda.business.service.TipoItemGradeService;
import br.com.wmw.lavenderepda.business.service.UnidadeService;
import br.com.wmw.lavenderepda.presentation.ui.combo.CondicaoComercialComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.CondicaoPagamentoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.ItemGradeComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TabelaPrecoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.UnidadeFederalComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class InfoPrecoProdutoUnidadeForm extends BaseUIForm {
	
	private LabelValue lbVlUnidadePrincipal;
	private BaseGridEdit gridUnidadesProduto;
	private Produto produto;
	private ItemTabelaPreco itemTabPreco;
	private CondicaoPagamentoComboBox cbCondicaoPagamento;
	private ItemGradeComboBox cbItemGrade1;
	private UnidadeFederalComboBox cbUf;
	private LabelName lbItemGrade1;
	private TabelaPrecoComboBox cbTabelaPreco;
	private CondicaoComercialComboBox cbCondicaoComercial;
	private BaseScrollContainer scBase;
	private LabelContainer lbDsProdutoContainer;

	public InfoPrecoProdutoUnidadeForm(Produto produto) throws SQLException {
		super(Messages.PRECO_PROD_UNIDADE);
		this.produto = produto;
		lbDsProdutoContainer = new LabelContainer(this.produto.toString());
		cbTabelaPreco = new TabelaPrecoComboBox();
		cbTabelaPreco.loadTabelaPrecoByProduto(produto);
		cbTabelaPreco.setSelectedIndex(0);
		if (LavenderePdaConfig.usaCondicaoComercialPedido) {
			cbCondicaoComercial = new CondicaoComercialComboBox();
			cbCondicaoComercial.carregaCondicoesComerciaisForListProduto();
			cbCondicaoComercial.setSelectedIndex(0);
		}
		scBase = new BaseScrollContainer(false, true);
		scBase.setBackColor(getBackColor());
		cbCondicaoPagamento = new CondicaoPagamentoComboBox(Messages.COND_PAGTO);
		cbItemGrade1 = new ItemGradeComboBox();
		cbItemGrade1.drawBackgroundWhenDisabled = true;
		cbUf = new UnidadeFederalComboBox();
		cbUf.carregaUf();
		cbUf.setSelectedIndex(0);
		lbItemGrade1 = new LabelName(" ");
		lbVlUnidadePrincipal = new LabelValue();
		if (LavenderePdaConfig.isConfigGradeProduto()) {
			loadComboGradeProduto();
		}
		Unidade unidade = UnidadeService.getInstance().findByCdUnidade(produto.cdUnidade, false);
		if (unidade != null) {
			lbVlUnidadePrincipal.setValue(unidade.dsUnidade);
		}
	}
	

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, scBase, LEFT, getTop(), FILL, FILL);
		UiUtil.add(scBase, lbDsProdutoContainer, LEFT, TOP, FILL, LabelContainer.getStaticHeight());
		UiUtil.add(scBase, new LabelName(Messages.UNIDADE_LABEL_UNIDADE_PRINCIPAL), lbVlUnidadePrincipal, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(scBase, new LabelName(Messages.TABELA_PRECO), cbTabelaPreco, getLeft(), AFTER + HEIGHT_GAP);
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
			UiUtil.add(scBase, new LabelName(Messages.PEDIDO_LABEL_CDCONDICAOPAGAMENTO), cbCondicaoPagamento, getLeft(), AFTER + HEIGHT_GAP);
			cbCondicaoPagamento.loadCondicoesPagamento(cbTabelaPreco.getValue(), false, 0);
			cbCondicaoPagamento.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.usaPrecoPorUf) {
			UiUtil.add(scBase, new LabelName(Messages.UF), cbUf, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (LavenderePdaConfig.isConfigGradeProduto() && cbItemGrade1.size() > 0) {
    		UiUtil.add(scBase, lbItemGrade1, cbItemGrade1, getLeft(), AFTER + HEIGHT_GAP);
    	}
		if (LavenderePdaConfig.usaCondicaoComercialPedido) {
			UiUtil.add(scBase, new LabelName(Messages.PEDIDO_LABEL_CONDICAOCOMERCIAL), cbCondicaoComercial, getLeft(), AFTER + HEIGHT_GAP);
		}
        GridColDefinition[] gridColDef = {
            new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
            new GridColDefinition(Messages.METAS_UNIDADE, -29, LEFT),
            new GridColDefinition(Messages.UNIDADE_LABEL_QT_ELEMENTAR, -16, RIGHT),
            new GridColDefinition(Messages.UNIDADE_LABEL_VL_ELEMENTAR, -16, RIGHT),
            new GridColDefinition(Messages.PRODUTO_LABEL_PRECO, -28, RIGHT),
            new GridColDefinition(Messages.UNIDADE_LABEL_FATOR, -16, RIGHT)};
        gridUnidadesProduto = UiUtil.createGridEdit(gridColDef);
        UiUtil.add(scBase, gridUnidadesProduto, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
        if (gridUnidadesProduto.getHeight() < UiUtil.getControlPreferredHeight() * 5) {
        	gridUnidadesProduto.resetSetPositions();
        	gridUnidadesProduto.setRect(LEFT, KEEP, FILL, UiUtil.getControlPreferredHeight() * 8);
        }
        loadItemTabPreco(null);
    	carregaGridUnidadesProduto();
	}
	
	
	private void carregaGridUnidadesProduto() throws SQLException {
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		int listSize = 0;
		Vector domainList = new Vector(0);
		String[][] gridItems;
		try {
			gridUnidadesProduto.setItems(null);
			//---
			domainList = ProdutoUnidadeService.getInstance().findAllByProduto(produto);
			listSize = domainList.size();
			//--
			if (listSize > 0) {
				String[] item = getItemProdutoUnidade((ProdutoUnidade) domainList.items[0]);
				gridItems = new String[listSize][item.length];
				gridItems[0] = item;
				for (int i = 1; i < listSize; i++) {
					gridItems[i] = getItemProdutoUnidade((ProdutoUnidade) domainList.items[i]);
				}
				//--
				gridUnidadesProduto.setItems(gridItems);
				gridUnidadesProduto.qsort(3);
			}
		} finally {
			gridItems = null;
			domainList = null;
			msg.unpop();
		}
	}

	protected String[] getItemProdutoUnidade(ProdutoUnidade produtoUnidade) throws SQLException {
		if (LavenderePdaConfig.usaUnidadeAlternativa && ValueUtil.isNotEmpty(produtoUnidade.cdUnidade) && !ValueUtil.valueEquals(produtoUnidade.cdUnidade, itemTabPreco.cdUnidade)) {
			loadItemTabPreco(produtoUnidade.cdUnidade);
		}
		double vlItem = itemTabPreco != null ? ProdutoUnidadeService.getInstance().calculateUnidadeAlternativa(null, produtoUnidade, itemTabPreco, itemTabPreco.vlUnitario, produto) : 0;
		String[] item = { produtoUnidade.rowKey,
				produtoUnidade.toString(),
				StringUtil.getStringValueToInterface(produtoUnidade.qtEmbElementar),
				StringUtil.getStringValueToInterface(produtoUnidade.vlEmbElementar),
				StringUtil.getStringValueToInterface(vlItem),
				StringUtil.getStringValueToInterface(produtoUnidade.vlIndiceFinanceiro)
		};
		return item;
	}
	
	private void loadComboGradeProduto() throws SQLException {
    	lbItemGrade1.setValue("-");
		cbItemGrade1.removeAll();
		Vector produtoGradeList = ProdutoGradeService.getInstance().findProdutoGradeList(produto.cdProduto);
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
	
	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == cbCondicaoPagamento || event.target == cbItemGrade1 || event.target == cbUf || event.target == cbTabelaPreco || event.target == cbCondicaoComercial) {
					gridUnidadesProduto.removeAllElements();
					loadItemTabPreco(null);
					carregaGridUnidadesProduto();
				}
				break;
	
			default:
				break;
		}
	}
	
	private void loadItemTabPreco(String cdUnidade) throws SQLException {
		ItemTabelaPreco itemTabelaPrecoFilter = new ItemTabelaPreco();
		itemTabelaPrecoFilter.cdEmpresa = produto.cdEmpresa;
		itemTabelaPrecoFilter.cdRepresentante = produto.cdRepresentante;
		itemTabelaPrecoFilter.cdProduto = produto.cdProduto;
		int cdPrazoPagtoPreco = ItemTabelaPreco.CDPRAZOPAGTOPRECO_VALOR_PADRAO; 
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
			if (ValueUtil.isEmpty(cdUnidade)) {
				cdUnidade = ItemTabelaPreco.CDUNIDADE_VALOR_PADRAO;
				if (LavenderePdaConfig.usaUnidadeAlternativa) {
					if (ValueUtil.isNotEmpty(produto.cdUnidade)) {
						cdUnidade = produto.cdUnidade;
					}
				}
			}
    		CondicaoPagamento condPagto = (CondicaoPagamento) cbCondicaoPagamento.getSelectedItem();
    		if (condPagto != null) {
    			cdPrazoPagtoPreco = condPagto.cdPrazoPagtoPreco;
        	} 
    		produto.itemTabelaPreco = ItemTabelaPrecoService.getInstance().getItemTabelaPrecoGradeNivel1UnidadePrazo(cbTabelaPreco.getValue(), produto.cdProduto, cbUf.getValue(), cbItemGrade1.getValue(), cdUnidade, cdPrazoPagtoPreco);
    	} else {
    		produto.itemTabelaPreco = ItemTabelaPrecoService.getInstance().getItemTabelaPrecoGradeNivel1(cbTabelaPreco.getValue(), produto.cdProduto, cbUf.getValue(), cbItemGrade1.getValue());
    	}
		itemTabPreco = produto.itemTabelaPreco;
		if (LavenderePdaConfig.usaCondicaoComercialPedido) {
			CondicaoComercialService.getInstance().aplicaIndiceFinanceiroCondComercialProdutoBase(produto, cbCondicaoComercial.getCondicaoComercial(), !LavenderePdaConfig.aplicaIndicesNaUnidadePadraoParaUnidadeAlternativa);
		}
		if (produto.itemTabelaPreco != null && produto.itemTabelaPreco.vlUnitario > 0) {
    		aplicaDescPromocional();
    	}
	}
	
	
	private void aplicaDescPromocional() {
    	if (LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco) {
    		double vlPctDesc = itemTabPreco.vlPctDescPromocional;
    		if (vlPctDesc > 0) {
	    		itemTabPreco.vlUnitario = (itemTabPreco.vlUnitario * (1 - (vlPctDesc / 100)));
	    		if (!LavenderePdaConfig.aplicaIndicesNaUnidadePadraoParaUnidadeAlternativa) {
	    			itemTabPreco.vlUnitario = ValueUtil.round(itemTabPreco.vlUnitario);
	    		}
    		}
    	}
    }

}
