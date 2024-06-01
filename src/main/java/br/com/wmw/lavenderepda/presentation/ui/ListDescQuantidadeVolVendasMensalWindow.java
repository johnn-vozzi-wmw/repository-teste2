package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.DescQuantidade;
import br.com.wmw.lavenderepda.business.domain.DescontoVenda;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.service.CalculaEmbalagensService;
import br.com.wmw.lavenderepda.business.service.CalculaEmbalagensService.EmbalagensResultantes;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.ConversaoUnidadeService;
import br.com.wmw.lavenderepda.business.service.DescontoVendaService;
import br.com.wmw.lavenderepda.business.service.ProdutoUnidadeService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import br.com.wmw.lavenderepda.presentation.ui.combo.FaixasDescontoVendaComboBox;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.sys.SpecialKeys;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.GridEvent;
import totalcross.ui.event.KeyEvent;
import totalcross.util.Vector;

public class ListDescQuantidadeVolVendasMensalWindow extends WmwWindow {
	
	private static final String COLUNA_ROWKEY_KEY = "0";
	private static final String COLUNA_QUANT_FAIXA_DESC_KEY = "1";
	private static final String COLUNA_PCT_FAIXA_DESC_KEY = "2";
	private static final String COLUNA_VL_ITEM_FAIXA_DESC_KEY = "3";
	
	protected ButtonPopup btConfirmar;
	private EditNumberFrac edQuantidade;
	protected BaseGridEdit grid;
	protected FaixasDescontoVendaComboBox cbFaixasDescontoVenda;
	private LabelValue lvValorBaseItem;
	private LabelValue lvTotalVendaMensal;
	private LabelValue lvValorFinalItem;
	private LabelName lbQuantidade;
	private LabelName lbValorBaseItem;
	private LabelName lbTotalVendaMensal;
	private LabelName lbValorFinalItem;
	private LabelName lbGridFaixasDesconto;

	protected ItemPedido itemPedido;
	protected Produto produto;
	protected Cliente cliente;
	private Map<String, GridColDefinition> gridColDefinitionMap;
	private Map<String, Integer> gridColIndexesMap;
	private List<GridColDefinition> colDefinitionList = new ArrayList<GridColDefinition>();
	public DescQuantidade descontoQuantidadeSelected;
	public boolean isCanceled;
	protected Vector descQuantidadeList;
	protected double vlBaseItemPedido;
	private double vlTotalCalculoVolumeMensal = 0d;
	private double vlFinalItem = 0d;
	private boolean gerouEmbalagemCompleta;
	private DescontoVenda descontoVenda;
	private DescontoVenda descontoVendaPadrao;
	private boolean teclaEnterPressionada;
	
