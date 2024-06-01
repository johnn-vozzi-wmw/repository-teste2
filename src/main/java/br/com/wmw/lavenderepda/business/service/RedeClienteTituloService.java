package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RedeClienteTituloPdbxDao;

public class RedeClienteTituloService extends CrudPersonLavendereService {

    private static RedeClienteTituloService instance;

    private RedeClienteTituloService() {}

    public static RedeClienteTituloService getInstance() {
    	return (instance == null) ? instance = new RedeClienteTituloService() : instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return RedeClienteTituloPdbxDao.getInstance();
    }
	
}