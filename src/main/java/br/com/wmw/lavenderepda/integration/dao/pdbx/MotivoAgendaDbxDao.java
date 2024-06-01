package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.MotivoAgenda;
import totalcross.sql.ResultSet;

public class MotivoAgendaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MotivoAgenda();
	}

    private static MotivoAgendaDbxDao instance;

    public MotivoAgendaDbxDao() {
        super(MotivoAgenda.TABLE_NAME);
    }
    
    public static MotivoAgendaDbxDao getInstance() {
        if (instance == null) {
            instance = new MotivoAgendaDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        MotivoAgenda motivoAgenda = new MotivoAgenda();
        motivoAgenda.rowKey = rs.getString("rowkey");
        motivoAgenda.cdMotivoAgenda = rs.getString("cdMotivoAgenda");
        motivoAgenda.dsMotivoAgenda = rs.getString("dsMotivoAgenda");
        motivoAgenda.flAgendaVisita = rs.getString("flAgendaVisita");
        motivoAgenda.flTransferenciaAgenda = rs.getString("flTransferenciaAgenda");
        motivoAgenda.flTipoAlteracao = rs.getString("flTipoAlteracao");
        motivoAgenda.cdUsuario = rs.getString("cdUsuario");
        motivoAgenda.nuCarimbo = rs.getInt("nuCarimbo");
        return motivoAgenda;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDMOTIVOAGENDA,");
        sql.append(" DSMOTIVOAGENDA,");
        sql.append(" FLAGENDAVISITA,");
        sql.append(" FLTRANSFERENCIAAGENDA,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDMOTIVOAGENDA,");
        sql.append(" DSMOTIVOAGENDA,");
        sql.append(" FLAGENDAVISITA,");
        sql.append(" FLTRANSFERENCIAAGENDA,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MotivoAgenda motivoAgenda = (MotivoAgenda) domain;
        sql.append(Sql.getValue(motivoAgenda.cdMotivoAgenda)).append(",");
        sql.append(Sql.getValue(motivoAgenda.dsMotivoAgenda)).append(",");
        sql.append(Sql.getValue(motivoAgenda.flAgendaVisita)).append(",");
        sql.append(Sql.getValue(motivoAgenda.flTransferenciaAgenda)).append(",");
        sql.append(Sql.getValue(motivoAgenda.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(motivoAgenda.cdUsuario)).append(",");
        sql.append(Sql.getValue(motivoAgenda.nuCarimbo));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MotivoAgenda motivoAgenda = (MotivoAgenda) domain;
        sql.append(" DSMOTIVOAGENDA = ").append(Sql.getValue(motivoAgenda.dsMotivoAgenda)).append(",");
        sql.append(" FLAGENDAVISITA = ").append(Sql.getValue(motivoAgenda.flAgendaVisita)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(motivoAgenda.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(motivoAgenda.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(motivoAgenda.nuCarimbo));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MotivoAgenda motivoAgenda = (MotivoAgenda) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDMOTIVOAGENDA = ", motivoAgenda.cdMotivoAgenda);
		sqlWhereClause.addAndCondition("FLAGENDAVISITA = ", motivoAgenda.flAgendaVisita);
		sqlWhereClause.addAndCondition("FLTRANSFERENCIAAGENDA = ", motivoAgenda.flTransferenciaAgenda);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}