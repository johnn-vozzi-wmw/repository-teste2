package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class VerbaItemPedido extends BaseDomain {

    public static String TABLE_NAME = "TBLVPVERBAITEMPEDIDO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdVerba;
    public String flOrigemPedido;
    public String nuPedido;
    public String cdProduto;
    public String flTipoItemPedido;
    public int nuSeqProduto;
    public double vlGerado;
    public double vlConsumido;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof VerbaItemPedido) {
            VerbaItemPedido verbaItemPedido = (VerbaItemPedido) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, verbaItemPedido.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, verbaItemPedido.cdRepresentante) &&
                ValueUtil.valueEquals(cdVerba, verbaItemPedido.cdVerba) &&
                ValueUtil.valueEquals(flOrigemPedido, verbaItemPedido.flOrigemPedido) &&
                ValueUtil.valueEquals(nuPedido, verbaItemPedido.nuPedido) &&
                ValueUtil.valueEquals(cdProduto, verbaItemPedido.cdProduto) &&
                ValueUtil.valueEquals(flTipoItemPedido, verbaItemPedido.flTipoItemPedido) &&
                (nuSeqProduto == verbaItemPedido.nuSeqProduto);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdVerba);
    	strBuffer.append(";");
    	strBuffer.append(flOrigemPedido);
    	strBuffer.append(";");
    	strBuffer.append(nuPedido);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
    	strBuffer.append(";");
    	strBuffer.append(flTipoItemPedido);
    	strBuffer.append(";");
    	strBuffer.append(nuSeqProduto);
        return strBuffer.toString();
    }

}