package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.CondicaoPagamentoService;
import totalcross.util.Vector;

public class CondicaoPagamentoComboBox extends BaseComboBox {

	public CondicaoPagamentoComboBox(String title) {
		super(title);
	}

	public String getValue() {
		CondicaoPagamento condicaoPagamento = (CondicaoPagamento) getSelectedItem();
		if (condicaoPagamento != null) {
			return condicaoPagamento.cdCondicaoPagamento;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		if (value != null) {
			CondicaoPagamento condicaoPagamento = new CondicaoPagamento();
			condicaoPagamento.cdEmpresa = SessionLavenderePda.cdEmpresa;
			condicaoPagamento.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondicaoPagamento.class);
			condicaoPagamento.cdCondicaoPagamento = value;
			select(condicaoPagamento);
		} else {
			setSelectedIndex(DefaultItemNull);
		}
	}

	public void loadCondicoesPagamento(Pedido pedido) throws SQLException {
		removeAll();
		Vector list;
		if (pedido.isPedidoAberto()) {
			list = CondicaoPagamentoService.getInstance().loadCondicoesPagamento(pedido, getOrderByColumn());
		} else {
			list = findCondicaoPagamentoByExample(pedido.cdEmpresa, pedido.cdCondicaoPagamento);
		}
		add(list);
	}

	public void loadCondicoesPagamento(String cdtabelaPreco, boolean flPagamentoAVista, double qtMinProduto) throws SQLException {
		removeAll();
		Vector list = CondicaoPagamentoService.getInstance().loadCondicoesPagamento(null, null, cdtabelaPreco, null, null, flPagamentoAVista, qtMinProduto, getOrderByColumn());
		add(list);
	}
	
	public void loadCondicoesPagamentoCombo() throws SQLException {
		removeAll();
    	Vector list = findCondicaoPagamentoByExample(SessionLavenderePda.cdEmpresa, null);
    	add(list);
	}

	private Vector findCondicaoPagamentoByExample(String cdEmpresa, String cdCondicaoPagamento) throws SQLException {
		CondicaoPagamento filter = new CondicaoPagamento();
		filter.cdEmpresa = cdEmpresa;
		filter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondicaoPagamento.class);
		filter.cdCondicaoPagamento = cdCondicaoPagamento;
		filter.sortAtributte = getOrderByColumn();
		filter.sortAsc = ValueUtil.VALOR_SIM;
		Vector list = CondicaoPagamentoService.getInstance().findAllByExample(filter);
		return list;
	}
	
	private String getOrderByColumn() {
		if (LavenderePdaConfig.isOrdenaCondPagtoPedidoPorDiasMedioPagtoOnCombo()) {
			return LavenderePdaConfig.isBloqueiaCondPagtoPorDiasMaximoCliente() ? CondicaoPagamento.NM_COLUNA_QTDIASMAXIMOPAGAMENTO : CondicaoPagamento.NM_COLUNA_QTDIASMEDIOSPAGAMENTO;
		} else {
			return CondicaoPagamento.NM_COLUNA_DSCONDICAOPAGAMENTO;
		}
	}
	
	public int getNuIntervaloParcelas() {
		CondicaoPagamento condicaoPagamento = (CondicaoPagamento) getSelectedItem();
		return condicaoPagamento != null ? condicaoPagamento.nuIntervaloParcelas : 0;
		
	}
	
	public int getNuIntervaloEntrada() {
		CondicaoPagamento condicaoPagamento = (CondicaoPagamento) getSelectedItem();
		return condicaoPagamento != null ? condicaoPagamento.nuIntervaloEntrada : 0;
	}
	
	public int getQtDiasMaximoPagamento() {
		CondicaoPagamento condicaoPagamento = (CondicaoPagamento) getSelectedItem();
		return condicaoPagamento != null ? condicaoPagamento.qtDiasMaximoPagamento : 0;
	}

}