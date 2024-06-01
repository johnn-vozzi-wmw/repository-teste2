package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.PedidoEstoqueService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListProdutosDevolucaoForm extends BaseCrudListForm {
	
	private boolean parcial;
	private ButtonAction btOk;
	private Vector produtoList;

	public ListProdutosDevolucaoForm(boolean parcial, Vector produtoList) {
		super(Messages.REMESSAESTOQUE_DEVOLUCAO_ESTOQUE);
		this.parcial = parcial;
		this.produtoList = produtoList;
		listContainer = new GridListContainer(2, 1);
		listContainer.setBarTopSimple();
		btOk = new ButtonAction(Messages.REMESSAESTOQUE_DEVOLVER, "images/ok.png");
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		return new Produto();
	}
	
	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		if (produtoList == null) {
			produtoList = ProdutoService.getInstance().getProdutoListDevolucao(parcial);
		}
		return produtoList;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return ProdutoService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(barBottomContainer, btOk, 5);
		LabelValue texto = new LabelValue();
		UiUtil.add(this, texto, getLeft(), getTop());
		texto.setMultipleLinesText(parcial ? Messages.REMESSAESTOQUE_TEXTO_DEVOLUCAO_PARCIAL : Messages.REMESSAESTOQUE_TEXTO_DEVOLUCAO_TOTAL);
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL - barBottomContainer.getHeight());
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == btOk) {
				try {
					if (UiUtil.showConfirmYesNoMessage(Messages.REMESSAESTOQUE_CONFIRMACAO_DEVOLUCAO)) {
						UiUtil.showProcessingMessage();
						PedidoEstoqueService.getInstance().geraDevolucao(parcial, produtoList);
						UiUtil.showSucessMessage(Messages.REMESSAESTOQUE_DEVOLUCAO_SUCESSO);
						((BaseCrudListForm)prevContainer).list();
						voltarClick();
					}
				} finally {
					UiUtil.unpopProcessingMessage();
				}
			}
			break;
		}
	}
	
	@Override
	protected String[] getItem(Object domain) throws SQLException {
		Produto produto = (Produto) domain;
		return new String[] {
				StringUtil.getStringValue(produto), Messages.PRODUTO_INFO_LABEL_QTD + " " + StringUtil.getStringValueToInterface(produto.qtEstoqueProduto)
			};
	}

}
