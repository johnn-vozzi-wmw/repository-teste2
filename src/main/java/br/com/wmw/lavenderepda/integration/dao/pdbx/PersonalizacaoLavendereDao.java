package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.Personalizacao;
import br.com.wmw.framework.integration.dao.PersonalizacaoDao;

public class PersonalizacaoLavendereDao extends PersonalizacaoDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Personalizacao();
	}

	public PersonalizacaoLavendereDao() {
		super("TBLVPPERSONALIZACAO");
	}
	
	public static PersonalizacaoDao getInstance() {
		if (instance == null) { 
			instance = new PersonalizacaoLavendereDao();
		}
		return instance;
	}

}
