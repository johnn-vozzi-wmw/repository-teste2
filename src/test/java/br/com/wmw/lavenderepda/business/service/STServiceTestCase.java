package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.util.ValueUtil;
import static org.junit.jupiter.api.Assertions.*;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.Tributacao;
import br.com.wmw.lavenderepda.business.domain.TributacaoConfig;
import br.com.wmw.lavenderepda.business.domain.TributacaoVlBase;
import org.junit.jupiter.api.Test;

public class STServiceTestCase  {

	@Test
	public void testCalculaIcmsPersonalizado() throws SQLException {
		TributacaoConfig tributacaoConfig = new TributacaoConfig();
		Tributacao tributacao = new Tributacao();
		ItemPedido itemPedido = new ItemPedido();
		Cliente cliente = new Cliente();
		Produto produto = new Produto();
		LavenderePdaConfig.nuCasasDecimaisPisCofinsIcms = 2;
		
		//Teste com flCalculaIcms = "N"
		tributacaoConfig.flCalculaIcms = ValueUtil.VALOR_NAO;
		tributacaoConfig.flFreteBaseIcms = ValueUtil.VALOR_NAO;
		tributacaoConfig.flFreteBaseIpi = ValueUtil.VALOR_NAO;
		tributacaoConfig.flTipoCalculoPisCofins = "2";
		cliente.nuInscricaoEstadual = "ISENTO";
		cliente.flDebitaPisCofinsZonaFranca = ValueUtil.VALOR_NAO;
		cliente.vlPctIcms = 7.00;
		produto.vlPctIcms = 12.00;
		tributacao.vlPctIcms = 17.00;
		itemPedido.vlTotalItemPedido = 30.50;
		itemPedido.vlTotalItemPedidoFrete = 1.50;
		itemPedido.vlIpiItem = 0.32;
		itemPedido.vlPis = 0.50;
		itemPedido.vlCofins = 0.50;
		itemPedido.setPedido(new Pedido());
		itemPedido.pedido.setCliente(cliente);
		itemPedido.setProduto(produto);
		itemPedido.setQtItemFisico(1);
		STService.getInstance().calculaIcmsPersonalizado(itemPedido, tributacaoConfig, tributacao, 50.00);
		assertEquals(0.0, itemPedido.vlIcms);
		
		//Teste com flCalculaIcms = "S" - Vai utilizar o vlPctIcms da tributação 
		tributacaoConfig.flCalculaIcms = ValueUtil.VALOR_SIM;
		STService.getInstance().calculaIcmsPersonalizado(itemPedido, tributacaoConfig, tributacao, 50.00);
		assertEquals(9.0, itemPedido.vlTotalIcmsItem, 2);
		
		//Teste quando não possuir vlPctIcms da Tributação. Nesse teste utilizará vlPctIcms do produto
		tributacao = null;
		STService.getInstance().calculaIcmsPersonalizado(itemPedido, tributacaoConfig, tributacao, 50.00);
		assertEquals(6.0, itemPedido.vlTotalIcmsItem, 2);
		
		//Teste utilizando o vlPctIcms do cliente
		cliente.nuInscricaoEstadual = "12345";
		STService.getInstance().calculaIcmsPersonalizado(itemPedido, tributacaoConfig, tributacao, 50.00);
		assertEquals(3.5, itemPedido.vlTotalIcmsItem, 2);
		
		//Teste utilizando como base de cálculo (vlItemPedido + vlItemFrete + viIpiItem - (vlPis + vlCofins)) 
		//Nesse exemplo só utilizará vlItemPedido para compor a base de cálculo . vlPctIcms do cliente
		tributacao = new Tributacao();
		STService.getInstance().calculaIcmsPersonalizado(itemPedido, tributacaoConfig, tributacao, 0);
		assertEquals(2.135, itemPedido.vlTotalIcmsItem, 2);
		
		//Teste aplicando vlItemFrete na base de cálculo
		tributacaoConfig.flFreteBaseIcms = ValueUtil.VALOR_SIM;
		STService.getInstance().calculaIcmsPersonalizado(itemPedido, tributacaoConfig, tributacao, 0);
		assertEquals(2.24, itemPedido.vlTotalIcmsItem, 2);
		
		//Teste aplicando vlIpi na base de cálculo
	    tributacaoConfig.flFreteBaseIpi = ValueUtil.VALOR_SIM;
		STService.getInstance().calculaIcmsPersonalizado(itemPedido, tributacaoConfig, tributacao, 0);
		assertEquals(2.26, itemPedido.vlTotalIcmsItem, 2);
		
		//Teste aplicando vlPis e vlCofins na base de cálculo
		cliente.flDebitaPisCofinsZonaFranca = ValueUtil.VALOR_SIM;
		STService.getInstance().calculaIcmsPersonalizado(itemPedido, tributacaoConfig, tributacao, 0);
		assertEquals(2.2, itemPedido.vlTotalIcmsItem, 2);
		
		//Teste aplicando percentual de Repasse
		tributacao.flBaseIcmsRetidoComRepasse = ValueUtil.VALOR_SIM;
		tributacao.vlPctRepasseIcms = 0.55;
		STService.getInstance().calculaIcmsPersonalizado(itemPedido, tributacaoConfig, tributacao, 0);
		assertEquals(2.23, itemPedido.vlTotalIcmsItem, 0);
		
		//Teste aplicando percentual de Redução Base
		tributacao.vlPctReducaoBaseIcms = 2.5;
		STService.getInstance().calculaIcmsPersonalizado(itemPedido, tributacaoConfig, tributacao, 0);
		assertEquals(2.17, itemPedido.vlTotalIcmsItem, 0);
		
		//Teste aplicando percentual de Redução ICMS
		tributacao.vlPctReducaoIcms = 1.25;
		STService.getInstance().calculaIcmsPersonalizado(itemPedido, tributacaoConfig, tributacao, 0);
		assertEquals(2.14, itemPedido.vlTotalIcmsItem, 1);
	}

