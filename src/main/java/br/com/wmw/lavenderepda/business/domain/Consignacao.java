package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;
import totalcross.util.Vector;

public class Consignacao extends BaseDomain {

	public static String TABLE_NAME = "TBLVPCONSIGNACAO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdConsignacao;
    public Date dtConsignacao;
    public Date dtProximaVisita;
    public double vlTotalConsignado;
    public String flOrigemPedido;
    public String nuPedido;
    public String flPagamentoEfetuado;
	public Vector itemConsignacaoList;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Consignacao) {
            Consignacao consignacao = (Consignacao) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, consignacao.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, consignacao.cdRepresentante) &&
                ValueUtil.valueEquals(cdCliente, consignacao.cdCliente) &&
                ValueUtil.valueEquals(cdConsignacao, consignacao.cdConsignacao);
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
        primaryKey.append(cdConsignacao);
        return primaryKey.toString();
    }

}