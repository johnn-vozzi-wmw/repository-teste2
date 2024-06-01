package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.TipoItemGrade;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoItemGradeDbxDao;

public class TipoItemGradeService extends CrudService {

    private static TipoItemGradeService instance;

    private TipoItemGradeService() {
        //--
    }

    public static TipoItemGradeService getInstance() {
        if (instance == null) {
            instance = new TipoItemGradeService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return TipoItemGradeDbxDao.getInstance();
    }

    @Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public String getDsTipoItemGrade(String cdTipoItemGrade) throws SQLException {
		TipoItemGrade tipoItemGrade = new TipoItemGrade();
		tipoItemGrade.cdEmpresa = SessionLavenderePda.cdEmpresa;
		tipoItemGrade.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		tipoItemGrade.cdTipoItemGrade = cdTipoItemGrade;
    	String dsTipoItemGrade = findColumnByRowKey(tipoItemGrade.getRowKey(), "DSTIPOITEMGRADE");
    	if (!ValueUtil.isEmpty(dsTipoItemGrade)) {
    		return dsTipoItemGrade;
    	} else {
    		return StringUtil.getStringValue(cdTipoItemGrade);
    	}
    }

}