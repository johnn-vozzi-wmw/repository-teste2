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
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ItemNfDevolucao;
import br.com.wmw.lavenderepda.business.domain.NfDevolucao;
import br.com.wmw.lavenderepda.business.service.ItemNfDevolucaoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListItensNfDevolucaoForm extends BaseCrudListForm {
	
	private NfDevolucao nfDevolucao;
	
	public ListItensNfDevolucaoForm(NfDevolucao nfDevolucao) {
		super(ValueUtil.VALOR_NI);
		this.nfDevolucao = nfDevolucao;
		listContainer = new GridListContainer(5, 2);
		listContainer.setColPosition(3, RIGHT);
		listContainer.setColPosition(4, RIGHT);
		listResizeable = false;
	}

	@Override
	protected BaseDomain getDomainFilter() {
		ItemNfDevolucao itemNfDelovucao = new ItemNfDevolucao();
		Cliente cliente = SessionLavenderePda.getCliente();
		itemNfDelovucao.cdEmpresa = cliente.cdEmpresa;
		itemNfDelovucao.cdRepresentante = cliente.cdRepresentante;
		itemNfDelovucao.nuNfDevolucao = nfDevolucao.nuNfDevolucao;
		return itemNfDelovucao;
	}
	
	@Override
	protected Vector getDomainList() throws SQLException {
		return ItemNfDevolucaoService.getInstance().findAllByExample(getDomainFilter());
	}
	
	@Override
	protected String[] getItem(Object domain) throws SQLException {
		ItemNfDevolucao itemNfDelovucao = (ItemNfDevolucao) domain;
		String descricaoProduto = StringUtil.getStringValue(ProdutoService.getInstance().getDsProduto(itemNfDelovucao.cdProduto));

		if (descricaoProduto.equals(itemNfDelovucao.cdProduto)) {
			descricaoProduto = Messages.TITULOFINANCEIRO_TAB_ITENS_NF_DESCRICAO_INDISPONIVEL;
		}
		return new String[] {itemNfDelovucao.cdProduto, " - " + descricaoProduto,
			Messages.TITULOFINANCEIRO_LABEL_UN + StringUtil.getStringValueToInterface(itemNfDelovucao.qtItem, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface),
			Messages.TITULOFINANCEIRO_LABEL_VL_UNITARIO + StringUtil.getStringValueToInterface(itemNfDelovucao.vlUnitItem), 
			Messages.TITULOFINANCEIRO_LABEL_VL_TOTAL + StringUtil.getStringValueToInterface(itemNfDelovucao.vlTotalItem)};
	}

	@Override
	protected CrudService getCrudService() {
		return ItemNfDevolucaoService.getInstance();
	}

	@Override
	protected void onFormStart() {
		UiUtil.add(this, listContainer, LEFT, TOP, FILL, FILL);
		
	}
	
	@Override
	protected String getToolTip(BaseDomain domain) throws SQLException {
		ItemNfDevolucao itemNfDelovucao = (ItemNfDevolucao) domain;
		return ProdutoService.getInstance().getDsProduto(itemNfDelovucao.cdProduto);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		
	}

}
