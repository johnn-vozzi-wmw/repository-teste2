package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CorSistemaDbxDao;
import br.com.wmw.lavenderepda.business.domain.CorSistemaLavendere;

public class CorSistemaLavendereDbxDao extends CorSistemaDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CorSistemaLavendere();
	}

    public CorSistemaLavendereDbxDao() {
        super(CorSistemaLavendere.TABLE_NAME);
    }

    public static CorSistemaDbxDao getInstance() {
        if (instance == null) {
            instance = new CorSistemaLavendereDbxDao();
        }
        return instance;
    }

}