package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.CampoTela;
import br.com.wmw.framework.integration.dao.CampoTelaDbxDao;

public class CampoTelaLavendereDbxDao extends CampoTelaDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CampoTela();
	}

	public CampoTelaLavendereDbxDao() {
		super("TBLVPCAMPOTELA");
	}

    public static CampoTelaDbxDao getInstance() {
        if (instance == null) {
            instance = new CampoTelaLavendereDbxDao();
        }
        return instance;
    }

}