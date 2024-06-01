package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pacote;
import br.com.wmw.lavenderepda.business.service.PacoteService;
import totalcross.util.Vector;

public class PacoteComboBox extends BaseComboBox {
	
	public Vector comboItens;
	
	public PacoteComboBox() throws SQLException {
		super();
		defaultItemType = DefaultItemType_ALL;
		loadAllPacote();
	}
	
	public PacoteComboBox(ItemPedido itemPedido) throws SQLException {
		super();
		defaultItemType = DefaultItemType_NONE;
		loadAllPacoteByItemPedido(itemPedido);
	}

	public Pacote getDescontoPacote() {
		return (Pacote) getSelectedItem();
	}

	public Pacote getValue() {
		Pacote descontoPacote = (Pacote) getSelectedItem();
		return descontoPacote;
	}
	
	public void selectFirstLoad() {
		if (ValueUtil.isEmpty(comboItens)) return;
		setSelectedIndex(0);
	}

	public void setValue(String cdPacote) {
		Pacote pacote = new Pacote();
		pacote.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pacote.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		pacote.cdPacote = cdPacote;
		select(pacote);
	}
	
	public void setValue(Pacote pacote) {
		select(pacote);
	}

	public void loadAllPacoteByItemPedido(ItemPedido itemPedido) throws SQLException {
		removeAll();
		Vector list = comboItens = PacoteService.getInstance().findAllPacoteByItemPedido(itemPedido);
		list.qsort();
		add(list);
	}
	
	public void loadAllPacote() throws SQLException {
		removeAll();
		Pacote pacoteFilter = new Pacote(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getRepresentante().cdRepresentante);
		Vector list = comboItens = PacoteService.getInstance().findAllByExample(pacoteFilter);
		list.qsort();
		add(list);
	}

}
