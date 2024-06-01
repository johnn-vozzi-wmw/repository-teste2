package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class TipoVeiculo extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPTIPOVEICULO";
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTipoVeiculo;
    public String dsTipoVeiculo;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof TipoVeiculo) {
            TipoVeiculo tipoveiculo = (TipoVeiculo) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, tipoveiculo.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, tipoveiculo.cdRepresentante) && 
                ValueUtil.valueEquals(cdTipoVeiculo, tipoveiculo.cdTipoVeiculo);
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
        primaryKey.append(cdTipoVeiculo);
        return primaryKey.toString();
    }
    
    public String getCdDomain() {
		return cdTipoVeiculo;
	}

	public String getDsDomain() {
		return dsTipoVeiculo;
	}

}