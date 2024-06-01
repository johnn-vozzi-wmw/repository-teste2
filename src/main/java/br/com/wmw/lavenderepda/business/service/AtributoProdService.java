package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.AtributoProd;
import br.com.wmw.lavenderepda.integration.dao.pdbx.AtributoProdPdbxDao;
import totalcross.util.Vector;

public class AtributoProdService extends CrudService {

    private static AtributoProdService instance;

    private AtributoProdService() {
        //--
    }

    public static AtributoProdService getInstance() {
        if (instance == null) {
            instance = new AtributoProdService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return AtributoProdPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    //@Override
    public Vector findAllByEmpresaAndRep() throws SQLException {
    	AtributoProd atributoProd = new AtributoProd();
		atributoProd.cdEmpresa = SessionLavenderePda.cdEmpresa;
		atributoProd.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(AtributoProd.class);
    	return super.findAllByExample(atributoProd);
    }

}