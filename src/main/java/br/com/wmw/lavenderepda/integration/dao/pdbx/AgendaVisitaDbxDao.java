package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AgendaVisita;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.StatusAgenda;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.util.Date;

public class AgendaVisitaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new AgendaVisita();
	}

    private static AgendaVisitaDbxDao instance;

    public AgendaVisitaDbxDao() {
        super(AgendaVisita.TABLE_NAME);
    }

    public static AgendaVisitaDbxDao getInstance() {
        if (instance == null) {
            instance = new AgendaVisitaDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        AgendaVisita agendavisita = new AgendaVisita();
        agendavisita.rowKey = rs.getString("rowkey");
        agendavisita.cdEmpresa = rs.getString("cdEmpresa");
        agendavisita.cdRepresentante = rs.getString("cdRepresentante");
        agendavisita.cdCliente = rs.getString("cdCliente");
        agendavisita.nuDiaSemana = rs.getInt("nuDiasemana");
        agendavisita.flSemanaMes = rs.getString("flSemanames");
        agendavisita.nuSequenciaAgenda = rs.getInt("nuSequenciaAgenda");
        agendavisita.flTipoFrequencia = rs.getString("flTipofrequencia");
        agendavisita.nuSequencia = rs.getInt("nuSequencia");
        agendavisita.dtBase = rs.getDate("dtBase");
        agendavisita.hrAgenda = rs.getString("hrAgenda");
        agendavisita.dsObservacao = rs.getString("dsObservacao");
        agendavisita.nuCarimbo = rs.getInt("nuCarimbo");
        agendavisita.flTipoAlteracao = rs.getString("flTipoAlteracao");
        agendavisita.cdUsuario = rs.getString("cdUsuario");
        agendavisita.dtCriacao = rs.getDate("dtCriacao");
        if (LavenderePdaConfig.usaCopiaAgendaRepresentanteParaSupervisor) {
        	agendavisita.cdRepOriginal = rs.getString("cdRepOriginal");
        }
        if (LavenderePdaConfig.reagendaAgendaVisitaParaDiaPosteriorAnterior) {
        	agendavisita.flAgendaReagendada = rs.getString("flAgendaReagendada");
        	agendavisita.cdMotivoReagendamento = rs.getString("cdMotivoReagendamento");
        	agendavisita.dtAgendaReagendada = rs.getDate("dtAgendaReagendada");
        }
        if (LavenderePdaConfig.usaReagendamentoAgendaVisita || LavenderePdaConfig.reagendaAgendaVisitaParaDiaPosteriorAnterior) {
        	agendavisita.dtAgendaOriginal = rs.getDate("dtAgendaOriginal");
        }
        if (LavenderePdaConfig.usaDataLimiteValidadeAgendaVisita || LavenderePdaConfig.permiteIndicarAgendaVisitaValidaParaDataUnica || LavenderePdaConfig.usaReagendamentoAgendaVisita) {
        	agendavisita.dtFinal = rs.getDate("dtFinal");
        }
        if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
        	agendavisita.cdTipoAgenda = rs.getString("cdTipoAgenda");
        }
        if (LavenderePdaConfig.isUsaHoraFimAgendaVisita()) {
        	agendavisita.hrAgendaFim = rs.getString("hrAgendaFim");
        }
        if (LavenderePdaConfig.usaOrdenacaoPersonalizada()) {
        	agendavisita.nuOrdemManual = rs.getInt("nuOrdemManual");        	
        }
        return agendavisita;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		AgendaVisita agendaVisita = new AgendaVisita();
		agendaVisita.cdEmpresa = rs.getString("cdEmpresa");
		agendaVisita.cdRepresentante = rs.getString("cdRepresentante");
		agendaVisita.cdCliente = rs.getString("cdCliente");
		agendaVisita.nuDiaSemana = rs.getInt("nuDiasemana");
		agendaVisita.flSemanaMes = rs.getString("flSemanames");
		agendaVisita.nuSequenciaAgenda = rs.getInt("nuSequenciaAgenda");
		agendaVisita.flTipoFrequencia = rs.getString("flTipofrequencia");
		agendaVisita.nuSequencia = rs.getInt("nuSequencia");
		agendaVisita.dtBase = rs.getDate("dtBase");
		agendaVisita.hrAgenda = rs.getString("hrAgenda");
		agendaVisita.dsObservacao = rs.getString("dsObservacao");
        agendaVisita.dtCriacao = rs.getDate("dtCriacao");
		if (LavenderePdaConfig.usaDataLimiteValidadeAgendaVisita || LavenderePdaConfig.permiteIndicarAgendaVisitaValidaParaDataUnica || LavenderePdaConfig.usaReagendamentoAgendaVisita || LavenderePdaConfig.isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes()) {
			agendaVisita.dtFinal = rs.getDate("dtFinal");
		}
		if (LavenderePdaConfig.usaReagendamentoAgendaVisita || LavenderePdaConfig.reagendaAgendaVisitaParaDiaPosteriorAnterior) {
			agendaVisita.dtAgendaOriginal = rs.getDate("dtAgendaOriginal");
		}
		if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
			agendaVisita.cdTipoAgenda = rs.getString("cdTipoAgenda");
		}
		agendaVisita.nuCarimbo = rs.getInt("nuCarimbo");
		agendaVisita.flTipoAlteracao = rs.getString("flTipoAlteracao");
		if (LavenderePdaConfig.usaOrdenacaoPersonalizada()) {
			agendaVisita.nuOrdemManual = rs.getInt("nuOrdemManual");
		}
		agendaVisita.cdUsuario = rs.getString("cdUsuario");
		if (LavenderePdaConfig.usaCopiaAgendaRepresentanteParaSupervisor) {
			agendaVisita.cdRepOriginal = rs.getString("cdRepOriginal");
		}
		if (LavenderePdaConfig.isUsaHoraFimAgendaVisita()) {
			agendaVisita.hrAgendaFim = rs.getString("hrAgendaFim");
		}
		Visita v = new Visita();
		if (!LavenderePdaConfig.usaNuSequenciaNaChaveDaAgendaVisita) {
			v.hrChegadaVisita = rs.getString("hrChegadaVisita");
			v.hrSaidaVisita = rs.getString("hrSaidaVisita");
			v.nuPedido = rs.getString("nuPedido");
			v.flPedidoSemVisita = rs.getString("flPedidoSemVisita");
			v.flPedidoOutroCliente = rs.getString("flPedidoOutroCliente");
			v.dtAgendaVisita = rs.getDate("dtAgendaVisita");
		}
		v.flVisitaAgendada = rs.getString("flVisitaAgendada");
		v.flVisitaPositivada = rs.getString("flVisitaPositivada");
		if (LavenderePdaConfig.permiteMultAgendasNoDiaMesmoCliente) {
			v.cdChaveAgendaOrigem = rs.getString("cdChaveAgendaOrigem");
		}
		agendaVisita.setVisita(v);
		agendaVisita.cliente = new Cliente();
		agendaVisita.cliente.cdEmpresa = agendaVisita.cdEmpresa;
		agendaVisita.cliente.cdRepresentante = agendaVisita.cdRepresentante;
		agendaVisita.cliente.cdCliente = agendaVisita.cdCliente;
		agendaVisita.cliente.nmRazaoSocial = rs.getString("NMRAZAOSOCIAL");
		agendaVisita.cliente.nmFantasia = rs.getString("NMFANTASIA");
		agendaVisita.cliente.setFlStatusCliente(rs.getString("FLSTATUSCLIENTE"));
		agendaVisita.cliente.cdRede = rs.getString("CDREDE");
		agendaVisita.cliente.cdCategoria = rs.getString("CDCATEGORIA");
		agendaVisita.cliente.vlTicketMedio = rs.getDouble("VLTICKETMEDIO");
		agendaVisita.cliente.cdLatitude = rs.getDouble("CDLATITUDE");
		agendaVisita.cliente.cdLongitude = rs.getDouble("CDLONGITUDE");
		if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaAgendas) {
			agendaVisita.cliente.cdMarcadores = rs.getString("cdMarcadores");
		}
		return agendaVisita;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" DISTINCT rowkey,");
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
        sql.append(" dtCriacao,");
        if (LavenderePdaConfig.usaCopiaAgendaRepresentanteParaSupervisor) {
        	sql.append(" cdRepOriginal,");
        }
        if (LavenderePdaConfig.reagendaAgendaVisitaParaDiaPosteriorAnterior) {
        	sql.append(" flAgendaReagendada,");
        	sql.append(" cdMotivoReagendamento,");
        	sql.append(" dtAgendaReagendada,");
        }
        if (LavenderePdaConfig.usaReagendamentoAgendaVisita || LavenderePdaConfig.reagendaAgendaVisitaParaDiaPosteriorAnterior) {
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
        if (LavenderePdaConfig.usaOrdenacaoPersonalizada()) {
        	sql.append(" NUORDEMMANUAL,");        	
        }
        sql.append(" cdUsuario");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
		sql.append(" DISTINCT tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDCLIENTE,");
        sql.append(" tb.NUDIASEMANA,");
        sql.append(" tb.FLSEMANAMES,");
        sql.append(" tb.NUSEQUENCIAAGENDA,");
        sql.append(" tb.flTipoFrequencia,");
        sql.append(" tb.nuSequencia,");
        sql.append(" tb.dtBase,");
        sql.append(" tb.hrAgenda,");
        sql.append(" tb.dsObservacao,");
        sql.append(" tb.dtCriacao,");
        if (LavenderePdaConfig.usaDataLimiteValidadeAgendaVisita || LavenderePdaConfig.permiteIndicarAgendaVisitaValidaParaDataUnica || LavenderePdaConfig.usaReagendamentoAgendaVisita || LavenderePdaConfig.isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes()) {
        	sql.append(" tb.dtFinal,");
        }
        if (LavenderePdaConfig.usaReagendamentoAgendaVisita || LavenderePdaConfig.reagendaAgendaVisitaParaDiaPosteriorAnterior) {
        	sql.append(" tb.dtAgendaOriginal,");
        	sql.append(" tb.dtVencimento,");
        }
        if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
        	sql.append(" tb.cdTipoAgenda,");
        }
        sql.append(" tb.nuCarimbo,");
        sql.append(" tb.flTipoAlteracao,");
        if (LavenderePdaConfig.usaCopiaAgendaRepresentanteParaSupervisor) {
        	sql.append(" tb.cdRepOriginal,");
        }
        if (LavenderePdaConfig.isUsaHoraFimAgendaVisita()) {
        	sql.append(" tb.hrAgendaFim,");
        }
        if(LavenderePdaConfig.usaOrdenacaoPersonalizada()) {
        	sql.append(" tb.nuOrdemManual,");
        }
        sql.append(" tb.cdUsuario");
        if (!LavenderePdaConfig.usaNuSequenciaNaChaveDaAgendaVisita) {
	    	sql.append(" ,vis.HRCHEGADAVISITA,")
	    	.append(" vis.HRSAIDAVISITA,")
	    	.append(" vis.NUPEDIDO,")
	    	.append(" vis.FLPEDIDOSEMVISITA,")
	    	.append(" vis.FLPEDIDOOUTROCLIENTE");
        }
    	sql.append(" ,vis.FLVISITAAGENDADA,")
    	.append(" vis.FLVISITAPOSITIVADA,")
    	.append(" vis.DTAGENDAVISITA, ");
        if (LavenderePdaConfig.permiteMultAgendasNoDiaMesmoCliente) {
	    	sql.append(" vis.CDCHAVEAGENDAORIGEM, ");
        }
        sql.append(DaoUtil.ALIAS_CLIENTE).append(".NMRAZAOSOCIAL, ")
		        .append(DaoUtil.ALIAS_CLIENTE).append(".NMFANTASIA, ")
		        .append(DaoUtil.ALIAS_CLIENTE).append(".FLSTATUSCLIENTE, ")
		        .append(DaoUtil.ALIAS_CLIENTE).append(".CDREDE, ")
		        .append(DaoUtil.ALIAS_CLIENTE).append(".CDCATEGORIA, ")
		        .append(DaoUtil.ALIAS_CLIENTE).append(".VLTICKETMEDIO, ")
		        .append(DaoUtil.ALIAS_CLIENTE).append(".CDLATITUDE, ")
		        .append(DaoUtil.ALIAS_CLIENTE).append(".CDLONGITUDE ");
		if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaAgendas) {
			DaoUtil.addSelectMarcadores(sql);
		}
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
        if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
        	sql.append(" cdTipoAgenda,");
        }
        sql.append(" nuCarimbo,");
        sql.append(" flTipoAlteracao,");
        if (LavenderePdaConfig.usaReagendamentoAgendaVisita) {
        	sql.append(" dtAgendaOriginal,");
        }
        if (LavenderePdaConfig.usaDataLimiteValidadeAgendaVisita || LavenderePdaConfig.permiteIndicarAgendaVisitaValidaParaDataUnica || LavenderePdaConfig.usaReagendamentoAgendaVisita) {
        	sql.append(" dtFinal,");
        }
        if (LavenderePdaConfig.isUsaHoraFimAgendaVisita()) {
        	sql.append(" hrAgendaFim,");
        }
        sql.append(" nuOrdemManual,");
        sql.append(" cdUsuario,");
        sql.append(" dtCriacao");
    }
    
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	AgendaVisita agendaVisita = (AgendaVisita) domain; 
    	sql.append(Sql.getValue(agendaVisita.cdEmpresa)).append(",");
        sql.append(Sql.getValue(agendaVisita.cdRepresentante)).append(",");
        sql.append(Sql.getValue(agendaVisita.cdCliente)).append(",");
        sql.append(Sql.getValue(agendaVisita.nuDiaSemana)).append(",");
        sql.append(Sql.getValue(agendaVisita.flSemanaMes)).append(",");
        sql.append(Sql.getValue(agendaVisita.nuSequenciaAgenda)).append(",");
        sql.append(Sql.getValue(agendaVisita.flTipoFrequencia)).append(",");
        sql.append(Sql.getValue(agendaVisita.nuSequencia)).append(",");
        sql.append(Sql.getValue(agendaVisita.dtBase)).append(",");
        sql.append(Sql.getValue(agendaVisita.hrAgenda)).append(",");
        sql.append(Sql.getValue(agendaVisita.dsObservacao)).append(",");
        if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
        	sql.append(Sql.getValue(agendaVisita.cdTipoAgenda)).append(",");
        }
        sql.append(Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT)).append(",");
        sql.append(Sql.getValue(agendaVisita.flTipoAlteracao)).append(",");
        if (LavenderePdaConfig.usaReagendamentoAgendaVisita) {
        	sql.append(Sql.getValue(agendaVisita.dtAgendaOriginal)).append(",");
        }
        if (LavenderePdaConfig.usaDataLimiteValidadeAgendaVisita || LavenderePdaConfig.permiteIndicarAgendaVisitaValidaParaDataUnica || LavenderePdaConfig.usaReagendamentoAgendaVisita) {
        	sql.append(Sql.getValue(agendaVisita.dtFinal)).append(",");
        }
        if (LavenderePdaConfig.isUsaHoraFimAgendaVisita()) {
        	sql.append(Sql.getValue(agendaVisita.hrAgendaFim)).append(",");
        }
        sql.append(Sql.getValue(agendaVisita.nuOrdemManual)).append(",");
        sql.append(Sql.getValue(agendaVisita.cdUsuario)).append(",");
        sql.append(Sql.getValue(agendaVisita.dtCriacao));

    }
    
    
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { 
    	AgendaVisita agendaVisita = (AgendaVisita) domain; 
		if (agendaVisita.flTipoFrequencia != null) {
			sql.append(" FLTIPOFREQUENCIA = ").append(Sql.getValue(agendaVisita.flTipoFrequencia));
		}
		if (agendaVisita.dtFinal != null) {
			if (agendaVisita.flTipoFrequencia != null) {
				sql.append(", ");
			}
			sql.append(" DTFINAL = ").append(Sql.getValue(agendaVisita.dtFinal));
		}
    	if (LavenderePdaConfig.permiteReagendamentoAgendaParaDataIgualOriginal || LavenderePdaConfig.isUsaHoraFimAgendaVisita()) {
    		if (agendaVisita.flTipoFrequencia != null || agendaVisita.dtFinal != null) {
    			sql.append(", ");
    		}
    		sql.append(" HRAGENDA = ").append(Sql.getValue(agendaVisita.hrAgenda));
    		if (LavenderePdaConfig.isUsaHoraFimAgendaVisita()) {
        		sql.append(", HRAGENDAFIM = ").append(Sql.getValue(agendaVisita.hrAgendaFim));
            }
    	}
    	if (LavenderePdaConfig.reagendaAgendaVisitaParaDiaPosteriorAnterior) {
    		sql.append((LavenderePdaConfig.permiteReagendamentoAgendaParaDataIgualOriginal || LavenderePdaConfig.isUsaHoraFimAgendaVisita() || agendaVisita.dtFinal != null || agendaVisita.flTipoFrequencia != null) ? ", FLAGENDAREAGENDADA = "  : " FLAGENDAREAGENDADA = ");
    		sql.append(Sql.getValue(agendaVisita.flAgendaReagendada));
    	}
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	AgendaVisita agendavisita = (AgendaVisita) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		if (ValueUtil.isNotEmpty(agendavisita.cdEmpresaNegacao)) {
			sqlWhereClause.addAndCondition("tb.CDEMPRESA != ", agendavisita.cdEmpresaNegacao);
		} else {
			sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", agendavisita.cdEmpresa);
		}
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", agendavisita.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDCLIENTE = ", agendavisita.cdCliente);
		sqlWhereClause.addAndCondition("tb.NUDIASEMANA = ", agendavisita.nuDiaSemana);
		sqlWhereClause.addAndOrCondition("tb.FLSEMANAMES = ", agendavisita.flSemanaMes, ValueUtil.VALOR_SIM);
		sqlWhereClause.addAndCondition("tb.NUSEQUENCIAAGENDA = ", agendavisita.nuSequenciaAgenda);
		if (LavenderePdaConfig.reagendaAgendaVisitaParaDiaPosteriorAnterior) {
			sqlWhereClause.addAndCondition("tb.FLAGENDAREAGENDADA = ", agendavisita.flAgendaReagendada);
		}
		if (!ValueUtil.isEmpty(agendavisita.nuDiaSemanaFilter) && (!LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita || agendavisita.filterNovoPedido)) {
			sqlWhereClause.addAndConditionForced("tb.NUDIASEMANA = ", ValueUtil.getIntegerValue(agendavisita.nuDiaSemanaFilter));
		}
		if (LavenderePdaConfig.usaNuSequenciaNaChaveDaAgendaVisita) {
			sqlWhereClause.addAndCondition("tb.NUSEQUENCIA = ", agendavisita.nuSequencia);
		}
		if (LavenderePdaConfig.isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes()) {
			sqlWhereClause.addAndCondition("tb.FLTIPOFREQUENCIA = ", agendavisita.flTipoFrequencia);
		}
		//--
		sql.append(sqlWhereClause.getSql());
		if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaAgendas && ValueUtil.isNotEmpty(agendavisita.cdMarcador)) {
			sql.append(" AND EXISTS(SELECT cdMarcador from TBLVPMARCADORCLIENTE ml where ml.cdEmpresa = tb.cdEmpresa and ml.cdRepresentante = tb.CDREPRESENTANTE AND ml.cdCliente = tb.CDCLIENTE AND ml.cdMarcador = ").append(Sql.getValue(agendavisita.cdMarcador)).append(") ");
		}
		if (LavenderePdaConfig.usaClienteSemPedidoFornecedor && ValueUtil.isNotEmpty(agendavisita.cdFornecedor)) {
			sql.append(" AND NOT EXISTS (")
       		.append("SELECT 1 FROM TBLVPPEDIDOERP ERP ")
       		.append(" JOIN TBLVPITEMPEDIDOERP IERP ON ERP.CDEMPRESA = IERP.CDEMPRESA AND ")
       		.append(" ERP.CDREPRESENTANTE = IERP.CDREPRESENTANTE AND ")
       		.append(" ERP.NUPEDIDO = IERP.NUPEDIDO AND ")
       		.append(" ERP.FLORIGEMPEDIDO = IERP.FLORIGEMPEDIDO ")
       		.append(" JOIN TBLVPPRODUTO PROD ON PROD.CDEMPRESA = ERP.CDEMPRESA AND ")
       		.append(" PROD.CDREPRESENTANTE = ERP.CDREPRESENTANTE AND ")
       		.append(" PROD.CDPRODUTO = IERP.CDPRODUTO ")
       		.append(" WHERE ERP.CDEMPRESA = TB.CDEMPRESA AND ")
       		.append(" ERP.CDREPRESENTANTE = TB.CDREPRESENTANTE AND ")
       		.append(" ERP.CDCLIENTE = TB.CDCLIENTE AND ")
       		.append(" PROD.CDFORNECEDOR = ").append(Sql.getValue(agendavisita.cdFornecedor)).append(" AND ");
       		Date date = DateUtil.getCurrentDate();
       		DateUtil.decDay(date, LavenderePdaConfig.nuDiasClienteSemPedidoFornecedor);
       		sql.append(" ERP.DTEMISSAO >= ").append(Sql.getValue(date))
       		.append(")");
		}
		if (ValueUtil.isNotEmpty(agendavisita.hrAgendaConflito) && ValueUtil.isNotEmpty(agendavisita.hrAgendaFimConflito)) {
			sql.append(" AND (").append(Sql.getValue(agendavisita.hrAgendaConflito)).append(" BETWEEN TB.HRAGENDA AND TB.HRAGENDAFIM")
			.append(" OR ").append(Sql.getValue(agendavisita.hrAgendaFimConflito)).append(" BETWEEN TB.HRAGENDA AND TB.HRAGENDAFIM")
			.append(" OR ").append(" TB.HRAGENDA BETWEEN ").append(Sql.getValue(agendavisita.hrAgendaConflito)).append(" AND ").append(Sql.getValue(agendavisita.hrAgendaFimConflito))
			.append(" OR ").append(" TB.HRAGENDAFIM BETWEEN ").append(Sql.getValue(agendavisita.hrAgendaConflito)).append(" AND ").append(Sql.getValue(agendavisita.hrAgendaFimConflito))
			.append(" OR ").append(" TB.HRAGENDA = ").append(Sql.getValue(agendavisita.hrAgendaConflito)).append(" OR TB.HRAGENDAFIM = ").append(Sql.getValue(agendavisita.hrAgendaFimConflito))
			.append(" OR ").append(" TB.HRAGENDA = ").append(Sql.getValue(agendavisita.hrAgendaFimConflito)).append(" OR TB.HRAGENDAFIM = ").append(Sql.getValue(agendavisita.hrAgendaConflito)).append(")");
		}
		addFiltroCliente(sql, agendavisita);
		if (!LavenderePdaConfig.usaNuSequenciaNaChaveDaAgendaVisita) {
			if (!agendavisita.contabVariosPedidoRota) {
				addFilterExistsVisita(sql, agendavisita);
			}
			sql.append(" GROUP BY tb.CDEMPRESA, tb.CDREPRESENTANTE, tb.CDCLIENTE, tb.NUDIASEMANA, tb.FLSEMANAMES, tb.NUSEQUENCIAAGENDA ");
		}
    }

	@Override
	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		if (((AgendaVisita)domain).orderByHrAgendaDif) {
			sql.append(" ORDER BY ABS(time('now', 'localtime') - hrAgenda)");
		} else if (LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita && AgendaVisita.COL_ORDER_DTAGENDA.equalsIgnoreCase(domain.sortAtributte)) {
			sql.append(" ORDER BY ");
			sql.append(" vis.DTAGENDAVISITA ");
			sql.append(ValueUtil.VALOR_SIM.equals(domain.sortAsc) ? " ASC" : " DESC").append(",");
			if (((AgendaVisita)domain).filterAVisitar) {
				sql.append(" TB.HRAGENDA ");
			} else {
				sql.append(" vis.hrchegadavisita ");
			}
			sql.append(ValueUtil.VALOR_SIM.equals(domain.sortAsc) ? " ASC" : " DESC");
		} else if (LavenderePdaConfig.usaMelhorRotaSistema && AgendaVisita.COL_ORDER_NUORDEMMANUAL.equalsIgnoreCase(domain.sortAtributte)) {
			String order = ValueUtil.VALOR_SIM.equals(domain.sortAsc) ? " ASC" : " DESC";
			sql.append(" ORDER BY ")
			.append("tb.NUORDEMMANUAL ").append(order).append(", tb.NUSEQUENCIA ").append(order);
		} else if (domain != null && ValueUtil.isNotEmpty(domain.sortAtributte)) {
	    		boolean possuiTB = sql.indexOf(" tb ") > -1 || sql.indexOf("tb.") > -1;
	    		sql.append(" order by ");
	    		String[] sortAtributtes = StringUtil.split(domain.sortAtributte, ',');
	    		String[] sortAsc = StringUtil.split(domain.sortAsc, ',');
	    		for (int i = 0; i < sortAtributtes.length; i++) {
	    			boolean cdClienteAsNumber = LavenderePdaConfig.usaOrdemNumericaColunaCodigoCliente && "CDCLIENTE".equals(sortAtributtes[i]);
	    			if (cdClienteAsNumber) sql.append("CAST(");
	    			sql.append(possuiTB ? "tb." + sortAtributtes[i] : sortAtributtes[i]);
	    			if (cdClienteAsNumber) sql.append(" AS DECIMAL)");
	    			sql.append(ValueUtil.VALOR_SIM.equals(sortAsc[i]) ? " ASC" : " DESC");
	    			if (!(i == (sortAtributtes.length - 1))) {
	    				sql.append(" , ");
	    			}
				}    		
    		
			}
	}
    
    private void addFiltroCliente(StringBuffer sql, AgendaVisita agendavisita) {
    	if (ValueUtil.isEmpty(agendavisita.cdCategoria) && ValueUtil.isEmpty(agendavisita.cdRede) && ValueUtil.isEmpty(agendavisita.dsFiltro)) return;
    	boolean addedMultipleCondition = false;
    	
    	String dsFiltro = getDsFiltro(agendavisita.dsFiltro);
    	sql.append(" AND TB.CDCLIENTE IN (");
    	sql.append(" SELECT TC.CDCLIENTE FROM TBLVPCLIENTE TC");
    	addJoinContato(sql);
    	sql.append(" WHERE TC.CDEMPRESA = TB.CDEMPRESA");
    	sql.append(" AND TC.CDREPRESENTANTE = TB.CDREPRESENTANTE");
    	if (ValueUtil.isNotEmpty(agendavisita.cdRede) && LavenderePdaConfig.isUsaCaracteristicasClienteNoSistema()) {
    		sql.append(" AND (TC.CDREDE LIKE ").append(Sql.getValue(agendavisita.cdRede));
    		addedMultipleCondition = true;
    	}
    	if (ValueUtil.isNotEmpty(agendavisita.cdCategoria) && LavenderePdaConfig.isUsaCaracteristicasClienteNoSistema()) {
    		sql.append(addedMultipleCondition ? " AND " : " AND (");
    		sql.append("TC.CDCATEGORIA LIKE ").append(Sql.getValue(agendavisita.cdCategoria));
    		addedMultipleCondition = true;
    	}
    	if (ValueUtil.isNotEmpty(dsFiltro)) {
    		sql.append(addedMultipleCondition ? " AND " : " AND (");
    		sql.append("TC.CDCLIENTE LIKE ").append(Sql.getValue(dsFiltro));
    		sql.append(" OR TC.NMRAZAOSOCIAL LIKE ").append(Sql.getValue(dsFiltro));
    		sql.append(" OR TC.NMFANTASIA LIKE ").append(Sql.getValue(dsFiltro));
    		if (LavenderePdaConfig.usaFiltroCnpjClienteListaClientes()) {    			
    			sql.append(" OR TC.NUCNPJ LIKE ").append(Sql.getValue(dsFiltro));
    		}
    		if (LavenderePdaConfig.isUsaFiltroCidadeUFBairroNaListaDeClientes()) {
    			sql.append(" OR TC.DSCIDADECOMERCIAL LIKE ").append(Sql.getValue(dsFiltro));
    			sql.append(" OR TC.DSBAIRROCOMERCIAL LIKE ").append(Sql.getValue(dsFiltro));
    			sql.append(" OR TC.DSESTADOCOMERCIAL LIKE ").append(Sql.getValue(dsFiltro));
    			if (LavenderePdaConfig.isUsaFiltroLogradouroCidadeUFBairroNaListaDeClientes()) {
    				sql.append(" OR TC.DSLOGRADOUROCOMERCIAL LIKE ").append(Sql.getValue(dsFiltro));
    				sql.append(" OR TC.NULOGRADOUROCOMERCIAL LIKE ").append(Sql.getValue(dsFiltro));
    			}
    		}
    		if (LavenderePdaConfig.usaFiltroContatoListaClientes()) {
    			sql.append(" OR TC.NMCONTATOPRINCIPAL LIKE ").append(Sql.getValue(dsFiltro));
    			sql.append(" OR TCNTT.NMCONTATO LIKE ").append(Sql.getValue(dsFiltro));
    			sql.append(" OR TCNTTERP.NMCONTATO LIKE ").append(Sql.getValue(dsFiltro));
    		}
    		addedMultipleCondition = true;
    	}
    	sql.append(addedMultipleCondition ? "))" : ")");
    }
    
    private String getDsFiltro(String dsFiltro) {
    	if (ValueUtil.isEmpty(dsFiltro)) return "";
    	boolean onlyStartString = false;
    	if (LavenderePdaConfig.usaPesquisaInicioString) {
    		if (dsFiltro.startsWith("*")) {
    			dsFiltro = dsFiltro.substring(1);
    		} else {
    			onlyStartString = true;
    		}
    	}
    	return onlyStartString ? dsFiltro + "%" : "%" + dsFiltro + "%";
    }
    
    private void addFilterExistsVisita(StringBuffer sql, AgendaVisita domain) {
    	if (domain.statusAgendaCdPositivado > 0 && domain.statusAgendaCdPositivado != -1) {
    		if (domain.statusAgendaCdPositivado == StatusAgenda.STATUSAGENDA_CDAVISITAR) {
    			sql.append(" AND NOT EXISTS ");
    		} else {
    			sql.append(" AND EXISTS ");
    		}
    		sql.append("(SELECT 1 FROM TBLVPVISITA ")
			.append(" WHERE CDEMPRESA = tb.CDEMPRESA AND CDREPRESENTANTE = tb.CDREPRESENTANTE ")
			.append(" AND CDCLIENTE = tb.CDCLIENTE AND ")
			.append(" (STRFTIME('%w', DTAGENDAVISITA) + 1) = tb.NUDIASEMANA");
    		if (ValueUtil.isNotEmpty(domain.dtAgenda)) {
    			sql.append(" AND DTAGENDAVISITA = ").append(Sql.getValue(domain.dtAgenda));
    		}
    		if (LavenderePdaConfig.permiteMultAgendasNoDiaMesmoCliente) {
    			sql.append(" AND CDCHAVEAGENDAORIGEM = tb.ROWKEY ");
    		}
    		switch (domain.statusAgendaCdPositivado) {
	    		case StatusAgenda.STATUSAGENDA_CDAVISITAR: {
	    			sql.append(" AND FLVISITAAGENDADA = 'S'")
	    			.append(LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita ? " AND DTSAIDAVISITA IS NOT NULL)" :
	    					")");
	    			break;
	    		}
	    		case StatusAgenda.STATUSAGENDA_CDNAOPOSITIVADO:
	    		case StatusAgenda.STATUSAGENDA_CDPOSITIVADO: {
	    			sql.append(" AND FLVISITAAGENDADA = 'S' AND FLVISITAPOSITIVADA = ")
	    			.append(domain.statusAgendaCdPositivado == StatusAgenda.STATUSAGENDA_CDPOSITIVADO ? "'S'" : "'N'").append(")");
	    			break;
	    		}
	    		case StatusAgenda.STATUSAGENDA_CDREAGENDADO: {
	    			sql.append(" AND FLVISITAAGENDADA = 'S' AND FLVISITAREAGENDADA = 'S')");
	    			break;
	    		}
	    		case StatusAgenda.STATUSAGENDA_CDTRANSFERIDO: {
	    			sql.append(" AND FLVISITAAGENDADA = 'S' AND FLVISITATRANSFERIDA = 'S')");
	    			break;
	    		}
    		}
    	}
    }
    
    @Override
    protected void addJoinSummary(BaseDomain domainFilter, StringBuffer sql) {
	    AgendaVisita agendaVisitaFilter = (AgendaVisita) domainFilter;
	    String dtAgenda = DateUtil.formatDateDb(agendaVisitaFilter.dtAgenda);
	    String dtFinal = agendaVisitaFilter.dtFinal != null ?  DateUtil.formatDateDb(agendaVisitaFilter.dtFinal) : "";
	    DaoUtil.addJoinVisita(sql, dtAgenda, dtFinal);
	    DaoUtil.addJoinClienteAgendaVisita(agendaVisitaFilter, sql);
    }
    
    private void addJoinContato(StringBuffer sql) {
    	if (LavenderePdaConfig.usaFiltroContatoListaClientes()) {    	
    		sql.append(" LEFT OUTER JOIN ( ");
    		sql.append(" SELECT CDEMPRESA, CDREPRESENTANTE, CDCLIENTE,");
    		sql.append(" GROUP_CONCAT(NMCONTATO) NMCONTATO ");
    		sql.append(" FROM TBLVPCONTATO ");
    		sql.append(" GROUP BY CDEMPRESA ,CDREPRESENTANTE ,CDCLIENTE ");
    		sql.append(" ) TCNTT ON TC.CDEMPRESA = TCNTT.CDEMPRESA ");
    		sql.append(" AND TC.CDREPRESENTANTE = TCNTT.CDREPRESENTANTE ");
    		sql.append(" AND TC.CDCLIENTE = TCNTT.CDCLIENTE ");
    		sql.append(" LEFT OUTER JOIN ( ");
    		sql.append(" SELECT CDEMPRESA ,CDREPRESENTANTE ,CDCLIENTE ");
    		sql.append(" ,GROUP_CONCAT(NMCONTATO) NMCONTATO ");
    		sql.append(" FROM TBLVPCONTATOERP ");
    		sql.append(" GROUP BY CDEMPRESA ");
    		sql.append(" ,CDREPRESENTANTE ,CDCLIENTE ");
    		sql.append(" ) TCNTTERP ON TC.CDEMPRESA = TCNTTERP.CDEMPRESA ");
    		sql.append(" AND TC.CDREPRESENTANTE = TCNTTERP.CDREPRESENTANTE ");
    		sql.append(" AND TC.CDCLIENTE = TCNTTERP.CDCLIENTE ");
    	}
    }
    
    public int findMaxNuSequencia(AgendaVisita agendaVisita) throws SQLException {
    	StringBuffer sql = new StringBuffer();
    	sql.append("SELECT MAX(NUSEQUENCIAAGENDA) FROM TBLVPAGENDAVISITA WHERE")
    	.append(" CDEMPRESA = ").append(Sql.getValue(agendaVisita.cdEmpresa)).append(" AND")
    	.append(" CDREPRESENTANTE = ").append(Sql.getValue(agendaVisita.cdRepresentante)).append(" AND")
    	.append(" CDCLIENTE = ").append(Sql.getValue(agendaVisita.cdCliente)).append(" AND")
    	.append(" NUDIASEMANA = ").append(Sql.getValue(agendaVisita.nuDiaSemana)).append(" AND")
    	.append(" FLSEMANAMES = ").append(Sql.getValue(agendaVisita.flSemanaMes));
    	return getInt(sql.toString()) + 1;
//    	ResultSet rs = executeQuery(sql.toString());
//    	try {
//    		if (rs.next()) {
//    			int result = rs.getInt("MAX(NUSEQUENCIAAGENDA)");
//    			return ValueUtil.getIntegerValue(result) + 1;
//    		}
//    		return 1;
//    	} finally {
//			rs.close();
//		}
    }
    
    public boolean findAgendaVisitaByDtBase(AgendaVisita agendaVisita) throws SQLException {
    	StringBuffer sql = new StringBuffer();
    	sql.append("SELECT count(*) FROM TBLVPAGENDAVISITA WHERE")
    	.append(" CDEMPRESA = '").append(agendaVisita.cdEmpresa).append("' AND")
    	.append(" CDREPRESENTANTE = '").append(agendaVisita.cdRepresentante).append("' AND")
    	.append(" CDCLIENTE = '").append(agendaVisita.cdCliente).append("' AND")
    	.append(" NUDIASEMANA = '").append(agendaVisita.nuDiaSemana).append("' AND")
    	.append(" FLSEMANAMES = '").append(agendaVisita.flSemanaMes).append("' AND")
    	.append(" DTBASE = '").append(DateUtil.formatDateDb(agendaVisita.dtBase)).append("'");
    	return getInt(sql.toString()) > 0;
//    	ResultSet rs = executeQuery(sql.toString());
//    	try {
//			if (rs.next()) return true;
//			return false;
//		} finally {
//			rs.close();
//		}
    }
    
    public void limpaNuOrdemManualAgenda(AgendaVisita filter) throws SQLException {
    	StringBuffer sql = new StringBuffer();
    	sql.append("UPDATE TBLVPAGENDAVISITA SET NUORDEMMANUAL = 0 ")
    	.append("WHERE (NUDIASEMANA <> ").append(Sql.getValue(filter.nuDiaSemana)).append(" OR ")
    	.append("FLSEMANAMES <> ").append(Sql.getValue(filter.flSemanaMes)).append(") AND NOT ")
    	.append("(NUDIASEMANA = ").append(Sql.getValue(filter.nuDiaSemana)).append(" AND ")
    	.append("FLSEMANAMES = 'S')");
		try {
			executeUpdate(sql.toString());
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
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
