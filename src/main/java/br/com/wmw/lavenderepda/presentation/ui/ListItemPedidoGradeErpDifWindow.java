package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGradeErpDif;
import br.com.wmw.lavenderepda.business.service.ItemPedidoGradeErpDifService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.TipoItemGradeService;
import totalcross.ui.gfx.Color;
import totalcross.util.Vector;

public class ListItemPedidoGradeErpDifWindow extends WmwWindow {
	
	private ItemPedido itemPedido;
	private Vector itensPedidoGradeErpDif1List;
	private LabelValue lbDsProduto;
	private BaseGridEdit grid;

	public ListItemPedidoGradeErpDifWindow(ItemPedido itemPedido) throws SQLException {
		super(Messages.LISTA_ITEMPEDIDOGRADEERPDIF_TITULOGRADE);
		this.itemPedido = itemPedido;
		carregaItemPedidoGradeErpDifList();
		scrollable = false;
		montaGridGrade1();
		carregaGridGrade1();
		updateTotaisGrade1();
		setDefaultWideRect();
	}
	
	private void carregaItemPedidoGradeErpDifList() {
		itensPedidoGradeErpDif1List = ItemPedidoGradeErpDifService.getInstance().findAllItemPedidoGradePorItemPedido(itemPedido);
	}

	@Override
	public void initUI() {
		super.initUI();
		addBtFechar();
		criaDescricaoProduto();
		UiUtil.add(this, lbDsProduto, getLeft(), getNextY(), FILL, PREFERRED);
		if (grid == null) return; 
		UiUtil.add(this, grid, LEFT + HEIGHT_GAP, AFTER + HEIGHT_GAP, FILL, getGridHeight());
	}
	
	private void carregaGridGrade1() throws SQLException {
		int size = grid.size();
		for (int i = 0; i < size; i++) {
			String[] item = grid.getItem(i);
			ItemPedidoGradeErpDif itemPedidoGrade1ErpDif = (ItemPedidoGradeErpDif) itensPedidoGradeErpDif1List.items[i];
			item[1] = getQtdAsString(itemPedidoGrade1ErpDif.qtItemFisicoOrg);
			item[2] = getQtdAsString(itemPedidoGrade1ErpDif.qtItemFisicoErp);
		}
		
	}
	
	private void updateTotaisGrade1() {
		double qtTotalItemOrg = 0;
		double qtTotalItemErp = 0;
		String[] itemTotais = new String[3];
        itemTotais[0] = Messages.LABEL_TOTAL_GRIDEDIT;
		int size = grid.size();
        for (int i = 0; i < size; i++) {
        	double qtItemFisico = ValueUtil.getDoubleValue(grid.getItem(i)[1].replace(".", "")); 
        	double qtItemErp = ValueUtil.getDoubleValue(grid.getItem(i)[2].replace(".", "")); 
        	qtTotalItemOrg += qtItemFisico;
        	qtTotalItemErp += qtItemErp;
        }
    	itemTotais[1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? ValueUtil.getIntegerValue(qtTotalItemOrg) : qtTotalItemOrg, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? 0 : ValueUtil.doublePrecisionInterface);
        itemTotais[2] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(qtTotalItemErp) : qtTotalItemErp, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
        grid.add(itemTotais, size);
        grid.gridController.setRowDisable(grid.size());
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
	
	private int getGridHeight() {
		int baseGridHeight = FILL - cFundoFooter.getHeight() - lbDsProduto.getHeight();
		int	gradeHeight = (2 + itensPedidoGradeErpDif1List.size()) * UiUtil.getControlPreferredHeight() + HEIGHT_GAP;
		int baseWindowHeight = this.height - cFundoFooter.getHeight() - lbDsProduto.getY2();
		boolean gradeHeightTooBig = gradeHeight * 1.2 > baseWindowHeight;
		return gradeHeight > baseWindowHeight || gradeHeightTooBig ? baseGridHeight : gradeHeight;
	}
	
	private void montaGridGrade1() throws SQLException {
		grid = UiUtil.createGridEdit(getGridGrade1Definition());
		grid.captionsBackColor = ColorUtil.gridCaptionsBackColor;
		setGridAlignment();
		grid.setGridControllable();
		grid.gridController.setRowBackColor(Color.brighter(Color.DARK, 50), itensPedidoGradeErpDif1List.size());
		int rowSize = itensPedidoGradeErpDif1List.size();
		for (int i = 0; i < rowSize; i++) {
			ItemPedidoGradeErpDif itemPedidoGradeErpDif = (ItemPedidoGradeErpDif) itensPedidoGradeErpDif1List.items[i];
			grid.add(new String[]{itemPedidoGradeErpDif.itemGrade1.dsItemGrade, "", ""});
		}
		grid.drawHighlight = false;
		grid.gridController.colDisableList.addElement(0);
		grid.gridController.colDisableList.addElement(1);
		grid.gridController.colDisableList.addElement(2);
	}


	private GridColDefinition[] getGridGrade1Definition() {
		String dsTipoItemGrade = loadDsTipoItemGrade1();
		GridColDefinition gridColDefinitionDescricao = new GridColDefinition(dsTipoItemGrade, -55, LEFT);
		GridColDefinition gridColDefinitionQtdOrig = new GridColDefinition(Messages.LISTA_ITEMPEDIDOGRADEERPDIF_QTDORIG, -20, RIGHT);
		GridColDefinition gridColDefinitionQtdErp = new GridColDefinition(Messages.LISTA_ITEMPEDIDOGRADEERPDIF_QTDERP, -20, RIGHT);
		GridColDefinition[] gridColDefinition = new GridColDefinition[3];
		gridColDefinition[0] = gridColDefinitionDescricao;
		gridColDefinition[1] = gridColDefinitionQtdOrig;
		gridColDefinition[2] = gridColDefinitionQtdErp;
		return gridColDefinition;
	}

	private String loadDsTipoItemGrade1() {
		if (ValueUtil.isEmpty(itensPedidoGradeErpDif1List)) return ValueUtil.VALOR_NI;
		String dsTipoItemGrade = ValueUtil.VALOR_NI;
		try {
			dsTipoItemGrade = TipoItemGradeService.getInstance().getDsTipoItemGrade(((ItemPedidoGradeErpDif) itensPedidoGradeErpDif1List.items[0]).itemGrade1.cdTipoItemGrade);
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.ITEMGRADEERPDIF_ERRO_CARREGAMENTO_TIPO_GRADE, ex.getMessage()));
		}
		return dsTipoItemGrade;
	}
	
	private void setGridAlignment() {
		grid.aligns[0] = LEFT;
		grid.aligns[1] = CENTER;
		grid.aligns[2] = CENTER;	
	}

}
