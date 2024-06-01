package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.DescProgConfigFam;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescProgConfigFamDbxDao;
import totalcross.util.Vector;

public class DescProgConfigFamService extends CrudService {

    private static DescProgConfigFamService instance;

    private DescProgConfigFamService() {}
    
	public static DescProgConfigFamService getInstance() { 
		return instance == null ? instance = new DescProgConfigFamService() : instance; 
	}

	@Override 
	protected CrudDao getCrudDao() { 
		return DescProgConfigFamDbxDao.getInstance(); 
	}
	
	@Override 
	public void validate(BaseDomain domain) {}
	
	public Vector findAllFamByDescProg(DescProgConfigFam descProgConfigFam) throws SQLException { 
		return DescProgConfigFamDbxDao.getInstance().findAllFamByDescProg(descProgConfigFam);
	}
	
}