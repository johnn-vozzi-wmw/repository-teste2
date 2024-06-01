package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.RelVendasProdutoPorCliente;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.RelVendasProdutoPorClienteService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.util.Vector;

public class RelVendasProdutoPorClienteWindow extends WmwWindow {

	public String cdProdutoFilter;
	public BaseGridEdit grid;
	Vector relVendasProdutoPorClienteList;
	private LabelContainer lbDsProdutoContainer;

	public RelVendasProdutoPorClienteWindow(String cdProduto) throws SQLException {
		super(Messages.RELVENDASPRODUTOPORCLIENTE_TITULO);
		cdProdutoFilter = cdProduto;
		lbDsProdutoContainer = new LabelContainer(ProdutoService.getInstance().getDescriptionWithId(cdProdutoFilter));
		setDefaultRect();
	}

	public void initUI() {
	   try {
		super.initUI();
        int oneChar = fm.charWidth('A');
        UiUtil.add(this, lbDsProdutoContainer, LEFT, getTop(), FILL, LabelContainer.getStaticHeight() + 50);
		GridColDefinition[] gridColDefiniton = {
			new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
			new GridColDefinition(Messages.CLIENTE_NOME_ENTIDADE, oneChar * 22, LEFT),
			new GridColDefinition(Messages.RELVENDASPRODUTOPORCLIENTE_DT_EMISSAO, oneChar * 8, LEFT),
			new GridColDefinition(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO, oneChar * 7, LEFT)};
		grid = UiUtil.createGridEdit(gridColDefiniton, false);
		UiUtil.add(this, grid, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
		carregaGrid();
		} catch (Throwable ee) {ee.printStackTrace();}
	}

	public void carregaGrid() throws SQLException {
		grid.clear();
		RelVendasProdutoPorCliente relVendasProdutoPorClienteFilter = new RelVendasProdutoPorCliente();
		relVendasProdutoPorClienteFilter.cdProduto = cdProdutoFilter;
		relVendasProdutoPorClienteList = RelVendasProdutoPorClienteService.getInstance().findAllByExample(relVendasProdutoPorClienteFilter);
		int size = relVendasProdutoPorClienteList.size();
		if (size > 0) {
			RelVendasProdutoPorCliente rel;
			for (int x = 0; x < size; x++) {
				rel = (RelVendasProdutoPorCliente)relVendasProdutoPorClienteList.items[x];
				if (rel != null) {
					String[] item = {"", StringUtil.getStringValue(rel.nmRazaoSocial), StringUtil.getStringValue(rel.dtEmissao), StringUtil.getStringValueToInterface(rel.qtItemFisico)};
					grid.add(item);
				}
			}
			grid.qsort(2, false);
		}
	}

	public void popup() {
		if ((relVendasProdutoPorClienteList != null) && (relVendasProdutoPorClienteList.size() > 0)) {
			super.popup();
		} else {
			UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.MSG_NAO_POSSUI_DADOS, new String[]{ Messages.PRODUTO_NOME_ENTIDADE, Messages.RELVENDASPRODUTOPORCLIENTE_NOME_ENTIDADE}));
		}
	}

	protected void onUnpop() {
		super.onUnpop();
		grid.setItems(null);
	}

}
