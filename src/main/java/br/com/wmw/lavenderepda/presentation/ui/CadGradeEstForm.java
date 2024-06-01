package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ContratoCliente;
import br.com.wmw.lavenderepda.business.domain.ContratoProdEst;
import br.com.wmw.lavenderepda.business.domain.ContratoProduto;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ContratoClienteService;
import br.com.wmw.lavenderepda.business.service.ContratoProdEstService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import br.com.wmw.lavenderepda.presentation.ui.ext.PedidoUiUtil;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.Edit;
import totalcross.ui.Grid;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.util.Vector;

public class CadGradeEstForm extends BaseCrudCadForm {

    private BaseGridEdit grid;
    private static final int GRID_POS_QT_CONTRATO = 1;
    private final int GRID_POS_ESTOQUE_ATUAL = 2;
    private final int GRID_POS_FL_TIPO_ALTERACAO = 3;
    private ContratoCliente contratoClienteVigente;
    private ButtonAction btEmitirPedido;
    private Vector contratoProdEstList;
	private boolean exeptionTriggered = false;
	private boolean forceFocusSobra = false;
	private int lastSelectedRow = -1;

    public CadGradeEstForm(ContratoCliente contratoClienteVigente) {
        super(Messages.FACEAMENTOESTOQUE_TITULO_CADASTRO);
        //--
        this.contratoClienteVigente = contratoClienteVigente;
        btEmitirPedido = new ButtonAction(Messages.BOTAO_GERAR_PEDIDO, "images/add.png");
    }

    //@Override
    public String getEntityDescription() {
    	return Messages.FACEAMENTOESTOQUE_NOME_ENTIDADE;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return ContratoProdEstService.getInstance();
    }

    //@Override
    protected BaseDomain createDomain() throws SQLException {
        return new ContratoProdEst();
    }

