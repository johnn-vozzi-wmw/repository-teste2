package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.IAddId;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelTitle;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.VerbaGrupoSaldo;
import br.com.wmw.lavenderepda.business.service.GrupoProduto1Service;
import br.com.wmw.lavenderepda.business.service.VerbaGrupoSaldoService;
import totalcross.ui.Button;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.util.Vector;

public class RelVerbaGrupoSaldoWindow extends WmwWindow {

	private LabelValue lbVerbaDisponivel;
	private LabelValue lbVerbaPositiva;
	private LabelValue lbVerbaSaldoFinal;
	private ScrollTabbedContainer scrollTabbedContainer;
	private ButtonPopup btRecalcular;
	private Pedido pedido;
	private ItemPedido itemPedido;
	private boolean fromItem;
	private final int spacer = WIDTH_GAP * 5;
	private int INDEX_ITEM;
	private int INDEX_PEDIDO;

	public RelVerbaGrupoSaldoWindow(Pedido pedido) {
		this(pedido, null);
	}

	public RelVerbaGrupoSaldoWindow(Pedido pedido, ItemPedido itemPedido) {
		super(Messages.VERBAGRUPOSALDO_NOME_ENTIDADE);
		this.pedido = pedido;
		this.itemPedido = itemPedido;
		this.fromItem = itemPedido != null && itemPedido.cdProduto != null;
		String[] tabs;
		if (fromItem) {
			tabs = new String[]{Messages.LABEL_ITEM, Messages.LABEL_PEDIDO, Messages.LABEL_TODOS};
			INDEX_ITEM = 0;
			INDEX_PEDIDO = 1;
		} else {
			tabs = new String[]{Messages.LABEL_PEDIDO, Messages.LABEL_TODOS};
			INDEX_PEDIDO = 0;
		}
		scrollTabbedContainer = new ScrollTabbedContainer(tabs);
		scrollTabbedContainer.setID("scrollTabbedContainer");
		setDefaultRect();
	}

	private double addMain(Container container, double vlVerbaPedido, double vlVerbaPositiva, double vlSaldoTotal, Vector verbaGrupoSaldoList) throws SQLException {
		if (ValueUtil.isNotEmpty(verbaGrupoSaldoList)) {
			for (int i = 0; i < verbaGrupoSaldoList.size(); i++) {
				VerbaGrupoSaldo verbaGrupoSaldo = (VerbaGrupoSaldo) verbaGrupoSaldoList.items[i];
				double vlSaldo = VerbaGrupoSaldoService.getInstance().getVlSaldo(verbaGrupoSaldo.cdEmpresa, verbaGrupoSaldo.cdRepresentante, verbaGrupoSaldo.cdGrupoProduto1);
				LabelTitle lbTituloGrupoProduto = new LabelTitle(GrupoProduto1Service.getInstance().getDsGrupoProduto(verbaGrupoSaldo.cdGrupoProduto1));
				lbTituloGrupoProduto.align = CENTER;
				lbTituloGrupoProduto.split(this.width - WIDTH_GAP_BIG * 2);
				UiUtil.add(container, lbTituloGrupoProduto, CENTER, getNextY() + HEIGHT_GAP);
				UiUtil.add(container, new LabelName(Messages.VERBASALDO_LABEL_VLVERBADISPONIVEL) , BaseUIForm.CENTEREDLABEL, getNextY() + HEIGHT_GAP);
				lbVerbaDisponivel = new LabelValue();
				addIDComposed(container, lbVerbaDisponivel, i, "lbVerbaDisponivel");
				UiUtil.add(container, lbVerbaDisponivel, AFTER + spacer, SAME);
				if (pedido.isSimulaControleVerba()) {
					lbVerbaDisponivel.setValue(Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(vlSaldo));
				} else {
					double vlVerbaItemPedidoGrupo = 0;
					double vlVerbaItemPositivoPedidoGrupo = 0;
					for (int j = 0; j < pedido.itemPedidoList.size(); j++) {
						ItemPedido itemPedidoGrupo = (ItemPedido) pedido.itemPedidoList.items[j];
						if (ValueUtil.valueEquals(verbaGrupoSaldo.cdGrupoProduto1, itemPedidoGrupo.getProduto().cdGrupoProduto1)) {
							vlVerbaItemPedidoGrupo += itemPedidoGrupo.vlVerbaItem;
							vlVerbaItemPositivoPedidoGrupo += itemPedidoGrupo.vlVerbaItemPositivo;
						}
					}
					double vlVerbaDisponivel = vlSaldo - vlVerbaItemPedidoGrupo;
					lbVerbaDisponivel.setValue(Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(vlVerbaDisponivel - (LavenderePdaConfig.isMostraFlexPositivoPedido() && LavenderePdaConfig.geraVerbaPositiva ? vlVerbaItemPositivoPedidoGrupo : 0)));
					addUpper(container, vlVerbaItemPedidoGrupo, vlVerbaItemPositivoPedidoGrupo , i);
					vlSaldoTotal += addBottom(container, vlVerbaPedido, vlVerbaPositiva, vlSaldo, verbaGrupoSaldo);
				}
			}
		}
		return vlSaldoTotal;
	}

