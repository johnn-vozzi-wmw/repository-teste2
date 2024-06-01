package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.List;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseEdit;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.PushButtonGroupBase;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.LavendereConfig;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemGrade;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.TipoItemGradeService;
import totalcross.sys.Settings;
import totalcross.sys.SpecialKeys;
import totalcross.ui.PushButtonGroup;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.ui.gfx.Color;
import totalcross.util.Vector;

public class ListEstoqueGradeFaltanteWindow extends WmwWindow {
	
	private static final String FIRST_COLUMN = "0";
	private static final String COLUMN_ESTOQUE = "1";
	private static final int POSICAO_COLUNA_QUANTIDADE = 2;
	
	private ItemPedido itemPedido;
	private Vector itensGrade1List;
	private EditNumberFrac edControlGrid;
	private ButtonPopup btOk;
	private ButtonPopup btAjustar;
	private PushButtonGroupBase numericPad;
	private BaseEdit edBaseNumPad;
	private int numPadHeight = 0;
	private LabelValue lbDsProduto;
	private BaseGridEdit grid;
	private boolean confirmouAlteracao;

	public ListEstoqueGradeFaltanteWindow(ItemPedido itemPedido) throws SQLException {
		super(Messages.LISTA_ESTOQUE_GRADE_FALTANTE);
		this.itemPedido = itemPedido;
		populaItensGrade1List(itemPedido.getItemPedidoGradeErroList());
		scrollable = false;
		edControlGrid = new EditNumberFrac("00000", 9, BaseEdit.EDIT_TYPE_ONLY_NUMBER_DEC);
		btOk = new ButtonPopup("  " + FrameworkMessages.BOTAO_OK + "  ");
		btAjustar = new ButtonPopup("  " + Messages.BOTAO_AJUSTAR + "  ");
		montaGridGrade();
		carregaGrid();
		montaTecladoNumerico();
		setDefaultWideRect();
		confirmouAlteracao = false;
		if (LavenderePdaConfig.usaOrdenacaoNuSequenciaGradeProduto) {
			grid.disableSort = false;
		}
	}

	@Override
	public void initUI() {
		super.initUI();
		addButtonPopup(btOk);
		addButtonPopup(btAjustar);
		addBtFechar();
		int remainingSize = 0;
		if (LavenderePdaConfig.usaTecladoFixoTelaItemPedido && itemPedido.pedido.isPedidoAberto()) {
			numPadHeight = UiUtil.getControlPreferredHeight() * 2;
			UiUtil.add(this, numericPad, LEFT, BOTTOM - cFundoFooter.getHeight(), FILL + 4, numPadHeight);
			remainingSize += numPadHeight;
		}
		criaDescricaoProduto();
		UiUtil.add(this, lbDsProduto, getLeft(), getNextY(), FILL, PREFERRED);
		if (grid == null) {
			return; 
		}
		UiUtil.add(this, grid, LEFT + HEIGHT_GAP, AFTER + HEIGHT_GAP, FILL, getGridHeight(remainingSize));
		if (!LavendereConfig.getInstance().usaFocoCampoBuscaAutomaticamente) {
			return;
		}
		grid.setSelectedIndex(0);
		grid.exibeControleGrid(0, POSICAO_COLUNA_QUANTIDADE);
	}
	
	@Override
	public void onEvent(Event event) {
		try {
			super.onEvent(event);
	    	switch (event.type) {
		    	case ControlEvent.PRESSED: {
		    		if (event.target == btOk) {
		    			btOkClick();
		    		} else if (event.target == btAjustar) {
		    			btAjustarClick();
		    		}  else if (event.target == numericPad) {
		    			KeyEvent ke = new KeyEvent();
		    			String s = numericPad.getSelectedItem();
		    			if (s != null) {
		    				if (s.equals("<")) {
		    					ke.key = SpecialKeys.BACKSPACE;
		    				} else {
		    					ke.key = s.charAt(0);
		    				}
		    				if (edBaseNumPad != null) {
		    					ke.target = edBaseNumPad;
		    					edBaseNumPad.onEvent(ke);
		    				}
		    			}
		    			numericPad.setSelectedIndex(-1);
		    		}
		    		break;
		    	}
		    	case ControlEvent.FOCUS_IN: {
		    		if (event.target instanceof BaseEdit && ((BaseEdit)event.target).isEditable()) {
		    			edBaseNumPad = (BaseEdit) event.target;
		    		} else {
		    			edBaseNumPad = new EditText("", 0);
		    		}
		    		break;
		    	}
		    	case ControlEvent.FOCUS_OUT: {
		    		if ((!LavenderePdaConfig.usaTecladoVirtual || LavenderePdaConfig.usaTecladoFixoTelaItemPedido) && itemPedido.pedido.isPedidoAberto()) {
		    			edBaseNumPad = new EditText("", 0);
		    		}
		    		break;
		    	}
		    	case KeyEvent.SPECIAL_KEY_PRESS: {
		    		KeyEvent ke = (KeyEvent) event; 
		    		if (ke.isActionKey() && LavenderePdaConfig.isUsaTeclaEnterComoConfirmacaoItemPedido()) {
		    			grid.hideControl();
		    			ControlEvent ce = new ControlEvent(ControlEvent.PRESSED, btOk);
		    			postEvent(ce);
		    		}
		    		break;
		    	}
		    	case ValueChangeEvent.VALUE_CHANGE: {
					if (event.target instanceof BaseEdit) {
						grid.hideControl();
						if (!LavenderePdaConfig.usaTecladoVirtual || LavenderePdaConfig.usaTecladoFixoTelaItemPedido) {
							edBaseNumPad = new EditText("", 0);
						}
						recalculaQuantidadeExcedente(grid.getSelectedItem(), grid.getSelectedIndex());
					}
					break;
				}
	    	}
	   	} catch (ValidationException e) {
	   		UiUtil.showErrorMessage(e);
		} catch (Throwable ee) {
			UiUtil.showErrorMessage(ee);
		}
	}

