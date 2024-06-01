package br.com.wmw.lavenderepda.sync;

import java.sql.SQLException;

import br.com.wmw.framework.async.AsyncExecution;
import br.com.wmw.framework.async.AsyncWindow;
import br.com.wmw.framework.notification.Notification;
import br.com.wmw.framework.notification.NotificationConstants;
import br.com.wmw.framework.notification.NotificationManager;
import br.com.wmw.framework.presentation.ui.BaseMainWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.HttpConnection;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.notification.LavendereNotificationConstants;
import br.com.wmw.lavenderepda.presentation.ui.AdmSenhaDinamicaWindow;
import br.com.wmw.lavenderepda.presentation.ui.ListMetaVendaTipoForm;
import br.com.wmw.lavenderepda.presentation.ui.ListNovoClienteAnaForm;
import br.com.wmw.lavenderepda.presentation.ui.ListNovoSacWindow;
import br.com.wmw.lavenderepda.presentation.ui.ListPedidoForm;
import br.com.wmw.lavenderepda.presentation.ui.ListRecadoForm;
import br.com.wmw.lavenderepda.presentation.ui.ListRelNovidadeForm;
import br.com.wmw.lavenderepda.presentation.ui.ListValorIndicadorForm;
import br.com.wmw.lavenderepda.presentation.ui.MainLavenderePda;
import br.com.wmw.lavenderepda.presentation.ui.RelClientesNaoAtendidosForm;
import br.com.wmw.lavenderepda.presentation.ui.RelDiferencasListPedidosWindow;
import br.com.wmw.lavenderepda.presentation.ui.RelErrosSincronizacaoWindow;
import br.com.wmw.lavenderepda.presentation.ui.RelInfoEntregaPedidoForm;
import br.com.wmw.lavenderepda.presentation.ui.RelItemCustoAtualizadoWindow;
import br.com.wmw.lavenderepda.presentation.ui.RelNovosClientesWindow;
import totalcross.ui.MainWindow;
import totalcross.util.Vector;

public class SyncActions {

	public enum SYNC_OPTION {
		RECEBER_DADOS,
		ENVIAR_DADOS,
		ENVIAR_RECEBER_DADOS,
		TESTE_CONEXAO,
		CARGA_INICIAL_FOTO
	}
	
	public static void executeConnection(SYNC_OPTION op, HttpConnection connection) {
		executeConnection(op, connection, false, -1);
	}
	
	public static void executeConnection(SYNC_OPTION op, HttpConnection connection, boolean close, int sleep) {
		new AsyncWindow(new AsyncExecution() {
			AtualizacaoDadosManager atualizacaoManager;
			ParamsSync sync;
			@Override
			public void executeAsync() {
				switch (op) {
				case ENVIAR_DADOS:
					atualizacaoManager.executeEnviarDados();
					if (sleep > 0) {
						LogSync.sucess(Messages.SINCRONIZACAO_MSG_INICIO_RECEBIMENTO);
					}
					break;
				case RECEBER_DADOS:
					atualizacaoManager.executeReceberDados();
					break;
				case TESTE_CONEXAO:
					LogSync.info("Teste conexão");
					break;
				default:
					break;
				}
			}
			
			@Override
			public boolean beforeExecuteAsync() throws SQLException {
				HttpConnectionManager.setHttpConnection(connection);
				sync = HttpConnectionManager.getDefaultParamsSync();
				atualizacaoManager = new AtualizacaoDadosManager(sync);
				return true;
			}

			@Override
			public void afterExecuteASync() {
				afterAsyncProcess();
			}
		}, close, sleep).popup();
	}
	
	public static void afterAsyncProcess() {
		NotificationManager.showNotifications();
	}
	
	public static void showLiberacaoSenhaEnvioPedidoBloqueado(String message) throws SQLException, Exception {
		if (!SessionLavenderePda.autorizadoPorSenhaEnviarPedidosQualquerHorario && UiUtil.showConfirmYesNoMessage(message)) {
			AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
			senhaForm.setMensagem(Messages.PEDIDO_MSG_ENVIO_BLOQUEADO_HORALIMITE_SENHA);
			senhaForm.setChaveSemente(SenhaDinamica.SENHA_SISTEMA_ENVIAR_PEDIDO_BYHORA);
			if (senhaForm.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
				UiUtil.showConfirmMessage("Envio de pedidos liberado com sucesso");
				SessionLavenderePda.autorizadoPorSenhaEnviarPedidosQualquerHorario = true;
				SessionLavenderePda.envioPedidosBloqueadoHoraLimite = false;
				executeConnection(SYNC_OPTION.ENVIAR_DADOS, HttpConnectionManager.getHttpConnection(), true, 1);
			}
		}
	}
	
