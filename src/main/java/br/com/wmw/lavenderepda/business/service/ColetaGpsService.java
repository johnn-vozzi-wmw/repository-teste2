package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ColetaGps;
import br.com.wmw.lavenderepda.business.domain.MotivoColeta;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ColetaGpsDbxDao;
import br.com.wmw.lavenderepda.thread.EnviaDadosThread;
import totalcross.sys.Time;
import totalcross.util.Vector;

public class ColetaGpsService extends CrudService {

    private static ColetaGpsService instance;
    
    private ColetaGpsService() {
        //--
    }
    
    public static ColetaGpsService getInstance() {
        if (instance == null) {
            instance = new ColetaGpsService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ColetaGpsDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    	//--
    }
    
    public void iniciaColetaGps() throws SQLException {
    	iniciaColetaGps(TimeUtil.getCurrentTimeHHMMSS(), null);
    }
    
    public void iniciaColetaGps(String hrInicioColeta, String hrFimColeta) throws SQLException {
    	ColetaGps coletaGps = new ColetaGps();
    	coletaGps.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	coletaGps.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	coletaGps.dtColetaGps = DateUtil.getCurrentDate();
    	coletaGps.hrInicioColeta = hrInicioColeta;
    	coletaGps.hrFimColeta = hrFimColeta;
    	insert(coletaGps);
    }
    
    
    public void paraColetaGps(ColetaGps coletaGpsEmAndamento) throws SQLException {
    	if (coletaGpsEmAndamento != null) {
    		coletaGpsEmAndamento.hrFimColeta = TimeUtil.getCurrentTimeHHMMSS();
    		update(coletaGpsEmAndamento);
    	}
    }
    
    public ColetaGps getLastColetaGpsEmAndamento() throws SQLException {
    	ColetaGps coletaGpsFilter = new ColetaGps();
    	coletaGpsFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	coletaGpsFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	coletaGpsFilter.onlyColetaGpsEmAndamento = true;
    	coletaGpsFilter.sortAtributte = ColetaGps.NMCOLUNA_DTCOLETAGPS + "," + ColetaGps.NMCOLUNA_HRINICIOCOLETA;
    	coletaGpsFilter.sortAsc = ValueUtil.VALOR_NAO + ", " + ValueUtil.VALOR_NAO;
    	Vector coletaGpsList = findAllByExample(coletaGpsFilter);
    	if (ValueUtil.isNotEmpty(coletaGpsList)) {
    		return (ColetaGps) coletaGpsList.items[0];
    	}
    	return null;
    }
    
    public void finalizaColetaGpsFimDia(ColetaGps coletaGpsEmAndamento) throws SQLException {
    	if (coletaGpsEmAndamento != null) {
    		coletaGpsEmAndamento.hrFimColeta = "23:59:59";
    		update(coletaGpsEmAndamento);
    	}
    }
    
    public void finalizaColetaGpsNoHorarioLimite(ColetaGps coletaGpsEmAndamento) throws SQLException {
    	if (coletaGpsEmAndamento != null) {
    		coletaGpsEmAndamento.hrFimColeta = LavenderePdaConfig.getHoraLimiteColetaGpsManual();
    		coletaGpsEmAndamento.dtEncerramentoAuto = DateUtil.getCurrentDate();
    		coletaGpsEmAndamento.hrEncerramentoAuto = TimeUtil.getCurrentTimeHHMMSS();
    		if (LavenderePdaConfig.usaMotivoParadaColetaGps) {
				MotivoColeta motivoColeta = MotivoColetaService.getInstance().findMotivoColetaPadrao();
				if (ValueUtil.isNotEmpty(motivoColeta.cdMotivo)) {
					coletaGpsEmAndamento.cdMotivoColeta = motivoColeta.cdMotivo;
				}
			}
    		update(coletaGpsEmAndamento);
    		SessionLavenderePda.setColetaGpsEmAndamento(false);
    		UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.COLETAGPS_MSG_COLETA_GPS_PARARA_AUTO, new String[] {StringUtil.getStringValue(coletaGpsEmAndamento.dtColetaGps), LavenderePdaConfig.usaHorarioLimiteColetaGpsManual}));
    	}
    }
    
    public void encerraColetaGpsSeNecessario() throws SQLException {
		if (SessionLavenderePda.isColetaGpsEmAndamento() && LavenderePdaConfig.isUsaHorarioLimiteColetaGpsManual()) {
			ColetaGps coletaGpsEmAndamento = getLastColetaGpsEmAndamento();
			if (coletaGpsEmAndamento != null && isDeveEncerrarAutomaticamente(coletaGpsEmAndamento, TimeUtil.getCurrentTimeHHMMSS())) {
				finalizaColetaGpsNoHorarioLimite(coletaGpsEmAndamento);
			}
		}
	}
    
    public boolean isDeveEncerrarAutomaticamente(ColetaGps coletaGpsEmAndamento, String horaAtual) {
		if (!DateUtil.getCurrentDate().isBefore(coletaGpsEmAndamento.dtColetaGps)) {
			Time hrLimite = getHora(LavenderePdaConfig.getHoraLimiteColetaGpsManual());
			Time hrInicioColeta = getHora(coletaGpsEmAndamento.hrInicioColeta);
			Time hrAtual = getHora(horaAtual); 
			if (hrInicioColeta.getTimeLong() < hrLimite.getTimeLong() && hrAtual.getTimeLong() > hrLimite.getTimeLong()) {
				return true;
			}
		}
		return false;
	}
    
    public Time getHora(String hora) {
		Time newHora = new Time();
		newHora.hour = ValueUtil.getIntegerValue(StringUtil.split(hora, ':', 0));
	    newHora.minute = ValueUtil.getIntegerValue(StringUtil.split(hora, ':', 1));
	    newHora.second = ValueUtil.getIntegerValue(StringUtil.split(hora, ':', 2));
	    return newHora;
	}

}
