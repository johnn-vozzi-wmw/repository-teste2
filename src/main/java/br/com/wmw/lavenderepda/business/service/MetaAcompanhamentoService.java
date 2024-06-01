package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Meta;
import br.com.wmw.lavenderepda.business.domain.MetaAcompanhamento;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MetaAcompanhamentoDbxDao;

public class MetaAcompanhamentoService extends CrudService {

    private static MetaAcompanhamentoService instance;

    private MetaAcompanhamentoService() {
        //--
    }

    public static MetaAcompanhamentoService getInstance() {
        if (instance == null) {
            instance = new MetaAcompanhamentoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return MetaAcompanhamentoDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
    public double getVlRealizadoMeta(String cdRepresentante) throws SQLException {
		MetaAcompanhamento metaAcompanhamento = new MetaAcompanhamento();
		metaAcompanhamento.cdRepresentante = cdRepresentante;
		metaAcompanhamento.cdEmpresa = SessionLavenderePda.cdEmpresa;
		//--
		return sumByExample(metaAcompanhamento, "vlRealizadoMeta");
    }
    
	public void getMetaAcompanhamentoList(Meta meta) throws SQLException {
		MetaAcompanhamento metaAcompanhamentoFilter = new MetaAcompanhamento();
		metaAcompanhamentoFilter.cdEmpresa = meta.cdEmpresa;
		metaAcompanhamentoFilter.cdRepresentante = meta.cdRepresentante;
		metaAcompanhamentoFilter.dsPeriodo = meta.dsPeriodo;
		meta.metaAcompanhamentoList = MetaAcompanhamentoService.getInstance().findAllByExample(metaAcompanhamentoFilter);
	}

}