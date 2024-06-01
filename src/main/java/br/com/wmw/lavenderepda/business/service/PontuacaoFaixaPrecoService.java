package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PontuacaoFaixaPrecoPdbxDao;

public class PontuacaoFaixaPrecoService extends CrudPersonLavendereService {

    private static PontuacaoFaixaPrecoService instance;

    public static PontuacaoFaixaPrecoService getInstance() {
        return instance == null ? instance = new PontuacaoFaixaPrecoService() : instance;
    }

    protected CrudDao getCrudDao() { return PontuacaoFaixaPrecoPdbxDao.getInstance(); }
    public void validate(BaseDomain domain) throws java.sql.SQLException {}
    
}