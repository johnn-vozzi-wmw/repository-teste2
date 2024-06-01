package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.AgendaAtendimento;
import totalcross.sql.ResultSet;

public class AgendaAtendimentoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new AgendaAtendimento();
	}

    private static AgendaAtendimentoDbxDao instance;

    public AgendaAtendimentoDbxDao() {
        super(AgendaAtendimento.TABLE_NAME);
    }
    
    public static AgendaAtendimentoDbxDao getInstance() {
        if (instance == null) {
            instance = new AgendaAtendimentoDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        AgendaAtendimento agendaAtendimento = new AgendaAtendimento();
        agendaAtendimento.rowKey = rs.getString("rowkey");
        agendaAtendimento.cdEmpresa = rs.getString("cdEmpresa");
        agendaAtendimento.cdAgendaAtendimento = rs.getString("cdAgendaAtendimento");
        agendaAtendimento.cdCliente = rs.getString("cdCliente");
        agendaAtendimento.cdRepresentante = rs.getString("cdRepresentante");
        agendaAtendimento.cdUsuario = rs.getString("cdUsuario");
        agendaAtendimento.cdLote = rs.getString("cdLote");
        agendaAtendimento.cdCampanhaGeral = rs.getString("cdCampanhaGeral");
        agendaAtendimento.nmContato = rs.getString("nmContato");
        agendaAtendimento.nuFone = rs.getString("nuFone");
        agendaAtendimento.dtAgendaAtendimento = rs.getDate("dtAgendaAtendimento");
        agendaAtendimento.dsAgendaAtendimento = rs.getString("dsAgendaAtendimento");
        agendaAtendimento.dtVencimento = rs.getDate("dtVencimento");
        agendaAtendimento.hrAgendaAtendimento = rs.getString("hrAgendaAtendimento");
        agendaAtendimento.dsObservacao = rs.getString("dsObservacao");
        agendaAtendimento.cdAgendaAnterior = rs.getString("cdAgendaAnterior");
        agendaAtendimento.flReagendamento = rs.getString("flReagendamento");
        agendaAtendimento.cdUsuarioAgenda = rs.getString("cdUsuarioAgenda");
        agendaAtendimento.cdMotivoTransferencia = rs.getString("cdMotivoTransferencia");
        agendaAtendimento.nuCarimbo = rs.getInt("nuCarimbo");
        agendaAtendimento.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return agendaAtendimento;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
	
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDAGENDAATENDIMENTO,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDUSUARIO,");
        sql.append(" CDLOTE,");
        sql.append(" CDCAMPANHAGERAL,");
        sql.append(" NMCONTATO,");
        sql.append(" NUFONE,");
        sql.append(" DTAGENDAATENDIMENTO,");
        sql.append(" DSAGENDAATENDIMENTO,");
        sql.append(" DTVENCIMENTO,");
        sql.append(" HRAGENDAATENDIMENTO,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" CDAGENDAANTERIOR,");
        sql.append(" FLREAGENDAMENTO,");
        sql.append(" CDUSUARIOAGENDA,");
        sql.append(" CDMOTIVOTRANSFERENCIA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDAGENDAATENDIMENTO,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDUSUARIO,");
        sql.append(" CDLOTE,");
        sql.append(" CDCAMPANHAGERAL,");
        sql.append(" NMCONTATO,");
        sql.append(" NUFONE,");
        sql.append(" DTAGENDAATENDIMENTO,");
        sql.append(" DSAGENDAATENDIMENTO,");
        sql.append(" DTVENCIMENTO,");
        sql.append(" HRAGENDAATENDIMENTO,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" CDAGENDAANTERIOR,");
        sql.append(" FLREAGENDAMENTO,");
        sql.append(" CDUSUARIOAGENDA,");
        sql.append(" CDMOTIVOTRANSFERENCIA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        AgendaAtendimento agendaAtendimento = (AgendaAtendimento) domain;
        sql.append(Sql.getValue(agendaAtendimento.cdEmpresa)).append(",");
        sql.append(Sql.getValue(agendaAtendimento.cdAgendaAtendimento)).append(",");
        sql.append(Sql.getValue(agendaAtendimento.cdCliente)).append(",");
        sql.append(Sql.getValue(agendaAtendimento.cdRepresentante)).append(",");
        sql.append(Sql.getValue(agendaAtendimento.cdUsuario)).append(",");
        sql.append(Sql.getValue(agendaAtendimento.cdLote)).append(",");
        sql.append(Sql.getValue(agendaAtendimento.cdCampanhaGeral)).append(",");
        sql.append(Sql.getValue(agendaAtendimento.nmContato)).append(",");
        sql.append(Sql.getValue(agendaAtendimento.nuFone)).append(",");
        sql.append(Sql.getValue(agendaAtendimento.dtAgendaAtendimento)).append(",");
        sql.append(Sql.getValue(agendaAtendimento.dsAgendaAtendimento)).append(",");
        sql.append(Sql.getValue(agendaAtendimento.dtVencimento)).append(",");
        sql.append(Sql.getValue(agendaAtendimento.hrAgendaAtendimento)).append(",");
        sql.append(Sql.getValue(agendaAtendimento.dsObservacao)).append(",");
        sql.append(Sql.getValue(agendaAtendimento.cdAgendaAnterior)).append(",");
        sql.append(Sql.getValue(agendaAtendimento.flReagendamento)).append(",");
        sql.append(Sql.getValue(agendaAtendimento.cdUsuarioAgenda)).append(",");
        sql.append(Sql.getValue(agendaAtendimento.cdMotivoTransferencia)).append(",");
        sql.append(Sql.getValue(agendaAtendimento.nuCarimbo)).append(",");
        sql.append(Sql.getValue(agendaAtendimento.flTipoAlteracao));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        AgendaAtendimento agendaAtendimento = (AgendaAtendimento) domain;
        sql.append(" CDCLIENTE = ").append(Sql.getValue(agendaAtendimento.cdCliente)).append(",");
        sql.append(" CDREPRESENTANTE = ").append(Sql.getValue(agendaAtendimento.cdRepresentante)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(agendaAtendimento.cdUsuario)).append(",");
        sql.append(" CDLOTE = ").append(Sql.getValue(agendaAtendimento.cdLote)).append(",");
        sql.append(" CDCAMPANHAGERAL = ").append(Sql.getValue(agendaAtendimento.cdCampanhaGeral)).append(",");
        sql.append(" NMCONTATO = ").append(Sql.getValue(agendaAtendimento.nmContato)).append(",");
        sql.append(" NUFONE = ").append(Sql.getValue(agendaAtendimento.nuFone)).append(",");
        sql.append(" DTAGENDAATENDIMENTO = ").append(Sql.getValue(agendaAtendimento.dtAgendaAtendimento)).append(",");
        sql.append(" DSAGENDAATENDIMENTO = ").append(Sql.getValue(agendaAtendimento.dsAgendaAtendimento)).append(",");
        sql.append(" DTVENCIMENTO = ").append(Sql.getValue(agendaAtendimento.dtVencimento)).append(",");
        sql.append(" HRAGENDAATENDIMENTO = ").append(Sql.getValue(agendaAtendimento.hrAgendaAtendimento)).append(",");
        sql.append(" DSOBSERVACAO = ").append(Sql.getValue(agendaAtendimento.dsObservacao)).append(",");
        sql.append(" CDAGENDAANTERIOR = ").append(Sql.getValue(agendaAtendimento.cdAgendaAnterior)).append(",");
        sql.append(" FLREAGENDAMENTO = ").append(Sql.getValue(agendaAtendimento.flReagendamento)).append(",");
        sql.append(" CDUSUARIOAGENDA = ").append(Sql.getValue(agendaAtendimento.cdUsuarioAgenda)).append(",");
        sql.append(" CDMOTIVOTRANSFERENCIA = ").append(Sql.getValue(agendaAtendimento.cdMotivoTransferencia)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(agendaAtendimento.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(agendaAtendimento.flTipoAlteracao));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        AgendaAtendimento agendaAtendimento = (AgendaAtendimento) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", agendaAtendimento.cdEmpresa);
		sqlWhereClause.addAndCondition("CDAGENDAATENDIMENTO = ", agendaAtendimento.cdAgendaAtendimento);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}