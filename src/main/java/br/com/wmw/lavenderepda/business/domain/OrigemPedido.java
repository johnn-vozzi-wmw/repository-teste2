package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;

public class OrigemPedido extends BaseDomain {

	public static final String FLORIGEMPEDIDO_PDA = "P";
	public static final String FLORIGEMPEDIDO_WEB = "W";
	public static final String FLORIGEMPEDIDO_ERP = "E";
	public static final String FLORIGEMPEDIDO_PORTAL = "B";

	public String flOrigemPedido;
	public String dsOrigemPedido;

	//@Override
	public String getPrimaryKey() {
		return StringUtil.getStringValue(flOrigemPedido);
	}

	public static String getDsOrigemPedidoMessage(String flOrigemPedido) {
		if (!ValueUtil.isEmpty(flOrigemPedido)) {
			if (FLORIGEMPEDIDO_PDA.equals(flOrigemPedido)) {
				return Messages.ORIGEMPEDIDO_PDA;
			} else if (FLORIGEMPEDIDO_WEB.equals(flOrigemPedido)) {
				return Messages.ORIGEMPEDIDO_WEB;
			} else if (FLORIGEMPEDIDO_ERP.equals(flOrigemPedido)) {
				return Messages.ORIGEMPEDIDO_ERP;
			} else if (FLORIGEMPEDIDO_PORTAL.equals(flOrigemPedido)) {
				return Messages.ORIGEMPEDIDO_PORTAL;
			} else {
				return Messages.ORIGEMPEDIDO_DESCONHECIDO;
			}
		} else {
			return ValueUtil.VALOR_NI;
		}
	}
}
