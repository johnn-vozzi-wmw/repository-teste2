package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConfig;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoProduto;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.ScrollPosition;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListNovoProdutoPesquisaMercadoForm extends LavendereCrudListForm {

	private final PesquisaMercadoConfig pesquisaMercadoConfig;
	private final String nuPedido;
	private final CadPesquisaMercadoProdutoForm cadPesquisaMercadoProdutoForm;
	private boolean listInicialized;

	public ListNovoProdutoPesquisaMercadoForm(PesquisaMercadoConfig pesquisaMercadoConfig, String nuPedido, CadPesquisaMercadoProdutoForm cadPesquisaMercadoProdutoForm) throws SQLException {
		super(Messages.PESQUISA_MERCADO_NOVO_PRODUTO_TITLE);
		singleClickOn = true;
		listResizeable = false;
		this.pesquisaMercadoConfig = pesquisaMercadoConfig;
		this.nuPedido = nuPedido;
		this.cadPesquisaMercadoProdutoForm = cadPesquisaMercadoProdutoForm;
		constructorListContainer();
	}

	private void constructorListContainer() {
		ScrollPosition.AUTO_HIDE = false;
		listContainer = new GridListContainer(2, 2);
		String[][] matriz = new String[2][2];
		final String cdProdutoAttribute = "CDPRODUTO";
		final String dsProdutoAttribute = "DSPRODUTO";
		matriz[0][0] = Messages.PRODUTO_LABEL_CODIGO;
		matriz[0][1] = cdProdutoAttribute;
		matriz[1][0] = Messages.PRODUTO_LABEL_DSPRODUTO;
		matriz[1][1] = dsProdutoAttribute;
		listContainer.setColsSort(matriz);
		sortAtributte = cdProdutoAttribute;
		sortAsc = ValueUtil.VALOR_SIM;
		ScrollPosition.AUTO_HIDE = true;
	}

	@Override
	public void initUI() {
		super.initUI();
		listInicialized = true;
	}

	@Override
	public void list() throws SQLException {
		if (listInicialized) {
			super.list();
		}
	}

	@Override
	protected Vector getDomainList() throws SQLException {
		return ProdutoService.getInstance().findProdutosNotInPesquisaMercado(pesquisaMercadoConfig, edFiltro.getValue(), sortAtributte, sortAsc);
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return getDomainList();
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		String[] item = new String[2];
		Produto produto = (Produto) domain;
		item[0] = produto.cdProduto + " - ";
		item[1] = produto.dsProduto;
		return item;
	}

	@Override
	protected String getToolTip(BaseDomain domain) throws SQLException {
		Produto produto = (Produto) domain;
		return MessageUtil.getMessage(Messages.PESQUISA_MERCADO_PROD_CONC_PRODUTO_CODIGO_LABEL, new String[] {produto.cdProduto, produto.dsProduto});
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		return new PesquisaMercadoConfig();
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return ProdutoService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, edFiltro, getLeft(), getTop() + HEIGHT_GAP_BIG);
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
	}
	
	@Override
	protected void filtrarClick() throws SQLException {
		list();
	}

	@Override
	public void detalhesClick() throws SQLException {
		Produto produto = (Produto) getSelectedDomain();
		close();
		PesquisaMercadoProduto pesquisaMercadoProduto = new PesquisaMercadoProduto();
		pesquisaMercadoProduto.cdEmpresa = pesquisaMercadoConfig.cdEmpresa;
		pesquisaMercadoProduto.cdPesquisaMercadoConfig = pesquisaMercadoConfig.cdPesquisaMercadoConfig;
		pesquisaMercadoProduto.cdProduto = produto.cdProduto;
		pesquisaMercadoProduto.dsProduto = produto.dsProduto;
		CadPesquisaMercadoConcorrenteFormWindow cadPesquisaMercadoConcorrenteFormWindow = new CadPesquisaMercadoConcorrenteFormWindow(pesquisaMercadoConfig, pesquisaMercadoProduto, null, nuPedido, true, false);
		cadPesquisaMercadoConcorrenteFormWindow.popup();
		cadPesquisaMercadoProdutoForm.btFiltrarClick();
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		// TODO Auto-generated method stub
	}
	
}
