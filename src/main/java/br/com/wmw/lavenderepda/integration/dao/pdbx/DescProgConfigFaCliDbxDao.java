package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.DescProgConfigFaCli;
import br.com.wmw.lavenderepda.business.domain.DescProgFaixa;
import totalcross.sql.ResultSet;

public class DescProgConfigFaCliDbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescProgConfigFaCli();
	}

    private static DescProgConfigFaCliDbxDao instance;

    public DescProgConfigFaCliDbxDao() { super(DescProgFaixa.TABLE_NAME); }
    public static DescProgConfigFaCliDbxDao getInstance() { return (instance == null) ? instance = new DescProgConfigFaCliDbxDao() : instance; }
	@Override protected void addUpdateValues(BaseDomain baseDomain, StringBuffer stringBuffer) throws SQLException {}
	@Override protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { addSelectColumns(null, sql); }
	@Override protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException { return populate(domainFilter, rs); }

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
	    DescProgConfigFaCli descProgConfigFaCli = new DescProgConfigFaCli();
	    descProgConfigFaCli.rowKey = rs.getString("rowkey");
	    descProgConfigFaCli.cdEmpresa = rs.getString("cdEmpresa");
	    descProgConfigFaCli.cdRepresentante = rs.getString("cdRepresentante");
	    descProgConfigFaCli.cdDescProgressivo = rs.getString("cdDescProgressivo");
	    descProgConfigFaCli.cdFaixaCli = rs.getString("cdFaixaCli");
	    descProgConfigFaCli.cdCliente = rs.getString("cdCliente");
	    descProgConfigFaCli.cdUsuario = rs.getString("cdUsuario");
        return descProgConfigFaCli;
    }

	@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
	    sql.append(" tb.rowkey,");
	    sql.append(" tb.CDEMPRESA,");
	    sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDDESCPROGRESSIVO,");
	    sql.append(" tb.CDFAIXACLI,");
	    sql.append(" tb.CDCLIENTE,");
	    sql.append(" tb.CDUSUARIO");
    }

	@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
		DescProgConfigFaCli descProgConfigFaCli = (DescProgConfigFaCli) domain;
	    SqlWhereClause sqlWhereClause = new SqlWhereClause();
	    sqlWhereClause.addAndCondition("CDEMPRESA = ", descProgConfigFaCli.cdEmpresa);
	    sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", descProgConfigFaCli.cdRepresentante);
		sqlWhereClause.addAndCondition("CDDESCPROGRESSIVO = ", descProgConfigFaCli.cdDescProgressivo);
	    sqlWhereClause.addAndCondition("CDFAIXACLI  = ", descProgConfigFaCli.cdFaixaCli);
		sqlWhereClause.addAndCondition("CDCLIENTE  = ", descProgConfigFaCli.cdCliente);
		sql.append(sqlWhereClause.getSql());
    }

}