	public ListDescQuantidadeVolVendasMensalWindow(ItemPedido itemPedido, Vector descQuantidadeList) throws Exception {
		super(Messages.DESCONTOQUANTIDADE_VOLUME_VENDAS);
		this.itemPedido = itemPedido;
		this.produto = itemPedido.getProduto();
		this.vlBaseItemPedido = itemPedido.vlBaseItemPedido;
		this.descQuantidadeList = descQuantidadeList;
		this.cliente = itemPedido.pedido.getCliente();
		btConfirmar = new ButtonPopup(Messages.BT_CONFIRMAR);
		lbQuantidade = new LabelName(Messages.ITEMPEDIDO_LABEL_QTITEMFISICONAOABREVIADO);
		edQuantidade = new EditNumberFrac("999999999", 9, LavenderePdaConfig.isUsaQtdInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
		if (itemPedido.getQtItemFisico() > 0) {
			edQuantidade.setValue(itemPedido.getQtItemFisico());
		}
		lbValorBaseItem = new LabelName(Messages.DESCONTOQUANTIDADE_VOLUME_VENDAS_VL_BASE_ITEM);
		lvValorBaseItem = new LabelValue(StringUtil.getStringValueToInterface(vlBaseItemPedido));
		
		cbFaixasDescontoVenda = new FaixasDescontoVendaComboBox(Messages.DESCONTOQUANTIDADE_VOLUME_VENDAS_FAIXA_DESCONTO_MENSAL);
		cbFaixasDescontoVenda.load(itemPedido.cdEmpresa, cliente.cdEstadoComercial);
		carregaFaixaVendaCliente(itemPedido.pedido);
		
		lbTotalVendaMensal = new LabelName(Messages.DESCONTOQUANTIDADE_VOLUME_VENDAS_TOTAL_MENSAL);
		lvTotalVendaMensal = new LabelValue(StringUtil.getStringValueToInterface(vlTotalCalculoVolumeMensal));
		
		calculaValorFinalItem(descontoQuantidadeSelected, descontoVenda == null ? SessionLavenderePda.descontoVendaSimulado : descontoVenda);
		lbValorFinalItem = new LabelName(Messages.DESCONTOQUANTIDADE_VOLUME_VENDAS_VL_FINAL_ITEM);
		lvValorFinalItem = new LabelValue(StringUtil.getStringValueToInterface(vlFinalItem));
		
		loadGridColDefinitionMap();
		configureGrid();
		loadGridColIndexesMap();
		lbGridFaixasDesconto = new LabelName(Messages.DESCONTOQUANTIDADE_VOLUME_VENDAS_FAIXAS_DESC_QUANTIDADE);
		list();
		setDefaultRect();
	}
	
	@Override
	public void popup() {
		if (itemPedido.getQtItemFisico() > 0) {
			try {
				edQuantidadeChange();
			} catch (SQLException e) {
				ExceptionUtil.handle(e);
			}
		}
		super.popup();
	}

	private void loadGridColDefinitionMap() {
		gridColDefinitionMap = new LinkedHashMap<String, GridColDefinition>();
		int oneChar = fm.charWidth('A');
		boolean quantidadeVisivel = LavenderePdaConfig.isColunaQuantidadeVisivelGridDescontoQuantidade() || LavenderePdaConfig.isTodasColunasVisiveisGridDescontoQuantidade();
		boolean pctDescontoVisivel = LavenderePdaConfig.isColunaPctDescontoVisivelGridDescontoQuantidade() || LavenderePdaConfig.isTodasColunasVisiveisGridDescontoQuantidade();
		boolean vlItemVisivel = LavenderePdaConfig.isColunaVlItemVisivelGridDescontoQuantidade() || LavenderePdaConfig.isTodasColunasVisiveisGridDescontoQuantidade();
		
		GridColDefinition colunaRowkey = new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT);
		gridColDefinitionMap.put(COLUNA_ROWKEY_KEY, colunaRowkey);
		GridColDefinition colunaQuantidade = new GridColDefinition(Messages.ITEMPEDIDO_LABEL_QTITEMFISICONAOABREVIADO, quantidadeVisivel ? oneChar * 9 : 0, CENTER);
		gridColDefinitionMap.put(COLUNA_QUANT_FAIXA_DESC_KEY, colunaQuantidade);
		GridColDefinition colunaPctDesc = new GridColDefinition(Messages.DESCONTO_LABEL_VLPCTDESCONTO, pctDescontoVisivel ? oneChar * 10 : 0, CENTER);
		gridColDefinitionMap.put(COLUNA_PCT_FAIXA_DESC_KEY, colunaPctDesc);
		GridColDefinition colunaValorItemDesc = new GridColDefinition(Messages.DESCONTOQUANTIDADE_VOLUME_VENDAS_VALOR_ITEM_DESC, vlItemVisivel ? oneChar * 9 : 0, CENTER);
		gridColDefinitionMap.put(COLUNA_VL_ITEM_FAIXA_DESC_KEY, colunaValorItemDesc);
	}

	private void configureGrid() throws Exception {
		if (LavenderePdaConfig.isConfigColunasGridDescontoQuantidadeDesligado()) throw new Exception(Messages.DESCONTOQUANTIDADE_VOLUME_VENDAS_CONFIG_COLUNAS_EXCEPTION);
		
		colDefinitionList = new ArrayList<GridColDefinition>(gridColDefinitionMap.values());
		sortColDefinitionList();
		int size = colDefinitionList.size();
		GridColDefinition[] gridColDefinitionArray = new GridColDefinition[size];
		for (int i = 0; i < size; i++) {
			gridColDefinitionArray[i] = colDefinitionList.get(i);
		}
		grid = UiUtil.createGridEdit(gridColDefinitionArray);
		grid.setGridControllable();
	}
	
