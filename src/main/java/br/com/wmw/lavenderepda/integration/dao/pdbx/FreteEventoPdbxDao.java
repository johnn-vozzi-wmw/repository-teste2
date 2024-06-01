package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.FreteEvento;
import totalcross.sql.ResultSet;

public class FreteEventoPdbxDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FreteEvento();
	}

    private static FreteEventoPdbxDao instance;

    public FreteEventoPdbxDao() {
        super(FreteEvento.TABLE_NAME);
    }

    public static FreteEventoPdbxDao getInstance() {
        return (instance == null) ? instance = new FreteEventoPdbxDao() : instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	FreteEvento freteEvento = new FreteEvento();
        freteEvento.rowKey = rs.getString("rowkey");
        freteEvento.cdEmpresa = rs.getString("cdEmpresa");
        freteEvento.cdRepresentante = rs.getString("cdRepresentante");
        freteEvento.cdFreteEvento = rs.getString("cdTransportadora");
        freteEvento.dsFreteEvento = rs.getString("dsFreteEvento");
        freteEvento.nuTipoBaseCalculo = rs.getInt("nuTipoBaseCalculo");
        return freteEvento;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowKey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDFRETEEVENTO,");
        sql.append(" DSFRETEEVENTO,");
        sql.append(" NUTIPOBASECALCULO,");
        sql.append(" FLMODOCALCULO");
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        FreteEvento freteEventoFilter = (FreteEvento) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", freteEventoFilter.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", freteEventoFilter.cdRepresentante);
		sqlWhereClause.addAndCondition("CDFRETEEVENTO = ", freteEventoFilter.cdFreteEvento);
		sqlWhereClause.addAndCondition("DSFRETEEVENTO = ", freteEventoFilter.dsFreteEvento);
		sqlWhereClause.addAndCondition("NUTIPOBASECALCULO = ", freteEventoFilter.nuTipoBaseCalculo);
		sql.append(sqlWhereClause.getSql());
    }

}