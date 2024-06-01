package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class GrupoDescCli extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPGRUPODESCCLI";
    
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdGrupoDescCli;
    public String dsGrupoDescCli;
    public int nuPrioridade;
    
    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof GrupoDescCli) {
            GrupoDescCli grupoDescCli = (GrupoDescCli) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, grupoDescCli.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, grupoDescCli.cdRepresentante) && 
                ValueUtil.valueEquals(cdCliente, grupoDescCli.cdCliente) &&
                ValueUtil.valueEquals(cdGrupoDescCli, grupoDescCli.cdGrupoDescCli);
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
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(cdGrupoDescCli);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdGrupoDescCli;
	}

	@Override
	public String getDsDomain() {
		return dsGrupoDescCli;
	}

}