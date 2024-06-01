package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.ValueUtil;

public class DescProgFaixa extends BasePersonDomain {

	public static final String TABLE_NAME = "TBLVPDESCPROGFAIXA";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdFaixaCli;
    public String dsFaixaCli;
    public String flTipoAlteracao;

	public DescProgFaixa() { super(TABLE_NAME); }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DescProgFaixa)) return false;
        DescProgFaixa descProgressivoConfig = (DescProgFaixa) obj;
        return ValueUtil.valueEquals(cdEmpresa, descProgressivoConfig.cdEmpresa)
            && ValueUtil.valueEquals(cdRepresentante, descProgressivoConfig.cdRepresentante)
            && ValueUtil.valueEquals(cdFaixaCli, descProgressivoConfig.cdFaixaCli);
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdFaixaCli);
        return primaryKey.toString();
    }

}