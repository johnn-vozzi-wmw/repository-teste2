package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.MetasPorForn;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MetasPorFornPdbxDao;
import totalcross.util.Vector;

public class MetasPorFornService extends CrudService {

    private static MetasPorFornService instance;

    private MetasPorFornService() {
        //--
    }

    public static MetasPorFornService getInstance() {
        if (instance == null) {
            instance = new MetasPorFornService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return MetasPorFornPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public String getMetasFornecedorList(String dsPeriodo, String cdRep) throws SQLException {
    	MetasPorForn metasFornecedorFilter = new MetasPorForn();
    	metasFornecedorFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		metasFornecedorFilter.cdRepresentante = cdRep;
    	} else {
    		metasFornecedorFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	}
    	metasFornecedorFilter.dsPeriodo = dsPeriodo;
    	return findAllByExampleToGrid(metasFornecedorFilter);
    }

    public Vector findMetasFornecedorList(String dsPeriodo, String cdRep) throws SQLException {
    	MetasPorForn metasFornecedorFilter = new MetasPorForn();
    	metasFornecedorFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	metasFornecedorFilter.cdRepresentante = cdRep;
    	metasFornecedorFilter.dsPeriodo = dsPeriodo;
    	Vector list = findAllByExampleSummary(metasFornecedorFilter);
    	return list;
    }
    
    public Vector findAllDistinctPeriodo() throws SQLException {
    	MetasPorForn metasFornecedorFilter = new MetasPorForn();
    	metasFornecedorFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	metasFornecedorFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(MetasPorForn.class);
    	return MetasPorFornPdbxDao.getInstance().findAllDistinctPeriodo(metasFornecedorFilter);
    }
}