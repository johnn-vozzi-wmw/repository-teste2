package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.event.ButtonOptionsEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.ButtonOptions;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.domain.Recado;
import br.com.wmw.lavenderepda.business.domain.UsuarioWeb;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import br.com.wmw.lavenderepda.business.service.RecadoService;
import br.com.wmw.lavenderepda.business.service.UsuarioWebService;
import totalcross.ui.Button;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class CadRecadoForm extends BaseCrudCadForm {

	private EditText edCdUsuarioremetente;
	private EditText edCdUsuariodestinatario;
	private EditText edDsAssunto;
	private EditMemo edDsRecado;
	private EditText edDtEnvio;
	private EditText edHrEnvio;
	private CheckBoolean ckLido;
	private BaseButton btAddDestinatario;
	private ButtonAction btEnviar;
	private Button btSalvarRecado;
	public ButtonOptions btOpcoes;
	private ButtonAction btResponderRecado;
	private EditText edEmpresa;
	private EditText edCliente;
	private LabelName lbUsuarioRemetente;
	private LabelName lbUsuarioDestinatario;
	private LabelName lbDtEnvio;
	private LabelName lbDsAssunto;
	private LabelName lbDsRecado;
	private LabelName lbEmpresa;
	private LabelName lbCliente;

	private boolean inRecadoEnviado;
	private boolean inRecadoRecebido;
	private String[] cdDestinatarios;
	private UsuarioWeb[] cdUsuariosWebDestinatarios;
	private boolean hasUsuarioRepOrWebValid = true;
	private String dsAssuntoRecado = "";
	private String hrCadastroRecado = "";

	public CadRecadoForm() {
		super(Messages.RECADO_NOME_ENTIDADE);
		scrollable = true;
		edCdUsuarioremetente = new EditText("@@@@@@@@@@@", 20);		
		edCdUsuarioremetente.drawBackgroundWhenDisabled = true;
		edCdUsuarioremetente.setID("edCdUsuarioremetente");
		edCdUsuariodestinatario = new EditText("@@@@@@@@@@@", 20);
		edCdUsuariodestinatario.drawBackgroundWhenDisabled = true;
		edCdUsuariodestinatario.setID("edCdUsuariodestinatario");
		edDsAssunto = new EditText("@@@@@@@@@@", 50).setID("edDsAssunto");
		edDsRecado = new EditMemo("@@@@@@@@@@", 15, Recado.MAX_LENGTH_DS_RECADO).setID("edDsRecado");
		edDsRecado.drawDots = false;
		edDtEnvio = new EditText("@@/@@/@@@@", 50);
		edDtEnvio.drawBackgroundWhenDisabled = true;
		edDtEnvio.setID("edDtEnvio");
		edHrEnvio = new EditText("99:99.", 5);
		edHrEnvio.drawBackgroundWhenDisabled = true;
		edHrEnvio.setID("edHrEnvio");
		ckLido = new CheckBoolean(Messages.RECADO_LABEL_CDSTATUS).setID("ckLido");
		btAddDestinatario = new BaseButton("", UiUtil.getColorfulImage("images/add.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight())).setID("btAddDestinatario");
		btEnviar = new ButtonAction(Messages.RECADO_BUTTON_ENVIAR, "images/enviarrecado.png").setID("btEnviar");
		btSalvarRecado = new Button(FrameworkMessages.BOTAO_SALVAR);
		btOpcoes = new ButtonOptions();
		btResponderRecado = new ButtonAction(Messages.RECADO_BUTTON_RESPONDERRECADO, "images/enviarrecado.png");
		edEmpresa = new EditText("@@@@@@@@", 100);
		edCliente = new EditText("@@@@@@@@", 100);

		lbUsuarioRemetente = new LabelName(Messages.RECADO_LABEL_CDUSUARIOREMETENTE);
		lbUsuarioDestinatario = new LabelName(Messages.RECADO_LABEL_CDUSUARIODESTINATARIO);
		lbDtEnvio = new LabelName(Messages.RECADO_LABEL_DTENVIO);
		lbDsAssunto = new LabelName(Messages.RECADO_LABEL_DSASSUNTO);
		lbDsRecado = new LabelName(Messages.RECADO_LABEL_DSRECADO);
		lbEmpresa = new LabelName(Messages.RECADO_LABEL_EMPRESA);
		lbCliente = new LabelName(Messages.RECADO_LABEL_CLIENTE);
		//--
		setReadOnly();
	}

	@Override
	protected String getEntityDescription() {
		return title;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return RecadoService.getInstance();
	}

	@Override
	protected BaseDomain createDomain() throws SQLException {
		return new Recado();
	}

	public Recado getRecado() throws java.sql.SQLException {
		return (Recado) getDomain();
	}

	@Override
	public void add() throws SQLException {
		super.add();
		Recado recado = (Recado) getDomain();
		recado.dtCadastro = DateUtil.getCurrentDate();
		recado.hrCadastro = TimeUtil.getCurrentTimeHHMM();
		recado.flTipoRecado = Recado.TIPORECADO_NORMAL;
		recado.flObrigaResposta = ValueUtil.VALOR_NAO;
		recado.flRespostaEnviada = ValueUtil.VALOR_NAO;
	}

	@Override
	protected BaseDomain screenToDomain() throws SQLException {
		Recado recado = (Recado) getDomain();
		recado.dsAssunto = edDsAssunto.getValue();
		recado.dsRecado = edDsRecado.getValue();
		if (isEditing()) {
			recado.flLido = ckLido.getValue();
			if (ValueUtil.VALOR_SIM.equals(recado.flLido)) {
				recado.dtLeitura = DateUtil.getCurrentDate();
				recado.hrLeitura = TimeUtil.getCurrentTimeHHMM();
			}
		} else {
			recado.flLido = ValueUtil.VALOR_NAO;
		}
		//--
		return recado;
	}

	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		Recado recado = (Recado) domain;
		dsAssuntoRecado = recado.dsAssunto;
		hrCadastroRecado = recado.hrCadastro;
		edCdUsuarioremetente.setValue("");
		edCdUsuariodestinatario.setValue("");
		edEmpresa.setText("");
		edCliente.setText("");
		//--
		UsuarioWeb usuarioWeb = new UsuarioWeb();
		usuarioWeb.cdUsuarioWeb = recado.cdUsuarioRemetente;
		usuarioWeb.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		usuarioWeb = (UsuarioWeb) UsuarioWebService.getInstance().findByRowKey(usuarioWeb.getRowKey());
		edCdUsuarioremetente.setValue(usuarioWeb != null ? usuarioWeb.nmUsuarioWeb : Messages.RECADO_USUARIO_NAO_ENCONTRADO);
		//Nomes dos destinátários
		Recado recadoFilter = new Recado();
		recadoFilter.cdUsuarioRemetente = recado.cdUsuarioRemetente;
		recadoFilter.dsAssunto = recado.dsAssunto;
		recadoFilter.hrCadastro = recado.hrCadastro;
		Vector recadosList = RecadoService.getInstance().findAllByExample(recadoFilter);
		int size = recadosList.size();
		cdDestinatarios = new String[size];
		Recado recadoTemp;
		cdUsuariosWebDestinatarios = new UsuarioWeb[size];
		String[] nmDestinatarios = new String[size];
		for (int i = 0; i < size; i++) {
			recadoTemp = (Recado) recadosList.items[i];
			UsuarioWeb usuarioWebFilter = new UsuarioWeb();
			usuarioWebFilter.cdUsuarioWeb = recadoTemp.cdUsuarioDestinatario;
			usuarioWebFilter.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
			UsuarioWeb usuarioWebDestinatario = (UsuarioWeb) UsuarioWebService.getInstance().findByRowKey(usuarioWebFilter.getRowKey());
			cdUsuariosWebDestinatarios[i] = usuarioWebDestinatario;
			nmDestinatarios[i] = usuarioWebDestinatario.nmUsuarioWeb;
			cdDestinatarios[i] = recadoTemp.cdUsuarioDestinatario;
		}
		recadosList = null;
		edCdUsuariodestinatario.setValue(StringUtil.concatStrings(nmDestinatarios, ';'));
		if (isMostraLabelsEmpresaCliente(recado)) {
			Cliente clienteFilter = new Cliente();
			clienteFilter.cdEmpresa = recado.cdEmpresaCliente;
			clienteFilter.cdRepresentante = recado.cdRepresentanteCliente;
			clienteFilter.cdCliente = recado.cdCliente;
			Empresa empresaFilter = new Empresa();
			empresaFilter.cdEmpresa = recado.cdEmpresaCliente;
			recado.cliente = (Cliente) ClienteService.getInstance().findByRowKey(clienteFilter.getRowKey());
			recado.empresa = (Empresa) EmpresaService.getInstance().findByPrimaryKey(empresaFilter);

			edEmpresa.setText(StringUtil.getStringValue(recado.empresa == null ? recado.cdEmpresaCliente : recado.empresa));
			edCliente.setText(StringUtil.getStringValue(recado.cliente == null ? recado.cdCliente : recado.cliente));
			onFormStart();
			reposition();
		} else {
			removeLabelsEmpresaCliente();
		}
		//--
		edDsAssunto.setValue(recado.dsAssunto);
		edDsRecado.setValue(recado.dsRecado);
		edDtEnvio.setValue(StringUtil.getStringValue(recado.dtEnvio));
		edHrEnvio.setValue(recado.hrEnvio);
		if (isEditing()) {
			ckLido.setValue(recado.flLido);
			btAddDestinatario.setImage(UiUtil.getColorfulImage("images/menu.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
		} else {
			btAddDestinatario.setImage(UiUtil.getColorfulImage("images/add.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
		}
		if (inRecadoRecebido) {
			if (LavenderePdaConfig.excluiRecadoAutomaticamenteAposLeitura) {
				delete(recado);
				getBaseCrudListForm().list();
				return;
			}
			if (!ckLido.isChecked() && LavenderePdaConfig.marcaRecadoComoLidoAutomaticamente) {
				ckLido.setChecked(true);
				marcaRecadoComoLido(recado);
				recado.dsAssunto = edDsAssunto.getValue();
				recado.dsRecado = edDsRecado.getValue();
				update(recado);
				getBaseCrudListForm().list();
			}
		}
	}

	private void removeLabelsEmpresaCliente() {
		remove(edEmpresa);
		remove(lbEmpresa);
		remove(edCliente);
		remove(lbCliente);
		repositionChildren();
	}

	private boolean isMostraLabelsEmpresaCliente(Recado recado) {
		return LavenderePdaConfig.isUsaRecadoPorFuncionalidade() && ValueUtil.isNotEmpty(recado.cdEmpresaCliente) && ValueUtil.isNotEmpty(recado.cdCliente);
	}

	private void marcaRecadoComoLido(Recado recado) {
		recado.flLido = ValueUtil.VALOR_SIM;
		recado.dtLeitura = DateUtil.getCurrentDate();
		recado.hrLeitura = TimeUtil.getCurrentTimeHHMM();
	}

	@Override
	protected void clearScreen() throws java.sql.SQLException {
		edCdUsuarioremetente.setText("");
		//--
		UsuarioWeb usuarioWeb;
		usuarioWeb = new UsuarioWeb();
		usuarioWeb.cdUsuarioWeb = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		usuarioWeb.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		usuarioWeb = (UsuarioWeb) UsuarioWebService.getInstance().findByRowKey(usuarioWeb.getRowKey());
		hasUsuarioRepOrWebValid = false;
		if (usuarioWeb != null) {
			edCdUsuarioremetente.setValue(usuarioWeb.nmUsuarioWeb);
			getRecado().cdUsuarioRemetente = usuarioWeb.cdUsuarioWeb;
			hasUsuarioRepOrWebValid = true;
		}
		cdDestinatarios = new String[0];
		cdUsuariosWebDestinatarios = null;
		//--
		edCdUsuariodestinatario.setText("");
		edDsAssunto.setText("");
		edDsRecado.setText("");
		edDtEnvio.setValue(StringUtil.getStringValue(DateUtil.getCurrentDate()));
		edHrEnvio.setText(TimeUtil.getCurrentTimeHHMM());
		btAddDestinatario.setImage(UiUtil.getColorfulImage("images/add.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
		edEmpresa.setText("");
		edCliente.setText("");
	}

	@Override
	protected void visibleState() throws SQLException {
		btSalvarRecado.setVisible(!isEditing() && hasUsuarioRepOrWebValid);
		btEnviar.setVisible(!isEditing() && hasUsuarioRepOrWebValid);
		btExcluir.setVisible(isEditing() && !LavenderePdaConfig.bloquearExclusaoRecado && hasUsuarioRepOrWebValid && (!LavenderePdaConfig.excluiRecadoAutomaticamenteAposLeitura || !inRecadoRecebido));
		btAddDestinatario.setVisible(!hasUsuarioRepOrWebValid || !isEditing() || inRecadoEnviado);
		edCdUsuarioremetente.setEnabled(false);
		edCdUsuariodestinatario.setEnabled(false);
		edDtEnvio.setEnabled(false);
		edHrEnvio.setEnabled(false);
		edDsAssunto.setEnabled(!isEditing());
		edDsRecado.setEnabled(!isEditing());
		ckLido.setEnabled(false);
		ckLido.setVisible(isEditing());
		btResponderRecado.setVisible(isEditing() && inRecadoRecebido);
		btOpcoes.setVisible(false);
		btOpcoes.removeItem(Messages.RECADO_BUTTON_MARCAR_LIDO);
		edEmpresa.setEnabled(false);
		edCliente.setEnabled(false);
		if (isEditing() && !ckLido.getValue().equals(ValueUtil.VALOR_SIM) && !inRecadoEnviado && hasUsuarioRepOrWebValid && !LavenderePdaConfig.excluiRecadoAutomaticamenteAposLeitura) {
			btOpcoes.addItem(Messages.RECADO_BUTTON_MARCAR_LIDO);
			btOpcoes.setVisible(true);
		}
		if (!isMostraLabelsEmpresaCliente(getRecado())) {
			removeLabelsEmpresaCliente();
		}
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(barBottomContainer, btEnviar, 5);
		UiUtil.add(barBottomContainer, btOpcoes, 3);
		if (LavenderePdaConfig.isPermiteUsuarioNormalEnviarRecado()) {
			UiUtil.add(barBottomContainer, btResponderRecado, 5);
		}
		//--
		UiUtil.add(this, lbUsuarioRemetente, edCdUsuarioremetente, getLeft(), getNextY());
		UiUtil.add(this, lbUsuarioDestinatario, getLeft(), getNextY(), PREFERRED, PREFERRED);
		UiUtil.add(this, edCdUsuariodestinatario, getLeft(), AFTER, getWFill() - UiUtil.getControlPreferredHeight() - WIDTH_GAP);
		UiUtil.add(this, btAddDestinatario, AFTER + WIDTH_GAP, SAME, UiUtil.getControlPreferredHeight(), SAME);
		if (LavenderePdaConfig.isUsaRecadoPorFuncionalidade()) {
			UiUtil.add(this, lbEmpresa, edEmpresa, getLeft(), getNextY());
			UiUtil.add(this, lbCliente, edCliente, getLeft(), getNextY());
		}
		UiUtil.add(this, lbDtEnvio, getLeft(), getNextY(), PREFERRED, PREFERRED);
		UiUtil.add(this, edDtEnvio, getLeft(), AFTER, PREFERRED);
		UiUtil.add(this, edHrEnvio, AFTER + WIDTH_GAP, SAME, PREFERRED);
		UiUtil.add(this, ckLido, AFTER + WIDTH_GAP, SAME, FILL);
		//--
		UiUtil.add(this, lbDsAssunto, edDsAssunto, getLeft(), getNextY());
		UiUtil.add(this, lbDsRecado, getLeft(), getNextY());
		UiUtil.add(this, edDsRecado, getLeft(), AFTER, getWFill(), PREFERRED);
	}

	@Override
	public void onFormShow() throws SQLException {
		super.onFormShow();
		if (!hasUsuarioRepOrWebValid) {
			UiUtil.showWarnMessage(Messages.RECADO_MSG_SEM_USUARIOWEB_CADASTRADO);
		}
	}

	//@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btAddDestinatario) {
					ListRecadoDestinatarioWindow listRecadoDestinatarioForm;
					listRecadoDestinatarioForm = new ListRecadoDestinatarioWindow(isEditing(), cdUsuariosWebDestinatarios);
					listRecadoDestinatarioForm.popup();
					if (!listRecadoDestinatarioForm.closedByBtFechar) {
						cdUsuariosWebDestinatarios = listRecadoDestinatarioForm.getCheckedUsuarioWeb();
						edCdUsuariodestinatario.setText(listRecadoDestinatarioForm.getNmDestinatarios());
						cdDestinatarios = listRecadoDestinatarioForm.getCheckedDestinatarios();
					}
				} else if (event.target == btEnviar) {
					enviarClick();
				} else if (event.target == btSalvarRecado) {
					salvarClick();
				} else if (event.target == btResponderRecado) {
					btResponderRecadoClick();
				}
				break;
			}
			case ButtonOptionsEvent.OPTION_PRESS: {
				if (event.target == btOpcoes) {
					if (btOpcoes.selectedItem.equals(Messages.RECADO_BUTTON_MARCAR_LIDO)) {
						ckLido.setValue(ValueUtil.VALOR_SIM);
						salvarClick();
						getBaseCrudListForm().list();
					}
				}
				break;
			}
		}
	}

	private void enviarClick() throws SQLException {
		if (!ValueUtil.isEmpty(cdDestinatarios)) {
			//--
			if (isEditing()) {
				UsuarioWeb usuarioWeb = new UsuarioWeb();
				usuarioWeb.nmUsuarioWeb = edCdUsuarioremetente.getValue();
				usuarioWeb.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
				Vector usuarioWebList = UsuarioWebService.getInstance().findAllByExample(usuarioWeb);
				String cdUsuarioRemetente = ((UsuarioWeb) usuarioWebList.items[0]).cdUsuarioWeb;
				//--
				Recado recadoFilter = new Recado();
				recadoFilter.cdUsuarioRemetente = cdUsuarioRemetente;
				recadoFilter.dsAssunto = dsAssuntoRecado;
				recadoFilter.hrCadastro = hrCadastroRecado;
				Vector recadosList = RecadoService.getInstance().findAllByExample(recadoFilter);
				int size = recadosList.size();
				Recado recadoPersist;
				for (int i = 0; i < size; i++) {
					recadoPersist = (Recado) recadosList.items[i];
					recadoPersist.dsAssunto = edDsAssunto.getValue();
					recadoPersist.dsRecado = edDsRecado.getValue();
					recadoPersist.cdStatusEnvio = StringUtil.getStringValue(Recado.STATUSENVIO_CAIXASAIDA);
					insertOrUpdate(recadoPersist);
				}
			} else {
				Recado recado = (Recado) screenToDomain();
				recado.cdStatusEnvio = StringUtil.getStringValue(Recado.STATUSENVIO_CAIXASAIDA);
				insertOrUpdate(recado);
				if (recado.isRecadoResposta()) {
					RecadoService.getInstance().updateRespostaEnviada(recado.cdRecadoRelacionado);
				}
			}

			if (LavenderePdaConfig.usaEnvioRecadoServidorSemConfirmacao) {
				enviarRecadoServidor();
			} else if (LavenderePdaConfig.sugereEnvioAutomaticoRecado && UiUtil.showConfirmYesNoMessage(Messages.RECADO_MSG_ENVIAR_RECADO_AGORA)) {
				enviarRecadoServidor();
			} else {
				UiUtil.showSucessMessage(Messages.RECADO_MSG_ENVIADOSUCESSO);
			}

			list();
			close();
		} else {
			UiUtil.showWarnMessage(Messages.RECADO_MSG_SEM_DESTINATARIOS);
		}
	}

	//@Override
	protected void salvarClick() throws SQLException {
		if (!ValueUtil.isEmpty(cdDestinatarios)) {
			save();
			close();
		} else {
			UiUtil.showWarnMessage(Messages.RECADO_MSG_SEM_DESTINATARIOS);
		}
	}

	private void btResponderRecadoClick() throws SQLException {
		Recado recado = getRecado();
		String cdRecadoRelacionado = recado.cdRecado;
		String recadoRelacionadoObrigaResposta = recado.flObrigaResposta;
		String dsAssuntoRecadoAnterior = recado.dsAssunto;
		String cdCliente = recado.cdCliente;
		String cdEmpresa = recado.cdEmpresaCliente;
		String cdRepresentante = recado.cdRepresentanteCliente;
		Cliente cliente = recado.cliente;
		Empresa empresa = recado.empresa;
		UsuarioWeb usuarioWeb = new UsuarioWeb();
		usuarioWeb.cdUsuarioWeb = recado.cdUsuarioRemetente;
		usuarioWeb.cdUsuario = recado.cdUsuarioRemetente;
		usuarioWeb = (UsuarioWeb) UsuarioWebService.getInstance().findByRowKey(usuarioWeb.getRowKey());
		if (usuarioWeb == null) {
			usuarioWeb = new UsuarioWeb();
			usuarioWeb.cdUsuarioWeb = recado.cdUsuarioRemetente;
			usuarioWeb.nmUsuarioWeb = Messages.RECADO_USUARIO_NAO_ENCONTRADO;
		}
		//
		add();
		recado = getRecado();
		recado.cdUsuarioDestinatario = usuarioWeb.cdUsuarioWeb;
		recado.cdRecadoRelacionado = cdRecadoRelacionado;
		if(LavenderePdaConfig.isUsaRecadoPorFuncionalidade() && ValueUtil.isNotEmpty(cdCliente)) {
			onFormStart();
			reposition();
			recado.cliente = cliente;
			recado.cdCliente = cdCliente;
			recado.empresa = empresa;
			recado.cdEmpresaCliente = cdEmpresa;
			recado.cdRepresentanteCliente = cdRepresentante;
			edEmpresa.setText(StringUtil.getStringValue(recado.empresa == null ? recado.cdEmpresaCliente : recado.empresa));
			edCliente.setText(StringUtil.getStringValue(recado.cliente == null ? recado.cdCliente : recado.cliente));
		}
		recado.flTipoRecado = ValueUtil.VALOR_SIM.equals(recadoRelacionadoObrigaResposta) ? Recado.TIPORECADO_RESPOSTA_OBRIGATORIO : Recado.TIPORECADO_RESPOSTA_NORMAL;
		//
		cdDestinatarios = new String[1];
		cdDestinatarios[0] = getRecado().cdUsuarioDestinatario;
		btAddDestinatario.setVisible(false);
		edCdUsuariodestinatario.setText("");
		edCdUsuariodestinatario.setText(usuarioWeb.nmUsuarioWeb);
		edDsAssunto.setText(StringUtil.resume("Re: " + dsAssuntoRecadoAnterior, 44) );
	}

	protected void insertOrUpdate(BaseDomain domain) throws java.sql.SQLException {
		if (isEditing()) {
			update(domain);
		} else {
			int cont = 0;
			do {
				Recado recado = (Recado) domain;
				recado.cdUsuarioDestinatario = cdDestinatarios[cont];
				recado.cdRecado = StringUtil.getStringValue(ValueUtil.getIntegerValue(getCrudService().generateIdGlobal()) + cont);
				insert(recado);
				cont++;
			} while (cont < cdDestinatarios.length);
		}
	}

	//@Override
	public void delete(BaseDomain domain) throws SQLException {
		Recado recado = (Recado) domain;
		//--
		Recado recadoFilter = new Recado();
		recadoFilter.cdUsuarioRemetente = recado.cdUsuarioRemetente;
		recadoFilter.dsAssunto = recado.dsAssunto;
		recadoFilter.hrCadastro = recado.hrCadastro;
		Vector recadosList = RecadoService.getInstance().findAllByExample(recadoFilter);
		int size = recadosList.size();
		Recado recadoTemp;
		for (int i = 0; i < size; i++) {
			recadoTemp = (Recado) recadosList.items[i];
			marcaRecadoComoLido(recadoTemp);
			super.delete(recadoTemp);
		}
		recadosList = null;
	}

	public void setInRecadoEnviado(boolean on) {
		inRecadoEnviado = on;
	}

	public void setInRecadoRecebido(boolean on) {
		inRecadoRecebido = on;
	}

	//@Override
	public void close() throws SQLException {
		if (inRecadoRecebido) {
			if (LavenderePdaConfig.obrigaRespostaRecado) {
				Recado recado = getRecado();
				if (recado.isRecadoResposta() && !isEditing()) {
					Recado recadoFilter = new Recado();
					recadoFilter.cdRecado = getRecado().cdRecadoRelacionado;
					recado = (Recado) RecadoService.getInstance().findByRowKey(recadoFilter.getRowKey());
				}
				if (ValueUtil.VALOR_SIM.equals(recado.flObrigaResposta) && ValueUtil.VALOR_NAO.equals(recado.flRespostaEnviada)) {
					UiUtil.showWarnMessage(Messages.RECADO_MSG_NAOPERMITIRSAIRSEMRESPONDERRECADO);
					return;
				}
			}
			if (LavenderePdaConfig.excluiRecadoAutomaticamenteAposLeitura && isEditing()) {
				if (UiUtil.showConfirmYesCancelMessage(Messages.RECADO_MSG_CONFIRMASAIDA) == 0) {
					return;
				}
			}
		}
		super.close();
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

	protected void excluirClick() throws SQLException {
		if (LavenderePdaConfig.obrigaRespostaRecado) {
			if (ValueUtil.VALOR_SIM.equals(getRecado().flObrigaResposta) && ValueUtil.VALOR_NAO.equals(getRecado().flRespostaEnviada)) {
				UiUtil.showWarnMessage(Messages.RECADO_MSG_NAOPERMITIREXCLUIRRECADO);
				return;
			} else if (Recado.TIPORECADO_RESPOSTA_OBRIGATORIO.equals(getRecado().flTipoRecado)) {
				UiUtil.showWarnMessage(Messages.RECADO_MSG_NAOPERMITIREXCLUIRRECADORESPOSTA);
				return;
			}
		}
		super.excluirClick();

	}

	public EditText getEdEmpresa() {
		return edEmpresa;
	}

	public void setEdEmpresa(EditText edEmpresa) {
		this.edEmpresa = edEmpresa;
	}

	public EditText getEdCliente() {
		return edCliente;
	}

	public void setEdCliente(EditText edCliente) {
		this.edCliente = edCliente;
	}

	public LabelName getLbEmpresa() {
		return lbEmpresa;
	}

	public void setLbEmpresa(LabelName lbEmpresa) {
		this.lbEmpresa = lbEmpresa;
	}

	public LabelName getLbCliente() {
		return lbCliente;
	}

	public void setLbCliente(LabelName lbCliente) {
		this.lbCliente = lbCliente;
	}

}
