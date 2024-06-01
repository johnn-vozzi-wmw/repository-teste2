package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import totalcross.sql.ResultSet;

public class LogPdaPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new LogPda();
	}

    private static LogPdaPdbxDao instance;

    public LogPdaPdbxDao() {
        super(LogPda.TABLE_NAME);
    }

    public static LogPdaPdbxDao getInstance() {
        if (instance == null) {
            instance = new LogPdaPdbxDao();
        }
        return instance;
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" cdLog,");
        sql.append(" cdNivel,");
        sql.append(" cdCategoria,");
        sql.append(" dsLog,");
        sql.append(" dtLog,");
        sql.append(" hrLog,");
        sql.append(" flTipoAlteracao,");
        sql.append(" cdUsuario");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        LogPda logPda = new LogPda();
        logPda.rowKey = rs.getString("rowkey");
        logPda.cdLog = rs.getString("cdLog");
        logPda.cdNivel = rs.getString("cdNivel");
        logPda.cdCategoria = rs.getInt("cdCategoria");
        logPda.dsLog = rs.getString("dsLog");
        logPda.dtLog = rs.getDate("dtLog");
        logPda.hrLog = rs.getString("hrLog");
        logPda.flTipoAlteracao = rs.getString("flTipoAlteracao");
        logPda.cdUsuario = rs.getString("cdUsuario");
        return logPda;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" cdLog,");
        sql.append(" cdNivel,");
        sql.append(" cdCategoria,");
        sql.append(" dsLog,");
        sql.append(" dtLog,");
        sql.append(" hrLog,");
        sql.append(" NUCARIMBO,");
        sql.append(" flTipoAlteracao,");
        sql.append(" cdUsuario");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        LogPda logPda = (LogPda) domain;
        sql.append(Sql.getValue(logPda.cdLog)).append(",");
        sql.append(Sql.getValue(logPda.cdNivel)).append(",");
        sql.append(Sql.getValue(logPda.cdCategoria)).append(",");
        sql.append(Sql.getValue(logPda.dsLog)).append(",");
        sql.append(Sql.getValue(logPda.dtLog)).append(",");
        sql.append(Sql.getValue(logPda.hrLog)).append(",");
        sql.append(Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT)).append(",");
        sql.append(Sql.getValue(logPda.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(logPda.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        LogPda logPda = (LogPda) domain;
        sql.append(" cdNivel = ").append(Sql.getValue(logPda.cdNivel)).append(",");
        sql.append(" cdCategoria = ").append(Sql.getValue(logPda.cdCategoria)).append(",");
        sql.append(" dsLog = ").append(Sql.getValue(logPda.dsLog)).append(",");
        sql.append(" dtLog = ").append(Sql.getValue(logPda.dtLog)).append(",");
        sql.append(" hrLog = ").append(Sql.getValue(logPda.hrLog)).append(",");
        sql.append(" flTipoAlteracao = ").append(Sql.getValue(logPda.flTipoAlteracao)).append(",");
        sql.append(" cdUsuario = ").append(Sql.getValue(logPda.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        LogPda logPda = (LogPda) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("cdLog = ", logPda.cdLog);
		//--
		sql.append(sqlWhereClause.getSql());
    }
}