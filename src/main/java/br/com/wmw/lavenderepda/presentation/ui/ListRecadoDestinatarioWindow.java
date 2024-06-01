package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.UsuarioWeb;
import br.com.wmw.lavenderepda.business.service.UsuarioWebService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.GridEvent;
import totalcross.util.Vector;

public class ListRecadoDestinatarioWindow extends WmwWindow {

	private ButtonPopup btSelecionar;
	private BaseGridEdit grid;
	private EditFiltro edFiltro;
	private final boolean readOnly;

	public HashMap<String, UsuarioWeb> cdDestinatarios;

	private void configure() {
		btSelecionar = new ButtonPopup(Messages.BOTAO_SELECIONAR).setID("btSelecionar");
		edFiltro = new EditFiltro("999999999", 50);
		GridColDefinition[] gridColDefiniton = {new GridColDefinition(Messages.LIST_RECADO_USUARIO, -1, LEFT)};
		grid = UiUtil.createGridEdit(gridColDefiniton, !readOnly).setID("grid");
		grid.canClickSelectAll = true;
	}

	public ListRecadoDestinatarioWindow(boolean readOnly, UsuarioWeb[] destinatarios) {
		super(Messages.RECADO_LABEL_CDUSUARIODESTINATARIO);
		this.readOnly = readOnly;
		configureDestinatarios(destinatarios);
		configure();
		setDefaultRect();
	}

	public void configureDestinatarios(UsuarioWeb[] destinatarios) {
		if (destinatarios == null) {
			return;
		}
		if (this.cdDestinatarios == null) {
			this.cdDestinatarios = new HashMap<>();
		}
		for (UsuarioWeb usuarioWeb : destinatarios) {
			usuarioWeb.checkedInRecadoList = true;
			if (this.cdDestinatarios.get(usuarioWeb.cdUsuarioWeb) == null) {
				this.cdDestinatarios.put(usuarioWeb.cdUsuarioWeb, usuarioWeb);
			} else {
				this.cdDestinatarios.get(usuarioWeb.cdUsuarioWeb).checkedInRecadoList = true;
			}
		}
	}

