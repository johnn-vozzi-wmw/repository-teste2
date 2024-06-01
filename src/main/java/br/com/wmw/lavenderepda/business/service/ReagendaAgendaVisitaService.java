package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AgendaVisita;
import br.com.wmw.lavenderepda.business.domain.ReagendaAgendaVisita;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ReagendaAgendaVisitaDbxDao;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ReagendaAgendaVisitaService extends CrudService {

    private static ReagendaAgendaVisitaService instance;
    
    private ReagendaAgendaVisitaService() {
        //--
    }
    
    public static ReagendaAgendaVisitaService getInstance() {
        if (instance == null) {
            instance = new ReagendaAgendaVisitaService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return ReagendaAgendaVisitaDbxDao.getInstance();
    }
    
    
    public ReagendaAgendaVisita findReagendaByAgenda(AgendaVisita agenda) throws SQLException {
    	ReagendaAgendaVisita reagendaFilter = montaReagendaFilterByAgenda(agenda); 
    	return (ReagendaAgendaVisita) findByPrimaryKey(reagendaFilter);
    }
    
    public Vector findAllByAgenda(AgendaVisita agenda) throws SQLException {
    	ReagendaAgendaVisita reagendaFilter = montaReagendaFilterByAgenda(agenda); 
    	return findAllByExample(reagendaFilter);
    }
    
    public Vector findAllOntemEmDiante() throws SQLException {
    	Vector reagendaList = findAll();
    	reagendaList = filterByDataMaiorQueOntem(reagendaList);
    	return reagendaList;
    }
    
    private Vector filterByDataMaiorQueOntem(Vector list) {
    	Vector newList = new Vector();
    	int size = list.size();
    	Date data = new Date();
    	DateUtil.decDay(data, 1);
    	for (int i = 0; i < size; i++) {
    		ReagendaAgendaVisita reagenda = (ReagendaAgendaVisita)list.elementAt(i); 
			if (reagenda.dtAgendaOriginal.isAfter(data) || reagenda.dtAgendaOriginal.equals(data)) {
				newList.addElement(reagenda);
			}
		}
    	return newList;
    }
    
    public void criaVisitaAgendaReagendadaServidor() throws SQLException {
    	if (LavenderePdaConfig.reagendaAgendaVisitaParaDiaPosteriorAnterior) {
			Vector reagendamentoList = findAllOntemEmDiante();
			int size = reagendamentoList.size();
			for (int i = 0; i < size; i++) {
				ReagendaAgendaVisita reagenda = (ReagendaAgendaVisita) reagendamentoList.elementAt(i);
				insertVisitaByReagendaAgendaVisita(reagenda);
			}
    	}
	}

	private void insertVisitaByReagendaAgendaVisita(ReagendaAgendaVisita reagenda) throws SQLException {
		VisitaService.getInstance().insertVisitaByReagendaAgendaVisita(reagenda, Visita.FL_VISITA_REAGENDADA);
	}
    
    private ReagendaAgendaVisita montaReagendaFilterByAgenda(AgendaVisita agenda) {
    	ReagendaAgendaVisita filter = new ReagendaAgendaVisita();
    	filter.cdEmpresa = agenda.cdEmpresa;
    	filter.cdCliente = agenda.cdCliente;
    	filter.cdRepresentante = agenda.cdRepresentante;
    	filter.nuDiaSemana = agenda.nuDiaSemana;
    	filter.flSemanaMes = agenda.flSemanaMes;
    	return filter;
    }
 
    @Override
    public void validate(BaseDomain domain) {
    	//--
    }

}