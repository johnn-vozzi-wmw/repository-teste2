package br.com.wmw.lavenderepda.business.builder;

import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import totalcross.util.Vector;

public class PedidoBuilder {
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String nuPedido;
	public String flOrigemPedido;
	public Vector itemPedidoList;
	
	
	public PedidoBuilder(String cdEmpresa, String cdRepresentante, String nuPedido, String flOrigemPedido) {
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.nuPedido = nuPedido;
		this.flOrigemPedido = flOrigemPedido;
	}

	public PedidoBuilder(ItemPedido itemPedido) {
		this.cdEmpresa = itemPedido.cdEmpresa;
		this.cdRepresentante = itemPedido.cdRepresentante;
		this.nuPedido = itemPedido.nuPedido;
		this.flOrigemPedido = itemPedido.flOrigemPedido;
		this.itemPedidoList = new Vector();
		this.itemPedidoList.addElement(itemPedido);
	}

	public Pedido build() {
		return new Pedido(this);
	}
}
