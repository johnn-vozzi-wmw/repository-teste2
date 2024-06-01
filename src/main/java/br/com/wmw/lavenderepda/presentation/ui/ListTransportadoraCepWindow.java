package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TipoFrete;
import br.com.wmw.lavenderepda.business.domain.Transportadora;
import br.com.wmw.lavenderepda.business.domain.TransportadoraCep;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.TransportadoraCepService;
import br.com.wmw.lavenderepda.business.service.TransportadoraService;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoFreteComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereWmwComboBoxListWindow;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListTransportadoraCepWindow extends LavendereWmwComboBoxListWindow {
	
	private ButtonPopup btSelecionar;
	private TipoFreteComboBox cbTipoFrete;
	private Vector transportadoraCepList;
	private Pedido pedido;
	

	public ListTransportadoraCepWindow(Pedido pedidoAtual) throws SQLException {
		super(Messages.TRANSPORTADORACEP_lIST_TITULO);
		this.pedido = pedidoAtual;
		cbTipoFrete = new TipoFreteComboBox();
		cbTipoFrete.loadTipoFrete(pedido);
		cbTipoFrete.setID("cbTipoFrete");
		listContainer = new GridListContainer(4, 2);
        listContainer.setColPosition(3, RIGHT);
       	configListContainer("NMTRANSPORTADORA");
    	listContainer.setColsSort(new String[][]{{Messages.TRANSPORTADORACEP_LABEL_CDTRANSPORTADORA, "CDTRANSPORTADORA"}});
        btSelecionar = new ButtonPopup(Messages.BOTAO_SELECIONAR);
        btFechar.setText(Messages.BOTAO_CANCELAR);
        setDefaultRect();
	}
	
	protected CrudService getCrudService() throws SQLException {
		return TransportadoraCepService.getInstance();
	}
	private TransportadoraCepService getTransportadoraCepService() throws SQLException {
		return (TransportadoraCepService) getCrudService();
	}
	
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		Cliente  cliente = (Cliente) ClienteService.getInstance().findByRowKey(SessionLavenderePda.getCliente().rowKey);
		TipoFrete tipoFrete = (TipoFrete) cbTipoFrete.getSelectedItem();
		transportadoraCepList = getTransportadoraCepService().findTranspCepByCliente(cliente);
		return tipoFrete == null || tipoFrete.isTipoFreteSemFrete() ?  new Vector() : transportadoraCepList;
	}
	
	protected String[] getItem(Object domain) throws SQLException {
		TransportadoraCep transportadoraCep = (TransportadoraCep) domain;
		Transportadora transportadora = TransportadoraService.getInstance().getTransportadora(transportadoraCep.cdTransportadora);
		TipoFrete tipoFrete = (TipoFrete) cbTipoFrete.getSelectedItem();
		double vlFrete = TransportadoraCepService.getInstance().getVlFreteLista(pedido, transportadoraCep, tipoFrete);
		String [] item = 
			{StringUtil.getStringValue(transportadoraCep.nmTransportadora),
			 "",
			 StringUtil.getStringValue(new StringBuffer(Messages.TRANSPORTADORACEP_LIST_ITEM_PCTFRETE).append(ValueUtil.round(tipoFrete.isCalculaFrete() ? transportadoraCep.vlPctFrete : 0, 2))),
			 StringUtil.getStringValue(
					 (LavenderePdaConfig.usaCalculoTransportadoraMaisRentavelPedido && transportadora.isFlMostraFrete() 
							 ? new StringBuffer(Messages.TRANSPORTADORACEP_LIST_ITEM_VLFRETE).append(StringUtil.getStringValueToInterface(vlFrete)): ""))
			};
		return item;
	}
	
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c != null ? c.id : null;
	}
	
	protected BaseDomain getDomainFilter() {
		return new TransportadoraCep();
	}
	
	protected void onFormStart() throws SQLException {
    	UiUtil.add(this, new LabelName(Messages.TIPOFRETE_LABEL_TIPOFRETE), cbTipoFrete, getLeft(), getNextY(), getWFill(), UiUtil.getControlPreferredHeight());
    	UiUtil.add(this, new LabelValue(Messages.TRANSPORTADORAREG_LABEL_SELECIONE_TRANSPORTADORA), getLeft(), getNextY() + HEIGHT_GAP);
        UiUtil.add(this, listContainer, LEFT, getNextY() + HEIGHT_GAP, FILL, FILL);
	}
	
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
	    	case ControlEvent.PRESSED: {
	    		if (event.target == cbTipoFrete) {
	    			cbTipoFreteChange();
	    		} else if (event.target == btSelecionar) {
					btSelecionarClick();
					fecharWindow();
				} else if (event.target == btFechar) {
					this.unpop();
				}
	    	}
		}
	}
	
	protected void addButtons() {
		addButtonPopup(btSelecionar);
		super.addButtons();
	}

	private void btSelecionarClick() throws SQLException {
		if (cbTipoFrete.isValueSelectedEmpty()) {
			throw new ValidationException(Messages.TRANSPORTADORACEP_ERRO_NENHUM_TIPOFRETE_SELECIONADO);
		} else {
			setTransportadoraCep(getSelectedRowKey());
		} 
		PedidoService.getInstance().calculate(pedido);
	}
	
	private void cbTipoFreteChange() throws SQLException {
		list();
		setTransportadoraPreferencial(transportadoraCepList, SessionLavenderePda.getCliente(), cbTipoFrete.getValue());
	}
	
	private void setTransportadoraPreferencial(Vector transportadoraCepList, Cliente cliente, String cdTipoFrete) throws SQLException {
		TipoFrete tipoFrete = (TipoFrete) cbTipoFrete.getSelectedItem();
		if (LavenderePdaConfig.usaTransportadoraPreferencialCliente && transportadoraCepList.size() > 0 && !tipoFrete.isTipoFreteSemFrete()) {
			String cdTransportadoraPreferencial = TransportadoraCepService.getInstance().getCdTransportadoraPreferencial(cliente, tipoFrete);
			for (int i = 0; i < transportadoraCepList.size(); i++) {
				TransportadoraCep transportadoraCep = (TransportadoraCep) transportadoraCepList.items[i];
				if (transportadoraCep.cdTransportadora.equalsIgnoreCase(cdTransportadoraPreferencial)) {
					listContainer.setSelectedIndex(i);
					break;
				}
			}
		}
	}
	
	private void setTransportadoraCep(String rowKey) throws SQLException {
		TipoFrete tipoFrete = (TipoFrete) cbTipoFrete.getSelectedItem();
		TransportadoraCepService.getInstance().setTransportadoraCep(pedido, tipoFrete, rowKey);
	}

}
