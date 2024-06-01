package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.AtributoOpcaoProd;
import br.com.wmw.lavenderepda.integration.dao.pdbx.AtributoOpcaoProdPdbxDao;
import totalcross.util.Vector;

public class AtributoOpcaoProdService extends CrudService {

    private static AtributoOpcaoProdService instance;

    private AtributoOpcaoProdService() {
        //--
    }

    public static AtributoOpcaoProdService getInstance() {
        if (instance == null) {
            instance = new AtributoOpcaoProdService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return AtributoOpcaoProdPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    //@Override
    public Vector findAllByEmpresaAndRep(String cdAtributo) throws SQLException {
    	AtributoOpcaoProd atributoProd = new AtributoOpcaoProd();
		atributoProd.cdEmpresa = SessionLavenderePda.cdEmpresa;
		atributoProd.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		atributoProd.cdAtributoProduto = cdAtributo;
    	return super.findAllByExample(atributoProd);
    }

}