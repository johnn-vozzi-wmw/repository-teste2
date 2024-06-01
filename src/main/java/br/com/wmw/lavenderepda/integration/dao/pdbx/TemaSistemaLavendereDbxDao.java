package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.TemaSistemaDbxDao;
import br.com.wmw.lavenderepda.business.domain.TemaSistemaLavendere;

public class TemaSistemaLavendereDbxDao extends TemaSistemaDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TemaSistemaLavendere();
	}

    public TemaSistemaLavendereDbxDao() {
        super("TBLVPTEMASISTEMA");
    }

    public static TemaSistemaDbxDao getInstance() {
        if (instance == null) {
			instance = new TemaSistemaLavendereDbxDao();
        }
        return instance;
    }

}