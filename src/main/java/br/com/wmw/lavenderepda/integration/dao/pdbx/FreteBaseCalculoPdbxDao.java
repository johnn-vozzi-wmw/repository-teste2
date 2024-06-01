package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.FreteBaseCalculo;
import totalcross.sql.ResultSet;

public class FreteBaseCalculoPdbxDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FreteBaseCalculo();
	}

    private static FreteBaseCalculoPdbxDao instance;

    public FreteBaseCalculoPdbxDao() {
        super(FreteBaseCalculo.TABLE_NAME);
    }

    public static FreteBaseCalculoPdbxDao getInstance() {
        return (instance == null) ? instance = new FreteBaseCalculoPdbxDao() : instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {}
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {}
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {}
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {}

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	FreteBaseCalculo freteBaseCalculo = new FreteBaseCalculo();
        freteBaseCalculo.rowKey = rs.getString("rowkey");
        freteBaseCalculo.cdEmpresa = rs.getString("cdEmpresa");
        freteBaseCalculo.cdRepresentante = rs.getString("cdRepresentante");
        freteBaseCalculo.cdTransportadora = rs.getString("cdTransportadora");
        freteBaseCalculo.cdFreteConfig = rs.getString("cdFreteConfig");
        freteBaseCalculo.cdFreteCalculo = rs.getInt("cdFreteCalculo");
        freteBaseCalculo.cdFreteCalculoBC = rs.getInt("cdFreteCalculoBC");
        return freteBaseCalculo;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowKey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDTRANSPORTADORA,");
        sql.append(" CDFRETECONFIG,");
        sql.append(" CDFRETECONFIG,");
        sql.append(" CDFRETECALCULOBC");
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        FreteBaseCalculo freteBaseCalculoFilter = (FreteBaseCalculo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", freteBaseCalculoFilter.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", freteBaseCalculoFilter.cdRepresentante);
		sqlWhereClause.addAndCondition("CDTRANSPORTADORA = ", freteBaseCalculoFilter.cdTransportadora);
		sqlWhereClause.addAndCondition("CDFRETECONFIG = ", freteBaseCalculoFilter.cdFreteConfig);
		sqlWhereClause.addAndCondition("CDFRETECALCULO = ", freteBaseCalculoFilter.cdFreteCalculo);
		sqlWhereClause.addAndCondition("CDFRETECALCULOBC = ", freteBaseCalculoFilter.cdFreteCalculoBC);
		sql.append(sqlWhereClause.getSql());
    }

}