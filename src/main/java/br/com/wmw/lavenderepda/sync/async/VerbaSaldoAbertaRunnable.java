package br.com.wmw.lavenderepda.sync.async;

import br.com.wmw.framework.async.AsyncPool;
import br.com.wmw.framework.async.RunnableImpl;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.VerbaSaldo;
import br.com.wmw.lavenderepda.business.domain.dto.VerbaSaldoDTO;
import br.com.wmw.lavenderepda.business.service.VerbaSaldoService;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.json.JSONObject;

public class VerbaSaldoAbertaRunnable extends RunnableImpl {
	
	private static VerbaSaldoAbertaRunnable instance;
	
	@Override
	public void exec() {
		if (SessionLavenderePda.initLockSync()) {
			try {
					VerbaSaldo verbaSaldo = VerbaSaldoService.getInstance().getVerbaSaldoInstanced(SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, VerbaSaldo.FLORIGEM_ABERTO);
					verbaSaldo = (VerbaSaldo)VerbaSaldoService.getInstance().findByPrimaryKey(verbaSaldo);
					if (verbaSaldo != null) {
						VerbaSaldoDTO dto = new VerbaSaldoDTO(verbaSaldo);
						JSONObject json = new JSONObject(dto);
						SyncManager.enviaDadosVerbaSaldoPedidoAberto(json.toString());
					}
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
			} finally {
				SessionLavenderePda.releaseLockSync();
			}
		}
	}
	
	public static void addQueue() {
		if (instance == null) {
			instance = new VerbaSaldoAbertaRunnable();
		}
		AsyncPool.getInstance().execute(instance);
	}

}
