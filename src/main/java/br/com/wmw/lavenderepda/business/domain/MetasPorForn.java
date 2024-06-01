package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class MetasPorForn extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPMETASPORFORN";

    public String cdEmpresa;
    public String cdRepresentante;
    public String dsPeriodo;
    public String cdFornecedor;
    public String nuSequencia;
    public double vlMeta;
    public double vlRealizado;
    public double qtUnidadeMeta;
    public double qtCaixaMeta;
    public double qtMixClienteMeta;
    public double qtMixProdutoMeta;
    //Não persistente
    public String dsFornecedor;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof MetasPorForn) {
            MetasPorForn metasporfornecedor = (MetasPorForn) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, metasporfornecedor.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, metasporfornecedor.cdRepresentante) &&
                ValueUtil.valueEquals(dsPeriodo, metasporfornecedor.dsPeriodo) &&
                ValueUtil.valueEquals(cdFornecedor, metasporfornecedor.cdFornecedor);
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
    	strBuffer.append(cdFornecedor);
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
		return cdFornecedor;
	}

	public String getDsDomain() {
		return dsFornecedor;
	}

}