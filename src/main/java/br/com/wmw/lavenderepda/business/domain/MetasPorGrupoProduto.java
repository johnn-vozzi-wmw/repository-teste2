package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class MetasPorGrupoProduto extends BaseDomain {

    public static String TABLE_NAME = "TBLVPMETAPORGRUPOPROD";

	public static String CDGRUPOPRODUTO_NULO = "0";

    public String cdEmpresa;
    public String cdRepresentante;
    public String dsPeriodo;
    public String cdGrupoProduto1;
    public String cdGrupoProduto2;
    public String cdGrupoProduto3;
    public String cdProduto;
    public String nuSequencia;
    public Date dtInicial;
    public Date dtFinal;
    public double vlMeta;
    public double vlRealizado;
    public double qtUnidadeMeta;
    public double qtUnidadeRealizado;
    public double qtCaixaMeta;
    public double qtCaixaRealizado;
    public double qtMixClienteMeta;
    public double qtMixClienteRealizado;
    public String dsGrupoProduto1;

    //-- Não Persistente
    public String cdGrupoProduto1Like;
    public boolean findDescGrupoProduto1;
    public String dsGrupoProduto2;
    public String dsGrupoProduto3;
    public String cdGrupoProduto2Dif;
    public String cdGrupoProduto3Dif;
    public String cdProdutoDif;
    public double vlRealizadoPedidosPda;
    public double vlSaldoLiberadoSenha;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof MetasPorGrupoProduto) {
            MetasPorGrupoProduto metasporgrupoproduto = (MetasPorGrupoProduto) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, metasporgrupoproduto.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, metasporgrupoproduto.cdRepresentante) &&
                ValueUtil.valueEquals(cdGrupoProduto1, metasporgrupoproduto.cdGrupoProduto1) &&
                ValueUtil.valueEquals(cdGrupoProduto2, metasporgrupoproduto.cdGrupoProduto2) &&
                ValueUtil.valueEquals(cdGrupoProduto3, metasporgrupoproduto.cdGrupoProduto3) &&
                ValueUtil.valueEquals(cdProduto, metasporgrupoproduto.cdProduto) &&
                ValueUtil.valueEquals(dsPeriodo, metasporgrupoproduto.dsPeriodo);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(dsPeriodo);
    	strBuffer.append(";");
    	strBuffer.append(cdGrupoProduto1);
    	strBuffer.append(";");
    	strBuffer.append(cdGrupoProduto2);
    	strBuffer.append(";");
    	strBuffer.append(cdGrupoProduto3);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
        return strBuffer.toString();
    }

    public double getPctRealizadoMeta() {
    	double pctRealizadoMeta = 0;
    	if (vlMeta > 0) {
    		pctRealizadoMeta = (vlRealizado * 100) / vlMeta;
    	}
    	return pctRealizadoMeta;
    }

    public double getPctRealizadoQtUnidade() {
    	double pctRealizadoMeta = 0;
    	if (qtUnidadeMeta > 0) {
    		pctRealizadoMeta = (qtUnidadeRealizado * 100) / qtUnidadeMeta;
    	}
    	return pctRealizadoMeta;
    }

    public double getPctRealizadoQtCaixa() {
    	double pctRealizadoMeta = 0;
    	if (qtCaixaMeta > 0) {
    		pctRealizadoMeta = (qtCaixaRealizado * 100) / qtCaixaMeta;
    	}
    	return pctRealizadoMeta;
    }

    public double getPctRealizadoQtMixCliente() {
    	double pctRealizadoMeta = 0;
    	if (qtMixClienteMeta > 0) {
    		pctRealizadoMeta = (qtMixClienteRealizado * 100) / qtMixClienteMeta;
    	}
    	return pctRealizadoMeta;
    }

}