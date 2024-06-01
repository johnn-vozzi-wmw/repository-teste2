package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.AtributoOpcaoProd;
import totalcross.sql.ResultSet;

public class AtributoOpcaoProdPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new AtributoOpcaoProd();
	}

    private static AtributoOpcaoProdPdbxDao instance;

    public AtributoOpcaoProdPdbxDao() {
        super(AtributoOpcaoProd.TABLE_NAME);
    }

    public static AtributoOpcaoProdPdbxDao getInstance() {
        if (instance == null) {
            instance = new AtributoOpcaoProdPdbxDao();
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
        sql.append(" CDATRIBUTOOPCAOPRODUTO,");
        sql.append(" DSATRIBUTOOPCAOPRODUTO,");
        sql.append(" FLTIPOALTERACAO");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        AtributoOpcaoProd atributoOpcaoProd = new AtributoOpcaoProd();
        atributoOpcaoProd.rowKey = rs.getString("rowkey");
        atributoOpcaoProd.cdEmpresa = rs.getString("cdEmpresa");
        atributoOpcaoProd.cdRepresentante = rs.getString("cdRepresentante");
        atributoOpcaoProd.cdAtributoProduto = rs.getString("cdAtributoProduto");
        atributoOpcaoProd.cdAtributoOpcaoProduto = rs.getString("cdAtributoOpcaoProduto");
        atributoOpcaoProd.dsAtributoOpcaoProduto = rs.getString("dsAtributoOpcaoProduto");
        atributoOpcaoProd.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return atributoOpcaoProd;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        AtributoOpcaoProd atributoOpcaoProd = (AtributoOpcaoProd) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", atributoOpcaoProd.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", atributoOpcaoProd.cdRepresentante);
		sqlWhereClause.addAndCondition("CDATRIBUTOPRODUTO = ", atributoOpcaoProd.cdAtributoProduto);
		sqlWhereClause.addAndCondition("CDATRIBUTOOPCAOPRODUTO = ", atributoOpcaoProd.cdAtributoOpcaoProduto);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}