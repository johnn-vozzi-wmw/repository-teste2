package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.List;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.BonifCfgBrinde;
import br.com.wmw.lavenderepda.business.domain.BonifCfgFaixaQtde;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class ListBonifCfgBrindeWindow extends WmwWindow {

	public ListBonifCfgBrindeForm listBonifCfgBrindeForm;
	private final ButtonPopup btConfirmar;
	private final ButtonPopup btInserirTodos;
	
	public ListBonifCfgBrindeWindow(BonifCfgFaixaQtde bonifCfgFaixa, Pedido pedido) {
		super(Messages.BONIFCFG_LISTA_BRINDES);
		listBonifCfgBrindeForm = new ListBonifCfgBrindeForm(bonifCfgFaixa, pedido);
		btConfirmar = new ButtonPopup(Messages.BOTAO_LABEL_CONFIRMAR_SELECAO);
		btInserirTodos = new ButtonPopup(Messages.BOTAO_LABEL_INSERIR_TODOS);
		setDefaultRect();
	}
	
	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, listBonifCfgBrindeForm, LEFT, getTop(), FILL, FILL);
		addButtonPopup(btInserirTodos);
		addButtonPopup(btConfirmar);
	}
	
	@Override
	public void onWindowEvent(Event event) throws SQLException {
		super.onWindowEvent(event);
		if (event.type == ControlEvent.PRESSED) {
			if (event.target == btConfirmar) {
				setBrindesSelecionadosList();
				fecharWindow();
			}
			if (event.target == btInserirTodos) {
				listBonifCfgBrindeForm.getListContainer().checkAll(true);
				setBrindesSelecionadosList();
				fecharWindow();
			}
		}
	}
	
	@Override
	protected void addBtFechar() {
		//Sem a possibilidade de fechar sem ação
	}

	private void setBrindesSelecionadosList() {
		listBonifCfgBrindeForm.setBrindesSelecionadosList();
	}

	public List<BonifCfgBrinde> getBrindesSelecionadosList() {
		return listBonifCfgBrindeForm.getBrindesSelecionadosList();
	}

}
