package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MotivoTrocaPdbxDao;

public class MotivoTrocaService extends CrudService {

    private static MotivoTrocaService instance;

    private MotivoTrocaService() {
        //--
    }

    public static MotivoTrocaService getInstance() {
        if (instance == null) {
            instance = new MotivoTrocaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return MotivoTrocaPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

}