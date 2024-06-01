package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class DescProgQtd extends BaseDomain {

	public static String TABLE_NAME = "TBLVPDESCPROGQTD";
	public static String NOME_COLUNA_VLPCTDESCONTO = "VLPCTDESCONTO";
	public static String NOME_COLUNA_QTUNIDADE = "QTUNIDADE";

	public static final String SEM_UNIDADE = "0";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdUnidade;
    public int qtUnidade;
    public double vlPctDesconto;
    public int nuCarimbo;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof DescProgQtd) {
            DescProgQtd descProgQtd = (DescProgQtd) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, descProgQtd.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, descProgQtd.cdRepresentante) &&
                ValueUtil.valueEquals(cdUnidade, descProgQtd.cdUnidade) &&
                ValueUtil.valueEquals(qtUnidade, descProgQtd.qtUnidade);
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
        primaryKey.append(cdUnidade);
        primaryKey.append(";");
        primaryKey.append(qtUnidade);
        return primaryKey.toString();
    }

}