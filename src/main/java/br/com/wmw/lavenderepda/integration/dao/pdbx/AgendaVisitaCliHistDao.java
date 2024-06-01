package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.AgendaVisitaCliHist;
import totalcross.sql.ResultSet;

public class AgendaVisitaCliHistDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new AgendaVisitaCliHist();
	}

    private static AgendaVisitaCliHistDao instance;

    public AgendaVisitaCliHistDao() {
        super(AgendaVisitaCliHist.TABLE_NAME);
    }

    public static AgendaVisitaCliHistDao getInstance() {
        if (instance == null) {
            instance = new AgendaVisitaCliHistDao();
        }
        return instance;
    }

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        AgendaVisitaCliHist agendavisitaCliHist = new AgendaVisitaCliHist();
        agendavisitaCliHist.rowKey = rs.getString("rowkey");
        agendavisitaCliHist.cdEmpresa = rs.getString("cdEmpresa");
        agendavisitaCliHist.cdRepresentante = rs.getString("cdRepresentante");
        agendavisitaCliHist.cdCliente = rs.getString("cdCliente");
        agendavisitaCliHist.dtAgenda = rs.getDate("dtAgenda");
        agendavisitaCliHist.qtTotalAgenda = rs.getInt("qtTotalAgenda");
        agendavisitaCliHist.cdUsuario = rs.getString("cdUsuario");
        agendavisitaCliHist.dtAlteracao = rs.getDate("dtAlteracao");
        agendavisitaCliHist.hrAlteracao = rs.getString("hrAlteracao");
        agendavisitaCliHist.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return agendavisitaCliHist;
    }

    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDCLIENTE,");
        sql.append(" tb.DTAGENDA,");
        sql.append(" tb.QTTOTALAGENDA,");
        sql.append(" tb.CDUSUARIO,");
        sql.append(" tb.NUCARIMBO,");
        sql.append(" tb.DTALTERACAO,");
        sql.append(" tb.HRALTERACAO,");
        sql.append(" tb.FLTIPOALTERACAO");
    }

	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}
	
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		return null;
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { 
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" DTAGENDA,");
        sql.append(" QTTOTALAGENDA,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" FLTIPOALTERACAO");
    }
    
    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	AgendaVisitaCliHist agendavisitaCliHist = (AgendaVisitaCliHist) domain; 
    	sql.append(Sql.getValue(agendavisitaCliHist.cdEmpresa)).append(",");
        sql.append(Sql.getValue(agendavisitaCliHist.cdRepresentante)).append(",");
        sql.append(Sql.getValue(agendavisitaCliHist.cdCliente)).append(",");
        sql.append(Sql.getValue(agendavisitaCliHist.dtAgenda)).append(",");
        sql.append(Sql.getValue(agendavisitaCliHist.qtTotalAgenda)).append(",");
        sql.append(Sql.getValue(agendavisitaCliHist.cdUsuario)).append(",");
        sql.append(Sql.getValue(agendavisitaCliHist.nuCarimbo)).append(",");
        sql.append(Sql.getValue(agendavisitaCliHist.dtAlteracao)).append(",");
        sql.append(Sql.getValue(agendavisitaCliHist.hrAlteracao)).append(",");
        sql.append(Sql.getValue(agendavisitaCliHist.flTipoAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { 
    	AgendaVisitaCliHist agendaVisitaCliHist = (AgendaVisitaCliHist) domain; 
        sql.append(" QTTOTALAGENDA = ").append(Sql.getValue(agendaVisitaCliHist.qtTotalAgenda)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(agendaVisitaCliHist.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(agendaVisitaCliHist.hrAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(agendaVisitaCliHist.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(agendaVisitaCliHist.flTipoAlteracao));
    }

    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	AgendaVisitaCliHist agendaVisitaCliHist = (AgendaVisitaCliHist) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals("CDEMPRESA", agendaVisitaCliHist.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("CDREPRESENTANTE", agendaVisitaCliHist.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("CDCLIENTE", agendaVisitaCliHist.cdCliente);
		sqlWhereClause.addAndConditionEquals("DTAGENDA", agendaVisitaCliHist.dtAgenda);
		sqlWhereClause.addAndCondition("DTAGENDA < ", agendaVisitaCliHist.dtAgendaFilter);
		sqlWhereClause.addAndConditionEquals("QTTOTALAGENDA", agendaVisitaCliHist.qtTotalAgenda);
		sql.append(sqlWhereClause.getSql());
    }

}
