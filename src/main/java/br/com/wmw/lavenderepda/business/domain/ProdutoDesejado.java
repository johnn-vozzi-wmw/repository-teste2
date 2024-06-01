package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class ProdutoDesejado extends LavendereBasePersonDomain {

	public static final String TABLE_NAME = "TBLVPPRODUTODESEJADO";
	public static final String NMCOLUNA_DSPRODUTODESEJADO = "DSPRODUTODESEJADO";
	public static final String ORIGEM_REGISTRO_PEDIDO = "P";

    public String cdEmpresa;
    public String cdRepresentante;
    public String nuPedido;
    public String flOrigemPedido;
    public String cdProdutoDesejado;
    public String cdUsuarioEmissao;
    public Date dtEmissaoPedido;
    public String flOrigemRegistro;
    public String cdConcorrente;
    public String dsOutroConcorrente;
    public String flOutroConcorrente;
    public String dsProdutoDesejado;
    
    //--
    public Date dtEmissaoPedidoFilter;
    
	public ProdutoDesejado() {
		super(TABLE_NAME);
	}

    public ProdutoDesejado(Builder builder) {
    	super(TABLE_NAME);
    	this.cdEmpresa = builder.cdEmpresa;
		this.cdRepresentante = builder.cdRepresentante;
		this.nuPedido = builder.nuPedido;
		this.flOrigemPedido = builder.flOrigemPedido;
		this.cdUsuarioEmissao = builder.cdUsuarioEmissao;
		this.dtEmissaoPedido = builder.dtEmissaoPedido;
    }

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ProdutoDesejado) {
            ProdutoDesejado produtoDesejado = (ProdutoDesejado) obj;
            return
            	ValueUtil.valueEquals(cdProdutoDesejado, produtoDesejado.cdProdutoDesejado) &&
            	ValueUtil.valueEquals(cdEmpresa, produtoDesejado.cdEmpresa) &&
            	ValueUtil.valueEquals(cdRepresentante, produtoDesejado.cdRepresentante) &&
            	ValueUtil.valueEquals(flOrigemPedido, produtoDesejado.flOrigemPedido) &&
                ValueUtil.valueEquals(nuPedido, produtoDesejado.nuPedido);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdProdutoDesejado)
        .append(";")
        .append(cdEmpresa)
        .append(";")
        .append(cdRepresentante)
        .append(";")
        .append(flOrigemPedido)
        .append(";")
        .append(nuPedido);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdProdutoDesejado;
	}

	@Override
	public String getDsDomain() {
		return dsProdutoDesejado;
	}
	
	public static class Builder {
		
		public String cdEmpresa;
	    public String cdRepresentante;
	    public String nuPedido;
	    public String flOrigemPedido;
	    public String cdUsuarioEmissao;
	    public Date dtEmissaoPedido;
	    
	    public Builder(Pedido pedido) {
	    	this(pedido.cdEmpresa, pedido.cdRepresentante, pedido.nuPedido, pedido.flOrigemPedido);
	    }
	    
	    public Builder(String cdEmpresa, String cdRepresentante) {
	    	this(cdEmpresa, cdRepresentante, null, null);
	    }
	    
	    public Builder(String cdEmpresa, String cdRepresentante, String nuPedido, String flOrigemPedido) {
	    	this.cdEmpresa = cdEmpresa;
			this.cdRepresentante = cdRepresentante;
			this.flOrigemPedido = flOrigemPedido;
			this.nuPedido = nuPedido;
	    }
	    
	    public Builder comCdUsuarioEmissao(String cdUsuario) {
	    	this.cdUsuarioEmissao = cdUsuario;
	    	return this;
	    }
	    
	    public Builder comDtEmissaoPedido(Date dtEmissao) {
	    	this.dtEmissaoPedido = dtEmissao;
	    	return this;
	    }
	    
	    public ProdutoDesejado build() {
	    	return new ProdutoDesejado(this);
	    }
		
	}

}