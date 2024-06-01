package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Concorrente;
import totalcross.sql.ResultSet;

public class ConcorrentePdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Concorrente();
	}

    private static ConcorrentePdbxDao instance;

    public ConcorrentePdbxDao() {
        super(Concorrente.TABLE_NAME);
    }

    public static ConcorrentePdbxDao getInstance() {
        if (instance == null) {
            instance = new ConcorrentePdbxDao();
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
        sql.append(" CDCONCORRENTE,");
        sql.append(" DSCONCORRENTE,");
        sql.append(" FLTIPOALTERACAO");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Concorrente concorrente = new Concorrente();
        concorrente.rowKey = rs.getString("rowkey");
        concorrente.cdEmpresa = rs.getString("cdEmpresa");
        concorrente.cdConcorrente = rs.getString("cdConcorrente");
        concorrente.dsConcorrente = rs.getString("dsConcorrente");
        concorrente.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return concorrente;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Concorrente concorrente = (Concorrente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", concorrente.cdEmpresa);
		sqlWhereClause.addAndCondition("CDCONCORRENTE = ", concorrente.cdConcorrente);
		//--
		sql.append(sqlWhereClause.getSql());
    }

    @Override
    protected void addWhereByExampleColumnFilterEmpresaRepresentante(SqlWhereClause sqlWhereClause) {
    	sqlWhereClause.addAndCondition("CDEMPRESA = ", SessionLavenderePda.cdEmpresa);
    }

}