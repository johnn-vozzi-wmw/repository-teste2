package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.StatusVencimentoPedidoConsignacaoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.ScrollPosition;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ListVencimentoPedidoConsignacaoForm extends LavendereCrudListForm {
	
	private StatusVencimentoPedidoConsignacaoComboBox cbStatusVencimentoPedidoConsignacaoComboBox;

	public ListVencimentoPedidoConsignacaoForm() throws SQLException {
		super(Messages.PEDIDO_CONSIGNACAO_VENCIMENTO);
		setBaseCrudCadForm(new CadPedidoForm());
		cbStatusVencimentoPedidoConsignacaoComboBox = new StatusVencimentoPedidoConsignacaoComboBox();
		constructorListContainer();
		singleClickOn  = true;
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return new Pedido();
	}

	@Override
	protected CrudService getCrudService() {
		return PedidoService.getInstance();
	}

	@Override
	protected void onFormStart() {
		UiUtil.add(this, cbStatusVencimentoPedidoConsignacaoComboBox, getLeft(), getNextY() + HEIGHT_GAP);
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
	}
	
	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		Pedido pedidoFilter = (Pedido) getDomainFilter();
		pedidoFilter.pedidoConsignadoVencido = cbStatusVencimentoPedidoConsignacaoComboBox.isStatusVencimento();
		pedidoFilter.pedidoConsignadoAVencer = cbStatusVencimentoPedidoConsignacaoComboBox.isStatusAVencer();
		Vector pedidoList = ((PedidoService) getCrudService()).findPedidosConsignadosOrdenadosPorVencimento(pedidoFilter);
		setClienteNaLista(pedidoList);
		return pedidoList;
	}

	private void setClienteNaLista(Vector pedidoList) throws SQLException {
		for (int i = 0; i < pedidoList.size(); i++) {
			Pedido pedido = (Pedido) pedidoList.items[i];
			pedido.setCliente(ClienteService.getInstance().getCliente(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdCliente));
		}
	}
	
	@Override
	protected String[] getItem(Object domain) throws SQLException {
		Pedido pedido = (Pedido) domain;
		String[] item = {
	        pedido.getCliente().toString(),
	        "",
	        montaLinha(Messages.PEDIDO_NOME_ENTIDADE, pedido.nuPedido),
	        montaLinha(Messages.PEDIDO_CONSIGNACAO_QTD_DIAS_VENCIMENTO, getQtdDias(pedido.dtConsignacao)),
	        montaLinha(Messages.PRODUTO_LABEL_RS, StringUtil.getStringValueToInterface(pedido.vlTotalPedido)),
	        montaLinha(Messages.TITULOFINANCEIRO_LABEL_DTVENCIMENTO, StringUtil.getStringValue(pedido.dtConsignacao)),
	    };
		return item;
	}
	
	@Override
	protected void setPropertiesInRowList(Item containerItem, BaseDomain domain) {
		Pedido pedido = (Pedido)domain;
		if (pedido.isPedidoConsignadoVencido()) {
			containerItem.setBackColor(LavendereColorUtil.COR_PEDIDO_CONSIGNACAO_VENCIDO);
		}
	}
	
	private String montaLinha(String nomeColuna, String valorColuna) {
		StringBuilder linha =  new StringBuilder();
		linha.append(nomeColuna).append(": ").append(valorColuna);
		return linha.toString();
	}
	
	private String getQtdDias(Date dtConsignacao) {
		if (ValueUtil.isNotEmpty(dtConsignacao)) {
			dtConsignacao.advance(LavenderePdaConfig.getNuDiasValidadePedidoEmConsignacao());
		}
		int qtdDias = DateUtil.getDaysBetween(DateUtil.getCurrentDate(), dtConsignacao);
		qtdDias = qtdDias < 0 ? qtdDias * -1 : qtdDias;
		return StringUtil.getStringValue(qtdDias);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == cbStatusVencimentoPedidoConsignacaoComboBox) {	
					list();
				}
				break;
			}
		}
	}
	
	@Override
	public void detalhesClick() throws SQLException {
		if (listContainer.getSelectedIndex() >= 0) {
			try {
				UiUtil.showProcessingMessage();
				CadPedidoForm cadPedido = (CadPedidoForm)getBaseCrudCadForm();
				BaseDomain domain = getSelectedDomain();
				cadPedido.edit(domain);
				show(cadPedido);
			} finally {
				UiUtil.unpopProcessingMessage();
			}
		} else {
			UiUtil.showGridEmptySelectionMessage(Messages.PEDIDO_NOME_ENTIDADE);
		}
	}
	
	private void constructorListContainer() {
		ScrollPosition.AUTO_HIDE = false;
		configListContainer("CLIENTE");
		listContainer = new GridListContainer(6, 2);
		listContainer.setColsSort(new String[][] {
			{Messages.CLIENTE_NOME_ENTIDADE, "CLIENTE"},
			{Messages.PEDIDO_NOME_ENTIDADE, "PEDIDO"},
			{Messages.PEDIDO_LABEL_TOTALVENDAS, "VLTOTALPEDIDO"},
			{Messages.PEDIDO_CONSIGNACAO_QTD_DIAS_VENCIMENTO, "QTDDIAS"},
			{Messages.CONSIGNACAO_DT_CONSIGNACAO, "DTCONSIGNACAO"}
		});
    	listContainer.setColPosition(3, RIGHT);
    	listContainer.setColPosition(5, RIGHT);
    	ScrollPosition.AUTO_HIDE = true;
	}
	
}
