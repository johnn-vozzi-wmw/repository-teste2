package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class DescMaxProdCli extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPDESCMAXPRODCLI";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdGrupoDescMaxProd;
    public String cdCliente;
    public double vlPctDescMax;
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DescMaxProdCli) {
            DescMaxProdCli descMaxProdCli = (DescMaxProdCli) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, descMaxProdCli.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, descMaxProdCli.cdRepresentante) && 
                ValueUtil.valueEquals(cdGrupoDescMaxProd, descMaxProdCli.cdGrupoDescMaxProd) && 
                ValueUtil.valueEquals(cdCliente, descMaxProdCli.cdCliente);
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
        primaryKey.append(cdGrupoDescMaxProd);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        return primaryKey.toString();
    }
    
}