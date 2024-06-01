package br.com.wmw.lavenderepda.business.domain;

import java.sql.Date;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;

public class ValorIndicador extends BaseDomain {

    public static final String TABLE_NAME = "TBLVPVALORINDICADOR";

	public static final String PERIODO_TICKET_MEDIO = Messages.VALORINDICADOR_PERIODO_TICKET_MEDIO;

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdIndicador;
    public String dsPeriodo;
    public String dsVlIndicador;
    public Date dtReferencia;
    public String dsMascaraFormato;

    // não persistente
    public String dsIndicador;
    
    public ValorIndicador() { }
    
    public ValorIndicador(String cdEmpresa, String cdRepresentante, String cdIndicador, String dsPeriodo) {
        this.cdEmpresa = cdEmpresa;
        this.cdRepresentante = cdRepresentante;
        this.cdIndicador = cdIndicador;
        this.dsPeriodo = dsPeriodo;
	}
    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ValorIndicador) {
            ValorIndicador valorIndicador = (ValorIndicador) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, valorIndicador.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, valorIndicador.cdRepresentante) &&
                ValueUtil.valueEquals(cdIndicador, valorIndicador.cdIndicador) &&
                ValueUtil.valueEquals(dsPeriodo, valorIndicador.dsPeriodo);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdIndicador);
    	strBuffer.append(";");
    	strBuffer.append(dsPeriodo);
        return strBuffer.toString();
    }
}