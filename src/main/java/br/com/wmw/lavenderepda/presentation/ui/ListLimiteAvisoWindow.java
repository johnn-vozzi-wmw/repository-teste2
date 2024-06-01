package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoErro;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.util.Vector;

public class ListLimiteAvisoWindow extends WmwListWindow {

	
	private Vector itens;
	private ButtonPopup btSalvar, btCancelar, btOk;
	private Boolean qtdMaximaAtingida;
	public boolean continuar;
	
	public ListLimiteAvisoWindow(Vector itens) {
		this(itens,false);
	}
	
	public ListLimiteAvisoWindow(Vector itens, boolean qtdMaximaAtingida) {
		super(qtdMaximaAtingida ? Messages.ITEMPEDIDO_AVISO_ITEM_ATINGIU_QTD_MAX_PETMITDA : Messages.ITEMPEDIDO_AVISO_ITEM_ATINGIU_LIMITES);
		this.itens = itens;
		btSalvar = new ButtonPopup(Messages.ITEMPEDIDO_REL_LIMITE_ITEM_BOTAO_CONTINUAR);
		btCancelar = new ButtonPopup(FrameworkMessages.BOTAO_CANCELAR);
		btOk = new ButtonPopup(FrameworkMessages.BOTAO_OK);
		this.qtdMaximaAtingida = qtdMaximaAtingida;
		configureListContainer();
		setDefaultRect();
	}
	
	protected void configureListContainer() {
		if (itens != null) {
			ProdutoErro produtoErro = (ProdutoErro) itens.items[0];
			if (produtoErro != null && produtoErro.isItemGrade) {
				listContainer = new GridListContainer(10, 3, false, true);
				listContainer.setColPosition(3, LEFT);
				listContainer.setColPosition(4, CENTER);
				listContainer.setColPosition(5, RIGHT);
			}
		}
		if (listContainer == null) {
			listContainer = new GridListContainer(3, 1, false, true);
		}
		listContainer.setColPosition(1, LEFT);
		listContainer.setColPosition(2, LEFT);
		listContainer.setBarTopSimple();
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) {
		return itens;
	}

	@Override
	protected String[] getItem(Object domain) {
		ProdutoErro produtoErro = (ProdutoErro) domain;
		Vector item = new Vector();
		item.addElement(StringUtil.getStringValue(produtoErro.dsMensagemErro));
		if (produtoErro.isItemGrade) {
			item.addElement("");
			item.addElement("");
			item.addElement(StringUtil.getStringValue(produtoErro.dsItemGrade1));
			item.addElement(StringUtil.getStringValue(produtoErro.dsItemGrade2));
			item.addElement(StringUtil.getStringValue(produtoErro.dsItemGrade3));
			item.addElement("");
			item.addElement("");
		}
		item.addElement(StringUtil.getStringValue(produtoErro.dsVlLimite));
		item.addElement(StringUtil.getStringValue(produtoErro.dsVlAtual));
		return (String[]) item.toObjectArray();
	}

	@Override
	protected String getSelectedRowKey() {
		return null;
	}

	@Override
	protected CrudService getCrudService() {
		return ItemPedidoService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return new ItemPedido();
	}

	@Override
	protected void onFormStart() {
		UiUtil.add(this, listContainer, LEFT, TOP, FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btSalvar) {
					btSalvarClick();
				} else if (event.target == btCancelar || event.target == btOk) {
					continuar = false;
					btFecharClick();
				}
				break;
			} 
			case KeyEvent.SPECIAL_KEY_PRESS: {
				KeyEvent ke = (KeyEvent) event;
				if (ke.isActionKey()) {
					btSalvarClick();
				}
				break;
			}
		}
		
	}

	private void btSalvarClick() throws SQLException {
		continuar = true;
		btFecharClick();
	}
	
	protected void addButtons() {
		if (qtdMaximaAtingida) {
			addButtonPopup(btOk);
		} else {
			addButtonPopup(btSalvar);
			addButtonPopup(btCancelar);
		}
	}
	
	@Override
	protected void addBtFechar() {
	}

}
