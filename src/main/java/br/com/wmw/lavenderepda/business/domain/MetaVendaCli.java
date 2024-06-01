package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class MetaVendaCli extends LavendereBaseDomain {
	
	public static final String TABLE_NAME = "TBLVPMETAVENDACLI";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdMetaVendaCli;
	public String cdCliente;
	public String dsMetaVendaCli;
	public Date dtInicialVigencia;
	public Date dtFinalVigencia;
	
	//Nao persistentes
	public double vlMeta;
	public double vlRealizado;
	public double vlRealizar;
	public double pctRealizado;
	public double pctRestante;
	public static String sortAttr;
	public Cliente cliente;

	//Override
	public boolean equals(Object obj) {
		if (obj instanceof MetaVendaCli) {
			MetaVendaCli metaVendaCli = (MetaVendaCli) obj;
			return  metaVendaCli != null && 
					ValueUtil.valueEquals(metaVendaCli.cdEmpresa, cdEmpresa) &&
					ValueUtil.valueEquals(metaVendaCli.cdRepresentante, cdRepresentante) &&
					ValueUtil.valueEquals(metaVendaCli.cdMetaVendaCli, cdMetaVendaCli) &&
					ValueUtil.valueEquals(metaVendaCli.cdCliente, cdCliente);
		}
		return false;
	}

	@Override
	public String getCdDomain() {
		return cdMetaVendaCli;
	}

	@Override
	public String getDsDomain() {
		return dsMetaVendaCli;
	}

	//Override
	public String toString() {
    	if (sortAttr != null &&  sortAttr.equals("CDMETAVENDACLI")) {
    		return cdMetaVendaCli;
    	}
    	return super.toString();
    }

	//Override
    public int getSortIntValue() {
    	if ("vlMeta".equals(sortAttr)) {
    		return ValueUtil.getIntegerValue(vlMeta);
    	} else if ("vlRealizado".equals(sortAttr)) {
    		return ValueUtil.getIntegerValue(getPctRealizadoMeta());
    	} else if ("pctRestante".equals(sortAttr)) {
    		return ValueUtil.getIntegerValue(getPctRestanteMeta());
    	}
    	return ValueUtil.getIntegerValue(cdMetaVendaCli);
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
		primaryKey.append(cdCliente);
		return primaryKey.toString();
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
