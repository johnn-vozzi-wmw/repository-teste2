package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.List;

import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class InfoEstoqueProdutoForm extends BaseUIForm {
	
	private BaseGridEdit grid;
	private Produto produto;
	private LabelContainer lbDsProdutoContainer;

	public InfoEstoqueProdutoForm(Produto produto) throws SQLException {
		super(Messages.ESTOQUE_INFO_PRODUTO);
		this.produto = produto;
		lbDsProdutoContainer = new LabelContainer(this.produto.toString());
	}
	
	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, lbDsProdutoContainer, LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
		GridColDefinition[] gridColDefiniton = {
			new GridColDefinition(Messages.INFO_LOCAL_ESTOQUE, -50, CENTER),
			new GridColDefinition(Messages.INFO_QUANTIDADE_DISPONIVEL, -25, CENTER),
		};
		grid = UiUtil.createGridEdit(gridColDefiniton, false);
		UiUtil.add(this, grid, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
		carregar();
	}

	private void carregar() throws SQLException {
		Estoque estoqueFilter = new Estoque();
		estoqueFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		estoqueFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		estoqueFilter.cdProduto = produto.cdProduto;
		List<Object[]> listEstoquePorLocal =  EstoqueService.getInstance().buscaSomaEstoqueLocalPorProduto(estoqueFilter);
		for (Object[] object : listEstoquePorLocal) {
			String[] itemGrid = new String[2];
			itemGrid[0] = StringUtil.getStringValue(object[0]);
			itemGrid[1] = StringUtil.getStringValue(object[1]);
			grid.add(itemGrid);
		}
	}
	
	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED:
				break;
			default:
				break;
		}
	}
	
}
