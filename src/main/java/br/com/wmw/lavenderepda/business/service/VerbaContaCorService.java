package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.VerbaContaCor;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VerbaContaCorPdbxDao;

public class VerbaContaCorService extends CrudService {

    private static VerbaContaCorService instance;

    private VerbaContaCorService() {
        //--
    }

    public static VerbaContaCorService getInstance() {
        if (instance == null) {
            instance = new VerbaContaCorService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return VerbaContaCorPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {

    }

    public String getDescricaoContaCorrente(String cdContaCorrente) throws SQLException {
    	VerbaContaCor verbaContaCorrenteFilter = new VerbaContaCor();
    	verbaContaCorrenteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	verbaContaCorrenteFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	verbaContaCorrenteFilter.cdContaCorrente = cdContaCorrente;
    	String dsContaCorrente = findColumnByRowKey(verbaContaCorrenteFilter.getRowKey(),"DSCONTACORRENTE");
    	if (dsContaCorrente == null) {
    		return cdContaCorrente;
    	}
    	return dsContaCorrente;
    }

}