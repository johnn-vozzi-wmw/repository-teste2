package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class TipoEndereco extends LavendereBaseDomain {

	public String cdTipoEndereco;
    public String cdEmpresa;
    public String dsTipoEndereco;
    public Date dtAlteracao;
    public String hrAlteracao;
    public Integer nuMinDiasEntregaPedido;
    public String hrLimiteEnvioPedido;
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof TipoEndereco) {
            TipoEndereco tipoEndereco = (TipoEndereco) obj;
            return
                ValueUtil.valueEquals(cdTipoEndereco, tipoEndereco.cdTipoEndereco);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdTipoEndereco);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdTipoEndereco;
	}

	@Override
	public String getDsDomain() {
		return dsTipoEndereco;
	}

}