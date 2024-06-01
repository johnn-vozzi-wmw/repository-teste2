package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.AtributoProd;
import totalcross.sql.ResultSet;

public class AtributoProdPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new AtributoProd();
	}

    private static AtributoProdPdbxDao instance;

    public AtributoProdPdbxDao() {
        super(AtributoProd.TABLE_NAME);
    }

    public static AtributoProdPdbxDao getInstance() {
        if (instance == null) {
            instance = new AtributoProdPdbxDao();
        }
        return instance;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDATRIBUTOPRODUTO,");
        sql.append(" DSATRIBUTOPRODUTO,");
        sql.append(" FLTIPOALTERACAO");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        AtributoProd atributoProd = new AtributoProd();
        atributoProd.rowKey = rs.getString("rowkey");
        atributoProd.cdEmpresa = rs.getString("cdEmpresa");
        atributoProd.cdRepresentante = rs.getString("cdRepresentante");
        atributoProd.cdAtributoProduto = rs.getString("cdAtributoProduto");
        atributoProd.dsAtributoProduto = rs.getString("dsAtributoProduto");
        atributoProd.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return atributoProd;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        AtributoProd atributoProd = (AtributoProd) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", atributoProd.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", atributoProd.cdRepresentante);
		sqlWhereClause.addAndCondition("CDATRIBUTOPRODUTO = ", atributoProd.cdAtributoProduto);
		//--
		sql.append(sqlWhereClause.getSql());
    }

    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		sql.append(" order by dsAtributoProduto");
    }

}