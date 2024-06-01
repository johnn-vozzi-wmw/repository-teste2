package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class ProdutoCreditoDesc extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPPRODUTOCREDITODESC";

	public static String FLTIPOCADASTRO_DESCONTO = "D";
	public static String FLTIPOCADASTRO_QTD = "Q";
	
    public String cdEmpresa;
    public String cdProduto;
    public String cdProdutoCreditoDesc;
    public Date dtVigenciaInicial;
    public double qtItem;
    public double vlUnitario;
    public String flTipoCadastroProduto;
    public Date dtVigenciaFinal;
    public String flInativacaoAuto;
    public Date dtCriacao;
    public String hrCriacao;
    public Date dtAlteracao;
    public String hrAlteracao;
    //--
    public String itemPedidoRowkey; 
    public String dsProduto; 

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ProdutoCreditoDesc) {
            ProdutoCreditoDesc produtoCreditoDesconto = (ProdutoCreditoDesc) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, produtoCreditoDesconto.cdEmpresa) && 
                ValueUtil.valueEquals(cdProduto, produtoCreditoDesconto.cdProduto) && 
                ValueUtil.valueEquals(cdProdutoCreditoDesc, produtoCreditoDesconto.cdProdutoCreditoDesc);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(cdProdutoCreditoDesc);
        return primaryKey.toString();
    }

    @Override
    public String getSortStringValue() {
    	return dsProduto;
    }
	
	public boolean isVigente() {
		Date dataAtual = DateUtil.getCurrentDate();
		return ValueUtil.isNotEmpty(dtVigenciaInicial) && ValueUtil.isNotEmpty(dtVigenciaFinal) && !dataAtual.isBefore(dtVigenciaInicial) && !dataAtual.isAfter(dtVigenciaFinal);
	}

}