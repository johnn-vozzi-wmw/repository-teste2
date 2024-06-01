package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import totalcross.util.Date;

public class AtendimentoAtiv extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPATENDIMENTOATIV";
	
	public static final String CDTIPOATENDIMENTO_PROGRAMADO = "P";
	public static final String CDTIPOATENDIMENTO_TEMPORARIO = "T";
	public static final String CDTIPOATENDIMENTO_SAC = "S";
	public static final String CDTIPOATENDIMENTO_PESQUISA = "Q";
	
	public static final String CDSTATUSATENDIMENTO_NAO_INICIADO = "1";
	public static final String CDSTATUSATENDIMENTO_EM_ANDAMENTO = "2";
	public static final String CDSTATUSATENDIMENTO_POSITIVADO = "3";
	public static final String CDSTATUSATENDIMENTO_NAO_POSITIVADO = "4";
	public static final String CDSTATUSATENDIMENTO_CONCLUIDO = "5";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdAtendimentoAtividade;
    public String cdUsuarioAtendimento;
    public String cdUsuarioOriginal;
    public String cdAtendimentoGeracao;
    public String cdSac;
    public String cdTipoSac;
    public String cdAtividadeSac;
    public String cdPesquisa;
    public String dsObservacao;
    public Date dtAtendimento;
    public String flTipoGeracao;
    public String flTipoAtendimento;
    public String cdStatusAtendimento;
    public String hrAtendimento;
    public String flOrigem;
    public Date dtAlteracao;
    public String hrAlteracao;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof AtendimentoAtiv) {
            AtendimentoAtiv atendimentoAtiv = (AtendimentoAtiv) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, atendimentoAtiv.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, atendimentoAtiv.cdRepresentante) && 
                ValueUtil.valueEquals(cdCliente, atendimentoAtiv.cdCliente) && 
                ValueUtil.valueEquals(cdAtendimentoAtividade, atendimentoAtiv.cdAtendimentoAtividade);
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
        primaryKey.append(cdAtendimentoAtividade);
        return primaryKey.toString();
    }

    public String getDsTipoAtendimento(String tipoAtendimento) {
    	if (CDTIPOATENDIMENTO_PROGRAMADO.equals(tipoAtendimento)) {
    		return Messages.ATENDIMENTO_PROGRAMADO;
    	} else if (CDTIPOATENDIMENTO_TEMPORARIO.equals(tipoAtendimento)) {
    		return Messages.ATENDIMENTO_TEMPORARIO;
    	} else if (CDTIPOATENDIMENTO_SAC.equals(tipoAtendimento)) {
    		return Messages.ATENDIMENTO_SAC;
    	} else if (CDTIPOATENDIMENTO_PESQUISA.equals(tipoAtendimento)) {
    		return Messages.ATENDIMENTO_PESQUISA;
    	}
    	return "";
	}
    
    public String getDsStatusAtendimento(String statusAtendimento) {
    	if (CDSTATUSATENDIMENTO_NAO_INICIADO.equals(statusAtendimento)) {
    		return Messages.ATENDIMENTO_NAOINICIADO;
    	} else if (CDSTATUSATENDIMENTO_EM_ANDAMENTO.equals(statusAtendimento)) {
    		return Messages.ATENDIMENTO_EMANDAMENTO;
    	} else if (CDSTATUSATENDIMENTO_POSITIVADO.equals(statusAtendimento)) {
    		return Messages.ATENDIMENTO_POSITIVADO;
    	} else if (CDSTATUSATENDIMENTO_NAO_POSITIVADO.equals(statusAtendimento)) {
    		return Messages.ATENDIMENTO_NAOPOSITIVADO;
    	} else if (CDSTATUSATENDIMENTO_CONCLUIDO.equals(statusAtendimento)) {
    		return Messages.ATENDIMENTO_CONCLUIDO;
    	}
    	return "";
    }
}