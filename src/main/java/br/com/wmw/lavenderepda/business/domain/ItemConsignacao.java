package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ItemConsignacao extends BaseDomain {

	public static String TABLE_NAME = "TBLVPITEMCONSIGNACAO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdConsignacao;
    public String cdProduto;
    public String cdTabelaPreco;
    public double vlItem;
    public double qtItemConsignado;
    public double qtItemSobra;
    //Não persistente
    public boolean isEditing = false;
    public double qtItemVenda;

    public double getQtItemVenda() {
    	return qtItemConsignado - qtItemSobra;
    }

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemConsignacao) {
            ItemConsignacao itemConsignacao = (ItemConsignacao) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, itemConsignacao.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, itemConsignacao.cdRepresentante) &&
                ValueUtil.valueEquals(cdCliente, itemConsignacao.cdCliente) &&
                ValueUtil.valueEquals(cdConsignacao, itemConsignacao.cdConsignacao) &&
                ValueUtil.valueEquals(cdProduto, itemConsignacao.cdProduto);
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
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        return primaryKey.toString();
    }

}