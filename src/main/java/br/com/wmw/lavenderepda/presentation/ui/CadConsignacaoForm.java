package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.print.AbstractPrinter;
import br.com.wmw.framework.print.Dpp250Printer;
import br.com.wmw.framework.print.MPTPrinter;
import br.com.wmw.framework.print.MonoImage;
import br.com.wmw.framework.print.StarPrinter;
import br.com.wmw.framework.print.ZebraPrinter;
import br.com.wmw.framework.print.ZonerichPrinter;
import br.com.wmw.framework.util.BluetoothUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.Consignacao;
import br.com.wmw.lavenderepda.business.domain.FichaFinanceira;
import br.com.wmw.lavenderepda.business.domain.ItemConsignacao;
import br.com.wmw.lavenderepda.business.domain.Pagamento;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoTabPreco;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.ConsignacaoService;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import br.com.wmw.lavenderepda.business.service.FichaFinanceiraService;
import br.com.wmw.lavenderepda.business.service.ItemConsignacaoService;
import br.com.wmw.lavenderepda.business.service.PagamentoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import br.com.wmw.lavenderepda.presentation.ui.ext.PedidoUiUtil;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.io.File;
import totalcross.io.IOException;
import totalcross.io.device.bluetooth.RemoteDevice;
import totalcross.sys.InvalidNumberException;
import totalcross.sys.Vm;
import totalcross.ui.Edit;
import totalcross.ui.Grid;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.ui.font.Font;
import totalcross.ui.gfx.Color;
import totalcross.ui.gfx.Graphics;
import totalcross.ui.image.ImageException;
import totalcross.util.Vector;

public class CadConsignacaoForm extends BaseCrudCadForm {

    private BaseGridEdit grid;
	private SessionContainer containerDados;
	private LabelName lbTotalConsig;
	private LabelName lbTotalVenda;
	private LabelName lbDtConsig;
	private LabelName lbDtProximaVisita;
	private LabelValue lbVlTotalConsig;
	private LabelValue lbVlTotalVenda;
	private LabelValue lbVlDtConsig;
	private ButtonAction btGerarPedido;
	private ButtonAction btApontarSobra;
	private ButtonAction btImprimir;
	private EditDate edDtProxVisita;

	private final int GRID_POS_PRODUTO = 0;
	private final int GRID_POS_QTD_CONSIG = 1;
    private final int GRID_POS_VLTOTAL_CONSIG = 2;
    private final int GRID_POS_QTD_SOBRA = 3;
    private final int GRID_POS_QTD_ITEM_VENDA = 4;
    private final int GRID_POS_VLTOTAL_RECEBER = 5;
    private final int GRID_POS_VLITEM = 6;
    private final int GRID_POS_CDPRODUTO = 7;
    private final int GRID_LENGHT = 8;

	private String APP_OBJ_COL_QTD_CONSIG;
	private String APP_OBJ_COL_QTD_SOBRA;

	private int lastSelectedRow = -1;
	private double vlTotalConsignado = 0;
	private boolean exeptionTriggered = false;
	private boolean isCancelClick = false;
	private boolean forceFocusSobra = false;
	private boolean forceFocusConsig = false;
	private boolean inGeracaoPedido = false;
	private boolean isApontandoSobra = false;

	private final String CONSIGNACAO_MSG_CONFIRM_GERAR_PEDIDO = Messages.PEDIDO_MSG_CONFIRM_GERAR_PEDIDO;
	private final String CONSIGNACAO_MSG_VALIDACAO_NAO_POSSUI_ITENS_VALIDO = Messages.CONSIGNACAO_MSG_VALIDACAO_NAO_POSSUI_ITENS_VALIDO;
	private final String CONSIGNACAO_MSG_VALIDACAO_SOBRA_MAIOR_CONSIGNACAO = Messages.CONSIGNACAO_MSG_VALIDACAO_SOBRA_MAIOR_CONSIGNACAO;

	private AbstractPrinter printer;
	private String enderecoImpressora;
	private int portaImpressora;
	private ZebraPrinter zebraPrinter;
	private MPTPrinter mptPrinter;
	private Dpp250Printer dpp250Printer;
	private ZonerichPrinter zonerichPrinter;

    public CadConsignacaoForm() {
        super(Messages.CONSIGNACAO_NOME_ENTIDADE);
        //--
        lbDtProximaVisita = new LabelName(Messages.CONSIGNACAO_IMPRESSAO_PROXIMA_VISITA);
        lbDtProximaVisita.setFont(UiUtil.defaultFontSmall);
		lbTotalConsig = new LabelName(Messages.CONSIGNACAO_TOT_CONSIGNADO);
		lbTotalConsig.setFont(UiUtil.defaultFontSmall);
		lbVlTotalConsig = new LabelValue("9999999999999999");
		lbVlTotalConsig.setFont(UiUtil.defaultFontSmall);
		//--
		lbTotalVenda = new LabelName(Messages.CONSIGNACAO_TOT_VENDA);
		lbTotalVenda.setFont(UiUtil.defaultFontSmall);
		lbVlTotalVenda = new LabelValue("99999999999999999");
		lbVlTotalVenda.setFont(UiUtil.defaultFontSmall);
		//--
		lbDtConsig = new LabelName(Messages.DATA_LABEL_DATA);
		lbDtConsig.setFont(UiUtil.defaultFontSmall);
		lbVlDtConsig = new LabelValue("99/99/9999");
		lbVlDtConsig.setFont(UiUtil.defaultFontSmall);
		edDtProxVisita = new EditDate();
		edDtProxVisita.setFont(UiUtil.defaultFontSmall);
		//--
		containerDados = new SessionContainer();
		containerDados.setBackColor(ColorUtil.componentsBackColor);
		btGerarPedido = new ButtonAction(Messages.BOTAO_GERAR_PEDIDO, "images/add.png");
		btApontarSobra = new ButtonAction(Messages.CONSIGNACAO_APONTA_SOBRA, "images/reabrirpedido.png");
		btImprimir = new ButtonAction(Messages.BOTAO_IMPRESSAO, "images/impressao.png");
    }