	public static void showRecebimentoCargaFoto() {
		try {
			SyncCargaInicialFotos syncCargaInicialFotos = new SyncCargaInicialFotos(new LavendereWeb2Tc());
			syncCargaInicialFotos.executeForeGround();
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e);
		}
	}
	
	public static void showListRecado() throws SQLException {
		((BaseMainWindow) MainWindow.getMainWindow()).show(new ListRecadoForm());
	}

	public static void showListValorIndicador() throws SQLException {
		((BaseMainWindow) MainWindow.getMainWindow()).show(new ListValorIndicadorForm(!SessionLavenderePda.isUsuarioSupervisor()));
	}

	public static void showListNovoSac() throws SQLException {
		ListNovoSacWindow listNovoSacWindow = new ListNovoSacWindow();
		listNovoSacWindow.popup();
	}

	public static void showRelMetaVendas() throws SQLException {
		ListMetaVendaTipoForm listMetaVendaFamiliaForm = new ListMetaVendaTipoForm();
		((BaseMainWindow) MainWindow.getMainWindow()).show(listMetaVendaFamiliaForm);
		if (listMetaVendaFamiliaForm.hasMetaVendaAbaixoPlanejadoHoje()) {
			UiUtil.showWarnMessage(Messages.META_VENDA_MSG_ABAIXO_PLANEJADO);
		}
	}

	public static void showNovoClienteAnalise() throws SQLException {
		ListNovoClienteAnaForm listNovoClienteAnaForm = new ListNovoClienteAnaForm();
		listNovoClienteAnaForm.ckApenasPendencias.setChecked(true);
		((BaseMainWindow) MainWindow.getMainWindow()).show(listNovoClienteAnaForm);
	}

	public static void showRelClientesNaoAtendidos() throws SQLException {
		((BaseMainWindow) MainWindow.getMainWindow()).show(new RelClientesNaoAtendidosForm());
	}

	public static void showRelInfoEntregaPedido() throws SQLException {
		((BaseMainWindow) MainWindow.getMainWindow()).show(new RelInfoEntregaPedidoForm());
	}
	
	public static void showRelNovoCliente() throws SQLException {
		RelNovosClientesWindow relCli = new RelNovosClientesWindow();
		relCli.tryPopup();
	}

	public static void showRelDiferencaPedidos() {
		RelDiferencasListPedidosWindow relDiferencasPedidoWindow = new RelDiferencasListPedidosWindow();
		relDiferencasPedidoWindow.popup();
	}
	
	public static void showDetalhesErrosSync() {
		if (LogSync.getErrorList().size() > 0) {
			RelErrosSincronizacaoWindow relErros = new RelErrosSincronizacaoWindow();
			relErros.popup();
		}
	}
	
	public static void exibeMensagemClienteChurn() {
		UiUtil.showWarnMessage(Messages.CLIENTECHURN_NOVOS_REGISTROS);
	}

	public static void showAtualizacaoItemTabelaPreco() throws SQLException {
		Vector listPedidosAtualizados = PedidoService.getInstance().atualizaPedidoEItensAposAlteracaoPreco();
		if (ValueUtil.isNotEmpty(listPedidosAtualizados)) {
			RelItemCustoAtualizadoWindow atualizadoWindow = new RelItemCustoAtualizadoWindow(listPedidosAtualizados);
			atualizadoWindow.popup();
		}
	}
	

	public static void showListPedidoParaGerarNovoPedidoDiferencas() throws SQLException {
		ListPedidoForm listPedidoForm = new ListPedidoForm();
		listPedidoForm.inPedidoDiferentesAposSync = true;
		((BaseMainWindow) MainWindow.getMainWindow()).show(listPedidoForm);
	}

	public static void showNovidades() throws SQLException {
		((BaseMainWindow) MainWindow.getMainWindow()).show(new ListRelNovidadeForm());
	}
	
	public static Notification getUsuarioBloqueadoNotification() {
		return new Notification(NotificationConstants.USUARIO_BLOQUEADO, Messages.ERRO_EQUIPAMENTO_NAO_LIBERADO) {
			@Override
			public void process() throws Exception {
				UiUtil.showErrorMessage(message);
				MainLavenderePda.getInstance().exit();
			}
		};
	}
	
	public static Notification getSyncAndamentoNotification() {
		return new Notification(LavendereNotificationConstants.SYNC_ANDAMENTO, Messages.SINCRONIZACAO_MSG_SYNC_BACKGROUND_EM_ANDAMENTO) {
			@Override
			public void process() throws Exception {
				UiUtil.showWarnMessage(message);
			}
		};
	}
	
	public static Notification getAppUpdateNotification(String msg) {
		return new Notification(LavendereNotificationConstants.ATUALIZACAO_APP, msg) {
			@Override
			public void process() throws Exception {
				SyncExtend.showMessageAtualizaApp(message);
			}
		};
	}

}
