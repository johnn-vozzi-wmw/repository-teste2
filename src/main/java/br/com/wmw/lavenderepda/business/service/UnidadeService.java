package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Unidade;
import br.com.wmw.lavenderepda.integration.dao.pdbx.UnidadePdbxDao;

public class UnidadeService extends CrudService {

    private static UnidadeService instance;

    private UnidadeService() {
        //--
    }

    public static UnidadeService getInstance() {
        if (instance == null) {
            instance = new UnidadeService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return UnidadePdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public Unidade findByCdUnidade(String cdUnidade, boolean dsPlural) throws SQLException {
    	Unidade unidade = new Unidade();
    	unidade.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	unidade.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	unidade.cdUnidade = cdUnidade;
    	if (cdUnidade != null) {
	    	unidade.dsUnidade = findColumnByRowKey(unidade.getRowKey(), "DSUNIDADE");
	    	if (dsPlural) {
	    		unidade.dsUnidadePlural = findColumnByRowKey(unidade.getRowKey(), "DSUNIDADEPLURAL");
	    	}
    	}
    	return unidade;
    }

    public String getDsUnidade(String cdUnidade) throws SQLException {
    	if (ValueUtil.isEmpty(cdUnidade)) return ValueUtil.VALOR_NI;
    	Unidade unidade = new Unidade();
    	unidade.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	unidade.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	unidade.cdUnidade = cdUnidade;
    	String dsUnidade = findColumnByRowKey(unidade.getRowKey(), "DSUNIDADE");
    	unidade = null;
    	if (!ValueUtil.isEmpty(dsUnidade)) {
    		return dsUnidade;
    	} else {
    		return StringUtil.getStringValue(cdUnidade);
    	}
    }
    
    public Unidade findUnidadeByCdUnidade(String cdUnidade) throws SQLException {
    	Unidade unidade = new Unidade();
		unidade.cdEmpresa = SessionLavenderePda.cdEmpresa;
		unidade.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		unidade.cdUnidade = cdUnidade;
    	return (Unidade) UnidadePdbxDao.getInstance().findByPrimaryKey(unidade);
    }

}
