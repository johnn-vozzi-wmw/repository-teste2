package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class GrupoDescProd extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPGRUPODESCPROD";
    
    public String cdEmpresa;
    public String cdProduto;
    public String cdRepresentante;
    public String cdGrupoDescProd;
    public String dsGrupoDescProd;
    public int nuPrioridade;
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GrupoDescProd) {
            GrupoDescProd grupoDescProd = (GrupoDescProd) obj;
            return
            	ValueUtil.valueEquals(cdEmpresa, grupoDescProd.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, grupoDescProd.cdRepresentante) &&
                ValueUtil.valueEquals(cdProduto, grupoDescProd.cdProduto) &&
                ValueUtil.valueEquals(cdGrupoDescProd, grupoDescProd.cdGrupoDescProd);
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
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(cdGrupoDescProd);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdGrupoDescProd;
	}

	@Override
	public String getDsDomain() {
		return dsGrupoDescProd;
	}

}