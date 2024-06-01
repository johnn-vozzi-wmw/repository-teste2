package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseToolTip;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.SugVendaPerson;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class FiltroAvancadoSugPersonWindow extends WmwWindow {
	
	private List<SugVendaPerson> sugVendaPersonList;
	private ButtonPopup btLimpar;
	private ButtonPopup btFiltrar;
	protected Map<CheckBoolean, SugVendaPerson> checkSugestaoMap;
	protected boolean filtroAplicado;
	private boolean statusChecks[];

	public FiltroAvancadoSugPersonWindow(List<SugVendaPerson> sugVendaPersonList, boolean[] statusChecks) throws SQLException {
		super(Messages.PRODUTO_FILTRO_AVANCADO);
		this.sugVendaPersonList = sugVendaPersonList;
		this.statusChecks = statusChecks;
		btLimpar = new ButtonPopup(FrameworkMessages.BOTAO_LIMPAR);
		btFiltrar = new ButtonPopup(FrameworkMessages.BOTAO_FILTRAR);
		setDefaultRect();
	}
	
	@Override
	public void initUI() {
		super.initUI();
		addButtonPopup(btFiltrar);
		addButtonPopup(btLimpar);
		addButtonPopup(btFechar);
		try {
			carregaFiltrosAplicados();
		} catch (Throwable e) {
			
		} 
	}
	
	@Override
	public void onWindowEvent(Event event) throws SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btFiltrar) {
					aplicaFiltrosSugAvancada();
					fecharWindow();
				} else if (event.target == btLimpar) {
					limpaChecksFiltro();
				}
				break;
			}
		}
	}
	
	private void carregaFiltrosAplicados() throws SQLException {
		int nSugLinha = 1;
		checkSugestaoMap = new HashMap<>();
		int y = getTop() + HEIGHT_GAP;
		int x = LEFT + WIDTH_GAP;
		int checkWidth = (width - WIDTH_GAP_BIG * 3) / 3;
		int size = sugVendaPersonList.size();
		for (int i = 0; i < size; i++, nSugLinha++) {
			SugVendaPerson sugVendaPerson = sugVendaPersonList.get(i);
			CheckBoolean check = new CheckBoolean(ValueUtil.isEmpty(sugVendaPerson.dsSugVendaPersonAbrev) ? sugVendaPerson.dsSugVendaPerson : sugVendaPerson.dsSugVendaPersonAbrev);
			new BaseToolTip(check, sugVendaPerson.dsSugVendaPerson);
			check.appId = i;
			checkSugestaoMap.put(check, sugVendaPerson);
			UiUtil.add(this, check, x, y, checkWidth);
			check.setChecked(true);
			x = check.getX2() + WIDTH_GAP_BIG;
			if (nSugLinha == 3) {
				y = check.getY2() + HEIGHT_GAP;
				x = LEFT + WIDTH_GAP;
				nSugLinha = 0;
			}
		}
	}
	
	private void atualizaCheckStatus() {
		for (CheckBoolean check : checkSugestaoMap.keySet()) {
			if (check.appId < statusChecks.length) {
				check.setChecked(statusChecks[check.appId]);
			}
		}
	}
	
	private void aplicaFiltrosSugAvancada() {
		filtroAplicado = true;
		for (CheckBoolean check : checkSugestaoMap.keySet()) {
			if (check.appId < statusChecks.length) {
				statusChecks[check.appId] = check.isChecked();
			}
		}
	}
	
	@Override
	protected void onPopup() {
		super.onPopup();
		atualizaCheckStatus();
	}
	
	private void limpaChecksFiltro() {
		for (CheckBoolean check : checkSugestaoMap.keySet()) {
			check.setChecked(false);
		}
	}
	
	@Override
	protected void addBtFechar() {
	}

}
