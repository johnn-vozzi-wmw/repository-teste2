package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;

public class MetasPorProduto extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPMETASPORPRODUTO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String dsPeriodo;
    public String cdProduto;
    public double vlMeta;
    public double vlRealizado;
    public String dsProduto;
    public String nuSequencia;
    public double qtUnidadeMeta;
    public double qtUnidadeRealizado;
    public double qtCaixaMeta;
    public double qtCaixaRealizado;
    public double qtMixClienteMeta;
    public double qtMixClienteRealizado;

    //-- Não Persistente
    public String cdProdutoLike;
    public boolean findDescProduto;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof MetasPorProduto) {
            MetasPorProduto metasporproduto = (MetasPorProduto) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, metasporproduto.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, metasporproduto.cdRepresentante) &&
                ValueUtil.valueEquals(cdProduto, metasporproduto.cdProduto) &&
                ValueUtil.valueEquals(dsPeriodo, metasporproduto.dsPeriodo);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
    	strBuffer.append(";");
    	strBuffer.append(dsPeriodo);
        return strBuffer.toString();
    }

    public double getPctRealizadoMeta() {
    	double pctRealizadoMeta = 0;
    	if (vlMeta > 0) {
    		pctRealizadoMeta = (vlRealizado * 100) / vlMeta;
    	}
    	return pctRealizadoMeta;
    }

	public String getCdDomain() {
		return cdProduto;
	}

	public String getDsDomain() {
		if (!LavenderePdaConfig.usaDescricaoCodigoNaVisualizacaoEntidades) {
			return cdProduto + "-" + StringUtil.getStringValue(dsProduto);
		}
		return dsProduto;
	}

}