	@Test
	public void testeAplicaSTItemPedidoPersonalizado() throws SQLException {
		TributacaoConfig tributacaoConfig = new TributacaoConfig();
		TributacaoVlBase tributacaoVlBase = new TributacaoVlBase();
		Tributacao tributacao = new Tributacao();
		ItemPedido itemPedido = new ItemPedido();
		LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado = true;
		LavenderePdaConfig.nuCasasDecimaisVlStVlIpi = 2;
		Pedido pedido = new Pedido();
		pedido.setCondicaoPagamento(new CondicaoPagamento());
		itemPedido.pedido = pedido;
		
		//Teste com flCalculaSt = "N"
		tributacaoConfig.flCalculaSt = ValueUtil.VALOR_NAO;
		tributacaoConfig.flFreteBaseSt = ValueUtil.VALOR_NAO;
		tributacaoConfig.flIpiBaseSt = ValueUtil.VALOR_NAO;
		tributacao.vlPctIcmsRetido = 18.00;
		tributacao.vlPctMargemAgregada = 35.00;
		itemPedido.setQtItemFisico(1);
		itemPedido.vlTotalItemPedido = 30.50;
		itemPedido.vlTotalItemPedidoFrete = 1.50;
		itemPedido.vlIpiItem = 0.32;
		STService.getInstance().aplicaSTItemPedidoPersonalizado(itemPedido, tributacaoConfig, tributacao, tributacaoVlBase, 70.14);
		assertEquals(0.0, itemPedido.vlTotalStItem);
		
		//Teste com flCalculaSt = "S"
		tributacaoConfig.flCalculaSt = ValueUtil.VALOR_SIM;
		STService.getInstance().aplicaSTItemPedidoPersonalizado(itemPedido, tributacaoConfig, tributacao, tributacaoVlBase,  70.14);
		assertEquals(13.00, itemPedido.vlTotalStItem, 2);
		
		//Teste utilizando como base de cálculo (vlItemPedido + vlItemFrete + viIpiItem  + Percentual de MVA) 
		//Nesse exemplo só utilizará vlItemPedido e  MVA para compor a base de cálculo.
		STService.getInstance().aplicaSTItemPedidoPersonalizado(itemPedido, tributacaoConfig, tributacao, tributacaoVlBase, 0);
		assertEquals(7.4115, itemPedido.vlTotalStItem, 2);
		
		//Teste aplicando vlItemFrete na base de cálculo
		tributacaoConfig.flFreteBaseSt = ValueUtil.VALOR_SIM;
		STService.getInstance().aplicaSTItemPedidoPersonalizado(itemPedido, tributacaoConfig, tributacao, tributacaoVlBase, 0);
		assertEquals(7.776, itemPedido.vlTotalStItem, 2);
		
		//Teste aplicando vlIpi na base de cálculo
		tributacaoConfig.flIpiBaseSt = ValueUtil.VALOR_SIM;
		STService.getInstance().aplicaSTItemPedidoPersonalizado(itemPedido, tributacaoConfig, tributacao, tributacaoVlBase, 0);
		assertEquals(7.853760000000001, itemPedido.vlTotalStItem, 2);
		
		//Teste aplicando percentual de Repasse
		tributacao.flBaseIcmsRetidoComRepasse = ValueUtil.VALOR_SIM;
		tributacao.vlPctRepasseIcms = 0.55;
		STService.getInstance().aplicaSTItemPedidoPersonalizado(itemPedido, tributacaoConfig, tributacao, tributacaoVlBase, 0);
		assertEquals(7.73, itemPedido.vlTotalStItem, 0);
		
		//Teste aplicando percentual de Redução Base
		tributacao.vlPctReducaoBaseIcmsRetido = 2.5;
		STService.getInstance().aplicaSTItemPedidoPersonalizado(itemPedido, tributacaoConfig, tributacao, tributacaoVlBase, 0);
		assertEquals(7.54, itemPedido.vlTotalStItem, 0);
	}

