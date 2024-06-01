package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.lavenderepda.Messages;
import totalcross.util.Vector;

public class RelProdutosRentabilidadeSemAlcadaException extends ValidationException {

	private static final long serialVersionUID = 1L;
	
	private Vector itemPedidoErroList;
	
	public RelProdutosRentabilidadeSemAlcadaException(Vector itemPedidoErroList) {
		super(Messages.MARGEMRENTABFAIXA_REL_ITENS_SEM_RENTABILIDADE_ALCADA);
		this.itemPedidoErroList = itemPedidoErroList;
	}
	
	public Vector getItemPedidoErroList() {
		return itemPedidoErroList;
	}
	

	
}
