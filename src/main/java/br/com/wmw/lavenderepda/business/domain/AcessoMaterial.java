package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class AcessoMaterial extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPACESSOMATERIAL";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdUsuarioAcesso;
    public String cdAcessoMaterial;
    public String nmMaterial;
    public Date dtAcesso;
    public String hrAcesso;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AcessoMaterial) {
            AcessoMaterial acessoMaterial = (AcessoMaterial) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, acessoMaterial.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, acessoMaterial.cdRepresentante) && 
                ValueUtil.valueEquals(cdUsuarioAcesso, acessoMaterial.cdUsuarioAcesso) && 
                ValueUtil.valueEquals(cdAcessoMaterial, acessoMaterial.cdAcessoMaterial);
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
        primaryKey.append(cdUsuarioAcesso);
        primaryKey.append(";");
        primaryKey.append(cdAcessoMaterial);
        return primaryKey.toString();
    }

}