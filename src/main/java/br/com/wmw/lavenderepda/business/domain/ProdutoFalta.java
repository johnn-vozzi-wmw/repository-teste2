package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class ProdutoFalta extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPPRODUTOFALTA";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
	public String cdProduto;
    public String cdProdutoFalta;
    public String cdTabelaPreco;
    public String cdCampanha;
    public Date dtFaltante;
    public Date dtRegistro;
    public int qtFaltante;
    public int qtAtendida;
    public double vlPctDesconto;
    public String nuPedido;
    public Date dtEntregaPedido;
    public String cdUnidade;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ProdutoFalta) {
            ProdutoFalta produtoFalta = (ProdutoFalta) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, produtoFalta.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, produtoFalta.cdRepresentante) && 
                ValueUtil.valueEquals(cdCliente, produtoFalta.cdCliente) && 
                ValueUtil.valueEquals(cdProduto, produtoFalta.cdProduto);
        }
        return false;
    }

	public ProdutoFalta() {
		super();
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
        primaryKey.append(cdProdutoFalta);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        return primaryKey.toString();
    }

}