package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.AtualizacaoBd;
import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.AtualizacaoBdDbxDao;

public class AtualizacaoBdLavendereDbxDao extends AtualizacaoBdDbxDao {

	public static final String TABLE_NAME = "TBLVPATUALIZACAOBD";

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new AtualizacaoBd();
	}

	public AtualizacaoBdLavendereDbxDao() {
		super(TABLE_NAME);
	}
	
	public static AtualizacaoBdDbxDao getInstance() {
        if (instance == null) {
            instance = new AtualizacaoBdLavendereDbxDao();
        }
        return instance;
    }
}
