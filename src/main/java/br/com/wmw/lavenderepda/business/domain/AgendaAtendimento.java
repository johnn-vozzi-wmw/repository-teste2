package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class AgendaAtendimento extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPAGENDAATENDIMENTO";

    public String cdEmpresa;
    public String cdAgendaAtendimento;
    public String cdCliente;
    public String cdRepresentante;
    public String cdLote;
    public String cdCampanhaGeral;
    public String nmContato;
    public String nuFone;
    public Date dtAgendaAtendimento;
    public String dsAgendaAtendimento;
    public Date dtVencimento;
    public String hrAgendaAtendimento;
    public String dsObservacao;
    public String cdAgendaAnterior;
    public String flReagendamento;
    public String cdUsuarioAgenda;
    public String cdMotivoTransferencia;
    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof AgendaAtendimento) {
            AgendaAtendimento agendaAtendimento = (AgendaAtendimento) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, agendaAtendimento.cdEmpresa) && 
                ValueUtil.valueEquals(cdAgendaAtendimento, agendaAtendimento.cdAgendaAtendimento);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdAgendaAtendimento);
        return primaryKey.toString();
    }

}