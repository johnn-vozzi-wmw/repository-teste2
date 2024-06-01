package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.BonifCfgProduto;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.util.Vector;

import java.sql.SQLException;

public class ListBonifCfgProdutoWindow extends WmwWindow {

	private final ListBonifCfgProdutoForm listBonifCfgProdutoForm;

	public ListBonifCfgProdutoWindow(Vector bonifCfgProdutoList) {
		super(Messages.TITULO_POLITICAS_BONIFICACAO_PRODUTOS);
		listBonifCfgProdutoForm = new ListBonifCfgProdutoForm(bonifCfgProdutoList);
		setDefaultRect();
	}

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, listBonifCfgProdutoForm, LEFT, getTop(), FILL, FILL);
	}

	@Override
	public void unpop() {
		super.unpop();
	}

	@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		switch (event.type) {
			case PenEvent.PEN_UP: {
				unpop();
			}
		}
	}

	public BonifCfgProduto getBonifCfgProdutoSelecionado() {
		return listBonifCfgProdutoForm.bonifCfgProdutoSelecionado;
	}

}
