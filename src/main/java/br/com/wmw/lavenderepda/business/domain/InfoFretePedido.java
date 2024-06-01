package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class InfoFretePedido extends BaseDomain {

	public static String TABLE_NAME = "TBLVPINFOFRETEPEDIDO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String nuPedido;
    public String flOrigemPedido;
    public String flTaxaEntrega;
    public double vlTaxaEntrega;
    public String flAjudante;
    public int qtAjudante;
    public String flAntecipaEntrega;
    public String flAgendamento;
    public String cdTipoVeiculo;
    

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof InfoFretePedido) {
            InfoFretePedido infoFretePedido = (InfoFretePedido) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, infoFretePedido.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, infoFretePedido.cdRepresentante) &&
                ValueUtil.valueEquals(nuPedido, infoFretePedido.nuPedido) &&
            	ValueUtil.valueEquals(flOrigemPedido, infoFretePedido.flOrigemPedido);
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
        primaryKey.append(nuPedido);
        primaryKey.append(";");
        primaryKey.append(flOrigemPedido);
        return primaryKey.toString();
    }

}