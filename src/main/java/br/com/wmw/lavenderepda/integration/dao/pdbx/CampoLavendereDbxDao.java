package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.Campo;
import br.com.wmw.framework.integration.dao.CampoDbxDao;

public class CampoLavendereDbxDao extends CampoDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Campo();
	}

	public static final String TABLE_NAME = "TBLVPCAMPO";

	public CampoLavendereDbxDao() {
		super(TABLE_NAME);
	}

    public static CampoDbxDao getInstance() {
        if (instance == null) {
            instance = new CampoLavendereDbxDao();
        }
        return instance;
    }

}