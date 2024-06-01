package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class Segmento extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPSEGMENTO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdSegmento;
    public String dsSegmento;

    //Não persistente
    public String flDefault;
    public ClienteSeg clienteSegFilter;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Segmento) {
            Segmento segmento = (Segmento) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, segmento.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, segmento.cdRepresentante) &&
                ValueUtil.valueEquals(cdSegmento, segmento.cdSegmento);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdSegmento);
        return primaryKey.toString();
    }

    public boolean isFlDefault() {
    	return ValueUtil.VALOR_SIM.equals(flDefault);
    }

    @Override
	public String getCdDomain() {
		return cdSegmento;
	}

    @Override
	public String getDsDomain() {
		return dsSegmento;
	}

}