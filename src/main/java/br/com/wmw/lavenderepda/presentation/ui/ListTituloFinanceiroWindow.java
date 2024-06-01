package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.TituloFinanceiro;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TituloFinanceiroPdbxDao;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.util.Vector;

public class ListTituloFinanceiroWindow extends WmwWindow {

	private static ListTituloFinanceiroWindow listTituloFinanceiroWindow;
	public BaseGridEdit grid;
	public LabelName lbInformativo;
	public Vector listTitulosAtrasados;
	
	public ListTituloFinanceiroWindow() throws SQLException {
		super(Messages.TITULO_ATRASOS);
		lbInformativo = new LabelName();
		lbInformativo.setForeColor(Color.RED);
		montaGrid();
		carregaGrid();
		setDefaultRect();
	}
	
	public void montaGrid() {
		GridColDefinition[] gridColDefiniton = {
			new GridColDefinition(Messages.LABEL_NU_TITULO, -50, CENTER),
			new GridColDefinition(Messages.VALOR, -35, CENTER)};
		grid = UiUtil.createGridEdit(gridColDefiniton, false);
	}

	@Override
	public void initUI() {
		super.initUI();
		addBtFechar();
		int x = LEFT;
		int y = getTop();
		UiUtil.add(this, grid, x + WIDTH_GAP, y + HEIGHT_GAP_BIG * 5, FILL - WIDTH_GAP, FILL);
		UiUtil.add(this, lbInformativo, x + WIDTH_GAP_BIG, y + HEIGHT_GAP_BIG);
	}
	
	public void carregaGrid() throws SQLException {
		grid.clear();
		TituloFinanceiro tituloAtrasado = new TituloFinanceiro();
		tituloAtrasado.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		tituloAtrasado.cdEmpresa = SessionLavenderePda.cdEmpresa;
		tituloAtrasado.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		Vector tituloFinanceiroList = TituloFinanceiroPdbxDao.getInstance().findTitulosAtrasados(tituloAtrasado);
		listTitulosAtrasados = tituloFinanceiroList;
		int size = tituloFinanceiroList.size();
		lbInformativo.setValue(MessageUtil.getMessage(Messages.TITULO_FINANCEIRO_ATRASADO_ALERTA, size));
		
		for (int i = 0; i < size; i++) {
			TituloFinanceiro tituloFinanceiro = (TituloFinanceiro)tituloFinanceiroList.items[i];
			if (tituloFinanceiro != null) {
				String[] item = {
					StringUtil.getStringValue(tituloFinanceiro.nuTitulo),
					Messages.MOEDA + " " + StringUtil.getStringValueToInterface(tituloFinanceiro.vlTitulo)
				};
				grid.add(item);
			}
		}
	}

	public static ListTituloFinanceiroWindow getInstance() throws SQLException {
		if (listTituloFinanceiroWindow == null) {
			return new ListTituloFinanceiroWindow();
		} else {
			return listTituloFinanceiroWindow;
		}
	}
	
	@Override
	public void popup() {
		if (listTitulosAtrasados.size() > 0) {
			super.popup();
		}
	}
}
