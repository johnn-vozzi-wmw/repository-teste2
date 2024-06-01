package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.VerbaCliente;
import br.com.wmw.lavenderepda.business.service.VerbaClienteService;
import br.com.wmw.lavenderepda.presentation.ui.combo.CdVerbaSaldoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto1ComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelVerbaClienteWindow extends WmwListWindow {

	private LabelValue dsCliente;
	private GrupoProduto1ComboBox cbGrupoProduto1;
	private CdVerbaSaldoComboBox cbCdVerbaSaldoCliente;
	
	private Cliente cliente;
	private VerbaCliente verbaCliente;
	
	public RelVerbaClienteWindow(Cliente cliente, String cdGrupoProduto1) throws SQLException {
		super(Messages.VERBACLIENTE_TITULO);
		this.cliente = cliente;
		dsCliente = new LabelValue(cliente.toString());
		cbGrupoProduto1 = new GrupoProduto1ComboBox();
		cbGrupoProduto1.loadGrupoProduto1(null);
		cbGrupoProduto1.setSelectedIndex(0);
		cbCdVerbaSaldoCliente = new CdVerbaSaldoComboBox(cdGrupoProduto1);
		cbCdVerbaSaldoCliente.setSelectedIndex(0);
		if (ValueUtil.isNotEmpty(cdGrupoProduto1)) {
			cbGrupoProduto1.setValue(cdGrupoProduto1);
			cbGrupoProduto1.setEditable(false);
		}
		constructorListContainer();
		setDefaultRect();
	}
	
	private void constructorListContainer() {
		listContainer = new GridListContainer(4, 2);
		listContainer.setColPosition(3, RIGHT);
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return VerbaClienteService.getInstance().findVerbaClienteGrupoProduto1ByExample(cbGrupoProduto1.getValue(), cliente.cdCliente, cbCdVerbaSaldoCliente.getValue());
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		VerbaCliente verbaCliente = (VerbaCliente) domain;
		return new String[]{verbaCliente.cdVerbaSaldoCliente,
				ValueUtil.VALOR_NI,
				verbaCliente.grupoProduto1.toString(),
				MessageUtil.getMessage(Messages.VERBACLIENTE_VLSALDO, verbaCliente.vlSaldo)
				};
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		return listContainer.getSelectedId();
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return VerbaClienteService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		VerbaCliente verbaCliente = new VerbaCliente(cliente);
		verbaCliente.cdGrupoProduto1 = cbGrupoProduto1.getValue();
		verbaCliente.cdVerbaSaldoCliente = cbCdVerbaSaldoCliente.getValue();
		return verbaCliente;
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, new LabelName(Messages.LABEL_CLIENTE), dsCliente, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.CODIGO) ,cbCdVerbaSaldoCliente, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.GRUPOPRODUTO1_PRODUTO_NOME_ENTIDADE),cbGrupoProduto1, getLeft(), getNextY());
		UiUtil.add(this, listContainer, LEFT, getNextY(), FILL, FILL);
		
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == cbGrupoProduto1 || event.target == cbCdVerbaSaldoCliente) {
				list();
			}
			break;
		}
	}
	
	@Override
	public void detalhesClick() throws SQLException {
		if (listContainer.getContainer(listContainer.getSelectedIndex()) != null) {
			this.verbaCliente = (VerbaCliente)getSelectedDomain();
			fecharWindow();
		}
	}
	
	public VerbaCliente getVerbaCliente() {
		return verbaCliente;
	}

}
