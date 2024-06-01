package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.SyncException;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.dto.EstoqueDto;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

import java.sql.SQLException;

public class ListEstoqueFiliaisWindow extends BaseCrudListForm {

	public LabelContainer estoquesContainer;
	private Produto produto;

	public ListEstoqueFiliaisWindow(Produto produto) throws SQLException {
		super(Messages.TITULO_TELA_LINHA_ESTOQUE_FILIAIS);
		estoquesContainer = new LabelContainer(produto.toString());
		this.produto = produto;
		int oneChar = fm.charWidth('A');
		GridColDefinition[] gridColDefiniton = new GridColDefinition[]{
				new GridColDefinition(Messages.EMPRESA_NOME_ENTIDADE, oneChar * 20, LEFT),
				new GridColDefinition(Messages.ESTOQUE_NOME_ENTIDADE, oneChar * 3, LEFT)
		};
		gridEdit = UiUtil.createGridEdit(gridColDefiniton);
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, estoquesContainer, LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
		UiUtil.add(this, gridEdit);
		gridEdit.setRect(LEFT, AFTER + HEIGHT_GAP, FILL, FILL - (barBottomContainer.getHeight() + HEIGHT_GAP));
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {

	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return EstoqueService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		return new Estoque();
	}

	@Override
	protected Vector getDomainList() throws java.sql.SQLException {
		try {
			String retorno = SyncManager.buscaEstoqueFiliais(produto.cdProduto);
			if (ValueUtil.isNotEmpty(retorno) && !retorno.startsWith("ERRO:")) {
				return EstoqueService.getInstance().getEstoqueListByJoson(retorno);
			}
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e.getMessage());
			voltarClick();
		}
		return new Vector();
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		EstoqueDto estoqueDto = (EstoqueDto) domain;
		String[] item = null;
		item = new String[]{
				StringUtil.getStringValue(estoqueDto.cdEmpresa + (estoqueDto.nmEmpresaCurto == null ? " " : " - " + estoqueDto.nmEmpresaCurto)),
				StringUtil.getStringValue(estoqueDto.qtEstoque)
		};
		return item;
	}

	@Override
	public String[][] getItems(String[][] items) throws SQLException {
		String[][] itensFinal = new String[items.length][3];
		for (int i = 0; i < items.length; i++) {
			itensFinal[i][0] = items[i][0];
			itensFinal[i][1] = items[i][1];
		}
		items = null;
		return itensFinal;
	}

	@Override
	public void onFormClose() throws SQLException {
		if (estoquesContainer != null) {
			estoquesContainer.clear();
			estoquesContainer = null;
		}
		produto = null;
		super.onFormClose();
	}
}
