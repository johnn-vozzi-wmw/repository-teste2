package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class VerbaSaldoVigencia extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPVERBASALDOVIGENCIA";
	public static final String NMCOLUNA_DTINICIOVIGENCIA = "DTINICIOVIGENCIA";
	public static final String NMCOLUNA_VLSALDO = "VLSALDO";

    public String cdEmpresa;
    public String cdRepresentante;
    public int cdMesSaldo;
    public double vlSaldo;
    public Date dtSaldo;
    public Date dtInicioVigencia;
    public Date dtFimVigencia;
    
    // Nao persistente
    public Date dtVigenciaFilter;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VerbaSaldoVigencia) {
            VerbaSaldoVigencia verbaSaldoVigencia = (VerbaSaldoVigencia) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, verbaSaldoVigencia.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, verbaSaldoVigencia.cdRepresentante) && 
                ValueUtil.valueEquals(cdMesSaldo, verbaSaldoVigencia.cdMesSaldo);
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
        primaryKey.append(cdMesSaldo);
        return primaryKey.toString();
    }

}