package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class MotCancelPedido extends LavendereBaseDomain {
	
	public static final String TABLE_NAME = "TBLVPMOTCANCELPEDIDO";

    public String cdMotivoCancelamento;
    public String dsMotivoCancelamento;
    public String flCancelamentoAuto;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof MotCancelPedido) {
            MotCancelPedido motCancelPedido = (MotCancelPedido) obj;
            return
                ValueUtil.valueEquals(cdMotivoCancelamento, motCancelPedido.cdMotivoCancelamento);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdMotivoCancelamento);
        return primaryKey.toString();
    }

	//@Override
	public String getCdDomain() {
		return cdMotivoCancelamento;
	}

	//@Override
	public String getDsDomain() {
		return dsMotivoCancelamento;
	}

}