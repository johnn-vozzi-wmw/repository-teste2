package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto3;
import br.com.wmw.lavenderepda.business.domain.Restricao;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;

public class GrupoProduto3PdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new GrupoProduto3();
	}

    private static GrupoProduto3PdbxDao instance;

    public GrupoProduto3PdbxDao() {
        super(GrupoProduto3.TABLE_NAME);
    }

    public static GrupoProduto3PdbxDao getInstance() {
        if (instance == null) {
            instance = new GrupoProduto3PdbxDao();
        }
        return instance;
    }

    @Override
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
        sql.append(" CDGRUPOPRODUTO3,");
        sql.append(" DSGRUPOPRODUTO3,");
        sql.append(" FLBLOQUEIAVENDAPORMETA");
    }

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        GrupoProduto3 grupoProduto3 = new GrupoProduto3();
        grupoProduto3.rowKey = rs.getString("rowkey");
        grupoProduto3.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
        grupoProduto3.cdGrupoProduto2 = rs.getString("cdGrupoProduto2");
        grupoProduto3.cdGrupoProduto3 = rs.getString("cdGrupoProduto3");
        grupoProduto3.dsGrupoProduto3 = rs.getString("dsGrupoProduto3");
        grupoProduto3.flBloqueiaVendaPorMeta  = rs.getString("flBloqueiaVendaPorMeta");
        return grupoProduto3;
    }

    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        GrupoProduto3 grupoProduto3 = (GrupoProduto3) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO1 = ", grupoProduto3.cdGrupoProduto1);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO2 = ", grupoProduto3.cdGrupoProduto2);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO3 = ", grupoProduto3.cdGrupoProduto3);
		sqlWhereClause.addAndCondition("FLBLOQUEIAVENDAPORMETA = ", grupoProduto3.flBloqueiaVendaPorMeta);
		//--
		if (LavenderePdaConfig.filtraGrupoProdutoPorTipoPedido && grupoProduto3.grupoProdTipoPedFilter != null) {
			sqlWhereClause.addAndCondition(DaoUtil.getExistsGrupoProdTipoPedCondition(grupoProduto3.grupoProdTipoPedFilter, 3));
		}
		if (grupoProduto3.filterByGrupoProdutoExistente) {
			sqlWhereClause.addAndCondition(getCdGrupoProdExistenteProdutoCondition(grupoProduto3, grupoProduto3.restricaoFilter));
		}
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	sql.append(" order by DSGRUPOPRODUTO3 ");
    }
    
	private void appendSqlGrupoProduto(GrupoProduto3 domainFilter, StringBuffer sql) {
		sql.append("SELECT DISTINCT PROD.cdGrupoProduto3 ");
		sql.append("from ");
		if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
			sql.append("TBLVPPRODUTOTABPRECO");
		} else {
			sql.append("TBLVPPRODUTO");
		}
		sql.append(" PROD ");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" PROD.cdGrupoProduto1 = ", domainFilter.produtoFilter.cdGrupoProduto1);
		sqlWhereClause.addAndCondition(" PROD.cdGrupoProduto2 = ", domainFilter.produtoFilter.cdGrupoProduto2);
		sqlWhereClause.addAndCondition(" PROD.cdEmpresa = ", SessionLavenderePda.cdEmpresa);
		sqlWhereClause.addAndCondition(" PROD.cdRepresentante = ", SessionLavenderePda.getCdRepresentanteFiltroDados(GrupoProduto3.class));
		if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
			sqlWhereClause.addAndLikeCondition(" PROD.dsTabPrecoList", domainFilter.produtoFilter.cdTabelaPreco);
		}
		sql.append(sqlWhereClause.getSql());
	}
	
	private String getCdGrupoProdExistenteProdutoCondition(GrupoProduto3 domainFilter, Restricao restricaoFilter) {
		StringBuffer sbGrupoProdExistente = new StringBuffer();
		
		sbGrupoProdExistente.append(" CDGRUPOPRODUTO3 IN (");
		appendSqlGrupoProduto(domainFilter, sbGrupoProdExistente);
		if (domainFilter.addCteRestricao && restricaoFilter != null) {
			RestricaoDbxDao.getInstance().addConditionRestricao(sbGrupoProdExistente, restricaoFilter);
		}
		sbGrupoProdExistente.append(")");
		return sbGrupoProdExistente.toString();
	}
	
	@Override
	protected void addCTE(BaseDomain domainFilter, StringBuffer sql) {
		GrupoProduto3 grupoProduto3 = (GrupoProduto3) domainFilter;
		if (grupoProduto3.addCteRestricao) {
			RestricaoDbxDao.getInstance().appendCTERestricao(sql, grupoProduto3.restricaoFilter);
		}
	}

}