package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;

public class DescontoIcms extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPDESCONTOICMS";

    public String cdEmpresa;
    public String cdRepresentante;
    public double vlPctIcms;
    public double vlPctDesconto;
    public double vlPctAcrescimo;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DescontoIcms) {
            DescontoIcms descontoIcms = (DescontoIcms) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, descontoIcms.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, descontoIcms.cdRepresentante) && 
                ValueUtil.valueEquals(vlPctIcms, descontoIcms.vlPctIcms);
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
        primaryKey.append(getPadVlPctIcms());
        return primaryKey.toString();
    }
    
    private String getPadVlPctIcms() {
    	String pctIcmsStr = String.valueOf(vlPctIcms);
		int casas = pctIcmsStr.indexOf(".") + 1;
    	casas += ValueUtil.DOUBLE_PRECISION_ARMAZENAMENTO;
    	return StringUtil.fillZero(pctIcmsStr, casas, true, true);
    }

}