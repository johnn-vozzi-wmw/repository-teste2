package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.RentabilidadeFaixa;
import br.com.wmw.lavenderepda.business.domain.VerbaSaldo;
import br.com.wmw.lavenderepda.business.service.ComiRentabilidadeService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.RentabilidadeFaixaService;
import br.com.wmw.lavenderepda.business.service.STService;
import br.com.wmw.lavenderepda.business.service.VerbaSaldoService;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class TotalizadoresPedidoForm extends BaseUIForm {
	
	private Pedido pedido;
	private ItemPedido itemPedido; 
	public boolean isWindow;
	
	private LabelValue lbVlTotalTabelaPedido;
	private LabelValue lbVlTotalDescontoPedido;
	private LabelValue lbVlPctDescontoPedido;
	private LabelValue lbVlTotalPedidoSemImposto;
	private LabelValue lbVlTotalSTPedido;
	private LabelValue lbVlTotalDoPedido;
	private BaseButton btIconeRentabilidadePedido;
	private LabelName lbPctRetabilidadePedido;
	private LabelValue lbVlPctRentabilidadePedido;
	private LabelValue lbVlVerbaDisponivel;
	private LabelValue lbVlVerbaConsumida;
	private LabelValue lbVlVerbaPositiva;
	private LabelValue lbVlVerbaSaldoPedido;
	private LabelValue lbVlVerbaSaldoFinal;
	private LabelValue lbVlTotalMix;
	public LabelValue lbVlTotalUnidade;
	public CadTotalizadoresPedidoWindow cadTotalizadoresPedidoWindow;
    protected BaseScrollContainer scroolContainer;

	public TotalizadoresPedidoForm() {
		super(Messages.GRADE_LABEL_BOTAO_TOTALIZADORES);
        scroolContainer = new BaseScrollContainer(false, true);
        scroolContainer.setBackColor(LavendereColorUtil.formsBackColor);
		barTopContainer.setVisible(false);
		barBottomContainer.setVisible(false);
		lbVlTotalTabelaPedido  = new LabelValue();
		lbVlTotalDescontoPedido = new LabelValue();
		lbVlPctDescontoPedido = new LabelValue();
		lbVlTotalPedidoSemImposto = new LabelValue();
		lbVlTotalSTPedido = new LabelValue();
		lbVlTotalDoPedido = new LabelValue();
		btIconeRentabilidadePedido = new BaseButton(Messages.RENTABILIDADEFAIXA_MSG_NENHUMA_FAIXA_ATINGIDA, getEmptyIcon(), LEFT, WIDTH_GAP);
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() && LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0) {
			btIconeRentabilidadePedido.useBorder = false;
			btIconeRentabilidadePedido.setBackColor(ColorUtil.formsBackColor);
		}
		lbPctRetabilidadePedido = new LabelName(Messages.PEDIDO_LABEL_PERCENTUAL_RENTABILIDADE);
		lbVlPctRentabilidadePedido = new LabelValue();
		lbVlVerbaDisponivel = new LabelValue();
		lbVlVerbaConsumida = new LabelValue();
		lbVlVerbaPositiva = new LabelValue();
		if (LavenderePdaConfig.isApresentaSaldoVerbaPedidoInTotais()) {
			lbVlVerbaSaldoPedido = new LabelValue();
		}
		lbVlVerbaSaldoFinal = new LabelValue();
		lbVlTotalMix = new LabelValue();
		lbVlTotalUnidade = new LabelValue();
	}

	//@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, scroolContainer, LEFT, TOP, FILL, FILL);
		if (isWindow) {
			UiUtil.add(scroolContainer, new LabelName(Messages.GRADE_LABEL_TOTAL_TABELA), lbVlTotalTabelaPedido, getLeft(), TOP + HEIGHT_GAP_BIG);
		} else {
			String labelTotal = ValueUtil.isNotEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher) && pedido.isPedidoTroca() ? Messages.PEDIDO_LABEL_TOTAL_TABELA_TROCA : Messages.PEDIDO_LABEL_TOTAL_TABELA;
			UiUtil.add(scroolContainer, new LabelName(labelTotal), lbVlTotalTabelaPedido, getLeft(), TOP + HEIGHT_GAP_BIG);
		}
		adicionaCamposDescontos();
		adicionaCamposST();
		adicionaCamposRentabilidade();
		adicionaCamposVerba();
		adicionaCamposUnidades();
	}

	private void adicionaCamposST() throws SQLException {
		if ((LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) && !LavenderePdaConfig.usaApresentacaoValorStCapaPedido && !pedido.isPedidoTroca()) {
			UiUtil.add(scroolContainer, new LabelName(Messages.PEDIDO_LABEL_TOTAL_PEDIDO_COM_ST), lbVlTotalSTPedido, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(scroolContainer, new LabelName(Messages.PEDIDO_LABEL_TOTALPEDIDO), lbVlTotalDoPedido, getLeft(), AFTER + HEIGHT_GAP);
		}
	}

	private void adicionaCamposRentabilidade() throws SQLException {
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() && !pedido.isPedidoTroca()) {
			if (LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0) {
				UiUtil.add(scroolContainer, lbPctRetabilidadePedido, btIconeRentabilidadePedido, getLeft(), AFTER + UiUtil.getLabelPreferredHeight());
			} else {
				UiUtil.add(scroolContainer, lbPctRetabilidadePedido, lbVlPctRentabilidadePedido, getLeft(), AFTER + UiUtil.getLabelPreferredHeight());
			}
		}
	}

	private void adicionaCamposUnidades() throws SQLException {
		if (isWindow) {
			UiUtil.add(scroolContainer, new LabelName(Messages.GRADE_LABEL_MIX_ITEM), lbVlTotalMix, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(scroolContainer, new LabelName(Messages.GRADE_LABEL_UNIDADE), lbVlTotalUnidade, getLeft(), AFTER + HEIGHT_GAP);
			return;
		}
		String labelTotalUnidade = ValueUtil.isNotEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher) && pedido.isPedidoTroca() ? Messages.PEDIDO_LABEL_TOTAL_UNIDADE_TROCA : Messages.PEDIDO_LABEL_TOTAL_UNIDADE;
		UiUtil.add(scroolContainer, new LabelName(Messages.PEDIDO_LABEL_TOTAL_MIX), lbVlTotalMix, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(scroolContainer, new LabelName(labelTotalUnidade), lbVlTotalUnidade, getLeft(), AFTER + HEIGHT_GAP);
	}
	
	private void adicionaCamposVerba() throws SQLException {
		if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco && !pedido.isPedidoTroca()) {
			UiUtil.add(scroolContainer, new LabelName(Messages.PEDIDO_LABEL_VLVERBADISPONIVEL), lbVlVerbaDisponivel, getLeft(), AFTER + UiUtil.getLabelPreferredHeight());
			UiUtil.add(scroolContainer, new LabelName(Messages.PEDIDO_LABEL_VLVERBAPEDIDO), lbVlVerbaConsumida, getLeft(), AFTER + HEIGHT_GAP);
			if (LavenderePdaConfig.isMostraFlexPositivoPedido() && LavenderePdaConfig.geraVerbaPositiva) {
				UiUtil.add(scroolContainer, new LabelName(Messages.PEDIDO_LABEL_VLVERBAGERADOPEDIDO), lbVlVerbaPositiva, getLeft(), AFTER + HEIGHT_GAP);
			}
			if (LavenderePdaConfig.isApresentaSaldoVerbaPedidoInTotais()) {
				UiUtil.add(scroolContainer, new LabelName(Messages.PEDIDO_LABEL_VLVERBASALDOPEDIDO), lbVlVerbaSaldoPedido, getLeft(), AFTER + HEIGHT_GAP);
			}
			UiUtil.add(scroolContainer, new LabelName(Messages.PEDIDO_LABEL_VLSALDO), lbVlVerbaSaldoFinal, getLeft(), AFTER + HEIGHT_GAP);
			if (LavenderePdaConfig.isMostraFlexPositivoPedido() && !LavenderePdaConfig.geraVerbaPositiva) {
				UiUtil.add(scroolContainer, new LabelName(Messages.VERBASALDO_LABEL_VLVERBAPEDIDOPOSITIVO), lbVlVerbaPositiva, getLeft(), AFTER + HEIGHT_GAP);
			}
		}
	}

	private void adicionaCamposDescontos() throws SQLException {
		if (!LavenderePdaConfig.exibeAbaTotalizadoresPedidoGrade() && !pedido.isPedidoTroca()) {
			UiUtil.add(scroolContainer, new LabelName(Messages.PEDIDO_LABEL_TOTAL_DESCONTO), lbVlTotalDescontoPedido, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(scroolContainer, new LabelName(Messages.DESCONTO_LABEL_VLPCTDESCONTO), lbVlPctDescontoPedido, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(scroolContainer, new LabelName(Messages.PEDIDO_LABEL_TOTAL_PEDIDO_SEM_IMPOSTOS), lbVlTotalPedidoSemImposto, getLeft(), AFTER + HEIGHT_GAP);
		}
	}

	public void setInfoRentabilidadeVisible(boolean isOculto) {
		lbPctRetabilidadePedido.setVisible(!isOculto);
		btIconeRentabilidadePedido.setVisible(!isOculto);
		lbVlPctRentabilidadePedido.setVisible(!isOculto);
	}
	
	public void onFormShow() throws SQLException {
		super.onFormShow();
	}

	//@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target  == btIconeRentabilidadePedido) {
					RelRentabilidadeFaixaWindow relRentabilidadeFaixa = new RelRentabilidadeFaixaWindow(null, getPedido());
					relRentabilidadeFaixa.popup();
				}
				break;
			}
		}
	}
	
	public void domainToScreen() throws SQLException {
		double valorBase = getValorBase();
		lbVlTotalTabelaPedido.setValue(valorBase);
		setValoresDesconto(valorBase);
		setCamposSt();
		setCamposRentabilidade();
		setTotalizadoresRelacionadosAVerba(pedido);
		setCamposUnidades(pedido);
	}

	private void setCamposSt() {
		if ((LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) && !LavenderePdaConfig.usaApresentacaoValorStCapaPedido) {
			double vlSt = STService.getInstance().getVlTotalStPedido(pedido);
			lbVlTotalSTPedido.setValue(vlSt);
			lbVlTotalDoPedido.setValue(pedido.vlTotalPedido + vlSt);
		}
	}

	private void setCamposRentabilidade() throws SQLException {
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido()) {
			atualizaComponentesRentabilidade(lbVlPctRentabilidadePedido, btIconeRentabilidadePedido);
		}
	}

	private void setValoresDesconto(double valorBase) throws SQLException {
		double vlDesconto = ValueUtil.round(valorBase - pedido.vlTotalPedido);
		vlDesconto = vlDesconto > 0 ? vlDesconto : 0;
		lbVlTotalDescontoPedido.setValue(vlDesconto);
		lbVlTotalPedidoSemImposto.setValue(pedido.vlTotalPedido);
		if (valorBase > 0) {
			lbVlPctDescontoPedido.setValue(ValueUtil.round(vlDesconto * 100 / valorBase));
		} else {
			lbVlPctDescontoPedido.setValue(ValueUtil.round(0));
		}
	}

	private double getValorBase() throws SQLException {
		return ItemPedidoService.getInstance().getValorTotalTotalizadoresPedido(pedido, itemPedido);
	}

	private void setCamposUnidades(Pedido pedido) throws SQLException {
		PedidoService.getInstance().findItemPedidoList(pedido);
		boolean itemJaSalvo = ItemPedidoService.getInstance().isItemJaInseridoMesmaUnidade(pedido, itemPedido);
		int qtItens = pedido.itemPedidoList != null ? pedido.itemPedidoList.size() : 0;
		int qtItensTroca = pedido.itemPedidoTrocaList != null ? pedido.itemPedidoTrocaList.size() : 0;
		if (itemPedido != null && itemJaSalvo) {
			int totalMix = qtItens > 0 ? qtItens - 1 : 0;
			lbVlTotalMix.setValue(StringUtil.getStringValueToInterface(totalMix));
			double qtUnidade = totalMix == 0 || pedido.getQtItensLista() == 0 ? 0 : getQtItensLista(pedido, itemPedido);
			lbVlTotalUnidade.setValue(StringUtil.getStringValueToInterface(qtUnidade, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface));
		} else {
			lbVlTotalMix.setValue(StringUtil.getStringValueToInterface(qtItens == 0 ? qtItensTroca : qtItens));
			lbVlTotalUnidade.setValue(StringUtil.getStringValueToInterface(pedido.getQtItensLista(), LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface));
		}
	}
	
	public double getQtItensLista(Pedido pedido, ItemPedido item) throws SQLException {
		if (ValueUtil.isEmpty(pedido.itemPedidoList)) return 0;

		double sumQtItens = 0.0;
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (produtoExiste(item, itemPedido)) {
				continue;
			}
			sumQtItens += itemPedido.getQtItemLista();
		}
		return sumQtItens;

	}

	private boolean produtoExiste(ItemPedido item, ItemPedido itemPedido) {
		return itemPedido.cdProduto.equals(item.cdProduto) && itemPedido.flTipoItemPedido.equals(item.flTipoItemPedido) && ValueUtil.valueEquals(itemPedido.cdUnidade, item.cdUnidade);
	}


	private void atualizaComponentesRentabilidade(LabelValue lvRentabilidade, BaseButton btIcone) throws SQLException {
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() && !getPedido().isPedidoBonificacao()) {
			if (LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0) {
				atualizaIconeRentabilidade(btIcone);
			} else if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem()) {
				lvRentabilidade.usePercentValue = false;
				lvRentabilidade.setValue(ComiRentabilidadeService.getInstance().getEscalaFaixaByPctRentabilidadePedido(getPedido()));
			} else {
				lvRentabilidade.setValue(getPedido().getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false));
			}
		}
	}
	
	private void atualizaIconeRentabilidade(BaseButton btIcone) throws SQLException {
		if (!getPedido().isPedidoBonificacao()) {
			btIcone.setImage(RentabilidadeFaixaService.getInstance().getIconRentabilidadePedido(getPedido()));
			RentabilidadeFaixa rentabilidadeFaixaAtingida = RentabilidadeFaixaService.getInstance().getRentabilidadeFaixaPedido(getPedido());
			if (rentabilidadeFaixaAtingida != null) {
				btIcone.setText(StringUtil.getStringValue(rentabilidadeFaixaAtingida.dsFaixa));
				btIcone.setForeColor(RentabilidadeFaixaService.getInstance().getCorRentabilidadeFaixa(rentabilidadeFaixaAtingida));
			} else {
				btIcone.setText(Messages.RENTABILIDADEFAIXA_MSG_NENHUMA_FAIXA_ATINGIDA);
				btIcone.setForeColor(Color.BRIGHT);
			}
		}
	}

	private void setTotalizadoresRelacionadosAVerba(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco) {
			clearToTalizadoresDeVerba();
			Vector verbaList =  VerbaSaldoService.getInstance().findAllGrupoByContaC();
			if (ValueUtil.isEmpty(verbaList)) return;

			VerbaSaldo verbaSaldo = VerbaSaldoService.getInstance().getVerbaSaldo(verbaList);
			if (verbaSaldo == null) return;
			
			double vlVerbaPedido = ValueUtil.round(pedido.vlVerbaPedido); 
			double vlVerbaPositiva =  ValueUtil.round(pedido.vlVerbaPedidoPositivo);
			if (usaVigencia(pedido, verbaSaldo)) {
				vlVerbaPedido = 0;
			}
			if (LavenderePdaConfig.usaPedidoBonificacao() && pedido.isPedidoBonificacao() && !pedido.getTipoPedido().isIgnoraControleVerba()) {
				vlVerbaPedido = ValueUtil.round(pedido.vlTotalItens) * -1;
			}
			lbVlVerbaDisponivel.setValue(VerbaSaldoService.getInstance().getVlVerbaDisponivel(pedido, verbaSaldo.vlSaldo));
			lbVlVerbaConsumida.setValue(StringUtil.getStringValueToInterface(vlVerbaPedido));
			if (LavenderePdaConfig.isMostraFlexPositivoPedido()) {
				lbVlVerbaPositiva.setValue(StringUtil.getStringValueToInterface(vlVerbaPositiva));
			}
			if (LavenderePdaConfig.isApresentaSaldoVerbaPedidoInTotais()) {
				lbVlVerbaSaldoPedido.setValue(StringUtil.getStringValueToInterface(pedido.getVlVerbaPedidoDisponivel()));
			}
			lbVlVerbaSaldoFinal.setValue(VerbaSaldoService.getInstance().getDsSaldoFinal(pedido, verbaSaldo.vlSaldo, vlVerbaPedido, vlVerbaPositiva));
			lbVlVerbaSaldoFinal.setForeColor(ValueUtil.getDoubleValue(lbVlVerbaSaldoFinal.getValue()) > 0 ? ColorUtil.softGreen : ColorUtil.softRed);
		}
	}

	private boolean usaVigencia(Pedido pedido, VerbaSaldo verbaSaldo) {
		return LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco() &&
				(verbaSaldo.dtVigenciaInicial.isAfter(pedido.dtEmissao) || verbaSaldo.dtVigenciaFinal.isBefore(pedido.dtEmissao));
	}
	
	private void clearToTalizadoresDeVerba() {
		lbVlVerbaDisponivel.setValue("");
		lbVlVerbaConsumida.setValue("");
		lbVlVerbaPositiva.setValue("");
		if (LavenderePdaConfig.isApresentaSaldoVerbaPedidoInTotais()) {
			lbVlVerbaSaldoPedido.setValue("");
		}
		lbVlVerbaSaldoFinal.setValue("");
	}
	
	private Image getEmptyIcon() {
		int heigthContainerIcons = (int)(UiUtil.getControlPreferredHeight() * 1.2);
		int alturaLargura = (int)(heigthContainerIcons * 0.70);
		return UiUtil.getEmptyImage(alturaLargura, alturaLargura);
	
	}
	
	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) throws SQLException {
		this.pedido = pedido;
	}

	public ItemPedido getItemPedido() {
		return itemPedido;
	}

	public void setItemPedido(ItemPedido itemPedido) {
		this.itemPedido = itemPedido;
	}

	public void clearComponentesFromScreen() {
		scroolContainer.removeAll();
		this.removeAll();
	}
	
}
