package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.CondComercialExcec;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CondComercialExcecDao;

public class CondComercialExcecService extends CrudService {

    private static CondComercialExcecService instance;

    public static CondComercialExcecService getInstance() {
        if (instance == null) {
            instance = new CondComercialExcecService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CondComercialExcecDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    	
    }

    public CondComercialExcec findCondComercialExcec(String cdEmpresa, String cdRepresentante, String cdProduto, String cdCondicaoComercial, String cdItemGrade1) throws SQLException {
    	CondComercialExcec condComercialExcecFilter = getDomainFilter(cdEmpresa, cdRepresentante, cdProduto, cdCondicaoComercial, cdItemGrade1);
    	if (cdEmpresa != null && cdRepresentante != null && cdProduto != null && cdCondicaoComercial != null && cdItemGrade1 != null) {
    		condComercialExcecFilter = (CondComercialExcec) findByRowKey(condComercialExcecFilter.getRowKey());
    	} else {
    		condComercialExcecFilter = null;    	
    	}
    	if (condComercialExcecFilter == null && !ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(cdItemGrade1)) {
    		condComercialExcecFilter = getDomainFilter(cdEmpresa, cdRepresentante, cdProduto, cdCondicaoComercial, ProdutoGrade.CD_ITEM_GRADE_PADRAO);
    		condComercialExcecFilter = (CondComercialExcec) findByRowKey(condComercialExcecFilter.getRowKey());
		}
    	if (condComercialExcecFilter == null) {
    		condComercialExcecFilter = new CondComercialExcec();
		}
    	return condComercialExcecFilter;
    }

	private CondComercialExcec getDomainFilter(String cdEmpresa, String cdRepresentante, String cdProduto, String cdCondicaoComercial, String cdItemGrade1) {
		CondComercialExcec condComercialExcecFilter = new CondComercialExcec();
    	condComercialExcecFilter.cdEmpresa = cdEmpresa;
    	condComercialExcecFilter.cdRepresentante = cdRepresentante;
    	condComercialExcecFilter.cdProduto = cdProduto;
    	condComercialExcecFilter.cdCondicaoComercial = cdCondicaoComercial;
    	condComercialExcecFilter.cdItemGrade1 = cdItemGrade1;
		return condComercialExcecFilter;
	}

}