package br.com.wmw.lavenderepda.util;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.WmwInputBox;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.service.PedidoService;

public class UiMessagesUtil {

	public static void mostraMensagemPedidosAbertos() throws SQLException {
		boolean visitaEmAndamento = false;
		int pedidosEmOrcamento = 0;
		
		int pedidosAbertos = PedidoService.getInstance().countPedidosByStatus(false, false, LavenderePdaConfig.cdStatusPedidoAberto);
		int pedidosFechados = PedidoService.getInstance().countPedidosByStatus(false, false, LavenderePdaConfig.cdStatusPedidoFechado);
		if (LavenderePdaConfig.usaNovoPedidoOrcamentoSemRegistroChegada) {
			pedidosEmOrcamento = PedidoService.getInstance().countPedidosEmStatusPreOrcamento();
			visitaEmAndamento = SessionLavenderePda.visitaAndamento != null;
		}
		if (pedidosAbertos != 0 || pedidosFechados != 0 || pedidosEmOrcamento != 0 || visitaEmAndamento) {
			StringBuffer sb = new StringBuffer();
			sb.append(MessageUtil.quebraLinhas(Messages.MSG_PEDIDOS_PENDENTES)+ "\n")
				.append(pedidosAbertos).append(" ").append(Messages.QTDE_PEDIDO_ABERTO).append("\n")
				.append(pedidosFechados).append(" ").append(Messages.QTDE_PEDIDO_FECHADO);
			if (pedidosEmOrcamento != 0) {
				sb.append("\n").append(pedidosEmOrcamento).append(" ").append(Messages.QTDE_PEDIDO_EM_ORCAMENTO);
			}
			if (visitaEmAndamento) {
				sb.append("\n").append(Messages.AVISO_VISITA_EM_ANDAMENTO);
			}
			UiUtil.showWarnMessage(sb.toString());
		}
	}
	
	public static WmwInputBox getInputBoxBackup(String message) {
		return new WmwInputBox(FrameworkMessages.TITULO_MSG_ATENCAO, MessageUtil.getMessage(message, Messages.BACKUP_CONFERENCIA_CONFIRMO), ValueUtil.VALOR_NI, new String[] {FrameworkMessages.BOTAO_CANCELAR, FrameworkMessages.BOTAO_OK});
	}
}
