package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class TipoAnaliseCliente extends LavendereBaseDomain {

    public String cdTipoAnalise;
    public String dsTipoAnalise;
    public Date dtAlteracao;
    public String hrAlteracao;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof TipoAnaliseCliente) {
            TipoAnaliseCliente tipoAnaliseCliente = (TipoAnaliseCliente) obj;
            return
                ValueUtil.valueEquals(cdTipoAnalise, tipoAnaliseCliente.cdTipoAnalise);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdTipoAnalise);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdTipoAnalise;
	}

	@Override
	public String getDsDomain() {
		return dsTipoAnalise;
	}

}