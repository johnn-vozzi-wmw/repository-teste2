package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Local;
import totalcross.sql.ResultSet;

public class LocalDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Local();
	}

    private static LocalDbxDao instance;

    public LocalDbxDao() {
        super(Local.TABLE_NAME); 
    }
    
    public static LocalDbxDao getInstance() {
        if (instance == null) {
            instance = new LocalDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        Local local = new Local();
        local.rowKey = rs.getString("rowkey");
        local.cdLocal = rs.getString("cdLocal");
        local.dsLocal = rs.getString("dsLocal");
        local.flTipoAlteracao = rs.getString("flTipoAlteracao");
        local.cdUsuario = rs.getString("cdUsuario");
        local.nuCarimbo = rs.getInt("nuCarimbo");
        local.dtAlteracao = rs.getDate("dtAlteracao");
        local.hrAlteracao = rs.getString("hrAlteracao");
        return local;
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
        sql.append(" CDLOCAL,");
        sql.append(" DSLOCAL,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
    	sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDLOCAL,");
        sql.append(" DSLOCAL,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        Local local = (Local) domain;
        sql.append(Sql.getValue(local.cdEmpresa)).append(",");
        sql.append(Sql.getValue(local.cdRepresentante)).append(",");
        sql.append(Sql.getValue(local.cdLocal)).append(",");
        sql.append(Sql.getValue(local.dsLocal)).append(",");
        sql.append(Sql.getValue(local.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(local.cdUsuario)).append(",");
        sql.append(Sql.getValue(local.nuCarimbo)).append(",");
        sql.append(Sql.getValue(local.dtAlteracao)).append(",");
        sql.append(Sql.getValue(local.hrAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        Local local = (Local) domain;
        sql.append(" DSLOCAL = ").append(Sql.getValue(local.dsLocal)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(local.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(local.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(local.nuCarimbo)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(local.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(local.hrAlteracao));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        Local local = (Local) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", local.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", local.cdRepresentante);
		sqlWhereClause.addAndCondition("CDLOCAL = ", local.cdLocal);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}