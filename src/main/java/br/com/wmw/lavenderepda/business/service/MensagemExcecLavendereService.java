package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.service.MensagemExcecService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MensagemExcecLavendereDao;

public class MensagemExcecLavendereService extends MensagemExcecService {
	
	public static MensagemExcecService getInstance() {
        if (instance == null) {
            instance = new MensagemExcecLavendereService();
        }
        return instance;
    }

    protected CrudDao getCrudDao() {
        return MensagemExcecLavendereDao.getInstance();
    }

}
