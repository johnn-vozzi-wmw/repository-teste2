package br.com.wmw.lavenderepda.thread;

import java.sql.SQLException;

import br.com.wmw.framework.async.AsyncPool;
import br.com.wmw.framework.async.RunnableImpl;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.WmwMessageBox;
import br.com.wmw.framework.presentation.ui.ext.WmwMessageBox.TYPE_MESSAGE;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.service.ConfigAcessoSistemaService;
import br.com.wmw.lavenderepda.presentation.ui.AdmSenhaDinamicaWindow;
import br.com.wmw.lavenderepda.presentation.ui.MainLavenderePda;
import totalcross.ui.Container;

public class BloqueioAcessoUsuarioSistemaRunnable extends RunnableImpl {

	private static BloqueioAcessoUsuarioSistemaRunnable instance;
	private boolean avisadoBloqueio;
	private boolean ignoraAvisoBloqueio;
	private boolean liberadoSenha;
	private boolean primeiraExecucao = true;
	private boolean feriado;
	private String cdFuncao;
	private static final int CINCO_MINUTOS = 300000;
	private int millisShowAviso = LavenderePdaConfig.nuMinutosAvisoBloqueioSistema * 60000;
	
	
	BloqueioAcessoUsuarioSistemaRunnable(int millis, String cdFuncao, boolean feriado) {
		super(millis);
		this.cdFuncao = cdFuncao;
		this.feriado = feriado;
	}
	
	
	public static BloqueioAcessoUsuarioSistemaRunnable getInstance(int millis, String cdFuncao, boolean feriado) {
		if (instance == null) {
			instance = new BloqueioAcessoUsuarioSistemaRunnable(millis, cdFuncao, feriado);
		}
		return instance;
	}
	
	
	@Override
	public void exec() {
		if (primeiraExecucao) {
			primeiraExecucao = false;
			if (! ignoraAvisoBloqueio) {
				millisShowAviso = millisShowAviso >= millisWait ? millisWait / 2 : millisShowAviso;
				millisWait -= millisShowAviso;
			}
			return;
		}
		if (! avisadoBloqueio && ! ignoraAvisoBloqueio) {
			avisadoBloqueio = true;
			mostraAvisoBloqueio();
			millisWait = millisShowAviso;
			return;
		}
		millisWait = -1;
		bloqueiaUsoSistema();
	}
	
	private void mostraAvisoBloqueio() {
		MainLavenderePda.getInstance().runOnMainThread(new Runnable() {
			@Override
			public void run() {
				UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.MSG_AVISO_BLOQUEIO_SISTEMA, millisShowAviso / 60000));
			}
		}, false);		
	}
	
	private void bloqueiaUsoSistema() {
		closeWarnMessage();
		MainLavenderePda.getInstance().runOnMainThread(new Runnable() {
			@Override
			public void run() {
				try {
					int result = UiUtil.showMessage(Messages.MSG_BLOQUEIO_SISTEMA, TYPE_MESSAGE.WARN, new String[] {Messages.MSG_BOTAO_SAIR, Messages.MSG_BOTAO_SENHA});
					if (result == 1) {
						showPopupSenha();
						if (liberadoSenha) {
							return;
						}
					}
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
				try {
					MainLavenderePda.getInstance().logout();
				} catch (SQLException e) {
					MainLavenderePda.getInstance().simpleExit(true);
				}
				removeQueue();
				Session.setCdUsuario(null);
				MainLavenderePda.getInstance().initUI();
			}
		}, false);	
	}
	

	private void showPopupSenha() throws SQLException {
		AdmSenhaDinamicaWindow senhaWindow = new AdmSenhaDinamicaWindow();
		senhaWindow.setMensagem(Messages.MSG_SENHA_DESBLOQUEAR);
		senhaWindow.setChaveSemente(SenhaDinamica.SENHA_SISTEMA_BLOQUEADO);
		senhaWindow.setCdUsuario(Session.getCdUsuario());
		if (senhaWindow.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
			liberadoSenha = true;
			String tempoLiberado = senhaWindow.edTempoLiberacao.getValueWithMask();
			setNovoTempoMostrarAviso(tempoLiberado);
		}
	}

	private void setNovoTempoMostrarAviso(String tempoLiberado) throws SQLException {
		int seconds = TimeUtil.getSecondsBetween(tempoLiberado + ":00", "00:00:00");
		if (! feriado) {
			seconds = ConfigAcessoSistemaService.getInstance().verificaNovoTempoIncideEmHorarioLiberado(cdFuncao, seconds);
		}
		removeQueue();
		AsyncPool.getInstance().execute(getInstance(seconds * 1000, cdFuncao, feriado));
	}
	

	public static void addQueue(int seconds, String cdFuncao, boolean feriado) {
		int millis = seconds * 1000;
		instance = null;
		instance = getInstance(millis, cdFuncao, feriado);
		if (millis < CINCO_MINUTOS) {
			instance.ignoraAvisoBloqueio = true;
			MainLavenderePda.getInstance().runOnMainThread(new Runnable() {
				@Override
				public void run() {
					UiUtil.showWarnMessage(Messages.MSG_BLOQUEIO_MENOS_5_MIN);
				}
			}, false);	
		}
		AsyncPool.getInstance().execute(instance);
	}

	public static void removeQueue() {
		if (instance != null) {
			instance.stopRunning();
			instance = null;
		}
	}
	

	private void closeWarnMessage() {
		Container cont = MainLavenderePda.getTopMost();
		if (cont instanceof WmwMessageBox) {
			((WmwMessageBox) cont).unpop();
		}
	}
}
