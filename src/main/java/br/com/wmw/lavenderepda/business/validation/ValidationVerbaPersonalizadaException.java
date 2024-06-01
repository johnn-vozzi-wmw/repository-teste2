package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Verba;

public class ValidationVerbaPersonalizadaException extends ValidationException {

	private static final long serialVersionUID = 1L;
	private ItemPedido itemPedidoNegativo;
	
	public ValidationVerbaPersonalizadaException(String areaException, double vlVerbaExtrapolada,  final String dsProduto, ItemPedido itemPedido) {
		super(getMessageByArea(areaException, vlVerbaExtrapolada, dsProduto));
		this.setItemPedidoNegativo(itemPedido);
	}
	
	private static String getMessageByArea(String areaException, double vlVerbaExtrapolada, String dsProduto) {
		String[] params = new String[3];
		switch (areaException) {
		case Verba.VERBA_GRUPO_PRODUTO:
			params = new String[] {Messages.VERBA_GRUPO_PRODUTO_MESSAGE_EXCEPTION, StringUtil.getStringValue(vlVerbaExtrapolada), dsProduto};
			break;
		case Verba.VERBA_NAO_CONSUMIDA:
			params = new String[] {dsProduto};
			break;
		default:
			break;
		}
		return MessageUtil.getMessage(params.length == 3 ? Messages.VERBASALDO_PERSONALIZADO_MSG_SALDO_INDISPONIVEL : Messages.VERBASALDO_PERSONALIZADO_MSG_SEM_VERBA_DISPONIVEL, params);
	}
	
	@Override
	public String toString() {
		return "VerbaPersonalizadaException";
	}

	public ItemPedido getItemPedidoNegativo() {
		return itemPedidoNegativo;
	}

	public void setItemPedidoNegativo(ItemPedido itemPedidoNegativo) {
		this.itemPedidoNegativo = itemPedidoNegativo;
	}

}
