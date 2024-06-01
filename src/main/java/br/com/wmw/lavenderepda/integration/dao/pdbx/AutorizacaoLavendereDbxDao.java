package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.Autorizacao;
import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.AutorizacaoDbxDao;

public class AutorizacaoLavendereDbxDao extends AutorizacaoDbxDao {

	public static final String TABLE_NAME_LVP = "TBLVPAUTORIZACAO"; 

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Autorizacao();
	}

	public AutorizacaoLavendereDbxDao() {
		super(TABLE_NAME_LVP);
	}

    public static AutorizacaoDbxDao getInstance() {
        if (instance == null) {
            instance = new AutorizacaoLavendereDbxDao();
        }
        return instance;
    }

}