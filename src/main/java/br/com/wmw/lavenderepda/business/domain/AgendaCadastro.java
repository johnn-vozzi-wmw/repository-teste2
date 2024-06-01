package br.com.wmw.lavenderepda.business.domain;

public class AgendaCadastro extends AgendaVisita {
	
	public static String TABLE_NAME = "TBLVPAGENDACADASTRO";
	
	public AgendaCadastro() {
		super();
	}
	
	public AgendaCadastro(AgendaVisita agendaVisita) {
		cdEmpresa = agendaVisita.cdEmpresa;
		cdRepresentante = agendaVisita.cdRepresentante;
		cdCliente = agendaVisita.cdCliente;
		nuDiaSemana = agendaVisita.nuDiaSemana;
		flSemanaMes = agendaVisita.flSemanaMes;
		flTipoFrequencia = agendaVisita.flTipoFrequencia;
		nuSequencia = agendaVisita.nuSequencia;
		dtBase = agendaVisita.dtBase;
		hrAgenda = agendaVisita.hrAgenda;
		dsObservacao = agendaVisita.dsObservacao;
		dtAgendaOriginal = agendaVisita.dtAgendaOriginal;
		dtFinal = agendaVisita.dtFinal;
		cdTipoAgenda = agendaVisita.cdTipoAgenda;
		cdRepOriginal = agendaVisita.cdRepOriginal;
		flAgendaReagendada = agendaVisita.flAgendaReagendada;
		cdMotivoReagendamento = agendaVisita.cdMotivoReagendamento;
		dtAgendaReagendada = agendaVisita.dtAgendaReagendada;
		hrAgendaFim = agendaVisita.hrAgendaFim;
		nuSequenciaAgenda = agendaVisita.nuSequenciaAgenda;
	}
	
}