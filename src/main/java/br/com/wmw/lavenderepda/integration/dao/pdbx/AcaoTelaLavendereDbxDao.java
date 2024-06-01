package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.AcaoTela;
import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.AcaoTelaDbxDao;

public class AcaoTelaLavendereDbxDao extends AcaoTelaDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new AcaoTela();
	}

    public static final String TABLE_NAME = "TBLVPACAOTELA";
    
    public AcaoTelaLavendereDbxDao(String tableName) {
		super(tableName);
	}

	public static AcaoTelaDbxDao getInstance() {
        if (instance == null) {
            instance = new AcaoTelaLavendereDbxDao(TABLE_NAME);
        }
        return instance;
    }

}
