package br.com.wmw.lavenderepda.business.builder;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoConsignacao;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;

public class ItemPedidoBuilder {
	
	// Parametros Obrigatórios
    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemPedido;
    public String nuPedido;
    public String cdProduto;
    public String flTipoItemPedido;
    public int nuSeqProduto;
	public TabelaPreco tabelaPreco;
	public ItemTabelaPreco itemTabelaPreco;
	public String cdTabelaPreco;
    
    public Pedido pedido;
    
    
	public ItemPedidoBuilder(String cdEmpresa, String cdRepresentante, String cdProduto, String flOrigemPedido) {
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdProduto = cdProduto;
		this.flOrigemPedido = flOrigemPedido;
	}
	
	public ItemPedidoBuilder(final Pedido pedido) {
		this.pedido = pedido;
		this.cdEmpresa = pedido.cdEmpresa;
		this.cdRepresentante = pedido.cdRepresentante;
		this.flOrigemPedido = pedido.flOrigemPedido;
		this.nuPedido = pedido.nuPedido;
	}
	
	public ItemPedidoBuilder(final PedidoConsignacao pedidoConsignacao) {
		if (pedidoConsignacao != null) {
			cdEmpresa = pedidoConsignacao.cdEmpresa;
			cdRepresentante = pedidoConsignacao.cdRepresentante;
			nuPedido = pedidoConsignacao.nuPedido;
			flOrigemPedido = pedidoConsignacao.flOrigemPedido;
			cdProduto = pedidoConsignacao.cdProduto;
			flTipoItemPedido = pedidoConsignacao.flTipoItemPedido;
			nuSeqProduto = pedidoConsignacao.nuSeqProduto;
		}
	}
    
    public ItemPedidoBuilder(String cdEmpresa, String cdRepresentante, String cdProduto) {
    	this(cdEmpresa, cdRepresentante, cdProduto, "P");
    }
	
	public ItemPedidoBuilder(Produto produto) {
		this(produto.cdEmpresa, produto.cdRepresentante, produto.cdProduto);
	}

	public ItemPedidoBuilder comPedido(Pedido newPedido) {
		this.pedido = newPedido;
		this.nuPedido = newPedido.nuPedido;
		return this;
	}

	public ItemPedidoBuilder comTabelaPreco() {
		tabelaPreco = new TabelaPreco();
		itemTabelaPreco = new ItemTabelaPreco();
		return this;
	}
	
	public ItemPedido build() {
		return new ItemPedido(this);
	}

}
