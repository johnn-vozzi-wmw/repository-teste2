package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class CondPagtoSeg extends BaseDomain {

	public static String TABLE_NAME = "TBLVPCONDPAGTOSEG";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdSegmento;
    public String cdCondicaoPagamento;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof CondPagtoSeg) {
            CondPagtoSeg condPagtoSeg = (CondPagtoSeg) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, condPagtoSeg.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, condPagtoSeg.cdRepresentante) &&
                ValueUtil.valueEquals(cdSegmento, condPagtoSeg.cdSegmento) &&
                ValueUtil.valueEquals(cdCondicaoPagamento, condPagtoSeg.cdCondicaoPagamento);
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
        primaryKey.append(cdCondicaoPagamento);
        return primaryKey.toString();
    }

}