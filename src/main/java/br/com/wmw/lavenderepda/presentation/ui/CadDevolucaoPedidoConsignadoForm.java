package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.event.KeyboardEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ScannerCameraUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoConsignacao;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoTabPreco;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.service.PedidoConsignacaoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoUnidadeService;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto1ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto2ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto3ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto4ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.PedidoUiUtil;
import br.com.wmw.lavenderepda.print.PedidoConsignacaoManagerPrint;
import totalcross.io.device.scanner.ScanEvent;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.ui.gfx.Color;
import totalcross.util.Vector;

public class CadDevolucaoPedidoConsignadoForm extends BaseCrudCadForm {
	
	private Pedido pedido;
	private String dsFiltro;
	private Vector itemPedidoConsignadoList;
	private Vector itemPedidoConsignadoComQtdDevolvidaList;
	
	
	private EditFiltro edFiltro;
	private BaseGridEdit grid;
	private EditNumberFrac edControlGrid;
	private LabelName lbGrupoProduto1;
	private GrupoProduto1ComboBox cbGrupoProduto1;
	private GrupoProduto2ComboBox cbGrupoProduto2;
	private GrupoProduto3ComboBox cbGrupoProduto3;
	private GrupoProduto4ComboBox cbGrupoProduto4;
	private ButtonAction btLeitorCamera;
	

	public CadDevolucaoPedidoConsignadoForm(Pedido pedido) throws SQLException {
		super(Messages.BOTAO_DEVOLUCAO_CONSIGNACAO_PEDIDO);
		this.pedido = pedido;
		itemPedidoConsignadoList = new Vector();
		itemPedidoConsignadoComQtdDevolvidaList = new Vector();
		edFiltro = new EditFiltro("999999999", 25);
		edFiltro.idAgrupador = PedidoConsignacao.APPOBJ_CAMPOS_FILTRO_PEDIDO_CONSIGNACAO;
		edControlGrid = new EditNumberFrac("00000", 9);
		if (LavenderePdaConfig.usaCameraParaLeituraCdBarras()) {
			btLeitorCamera = new ButtonAction(Messages.CAMERA, "images/barcode.png");
		}
		montaGrid();
		montaGrupoProduto();
		loadLabelGruposProdutos();
	}
	
	@Override
	protected BaseDomain screenToDomain() {
		return null;
	}

	@Override
	protected void domainToScreen(BaseDomain domain) {
		
	}

	@Override
	protected void clearScreen() {
		
	}

	@Override
	protected BaseDomain createDomain() {
		return null;
	}

	@Override
	protected String getEntityDescription() {
		return Messages.BOTAO_DEVOLUCAO_CONSIGNACAO_PEDIDO;
	}

	@Override
	protected CrudService getCrudService() {
		return PedidoConsignacaoService.getInstance();
	}

