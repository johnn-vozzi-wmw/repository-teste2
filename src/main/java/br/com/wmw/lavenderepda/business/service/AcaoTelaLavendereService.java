package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.service.AcaoTelaService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.AcaoTelaLavendereDbxDao;

public class AcaoTelaLavendereService extends AcaoTelaService {

	public static AcaoTelaService getInstance() {
        if (instance == null) {
            instance = new AcaoTelaLavendereService();
        }
        return instance;
    }
	
	@Override
    protected CrudDao getCrudDao() {
        return AcaoTelaLavendereDbxDao.getInstance();
    }
	
}
