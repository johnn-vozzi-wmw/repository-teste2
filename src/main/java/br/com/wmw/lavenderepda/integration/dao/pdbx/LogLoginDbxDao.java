package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.LogLogin;
import totalcross.sql.ResultSet;

public class LogLoginDbxDao extends LavendereCrudDbxDao {

    private static LogLoginDbxDao instance;

    public LogLoginDbxDao() {
        super(LogLogin.TABLE_NAME); 
    }
    
    public static LogLoginDbxDao getInstance() {
        if (instance == null) {
            instance = new LogLoginDbxDao();
        }
        return instance;
    }
    
	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new LogLogin();
	}
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        LogLogin logLogin = new LogLogin();
        logLogin.rowKey = rs.getString("rowkey");
        logLogin.dsEquipamento = rs.getString("dsEquipamento");
        logLogin.cdUsuarioLogin = rs.getString("cdUsuarioLogin");
        logLogin.flAmbiente = rs.getString("flAmbiente");
        logLogin.dsStatus = rs.getString("dsStatus");
        logLogin.dsMotivo = rs.getString("dsMotivo");
        logLogin.dtLogin = rs.getDate("dtLogin");
        logLogin.hrLogin = rs.getString("hrLogin");
        logLogin.cdUsuario = rs.getString("cdUsuario");
        logLogin.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return logLogin;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        LogLogin logLogin = new LogLogin();
        logLogin.rowKey = rs.getString("rowkey");
        logLogin.dsEquipamento = rs.getString("dsEquipamento");
        logLogin.cdUsuarioLogin = rs.getString("cdUsuarioLogin");
        logLogin.flAmbiente = rs.getString("flAmbiente");
        logLogin.dsStatus = rs.getString("dsStatus");
        logLogin.dsMotivo = rs.getString("dsMotivo");
        return logLogin;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" DSEQUIPAMENTO,");
        sql.append(" CDUSUARIOLOGIN,");
        sql.append(" FLAMBIENTE,");
        sql.append(" DSSTATUS,");
        sql.append(" DSMOTIVO,");
        sql.append(" DTLOGIN,");
        sql.append(" HRLOGIN,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO");

    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" DSEQUIPAMENTO, ");
        sql.append(" CDUSUARIOLOGIN, ");
        sql.append(" FLAMBIENTE, ");
        sql.append(" DSSTATUS, ");
        sql.append(" DSMOTIVO, ");
		sql.append(" rowkey");
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" DSEQUIPAMENTO,");
        sql.append(" CDUSUARIOLOGIN,");
        sql.append(" FLAMBIENTE,");
        sql.append(" DSSTATUS,");
        sql.append(" DSMOTIVO,");
        sql.append(" DTLOGIN,");
        sql.append(" HRLOGIN,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO");

    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        LogLogin logLogin = (LogLogin) domain;
        sql.append(Sql.getValue(logLogin.dsEquipamento)).append(",");
        sql.append(Sql.getValue(logLogin.cdUsuarioLogin)).append(",");
        sql.append(Sql.getValue(logLogin.flAmbiente)).append(",");
        sql.append(Sql.getValue(logLogin.dsStatus)).append(",");
        sql.append(Sql.getValue(logLogin.dsMotivo)).append(",");
        sql.append(Sql.getValue(logLogin.dtLogin)).append(",");
        sql.append(Sql.getValue(logLogin.hrLogin)).append(",");
        sql.append(Sql.getValue(logLogin.cdUsuario)).append(",");
        sql.append(Sql.getValue(logLogin.flTipoAlteracao));

    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        LogLogin logLogin = (LogLogin) domain;
        sql.append(" FLAMBIENTE = ").append(Sql.getValue(logLogin.flAmbiente)).append(",");
        sql.append(" DSSTATUS = ").append(Sql.getValue(logLogin.dsStatus)).append(",");
        sql.append(" DSMOTIVO = ").append(Sql.getValue(logLogin.dsMotivo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(logLogin.cdUsuario));

    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        LogLogin logLogin = (LogLogin) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("DSEQUIPAMENTO = ", logLogin.dsEquipamento);
		sqlWhereClause.addAndCondition("CDUSUARIOLOGIN = ", logLogin.cdUsuarioLogin);
		sqlWhereClause.addAndCondition("DTLOGIN = ", logLogin.dtLogin);
		sqlWhereClause.addAndCondition("HRLOGIN = ", logLogin.hrLogin);
		sqlWhereClause.addAndLikeCondition("FLAMBIENTE", logLogin.flAmbiente, false);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}