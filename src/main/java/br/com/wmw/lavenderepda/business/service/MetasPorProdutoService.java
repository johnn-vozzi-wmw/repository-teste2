package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.MetasPorProduto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MetasPorProdutoPdbxDao;
import totalcross.util.Vector;

public class MetasPorProdutoService extends CrudService {

    private static MetasPorProdutoService instance;

    private MetasPorProdutoService() {
        //--
    }

    public static MetasPorProdutoService getInstance() {
        if (instance == null) {
            instance = new MetasPorProdutoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return MetasPorProdutoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public Vector getMetasProduto(String filtro, String cdRep, String dsPeriodo) throws SQLException {
		return findAllByExampleSummary(populeMetasProdutoForFilter(filtro, cdRep, dsPeriodo));
    }

    public MetasPorProduto populeMetasProdutoForFilter(String filtro, String cdRep, String dsPeriodo) {
    	MetasPorProduto metasPorProdutoFilter = new MetasPorProduto();
    	metasPorProdutoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	metasPorProdutoFilter.findDescProduto = true;
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		metasPorProdutoFilter.cdRepresentante = cdRep;
    	} else {
    		metasPorProdutoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	}
    	if (!ValueUtil.isEmpty(dsPeriodo)) {
    		metasPorProdutoFilter.dsPeriodo = dsPeriodo;
    	}
    	if (!ValueUtil.isEmpty(filtro)) {
    		metasPorProdutoFilter.cdProdutoLike = "%" + filtro + "%";
    	}
    	return metasPorProdutoFilter;
    }
    
    public Vector findAllDistinctPeriodo() throws SQLException {
    	MetasPorProduto metasPorProdutoFilter = new MetasPorProduto();
    	metasPorProdutoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	metasPorProdutoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(MetasPorProduto.class);
    	return MetasPorProdutoPdbxDao.getInstance().findAllDistinctPeriodo(metasPorProdutoFilter);
    }
}