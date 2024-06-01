package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class DescFaixaValorPedido extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPDESCFAIXAVALORPEDIDO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdFaixaValorPedido;
    public double vlTotalPedido;
    public double vlPctDesconto;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DescFaixaValorPedido) {
            DescFaixaValorPedido descFaixaValorPedido = (DescFaixaValorPedido) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, descFaixaValorPedido.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, descFaixaValorPedido.cdRepresentante) && 
                ValueUtil.valueEquals(cdFaixaValorPedido, descFaixaValorPedido.cdFaixaValorPedido);
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
        primaryKey.append(cdFaixaValorPedido);
        return primaryKey.toString();
    }

}