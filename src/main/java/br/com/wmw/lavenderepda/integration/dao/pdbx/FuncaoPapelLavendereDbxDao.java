package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.FuncaoPapel;
import br.com.wmw.framework.integration.dao.FuncaoPapelDbxDao;

public class FuncaoPapelLavendereDbxDao extends FuncaoPapelDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FuncaoPapel();
	}

	public FuncaoPapelLavendereDbxDao() {
		super("TBLVPFUNCAOPAPEL");
	}

    public static FuncaoPapelDbxDao getInstance() {
        if (instance == null) {
            instance = new FuncaoPapelLavendereDbxDao();
        }
        return instance;
    }

}