package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.VerbaSaldoService;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.ui.gfx.Color;

public class RelPreviaDescontosComVerbaWindow extends WmwWindow {

	private GridListContainer listContainer;
	private Pedido pedido;
	private LabelName lbPctDescProg;
	private LabelValue lbPctDescProgValue;
	private LabelName lbVlVerbaConsumida;
	private LabelValue lbVlVerbaConsumidaValue;
	private LabelName lbVlVerbaGerada;
	private LabelValue lbVlVerbaGeradaValue;
	private LabelName lbVlSaldo;
	private LabelValue lbVlSaldoValue;
	private LabelName lbTotalPedido;
	private LabelValue lbTotalPedidoValue;
	private ButtonPopup btFecharPedido;
	private double vlTotVerbaConsumidaAposAplicarDescProg;
	private double vlTotVerbaGeradaAposAplicarDescProg;
	private String sortAtributte = null;
	private String sortAsc = ValueUtil.VALOR_SIM;
	private double vlSaldo = 0;
	private boolean inConsultaItensByCadItemPedido = false;
	public CadPedidoForm cadPedidoForm;

	public RelPreviaDescontosComVerbaWindow(Pedido pedido, boolean inConsultaItensByCadItemPedido) {
		super(Messages.PEDIDO_PREVISAODESCONTOS_RELATORIO);
		this.inConsultaItensByCadItemPedido = inConsultaItensByCadItemPedido;
		lbPctDescProg = new LabelName(Messages.PEDIDO_LABEL_PREVDESCPROGRESSIVO);
		lbPctDescProgValue = new LabelValue("999999999999");
		lbVlVerbaConsumida = new LabelName(Messages.PEDIDO_LABEL_VLTOTVERBA_CONSUMIDA);
		lbVlVerbaConsumidaValue = new LabelValue("999999999999");
		lbVlVerbaGerada = new LabelName(Messages.PEDIDO_LABEL_VLTOTVERBA_GERADA);
		lbVlVerbaGeradaValue = new LabelValue("999999999999");
		lbVlSaldo = new LabelName(Messages.PEDIDO_LABEL_VLVERBA_DISPONIVEL);
		lbVlSaldoValue = new LabelValue("999999999999");
		lbTotalPedido = new LabelName(Messages.RELPREVIADESCONTOSCOMVERBA_LABEL_VL_TOTAL_PED);
		lbTotalPedidoValue = new LabelValue("999999999999");
        this.pedido = pedido;
        btFecharPedido = new ButtonPopup(Messages.BOTAO_FECHAR_PEDIDO);
        btFecharPedido.setBackColor(btFechar.getBackColor());
		sortAtributte = "CDPRODUTO";
		sortAsc = ValueUtil.VALOR_SIM;
		listContainer = new GridListContainer(6 , 3 , false, false);
		listContainer.setColsSort(new String[][]{{Messages.PRODUTO_LABEL_CODIGO, "CDPRODUTO"}, {Messages.PRODUTO_LABEL_DSPRODUTO, "DSPRODUTO"}, {Messages.ITEM_PEDIDO_SEQ_INSERCAO, "NUSEQITEMPEDIDO"}});
		listContainer.setColPosition(3, LEFT);
		listContainer.setColPosition(4, CENTER);
		listContainer.setColPosition(5, RIGHT);
		makeUnmovable();
		setDefaultRect();
	}

	//@Override
	public void initUI() {
	   try {
		super.initUI();
		if (listContainer != null) {
			listContainer.atributteSortSelected = sortAtributte;
			listContainer.sortAsc = sortAsc;
		}
		if (!inConsultaItensByCadItemPedido) {
			addButtonPopup(btFecharPedido);
		}
		addButtonPopup(btFechar);
		addComponentsCentralizados();
		int qtLinhasTela = 3;
		if (!pedido.isPedidoBonificacao()) {
			qtLinhasTela++;
		}
		if (LavenderePdaConfig.isMostraFlexPositivoPedido()) {
			qtLinhasTela++;
		}
		UiUtil.add(this, listContainer, LEFT, TOP , FILL, FILL - (((HEIGHT_GAP * qtLinhasTela) - 1) + (UiUtil.getLabelPreferredHeight() * qtLinhasTela)));
		toggleDiscAndLoadGrid();
		} catch (Throwable ee) {ee.printStackTrace();}
	}

