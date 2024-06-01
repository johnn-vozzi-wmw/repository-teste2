package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class ParcelaPedido extends BaseDomain {

    public static String TABLE_NAME = "TBLVPPARCELAPEDIDO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemPedido;
    public String nuPedido;
    public String cdParcela;
    public double vlParcela;
    public Date dtVencimento;
    public int qtDiasPrazo;
    public double vlPctParcela;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ParcelaPedido) {
            ParcelaPedido parcelaPedido = (ParcelaPedido) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, parcelaPedido.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, parcelaPedido.cdRepresentante) &&
                ValueUtil.valueEquals(flOrigemPedido, parcelaPedido.flOrigemPedido) &&
                ValueUtil.valueEquals(nuPedido, parcelaPedido.nuPedido) &&
                ValueUtil.valueEquals(cdParcela, parcelaPedido.cdParcela);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(flOrigemPedido);
    	strBuffer.append(";");
    	strBuffer.append(nuPedido);
    	strBuffer.append(";");
    	strBuffer.append(cdParcela);
        return strBuffer.toString();
    }

    //@Override
    public String toString() {
    	if (ValueUtil.isNotEmpty(dtVencimento)) {
    		return StringUtil.getStringValue(dtVencimento.getDateInt());
    	}
    	return ValueUtil.VALOR_NI;
    }

}