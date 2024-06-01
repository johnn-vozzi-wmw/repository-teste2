package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AgendaVisita;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.EnderecoGpsPdaService;
import br.com.wmw.lavenderepda.business.service.VisitaService;
import totalcross.util.Date;

public class VisitaUiUtil {

	private VisitaUiUtil() {}
	
	public static boolean avisaEBloqueiaCriarPedidoVisitaAndamento(Cliente cliente) throws SQLException {
		if (LavenderePdaConfig.isBloqueiaNovoPedidoClienteSemRegistroChegada() && SessionLavenderePda.visitaAndamento != null && !ValueUtil.valueEquals(cliente.cdCliente, SessionLavenderePda.visitaAndamento.cdCliente)) {
			Cliente clienteVisita = ClienteService.getInstance().getCliente(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getRepresentante().cdRepresentante, SessionLavenderePda.visitaAndamento.cdCliente);
			if (clienteVisita.cdCliente != null) {
				if (LavenderePdaConfig.usaNovoPedidoOrcamentoSemRegistroChegada && !cliente.convertendoOrcamentoEmPedido) {
					SessionLavenderePda.getCliente().somentePedidoPreOrcamento = true;
					return false;
				} else {
					UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.CLIENTE_VISITA_EM_ANDAMENTO_BLOQUEIO_PEDIDO, clienteVisita.toString()));
					return true;
				}
			}
			UiUtil.showWarnMessage(Messages.CLIENTE_VISITA_EM_ANDAMENTO_SEM_CLIENTE);
			VisitaService.getInstance().desfazVisitaEmAndamento(SessionLavenderePda.visitaAndamento);
		}
		return false;
	}
	
	public static boolean sugereRegistroChegada(Cliente cliente, AgendaVisita agendaVisita) throws SQLException {
		if (SessionLavenderePda.visitaAndamento == null && !cliente.isClienteDefaultParaNovoPedido()) {
			if (LavenderePdaConfig.isBloqueiaNovoPedidoClienteSemRegistroChegada() || UiUtil.showConfirmYesNoMessage(CadClienteMenuForm.getRegistrarChegadaMessage(cliente.isNovoCliente()))) {
				Date dtAgendaAtual = agendaVisita != null ? agendaVisita.dtAgendaAtual : null;
				return CadClienteMenuForm.btRegistrarAtualizarChegadaClick(dtAgendaAtual);
			}
		}
		return true;
	}
	
	public static boolean mostraMensagemDeCadastroDeCoordenadasCliente() {
		if (LavenderePdaConfig.isUsaCadastroCoordenadasGeograficasSomenteMenuCliente()) {
			if (LavenderePdaConfig.obrigaCadCoordenadasGeograficas) {
				UiUtil.showErrorMessage(Messages.PEDIDO_CLIENTE_SEM_COORD_GEOGRAFICAS);
				return false;
			}
		} else {
			CadCoordenadasGeograficasWindow cadCoordenadasGeograficasWindow = new CadCoordenadasGeograficasWindow(SessionLavenderePda.getCliente(), false);
			cadCoordenadasGeograficasWindow.popup();
			if (LavenderePdaConfig.obrigaCadCoordenadasGeograficas && !cadCoordenadasGeograficasWindow.cadastrouCoordenada) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isCadastraCoordenada() throws SQLException {
		return LavenderePdaConfig.isUsaCadastroCoordenadasGeograficasCliente() && EnderecoGpsPdaService.getInstance().isCadastraCoordenada(SessionLavenderePda.getCliente());
	}
	
	public static boolean sugereNovoOrcamentoSemCheckInNaChegadaCliente() {
		if (SessionLavenderePda.visitaAndamento == null && LavenderePdaConfig.isBloqueiaNovoPedidoClienteSemRegistroChegada()) {
			return SessionLavenderePda.getCliente().somentePedidoPreOrcamento || sugereNovoOrcamentoSemCheckIn(null);
		}
		return false;
	}
	
	public static boolean sugereNovoOrcamentoSemCheckIn(final Cliente cliente) {
		if (LavenderePdaConfig.usaNovoPedidoOrcamentoSemRegistroChegada) {
			boolean somenteOrcamento = false;
			if (SessionLavenderePda.visitaAndamento != null) {
				Cliente clienteVisita = new Cliente();
				clienteVisita.cdEmpresa = SessionLavenderePda.visitaAndamento.cdEmpresa; 
				clienteVisita.cdRepresentante = SessionLavenderePda.visitaAndamento.cdRepresentante; 
				clienteVisita.cdCliente = SessionLavenderePda.visitaAndamento.cdCliente; 
				if (ValueUtil.valueNotEqualsIfNotNull(clienteVisita, cliente)) {
					somenteOrcamento = true;
				} else {
					return false;
				}
			}
			somenteOrcamento = somenteOrcamento || UiUtil.showConfirmMessage(Messages.MSG_REGISTRO_CHEGADA, Messages.MSG_CONTINUAR_COMO_ORCAMENTO_OU_PEDIDO, new String[] {Messages.BOTAO_NOVO_ORCAMENTO, Messages.BOTAO_NOVO_PEDIDO}) == 0;
			if (somenteOrcamento) {
				SessionLavenderePda.getCliente().somentePedidoPreOrcamento = true;
				return true;
			}
		}
		return false;
	}
}
