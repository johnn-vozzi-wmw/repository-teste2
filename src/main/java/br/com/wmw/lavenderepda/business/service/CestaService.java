package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Cesta;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CestaPdbxDao;
import totalcross.util.Vector;

public class CestaService extends CrudService {

    private static CestaService instance;

    private CestaService() {
        //--
    }

    public static CestaService getInstance() {
        if (instance == null) {
            instance = new CestaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CestaPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public Vector findAllCestas(String cdCliente) throws SQLException {
    	Cesta cestaFilter = new Cesta();
    	cestaFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	cestaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	cestaFilter.cdClienteFilter = cdCliente;
    	return findAllByExample(cestaFilter);
    }

}