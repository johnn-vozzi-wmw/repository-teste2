package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.gps.GpsData;
import br.com.wmw.framework.gps.GpsService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditNumberMask;
import br.com.wmw.framework.presentation.ui.ext.EditTextSpeech;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.WmwMessageBox.TYPE_MESSAGE;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DapLaudo;
import br.com.wmw.lavenderepda.business.service.DapLaudoService;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.sys.Vm;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadDapForm extends BaseCrudCadForm {
	
	private static final String SYSTEM_VIEWER = "viewer";
	
	private LabelValue lvDsCliente;
	private EditNumberMask edNuCpf;
	private EditNumberMask edNuCnpj;
	private LabelValue lvDsMatriculaDap;
	private LabelValue lvDsLocal;
	private LabelValue lvDsPosGeografica;
	private LabelValue lvDsSafra;
	private LabelValue lvDsCultura;
	private LabelValue lvDtEmissao;
	private LabelValue lvDsSequenciaDap;
	private LabelName lbDsNuCpfCnpj;
	private EditNumberFrac edQtArea;
	private EditTextSpeech edDsAspecto;
	private EditTextSpeech edDsRecomendacoes;
	private EditMemo edObs;
	private ButtonAction btFecharLaudo;
	private ButtonAction btGerarPdf;
	private ButtonAction btAssinaturas;
	private ScrollTabbedContainer scTabDap;
	private ButtonAction btReabrirLaudo;
	private ButtonAction btEnviarLaudo;
	private BaseButton btCadCoordenadas;
	private DapLaudo dapLaudo;
	
	public CadDapForm() throws SQLException {
		super(Messages.LISTA_DAP_LAUDO_TITULO);
		initScTabbed();
		lvDsCliente = new LabelValue();
        edNuCpf = new EditNumberMask("###.###.###-##");
        edNuCnpj = new EditNumberMask("##.###.###/####-##");
		lvDsMatriculaDap = new LabelValue();
		lvDsSequenciaDap = new LabelValue();
		lvDsLocal = new LabelValue();
		lvDsPosGeografica = new LabelValue();
		lvDsSafra = new LabelValue();
		lvDsCultura = new LabelValue();
		lvDtEmissao = new LabelValue();
		edQtArea = new EditNumberFrac("9999999999", 9);
		edDsAspecto = new EditTextSpeech("@@@@@@@@@@", 32000, 5);
		edDsRecomendacoes = new EditTextSpeech("@@@@@@@@@@", 32000, 5);
		btFecharLaudo = new ButtonAction(Messages.BOTAO_FECHAR_DAP_LAUDO, "images/fecharpedido.png");
		btReabrirLaudo = new ButtonAction(Messages.BOTAO_REABRIR_DAP_LAUDO, "images/reabrirpedido.png");
		btGerarPdf = new ButtonAction(Messages.BOTAO_GERAR_PDF_DAP, "images/pdf-file.png");
		btAssinaturas = new ButtonAction(Messages.BOTAO_ASSINATURAS, "images/edicao.png");
		btEnviarLaudo = new ButtonAction(Messages.BOTAO_ENVIAR_DAP_LAUDO, "images/enviar.png");
		edObs = new EditMemo("@@@", 10, 1000);
		int imgSize = UiUtil.getControlPreferredHeight() - HEIGHT_GAP_BIG;
		btCadCoordenadas = new BaseButton("", UiUtil.getColorfulImage("images/gps.png", imgSize, imgSize));
		lbDsNuCpfCnpj = new LabelName(Messages.CAD_DAP_CAMPO_CPF_CNPJ);
	}
	
	public CadDapForm(DapLaudo dapLaudo) throws SQLException {
		this();
		this.dapLaudo = dapLaudo;
		domainToScreen(dapLaudo);
	}
	
	private void initScTabbed() {
        String[] abas = new String[]{Messages.DAPLAUDO_TAB_CADASTRO, Messages.DAPLAUDO_TAB_OBS};
        scTabDap = new ScrollTabbedContainer(abas);
        scTabDap.allSameWidth = true;
	}
	
	@Override
	protected BaseDomain createDomain() throws SQLException {
		return new DapLaudo();
	}

	@Override
	protected String getEntityDescription() {
		return title;
	}
	
	@Override
	protected CrudService getCrudService() throws SQLException {
		return DapLaudoService.getInstance();
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == btFecharLaudo) {
				btFecharLaudoClick();
			}
			if (event.target == btGerarPdf) {
				btGerarPdfLaudoClick();
			}
			if (event.target == btCadCoordenadas) {
				btColetaGpsClick();
			}
			if (event.target == btAssinaturas) {
				btAssinaturasClick();
			}
			if (event.target == btReabrirLaudo) {
				btReabrirLaudoClick();
			}
			if (event.target == btEnviarLaudo) {
				btEnviarLaudoClick();
			}
			break;
		}
	}

	private void btEnviarLaudoClick() {
		try {
			enviaLaudo();
		} catch (ValidationException e) {
			UiUtil.showErrorMessage(e.getMessage());
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}
	
	private void enviaLaudo() throws Exception {
		DapLaudo dapLaudo = getDapLaudo();
		dapLaudo.flStatusLaudo = DapLaudo.FLSTATUSLAUDO_EDITADO;
		dapLaudo.imAssCliente = null;
		dapLaudo.imAssTecnico = null;
		screenToDomain();
		DapLaudoService.getInstance().validateInformacoesDap(dapLaudo);
		DapLaudoService.getInstance().insert(dapLaudo);
		LoadingBoxWindow msg = new LoadingBoxWindow();
		msg.popupNonBlocking();
		try {
			SyncManager.enviaDapLaudoAtua(dapLaudo.isDapFechado());
		} finally {
			msg.unpop();
		}
		UiUtil.showSucessMessage(Messages.DAPATUA_ENVIADO_SUCESSO);
		visibleState();
	}

	private void btAssinaturasClick() throws SQLException {
		new CadAssinaturaDapWindow(getDapLaudo(), true).popup();
	}

	private void btGerarPdfLaudoClick() throws SQLException {
		LoadingBoxWindow msg = UiUtil.createProcessingMessage(Messages.CAD_DAPLAUDO_ERRO_ENVIANDO_PDF);
		msg.popupNonBlocking();
		try {
			if (SyncManager.isConexaoPdaDisponivel()) {
				geraPdfOnline();
			} else {
				UiUtil.showErrorMessage(Messages.CAD_DAPLAUDO_ERRO_ENVIO_PDF);
			}
		} finally {
			msg.unpop();
		}
	}
	
	private void geraPdfOnline() {
		try {
			String pdf = SyncManager.geraPdfDapLaudo(getDapLaudo());
			pdf = pdf.replaceAll("\\\\", "/").replaceAll("//", "/");
			if (VmUtil.isAndroid() || VmUtil.isIOS()) {
				int executed = Vm.exec(SYSTEM_VIEWER, pdf, 0, true);
				if (executed == -1) {
					UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.MSG_CAMINHO_PDF_PEDIDO, pdf));
				}
			} else if (VmUtil.isSimulador()) {
				Vm.exec(SYSTEM_VIEWER, "file:///" + pdf, 0, true);
			}
		} catch (Throwable e) {
			throw new ValidationException(MessageUtil.getMessage(Messages.MSG_ERRO_GERAR_PDF_DAP, StringUtil.clearEnterException(e.getMessage())));
		}
	}

	private void btFecharLaudoClick() throws SQLException {
		if (!UiUtil.showConfirmYesNoMessage(Messages.DAP_MSG_CONFIRM_FECHARDAP)) return;
		DapLaudo dapLaudo = getDapLaudo();
		screenToDomain();
		DapLaudoService.getInstance().validateInformacoesDap(getDapLaudo());
		mostraPopUpAssinatura(dapLaudo);
	}
	
	private void btReabrirLaudoClick() throws SQLException {
		if (!UiUtil.showConfirmYesNoMessage(Messages.DAP_MSG_CONFIRM_REABRIRDAP)) return;
		DapLaudo dapLaudo = getDapLaudo();
		dapLaudo.flStatusLaudo = DapLaudo.FLSTATUSLAUDO_EM_EDICAO;
		visibleState();
	}

	private void mostraPopUpAssinatura(DapLaudo dapLaudo) throws SQLException {
		CadAssinaturaDapWindow cadAssinatura = new CadAssinaturaDapWindow(dapLaudo);
		cadAssinatura.popup();
		if (!cadAssinatura.closedByBtFechar) {
			edit(dapLaudo);
		}
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, scTabDap, getTop() + HEIGHT_GAP, barTopContainer.getHeight());
		Container tabDap = scTabDap.getContainer(0);
		UiUtil.add(tabDap, new LabelName(Messages.CAD_DAP_CAMPO_CLIENTE), lvDsCliente, getLeft(), TOP + HEIGHT_GAP_BIG);
		UiUtil.add(tabDap, lbDsNuCpfCnpj, getLeft(), AFTER + HEIGHT_GAP_BIG, getWFill(), PREFERRED);
		UiUtil.add(tabDap, edNuCnpj, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(tabDap, edNuCpf, SAME, SAME);
		UiUtil.add(tabDap, new LabelName(Messages.LISTA_DAP_CLIENTE_COL_MATRICULA), lvDsMatriculaDap, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(tabDap, new LabelName(Messages.LISTA_DAP_CLIENTE_COL_CID_UF_LOC), lvDsLocal, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(tabDap, new LabelName(Messages.CAD_DAP_CAMPO_POS), getLeft(), AFTER + HEIGHT_GAP_BIG, getWFill(), PREFERRED);
		UiUtil.add(tabDap,  lvDsPosGeografica, getLeft(), AFTER + HEIGHT_GAP_BIG, FILL - (btCadCoordenadas.getPreferredWidth() + WIDTH_GAP + WIDTH_GAP_BIG));
		UiUtil.add(tabDap, btCadCoordenadas, RIGHT - (lvDsPosGeografica.getWidth()/3) , SAME);
		UiUtil.add(tabDap, new LabelName(Messages.CAD_DAP_CAMPO_SAFRA), lvDsSafra, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(tabDap, new LabelName(Messages.CAD_DAP_CAMPO_CULTURA), lvDsCultura, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(tabDap, new LabelName(Messages.CAD_DAP_CAMPO_DTEMISSAO), lvDtEmissao, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(tabDap, new LabelName(Messages.CAD_DAP_CAMPO_SEQUENCIA_LAUDO), lvDsSequenciaDap, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(tabDap, new LabelName(Messages.CAD_DAP_CAMPO_AREA), edQtArea, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(tabDap, new LabelName(Messages.CAD_DAP_CAMPO_ASPECTO), edDsAspecto, getLeft(), AFTER + HEIGHT_GAP_BIG, FILL - HEIGHT_GAP, PREFERRED);
		UiUtil.add(tabDap, new LabelName(Messages.CAD_DAP_CAMPO_RECOMENDACAO), edDsRecomendacoes, getLeft(), AFTER + HEIGHT_GAP_BIG, FILL - HEIGHT_GAP, PREFERRED + HEIGHT_GAP_BIG);
		Container tabObs = scTabDap.getContainer(1);
		UiUtil.add(tabObs, new LabelName(Messages.DAPLAUDO_TAB_OBS), edObs, getLeft(), TOP + HEIGHT_GAP_BIG);
		UiUtil.add(barBottomContainer, btGerarPdf,  1);
		UiUtil.add(barBottomContainer, btReabrirLaudo,  4);
		UiUtil.add(barBottomContainer, btEnviarLaudo, 4);
		UiUtil.add(barBottomContainer, btFecharLaudo,  5);
		UiUtil.add(barBottomContainer, btAssinaturas,  5);
	}

	private void mudaLabelBtVoltar() throws SQLException {
		if (getDapLaudo().isDapFechado()) {
			btVoltar.setText(FrameworkMessages.BOTAO_VOLTAR);
		} else {
			btVoltar.setText(FrameworkMessages.BOTAO_CANCELAR);
		}
	}
	
	@Override
	protected BaseDomain screenToDomain() throws SQLException {
		DapLaudo dapLaudo = getDapLaudo();
		dapLaudo.qtArea = edQtArea.getValueDouble();
		dapLaudo.dsAspectoCultura = edDsAspecto.getValue().trim();
		dapLaudo.dsRecomendacoes = edDsRecomendacoes.getValue().trim();
		dapLaudo.dsObservacao = edObs.getValue().trim();
		return dapLaudo;
	}
	
	private DapLaudo getDapLaudo() throws SQLException {
		return isEditing() ? (DapLaudo)getDomain() : this.dapLaudo;
	}

	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		DapLaudo dapLaudo = (DapLaudo) domain;
		lvDsCliente.setValue(dapLaudo.nmRazaoSocial);
		setCpfOrCnpj(dapLaudo);
		lvDsMatriculaDap.setValue(dapLaudo.cdDapMatricula);
		lvDsSequenciaDap.setValue(dapLaudo.nuSeqLaudo);
		lvDsLocal.setValue(dapLaudo.getDsLocal());
		lvDsPosGeografica.setValue(dapLaudo.getPosGeografica());
		lvDsSafra.setValue(dapLaudo.dsSafra);
		lvDsCultura.setValue(dapLaudo.dapCultura.toString());
		lvDtEmissao.setValue(dapLaudo.getDtEmissaoToString());
		edQtArea.setValue(dapLaudo.dapCultura.qtArea);
		edDsAspecto.setValue(dapLaudo.dsAspectoCultura);
		edDsRecomendacoes.setValue(dapLaudo.dsRecomendacoes);
		if (isEditing()) {
			edObs.setValue(dapLaudo.dsObservacao);
			edQtArea.setValue(dapLaudo.qtArea);
			mudaLabelBtVoltar();
			dapLaudo.enviadoServidor = DapLaudoService.getInstance().isDapEnviadoServidor(dapLaudo);
		}
	}

	private void setCpfOrCnpj(DapLaudo dapLaudo) {
		if (dapLaudo.isClientePessoaFisica()) {
			edNuCpf.setValue(dapLaudo.nuCnpj);
		} else {
			edNuCnpj.setValue(dapLaudo.nuCnpj);
		}
	}
	
	@Override
	protected void clearScreen() throws SQLException {
	}
	
	@Override
	protected void visibleState() throws SQLException {
		DapLaudo dapLaudo = getDapLaudo();
		btExcluir.setVisible(isEditing() && dapLaudo.isDapAberto());
		btGerarPdf.setVisible(isEditing() && dapLaudo.enviadoServidor && dapLaudo.isDapFechado());
		btFecharLaudo.setVisible(dapLaudo.isDapAberto() || dapLaudo.isDapEmEdicao() || (dapLaudo.isDapEditado() && !SessionLavenderePda.isUsuarioSupervisor()));
		btCadCoordenadas.setVisible(dapLaudo.isDapAberto());
		btSalvar.setVisible(!dapLaudo.isDapFechado());
		btAssinaturas.setVisible(dapLaudo.isDapFechado());
		edNuCnpj.setVisible(!dapLaudo.isClientePessoaFisica());
		edNuCpf.setVisible(dapLaudo.isClientePessoaFisica());
		btSalvar.setVisible(dapLaudo.isDapAberto() || dapLaudo.isDapEmEdicao());
		btReabrirLaudo.setVisible(dapLaudo.isDapFechado() && SessionLavenderePda.isUsuarioSupervisor());
		btEnviarLaudo.setVisible(dapLaudo.isDapEmEdicao() && SessionLavenderePda.isUsuarioSupervisor());
		setEditableComponents();
	}
	
	private void setEditableComponents() throws SQLException {
		DapLaudo dapLaudo = getDapLaudo();
		edQtArea.setEditable(dapLaudo.isDapAberto() || dapLaudo.isDapEmEdicao());
		edDsAspecto.setEditable(dapLaudo.isDapAberto() || dapLaudo.isDapEmEdicao());
		edDsRecomendacoes.setEditable(dapLaudo.isDapAberto() || dapLaudo.isDapEmEdicao());
		edObs.setEditable(dapLaudo.isDapAberto() || dapLaudo.isDapEmEdicao());
		edNuCpf.setEditable(false);
		edNuCnpj.setEditable(false);
	}
	
	private void btColetaGpsClick() throws SQLException {
		DapLaudo dapLaudo = getDapLaudo();
		LoadingBoxWindow msg = UiUtil.createProcessingMessage(Messages.CAD_COORD_COLETANDO);
		msg.popupNonBlocking();
		int acaoColeta = 0;
		try {
			do {
				if ((VmUtil.isAndroid() || VmUtil.isIOS())) {
					GpsData gpsData = GpsService.getInstance().forceReadData(LavenderePdaConfig.vlTimeOutTentativaColetaGps);
					if (gpsData.isGpsOff()) {
						UiUtil.showWarnMessage(Messages.CAD_COORD_GPS_DESLIGADO);
						return;
					}
					if (gpsData.isSuccess()) {
						dapLaudo.cdLatitude = gpsData.latitude;
						dapLaudo.cdLongitude = gpsData.longitude;
						lvDsPosGeografica.setValue(dapLaudo.getPosGeografica());
						UiUtil.showSucessMessage(Messages.CAD_COORD_SUCESSO);
						acaoColeta = 0;
					} else {
						String[] botoes = new String[] { Messages.CAD_COORD_MAIS_TARDE, Messages.CAD_COORD_TENTAR_NOVAMENTE };
						acaoColeta = UiUtil.showMessage(FrameworkMessages.TITULO_MSG_ATENCAO, Messages.CAD_COORD_COLETA_ERRO, TYPE_MESSAGE.WARN, botoes);
					}
				} else {
					UiUtil.showWarnMessage(Messages.CAD_COORD_PLATAFORMA_NAO_SUPORTADA);
				}
			} while (acaoColeta == 1);
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		} finally {
			msg.unpop();
		}
	}
	
}
