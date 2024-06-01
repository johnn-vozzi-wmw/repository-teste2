package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemKitPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoKit;
import br.com.wmw.lavenderepda.business.service.ProdutoKitService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.sys.Convert;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class ListProdutoKitWindow extends WmwWindow {

	private static final String DEFAULT_SIZE = "99999";
	public LabelName lbQtKitOriginal;
	public LabelName lbQtKitAtual;
	public EditNumberFrac edQtKitGrid;
	public EditNumberFrac edQtKitOriginal;
	public EditNumberFrac edQtKitAtual;
	private BaseGridEdit grid;
	private LabelName lbDsProduto;
	private ButtonPopup btConfirmar;
	private ItemPedido itemPedido;
	private boolean readOnly;

	public ListProdutoKitWindow(ItemPedido itemPedido, boolean readOnly) throws SQLException {
		super(Messages.PRODUTOKIT_TITULO_CADASTRO);
		this.itemPedido = itemPedido;
		this.readOnly = readOnly;
		edQtKitGrid = new EditNumberFrac(DEFAULT_SIZE, 9);
		edQtKitOriginal = new EditNumberFrac(DEFAULT_SIZE, 9);
		edQtKitOriginal.setEnabled(false);
		edQtKitAtual = new EditNumberFrac(DEFAULT_SIZE, 9);
		edQtKitAtual.setEnabled(false);
		lbQtKitOriginal = new LabelName(Messages.PRODUTOKIT_TOTAL_ORIGINAL);
		lbQtKitAtual = new LabelName(Messages.PRODUTOKIT_TOTAL_ATUAL);
		lbDsProduto = new LabelName(Convert.insertLineBreak(width - 30, fm, itemPedido.cdProduto + " - " + itemPedido.getProduto().getDescription()), CENTER);
		btConfirmar = new ButtonPopup(FrameworkMessages.BOTAO_OK);
		btFechar.setText(FrameworkMessages.BOTAO_CANCELAR);
		setDefaultRect();
	}
	
	@Override
	protected void addBtFechar() {
		if (!readOnly) {
			addButtonPopup(btConfirmar);
			if (!LavenderePdaConfig.isUsaKitBaseadoNoProdutoOcultaQtAtual()) {
				super.addBtFechar();
			}
		} else {
			super.addBtFechar();
		}
	}

	@Override
	public void initUI() {
		try {
			super.initUI();
			if (LavenderePdaConfig.isUsaKitBaseadoNoProdutoOcultaQtAtual()) {
				UiUtil.add(this, edQtKitOriginal, RIGHT - WIDTH_GAP, BOTTOM - WIDTH_GAP, PREFERRED + WIDTH_GAP_BIG * 2,
						UiUtil.getLabelPreferredHeight());
				UiUtil.add(this, lbQtKitOriginal, BEFORE - WIDTH_GAP, SAME);
			} else {
				UiUtil.add(this, edQtKitAtual, RIGHT - WIDTH_GAP, BOTTOM - WIDTH_GAP, PREFERRED + WIDTH_GAP_BIG * 2, UiUtil.getLabelPreferredHeight());
				UiUtil.add(this, lbQtKitAtual, BEFORE - WIDTH_GAP, SAME);
				UiUtil.add(this, edQtKitOriginal, BEFORE - WIDTH_GAP_BIG, SAME, PREFERRED + WIDTH_GAP_BIG * 2, UiUtil.getLabelPreferredHeight());
				UiUtil.add(this, lbQtKitOriginal, BEFORE - WIDTH_GAP, SAME);
			}
			UiUtil.add(this, lbDsProduto, LEFT, TOP + HEIGHT_GAP, FILL, PREFERRED);
			GridColDefinition[] gridColDefiniton = { new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
					!LavenderePdaConfig.isUsaKitBaseadoNoProdutoOcultaQtAtual()
							? new GridColDefinition(Messages.PRODUTOKIT_LABEL_CDPRODUTO, -50, LEFT)
							: new GridColDefinition(Messages.PRODUTOKIT_LABEL_CDPRODUTO, -76, LEFT),
					new GridColDefinition(Messages.PRODUTOKIT_LABEL_QTITEMFISICO_ORIG, -24, LEFT),
					!LavenderePdaConfig.isUsaKitBaseadoNoProdutoOcultaQtAtual()
							? new GridColDefinition(Messages.PRODUTOKIT_LABEL_QTITEMFISICO_ATUAL, -26, LEFT)
							: new GridColDefinition("", 0, LEFT) };
			grid = UiUtil.createGridEdit(gridColDefiniton, false);
			grid.extraHorizScrollButtonHeight = 0;
			grid.useZeroAsEmpty = false;
			UiUtil.add(this, grid, LEFT, AFTER + HEIGHT_GAP_BIG, FILL,
					height - (height - edQtKitOriginal.getY()) - lbDsProduto.getY2() - HEIGHT_GAP_BIG - HEIGHT_GAP);
			if (!readOnly) {
				grid.setGridControllable();
				grid.gridController.setColForeColor(LavendereColorUtil.baseForeColorSystem, 3);
				edQtKitGrid = grid.setColumnEditableDouble(3, true, 9);
				edQtKitGrid.autoSelect = true;
			}
			carregaComponentsKitProduto();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reposition() {
		super.reposition();
		grid.setColumnWidth(2, -24);
		if (!LavenderePdaConfig.isUsaKitBaseadoNoProdutoOcultaQtAtual()) {
			grid.setColumnWidth(1, -50);
			grid.setColumnWidth(3, -26);
		} else {
			grid.setColumnWidth(1, -76);
			grid.setColumnWidth(3, 0);
		}
		grid.setRect(grid.getX(), grid.getY(), FILL, height - (height - edQtKitOriginal.getY()) - lbDsProduto.getY2() - HEIGHT_GAP_BIG - HEIGHT_GAP);
	}

	//@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btConfirmar) {
					if (validateTotalOriginal()) {
						screenToDomain();
						unpop();
					}
				}
				break;
			}
			case ControlEvent.FOCUS_OUT: {
				if (event.target == edQtKitGrid) {
					sumQtItemKitGrid();
				}
				break;
			}
		}
	}

	public void clearGrid() {
		grid.clear();
		grid.setSelectedIndex(-1);
	}

	public void carregaComponentsKitProduto() throws SQLException {
		clearGrid();
		double qtItemFisicoOrg = 0;
		double qtItemFisicoAtual = 0;
		if (!ValueUtil.isEmpty(itemPedido.itemKitPedidoList)) {
			int size = itemPedido.itemKitPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemKitPedido itemKitPedido = (ItemKitPedido)itemPedido.itemKitPedidoList.items[i];
				ProdutoKit produtoKit = ProdutoKitService.getInstance().getProdutoKitByItemKitPedido(itemKitPedido);
				String[] item = new String[4];
				item[0] = itemKitPedido.getRowKey();
				item[1] = ProdutoService.getInstance().getDescriptionWithId(itemKitPedido.cdProduto);
				item[2] = "";
				if (produtoKit != null) {
					item[2] = StringUtil.getStringValueToInterface(produtoKit.qtItemFisico);
					qtItemFisicoOrg += produtoKit.qtItemFisico;
				}
				item[3] = StringUtil.getStringValueToInterface(itemKitPedido.qtItemFisico);
				qtItemFisicoAtual += itemKitPedido.qtItemFisico;
				grid.add(item);
			}
		}
		edQtKitOriginal.setValue(qtItemFisicoOrg);
		edQtKitAtual.setValue(qtItemFisicoAtual);
	}

	private void sumQtItemKitGrid() {
		int gridSize = grid.size();
		double qtItemKitAtual = 0;
		for (int i = 0; i < gridSize; i++) {
			String[] item = grid.getItem(i);
			qtItemKitAtual += ValueUtil.getDoubleValue(item[3]);
		}
		edQtKitAtual.setValue(qtItemKitAtual);
	}

	private void screenToDomain() {
		for (int i = 0; i < grid.size(); i++) {
			String[] item = grid.getItem(i);
			for (int j = 0; j < itemPedido.itemKitPedidoList.size(); j++) {
				ItemKitPedido itemKitPedido = (ItemKitPedido)itemPedido.itemKitPedidoList.items[j];
				if (item[0].equals(itemKitPedido.getRowKey())) {
					itemKitPedido.qtItemFisico = ValueUtil.getDoubleValue(item[3]);
					break;
				}
			}
		}
	}

	private boolean validateTotalOriginal() {
		if (!LavenderePdaConfig.isUsaKitBaseadoNoProdutoOcultaQtAtual() && (edQtKitAtual.getValueDouble() != edQtKitOriginal.getValueDouble())) {
				UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.PRODUTOKIT_MSG_QTDE_ATUAL_DIFERENTE_ORIGINAL, new String[]{StringUtil.getStringValueToInterface(edQtKitAtual.getValueDouble()), StringUtil.getStringValueToInterface(edQtKitOriginal.getValueDouble())}));
				return false;
		}
		return true;
	}

}