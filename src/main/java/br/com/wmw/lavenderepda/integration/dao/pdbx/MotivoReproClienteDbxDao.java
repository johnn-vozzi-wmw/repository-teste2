package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.MotivoReproCliente;
import totalcross.sql.ResultSet;

public class MotivoReproClienteDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MotivoReproCliente();
	}

    private static MotivoReproClienteDbxDao instance = null;

    public MotivoReproClienteDbxDao() {
        super(MotivoReproCliente.TABLE_NAME);
    }
    
    public static MotivoReproClienteDbxDao getInstance() {
        if (instance == null) {
            instance = new MotivoReproClienteDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        MotivoReproCliente motivoReproCliente = new MotivoReproCliente();
        motivoReproCliente.rowKey = rs.getString("rowkey");
        motivoReproCliente.cdMotivoReprovacao = rs.getString("cdMotivoReprovacao");
        motivoReproCliente.dsMotivoReprovacao = rs.getString("dsMotivoReprovacao");
        motivoReproCliente.nuCarimbo = rs.getInt("nuCarimbo");
        motivoReproCliente.flTipoAlteracao = rs.getString("flTipoAlteracao");
        motivoReproCliente.dtAlteracao = rs.getDate("dtAlteracao");
        motivoReproCliente.hrAlteracao = rs.getString("hrAlteracao");
        motivoReproCliente.cdUsuario = rs.getString("cdUsuario");
        return motivoReproCliente;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDMOTIVOREPROVACAO,");
        sql.append(" DSMOTIVOREPROVACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDMOTIVOREPROVACAO,");
        sql.append(" DSMOTIVOREPROVACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        MotivoReproCliente motivoReproCliente = (MotivoReproCliente) domain;
        sql.append(Sql.getValue(motivoReproCliente.cdMotivoReprovacao)).append(",");
        sql.append(Sql.getValue(motivoReproCliente.dsMotivoReprovacao)).append(",");
        sql.append(Sql.getValue(motivoReproCliente.nuCarimbo)).append(",");
        sql.append(Sql.getValue(motivoReproCliente.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(motivoReproCliente.dtAlteracao)).append(",");
        sql.append(Sql.getValue(motivoReproCliente.hrAlteracao)).append(",");
        sql.append(Sql.getValue(motivoReproCliente.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        MotivoReproCliente motivoReproCliente = (MotivoReproCliente) domain;
        sql.append(" DSMOTIVOREPROVACAO = ").append(Sql.getValue(motivoReproCliente.dsMotivoReprovacao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(motivoReproCliente.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(motivoReproCliente.flTipoAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(motivoReproCliente.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(motivoReproCliente.hrAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(motivoReproCliente.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        MotivoReproCliente motivoReproCliente = (MotivoReproCliente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDMOTIVOREPROVACAO = ", motivoReproCliente.cdMotivoReprovacao);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}