	private void addComponentsCentralizados() throws SQLException {
		UiUtil.add(this, lbTotalPedido, BaseUIForm.CENTEREDLABEL, BOTTOM);
		UiUtil.add(this, lbTotalPedidoValue, AFTER + WIDTH_GAP_BIG, SAME);
		if (!pedido.isPedidoBonificacao()) {
			UiUtil.add(this, lbPctDescProg, BaseUIForm.CENTEREDLABEL, BEFORE - HEIGHT_GAP);
			UiUtil.add(this, lbPctDescProgValue, AFTER + WIDTH_GAP_BIG, SAME);
		}
		UiUtil.add(this, lbVlSaldo, BaseUIForm.CENTEREDLABEL, BEFORE - HEIGHT_GAP);
		UiUtil.add(this, lbVlSaldoValue, AFTER + WIDTH_GAP_BIG, SAME);
		UiUtil.add(this, lbVlVerbaConsumida, BaseUIForm.CENTEREDLABEL, BEFORE - HEIGHT_GAP);
		UiUtil.add(this, lbVlVerbaConsumidaValue, AFTER + WIDTH_GAP_BIG, SAME);
		if (LavenderePdaConfig.isMostraFlexPositivoPedido()) {
			UiUtil.add(this, lbVlVerbaGerada, BaseUIForm.CENTEREDLABEL, BEFORE - HEIGHT_GAP);
			UiUtil.add(this, lbVlVerbaGeradaValue, AFTER + WIDTH_GAP_BIG, SAME);
		}
	}

	public void reposition() {
		super.reposition();
		try {
		addComponentsCentralizados();
		} catch (Throwable ee) {ee.printStackTrace();}
	}

	private void toggleDiscAndLoadGrid() throws SQLException {
		lbPctDescProgValue.setValue(pedido.getVlPctDescProgressivo());
		if (LavenderePdaConfig.permiteEditarDescontoProgressivo) {
			lbPctDescProgValue.setValue(pedido.vlPctDescProgressivo);
		}
    	PedidoService.getInstance().isAplicaDescontoProgressivoNoPedido(pedido);
		loadGridAndCalcVerba();
		PedidoService.getInstance().retiraDescontosDoFimDoPedidoConsumindoFlex(pedido);
	}

