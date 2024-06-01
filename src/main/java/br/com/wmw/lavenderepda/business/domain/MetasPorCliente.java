package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class MetasPorCliente extends BaseDomain {

    public static String TABLE_NAME = "TBLVPMETASPORCLIENTE";

    public String cdEmpresa;
    public String cdRepresentante;
    public String dsPeriodo;
    public String cdCliente;
    public String nuSequencia;
    public double vlMeta;
    public double vlRealizado;
    public double qtUnidadeMeta;
    public double qtCaixaMeta;
    public double qtMixProdutoMeta;
    //Não Persistente
    public String dsCliente;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof MetasPorCliente) {
            MetasPorCliente metasporcliente = (MetasPorCliente) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, metasporcliente.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, metasporcliente.cdRepresentante) &&
                ValueUtil.valueEquals(dsPeriodo, metasporcliente.dsPeriodo) &&
                ValueUtil.valueEquals(cdCliente, metasporcliente.cdCliente);
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
    	strBuffer.append(cdCliente);
        return strBuffer.toString();
    }

    public double getPctRealizadoMeta() {
    	double pctRealizadoMeta = 0;
    	if (vlMeta > 0) {
    		pctRealizadoMeta = (vlRealizado * 100) / vlMeta;
    	}
    	return pctRealizadoMeta;
    }


}