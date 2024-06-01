package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.presentation.ui.combo.RedeComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Date;

public class FiltroAvancadoPedidoRelacionadoWindow extends WmwWindow  {

	private static final String DTEMISSAOFILTER = "Emissao";
	private static final String DTENTREGAFILTER = "Entrega";

	public BaseComboBox cbDtEmissaoEntrega;
	public RedeComboBox cbRede;
	public EditText edDsCliente;
	public BaseButton btFiltrarCliente;
	public BaseButton btTodosClientes;
	public EditDate edDateInitial;
	public EditDate edDateFinal;
	public ButtonPopup btLimpar;
	public ButtonPopup btFiltrar;
	public String cdClienteFilter;
	public boolean filtrando;
	public String dsClienteFilter;

	public FiltroAvancadoPedidoRelacionadoWindow() throws SQLException {
		super(Messages.PRODUTO_FILTRO_AVANCADO);
		cbRede = new RedeComboBox();
		edDsCliente = new EditText("@@@@@@@@@@", 100);
		edDsCliente.drawBackgroundWhenDisabled = true;
		edDsCliente.setEditable(false);
		edDsCliente.setText(Messages.LABEL_TODOS);
		btFiltrarCliente = new BaseButton(FrameworkMessages.BOTAO_DETALHES_RESUMIDO);
		btTodosClientes = new BaseButton(Messages.BOTAO_TODOS);
		edDateInitial = new EditDate();
		edDateFinal = new EditDate();
		btLimpar = new ButtonPopup(FrameworkMessages.BOTAO_LIMPAR);
		btFiltrar = new ButtonPopup(FrameworkMessages.BOTAO_FILTRAR);
		if (LavenderePdaConfig.usaFiltroSelecaoTipoDataListaPedidos()) {
			cbDtEmissaoEntrega = new BaseComboBox();
			cbDtEmissaoEntrega.add(DTEMISSAOFILTER);
			cbDtEmissaoEntrega.add(DTENTREGAFILTER);
			cbDtEmissaoEntrega.setSelectedIndex(0);
		}
		setDefaultRect();
	}

	@Override
	public void initUI() {
		super.initUI();
		if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema()) {
			UiUtil.add(this, new LabelName(Messages.REDE_NOME_ENTIDADE), cbRede, getLeft(), getNextY());
		}
		UiUtil.add(this, new LabelName(Messages.CLIENTE_NOME_ENTIDADE), getLeft(), getNextY(), FILL - HEIGHT_GAP, UiUtil.getLabelPreferredHeight());
		UiUtil.add(this, edDsCliente, getLeft(), AFTER, FILL - btFiltrarCliente.getPreferredWidth() - btTodosClientes.getPreferredWidth() - (WIDTH_GAP * 2) - WIDTH_GAP_BIG);
		UiUtil.add(this, btTodosClientes, RIGHT - WIDTH_GAP_BIG, SAME, PREFERRED, SAME);
		UiUtil.add(this, btFiltrarCliente, BEFORE - WIDTH_GAP, SAME, PREFERRED, SAME);
		UiUtil.add(this, new LabelName(Messages.PEDIDO_LABEL_DTEMISSAO), getLeft(), AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
		if (LavenderePdaConfig.usaFiltroSelecaoTipoDataListaPedidos()) {
			UiUtil.add(this, cbDtEmissaoEntrega, getLeft(), AFTER , getWFill());
		}
		UiUtil.add(this, edDateInitial, getLeft(), AFTER + (LavenderePdaConfig.usaFiltroSelecaoTipoDataListaPedidos() ? HEIGHT_GAP : 0));
		UiUtil.add(this, edDateFinal, AFTER + WIDTH_GAP_BIG , SAME);
		addButtonPopup(btFiltrar);
		addButtonPopup(btLimpar);
		addButtonPopup(btFechar);

	}

	@Override
	public void onWindowEvent(Event event) throws SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == btLimpar) {
				btLimparFiltrosClick();
			} else if (event.target == btFiltrarCliente) {
				btFiltroClienteClick();
			} else if (event.target == btTodosClientes) {
				btTodosClientesClick();
			} else if (event.target == btFiltrar) {
				validateFilterData(edDateInitial.getValue(), edDateFinal.getValue());
				filtrando = true;
				fecharWindow();
			}
			break;
		}
	}

	public void validateFilterData(Date dateInitial, Date dateFinal) {
		if (!ValueUtil.isEmpty(dateInitial) && !ValueUtil.isEmpty(dateFinal)) {
			if (dateInitial.isAfter(dateFinal)) {
				String[] param = {StringUtil.getStringValue(dateFinal), StringUtil.getStringValue(dateInitial)};
				throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_DATA_INICIAL_MAIOR, param));
			}
		}
	}

	private void btLimparFiltrosClick() {
		if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema()) {
			cbRede.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.usaFiltroSelecaoTipoDataListaPedidos()) {
			cbDtEmissaoEntrega.setSelectedIndex(0);
		}
		cdClienteFilter = null;
		edDateFinal.setValue(null);
		edDateInitial.setValue(null);
		edDsCliente.setText(Messages.LABEL_TODOS);
	}

	private void btFiltroClienteClick() throws SQLException {
		Representante representanteOld = SessionLavenderePda.getRepresentante();
		ListClienteWindow listCliente = new ListClienteWindow();
		listCliente.popup();
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			if ((SessionLavenderePda.getRepresentante().cdRepresentante != null) && (listCliente.cliente == null)) {
				SessionLavenderePda.setRepresentante(representanteOld);
			}
		}
		if (listCliente.cliente != null) {
			edDsCliente.setText(listCliente.cliente.getDescription());
			cdClienteFilter = listCliente.cliente.cdCliente;
			dsClienteFilter = listCliente.cliente.nmRazaoSocial;
		}
	}

	private void btTodosClientesClick() {
		edDsCliente.setText(Messages.LABEL_TODOS);
		cdClienteFilter = "";
	}

}