	@Override
	protected void onFormStart() {
		int yFiltrosTop = TOP + barTopContainer.getHeight();
		Vector filtrosVisiveis = new Vector(StringUtil.split(LavenderePdaConfig.filtrosFixoTelaItemPedido, ';'));
		boolean exibeFiltroGrupoProduto = LavenderePdaConfig.usaFiltroGrupoProduto != 0 && (LavenderePdaConfig.isTodosFiltrosFixosTelaItemPedido() || filtrosVisiveis.indexOf(CadItemPedidoForm.NU_FILTRO_GRUPOPRODUTO) != -1);
		if (exibeFiltroGrupoProduto) {
			UiUtil.add(this, lbGrupoProduto1, getLeft(), yFiltrosTop);
   			UiUtil.add(this, cbGrupoProduto1, getLeft(), AFTER);
   			cbGrupoProduto1.setSelectedIndex(0);
   			yFiltrosTop = AFTER + HEIGHT_GAP;
   			//--
			if (LavenderePdaConfig.usaFiltroGrupoProduto >= 2) {
       			UiUtil.add(this, cbGrupoProduto2, getLeft(), yFiltrosTop);
       			cbGrupoProduto2.setSelectedIndex(0);
			}
			if (LavenderePdaConfig.usaFiltroGrupoProduto >= 3) {
       			UiUtil.add(this, cbGrupoProduto3, getLeft(), yFiltrosTop);
       			cbGrupoProduto3.setSelectedIndex(0);
			}
			if (LavenderePdaConfig.usaFiltroGrupoProduto >= 4) {
				UiUtil.add(this, cbGrupoProduto4, getLeft(), yFiltrosTop);
				cbGrupoProduto4.setSelectedIndex(0);
			}
		}
		UiUtil.add(this, edFiltro, getLeft(), yFiltrosTop);
		UiUtil.add(this, grid, LEFT, AFTER + HEIGHT_GAP, FILL, FILL - barBottomContainer.getHeight());
		if (LavenderePdaConfig.usaCameraParaLeituraCdBarras()) {
			UiUtil.add(barBottomContainer, btLeitorCamera, 4);
		}
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == cbGrupoProduto1) {
					cbGrupoProduto1Change();
					filtrarClick(event.target, false);
				} else if (event.target == cbGrupoProduto2) {
					cbGrupoProduto2Change();
					filtrarClick(event.target, false);
				} else if (event.target == cbGrupoProduto3) {
					cbGrupoProduto3Change();
					filtrarClick(event.target, false);
				} else if (event.target == cbGrupoProduto4) {
					filtrarClick(event.target, false);
				} else if (event.target == btLeitorCamera) {
					realizaLeituraCamera();
				} 
				break;
			}
			case EditIconEvent.PRESSED: {
				if (event.target == edFiltro) {
					filtrarClick();
				}
				break;
			}
			case KeyboardEvent.KEYBOARD_PRESS: {
				if (event.target == edFiltro) {
					filtrarClick();
				}
				break;
			}
			case ScanEvent.SCANNED: {
				if (event.target == edFiltro) {
					filtrarLeitorCdBarrasClick();
				}
				break;
			}
			case KeyEvent.SPECIAL_KEY_PRESS: {
				KeyEvent ke = (KeyEvent) event;
				if (ke.isActionKey() && event.target == edFiltro) {
					filtrarLeitorCdBarrasClick();
				}
				break;
			}
			case ControlEvent.FOCUS_OUT: {
	    		if (event.target instanceof EditNumberFrac) {
	    			updateQtdDevolvida(false);
	    		}
	    		break;
	    	}
		}
	}
	
	@Override
	protected void refreshComponents() throws SQLException {
		itemPedidoConsignadoList.removeAllElements();
		itemPedidoConsignadoList.addElementsNotNull(pedido.itemPedidoList.items);
		carregaLista();
	}

	@Override
	protected void visibleState() throws SQLException {
		super.visibleState();
		if (!LavenderePdaConfig.usaIncrementoQuantidadePorLeituraCodigoBarras) {
			stopScanner();
		} else {
			startScanner(); 
		}
		if (LavenderePdaConfig.usaCameraParaLeituraCdBarras()) {
			btLeitorCamera.setVisible(LavenderePdaConfig.usaIncrementoQuantidadePorLeituraCodigoBarras);
		}
	}
	
	@Override
	protected void processaLeitura(ScanEvent event) throws SQLException {
		dsFiltro = event.data;
		String errorCode = "NR";
		if (!errorCode.equals(dsFiltro)) {
			super.processaLeitura(event);
			realizaFiltroLeituraCdBarras();
		}
	}
	
	
	@Override
	public void onFormShow() throws SQLException {
		super.onFormShow();
		limpaFiltros();
		edFiltro.requestFocus();
	}
	
	@Override
	protected void voltarClick() throws SQLException {
		itemPedidoConsignadoComQtdDevolvidaList.removeAllElements();
		super.voltarClick();
	}
	
	
	@Override
	protected void salvarClick() throws SQLException {
		Vector itemPedidoDevolvidoList = PedidoConsignacaoService.getInstance().getItemPedidoDevolvidoList(itemPedidoConsignadoList);
		if (itemPedidoDevolvidoList.size() > 0) {
			PedidoConsignacaoService.getInstance().validaPercentualLimiteDevolucaoCliente(pedido, itemPedidoDevolvidoList);
			if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_CONSIGNACAO_MSG_CONFIRM_DEVOLUCAO)) {
				super.salvarClick();
				itemPedidoConsignadoComQtdDevolvidaList.removeAllElements();
				if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_CONFIRMA_ENVIO_DEVOLUCAO)) {
					enviaDevolucaoPedidoConsignado();
				}
				if (LavenderePdaConfig.isSugereImpressaoPedidoConsignacaoDevolucao()) {
					Vector pedidoConsignacaoDevolucaoAtualList = PedidoConsignacaoService.getInstance().getPedidoConsignacaoDevolucaoAtualList(itemPedidoDevolvidoList);
					sugereImpressaoPedidoConsignacaoDevolucao(pedidoConsignacaoDevolucaoAtualList);
				}
			}
		} else {
			UiUtil.showErrorMessage(Messages.PEDIDO_CONSIGNACAO_MSG_ERRO_NENHUM_ITEM_POSSUI_INFO_DEVOLUCAO);
		}
	}
		
	@Override
	protected void insert(BaseDomain domain) throws SQLException {
		PedidoConsignacaoService.getInstance().insereDevolucaoPedidoConsignado(itemPedidoConsignadoComQtdDevolvidaList);
		PedidoService.getInstance().atualizaPedidoAposDevolucao(itemPedidoConsignadoComQtdDevolvidaList);
	}
	
	private void enviaDevolucaoPedidoConsignado() throws SQLException {
		if (LavenderePdaConfig.usaEnvioPedidoBackground) {
			PedidoConsignacaoService.getInstance().enviaPedidosConsignacaoBackground(pedido);
			close();
		} else {
			PedidoUiUtil.enviaConsignacao();
		}
	}
	
	private void updateQtdDevolvida(boolean usouLeituraCdBarras) {
		double qtTtItemDevolvido = 0;
        for (int i = 0; i < (grid.size()); i++) {
        	ItemPedido itemPedido = (ItemPedido) itemPedidoConsignadoList.items[i];
    		qtTtItemDevolvido = !usouLeituraCdBarras ? ValueUtil.round(ValueUtil.getDoubleValue(grid.getItem(i)[3])) : (itemPedido.qtDevolvida + 1);
    		qtTtItemDevolvido = LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(qtTtItemDevolvido) : qtTtItemDevolvido;
    		if (qtTtItemDevolvido > itemPedido.getQtItemFisico()) {
    			String[] registroDevolucao = grid.getItem(i);
    			registroDevolucao [3] = StringUtil.getStringValueToInterface(itemPedido.qtDevolvida);
    			grid.setItens(registroDevolucao, i);
    			UiUtil.showErrorMessage(Messages.PEDIDO_CONSIGNACAO_MSG_QTD_DEVOLVIDA_MAIOR_INSERIDA_PEDIDO);
    		} else {
    			itemPedido.qtDevolvida = qtTtItemDevolvido;
    			if (!itemPedidoConsignadoComQtdDevolvidaList.contains(itemPedido)) {
    				itemPedidoConsignadoComQtdDevolvidaList.addElement(itemPedido);
    			}
    		}
        }
        
	}
	
	private void montaGrid() throws SQLException {
		int pnuTamanhoLote = LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil ? -17 : 0;
		int pnuTamanhoProduto = pnuTamanhoLote == 0 ? -61 : -44;
		GridColDefinition[] gridColDefiniton = { new GridColDefinition(Messages.ITEMPEDIDO_LABEL_PRODUTO, pnuTamanhoProduto, LEFT), 
				new GridColDefinition(LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil ? Messages.LOTEPRODUTO_LOTE : FrameworkMessages.CAMPO_VAZIO, pnuTamanhoLote, LEFT), 
				new GridColDefinition(Messages.PEDIDO_CONSIGNACAO_QTD_INSERIDO, -17, RIGHT), 
				new GridColDefinition(Messages.PEDIDO_CONSIGNACAO_QTD_ITEM_DEVOLVIDO, -17, RIGHT)};
		grid = UiUtil.createGridEdit(gridColDefiniton);
		edControlGrid = grid.setColumnEditableDouble(3, true, 9);
		edControlGrid.autoSelect = true;
		grid.captionsBackColor = ColorUtil.gridCaptionsBackColor;
		grid.setGridControllable();
		grid.disableSort = false;
		refreshComponents();
		grid.drawHighlight = false;
		grid.gridController.setRowDisable(grid.size());
	}
	
	private void carregaLista() throws SQLException {
		grid.removeAllElements();
		for (int i = 0; i < itemPedidoConsignadoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido)itemPedidoConsignadoList.items[i];
			for (int j = 0; j < itemPedidoConsignadoComQtdDevolvidaList.size(); j++) {
				ItemPedido itemPedidoComQtdDevolvida = (ItemPedido)itemPedidoConsignadoComQtdDevolvidaList.items[j];
				if (ValueUtil.valueEquals(itemPedido, itemPedidoComQtdDevolvida)) {
					itemPedido.qtDevolvida = itemPedidoComQtdDevolvida.qtDevolvida;
				}
			}
			grid.add(new String[]{itemPedido.getProduto().toString(), itemPedido.cdLoteProduto, StringUtil.getStringValueToInterface(itemPedido.getQtItemFisico()), StringUtil.getStringValueToInterface(itemPedido.qtDevolvida), itemPedido.getRowKey()});
		}
	}
	
	private void montaGrupoProduto() throws SQLException {
		cbGrupoProduto1 = new GrupoProduto1ComboBox();
		cbGrupoProduto2 = new GrupoProduto2ComboBox();
		cbGrupoProduto3 = new GrupoProduto3ComboBox();
		cbGrupoProduto4 = new GrupoProduto4ComboBox();
		if (LavenderePdaConfig.usaFiltroGrupoProduto != 0) {
			lbGrupoProduto1 = new LabelName(Messages.GRUPOPRODUTO1_CDGRUPOPRODUTO1);
			cbGrupoProduto1.popupTitle = lbGrupoProduto1.getValue();
			cbGrupoProduto1.loadGrupoProduto1(null);
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 1) {
				lbGrupoProduto1.setText(lbGrupoProduto1.getText() + " / " + Messages.GRUPOPRODUTO2_CDGRUPOPRODUTO2);
				cbGrupoProduto2.popupTitle = Messages.GRUPOPRODUTO2_CDGRUPOPRODUTO2;
				cbGrupoProduto2.loadGrupoProduto2(cbGrupoProduto1.getValue(), null);
			}
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 2) {
				lbGrupoProduto1.setText(lbGrupoProduto1.getText() + " / " + Messages.GRUPOPRODUTO3_CDGRUPOPRODUTO3);
				cbGrupoProduto3.popupTitle = Messages.GRUPOPRODUTO3_CDGRUPOPRODUTO3;
				cbGrupoProduto3.loadGrupoProduto3(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), null);
			}
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 3) {
				lbGrupoProduto1.setText(lbGrupoProduto1.getText() + " / " + Messages.GRUPOPRODUTO4_CDGRUPOPRODUTO4);
				cbGrupoProduto4.popupTitle = Messages.GRUPOPRODUTO4_CDGRUPOPRODUTO4;
				cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue());
				
			}
		}
	}
	
	private void cbGrupoProduto1Change() throws SQLException {
		cbGrupoProduto2.loadGrupoProduto2(cbGrupoProduto1.getValue(), pedido);
		cbGrupoProduto2.setSelectedIndex(0);
		cbGrupoProduto3.loadGrupoProduto3(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), pedido);
		cbGrupoProduto3.setSelectedIndex(0);
		cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue());
		cbGrupoProduto4.setSelectedIndex(0);
	}

	private void cbGrupoProduto2Change() throws SQLException {
		cbGrupoProduto3.loadGrupoProduto3(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), pedido);
		cbGrupoProduto3.setSelectedIndex(0);
		cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue());
		cbGrupoProduto4.setSelectedIndex(0);
	}
	
	private void cbGrupoProduto3Change() throws SQLException {
		cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue());
		cbGrupoProduto4.setSelectedIndex(0);
	}
	
	private void loadLabelGruposProdutos() {
		if (ValueUtil.isNotEmpty(LavenderePdaConfig.labelGruposProduto) && !ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.labelGruposProduto)) {
			String[] temp = StringUtil.split(LavenderePdaConfig.labelGruposProduto, ';');
			if ((temp.length > 0) && (lbGrupoProduto1 != null)) {
				lbGrupoProduto1.setText(temp[0]);
				cbGrupoProduto1.popupTitle = temp[0];
				if ((temp.length > 1) && LavenderePdaConfig.usaFiltroGrupoProduto >= 2) {
					lbGrupoProduto1.setText(lbGrupoProduto1.getText() + " / " + temp[1]);
					cbGrupoProduto2.popupTitle = temp[1];
					if ((temp.length > 2) && LavenderePdaConfig.usaFiltroGrupoProduto >= 3) {
						lbGrupoProduto1.setText(lbGrupoProduto1.getText() + " / " + temp[2]);
						cbGrupoProduto3.popupTitle = temp[2];
						if ((temp.length > 3) && LavenderePdaConfig.usaFiltroGrupoProduto >= 4) {
							lbGrupoProduto1.setText(lbGrupoProduto1.getText() + " / " + temp[3]);
							cbGrupoProduto4.popupTitle = temp[3];
						}
					}
				}
			}
			
		}
	}
	
	private boolean isAlgumFiltroEspecialSelecionado() {
		return !cbGrupoProduto1.isValueSelectedEmpty();
    }
	
	private boolean validateFiltro() {
		dsFiltro = edFiltro.getText();
		String filtro = dsFiltro;
		if (LavenderePdaConfig.usaPesquisaInicioString && dsFiltro.startsWith("*")) {
			filtro = dsFiltro.substring(1);
		}
		if (ValueUtil.isEmpty(filtro) && !isAlgumFiltroEspecialSelecionado()) {
			return true;
		}
		
    	if ((filtro == null) || ((filtro.length() < LavenderePdaConfig.qtMinimaCaracteresFiltroProduto) && !isAlgumFiltroEspecialSelecionado())) {
			return false;
		}
		return true;
	}
	
	private boolean filtrarLeitorCdBarrasClick() throws SQLException {
		return filtrarClick(null, true);
	}
	
	private boolean filtrarClick() throws SQLException {
		return filtrarClick(null, false);
	}
	
	protected boolean filtrarClick(final Object target, boolean usouLeitorCdBarras) throws SQLException {
		filtrarProduto(target, usouLeitorCdBarras);
		if (LavenderePdaConfig.limpaFiltroProdutoAutomaticamente) {
			edFiltro.setValue("");
		}
		edFiltro.requestFocus();
		return grid.size() == 1;
	}
	
	private void filtrarProduto(Object target, boolean usouLeitorCdBarras) throws SQLException {
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		try {
			dsFiltro = edFiltro.getText();
			if (validateFiltro()) {
				if (ValueUtil.isEmpty(edFiltro.getValue()) && !isAlgumFiltroEspecialSelecionado()) {
					refreshComponents();
				} else {
					boolean achou = false;
					Vector produtoList = getProdutoList();
					if (produtoList.size() > 0) {
						filtraItemPedidoConsignadoFiltradoPorProdutosEncontrados(produtoList);
						if (itemPedidoConsignadoList.size() > 0) {
							carregaLista();
							achou = true;
						}
					} 
					if(LavenderePdaConfig.usaIncrementoQuantidadePorLeituraCodigoBarras && usouLeitorCdBarras && achou) {
						updateQtdDevolvida(usouLeitorCdBarras);
						carregaLista();
					}
					if (!achou) {
						UiUtil.showWarnMessage(Messages.PRODUTO_MSG_NAO_ENCONTRADO);
						refreshComponents();
					}

				}
			} else if (!(target == cbGrupoProduto1)) { 
				UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.MSG_FILTRO_OBRIGATORIO_LIST_PRODUTO, LavenderePdaConfig.qtMinimaCaracteresFiltroProduto));
			}
		} finally {
			msg.unpop();
		}
		
	}
	
	private Vector getProdutoList() throws SQLException {
		ProdutoBase produto;
		if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
			produto = new ProdutoTabPreco();
		} else {
			produto = new Produto();
		}
		produto.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produto.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		produto.cdOrigemSetor = pedido.cdOrigemSetor;	
		Vector produtoList = ProdutoService.getInstance().findProdutos(dsFiltro, pedido.cdTabelaPreco, null, false, produto, false, false);
		if (LavenderePdaConfig.usaFiltroGrupoProduto != 0 && (ValueUtil.isNotEmpty(cbGrupoProduto1.getValue()))) {
			produtoList = ProdutoService.getInstance().findProdutosByGrupoProduto(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue(), cbGrupoProduto4.getValue(), produtoList);
		}
		return produtoList;
		
	}
	
	private void filtraItemPedidoConsignadoFiltradoPorProdutosEncontrados(Vector produtoList) throws SQLException {
		refreshComponents();
		for (int i = 0; i < itemPedidoConsignadoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoConsignadoList.items[i];
			boolean achou = false;
			for (int j = 0; j < produtoList.size(); j++) {
				ProdutoBase produto = (ProdutoBase)produtoList.items[j];
				if (ValueUtil.valueEquals(itemPedido.cdProduto, produto.cdProduto)) {
					achou = true;
					break;
				}
			}
			if (!achou) {
				itemPedidoConsignadoList.removeElementAt(i);
				i--;
			}
		}
		
	}
	
	private boolean realizaFiltroLeituraCdBarras() throws SQLException {
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getProdutoUnidadeByCdBarras((ItemPedido)itemPedidoConsignadoList.items[0], dsFiltro);
			if (produtoUnidade != null) {
				dsFiltro = produtoUnidade.cdProduto;
			}
		}
		edFiltro.setText(dsFiltro);
		if (filtrarLeitorCdBarrasClick()) {
			edFiltro.setText(""); 
			return LavenderePdaConfig.usaIncrementoQuantidadePorLeituraCodigoBarras;
		}
		edFiltro.setText("");
		return false;
	}
	
	private void realizaLeituraCamera() throws SQLException {
		StringBuffer dsInfo = new StringBuffer("");
		String[] registroDevolucao = grid.getItem(0);
		double qtDevolvida = ValueUtil.getDoubleValue(registroDevolucao[3]);
		if (LavenderePdaConfig.usaIncrementoQuantidadePorLeituraCodigoBarras && qtDevolvida > 0) {
			dsInfo.append(Messages.ITEMPEDIDO_LABEL_PRODUTO);
			dsInfo.append(" ");
			dsInfo.append(registroDevolucao[0]);
			dsInfo.append(" ");
			dsInfo.append(Messages.PEDIDO_CONSIGNACAO_QTD_INSERIDO);
			dsInfo.append(" ");
			dsInfo.append(registroDevolucao[1]);
			dsInfo.append(" ");
			dsInfo.append(Messages.PEDIDO_CONSIGNACAO_QTD_ITEM_DEVOLVIDO);
			dsInfo.append(" ");
			dsInfo.append(registroDevolucao[2]);
		}
		dsFiltro = ScannerCameraUtil.realizaLeitura(ScannerCameraUtil.MODO_SOMENTE_CODIGO_BARRAS, dsInfo.toString());
		if (ValueUtil.isNotEmpty(dsFiltro) && realizaFiltroLeituraCdBarras()) {
			insereItemDevolvido();
			realizaLeituraCamera();
			refreshComponents();
		}
	}
	private void insereItemDevolvido() {
		for (int i = 0; i < (grid.size()); i++) {
			String[] registroDevolucao = grid.getItem(i);
			double qtdDevolvida = ValueUtil.getDoubleValue(registroDevolucao[3]) + 1;
			registroDevolucao [3] = StringUtil.getStringValue(qtdDevolvida);
			grid.setItens(registroDevolucao, i);
		}
		updateQtdDevolvida(true);
		repaint();
	}
	
		
	private void limpaFiltros() {
		edFiltro.setValue("");
		cbGrupoProduto1.setSelectedIndex(0);
		cbGrupoProduto2.setSelectedIndex(0);
		cbGrupoProduto3.setSelectedIndex(0);
		cbGrupoProduto4.setSelectedIndex(0);
	}
	
	private void sugereImpressaoPedidoConsignacaoDevolucao(Vector pedidoConsignacaoDevolucaoList) throws SQLException {
		if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_SUGERE_IMPRESSAO_PEDIDOCONSIGNACAODEVOLUCAO)) {
			btImprimirDevolucaoClick(pedidoConsignacaoDevolucaoList);
		}
	}
	
	private void btImprimirDevolucaoClick(Vector pedidoConsignacaoDevolucaoList) throws SQLException {
		pedido.setPedidoConsignacaoDevolucaoList(pedidoConsignacaoDevolucaoList);
		PedidoConsignacaoManagerPrint pedidoConsignacaoManagerPrint = new PedidoConsignacaoManagerPrint(pedido);
		LoadingBoxWindow pb = UiUtil.createProcessingMessage();
		try {
			pb.makeUnmovable();
			pb.popupNonBlocking();
			pedidoConsignacaoManagerPrint.imprimePedidoConsignacaoDevolucao();
			if (LavenderePdaConfig.usaImpressaoComprovanteConsignacaoDevolucao == 2) {
				if (UiUtil.showConfirmYesNoMessage(Messages.IMPRESSAOCONSIGNACAO_DEVOLUCAO_MSG_CONFIRMACAO_COMPROVANTE)) {
					pedidoConsignacaoManagerPrint.imprimePedidoConsignacaoDevolucao(true);
				}
			}
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.IMPRESSAOCONSIGNACAO_DEVOLUCAO_MSG_ERRO, ex.getMessage()));
		} finally {
			pb.unpop();
		}
	}

}
