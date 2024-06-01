package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Vector;

public class DescQuantidadePeso extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPDESCQUANTIDADEPESO";

    public String cdEmpresa;
    public String cdRepresentante;
	public double vlPeso;
    public double vlPctDesconto;
	public Vector produtoBloqueadoList;
	public String cdTabelaPreco;
	public String nmTabelaPreco;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof DescQuantidadePeso) {
            DescQuantidadePeso descQuantidadePeso = (DescQuantidadePeso) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, descQuantidadePeso.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, descQuantidadePeso.cdRepresentante) && 
                ValueUtil.valueEquals(cdTabelaPreco, descQuantidadePeso.cdTabelaPreco) && 
                ValueUtil.valueEquals(vlPeso, descQuantidadePeso.vlPeso);
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
        primaryKey.append(vlPeso);
        primaryKey.append(";");
        primaryKey.append(cdTabelaPreco);
        return primaryKey.toString();
    }

}