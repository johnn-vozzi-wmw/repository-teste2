package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.presentation.ui.combo.CidadeClienteComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.ClienteComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RedeComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class FiltroPlanejarVendaMetaAvancadoWindow  extends WmwWindow{
	
	private ListPlanejarVendaMetaForm listPlanejarVendaMetaForm;
	private ButtonPopup btLimpar;
	private ButtonPopup btFiltrar;
	private ClienteComboBox cbClienteComboBox;
	private CidadeClienteComboBox cbCidadeClienteComboBox;
	private RedeComboBox cbRedeComboBox;
	private LabelName lbCliente;
	private LabelName lbCidade;
	public boolean aplicouFiltros = false;
	
	public FiltroPlanejarVendaMetaAvancadoWindow(ListPlanejarVendaMetaForm listPlanejarVendaMetaForm) throws SQLException {
		super(Messages.PLANEJAMENTOMETAVENDA_FILTRO_AVANCADO);
		this.listPlanejarVendaMetaForm = listPlanejarVendaMetaForm;
		cbClienteComboBox = new ClienteComboBox(Messages.MENU_OPCAO_CLIENTES, 3);
		cbCidadeClienteComboBox = new CidadeClienteComboBox();
		cbCidadeClienteComboBox.load();
		cbRedeComboBox = new RedeComboBox();
		cbRedeComboBox.setSelectedIndex(0);
		lbCliente = new LabelName(Messages.CLIENTE_NOME_ENTIDADE);
		lbCidade = new LabelName(Messages.NOVOCLIENDERECO_LABEL_CIDADE);
		btLimpar = new ButtonPopup(FrameworkMessages.BOTAO_LIMPAR);
		btFiltrar = new ButtonPopup(FrameworkMessages.BOTAO_FILTRAR);
		loadDefaultFilters();
		carregaFiltrosAplicados();
		setDefaultRect();
	}

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, new LabelName(Messages.REDE_NOME_ENTIDADE), cbRedeComboBox, getLeft(), getNextY());
		UiUtil.add(this, lbCliente, cbClienteComboBox, getLeft(), getNextY());
		UiUtil.add(this, lbCidade, cbCidadeClienteComboBox, getLeft(), getNextY());
		addButtonPopup(btFiltrar);
		addButtonPopup(btLimpar);
		addButtonPopup(btFechar);
	}
	
	@Override
	public void onEvent(Event event) {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btFechar) {
					super.onEvent(event);
				} else if (event.target == btFiltrar) {
					try {
						aplicaFiltrosNoListPlanejarVendaMetaForm();
						btFiltrarClick();
					} catch (SQLException e) {
						ExceptionUtil.handle(e);
					}
				} else if (event.target == btLimpar) {
					try {
						loadDefaultFilters();
						aplicaFiltrosNoListPlanejarVendaMetaForm();
					} catch (SQLException e) {
						ExceptionUtil.handle(e);
					}
				} else {
					listPlanejarVendaMetaForm.onEvent(event);
				}
				break;
			}
		}
	}
		
	@Override
	protected void onPopup() {
		super.onPopup();
		reposition();
		ajustaTamanhoBotoes();
	}
	
	private void btFiltrarClick() throws SQLException {
		fecharWindow();
		closedByBtFechar = false;
	}
	
	private void loadDefaultFilters() throws SQLException {
		cbClienteComboBox.setSelectedIndex(0);
		cbCidadeClienteComboBox.setSelectedIndex(0);
	}
	
	public void carregaFiltrosAplicados() throws SQLException {
		cbClienteComboBox.setValue(listPlanejarVendaMetaForm.platVendaMeta.cdCliente);
		cbCidadeClienteComboBox.setValue(listPlanejarVendaMetaForm.platVendaMeta.dsCidade);
	}
	
	public void aplicaFiltrosNoListPlanejarVendaMetaForm() throws SQLException {
		listPlanejarVendaMetaForm.platVendaMeta.cdCliente = cbClienteComboBox.getValue();
		listPlanejarVendaMetaForm.platVendaMeta.dsCidade = cbCidadeClienteComboBox.getValue();
		listPlanejarVendaMetaForm.platVendaMeta.cdRede = cbRedeComboBox.getValue();
		aplicouFiltros = true;
	}
}
