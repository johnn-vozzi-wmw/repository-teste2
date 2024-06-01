package br.com.wmw.lavenderepda.business.service;


import br.com.wmw.framework.business.service.PersonalizacaoService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PersonalizacaoLavendereDao;

public class PersonalizacaoLavendereService extends PersonalizacaoService {
	
	public static PersonalizacaoService getInstance() {
		if (instance == null) {
			instance = new PersonalizacaoLavendereService();
		}
		return instance;
	}
	
	@Override
	protected CrudDao getCrudDao() {
		return PersonalizacaoLavendereDao.getInstance();
	}
	
}
