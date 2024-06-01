package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.RepresentanteEmp;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RepresentanteEmpPdbxDao;
import totalcross.util.Vector;

public class RepresentanteEmpService extends CrudService {

    private static RepresentanteEmpService instance;

    private RepresentanteEmpService() {
        //--
    }

    public static RepresentanteEmpService getInstance() {
        if (instance == null) {
            instance = new RepresentanteEmpService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return RepresentanteEmpPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

	public Vector findAllByExampleJoinUsuarioPdaRep(RepresentanteEmp repEmp) throws SQLException {
		return RepresentanteEmpPdbxDao.getInstance().findAllByExampleJoinUsuarioPdaRep(repEmp);
	}
	
	public Vector findAllOutrosCdEmpresas(String cdEmpresaNegacao, String cdRepresentante) throws SQLException {
		return RepresentanteEmpPdbxDao.getInstance().findAllOutrosCdEmpresas(cdEmpresaNegacao, cdRepresentante);
	}

}