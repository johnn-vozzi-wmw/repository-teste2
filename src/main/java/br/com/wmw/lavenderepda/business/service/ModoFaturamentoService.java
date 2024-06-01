package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.ModoFaturamento;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ModoFaturamentoDbxDao;

public class ModoFaturamentoService extends CrudService {

    private static ModoFaturamentoService instance;

    private ModoFaturamentoService() {
    	//--
    }
    
    public static ModoFaturamentoService getInstance() {
        if (instance == null) {
            instance = new ModoFaturamentoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ModoFaturamentoDbxDao.getInstance();
    }

	//@Override
	public void validate(BaseDomain domain) throws SQLException {}

	public ModoFaturamento findModoFaturamentoByCdModoFaturamento(String cdModoFaturamento) throws SQLException {
		ModoFaturamento modoFaturamentoRef = new ModoFaturamento(cdModoFaturamento);
		return (ModoFaturamento) findByPrimaryKey(modoFaturamentoRef);
	}
}