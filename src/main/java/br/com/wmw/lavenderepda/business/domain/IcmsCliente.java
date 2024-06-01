package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class IcmsCliente extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPICMSCLIENTE";
	public static final String CD_TRIBUTACAO_CONFIG_PADRAO = "0";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdRamoAtividade;
    public String cdUf;
    public String flContribuinte;
    public String flConsumidorFinal;
    public String cdConfigIcmsEspecial;
    public double vlPctIcms;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IcmsCliente) {
            IcmsCliente icmsCliente = (IcmsCliente) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, icmsCliente.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, icmsCliente.cdRepresentante) && 
                ValueUtil.valueEquals(cdRamoAtividade, icmsCliente.cdRamoAtividade) && 
                ValueUtil.valueEquals(cdUf, icmsCliente.cdUf) && 
                ValueUtil.valueEquals(flContribuinte, icmsCliente.flContribuinte) && 
                ValueUtil.valueEquals(flConsumidorFinal, icmsCliente.flConsumidorFinal);
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
        primaryKey.append(cdRamoAtividade);
        primaryKey.append(";");
        primaryKey.append(cdUf);
        primaryKey.append(";");
        primaryKey.append(flContribuinte);
        primaryKey.append(";");
        primaryKey.append(flConsumidorFinal);
        return primaryKey.toString();
    }

}