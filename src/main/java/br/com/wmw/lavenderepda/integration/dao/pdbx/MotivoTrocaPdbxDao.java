package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.MotivoTroca;
import totalcross.sql.ResultSet;

public class MotivoTrocaPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MotivoTroca();
	}

    private static MotivoTrocaPdbxDao instance;

    public MotivoTrocaPdbxDao() {
        super(MotivoTroca.TABLE_NAME);
    }

    public static MotivoTrocaPdbxDao getInstance() {
        if (instance == null) {
            instance = new MotivoTrocaPdbxDao();
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
        sql.append(" CDMOTIVOTROCA,");
        sql.append(" DSMOTIVOTROCA");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        MotivoTroca motivotroca = new MotivoTroca();
        motivotroca.rowKey = rs.getString("rowkey");
        motivotroca.cdEmpresa = rs.getString("cdEmpresa");
        motivotroca.cdRepresentante = rs.getString("cdRepresentante");
        motivotroca.cdMotivotroca = rs.getString("cdMotivotroca");
        motivotroca.dsMotivotroca = rs.getString("dsMotivotroca");
        return motivotroca;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MotivoTroca motivotroca = (MotivoTroca) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", motivotroca.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", motivotroca.cdRepresentante);
		sqlWhereClause.addAndCondition("CDMOTIVOTROCA = ", motivotroca.cdMotivotroca);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}