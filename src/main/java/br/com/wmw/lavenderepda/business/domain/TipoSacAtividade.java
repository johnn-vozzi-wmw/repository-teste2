package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class TipoSacAtividade extends LavendereBaseDomain {
	
	public static String TABLE_NAME = "TBLVPTIPOSACATIVIDADE";

    public String cdEmpresa;
    public String cdTipoSac;
    public String cdAtividade;
    public String cdUsuarioTipoSac;
    public String dsTitulo;
    public int nuSequencia;
    public int nuDiasUteis;
    public int nuDiasPrazoMax;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof TipoSacAtividade) {
            TipoSacAtividade tipoSacAtividade = (TipoSacAtividade) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, tipoSacAtividade.cdEmpresa) && 
                ValueUtil.valueEquals(cdTipoSac, tipoSacAtividade.cdTipoSac) && 
                ValueUtil.valueEquals(cdAtividade, tipoSacAtividade.cdAtividade);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdTipoSac);
        primaryKey.append(";");
        primaryKey.append(cdAtividade);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdTipoSac;
	}

	@Override
	public String getDsDomain() {
		return dsTitulo;
	}

}