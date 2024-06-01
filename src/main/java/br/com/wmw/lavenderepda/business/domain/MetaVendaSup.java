package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class MetaVendaSup extends BaseDomain {

	public static String TABLE_NAME = "TBLVPMETAVENDASUP";

    public String cdEmpresa;
    public String cdMetaVenda;
    public String cdGerente;
    public String vlChaveTipo;
    public String cdSupervisor;
    public double vlMetaVenda;
    public double vlRealizadoVenda;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof MetaVendaSup) {
            MetaVendaSup metaVendaSup = (MetaVendaSup) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, metaVendaSup.cdEmpresa) &&
                ValueUtil.valueEquals(cdMetaVenda, metaVendaSup.cdMetaVenda) &&
                ValueUtil.valueEquals(cdGerente, metaVendaSup.cdGerente) &&
                ValueUtil.valueEquals(vlChaveTipo, metaVendaSup.vlChaveTipo) &&
                ValueUtil.valueEquals(cdSupervisor, metaVendaSup.cdSupervisor);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdMetaVenda);
        primaryKey.append(";");
        primaryKey.append(cdGerente);
        primaryKey.append(";");
        primaryKey.append(vlChaveTipo);
        primaryKey.append(";");
        primaryKey.append(cdSupervisor);
        return primaryKey.toString();
    }

}