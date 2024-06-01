package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import totalcross.util.Date;

public class VisitaSupervisor extends BaseDomain {

    public static String TABLE_NAME = "TBLVPVISITASUPERVISOR";

    public static final String TIPOORIGEM_PALM = "P";

    public String cdEmpresa;
    public String cdVisita;
    public String cdSupervisor;
    public String flOrigemVisita;
    public String cdRepresentante;
    public String cdCliente;
    public Date dtVisita;
    public String hrVisita;
    public String dsObservacao;
    public String qtTempoAtendimento;
    public String flRepresentantePresente;


    //Override
    public boolean equals(Object obj) {
        if (obj instanceof VisitaSupervisor) {
            VisitaSupervisor visita = (VisitaSupervisor) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, visita.cdEmpresa) &&
                ValueUtil.valueEquals(cdVisita, visita.cdVisita) &&
                ValueUtil.valueEquals(cdSupervisor, visita.cdSupervisor) &&
                ValueUtil.valueEquals(flOrigemVisita, visita.flOrigemVisita);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdVisita);
    	strBuffer.append(";");
    	strBuffer.append(cdSupervisor);
    	strBuffer.append(";");
    	strBuffer.append(flOrigemVisita);
        return strBuffer.toString();
    }

    public String getNmRazaoSocial() throws SQLException {
    	return ClienteService.getInstance().getDescriptionWithKey(cdEmpresa, cdRepresentante, cdCliente);
    }

}