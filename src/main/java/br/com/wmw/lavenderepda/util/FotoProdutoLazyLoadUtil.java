package br.com.wmw.lavenderepda.util;

import java.util.ArrayList;

import br.com.wmw.framework.async.AsyncPool;
import br.com.wmw.framework.presentation.ui.BaseCrudForm;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.vo.FotoProdutoLazyLoadVO;
import br.com.wmw.lavenderepda.presentation.ui.AbstractBaseCadItemPedidoForm;
import br.com.wmw.lavenderepda.presentation.ui.MainLavenderePda;
import br.com.wmw.lavenderepda.sync.async.FotoProdutoRunnable;
import totalcross.util.Vector;
import totalcross.util.concurrent.Lock;

public class FotoProdutoLazyLoadUtil {
	
	private static ArrayList<FotoProdutoRunnable> fotoProdutoRunnableList = new ArrayList<>();
	private static Lock lock = new Lock();
	
	private FotoProdutoLazyLoadUtil() {
		
	}

	public static void startThreadFotoProduto(AbstractBaseCadItemPedidoForm abstractBaseCadItemPedidoForm, Vector produtoList, int imageSize, int startIndex) {
		FotoProdutoLazyLoadVO fotoProdLzLd = new FotoProdutoLazyLoadVO(abstractBaseCadItemPedidoForm, produtoList, imageSize, startIndex);
		FotoProdutoRunnable fotoProdutoRunnable = new FotoProdutoRunnable(fotoProdLzLd);
		fotoProdutoRunnableList.add(fotoProdutoRunnable);
		AsyncPool.getInstance().execute(fotoProdutoRunnable);
	}
	
	public static void startThreadFotoProduto(BaseCrudListForm baseCrudForm, Vector produtoList, int imageSize, int startIndex) {
		FotoProdutoLazyLoadVO fotoProdLzLd = new FotoProdutoLazyLoadVO(baseCrudForm, produtoList, imageSize, startIndex);
		FotoProdutoRunnable fotoProdutoRunnable = new FotoProdutoRunnable(fotoProdLzLd);
		fotoProdutoRunnableList.add(fotoProdutoRunnable);
		AsyncPool.getInstance().execute(fotoProdutoRunnable);
	}
	
	public static void updateItemFromListContainer(BaseCrudForm baseCrudForm, ProdutoBase produto, int i) {
		if (baseCrudForm instanceof BaseCrudListForm) {
			runThreadUpdateItemBaseCrudListForm((BaseCrudListForm)baseCrudForm, produto, i);
		} else if (baseCrudForm instanceof AbstractBaseCadItemPedidoForm) {
			runThreadUpdateItemAbsCadItemPedidoForm((AbstractBaseCadItemPedidoForm)baseCrudForm, produto, i);
		}
	}
	
	private static void runThreadUpdateItemAbsCadItemPedidoForm(AbstractBaseCadItemPedidoForm abstractBaseCadItemPedidoForm, ProdutoBase produto, int i) {
		MainLavenderePda.getInstance().runOnMainThread(new Runnable() {
			@Override
			public void run() {
				abstractBaseCadItemPedidoForm.updateItem(produto, i);
			}
		}, false);
	}
	
	private static void runThreadUpdateItemBaseCrudListForm(BaseCrudListForm baseCrudListForm, ProdutoBase produto, int i) {
		MainLavenderePda.getInstance().runOnMainThread(new Runnable() {
			@Override
			public void run() {
				try {
					baseCrudListForm.updateItem(produto, i);
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			}
		}, false);
	}
	
	public static void stopAllThreadsFotoProdutoLazyLoad() {
		synchronized (lock) {
			for (FotoProdutoRunnable fotoProdutoRunnable : fotoProdutoRunnableList) {
				fotoProdutoRunnable.stopRunning();
			}
			fotoProdutoRunnableList.clear();
		}
	}
	
	public static void removeRunnableFromList(FotoProdutoRunnable fotoProdutoRunnable) {
		synchronized (lock) {
			fotoProdutoRunnableList.remove(fotoProdutoRunnable);
		}
	}

}
