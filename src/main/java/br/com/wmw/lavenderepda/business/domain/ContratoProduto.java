package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class ContratoProduto extends BaseDomain {

	public static String TABLE_NAME = "TBLVPCONTRATOPRODUTO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String flTipoContrato;
    public Date dtVigenciaInicial;
    public String cdProduto;
    public double qtProdutoContrato;
    public int nuCarimbo;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ContratoProduto) {
            ContratoProduto contratoProduto = (ContratoProduto) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, contratoProduto.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, contratoProduto.cdRepresentante) &&
                ValueUtil.valueEquals(cdCliente, contratoProduto.cdCliente) &&
                ValueUtil.valueEquals(flTipoContrato, contratoProduto.flTipoContrato) &&
                ValueUtil.valueEquals(dtVigenciaInicial, contratoProduto.dtVigenciaInicial) &&
                ValueUtil.valueEquals(cdProduto, contratoProduto.cdProduto);
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
        primaryKey.append(flTipoContrato);
        primaryKey.append(";");
        primaryKey.append(dtVigenciaInicial);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        return primaryKey.toString();
    }

}