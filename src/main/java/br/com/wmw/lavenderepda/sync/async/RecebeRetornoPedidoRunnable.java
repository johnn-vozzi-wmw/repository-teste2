package br.com.wmw.lavenderepda.sync.async;

import java.sql.SQLException;

import br.com.wmw.framework.async.AsyncPool;
import br.com.wmw.framework.async.RunnableImpl;
import br.com.wmw.framework.business.service.LogSyncBackgroundService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.presentation.ui.MainLavenderePda;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import totalcross.sys.Vm;

public class RecebeRetornoPedidoRunnable extends RunnableImpl {
	
	private static RecebeRetornoPedidoRunnable instance;
	private Pedido pedido;
	private Pedido pedidoBonificado;
	
	public static RecebeRetornoPedidoRunnable getInstance() {
		if (instance == null) {
			instance = new RecebeRetornoPedidoRunnable();
		}
		return instance;
	}

	private void recebeRetorno() {
		while (!SessionLavenderePda.initLockSync()) {
			Vm.safeSleep(1000);
		}
		try {
			try {
				LavendereWeb2Tc erpToPda2 = new LavendereWeb2Tc(HttpConnectionManager.getDefaultParamsSync().configReadTimeout(LavenderePdaConfig.valorTimeOutRetornoDadosRelativosPedido));
				erpToPda2.recebeRetornoPedido(pedido, true);
				if (pedidoBonificado != null && LavenderePdaConfig.isUsaPoliticaBonificacao()) {
					erpToPda2.recebeRetornoPedido(pedidoBonificado, true);
				}
			} catch (ValidationException e) {
				LogSyncBackgroundService.getInstance().insertLogDebug(e.getMessage());
				addMsgAlerta(e.getMessage());
			} catch (Throwable e) {
				LogSyncBackgroundService.getInstance().insertLogDebug(Messages.PEDIDO_MSG_ERRO_RECEBIMENTO_RETORNO_PEDIDO);
				addMsgAlerta(Messages.PEDIDO_MSG_ERRO_RECEBIMENTO_RETORNO_PEDIDO);
			} finally {
				setPedidoBonificado(null);
			}
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		} finally {
			SessionLavenderePda.releaseLockSync();
		}
	}
	
	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}
	
	public void setPedidoBonificado(Pedido pedidoBonificado) {
		this.pedidoBonificado = pedidoBonificado;
	}
	
	private static void addMsgAlerta(String msgAtual) throws SQLException {
		MainLavenderePda.getInstance().runOnMainThread(new Runnable() {
			@Override
			public void run() {
				try {
					BaseUIForm.addMsgAlerta(msgAtual);
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			}
		});
	}

	@Override
	public void exec() {
		recebeRetorno();
	}
	
	public static void addQueue() {
		AsyncPool.getInstance().execute(getInstance());
	}
	
}
