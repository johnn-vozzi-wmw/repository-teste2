package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.Tela;
import br.com.wmw.framework.integration.dao.TelaDbxDao;

public class TelaLavendereDbxDao extends TelaDbxDao {
	
	public static final String TABLE_NAME_LVP = "TBLVPTELA";


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Tela();
	}

	public TelaLavendereDbxDao() {
		super(TABLE_NAME_LVP);
	}

    public static TelaDbxDao getInstance() {
        if (instance == null) {
            instance = new TelaLavendereDbxDao();
        }
        return instance;
    }

}