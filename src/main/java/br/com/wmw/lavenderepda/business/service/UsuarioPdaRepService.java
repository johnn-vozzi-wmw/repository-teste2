package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.UsuarioPdaRep;
import br.com.wmw.lavenderepda.integration.dao.pdbx.UsuarioPdaRepPdbxDao;
import totalcross.util.Vector;

public class UsuarioPdaRepService extends CrudService {

    private static UsuarioPdaRepService instance;

    private UsuarioPdaRepService() {
        //--
    }

    public static UsuarioPdaRepService getInstance() {
        if (instance == null) {
            instance = new UsuarioPdaRepService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return UsuarioPdaRepPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
    public String getCdUsuarioByRepresentante(String cdRepresentante) throws SQLException {
    	UsuarioPdaRep usuarioPdaRep = new UsuarioPdaRep();
    	usuarioPdaRep.cdRepresentante = cdRepresentante;
    	Vector findAllByExample = findAllByExample(usuarioPdaRep);
      Vector result = findAllByExample;
    	if (!ValueUtil.isEmpty(result)) {
    		return ((UsuarioPdaRep)result.items[0]).cdUsuario;
    	} else {
    		return null;
    	}
    }

}