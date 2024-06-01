package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.AcessoMaterial;
import br.com.wmw.lavenderepda.integration.dao.pdbx.AcessoMaterialDbxDao;

public class AcessoMaterialService extends CrudService {

    private static AcessoMaterialService instance;
    
    private AcessoMaterialService() {
        //--
    }
    
    public static AcessoMaterialService getInstance() {
        if (instance == null) {
            instance = new AcessoMaterialService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return AcessoMaterialDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
    }

	public void insereAcesso(String nmMaterial) throws SQLException {
		AcessoMaterial acessoMaterial = new AcessoMaterial();
		acessoMaterial.cdEmpresa = SessionLavenderePda.cdEmpresa;
		acessoMaterial.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		acessoMaterial.cdAcessoMaterial = TimeUtil.getCurrentDateTimeDDDDDSSSSSMMM();
		acessoMaterial.cdUsuarioAcesso = Session.getCdUsuario();
		acessoMaterial.dtAcesso = DateUtil.getCurrentDate();
		acessoMaterial.hrAcesso = TimeUtil.getCurrentTimeHHMMSS();
		acessoMaterial.nmMaterial = getNmMaterialTruncated(nmMaterial);
		insert(acessoMaterial);
	}

	private String getNmMaterialTruncated(String nmMaterial) {
		if (ValueUtil.isNotEmpty(nmMaterial) && nmMaterial.length() > 500) {
			return nmMaterial.substring(0, 500);
		}
		return nmMaterial;
	}

}