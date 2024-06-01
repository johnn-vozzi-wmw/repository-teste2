package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.DescontoCCP;
import totalcross.sql.ResultSet;

public class DescontoCCPPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescontoCCP();
	}

    public DescontoCCPPdbxDao() {
        super(DescontoCCP.TABLE_NAME);
    }

    public static DescontoCCPPdbxDao getInstance() {
    	return new DescontoCCPPdbxDao();
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLASSECLIENTE,");
        sql.append(" CDCLASSEPRODUTO,");
        sql.append(" VLPCTDESCONTO");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        DescontoCCP descontoCCP = new DescontoCCP();
        descontoCCP.rowKey = rs.getString("rowkey");
        descontoCCP.cdEmpresa = rs.getString("cdEmpresa");
        descontoCCP.cdRepresentante = rs.getString("cdRepresentante");
        descontoCCP.cdClassecliente = rs.getString("cdClassecliente");
        descontoCCP.cdClasseProduto = rs.getString("cdClasseProduto");
        descontoCCP.vlPctDesconto = ValueUtil.round(rs.getDouble("vlPctDesconto"));
        return descontoCCP;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        DescontoCCP descontoCCP = (DescontoCCP) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", descontoCCP.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", descontoCCP.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLASSECLIENTE = ", descontoCCP.cdClassecliente);
		sqlWhereClause.addAndCondition("CDCLASSEPRODUTO = ", descontoCCP.cdClasseProduto);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}