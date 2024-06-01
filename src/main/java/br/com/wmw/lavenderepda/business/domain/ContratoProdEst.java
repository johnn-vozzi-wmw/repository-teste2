package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class ContratoProdEst extends BaseDomain {

	public static String TABLE_NAME = "TBLVPCONTRATOPRODEST";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String flTipoContrato;
    public Date dtVigenciaInicial;
    public String cdProduto;
    public Date dtCadastro;
    public double qtEstoqueAtual;

    //Não persistente
    public ContratoProduto contratoProduto;
    public String qtEstAtual;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ContratoProdEst) {
            ContratoProdEst contratoProdEst = (ContratoProdEst) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, contratoProdEst.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, contratoProdEst.cdRepresentante) &&
                ValueUtil.valueEquals(cdCliente, contratoProdEst.cdCliente) &&
                ValueUtil.valueEquals(flTipoContrato, contratoProdEst.flTipoContrato) &&
                ValueUtil.valueEquals(dtVigenciaInicial, contratoProdEst.dtVigenciaInicial) &&
                ValueUtil.valueEquals(cdProduto, contratoProdEst.cdProduto) &&
                ValueUtil.valueEquals(dtCadastro, contratoProdEst.dtCadastro);
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
        primaryKey.append(";");
        primaryKey.append(dtCadastro);
        return primaryKey.toString();
    }

}