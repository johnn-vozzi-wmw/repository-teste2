package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.BaseService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.StatusAgenda;
import totalcross.util.Vector;

public class StatusAgendaService extends BaseService{
	
	 private static StatusAgendaService instance;
	    
	    private StatusAgendaService() {
	        //--
	    }
	    
	    public static StatusAgendaService getInstance() {
	        if (instance == null) {
	            instance = new StatusAgendaService();
	        }
	        return instance;
	    }
	    
	    //@Override
	    protected CrudDao getCrudDao() {
	    	return null;
	    }
	    
	    //@Override
	    public void validate(BaseDomain domain) throws java.sql.SQLException {
	    }
	    
	    //@Override
	    public Vector findAll() throws java.sql.SQLException {
			Vector statusAgendaList = new Vector();
			//--
			StatusAgenda statusAgenda = new StatusAgenda();
			statusAgenda.cdStatusAgenda = StatusAgenda.STATUSAGENDA_CDAVISITAR;
			statusAgenda.dsStatusAgenda = StatusAgenda.STATUSAGENDA_DSAVISITAR;
			statusAgendaList.addElement(statusAgenda);
			//--
			statusAgenda = new StatusAgenda();
			statusAgenda.cdStatusAgenda = StatusAgenda.STATUSAGENDA_CDNAOPOSITIVADO;
			statusAgenda.dsStatusAgenda = StatusAgenda.STATUSAGENDA_DSNAOPOSITIVADO;
			statusAgendaList.addElement(statusAgenda);
			//--
			statusAgenda = new StatusAgenda();
			statusAgenda.cdStatusAgenda = StatusAgenda.STATUSAGENDA_CDPOSITIVADO;
			statusAgenda.dsStatusAgenda = StatusAgenda.STATUSAGENDA_DSPOSITIVADO;
			statusAgendaList.addElement(statusAgenda);
			//--
			return statusAgendaList;    	
	    }


}
