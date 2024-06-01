package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AgendaCadastro;
import totalcross.sql.ResultSet;

public class AgendaCadastroDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new AgendaCadastro();
	}

    private static AgendaCadastroDbxDao instance;

    public AgendaCadastroDbxDao() {
        super(AgendaCadastro.TABLE_NAME);
    }

    public static AgendaCadastroDbxDao getInstance() {
        if (instance == null) {
            instance = new AgendaCadastroDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        AgendaCadastro agendaCadastro = new AgendaCadastro();
        agendaCadastro.rowKey = rs.getString("rowkey");
        agendaCadastro.cdEmpresa = rs.getString("cdEmpresa");
        agendaCadastro.cdRepresentante = rs.getString("cdRepresentante");
        agendaCadastro.cdCliente = rs.getString("cdCliente");
        agendaCadastro.nuDiaSemana = rs.getInt("nuDiasemana");
        agendaCadastro.flSemanaMes = rs.getString("flSemanames");
        agendaCadastro.nuSequenciaAgenda = rs.getInt("nuSequenciaAgenda");
        agendaCadastro.flTipoFrequencia = rs.getString("flTipofrequencia");
        agendaCadastro.nuSequencia = rs.getInt("nuSequencia");
        agendaCadastro.dtBase = rs.getDate("dtBase");
        agendaCadastro.hrAgenda = rs.getString("hrAgenda");
        agendaCadastro.dsObservacao = rs.getString("dsObservacao");
        agendaCadastro.nuCarimbo = rs.getInt("nuCarimbo");
        agendaCadastro.flTipoAlteracao = rs.getString("flTipoAlteracao");
        agendaCadastro.cdUsuario = rs.getString("cdUsuario");
        if (LavenderePdaConfig.usaReagendamentoAgendaVisita) {
        	agendaCadastro.dtAgendaOriginal = rs.getDate("dtAgendaOriginal");
        }
        if (LavenderePdaConfig.usaDataLimiteValidadeAgendaVisita || LavenderePdaConfig.permiteIndicarAgendaVisitaValidaParaDataUnica || LavenderePdaConfig.usaReagendamentoAgendaVisita) {
        	agendaCadastro.dtFinal = rs.getDate("dtFinal");
        }
        if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
        	agendaCadastro.cdTipoAgenda = rs.getString("cdTipoAgenda");
        }
        
        if (LavenderePdaConfig.isUsaHoraFimAgendaVisita()) {
        	agendaCadastro.hrAgendaFim = rs.getString("hrAgendaFim");
        }
        return agendaCadastro;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		AgendaCadastro agendaCadastro = new AgendaCadastro();
		agendaCadastro.cdEmpresa = rs.getString("cdEmpresa");
        agendaCadastro.cdRepresentante = rs.getString("cdRepresentante");
        agendaCadastro.cdCliente = rs.getString("cdCliente");
        agendaCadastro.nuDiaSemana = rs.getInt("nuDiasemana");
        agendaCadastro.flSemanaMes = rs.getString("flSemanames");
        agendaCadastro.nuSequenciaAgenda = rs.getInt("nuSequenciaAgenda");
        agendaCadastro.flTipoFrequencia = rs.getString("flTipofrequencia");
        agendaCadastro.nuSequencia = rs.getInt("nuSequencia");
        agendaCadastro.dtBase = rs.getDate("dtBase");
        agendaCadastro.hrAgenda = rs.getString("hrAgenda");
        agendaCadastro.dsObservacao = rs.getString("dsObservacao");
        if (LavenderePdaConfig.usaDataLimiteValidadeAgendaVisita || LavenderePdaConfig.permiteIndicarAgendaVisitaValidaParaDataUnica) {
        	agendaCadastro.dtFinal = rs.getDate("dtFinal");
        }
        if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
        	agendaCadastro.cdTipoAgenda = rs.getString("cdTipoAgenda");
        }
        if (LavenderePdaConfig.isUsaHoraFimAgendaVisita()) {
        	agendaCadastro.hrAgendaFim = rs.getString("hrAgendaFim");
        }
        agendaCadastro.nuCarimbo = rs.getInt("nuCarimbo");
        agendaCadastro.flTipoAlteracao = rs.getString("flTipoAlteracao");
        agendaCadastro.cdUsuario = rs.getString("cdUsuario");
        return agendaCadastro;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" NUDIASEMANA,");
        sql.append(" FLSEMANAMES,");
        sql.append(" NUSEQUENCIAAGENDA,");
        sql.append(" flTipoFrequencia,");
        sql.append(" nuSequencia,");
        sql.append(" dtBase,");
        sql.append(" hrAgenda,");
        sql.append(" dsObservacao,");
        sql.append(" nuCarimbo,");
        sql.append(" flTipoAlteracao,");
        if (LavenderePdaConfig.usaReagendamentoAgendaVisita) {
        	sql.append(" dtAgendaOriginal,");
        	sql.append(" dtVencimento,");
        }
        if (LavenderePdaConfig.usaDataLimiteValidadeAgendaVisita || LavenderePdaConfig.permiteIndicarAgendaVisitaValidaParaDataUnica || LavenderePdaConfig.usaReagendamentoAgendaVisita) {
        	sql.append(" dtFinal,");
        }
        if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
        	sql.append(" cdTipoAgenda,");
        }
        if (LavenderePdaConfig.isUsaHoraFimAgendaVisita()) {
        	sql.append(" hrAgendaFim,");
        }
        sql.append(" cdUsuario");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
		sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" NUDIASEMANA,");
        sql.append(" FLSEMANAMES,");
        sql.append(" NUSEQUENCIAAGENDA,");
        sql.append(" flTipoFrequencia,");
        sql.append(" nuSequencia,");
        sql.append(" dtBase,");
        sql.append(" hrAgenda,");
        sql.append(" dsObservacao,");
        if (LavenderePdaConfig.usaDataLimiteValidadeAgendaVisita || LavenderePdaConfig.permiteIndicarAgendaVisitaValidaParaDataUnica) {
        	sql.append(" dtFinal,");
        }
        if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
        	sql.append(" cdTipoAgenda,");
        }
        if (LavenderePdaConfig.isUsaHoraFimAgendaVisita()) {
        	sql.append(" hrAgendaFim,");
        }
        sql.append(" nuCarimbo,");
        sql.append(" flTipoAlteracao,");
        sql.append(" cdUsuario");
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { 
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" NUDIASEMANA,");
        sql.append(" FLSEMANAMES,");
        sql.append(" NUSEQUENCIAAGENDA,");
        sql.append(" flTipoFrequencia,");
        sql.append(" nuSequencia,");
        sql.append(" dtBase,");
        sql.append(" hrAgenda,");
        sql.append(" dsObservacao,");
        sql.append(" nuCarimbo,");
        sql.append(" flTipoAlteracao,");
        if (LavenderePdaConfig.usaReagendamentoAgendaVisita) {
        	sql.append(" dtAgendaOriginal,");
        }
        if (LavenderePdaConfig.usaDataLimiteValidadeAgendaVisita || LavenderePdaConfig.permiteIndicarAgendaVisitaValidaParaDataUnica || LavenderePdaConfig.usaReagendamentoAgendaVisita) {
        	sql.append(" dtFinal,");
        }
        if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
        	sql.append(" cdTipoAgenda,");
        }
        if (LavenderePdaConfig.isUsaHoraFimAgendaVisita()) {
        	sql.append(" hrAgendaFim,");
        }
        sql.append(" cdUsuario");
    }
    
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	AgendaCadastro agendaCadastro = (AgendaCadastro) domain; 
    	sql.append(Sql.getValue(agendaCadastro.cdEmpresa)).append(",");
        sql.append(Sql.getValue(agendaCadastro.cdRepresentante)).append(",");
        sql.append(Sql.getValue(agendaCadastro.cdCliente)).append(",");
        sql.append(Sql.getValue(agendaCadastro.nuDiaSemana)).append(",");
        sql.append(Sql.getValue(agendaCadastro.flSemanaMes)).append(",");
        sql.append(Sql.getValue(agendaCadastro.nuSequenciaAgenda)).append(",");
        sql.append(Sql.getValue(agendaCadastro.flTipoFrequencia)).append(",");
        sql.append(Sql.getValue(agendaCadastro.nuSequencia)).append(",");
        sql.append(Sql.getValue(agendaCadastro.dtBase)).append(",");
        sql.append(Sql.getValue(agendaCadastro.hrAgenda)).append(",");
        sql.append(Sql.getValue(agendaCadastro.dsObservacao)).append(",");
        sql.append(Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT)).append(",");
        sql.append(Sql.getValue(agendaCadastro.flTipoAlteracao)).append(",");
        if (LavenderePdaConfig.usaReagendamentoAgendaVisita) {
        	sql.append(Sql.getValue(agendaCadastro.dtAgendaOriginal)).append(",");
        }
        if (LavenderePdaConfig.usaDataLimiteValidadeAgendaVisita || LavenderePdaConfig.permiteIndicarAgendaVisitaValidaParaDataUnica || LavenderePdaConfig.usaReagendamentoAgendaVisita) {
        	sql.append(Sql.getValue(agendaCadastro.dtFinal)).append(",");
        }
        if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
        	sql.append(Sql.getValue(agendaCadastro.cdTipoAgenda)).append(",");
        }
        if (LavenderePdaConfig.isUsaHoraFimAgendaVisita()) {
        	sql.append(Sql.getValue(agendaCadastro.hrAgendaFim)).append(",");
        }
        sql.append(Sql.getValue(agendaCadastro.cdUsuario));
    }
    
    
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	AgendaCadastro agendaCadastro = (AgendaCadastro) domain;
    	sql.append(" NUDIASEMANA = ").append(Sql.getValue(agendaCadastro.nuDiaSemana)).append(",")
    	.append(" FLSEMANAMES = ").append(Sql.getValue(agendaCadastro.flSemanaMes)).append(",")
    	.append(" NUSEQUENCIAAGENDA = ").append(Sql.getValue(agendaCadastro.nuSequenciaAgenda)).append(",")
    	.append(" FLTIPOFREQUENCIA = ").append(Sql.getValue(agendaCadastro.flTipoFrequencia)).append(",")
    	.append(" NUSEQUENCIA = ").append(Sql.getValue(agendaCadastro.nuSequencia)).append(",")
    	.append(" DTBASE = ").append(Sql.getValue(agendaCadastro.dtBase)).append(",")
    	.append(" HRAGENDA = ").append(Sql.getValue(agendaCadastro.hrAgenda)).append(",")
    	.append(" DSOBSERVACAO = ").append(Sql.getValue(agendaCadastro.dsObservacao)).append(",")
    	.append(" DTAGENDAORIGINAL = ").append(Sql.getValue(agendaCadastro.dtAgendaOriginal)).append(",")
    	.append(" CDTIPOAGENDA = ").append(Sql.getValue(agendaCadastro.cdTipoAgenda)).append(",")
    	.append(" DTFINAL = ").append(Sql.getValue(agendaCadastro.dtFinal)).append(",")
    	.append(" HRAGENDAFIM = ").append(Sql.getValue(agendaCadastro.hrAgendaFim)).append(",")
    	.append(" CDUSUARIO = ").append(Sql.getValue(agendaCadastro.cdUsuario)).append(",")
    	.append(" FLTIPOALTERACAO = ").append(Sql.getValue(agendaCadastro.flTipoAlteracao));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	AgendaCadastro agendaCadastro = (AgendaCadastro) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", agendaCadastro.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", agendaCadastro.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", agendaCadastro.cdCliente);
		sqlWhereClause.addAndCondition("NUDIASEMANA = ", agendaCadastro.nuDiaSemana);
		sqlWhereClause.addAndOrCondition("FLSEMANAMES = ", agendaCadastro.flSemanaMes, ValueUtil.VALOR_SIM);
		sqlWhereClause.addAndCondition("NUSEQUENCIAAGENDA = ", agendaCadastro.nuSequenciaAgenda);
		if (!ValueUtil.isEmpty(agendaCadastro.nuDiaSemanaFilter)) {
			sqlWhereClause.addAndConditionForced("NUDIASEMANA = ", ValueUtil.getIntegerValue(agendaCadastro.nuDiaSemanaFilter));
		}
		if (LavenderePdaConfig.usaNuSequenciaNaChaveDaAgendaVisita) {
			sqlWhereClause.addAndCondition("NUSEQUENCIA = ", agendaCadastro.nuSequencia);
		}
		if (LavenderePdaConfig.isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes()) {
			sqlWhereClause.addAndCondition("FLTIPOFREQUENCIA = ", agendaCadastro.flTipoFrequencia);
		}
		//--
		sql.append(sqlWhereClause.getSql());
    }


	public void excluiAgendaVisitaDataFinalUltrapassada() {
		StringBuffer sql = getSqlBuffer();
		sql.append("DELETE FROM ")
				.append(tableName)
				.append(" WHERE DTFINAL < ").append(Sql.getValue(DateUtil.getCurrentDate()));
		try {
			executeUpdate(sql.toString());
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}
    
}
