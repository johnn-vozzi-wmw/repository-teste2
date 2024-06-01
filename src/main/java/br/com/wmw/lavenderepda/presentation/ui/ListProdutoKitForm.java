package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoKit;
import br.com.wmw.lavenderepda.business.service.ProdutoKitService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListProdutoKitForm extends BaseCrudListForm {

	public LabelName lbTotal;
	public LabelValue lvQtKitGrid;
	private Produto produtoBase;

    public ListProdutoKitForm(Produto produto) {
        super(Messages.PRODUTOKIT_TITULO_CADASTRO);
        produtoBase = produto;
		lvQtKitGrid = new LabelValue("99999");
		lbTotal = new LabelName(Messages.LABEL_TOTAL);
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return ProdutoKitService.getInstance();
    }

    //@Override
    protected Vector getDomainList() throws java.sql.SQLException {
    	ProdutoKit produtoKit = new ProdutoKit();
    	produtoKit.cdEmpresa = produtoBase.cdEmpresa;
    	produtoKit.cdRepresentante = produtoBase.cdRepresentante;
    	produtoKit.cdKit = produtoBase.cdProduto;
        return getCrudService().findAllByExample(produtoKit);
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
    	ProdutoKit descontoQuantidade = (ProdutoKit) domain;
        String[] item = {
                StringUtil.getStringValue(ProdutoService.getInstance().getDsProduto(descontoQuantidade.cdProduto)),
                StringUtil.getStringValueToInterface(descontoQuantidade.qtItemFisico)};
        return item;
    }

    //@Override
    protected String getSelectedRowKey() throws SQLException {
        String[] item = gridEdit.getSelectedItem();
        return item[0];
    }

    //@Override
    protected void onFormStart() throws SQLException {
    	UiUtil.add(this, lbTotal, LEFT + WIDTH_GAP, BOTTOM);
    	UiUtil.add(this, lvQtKitGrid, AFTER + WIDTH_GAP, BOTTOM, FILL);
    	//--
        GridColDefinition[] gridColDefiniton = {
            new GridColDefinition(Messages.PRODUTOKIT_LABEL_CDPRODUTO, -80, LEFT),
            new GridColDefinition(Messages.PRODUTOKIT_LABEL_QTITEMFISICO_ORIG, -20, LEFT)};
        gridEdit = UiUtil.createGridEdit(gridColDefiniton);
        UiUtil.add(this, gridEdit);
        gridEdit.setRect(LEFT, getTop(), FILL, FILL - lvQtKitGrid.getHeight() - HEIGHT_GAP);
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    }

	protected BaseDomain getDomainFilter() throws SQLException {
		return null;
	}

	public void visibleState() {
		super.visibleState();
		barBottomContainer.setVisible(false);
	}

	public void list() throws SQLException {
		super.list();
        sumQtItemKitGrid();
	}

	private void sumQtItemKitGrid() {
		int gridSize = gridEdit.size();
		double qtItemKitAtual = 0;
		for (int i = 0; i < gridSize; i++) {
			String[] item = gridEdit.getItem(i);
			qtItemKitAtual += ValueUtil.getDoubleValue(item[1]);
		}
		lvQtKitGrid.setValue(qtItemKitAtual);
	}

}
