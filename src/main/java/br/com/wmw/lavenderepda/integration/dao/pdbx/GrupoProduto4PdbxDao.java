package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto4;
import br.com.wmw.lavenderepda.business.domain.Restricao;
import totalcross.sql.ResultSet;

public class GrupoProduto4PdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new GrupoProduto4();
	}

    private static GrupoProduto4PdbxDao instance;

    public GrupoProduto4PdbxDao() {
        super(GrupoProduto4.TABLE_NAME);
    }
    
    public static GrupoProduto4PdbxDao getInstance() {
        if (instance == null) {
            instance = new GrupoProduto4PdbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        GrupoProduto4 grupoProduto4 = new GrupoProduto4();
        grupoProduto4.rowKey = rs.getString("rowkey");
        grupoProduto4.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
        grupoProduto4.cdGrupoProduto2 = rs.getString("cdGrupoProduto2");
        grupoProduto4.cdGrupoProduto3 = rs.getString("cdGrupoProduto3");
        grupoProduto4.cdGrupoProduto4 = rs.getString("cdGrupoProduto4");
        grupoProduto4.dsGrupoProduto4 = rs.getString("dsGrupoProduto4");
        grupoProduto4.nuCarimbo = rs.getInt("nuCarimbo");
        grupoProduto4.flTipoAlteracao = rs.getString("flTipoAlteracao");
        grupoProduto4.cdUsuario = rs.getString("cdUsuario");
        return grupoProduto4;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDGRUPOPRODUTO1,");
        sql.append(" CDGRUPOPRODUTO2,");
        sql.append(" CDGRUPOPRODUTO3,");
        sql.append(" CDGRUPOPRODUTO4,");
        sql.append(" DSGRUPOPRODUTO4,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDGRUPOPRODUTO1,");
        sql.append(" CDGRUPOPRODUTO2,");
        sql.append(" CDGRUPOPRODUTO3,");
        sql.append(" CDGRUPOPRODUTO4,");
        sql.append(" DSGRUPOPRODUTO4,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        GrupoProduto4 grupoProduto4 = (GrupoProduto4) domain;
        sql.append(Sql.getValue(grupoProduto4.cdGrupoProduto1)).append(",");
        sql.append(Sql.getValue(grupoProduto4.cdGrupoProduto2)).append(",");
        sql.append(Sql.getValue(grupoProduto4.cdGrupoProduto3)).append(",");
        sql.append(Sql.getValue(grupoProduto4.cdGrupoProduto4)).append(",");
        sql.append(Sql.getValue(grupoProduto4.dsGrupoProduto4)).append(",");
        sql.append(Sql.getValue(grupoProduto4.nuCarimbo)).append(",");
        sql.append(Sql.getValue(grupoProduto4.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(grupoProduto4.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        GrupoProduto4 grupoProduto4 = (GrupoProduto4) domain;
        sql.append(" DSGRUPOPRODUTO4 = ").append(Sql.getValue(grupoProduto4.dsGrupoProduto4)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(grupoProduto4.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(grupoProduto4.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(grupoProduto4.cdUsuario));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        GrupoProduto4 grupoProduto4 = (GrupoProduto4) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO1 = ", grupoProduto4.cdGrupoProduto1);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO2 = ", grupoProduto4.cdGrupoProduto2);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO3 = ", grupoProduto4.cdGrupoProduto3);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO4 = ", grupoProduto4.cdGrupoProduto4);
		//--
		if (grupoProduto4.filterByGrupoProdutoExistente) {
			sqlWhereClause.addAndCondition(getCdGrupoProdExistenteProdutoCondition(grupoProduto4, grupoProduto4.restricaoFilter));
		}
		
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	sql.append(" order by DSGRUPOPRODUTO4 ");
    }
    
    private void appendSqlGrupoProduto(GrupoProduto4 domainFilter, StringBuffer sql) {
		sql.append("SELECT DISTINCT PROD.cdGrupoProduto4 ");
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
		sqlWhereClause.addAndCondition(" PROD.cdGrupoProduto3 = ", domainFilter.produtoFilter.cdGrupoProduto3);
		sqlWhereClause.addAndCondition(" PROD.cdEmpresa = ", SessionLavenderePda.cdEmpresa);
		sqlWhereClause.addAndCondition(" PROD.cdRepresentante = ", SessionLavenderePda.getCdRepresentanteFiltroDados(GrupoProduto4.class));
		if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
			sqlWhereClause.addAndLikeCondition(" PROD.dsTabPrecoList", domainFilter.produtoFilter.cdTabelaPreco);
		}
		sql.append(sqlWhereClause.getSql());
	}
	
	private String getCdGrupoProdExistenteProdutoCondition(GrupoProduto4 domainFilter, Restricao restricaoFilter) {
		StringBuffer sbGrupoProdExistente = new StringBuffer();
		
		sbGrupoProdExistente.append(" CDGRUPOPRODUTO4 IN (");
		appendSqlGrupoProduto(domainFilter, sbGrupoProdExistente);
		if (domainFilter.addCteRestricao && restricaoFilter != null) {
			RestricaoDbxDao.getInstance().addConditionRestricao(sbGrupoProdExistente, restricaoFilter);
		}
		sbGrupoProdExistente.append(")");
		return sbGrupoProdExistente.toString();
	}
	
	@Override
	protected void addCTE(BaseDomain domainFilter, StringBuffer sql) {
		GrupoProduto4 grupoProduto4 = (GrupoProduto4) domainFilter;
		if (grupoProduto4.addCteRestricao) {
			RestricaoDbxDao.getInstance().appendCTERestricao(sql, grupoProduto4.restricaoFilter);
		}
	}
    
}