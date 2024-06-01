package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.DomainUtil;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ConfigAcessoSistema;
import br.com.wmw.lavenderepda.business.domain.dto.ConfigAcessoSistemaDTO;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ConfigAcessoSistemaDbxDao;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.json.JSONFactory;
import totalcross.sys.InvalidNumberException;
import totalcross.sys.Time;
import totalcross.util.Date;
import totalcross.util.InvalidDateException;
import totalcross.util.Vector;

public class ConfigAcessoSistemaService extends CrudService {

    private static ConfigAcessoSistemaService instance;
    private static final String SEM_CONFIG = "Nenhuma config. encontrada";
    
    private ConfigAcessoSistemaService() {
        //--
    }
    
    public static ConfigAcessoSistemaService getInstance() {
        if (instance == null) {
            instance = new ConfigAcessoSistemaService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return ConfigAcessoSistemaDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
    }

   

	public ConfigAcessoSistema getConfigAcessoSistema(String cdFuncao, String cdUsuario) throws SQLException {
		ConfigAcessoSistema configAcessoSistemaApp = findConfigAcessoSistema(cdFuncao);
		try {
			String retorno = SyncManager.getConfigAcessoSistema(cdFuncao);
			ConfigAcessoSistemaDTO acessoSistemaDTO = new ConfigAcessoSistemaDTO();
			if (retorno != null) {
				if (SEM_CONFIG.equals(retorno.trim())) {
					return null;
				}
				acessoSistemaDTO = JSONFactory.parse(retorno, ConfigAcessoSistemaDTO.class);
			}
			if (acessoSistemaDTO != null && ValueUtil.isNotEmpty(acessoSistemaDTO.cdFuncao)) {
				ConfigAcessoSistema configAcessoSistema = new ConfigAcessoSistema();
				DomainUtil.copyProperties(acessoSistemaDTO, configAcessoSistema);
				configAcessoSistema.cdUsuario = cdUsuario;
				atualizaConfigAcessoSistema(configAcessoSistema, configAcessoSistemaApp);
				return configAcessoSistema;
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
		return configAcessoSistemaApp;
	}

	public ConfigAcessoSistema findConfigAcessoSistema(String cdFuncao) throws SQLException {
		ConfigAcessoSistema configAcessoSistema = new ConfigAcessoSistema();
		configAcessoSistema.cdFuncao = cdFuncao;
		configAcessoSistema.nuDiaSemana = DateUtil.getDayOfWeek(new Date()) + 1;
		configAcessoSistema.hrFilter = TimeUtil.getCurrentTimeHHMM();
		Vector configAcessoList = findAllByExample(configAcessoSistema);
		return ValueUtil.isNotEmpty(configAcessoList) ? (ConfigAcessoSistema) configAcessoList.items[0] : null;
	}

	private void atualizaConfigAcessoSistema(ConfigAcessoSistema configAcessoSistema, ConfigAcessoSistema configAcessoSistemaApp) throws SQLException {
		if (configAcessoSistemaApp != null) {
//			delete(configAcessoSistemaApp);
			configAcessoSistema.nuCarimbo = configAcessoSistemaApp.nuCarimbo;
		}
		configAcessoSistemaApp = (ConfigAcessoSistema) findByPrimaryKey(configAcessoSistema);
		if (configAcessoSistemaApp != null) {
//			delete(configAcessoSistemaApp);
			configAcessoSistema.nuCarimbo = configAcessoSistemaApp.nuCarimbo;
		}
		deleteHorarioIncidente(configAcessoSistema);
		ConfigAcessoSistemaDbxDao.getInstance().insert(configAcessoSistema);
	}

	private void deleteHorarioIncidente(ConfigAcessoSistema configAcessoSistema) throws SQLException {
		ConfigAcessoSistema filter = new ConfigAcessoSistema();
		filter.cdFuncao = configAcessoSistema.cdFuncao;
		filter.nuDiaSemana = DateUtil.getDayOfWeek(new Date()) + 1;
		Vector configAcessoList = findAllByExample(filter);
		int size = configAcessoList.size();
		for (int i = 0; i < size; i++) {
			ConfigAcessoSistema configApp = (ConfigAcessoSistema) configAcessoList.items[i];
			if (validaHorariosIncidentes(configAcessoSistema.hrInicio, configAcessoSistema.hrFim, configApp.hrInicio, configApp.hrFim)) {
				delete(configApp);
			}
		}
	}
	
	private boolean validaHorariosIncidentes(String hrInicioNew, String hrFimNew, String hrInicioOld, String hrFimOld) {
		try {
			String dtPadrao = "20000101-";
			String segPadrao = ":00";
			long hrInicioNewLong = new Time(dtPadrao + hrInicioNew + segPadrao).getTime();
			long hrFimNewLong = new Time(dtPadrao + hrFimNew + segPadrao).getTime();
			long hrInicioOldLong = new Time(dtPadrao + hrInicioOld + segPadrao).getTime();
			long hrFimOldLong = new Time(dtPadrao + hrFimOld + segPadrao).getTime();
			boolean hrInicioNewAfterOld = hrInicioNewLong >= hrInicioOldLong;
			boolean hrInicioNewBeforeOld = hrInicioNewLong <= hrFimOldLong;
			if (hrInicioNewAfterOld && hrInicioNewBeforeOld) {
				return true;
			}
			boolean hrFimNewAfterOld = hrFimNewLong >= hrInicioOldLong;
			boolean hrFimNewBeforeOld = hrFimNewLong <= hrFimOldLong;
			if (hrFimNewBeforeOld && hrFimNewAfterOld) {
				return true;
			}
		} catch (InvalidDateException e) {
			ExceptionUtil.handle(e);
		} catch (InvalidNumberException e) {
			ExceptionUtil.handle(e);
		}
		return false;
	}

	public int verificaNovoTempoIncideEmHorarioLiberado(String cdFuncao, int seconds) throws SQLException {
		Time time = TimeUtil.getCurrentTime();
		time.inc(0, 0, seconds);
		ConfigAcessoSistema configAcessoSistema = new ConfigAcessoSistema();
		configAcessoSistema.cdFuncao = cdFuncao;
		configAcessoSistema.nuDiaSemana = DateUtil.getDayOfWeek(new Date())  + 1;
		configAcessoSistema.hrFilter = TimeUtil.getTimeHHMM(time);
		Vector configList = getInstance().findAllByExample(configAcessoSistema);
		if (ValueUtil.isNotEmpty(configList)) {
			configAcessoSistema = (ConfigAcessoSistema) configList.items[0];
			return TimeUtil.getSecondsBetween(TimeUtil.getCurrentTimeHHMMSS(), configAcessoSistema.hrFim + ":00");
		}
		return seconds;
	}
	
}

