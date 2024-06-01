package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class TipoFreteCli extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPTIPOFRETECLI";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdTipoFrete;
    public String flDefault;
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TipoFreteCli) {
            TipoFreteCli tipoFreteCli = (TipoFreteCli) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, tipoFreteCli.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, tipoFreteCli.cdRepresentante) && 
                ValueUtil.valueEquals(cdCliente, tipoFreteCli.cdCliente) && 
                ValueUtil.valueEquals(cdTipoFrete, tipoFreteCli.cdTipoFrete);
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
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(cdTipoFrete);
        return primaryKey.toString();
    }
    
	public boolean isDefault() {
		return ValueUtil.VALOR_SIM.equals(flDefault);
	}

}