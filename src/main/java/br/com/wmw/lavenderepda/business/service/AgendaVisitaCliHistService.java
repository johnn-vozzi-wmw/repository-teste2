package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.lavenderepda.business.domain.AgendaVisita;
import br.com.wmw.lavenderepda.business.domain.AgendaVisitaCliHist;
import br.com.wmw.lavenderepda.integration.dao.pdbx.AgendaVisitaCliHistDao;
import totalcross.util.Date;
import totalcross.util.Vector;

public class AgendaVisitaCliHistService extends CrudService {

    private static AgendaVisitaCliHistService instance;

    private AgendaVisitaCliHistService() {
        //--
    }

    public static AgendaVisitaCliHistService getInstance() {
        if (instance == null) {
            instance = new AgendaVisitaCliHistService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return AgendaVisitaCliHistDao.getInstance();
    }

    @Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public void geraAgendaHistCli() throws SQLException {
    	Date dtAgendaInicio = AgendaVisitaService.getInstance().getUltimoDiaUtil();
    	Date dtAgendaFim = DateUtil.getCurrentDate();
    	DateUtil.addDay(dtAgendaFim, 6);
    	Vector listAgendaVisita = AgendaVisitaService.getInstance().findAgendasVisitas(dtAgendaInicio, dtAgendaFim);
    	Map<String, AgendaVisitaCliHist> listAgendaVisitaCliHistMap = new HashMap<>(listAgendaVisita.size());
    	for (AgendaVisita agendaVisita : VectorUtil.iterateOver(listAgendaVisita, AgendaVisita.class)) {
    		AgendaVisitaCliHist agendaVisitaCliHist = new AgendaVisitaCliHist(agendaVisita);
			if (listAgendaVisitaCliHistMap.containsKey(agendaVisitaCliHist.getPrimaryKey())) {
				agendaVisitaCliHist = listAgendaVisitaCliHistMap.get(agendaVisitaCliHist.getPrimaryKey());
				agendaVisitaCliHist.qtTotalAgenda++;
			} else {
				agendaVisitaCliHist.qtTotalAgenda = 1;
				listAgendaVisitaCliHistMap.put(agendaVisitaCliHist.getPrimaryKey(), agendaVisitaCliHist);
			}
    	}
    	deleteRegistrosAntigos();
    	insertOrUpdateAgendaVisitaCliHist(listAgendaVisitaCliHistMap);
    }
    
    public void insertOrUpdateAgendaVisitaCliHist(Map<String, AgendaVisitaCliHist> listAgendaVisitaHist) throws SQLException {
        for (AgendaVisitaCliHist agendaVisitaCliHist : listAgendaVisitaHist.values()) {
            insert(agendaVisitaCliHist);
        }
    }
    
	@Override
	public void insert(BaseDomain domain) throws SQLException {
		validate(domain);
		validateDuplicated(domain);
		getCrudDao().insert(domain);
	}
	
	public void deleteRegistrosAntigos() throws SQLException {
		deleteAllByExample(new AgendaVisitaCliHist());
	}
	
}