	@Test
	public void testeAplicaSTItemPedidoPersonalizadoSt2() throws SQLException {
		TributacaoConfig tributacaoConfig = new TributacaoConfig();
		TributacaoVlBase tributacaoVlBase = new TributacaoVlBase();
		Tributacao tributacao = new Tributacao();
		ItemPedido itemPedido = new ItemPedido();
		LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado = true;
		LavenderePdaConfig.nuCasasDecimaisVlStVlIpi = 2;
		Pedido pedido = new Pedido();
		pedido.setCondicaoPagamento(new CondicaoPagamento());
		itemPedido.pedido = pedido;

		//Teste com flCalculaSt = "N"
		tributacaoConfig.flCalculaSt = ValueUtil.VALOR_NAO;
		tributacaoConfig.flFreteBaseSt = ValueUtil.VALOR_NAO;
		tributacaoConfig.flIpiBaseSt = ValueUtil.VALOR_NAO;
		tributacao.vlPctIcmsRetido = 18.0;
		tributacao.vlPctMargemAgregada = 140.0;
		itemPedido.setQtItemFisico(1);
		itemPedido.vlTotalItemPedido = 1.5;
		itemPedido.vlTotalItemPedidoFrete = 0;
		itemPedido.vlIpiItem = 0.06;
		STService.getInstance().aplicaSTItemPedidoPersonalizado(itemPedido, tributacaoConfig, tributacao, tributacaoVlBase,  30.50);
		assertEquals(0.0, itemPedido.vlTotalStItem);

		//Teste com flCalculaSt = "2"
		tributacaoConfig.flCalculaIcms = ValueUtil.VALOR_SIM;
		tributacaoConfig.flCalculaIpi = ValueUtil.VALOR_SIM;
		tributacaoConfig.flCalculaSt = "2";
		tributacaoVlBase.vlBaseIcmsRetidoCalcRetido = 2.38;
		itemPedido.vlIcms = 0.36;
		itemPedido.setQtItemFisico(2);
		STService.getInstance().aplicaSTItemPedidoPersonalizado(itemPedido, tributacaoConfig, tributacao, tributacaoVlBase,  2.38);
		assertEquals(0.86, itemPedido.vlTotalStItem, 0);

		//Teste com flCalculaSt = "2" (2)
		itemPedido.setQtItemFisico(1);
		tributacaoVlBase.vlBaseIcmsRetidoCalcRetido = 1.07;
		itemPedido.vlTotalIpiItem = 0.06;
		itemPedido.vlIcms = 0.18;
		STService.getInstance().aplicaSTItemPedidoPersonalizado(itemPedido, tributacaoConfig, tributacao, tributacaoVlBase, 1.07);
		assertEquals(0.67, itemPedido.vlTotalStItem, 0);

		//Teste com flCalculaSt = "2" (3)
		itemPedido.setQtItemFisico(2);
		itemPedido.vlTotalItemPedido = 3;
		itemPedido.vlTotalIpiItem = 0.00;
		tributacaoVlBase.vlBaseIcmsRetidoCalcRetido = 0;
		itemPedido.vlIcms = 0.36;
		STService.getInstance().aplicaSTItemPedidoPersonalizado(itemPedido, tributacaoConfig, tributacao, tributacaoVlBase, 0);
		assertEquals(1.3, itemPedido.vlTotalStItem, 0);
	}

