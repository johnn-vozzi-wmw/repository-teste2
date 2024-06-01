package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class ValorizacaoProd extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPVALORIZACAOPROD";

	public static final String STATUS_ENVIADO = "E";
	public static final String STATUS_NAO_ENVIADO = "N";
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    public String cdProdutoValorizacao;
    public double qtItem;
    public double vlUnitario;
    public double vlTotalItem;
    public String dsObservacao;
    public Date dtValorizacao;
    public String hrValorizacao;
    public Date dtAlteracao;
    public String hrAlteracao;
    
    //Nao persiste
    public Date dtValorizacaoFiltroInic;
    public Date dtValorizacaoFiltroFim;
    public String dsProduto;

    public String dsPrincipioAtivo;
    public String nuCodigoBarras;
    public String cdProdutoLikeFilter;
    public String dsMarca;
    public String dsReferencia;
    public String dsReferenciaLikeFilter;
    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ValorizacaoProd) {
            ValorizacaoProd valorizacaoProd = (ValorizacaoProd) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, valorizacaoProd.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, valorizacaoProd.cdRepresentante) && 
                ValueUtil.valueEquals(cdProduto, valorizacaoProd.cdProduto) && 
                ValueUtil.valueEquals(cdProdutoValorizacao, valorizacaoProd.cdProdutoValorizacao);
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
        primaryKey.append(cdProdutoValorizacao);
        return primaryKey.toString();
    }

}