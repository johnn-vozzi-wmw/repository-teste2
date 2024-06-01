package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
public class CadCargaPedidoWindow extends WmwWindow {
	
	public CadCargaPedidoForm cadCargaPedidoForm;
	private ButtonPopup btSalvar;
	private ButtonPopup btCancelar;
	
	public boolean novaCargaCriada;
	
	public CadCargaPedidoWindow(String cdCliente) throws SQLException {
		super(Messages.CARGAPEDIDO_TITULO_CADASTRO);
		cadCargaPedidoForm = new CadCargaPedidoForm(cdCliente, true);
		cadCargaPedidoForm.cadCargaPedidoWindow = this;
		btSalvar = new ButtonPopup(FrameworkMessages.BOTAO_SALVAR);
		btCancelar = new ButtonPopup(FrameworkMessages.BOTAO_CANCELAR);
		novaCargaCriada = false;
		scrollable = false;
		setDefaultRect();
	}
	
	//@Override
	protected void addBtFechar() {
	}
	
	//@Override
	public void initUI() {
		super.initUI();
		addButtonPopup(btSalvar);
		addButtonPopup(btCancelar);
		UiUtil.add(this, cadCargaPedidoForm , LEFT , getTop() , FILL , FILL - footerH);
	}
	
	//@Override
	public void onEvent(Event event) {
	   try {
		switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == btSalvar) {
					try {
						cadCargaPedidoForm.onSave();
					} catch (ValidationException ex) {
						UiUtil.showErrorMessage(ex);
					}
					
				} else if (event.target == btCancelar) {
					cadCargaPedidoForm.voltarClick();
				}
				break;
			default:
				break;
		}
	   }
		catch (Throwable e) {e.printStackTrace();}
	}
	
	
	

}
