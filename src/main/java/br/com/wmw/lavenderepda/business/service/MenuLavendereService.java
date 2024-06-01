package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.service.MenuService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MenuLavendereDbxDao;

public class MenuLavendereService extends MenuService {

    private MenuLavendereService() {
        super();
    }

    public static MenuService getInstance() {
        if (instance == null) {
            instance = new MenuLavendereService();
        }
        return instance;
    }

    protected CrudDao getCrudDao() {
        return MenuLavendereDbxDao.getInstance();
    }

}