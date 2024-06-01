package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.util.DateUtil;
import static org.junit.jupiter.api.Assertions.*;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import org.junit.jupiter.api.Test;
import totalcross.util.Date;

public class RotaEntregaServiceTestCase  {

	@Test
	public void testSugestaoDataEntregaBaseadaEmUmaRota() throws SQLException {
		
		RotaEntregaService rotaEntregaService = RotaEntregaService.getInstance();
		
		Date dataEntrega = DateUtil.getDateValue(1, 2, 2018);
		
		//teste com parâmetro usaRotaDeEntregaNoPedidoComCadastro diferente de 4
		LavenderePdaConfig.usaRotaDeEntregaNoPedidoComCadastro = "1";
		assertEquals(dataEntrega, rotaEntregaService.getSugestaoDataEntregaBaseadaEmDiaDisponivel(DateUtil.getDateValue(1, 2, 2018), null));
		
		//teste com parâmetro usaRotaDeEntregaNoPedidoComCadastro = 4 e nuDiasPrevisaoEntrega = 0
		LavenderePdaConfig.usaRotaDeEntregaNoPedidoComCadastro = "4";
		LavenderePdaConfig.nuDiasPrevisaoEntrega = 0;
		assertEquals(dataEntrega, rotaEntregaService.getSugestaoDataEntregaBaseadaEmDiaDisponivel(DateUtil.getDateValue(1, 2, 2018), null));
		
	}

	@Test
	public void testGetDataEntregaDisponivel() throws SQLException {
		RotaEntregaService rotaEntregaService = RotaEntregaService.getInstance();
		
		//teste com dias menor que 7 dias, não deve entrar na regra
		String diasEntregaRota = "N S N";
		assertEquals(DateUtil.getDateValue(7, 2, 2018), rotaEntregaService.getDataEntregaDisponivel(DateUtil.getDateValue(7, 2, 2018), diasEntregaRota));
		
		//teste sem nenhum dia valido
		diasEntregaRota = "N N N N N N N";
		assertEquals(DateUtil.getDateValue(7, 2, 2018), rotaEntregaService.getDataEntregaDisponivel(DateUtil.getDateValue(7, 2, 2018), diasEntregaRota));
		
		//teste com informações inconsistentes
		diasEntregaRota = "N N N N V X N";
		assertEquals(DateUtil.getDateValue(7, 2, 2018), rotaEntregaService.getDataEntregaDisponivel(DateUtil.getDateValue(7, 2, 2018), diasEntregaRota));
		
		
		//teste entrega nas segundas, quartas e sextas
		diasEntregaRota = "N S N S N S N";
		
		assertEquals(DateUtil.getDateValue(7, 2, 2018), rotaEntregaService.getDataEntregaDisponivel(DateUtil.getDateValue(7, 2, 2018), diasEntregaRota));
		
		assertEquals(DateUtil.getDateValue(9, 2, 2018), rotaEntregaService.getDataEntregaDisponivel(DateUtil.getDateValue(8, 2, 2018), diasEntregaRota));
		
		assertEquals(DateUtil.getDateValue(16, 11, 2018), rotaEntregaService.getDataEntregaDisponivel(DateUtil.getDateValue(16, 11, 2018), diasEntregaRota));
		
		assertEquals(DateUtil.getDateValue(19, 11, 2018), rotaEntregaService.getDataEntregaDisponivel(DateUtil.getDateValue(17, 11, 2018), diasEntregaRota));
		
	}

}
