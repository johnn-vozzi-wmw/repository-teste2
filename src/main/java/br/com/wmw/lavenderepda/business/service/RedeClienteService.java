package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.Rede;
import br.com.wmw.lavenderepda.business.domain.RedeCliente;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RedeClientePdbxDao;

public class RedeClienteService extends CrudPersonLavendereService {

    private static RedeClienteService instance;

    public static RedeClienteService getInstance() {
        if (instance == null) {
            instance = new RedeClienteService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return RedeClientePdbxDao.getInstance();	
    }

    //--------------------------------------------------------------------------------------------------------------
    // Validações
    //--------------------------------------------------------------------------------------------------------------

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
	public int getQtClientesAtrasoByRede(Rede rede) throws SQLException {
		if (rede == null) return 0;
		return countByExample(getRedeClienteFilter(rede));
	}

	private RedeCliente getRedeClienteFilter(Rede rede) {
		RedeCliente redeClienteFilter = new RedeCliente();
		redeClienteFilter.cdRede = rede.cdRede;
		redeClienteFilter.flStatusClienteFilter = RedeCliente.FLSTATUSCLIENTE_ATRASADO;
		return redeClienteFilter;
	}
    
    
}