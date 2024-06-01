package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class IndicadorWmw extends Indicador {

	public static final String TABLE_NAME = "TBLVPINDICADORWMW";
	public static final String CD_TIPO_APURACAO_2 = "2";

    public String cdTipoApuracao;
    public String dsMascaraFormato;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IndicadorWmw) {
            IndicadorWmw indicadorWmw = (IndicadorWmw) obj;
            return
                ValueUtil.valueEquals(cdIndicador, indicadorWmw.cdIndicador);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdIndicador);
        return primaryKey.toString();
    }

}