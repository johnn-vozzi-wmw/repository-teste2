package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class DepositoBancario extends BaseDomain {

	public static String TABLE_NAME = "TBLVPDEPOSITOBANCARIO";

    public String cdEmpresa;
    public String cdRepresentante;
    public Date dtFechamentoDiario;
    public String nuDepositoBancario;
    public double vlTotalDepositoBancario;
    public Date dtDepositoBancario;
    public String hrAlteracao;
    public Date dtAlteracao;
    
    //Não Persistente
    public Date dtFechamentoDiarioFilter;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DepositoBancario) {
            DepositoBancario depositobancario = (DepositoBancario) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, depositobancario.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, depositobancario.cdRepresentante) && 
                ValueUtil.valueEquals(dtFechamentoDiario, depositobancario.dtFechamentoDiario) && 
                ValueUtil.valueEquals(nuDepositoBancario, depositobancario.nuDepositoBancario);
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
        primaryKey.append(dtFechamentoDiario);
        primaryKey.append(";");
        primaryKey.append(nuDepositoBancario);
        return primaryKey.toString();
    }

}