package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.util.DateUtil;
import static org.junit.jupiter.api.Assertions.*;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ColetaGps;
import org.junit.jupiter.api.Test;

public class ColetaGpsServiceTestCase  {

	@Test
	public void testIsDeveEncerrarAutomaticamente() {
		LavenderePdaConfig.usaHorarioLimiteColetaGpsManual = "19:00";
		String hrAtual = "19:30:00";
		ColetaGps coletaGps = new ColetaGps();
		coletaGps.dtColetaGps = DateUtil.getDateValue(20, 12, 2016);
		coletaGps.hrInicioColeta = "18:59:59";
		assertTrue(ColetaGpsService.getInstance().isDeveEncerrarAutomaticamente(coletaGps, hrAtual));
		
		coletaGps.hrInicioColeta = "19:00:00";
		assertFalse(ColetaGpsService.getInstance().isDeveEncerrarAutomaticamente(coletaGps, hrAtual));
		
		coletaGps.hrInicioColeta = "19:00:01";
		assertFalse(ColetaGpsService.getInstance().isDeveEncerrarAutomaticamente(coletaGps, hrAtual));
		
		coletaGps.hrInicioColeta = "19:01:00";
		assertFalse(ColetaGpsService.getInstance().isDeveEncerrarAutomaticamente(coletaGps, hrAtual));
		
		coletaGps.hrInicioColeta = "20:00:00";
		assertFalse(ColetaGpsService.getInstance().isDeveEncerrarAutomaticamente(coletaGps, hrAtual));
		
		coletaGps.hrInicioColeta = "00:00:00";
		assertTrue(ColetaGpsService.getInstance().isDeveEncerrarAutomaticamente(coletaGps, hrAtual));
		
		coletaGps.dtColetaGps = DateUtil.getDateValue(21, 12, 2016);
		coletaGps.hrInicioColeta = "18:59:59";
		assertTrue(ColetaGpsService.getInstance().isDeveEncerrarAutomaticamente(coletaGps, hrAtual));
		
		coletaGps.hrInicioColeta = "19:00:01";
		assertFalse(ColetaGpsService.getInstance().isDeveEncerrarAutomaticamente(coletaGps, hrAtual));
		
		coletaGps.hrInicioColeta = "00:00:00";
		assertTrue(ColetaGpsService.getInstance().isDeveEncerrarAutomaticamente(coletaGps, hrAtual));
		
		coletaGps.dtColetaGps = DateUtil.getDateValue(22, 12, 2016);
		coletaGps.hrInicioColeta = "08:00:00";
		assertTrue(ColetaGpsService.getInstance().isDeveEncerrarAutomaticamente(coletaGps, hrAtual));
		
		coletaGps.hrInicioColeta = "19:00:01";
		assertFalse(ColetaGpsService.getInstance().isDeveEncerrarAutomaticamente(coletaGps, hrAtual));
		
	}

}
