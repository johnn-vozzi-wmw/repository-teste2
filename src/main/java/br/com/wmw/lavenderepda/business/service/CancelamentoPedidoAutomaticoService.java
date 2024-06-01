package br.com.wmw.lavenderepda.business.service;

import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.validation.CancelamentoPedidoAutoException;
import totalcross.util.Date;
import totalcross.util.Vector;

public class CancelamentoPedidoAutomaticoService {
	
	private CancelamentoPedidoAutomaticoService() {}

	private static CancelamentoPedidoAutomaticoService instance;
	
	public static CancelamentoPedidoAutomaticoService getInstance() {
		return instance == null ? instance = new CancelamentoPedidoAutomaticoService() : instance;
	}
	
	public void cancelaPedidosAutomaticamente() throws CancelamentoPedidoAutoException {
		if (!validaCancelamentoAutomatico()) {
			return;
		}
		final String cdMotCancelPedido = getCdMotivoCancelamentoPedidoAuto();
		beforeCancelamentoPedidosAutomatico(cdMotCancelPedido);
		List<Pedido> listPedidosCancelados = new ArrayList<>();
		logInicioSyncPedidos();
		listPedidosCancelados.addAll(cancelaPedidosAbertosAutomaticamente(cdMotCancelPedido));
		listPedidosCancelados.addAll(cancelaPedidosFechadosAutomaticamente(cdMotCancelPedido));
		logFinalizacaoSyncPedidos(listPedidosCancelados);
	}

	public void logInicioSyncPedidos() {
		if (!LavenderePdaConfig.usaCancelamentoAutomaticoPedidoAbertoFechado()) {
			return;
		}
		String messageCancelamento = getLogMessageCancelamento();
		if (ValueUtil.isNotEmpty(messageCancelamento)) {
			LogSync.info(messageCancelamento);
		}
	}

	private String getLogMessageCancelamento() {
		if (LavenderePdaConfig.usaCancPedFechadoAuto && LavenderePdaConfig.usaCancPedAbertoAuto) return Messages.CANCELAMENTO_SYNC_INICIANDO_PROCESSO_PEDIDOS_ABERTOS_FECHADOS;
		if (LavenderePdaConfig.usaCancPedFechadoAuto) return Messages.CANCELAMENTO_SYNC_INICIANDO_PROCESSO_PEDIDOS_FECHADOS;
		if (LavenderePdaConfig.usaCancPedAbertoAuto) return Messages.CANCELAMENTO_SYNC_INICIANDO_PROCESSO_PEDIDOS_ABERTOS;
		return ValueUtil.VALOR_NI;
	}

	public void logFinalizacaoSyncPedidos(List<Pedido> listPedidos) {
		if (!LavenderePdaConfig.usaCancelamentoAutomaticoPedidoAbertoFechado())
			return;
		if (ValueUtil.isNotEmpty(listPedidos)) {
			for (Pedido pedido : listPedidos) {
				LogSync.warn(MessageUtil.getMessage(Messages.CANCELAMENTO_SYNC_PEDIDO, new String[] {pedido.nuPedido, pedido.statusPedidoPda.cdStatusPedido, pedido.statusPedidoPda.dsStatusPedido}));
			}
		} else {
			LogSync.warn(Messages.CANCELAMENTO_SYNC_PEDIDO_NENHUM_CANCELADO);
		}
		LogSync.logSection(Messages.CANCELAMENTO_SYNC_PEDIDOS_FINAL_PROCESSO);
	}

	private boolean validaCancelamentoAutomatico() throws CancelamentoPedidoAutoException {
		if (!LavenderePdaConfig.usaCancelamentoAutomaticoPedidoAbertoFechado()) return false;
		try {
			String dtUltCancelamentoConfig =  ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.dataUltimoCancelamentoAutomaticoPedido, getChaveConfigInterno());
			Date lastWeekDayCancelamento = DateUtil.getLastDateOfDay(LavenderePdaConfig.nuDiaSemCanPedAbertoFechadoAuto - 1);
			return (ValueUtil.isEmpty(dtUltCancelamentoConfig)) ? DateUtil.isAfterOrEquals(lastWeekDayCancelamento) : DateUtil.isAfter(lastWeekDayCancelamento, new Date(dtUltCancelamentoConfig));
		} catch (Throwable e) {
			throw new CancelamentoPedidoAutoException(MessageUtil.getMessage(Messages.CANCELAMENTO_PEDIDO_VALIDATION_ERROR, e.getMessage()));
		}
	}

	private String getCdMotivoCancelamentoPedidoAuto() {
		try {
			return MotCancelPedidoService.getInstance().findCdMotCancelPedidoDefault();
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			return null;
		}
	}

	private void beforeCancelamentoPedidosAutomatico(final String cdMotCancelPedido) throws CancelamentoPedidoAutoException {
		if (ValueUtil.isEmpty(cdMotCancelPedido)) throw new CancelamentoPedidoAutoException(Messages.CANCELAMENTO_PEDIDO_STATUS_INVALIDO);
		updateDataUltimoCancelamentoConfigInterno();
	}

	private void updateDataUltimoCancelamentoConfigInterno() throws CancelamentoPedidoAutoException {
		try {
			ConfigInternoService.getInstance().addValue(ConfigInterno.dataUltimoCancelamentoAutomaticoPedido, getChaveConfigInterno(), DateUtil.formatDateDDMMYYYY(DateUtil.getCurrentDate()));
		} catch (Throwable e) {
			throw new CancelamentoPedidoAutoException(MessageUtil.getMessage(Messages.CANCELAMENTO_PEDIDO_ERROR_CONFIGINTERNO, e.getMessage()));
		}
	}

	private String getChaveConfigInterno() {
		return SessionLavenderePda.cdEmpresa + "-" +  SessionLavenderePda.getRepresentante().cdRepresentante;
	}

	private List<Pedido> cancelaPedidosAbertosAutomaticamente(final String cdMotCancelPedido) throws CancelamentoPedidoAutoException {
		if (!LavenderePdaConfig.usaCancPedAbertoAuto) return new ArrayList<>();
		return cancelaPedidos(PedidoService.getInstance().findPedidoPdaListByStatus(LavenderePdaConfig.cdStatusPedidoAberto), cdMotCancelPedido);
	}
	
	private List<Pedido> cancelaPedidosFechadosAutomaticamente(final String cdMotCancelPedido) throws CancelamentoPedidoAutoException {
		if (!LavenderePdaConfig.usaCancPedFechadoAuto) return new ArrayList<>();
		return cancelaPedidos(PedidoService.getInstance().findPedidoPdaListByStatus(LavenderePdaConfig.cdStatusPedidoFechado), cdMotCancelPedido);
	}
	
	private List<Pedido> cancelaPedidos(Vector listPedidos, final String cdMotCancelPedido) throws CancelamentoPedidoAutoException {
		if (ValueUtil.isEmpty(listPedidos)) return new ArrayList<>();
		PedidoService pedidoService = PedidoService.getInstance();
		Pedido pedido = null;
		try {
			int size = listPedidos.size();
			List<Pedido> listPedidoCancelados = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				pedido = (Pedido) listPedidos.items[i];
				if (pedido.dtEmissao.equals(DateUtil.getCurrentDate())) continue;
				pedidoService.efetuaCancelamentoPedido(cdMotCancelPedido, pedido);
				listPedidoCancelados.add(pedido);
			}
			return listPedidoCancelados;
		} catch (Throwable e) {
			throw new CancelamentoPedidoAutoException(pedido, e.getMessage());
		}
	}

}
