package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.AtendimentoAtiv;
import br.com.wmw.lavenderepda.business.domain.TipoSacAtividade;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoSacAtividadeDao;

public class TipoSacAtividadeService extends CrudService {

    private static TipoSacAtividadeService instance;
    
    private TipoSacAtividadeService() {
        //--
    }
    
    public static TipoSacAtividadeService getInstance() {
        if (instance == null) {
            instance = new TipoSacAtividadeService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return TipoSacAtividadeDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
    public String getDsAtividade(AtendimentoAtiv atendimentoAtiv) throws SQLException {
    	TipoSacAtividade tipoSacAtividadeFilter = new TipoSacAtividade();
    	tipoSacAtividadeFilter.cdEmpresa = atendimentoAtiv.cdEmpresa;
    	tipoSacAtividadeFilter.cdTipoSac = atendimentoAtiv.cdTipoSac;
    	tipoSacAtividadeFilter.cdAtividade = atendimentoAtiv.cdAtividadeSac;
    	return findColumnByRowKey(tipoSacAtividadeFilter.getRowKey(), "DSTITULO");
    }
    
}