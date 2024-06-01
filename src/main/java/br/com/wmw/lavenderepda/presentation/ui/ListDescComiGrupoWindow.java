package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescComiFaixa;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.DescComiFaixaService;
import br.com.wmw.lavenderepda.business.service.GrupoProduto1Service;
import br.com.wmw.lavenderepda.business.service.GrupoProduto2Service;
import br.com.wmw.lavenderepda.business.service.GrupoProduto3Service;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.util.Vector;

public class ListDescComiGrupoWindow extends WmwWindow {

	private Pedido pedido;
	private ItemPedido itemPedido;
	private BaseGridEdit grid;
	private String labelGrupoProduto1;
	private String labelGrupoProduto2;
	private String labelGrupoProduto3;
	private LabelName lbNuItensGrupo;
	private Vector domainList;
	public LabelValue lvNuItensGrupo;
	public DescComiFaixa descComiGrupo;
	public boolean selecionouItemGrid;
	public boolean inModeSelection = true;
	public int gridSize;
	public int nivelRelatorio;

    public ListDescComiGrupoWindow(Pedido pedido, ItemPedido itemPedido) throws SQLException {
    	super(Messages.DESCCOMIGRUPO_TITULO_CADASTRO);
    	nivelRelatorio = ValueUtil.isNotEmpty(itemPedido.getProduto().cdGrupoProduto3) ? 3 : ValueUtil.isNotEmpty(itemPedido.getProduto().cdGrupoProduto2) ? 2 : 1;
    	this.pedido = pedido;
        this.itemPedido = itemPedido;
        lbNuItensGrupo = new LabelName(Messages.ITEMPEDIDO_LABEL_QTITENS);
        lvNuItensGrupo = new LabelValue("@");
        loadLabelGruposProdutos();
        loadDomainList();
		//--
		makeUnmovable();
		setRect(8, 8);
    }

    private void loadDomainList() throws SQLException {
    	if (ValueUtil.isEmpty(itemPedido.getProduto().cdGrupoProduto1)) {
    		domainList = new Vector(0);
    	} else {
    		domainList = DescComiFaixaService.getInstance().findAllByGrupo(pedido.cdCliente, SessionLavenderePda.usuarioPdaRep.cdRepresentante, pedido.cdCondicaoPagamento, pedido.getCliente().cdRamoAtividade, pedido.getCliente().cdCidadeComercial, itemPedido.getProduto().cdGrupoProduto1, itemPedido.getProduto().cdGrupoProduto2, itemPedido.getProduto().cdGrupoProduto3);
    	}
    }

