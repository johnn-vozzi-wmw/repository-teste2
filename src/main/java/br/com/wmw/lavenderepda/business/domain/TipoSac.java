package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class TipoSac extends LavendereBaseDomain {
	
	public static String TABLE_NAME = "TBLVPTIPOSAC";

    public String cdEmpresa;
    public String cdTipoSac;
    public String dsTipoSac;
    public String flRelacionarNotaFiscal;
    public String flRelacionarPedido;
    public String flRelacionarProduto;
    
    public SubTipoSac subTipoSac;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof TipoSac) {
            TipoSac tipoSac = (TipoSac) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, tipoSac.cdEmpresa) && 
                ValueUtil.valueEquals(cdTipoSac, tipoSac.cdTipoSac);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdTipoSac);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdTipoSac;
	}

	@Override
	public String getDsDomain() {
		return dsTipoSac;
	}

}