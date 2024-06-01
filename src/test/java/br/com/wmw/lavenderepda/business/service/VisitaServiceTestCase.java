package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.util.DateUtil;
import static org.junit.jupiter.api.Assertions.*;
import br.com.wmw.lavenderepda.business.domain.Visita;
import org.junit.jupiter.api.Test;

public class VisitaServiceTestCase  {

	@Test
	public void testPermiteAtualizarRegistroSaida() {
		Visita ultimoRegistroChegadaDoRepresentante = new Visita();
		Visita ultimoRegistroSaidaDoCliente = new Visita();
		
		//Registro de chegada anterior ao registro de saída
		ultimoRegistroChegadaDoRepresentante.dtChegadaVisita = DateUtil.getDateValue(24, 02, 2016);
		ultimoRegistroChegadaDoRepresentante.hrChegadaVisita = "09:00"; 
		ultimoRegistroSaidaDoCliente.dtSaidaVisita = DateUtil.getDateValue(24, 02, 2016);
		ultimoRegistroSaidaDoCliente.hrSaidaVisita = "09:01";
		assertTrue(VisitaService.getInstance().permiteAtualizarRegistroSaida(ultimoRegistroChegadaDoRepresentante, ultimoRegistroSaidaDoCliente));
		
		
		//Registro de chegada anterior ao registro de saída
		ultimoRegistroChegadaDoRepresentante.dtChegadaVisita = DateUtil.getDateValue(23, 02, 2016);
		ultimoRegistroChegadaDoRepresentante.hrChegadaVisita = "09:00"; 
		ultimoRegistroSaidaDoCliente.dtSaidaVisita = DateUtil.getDateValue(24, 02, 2016);
		ultimoRegistroSaidaDoCliente.hrSaidaVisita = "09:01";
		assertTrue(VisitaService.getInstance().permiteAtualizarRegistroSaida(ultimoRegistroChegadaDoRepresentante, ultimoRegistroSaidaDoCliente));
		
		
		//Registro de chegada posterior ao registro de saída
		ultimoRegistroChegadaDoRepresentante.dtChegadaVisita = DateUtil.getDateValue(24, 02, 2016);
		ultimoRegistroChegadaDoRepresentante.hrChegadaVisita = "09:02"; 
		ultimoRegistroSaidaDoCliente.dtSaidaVisita = DateUtil.getDateValue(24, 02, 2016);
		ultimoRegistroSaidaDoCliente.hrSaidaVisita = "09:01";
		assertFalse(VisitaService.getInstance().permiteAtualizarRegistroSaida(ultimoRegistroChegadaDoRepresentante, ultimoRegistroSaidaDoCliente));
		
		
		//Registro de chegada posterior ao registro de saída
		ultimoRegistroChegadaDoRepresentante.dtChegadaVisita = DateUtil.getDateValue(25, 02, 2016);
		ultimoRegistroChegadaDoRepresentante.hrChegadaVisita = "09:01"; 
		ultimoRegistroSaidaDoCliente.dtSaidaVisita = DateUtil.getDateValue(24, 02, 2016);
		ultimoRegistroSaidaDoCliente.hrSaidaVisita = "09:01";
		assertFalse(VisitaService.getInstance().permiteAtualizarRegistroSaida(ultimoRegistroChegadaDoRepresentante, ultimoRegistroSaidaDoCliente));
		
		
		//Registro de chegada igual ao registro de saída
		ultimoRegistroChegadaDoRepresentante.dtChegadaVisita = DateUtil.getDateValue(24, 02, 2016);
		ultimoRegistroChegadaDoRepresentante.hrChegadaVisita = "09:01"; 
		ultimoRegistroSaidaDoCliente.dtSaidaVisita = DateUtil.getDateValue(24, 02, 2016);
		ultimoRegistroSaidaDoCliente.hrSaidaVisita = "09:01";
		assertTrue(VisitaService.getInstance().permiteAtualizarRegistroSaida(ultimoRegistroChegadaDoRepresentante, ultimoRegistroSaidaDoCliente));
	}

}
