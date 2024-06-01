package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto1;
import br.com.wmw.lavenderepda.business.domain.MetasPorGrupoProduto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MetasPorGrupoProdutoPdbxDao;
import totalcross.util.Vector;

public class MetasPorGrupoProdutoService extends CrudService {

    private static MetasPorGrupoProdutoService instance;

    private MetasPorGrupoProdutoService() {
        //--
    }

    public static MetasPorGrupoProdutoService getInstance() {
        if (instance == null) {
            instance = new MetasPorGrupoProdutoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return MetasPorGrupoProdutoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public Vector findAllMetasByGrupoProduto(MetasPorGrupoProduto metasPorGrupoProduto1Filter, int nuNivel) throws SQLException {
    	return MetasPorGrupoProdutoPdbxDao.getInstance().findAllMetasByGrupoProduto(metasPorGrupoProduto1Filter, nuNivel);
    }
    
    public Vector findMetasByGrupoProduto(MetasPorGrupoProduto metasPorGrupoProduto1Filter, int nuNivel) throws SQLException {
    	return MetasPorGrupoProdutoPdbxDao.getInstance().findMetasByGrupoProduto(metasPorGrupoProduto1Filter, nuNivel);
    }
    
    public Vector findAllMetasVigentes(MetasPorGrupoProduto metasPorGrupoProdutoFilter) throws SQLException {
    	return MetasPorGrupoProdutoPdbxDao.getInstance().findAllMetasVigentes(metasPorGrupoProdutoFilter);
    }
 
    public String getDescricaoCodigoMeta(String descricao, String codigo) {
    	GrupoProduto1 grupo = new GrupoProduto1();
    	grupo.dsGrupoproduto1 = descricao;
    	grupo.cdGrupoproduto1 = codigo;
    	return grupo.toString();
    }
    
    public Vector findAllDistinctPeriodo() throws SQLException {
    	MetasPorGrupoProduto metasPorGrupoProdutoFilter = new MetasPorGrupoProduto();
    	metasPorGrupoProdutoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	return MetasPorGrupoProdutoPdbxDao.getInstance().findAllDistinctPeriodo(metasPorGrupoProdutoFilter);
    }
    
}