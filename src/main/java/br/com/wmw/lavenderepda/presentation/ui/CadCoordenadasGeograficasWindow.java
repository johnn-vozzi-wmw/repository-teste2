package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.gps.GpsData;
import br.com.wmw.framework.gps.GpsService;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelSubtitle;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.WmwMessageBox.TYPE_MESSAGE;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.EnderecoGpsPda;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.Prospect;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.service.CidadeService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.EnderecoGpsPdaService;
import br.com.wmw.lavenderepda.business.service.NovoClienteService;
import br.com.wmw.lavenderepda.business.service.UFService;
import totalcross.sql.Types;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadCoordenadasGeograficasWindow extends WmwWindow {

	private static final String VALOR_VAZIO = "-";

	private LabelValue vlNmCliente;
	private LabelValue vlLogradouro;
	private LabelValue vlBairro;
	private LabelValue vlCidade;
	private LabelValue vlEstado;
	private LabelValue vlLatitude;
	private LabelValue vlLongitude;
	private LabelName lbLatitude;
	private LabelName lbLongitude;
	private LabelName lbCliente;
	private LabelName lbLogradouro;
	private LabelName lbBairro;
	private LabelName lbCidade;
	private LabelName lbEstado;
	private CheckBoolean ckConfirmacao;
	private ButtonPopup btCadastrar;
	private ButtonPopup btMaisTarde;
	private ButtonPopup btSenha;
	private BaseButton btColetar;
	private LabelSubtitle lbStatusColeta;
	private int acaoColeta = 0;
	private boolean cadastroManual;
	public boolean cadastrouCoordenada;
	public boolean cadCoordAutoNovoCliente;
	public boolean cadastroManualNovoCli;
	public boolean liberadoPorSenha;
	public Cliente cliente;
	public Prospect prospect;

	public CadCoordenadasGeograficasWindow(Cliente cliente, boolean cadastroManual) {
		this(cliente, cadastroManual, false);
	}
	
	public CadCoordenadasGeograficasWindow(Cliente cliente, boolean cadastroManual, boolean cadCoordAutoNovoCliente) {
		this(cadastroManual, cadCoordAutoNovoCliente);
		this.cliente = cliente;
		setDefaultRect();
		domainToScreenCliente();
	}
	
	public CadCoordenadasGeograficasWindow(Prospect prospect, boolean cadastroManual, boolean cadCoordAutoNovoCliente) {
		this(cadastroManual, cadCoordAutoNovoCliente);
		this.prospect = prospect;
		setDefaultRect();
		domainToScreenProspect();
	}
	
	private CadCoordenadasGeograficasWindow(boolean cadastroManual, boolean cadCoordAutoNovoCliente) {
		super(Messages.CAD_COORD_TITULO);
		this.cadastroManual = cadastroManual;
		this.cadCoordAutoNovoCliente = cadCoordAutoNovoCliente;
		lbCliente = new LabelName(Messages.CAD_COORD_CLIENTE);
		lbLogradouro = new LabelName(Messages.CAD_COORD_ENDERECO);
		lbBairro = new LabelName(Messages.CAD_COORD_BAIRRO);
		lbCidade = new LabelName(Messages.CAD_COORD_CIDADE);
		lbEstado = new LabelName(Messages.CAD_COORD_UF);
		vlNmCliente = new LabelValue();
		vlLogradouro = new LabelValue();
		vlBairro = new LabelValue();
		vlCidade = new LabelValue();
		vlEstado = new LabelValue();
		lbLatitude = new LabelName(Messages.CAD_COORD_LAT);
		vlLatitude = new LabelValue();
		lbLongitude = new LabelName(Messages.CAD_COORD_LON);
		vlLongitude = new LabelValue();
		ckConfirmacao = new CheckBoolean(Messages.CAD_COORD_CONFIRM_INFO);
		ckConfirmacao.setEnabled(false);
		btCadastrar = new ButtonPopup(Messages.CAD_COORD_CADASTRAR);
		btMaisTarde = new ButtonPopup(Messages.CAD_COORD_MAIS_TARDE);
		btSenha = new ButtonPopup(Messages.MENU_SISTEMA_GERAR_SENHA);
		btSenha.setVisible(!cadastroManual);
		btColetar = new BaseButton(Messages.CAD_COORD_COLETAR, UiUtil.getColorfulImage("images/gps.png", UiUtil.getButtonImageIconSize(), UiUtil.getButtonImageIconSize()), RIGHT, WIDTH_GAP_BIG);
		lbStatusColeta = new LabelSubtitle();
		lbStatusColeta.setForeColor(ColorUtil.softRed);
	}

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, lbCliente, vlNmCliente, getLeft(), getNextY());
		UiUtil.add(this, btColetar, CENTER, getNextY());
		UiUtil.add(this, lbLatitude, vlLatitude, getLeft(), getNextY());
		UiUtil.add(this, lbLongitude, vlLongitude, getLeft(), getNextY());
		SessionContainer containerStatus = new SessionContainer();
		UiUtil.add(this, containerStatus, LEFT,  AFTER, FILL);
		UiUtil.add(containerStatus, lbStatusColeta, getLeft(), CENTER, FILL, PREFERRED);
		UiUtil.add(this, ckConfirmacao, getLeft(), getNextY());
		UiUtil.add(this, lbLogradouro, vlLogradouro, getLeft(), getNextY());
		UiUtil.add(this, lbBairro, vlBairro, getLeft(), getNextY());
		UiUtil.add(this, lbCidade, vlCidade, getLeft(), getNextY());
		UiUtil.add(this, lbEstado, vlEstado, getLeft(), getNextY());
		addButtonPopup(btCadastrar);
		if (prospect == null && ((LavenderePdaConfig.obrigaCadCoordenadasGeograficas && !cadCoordAutoNovoCliente) || (LavenderePdaConfig.obrigaCadCoordenadasGeograficasNovoCliente && cadCoordAutoNovoCliente))) {
			addButtonPopup(btSenha);
		}
		addButtonPopup(btMaisTarde);
	}

	@Override
	protected void addBtFechar() {
		//Não utilizado nesta Window
	}
	
	private void domainToScreenCliente() {
		vlNmCliente.setValue(cliente.nmRazaoSocial);
		vlLogradouro.setValue(cliente.dsLogradouroComercial);
		vlBairro.setValue(cliente.dsBairroComercial);
		vlCidade.setValue(cliente.dsCidadeComercial);
		String dsEstadoComercial = ValueUtil.isNotEmpty(cliente.dsEstadoComercial) ? cliente.dsEstadoComercial : cliente.cdEstadoComercial;
		vlEstado.setValue(dsEstadoComercial);
	}

	private void domainToScreenProspect() {
		vlNmCliente.setValue(prospect.getHashValuesDinamicos().getString(Prospect.NMCOLUNA_NMRAZAOSOCIAL));
		vlLogradouro.setValue(prospect.getHashValuesDinamicos().getString(Prospect.NMCOLUNA_DSLOGRADOUROCOMERCIAL));
		vlBairro.setValue(prospect.getHashValuesDinamicos().getString(Prospect.NMCOLUNA_DSBAIRROCOMERCIAL));
		vlCidade.setValue(prospect.getHashValuesDinamicos().getString(Prospect.NMCOLUNA_DSCIDADECOMERCIAL));
		vlEstado.setValue(prospect.getHashValuesDinamicos().getString(Prospect.NMCOLUNA_CDUFCOMERCIAL));
	}

	@Override
	public void onWindowEvent(final Event event) throws SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
		case ControlEvent.PRESSED: {
			if (event.target == btMaisTarde) {
				fecharWindow();
			} else if (event.target == btColetar) {
				coletarCoordenadasGps();
			} else if (event.target == btCadastrar) {
				cadastrarEnderecoGpsPda();
			} else if (event.target == btSenha) {
				AdmSenhaDinamicaWindow senha = new AdmSenhaDinamicaWindow();
				senha.setMensagem(Messages.CAD_COORD_SENHA_PEDIDO);
				senha.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_CAD_COORDENADAS);
				if (cadCoordAutoNovoCliente) {
					senha.setNuCnpj(cliente.nuCnpj);
				} else {
					senha.setCdCliente(cliente.cdCliente);
				}
				if (senha.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
					cadastrouCoordenada = true;
					liberadoPorSenha = true;
					fecharWindow();
				}
			}
			break;
		}
		}
	}

	@Override
	public void popup() {
		if (cliente != null && (isColetaAutomaticamenteCoordenadasClienteSelecionado() || isColetaAutomaticamenteCoordenadasNovoCliente())) {
			coletarCoordenadasGps();
			if (acaoColeta > 1 || (!cadastroManual && acaoColeta == 0)) {
				super.popup();
			}
		} else {
			super.popup();
		}
	}

	private boolean isColetaAutomaticamenteCoordenadasNovoCliente() {
		return LavenderePdaConfig.isCadastroCoordenadasGeograficasNovoClienteAutomatico() && cliente.isNovoCliente();
	}

	private boolean isColetaAutomaticamenteCoordenadasClienteSelecionado() {
		return LavenderePdaConfig.isColetaAutomaticamenteCoordenadasGeograficasCliente() && !cliente.isNovoCliente() && !cliente.isClienteDefaultParaNovoPedido();
	}

	private void coletarCoordenadasGps() {
		LoadingBoxWindow msg = UiUtil.createProcessingMessage(Messages.CAD_COORD_COLETANDO);
		msg.popupNonBlocking();
		try {
			do {
				acaoColeta = 2;
				if (VmUtil.isAndroid() || VmUtil.isIOS()) {
					GpsData gpsData = GpsService.getInstance().forceReadData(LavenderePdaConfig.vlTimeOutTentativaColetaGps);
					if (gpsData.isGpsOff()) {
						if (!SessionLavenderePda.isSistemaLiberadoSenhaGpsOff) {
							UiUtil.showWarnMessage(Messages.CAD_COORD_GPS_DESLIGADO);
							atualizarComponentes();
							return;
						}
					}
					if (gpsData.isSuccess()) {
						vlLatitude.setText(gpsData.latitude + "");
						vlLongitude.setText(gpsData.longitude + "");
					} else {
						if (cadastroManual) {
							UiUtil.showWarnMessage(Messages.CAD_COORD_COLETA_ERRO);
							vlLatitude.setText("");
							vlLongitude.setText("");
						} else {
							String[] botoes = new String[] { Messages.CAD_COORD_MAIS_TARDE, Messages.CAD_COORD_TENTAR_NOVAMENTE };
							acaoColeta = UiUtil.showMessage(FrameworkMessages.TITULO_MSG_ATENCAO, Messages.CAD_COORD_COLETA_ERRO, TYPE_MESSAGE.WARN, botoes);
						}
					}
				} else {
					UiUtil.showWarnMessage(Messages.CAD_COORD_PLATAFORMA_NAO_SUPORTADA);
				}
				atualizarComponentes();
			} while (acaoColeta == 1);
		} catch (Throwable ex) {
			//Apenas nao realiza a coleta
		} finally {
			msg.unpop();
		}
	}

	private void atualizarComponentes() {
		if (ValueUtil.isNotEmpty(vlLatitude.getText()) && ValueUtil.isNotEmpty(vlLongitude.getText())) {
			ckConfirmacao.setEnabled(true);
			lbStatusColeta.setValue(Messages.CAD_COORD_SUCESSO);
			lbStatusColeta.setForeColor(ColorUtil.softGreen);
		} else {
			ckConfirmacao.setEnabled(false);
			lbStatusColeta.setValue(Messages.CAD_COORD_FALHA);
			lbStatusColeta.setForeColor(ColorUtil.softRed);
		}
	}
	
	private void cadastrarEnderecoGpsPda() throws SQLException {
		if (!ckConfirmacao.isChecked()) {
			UiUtil.showWarnMessage(Messages.CAD_COORD_MARCAR_OPCAO_CONFIRMARINFOS);
			return;
		}
		if (!selecionaNovoClienteCadCoordenadas() && prospect == null) {
			return;
		}
		EnderecoGpsPda enderecoGpsPda = new EnderecoGpsPda();
		if (cliente != null) {
			cadastraEnderecoFromCliente(enderecoGpsPda);
		} else {
			cadastraEnderecoFromProspect(enderecoGpsPda);
		}
		enderecoGpsPda.dtColeta = DateUtil.getCurrentDate();
		enderecoGpsPda.hrColeta = TimeUtil.getCurrentTimeHHMM();
		enderecoGpsPda.cdLatitude = vlLatitude.getDoubleValue();
		enderecoGpsPda.cdLongitude = vlLongitude.getDoubleValue();
		EnderecoGpsPdaService.getInstance().insertOrUpdate(enderecoGpsPda);
		if (cliente != null) {
			cliente.cdLatitude = enderecoGpsPda.cdLatitude;
			cliente.cdLongitude = enderecoGpsPda.cdLongitude;
		} else {
			prospect.cdLatitude = enderecoGpsPda.cdLatitude;
			prospect.cdLongitude = enderecoGpsPda.cdLongitude;
		}
		if (!cadCoordAutoNovoCliente && cliente != null) {
			ClienteService.getInstance().update(cliente);
		}
		cadastrouCoordenada = true;
		fecharWindow();
	}
	
	private void cadastraEnderecoFromCliente(EnderecoGpsPda enderecoGpsPda) {
		enderecoGpsPda.dsLogradouro = ValueUtil.isNotEmpty(cliente.dsLogradouroComercial) ? cliente.dsLogradouroComercial : VALOR_VAZIO;
		enderecoGpsPda.nuLogradouro = ValueUtil.isNotEmpty(cliente.nuLogradouroComercial) ? cliente.nuLogradouroComercial : VALOR_VAZIO;
		enderecoGpsPda.dsBairro = ValueUtil.isNotEmpty(cliente.dsBairroComercial) ? cliente.dsBairroComercial : VALOR_VAZIO;
		enderecoGpsPda.dsCidade = ValueUtil.isNotEmpty(cliente.dsCidadeComercial) ? cliente.dsCidadeComercial : VALOR_VAZIO;
		enderecoGpsPda.dsEstado = ValueUtil.isNotEmpty(cliente.dsEstadoComercial) ? cliente.dsEstadoComercial : VALOR_VAZIO;
		enderecoGpsPda.dsCep = ValueUtil.isNotEmpty(cliente.dsCepComercial) ? cliente.dsCepComercial : VALOR_VAZIO;
		enderecoGpsPda.cdCliente = cliente.cdCliente;
	}
	
	private void cadastraEnderecoFromProspect(EnderecoGpsPda enderecoGpsPda) throws SQLException {
		String dsLogradouro = prospect.getHashValuesDinamicos().getString(Prospect.NMCOLUNA_DSLOGRADOUROCOMERCIAL);
		String dsBairro = prospect.getHashValuesDinamicos().getString(Prospect.NMCOLUNA_DSBAIRROCOMERCIAL);
		String dsCidade = prospect.getHashValuesDinamicos().getString(Prospect.NMCOLUNA_DSCIDADECOMERCIAL);
		String cdCidade = prospect.getHashValuesDinamicos().getString(Prospect.NMCOLUNA_CDCIDADECOMERCIAL);
		String dsEstado = prospect.getHashValuesDinamicos().getString(Prospect.NMCOLUNA_DSESTADOCOMERCIAL);
		String cdUf = prospect.getHashValuesDinamicos().getString(Prospect.NMCOLUNA_CDUFCOMERCIAL);
		String nuLogradouro = prospect.getHashValuesDinamicos().getString(Prospect.NMCOLUNA_DSNUMEROLOGRADOUROCOMERCIAL);
		String dsCep = prospect.getHashValuesDinamicos().getString(Prospect.NMCOLUNA_DSCEPCOMERCIAL);
		if (ValueUtil.isEmpty(dsCidade) && ValueUtil.isNotEmpty(cdCidade)) {
			dsCidade = CidadeService.getInstance().getNmCidade(cdCidade);
		}
		if (ValueUtil.isEmpty(dsEstado) && ValueUtil.isNotEmpty(cdUf)) {
			dsEstado = UFService.getInstance().getDsUf(cdUf);
		}
		enderecoGpsPda.dsLogradouro = ValueUtil.isNotEmpty(dsLogradouro) ? dsLogradouro : VALOR_VAZIO;
		enderecoGpsPda.nuLogradouro = ValueUtil.isNotEmpty(nuLogradouro) ? nuLogradouro : VALOR_VAZIO;
		enderecoGpsPda.dsBairro = ValueUtil.isNotEmpty(dsBairro) ? dsBairro : VALOR_VAZIO;
		enderecoGpsPda.dsCidade = ValueUtil.isNotEmpty(dsCidade) ? dsCidade : VALOR_VAZIO;
		enderecoGpsPda.dsEstado = ValueUtil.isNotEmpty(dsEstado) ? dsEstado : VALOR_VAZIO;
		enderecoGpsPda.dsCep = ValueUtil.isNotEmpty(dsCep) ? dsCep : VALOR_VAZIO;
	}

	private boolean selecionaNovoClienteCadCoordenadas() throws SQLException {
		if (LavenderePdaConfig.isUsaCadastroCoordenadasGeograficasNovoCliente() && SessionLavenderePda.getCliente() != null && SessionLavenderePda.getCliente().isClienteDefaultParaCadCoordenadas()) {
			ListNovoClienteWindow listNovoClienteWindow = new ListNovoClienteWindow(ListNovoClienteWindow.CADCOORDENADASEMCLI);
			listNovoClienteWindow.popup();
			if (Cliente.CD_CLIENTE_DEFAULT_PARA_CADASTRO_COORDENADA.equals(SessionLavenderePda.getCliente().cdCliente)) {
				return false;
			}
			NovoCliente novoCliente = NovoClienteService.getInstance().getNovoClienteByCliente(SessionLavenderePda.getCliente());
			NovoClienteService.getInstance().updateColumn(novoCliente.getRowKey(), "cdLatitude", vlLatitude.getDoubleValue(), Types.DECIMAL);
			NovoClienteService.getInstance().updateColumn(novoCliente.getRowKey(), "cdLongitude", vlLongitude.getDoubleValue(), Types.DECIMAL);
			cliente = SessionLavenderePda.getCliente();
		}
		return true;
	}
}
