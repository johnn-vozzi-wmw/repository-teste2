package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.CondicaoPagamentoService;

public class RelCotaCondPagto extends WmwWindow {
	
	private LabelValue lbCotaQtdItens, lbCotaVlPedido, lbCotaQtdItensCondPagto, lbCotaVlPedidoCondPagto, lbIndiceFinanceiro;
	
	public RelCotaCondPagto(Pedido pedido) throws SQLException {
		super(Messages.COTAS_CONDICAO_PAGAMENTO);
		CondicaoPagamento condicaoPagamento = CondicaoPagamentoService.getInstance().getCondicaoPagamento(pedido.cdCondicaoPagamento);
		lbCotaQtdItens = new LabelValue(pedido.itemPedidoList.size());
		lbCotaVlPedido = new LabelValue(Messages.PEDIDO_RELACIONADO_REAIS + StringUtil.getStringValueToInterface(pedido.vlTotalPedido));
		lbCotaQtdItensCondPagto = new LabelValue(condicaoPagamento.qtMixCotaItens);
		lbCotaVlPedidoCondPagto = new LabelValue(Messages.PEDIDO_RELACIONADO_REAIS + StringUtil.getStringValueToInterface(condicaoPagamento.vlCotaItens));
		lbIndiceFinanceiro = new LabelValue(StringUtil.getStringValueToInterface(ValueUtil.round((condicaoPagamento.vlIndiceFinanceiro - 1) * 100)) + "%");
	}
	
	@Override
	public void initUI() {
		UiUtil.add(this, new LabelName(Messages.LABEL_COTA_INDICE_FINANCEIRO), LEFT + WIDTH_GAP_BIG, TOP + HEIGHT_GAP_BIG);
		UiUtil.add(this, lbIndiceFinanceiro, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(this, new LabelName(Messages.LABEL_COTA_QTD_ITENS_COND_PAGTO), SAME, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(this, lbCotaQtdItensCondPagto, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(this, new LabelName(Messages.LABEL_COTA_QTD_ITENS_PEDIDO_ATUAL), SAME, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(this, lbCotaQtdItens, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(this, new LabelName(Messages.LABEL_COTA_VL_PEDIDO_COND_PAGTO), SAME, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(this, lbCotaVlPedidoCondPagto, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(this, new LabelName(Messages.LABEL_COTA_VL_PEDIDO_ATUAL), SAME, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(this, lbCotaVlPedido, SAME, AFTER + HEIGHT_GAP);
		super.initUI();
	}
	
	@Override
	public void popup() {
		setDefaultRect();
		super.popup();
	}

}