	private double addBottom(Container container, double vlVerbaPedido, double vlVerbaPositiva, double saldoFinal, VerbaGrupoSaldo verbaGrupoSaldo) throws SQLException {
		if (pedido.isSimulaControleVerba()) {
			saldoFinal += vlVerbaPedido + (LavenderePdaConfig.isMostraFlexPositivoPedido() && LavenderePdaConfig.geraVerbaPositiva ? vlVerbaPositiva : 0);
		}
		lbVerbaSaldoFinal.setValue(Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(saldoFinal));
		if (saldoFinal > 0) {
			lbVerbaSaldoFinal.setForeColor(ColorUtil.softGreen);
		} else {
			lbVerbaSaldoFinal.setForeColor(ColorUtil.softRed);
		}
		if (LavenderePdaConfig.isMostraFlexPositivoPedido() && !LavenderePdaConfig.geraVerbaPositiva) {
			//--
			UiUtil.add(container, new LabelName(Messages.VERBASALDO_LABEL_VLVERBAPEDIDOPOSITIVO), BaseUIForm.CENTEREDLABEL, AFTER + (HEIGHT_GAP * 2));
			lbVerbaPositiva = new LabelValue();
			UiUtil.add(container, lbVerbaPositiva, AFTER + spacer, SAME);
			double vlVerbaPositivaAtual;
			if (verbaGrupoSaldo == null) {
				vlVerbaPositivaAtual = vlVerbaPositiva;
			} else {
				vlVerbaPositivaAtual = VerbaGrupoSaldoService.getInstance().getVlVerbaPositivaByGrupoFromItemPedidoList(pedido.itemPedidoList, verbaGrupoSaldo.cdGrupoProduto1);
			}
			lbVerbaPositiva.setValue(Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(vlVerbaPositivaAtual));
			Button sep3 = new Button("");
			UiUtil.add(container, sep3, LEFT, AFTER + (HEIGHT_GAP * 10), FILL, 1);
			sep3.setForeColor(Color.getRGB(125, 125, 125));
		}
		return saldoFinal;
	}

