package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.EquipamentoDbxDao;
import br.com.wmw.lavenderepda.business.domain.EquipamentoLavendere;

public class EquipamentoLavendereDbxDao extends EquipamentoDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new EquipamentoLavendere();
	}

	public EquipamentoLavendereDbxDao() {
		super(EquipamentoLavendere.TABLE_NAME);
	}
	
	public static EquipamentoDbxDao getInstance() {
        if (instance == null) {
            instance = new EquipamentoLavendereDbxDao();
        }
        return instance;
    }

}
