package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.LavendereConfig;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.PreferenciaFuncao;
import br.com.wmw.lavenderepda.business.domain.UsuarioPdaRep;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PreferenciaFuncaoDbxDao;
import totalcross.util.Vector;

public class PreferenciaFuncaoService extends CrudService {

    private static PreferenciaFuncaoService instance;
    
    private PreferenciaFuncaoService() {
        //--
    }
    
    public static PreferenciaFuncaoService getInstance() {
        if (instance == null) {
            instance = new PreferenciaFuncaoService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return PreferenciaFuncaoDbxDao.getInstance();
    }

	@Override
	public void validate(BaseDomain domain) throws SQLException {	}
    
	public Vector findAllPreferenciaFuncao() throws SQLException {
    	UsuarioPdaRep usuarioPdaRep = SessionLavenderePda.usuarioPdaRep;
    	PreferenciaFuncao preferenciaFuncaoFilter = new PreferenciaFuncao();
    	preferenciaFuncaoFilter.cdSistema = LavendereConfig.CDSISTEMA;
		preferenciaFuncaoFilter.cdFuncao = usuarioPdaRep.usuario.cdFuncao;
		preferenciaFuncaoFilter.cdUsuario = usuarioPdaRep.cdUsuario;
    	return findAllByExample(preferenciaFuncaoFilter);
    }

}