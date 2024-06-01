package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class TabelaPrecoSeg extends BaseDomain {

	public static String TABLE_NAME = "TBLVPTABELAPRECOSEG";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdSegmento;
    public String cdTabelaPreco;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof TabelaPrecoSeg) {
            TabelaPrecoSeg tabelaPrecoSeg = (TabelaPrecoSeg) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, tabelaPrecoSeg.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, tabelaPrecoSeg.cdRepresentante) &&
                ValueUtil.valueEquals(cdSegmento, tabelaPrecoSeg.cdSegmento) &&
                ValueUtil.valueEquals(cdTabelaPreco, tabelaPrecoSeg.cdTabelaPreco);
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
        primaryKey.append(cdSegmento);
        primaryKey.append(";");
        primaryKey.append(cdTabelaPreco);
        return primaryKey.toString();
    }

}