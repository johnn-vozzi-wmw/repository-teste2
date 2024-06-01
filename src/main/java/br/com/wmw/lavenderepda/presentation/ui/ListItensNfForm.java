package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ItemTituloFinan;
import br.com.wmw.lavenderepda.business.domain.TituloFinanceiro;
import br.com.wmw.lavenderepda.business.service.ItemTituloFinanService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListItensNfForm extends BaseCrudListForm {
	
	public TituloFinanceiro tituloFinanceiro;
	
	public ListItensNfForm(TituloFinanceiro tituloFinanceiro) {
		super(ValueUtil.VALOR_NI);
		this.tituloFinanceiro = tituloFinanceiro;
		listContainer = new GridListContainer(5, 2);
		listContainer.setColPosition(3, RIGHT);
		listResizeable = false;
	}

	@Override
	protected BaseDomain getDomainFilter() {
		ItemTituloFinan itemTituloFinanceiro = new ItemTituloFinan();
		Cliente cliente = SessionLavenderePda.getCliente();
		itemTituloFinanceiro.cdEmpresa = cliente.cdEmpresa;
		itemTituloFinanceiro.cdRepresentante = cliente.cdRepresentante;
		itemTituloFinanceiro.cdCliente = cliente.cdCliente;
		itemTituloFinanceiro.cdTitulo = tituloFinanceiro.getHashValuesDinamicos().getString("NUTITULO");
		itemTituloFinanceiro.nuNF = tituloFinanceiro.getHashValuesDinamicos().getString("NUNF");
		return itemTituloFinanceiro;
	}
	
	@Override
	protected Vector getDomainList() throws SQLException {
		return ItemTituloFinanService.getInstance().findAllByExample(getDomainFilter());
	}
	
	@Override
	protected String[] getItem(Object domain) throws SQLException {
		ItemTituloFinan itemTituloFinanceiro = (ItemTituloFinan) domain;
		String descricaoProduto = StringUtil.getStringValue(ProdutoService.getInstance().getDsProduto(itemTituloFinanceiro.cdItem));
		if (descricaoProduto.equals(itemTituloFinanceiro.cdItem)) {
			descricaoProduto = Messages.TITULOFINANCEIRO_TAB_ITENS_NF_DESCRICAO_INDISPONIVEL;
		}
		return new String[] {itemTituloFinanceiro.cdItem, " - " + descricaoProduto,
				Messages.TITULOFINANCEIRO_LABEL_UN + StringUtil.getStringValueToInterface(itemTituloFinanceiro.qtdItem),
				Messages.TITULOFINANCEIRO_LABEL_VL_UNITARIO + StringUtil.getStringValueToInterface(itemTituloFinanceiro.vlUnitItem), 
				Messages.TITULOFINANCEIRO_LABEL_VL_TOTAL + StringUtil.getStringValueToInterface(itemTituloFinanceiro.vlTotalItem)};
	}

	@Override
	protected CrudService getCrudService() {
		return ItemTituloFinanService.getInstance();
	}

	@Override
	protected void onFormStart() {
		UiUtil.add(this, listContainer, LEFT, TOP, FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) {
	}
	
	@Override
	protected void initCabecalhoRodape() {
	}
	
	@Override
	public void visibleState() {
	}
	
	@Override
	protected String getToolTip(BaseDomain domain) throws SQLException {
		ItemTituloFinan itemTituloFinanceiro = (ItemTituloFinan) domain;
		return ProdutoService.getInstance().getDsProduto(itemTituloFinanceiro.cdItem);
	}

}
