package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class MetaVendaCliGrupo extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPMETAVENDACLIGRUPO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdMetaVendaCli;
    public String cdGrupoProduto;
    public String cdCliente;
    public double vlMeta;
    public double vlRealizado;
	//Nao persistentes
    public static String sortAttr;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof MetaVendaCliGrupo) {
            MetaVendaCliGrupo metaVendaCliGrupo = (MetaVendaCliGrupo) obj;
			return  metaVendaCliGrupo != null && 
					ValueUtil.valueEquals(metaVendaCliGrupo.cdEmpresa, cdEmpresa) &&
					ValueUtil.valueEquals(metaVendaCliGrupo.cdRepresentante, cdRepresentante) &&
					ValueUtil.valueEquals(metaVendaCliGrupo.cdMetaVendaCli, cdMetaVendaCli) &&
	                ValueUtil.valueEquals(metaVendaCliGrupo.cdGrupoProduto, cdGrupoProduto) &&
					ValueUtil.valueEquals(metaVendaCliGrupo.cdCliente, cdCliente);
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
        primaryKey.append(cdMetaVendaCli);
        primaryKey.append(";");
        primaryKey.append(cdGrupoProduto);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        return primaryKey.toString();
    }

    public int getSortIntValue() {
    	if ("VLMETA".equals(sortAttr)) {
    		return ValueUtil.getIntegerValue(vlMeta);
    	} else if ("VLREALIZADO".equals(sortAttr)) {
    		return ValueUtil.getIntegerValue(getPctRealizadoMeta());
    	}
    	return ValueUtil.getIntegerValue(cdGrupoProduto);
    }

    public double getPctRealizadoMeta() {
		double pctRealizadoMeta = 0;
		if (vlMeta > 0) {
			pctRealizadoMeta = (vlRealizado * 100) / vlMeta;
		}
		return pctRealizadoMeta;
	}
    
	public double getPctRestanteMeta() {
		return (getPctRealizadoMeta() < 100 && vlMeta > 0) ? 100 - getPctRealizadoMeta() : 0d;
	}
	
	public boolean isMetaAtingida() {
		return (vlRealizado >= vlMeta);
	}
	
	public boolean isMetaNaoAtingida() {
		return !isMetaAtingida();
	}
	
}