package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.MotRegistroVisita;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MotRegistroVisitaPdbxDao;

public class MotRegistroVisitaService extends CrudService {

    private static MotRegistroVisitaService instance;

    private MotRegistroVisitaService() {
        //--
    }

    public static MotRegistroVisitaService getInstance() {
        if (instance == null) {
            instance = new MotRegistroVisitaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return MotRegistroVisitaPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public int countMotivosRegistrosVisitas(String cdEmpresa, String cdMotivoRegistroVisita) throws SQLException {
		MotRegistroVisita motRegistroVisitaFilter = new MotRegistroVisita();
		motRegistroVisitaFilter.cdEmpresa = cdEmpresa;
		motRegistroVisitaFilter.cdMotivoRegistroVisita = cdMotivoRegistroVisita;
		return MotRegistroVisitaService.getInstance().countByExample(motRegistroVisitaFilter);
	}
    
    public MotRegistroVisita getMotivoVisitaByFilter(String cdEmpresa, String cdMotivoRegistroVisita) throws SQLException {
		MotRegistroVisita motRegistroVisitaFilter = new MotRegistroVisita();
		motRegistroVisitaFilter.cdEmpresa = cdEmpresa;
		motRegistroVisitaFilter.cdMotivoRegistroVisita = cdMotivoRegistroVisita;
		return (MotRegistroVisita) MotRegistroVisitaService.getInstance().findByRowKey(motRegistroVisitaFilter.getRowKey());
    }
    
}