package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TipoFrete;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoFreteComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TransportadoraComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadFreteManualForm extends WmwWindow {
	
	
	private Pedido pedido;
	private TransportadoraComboBox cbTransportadora;
	private TipoFreteComboBox cbTipoFrete;
	private EditNumberFrac edVlFrete;
	private ButtonPopup btSalvar;
	private ButtonPopup btFretes;
	private boolean fromListFrete;
	public boolean adicionadoFreteManual;
	
	public CadFreteManualForm(Pedido pedido) throws SQLException {
		this(pedido, false);
	}
	
	public CadFreteManualForm(Pedido pedido, boolean fromListFrete) throws SQLException {
		super(Messages.FRETE_PERSONALIZADO_MANUAL);
		this.pedido = pedido;
		this.fromListFrete = fromListFrete;
		instantiateComponents();
		populateFields();
		setDefaultRect();
	}
	
	private void populateFields() throws SQLException {
		cbTransportadora.setValue(pedido.cdTransportadora);
		cbTipoFrete.setValue(pedido.cdTipoFrete, pedido.getCliente() != null ? pedido.getCliente().cdEstadoComercial : TipoFrete.CD_ESTADO_PADRAO);
		edVlFrete.setValue(pedido.vlFrete);
	}

	private void instantiateComponents() throws SQLException {
		cbTransportadora = new TransportadoraComboBox();
		cbTipoFrete = new TipoFreteComboBox();
		cbTransportadora.carregaTransportadoras(pedido, false);
		cbTipoFrete.loadTipoFrete(pedido);	
		edVlFrete = new EditNumberFrac("9999999", 9);
		btSalvar = new ButtonPopup(Messages.LABEL_BT_SALVAR);
		btFretes = new ButtonPopup(Messages.FRETE_NOME_ENTIDADE);
	}
	
	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, new LabelName(Messages.TRANSPORTADORA), cbTransportadora, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.TIPOFRETE_LABEL_TIPOFRETE), cbTipoFrete , getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.FRETE_VALOR_FRETE), edVlFrete , getLeft(), getNextY());
		if (!fromListFrete) {
			UiUtil.add(cFundoFooter, btFretes);
		} else {
			btFechar.setText(FrameworkMessages.BOTAO_VOLTAR);;
		}
		UiUtil.add(cFundoFooter, btSalvar);
		ajustaTamanhoBotoes();
	}
	
	@Override
	public void onEvent(Event event) {
		super.onEvent(event);
		switch(event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btSalvar) {
					btSalvarClick();
				}
				if (event.target == btFechar) {
					adicionadoFreteManual = false;
				}
				if (event.target == btFretes) {
					btFretesClick();
				}
			}
		}
	}

	private void btFretesClick() {
		try {
			adicionadoFreteManual = exibirPopupFretePersonalizado(pedido);
			if (adicionadoFreteManual) unpop();
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
			adicionadoFreteManual = false;
		}
	}

	private void btSalvarClick() {
		try {
			validateFields();
			setValuesToDomain();
			unpop();
		} catch (ValidationException e) {
			UiUtil.showErrorMessage(e.getMessage());
			ExceptionUtil.handle(e);
		}
	}

	private void setValuesToDomain() {
		pedido.cdTransportadora = cbTransportadora.getValue();
		pedido.cdTipoFrete = cbTipoFrete.getValue();
		pedido.vlFrete = edVlFrete.getValueDouble();
		pedido.cdFreteConfig = null;
		pedido.freteConfig = null;
		adicionadoFreteManual = true;
	}

	private void validateFields() {
		if (ValueUtil.isEmpty(cbTransportadora.getValue()) || ValueUtil.isEmpty(cbTipoFrete.getValue()) || edVlFrete.getValueDouble() == 0) {
			throw new ValidationException("Todos os campos são obrigatórios, favor revisar.");
		}
	}
	
	private boolean exibirPopupFretePersonalizado(Pedido pedido) throws SQLException {
		TipoPedido tipoPedido = pedido.getTipoPedido();
		if (tipoPedido != null && tipoPedido.isIgnoraCalculoFrete()) return true;
		ListFreteWindow listFreteWindow = new ListFreteWindow(pedido, true);
		listFreteWindow.popup();
		return listFreteWindow.freteAplicado;
	}

}
