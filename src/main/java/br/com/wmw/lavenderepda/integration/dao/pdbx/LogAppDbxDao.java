package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.business.domain.LogApp;
import totalcross.sql.ResultSet;

public class LogAppDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new LogApp();
	}

    private static LogAppDbxDao instance;

    public LogAppDbxDao() {
        super(LogApp.TABLE_NAME); 
    }
    
    public static LogAppDbxDao getInstance() {
        if (instance == null) {
            instance = new LogAppDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        LogApp logApp = new LogApp();
        logApp.rowKey = rs.getString("rowkey");
        logApp.cdEmpresa = rs.getString("cdEmpresa");
        logApp.cdRepresentante = rs.getString("cdRepresentante");
        logApp.cdLog = rs.getString("cdLog");
        logApp.dtLog = rs.getDate("dtLog");
        logApp.hrLog = rs.getString("hrLog");
        logApp.cdUsuarioLog = rs.getString("cdUsuarioLog");
        logApp.flTipoLog = rs.getString("flTipoLog");
        logApp.vlChave = rs.getString("vlChave");
        logApp.cdProcesso = rs.getString("cdProcesso");
        logApp.cdCliente = rs.getString("cdCliente");
        logApp.dsDetalhes = rs.getString("dsDetalhes");
        logApp.nuCarimbo = rs.getInt("nuCarimbo");
        logApp.cdUsuario = rs.getString("cdUsuario");
        logApp.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return logApp;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDLOG,");
        sql.append(" DTLOG,");
        sql.append(" HRLOG,");
        sql.append(" CDUSUARIOLOG,");
        sql.append(" FLTIPOLOG,");
        sql.append(" VLCHAVE,");
        sql.append(" CDPROCESSO,");
        sql.append(" CDCLIENTE,");
        sql.append(" DSDETALHES,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO" );
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDLOG,");
        sql.append(" DTLOG,");
        sql.append(" HRLOG,");
        sql.append(" CDUSUARIOLOG,");
        sql.append(" FLTIPOLOG,");
        sql.append(" VLCHAVE,");
        sql.append(" CDPROCESSO,");
        sql.append(" CDCLIENTE,");
        sql.append(" DSDETALHES,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        LogApp logApp = (LogApp) domain;
        sql.append(Sql.getValue(logApp.cdEmpresa)).append(",");
        sql.append(Sql.getValue(logApp.cdRepresentante)).append(",");
        sql.append(Sql.getValue(logApp.cdLog)).append(",");
        sql.append(Sql.getValue(logApp.dtLog)).append(",");
        sql.append(Sql.getValue(logApp.hrLog)).append(",");
        sql.append(Sql.getValue(logApp.cdUsuarioLog)).append(",");
        sql.append(Sql.getValue(logApp.flTipoLog)).append(",");
        sql.append(Sql.getValue(logApp.vlChave)).append(",");
        sql.append(Sql.getValue(logApp.cdProcesso)).append(",");
        sql.append(Sql.getValue(logApp.cdCliente)).append(",");
        sql.append(Sql.getValue(logApp.dsDetalhes)).append(",");
        sql.append(Sql.getValue(logApp.nuCarimbo)).append(",");
        sql.append(Sql.getValue(logApp.cdUsuario)).append(",");
        sql.append(Sql.getValue(logApp.flTipoAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        LogApp logApp = (LogApp) domain;
        sql.append(" DTLOG = ").append(Sql.getValue(logApp.dtLog)).append(",");
        sql.append(" HRLOG = ").append(Sql.getValue(logApp.hrLog)).append(",");
        sql.append(" CDUSUARIOLOG = ").append(Sql.getValue(logApp.cdUsuarioLog)).append(",");
        sql.append(" FLTIPOLOG = ").append(Sql.getValue(logApp.flTipoLog)).append(",");
        sql.append(" VLCHAVE = ").append(Sql.getValue(logApp.vlChave)).append(",");
        sql.append(" CDPROCESSO = ").append(Sql.getValue(logApp.cdProcesso)).append(",");
        sql.append(" CDCLIENTE = ").append(Sql.getValue(logApp.cdCliente)).append(",");
        sql.append(" DSDETALHES = ").append(Sql.getValue(logApp.dsDetalhes)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(logApp.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(logApp.cdUsuario)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(logApp.flTipoAlteracao));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        LogApp logApp = (LogApp) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", logApp.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", logApp.cdRepresentante);
		sqlWhereClause.addAndCondition("CDLOG = ", logApp.cdLog);
		//--
		sql.append(sqlWhereClause.getSql());
    }

	public void deleteLogEnviadoServidor() {
		StringBuffer sql = getSqlBuffer();
        sql.append(" DELETE FROM ").append(tableName);
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("FLTIPOALTERACAO = ''");
		sql.append(sqlWhereClause.getSql());
        try {
        	executeUpdate(sql.toString());
        } catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}
    
}