package br.com.wmw.lavenderepda.business.service;

import static org.junit.jupiter.api.Assertions.*;

import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AgendaVisita;
import org.junit.jupiter.api.Test;
import totalcross.util.Date;
import totalcross.util.InvalidDateException;

public class AgendaVisitaServiceTestCase  {

	@Test
	public void testVerifyFrequenciaAgenda() throws InvalidDateException {
		AgendaVisita agendaVisita = new AgendaVisita(); 
		agendaVisita.dtBase = new Date(16, 6, 2016);
		
		//--Frequencia: quinzenal
		agendaVisita.dtAgendaAtual = new Date(16, 6, 2016);
		agendaVisita.flTipoFrequencia = AgendaVisita.FLTIPOFREQUENCIA_QUINZENAL;
		assertTrue(AgendaVisitaService.getInstance().verifyFrequenciaAgenda(agendaVisita, agendaVisita.dtAgendaAtual));
		//--1ª semana
		agendaVisita.dtAgendaAtual = new Date(23, 6, 2016);
		assertFalse(AgendaVisitaService.getInstance().verifyFrequenciaAgenda(agendaVisita, agendaVisita.dtAgendaAtual));
		//--2ª semana
		agendaVisita.dtAgendaAtual = new Date(30, 6, 2016);
		assertTrue(AgendaVisitaService.getInstance().verifyFrequenciaAgenda(agendaVisita, agendaVisita.dtAgendaAtual));
		//--3ª semana
		agendaVisita.dtAgendaAtual = new Date(7, 7, 2016);
		assertFalse(AgendaVisitaService.getInstance().verifyFrequenciaAgenda(agendaVisita, agendaVisita.dtAgendaAtual));
		//--4ª semana
		agendaVisita.dtAgendaAtual = new Date(14, 7, 2016);
		assertTrue(AgendaVisitaService.getInstance().verifyFrequenciaAgenda(agendaVisita, agendaVisita.dtAgendaAtual));
		
		//--Frequencia: mensal
		agendaVisita.dtAgendaAtual = new Date(16, 6, 2016);
		agendaVisita.flTipoFrequencia = AgendaVisita.FLTIPOFREQUENCIA_MENSAL;
		assertTrue(AgendaVisitaService.getInstance().verifyFrequenciaAgenda(agendaVisita, agendaVisita.dtAgendaAtual));
		//--1ª semana
		agendaVisita.dtAgendaAtual = new Date(23, 6, 2016);
		assertFalse(AgendaVisitaService.getInstance().verifyFrequenciaAgenda(agendaVisita, agendaVisita.dtAgendaAtual));
		//--2ª semana
		agendaVisita.dtAgendaAtual = new Date(30, 6, 2016);
		assertFalse(AgendaVisitaService.getInstance().verifyFrequenciaAgenda(agendaVisita, agendaVisita.dtAgendaAtual));
		//--3ª semana
		agendaVisita.dtAgendaAtual = new Date(7, 7, 2016);
		assertFalse(AgendaVisitaService.getInstance().verifyFrequenciaAgenda(agendaVisita, agendaVisita.dtAgendaAtual));
		//--4ª semana
		agendaVisita.dtAgendaAtual = new Date(14, 7, 2016);
		assertTrue(AgendaVisitaService.getInstance().verifyFrequenciaAgenda(agendaVisita, agendaVisita.dtAgendaAtual));
		
		//--Frequencia: bimestral
		agendaVisita.dtAgendaAtual = new Date(16, 6, 2016);
		agendaVisita.flTipoFrequencia = AgendaVisita.FLTIPOFREQUENCIA_BIMESTRAL;
		assertTrue(AgendaVisitaService.getInstance().verifyFrequenciaAgenda(agendaVisita, agendaVisita.dtAgendaAtual));
		//--1º mes
		agendaVisita.dtAgendaAtual = new Date(14, 7, 2016);
		assertFalse(AgendaVisitaService.getInstance().verifyFrequenciaAgenda(agendaVisita, agendaVisita.dtAgendaAtual));
		//--2º mes
		agendaVisita.dtAgendaAtual = new Date(18, 8, 2016);
		assertTrue(AgendaVisitaService.getInstance().verifyFrequenciaAgenda(agendaVisita, agendaVisita.dtAgendaAtual));
		//--3º mes
		agendaVisita.dtAgendaAtual = new Date(15, 9, 2016);
		assertFalse(AgendaVisitaService.getInstance().verifyFrequenciaAgenda(agendaVisita, agendaVisita.dtAgendaAtual));
		//--4º mes
		agendaVisita.dtAgendaAtual = new Date(20, 10, 2016);
		assertTrue(AgendaVisitaService.getInstance().verifyFrequenciaAgenda(agendaVisita, agendaVisita.dtAgendaAtual));
		
		agendaVisita.dtAgendaAtual = null;
		assertFalse(AgendaVisitaService.getInstance().verifyFrequenciaAgenda(agendaVisita, agendaVisita.dtAgendaAtual));
		
		agendaVisita.dtBase = null;
		assertFalse(AgendaVisitaService.getInstance().verifyFrequenciaAgenda(agendaVisita, agendaVisita.dtAgendaAtual));
		
		LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita = true;
		agendaVisita.dtAgenda = new Date(20, 10, 2016);
		assertFalse(AgendaVisitaService.getInstance().verifyFrequenciaAgenda(agendaVisita, agendaVisita.dtAgendaAtual));
		LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita = false;
		
	}
	
	@Test
	public void devePassarValidacaoCasoDtAgendaEDtBaseForemIguais() {
		AgendaVisita agendaVisita = new AgendaVisita();
		agendaVisita.dtAgenda = DateUtil.toDate("2022-01-01");
		agendaVisita.dtBase = DateUtil.toDate("2022-01-01");
		assertTrue(AgendaVisitaService.getInstance().validaDtAgenda(agendaVisita, null));
	}
	
	@Test
	public void devePassarValidacaoCasoRecebaFiltroDtAgendaPorParametro() {
		Date dataAgenda = DateUtil.toDate("2022-01-01");
		AgendaVisita agendaVisita = new AgendaVisita();
		agendaVisita.dtBase = DateUtil.toDate("2022-01-01");
		assertTrue(AgendaVisitaService.getInstance().validaDtAgenda(agendaVisita, dataAgenda));
	}
	
	@Test
	public void deveFalharValidacaoSeDiaForAntesQueDtBase() {
		Date dataAgenda = DateUtil.toDate("2022-01-01");
		AgendaVisita agendaVisita = new AgendaVisita();
		agendaVisita.dtAgenda = DateUtil.toDate("2022-01-01");
		agendaVisita.dtBase = DateUtil.toDate("2022-01-02");
		assertFalse(AgendaVisitaService.getInstance().validaDtAgenda(agendaVisita, dataAgenda));
	}
	
	@Test
	public void devePassarValidacaoCasoDtBaseForNullEValidarDtCriacao() {
		AgendaVisita agendaVisita = new AgendaVisita();
		agendaVisita.dtCriacao= DateUtil.toDate("2022-01-02");
		agendaVisita.dtAgenda = DateUtil.toDate("2022-01-02");
		assertTrue(AgendaVisitaService.getInstance().validaDtAgenda(agendaVisita, null));
	}

}
