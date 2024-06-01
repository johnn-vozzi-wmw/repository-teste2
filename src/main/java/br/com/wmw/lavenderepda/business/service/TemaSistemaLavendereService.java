package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.TemaSistema;
import br.com.wmw.framework.business.service.TemaSistemaService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TemaSistemaLavendereDbxDao;

public class TemaSistemaLavendereService extends TemaSistemaService {

	private static TemaSistemaLavendereService instance;

    private TemaSistemaLavendereService() {
        super();
    }

    public static TemaSistemaLavendereService getTemaSistemaLavendereInstance() {
        if (instance == null) {
            instance = new TemaSistemaLavendereService();
        }
        return instance;
    }

    protected CrudDao getCrudDao() {
    	return TemaSistemaLavendereDbxDao.getInstance();
    }

    public TemaSistema getTemaAtual() throws SQLException {
    	return getTemaPadrao(ValueUtil.getIntegerValue(ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.temaPadraoSistema)));
    }

}