	@Test
	public void testeAplicaSTItemPedidoNormal() throws SQLException {
		//STReverso
		
		LavenderePdaConfig.usaUnidadeAlternativa = false;
		LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao = true;
		LavenderePdaConfig.nuCasasDecimaisVlStVlIpi = 5;
		LavenderePdaConfig.usaCalculoReversoNaST = true;
		LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos = false;
		LavenderePdaConfig.configCalculaStItemPedido = "{ \"usa\":\"S\", \"PMPFSobrevlBaseRetido\":\"N\" }";
				
		Tributacao tributacao = new Tributacao();
		tributacao.cdEmpresa = "1";
		tributacao.cdRepresentante = "848";
		tributacao.cdTipoPedido = "0";
		tributacao.cdTipoRecolhimento = 2;
		tributacao.cdTributacaoCliente = "0";
		tributacao.cdTributacaoProduto = "10007";
		tributacao.cdUf = "0";
		tributacao.flBaseIcmsRetidoComDesconto = "N";
		tributacao.flBaseIcmsRetidoComRepasse = "N";
		tributacao.flMedicamento = "N";
		tributacao.vlPctIcms = 18;
		tributacao.vlPctIcmsRetido = 18;
		tributacao.vlPctReducaoBaseIcms = 61.11;
		tributacao.vlPctReducaoBaseIcmsRetido = 61.11;
		
		TributacaoVlBase tributacaoVlBase = new TributacaoVlBase();
		tributacaoVlBase.cdEmpresa = "1";
		tributacaoVlBase.cdRepresentante = "848";
		tributacaoVlBase.cdProduto = "610150";
		tributacaoVlBase.cdTributacaoCliente = "0";
		tributacaoVlBase.cdTributacaoProduto = "10007";
		tributacaoVlBase.vlBaseIcmsRetidoCalcRetido = 4.19;
		
		ItemPedido itemPedido = new ItemPedido();
		Cliente cliente = new Cliente();
		Produto produto = new Produto();
		LavenderePdaConfig.nuCasasDecimaisPisCofinsIcms = 2;
		
		itemPedido.vlIcms = 0.22469;
		itemPedido.vlItemPedido = 3.21;
		itemPedido.vlBaseItemPedido = 5.22;
		itemPedido.vlBaseItemTabelaPreco = 5.22;
		itemPedido.vlTotalItemPedido = 5.22;
		itemPedido.vlItemPedidoStBase = 5.22;
		itemPedido.vlItemPedidoStReverso = 3.21;
		itemPedido.setPedido(new Pedido());
		itemPedido.pedido.setCliente(cliente);
		itemPedido.setProduto(produto);
		itemPedido.setQtItemFisico(1);
		itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_VLITEMST;
		
		STService.getInstance().aplicaSTItemPedidoNormal(itemPedido, tributacao, tributacaoVlBase);
		System.out.println("valor da st: " + itemPedido.vlSt);
		assertEquals(0.07377, itemPedido.vlSt, 0);
	}
	
