package br.com.wmw.lavenderepda.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.BonifCfgFaixaQtde;
import br.com.wmw.test.method.MethodTestUtils;

public class BonifCfgFaixaQtdeServiceTestCase {

	@Test
	public void deveRetornarTresQuandoAtingirAFaixaQueConcedeMultiplos() {
		BonifCfgFaixaQtde bonifCfgQtd = createBonifCfgFaixaQtde(true);
		double qtItemFisicoVendida = 17; 
		double qtBonificacaoAtual = 0;
		boolean isFirstItemPedidoFromList = true;
		double result = MethodTestUtils.invokePrivateMethod(BonifCfgFaixaQtdeService.getInstance(), "getQtBonificacaoAtingida", bonifCfgQtd, qtItemFisicoVendida, qtBonificacaoAtual, isFirstItemPedidoFromList, false);
		assertEquals(3, result , "A cada 5 bonifica 1, então deve retonar 3.");
	}
	
	@Test
	public void deveRetornarUmQuandoAtingirAFaixaQueNaoConcedeMultiplos() {
		BonifCfgFaixaQtde bonifCfgQtd = createBonifCfgFaixaQtde(false);
		double qtItemFisicoVendida = 17; 
		double qtBonificacaoAtual = 0;
		boolean isFirstItemPedidoFromList = true;
		double result = MethodTestUtils.invokePrivateMethod(BonifCfgFaixaQtdeService.getInstance(), "getQtBonificacaoAtingida", bonifCfgQtd, qtItemFisicoVendida, qtBonificacaoAtual, isFirstItemPedidoFromList, false);
		assertEquals(1 , result , "Atingiu a Faixa de 5 vendidos, então deve retornar 1.");
	}
	
	@Test
	public void deveRetornarZeroQuandoNaoTrocarDeFaixaOuMultiploENaoSerOPrimeiroItemPedidoDaLista() {
		BonifCfgFaixaQtde bonifCfgQtd = createBonifCfgFaixaQtde(true);
		double qtItemFisicoVendida = 17; 
		double qtBonificacaoAtual = 3;
		boolean isFirstItemPedidoFromList = false;
		double result = MethodTestUtils.invokePrivateMethod(BonifCfgFaixaQtdeService.getInstance(), "getQtBonificacaoAtingida", bonifCfgQtd, qtItemFisicoVendida, qtBonificacaoAtual, isFirstItemPedidoFromList, false);
		assertEquals(0 , result , "Não houve troca de Faixa ou de Multiplo, logo, a quantidade retornada deve ser 0.");
	}
	
	@Test
	public void deveRetornarQuatroQuandoNaoTrocarDeFaixaOuMultiploMasSerOPrimeiroItemPedidoDaLista() {
		BonifCfgFaixaQtde bonifCfgQtd = createBonifCfgFaixaQtde(true);
		double qtItemFisicoVendida = 21; 
		double qtBonificacaoAtual = 4;
		boolean isFirstItemPedidoFromList = true;
		double result = MethodTestUtils.invokePrivateMethod(BonifCfgFaixaQtdeService.getInstance(), "getQtBonificacaoAtingida", bonifCfgQtd, qtItemFisicoVendida, qtBonificacaoAtual, isFirstItemPedidoFromList, false);
		assertEquals(qtBonificacaoAtual , result , "Não houve troca de faixa e nem de multiplo, porém o primeiro item pedido da lista precisa sobrescrever a faixa atual para controle");
	}
	
	private BonifCfgFaixaQtde createBonifCfgFaixaQtde(boolean isConcedeMultiplo) {
		BonifCfgFaixaQtde bonifCfgQtde = new BonifCfgFaixaQtde();
		bonifCfgQtde.cdEmpresa = "1";
		bonifCfgQtde.cdBonifCfg = "1";
		bonifCfgQtde.cdFaixaQtde = "1";
		bonifCfgQtde.qtVendida = 5;
		bonifCfgQtde.qtBonificada = 1;
		bonifCfgQtde.flConcedeMultiplos = isConcedeMultiplo ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
		
		return bonifCfgQtde;
	}
}
