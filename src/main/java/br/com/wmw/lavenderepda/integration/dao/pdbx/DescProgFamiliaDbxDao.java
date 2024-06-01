package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.DescProgFamilia;
import totalcross.sql.ResultSet;

public class DescProgFamiliaDbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescProgFamilia();
	}

    private static DescProgFamiliaDbxDao instance;

    public DescProgFamiliaDbxDao() { super(DescProgFamilia.TABLE_NAME); }
    public static DescProgFamiliaDbxDao getInstance() { return (instance == null) ? instance = new DescProgFamiliaDbxDao() : instance; }
	@Override protected void addUpdateValues(BaseDomain baseDomain, StringBuffer stringBuffer) throws SQLException {}
	@Override protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { addSelectColumns(null, sql); }
	@Override protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException { return populate(domainFilter, rs); }

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
	    DescProgFamilia descProgFamilia = new DescProgFamilia();
	    descProgFamilia.rowKey = rs.getString("rowkey");
	    descProgFamilia.cdEmpresa = rs.getString("cdEmpresa");
	    descProgFamilia.cdRepresentante = rs.getString("cdRepresentante");
	    descProgFamilia.cdFamiliaDescProg = rs.getString("cdFamiliaDescProg");
	    descProgFamilia.dsFamiliaProd = rs.getString("dsFamiliaProd");
	    descProgFamilia.cdUsuario = rs.getString("cdUsuario");
        return descProgFamilia;
    }

	@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
	    sql.append(" tb.rowkey,");
	    sql.append(" tb.CDEMPRESA,");
	    sql.append(" tb.CDREPRESENTANTE,");
	    sql.append(" tb.CDFAMILIADESCPROG,");
	    sql.append(" tb.DSFAMILIAPROD,");
	    sql.append(" tb.CDUSUARIO");
    }

	@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
		DescProgFamilia descProgFamilia = (DescProgFamilia) domain;
	    SqlWhereClause sqlWhereClause = new SqlWhereClause();
	    sqlWhereClause.addAndCondition("CDEMPRESA = ", descProgFamilia.cdEmpresa);
	    sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", descProgFamilia.cdRepresentante);
	    sqlWhereClause.addAndCondition("CDFAMILIADESCPROG  = ", descProgFamilia.cdFamiliaDescProg);
		sql.append(sqlWhereClause.getSql());
    }

}