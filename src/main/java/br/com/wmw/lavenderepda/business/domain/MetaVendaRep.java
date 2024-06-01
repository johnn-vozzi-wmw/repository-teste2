package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class MetaVendaRep extends BaseDomain {

	public static String TABLE_NAME = "TBLVPMETAVENDAREP";

    public String cdEmpresa;
	public String cdSupervisor;
	public String cdRepresentante;
    public String cdMetaVenda;
    public String vlChaveTipo;
    public String cdGerente;
    public double vlMetaVenda;
    public double vlRealizadoVenda;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof MetaVendaRep) {
            MetaVendaRep metaVendaRep = (MetaVendaRep) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, metaVendaRep.cdEmpresa) &&
                ValueUtil.valueEquals(cdMetaVenda, metaVendaRep.cdMetaVenda) &&
                ValueUtil.valueEquals(cdGerente, metaVendaRep.cdGerente) &&
                ValueUtil.valueEquals(vlChaveTipo, metaVendaRep.vlChaveTipo) &&
                ValueUtil.valueEquals(cdSupervisor, metaVendaRep.cdSupervisor) &&
                ValueUtil.valueEquals(cdRepresentante, metaVendaRep.cdRepresentante);
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
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        return primaryKey.toString();
    }

}