package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.ParcelaPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.UsuarioPdaRep;
import org.junit.jupiter.api.Test;
import totalcross.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

public class ParcelaPedidoServiceTestCase  {

	@Test
	public void testValidateParcelasPercentuais() throws SQLException {
		Pedido pedido = new Pedido("TBLVPPEDIDO");
		pedido.setCondicaoPagamento(new CondicaoPagamento());
		pedido.getCondicaoPagamento().cdEmpresa = "1";
		pedido.getCondicaoPagamento().cdRepresentante = "1";
		pedido.getCondicaoPagamento().cdCondicaoPagamento = "1";
		pedido.getCondicaoPagamento().cdTipoCondPagto = "2";
		pedido.getCondicaoPagamento().dsCondicaoPagamento = "-";
		pedido.cdCondicaoPagamento = "1";
		SessionLavenderePda.usuarioPdaRep = new UsuarioPdaRep();
		SessionLavenderePda.usuarioPdaRep.cdUsuario = "1";
		// Pedido com condição de pagamento do tipo errado
		try {
			ParcelaPedidoService.getInstance().validateParcelasPercentualTotal(pedido);
			fail("Sistema não pode permitir um pedido com condição de pagamento do tipo errado.");
		} catch (ValidationException ex) {
			// Ok
		}
		pedido.getCondicaoPagamento().cdTipoCondPagto = "9";
		// Vazio
		pedido.parcelaPedidoList = new Vector();
		try {
			ParcelaPedidoService.getInstance().validateParcelasPercentualTotal(pedido);
			fail("Sistema não pode permitir um pedido sem parcelas.");
		} catch (ValidationException ex) {
			// Ok
		}
		// Parcela com porcentagem acima de 100%
		ParcelaPedido parcelaPedido = new ParcelaPedido();
		parcelaPedido.vlPctParcela = 150;
		pedido.parcelaPedidoList.addElement(parcelaPedido);
		try {
			ParcelaPedidoService.getInstance().validateParcelasPercentualTotal(pedido);
			fail("Sistema não deve permitir que as parcelas em conjunto tenham um percentual maior que 100");
		} catch (ValidationException ex) {
			// Ok
		}
		// Parcela com porcentagem abaixo de 100%
		pedido.parcelaPedidoList.removeAllElements();
		parcelaPedido.vlPctParcela = 90;
		pedido.parcelaPedidoList.addElement(parcelaPedido);
		try {
			ParcelaPedidoService.getInstance().validateParcelasPercentualTotal(pedido);
			fail("Sistema não deve permitir que as parcelas em conjunto tenham um percentual menor que 100");
		} catch (ValidationException ex) {
			// Ok
		}
		// Parcela com porcentagem de 100%
		pedido.parcelaPedidoList.removeAllElements();
		parcelaPedido.vlPctParcela = 100;
		pedido.parcelaPedidoList.addElement(parcelaPedido);
		try {
			ParcelaPedidoService.getInstance().validateParcelasPercentualTotal(pedido);
		} catch (ValidationException ex) {
			fail("Sistema deve permitir que as parcelas em conjunto tenham um percentual de 100");
		}
	}

	@Test
	public void testGetVlParcelas() {
		double vlTotal = 200d;
		int nuParcelas = 3;

		double[] parcelas = ParcelaPedidoService.getInstance().getVlParcelas(vlTotal, nuParcelas);
		assertEquals(3, parcelas.length);
		assertEquals(66.66, parcelas[0], 0.00);
		assertEquals(66.66, parcelas[1], 0.00);
		assertEquals(66.68, parcelas[2], 0.00);

		nuParcelas = 1;
		parcelas = ParcelaPedidoService.getInstance().getVlParcelas(vlTotal, nuParcelas);
		assertEquals(1, parcelas.length);
		assertEquals(200d, parcelas[0], 0.00);

		nuParcelas = 0;
		parcelas = ParcelaPedidoService.getInstance().getVlParcelas(vlTotal, nuParcelas);
		assertEquals(1, parcelas.length);
		assertEquals(200d, parcelas[0], 0.00);

		vlTotal = 69.69;
		nuParcelas = 4;
		parcelas = ParcelaPedidoService.getInstance().getVlParcelas(vlTotal, nuParcelas);
		assertEquals(4, parcelas.length);
		assertEquals(17.43, parcelas[3], 0.00);

		vlTotal = 111.075;
		nuParcelas = 6;
		parcelas = ParcelaPedidoService.getInstance().getVlParcelas(vlTotal, nuParcelas);
		assertEquals(6, parcelas.length);
		assertEquals(18.53, parcelas[5], 0.00);


	}

}
