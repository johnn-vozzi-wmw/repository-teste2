package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Visita;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class VisitaPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Visita();
	}

    private static VisitaPdbxDao instance;
	public static String erroOcorridoAtualizacao;


    public VisitaPdbxDao() {
        super(Visita.TABLE_NAME);
    }

    public static VisitaPdbxDao getInstance() {
        if (instance == null) {
            instance = new VisitaPdbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Visita visita = new Visita();
        visita.rowKey = rs.getString("rowkey");
        visita.cdEmpresa = rs.getString("cdEmpresa");
        visita.cdRepresentante = rs.getString("cdRepresentante");
        visita.flOrigemVisita = rs.getString("flOrigemVisita");
        visita.cdVisita = rs.getString("cdVisita");
        visita.cdCliente = rs.getString("cdCliente");
        visita.flVisitaPositivada = rs.getString("flVisitapositivada");
        visita.cdMotivoRegistroVisita = rs.getString("cdMotivoregistrovisita");
        visita.dtVisita = rs.getDate("dtVisita");
        visita.hrVisita = rs.getString("hrVisita");
        visita.dsObservacao = rs.getString("dsObservacao");
        visita.dtChegadaVisita = rs.getDate("dtChegadaVisita");
        visita.hrChegadaVisita = rs.getString("hrChegadaVisita");
        visita.dsObservacaoChegada = rs.getString("dsObservacaoChegada");
        visita.dtSaidaVisita = rs.getDate("dtSaidaVisita");
        visita.hrSaidaVisita = rs.getString("hrSaidaVisita");
        visita.dsObservacaoSaida = rs.getString("dsObservacaoSaida");
        visita.nuPedido = rs.getString("nuPedido");
        visita.nuSequencia = rs.getInt("nuSequencia");
        visita.dtServidor = rs.getDate("dtServidor");
        visita.hrServidor = rs.getString("hrServidor");
        visita.flEnviadoServidor = rs.getString("flEnviadoServidor");
        visita.flPedidoSemVisita = rs.getString("flPedidoSemVisita");
        visita.flPedidoOutroCliente = rs.getString("flPedidoOutroCliente");
        visita.flVisitaReagendada = rs.getString("flVisitaReagendada");
        visita.flTipoAlteracao = rs.getString("flTipoAlteracao");
        visita.cdUsuario = rs.getString("cdUsuario");
        visita.flVisitaAgendada = rs.getString("flVisitaAgendada");
        if (LavenderePdaConfig.usaCopiaAgendaRepresentanteParaSupervisor) {
        	visita.cdRepOriginal = rs.getString("cdRepOriginal");
        }
        if (LavenderePdaConfig.usaTransferenciaAgendaVisitaParaAgendaAtendimento || LavenderePdaConfig.usaReagendamentoAgendaVisita) {
        	visita.cdMotivoReagendamento = rs.getString("cdMotivoReagendamento");
        }
        if (LavenderePdaConfig.usaTransferenciaAgendaVisitaParaAgendaAtendimento) {
        	visita.flVisitaTransferida = rs.getString("flVisitaTransferida");
        }
        visita.dtAgendaVisita = rs.getDate("dtAgendaVisita");
        if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
        	visita.cdTipoAgenda = rs.getString("cdTipoAgenda");
        }
        if (LavenderePdaConfig.isToleranciaVisitasForaAgenda() && LavenderePdaConfig.liberaSenhaVisitaClienteForaAgenda) {
        	visita.flLiberadoSenha = rs.getString("flLiberadoSenha");
        }
        if (LavenderePdaConfig.isNaoConsideraRegistroVisitaManualTolerancia() || LavenderePdaConfig.isNaoConsideraApenasRegistroVisitaManualDiaAnteriorTolerancia()) {
        	visita.flVisitaManual = rs.getString("flVisitaManual");
        }
        if (LavenderePdaConfig.usaGeracaoVisitaProspectNovoCliente) {
        	visita.flNovoClienteProspect = rs.getString("flNovoClienteProspect");
        	visita.flLocalizacaoChegada = rs.getString("flLocalizacaoChegada");
        	visita.flLocalizacaoSaida = rs.getString("flLocalizacaoSaida");
        }
        if (LavenderePdaConfig.permiteMultAgendasNoDiaMesmoCliente) {
            visita.cdChaveAgendaOrigem = rs.getString("cdChaveAgendaOrigem");
        }
        return visita;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMVISITA,");
        sql.append(" CDVISITA,");
        sql.append(" CDCLIENTE,");
        sql.append(" FLVISITAPOSITIVADA,");
        sql.append(" CDMOTIVOREGISTROVISITA,");
        sql.append(" DTVISITA,");
        sql.append(" HRVISITA,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" DTCHEGADAVISITA,");
        sql.append(" HRCHEGADAVISITA,");
        sql.append(" DSOBSERVACAOCHEGADA,");
        sql.append(" DTSAIDAVISITA,");
        sql.append(" HRSAIDAVISITA,");
        sql.append(" DSOBSERVACAOSAIDA,");
        sql.append(" NUPEDIDO,");
        sql.append(" NUSEQUENCIA,");
        sql.append(" DTSERVIDOR,");
        sql.append(" HRSERVIDOR,");
        sql.append(" FLENVIADOSERVIDOR,");
        sql.append(" FLPEDIDOSEMVISITA,");
        sql.append(" FLPEDIDOOUTROCLIENTE,");
        sql.append(" FLVISITAREAGENDADA,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" FLVISITAAGENDADA,");
        if (LavenderePdaConfig.usaCopiaAgendaRepresentanteParaSupervisor) {
        	sql.append(" CDREPORIGINAL,");
        }
        if (LavenderePdaConfig.usaTransferenciaAgendaVisitaParaAgendaAtendimento || LavenderePdaConfig.usaReagendamentoAgendaVisita) {
        	sql.append(" CDMOTIVOREAGENDAMENTO,");
        }
        if (LavenderePdaConfig.usaTransferenciaAgendaVisitaParaAgendaAtendimento) {
        	sql.append(" FLVISITATRANSFERIDA,");
        }
        sql.append(" DTAGENDAVISITA,");
        if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
        	sql.append(" CDTIPOAGENDA,");
        }
        if (LavenderePdaConfig.isToleranciaVisitasForaAgenda() && LavenderePdaConfig.liberaSenhaVisitaClienteForaAgenda) {
        	sql.append(" FLLIBERADOSENHA,");
        }
        if (LavenderePdaConfig.isNaoConsideraRegistroVisitaManualTolerancia() || LavenderePdaConfig.isNaoConsideraApenasRegistroVisitaManualDiaAnteriorTolerancia()) {
        	sql.append(" FLVISITAMANUAL,");
        }
        if (LavenderePdaConfig.usaGeracaoVisitaProspectNovoCliente) {
        	sql.append(" FLNOVOCLIENTEPROSPECT,");
        	sql.append(" FLLOCALIZACAOCHEGADA,");
        	sql.append(" FLLOCALIZACAOSAIDA,");
        }
        if (LavenderePdaConfig.permiteMultAgendasNoDiaMesmoCliente) {
        	sql.append(" CDCHAVEAGENDAORIGEM,");
        }
        sql.append(" CDUSUARIO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMVISITA,");
        sql.append(" CDVISITA,");
        sql.append(" CDCLIENTE,");
        sql.append(" FLVISITAPOSITIVADA,");
        sql.append(" CDMOTIVOREGISTROVISITA,");
        sql.append(" DTVISITA,");
        sql.append(" HRVISITA,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" DTCHEGADAVISITA,");
        sql.append(" HRCHEGADAVISITA,");
        sql.append(" DSOBSERVACAOCHEGADA,");
        sql.append(" DTSAIDAVISITA,");
        sql.append(" HRSAIDAVISITA,");
        sql.append(" DSOBSERVACAOSAIDA,");
        sql.append(" NUPEDIDO,");
        sql.append(" NUSEQUENCIA,");
        sql.append(" DTSERVIDOR,");
        sql.append(" HRSERVIDOR,");
        sql.append(" FLENVIADOSERVIDOR,");
        sql.append(" FLPEDIDOSEMVISITA,");
        sql.append(" FLPEDIDOOUTROCLIENTE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" FLVISITAAGENDADA,");
        if (LavenderePdaConfig.usaCopiaAgendaRepresentanteParaSupervisor) {
        	sql.append(" CDREPORIGINAL,");
        }
        if (LavenderePdaConfig.usaReagendamentoAgendaVisita) {
        	sql.append(" FLVISITAREAGENDADA,");
        }
        if (LavenderePdaConfig.usaTransferenciaAgendaVisitaParaAgendaAtendimento || LavenderePdaConfig.usaReagendamentoAgendaVisita) {
        	sql.append(" CDMOTIVOREAGENDAMENTO,");
        }
        if (LavenderePdaConfig.usaTransferenciaAgendaVisitaParaAgendaAtendimento) {
        	sql.append(" FLVISITATRANSFERIDA,");
        }
        sql.append(" DTAGENDAVISITA,");
        if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
        	sql.append(" CDTIPOAGENDA,");
        }
        if (LavenderePdaConfig.isToleranciaVisitasForaAgenda() && LavenderePdaConfig.liberaSenhaVisitaClienteForaAgenda) {
        	sql.append(" FLLIBERADOSENHA,");
        }
        if (LavenderePdaConfig.isNaoConsideraRegistroVisitaManualTolerancia() || LavenderePdaConfig.isNaoConsideraApenasRegistroVisitaManualDiaAnteriorTolerancia()) {
        	sql.append(" FLVISITAMANUAL,");
        }
        if (LavenderePdaConfig.usaGeracaoVisitaProspectNovoCliente) {
        	sql.append(" FLNOVOCLIENTEPROSPECT,");
        	sql.append(" FLLOCALIZACAOCHEGADA,");
        	sql.append(" FLLOCALIZACAOSAIDA,");
        }
        if (LavenderePdaConfig.permiteMultAgendasNoDiaMesmoCliente) {
        	sql.append(" CDCHAVEAGENDAORIGEM,");
        }
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Visita visita = (Visita) domain;
        sql.append(Sql.getValue(visita.cdEmpresa)).append(",");
        sql.append(Sql.getValue(visita.cdRepresentante)).append(",");
        sql.append(Sql.getValue(visita.flOrigemVisita)).append(",");
        sql.append(Sql.getValue(visita.cdVisita)).append(",");
        sql.append(Sql.getValue(visita.cdCliente)).append(",");
        sql.append(Sql.getValue(visita.flVisitaPositivada)).append(",");
        sql.append(Sql.getValue(visita.cdMotivoRegistroVisita)).append(",");
        sql.append(Sql.getValue(visita.dtVisita)).append(",");
        sql.append(Sql.getValue(visita.hrVisita)).append(",");
        sql.append(Sql.getValue(visita.dsObservacao)).append(",");
        sql.append(Sql.getValue(visita.dtChegadaVisita)).append(",");
        sql.append(Sql.getValue(visita.hrChegadaVisita)).append(",");
        sql.append(Sql.getValue(visita.dsObservacaoChegada)).append(",");
        sql.append(Sql.getValue(visita.dtSaidaVisita)).append(",");
        sql.append(Sql.getValue(visita.hrSaidaVisita)).append(",");
        sql.append(Sql.getValue(visita.dsObservacaoSaida)).append(",");
        sql.append(Sql.getValue(visita.nuPedido)).append(",");
        sql.append(Sql.getValue(visita.nuSequencia)).append(",");
        sql.append(Sql.getValue(visita.dtServidor)).append(",");
        sql.append(Sql.getValue(visita.hrServidor)).append(",");
        sql.append(Sql.getValue(visita.flEnviadoServidor)).append(",");
        sql.append(Sql.getValue(visita.flPedidoSemVisita)).append(",");
        sql.append(Sql.getValue(visita.flPedidoOutroCliente)).append(",");
        sql.append(Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT)).append(",");
        sql.append(Sql.getValue(visita.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(visita.flVisitaAgendada)).append(",");
        if (LavenderePdaConfig.usaCopiaAgendaRepresentanteParaSupervisor) {
        	sql.append(Sql.getValue(visita.cdRepOriginal)).append(",");
        }
        if (LavenderePdaConfig.usaReagendamentoAgendaVisita) {
        	sql.append(Sql.getValue(visita.flVisitaReagendada)).append(",");
        }
        if (LavenderePdaConfig.usaTransferenciaAgendaVisitaParaAgendaAtendimento || LavenderePdaConfig.usaReagendamentoAgendaVisita) {
        	sql.append(Sql.getValue(visita.cdMotivoReagendamento)).append(",");
        }
        if (LavenderePdaConfig.usaTransferenciaAgendaVisitaParaAgendaAtendimento) {
        	sql.append(Sql.getValue(visita.flVisitaTransferida)).append(",");
        }
        sql.append(Sql.getValue(visita.dtAgendaVisita)).append(",");
        if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
        	sql.append(Sql.getValue(visita.cdTipoAgenda)).append(",");
        }
        if (LavenderePdaConfig.isToleranciaVisitasForaAgenda() && LavenderePdaConfig.liberaSenhaVisitaClienteForaAgenda) {
        	sql.append(Sql.getValue(visita.flLiberadoSenha)).append(",");
        }
        if (LavenderePdaConfig.isNaoConsideraRegistroVisitaManualTolerancia() || LavenderePdaConfig.isNaoConsideraApenasRegistroVisitaManualDiaAnteriorTolerancia()) {
        	sql.append(Sql.getValue(visita.flVisitaManual)).append(",");
        }
        if (LavenderePdaConfig.usaGeracaoVisitaProspectNovoCliente) {
        	sql.append(Sql.getValue(visita.flNovoClienteProspect)).append(",");
        	sql.append(Sql.getValue(visita.flLocalizacaoChegada)).append(",");
        	sql.append(Sql.getValue(visita.flLocalizacaoSaida)).append(",");
        }
        if (LavenderePdaConfig.permiteMultAgendasNoDiaMesmoCliente) {
        	sql.append(Sql.getValue(visita.cdChaveAgendaOrigem)).append(",");
        }
        sql.append(Sql.getValue(visita.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Visita visita = (Visita) domain;
        sql.append(" CDCLIENTE = ").append(Sql.getValue(visita.cdCliente)).append(",");
        sql.append(" FLVISITAPOSITIVADA = ").append(Sql.getValue(visita.flVisitaPositivada)).append(",");
        sql.append(" CDMOTIVOREGISTROVISITA = ").append(Sql.getValue(visita.cdMotivoRegistroVisita)).append(",");
        sql.append(" HRVISITA = ").append(Sql.getValue(visita.hrVisita)).append(",");
        sql.append(" DSOBSERVACAO = ").append(Sql.getValue(visita.dsObservacao)).append(",");
        sql.append(" DTCHEGADAVISITA = ").append(Sql.getValue(visita.dtChegadaVisita)).append(",");
        sql.append(" HRCHEGADAVISITA = ").append(Sql.getValue(visita.hrChegadaVisita)).append(",");
        sql.append(" DSOBSERVACAOCHEGADA = ").append(Sql.getValue(visita.dsObservacaoChegada)).append(",");
        sql.append(" DTSAIDAVISITA = ").append(Sql.getValue(visita.dtSaidaVisita)).append(",");
        sql.append(" HRSAIDAVISITA = ").append(Sql.getValue(visita.hrSaidaVisita)).append(",");
        sql.append(" DSOBSERVACAOSAIDA = ").append(Sql.getValue(visita.dsObservacaoSaida)).append(",");
        sql.append(" NUPEDIDO = ").append(Sql.getValue(visita.nuPedido)).append(",");
        sql.append(" NUSEQUENCIA = ").append(Sql.getValue(visita.nuSequencia)).append(",");
        sql.append(" FLENVIADOSERVIDOR = ").append(Sql.getValue(visita.flEnviadoServidor)).append(",");
        sql.append(" FLPEDIDOSEMVISITA = ").append(Sql.getValue(visita.flPedidoSemVisita)).append(",");
        sql.append(" FLPEDIDOOUTROCLIENTE = ").append(Sql.getValue(visita.flPedidoOutroCliente)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(visita.flTipoAlteracao)).append(",");
        if (LavenderePdaConfig.usaReagendamentoAgendaVisita) {
        	sql.append(" FLVISITAREAGENDADA = ").append(Sql.getValue(visita.flVisitaReagendada)).append(",");
        }
        if (LavenderePdaConfig.usaTransferenciaAgendaVisitaParaAgendaAtendimento || LavenderePdaConfig.usaReagendamentoAgendaVisita) {
        	sql.append(" CDMOTIVOREAGENDAMENTO = ").append(Sql.getValue(visita.cdMotivoReagendamento)).append(",");
        }
        if (LavenderePdaConfig.usaTransferenciaAgendaVisitaParaAgendaAtendimento) {
        	sql.append(" FLVISITATRANSFERIDA = ").append(Sql.getValue(visita.flVisitaTransferida)).append(",");
        }
        if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
        	sql.append(" CDTIPOAGENDA = ").append(Sql.getValue(visita.cdTipoAgenda)).append(",");
        }
        if (LavenderePdaConfig.isToleranciaVisitasForaAgenda() && LavenderePdaConfig.liberaSenhaVisitaClienteForaAgenda) {
        	sql.append(" FLLIBERADOSENHA = ").append(Sql.getValue(visita.flLiberadoSenha)).append(",");
        }
        if (LavenderePdaConfig.usaGeracaoVisitaProspectNovoCliente) {
        	sql.append(" FLLOCALIZACAOCHEGADA = ").append(Sql.getValue(visita.flLocalizacaoChegada)).append(",");
        	sql.append(" FLLOCALIZACAOSAIDA = ").append(Sql.getValue(visita.flLocalizacaoSaida)).append(",");
        }
        sql.append(" FLVISITAMANUAL = ").append(Sql.getValue(visita.flVisitaManual)).append(",");
        
        sql.append(" CDUSUARIO = ").append(Sql.getValue(visita.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Visita visita = (Visita) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		if (ValueUtil.isNotEmpty(visita.cdEmpresaNegacao)) {
			sqlWhereClause.addAndCondition("CDEMPRESA != ", visita.cdEmpresaNegacao);
		} else {
			sqlWhereClause.addAndCondition("CDEMPRESA = ", visita.cdEmpresa);
		}
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", visita.cdRepresentante);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", visita.nuPedido);
		sqlWhereClause.addAndCondition("NUSEQUENCIA = ", visita.nuSequencia);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", visita.cdCliente);
		sqlWhereClause.addAndCondition("FLORIGEMVISITA = ", visita.flOrigemVisita);
		sqlWhereClause.addAndCondition("CDVISITA = ", visita.cdVisita);
		if (ValueUtil.isNotEmpty(visita.dtChegadaVisita)) {
			if (ValueUtil.isNotEmpty(visita.dtVisita)) {
				sqlWhereClause.addAndCondition("(DTCHEGADAVISITA = ", visita.dtChegadaVisita);
				sqlWhereClause.addOrCondition("DTVISITA = " + Sql.getValue(visita.dtVisita) + ")");
			} else {
				sqlWhereClause.addAndCondition("DTCHEGADAVISITA = ", visita.dtChegadaVisita);
			}
		} else {
			sqlWhereClause.addAndCondition("DTVISITA = ", visita.dtVisita);
		}
		if (ValueUtil.isNotEmpty(visita.filterDtVisita)) {
			sqlWhereClause.addOrCondition("DTVISITA = " + Sql.getValue(visita.filterDtVisita));
		}
		if (visita.filterHrChegadaVisitaNotNull) {
			sqlWhereClause.addAndCondition("HRCHEGADAVISITA != ''");
		}
		if (visita.filterHrSaidaVisitaNull) {
			sqlWhereClause.addAndCondition("HRSAIDAVISITA = ''");
		}
		if (visita.filterNuPedidoNull) {
			sqlWhereClause.addAndCondition("NUPEDIDO = ''");
			sqlWhereClause.addAndCondition("(FLPEDIDOSEMVISITA != 'S'");
			sqlWhereClause.addAndCondition("FLPEDIDOOUTROCLIENTE != 'S')");
		}
		sqlWhereClause.addAndCondition("FLENVIADOSERVIDOR = ", visita.flEnviadoServidor);
		if (visita.filterFlTipoAlteracao) {
			sqlWhereClause.addAndConditionForced("FLTIPOALTERACAO = ", Sql.getValue(visita.flTipoAlteracao));
		} else {
			sqlWhereClause.addAndCondition("FLTIPOALTERACAO = ", visita.flTipoAlteracao);
		}
		sqlWhereClause.addAndCondition("FLVISITAPOSITIVADA = ", visita.flVisitaPositivada);
		sqlWhereClause.addAndCondition("DTAGENDAVISITA = ", visita.dtAgendaVisita);
		if (LavenderePdaConfig.isNaoConsideraApenasRegistroVisitaManualDiaAnteriorTolerancia() && ValueUtil.isNotEmpty(visita.nuSeqFilter)) {
			sqlWhereClause.addAndCondition("NUSEQUENCIA != " +  Sql.getValue(ValueUtil.getIntegerValue(visita.nuSeqFilter)));
		}
		sqlWhereClause.addAndCondition("FLVISITAMANUAL = ", visita.flVisitaManual);
		sqlWhereClause.addAndConditionEquals("CDCHAVEAGENDAORIGEM", visita.cdChaveAgendaOrigem);
		//--
		sql.append(sqlWhereClause.getSql());
    }


    public Vector findVisitasAntigas(BaseDomain domain) throws SQLException {
    	Visita visita = (Visita) domain;
    	Vector visitasAntigasList = new Vector();
    	StringBuffer sql = getSqlBuffer();
        sql.append(" select * from ");
        sql.append(tableName);
        //--
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", visita.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", visita.cdRepresentante);
		if (ValueUtil.isNotEmpty(visita.dtChegadaVisita)) {
			sqlWhereClause.addAndCondition("DTCHEGADAVISITA < ", visita.dtChegadaVisita);
		} else {
			sqlWhereClause.addAndCondition("DTVISITA < ", visita.dtVisita);
		}
		//--
		sql.append(sqlWhereClause.getSql());
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
	    	while (rs.next()) {
	    		Visita visitaAntiga = new Visita();
	    		visitaAntiga.cdEmpresa = rs.getString("CDEMPRESA");
	    		visitaAntiga.cdRepresentante = rs.getString("CDREPRESENTANTE");
	    		visitaAntiga.flOrigemVisita = rs.getString("FLORIGEMVISITA");
	    		visitaAntiga.cdVisita = rs.getString("CDVISITA");
	    		visitasAntigasList.addElement(visitaAntiga);
	    	}
	    	return visitasAntigasList;
    	}
    }

    public void deletaVisitaByData(BaseDomain domain) {
    	Visita visita = (Visita) domain;
    	StringBuffer sql = getSqlBuffer();
        sql.append(" delete from ");
        sql.append(tableName);
        //--
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", visita.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", visita.cdRepresentante);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", visita.nuPedido);
		sqlWhereClause.addAndCondition("NUSEQUENCIA = ", visita.nuSequencia);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", visita.cdCliente);
		if (ValueUtil.isNotEmpty(visita.dtChegadaVisita)) {
			sqlWhereClause.addAndCondition("DTCHEGADAVISITA < ", visita.dtChegadaVisita);
		} else {
			sqlWhereClause.addAndCondition("DTVISITA < ", visita.dtVisita);
		}
		//--
		sql.append(sqlWhereClause.getSql());
        //--
        try {
        	executeUpdate(sql.toString());
        } catch (Throwable e) {
			// Não faz anda, apenas não exclui nenhum registro
		}
    }

    @Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	if ((domain != null) && ValueUtil.isNotEmpty(domain.sortAtributte)) {
			super.addOrderBy(sql, domain);
		} 
    }
    
	public int findCountVisitasRealizadas() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT count(*) as nuVisitas");
		sql.append(" FROM ").append(this.tableName);
		sql.append(" WHERE DTVISITA < ").append(Sql.getValue(DateUtil.getCurrentDate()));
		return getInt(sql.toString());
	}
	
	public Vector findVisitasNaoEnviadasServidor() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT * FROM ").append(this.tableName);
		sql.append(" WHERE CDEMPRESA = ").append(Sql.getValue(SessionLavenderePda.cdEmpresa));
		if (!SessionLavenderePda.isUsuarioSupervisor()) {
		sql.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(SessionLavenderePda.getRepresentante().cdRepresentante));
		}
		sql.append(" AND FLENVIADOSERVIDOR <> ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector visitaList = new Vector();
			while (rs.next()) {
				visitaList.addElement(populate(getBaseDomainDefault(), rs));
			}
			return visitaList;
		}
	}
	
	public void updateVisitaTramsmitidaComSucesso(Visita visita) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" update ").append(tableName).append(" set");
		sql.append(" FLENVIADOSERVIDOR = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		sql.append(" ,FLTIPOALTERACAO = ").append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ORIGINAL));
		sql.append(" where ROWKEY = ").append(Sql.getValue(visita.getRowKey()));
		//--
		executeUpdate(sql.toString());
	}
}