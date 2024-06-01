package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.DescontoPacote;
import totalcross.sql.ResultSet;

public class DescontoPacotePdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescontoPacote();
	}

    private static DescontoPacotePdbxDao instance;

    public DescontoPacotePdbxDao() {
        super(DescontoPacote.TABLE_NAME);
    }

    public static DescontoPacotePdbxDao getInstance() {
        return instance == null ? instance = new DescontoPacotePdbxDao() : instance;
    }
    
    protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {}
    protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException { return null; }
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {}
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {}
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {}

    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowKey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDPACOTE,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" QTITEM,");
        sql.append(" VLPCTDESCONTO");
    }

    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        DescontoPacote descontoPacote = new DescontoPacote();
        descontoPacote.rowKey = rs.getString("rowkey");
        descontoPacote.cdEmpresa = rs.getString("cdEmpresa");
        descontoPacote.cdRepresentante = rs.getString("cdRepresentante");
        descontoPacote.cdProduto = rs.getString("cdProduto");
        descontoPacote.cdPacote = rs.getString("cdPacote");
        descontoPacote.cdTabelaPreco = rs.getString("cdTabelaPreco");
        descontoPacote.qtItem = rs.getDouble("qtItem");
        descontoPacote.vlPctDesconto = rs.getDouble("vlPctDesconto");
        return descontoPacote;
    }
    
    protected void addWhereByExample(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        DescontoPacote descontoPacoteFilter = (DescontoPacote) domainFilter;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
        sqlWhereClause.addAndCondition("CDEMPRESA = ", descontoPacoteFilter.cdEmpresa);
        sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", descontoPacoteFilter.cdRepresentante);
        sqlWhereClause.addAndCondition("CDPRODUTO = ", descontoPacoteFilter.cdProduto);
        sqlWhereClause.addAndCondition("CDPACOTE = ", descontoPacoteFilter.cdPacote);
        sqlWhereClause.addAndCondition("CDTABELAPRECO = ", descontoPacoteFilter.cdTabelaPreco);
        sqlWhereClause.addAndCondition("QTITEM = ", descontoPacoteFilter.qtItem);
        sql.append(sqlWhereClause.getSql());
    }

}