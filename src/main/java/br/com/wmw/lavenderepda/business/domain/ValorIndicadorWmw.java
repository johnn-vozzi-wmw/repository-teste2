package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class ValorIndicadorWmw extends BasePersonDomain {

	public static final String TABLE_NAME = "TBLVPVALORINDICADORWMW";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdIndicador;
    public String cdApuracao;
    public double vlIndicador;
    public Date dtApuracao;
    public Date dtOrdenacao;
    public String hrApuracao;
    public String dsApuracao;
    public double vlMeta;
    
    public ValorIndicadorWmw() {
		super(TABLE_NAME);
	}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ValorIndicadorWmw) {
            ValorIndicadorWmw valorIndicadorWmw = (ValorIndicadorWmw) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, valorIndicadorWmw.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, valorIndicadorWmw.cdRepresentante) && 
                ValueUtil.valueEquals(cdIndicador, valorIndicadorWmw.cdIndicador) && 
                ValueUtil.valueEquals(cdApuracao, valorIndicadorWmw.cdApuracao);
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
        primaryKey.append(cdIndicador);
        primaryKey.append(";");
        primaryKey.append(cdApuracao);
        return primaryKey.toString();
    }

}