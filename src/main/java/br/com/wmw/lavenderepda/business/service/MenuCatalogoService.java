package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.MenuCatalogo;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MenuCatalogoDbxDao;
import totalcross.util.Vector;

import java.sql.SQLException;

public class MenuCatalogoService extends CrudService {

    private static MenuCatalogoService instance;

    public static MenuCatalogoService getInstance() {
        if (instance == null) {
            instance = new MenuCatalogoService();
        }
        return instance;
    }

    @Override
    public void validate(BaseDomain domain) throws SQLException {
    }

    @Override
    protected CrudDao getCrudDao() {
        return MenuCatalogoDbxDao.getInstance();
    }

    public Vector findRegistrosEntidadeSql(MenuCatalogo menuCatalogo) throws SQLException {
        return MenuCatalogoDbxDao.getInstance().findRegistrosEntidadeSql(menuCatalogo);
    }

    public int countRegistrosEntidadeSql(MenuCatalogo menuCatalogo) throws SQLException {
        return MenuCatalogoDbxDao.getInstance().countRegistrosEntidadeSql(menuCatalogo);
    }

    public MenuCatalogo findEntidadePrimeiroNivel(String cdFuncionalidade) throws SQLException {
        return MenuCatalogoDbxDao.getInstance().findEntidadePrimeiroNivel(cdFuncionalidade);
    }

    public Vector findAllEntidadesAgrupadas() throws SQLException {
        return MenuCatalogoDbxDao.getInstance().findAllEntidadesAgrupadas();
    }
    
    public Vector findAllMenuCatalogoIndividual(String cdFuncionalidade) throws SQLException {
        MenuCatalogo filter = new MenuCatalogo();
        filter.cdFuncionalidade = cdFuncionalidade;
        filter.flIndividual = ValueUtil.VALOR_SIM;
        return MenuCatalogoDbxDao.getInstance().findAllByExample(filter);
    }

}
