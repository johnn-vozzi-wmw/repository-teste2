package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServ;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RequisicaoServRespDbxDao;
import totalcross.util.Vector;

public class RequisicaoServRespService extends CrudService  {
	
    private static RequisicaoServRespService instance;

    public static RequisicaoServRespService getInstance() {
        if (instance == null) {
            instance = new RequisicaoServRespService();
        }
        return instance;
    }

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return RequisicaoServRespDbxDao.getInstance();
	}
	
	public Vector findRespostasRequisicao(BaseDomain requisicaoServ) throws SQLException {
		return RequisicaoServRespDbxDao.getInstance().findRespostasRequisicao((RequisicaoServ)requisicaoServ);
	}

}
