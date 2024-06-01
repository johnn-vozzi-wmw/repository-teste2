package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class ReagendaAgendaVisita extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPREAGENDAAGENDAVISITA";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public Integer nuDiaSemana;
    public String flSemanaMes;
    public Date dtAgendaOriginal;
    public String hrAgendaOriginal;
    public Date dtReagendamento;
    public String cdMotivoReagendamento;
    public String dsObservacao;
	public Date dtAlteracao;
	public String hrAlteracao;
	
	//NÂO PERSISTENTE
	public int nuSequencia;
    
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
        primaryKey.append(dtAgendaOriginal);        
        return primaryKey.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ReagendaAgendaVisita) {
        	ReagendaAgendaVisita reagendamento = (ReagendaAgendaVisita) obj;
            return ValueUtil.valueEquals(cdEmpresa, reagendamento.cdEmpresa) && 
            		ValueUtil.valueEquals(cdRepresentante, reagendamento.cdRepresentante) && 
            		ValueUtil.valueEquals(cdCliente, reagendamento.cdCliente) && 
            		ValueUtil.valueEquals(nuDiaSemana, reagendamento.nuDiaSemana) && 
            		ValueUtil.valueEquals(flSemanaMes, reagendamento.flSemanaMes) &&
            		ValueUtil.valueEquals(dtAgendaOriginal, reagendamento.dtAgendaOriginal);
        }
        return false;
    }
    
  
}