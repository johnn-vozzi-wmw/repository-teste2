package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ReagendaAgendaVisita;
import totalcross.sql.ResultSet;

public class ReagendaAgendaVisitaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ReagendaAgendaVisita();
	}

    private static ReagendaAgendaVisitaDbxDao instance;

    public ReagendaAgendaVisitaDbxDao() {
        super(ReagendaAgendaVisita.TABLE_NAME); 
    }
    
    public static ReagendaAgendaVisitaDbxDao getInstance() {
        if (instance == null) {
            instance = new ReagendaAgendaVisitaDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        ReagendaAgendaVisita reagendamento = new ReagendaAgendaVisita();
        reagendamento.rowKey = rs.getString("rowkey");
        reagendamento.cdEmpresa = rs.getString("cdEmpresa");
        reagendamento.cdRepresentante = rs.getString("cdRepresentante");
        reagendamento.cdCliente = rs.getString("cdCliente");
        reagendamento.nuDiaSemana = rs.getInt("nuDiasemana");
        reagendamento.flSemanaMes = rs.getString("flSemanames");
        reagendamento.dtAgendaOriginal = rs.getDate("dtAgendaOriginal");
        reagendamento.hrAgendaOriginal = rs.getString("hrAgendaOriginal");
        reagendamento.dtReagendamento = rs.getDate("dtReagendamento");
        reagendamento.cdMotivoReagendamento = rs.getString("cdMotivoReagendamento");
        reagendamento.dsObservacao = rs.getString("dsObservacao");
        reagendamento.cdUsuario = rs.getString("cdUsuario");
        reagendamento.nuCarimbo = rs.getInt("nuCarimbo");
        reagendamento.flTipoAlteracao = rs.getString("flTipoAlteracao");
        reagendamento.dtAlteracao = rs.getDate("dtAlteracao");
        reagendamento.hrAlteracao = rs.getString("hrAlteracao");
        return reagendamento;
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
        sql.append(" CDCLIENTE,");
        sql.append(" NUDIASEMANA,");
        sql.append(" FLSEMANAMES,");
        sql.append(" DTAGENDAORIGINAL,");
        sql.append(" HRAGENDAORIGINAL,");
        sql.append(" DTREAGENDAMENTO,");
        sql.append(" CDMOTIVOREAGENDAMENTO,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
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
        sql.append(" CDCLIENTE,");
        sql.append(" NUDIASEMANA,");
        sql.append(" FLSEMANAMES,");
        sql.append(" DTAGENDAORIGINAL,");
        sql.append(" HRAGENDAORIGINAL,");
        sql.append(" DTREAGENDAMENTO,");
        sql.append(" CDMOTIVOREAGENDAMENTO,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        ReagendaAgendaVisita reagendamento = (ReagendaAgendaVisita) domain;
        sql.append(Sql.getValue(reagendamento.cdEmpresa)).append(",");
        sql.append(Sql.getValue(reagendamento.cdRepresentante)).append(",");
        sql.append(Sql.getValue(reagendamento.cdCliente)).append(",");
        sql.append(Sql.getValue(reagendamento.nuDiaSemana)).append(",");
        sql.append(Sql.getValue(reagendamento.flSemanaMes)).append(",");
        sql.append(Sql.getValue(reagendamento.dtAgendaOriginal)).append(",");
        sql.append(Sql.getValue(reagendamento.cdMotivoReagendamento)).append(",");
        sql.append(Sql.getValue(reagendamento.dtReagendamento)).append(",");
        sql.append(Sql.getValue(reagendamento.hrAgendaOriginal)).append(",");
        sql.append(Sql.getValue(reagendamento.dsObservacao)).append(",");
        sql.append(Sql.getValue(reagendamento.cdUsuario)).append(",");
        sql.append(Sql.getValue(reagendamento.nuCarimbo)).append(",");
        sql.append(Sql.getValue(reagendamento.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(reagendamento.dtAlteracao)).append(",");
        sql.append(Sql.getValue(reagendamento.hrAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        ReagendaAgendaVisita reagendamento = (ReagendaAgendaVisita) domain;
        sql.append(" CDMOTIVOREAGENDAMENTO = ").append(Sql.getValue(reagendamento.cdMotivoReagendamento)).append(",");
        sql.append(" DTREAGENDAMENTO = ").append(Sql.getValue(reagendamento.dtReagendamento)).append(",");
        sql.append(" HRAGENDAORIGINAL = ").append(Sql.getValue(reagendamento.hrAgendaOriginal)).append(",");
        sql.append(" DSOBSERVACAO = ").append(Sql.getValue(reagendamento.dsObservacao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(reagendamento.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(reagendamento.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(reagendamento.flTipoAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(reagendamento.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(reagendamento.hrAlteracao));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        ReagendaAgendaVisita reagendamento = (ReagendaAgendaVisita) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", reagendamento.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", reagendamento.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", reagendamento.cdCliente);
		sqlWhereClause.addAndCondition("NUDIASEMANA = ", reagendamento.nuDiaSemana);
		sqlWhereClause.addAndCondition("FLSEMANAMES = ", reagendamento.flSemanaMes);
		sqlWhereClause.addAndCondition("DTAGENDAORIGINAL = ", reagendamento.dtAgendaOriginal);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}