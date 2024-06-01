package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CestaPositCliente;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CestaPositClientePdbxDao;
import totalcross.util.Vector;

public class CestaPositClienteService extends CrudService {

    private static CestaPositClienteService instance;

    private CestaPositClienteService() {
    }

    public static CestaPositClienteService getInstance() {
        if (instance == null) {
            instance = new CestaPositClienteService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CestaPositClientePdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

	public Vector findAllCestaPositCli(String dsFiltro, String cdCampanha, String cdCesta, boolean efetivado) throws SQLException {
		CestaPositCliente cestaPositClienteFilter = new CestaPositCliente();
		boolean onlyStartString = false;
    	if (LavenderePdaConfig.usaPesquisaInicioString && !ValueUtil.isEmpty(dsFiltro)) {
    		if (dsFiltro.startsWith("*")) {
    			dsFiltro = dsFiltro.substring(1);
    		} else {
    			onlyStartString = true;
    		}
    	}
    	if (ValueUtil.isNotEmpty(dsFiltro)) {
    		cestaPositClienteFilter.nmRazaoSocial = onlyStartString ? dsFiltro + "%" : "%" + dsFiltro + "%";
    		if (LavenderePdaConfig.usaFiltroCnpjClienteListaClientes()) {
    			cestaPositClienteFilter.nuCnpjFilter = onlyStartString ? dsFiltro + "%" : "%" + dsFiltro + "%";
    		}
    	} else {
    		cestaPositClienteFilter.nmRazaoSocial = null;
    	}
    	cestaPositClienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	cestaPositClienteFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	cestaPositClienteFilter.cdCampanha = cdCampanha;
    	cestaPositClienteFilter.cdCesta = cdCesta;
    	cestaPositClienteFilter.efetivado = efetivado;
		return CestaPositClientePdbxDao.getInstance().findAllListCestaPositivacao(cestaPositClienteFilter);
	}

}