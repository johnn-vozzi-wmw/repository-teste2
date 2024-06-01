package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class Feriado extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPFERIADO";

	public static final String FERIADO_CONTABILIZA_SABADO_DOMINGO = "1";
	public static final String FERIADO_CONTABILIZA_DOMINGO = "2";
	public static final String FERIADO_CONTABILIZA_SABADO = "3";


    public int nuDia;
    public int nuMes;
    public int nuAno;
    public String dsFeriado;
    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Feriado) {
            Feriado feriado = (Feriado) obj;
            return
                ValueUtil.valueEquals(nuDia, feriado.nuDia) &&
                ValueUtil.valueEquals(nuMes, feriado.nuMes) &&
                ValueUtil.valueEquals(nuAno, feriado.nuAno);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(nuDia);
        primaryKey.append(";");
        primaryKey.append(nuMes);
        primaryKey.append(";");
        primaryKey.append(nuAno);
        return primaryKey.toString();
    }

}