package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.CestaProduto;
import totalcross.sql.ResultSet;

public class CestaProdutoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CestaProduto();
	}

    private static CestaProdutoPdbxDao instance;

    public CestaProdutoPdbxDao() {
        super(CestaProduto.TABLE_NAME);
    }

    public static CestaProdutoPdbxDao getInstance() {
        if (instance == null) {
            instance = new CestaProdutoPdbxDao();
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
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCESTA,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOALTERACAO");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        CestaProduto cestaproduto = new CestaProduto();
        cestaproduto.rowKey = rs.getString("rowkey");
        cestaproduto.cdEmpresa = rs.getString("cdEmpresa");
        cestaproduto.cdRepresentante = rs.getString("cdRepresentante");
        cestaproduto.cdCesta = rs.getString("cdCesta");
        cestaproduto.cdProduto = rs.getString("cdProduto");
        cestaproduto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return cestaproduto;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CestaProduto cestaproduto = (CestaProduto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", cestaproduto.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", cestaproduto.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCESTA = ", cestaproduto.cdCesta);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", cestaproduto.cdProduto);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}