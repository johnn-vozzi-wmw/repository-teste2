package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class VariacaoProdCli extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPVARIACAOPRODCLI";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    public String cdCliente;
    public double vlPctVariacao;
    public Date dtInicioVigencia;
    public Date dtFimVigencia;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof VariacaoProdCli) {
            VariacaoProdCli variacaoProdCli = (VariacaoProdCli) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, variacaoProdCli.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, variacaoProdCli.cdRepresentante) && 
                ValueUtil.valueEquals(cdProduto, variacaoProdCli.cdProduto) && 
                ValueUtil.valueEquals(cdCliente, variacaoProdCli.cdCliente);
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
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        return primaryKey.toString();
    }

    public VariacaoProdCli() {
    	super();
	}
    
	public VariacaoProdCli(String cdEmpresa, String cdRepresentante, String cdProduto, String cdCliente) {
		super();
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdProduto = cdProduto;
		this.cdCliente = cdCliente;
	}

    
    
}