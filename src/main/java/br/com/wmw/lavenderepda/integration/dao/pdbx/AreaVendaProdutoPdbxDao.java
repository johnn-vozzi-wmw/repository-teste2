package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.AreaVendaProduto;
import totalcross.sql.ResultSet;

public class AreaVendaProdutoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new AreaVendaProduto();
	}

    private static AreaVendaProdutoPdbxDao instance;

    public AreaVendaProdutoPdbxDao() {
        super(AreaVendaProduto.TABLE_NAME);
    }

    public static AreaVendaProdutoPdbxDao getInstance() {
        if (instance == null) {
            instance = new AreaVendaProdutoPdbxDao();
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
        sql.append(" cdEmpresa,");
        sql.append(" cdRepresentante,");
        sql.append(" cdAreaVenda,");
        sql.append(" cdProduto,");
        sql.append(" flTipoAlteracao");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        AreaVendaProduto areavendaproduto = new AreaVendaProduto();
        areavendaproduto.rowKey = rs.getString("rowkey");
        areavendaproduto.cdEmpresa = rs.getString("cdEmpresa");
        areavendaproduto.cdRepresentante = rs.getString("cdRepresentante");
        areavendaproduto.cdAreavenda = rs.getString("cdAreavenda");
        areavendaproduto.cdProduto = rs.getString("cdProduto");
        areavendaproduto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return areavendaproduto;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        AreaVendaProduto areavendaproduto = (AreaVendaProduto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("cdEmpresa = ", areavendaproduto.cdEmpresa);
		sqlWhereClause.addAndCondition("cdRepresentante = ", areavendaproduto.cdRepresentante);
		sqlWhereClause.addAndCondition("cdAreaVenda = ", areavendaproduto.cdAreavenda);
		sqlWhereClause.addAndCondition("cdProduto = ", areavendaproduto.cdProduto);
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