    //@Override
    public String getEntityDescription() {
    	return title;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return ConsignacaoService.getInstance();
    }

    //@Override
    protected BaseDomain createDomain() throws SQLException {
        return new Consignacao();
    }

    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
        Consignacao consignacao = (Consignacao)getDomain();
        consignacao.vlTotalConsignado = vlTotalConsignado;
        consignacao.cdCliente = SessionLavenderePda.getCliente().cdCliente;
        consignacao.dtProximaVisita = edDtProxVisita.getValue();
        return consignacao;
    }

    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
    	Consignacao consignacao = (Consignacao)domain;
    	edDtProxVisita.setValue(consignacao.dtProximaVisita);
    }

    //@Override
    protected void clearScreen() throws java.sql.SQLException {
    	lbVlTotalConsig.setText("");
    	lbVlTotalVenda.setText("");
    }

    //@Override
    protected void onFormStart() throws SQLException {
		LabelContainer clienteContainer = new LabelContainer(SessionLavenderePda.getCliente().toString());
    	UiUtil.add(this, clienteContainer, LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
        //--
        int labelWidth = fm.stringWidth("999999999");
        UiUtil.add(this, containerDados, LEFT, BOTTOM - barBottomContainer.getHeight(), FILL, UiUtil.defaultFontSmall.fm.height + UiUtil.getControlPreferredHeight() + (HEIGHT_GAP * 3));
        UiUtil.add(containerDados, lbDtProximaVisita , LEFT + WIDTH_GAP, TOP + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
        UiUtil.add(containerDados, edDtProxVisita, AFTER + WIDTH_GAP_BIG, SAME);
        //--
        UiUtil.add(containerDados, lbVlTotalConsig , RIGHT - WIDTH_GAP, BOTTOM, labelWidth, PREFERRED);
        UiUtil.add(containerDados, lbTotalConsig, RIGHT - (labelWidth + WIDTH_GAP_BIG), SAME, PREFERRED, PREFERRED);
        //--
        UiUtil.add(containerDados, lbVlTotalVenda, RIGHT - WIDTH_GAP, SAME , labelWidth, PREFERRED);
        UiUtil.add(containerDados, lbTotalVenda, RIGHT - (labelWidth + WIDTH_GAP_BIG), SAME , PREFERRED, PREFERRED);
        //--
        UiUtil.add(containerDados, lbDtConsig , LEFT + WIDTH_GAP, SAME, PREFERRED , PREFERRED);
        UiUtil.add(containerDados, lbVlDtConsig, AFTER + WIDTH_GAP_BIG, SAME , labelWidth, PREFERRED);
        //--
        UiUtil.add(containerDados, lbDtConsig , LEFT + WIDTH_GAP, SAME, PREFERRED , PREFERRED);
        UiUtil.add(containerDados, lbVlDtConsig, AFTER + WIDTH_GAP_BIG, SAME , labelWidth, PREFERRED);
        //--
        UiUtil.add(barBottomContainer, btImprimir , 1);
        UiUtil.add(barBottomContainer, btGerarPedido , 5);
        UiUtil.add(barBottomContainer, btApontarSobra , 5);
    	//--
        GridColDefinition[] gridColDefiniton = {
    		new GridColDefinition(Messages.ITEMPEDIDO_LABEL_PRODUTO, -59, LEFT),
    		new GridColDefinition(Messages.CONSIGNACAO_QTD_CONSIGNADO, -20, LEFT),
    		new GridColDefinition(Messages.CONSIGNACAO_TOT_CONSIGNADO, -20, LEFT),
	        new GridColDefinition(Messages.CONSIGNACAO_QTDADE_SOBRA, 0, LEFT),
	        new GridColDefinition(Messages.CONSIGNACAO_QT_VENDA, 0, LEFT),
	        new GridColDefinition(Messages.CONSIGNACAO_TOT_RECEBER, 0, LEFT),
			new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
			new GridColDefinition(FrameworkMessages.CAMPO_ID , 0, LEFT),
        };
        //--
        grid = UiUtil.createGridEdit(gridColDefiniton, false);
        grid.useKeyPress2ScrollInGrid = false;
        grid.useZeroAsEmpty = false;
        UiUtil.add(this, grid, LEFT, clienteContainer.getY2(), FILL, FILL - barBottomContainer.getHeight() - containerDados.getHeight());
        gridSettings();
        carregaGridItemConsignacao();
    }

	private void gridSettings() {
		grid.setGridControllable();
        //App Obj para reconhecer de qual campo foi o focus out
        APP_OBJ_COL_QTD_CONSIG = StringUtil.getStringValue(GRID_POS_QTD_CONSIG);
        APP_OBJ_COL_QTD_SOBRA = StringUtil.getStringValue(GRID_POS_QTD_SOBRA);
        //--
        EditNumberFrac edQtConsig =  grid.setColumnEditableDouble(GRID_POS_QTD_CONSIG, true, 9);
        edQtConsig.autoSelect = true;
        edQtConsig.appObj = APP_OBJ_COL_QTD_CONSIG;

        EditNumberFrac edQtSobra = grid.setColumnEditableDouble(GRID_POS_QTD_SOBRA, true, 9);
        edQtSobra.autoSelect = true;
        edQtSobra.appObj = APP_OBJ_COL_QTD_SOBRA;
	}

    protected void visibleState() throws SQLException {
    	btExcluir.setVisible(false);
    	btGerarPedido.setVisible(false);
    	lbVlTotalVenda.setVisible(false);
    	lbTotalVenda.setVisible(false);
    	btApontarSobra.setVisible(isEditing());
    	btImprimir.setVisible(true);
    }

    private void carregaGridItemConsignacao() throws SQLException {
    	LoadingBoxWindow pb = UiUtil.createProcessingMessage();
		pb.popupNonBlocking();
		//--
		try {
			Consignacao consignacao = (Consignacao)getDomain();
			grid.clear();
			grid.gridController.clearColors();
			grid.gridController.clearDisables();
			grid.gridController.setColForeColor(LavendereColorUtil.baseForeColorSystem, GRID_POS_QTD_CONSIG);
			grid.gridController.setColForeColor(LavendereColorUtil.baseForeColorSystem, GRID_POS_QTD_SOBRA);
			//--
			Produto produto = new Produto();
			produto.cdEmpresa = SessionLavenderePda.cdEmpresa;
			produto.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;

			Vector produtoList = getProdutoList();
			int size = produtoList.size();
			if (size > 0) {
				String[][] gridItems;
				gridItems = new String[size][GRID_LENGHT];
				//--
				for (int i = 0; i < size; i++) {
					ItemConsignacao itemConsignacao = new ItemConsignacao();
					itemConsignacao.cdCliente = SessionLavenderePda.getCliente().cdCliente;
					itemConsignacao.cdEmpresa = SessionLavenderePda.cdEmpresa;
					itemConsignacao.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
					itemConsignacao.cdProduto = ((ProdutoBase)produtoList.items[i]).cdProduto;
					itemConsignacao.cdTabelaPreco = SessionLavenderePda.getCliente().cdTabelaPreco;
					itemConsignacao.isEditing = false;
					if (isEditing()) {
						itemConsignacao.cdConsignacao = consignacao.cdConsignacao;
						ItemConsignacao itemConsignacaoTmp = (ItemConsignacao) ItemConsignacaoService.getInstance().findByRowKey(itemConsignacao.getRowKey());
						if (itemConsignacaoTmp != null) {
							itemConsignacao = itemConsignacaoTmp;
							itemConsignacao.isEditing = true;
						}
					}
					gridItems[i] = getItemConsignacaoToGrid(itemConsignacao);
				}
				grid.setItems(gridItems);
				if (!LavenderePdaConfig.usaOrdenacaoPorCodigoListaProdutos) {
					grid.qsort(GRID_POS_PRODUTO);
				}
				toggleEditableFieldsInGrid();
				//--
				updateTotalizarores();
			}
			if (isEditing()) {
				lbVlDtConsig.setValue(consignacao.dtConsignacao);
			} else {
				lbVlDtConsig.setValue(DateUtil.getCurrentDate());
			}
		} finally {
			pb.unpop();
		}
    }

    private void updateTotalizarores() {
		vlTotalConsignado = calcTotalByColumn(GRID_POS_VLTOTAL_CONSIG);
		lbVlTotalConsig.setValue(vlTotalConsignado);
		//--
		double totalReceber = calcTotalByColumn(GRID_POS_VLTOTAL_RECEBER);
		lbVlTotalVenda.setValue(totalReceber);
    }

	protected String[] getItemConsignacaoToGrid(ItemConsignacao itemConsignacao) throws SQLException {
		ItemConsignacaoService.getInstance().findVlBaseItemTabelaPreco(itemConsignacao);
		//--
		double vlTotalConsig = itemConsignacao.qtItemConsignado * itemConsignacao.vlItem;
		double qtSobra = itemConsignacao.qtItemSobra;
		double qtTotVenda = itemConsignacao.qtItemConsignado - itemConsignacao.qtItemSobra;
		double vlTotReceber = (itemConsignacao.qtItemConsignado - itemConsignacao.qtItemSobra) * itemConsignacao.vlItem;
		//--
		String vlTtReceber = "";
		String qtTtVenda = "";
		if ((qtTotVenda != 0 || itemConsignacao.isEditing) && (qtSobra != 0 || itemConsignacao.qtItemConsignado != 0)) {
			qtTtVenda = StringUtil.getStringValueToInterface(qtTotVenda);
		}
		if ((vlTotReceber != 0 || itemConsignacao.isEditing) && (qtSobra != 0 || itemConsignacao.qtItemConsignado != 0)) {
			vlTtReceber = StringUtil.getStringValueToInterface(vlTotReceber);
		}

		String[] item = {
			ProdutoService.getInstance().getDsProduto(itemConsignacao.cdProduto),
			itemConsignacao.qtItemConsignado == 0 ? "" : StringUtil.getStringValueToInterface(itemConsignacao.qtItemConsignado),
			vlTotalConsig == 0 ? "" : StringUtil.getStringValueToInterface(vlTotalConsig),
			!itemConsignacao.isEditing ? "" : StringUtil.getStringValueToInterface(qtSobra),
			qtTtVenda,
			vlTtReceber,
			StringUtil.getStringValueToInterface(itemConsignacao.vlItem),
			itemConsignacao.cdProduto
		};
		//--
		return item;
	}

	private void setColEnable(int row , boolean enable) {
		grid.gridController.setRowColDisable(row , GRID_POS_QTD_SOBRA, !enable);
		grid.gridController.setRowColDisable(row , GRID_POS_QTD_CONSIG, enable);
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
		//--
		Vector list = ProdutoService.getInstance().findProdutos("", SessionLavenderePda.getCliente().cdTabelaPreco, null, false, produto, false,false);
		if (LavenderePdaConfig.usaOrdemNumericaColunaCodigoProduto && LavenderePdaConfig.usaOrdenacaoPorCodigoListaProdutos) {
   			SortUtil.qsortInt(list.items, 0, list.size() - 1, true);
   		}
		//--
		return list;
	}

    public void reposition() {
    	super.reposition();
    	if (isApontandoSobra) {
    		changeToAppointment();
    	} else {
    		changeToConsignation();
    	}
    }

	private void changeToConsignation() {
		grid.setColumnWidth(0, -59);
		grid.setColumnWidth(1, -20);
		grid.setColumnWidth(2, -20);
		for (int i = 3; i < 6; i++) {
			grid.setColumnWidth(i, 0);
		}
		changeColorInGrid();
		Grid.repaint();

	}

	private void changeToAppointment() {
		grid.setColumnWidth(0, -19);
		for (int i = 5; i > 0; i--) {
			grid.setColumnWidth(i, -16);
		}
		changeColorInGrid();
		Grid.repaint();
	}

	void changeColorInGrid() {
		int defaultCelColor = 0;
		int celEditableColor = 0;
		if (isApontandoSobra) {
			defaultCelColor = celEditableColor = Color.getRGB(110, 110, 110);
		} else {
			defaultCelColor = ColorUtil.componentsForeColor;
			celEditableColor = LavendereColorUtil.baseForeColorSystem;
		}
		grid.gridController.setColForeColor(defaultCelColor, GRID_POS_PRODUTO);
		grid.gridController.setColForeColor(celEditableColor, GRID_POS_QTD_CONSIG);
		grid.gridController.setColForeColor(defaultCelColor, GRID_POS_VLTOTAL_CONSIG);
	}

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
			case PenEvent.PEN_DOWN: {
				if ((event.target == grid) || forceFocusSobra || forceFocusConsig) {
		    		if (forceFocusSobra) {
		    			grid.setSelectedIndex(lastSelectedRow);
		    			grid.exibeControleGrid(lastSelectedRow, GRID_POS_QTD_SOBRA);
		    		} else if (forceFocusConsig) {
		    			grid.setSelectedIndex(lastSelectedRow);
		    			grid.exibeControleGrid(lastSelectedRow, GRID_POS_QTD_CONSIG);
		    		}
				}
				forceFocusSobra = false;
				forceFocusConsig = false;
				exeptionTriggered = false;
				break;
			}
			case ControlEvent.FOCUS_OUT: {
				Object obj = event.target;
				if ((obj instanceof Edit) && !(obj == edDtProxVisita.edData)) {
					Edit ed = (Edit) obj;
					if (ed.appObj.equals(APP_OBJ_COL_QTD_CONSIG)) {
						if (!exeptionTriggered) {
							lastSelectedRow = grid.getSelectedIndex();
							edConsigFocusOut();
							isCancelClick = false;
						}
					} else if (ed.appObj.equals(APP_OBJ_COL_QTD_SOBRA)) {
						if (!exeptionTriggered) {
							lastSelectedRow = grid.getSelectedIndex();
							edSobraFocusOut(event);
							isCancelClick = false;
						}
					}
				}
				break;
			}
			case ControlEvent.PRESSED : {
				if (event.target == btGerarPedido) {
					btGerarPedidoClick();
				} else if (event.target == btApontarSobra) {
					btApontarSobraClick();
				} else if (event.target == btImprimir) {
					btImprimirClick();
				}
				break;
			}
		}
    }

    private void btApontarSobraClick() throws SQLException {
		beforeSave();
		save();
		afterSave();
		//--
    	inGeracaoPedido = false;
    	isApontandoSobra = true;
    	//--
    	toggleVisible();
    	changeToAppointment();
    	toggleEditableFieldsInGrid();
    }

    private void btImprimirClick() throws SQLException {
    	LoadingBoxWindow pb = UiUtil.createProcessingMessage();
    	pb.popupNonBlocking();
    	try {
    		imprimirConsignacao();
    	} finally {
			pb.unpop();
		}
    }

	private void imprimirConsignacao() throws SQLException {
		Consignacao consignacao = (Consignacao)screenToDomain();
		if (ValueUtil.isEmpty(consignacao.dtProximaVisita)) {
			throw new ValidationException(Messages.CONSIGNACAO_MSG_IMPRESSAO_DTPROXIMAVISITA_VAZIA);
		}
		//salva antes de imprimir, pois usuario pode ter mexido na grid
		save();
		edit(getDomain());

		ItemConsignacao itemConsignacaoFilter = new ItemConsignacao();
		itemConsignacaoFilter.cdEmpresa = consignacao.cdEmpresa;
		itemConsignacaoFilter.cdRepresentante = consignacao.cdRepresentante;
		itemConsignacaoFilter.cdConsignacao = consignacao.cdConsignacao;
		itemConsignacaoFilter.cdCliente = consignacao.cdCliente;
		Vector itemConsignacaoList = ItemConsignacaoService.getInstance().findAllByExample(itemConsignacaoFilter);
		int size = itemConsignacaoList.size();
		if (size == 0) {
			throw new ValidationException(Messages.CONSIGNACAO_MSG_IMPRESSAO_IMPOSSIVEL);
		}
		try {
			MonoImage mi = geraImagemConsignacao(consignacao, itemConsignacaoList, size);
			imprimirConsignacao(mi);
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e.getMessage());
		}
	}

	private void imprimirConsignacao(MonoImage mi) throws Exception {
		if (VmUtil.isSimulador()) {
			FileUtil.deleteFile("C:/image.png");
			File f = FileUtil.criaFile("C:/image.png");
			mi.createPng(f);
			f.close();
			UiUtil.showSucessMessage("Imagem da impressão gerada em C:/image.png");
		} else {
			loadPrinter();
			print(mi);
			LoadingBoxWindow progressBox = UiUtil.createProcessingMessage();
			progressBox.popupNonBlocking();
			Vm.safeSleep(6000);
			progressBox.unpop();
		}
	}

	private MonoImage geraImagemConsignacao(Consignacao consignacao, Vector itemConsignacaoList, int size) throws ImageException, SQLException {
		BluetoothUtil.turOnBluetooth();
		int initialX = 5;
		int x = initialX;
		int y = 0;
		int spaceBetweenLines = 5;
		int spaceFinal = 45;
		Font bigFont = Font.getFont("TcFont", true, 23);
		Font smallFont = Font.getFont("TcFont", true, 18);

		int heigthImage = (bigFont.fm.height * (3 + size)) + (smallFont.fm.height * 15) + (spaceBetweenLines * 23);
		int widthImage = 400;

		MonoImage mi = new MonoImage(widthImage + initialX, heigthImage + spaceFinal);
		Graphics g = mi.getGraphics();
		g.backColor = Color.WHITE;
		g.fillRect(0, y, widthImage + initialX, heigthImage + spaceFinal);
		g.foreColor = Color.BLACK;
		g.drawRect(x, y, widthImage, heigthImage);
		//--
		String titulo = EmpresaService.getInstance().getEmpresaName(consignacao.cdEmpresa);
		y += spaceBetweenLines;
		x = initialX + ((widthImage / 2) - (bigFont.fm.stringWidth(titulo) / 2)) + 3;
		g.setFont(bigFont);
		g.drawText(titulo, x, y);
		//--
		Cliente cliente = ClienteService.getInstance().getCliente(consignacao.cdEmpresa, consignacao.cdRepresentante, consignacao.cdCliente);
		y += bigFont.fm.height + (spaceBetweenLines * 2);
		x = initialX + spaceBetweenLines;
		g.setFont(smallFont);
		g.drawText(Messages.CLIENTE_LABEL_NMFANTASIA_LISTA + ": " + StringUtil.getStringValue(cliente.nmFantasia), x, y);
		y += smallFont.fm.height;
		g.drawText(Messages.CLIENTE_LABEL_NMRAZAOSOCIAL + ": " + StringUtil.getStringValue(cliente.nmRazaoSocial), x, y);
		y += smallFont.fm.height;
		g.drawText(Messages.CLIENTE_LABEL_NUCNPJ + ": " + StringUtil.getStringValue(cliente.nuCnpj), x, y);
		y += smallFont.fm.height;
		g.drawText(Messages.DATA_LABEL_DATA + ": " + StringUtil.getStringValue(consignacao.dtConsignacao), x, y);
		//--
		g.setFont(bigFont);
		y += bigFont.fm.height + (spaceBetweenLines * 2);
		x = initialX + spaceBetweenLines;
		int widthCelula = (widthImage - (spaceBetweenLines * 2)) / 3;
		g.drawRect(x, y, widthCelula + 1, bigFont.fm.height);
		g.drawText("Quantidade", x + 2, y);
		x += widthCelula;
		g.drawRect(x, y, widthCelula + 1, bigFont.fm.height);
		g.drawText(Messages.PRODUTO_NOME_ENTIDADE, x + 2, y);
		x += widthCelula;
		g.drawRect(x, y, widthCelula + 1, bigFont.fm.height);
		g.drawText(Messages.CONSIGNACAO_IMPRESSAO_TOTAL, x + 2, y);
		//--
		y += bigFont.fm.height - 1;
		x = initialX + spaceBetweenLines;
		for (int i = 0; i < size; i++) {
			ItemConsignacao itemConsignacao = (ItemConsignacao)itemConsignacaoList.items[i];
			g.drawRect(x, y, widthCelula + 1, bigFont.fm.height);
			g.drawText(StringUtil.getStringValueToInterface(itemConsignacao.qtItemConsignado), x + 2, y);
			x += widthCelula;
			g.drawRect(x, y, widthCelula + 1, bigFont.fm.height);
			String dsProduto = ProdutoService.getInstance().getDsProduto(itemConsignacao.cdProduto);
			if ((bigFont.fm.stringWidth(dsProduto) > widthCelula) && (dsProduto.length() > 10)) {
				dsProduto = dsProduto.substring(0, 10) + ".";
			}
			g.drawText(dsProduto, x + 2, y);
			x += widthCelula;
			g.drawRect(x, y, widthCelula + 1, bigFont.fm.height);
			g.drawText(StringUtil.getStringValueToInterface(itemConsignacao.qtItemConsignado * itemConsignacao.vlItem), x + 2, y);
			y += bigFont.fm.height - 1;
			x = initialX + spaceBetweenLines;
		}
		//--
		g.drawRect(x, y, (widthCelula * 2) + 1, bigFont.fm.height);
		g.drawText(Messages.CONSIGNACAO_IMPRESSAO_TOTAL, x + 2, y);
		x += widthCelula * 2;
		g.drawRect(x, y, widthCelula + 1, bigFont.fm.height);
		g.drawText(StringUtil.getStringValueToInterface(consignacao.vlTotalConsignado), x + 2, y);
		//--
		g.setFont(smallFont);
		y += smallFont.fm.height + (spaceBetweenLines * 3);
		x = initialX + spaceBetweenLines;
		g.drawRect(x, y, (widthCelula * 2) + 1, smallFont.fm.height);
		g.drawText(Messages.CONSIGNACAO_IMPRESSAO_DEBITO_ANTERIOR, x + 2, y);
		x += widthCelula * 2;
		//--
		FichaFinanceira fichaFinanceira = FichaFinanceiraService.getInstance().getFichaFinanceira(cliente);
		fichaFinanceira.getTitulos();
		Pagamento pagamento = new Pagamento();
		pagamento.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		pagamento.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pagamento.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		double vlTotalPagamentos = PagamentoService.getInstance().sumByExample(pagamento, "VLPAGO");
		double vlTotalPedidosAberto = FichaFinanceiraService.getInstance().getValuesPedidos(SessionLavenderePda.getCliente());
		double vlTotalTitulosAberto = fichaFinanceira.vlTotalAberto;
		double vlTotalDebitoAnterior = (vlTotalTitulosAberto + vlTotalPedidosAberto) - vlTotalPagamentos;
		g.drawRect(x, y, widthCelula + 1, smallFont.fm.height);
		g.drawText(Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(vlTotalDebitoAnterior), x + 2, y);
		y += smallFont.fm.height - 1;
		x = initialX + spaceBetweenLines;
		g.drawRect(x, y, (widthCelula * 3) + 1, smallFont.fm.height * 2);
		y += smallFont.fm.height;
		g.drawText("Ass:", x + 2, y);
		//--
		y += smallFont.fm.height + (spaceBetweenLines * 2);
		x = initialX + spaceBetweenLines;
		g.drawText("Declaro a existência do débito acima informado", x, y);
		y += smallFont.fm.height;
		g.drawText("e que recebi os itens descritos em", x, y);
		y += smallFont.fm.height;
		g.drawText("consignação de venda.", x, y);
		// --
		y += smallFont.fm.height + (spaceBetweenLines * 5);
		String lineAss = "________________________";
		x = initialX + ((widthImage / 2) - (smallFont.fm.stringWidth(lineAss) / 2)) + 3;
		g.drawText(lineAss, x, y);
		y += smallFont.fm.height;
		String dsCliente = StringUtil.getStringValue(cliente.nmRazaoSocial);
		x = initialX + ((widthImage / 2) - (smallFont.fm.stringWidth(dsCliente) / 2)) + 3;
		g.drawText(dsCliente, x, y);
		//--
		y += smallFont.fm.height + (spaceBetweenLines * 5);
		x = initialX + ((widthImage / 2) - (smallFont.fm.stringWidth(lineAss) / 2)) + 3;
		g.drawText(lineAss, x, y);
		y += smallFont.fm.height;
		String dsRep = RepresentanteService.getInstance().getDescription(SessionLavenderePda.usuarioPdaRep.cdRepresentante);
		x = initialX + ((widthImage / 2) - (smallFont.fm.stringWidth(dsRep) / 2)) + 3;
		g.drawText(dsRep, x, y);
		//--
		y += smallFont.fm.height + (spaceBetweenLines * 2);
		String dtProximaVisita = Messages.CONSIGNACAO_IMPRESSAO_PROXIMA_VISITA + ": " + consignacao.dtProximaVisita;
		x = initialX + ((widthImage / 2) - (smallFont.fm.stringWidth(dtProximaVisita) / 2)) + 3;
		g.drawText(dtProximaVisita, x, y);
		return mi;
	}

	private void toggleVisible() {
		lbVlTotalConsig.setVisible(!isApontandoSobra);
    	lbTotalConsig.setVisible(!isApontandoSobra);
    	btApontarSobra.setVisible(!isApontandoSobra);
    	btImprimir.setVisible(!isApontandoSobra);
    	lbDtProximaVisita.setVisible(!isApontandoSobra);
    	edDtProxVisita.setVisible(!isApontandoSobra);
    	btGerarPedido.setVisible(isApontandoSobra);
    	lbVlTotalVenda.setVisible(isApontandoSobra);
    	lbTotalVenda.setVisible(isApontandoSobra);
	}

    private void toggleEditableFieldsInGrid() {
    	int size = grid.size();
    	for (int i = 0; i < size; i++) {
    		setColEnable(i, isApontandoSobra);
		}
    }

	private void btGerarPedidoClick() throws SQLException {
		if (UiUtil.showConfirmYesNoMessage(CONSIGNACAO_MSG_CONFIRM_GERAR_PEDIDO)) {
			inGeracaoPedido = true;
			Consignacao consignacao = (Consignacao) screenToDomain();
			Pedido pedido = new Pedido();
			try {
				pedido = validateItensConsignacaoBeforeMakePedido(consignacao);
			} catch (ValidationException ex) {
				if (ex.getMessage().indexOf(FrameworkMessages.MSG_VALIDACAO_VALOR_INVALIDO) != -1) {
					boolean result = UiUtil.showConfirmYesNoMessage(ex.getMessage());
					if (result) {
						consignacao.nuPedido = "-1";
						update(consignacao);
						//--
						close();
						return;
					} else {
						isApontandoSobra = true;
						inGeracaoPedido = false;
						return;
					}
				} else {
					throw ex;
				}
			}
			boolean enviaAutomaticoERP = LavenderePdaConfig.sugereEnvioAutomaticoPedido && UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_ENVIAR_PEDIDOS_AGORA);
			//--
			Vector pedidosList = new Vector();
			pedidosList.addElement(pedido);
			//--
			String fecharPedido = "";
			try {
				fecharPedido = PedidoService.getInstance().fecharPedidos(pedidosList, true, false);
			} catch (ValidationException e) {
				PedidoService.getInstance().delete(pedido);
				//--
				ConsignacaoService.getInstance().retiraNuPedido(consignacao);
				//--
				throw e;
			}
			//--
			if (ValueUtil.isEmpty(fecharPedido) && enviaAutomaticoERP) {
				pedido.hrFimEmissao = TimeUtil.getCurrentTimeHHMM();
				PedidoPdbxDao.getInstance().update(pedido);
				//--
				PedidoUiUtil.enviaPedido(false, true);
			}
			//--
			isApontandoSobra = false;
			inGeracaoPedido = false;
			close();
		}
	}

	private Pedido validateItensConsignacaoBeforeMakePedido(Consignacao consignacao) throws SQLException {
		insertOrUpdate(consignacao);
		if (ValueUtil.isEmpty(consignacao.itemConsignacaoList)) {
			throw new ValidationException(CONSIGNACAO_MSG_VALIDACAO_NAO_POSSUI_ITENS_VALIDO);
		}
		return ConsignacaoService.getInstance().createNewPedidoByConsignacao(SessionLavenderePda.getCliente(), consignacao);
	}

	private void edConsigFocusOut() {
		String qtConsig = grid.getCellText(grid.getSelectedIndex(), GRID_POS_QTD_CONSIG);
		double qtItemConsig = ValueUtil.getDoubleValue(qtConsig);
		double qtItemSobra = ValueUtil.getDoubleValue(grid.getCellText(grid.getSelectedIndex(), GRID_POS_QTD_SOBRA));
		//--
		//--
		if (qtItemSobra > qtItemConsig) {
			forceFocusConsig = true;
			throw new ValidationException(MessageUtil.getMessage(CONSIGNACAO_MSG_VALIDACAO_SOBRA_MAIOR_CONSIGNACAO, new String[] {StringUtil.getStringValueToInterface(qtItemSobra), StringUtil.getStringValueToInterface(qtItemConsig)}));
		} else if ((qtItemConsig <= 0) && !ValueUtil.isEmpty(qtConsig)) {
			forceFocusConsig = true;
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_VALOR_INVALIDO + StringUtil.getStringValueToInterface(qtItemConsig));
		}
		double vlItem = ValueUtil.getDoubleValue(grid.getCellText(grid.getSelectedIndex(), GRID_POS_VLITEM));
		double vlTotalItem = qtItemConsig * vlItem;
		double qtItemVenda = qtItemConsig - qtItemSobra;
		double vlTotalReceber = qtItemVenda * vlItem;
		//--
		String vlTotItem = vlTotalItem == 0 ? "" : StringUtil.getStringValueToInterface(vlTotalItem);
		grid.setCellText(grid.getSelectedIndex(), GRID_POS_VLTOTAL_CONSIG, vlTotItem);

		if (!ValueUtil.isEmpty(qtConsig)) {
			if (ValueUtil.isEmpty(grid.getCellText(grid.getSelectedIndex(), GRID_POS_QTD_SOBRA))) {
				grid.setCellText(grid.getSelectedIndex(), GRID_POS_QTD_SOBRA, "0");
			}
			edSobraFocusOut(new ControlEvent());
		} else {
			grid.setCellText(grid.getSelectedIndex(), GRID_POS_QTD_ITEM_VENDA, StringUtil.getStringValueToInterface(qtItemVenda));
			grid.setCellText(grid.getSelectedIndex(), GRID_POS_VLTOTAL_RECEBER, StringUtil.getStringValueToInterface(vlTotalReceber));
		}
		//--
		vlTotalConsignado = 0;
		vlTotalConsignado = calcTotalByColumn(GRID_POS_VLTOTAL_CONSIG);
		lbVlTotalConsig.setValue(vlTotalConsignado);
	}

	private void edSobraFocusOut(Event event) {
		double qtItemConsig = ValueUtil.getDoubleValue(grid.getCellText(grid.getSelectedIndex(), GRID_POS_QTD_CONSIG));
		double qtItemSobra = ValueUtil.getDoubleValue(grid.getCellText(grid.getSelectedIndex(), GRID_POS_QTD_SOBRA));
		//--
		if (qtItemSobra > qtItemConsig) {
			if (event != null) {
				event.consumed = true;
			}
			forceFocusSobra = true;
			throw new ValidationException(MessageUtil.getMessage(CONSIGNACAO_MSG_VALIDACAO_SOBRA_MAIOR_CONSIGNACAO, new String[] {StringUtil.getStringValueToInterface(qtItemSobra), StringUtil.getStringValueToInterface(qtItemConsig)}));
		}
		double qtItemVenda = qtItemConsig - qtItemSobra;
		double vlItem = ValueUtil.getDoubleValue(grid.getCellText(grid.getSelectedIndex(), GRID_POS_VLITEM));
		double vlTotalReceber = qtItemVenda * vlItem;
		//--
		if (!ValueUtil.isEmpty(grid.getCellText(grid.getSelectedIndex(), GRID_POS_QTD_SOBRA))) {
			grid.setCellText(grid.getSelectedIndex(), GRID_POS_QTD_ITEM_VENDA, StringUtil.getStringValueToInterface(qtItemVenda));
			grid.setCellText(grid.getSelectedIndex(), GRID_POS_VLTOTAL_RECEBER, StringUtil.getStringValueToInterface(vlTotalReceber));
		} else {
			grid.setCellText(grid.getSelectedIndex(), GRID_POS_QTD_ITEM_VENDA , "");
			grid.setCellText(grid.getSelectedIndex(), GRID_POS_VLTOTAL_RECEBER , "");
		}
		//--
		double totalReceber = calcTotalByColumn(GRID_POS_VLTOTAL_RECEBER);
		lbVlTotalVenda.setValue(totalReceber);
	}

    public void afterExceptionThrowedOnEvent(Throwable ex){
       try {
    	super.afterExceptionThrowedOnEvent(ex);
		if (ex instanceof ValidationException) {
			if (ex.getMessage().equals(CONSIGNACAO_MSG_VALIDACAO_NAO_POSSUI_ITENS_VALIDO)) {
				inGeracaoPedido = false;
			} else if (ex.getMessage().indexOf(FrameworkMessages.MSG_VALIDACAO_VALOR_INVALIDO) != -1) {
				exeptionTriggered = true;
			} else if (ex.getMessage().indexOf(CONSIGNACAO_MSG_VALIDACAO_SOBRA_MAIOR_CONSIGNACAO.substring(20, 20)) != -1) {
				exeptionTriggered = true;
				isCancelClick = true;
			}
		}
		} catch (Throwable ee) {ee.printStackTrace();}
    }

    protected void salvarClick() throws SQLException {
    	if (!isCancelClick) {
    		super.salvarClick();
    	}
    	isCancelClick = false;
    }

    protected void insert(BaseDomain domain) throws SQLException {
    	Consignacao consignacao = (Consignacao) domain;
    	consignacao.dtConsignacao = DateUtil.getCurrentDate();
    	super.insert(domain);
    }

	private double calcTotalByColumn(int nuColumn) {
		int size = grid.size();
		double vlTotColumn = 0;
		for (int i = 0; i < size; i++) {
			String vlColumn = grid.getCellText(i, nuColumn);
			vlTotColumn += ValueUtil.getDoubleValue(vlColumn);
		}
		return vlTotColumn;
	}

	protected void insertOrUpdate(BaseDomain domain) throws SQLException {
		Consignacao consignacao = (Consignacao) domain;
		consignacao.itemConsignacaoList = new Vector(0);
		Vector itemConsignacaoInsertList = new Vector(0);
		//--
		int size = grid.size();
		double vlTotalConsignacao = 0;
		for (int i = 0; i < size; i++) {
			double qtItemConsig = ValueUtil.getDoubleValue(grid.getCellText(i, GRID_POS_QTD_CONSIG));
			String qtItemSobra = grid.getCellText(i, GRID_POS_QTD_SOBRA);
			if (ValueUtil.isEmpty(qtItemSobra) && qtItemConsig != 0 && inGeracaoPedido) {
				String dsProduto = grid.getCellText(i, GRID_POS_PRODUTO);
				inGeracaoPedido = false;
				throw new ValidationException(MessageUtil.getMessage(Messages.CONSIGNACAO_MSG_VALIDACAO_VLSOBRA_INVALIDO , new String[] {dsProduto}));
			}
			//--
			double qtSobra = ValueUtil.getDoubleValue(grid.getCellText(i, GRID_POS_QTD_SOBRA));
			double qtItemVenda = qtItemConsig - qtSobra;
			//--
			if (qtItemConsig < qtSobra) {
				throw new ValidationException(MessageUtil.getMessage(CONSIGNACAO_MSG_VALIDACAO_SOBRA_MAIOR_CONSIGNACAO, new String[] {StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(qtItemSobra)), StringUtil.getStringValueToInterface(qtItemConsig)}));
			} else if (qtItemConsig != 0) {
				ItemConsignacao itemConsignacao = new ItemConsignacao();
				itemConsignacao.cdProduto = grid.getCellText(i, GRID_POS_CDPRODUTO);
				itemConsignacao.cdTabelaPreco = SessionLavenderePda.getCliente().cdTabelaPreco;
				itemConsignacao.qtItemSobra = qtSobra;
				itemConsignacao.qtItemConsignado = qtItemConsig;
				itemConsignacao.vlItem = ValueUtil.getDoubleValue(grid.getCellText(i, GRID_POS_VLITEM));
				itemConsignacao.qtItemVenda = qtItemVenda;
				vlTotalConsignacao += (itemConsignacao.vlItem * qtItemConsig);
				//--
				itemConsignacaoInsertList.addElement(itemConsignacao);
				if (qtItemVenda > 0) {
					consignacao.itemConsignacaoList.addElement(itemConsignacao);
				}
			}
		}
		size = itemConsignacaoInsertList.size();
		if (isEditing() || (size > 0) || !ValueUtil.isEmpty(consignacao.dtProximaVisita)) {
			consignacao.vlTotalConsignado = vlTotalConsignacao;
			super.insertOrUpdate(consignacao);
			//--
			ItemConsignacao itemConsignacaoFilter = new ItemConsignacao();
			itemConsignacaoFilter.cdEmpresa = consignacao.cdEmpresa;
			itemConsignacaoFilter.cdRepresentante = consignacao.cdRepresentante;
			itemConsignacaoFilter.cdCliente = consignacao.cdCliente;
			itemConsignacaoFilter.cdConsignacao = consignacao.cdConsignacao;
			ItemConsignacaoService.getInstance().deleteAllByExample(itemConsignacaoFilter);
			itemConsignacaoFilter = null;
			//--
			for (int i = 0; i < size; i++) {
				ItemConsignacao itemConsignacao = (ItemConsignacao)itemConsignacaoInsertList.items[i];
				itemConsignacao.cdEmpresa = consignacao.cdEmpresa;
				itemConsignacao.cdRepresentante = consignacao.cdRepresentante;
				itemConsignacao.cdCliente = consignacao.cdCliente;
				itemConsignacao.cdConsignacao = consignacao.cdConsignacao;
				//--
				ItemConsignacaoService.getInstance().insert(itemConsignacao);
			}
		}
	}

	public void onFormClose() throws SQLException {
		super.onFormClose();
	}
	
	private void loadPrinter() throws Exception {
		enderecoImpressora = ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.configEnderecoImpressora);
		portaImpressora = ValueUtil.getIntegerValue(ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.configPortaImpressora));
		
		if (ValueUtil.isEmpty(enderecoImpressora)) {
			RemoteDevice remoteDevice = BluetoothUtil.chooseRemoteDeviceBluetooth();
			if (remoteDevice != null) {
				enderecoImpressora = remoteDevice.getBluetoothAddress();
			}
		}
		if (ValueUtil.isNotEmpty(enderecoImpressora)) {
			if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 1) {
				printer = new StarPrinter(enderecoImpressora, portaImpressora);
				printer.setNuCopies(LavenderePdaConfig.qtCopiasImpressaoPedido);
			} else if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 2) {
				zebraPrinter = new ZebraPrinter(enderecoImpressora, portaImpressora);
			} else if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 3 || LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 6) {
				mptPrinter = MPTPrinter.getNewInstance(enderecoImpressora, portaImpressora);
				mptPrinter.setNuCopies(LavenderePdaConfig.qtCopiasImpressaoPedido);
			} else if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 4) {
				zonerichPrinter = new ZonerichPrinter(enderecoImpressora, portaImpressora);
				zonerichPrinter.setNuCopies(LavenderePdaConfig.qtCopiasImpressaoPedido);
			} else if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 5) {
				dpp250Printer = Dpp250Printer.getNewInstance(enderecoImpressora, portaImpressora);
				dpp250Printer.setNuCopies(LavenderePdaConfig.qtCopiasImpressaoPedido);
			}
		}
	}
	
	private void print(MonoImage mi) throws IOException, ImageException, InvalidNumberException {
		if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 2) {
			for (int i = 0; i < LavenderePdaConfig.qtCopiasImpressaoPedido; i++) {
				mi.printTo(zebraPrinter);
			}
		} else if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 3 || LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 6) {
			mptPrinter.printImg(mi);
		} else if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 4) {
			zonerichPrinter.printImg(mi);
		} else if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 5) {
			try {
				dpp250Printer.printImg(mi);
			} catch (IOException e) {
				tentaNovamenteImpressaoDpp250(mi);
			}
		}
	} 
	
	private void tentaNovamenteImpressaoDpp250(MonoImage mi) throws InvalidNumberException, ImageException {
		try {
			dpp250Printer = Dpp250Printer.getNewInstance(enderecoImpressora, portaImpressora);
			dpp250Printer.setNuCopies(LavenderePdaConfig.qtCopiasImpressaoPedido);
			dpp250Printer.printImg(mi);
		} catch (IOException e) {
			UiUtil.showErrorMessage(Messages.PRINTER_DPP250_CONEXAO_ERRO);
		}
	}
	
}