package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class Indicador extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPINDICADOR";

	public static final int TICKET_MEDIO_EMPRESA = 1;
	public static final int TICKET_MEDIO_MENSAL = 2;
	public static final int TICKET_MEDIO_DIARIO = 3;

    public String cdIndicador;
    public String dsIndicador;
    public String dsVlPadraoIndicador;
    public int nuSequencia;
    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Indicador) {
            Indicador indicador = (Indicador) obj;
            return
                ValueUtil.valueEquals(cdIndicador, indicador.cdIndicador);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
    	return cdIndicador;
    }

	public String getCdDomain() {
		return cdIndicador;
	}

	public String getDsDomain() {
		return dsIndicador;
	}
}