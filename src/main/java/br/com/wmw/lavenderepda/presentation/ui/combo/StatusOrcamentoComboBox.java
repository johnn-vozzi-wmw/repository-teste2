package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.StatusOrcamento;
import br.com.wmw.lavenderepda.business.service.StatusOrcamentoService;
import totalcross.util.Vector;

public class StatusOrcamentoComboBox extends BaseComboBox {
	
	public StatusOrcamentoComboBox(boolean onList) throws SQLException {
		super(Messages.STATUSORCAMENTO_LABEL_COMBO);
		defaultItemType = onList ? DefaultItemType_ALL : DefaultItemType_NONE;
	}
	
	public void setValue(String value) {
		if (ValueUtil.isNotEmpty(value)) {
			StatusOrcamento status = new StatusOrcamento();
			status.cdEmpresa = SessionLavenderePda.cdEmpresa;
			status.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(StatusOrcamentoComboBox.class);
			status.cdStatusOrcamento = value;
			select(status);
		} else {
			setSelectedIndex(BaseComboBox.DefaultItemNull);
		}
	}
	
	public String getValue() {
		StatusOrcamento status = (StatusOrcamento)getSelectedItem();
		if (status != null) {
			return status.cdStatusOrcamento;
		}
		return null;
	}
	
	public StatusOrcamento getStatusOrcamento() {
		return (StatusOrcamento)getSelectedItem();
	}
	
	public void load() throws SQLException {
		load(null);
	}
	
	public void load(Pedido pedido) throws SQLException {
		removeAll();
		StatusOrcamento statusFilter = new StatusOrcamento();
		statusFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		statusFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(StatusOrcamentoComboBox.class);
		statusFilter.notFlStatusPreOrcamento = getNotFlStatusPreOrcamentoFilter(pedido);
		Vector list = StatusOrcamentoService.getInstance().findAllByExample(statusFilter);
		add(list);
		setSelectedIndex(0);
	}
	
	private String getNotFlStatusPreOrcamentoFilter(Pedido pedido) {
		return pedido == null || (LavenderePdaConfig.usaNovoPedidoOrcamentoSemRegistroChegada 
				&& pedido.statusOrcamento != null && pedido.statusOrcamento.isStatusPreOrcamento()) ? null : ValueUtil.VALOR_SIM;
	}
}
