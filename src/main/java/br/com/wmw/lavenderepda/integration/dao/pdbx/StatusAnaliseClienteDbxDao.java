package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.StatusAnaliseCliente;
import totalcross.sql.ResultSet;

public class StatusAnaliseClienteDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new StatusAnaliseCliente();
	}

    private static StatusAnaliseClienteDbxDao instance = null;
	public static String TABLE_NAME = "TBLVPSTATUSANALISECLIENTE";

    public StatusAnaliseClienteDbxDao() {
        super(TABLE_NAME);
    }
    
    public static StatusAnaliseClienteDbxDao getInstance() {
        if (instance == null) {
            instance = new StatusAnaliseClienteDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        StatusAnaliseCliente statusAnaliseCliente = new StatusAnaliseCliente();
        statusAnaliseCliente.rowKey = rs.getString("rowkey");
        statusAnaliseCliente.cdStatusAnalise = rs.getString("cdStatusAnalise");
        statusAnaliseCliente.dsStatusAnalise = rs.getString("dsStatusAnalise");
        statusAnaliseCliente.flReprovacao = rs.getString("flReprovacao");
        statusAnaliseCliente.flConclusao = rs.getString("flConclusao");
        statusAnaliseCliente.nuCarimbo = rs.getInt("nuCarimbo");
        statusAnaliseCliente.flTipoAlteracao = rs.getString("flTipoAlteracao");
        statusAnaliseCliente.flPermiteEdicao = rs.getString("flPermiteEdicao");
        statusAnaliseCliente.dtAlteracao = rs.getDate("dtAlteracao");
        statusAnaliseCliente.hrAlteracao = rs.getString("hrAlteracao");
        statusAnaliseCliente.cdUsuario = rs.getString("cdUsuario");
        return statusAnaliseCliente;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDSTATUSANALISE,");
        sql.append(" DSSTATUSANALISE,");
        sql.append(" FLREPROVACAO,");
        sql.append(" FLCONCLUSAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" FLPERMITEEDICAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDSTATUSANALISE,");
        sql.append(" DSSTATUSANALISE,");
        sql.append(" FLREPROVACAO,");
        sql.append(" FLCONCLUSAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        StatusAnaliseCliente statusAnaliseCliente = (StatusAnaliseCliente) domain;
        sql.append(Sql.getValue(statusAnaliseCliente.cdStatusAnalise)).append(",");
        sql.append(Sql.getValue(statusAnaliseCliente.dsStatusAnalise)).append(",");
        sql.append(Sql.getValue(statusAnaliseCliente.flReprovacao)).append(",");
        sql.append(Sql.getValue(statusAnaliseCliente.flConclusao)).append(",");
        sql.append(Sql.getValue(statusAnaliseCliente.nuCarimbo)).append(",");
        sql.append(Sql.getValue(statusAnaliseCliente.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(statusAnaliseCliente.dtAlteracao)).append(",");
        sql.append(Sql.getValue(statusAnaliseCliente.hrAlteracao)).append(",");
        sql.append(Sql.getValue(statusAnaliseCliente.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        StatusAnaliseCliente statusAnaliseCliente = (StatusAnaliseCliente) domain;
        sql.append(" DSSTATUSANALISE = ").append(Sql.getValue(statusAnaliseCliente.dsStatusAnalise)).append(",");
        sql.append(" FLREPROVACAO = ").append(Sql.getValue(statusAnaliseCliente.flReprovacao)).append(",");
        sql.append(" FLCONCLUSAO = ").append(Sql.getValue(statusAnaliseCliente.flConclusao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(statusAnaliseCliente.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(statusAnaliseCliente.flTipoAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(statusAnaliseCliente.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(statusAnaliseCliente.hrAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(statusAnaliseCliente.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        StatusAnaliseCliente statusAnaliseCliente = (StatusAnaliseCliente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDSTATUSANALISE = ", statusAnaliseCliente.cdStatusAnalise);
		sqlWhereClause.addAndCondition("FLCONCLUSAO = ", statusAnaliseCliente.flConclusao);
		sqlWhereClause.addAndCondition("FLPERMITEEDICAO = ", statusAnaliseCliente.flPermiteEdicao);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}