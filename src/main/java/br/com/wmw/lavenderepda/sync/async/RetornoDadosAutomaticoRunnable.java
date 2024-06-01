package br.com.wmw.lavenderepda.sync.async;

import br.com.wmw.framework.async.AsyncPool;
import br.com.wmw.framework.async.RunnableImpl;
import br.com.wmw.framework.presentation.ui.BaseContainer;
import br.com.wmw.framework.presentation.ui.BaseMainWindow;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.ext.WmwToast;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemNfe;
import br.com.wmw.lavenderepda.business.domain.ItemNfeReferencia;
import br.com.wmw.lavenderepda.business.domain.Nfe;
import br.com.wmw.lavenderepda.business.domain.PedidoBoleto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemNfeDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemNfeReferenciaDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NfeDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoBoletoDao;
import br.com.wmw.lavenderepda.presentation.ui.MainLavenderePda;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import totalcross.sys.Vm;

public class RetornoDadosAutomaticoRunnable extends RunnableImpl {

	private static RetornoDadosAutomaticoRunnable instance = null;

	public static RetornoDadosAutomaticoRunnable getInstance() {
		if (instance == null) {
			instance = new RetornoDadosAutomaticoRunnable();
		}
		return instance;
	}

	protected void atualizaTabelas() {
		while (!SessionLavenderePda.initLockSync()) {
			Vm.safeSleep(1000);
		}
		try {
			Vm.sleep(LavenderePdaConfig.valorTimeOutRetornoDadosRelativosPedido);
			boolean houveAtualizacao = false;
			LavendereWeb2Tc web2Tc = new LavendereWeb2Tc();
			if (LavenderePdaConfig.isUsaRetornoAutomaticoDadosNfeEItemNfeBackground()) {
				if (LavenderePdaConfig.usaNfePorReferencia) {
					houveAtualizacao |= atualizaItemNfeReferencia(web2Tc);
				} else {
					houveAtualizacao |= atualizaItemNfe(web2Tc);
				}
				if (houveAtualizacao) {
					houveAtualizacao |= atualizaNfe(web2Tc);
				}
			}
			if (LavenderePdaConfig.isUsaRetornoAutomaticoDadosPedidoBoletoBackground()) {
				houveAtualizacao |= atualizaPedidoBoleto();
			}
			if (houveAtualizacao) {
				addBtRecebeDadosBackground();
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		} finally {
			SessionLavenderePda.releaseLockSync();
		}
	}

	private boolean atualizaNfe(LavendereWeb2Tc web2Tc) {
		try {
			NfeDao.houveRecebimentoNfeBackground = false;
			NfeDao.erroOcorridoAtualizacao = "";
			web2Tc.getAtualizacoesAndUpdate(Nfe.TABLE_NAME);
		} catch (Throwable e) {
			if (NfeDao.houveRecebimentoNfeBackground) {
				NfeDao.erroOcorridoAtualizacao = MessageUtil.getMessage(Messages.PEDIDO_ERRO_PARCIAL_RECEBIMENTO_NFE_BOLETO_BACKGROUND, Nfe.class.getSimpleName());
			} else {
				NfeDao.erroOcorridoAtualizacao = MessageUtil.getMessage(Messages.PEDIDO_ERRO_RECEBIMENTO_NFE_BOLETO_BACKGROUND, Nfe.class.getSimpleName());
			}
			ExceptionUtil.handle(e);
		}
		return NfeDao.houveRecebimentoNfeBackground;
	}

	private boolean atualizaItemNfe(LavendereWeb2Tc web2Tc) {
		try {
			ItemNfeDbxDao.houveRecebimentoItemNfeBackground = false;
			ItemNfeDbxDao.erroOcorridoAtualizacao = "";
			web2Tc.getAtualizacoesAndUpdate(ItemNfe.TABLE_NAME);
			return ItemNfeDbxDao.houveRecebimentoItemNfeBackground;
		} catch (Throwable e) {
			ItemNfeDbxDao.erroOcorridoAtualizacao = MessageUtil.getMessage(Messages.PEDIDO_ERRO_RECEBIMENTO_NFE_BOLETO_BACKGROUND, ItemNfe.class.getSimpleName());
			ExceptionUtil.handle(e);
			return false;
		}
	}

	private boolean atualizaItemNfeReferencia(LavendereWeb2Tc web2Tc) {
		try {
			ItemNfeReferenciaDao.houveRecebimentoItemNfeReferenciaBackground = false;
			ItemNfeReferenciaDao.erroOcorridoAtualizacao = "";
			web2Tc.getAtualizacoesAndUpdate(ItemNfeReferencia.TABLE_NAME);
			return ItemNfeReferenciaDao.houveRecebimentoItemNfeReferenciaBackground;
		} catch (Throwable e) {
			ItemNfeReferenciaDao.erroOcorridoAtualizacao = MessageUtil.getMessage(Messages.PEDIDO_ERRO_RECEBIMENTO_NFE_BOLETO_BACKGROUND, ItemNfeReferencia.class.getSimpleName());
			ExceptionUtil.handle(e);
			return false;
		}
	}
	
	private boolean atualizaPedidoBoleto() {
		try {
			PedidoBoletoDao.houveRecebimentoBoletoBackground = false;
			PedidoBoletoDao.erroOcorridoAtualizacao = "";
			new LavendereWeb2Tc().getAtualizacoesAndUpdate(PedidoBoleto.TABLE_NAME);
		} catch (Throwable e) {
			if (PedidoBoletoDao.houveRecebimentoBoletoBackground) {
				PedidoBoletoDao.erroOcorridoAtualizacao = MessageUtil.getMessage(Messages.PEDIDO_ERRO_PARCIAL_RECEBIMENTO_NFE_BOLETO_BACKGROUND, PedidoBoleto.class.getSimpleName());
			} else {
				PedidoBoletoDao.erroOcorridoAtualizacao = MessageUtil.getMessage(Messages.PEDIDO_ERRO_RECEBIMENTO_NFE_BOLETO_BACKGROUND, PedidoBoleto.class.getSimpleName());
			}
			ExceptionUtil.handle(e);
		}
		return PedidoBoletoDao.houveRecebimentoBoletoBackground;
	}
	
	private void addBtRecebeDadosBackground() {
		BaseUIForm.btRecebeDadosBackgroundVisible = true;
		BaseContainer baseContainer = BaseMainWindow.getBaseMainWindowInstance().getActualForm();
//		WmwToast.show("Dados recebidos", 3000);
		if (baseContainer instanceof BaseUIForm) {
			MainLavenderePda.getInstance().runOnMainThread(new Runnable() {
				@Override
				public void run() {
					try {
						((BaseUIForm) baseContainer).updateActiveBtRecebeDadosBackground();
					} catch (Exception e) {
						ExceptionUtil.handle(e);
					}
				}
			});
		}
	}

	@Override
	public void exec() {
		atualizaTabelas();
	}
	
	public static void addQueue() {
		if (instance == null) {
			instance = new RetornoDadosAutomaticoRunnable();
			AsyncPool.getInstance().execute(instance);
		}
	}
	
}
