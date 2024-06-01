package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.MetasPorCliente;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MetasPorClientePdbxDao;
import totalcross.util.Vector;

public class MetasPorClienteService extends CrudService {

    private static MetasPorClienteService instance;

    private MetasPorClienteService() {
        //--
    }

    public static MetasPorClienteService getInstance() {
        if (instance == null) {
            instance = new MetasPorClienteService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return MetasPorClientePdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public String getMetasClienteList(String dsPeriodo, String cdRep) throws SQLException {
    	MetasPorCliente metasClienteFilter = new MetasPorCliente();
    	metasClienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		metasClienteFilter.cdRepresentante = cdRep;
    	} else {
    		metasClienteFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	}
    	metasClienteFilter.dsPeriodo = dsPeriodo;
    	return findAllByExampleToGrid(metasClienteFilter);
    }

    public Vector findMetasClienteList(String dsPeriodo, String cdRep) throws SQLException {
    	MetasPorCliente metasClienteFilter = new MetasPorCliente();
    	metasClienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	metasClienteFilter.cdRepresentante = cdRep;
    	metasClienteFilter.dsPeriodo = dsPeriodo;
    	Vector list = findAllByExampleSummary(metasClienteFilter);
    	return list;
    }
    
    public Vector findAllDistinctPeriodo() throws SQLException {
    	MetasPorCliente metasFornecedorFilter = new MetasPorCliente();
    	metasFornecedorFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	metasFornecedorFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(MetasPorCliente.class);
    	return MetasPorClientePdbxDao.getInstance().findAllDistinctPeriodo(metasFornecedorFilter);
    }
}