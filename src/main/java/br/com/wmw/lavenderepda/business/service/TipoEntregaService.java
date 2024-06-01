package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoEntregaPdbxDao;
import totalcross.util.Vector;

import java.sql.SQLException;

public class TipoEntregaService extends CrudService {

    private static TipoEntregaService instance;

    private TipoEntregaService() {
        //--
    }

    public static TipoEntregaService getInstance() {
        if (instance == null) {
            instance = new TipoEntregaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return TipoEntregaPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {

    }

	public Vector findAllTipoEntregaSemEmpresa() throws SQLException {
		return TipoEntregaPdbxDao.getInstance().findAllTipoEntregaSemEmpresa();
	}
}