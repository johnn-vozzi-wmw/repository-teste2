package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListItemPedidoAvisoDescAcrMaximoWindow extends WmwWindow {

	private ListItemPedidoAvisoDescAcrMaximoForm listItemPedidoAvisoDescAcrMaximoForm;
	private final ButtonPopup btConfirmar;
	private final ButtonPopup btCancelar;
	
	public ListItemPedidoAvisoDescAcrMaximoWindow(Vector itemPedidoList) {
		super(Messages.REL_ITENS_AGUARDANDO_CONFIRMACAO);
		listItemPedidoAvisoDescAcrMaximoForm = new ListItemPedidoAvisoDescAcrMaximoForm(itemPedidoList);
		btConfirmar = new ButtonPopup(Messages.BOTAO_LABEL_CONFIRMAR_SELECAO);
		btCancelar = new ButtonPopup(Messages.BOTAO_CANCELAR);
		setDefaultRect();
	}
	
	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, listItemPedidoAvisoDescAcrMaximoForm, LEFT, getTop(), FILL, FILL);
		addButtonPopup(btConfirmar);
		addButtonPopup(btCancelar);
	}
	
	@Override
	public void onWindowEvent(Event event) throws SQLException {
		super.onWindowEvent(event);
		if (event.type == ControlEvent.PRESSED) {
			if (event.target == btConfirmar) {
				preencheListaDeItensSelecionados();
				fecharWindow();
			}
			if (event.target == btCancelar) {
				fecharWindow();
			}
		}
	}
	
	@Override
	protected void addBtFechar() {
		//Trocado pelo botao de Cancelar
	}

	private void preencheListaDeItensSelecionados() {
		listItemPedidoAvisoDescAcrMaximoForm.preencheListaDeItensSelecionados();
	}

	public Vector getItemPedidoSelecionadosList() {
		return listItemPedidoAvisoDescAcrMaximoForm.getItemPedidoSelecionadosList();
	}
}
