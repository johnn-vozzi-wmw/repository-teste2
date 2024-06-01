package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class IndiceClienteGrupoProd extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPINDICECLIENTEGRUPOPROD";
	public static final String CD_CHAVE_VAZIO = "0";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdGrupoProduto1;
    public String cdGrupoProduto2;
    public String cdProduto;
    public double vlIndiceFinanceiro;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IndiceClienteGrupoProd) {
            IndiceClienteGrupoProd indiceClienteGrupoProd = (IndiceClienteGrupoProd) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, indiceClienteGrupoProd.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, indiceClienteGrupoProd.cdRepresentante) && 
                ValueUtil.valueEquals(cdCliente, indiceClienteGrupoProd.cdCliente) && 
                ValueUtil.valueEquals(cdGrupoProduto1, indiceClienteGrupoProd.cdGrupoProduto1) && 
                ValueUtil.valueEquals(cdGrupoProduto2, indiceClienteGrupoProd.cdGrupoProduto2) && 
                ValueUtil.valueEquals(cdProduto, indiceClienteGrupoProd.cdProduto);
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
        primaryKey.append(cdGrupoProduto1);
        primaryKey.append(";");
        primaryKey.append(cdGrupoProduto2);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        return primaryKey.toString();
    }

}