	private void loadGridAndCalcVerba() throws SQLException {
		vlTotVerbaConsumidaAposAplicarDescProg = 0;
		vlTotVerbaGeradaAposAplicarDescProg = 0;
		int listSize = 0;
		listContainer.removeAllContainers();
		//--
		listSize = pedido.itemPedidoList.size();
		Container[] all = new Container[listSize];
		//--
		vlSaldo = 0;
		if (listSize > 0) {
			//--
			vlSaldo = ValueUtil.round(VerbaSaldoService.getInstance().getVlSaldo(((ItemPedido) pedido.itemPedidoList.items[0]).cdContaCorrente, null));
			//--
			BaseDomain domain = getDomainFilterSortable();
			ItemPedido.sortAttr = domain.sortAtributte;
			pedido.itemPedidoList.qsort();
			//--
			BaseListContainer.Item c;
			if (domain.sortAsc == null || domain.sortAsc.startsWith(ValueUtil.VALOR_SIM)) {
				for (int i = 0; i < listSize; i++) {
					domain = (BaseDomain) pedido.itemPedidoList.items[i];
					all[i] = c = new BaseListContainer.Item(listContainer.getLayout());
					if ((i % 2) == 0) {
						c.setBackColor(Color.darker(c.getBackColor(), 10));
					}
					c.id = domain.getRowKey();
					c.setItens(getItem(domain));
					setPropertiesInRowList(c, domain);
					c.setToolTip(getToolTip(domain));
					domain = null;
				}
			} else {
				int pos = 0;
				for (int i = listSize - 1; i >= 0; i--, pos++) {
					domain = (BaseDomain) pedido.itemPedidoList.items[i];
					all[pos] = c = new BaseListContainer.Item(listContainer.getLayout());
					if ((pos % 2) == 0) {
						c.setBackColor(Color.darker(c.getBackColor(), 10));
					}
					c.id = domain.getRowKey();
					c.setItens(getItem(domain));
					setPropertiesInRowList(c, domain);
					c.setToolTip(getToolTip(domain));
					domain = null;
				}
			}
			listContainer.addContainers(all);
		} else {
			// Para consultar quando não tem itens no pedido
			vlSaldo  = VerbaSaldoService.getInstance().getVlSaldo("0", null);
		}
		lbVlVerbaConsumidaValue.setValue(StringUtil.getStringValuePositivo(vlTotVerbaConsumidaAposAplicarDescProg));
		lbVlVerbaGeradaValue.setValue(vlTotVerbaGeradaAposAplicarDescProg);
		if (vlSaldo > 0) {
			lbVlSaldoValue.setForeColor(ColorUtil.softGreen);
		} else {
			lbVlSaldoValue.setForeColor(ColorUtil.softRed);
		}
		lbVlSaldoValue.setValue(ValueUtil.round(vlSaldo));
		lbTotalPedidoValue.setValue(pedido.vlTotalPedido);
	}

	private BaseDomain getDomainFilterSortable() {
		BaseDomain baseDomain = getDomainFilter();
		baseDomain.sortAtributte = sortAtributte;
		baseDomain.sortAsc = sortAsc;
		return baseDomain;
	}

	private BaseDomain getDomainFilter() {
		return new ItemPedido();
	}

	private void setPropertiesInRowList(Container c, BaseDomain domain) {
		ItemPedido itemPedido = (ItemPedido) domain;
		if (itemPedido.vlVerbaItem < 0) {
			c.setBackColor(LavendereColorUtil.COR_PRODUTO_COM_VERBA_APOS_APLICAR_DESC_PROG);
		}
	}

    private String getToolTip(Object domain) throws SQLException {
    	return ((ItemPedido) domain).getDsProduto();
    }

