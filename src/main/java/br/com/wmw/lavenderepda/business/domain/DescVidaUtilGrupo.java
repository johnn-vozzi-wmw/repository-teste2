package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class DescVidaUtilGrupo extends BaseDomain {

    public static String TABLE_NAME = "TBLVPDESCVIDAUTILGRUPO";

	public static final String DESCVIDAUTILNAOENCONTRADO = " --- ";

    public String cdGrupoproduto1;
    public double vlPctvidautilminimo;
    public double vlPctvidautilmaximo;
    public double vlPctDesconto;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof DescVidaUtilGrupo) {
            DescVidaUtilGrupo descvidautilgrupo = (DescVidaUtilGrupo) obj;
            return
                ValueUtil.valueEquals(cdGrupoproduto1, descvidautilgrupo.cdGrupoproduto1) &&
                ValueUtil.valueEquals(vlPctvidautilminimo, descvidautilgrupo.vlPctvidautilminimo);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdGrupoproduto1);
    	strBuffer.append(";");
    	strBuffer.append(vlPctvidautilminimo);
        return strBuffer.toString();
    }

}