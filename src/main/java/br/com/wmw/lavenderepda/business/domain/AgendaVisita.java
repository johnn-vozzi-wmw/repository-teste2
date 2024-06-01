package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import totalcross.util.Date;

public class AgendaVisita extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPAGENDAVISITA";
	
	public static final String FLTIPOFREQUENCIA_SEM_FREQUENCIA = "0";
	public static final String FLTIPOFREQUENCIA_SEMANAL = "1";
	public static final String FLTIPOFREQUENCIA_QUINZENAL = "2";
	public static final String FLTIPOFREQUENCIA_MENSAL = "3";
	public static final String FLTIPOFREQUENCIA_UNICA = "4";
	public static final String FLTIPOFREQUENCIA_BIMESTRAL = "B";
	public static final String FLTIPOFREQUENCIA_TRIMESTRAL = "T";
	
	public static final String FLDSTIPOFREQUENCIA_SEM_FREQUENCIA = "Sempre neste dia";
	public static final String FLDSTIPOFREQUENCIA_SEMANAL = "Semanal";
	public static final String FLDSTIPOFREQUENCIA_QUINZENAL = "Quinzenal";
	public static final String FLDSTIPOFREQUENCIA_MENSAL = "A cada 4 semanas";
	public static final String FLDSTIPOFREQUENCIA_UNICA = "Única";
	public static final String FLDSTIPOFREQUENCIA_BIMESTRAL =  "Bimestral";
	public static final String FLDSTIPOFREQUENCIA_TRIMESTRAL =  "Trimestral";
	
	public static final String FLSEMANAMES_SEMANA_UM = "1";
	public static final String FLSEMANAMES_SEMANA_DOIS = "2";
	public static final String FLSEMANAMES_SEMANA_TRES = "3";
	public static final String FLSEMANAMES_SEMANA_QUATRO = "4";
	public static final String FLSEMANAMES_SEMANA_CINCO = "5";

	public static final String COL_ORDER_DTAGENDA = "DTAGENDA";
	public static final String COL_ORDER_NUORDEMMANUAL = "NUORDEMMANUAL";
	public static final int LIMITE_PONTOS_GMAPS = 23;

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public int nuDiaSemana;
    public String flSemanaMes;
    public String flTipoFrequencia;
    public int nuSequencia;
    public Date dtBase;
    public String hrAgenda;
    public String dsObservacao;
    public Date dtAgendaOriginal;
    public Date dtFinal;
    public String cdTipoAgenda;
    public String cdRepOriginal;
    public String flAgendaReagendada;
    public String cdMotivoReagendamento;
    public Date dtAgendaReagendada;
    public String hrAgendaFim;
    public int nuSequenciaAgenda;
    public int nuOrdemManual;
    public Date dtCriacao;
    
    //-- Não Persistentes
    public Date dtAgendaAtual;
    public int statusAgendaCdPositivado;
    public boolean firstAgenda;
    public String nuDiaSemanaFilter;
    public Cliente cliente;
    private Visita visita;
    public String rowkeyDaProximaSequencia; 
    public static String sortAttr;
    public String cdEmpresaNegacao;
    public int nuDiaInicial;
    public int nuDiaFinal;
    public String flSemanaMesInicio;
    public String flSemanaMesFim;
    public Date dtAgenda;
    public String statusAgenda;
    public boolean atualizaAgenda;
    public boolean filterNovoPedido;
    public boolean filterAVisitar;
    public String dsFiltro;
    public boolean clienteCoordenadaFilter;
    public boolean contabVariosPedidoRota;
    public boolean orderByHrAgendaDif;
    public String hrAgendaConflito;
    public String hrAgendaFimConflito;
    
    public String cdMarcador;
    
    public String cdRede;
    public String cdCategoria;
    public String cdFornecedor;
    
    public AgendaVisita() {
    }

    public AgendaVisita(String cdEmpresa, String cdRepresentante) {
        this.cdEmpresa = cdEmpresa;
        this.cdRepresentante = cdRepresentante;
    }
    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof AgendaVisita) {
            AgendaVisita agendavisita = (AgendaVisita) obj;
            boolean retorno = ValueUtil.valueEquals(cdEmpresa, agendavisita.cdEmpresa) &&
	                ValueUtil.valueEquals(cdRepresentante, agendavisita.cdRepresentante) &&
	                ValueUtil.valueEquals(cdCliente, agendavisita.cdCliente) &&
	                ValueUtil.valueEquals(nuDiaSemana, agendavisita.nuDiaSemana) &&
	                ValueUtil.valueEquals(flSemanaMes, agendavisita.flSemanaMes) &&
	                ValueUtil.valueEquals(nuSequenciaAgenda, agendavisita.nuSequenciaAgenda);
            if (LavenderePdaConfig.usaNuSequenciaNaChaveDaAgendaVisita) {
            	retorno = retorno && ValueUtil.valueEquals(nuSequencia, agendavisita.nuSequencia);
            }
            if (LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita && ValueUtil.isNotEmpty(dtAgenda)) {
            	retorno = retorno && ValueUtil.valueEquals(dtAgenda, agendavisita.dtAgenda);
            }
            return retorno;
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(nuDiaSemana);
        primaryKey.append(";");
        primaryKey.append(flSemanaMes);
        primaryKey.append(";");
        if (LavenderePdaConfig.usaNuSequenciaNaChaveDaAgendaVisita) {
        	primaryKey.append(nuSequencia);
        	primaryKey.append(";");
        }
        primaryKey.append(nuSequenciaAgenda);
        return primaryKey.toString();
    }
    
    public String getDescricaoTipoFrequencia(String frequencia) {
    	String dsFrequencia = FLDSTIPOFREQUENCIA_SEM_FREQUENCIA;
    	if (FLTIPOFREQUENCIA_SEMANAL.equals(frequencia)) {
    		dsFrequencia = FLDSTIPOFREQUENCIA_SEMANAL;
    	} else if (FLTIPOFREQUENCIA_QUINZENAL.equals(frequencia)) {
    		dsFrequencia = FLDSTIPOFREQUENCIA_QUINZENAL;
    	} else if (FLTIPOFREQUENCIA_MENSAL.equals(frequencia)) {
    		dsFrequencia = FLDSTIPOFREQUENCIA_MENSAL;
    	} else if (FLTIPOFREQUENCIA_BIMESTRAL.equals(frequencia)) {
    		dsFrequencia = FLDSTIPOFREQUENCIA_BIMESTRAL;
    	} else if (FLTIPOFREQUENCIA_TRIMESTRAL.equals(frequencia)) {
    		dsFrequencia = FLDSTIPOFREQUENCIA_TRIMESTRAL;
    	} else if (FLTIPOFREQUENCIA_UNICA.equals(frequencia)) {
    		dsFrequencia = FLDSTIPOFREQUENCIA_UNICA;
    	}
    	return dsFrequencia;
    }

    //@Override
    public int getSortIntValue() {
    	if (ValueUtil.isNotEmpty(sortAttr)) {
    		return ValueUtil.getIntegerValue(nuSequencia);
    	}
    	if(LavenderePdaConfig.usaOrdenacaoPersonalizada() && nuOrdemManual!=0) {
    		return ValueUtil.getIntegerValue(nuOrdemManual);
    	}
    	return ValueUtil.getIntegerValue(cdCliente);
    }
    
    @Override
    public String getSortStringValue() {
    	if ("DTAGENDA".equals(sortAtributte)) {
    		return DateUtil.formatDateYYYYMMDD(dtAgenda);
    	} else if ("HRAGENDA".equals(sortAtributte)) {
    		return StringUtil.getStringValue(hrAgenda);
    	} 
    	return cdCliente;
    }
    
    public void setSortAtribute(String sortAtribute) {
    	this.sortAtributte = sortAtribute;
    }

	public Visita getVisita() {
		if (this.visita == null) {
			visita = new Visita();
		}
		return visita;
	}

	public void setVisita(Visita visita) {
		this.visita = visita;
	}
    
	public boolean isAgendaVisitaBaseadaNaDataBase() {
		return ValueUtil.isNotEmpty(dtBase);
	}
	
	public String getDescricaoHoraAgenda() {
		return ValueUtil.isEmpty(hrAgendaFim) ? StringUtil.getStringValue(hrAgenda) : StringUtil.getStringValue(hrAgenda) + " - " + StringUtil.getStringValue(hrAgendaFim);
	}
	
	public boolean isValidaFlSemanaMes(int usaAgendaVisitaBaseadaNaSemanaDoMes) {
		if (ValueUtil.valueEquals(usaAgendaVisitaBaseadaNaSemanaDoMes, 1)) {
			return true;
		} else if (ValueUtil.valueEquals(usaAgendaVisitaBaseadaNaSemanaDoMes, 2)) {
			return FLTIPOFREQUENCIA_SEM_FREQUENCIA.equals(flTipoFrequencia) || FLTIPOFREQUENCIA_UNICA.equals(flTipoFrequencia) || FLTIPOFREQUENCIA_BIMESTRAL.equals(flTipoFrequencia) || FLTIPOFREQUENCIA_TRIMESTRAL.equals(flTipoFrequencia);
		}
		return false;
	}
	
    public boolean isFrequenciaBaseadaDtBase() {
    	return AgendaVisita.FLTIPOFREQUENCIA_UNICA.equals(flTipoFrequencia) || AgendaVisita.FLDSTIPOFREQUENCIA_QUINZENAL.equals(flTipoFrequencia) 
		|| AgendaVisita.FLTIPOFREQUENCIA_MENSAL.equals(flTipoFrequencia) || AgendaVisita.FLTIPOFREQUENCIA_BIMESTRAL.equals(flTipoFrequencia) 
		|| AgendaVisita.FLTIPOFREQUENCIA_TRIMESTRAL.equals(flTipoFrequencia);
    }
	
}
