package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.util.TimeUtil;
import totalcross.util.Date;

public class AgendaVisitaCliHist extends BaseDomain {

	public static String TABLE_NAME = "TBLVPAGENDAVISITACLIHIST";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public Date dtAgenda;
    public int qtTotalAgenda;
    public Date dtAlteracao;
    public String hrAlteracao;
    
    //Não persistentes
    public Date dtAgendaFilter;
    
    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(dtAgenda);
        primaryKey.append(";");
        return primaryKey.toString();
    }
    
    public AgendaVisitaCliHist() {
    	super();
    }
    	
    
    public AgendaVisitaCliHist(AgendaVisita agendaVisita) {
    	this.cdEmpresa = agendaVisita.cdEmpresa;
    	this.cdRepresentante = agendaVisita.cdRepresentante;
    	this.cdCliente = agendaVisita.cdCliente;
    	this.dtAgenda = agendaVisita.dtAgenda;
    	this.flTipoAlteracao = FLTIPOALTERACAO_INSERIDO;
		this.dtAlteracao = new Date();
		this.hrAlteracao = TimeUtil.getCurrentTimeHHMMSS();
		this.cdUsuario = Session.getCdUsuario();
    }
    	
}
