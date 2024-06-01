package util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.presentation.ui.VisitaUiUtil;

public class VisitaUiUtilTest {
	
	@Test
	public void deveDefinirClienteComoSomenteOrcamentoQuandoNecessario() {
		LavenderePdaConfig.usaNovoPedidoOrcamentoSemRegistroChegada = true;
		SessionLavenderePda.visitaAndamento = new Visita();
		SessionLavenderePda.visitaAndamento.cdEmpresa = "1";
		SessionLavenderePda.visitaAndamento.cdRepresentante = "10";
		SessionLavenderePda.visitaAndamento.cdCliente = "20";
		Cliente clienteSelecionado = new Cliente();
		clienteSelecionado.cdEmpresa = "1";
		clienteSelecionado.cdRepresentante = "10";
		clienteSelecionado.cdCliente = "20";
		SessionLavenderePda.setCliente(clienteSelecionado);
		
		boolean result = VisitaUiUtil.sugereNovoOrcamentoSemCheckIn(clienteSelecionado);
		assertFalse(result, "Quando est� tentando criar um pedido para um cliente que tem visita em andamento para ele, deve retornar falso para continuar o fluxo de um novo pedido comum.");
		assertFalse(SessionLavenderePda.getCliente().somentePedidoPreOrcamento, "Este cliente j� est� em visita em andamento, n�o faz sentido criar or�amento para ele em vez de pedido comum.");
		
		clienteSelecionado.cdCliente = "21";
		result = VisitaUiUtil.sugereNovoOrcamentoSemCheckIn(clienteSelecionado);
		assertTrue(result, "Quando est� tentando criar um pedido para um cliente onde h� outro cliente com visita em andamento, deve retornar verdadeiro para criar um pedido de or�amento.");
		assertTrue(SessionLavenderePda.getCliente().somentePedidoPreOrcamento, "O novo pedido para outro cliente fora da visita deve ser apenas 'Novo Or�amento'");
		
		LavenderePdaConfig.usaNovoPedidoOrcamentoSemRegistroChegada = false;
		SessionLavenderePda.getCliente().somentePedidoPreOrcamento = false;
		result = VisitaUiUtil.sugereNovoOrcamentoSemCheckIn(clienteSelecionado);
		assertFalse(result, "Deve ser falso pois a funcionalidade est� desativada");
		assertFalse(SessionLavenderePda.getCliente().somentePedidoPreOrcamento, "Deve ser falso pois a funcionalidade est� desligada");
	}

}
