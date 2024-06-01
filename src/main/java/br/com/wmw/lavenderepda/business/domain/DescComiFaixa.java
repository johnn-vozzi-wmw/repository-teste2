package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class DescComiFaixa extends BaseDomain {

    public static String TABLE_NAME = "TBLVPDESCCOMIFAIXA";

	public static final String SIGLE_EXCEPTION = "DCG";
	public static final int GRUPOPRODUTO_NIVEL_1 = 1;
	public static final int GRUPOPRODUTO_NIVEL_2 = 2;
	public static final int GRUPOPRODUTO_NIVEL_3 = 3;

    public String cdEmpresa;
    public String cdDescComi;
    public double qtItem;
    public double vlPctDesconto;
    public double vlPctComissao;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof DescComiFaixa) {
            DescComiFaixa desccomiprod = (DescComiFaixa) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, desccomiprod.cdEmpresa) &&
                ValueUtil.valueEquals(cdDescComi, desccomiprod.cdDescComi) &&
                ValueUtil.valueEquals(qtItem, desccomiprod.qtItem) &&
                ValueUtil.valueEquals(vlPctDesconto, desccomiprod.vlPctDesconto);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdDescComi);
    	strBuffer.append(";");
    	strBuffer.append(qtItem);
    	strBuffer.append(";");
    	strBuffer.append(vlPctDesconto);
        return strBuffer.toString();
    }

}