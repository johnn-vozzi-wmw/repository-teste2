package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import totalcross.util.Date;

public class ProdutoRetirada extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPPRODUTORETIRADA";
	public static final String COLUMN_DTMAXRETIRADA = "dtMaxRetirada";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdProduto;
    public Date dtCompra;
    public int nuNota;
    public int qtCompra;
    public int nuContrato;
    public int qtRestante;
    public Date dtMaxRetirada;
    
    //não persistentes
    public Date dtMaxRetiradaStartFilter;
    public String dsProduto;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ProdutoRetirada) {
            ProdutoRetirada produtoRetirada = (ProdutoRetirada) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, produtoRetirada.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, produtoRetirada.cdRepresentante) && 
                ValueUtil.valueEquals(cdCliente, produtoRetirada.cdCliente);
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
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(dtCompra);
        primaryKey.append(";");
        primaryKey.append(nuNota);
        return primaryKey.toString();
    }

	@Override
	public String toString() {
		if (LavenderePdaConfig.usaDescricaoCodigoNaVisualizacaoEntidades && !ValueUtil.valueEquals(cdProduto, dsProduto)) {
	    	StringBuilder strBuffer = new StringBuilder();
	    	if (dsProduto != null) {
	    		strBuffer.append(dsProduto);
	    	}
	    	if (ValueUtil.isNotEmpty(cdProduto)) {
	    		strBuffer.append(" [");
	    		strBuffer.append(cdProduto);
	    		strBuffer.append("]");
	    	}
	    	return strBuffer.toString();
		} else {
			return dsProduto;
		}
	}

}