package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.HashMap;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelTotalizador;
import br.com.wmw.framework.presentation.ui.ext.SessionTotalizerContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ClienteChurn;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.service.ClienteChurnService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.presentation.ui.combo.MotivoChurnComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.ui.Control;
import totalcross.ui.ScrollPosition;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class ListClienteChurnForm extends LavendereCrudListForm {
	
	private RepresentanteSupervComboBox cbRepresentante;
	private MotivoChurnComboBox cbMotivoChurn;
	private SessionTotalizerContainer sessaoTotalizadores;
	private LabelTotalizador ltQtTotalChurn;
	private LabelTotalizador ltQtParcialChurn;
	
	private int qtdTotalClientes;

	public ListClienteChurnForm() throws SQLException {
		super(Messages.CLIENTECHURN_NOME_ENTIDADE);
		setBaseCrudCadForm(new CadClienteChurnForm());
		singleClickOn = true;
		constructorListContainer();
		constructorComboBox();
		criaTotalizadores();
		calculaTotalizadorGeral();
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		return new ClienteChurn(cbRepresentante.getValue(), cbMotivoChurn.getValue(), edFiltro.getText());
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return ClienteChurnService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE), cbRepresentante, getLeft(), getNextY());
		}
		UiUtil.add(this, new LabelName(Messages.MOTIVO_CHURN_COMBO), cbMotivoChurn, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.CLIENTE_NOME_ENTIDADE), edFiltro, getLeft(), getNextY());
		UiUtil.add(this, sessaoTotalizadores, LEFT, BOTTOM, FILL, UiUtil.getLabelPreferredHeight());
		UiUtil.add(sessaoTotalizadores, ltQtTotalChurn, LEFT + UiUtil.getTotalizerGap(), CENTER, PREFERRED, PREFERRED);
		UiUtil.add(sessaoTotalizadores, ltQtParcialChurn,  RIGHT - UiUtil.getTotalizerGap(), SAME, PREFERRED, PREFERRED);
		UiUtil.add(this, listContainer, LEFT, edFiltro.getY2() + HEIGHT_GAP_BIG, FILL, FILL - sessaoTotalizadores.getHeight());
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == cbRepresentante) {
				cbRepresentanteChange();
			} else if (event.target == cbMotivoChurn) {
				cdMotivoChurnChange();
			}
			break;
		}
	}
	
	@Override
	public void loadDefaultFilters() throws SQLException {
		setComboRepresentante();
	}
	
	@Override
	public void onFormShow() throws SQLException {
		edFiltro.requestFocus();
		super.onFormShow();
	}
	
	@Override
	protected Control getComponentToFocus() {
		return edFiltro;
	}
	
	@Override
	protected void filtrarClick() throws SQLException {
		list();
		showRequestedFocus();
	}
	
	@Override
	protected String[] getItem(Object domain) throws SQLException {
		
		ClienteChurn clienteChurn = (ClienteChurn) domain;
		Vector item = new Vector();
		
		item.addElement(StringUtil.getStringValue(clienteChurn.getCliente().cdCliente) + " - " + StringUtil.getStringValue(clienteChurn.getCliente().nmRazaoSocial));
		item.addElement(FrameworkMessages.CAMPO_VAZIO);
		item.addElement(StringUtil.getStringValue(clienteChurn.getMotivoChurn()));
		item.addElement(StringUtil.getStringValue(clienteChurn.dtEntradaChurn));
		
		return (String[]) item.toObjectArray();
	}
	
	@Override
	protected Image[] getIconsLegend(BaseDomain domain) throws SQLException {
		resizeIconsLegend = false;
		useLeftTopIcons = true;
		int iconSize = listContainer.getLayout().relativeFontSizes[0] + listContainer.getFont().size;
		ClienteChurn clienteChurn = (ClienteChurn) domain;
		Image[] iconsLegend = ClienteService.getInstance().getIconsIndicadores(clienteChurn.getCliente(), new HashMap<>() , iconSize, ClienteChurn.APRESENTA_INDICADOR);
		return ValueUtil.isNotEmpty(iconsLegend) ? iconsLegend : super.getIconsLegend(domain);
	}
	
	@Override
	protected void afterList(Vector domainList) throws SQLException {
		calculaTotalizadorParcial();
		sessaoTotalizadores.reposition();
	}

	private void constructorListContainer() {
		ScrollPosition.AUTO_HIDE = false;
		configListContainer(DaoUtil.ALIAS_CLIENTE +".NMRAZAOSOCIAL");
		listContainer = new GridListContainer(4, 2);
		listContainer.setColsSort(new String[][]{{Messages.PEDIDO_LABEL_NMRAZAOSOCIAL, DaoUtil.ALIAS_CLIENTE + ".NMRAZAOSOCIAL"}, 
												 {Messages.CLIENTECHURN_DTENTRADA, "tb.DTENTRADACHURN"},
												 {Messages.CLIENTECHURN_MOTIVO, "M.DSMOTIVOCHURN"}});
		listContainer.setColPosition(3, RIGHT);
		ScrollPosition.AUTO_HIDE = true;
		
	}
	
	private void constructorComboBox() throws SQLException {
		cbRepresentante = new RepresentanteSupervComboBox();
		cbMotivoChurn = new MotivoChurnComboBox();
	}
	
	private void criaTotalizadores() {
		sessaoTotalizadores = new SessionTotalizerContainer();
		ltQtTotalChurn = new LabelTotalizador(" - ");
		ltQtParcialChurn = new LabelTotalizador(" - ");
	}
	
	private int getTotalClientePorRepresentante() throws SQLException {
		ClienteChurn clienteChurn = (ClienteChurn) getDomainFilter();
		return ClienteService.getInstance().getTotalClientePorRepresentante(clienteChurn.cdRepresentante); 
	}
	
	private void calculaTotalizadorGeral() throws SQLException {
		
		int qtdTotalClienteChurn = ClienteChurnService.getInstance().getTotalGeralClienteChurn((ClienteChurn) getDomainFilter());
		qtdTotalClientes = getTotalClientePorRepresentante();
		double pctTotalRiscoChurn = ValueUtil.round(((double)qtdTotalClienteChurn/(double)qtdTotalClientes) * 100);
		
		ltQtTotalChurn.setValue(Messages.CLIENTECHURN_TOTAL_GERAL + qtdTotalClienteChurn + "/" + qtdTotalClientes + ": " + StringUtil.getStringValueToInterface(pctTotalRiscoChurn) + "%");
	}
	
	private void calculaTotalizadorParcial() throws SQLException {
		
		int qtdParcialClienteChurn = ClienteChurnService.getInstance().getTotalParcialClienteChurn((ClienteChurn) getDomainFilter());;
		double pctParcialRicoChurn = ValueUtil.round(((double)qtdParcialClienteChurn/(double)qtdTotalClientes) * 100);
		ltQtParcialChurn.setValue(Messages.CLIENTECHURN_TOTAL_PARCIAL + qtdParcialClienteChurn + "/" + qtdTotalClientes + ": " + StringUtil.getStringValueToInterface(pctParcialRicoChurn) + "%");
	}
	
	private void setComboRepresentante() throws SQLException {
		if (ValueUtil.isNotEmpty(SessionLavenderePda.getRepresentante().cdRepresentante)) {
			cbRepresentante.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);
		}
		if (ValueUtil.isEmpty(cbRepresentante.getValue())) {
			cbRepresentante.setSelectedIndex(0);
		}
	}
	
	private void cbRepresentanteChange() throws SQLException {
		clearGrid();
		SessionLavenderePda.setRepresentante(null);
		if (ValueUtil.isNotEmpty(cbRepresentante.getValue())) {
			SessionLavenderePda.setRepresentante(((SupervisorRep)cbRepresentante.getSelectedItem()).representante);
		}
		calculaTotalizadorGeral();
		list();
	}
	
	private void cdMotivoChurnChange() throws SQLException {
		clearGrid();
		list();
	}
	
}
