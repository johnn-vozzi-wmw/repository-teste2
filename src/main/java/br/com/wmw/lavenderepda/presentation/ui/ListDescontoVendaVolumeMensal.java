package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.DescontoVenda;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.DescontoVendaService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ListDescontoVendaVolumeMensal extends WmwWindow {
	
	private static final String COLUNA_FAIXA_KEY = "1";
	private static final String COLUNA_PCT_FAIXA_DESC_KEY = "2";
	private static final String COLUNA_VL_PEDIDO_DESC_KEY = "3";
	
	private LabelName lbCliente;
	private LabelValue lvCliente;
	private LabelName lbVlTotalPedido;
	private LabelValue lvVlTotalPedido;
	private LabelName lbMes;
	private LabelValue lvMes;
	private LabelName lbVlVendaMensal;
	private LabelValue lvVlVendaMensal;
	
	private Cliente cliente;
	private DescontoVenda faixaCliente;
	private double vlTotalPedido;
	private double vlVendasMensalRede;
	
	private BaseGridEdit grid;
	private Map<String, GridColDefinition> gridColDefinitionMap;
	private List<GridColDefinition> colDefinitionList = new ArrayList<GridColDefinition>();
	private Vector descontoVendaList;
	
	public ListDescontoVendaVolumeMensal(Pedido pedido) throws SQLException {
		super(Messages.DESCONTOVENDA_MENSAL_TITULO_LISTA);
		
		this.cliente = pedido.getCliente();
		
		vlTotalPedido = pedido.vlTotalPedido;
		carregaFaixaVendaClienteRede(pedido);
		
		lbCliente = new LabelName(Messages.CLIENTE_NOME_ENTIDADE);
		lbVlTotalPedido = new LabelName(Messages.DESCONTOVENDA_MENSAL_LABEL_VLTOTALPEDIDO);
		lbMes = new LabelName(Messages.DESCONTOVENDA_MENSAL_LABEL_MES);
		lbVlVendaMensal = new LabelName(clientePossuiRede() ? Messages.DESCONTOVENDA_MENSAL_LABEL_VLVENDAMENSAL_REDE : Messages.DESCONTOVENDA_MENSAL_LABEL_VLVENDAMENSAL);
		
		lvCliente = new LabelValue("@");
		lvVlTotalPedido = new LabelValue("@");
		lvMes = new LabelValue("@");
		lvVlVendaMensal = new LabelValue("@");
		
		lvCliente.setValue(cliente.toString());
		lvVlTotalPedido.setValue(StringUtil.getStringValueToInterface(vlTotalPedido));
		lvMes.setValue(Date.monthNames[DateUtil.getCurrentDate().getMonth()]);
		lvVlVendaMensal.setValue(StringUtil.getStringValueToInterface(vlVendasMensalRede));
		
		loadGridColDefinitionMap();
		configureGrid();
		
		list();
		setDefaultRect();
		grifaFaixaVolumeVenda();
	}

	private void carregaFaixaVendaClienteRede(Pedido pedido) throws SQLException {
		vlVendasMensalRede = cliente.vlVendaMensal; 
		if (LavenderePdaConfig.usaVolumeVendaMensalRede && clientePossuiRede()) {
			vlVendasMensalRede = ClienteService.getInstance().sumVlVendaMensalClientesRede(cliente);
			vlVendasMensalRede = vlVendasMensalRede + vlTotalPedido;
			vlVendasMensalRede += PedidoPdbxDao.getInstance().sumVlTotalPedidosConsideraVolumeVendaMensalPorStatus(pedido);
			faixaCliente = DescontoVendaService.getInstance().getDescontoVenda(cliente.cdEmpresa, cliente.cdEstadoComercial, vlVendasMensalRede);
		} else if (cliente.vlVendaMensal > 0) {
			faixaCliente = DescontoVendaService.getInstance().getDescontoVenda(cliente.cdEmpresa, cliente.cdEstadoComercial, cliente.vlVendaMensal + vlTotalPedido);
	}
		if (faixaCliente == null) faixaCliente = new DescontoVenda();
	}
	
	private void loadGridColDefinitionMap() {
		gridColDefinitionMap = new HashMap<String, GridColDefinition>();
		int oneChar = fm.charWidth('A');
		boolean faixaVisivel = LavenderePdaConfig.isColunaFaixaVisivelGridDescontoVolumeVendaMensal() || LavenderePdaConfig.isTodasColunasVisiveisGridDescontoVolumeVendaMensal();
		boolean pctDescontoVisivel = LavenderePdaConfig.isColunaPctDescontoVisivelGridDescontoVolumeVendaMensal() || LavenderePdaConfig.isTodasColunasVisiveisGridDescontoVolumeVendaMensal();
		boolean vlPedidoVisivel = LavenderePdaConfig.isColunaVlPedidoVisivelGridDescontoVolumeVendaMensal() || LavenderePdaConfig.isTodasColunasVisiveisGridDescontoVolumeVendaMensal();
		
		GridColDefinition colunaFaixa = new GridColDefinition(Messages.DESCONTO_VOLUME_VENDA_MENSAL_VOLUME_FAIXA_DESCONTO, faixaVisivel ? oneChar * 15 : 0, CENTER);
		gridColDefinitionMap.put(COLUNA_FAIXA_KEY, colunaFaixa);
		GridColDefinition colunaPctDesc = new GridColDefinition(Messages.DESCONTO_VOLUME_VENDA_MENSAL_PCT_FAIXA_DESCONTO, pctDescontoVisivel ? oneChar * 2 : 0, CENTER);
		gridColDefinitionMap.put(COLUNA_PCT_FAIXA_DESC_KEY, colunaPctDesc);
		GridColDefinition colunaPedidoDesc = new GridColDefinition(Messages.DESCONTO_VOLUME_VENDA_MENSAL_VALOR_PEDIDO_FAIXA_DESCONTO, vlPedidoVisivel ? oneChar * 15 : 0, CENTER);
		gridColDefinitionMap.put(COLUNA_VL_PEDIDO_DESC_KEY, colunaPedidoDesc);
	}
	
	private void configureGrid() {
		GridColDefinition[] gridColDefinitionArray;
		colDefinitionList = new ArrayList<GridColDefinition>(gridColDefinitionMap.values());
		sortColDefinitionList();
		int size = colDefinitionList.size();
		gridColDefinitionArray = new GridColDefinition[size];
		for (int i = 0; i < size; i++) {
			gridColDefinitionArray[i] = colDefinitionList.get(i);
		}
		grid = UiUtil.createGridEdit(gridColDefinitionArray);
		grid.setGridControllable();
	}
	
	private void sortColDefinitionList() {
		if (LavenderePdaConfig.isTodasColunasVisiveisGridDescontoVolumeVendaMensal()) return;
		
		String[] vlParams = LavenderePdaConfig.configColunasGridDescontoVolumeVendasMensal.split(";");
		int startPosition = vlParams.length - 1;
		for (int i = startPosition; i >= 0; i--) {
			GridColDefinition coluna = gridColDefinitionMap.get(vlParams[i]);
			colDefinitionList.remove(coluna);
			colDefinitionList.add(0, coluna);
		}
	}
	
	private boolean clientePossuiRede() {
		return cliente.cdRede != null && !ValueUtil.VALOR_ZERO.equals(cliente.cdRede);
	}

	private String[] getItemComRede(Object domain) throws SQLException {
		DescontoVenda descontoVenda = (DescontoVenda) domain;
        int colunasVisiveis = 1;
        if (LavenderePdaConfig.isTodasColunasVisiveisGridDescontoVolumeVendaMensal()) {
        	colunasVisiveis += 3;
        } else {
        	colunasVisiveis += LavenderePdaConfig.isColunaFaixaVisivelGridDescontoVolumeVendaMensal() ? 1 : 0;
        	colunasVisiveis += LavenderePdaConfig.isColunaPctDescontoVisivelGridDescontoVolumeVendaMensal() ? 1: 0;
        	colunasVisiveis += LavenderePdaConfig.isColunaVlPedidoVisivelGridDescontoVolumeVendaMensal() ? 1 : 0;
	}
		int size = colDefinitionList.size();
		String[] gridValuesDefinitionArray = new String[colunasVisiveis];
		for (int i = 0; i < size; i++) {
			GridColDefinition colDefinition = colDefinitionList.get(i);
			if (colDefinition.dsRotulo.equalsIgnoreCase(Messages.DESCONTO_VOLUME_VENDA_MENSAL_VOLUME_FAIXA_DESCONTO)) {
				gridValuesDefinitionArray[i] = StringUtil.getStringValueToInterface(descontoVenda.vlVenda);
			} else if (colDefinition.dsRotulo.equalsIgnoreCase(Messages.DESCONTO_VOLUME_VENDA_MENSAL_PCT_FAIXA_DESCONTO)) {
				gridValuesDefinitionArray[i] = StringUtil.getStringValueToInterface(descontoVenda.vlPctDesconto);
			} else if (colDefinition.dsRotulo.equalsIgnoreCase(Messages.DESCONTO_VOLUME_VENDA_MENSAL_VALOR_PEDIDO_FAIXA_DESCONTO)) {
				double vlPedidoComDescontoRede = vlTotalPedido * (1 - (descontoVenda.vlPctDesconto / 100));
				gridValuesDefinitionArray[i] = StringUtil.getStringValueToInterface(vlPedidoComDescontoRede);
	}
			if (i == colunasVisiveis - 1) break;
	}
		return gridValuesDefinitionArray;
	}

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, lbCliente, lvCliente, LEFT + HEIGHT_GAP, AFTER + HEIGHT_GAP);
		UiUtil.add(this, lbMes, lvMes, LEFT + HEIGHT_GAP, AFTER + HEIGHT_GAP);
		UiUtil.add(this, lbVlTotalPedido, lvVlTotalPedido, LEFT + HEIGHT_GAP, AFTER + HEIGHT_GAP);
		UiUtil.add(this, lbVlVendaMensal, lvVlVendaMensal, LEFT + HEIGHT_GAP, AFTER + HEIGHT_GAP);
		UiUtil.add(this, grid, LEFT + HEIGHT_GAP, AFTER + HEIGHT_GAP, FILL, FILL);
	}
		
	private void list() throws SQLException {
		descontoVendaList = null;
		descontoVendaList = DescontoVendaService.getInstance().findDescontoVendaByEmpresaUfCliente(cliente.cdEmpresa, cliente.cdEstadoComercial);
		
		int listSize = 0;
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		try {
			listSize = descontoVendaList.size();
			//--
			clearGrid();
			if (listSize > 0) {
				String[][] gridItems;
				String[] item;
				item = getItemComRede(descontoVendaList.items[0]);
				gridItems = new String[listSize][item.length];
				gridItems[0] = item;
				for (int i = 1; i < listSize; i++) {
					gridItems[i] = getItemComRede(descontoVendaList.items[i]);
	}
				item = null;
				//--
				grid.setItems(gridItems);
			}
		} finally {
			msg.unpop();
		}
	}

	private void clearGrid() {
		grid.clear();
		grid.setSelectedIndex(-1);
	}
	
	private void grifaFaixaVolumeVenda() {
    	int size = descontoVendaList.size();
    	boolean faixaAtingida = false;
    	int index = -1;
    	for (int i = 0; i < size; i++) {
    		DescontoVenda descontoVenda = (DescontoVenda) descontoVendaList.elementAt(i);
    		if (faixaCliente.vlVenda == descontoVenda.vlVenda) {
    			faixaAtingida = true;
    			index = i;
    			grid.setSelectedIndex(index);
    			break;
		}
    		if (faixaCliente.vlVenda >= descontoVenda.vlVenda && i + 1 != size) {
    			DescontoVenda proxDescVenda = (DescontoVenda) descontoVendaList.elementAt(i + 1);
    			if (faixaCliente.vlVenda >= proxDescVenda.vlVenda) {
    				continue;
    			} else {
    				faixaAtingida = true;
    				index = i;
    				grid.setSelectedIndex(index);
    				break;
	}
    		} else if (faixaCliente.vlVenda >= descontoVenda.vlVenda) {
    			faixaAtingida = true;
    			index = i;
    			grid.setSelectedIndex(index);
    		}
    	}
    	if (faixaAtingida && index != -1) {
    		grid.gridController.setRowBackColor(LavendereColorUtil.COR_FUNDO_LISTA_FAIXA_DESC_QUANTIDADE_ATINGIDO, index);
    		repaintNow();
    	} else {
    		grid.gridController.clearColors();
    		repaintNow();
    	}
    	descontoVendaList = null;
	}

}
