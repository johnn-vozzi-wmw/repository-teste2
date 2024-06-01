package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.CondComCondPagto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CondComCondPagtoDbxDao;

public class CondComCondPagtoService extends CrudService {

    private static CondComCondPagtoService instance;

    private CondComCondPagtoService() {
        //--
    }

    public static CondComCondPagtoService getInstance() {
        if (instance == null) {
            instance = new CondComCondPagtoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CondComCondPagtoDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public CondComCondPagto getCondComCondPagto(String cdCondicaoComercial, String cdCondicaoPagamento) throws SQLException {
    	CondComCondPagto condComCondPagto = new CondComCondPagto();
    	condComCondPagto.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	condComCondPagto.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondComCondPagto.class);
    	condComCondPagto.cdCondicaoComercial = cdCondicaoComercial;
    	condComCondPagto.cdCondicaoPagamento = cdCondicaoPagamento;
    	return (CondComCondPagto)CondComCondPagtoService.getInstance().findByRowKey(condComCondPagto.getRowKey());
    }




}