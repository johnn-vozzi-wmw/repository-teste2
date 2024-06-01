package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.ValueUtil;

public class  DescProgFamilia extends BasePersonDomain {

	public static final String TABLE_NAME = "TBLVPDESCPROGFAMILIA";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdFamiliaDescProg;
    public String dsFamiliaProd;
    public String flTipoAlteracao;

	public DescProgFamilia() { super(TABLE_NAME); }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DescProgFamilia)) return false;
        DescProgFamilia descProgressivoConfig = (DescProgFamilia) obj;
        return ValueUtil.valueEquals(cdEmpresa, descProgressivoConfig.cdEmpresa)
            && ValueUtil.valueEquals(cdRepresentante, descProgressivoConfig.cdRepresentante)
            && ValueUtil.valueEquals(cdFamiliaDescProg, descProgressivoConfig.cdFamiliaDescProg);
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdFamiliaDescProg);
        return primaryKey.toString();
    }

}