package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.business.domain.MotivoReproCliente;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MotivoReproClienteDbxDao;

public class MotivoReproClienteService extends CrudService {

    private static MotivoReproClienteService instance = null;
    
    private MotivoReproClienteService() {
        //--
    }
    
    public static MotivoReproClienteService getInstance() {
        if (instance == null) {
            instance = new MotivoReproClienteService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return MotivoReproClienteDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {
    }

	public String getMotivoRepCliente(String cdMotivoReprovacao) throws SQLException {
		MotivoReproCliente motivoReproClienteFilter = new MotivoReproCliente();
		motivoReproClienteFilter.cdMotivoReprovacao = cdMotivoReprovacao;
		motivoReproClienteFilter = (MotivoReproCliente) findByPrimaryKey(motivoReproClienteFilter);
		return StringUtil.getStringValue(motivoReproClienteFilter);
	}

}