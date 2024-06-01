package br.com.wmw.lavenderepda.business.domain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class PeriodoEntrega extends LavendereBasePersonDomain {

	public static final String TABLE_NAME = "TBLVPPERIODOENTREGA";
	
	public String cdPeriodoEntrega;
    public String dsPeriodoEntrega;
    public String hrPeriodoEntregaInicial;
    public String hrPeriodoEntregaFinal;
    public String cdEmpresa;
    public Date dtAlteracao;
    public String hrAlteracao;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof PeriodoEntrega) {
            PeriodoEntrega periodoEntrega = (PeriodoEntrega) obj;
            return
                ValueUtil.valueEquals(cdPeriodoEntrega, periodoEntrega.cdPeriodoEntrega);
        }
        return false;
    }

    public PeriodoEntrega() {
    	super(PeriodoEntrega.TABLE_NAME);
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdPeriodoEntrega);
        return primaryKey.toString();
    }

    @Override
    public String getDsDomain() {
    	return dsPeriodoEntrega;
    }
    
    @Override
    public String getCdDomain() {
    	return cdPeriodoEntrega;
    }
    
}