    protected void visibleState() throws SQLException {
    	btExcluir.setVisible(false);
    }

    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
    	ContratoProdEst contratoProdEst = (ContratoProdEst) getDomain();
        return contratoProdEst;
    }

    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException { }

    //@Override
    protected void clearScreen() throws java.sql.SQLException { }

    //@Override
    protected void onFormStart() throws SQLException {
    	createGridContratoProdEst();
    	carregaGridContratoProdEst();
    }

    private void createGridContratoProdEst() {
    	LabelContainer clienteContainer = new LabelContainer(SessionLavenderePda.getCliente().toString());
    	UiUtil.add(this, clienteContainer, LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
    	//--
        GridColDefinition[] gridColDefiniton = {
    		new GridColDefinition(Messages.ITEMPEDIDO_LABEL_PRODUTO, -42, LEFT),
    		new GridColDefinition(Messages.FORECAST_QTCONTRATOPRODUTO, -29, LEFT),
    		new GridColDefinition(Messages.FACEAMENTOESTOQUE_LABEL_QTESTOQUEATUAL, -29, LEFT),
    		new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT)};
        grid = UiUtil.createGridEdit(gridColDefiniton, false);
        grid.useKeyPress2ScrollInGrid = false;
        grid.useZeroAsEmpty = false;
        UiUtil.add(this, grid);
        grid.setRect(LEFT, clienteContainer.getY2() + WIDTH_GAP, FILL, FILL - (barBottomContainer.getHeight() + HEIGHT_GAP));
        grid.setGridControllable();
	    grid.setColumnEditableInt(GRID_POS_ESTOQUE_ATUAL, true, 9);
        EditNumberFrac edQtEstoqueAtual =  grid.setColumnEditableDouble(GRID_POS_ESTOQUE_ATUAL, true, 9);
        edQtEstoqueAtual.autoSelect = true;
        edQtEstoqueAtual.appObj = StringUtil.getStringValue(GRID_POS_ESTOQUE_ATUAL);

        //--
        UiUtil.add(barBottomContainer, btEmitirPedido, 5);
    }

    private void carregaGridContratoProdEst() throws SQLException {
    	grid.clear();
    	grid.gridController.clearColors();
    	grid.gridController.clearDisables();
        grid.gridController.setColForeColor(LavendereColorUtil.baseForeColorSystem, GRID_POS_ESTOQUE_ATUAL);
        //--
        int size = contratoClienteVigente.contratoProdutoList.size();
        if (size > 0) {
        	for (int i = 0; i < size; i++) {
        		ContratoProduto contratoProduto = (ContratoProduto) contratoClienteVigente.contratoProdutoList.items[i];
        		//--
        		ContratoProdEst contratoProdEst = new ContratoProdEst();
        		contratoProdEst.cdEmpresa = contratoProduto.cdEmpresa;
        		contratoProdEst.cdRepresentante = contratoProduto.cdRepresentante;
        		contratoProdEst.cdCliente = contratoProduto.cdCliente;
        		contratoProdEst.cdProduto = contratoProduto.cdProduto;
        		contratoProdEst.flTipoContrato = contratoProduto.flTipoContrato;
        		contratoProdEst.dtVigenciaInicial = contratoProduto.dtVigenciaInicial;
        		contratoProdEst.dtCadastro = DateUtil.getCurrentDate();
        		contratoProdEst = (ContratoProdEst) ContratoProdEstService.getInstance().findByRowKey(contratoProdEst.getRowKey());
				//--
				String[] item = new String[4];
				//--
				item[0] = ProdutoService.getInstance().getDsProduto(contratoProduto.cdProduto);
				item[1] = StringUtil.getStringValueToInterface(contratoProduto.qtProdutoContrato);
				item[2] = (contratoProdEst != null) ? StringUtil.getStringValueToInterface(contratoProdEst.qtEstoqueAtual) : "";
				item[3] = (contratoProdEst != null) ? StringUtil.getStringValue(contratoProdEst.flTipoAlteracao) : "";
				//--
				grid.add(item);
			}
        }
    }

    public void reposition() {
    	super.reposition();
    	grid.setColumnWidth(1, -42);
    	grid.setColumnWidth(GRID_POS_QT_CONTRATO, -29);
    	grid.setColumnWidth(GRID_POS_ESTOQUE_ATUAL, -29);
    	grid.setColumnWidth(GRID_POS_FL_TIPO_ALTERACAO, 0);
		Grid.repaint();
    }

	protected void insertOrUpdate(BaseDomain domain) throws SQLException {
        int size = contratoClienteVigente.contratoProdutoList.size();
        contratoProdEstList = new Vector();
        if (size > 0) {
        	for (int i = 0; i < size; i++) {
        		ContratoProduto contratoProduto = (ContratoProduto) contratoClienteVigente.contratoProdutoList.items[i];
				//--
        		ContratoProdEst contratoProdEst = new ContratoProdEst();
        		contratoProdEst.cdEmpresa = contratoProduto.cdEmpresa;
        		contratoProdEst.cdRepresentante = contratoProduto.cdRepresentante;
        		contratoProdEst.cdCliente = contratoProduto.cdCliente;
        		contratoProdEst.cdProduto = contratoProduto.cdProduto;
        		contratoProdEst.flTipoContrato = contratoProduto.flTipoContrato;
        		contratoProdEst.dtVigenciaInicial = contratoProduto.dtVigenciaInicial;
        		contratoProdEst.dtCadastro = DateUtil.getCurrentDate();
        		contratoProdEst.qtEstoqueAtual = ValueUtil.getDoubleValue(grid.getCellText(i, GRID_POS_ESTOQUE_ATUAL));
        		contratoProdEst.qtEstAtual = grid.getCellText(i, GRID_POS_ESTOQUE_ATUAL);
        		String flTipoAlteracao = grid.getCellText(i, GRID_POS_FL_TIPO_ALTERACAO);
				//--
        		contratoProdEst.contratoProduto = contratoProduto;
				if (ValueUtil.isEmpty(flTipoAlteracao)) {
					insert(contratoProdEst);
				} else {
					update(contratoProdEst);
				}
				contratoProdEstList.addElement(contratoProdEst);
			}
        }
	}

	private void btGerarPedidoClick() throws SQLException {
		if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_CONFIRM_GERAR_PEDIDO)) {
			Pedido pedido;
			try {
				insertOrUpdate(getDomain());
				pedido = ContratoClienteService.getInstance().createNewPedidoByGrade(contratoClienteVigente, contratoProdEstList);
			} catch (ValidationException ex) {
				if (ex.getMessage().indexOf(FrameworkMessages.MSG_VALIDACAO_VALOR_INVALIDO) != -1) {
					boolean result = UiUtil.showConfirmYesNoMessage(ex.getMessage());
					if (result) {
						close();
					}
					return;
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
			close();
		}
	}

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btEmitirPedido) {
					btGerarPedidoClick();
				}
				break;
			}
			case PenEvent.PEN_DOWN: {
				if ((event.target == grid) || forceFocusSobra) {
		    		if (forceFocusSobra) {
		    			grid.setSelectedIndex(lastSelectedRow);
		    			grid.exibeControleGrid(lastSelectedRow, GRID_POS_ESTOQUE_ATUAL);
		    		}
				}
				forceFocusSobra = false;
				exeptionTriggered = false;
				break;
			}
			case ControlEvent.FOCUS_OUT: {
				Object obj = event.target;
				if (obj instanceof Edit) {
					Edit ed = (Edit) obj;
					if (ed.appObj.equals(StringUtil.getStringValue(GRID_POS_ESTOQUE_ATUAL))) {
						if (!exeptionTriggered) {
							lastSelectedRow = grid.getSelectedIndex();
							edSobraFocusOut(event);
						}
					}
				}
				break;
			}
		}
    }

	private void edSobraFocusOut(Event event) {
		double qtContrato = ValueUtil.getDoubleValue(grid.getCellText(grid.getSelectedIndex(), GRID_POS_QT_CONTRATO));
		double qtItemSobra = ValueUtil.getDoubleValue(grid.getCellText(grid.getSelectedIndex(), GRID_POS_ESTOQUE_ATUAL));
		//--
		if (qtItemSobra > qtContrato) {
			exeptionTriggered = true;
			int result = UiUtil.showConfirmYesCancelMessage(MessageUtil.getMessage(Messages.CONTRATOCLIENTE_MSG_VALIDACAO_SOBRA_MAIOR_CONTRATADO, new String[] {StringUtil.getStringValueToInterface(qtItemSobra), StringUtil.getStringValueToInterface(qtContrato)}));
			if (result != 1) {
				if (event != null) {
					event.consumed = true;
				}
				forceFocusSobra = true;
			}
		}
	}

}