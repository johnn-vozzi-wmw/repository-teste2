package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Entrega;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.EntregaService;

public class EntregaComboBox extends BaseComboBox {

	public EntregaComboBox() throws SQLException {
		super(Messages.LABEL_ENTREGA);
	}

	public String getValue() {
		Entrega entrega = (Entrega)getSelectedItem();
		if (entrega != null) {
			return entrega.cdEntrega;
		} else {
			return null;
		}
	}
	
	public void setValue(String value) {
		if (value != null) {
			Entrega entrega = new Entrega();
			entrega.cdEmpresa = SessionLavenderePda.cdEmpresa;
			entrega.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Entrega.class);
			entrega.cdEntrega = value;
			//--
			select(entrega);
		} else {
			setSelectedIndex(BaseComboBox.DefaultItemNull);
		}
	}

	public void load(Pedido pedido) throws java.sql.SQLException {
		removeAll();
		Entrega entregaFilter = new Entrega();
		entregaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		entregaFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		entregaFilter.cdTabelaPreco = pedido.cdTabelaPreco;
		entregaFilter.qtMinProduto = pedido.getQtItensLista();
		entregaFilter.vlMinPedido = pedido.vlTotalPedido;
		entregaFilter.ignoraValidacao = LavenderePdaConfig.usaBotaoIgnorarValidacoesPedido && pedido.isPendente();
		add(EntregaService.getInstance().findAllByExample(entregaFilter));
	}
	
	public void addEntregaByPedido(Pedido pedido) throws SQLException {
		Entrega entregaFilter = new Entrega();
		entregaFilter.cdEmpresa = pedido.cdEmpresa;
		entregaFilter.cdRepresentante = pedido.cdRepresentante;
		entregaFilter.cdEntrega = pedido.cdEntrega;
		if (indexOf(entregaFilter) == -1) {
			add(EntregaService.getInstance().findByPrimaryKey(entregaFilter));
		}
	}

}
