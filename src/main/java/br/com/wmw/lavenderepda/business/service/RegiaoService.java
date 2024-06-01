package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.Regiao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RegiaoDbxDao;
import totalcross.util.Vector;

public class RegiaoService extends CrudService {

private static RegiaoService instance = null;
    
    private RegiaoService() {
        //--
    }
    
    public static RegiaoService getInstance() {
        if (instance == null) {
            instance = new RegiaoService();
        }
        return instance;
    }
	
	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return RegiaoDbxDao.getInstance();
	}
	
	public Vector findAllRegiaoByCdRegiao(String value) throws SQLException {
		Regiao regiao = new Regiao();
		regiao.cdRegiao = value;
		Vector regiaoList = super.findAllByExample(regiao);
		return regiaoList;
	}

	public Vector findAllRegiaoByNmRegiao(String value) throws SQLException {
		Regiao regiao = new Regiao();
		regiao.nmRegiao = value;
		Vector regiaoList = findAllByExample(regiao);
		return regiaoList;
	}
	
	public Regiao getRegiao(String cdRegiao) throws SQLException {
		Regiao regiaoFilter = new Regiao();
		regiaoFilter.cdRegiao = cdRegiao;
		BaseDomain regiao = findByPrimaryKey(regiaoFilter);
		if (regiao == null)
			return regiaoFilter;
		return (Regiao) regiao;
		
	}
	
}
