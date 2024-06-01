package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class BonificacaoSaldo extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPBONIFICACAOSALDO";
	public static String FL_ORIGEM_ERP = "E";
	public static String FL_ORIGEM_PDA = "P";

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemSaldo;
    public double vlSaldo;
    public String dsControleAtualizacao;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BonificacaoSaldo) {
            BonificacaoSaldo bonificacaoSaldo = (BonificacaoSaldo) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, bonificacaoSaldo.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, bonificacaoSaldo.cdRepresentante) && 
                ValueUtil.valueEquals(flOrigemSaldo, bonificacaoSaldo.flOrigemSaldo);
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
        primaryKey.append(flOrigemSaldo);
        return primaryKey.toString();
    }

}