	@Test
	void testeValorBaseStComPMPFMenorQueVlBaseRetidoCalcRetido() throws SQLException {
		LavenderePdaConfig.configCalculaStItemPedido = "{ \"usa\":\"S\", \"PMPFSobrevlBaseRetido\":\"S\" }";
		Tributacao tributacao = criaTributacaoParaTestePMPF();
		TributacaoVlBase tributacaoVlBase = criaTributacaoVlBaseParaTestePMPF();
		
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.vlItemPedido = 32.54;
		tributacaoVlBase.vlPrecoMedioPonderado = 44.14;
		
		double vlBaseSt = tributacaoVlBase.getVlBaseIcmsRetidoCalcRetido(tributacao.vlPctDiferenca, itemPedido.vlItemPedido, tributacao.vlPctMargemAgregada);
		assertEquals(tributacaoVlBase.vlBaseIcmsRetidoCalcRetido, vlBaseSt, 0);
	}
	
	@Test
	void testeValorBaseStComPMPFMaiorQueVlBaseRetidoCalcRetido() throws SQLException {
		LavenderePdaConfig.configCalculaStItemPedido = "{ \"usa\":\"S\", \"PMPFSobrevlBaseRetido\":\"S\" }";
		Tributacao tributacao = criaTributacaoParaTestePMPF();
		TributacaoVlBase tributacaoVlBase = criaTributacaoVlBaseParaTestePMPF();
		
		ItemPedido itemPedido = new ItemPedido();
		
		itemPedido.vlItemPedido = 32.54;
		tributacaoVlBase.vlPrecoMedioPonderado = 22.14;
		
		double vlBaseSt = tributacaoVlBase.getVlBaseIcmsRetidoCalcRetido(0, itemPedido.vlItemPedido, tributacao.vlPctMargemAgregada);
		assertEquals(43.31, vlBaseSt, 0);
	}
	
	
	@Test
	void testeValorBaseStSemPMPFVlBaseMaiorQueCalculoMargemAgregada() throws SQLException {
		LavenderePdaConfig.configCalculaStItemPedido = "{ \"usa\":\"S\", \"PMPFSobrevlBaseRetido\":\"S\" }";
		Tributacao tributacao = criaTributacaoParaTestePMPF();
		TributacaoVlBase tributacaoVlBase = criaTributacaoVlBaseParaTestePMPF();
		
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.vlItemPedido = 32.54;
		
		double vlBaseSt = tributacaoVlBase.getVlBaseIcmsRetidoCalcRetido(tributacao.vlPctDiferenca, itemPedido.vlItemPedido, tributacao.vlPctMargemAgregada);
		assertEquals(43.31, vlBaseSt, 0);
	}
	
	@Test
	void testeValorBaseStSemPMPFVlBaseMenorQueCalculoMargemAgregada() throws SQLException {
		LavenderePdaConfig.configCalculaStItemPedido = "{ \"usa\":\"S\", \"PMPFSobrevlBaseRetido\":\"S\" }";
		Tributacao tributacao = criaTributacaoParaTestePMPF();
		TributacaoVlBase tributacaoVlBase = criaTributacaoVlBaseParaTestePMPF();
		
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.vlItemPedido = 53.88;
		
		double vlBaseSt = tributacaoVlBase.getVlBaseIcmsRetidoCalcRetido(tributacao.vlPctDiferenca, itemPedido.vlItemPedido, tributacao.vlPctMargemAgregada);
		assertEquals(tributacaoVlBase.vlBaseIcmsRetidoCalcRetido, vlBaseSt, 0);
	}

	private TributacaoVlBase criaTributacaoVlBaseParaTestePMPF() {
		TributacaoVlBase tributacaoVlBase = new TributacaoVlBase();
		tributacaoVlBase.vlBaseIcmsRetidoCalcRetido = 43.35;
		return tributacaoVlBase;
	}

	private Tributacao criaTributacaoParaTestePMPF() {
		Tributacao tributacao = new Tributacao();
		tributacao.vlPctDiferenca = 0.95;
		tributacao.vlPctMargemAgregada = 33.11 ;
		return tributacao;
	}

}
