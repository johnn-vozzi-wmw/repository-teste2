package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class SaldoVendaRep extends BaseDomain {

	public static String TABLE_NAME = "TBLVPSALDOVENDAREP";

    public String cdEmpresa;
    public String cdRepresentante;
    public Date dtUltimoSaldo;
    public double vlUltimoSaldo;
    public Date dtAlteracao;
    public String hrAlteracao;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SaldoVendaRep) {
            SaldoVendaRep saldovendarep = (SaldoVendaRep) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, saldovendarep.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, saldovendarep.cdRepresentante) && 
                ValueUtil.valueEquals(dtUltimoSaldo, saldovendarep.dtUltimoSaldo);
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
        primaryKey.append(DateUtil.formatDateDb(dtUltimoSaldo));
        return primaryKey.toString();
    }

}