package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import totalcross.util.Date;
import totalcross.util.Vector;

public class Visita extends BaseDomain {

    public static String TABLE_NAME = "TBLVPVISITA";

    public static final String FL_VISITA_TRANSFERIDA = "T";
    public static final String FL_VISITA_REAGENDADA = "R";
	public static final String FL_VISITA_POSITIVADA = "S";
	public static final String FL_VISITA_NAOPOSITIVADA = "N";
	
	public static final String FLLOCALIZACAO_PRESENTE = "P";
	public static final String FLLOCALIZACAO_NAOPRESENTE = "N";
	public static final String FLLOCALIZACAO_INDEFINIDO = "";
	

	public static final int CD_VISITA_POSITIVADA = 1;
	public static final int CD_VISITA_NAO_POSITIVADA = 2;
	public static final int CD_VISITA_TODOS = 3;

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemVisita;
    public String cdVisita;
    public String cdCliente;
    public String flVisitaPositivada;
    public String cdMotivoRegistroVisita;
    public Date dtVisita;
    public String hrVisita;
    public String dsObservacao;
    public Date dtChegadaVisita;
    public String hrChegadaVisita;
    public String dsObservacaoChegada;
    public Date dtSaidaVisita;
    public String hrSaidaVisita;
    public String dsObservacaoSaida;
    public String nuPedido;
    public int nuSequencia;
    public String flEnviadoServidor;
    public String flVisitaReagendada;
    public String flVisitaTransferida;
    public String cdMotivoReagendamento;
    public String flPedidoSemVisita;
    public String flPedidoOutroCliente;
    public Date dtServidor;
    public String hrServidor;
    public Date dtAgendaVisita;
    public String cdTipoAgenda;
    public String cdRepOriginal;
    public String flLiberadoSenha;
    public String flVisitaManual;
    public String flVisitaAgendada;
    public String flNovoClienteProspect;
    public String flLocalizacaoChegada;
	public String flLocalizacaoSaida;
	public String cdChaveAgendaOrigem;
	
    // Não Persistentes
    public String dsFrequenciaClienteVisita;
    public int nuLinhasEnvioServidor;
    private Vector visitaFotoList;
    private Vector visitaFotoExcluidasList;
    public boolean filterHrChegadaVisitaNotNull;
    public boolean filterHrSaidaVisitaNull;
    public boolean filterNuPedidoNull;
    public boolean filterFlTipoAlteracao;
    public boolean ignoraValidacaoObservacao;
    
    public String cdEmpresaNegacao;
    
    
    public Date filterDtVisita;
    public String nuSeqFilter;

	public Visita() {
		super();
		visitaFotoList = new Vector();
		visitaFotoExcluidasList = new Vector();
	}


	//Override
    public boolean equals(Object obj) {
        if (obj instanceof Visita) {
            Visita visita = (Visita) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, visita.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, visita.cdRepresentante) &&
                ValueUtil.valueEquals(flOrigemVisita, visita.flOrigemVisita) &&
                ValueUtil.valueEquals(cdVisita, visita.cdVisita);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(flOrigemVisita);
    	strBuffer.append(";");
    	strBuffer.append(cdVisita);
        return strBuffer.toString();
    }

    public Vector getVisitaFotoList() {
		return visitaFotoList;
	}

	public void setVisitaFotoList(Vector visitaFotoList) {
		this.visitaFotoList = visitaFotoList;
	}

    public Vector getVisitaFotoExcluidasList() {
		return visitaFotoExcluidasList;
	}

	public void setVisitaFotoExcluidasList(Vector visitaFotoExcluidasList) {
		this.visitaFotoExcluidasList = visitaFotoExcluidasList;
	}

	public boolean isVisitaEnviadaServidor() {
    	return ValueUtil.VALOR_SIM.equals(flEnviadoServidor);
	}
	
	public boolean isVisitaEmAndamento() {
		return LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita && ValueUtil.isNotEmpty(hrChegadaVisita) && ValueUtil.isEmpty(hrSaidaVisita) && ValueUtil.isEmpty(nuPedido) && !isPedidoSemVisita() && !isPedidoOutroCliente();
	}
	
	public boolean isVisitaPositivada() {
		return Visita.FL_VISITA_POSITIVADA.equals(flVisitaPositivada);
	}
	
	public boolean isPedidoSemVisita() {
		return ValueUtil.VALOR_SIM.equalsIgnoreCase(flPedidoSemVisita);
	}
	
	public boolean isVisitaRealizada() {
		return LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita && ValueUtil.isNotEmpty(hrChegadaVisita) && ValueUtil.isNotEmpty(hrSaidaVisita);
	}
	
	public boolean isPedidoOutroCliente() {
		return ValueUtil.VALOR_SIM.equalsIgnoreCase(flPedidoOutroCliente);
	}
	
	public boolean isVisitaAgendada() {
		return ValueUtil.VALOR_SIM.equalsIgnoreCase(flVisitaAgendada);
	}
	
	public boolean isVisitaReagendada() {
		return ValueUtil.VALOR_SIM.equalsIgnoreCase(flVisitaReagendada);
	}

	public boolean isAgendaVisita() {
		return ValueUtil.isNotEmpty(dtAgendaVisita);
	}
	
}