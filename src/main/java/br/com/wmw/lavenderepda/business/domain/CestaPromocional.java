package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class CestaPromocional extends BaseDomain {

	public static String TABLE_NAME = "TBLVPCESTAPROMOCIONAL";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdProduto;
    public String cdTabelaPreco;
    public Date dtVigenciaInicial;
    public Date dtVigenciaFinal;
    public double vlUnitario;
    public double vlPctMaxDesconto;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof CestaPromocional) {
            CestaPromocional cestapromocional = (CestaPromocional) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, cestapromocional.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, cestapromocional.cdRepresentante) &&
                ValueUtil.valueEquals(cdCliente, cestapromocional.cdCliente) &&
                ValueUtil.valueEquals(cdProduto, cestapromocional.cdProduto) &&
                ValueUtil.valueEquals(cdTabelaPreco, cestapromocional.cdTabelaPreco);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdCliente);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
    	strBuffer.append(";");
    	strBuffer.append(cdTabelaPreco);
        return  strBuffer.toString();
    }

}