	public void list() throws java.sql.SQLException {
		int listSize = 0;
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		try {
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
			}
			gridSize = grid.size();
			domainList = null;
		} finally {
			msg.unpop();
		}
	}

	public void clearGrid() {
		grid.clear();
		grid.setSelectedIndex(-1);
	}

    //@Override
    protected String[] getItem(Object domain) throws java.sql.SQLException {
        DescComiFaixa desccomigrupo = (DescComiFaixa) domain;
        String[] item = {
                StringUtil.getStringValue(desccomigrupo.rowKey),
                StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(desccomigrupo.qtItem) : desccomigrupo.qtItem),
                StringUtil.getStringValueToInterface(desccomigrupo.vlPctDesconto),
                StringUtil.getStringValueToInterface(desccomigrupo.vlPctComissao)};
        return item;
    }

    //@Override
    protected String getSelectedRowKey() {
        String[] item = grid.getSelectedItem();
        return item[0];
    }

    //@Override
    protected DescComiFaixa getSelectedDomainOnGrid() {
    	DescComiFaixa desccomigrupo = new DescComiFaixa();
        String[] item = grid.getSelectedItem();
        desccomigrupo.rowKey = StringUtil.getStringValue(item[0]);
        desccomigrupo.qtItem = ValueUtil.getIntegerValue(ValueUtil.removeThousandSeparator(item[1]));
        desccomigrupo.vlPctDesconto = ValueUtil.getDoubleValue(item[2]);
        desccomigrupo.vlPctComissao = ValueUtil.getDoubleValue(item[3]);
        return desccomigrupo;
    }

	//@Override
	public void initUI() {
	   try {
		super.initUI();
		if (nivelRelatorio >= DescComiFaixa.GRUPOPRODUTO_NIVEL_1) {
			UiUtil.add(this, new LabelName(labelGrupoProduto1), new LabelValue(GrupoProduto1Service.getInstance().getDsGrupoProduto(itemPedido.getProduto().cdGrupoProduto1)), getLeft(), TOP + HEIGHT_GAP, PREFERRED);
		}
		if (nivelRelatorio >= DescComiFaixa.GRUPOPRODUTO_NIVEL_2) {
			UiUtil.add(this, new LabelName(labelGrupoProduto2), new LabelValue(GrupoProduto2Service.getInstance().getDsGrupoProduto(itemPedido.getProduto())), getLeft(), AFTER + HEIGHT_GAP, PREFERRED);
		}
		if (nivelRelatorio >= DescComiFaixa.GRUPOPRODUTO_NIVEL_3) {
			UiUtil.add(this, new LabelName(labelGrupoProduto3), new LabelValue(GrupoProduto3Service.getInstance().getDsGrupoProduto(itemPedido.getProduto())), getLeft(), AFTER + HEIGHT_GAP, PREFERRED);
		}
		//--
		UiUtil.add(this, lbNuItensGrupo, lvNuItensGrupo, getLeft(), AFTER + HEIGHT_GAP);
		//--
		GridColDefinition[] gridColDefiniton = {
            new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
            new GridColDefinition(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO, -25, CENTER),
            new GridColDefinition(Messages.DESCONTO_LABEL_VLPCTDESCONTO, -40, CENTER),
            new GridColDefinition(Messages.DESCCOMI_LABEL_VLPCTCOMISSAO, -35, CENTER)};
        grid = UiUtil.createGridEdit(gridColDefiniton);
        try {
        	UiUtil.add(this, grid, LEFT, AFTER + (HEIGHT_GAP * 2), FILL, FILL);
        	if (grid.getHeight() <= UiUtil.getControlPreferredHeight() * 5) {
        		grid.resetSetPositions();
        		UiUtil.add(this, grid, LEFT, AFTER + (HEIGHT_GAP * 2), FILL, UiUtil.getControlPreferredHeight() * 5, lvNuItensGrupo);
        	}
        } catch (Throwable ex) {
        	remove(grid);
        	grid = UiUtil.createGridEdit(gridColDefiniton);
        	UiUtil.add(this, grid, LEFT, AFTER + (HEIGHT_GAP * 2), FILL, UiUtil.getControlPreferredHeight() * 5, lvNuItensGrupo);
        }
        //--
		list();
		selectDescAtualNaGrid();
		} catch (Throwable ee) {ee.printStackTrace();}
	}

	private void selectDescAtualNaGrid() {
		if (itemPedido.descComissaoGrupo != null) {
			int size = grid.size();
			for (int i = 0; i < size; i++) {
				String cellText = grid.getCellText(i, 0);
				if (itemPedido.descComissaoGrupo.rowKey.equals(cellText)) {
					grid.setSelectedIndex(i);
					descComiGrupo = itemPedido.descComissaoGrupo;
					break;
				}
			}
		}
	}

	//@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
	   try {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btFechar) {
					fecharWindow();
				}
				break;
			}
			case PenEvent.PEN_UP: {
				if (event.target instanceof BaseGridEdit) {
					if (inModeSelection && grid.dispararAcaoClickUnico && (grid.getSelectedIndex() != -1)) {
						grid.dispararAcaoClickUnico = false;
						selecionouItemGrid = true;
						descComiGrupo = getSelectedDomainOnGrid();
						unpop();
					}
				}
				break;
			}
		}
		} catch (Throwable ee) {ee.printStackTrace();}
	}

	public void popup() {
		if (gridSize > 0) {
			if (!inModeSelection) {
				lbNuItensGrupo.setVisible(false);
				lvNuItensGrupo.setVisible(false);
			}
			super.popup();
		}
	}

	private void loadLabelGruposProdutos() {
		labelGrupoProduto1 = GrupoProduto1Service.getInstance().getLabelGrupoProduto1();
		labelGrupoProduto2 = GrupoProduto2Service.getInstance().getLabelGrupoProduto2();
		labelGrupoProduto3 = GrupoProduto3Service.getInstance().getLabelGrupoProduto3();
	}

	protected void fecharWindow() throws SQLException {
		if ((descComiGrupo != null) || !inModeSelection) {
			selecionouItemGrid = false;
			unpop();
		} else {
			UiUtil.showWarnMessage(Messages.DESCCOMI_OBRIGATORIO_SEM_DESC);
		}
	}

}
