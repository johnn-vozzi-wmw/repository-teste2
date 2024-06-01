package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.integration.dao.pdbx.SupervisorRepPdbxDao;
import totalcross.util.Vector;

public class SupervisorRepService extends CrudService {

    private static SupervisorRepService instance;

    private SupervisorRepService() {
        //--
    }

    public static SupervisorRepService getInstance() {
        if (instance == null) {
            instance = new SupervisorRepService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return SupervisorRepPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
    public Vector getRepresentantesBySupervisor(String cdEmpresa, String cdSupervisor) throws SQLException {
    	SupervisorRep supervisorRepFilter = new SupervisorRep();
		supervisorRepFilter.cdEmpresa = cdEmpresa;
		supervisorRepFilter.cdSupervisor = cdSupervisor;
		return findAllByExample(supervisorRepFilter);
    }
    
    public Vector getCdRepresentantesBySupervisor(String cdEmpresa, String cdSupervisor) throws SQLException {
    	SupervisorRep supervisorRepFilter = new SupervisorRep();
		supervisorRepFilter.cdEmpresa = cdEmpresa;
		supervisorRepFilter.cdSupervisor = cdSupervisor;
		return findColumnValuesByExample(supervisorRepFilter, "CDREPRESENTANTE");
    }
    
    public boolean isCargaMultiplosSupervisores() throws SQLException {
    	Vector supervisoresList = findAll();
    	SupervisorRep sup = supervisoresList.size() > 0 ? (SupervisorRep) supervisoresList.items[0] : null;
    	if (sup != null) {
    		String cdSupervisor = sup.cdSupervisor;
			for (int i = 1; i < supervisoresList.size(); i++) {
				sup = (SupervisorRep) supervisoresList.items[i];
				if (!cdSupervisor.equals(sup.cdSupervisor)) {
					return true;
				}
			} 
		}
    	return false;
    }
    
	public boolean isRepresentanteValido(String cdRepresentante) throws SQLException {
		Vector listSupervisorRep = getRepresentantesBySupervisor(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getRepresentante().cdRepresentante);
		boolean valido = false;
		if (listSupervisorRep != null) {
			int sizeSupervisorRep = listSupervisorRep.size();
			SupervisorRep supervisorRepFilter;
			for (int i = 0; i < sizeSupervisorRep; i++) {
				supervisorRepFilter = (SupervisorRep) listSupervisorRep.elementAt(i);
				valido = supervisorRepFilter.cdRepresentante.equals(cdRepresentante) ? true : false;
				if (valido) {
					break;
				} else {
					continue;
				}
			} 
		}
		return valido;
	}
	
	public Vector findAllRepresentantesLiberadosSenhaSupervisor(int cdparametro) throws SQLException {
		return SupervisorRepPdbxDao.getInstance().findAllRepresentantesLiberadosSenhaSupervisor(cdparametro);
	}

}