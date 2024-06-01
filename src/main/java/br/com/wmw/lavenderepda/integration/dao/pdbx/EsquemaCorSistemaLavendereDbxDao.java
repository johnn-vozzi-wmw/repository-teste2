package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.EsquemaCorSistema;
import br.com.wmw.framework.integration.dao.EsquemaCorSistemaDbxDao;
import br.com.wmw.lavenderepda.business.service.EsquemaCorSistemaLavendereService;

public class EsquemaCorSistemaLavendereDbxDao extends EsquemaCorSistemaDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new EsquemaCorSistema();
	}

    public EsquemaCorSistemaLavendereDbxDao() {
        super("TBLVPESQUEMACORSISTEMA");
    }

    public static EsquemaCorSistemaDbxDao getInstance() {
        if (instance == null) {
			instance = new EsquemaCorSistemaLavendereDbxDao();
        }
        return instance;
    }

}