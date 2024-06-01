package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.DescProgFaixa;
import totalcross.sql.ResultSet;

public class DescProgFaixaDbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescProgFaixa();
	}

    private static DescProgFaixaDbxDao instance;

    public DescProgFaixaDbxDao() { super(DescProgFaixa.TABLE_NAME); }
    public static DescProgFaixaDbxDao getInstance() { return (instance == null) ? instance = new DescProgFaixaDbxDao() : instance; }
	@Override protected void addUpdateValues(BaseDomain baseDomain, StringBuffer stringBuffer) throws SQLException {}
	@Override protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { addSelectColumns(null, sql); }
	@Override protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException { return populate(domainFilter, rs); }

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
	    DescProgFaixa descProgFaixa = new DescProgFaixa();
	    descProgFaixa.rowKey = rs.getString("rowkey");
	    descProgFaixa.cdEmpresa = rs.getString("cdEmpresa");
	    descProgFaixa.cdRepresentante = rs.getString("cdRepresentante");
	    descProgFaixa.cdFaixaCli = rs.getString("cdFaixaCli");
	    descProgFaixa.dsFaixaCli = rs.getString("dsFaixaCli");
	    descProgFaixa.cdUsuario = rs.getString("cdUsuario");
        return descProgFaixa;
    }

	@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
	    sql.append(" tb.rowkey,");
	    sql.append(" tb.CDEMPRESA,");
	    sql.append(" tb.CDREPRESENTANTE,");
	    sql.append(" tb.CDFAIXACLI,");
	    sql.append(" tb.DSFAIXACLI,");
	    sql.append(" tb.CDUSUARIO");
    }

	@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
		DescProgFaixa descProgFaixa = (DescProgFaixa) domain;
	    SqlWhereClause sqlWhereClause = new SqlWhereClause();
	    sqlWhereClause.addAndCondition("CDEMPRESA = ", descProgFaixa.cdEmpresa);
	    sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", descProgFaixa.cdRepresentante);
	    sqlWhereClause.addAndCondition("CDFAIXACLI  = ", descProgFaixa.cdFaixaCli);
		sql.append(sqlWhereClause.getSql());
    }

}