	private void sortColDefinitionList() {
		if (LavenderePdaConfig.isTodasColunasVisiveisGridDescontoQuantidade()) return;
		
		String[] vlParams = LavenderePdaConfig.configColunasGridDescontoQuantidade.split(";");
		int startPosition = vlParams.length - 1;
		for (int i = startPosition; i >= 0; i--) {
			GridColDefinition coluna = gridColDefinitionMap.get(vlParams[i]);
			colDefinitionList.remove(coluna);
			colDefinitionList.add(1, coluna);
		}
	}
	
	private void loadGridColIndexesMap() {
		gridColIndexesMap = new LinkedHashMap<String, Integer>();
		int size = colDefinitionList.size();
		for (int i = 0; i < size; i++) {
			GridColDefinition colDefinition = colDefinitionList.get(i);
			if (colDefinition.dsRotulo.equalsIgnoreCase(Messages.ITEMPEDIDO_LABEL_QTITEMFISICONAOABREVIADO)) {
				gridColIndexesMap.put(COLUNA_QUANT_FAIXA_DESC_KEY, i);
			} else if (colDefinition.dsRotulo.equalsIgnoreCase(Messages.DESCONTO_LABEL_VLPCTDESCONTO)) {
				gridColIndexesMap.put(COLUNA_PCT_FAIXA_DESC_KEY, i);
			} else if (colDefinition.dsRotulo.equalsIgnoreCase(Messages.DESCONTOQUANTIDADE_VOLUME_VENDAS_VALOR_ITEM_DESC)) {
				gridColIndexesMap.put(COLUNA_VL_ITEM_FAIXA_DESC_KEY, i);
			}
		}
	}
	
