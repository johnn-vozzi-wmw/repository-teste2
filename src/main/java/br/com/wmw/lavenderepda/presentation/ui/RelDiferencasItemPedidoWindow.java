package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoErpDif;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.service.ItemPedidoErpDifService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoGradeService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class RelDiferencasItemPedidoWindow extends WmwWindow {
	
	private ItemPedidoErpDif itemPedidoDif;
	private ItemPedido itemPedido;
	private LabelValue edCdProduto;
	private EditMemo edDsObPedOrg;
	private EditMemo edDsObPedErp;
	private LabelValue edQtItemPedOrg;
	private LabelValue edQtItemPedErp;
	private LabelName lbProduto;
	private LabelValue edUnidade;
	private LabelName lbUnidade;
	private BaseButton btGrade;

	public RelDiferencasItemPedidoWindow(ItemPedidoErpDif itemPedidoErpDif) throws SQLException {
		super(Messages.REL_DIF_ITEMPEDIDO_DIFERENCAS_RELATORIO);
		scrollable = false;
		itemPedidoDif = itemPedidoErpDif;
		edCdProduto = new LabelValue();
		edQtItemPedOrg = new LabelValue();
		edQtItemPedErp = new LabelValue();
		edUnidade = new LabelValue();
		lbUnidade = new LabelName(Messages.METAS_UNIDADE);
		lbProduto = new LabelName(Messages.ITEMPEDIDO_LABEL_PRODUTO);
		edDsObPedOrg = new EditMemo("", 3, Pedido.MAX_LENGTH_DS_OBSERVACAO);
		edDsObPedErp = new EditMemo("", 3, Pedido.MAX_LENGTH_DS_OBSERVACAO);
		btGrade = new BaseButton(UiUtil.getColorfulImage("images/grade.png", (UiUtil.getControlPreferredHeight() / 3) * 2, (UiUtil.getControlPreferredHeight() / 3) * 2));
		btGrade.transparentBackground = true;
		btGrade.useBorder = false;
		carregaItemPedido();
		atualizaValores();
		setDefaultRect();
	}
	
	private void carregaItemPedido() throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.cdEmpresa = itemPedidoDif.cdEmpresa;
		itemPedido.cdRepresentante = itemPedidoDif.cdRepresentante;
		itemPedido.cdProduto = itemPedidoDif.cdProduto;
		itemPedido.nuPedido = itemPedidoDif.nuPedido;
		itemPedido.flTipoItemPedido = itemPedidoDif.flTipoItemPedido;
		itemPedido.flOrigemPedido = ItemPedido.ITEMPEDIDO_FLORIGEMERP;
		itemPedido.nuSeqProduto = itemPedidoDif.nuSeqProduto;
		this.itemPedido = (ItemPedido) ItemPedidoService.getInstance().findByRowKeyErp(itemPedido);
		if (this.itemPedido != null) {
			ItemPedidoGradeService.getInstance().findItemPedidoGradeList(this.itemPedido);
		}
		btGrade.setVisible(this.itemPedido != null && !ValueUtil.valueEquals(ProdutoGrade.CD_ITEM_GRADE_PADRAO, this.itemPedido.cdItemGrade1)); 
	}

	//@Override
	protected CrudService getCrudService() throws java.sql.SQLException {
		return ItemPedidoErpDifService.getInstance();
	}

	//@Override
	protected BaseDomain getDomainFilter() {
		return new ItemPedidoErpDif();
	}

	//@Override
	protected void atualizaValores() throws SQLException {
		edCdProduto.setValue(produtoToString(this.itemPedidoDif, " - "));
		final String qtItemFisicoOrg = LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? StringUtil.getStringValueToInterface(itemPedidoDif.qtItemfisicoOrg, 0) : StringUtil.getStringValueToInterface(itemPedidoDif.qtItemfisicoOrg);
		final String qtItemFisicoErp = LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? StringUtil.getStringValueToInterface(itemPedidoDif.qtItemFisicoErp, 0) : StringUtil.getStringValueToInterface(itemPedidoDif.qtItemFisicoErp);
		edQtItemPedOrg.setValue(qtItemFisicoOrg);
		edQtItemPedErp.setValue(qtItemFisicoErp);
		edDsObPedOrg.setValue(itemPedidoDif.dsObservacaoOrg);
		edDsObPedErp.setValue(itemPedidoDif.dsObservacaoErp);
		edUnidade.setValue(itemPedidoDif.cdUnidade);
	}
	
	private String produtoToString(ItemPedidoErpDif itemPedidoErpDif, final String separador) throws SQLException {
		StringBuffer strb = new StringBuffer();
		Produto produto = ProdutoService.getInstance().getProduto(itemPedidoErpDif.cdProduto);
		if (produto == null || ValueUtil.isEmpty(produto.cdProduto)) {
			if (!LavenderePdaConfig.ocultaColunaCdProduto) strb.append(itemPedidoErpDif.cdProduto).append(" - ");
			strb.append(itemPedidoErpDif.cdProduto);
			return strb.toString();
		}
		if (!LavenderePdaConfig.ocultaColunaCdProduto) {
			if (produto != null) {
				strb.append(produto.cdProduto + separador).append(ProdutoService.getInstance().getDescricaoProdutoComReferencia(produto));
			}
		} else {
			if (produto != null) {
				strb.append(StringUtil.getStringValue(ProdutoService.getInstance().getDescricaoProdutoComReferencia(produto)));
			}
		}
		return strb.toString();
	}
	
	//@Override
	public void initUI() {
		super.initUI();
        UiUtil.add(this, lbProduto, edCdProduto, getLeft(), getNextY(), PREFERRED);
		if (LavenderePdaConfig.isShowDiferencaPedidoQtdItemPedido() && LavenderePdaConfig.isConfigGradeProduto()) {
	        UiUtil.add(this, new LabelName(Messages.REL_DIF_ITEMPEDIDO_DIFERENCAS_QTORG_USAGRADE), edQtItemPedOrg, getLeft(), getNextY(), PREFERRED);
	        UiUtil.add(this, new LabelName(Messages.REL_DIF_ITEMPEDIDO_DIFERENCAS_QTERP_USAGRADE), edQtItemPedErp, getLeft(), getNextY(), PREFERRED);
        } else if (LavenderePdaConfig.isShowDiferencaPedidoQtdItemPedido()) {
        	UiUtil.add(this, new LabelName(Messages.REL_DIF_ITEMPEDIDO_DIFERENCAS_QTORG), edQtItemPedOrg, getLeft(), getNextY(), PREFERRED);
        	UiUtil.add(this, new LabelName(Messages.REL_DIF_ITEMPEDIDO_DIFERENCAS_QTERP), edQtItemPedErp, getLeft(), getNextY(), PREFERRED);
        }
		if (LavenderePdaConfig.isConfigGradeProduto()) {
        	UiUtil.add(this, btGrade,  SAME + edQtItemPedErp.getMaxTextWidth() + HEIGHT_GAP_BIG, edQtItemPedErp.getY(), (UiUtil.getControlPreferredHeight() / 3) * 2, (UiUtil.getControlPreferredHeight() / 3) * 2);
        }
        if (LavenderePdaConfig.usaUnidadeAlternativa) {
        	UiUtil.add(this, lbUnidade, edUnidade, getLeft(), getNextY(), PREFERRED);
        }
        if (LavenderePdaConfig.isShowDiferencaPedidoObsItemPedido()) {
        	UiUtil.add(this, new LabelName(Messages.REL_DIF_ITEMPEDIDO_DIFERENCAS_OBORG), edDsObPedOrg, getLeft(), getNextY(), PREFERRED - WIDTH_GAP_BIG);
        	UiUtil.add(this, new LabelName(Messages.REL_DIF_ITEMPEDIDO_DIFERENCAS_OBERP), edDsObPedErp, getLeft(), getNextY(), PREFERRED - WIDTH_GAP_BIG);
        }
	}

	//@Override
	public void onEvent(Event event) {
		try {
			super.onEvent(event);
			switch (event.type) {
				case ControlEvent.PRESSED: {
					if (event.target == btGrade) {
						if (itemPedido != null) {
							if (LavenderePdaConfig.usaGradeProduto2() && LavenderePdaConfig.isShowDiferencaPedidoItemPedidoGrade()) {
								ListItemPedidoGradeErpDifWindow listItemPedidoGradeErpDifWindow = new ListItemPedidoGradeErpDifWindow(itemPedido);
								listItemPedidoGradeErpDifWindow.popup();
							} else {
								CadItemPedidoGradeWindow cadItemPedidoGradeWindow = new CadItemPedidoGradeWindow(itemPedido, false, null);
								cadItemPedidoGradeWindow.popup();
								if (cadItemPedidoGradeWindow.windowClosed) {
									ItemPedidoService.getInstance().carregaItensGradePreco(itemPedido);
								}
							}
						}
					}
					break;
				}
			}
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		}
	}
	
	//@Override
	public void setEnabled(boolean enabled) {
		edDsObPedErp.setEnabled(enabled);
		edDsObPedOrg.setEnabled(enabled);
		edDsObPedOrg.drawBackgroundWhenDisabled = true;
		edDsObPedErp.drawBackgroundWhenDisabled = true;
	}
	
	//@Override
	protected void onPopup() {
		super.onPopup();
		setEnabled(false);
	}

}
