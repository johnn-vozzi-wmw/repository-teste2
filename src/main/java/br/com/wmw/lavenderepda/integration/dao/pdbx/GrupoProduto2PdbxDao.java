package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto2;
import br.com.wmw.lavenderepda.business.domain.Restricao;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;

public class GrupoProduto2PdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new GrupoProduto2();
	}

    private static GrupoProduto2PdbxDao instance;

    public GrupoProduto2PdbxDao() {
        super(GrupoProduto2.TABLE_NAME);
    }

    public static GrupoProduto2PdbxDao getInstance() {
        if (instance == null) {
            instance = new GrupoProduto2PdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDGRUPOPRODUTO1,");
        sql.append(" CDGRUPOPRODUTO2,");
        sql.append(" DSGRUPOPRODUTO2,");
        sql.append(" FLBLOQUEIAVENDAPORMETA");
    }

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        GrupoProduto2 grupoProduto2 = new GrupoProduto2();
        grupoProduto2.rowKey = rs.getString("rowkey");
        grupoProduto2.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
        grupoProduto2.cdGrupoProduto2 = rs.getString("cdGrupoProduto2");
        grupoProduto2.dsGrupoProduto2 = rs.getString("dsGrupoProduto2");
        grupoProduto2.flBloqueiaVendaPorMeta  = rs.getString("flBloqueiaVendaPorMeta");
        return grupoProduto2;
    }

    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        GrupoProduto2 grupoProduto2 = (GrupoProduto2) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO1 = ", grupoProduto2.cdGrupoProduto1);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO2 = ", grupoProduto2.cdGrupoProduto2);
		sqlWhereClause.addAndCondition("FLBLOQUEIAVENDAPORMETA = ", grupoProduto2.flBloqueiaVendaPorMeta);
		
		if(grupoProduto2.grupoProdTipoPedFilter != null && LavenderePdaConfig.filtraGrupoProdutoPorTipoPedido) {
			sqlWhereClause.addAndCondition(DaoUtil.getExistsGrupoProdTipoPedCondition(grupoProduto2.grupoProdTipoPedFilter, 2));
		}
		if (grupoProduto2.filterByGrupoProdutoExistente) {
			sqlWhereClause.addAndCondition(getCdGrupoProdExistenteProdutoCondition(grupoProduto2, grupoProduto2.restricaoFilter));
		}
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	sql.append(" order by DSGRUPOPRODUTO2 ");
    }
    
	private void appendSqlGrupoProduto(GrupoProduto2 domainFilter, StringBuffer sql) {
		sql.append("SELECT DISTINCT PROD.cdGrupoProduto2 ");
		sql.append("from ");
		if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
			sql.append("TBLVPPRODUTOTABPRECO");
		} else {
			sql.append("TBLVPPRODUTO");
		}
		sql.append(" PROD ");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" PROD.cdGrupoProduto1 = ", domainFilter.produtoFilter.cdGrupoProduto1);
		sqlWhereClause.addAndCondition(" PROD.cdEmpresa = ", SessionLavenderePda.cdEmpresa);
		sqlWhereClause.addAndCondition(" PROD.cdRepresentante = ", SessionLavenderePda.getCdRepresentanteFiltroDados(GrupoProduto2.class));
		if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
			sqlWhereClause.addAndLikeCondition(" PROD.dsTabPrecoList", domainFilter.produtoFilter.cdTabelaPreco);
		}
		sql.append(sqlWhereClause.getSql());
	}
	
	private String getCdGrupoProdExistenteProdutoCondition(GrupoProduto2 domainFilter, Restricao restricaoFilter) {
		StringBuffer sbGrupoProdExistente = new StringBuffer();
		
		sbGrupoProdExistente.append(" CDGRUPOPRODUTO2 IN (");
		appendSqlGrupoProduto(domainFilter, sbGrupoProdExistente);
		if (domainFilter.addCteRestricao && restricaoFilter != null) {
			RestricaoDbxDao.getInstance().addConditionRestricao(sbGrupoProdExistente, restricaoFilter);
		}
		sbGrupoProdExistente.append(")");
		return sbGrupoProdExistente.toString();
	}
	
	@Override
	protected void addCTE(BaseDomain domainFilter, StringBuffer sql) {
		GrupoProduto2 grupoProduto2 = (GrupoProduto2) domainFilter;
		if (grupoProduto2.addCteRestricao) {
			RestricaoDbxDao.getInstance().appendCTERestricao(sql, grupoProduto2.restricaoFilter);
		}
	}

}