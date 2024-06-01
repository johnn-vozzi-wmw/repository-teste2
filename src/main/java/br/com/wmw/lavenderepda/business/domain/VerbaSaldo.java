package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import totalcross.util.Date;

public class VerbaSaldo extends BaseDomain {

    public static final String TABLE_NAME = "TBLVPVERBASALDO";
    public static final String CDCONTACORRENTE_PADRAO = "0";
    public static final String NMCOLUNA_VLMINVERBA = "VLMINVERBA";
    public static final String FLORIGEM_ABERTO = "A";

    private String cdEmpresa;
    public String cdRepresentante;
    public String cdContaCorrente;
    public String flOrigemSaldo;
    public double vlSaldo;
    public double vlSaldoInicial;
    public Date dtSaldo;
    public Date dtVigenciaInicial;
    public Date dtVigenciaFinal;
    public double vlMinVerba;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof VerbaSaldo) {
            VerbaSaldo verbaSaldo = (VerbaSaldo) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, verbaSaldo.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, verbaSaldo.cdRepresentante) &&
                ValueUtil.valueEquals(cdContaCorrente, verbaSaldo.cdContaCorrente) &&
                ValueUtil.valueEquals(flOrigemSaldo, verbaSaldo.flOrigemSaldo);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(getCdEmpresa());
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdContaCorrente);
    	strBuffer.append(";");
    	strBuffer.append(flOrigemSaldo);
        return strBuffer.toString();
    }
    
    public boolean isVigente() {
    	Date dataAtual = DateUtil.getCurrentDate();
    	return ValueUtil.isNotEmpty(dtVigenciaInicial) && ValueUtil.isNotEmpty(dtVigenciaFinal) && !dataAtual.isBefore(dtVigenciaInicial) && !dataAtual.isAfter(dtVigenciaFinal);
    }
    
    public String getCdEmpresa() {
    	return LavenderePdaConfig.usaVerbaUnificada ? Empresa.EMPRESA_ZERO : cdEmpresa; 
	}

	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}
    
}