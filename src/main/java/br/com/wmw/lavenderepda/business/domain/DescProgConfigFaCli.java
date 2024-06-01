package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.ValueUtil;

public class DescProgConfigFaCli extends BasePersonDomain {

	public static final String TABLE_NAME = "TBLVPDESCPROGCONFIGFACLI";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdDescProgressivo;
    public String cdFaixaCli;
    public String cdCliente;
    public String flTipoAlteracao;

	public DescProgConfigFaCli() { super(TABLE_NAME); }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DescProgConfigFaCli)) return false;
        DescProgConfigFaCli descProgressivoConfig = (DescProgConfigFaCli) obj;
        return ValueUtil.valueEquals(cdEmpresa, descProgressivoConfig.cdEmpresa)
            && ValueUtil.valueEquals(cdRepresentante, descProgressivoConfig.cdRepresentante)
            && ValueUtil.valueEquals(cdDescProgressivo, descProgressivoConfig.cdDescProgressivo)
            && ValueUtil.valueEquals(cdFaixaCli, descProgressivoConfig.cdFaixaCli)
            && ValueUtil.valueEquals(cdCliente, descProgressivoConfig.cdCliente);
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdDescProgressivo);
        primaryKey.append(";");
        primaryKey.append(cdFaixaCli);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        return primaryKey.toString();
    }

}