package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Recado;
import br.com.wmw.lavenderepda.business.service.RecadoService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class CadMotivoClienteAtrasadoWindow extends WmwWindow {


	public EditMemo edMotivoAtraso;
	public EditMemo edMessage;
	public ButtonPopup btNovoPedido;
	public ButtonPopup btCancelar;
	public ButtonPopup btVerTitulos;
	public ButtonPopup btVerRede;
	public int options;
	public String message;
	public boolean fechaPopUp = false;
	
	public boolean isSomenteRedeAtrasado;
	public int qtClientesRedeAtraso;
	
	public CadMotivoClienteAtrasadoWindow(String message) {
		this(message, false, 0);
	}

	public CadMotivoClienteAtrasadoWindow(String message, final boolean isSomenteRedeAtrasado, final int qtClientesRedeAtraso) {
		super(Messages.CLIENTE_ATRASADO_LABEL);
		this.message = message;
		this.isSomenteRedeAtrasado = isSomenteRedeAtrasado;
		this.qtClientesRedeAtraso = qtClientesRedeAtraso;
		edMessage  = new EditMemo("@@@@@@@@@@", 7, 255);
		edMessage.setText(message);
		edMessage.setEditable(false);
		edMessage.transparentBackground = true;
		edMessage.drawDots = false;
		edMotivoAtraso = new EditMemo("@@@", 3, 4000);
		btNovoPedido = new ButtonPopup(Messages.CLIENTE_ATRASADO_BT_NOVO_PED_RESUMIDO);
		btCancelar = new ButtonPopup(FrameworkMessages.BOTAO_CANCELAR);
		btVerTitulos = new ButtonPopup(Messages.BOTAO_VER_TITULOS);
		if (showBotaoVerRede()) {
			btVerRede = new ButtonPopup(Messages.BOTAO_VER_REDE);
		}
		btFechar.setVisible(false);
		int windowHeight = titleFont.fm.height + footerH +  edMessage.getPreferredHeight() + HEIGHT_GAP_BIG * 2;
		if (LavenderePdaConfig.isUsaMotivoAtrasoClienteAtrasado()) {
			windowHeight += edMotivoAtraso.getPreferredHeight() + UiUtil.getLabelPreferredHeight() + HEIGHT_GAP;
		}
		setRect(LEFT + WIDTH_GAP_BIG, CENTER, FILL - WIDTH_GAP_BIG, windowHeight);
	}

	public void initUI() {
		super.initUI();
		UiUtil.add(this, edMessage, getLeft(), getTop());
		//--
		if (LavenderePdaConfig.isUsaMotivoAtrasoClienteAtrasado()) {
			UiUtil.add(this, new LabelName(Messages.CLIENTE_ATRASADO_MOTIVO_ATRASO), edMotivoAtraso, getLeft(), AFTER + HEIGHT_GAP);
		}
		addButtonPopup(btNovoPedido);
		if (showBotaoVerRede()) {
			addButtonPopup(btVerRede);
		}
		if (!isSomenteRedeAtrasado) {
			addButtonPopup(btVerTitulos);
		}
		addButtonPopup(btCancelar);
	}


	public void fecharWindow() throws SQLException {
		if (LavenderePdaConfig.isUsaMotivoAtrasoClienteAtrasado()) {
			fecharPopUp();
			if (fechaPopUp) {
				super.fecharWindow();
			}
		} else {
			options = 2;
			super.fecharWindow();
		}
	}


	private void gravaRecado() throws SQLException {
		if (ValueUtil.isNotEmpty(LavenderePdaConfig.usaMotivoAtrasoClienteAtrasado)) {
			Vector destinatarios = new Vector(StringUtil.split(LavenderePdaConfig.usaMotivoAtrasoClienteAtrasado, ';'));
			int size = destinatarios.size();
			String assunto = montaDsRecado();
			for (int i = 0; i < size; i++) {
				Recado recado = new Recado();
				recado.cdRecado = RecadoService.getInstance().generateIdGlobal() + "" + i;
				recado.cdUsuarioRemetente = Session.getCdUsuario();
				recado.cdUsuarioDestinatario = StringUtil.getStringValue(destinatarios.items[i]);
				recado.dsAssunto = Messages.CLIENTE_ATRASADO_MOTIVO_ATRASO_PAGAMENTO;
				recado.dsRecado = assunto;
				recado.dtCadastro = DateUtil.getCurrentDate();
				recado.hrCadastro = TimeUtil.getCurrentTimeHHMMSS();
				recado.flLido = ValueUtil.VALOR_NAO;
				recado.cdStatusEnvio = StringUtil.getStringValue(Recado.STATUSENVIO_CAIXASAIDA);
				RecadoService.getInstance().insert(recado);
			}
			if (LavenderePdaConfig.usaEnvioRecadoServidorSemConfirmacao) {
				enviarRecadoServidor();
			} else if (LavenderePdaConfig.sugereEnvioAutomaticoRecado && UiUtil.showConfirmYesNoMessage(Messages.CLIENTE_ATRASADO_ENVIAR_MOTIVO)) {
				enviarRecadoServidor();
			}
		}
	}

	public String montaDsRecado() {
		StringBuffer assunto = new StringBuffer();
		assunto.append(message);
		assunto.append("\n \n");
		assunto.append(edMotivoAtraso.getText());
		assunto.append("\n \n");
		assunto.append(Messages.REPRESENTANTE_NOME_ENTIDADE);
		assunto.append(": ");
		assunto.append(StringUtil.getStringValue(SessionLavenderePda.getRepresentante().nmRepresentante));
		assunto.append("\n");
		assunto.append(Messages.CLIENTE_ATRASADO_DATA_HORA);
		assunto.append(" ");
		assunto.append(DateUtil.getCurrentDate());
		assunto.append(" ");
		assunto.append(TimeUtil.getCurrentTimeHHMMSS());
		return assunto.toString();
	}

	private void fecharPopUp() throws SQLException {
		if (!checkQuantidadeCaracteres()) {
    		fechaPopUp = false;
    		return;
    	} else {
    		gravaRecado();
    		fechaPopUp = true;
    		unpop();
    		options = 2;
    	}
	}

	public void onEvent(Event event) {
		try {
			super.onEvent(event);
			switch (event.type) {
				case ControlEvent.PRESSED:
					if (event.target == btVerTitulos) {
						options = 1;
						unpop();
					} else if (event.target == btNovoPedido) {
						if (LavenderePdaConfig.isUsaMotivoAtrasoClienteAtrasado()) {
							if (!checkQuantidadeCaracteres()) {
								return;
							} else {
								gravaRecado();
								unpop();
							}
							options = 0;
						} else {
							options = 0;
							unpop();
						}
					} else if (event.target == btCancelar) {
						if (LavenderePdaConfig.isUsaMotivoAtrasoClienteAtrasado()) {
							fecharPopUp();
						} else {
							unpop();
							options = 2;
						}
					} else if (event.target == btVerRede) {
						options = 3;
						unpop();
					}
				break;
			}
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		}
	}

	private boolean checkQuantidadeCaracteres() {
		if (edMotivoAtraso.getText().trim().length() < LavenderePdaConfig.qtMinCaracteresMotivoAtrasoClienteAtrasado) {
			String[] args = {StringUtil.getStringValueToInterface(LavenderePdaConfig.qtMinCaracteresMotivoAtrasoClienteAtrasado)};
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.CLIENTE_ATRASADO_INFORME_MOTIVO_ATRASO, args));
			return false;
		} else {
			return true;
		}
	}

	private void enviarRecadoServidor() {
		LoadingBoxWindow mb = UiUtil.createProcessingMessage(Messages.SINCRONIZACAO_MSG_ENVIANDO_DADOS);
		mb.popupNonBlocking();
    	try {
    		boolean houveErros = RecadoService.getInstance().enviarRecadoServidor();
    		if (!houveErros && !LavenderePdaConfig.usaEnvioRecadoServidorSemConfirmacao) {
    			UiUtil.showSucessMessage(FrameworkMessages.MSG_SYNC_INFO_ENVIO_CONCLUIDO);
    		}
    	} finally {
    		mb.unpop();
    	}
    }
	
	private boolean showBotaoVerRede() {
		return qtClientesRedeAtraso > 0 || isSomenteRedeAtrasado;
	}
	
}