	@Override
	public void initUI() {
		try {
			super.initUI();
			if (!readOnly) {
				UiUtil.add(this, edFiltro, getLeft(), getTop() + HEIGHT_GAP);
			}
			UiUtil.add(this, grid, LEFT, AFTER + (readOnly ? 0 : HEIGHT_GAP), FILL, FILL - footerH);
			carregaGrid();
			hashToCheck();
			if (!readOnly) {
				addButtonPopup(btSelecionar);
			}
			addButtonPopup(btFechar);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btSelecionar) {
					btSelecionarClick();
				}
				break;
			}
			case EditIconEvent.PRESSED: {
				if (event.target == edFiltro) {
					carregaGrid();
					hashToCheck();
				}
				break;
			}
			case GridEvent.CHECK_CHANGED_EVENT: {
				if (!readOnly) {
					checkToHash();
				}
				break;
			}
		}
	}

	private void hashToCheck() {
		int size = grid.size();
		if (cdDestinatarios == null) {
			cdDestinatarios = new HashMap<>();
		}
		for (int i = 0; i < size; i++) {
			String cdUsuarioWeb = grid.getCellText(i,2);
			if (cdDestinatarios.get(cdUsuarioWeb) == null) {
				UsuarioWeb usuarioWeb = new UsuarioWeb();
				usuarioWeb.cdUsuarioWeb = cdUsuarioWeb;
				usuarioWeb.nmUsuarioWeb = grid.getCellText(i, 0);
				grid.setChecked(i, usuarioWeb.checkedInRecadoList);
				cdDestinatarios.put(cdUsuarioWeb, usuarioWeb);
			} else {
				grid.setChecked(i, cdDestinatarios.get(cdUsuarioWeb).checkedInRecadoList);
			}
		}
	}

	private void checkToHash() {
		int size = grid.size();
		if (cdDestinatarios == null) {
			cdDestinatarios = new HashMap<>();
		}
		for (int i = 0; i < size; i++) {
			String cdUsuarioWeb = grid.getCellText(i,2);
			if (cdDestinatarios.get(cdUsuarioWeb) == null) {
				UsuarioWeb usuarioWeb = new UsuarioWeb();
				usuarioWeb.cdUsuarioWeb = cdUsuarioWeb;
				usuarioWeb.nmUsuarioWeb = grid.getCellText(i, 0);
				usuarioWeb.checkedInRecadoList = grid.isChecked(i);
				cdDestinatarios.put(cdUsuarioWeb, usuarioWeb);
			} else {
				cdDestinatarios.get(cdUsuarioWeb).checkedInRecadoList = grid.isChecked(i);
			}
		}
	}

	public void carregaGrid() throws SQLException {
		if (readOnly) {
			for (UsuarioWeb usuarioWeb : cdDestinatarios.values()) {
				String[] temp = {usuarioWeb.nmUsuarioWeb, usuarioWeb.cdUsuarioWeb};
				grid.add(temp);
			}
			grid.markAll(true);
		} else {
			UsuarioWeb usuarioWeb = new UsuarioWeb();
			String cdUsuarioAtual = SessionLavenderePda.usuarioPdaRep.cdUsuario;
			usuarioWeb.cdUsuario = cdUsuarioAtual;
			usuarioWeb.likeFilter = getEdFiltroValue();
			Vector usuarioWebList = UsuarioWebService.getInstance().findAllByExample(usuarioWeb);
			grid.clear();
			int size = usuarioWebList.size();
			for (int i = 0; i < size; i++) {
				UsuarioWeb usuarioWebDestinatario = (UsuarioWeb) usuarioWebList.items[i];
				if (!ValueUtil.valueEquals(cdUsuarioAtual, usuarioWebDestinatario.cdUsuarioWeb)) {
					String[] temp = {usuarioWebDestinatario.nmUsuarioWeb, usuarioWebDestinatario.cdUsuarioWeb};
					grid.add(temp);
				}
			}
			grid.markAll(false);
		}
		grid.qsort(1);
	}

	private String getEdFiltroValue() {
		String filtro = edFiltro.getValue();
		return ValueUtil.isEmpty(filtro) ? null : filtro;
	}

	public void btSelecionarClick() {
		StringBuilder retorno = new StringBuilder();
		for (UsuarioWeb usuarioWeb : cdDestinatarios.values()) {
			if (usuarioWeb.checkedInRecadoList) {
				retorno.append(usuarioWeb.cdUsuarioWeb).append(";");
			}
		}
		if (ValueUtil.isEmpty(retorno.toString())) {
			UiUtil.showErrorMessage(Messages.RECADO_MSG_INFORME_DESTINATARIO);
		} else {
			unpop();
		}
	}

	@Override
	protected void onUnpop() {
		super.onUnpop();
		grid.setItems(null);
	}

	public String[] getCheckedDestinatarios() {
		List<String> destinatarioList = new ArrayList<>();
		for (UsuarioWeb usuarioWeb : cdDestinatarios.values()) {
			if (usuarioWeb.checkedInRecadoList) {
				destinatarioList.add(usuarioWeb.cdUsuarioWeb);
			}
		}
		return destinatarioList.toArray(new String[] {});
	}

	public UsuarioWeb[] getCheckedUsuarioWeb() {
		List<UsuarioWeb> destinatarioList = new ArrayList<>();
		for (UsuarioWeb usuarioWeb : cdDestinatarios.values()) {
			if (usuarioWeb.checkedInRecadoList) {
				destinatarioList.add(usuarioWeb);
			}
		}
		return destinatarioList.toArray(new UsuarioWeb[] {});
	}

	public String getNmDestinatarios() {
		StringBuilder nmDestinatarios = new StringBuilder();
		for (UsuarioWeb usuarioWeb : cdDestinatarios.values()) {
			if (usuarioWeb.checkedInRecadoList || readOnly) {
				nmDestinatarios.append(usuarioWeb.nmUsuarioWeb).append(";");
			}
		}
		return nmDestinatarios.toString();
	}

}
