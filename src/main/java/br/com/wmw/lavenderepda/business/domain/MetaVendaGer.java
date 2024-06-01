package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class MetaVendaGer extends BaseDomain {

	public static String TABLE_NAME = "TBLVPMETAVENDAGER";

    public String cdEmpresa;
    public String cdMetaVenda;
    public String cdGerente;
    public String vlChaveTipo;
    public double vlMetaVenda;
    public double vlRealizadoVenda;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof MetaVendaGer) {
            MetaVendaGer metaVendaGer = (MetaVendaGer) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, metaVendaGer.cdEmpresa) &&
                ValueUtil.valueEquals(cdMetaVenda, metaVendaGer.cdMetaVenda) &&
                ValueUtil.valueEquals(cdGerente, metaVendaGer.cdGerente) &&
                ValueUtil.valueEquals(vlChaveTipo, metaVendaGer.vlChaveTipo);
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
        return primaryKey.toString();
    }

}