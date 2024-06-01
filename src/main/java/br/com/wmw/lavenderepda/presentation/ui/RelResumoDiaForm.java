package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.List;

import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.ext.BaseToolTip;
import br.com.wmw.framework.presentation.ui.ext.CalendarWmw;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ResumoDia;
import br.com.wmw.lavenderepda.business.domain.ValorIndicador;
import br.com.wmw.lavenderepda.business.service.IndicadorService;
import br.com.wmw.lavenderepda.business.service.IndicadorWmwService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.PontuacaoConfigService;
import br.com.wmw.lavenderepda.business.service.ResumoDiaService;
import br.com.wmw.lavenderepda.business.service.ValorIndicadorService;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.StatusExcecaoMultiComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoPedidoComboBox;
import totalcross.sys.Settings;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Date;
import totalcross.util.Vector;

public class RelResumoDiaForm extends BaseUIForm {

	private BaseScrollContainer sContainer;
	private SessionContainer containerFiltros;
	private SessionContainer containerValoresMedios;
	private SessionContainer containerTituloIndicadores;
	private Container containerIndicadores;
	private EditDate edDataResumo;
	private LabelName lbDataResumo;
	private LabelName lbVlTotalVendido;
	private LabelName lbQtItensVendidos;
	private LabelName lbQtPedidos;
	private LabelName lbVlTotalVerbaConsumida;
	private LabelName lbMediaValorPorPedido;
	private LabelName lbMediaItensPorPedido;
	private LabelName lbMediaVerbaConsumidaPorPedido;
	private LabelName lbValoresMedios;
	private LabelName lbIndicadores;
	private LabelName lbVlTotalBonificacao;
	private LabelName lbVlVerbaBonificacao;
	private LabelName lbQtItensVendidosBonificados;
	private LabelName lbVlTotalPontuacao;
	private LabelName lbVlTotalPeso;
	private LabelValue lvVlTotalVendido;
	private LabelValue lvQtItensVendidos;
	private LabelValue lvQtPedidos;
	private LabelValue lvQtItensVendidosBonificados;
	private LabelValue lvVlTotalVerbaConsumida;
	private LabelValue lvMediaValorPorPedido;
	private LabelValue lvMediaItensPorPedido;
	private LabelValue lvMediaVerbaConsumidaPorPedido;
	private LabelValue lvVlTotalBonificacao;
	private LabelValue lvVlVerbaBonificacao;
	private LabelValue lvVlTotalPontuacao;
	private LabelValue lvVlTotalPeso;
	private ResumoDia resumoDia;
	private RepresentanteSupervComboBox cbRepresentante;
	protected StatusExcecaoMultiComboBox cbStatusExcecaoResumoDia;
	private TipoPedidoComboBox cbTipoPedido;
	private boolean isFirstRefresh;
	
	private LabelName lbVlTotalVolume;
	private LabelValue lvVlTotalVolume;

