package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.BigDecimal;

public class FaixaBoleto extends BaseDomain {

	public static String NM_COLUNA_NUBOLETOINICIO = "nuBoletoInicio";
	
	public static final String TABLE_NAME = "TBLVPFAIXABOLETO";
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdBoletoConfig;
    public BigDecimal nuBoletoInicio;
    public BigDecimal nuBoletoFim;
    public BigDecimal nuUltimoBoleto;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof FaixaBoleto) {
            FaixaBoleto faixaBoleto = (FaixaBoleto) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, faixaBoleto.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, faixaBoleto.cdRepresentante) && 
                ValueUtil.valueEquals(nuBoletoInicio, faixaBoleto.nuBoletoInicio) && 
                ValueUtil.valueEquals(nuBoletoFim, faixaBoleto.nuBoletoFim);
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
        primaryKey.append(nuBoletoInicio);
        primaryKey.append(";");
        primaryKey.append(nuBoletoFim);
        return primaryKey.toString();
    }

}