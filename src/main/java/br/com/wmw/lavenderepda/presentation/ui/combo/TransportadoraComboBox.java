package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TranspTipoPed;
import br.com.wmw.lavenderepda.business.domain.Transportadora;
import br.com.wmw.lavenderepda.business.service.TransportadoraService;

public class TransportadoraComboBox extends BaseComboBox {

	public TransportadoraComboBox() {
		super(LavenderePdaConfig.usaTransportadoraPedido() ? Messages.TRANSPORTADORA : "");
	}

	public String getValue() {
		Transportadora transportadora = (Transportadora) getSelectedItem();
		if (transportadora != null) {
			return transportadora.cdTransportadora;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		if (value != null) {
			Transportadora transportadora = new Transportadora();
			transportadora.cdEmpresa = SessionLavenderePda.cdEmpresa;
			transportadora.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Transportadora.class);
			transportadora.cdTransportadora = value;
			//--
			select(transportadora);
		} else {
			setSelectedIndex(BaseComboBox.DefaultItemNull);
		}
	}

	public void carregaTransportadoras(Pedido pedido, boolean isFreteConfig) throws SQLException {
		removeAll();
		Cliente cliente = pedido.getCliente();
		Transportadora transportadora = new Transportadora();
		transportadora.cdEmpresa = SessionLavenderePda.cdEmpresa;
		transportadora.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Transportadora.class);
		transportadora.cdRegiao = cliente.cdRegiao;
		transportadora.dsCepTratado = ValueUtil.getIntegerValue(StringUtil.getStringValue(cliente.dsCepComercial).replace("-", ""));
		transportadora.consideraFreteConfig = isFreteConfig;
		transportadora.sortAtributte = "NMTRANSPORTADORA";
		transportadora.sortAsc = ValueUtil.VALOR_SIM;
		prepareTranspTipoPedFilter(pedido, transportadora);
		add(TransportadoraService.getInstance().findAllByExample(transportadora));
	}

	protected void prepareTranspTipoPedFilter(Pedido pedido, Transportadora transportadora) {
		if (ValueUtil.isNotEmpty(pedido.cdTipoPedido)) {
			transportadora.transpTipoPedFilter = new TranspTipoPed();
			transportadora.transpTipoPedFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			transportadora.transpTipoPedFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TranspTipoPed.class);
			transportadora.transpTipoPedFilter.cdTipoPedido = pedido.cdTipoPedido;
		}
	}

}
