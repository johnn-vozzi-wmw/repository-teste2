package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Campanha;
import totalcross.sql.ResultSet;

public class CampanhaPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Campanha();
	}

    private static CampanhaPdbxDao instance;

    public CampanhaPdbxDao() {
        super(Campanha.TABLE_NAME);
    }

    public static CampanhaPdbxDao getInstance() {
        if (instance == null) {
            instance = new CampanhaPdbxDao();
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
        sql.append(" CDCAMPANHA,");
        sql.append(" DSCAMPANHA,");
        sql.append(" FLTIPOALTERACAO");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Campanha campanha = new Campanha();
        campanha.rowKey = rs.getString("rowkey");
        campanha.cdEmpresa = rs.getString("cdEmpresa");
        campanha.cdRepresentante = rs.getString("cdRepresentante");
        campanha.cdCampanha = rs.getString("cdCampanha");
        campanha.dsCampanha = rs.getString("dsCampanha");
        campanha.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return campanha;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Campanha campanha = (Campanha) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", campanha.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", campanha.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCAMPANHA = ", campanha.cdCampanha);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}