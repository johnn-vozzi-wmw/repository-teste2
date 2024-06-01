package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ContaCorrenteCli;
import totalcross.sql.ResultSet;

public class ContaCorrenteCliDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ContaCorrenteCli();
	}

    private static ContaCorrenteCliDbxDao instance;

    public ContaCorrenteCliDbxDao() {
        super(ContaCorrenteCli.TABLE_NAME);
    }

    public static ContaCorrenteCliDbxDao getInstance() {
        if (instance == null) {
            instance = new ContaCorrenteCliDbxDao();
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
        sql.append(" CDCLIENTE,");
        sql.append(" nuDocumento,");
        sql.append(" dtMovimentacao,");
        sql.append(" vlCredito,");
        sql.append(" vlDebito");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ContaCorrenteCli contaCorrenteCli = new ContaCorrenteCli();
        contaCorrenteCli.rowKey = rs.getString("rowkey");
        contaCorrenteCli.cdEmpresa = rs.getString("cdEmpresa");
        contaCorrenteCli.cdRepresentante = rs.getString("cdRepresentante");
        contaCorrenteCli.cdCliente = rs.getString("cdCliente");
        contaCorrenteCli.nuDocumento = rs.getString("nuDocumento");
        contaCorrenteCli.dtMovimentacao = rs.getDate("dtMovimentacao");
        contaCorrenteCli.vlCredito = ValueUtil.round(rs.getDouble("vlCredito"));
        contaCorrenteCli.vlDebito = ValueUtil.round(rs.getDouble("vlDebito"));
        return contaCorrenteCli;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ContaCorrenteCli contaCorrenteCli = (ContaCorrenteCli) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", contaCorrenteCli.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", contaCorrenteCli.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", contaCorrenteCli.cdCliente);
		sqlWhereClause.addAndCondition("nuDocumento = ", contaCorrenteCli.nuDocumento);
		//--
		sql.append(sqlWhereClause.getSql());
    }

    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	sql.append(" order by dtMovimentacao");
    }
}