	protected String[] getItem(Object domain) throws java.sql.SQLException {
        ItemPedido itemPedido = (ItemPedido) domain;
        String[] item;
        double vlBaseFlex = itemPedido.vlBaseItemPedido - ((itemPedido.vlBaseItemPedido * pedido.getVlPctDescProgressivo()) / 100);
        if (LavenderePdaConfig.isMostraFlexPositivoPedido() && itemPedido.vlVerbaItemPositivo != 0) {
        	//Quando o Flex é Positivo
        	vlTotVerbaGeradaAposAplicarDescProg += itemPedido.vlVerbaItemPositivo;
        	item = new String[]{
    			StringUtil.getStringValue(itemPedido.cdProduto),
    			" - ",
    			itemPedido.getDsProduto(),
    			Messages.PEDIDO_LABEL_VLBASE_PROGRESSIVO + " " + StringUtil.getStringValueToInterface(vlBaseFlex),
    			Messages.ITEMPEDIDO_LABEL_VLITEMPEDIDO_PREV + " " + StringUtil.getStringValueToInterface(itemPedido.vlItemPedido),
    			Messages.PEDIDO_LABEL_VLVERBA + " " + StringUtil.getStringValueToInterface(itemPedido.vlVerbaItemPositivo)};
        } else {
        	//Quando o Flex é Negativo
        	vlTotVerbaConsumidaAposAplicarDescProg += itemPedido.vlVerbaItem;
        	item = new String[] {
    			StringUtil.getStringValue(itemPedido.cdProduto),
    			" - ",
    			itemPedido.getDsProduto(),
    			Messages.PEDIDO_LABEL_VLBASE_PROGRESSIVO + " " + StringUtil.getStringValueToInterface(vlBaseFlex),
    			Messages.ITEMPEDIDO_LABEL_VLITEMPEDIDO_PREV  + " " + StringUtil.getStringValueToInterface(itemPedido.vlItemPedido),
    			Messages.PEDIDO_LABEL_VLVERBA + StringUtil.getStringValueToInterface(itemPedido.vlVerbaItem)};
        }
        return item;
    }

	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.WINDOW_CLOSED: {
				if ((listContainer != null) && (event.target == listContainer.popupMenuOrdenacao)) {
					if (listContainer.popupMenuOrdenacao.getSelectedIndex() != -1) {
						listContainer.reloadSortSettings();
						sortAtributte = listContainer.atributteSortSelected;
						sortAsc = StringUtil.getStringValue(listContainer.sortAsc);
						toggleDiscAndLoadGrid();
					}
				}
				break;
			}
			case ControlEvent.PRESSED : {
				if (event.target == btFecharPedido) {
					if (!inConsultaItensByCadItemPedido) {
						cadPedidoForm.inItemRenegotiation = false;
						if (ValueUtil.round(vlSaldo) < ValueUtil.round(vlTotVerbaConsumidaAposAplicarDescProg * -1)) {
							double vlVerba = (vlSaldo - ValueUtil.round(vlTotVerbaConsumidaAposAplicarDescProg)) * -1;
							vlVerba = vlVerba < 0 ? vlVerba *-1 : vlVerba;
							if (ValueUtil.round(vlVerba) > ValueUtil.round(SessionLavenderePda.usuarioPdaRep.representante.vlToleranciaVerba)) {
								throw new ValidationException(MessageUtil.getMessage(Messages.DESCONTO_PROGRESSIVO_MSG_SALDO_NEGATIVO, new String[]{StringUtil.getStringValueToInterface(vlSaldo + vlTotVerbaConsumidaAposAplicarDescProg)}));
							}
							boolean result = UiUtil.showConfirmYesNoMessage(MessageUtil.getMessage(Messages.DESCONTO_PROGRESSIVO_MSG_CONFIRM_SALDO_NEG , new String [] {StringUtil.getStringValueToInterface((vlSaldo + vlTotVerbaConsumidaAposAplicarDescProg) * -1)}));
							if (!result) {
								return;
							}
						}
						pedido.fecharPedidoComVerbaNeg = true;
						unpop();
					}
				}
				break;
			}
			case PenEvent.PEN_UP: {
				if ((event.target instanceof BaseListContainer.Item) && (listContainer.isEventoClickUnicoDisparado())) {
					if (!inConsultaItensByCadItemPedido) {
						PedidoService.getInstance().validateBateria();
						//--
						pedido.fecharPedidoComVerbaNeg = false;
						cadPedidoForm.inItemRenegotiation = true;
						BaseCrudCadForm cadItemPedido;
						if (pedido.isPedidoBonificacao()) {
							cadItemPedido = new CadItemPedidoBonificacaoForm(cadPedidoForm, pedido);
						} else {
							cadItemPedido = CadItemPedidoForm.getInstance(cadPedidoForm, pedido);
						}
						ItemPedido itemPedido = getSelectedItem();
						if (itemPedido != null) {
							cadItemPedido.edit(itemPedido);
							unpop();
							MainLavenderePda.getInstance().show(cadItemPedido);
						}
					}
				}
				break;
			}
		}
	}

	private ItemPedido getSelectedItem() throws SQLException {
		return (ItemPedido) ItemPedidoService.getInstance().findByRowKey(listContainer.getSelectedId());
	}

	protected void fecharWindow() throws SQLException {
		pedido.fecharPedidoComVerbaNeg = false;
		if (!inConsultaItensByCadItemPedido) {
			cadPedidoForm.inItemRenegotiation = false;
		}
		super.fecharWindow();
		ItemPedido.sortAttr = "NUSEQITEMPEDIDO";
	}

}