	@SuppressWarnings("unused")
	private int getColumnQuantidadeIndex() {
		int size = colDefinitionList.size();
		for (int i = 0; i < size; i++) {
			GridColDefinition coluna = colDefinitionList.get(i);
			if (coluna.dsRotulo.equalsIgnoreCase(Messages.ITEMPEDIDO_LABEL_QTITEMFISICONAOABREVIADO)) return i;
		}
		return -1;
	}
	
	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, new LabelName(Messages.PRODUTO_NOME_ENTIDADE), new LabelValue(produto.toString()), getLeft(), getTop() + HEIGHT_GAP);
		UiUtil.add(this, lbQuantidade, getLeft(), AFTER + HEIGHT_GAP, 300, UiUtil.getLabelPreferredHeight());
		UiUtil.add(this, lbValorBaseItem, RIGHT - HEIGHT_GAP, SAME);
		UiUtil.add(this, edQuantidade, getLeft(), AFTER, 100);
		UiUtil.add(this, lvValorBaseItem, RIGHT - HEIGHT_GAP, SAME + HEIGHT_GAP_BIG);
		UiUtil.add(this, lbGridFaixasDesconto, getLeft(), AFTER + HEIGHT_GAP_BIG + HEIGHT_GAP);
		UiUtil.add(this, grid, getLeft(), AFTER, FILL - HEIGHT_GAP, (int) (getHeight() / 2.5));
		UiUtil.add(this, new LabelName(Messages.DESCONTOQUANTIDADE_VOLUME_VENDAS_FAIXA_DESCONTO_MENSAL), cbFaixasDescontoVenda, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(this, lbTotalVendaMensal, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(this, lbValorFinalItem, RIGHT - HEIGHT_GAP, SAME);
		UiUtil.add(this, lvTotalVendaMensal, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(this, lvValorFinalItem, RIGHT - HEIGHT_GAP, SAME);
		lbValorFinalItem.setText(SessionLavenderePda.descontoVendaSimulado != null ? Messages.DESCONTOQUANTIDADE_VOLUME_VENDAS_VL_SIMULADO_ITEM : Messages.DESCONTOQUANTIDADE_VOLUME_VENDAS_VL_FINAL_ITEM);
	}
	
	@Override
	protected void addBtFechar() {
		addButtonPopup(btConfirmar);
		super.addBtFechar();
	}
	
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btConfirmar) {
					if (calculaEmbalagensCompletas(edQuantidade.getValueDouble(), true)) {
						if (confirmarClick()) {
							unpop();
						}
					}
				} else if (event.target == btFechar) {
					isCanceled = true;
					unpop();
				} else if (event.target == cbFaixasDescontoVenda) {
					cbFaixasDescontoVendaChange();
				}
				break;
			}
			case GridEvent.SELECTED_EVENT: {
				if ((event.target == grid) && (grid.getSelectedIndex() != -1)) {
					grid.gridController.clearColors();
					gridClick();
					grifaDescQuantidadeAtingido();
					repaintValores();
				}
				break;
			}
			case ControlEvent.FOCUS_OUT: {
				if (event.target == edQuantidade && !teclaEnterPressionada) {
					grid.gridController.clearColors();
					edQuantidadeChange();
					teclaEnterPressionada = false;
				}
				break;
			}
			case KeyEvent.SPECIAL_KEY_PRESS: {
				KeyEvent ke = (KeyEvent) event;
				int key = ke.key;
				if (ke.isActionKey()) {
					grid.gridController.clearColors();
					edQuantidadeChange();
					teclaEnterPressionada = true;
				} else if (key == SpecialKeys.BACKSPACE) {
					grid.gridController.clearColors();
				}
				break;
			}
		}
	}

	private void edQuantidadeChange() throws SQLException {
		descontoQuantidadeSelected = null;
		calculaDescQtdEmbalagemCompleta((int) edQuantidade.getValueDouble(), true);
		if (grifaDescQuantidadeAtingido()) {
			descontoQuantidadeSelected = getSelectedDomainOnGrid();
		}
		calculaValorFinalItem(descontoQuantidadeSelected, descontoVenda);
		repaintValores();
	}
	
	private boolean confirmarClick() throws SQLException {
		double quantidade = edQuantidade.getValueDouble();
		if (quantidade == 0) {
			UiUtil.showErrorMessage(Messages.DESCONTOQUANTIDADE_VOLUME_VENDAS_QUANTIDADE_NAO_INFORMADA);
			return false;
		}
		double qtItemFisicoOld = itemPedido.getQtItemFisico();
		double qtItemDesejadoOld = itemPedido.qtItemDesejado;
		itemPedido.setQtItemFisico(quantidade);
		if (LavenderePdaConfig.usaInsercaoQuantidadeDesejadaPedido) {
			itemPedido.qtItemDesejado = quantidade;
		}
		try {
			ConversaoUnidadeService.getInstance().validadeConversaoUnidade(itemPedido.pedido, itemPedido);
		} catch (ValidationException ve) {
			itemPedido.setQtItemFisico(qtItemFisicoOld);
			itemPedido.qtItemDesejado = qtItemDesejadoOld;
			if (LavenderePdaConfig.isConsisteConversaoUnidades() || LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecial() ||
					(LavenderePdaConfig.consisteMultiploEmbalagem && itemPedido.getProduto().flConsisteConversaoUnidade == null)) {
				throw ve;
			} else if (LavenderePdaConfig.avisaConversaoUnidades || (LavenderePdaConfig.consisteMultiploEmbalagem && !ValueUtil.VALOR_NAO.equals(itemPedido.getProduto().flConsisteConversaoUnidade))) {
				if (UiUtil.showConfirmYesCancelMessage(ve.getMessage() + " Deseja continuar?") == 0) {
					throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
				}
			}
		}
		return true;
	}

	private void gridClick() throws SQLException {
		descontoQuantidadeSelected = getSelectedDomainOnGrid();
		if (descontoQuantidadeSelected == null) return;
		
		edQuantidade.setValue(descontoQuantidadeSelected.qtItem);
		calculaEmbalagensCompletas(descontoQuantidadeSelected.qtItem, true);
		calculaValorFinalItem(descontoQuantidadeSelected, descontoVenda);
		repaintValores();
	}
	
    private DescQuantidade getSelectedDomainOnGrid() {
        String[] item = grid.getSelectedItem();
        if (item == null) return null;
        
        DescQuantidade descontoQuantidade = new DescQuantidade();
       	descontoQuantidade.rowKey = item[0];
        double qtItemSemSeparadores = ValueUtil.getDoubleValue(ValueUtil.removeThousandSeparator(item[gridColIndexesMap.get(COLUNA_QUANT_FAIXA_DESC_KEY)]));
        descontoQuantidade.qtItem = ValueUtil.getIntegerValue(qtItemSemSeparadores);
        descontoQuantidade.vlPctDesconto = ValueUtil.getDoubleValue(item[gridColIndexesMap.get(COLUNA_PCT_FAIXA_DESC_KEY)]);
        descontoQuantidade.vlDesconto = ValueUtil.getDoubleValue(item[gridColIndexesMap.get(COLUNA_VL_ITEM_FAIXA_DESC_KEY)]);
        return descontoQuantidade;
    }
    
	private void list() throws SQLException {
		int listSize = 0;
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		try {
			Vector domainList = this.descQuantidadeList;
			listSize = domainList.size();
			//--
			clearGrid();
			if (listSize > 0) {
				String[][] gridItems;
				String[] item = getItem(domainList.items[0]);
				gridItems = new String[listSize][item.length];
				gridItems[0] = item;
				for (int i = 1; i < listSize; i++) {
					gridItems[i] = getItem(domainList.items[i]);
				}
				item = null;
				//--
				grid.setItems(gridItems);
				domainList = null;
			}
		} finally {
			msg.unpop();
		}
	}

	private void clearGrid() {
		grid.clear();
		grid.setSelectedIndex(-1);
	}

    private String[] getItem(Object domain) throws java.sql.SQLException {
        DescQuantidade descontoQuantidade = (DescQuantidade) domain;
        int colunasVisiveis = 1;
        if (LavenderePdaConfig.isTodasColunasVisiveisGridDescontoQuantidade()) {
        	colunasVisiveis += 3;
        } else {
        	colunasVisiveis += LavenderePdaConfig.isColunaQuantidadeVisivelGridDescontoQuantidade() ? 1 : 0;
        	colunasVisiveis += LavenderePdaConfig.isColunaPctDescontoVisivelGridDescontoQuantidade() ? 1: 0;
        	colunasVisiveis += LavenderePdaConfig.isColunaVlItemVisivelGridDescontoQuantidade() ? 1 : 0;
        }
		int size = colDefinitionList.size();
		String[] gridValuesDefinitionArray = new String[colunasVisiveis];
		for (int i = 0; i < size; i++) {
			if (i == 0) {
				gridValuesDefinitionArray[0] = StringUtil.getStringValue(descontoQuantidade.rowKey); 
				continue;
			}
			GridColDefinition colDefinition = colDefinitionList.get(i);
			if (colDefinition.dsRotulo.equalsIgnoreCase(Messages.ITEMPEDIDO_LABEL_QTITEMFISICONAOABREVIADO)) {
				gridValuesDefinitionArray[i] = StringUtil.getStringValueToInterface(descontoQuantidade.qtItem, LavenderePdaConfig.isUsaQtdInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
			} else if (colDefinition.dsRotulo.equalsIgnoreCase(Messages.DESCONTO_LABEL_VLPCTDESCONTO)) {
				gridValuesDefinitionArray[i] = StringUtil.getStringValueToInterface(descontoQuantidade.vlPctDesconto);
			} else if (colDefinition.dsRotulo.equalsIgnoreCase(Messages.DESCONTOQUANTIDADE_VOLUME_VENDAS_VALOR_ITEM_DESC)) {
				gridValuesDefinitionArray[i] = StringUtil.getStringValueToInterface(descontoQuantidade.getVlDescVlBaseItemPedido(vlBaseItemPedido));
			}
			if (i == colunasVisiveis - 1) break;
		}
        return gridValuesDefinitionArray;
    }
    
    private void carregaFaixaVendaCliente(Pedido pedido) throws SQLException {
    	double vlVendasMensal = 0d;
    	double vlPedidoAtual = 0d;
    	if (LavenderePdaConfig.usaVolumeVendaMensalRede && (cliente.cdRede != null && !ValueUtil.VALOR_ZERO.equals(cliente.cdRede))) {
    		vlPedidoAtual = pedido.vlTotalItens;
    		vlVendasMensal = ClienteService.getInstance().sumVlVendaMensalClientesRede(cliente);
    		vlVendasMensal += PedidoPdbxDao.getInstance().sumVlTotalPedidosConsideraVolumeVendaMensalPorStatus(pedido);
    	} else {
    		vlVendasMensal = cliente.vlVendaMensal;
    		vlPedidoAtual = pedido.vlTotalPedido;
    	}
    	
		vlTotalCalculoVolumeMensal = vlVendasMensal + vlPedidoAtual;
		if (SessionLavenderePda.descontoVendaSimulado != null) {
			cbFaixasDescontoVenda.setValue(SessionLavenderePda.descontoVendaSimulado);
			descontoVendaPadrao = DescontoVendaService.getInstance().getDescontoVenda(pedido.cdEmpresa, cliente.cdEstadoComercial, vlTotalCalculoVolumeMensal);
			return;
		}
		if (vlTotalCalculoVolumeMensal == 0) return;
		
		descontoVenda = DescontoVendaService.getInstance().getDescontoVenda(pedido.cdEmpresa, cliente.cdEstadoComercial, vlTotalCalculoVolumeMensal);
		if (descontoVenda != null) {
			descontoVendaPadrao = (DescontoVenda) descontoVenda.clone();
			cbFaixasDescontoVenda.setValue(descontoVenda);
		} else {
			cbFaixasDescontoVenda.setSelectedIndex(0);
		}
    }
    
    private void calculaValorFinalItem(DescQuantidade descQuantidade, DescontoVenda descVenda) {
    	vlFinalItem = vlBaseItemPedido;
    	if (descQuantidade != null) {
			vlFinalItem = descQuantidade.getVlDescVlBaseItemPedido(vlBaseItemPedido);
    	}
    	if (descVenda != null) {
    		vlFinalItem = vlFinalItem * (1 - (descVenda.vlPctDesconto / 100));
    	} else if (SessionLavenderePda.descontoVendaSimulado != null) {
    		vlFinalItem = vlFinalItem * (1 - (SessionLavenderePda.descontoVendaSimulado.vlPctDesconto / 100));
    	}
    }
    
    private boolean grifaDescQuantidadeAtingido() {
    	if (LavenderePdaConfig.usaDescQuantidadeApenasEmbalagemCompleta && !gerouEmbalagemCompleta) {
    		clearGridSelection();
    		return false;
    	}
    	
    	double quantidade = edQuantidade.getValueDouble();
    	if (quantidade <= 0) {
    		clearGridSelection();
    		return false;
    	}
    	
    	int size = descQuantidadeList.size();
    	boolean faixaAtingida = false;
    	int index = -1;
    	for (int i = 0; i < size; i++) {
    		DescQuantidade descQuantidade = (DescQuantidade) descQuantidadeList.elementAt(i);
    		if (quantidade == descQuantidade.qtItem) {
    			faixaAtingida = true;
    			index = i;
    			grid.setSelectedIndex(index);
    			break;
    		}
    		if (quantidade > descQuantidade.qtItem && i + 1 != size) {
    			DescQuantidade proxDescQuantidade = (DescQuantidade) descQuantidadeList.elementAt(i + 1);
    			if (quantidade > proxDescQuantidade.qtItem && proxDescQuantidade.qtItem > descQuantidade.qtItem) {
    				continue;
    			} else {
    				faixaAtingida = true;
    				index = i;
    				grid.setSelectedIndex(index);
    				break;
    			}
    		} else if (quantidade > descQuantidade.qtItem) {
    			faixaAtingida = true;
    			index = i;
    			grid.setSelectedIndex(index);
    		}
    	}
    	if (faixaAtingida && index != -1) {
    		grid.gridController.setRowBackColor(LavendereColorUtil.COR_FUNDO_LISTA_FAIXA_DESC_QUANTIDADE_ATINGIDO, index);
    		repaintNow();
    		return true;
    	} else {
    		grid.gridController.clearColors();
    		repaintNow();
    		return false;
    	}
    }

	private void clearGridSelection() {
		grid.gridController.clearColors();
		grid.setSelectedIndex(-1);
		descontoQuantidadeSelected = null;
		repaintNow();
	}
    
    private boolean calculaEmbalagensCompletas(double quantidade, boolean fromGridClick) throws SQLException {
    	if (LavenderePdaConfig.usaDescQuantidadeApenasEmbalagemCompleta && quantidade > 0) {
    		EmbalagensResultantes embalagensResultantes = calculaDescQtdEmbalagemCompleta((int) quantidade, true);
    		if (embalagensResultantes != null && !embalagensResultantes.gerouEmbalagemCompleta()) {
    			return UiUtil.showConfirmYesNoMessage(getResultadoInText(embalagensResultantes));
    		} else if (!gerouEmbalagemCompleta) {
				UiUtil.showErrorMessage(Messages.APLICA_DESCONTOQTD_EMB_COMPLETA_CALCULO_ERRO);
			}
    	}
    	return true;
    }
    
	protected EmbalagensResultantes calculaDescQtdEmbalagemCompleta(double quantidade, final boolean calculaSugestaoEmb) throws SQLException {
		EmbalagensResultantes embalagensResultantes = null;
		if (ValueUtil.isNotEmpty(itemPedido.getItemTabelaPreco().descontoQuantidadeList)) {
			int qtMenorDescQuantidade = getMenorQtdItemDescQuantidade();
			if (qtMenorDescQuantidade > 0 && quantidade < qtMenorDescQuantidade) {
				gerouEmbalagemCompleta = false;
				grid.gridController.clearColors();
				repaintNow();
				return  CalculaEmbalagensService.getInstance().createEmbalagem(qtMenorDescQuantidade);
			}
			ProdutoUnidade produtoUnidadeFilter = new ProdutoUnidade();
			produtoUnidadeFilter.cdEmpresa = itemPedido.cdEmpresa;
			produtoUnidadeFilter.cdRepresentante = itemPedido.cdRepresentante;
			produtoUnidadeFilter.cdProduto = itemPedido.cdProduto;
			double[] tamanhosEmbalagensList = ProdutoUnidadeService.getInstance().getTamanhosEmbalagensByExample(produtoUnidadeFilter);
			if (tamanhosEmbalagensList != null) {
				boolean qtdDigitadaFormaEmbalagemCompleta = false;
				for (double tamanhoEmbalagem : tamanhosEmbalagensList) {
					if (tamanhoEmbalagem == quantidade) {
						qtdDigitadaFormaEmbalagemCompleta = true;
						break;
					}
				}
				if (!qtdDigitadaFormaEmbalagemCompleta) {
					embalagensResultantes = CalculaEmbalagensService.getInstance().getEmbalagensResultantes(tamanhosEmbalagensList, quantidade, calculaSugestaoEmb);
					if (embalagensResultantes.gerouEmbalagemCompleta()) {
						gerouEmbalagemCompleta = true;
					} else {
						gerouEmbalagemCompleta = false;
					}
				} else {
					gerouEmbalagemCompleta = true;
				}
			}
		}
		return embalagensResultantes;
	}
	
	private int getMenorQtdItemDescQuantidade() {
		int size = descQuantidadeList.size();
		int qtMenorDescQuantidade = 0;
		for (int i = 0; i < size; i++) {
			DescQuantidade descQuantidade = (DescQuantidade) descQuantidadeList.items[i];
			if (qtMenorDescQuantidade == 0 || qtMenorDescQuantidade > descQuantidade.qtItem) qtMenorDescQuantidade = descQuantidade.qtItem;
			
		}
		return qtMenorDescQuantidade;
	}

	private void cbFaixasDescontoVendaChange() {
		descontoVenda = cbFaixasDescontoVenda.getValue();
		if (!descontoVenda.equals(descontoVendaPadrao)) {
			lbValorFinalItem.setText(Messages.DESCONTOQUANTIDADE_VOLUME_VENDAS_VL_SIMULADO_ITEM);
			SessionLavenderePda.descontoVendaSimulado = descontoVenda;
		} else {
			lbValorFinalItem.setText(Messages.DESCONTOQUANTIDADE_VOLUME_VENDAS_VL_FINAL_ITEM);
			SessionLavenderePda.descontoVendaSimulado = null;
		}
		calculaValorFinalItem(descontoQuantidadeSelected, descontoVenda);
		repaintValores();
	}
	
	private void repaintValores() {
		lvValorFinalItem.setText(StringUtil.getStringValueToInterface(vlFinalItem));
		repaintNow();
	}
	
	private String getResultadoInText(EmbalagensResultantes embalagensResultantes) {
		double qtdEmbalagemMenor = embalagensResultantes.getResultadoEmbalagemCompletaMenor();
		double qtdEmbalagemMaior = embalagensResultantes.getResultadoEmbalagemCompletaMaior();
		if (qtdEmbalagemMenor == 0) {
			return MessageUtil.getMessage(Messages.APLICA_DESCONTOQTD_EMBALAGEM_COMPLETA_MAX, StringUtil.getStringValueToInterface(qtdEmbalagemMaior));
		} else {
			return MessageUtil.getMessage(Messages.APLICA_DESCONTOQTD_EMBALAGEM_COMPLETA_MIN_MAX, new String[]{StringUtil.getStringValueToInterface(qtdEmbalagemMenor), StringUtil.getStringValueToInterface(qtdEmbalagemMaior)});
		}
	}

}
