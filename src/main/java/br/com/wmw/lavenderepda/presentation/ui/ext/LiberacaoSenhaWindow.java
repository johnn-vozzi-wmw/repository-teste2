package br.com.wmw.lavenderepda.presentation.ui.ext;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.service.LogPdaService;
import br.com.wmw.lavenderepda.business.service.StatusGpsService;
import br.com.wmw.lavenderepda.presentation.ui.AdmSenhaDinamicaWindow;
import br.com.wmw.lavenderepda.presentation.ui.MainLavenderePda;

public class LiberacaoSenhaWindow {
	
	public static boolean verificaGpsDesligado(boolean menuClick, boolean isMenu) throws SQLException {
		boolean isPreValid = preValidationGps();
		if (!isPreValid) {
			return isPreValid;
		}
		if (StatusGpsService.getInstance().verificaGpsLigado(menuClick, isMenu)) {
			if (!liberaFuncionalidadeSenha(SenhaDinamica.SENHA_SISTEMA_BLOQUEADO_GPS_INATIVO, StatusGpsService.getInstance().getMessageBloqueioFuncaoAtual(isMenu))) {
				if (!menuClick && isMenu) {
					MainLavenderePda.getInstance().simpleExit();
				}
				return false;
			} else {
				SessionLavenderePda.isSistemaLiberadoSenhaGpsOff = true;
			}
		}
		return true;
	}

	private static boolean preValidationGps() throws SQLException {
		if (StatusGpsService.getInstance().isFakeGpsOn()) {
			LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_FAKE_GPS, Messages.LOG_WGPS_FAKE_GPS);
			controlaAcaoInterfaceFakeGps();
			return false;
		}
		return true;
	}

	private static void controlaAcaoInterfaceFakeGps() throws SQLException {
		UiUtil.showErrorMessage(Messages.GPS_ERRO_FAKE_GPS_ON);
		MainLavenderePda.getInstance().simpleExit();
	}

	private static boolean liberaFuncionalidadeSenha(int chaveSemente, String msgBloqueio) throws SQLException {
		AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
		senhaForm.setMensagem(msgBloqueio);
		senhaForm.setChaveSemente(chaveSemente);
		return senhaForm.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA;
	}
}
