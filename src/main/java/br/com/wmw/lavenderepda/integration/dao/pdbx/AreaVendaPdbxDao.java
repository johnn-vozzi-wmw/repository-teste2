package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.AreaVenda;
import totalcross.sql.ResultSet;

public class AreaVendaPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new AreaVenda();
	}

    private static AreaVendaPdbxDao instance;

    public AreaVendaPdbxDao() {
        super(AreaVenda.TABLE_NAME);
    }

    public static AreaVendaPdbxDao getInstance() {
        if (instance == null) {
            instance = new AreaVendaPdbxDao();
        }
        return instance;
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
        sql.append(" cdEmpresa,");
        sql.append(" cdRepresentante,");
        sql.append(" cdAreaVenda,");
        sql.append(" dsAreaVenda,");
        sql.append(" flTipoAlteracao");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        AreaVenda areavenda = new AreaVenda();
        areavenda.rowKey = rs.getString("rowkey");
        areavenda.cdEmpresa = rs.getString("cdEmpresa");
        areavenda.cdRepresentante = rs.getString("cdRepresentante");
        areavenda.cdAreavenda = rs.getString("cdAreavenda");
        areavenda.dsAreavenda = rs.getString("dsAreavenda");
        areavenda.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return areavenda;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        AreaVenda areavenda = (AreaVenda) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("cdEmpresa = ", areavenda.cdEmpresa);
		sqlWhereClause.addAndCondition("cdRepresentante = ", areavenda.cdRepresentante);
		sqlWhereClause.addAndCondition("cdAreaVenda = ", areavenda.cdAreavenda);
		//--
		sql.append(sqlWhereClause.getSql());
    }

    public String[] findCdsAreaVendaByExample(BaseDomain domain) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT CDAREAVENDA FROM ");
    	sql.append(tableName);
    	addWhereByExample(domain, sql);
    	return getCurrentDriver().getStrings1(sql.toString());
    }

}