	private void recalculaQuantidadeExcedente(String[] item, int posicaoGrid) {
		if (!LavenderePdaConfig.isExibeColunaComQuantidadeExcedenteParaProdutoGrade()) {
			return;
		}
		ItemGrade itemGrade = (ItemGrade) itensGrade1List.items[posicaoGrid];
		double qtEstoque  = itemGrade.qtEstoqueItem;
		double qtInserida = ValueUtil.getDoubleValue(deleteCaracterDePontuacao(item));
		if (qtInserida > qtEstoque) {
			item[POSICAO_COLUNA_QUANTIDADE - 1] = getQtdAsString(qtInserida - qtEstoque);
		} else {
			item[POSICAO_COLUNA_QUANTIDADE - 1] = ValueUtil.VALOR_ZERO;
		}
	}

   	private void btAjustarClick()  {
   		int size = grid.size();
		for (int i = 0; i < size; i++) {
			String[] item = grid.getItem(i);
			ItemGrade itemGrade1 = (ItemGrade) itensGrade1List.items[i];
			double qtItemFisico = itemGrade1.qtEstoqueItem; 
			item[POSICAO_COLUNA_QUANTIDADE] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(qtItemFisico) : qtItemFisico, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface); getQtdAsString(itemGrade1.qtEstoqueItem);
			if (!LavenderePdaConfig.isExibeColunaComQuantidadeExcedenteParaProdutoGrade()) {
				continue;
			}
			item[POSICAO_COLUNA_QUANTIDADE - 1] = ValueUtil.VALOR_ZERO;
		}
		grid.repaintNow();
	}

	private void btOkClick() throws SQLException {
		int size = itensGrade1List.size();
		for (int i = 0; i < size; i++) {
			ItemGrade itemGrade1 = (ItemGrade) itensGrade1List.items[i];
			String[] item = grid.getItem(i);
			String qtdeInserida = deleteCaracterDePontuacao(item);
			itemGrade1.qtItemGrade = ValueUtil.getDoubleValue(qtdeInserida);
		}
		confirmouAlteracao = true;
		unpop();
	}


	private String deleteCaracterDePontuacao(String[] item) {
		return StringUtil.delete(item[POSICAO_COLUNA_QUANTIDADE], '.');
	}
	
	private void populaItensGrade1List(List<ItemPedidoGrade> itemPedidoGradeErroList) {
		itensGrade1List = new Vector();
		for (ItemPedidoGrade itemPedidoGrade : itemPedidoGradeErroList) {
			itensGrade1List.addElement(itemPedidoGrade.itemGrade1);
		}
	}

	private void carregaGrid() throws SQLException {
		int size = grid.size();
		for (int i = 0; i < size; i++) {
			String[] item = grid.getItem(i);
			ItemGrade itemGrade1 = (ItemGrade)itensGrade1List.items[i];
			double qtItemFisico = itemPedido.getItemPedidoGradeErroList().get(i).qtItemFisico;
			double qtEstoque  = itemGrade1.qtEstoqueItem;
			if (LavenderePdaConfig.isExibeColunaComQuantidadeExcedenteParaProdutoGrade()) {
				qtEstoque = qtItemFisico - qtEstoque;
			}
			item[1] = getQtdAsString(qtEstoque);
			if (qtItemFisico == 0) {
				item[POSICAO_COLUNA_QUANTIDADE] = "";
			} else {
				item[POSICAO_COLUNA_QUANTIDADE] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(qtItemFisico) : qtItemFisico, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
			}
		}
	}
	
	private String getQtdAsString(double qtd) {
		return StringUtil.getStringValueToInterface(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? ValueUtil.getIntegerValue(qtd) : qtd, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? 0 : ValueUtil.doublePrecisionInterface);
	}

	private void criaDescricaoProduto() {
		try {
			lbDsProduto = new LabelValue(MessageUtil.quebraLinhas(StringUtil.getStringValue(ProdutoService.getInstance().getDescricaoProdutoReferenciaConsideraCodigo(itemPedido.getProduto())), width), CENTER);
		} catch (SQLException e) {
			lbDsProduto = new LabelValue();
		}
	}
	
	private int getGridHeight(int remainingSize) {
		int baseGridHeight = remainingSize == 0 ? FILL - cFundoFooter.getHeight() - lbDsProduto.getHeight() : FILL - cFundoFooter.getHeight() - remainingSize - HEIGHT_GAP;
		int	gradeHeight = (2 + itensGrade1List.size()) * UiUtil.getControlPreferredHeight() + HEIGHT_GAP;
		int baseWindowHeight = this.height - cFundoFooter.getHeight() - lbDsProduto.getY2() - remainingSize;
		boolean gradeHeightTooBig = gradeHeight * 1.2 > baseWindowHeight;
		return gradeHeight > baseWindowHeight || gradeHeightTooBig ? baseGridHeight : gradeHeight;
	}

	private void montaTecladoNumerico() {
		if (LavenderePdaConfig.usaTecladoFixoTelaItemPedido && itemPedido.pedido.isPedidoAberto()) {
			numericPad = new PushButtonGroupBase(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", StringUtil.getStringValue(Settings.decimalSeparator), "<" }, false, -1, -1, UiUtil.defaultGap * 2, 2, true, PushButtonGroup.NORMAL);
			numericPad.borderLarge = true;
			numericPad.setFocusLess(true);
        }
	}
	
	private void montaGridGrade() throws SQLException {
		String dsTipoItemGrade = TipoItemGradeService.getInstance().getDsTipoItemGrade(((ItemGrade)itensGrade1List.items[0]).cdTipoItemGrade);
		GridColDefinition gridColDefinitionDescricao = new GridColDefinition(dsTipoItemGrade, -55, LEFT);
		GridColDefinition gridColDefinitionEstoque = new GridColDefinition(getDescricaoColunaEstoque(), -20, RIGHT);
		GridColDefinition gridColDefinitionQuantidade = new GridColDefinition(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO, -20, RIGHT);
		GridColDefinition[]gridColDefinition = new GridColDefinition[3];
		gridColDefinition[0] = gridColDefinitionDescricao;
		gridColDefinition[POSICAO_COLUNA_QUANTIDADE - 1] = gridColDefinitionEstoque;
		gridColDefinition[POSICAO_COLUNA_QUANTIDADE] = gridColDefinitionQuantidade;
		grid = UiUtil.createGridEdit(gridColDefinition);
		edControlGrid = grid.setColumnEditableDoubleNoCalc(POSICAO_COLUNA_QUANTIDADE, true, 9, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface, 0);
		edControlGrid.autoSelect = true;
		edControlGrid.enableTeclado = !LavenderePdaConfig.usaTecladoFixoTelaItemPedido;
		edControlGrid.alignment = CENTER;
		grid.captionsBackColor = ColorUtil.gridCaptionsBackColor;
		setGridAlignment();
		grid.setGridControllable();
		if (!LavenderePdaConfig.usaOrdenacaoNuSequenciaGradeProduto) {
			grid.disableSort = true;
		}
		grid.gridController.setColBackColor(Color.brighter(Color.DARK, 90), 0);
		grid.gridController.setColBackColor(Color.brighter(Color.DARK, 90), 1);
		grid.gridController.setRowBackColor(Color.brighter(Color.DARK, 70), itensGrade1List.size());
		int rowSize = itensGrade1List.size();
		for (int i = 0; i < rowSize; i++) {
			ItemGrade itemGrade = (ItemGrade)itensGrade1List.items[i];
			grid.add(new String[]{StringUtil.getStringValue(itemGrade.dsItemGrade), "", ""});
		}
		grid.drawHighlight = false;
		grid.gridController.colDisableList.addElement(FIRST_COLUMN);
		grid.gridController.colDisableList.addElement(COLUMN_ESTOQUE);
		grid.gridController.setRowDisable(grid.size());
	}


	private String getDescricaoColunaEstoque() {
		return LavenderePdaConfig.isExibeColunaComQuantidadeExcedenteParaProdutoGrade() ? Messages.QTD_ESTOQUE_EXCEDENTE : Messages.ESTOQUE_TOTAL_ESTOQUE;
	}
	
	private void setGridAlignment() {
		grid.aligns[0] = LEFT;
		grid.aligns[POSICAO_COLUNA_QUANTIDADE - 1] = CENTER;
		grid.aligns[POSICAO_COLUNA_QUANTIDADE] = CENTER;	
	}
	
	public boolean isConfirmouAlteracao() {
		return confirmouAlteracao;
	}

}