	public RelResumoDiaForm() throws SQLException {
		super(Messages.RESUMO_DIA_TITULO);
		containerFiltros = new SessionContainer();
		containerValoresMedios = new SessionContainer();
		containerIndicadores = new Container();
		containerTituloIndicadores = new SessionContainer();
		sContainer = new BaseScrollContainer(false, true);
		edDataResumo = new EditDate();
		edDataResumo.setValue(DateUtil.getCurrentDate());
		edDataResumo.onlySelectByCalendar();
		lbDataResumo = new LabelName(Messages.DATA_LABEL_DATA);
		lbVlTotalVendido = new LabelName(Messages.RESUMO_DIA_LABEL_VL_TOTAL_VENDIDO);
		lbQtItensVendidos = new LabelName(Messages.RESUMO_DIA_LABEL_QT_ITENS_VENDIDOS);
		lbQtPedidos = new LabelName(Messages.RESUMO_DIA_LABEL_QT_PEDIDOS);
		lbVlTotalVerbaConsumida = new LabelName(Messages.RESUMO_DIA_LABEL_VL_TOTAL_VERBA_CONSUMIDA);
		lbMediaValorPorPedido = new LabelName(Messages.RESUMO_DIA_LABEL_MEDIA_VL_PEDIDO);
		lbMediaItensPorPedido = new LabelName(Messages.RESUMO_DIA_LABEL_MEDIA_ITENS_PEDIDO);
		lbMediaVerbaConsumidaPorPedido = new LabelName(Messages.RESUMO_DIA_LABEL_MEDIA_VERBA_CONSUMIDA_PEDIDO);
		lbValoresMedios = new LabelName(Messages.RESUMO_DIA_LABEL_VALORES_MEDIOS);
		lbIndicadores = new LabelName(Messages.RESUMO_DIA_LABEL_PRODUTIVIDADE);
		lbVlTotalBonificacao = new LabelName(Messages.RESUMO_DIA_LABEL_VL_TOTAL_BONIFICACAO);
		lbVlVerbaBonificacao = new LabelName(Messages.RESUMO_DIA_LABEL_VL_TOTAL_VERBA_BONIFICACAO);
		lbQtItensVendidosBonificados = new LabelName(Messages.RESUMO_DIA_LABEL_VL_QTD_ITENS_VENDIDOS_BONIFICADOS);
		lbVlTotalVolume = new LabelName(Messages.RESUMO_DIA_LABEL_TOTAL_VOLUME);
		lvVlTotalVendido = new LabelValue();
		lvQtItensVendidos = new LabelValue();
		lvQtPedidos = new LabelValue();
		lvQtItensVendidosBonificados = new LabelValue();
		lvVlTotalVerbaConsumida = new LabelValue();
		lvMediaValorPorPedido = new LabelValue();
		lvMediaItensPorPedido = new LabelValue();
		lvMediaVerbaConsumidaPorPedido = new LabelValue();
		lvVlTotalBonificacao = new LabelValue();
		lvVlVerbaBonificacao = new LabelValue();
		lvVlTotalVolume = new LabelValue();
		if (LavenderePdaConfig.usaControlePontuacao && (LavenderePdaConfig.mostraPontuacaoResumoDiaBase() || LavenderePdaConfig.mostraPontuacaoResumoDiaRealizado())) {
			lbVlTotalPontuacao = new LabelName(Messages.PONTUACAO_FECHAMENTO_DIARIO);
			lvVlTotalPontuacao = new LabelValue();
		}
		if(LavenderePdaConfig.isConfigCalculoPesoPedido()){
			lbVlTotalPeso = new LabelName(Messages.RESUMO_DIA_LABEL_VL_TOTAL_PESO);
			lvVlTotalPeso= new LabelValue();
		}
        if (LavenderePdaConfig.usaFiltroStatusExcecaoResumoDia()) {
        	cbStatusExcecaoResumoDia = new StatusExcecaoMultiComboBox();
        	cbStatusExcecaoResumoDia.load();
        }
		cbRepresentante = new RepresentanteSupervComboBox();
		cbRepresentante.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);
		if (!LavenderePdaConfig.naoCalculaDadosResumoDiaPda) {
			cbTipoPedido = new TipoPedidoComboBox(false, true);
			cbTipoPedido.setSelectedIndex(0);
		}
		calculateAndLoadResumoDia();
		isFirstRefresh = true;
	}

	protected void onFormStart() throws SQLException {
		int nuCamposFiltros = 1;
		nuCamposFiltros += SessionLavenderePda.isUsuarioSupervisor() ? 1 : 0;
		nuCamposFiltros += !LavenderePdaConfig.naoCalculaDadosResumoDiaPda ? 1 : 0;
		nuCamposFiltros += LavenderePdaConfig.usaFiltroStatusExcecaoResumoDia() ? 1 : 0;

		UiUtil.add(this, containerFiltros, LEFT, getTop(), FILL, UiUtil.getControlPreferredHeight() * nuCamposFiltros + UiUtil.getLabelPreferredHeight() * nuCamposFiltros + HEIGHT_GAP * nuCamposFiltros);
		UiUtil.add(containerFiltros, lbDataResumo, edDataResumo, getLeft(), TOP + HEIGHT_GAP);
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			UiUtil.add(containerFiltros, new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE), cbRepresentante, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (!LavenderePdaConfig.naoCalculaDadosResumoDiaPda) {
			UiUtil.add(containerFiltros, new LabelName(Messages.RESUMO_DIA_TIPO_PEDIDO), cbTipoPedido, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (LavenderePdaConfig.usaFiltroStatusExcecaoResumoDia()) {
			UiUtil.add(containerFiltros, new LabelName(Messages.PEDIDO_LABEL_CDSTATUSEXCECAO), cbStatusExcecaoResumoDia, getLeft(), AFTER + HEIGHT_GAP);
		}
		UiUtil.add(this, sContainer, LEFT, AFTER, FILL, FILL - barBottomContainer.getHeight());
		//--
		if (LavenderePdaConfig.isModoResumoDiario()) {
			UiUtil.add(sContainer, lbVlTotalVendido, CENTEREDLABEL, AFTER + HEIGHT_GAP_BIG);
			UiUtil.add(sContainer, lvVlTotalVendido, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
			
			UiUtil.add(sContainer, lbVlTotalBonificacao, CENTEREDLABEL, AFTER + HEIGHT_GAP);
			UiUtil.add(sContainer, lvVlTotalBonificacao, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
			
			UiUtil.add(sContainer, lbQtItensVendidos, CENTEREDLABEL, AFTER + HEIGHT_GAP);
			UiUtil.add(sContainer, lvQtItensVendidos, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
			
			UiUtil.add(sContainer, lbQtItensVendidosBonificados, CENTEREDLABEL, AFTER + HEIGHT_GAP);
			UiUtil.add(sContainer, lvQtItensVendidosBonificados, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
				
			UiUtil.add(sContainer, lbQtPedidos, CENTEREDLABEL, AFTER + HEIGHT_GAP);
			UiUtil.add(sContainer, lvQtPedidos, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
			if (isUsaVerba()) {
				UiUtil.add(sContainer, lbVlTotalVerbaConsumida, CENTEREDLABEL, AFTER + HEIGHT_GAP);
				UiUtil.add(sContainer, lvVlTotalVerbaConsumida, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
			}
			if (isUsaPedidoBonificacao()) {
				UiUtil.add(sContainer, lbVlVerbaBonificacao, CENTEREDLABEL, AFTER + HEIGHT_GAP);
				UiUtil.add(sContainer, lvVlVerbaBonificacao, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
			}
			if (LavenderePdaConfig.usaControlePontuacao && (LavenderePdaConfig.mostraPontuacaoResumoDiaBase() || LavenderePdaConfig.mostraPontuacaoResumoDiaRealizado())) {
				UiUtil.add(sContainer, lbVlTotalPontuacao, CENTEREDLABEL, AFTER + HEIGHT_GAP);
				UiUtil.add(sContainer, lvVlTotalPontuacao, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);				
			}
			if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
				UiUtil.add(sContainer, lbVlTotalPeso, CENTEREDLABEL, AFTER + HEIGHT_GAP);
				UiUtil.add(sContainer, lvVlTotalPeso, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
			}
			if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
				UiUtil.add(sContainer, lbVlTotalVolume, CENTEREDLABEL, AFTER + HEIGHT_GAP);
				UiUtil.add(sContainer, lvVlTotalVolume, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
			}
			UiUtil.add(sContainer, containerValoresMedios, LEFT, AFTER + HEIGHT_GAP, FILL, UiUtil.getControlPreferredHeight());
			UiUtil.add(containerValoresMedios, lbValoresMedios, CENTER, CENTER);
			UiUtil.add(sContainer, lbMediaValorPorPedido, CENTEREDLABEL, AFTER + HEIGHT_GAP);
			UiUtil.add(sContainer, lvMediaValorPorPedido, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
			UiUtil.add(sContainer, lbMediaItensPorPedido, CENTEREDLABEL, AFTER + HEIGHT_GAP);
			UiUtil.add(sContainer, lvMediaItensPorPedido, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
			if (isUsaVerba()) {
				UiUtil.add(sContainer, lbMediaVerbaConsumidaPorPedido, CENTEREDLABEL, AFTER + HEIGHT_GAP);
				UiUtil.add(sContainer, lvMediaVerbaConsumidaPorPedido, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
			}
			resumoDiaToScreen();
		}
		if (LavenderePdaConfig.isModoIndicadores()) {
			UiUtil.add(sContainer, containerTituloIndicadores, LEFT, AFTER + HEIGHT_GAP, FILL, UiUtil.getControlPreferredHeight());
			UiUtil.add(containerTituloIndicadores, lbIndicadores, CENTER, CENTER);
			cbRepresentanteChange();
		}
	}
	
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target instanceof CalendarWmw) {
					if (ValueUtil.isNotEmpty(edDataResumo.getValue()) && !LavenderePdaConfig.usaFechamentoDeVendasPorPeriodo) {
						calculateAndLoadResumoDia();
						resumoDiaToScreen();
						cbRepresentanteChange();
					}
				} else if (event.target == cbRepresentante) {
					cbRepresentanteChange();
					resumoDia = ResumoDiaService.getInstance().findResumoDia(edDataResumo.getValue(), getCdRepresentante());
                	calculateAndLoadResumoDia();
					resumoDiaToScreen();
                } else if (event.target == cbStatusExcecaoResumoDia) {
                	resumoDia = ResumoDiaService.getInstance().findResumoDia(edDataResumo.getValue(), getCdExececaoStatusPedido());
                	calculateAndLoadResumoDia();
                	resumoDiaToScreen();
	    		} else if (event.target == cbTipoPedido) {
                	resumoDia = ResumoDiaService.getInstance().findResumoDia(edDataResumo.getValue(), getCdRepresentante());
                	calculateAndLoadResumoDia();
                	resumoDiaToScreen();
                }
				break;
		}
	}

	private void cbRepresentanteChange() throws SQLException {
		if (!LavenderePdaConfig.isModoIndicadores()) return;
		
		List<ValorIndicador> listaIndicadores = IndicadorService.getInstance().buscaIndicadoresPor(SessionLavenderePda.cdEmpresa, getCdRepresentante(), edDataResumo.getValue());
		List<ValorIndicador> listaIndicadoresWmw = IndicadorWmwService.getInstance().buscaIndicadoresPor(SessionLavenderePda.cdEmpresa, getCdRepresentante(), edDataResumo.getValue());
		listaIndicadores.addAll(listaIndicadoresWmw);
		containerIndicadores.removeAll();
		sContainer.remove(containerIndicadores);
		UiUtil.add(sContainer, containerIndicadores, LEFT, containerTituloIndicadores.getY2(), FILL, UiUtil.getControlPreferredHeight() * listaIndicadores.size());
		boolean isFirst = true;
		for (ValorIndicador valorIndicador : listaIndicadores) {
			LabelName label = new LabelName(StringUtil.getStringAbreviada(valorIndicador.dsIndicador, (int) (Settings.screenWidth / 1.2)));
			String dsVlIndicador = valorIndicador.dsVlIndicador;
			if (ValueUtil	.isNotEmpty(dsVlIndicador) && ValueUtil.isNotEmpty(dsVlIndicador.trim())) {
				dsVlIndicador = ValorIndicadorService.getInstance().applyMaskOnDsVlIndicador(dsVlIndicador, valorIndicador.dsMascaraFormato);
			}
			new BaseToolTip(label, valorIndicador.dsIndicador);
			LabelValue labelValue = new LabelValue(ValueUtil.isNotEmpty(dsVlIndicador) ? dsVlIndicador : " ");
			int y = (isFirst) ? AFTER + WIDTH_GAP : AFTER;
			Container c = new Container();
			UiUtil.add(containerIndicadores, c, LEFT, AFTER, FILL);
			UiUtil.add(c, label, LEFT + WIDTH_GAP_BIG, y, PREFERRED);
			UiUtil.add(c, labelValue, RIGHT - WIDTH_GAP_BIG, SAME + WIDTH_GAP, PREFERRED, PREFERRED);
			UiUtil.add(c, new LabelName(" "), getLeft(), AFTER);
			if (!isFirst) {
				c.setBorderStyle(BORDER_TOP);
				c.borderColor = ColorUtil.componentsBorderColor;
			}
			isFirst = false;
		}
		if (!isFirstRefresh) {
			reposition();
		}
		isFirstRefresh = false;
	}

	private String getCdRepresentante() {
		if (cbRepresentante.getValue() == null) {
			return SessionLavenderePda.getRepresentante().cdRepresentante;
		}
		return cbRepresentante.getValue();
	}
	
	private String getCdExececaoStatusPedido() {
		return cbStatusExcecaoResumoDia.getSelected();
	}

	private void calculateAndLoadResumoDia() {
		if (!LavenderePdaConfig.usaFechamentoDeVendasPorPeriodo) {
			UiUtil.showProcessingMessage();
			try {
				if (!LavenderePdaConfig.naoCalculaDadosResumoDiaPda) {
					Vector dataList = PedidoService.getInstance().findDistinctDtEmissaoUltimosPedidosNaoTransmitidos(getCdRepresentante());
					String[] selectedStatusExcecao;
					if (cbStatusExcecaoResumoDia != null) {
						selectedStatusExcecao = cbStatusExcecaoResumoDia.getSelected().split(",");
					} else {
						selectedStatusExcecao = null;
					}
					if (ValueUtil.isNotEmpty(dataList)) {
						for (int i = 0; i < dataList.size(); i++) {
							ResumoDiaService.getInstance().calculateAndGetResumoDia((Date)dataList.items[i], getCdRepresentante(), cbTipoPedido.getValue(), selectedStatusExcecao);
						}
					}
					resumoDia = ResumoDiaService.getInstance().calculateAndGetResumoDia(edDataResumo.getValue(), getCdRepresentante(), cbTipoPedido.getValue(), selectedStatusExcecao);
				} else {
					resumoDia = ResumoDiaService.getInstance().findResumoDia(edDataResumo.getValue(), getCdRepresentante());
				}
			} catch (Throwable ex) {
				UiUtil.showErrorMessage(ex);
			} finally {
				UiUtil.unpopProcessingMessage();
			}
		}
	}

	private void resumoDiaToScreen() {
		if (resumoDia != null) {
			lvVlTotalVendido.setValue(resumoDia.vlTotalVendido);
			lvQtItensVendidos.setValue(resumoDia.qtItensVendidos);
			lvQtPedidos.setValue(resumoDia.qtPedidos);
			lvQtItensVendidosBonificados.setValue(resumoDia.qtItensVendidosBonificados);
			lvVlTotalVerbaConsumida.setValue(resumoDia.vlVerbaConsumida);
			lvMediaValorPorPedido.setValue(resumoDia.qtPedidos > 0 ? ValueUtil.round(resumoDia.vlTotalVendido / resumoDia.qtPedidos): 0);
			lvMediaItensPorPedido.setValue(resumoDia.qtPedidos > 0 ? ValueUtil.round((double)resumoDia.qtItensVendidos / (double)resumoDia.qtPedidos) : 0);
			lvMediaVerbaConsumidaPorPedido.setValue(resumoDia.qtPedidos > 0 ? ValueUtil.round(resumoDia.vlVerbaConsumida / resumoDia.qtPedidos) : 0);
			lvVlTotalBonificacao.setValue(resumoDia.vlTotalBonificacao);
			lvVlVerbaBonificacao.setValue(resumoDia.vlVerbaBonificacao);
			if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
				lvVlTotalVolume.setValue(resumoDia.vlTotalVolume);
			}
			if (LavenderePdaConfig.usaControlePontuacao && (LavenderePdaConfig.mostraPontuacaoResumoDiaBase() || LavenderePdaConfig.mostraPontuacaoResumoDiaRealizado())) {
				final String vlPontuacao = PontuacaoConfigService.getInstance().getPontuacaoBaseRealizada(resumoDia.vlTotalPontuacaoRealizado, resumoDia.vlTotalPontuacaoBase, LavenderePdaConfig.mostraPontuacaoResumoDiaBase(), LavenderePdaConfig.mostraPontuacaoResumoDiaRealizado());
				lvVlTotalPontuacao.setValue(vlPontuacao != null ? vlPontuacao : StringUtil.getStringValueToInterface(0));
				lvVlTotalPontuacao.setForeColor(PontuacaoConfigService.getInstance().getPontuacaoColor(resumoDia.vlTotalPontuacaoRealizado, resumoDia.vlTotalPontuacaoBase, LavenderePdaConfig.mostraPontuacaoResumoDiaBase(), LavenderePdaConfig.mostraPontuacaoResumoDiaRealizado(), true));
			}
			if(LavenderePdaConfig.isConfigCalculoPesoPedido()){
				lvVlTotalPeso.setValue(resumoDia.vlTotalPeso);
			}
		} else {
			clearScreen();
		}
	}

	private void clearScreen() {
		lvVlTotalVendido.setValue("");
		lvQtItensVendidos.setValue("");
		lvQtPedidos.setValue("");
		lvQtItensVendidosBonificados.setValue("");
		lvVlTotalVerbaConsumida.setValue("");
		lvMediaValorPorPedido.setValue("");
		lvMediaItensPorPedido.setValue("");
		lvMediaVerbaConsumidaPorPedido.setValue("");
		lvVlTotalBonificacao.setValue("");
		lvVlVerbaBonificacao.setValue("");
		if (LavenderePdaConfig.usaControlePontuacao && (LavenderePdaConfig.mostraPontuacaoResumoDiaBase() || LavenderePdaConfig.mostraPontuacaoResumoDiaRealizado())) {
			lvVlTotalPontuacao.setValue("");
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
			lvVlTotalPeso.setValue("");
		}
		if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
			lvVlTotalVolume.setValue("");
		}
	}

	public static boolean isUsaVerba() {
		boolean usaVerbaItemPedidoPorItemTabPreco = LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco;
		boolean informaVerbaManual = LavenderePdaConfig.informaVerbaManual;
		boolean permiteBonificarProdutoPedidoUsandoVerba = LavenderePdaConfig.isPermiteBonificarProdutoPedidoUsandoVerba() && LavenderePdaConfig.getPercMaxValorPedidoBonificadoDouble() > 0;
		boolean usaPedidoBonificacaoUsandoVerbaCliente = LavenderePdaConfig.usaPedidoBonificacaoUsandoVerbaCliente;
		boolean aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex = LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex;
		boolean usaVerbaPorFaixaRentabilidadeComissao = LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao;
		boolean usaVerbaGrupoProdComToleranciaNoDesconto = LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto;
		boolean usaVerbaPositivaPorGrupoProdutoTabelaPreco = LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco;
		boolean usaGeracaoOuConsumoVerbaGrupoSaldoPersonalizada = LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada();
		return usaVerbaItemPedidoPorItemTabPreco || informaVerbaManual || permiteBonificarProdutoPedidoUsandoVerba
				|| usaPedidoBonificacaoUsandoVerbaCliente || aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex
				|| usaVerbaPorFaixaRentabilidadeComissao || usaVerbaGrupoProdComToleranciaNoDesconto
				|| usaVerbaPositivaPorGrupoProdutoTabelaPreco || usaGeracaoOuConsumoVerbaGrupoSaldoPersonalizada;
	}

	public static boolean isUsaPedidoBonificacao() {
		return (LavenderePdaConfig.usaPedidoBonificacao() && LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco) || LavenderePdaConfig.usaPedidoBonificacaoUsandoVerbaCliente;
	}

}
