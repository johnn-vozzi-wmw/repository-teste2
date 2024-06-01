package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class Faceamento extends BaseDomain {

    public static String TABLE_NAME = "TBLVPFACEAMENTO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdProduto;
    public double qtPontoEquilibrio;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Faceamento) {
            Faceamento faceamento = (Faceamento) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, faceamento.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, faceamento.cdRepresentante) &&
                ValueUtil.valueEquals(cdCliente, faceamento.cdCliente) &&
                ValueUtil.valueEquals(cdProduto, faceamento.cdProduto);
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
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        return primaryKey.toString();
    }

}