	private void addTotalGeral(int index, Container container, double vlSaldoTotal, double vlVerbaPedido, double vlVerbaPositiva, Vector verbaGrupoSaldoList) throws SQLException {
		if (ValueUtil.isNotEmpty(verbaGrupoSaldoList) && !isRelatorioItem(index)) {
			UiUtil.add(container, new LabelTitle(Messages.VERBAGRUPOSALDO_LABEL_TOTAL_GERAL), CENTER, AFTER + HEIGHT_GAP);
			UiUtil.add(container, new LabelName(Messages.VERBASALDO_LABEL_VLVERBADISPONIVEL), BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
			lbVerbaDisponivel = new LabelValue();
			addIDComposed(container, lbVerbaDisponivel, index, "lbVerbaDisponivel");
			UiUtil.add(container, lbVerbaDisponivel, AFTER + spacer, SAME);
			if (pedido.isSimulaControleVerba()) {
				lbVerbaDisponivel.setValue(Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(vlSaldoTotal));
			} else {
				lbVerbaDisponivel.setValue(Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(vlSaldoTotal - vlVerbaPedido - (LavenderePdaConfig.isMostraFlexPositivoPedido() && LavenderePdaConfig.geraVerbaPositiva ? vlVerbaPositiva : 0)));
			}
			addUpper(container, vlVerbaPedido, vlVerbaPositiva, index);
			addBottom(container, vlVerbaPedido, vlVerbaPositiva, vlSaldoTotal, null);
		} else if (ValueUtil.isEmpty(verbaGrupoSaldoList)) {
			UiUtil.add(container, Messages.VERBAGRUPOSALDO_LABEL_NENHUM_SALDO_DISPONIVEL, CENTER, CENTER);
		}
	}

	private void addUpper(Container container, double vlVerbaPedido, double vlVerbaPositiva, int index) throws SQLException {
		UiUtil.add(container, new LabelName(Messages.VERBASALDO_LABEL_VLVERBAPEDIDO), BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
		LabelValue lbVerbaConsumida = new LabelValue();
		addIDComposed(container, lbVerbaConsumida, index, "lbVerbaConsumida");
		UiUtil.add(container, lbVerbaConsumida, AFTER + spacer, SAME);
		lbVerbaConsumida.setValue(Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(vlVerbaPedido));
		if (LavenderePdaConfig.isMostraFlexPositivoPedido() && LavenderePdaConfig.geraVerbaPositiva) {
			UiUtil.add(container, new LabelName(Messages.VERBASALDO_LABEL_VLVERBAGERADOPEDIDO), BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
			lbVerbaPositiva = new LabelValue();
			addIDComposed(container, lbVerbaPositiva, index, "lbVerbaPositiva");
			UiUtil.add(container, lbVerbaPositiva, AFTER + spacer, SAME);
			lbVerbaPositiva.setValue(Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(vlVerbaPositiva));
		}
		Button sep2 = new Button("");
		sep2.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
		int oneChar = fm.charWidth('A');
		UiUtil.add(container, sep2, SAME - WIDTH_GAP, AFTER + WIDTH_GAP, FILL - (oneChar * 4), 1);
		UiUtil.add(container, new LabelName(Messages.VERBASALDO_LABEL_VLSALDO), BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
		lbVerbaSaldoFinal = new LabelValue();
		addIDComposed(container, lbVerbaSaldoFinal, index, "lbVerbaSaldoFinal");
		UiUtil.add(container, lbVerbaSaldoFinal, AFTER + spacer, SAME);
	}

	@Override
	public void initUI() {
		super.initUI();
		try {
			UiUtil.add(this, scrollTabbedContainer, getLeft(), getTop(), FILL, FILL);
			btRecalcular = new ButtonPopup(Messages.BOTAO_RECALCULAR);
			addButtonPopup(btRecalcular);
			addButtonPopup(btFechar);
			int size = scrollTabbedContainer.getTabCount();
			for (int i = 0; i < size; i++) {
				Container container = scrollTabbedContainer.getContainer(i);
				double vlSaldoTotal = 0;
				double vlVerbaPedido = ValueUtil.round(pedido.vlVerbaPedido);
			double vlVerbaPositiva = ValueUtil.round(pedido.vlVerbaPedidoPositivo);
				Vector verbaGrupoSaldoList;
				if (isRelatorioItem(i)) {
					verbaGrupoSaldoList = getVerbaGrupoSaldoList(itemPedido.getProduto().cdGrupoProduto1);
				} else if (isRelatorioPedido(i)) {
					verbaGrupoSaldoList = getVerbaGrupoSaldoList(pedido.getCdGrupoProduto1Array());
				} else {
					verbaGrupoSaldoList = getVerbaGrupoSaldoList();
				}
				vlSaldoTotal = addMain(container, vlVerbaPedido, vlVerbaPositiva, vlSaldoTotal, verbaGrupoSaldoList);
				addTotalGeral(i, container, vlSaldoTotal, vlVerbaPedido, vlVerbaPositiva, verbaGrupoSaldoList);
			}
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
	}

	private boolean isRelatorioPedido(int i) {
		return i == INDEX_PEDIDO;
	}

	private boolean isRelatorioItem(int i) {
		return fromItem && i == INDEX_ITEM;
	}

	private Vector getVerbaGrupoSaldoList() throws SQLException {
		return getVerbaGrupoSaldoList(null, null);
	}

	private Vector getVerbaGrupoSaldoList(String cdGrupoProduto1) throws SQLException {
		return getVerbaGrupoSaldoList(cdGrupoProduto1, null);
	}

	private Vector getVerbaGrupoSaldoList(String[] cdGrupoProduto1Array) throws SQLException {
		if (cdGrupoProduto1Array == null) {
			return new Vector();
		}
		return getVerbaGrupoSaldoList(null, cdGrupoProduto1Array);
	}

	private Vector getVerbaGrupoSaldoList(String cdGrupoProduto1, String[] cdGrupoProduto1Array) throws SQLException {
		Vector verbaGrupoSaldoList;
		if (LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
			verbaGrupoSaldoList = VerbaGrupoSaldoService.getInstance().findAllVerbaGrupoSaldoWeb(pedido.cdEmpresa, pedido.cdRepresentante, cdGrupoProduto1, cdGrupoProduto1Array);
		} else {
			verbaGrupoSaldoList = VerbaGrupoSaldoService.getInstance().findAllVerbaGrupoSaldoErp(pedido.cdEmpresa, pedido.cdRepresentante, cdGrupoProduto1, cdGrupoProduto1Array);
		}
		return verbaGrupoSaldoList;
	}

	//@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btRecalcular) {
					unpop();
					VerbaGrupoSaldoService.getInstance().recalculateAndUpdateVerbaGrupoSaldoPda();
					UiUtil.showSucessMessage(Messages.VERBASALDO_MSG_SALDO_VERBA_RECALCULADO);
					RelVerbaGrupoSaldoWindow relVerbaGrupoSaldoWindow = new RelVerbaGrupoSaldoWindow(pedido, itemPedido);
					relVerbaGrupoSaldoWindow.popup();
				}
				break;
			}
		}
	}

	private <T> T addIDComposed(Container container, Control target, Integer index, String id) {
		if (container instanceof IAddId && target instanceof IAddId) {
			return (T) ADD_ID.addIDComposed((IAddId) container, (IAddId) target, index, id);
		} else {
			return (T) target;
		}
	}

}
