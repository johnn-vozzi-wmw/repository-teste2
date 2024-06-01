package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;

public class TipoItemPedido extends BaseDomain {

	public static final String TIPOITEMPEDIDO_NORMAL = "V";
	public static final String TIPOITEMPEDIDO_TROCA_REC = "R";
	public static final String TIPOITEMPEDIDO_TROCA_ENT = "T";
	public static final String TIPOITEMPEDIDO_BONIFICACAO = "B";
	public static final String TIPOITEMPEDIDO_OPORTUNIDADE = "O";

	public String flTipoItemPedido;
	public String dsTipoItemPedido;

	//@Override
	public String getPrimaryKey() {
		return StringUtil.getStringValue(flTipoItemPedido);
	}

	public static String getDsTipoItemPedidoMessage(String flTipoItemPedido) {
		if (!ValueUtil.isEmpty(flTipoItemPedido)) {
			if (TIPOITEMPEDIDO_NORMAL.equals(flTipoItemPedido)) {
				return Messages.TIPOITEMPEDIDO_NORMAL;
			} else if (TIPOITEMPEDIDO_TROCA_REC.equals(flTipoItemPedido)) {
				return Messages.TIPOITEMPEDIDO_TROCA_REC;
			} else if (TIPOITEMPEDIDO_TROCA_ENT.equals(flTipoItemPedido)) {
				return Messages.TIPOITEMPEDIDO_TROCA_ENT;
			} else if (TIPOITEMPEDIDO_BONIFICACAO.equals(flTipoItemPedido)) {
				return Messages.TIPOITEMPEDIDO_BONIFICACAO;
			} else {
				return Messages.TIPOITEMPEDIDO_DESCONHECIDO;
			}
		} else {
			return Messages.TIPOITEMPEDIDO_NULLO;
		}
	}
}
