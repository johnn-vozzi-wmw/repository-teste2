package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TipoPagamento;
import br.com.wmw.lavenderepda.business.service.TipoPagamentoService;
import totalcross.util.Vector;

public class TipoPagamentoComboBox extends BaseComboBox {

	public TipoPagamentoComboBox(boolean comboFichaFinanceira) {
		super(Messages.TIPOPAGTO_LABEL_TIPOPAGTO);
		if (LavenderePdaConfig.usaFiltroTituloFinanceiroPorTipoPagamento && comboFichaFinanceira) {
			defaultItemType = DefaultItemType_ALL;
		}
	}

	public TipoPagamentoComboBox() {
		super(Messages.TIPOPAGTO_LABEL_TIPOPAGTO);
	}

	public String getValue() {
		TipoPagamento tipoPagamento = (TipoPagamento)getSelectedItem();
		if (tipoPagamento != null) {
			return tipoPagamento.cdTipoPagamento;
		} else {
			return null;
		}
	}
	
	public TipoPagamento getTipoPagamento() {
		return (TipoPagamento)getSelectedItem();
	}

	public void setValue(String value) {
		if (value != null) {
			TipoPagamento tipoPagamento = new TipoPagamento();
			tipoPagamento.cdEmpresa = SessionLavenderePda.cdEmpresa;
			tipoPagamento.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TipoPagamento.class);
			tipoPagamento.cdTipoPagamento = value;
			//--
			select(tipoPagamento);
		} else {
			setSelectedIndex(-1);
		}
	}
	
	public void carregaTipoPagamentosCliente(Cliente cliente, String cdCondicaoPagamento) throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdCliente = cliente.cdCliente;
		pedidoFilter.setCliente(cliente);
		pedidoFilter.cdCondicaoPagamento = cdCondicaoPagamento;
		carregaTipoPagamentos(pedidoFilter, true);
	}
	
	public void carregaTipoPagamentos(Pedido pedido, boolean telaPagamento) throws SQLException {
		removeAll();
		//--
		if ((LavenderePdaConfig.usaTipoPagamentoPorCondicaoPagamento() || LavenderePdaConfig.usaTipoPagtoPorCondPagtoECondPagtoPorCliente()) && !LavenderePdaConfig.usaCondicaoPagamentoPorTipoPagamento() && ValueUtil.isEmpty(pedido.cdCondicaoPagamento)) {
			return;
		}
		Vector list = TipoPagamentoService.getInstance().getTipoPagamentoList(pedido, telaPagamento);
		//--
		if (ValueUtil.isNotEmpty(list)) {
			add(list);
			qsort();
		}
	}
	
	public boolean isCheque() {
		TipoPagamento tipoPagamento = (TipoPagamento)getSelectedItem();
		return tipoPagamento != null && tipoPagamento.isCheque();
	}
	
	public boolean isUsaVencimento() {
		TipoPagamento tipoPagamento = (TipoPagamento)getSelectedItem();
		return tipoPagamento != null && tipoPagamento.isUsaVencimento();
	}

	public void carregaTipoPagamentos() throws SQLException {
		removeAll();
		TipoPagamento tipoPagamentoFilter = new TipoPagamento();
		tipoPagamentoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		tipoPagamentoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TipoPagamento.class);
		Vector list = TipoPagamentoService.getInstance().findAllByExample(tipoPagamentoFilter);
		if (ValueUtil.isNotEmpty(list)) {
			add(list);
		}
	}

}
