package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.EquipamentoUsuarioDbxDao;
import br.com.wmw.lavenderepda.business.domain.EquipamentoUsuarioLavendere;

public class EquipamentoUsuarioLavendereDbxDao extends EquipamentoUsuarioDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new EquipamentoUsuarioLavendere();
	}
	
	public EquipamentoUsuarioLavendereDbxDao() {
        super(EquipamentoUsuarioLavendere.TABLE_NAME);
    }

    public static EquipamentoUsuarioDbxDao getInstance() {
        if (instance == null) {
            instance = new EquipamentoUsuarioLavendereDbxDao();
        }
        return instance;
    }

}
