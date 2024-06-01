package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.WmwInputBox;
import br.com.wmw.framework.presentation.ui.ext.WmwMessageBox.TYPE_MESSAGE;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.sync.SyncActions;
import br.com.wmw.lavenderepda.sync.SyncExtend;
import br.com.wmw.lavenderepda.sync.SyncActions.SYNC_OPTION;
import totalcross.sys.Settings;
import totalcross.sys.Time;
import totalcross.util.Date;
import totalcross.util.InvalidDateException;

public class AdmControlUpdateIosUiUtil {
	
	private AdmControlUpdateIosUiUtil() {
		super();
	}
	
	public static void verificaExpiracaoCertificadoIos() throws SQLException {
		if (Session.iOSCertTestMode) {
			if (naoPermiteTestarPeriodoExpiracaoReal()) {
				return;
			}
			WmwInputBox box = new WmwInputBox("data", "Digite um número de dias para incrementar a data de hoje que será colocada como data de expiração.", "2");
			box.popup();
			int a = ValueUtil.getIntegerValue(box.getValue());
			Date today = new Date();
			today.advance(a);
			Settings.iosCertDate = new Time(today);
		} else if (!VmUtil.isIOS()) {
			return;
		}
		try {
			Date blockDate = new Date(Settings.iosCertDate);
			blockDate.advance(-5);
			Date today = new Date();
			if (today.isAfter(blockDate) && !ValueUtil.VALOR_SIM.equals(ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.LIBERADOUTILIZACAOSENHACERTIFICADOIOS))) {
				gerenciaBloqueioAppIos();
			}
			Date warnDate = new Date(Settings.iosCertDate);
			warnDate.advance(-8);
			if (today.isAfter(warnDate)) {
				int qtDays = DateUtil.getDaysBetween(new Date(Settings.iosCertDate), today) - 4;
				if (qtDays >= 2) {
					UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.USUARIO_MSG_ALERTA_EXPIRACAO_CERTIFICADO_EM_BREVE, qtDays));
				} else if (qtDays == 1) {
					UiUtil.showWarnMessage(Messages.USUARIO_MSG_ALERTA_EXPIRACAO_CERTIFICADO_AMANHA);
				} else {
					UiUtil.showWarnMessage(Messages.USUARIO_MSG_ALERTA_EXPIRACAO_CERTIFICADO_HOJE);
				}
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}
	
	public static void gerenciaBloqueioAppIos() throws SQLException {
		final String msg = Messages.USUARIO_MSG_BLOQUEIO_SEGURANCA_CERTIFICADO_EXPIRACAO;
		int result = UiUtil.showMessage(msg, TYPE_MESSAGE.WARN, new String[] {FrameworkMessages.BOTAO_FECHAR, FrameworkMessages.BOTAO_SENHA, FrameworkMessages.BOTAO_ATUALIZAR});
		if (result == 2) {
			atualizaAppIos();
		} else if (result == 1) {
			boolean senhaValida = showTelaLiberacaoSenhaUtilizacaoIos(msg);
			if (senhaValida == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
				ConfigInternoService.getInstance().addValueGeral(ConfigInterno.LIBERADOUTILIZACAOSENHACERTIFICADOIOS, ValueUtil.VALOR_SIM);
				return;
			}
		}
		MainLavenderePda.getInstance().simpleExit();
	}

	private static boolean showTelaLiberacaoSenhaUtilizacaoIos(final String msg) throws SQLException {
		AdmSenhaDinamicaWindow senhaWindow = new AdmSenhaDinamicaWindow();
		senhaWindow.setMensagem(msg);
		senhaWindow.setCdUsuario(Session.getCdUsuario());
		senhaWindow.setChaveSemente(SenhaDinamica.SENHA_CONTROLE_VENCIMENTO_CERTIFICADO_IOS);
		return senhaWindow.show();
	}

	private static void atualizaAppIos() {
		int continuarAtualizacao;
		UiUtil.showWarnMessage(Messages.USUARIO_MSG_CERTIFICAR_ENVIO_DADOS);
		String[] botoes;
		do {
			try {
				SyncActions.executeConnection(SYNC_OPTION.ENVIAR_DADOS, HttpConnectionManager.getHttpConnection(), false, 1);
				botoes = new String[] {FrameworkMessages.BOTAO_FECHAR, Messages.SINCRONIZACAO_LABEL_ENVIAR_DADOS, FrameworkMessages.BOTAO_ATUALIZAR};
			} catch (Exception e) {
				ExceptionUtil.handle(e);
				UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.USUARIO_ERRO_ATUALIZACAO_AUTOMATICA_IOS_SYNC, e.getMessage()));
				botoes = new String[] {FrameworkMessages.BOTAO_FECHAR, Messages.SINCRONIZACAO_LABEL_ENVIAR_DADOS};
			}
			continuarAtualizacao = UiUtil.showMessage(Messages.USUARIO_MSG_CONTINUAR_ATUALIZACAO_AUTOMATICA_IOS, TYPE_MESSAGE.WARN, botoes);
		} while (continuarAtualizacao == 1);
		if (continuarAtualizacao == 2) {
			try {
				SyncExtend.atualizaApp();
			} catch (Exception e) {
				ExceptionUtil.handle(e);
				UiUtil.showErrorMessage(Messages.USUARIO_ERRO_ATUALIZACAO_AUTOMATICA_IOS);
			}
		}
	}

	private static boolean naoPermiteTestarPeriodoExpiracaoReal() {
		if (VmUtil.isIOS()) {
			try {
				Date certDate = new Date(Settings.iosCertDate);
				certDate.advance(-9);
				if (new Date().isAfter(certDate)) {
					return true;
				}
			} catch (InvalidDateException e) {
				ExceptionUtil.handle(e);
			}
		}
		return false;
	}

}
