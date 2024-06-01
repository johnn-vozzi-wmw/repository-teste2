package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.AgendaVisita;
import totalcross.util.Date;

public class AgendaVisitaDTO {
	
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
    public ClienteDTO cliente;
    
    public AgendaVisitaDTO(AgendaVisita agendaVisita) {
    	try {
    		FieldMapper.copy(agendaVisita, this);
    		if (agendaVisita.cliente != null) {
    			cliente = new ClienteDTO().copy(agendaVisita.cliente);
    		}
    	} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
    }
	
    public String getCdEmpresa() {
		return cdEmpresa;
	}
	
	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}
	
	public String getCdRepresentante() {
		return cdRepresentante;
	}
	
	public void setCdRepresentante(String cdRepresentante) {
		this.cdRepresentante = cdRepresentante;
	}
	
	public String getCdCliente() {
		return cdCliente;
	}
	
	public void setCdCliente(String cdCliente) {
		this.cdCliente = cdCliente;
	}
	
	public int getNuDiaSemana() {
		return nuDiaSemana;
	}
	
	public void setNuDiaSemana(int nuDiaSemana) {
		this.nuDiaSemana = nuDiaSemana;
	}
	
	public String getFlSemanaMes() {
		return flSemanaMes;
	}
	
	public void setFlSemanaMes(String flSemanaMes) {
		this.flSemanaMes = flSemanaMes;
	}
	
	public String getFlTipoFrequencia() {
		return flTipoFrequencia;
	}
	
	public void setFlTipoFrequencia(String flTipoFrequencia) {
		this.flTipoFrequencia = flTipoFrequencia;
	}
	
	public int getNuSequencia() {
		return nuSequencia;
	}
	
	public void setNuSequencia(int nuSequencia) {
		this.nuSequencia = nuSequencia;
	}
	
	public String getDtBase() {
		return dtBase == null ? null : DateUtil.formatDateDDMMYYYY(dtBase);
	}
	
	public void setDtBase(Date dtBase) {
		this.dtBase = dtBase;
	}
	
	public String getHrAgenda() {
		return hrAgenda;
	}
	
	public void setHrAgenda(String hrAgenda) {
		this.hrAgenda = hrAgenda;
	}
	
	public String getDsObservacao() {
		return dsObservacao;
	}
	
	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}
	
	public String getDtAgendaOriginal() {
		return dtAgendaOriginal == null ? null : DateUtil.formatDateDDMMYYYY(dtAgendaOriginal);
	}
	
	public void setDtAgendaOriginal(Date dtAgendaOriginal) {
		this.dtAgendaOriginal = dtAgendaOriginal;
	}
	
	public String getDtFinal() {
		return dtFinal == null ? null : DateUtil.formatDateDDMMYYYY(dtFinal);
	}
	
	public void setDtFinal(Date dtFinal) {
		this.dtFinal = dtFinal;
	}
	
	public String getCdTipoAgenda() {
		return cdTipoAgenda;
	}
	
	public void setCdTipoAgenda(String cdTipoAgenda) {
		this.cdTipoAgenda = cdTipoAgenda;
	}
	
	public String getCdRepOriginal() {
		return cdRepOriginal;
	}
	
	public void setCdRepOriginal(String cdRepOriginal) {
		this.cdRepOriginal = cdRepOriginal;
	}
	
	public String getFlAgendaReagendada() {
		return flAgendaReagendada;
	}
	
	public void setFlAgendaReagendada(String flAgendaReagendada) {
		this.flAgendaReagendada = flAgendaReagendada;
	}
	
	public String getCdMotivoReagendamento() {
		return cdMotivoReagendamento;
	}
	
	public void setCdMotivoReagendamento(String cdMotivoReagendamento) {
		this.cdMotivoReagendamento = cdMotivoReagendamento;
	}
	
	public String getDtAgendaReagendada() {
		return dtAgendaReagendada == null ? null : DateUtil.formatDateDDMMYYYY(dtAgendaReagendada);
	}
	
	public void setDtAgendaReagendada(Date dtAgendaReagendada) {
		this.dtAgendaReagendada = dtAgendaReagendada;
	}
	
	public String getHrAgendaFim() {
		return hrAgendaFim;
	}
	
	public void setHrAgendaFim(String hrAgendaFim) {
		this.hrAgendaFim = hrAgendaFim;
	}
	
	public int getNuSequenciaAgenda() {
		return nuSequenciaAgenda;
	}
	
	public void setNuSequenciaAgenda(int nuSequenciaAgenda) {
		this.nuSequenciaAgenda = nuSequenciaAgenda;
	}
	
	public int getNuOrdemManual() {
		return nuOrdemManual;
	}
	
	public void setNuOrdemManual(int nuOrdemManual) {
		this.nuOrdemManual = nuOrdemManual;
	}

	public ClienteDTO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteDTO cliente) {
		this.cliente = cliente;
	}
	
	public String getFlAtivo() {
		return ValueUtil.VALOR_SIM;
	}

	public String getFlTipoAlteracao() {
		return BaseDomain.FLTIPOALTERACAO_ALTERADO;
	}
	
}
