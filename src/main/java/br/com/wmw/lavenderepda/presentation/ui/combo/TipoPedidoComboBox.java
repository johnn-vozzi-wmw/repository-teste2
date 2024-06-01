package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.service.TipoPedidoService;
import totalcross.util.Vector;

public class TipoPedidoComboBox extends BaseComboBox {

	public TipoPedidoComboBox(boolean emptyComboBox, boolean resumoDia) throws SQLException {
		super(Messages.TIPOPEDIDO_LABEL_TIPOPEDIDO);
		if (resumoDia) {
			this.defaultItemType = BaseComboBox.DefaultItemType_ALL;
		}
		if (!emptyComboBox) {
			carregaTipoPedidos(null);
		}
	}

	public String getValue() {
		TipoPedido tipoPedido = (TipoPedido) getSelectedItem();
		if (tipoPedido != null) {
			return tipoPedido.cdTipoPedido;
		} else {
			return "";
		}
	}
	
	public TipoPedido getTipoPedido() {
		return (TipoPedido) getSelectedItem();
	}

	public void setValue(String value) {
		if (value != null) {
			TipoPedido tipoPedido = new TipoPedido();
			tipoPedido.cdEmpresa = SessionLavenderePda.cdEmpresa;
			tipoPedido.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TipoPedido.class);
			tipoPedido.cdTipoPedido = value;
			//--
			select(tipoPedido);
		} else {
			setSelectedIndex(BaseComboBox.DefaultItemNull);
		}
	}

	public void carregaTipoPedidos(Pedido pedido) throws SQLException {
		removeAll();
		TipoPedido tipoPedido = new TipoPedido();
		tipoPedido.cdEmpresa = SessionLavenderePda.cdEmpresa;
		tipoPedido.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TipoPedido.class);
		if (LavenderePdaConfig.isUsaPoliticaBonificacao() && (pedido == null || pedido.isPedidoAberto())) {
			tipoPedido.flBonificacaoContaCorrenteDif = ValueUtil.VALOR_SIM;
		}
		Vector list = TipoPedidoService.getInstance().getTipoPedidoList(tipoPedido);
		if (!ValueUtil.isEmpty(list)) {
			list.qsort();
			add(list);
		}
	}

}
