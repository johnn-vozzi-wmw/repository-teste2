package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.Menu;
import br.com.wmw.framework.integration.dao.MenuDbxDao;

public class MenuLavendereDbxDao extends MenuDbxDao {

	public static final String TABLE_NAME_LVP = "TBLVPMENU"; 

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Menu();
	}

	public MenuLavendereDbxDao() {
		super(TABLE_NAME_LVP);
	}

    public static MenuDbxDao getInstance() {
        if (instance == null) {
            instance = new MenuLavendereDbxDao();
        }
        return instance;
    }

}