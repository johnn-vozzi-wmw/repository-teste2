package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PontuacaoFaixaDiasPdbxDao;

public class PontuacaoFaixaDiasService extends CrudPersonLavendereService {

    private static PontuacaoFaixaDiasService instance;

    public static PontuacaoFaixaDiasService getInstance() {
        return instance == null ? instance = new PontuacaoFaixaDiasService() : instance;
    }

    protected CrudDao getCrudDao() { return PontuacaoFaixaDiasPdbxDao.getInstance(); }
    public void validate(BaseDomain domain) throws java.sql.SQLException {}
    
}