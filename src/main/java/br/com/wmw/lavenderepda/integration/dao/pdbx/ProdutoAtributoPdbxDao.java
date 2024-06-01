package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ProdutoAtributo;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ProdutoAtributoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProdutoAtributo();
	}

    private static ProdutoAtributoPdbxDao instance;

    public ProdutoAtributoPdbxDao() {
        super(ProdutoAtributo.TABLE_NAME);
    }

    public static ProdutoAtributoPdbxDao getInstance() {
        if (instance == null) {
            instance = new ProdutoAtributoPdbxDao();
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
        sql.append(" CDATRIBUTOPRODUTO,");
        sql.append(" CDATRIBUTOOPCAOPRODUTO,");
        sql.append(" CDPRODUTO");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ProdutoAtributo produtoAtributo = new ProdutoAtributo();
        produtoAtributo.rowKey = rs.getString("rowkey");
        produtoAtributo.cdEmpresa = rs.getString("cdEmpresa");
        produtoAtributo.cdRepresentante = rs.getString("cdRepresentante");
        produtoAtributo.cdAtributoProduto = rs.getString("cdAtributoProduto");
        produtoAtributo.cdAtributoOpcaoProduto = rs.getString("cdAtributoOpcaoProduto");
        produtoAtributo.cdProduto = rs.getString("cdProduto");
        return produtoAtributo;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoAtributo produtoAtributo = (ProdutoAtributo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", produtoAtributo.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", produtoAtributo.cdRepresentante);
		sqlWhereClause.addAndCondition("CDATRIBUTOPRODUTO = ", produtoAtributo.cdAtributoProduto);
		sqlWhereClause.addAndCondition("CDATRIBUTOOPCAOPRODUTO = ", produtoAtributo.cdAtributoOpcaoProduto);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", produtoAtributo.cdProduto);
		//--
		sql.append(sqlWhereClause.getSql());
    }

    public Vector findCdProdutosByExample(BaseDomain domain) throws SQLException {
        ProdutoAtributo produtoAtributo = (ProdutoAtributo) domain;
        StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT CDPRODUTO");
    	sql.append(" FROM ").append(tableName);
    	sql.append(" where CDEMPRESA = ").append(Sql.getValue(produtoAtributo.cdEmpresa));
        sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(produtoAtributo.cdRepresentante));
        sql.append(" and CDATRIBUTOPRODUTO = ").append(Sql.getValue(produtoAtributo.cdAtributoProduto));
        sql.append(" and CDATRIBUTOOPCAOPRODUTO = ").append(Sql.getValue(produtoAtributo.cdAtributoOpcaoProduto));
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
	    	Vector listProdutoAtributo = new Vector();
	    	while (rs.next()) {
	    		listProdutoAtributo.addElement(rs.getString(1));
	    	}
	    	return listProdutoAtributo;
        }
    }


}