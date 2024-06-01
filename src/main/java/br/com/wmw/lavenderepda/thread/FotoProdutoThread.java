package br.com.wmw.lavenderepda.thread;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.presentation.ui.AbstractBaseCadItemPedidoForm;
import br.com.wmw.lavenderepda.presentation.ui.MainLavenderePda;
import br.com.wmw.lavenderepda.util.Util;
import totalcross.sys.Time;
import totalcross.sys.Vm;
import totalcross.util.Vector;
import totalcross.util.concurrent.Lock;

public class FotoProdutoThread extends Thread {

	private static FotoProdutoThread instance;
	
	private boolean keepRunning;
	private static Lock lock = new Lock();
	
	private BaseCrudListForm baseCrudListForm;
	private AbstractBaseCadItemPedidoForm abstractBaseCadItemPedidoForm;
	private Vector produtoList;
	private int imageSize;
	private int nuFotoCarregadas;
	
	private FotoProdutoThread(BaseCrudListForm baseCrudListForm2, Vector produtoList2, int imageSize2) {
		this.baseCrudListForm = baseCrudListForm2;
		this.produtoList = produtoList2;
		this.imageSize = imageSize2;
	}
	
	private FotoProdutoThread(AbstractBaseCadItemPedidoForm baseCrudListForm2, Vector produtoList2, int imageSize2) {
		this.abstractBaseCadItemPedidoForm = baseCrudListForm2;
		this.produtoList = produtoList2;
		this.imageSize = imageSize2;
	}

	public static void start(BaseCrudListForm baseCrudListForm, Vector produtoList, int imageSize) {
		synchronized (lock) {
			if (instance != null) {
				instance.keepRunning = false;
			}
			instance = new FotoProdutoThread(baseCrudListForm, produtoList, imageSize);
			instance.setPriority(Thread.MIN_PRIORITY);
			instance.keepRunning = true;
			instance.start();
		}
	}
	
	public static void start(AbstractBaseCadItemPedidoForm baseCrudListForm, Vector produtoList, int imageSize) {
		synchronized (lock) {
			if (instance != null) {
				instance.keepRunning = false;
			}
			instance = new FotoProdutoThread(baseCrudListForm, produtoList, imageSize);
			instance.setPriority(Thread.MIN_PRIORITY);
			instance.keepRunning = true;
			instance.start();
		}
	}
	
	public static void setToStop() {
		synchronized (lock) {
			if (instance != null) {
				instance.keepRunning = false;
				instance = null;
			}
		}
	}

	@Override
	public void run() {
		int size = produtoList.size();
		long nuTimeLoadFoto = 0;
		for (int i = 0; i < size; i++) {
			try {
				if (nuFotoCarregadas > 10) {
					Vm.sleep(((int)nuTimeLoadFoto * 1000));
				}
				nuTimeLoadFoto = new Time().getTimeLong();
				ProdutoBase produto = (ProdutoBase) produtoList.items[i];
				produto.imageProduto = Util.getImageForProdutoList(produto, imageSize, true);
				try {
					if (! keepRunning){
						break;
					}
					if (baseCrudListForm != null) {
						updateListItem(i, produto);
					}
					if (abstractBaseCadItemPedidoForm != null) {
						if (nuFotoCarregadas == 0) {
							Vm.sleep(500); // Sleep para carregar a primeira foto na lista
						}
						updateItem(i, produto);
					}
					nuFotoCarregadas++;
				} catch (SQLException e) {
					ExceptionUtil.handle(e);
				}
				nuTimeLoadFoto = new Time().getTimeLong() - nuTimeLoadFoto;
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
			}
		}
	}

	private void updateItem(int i, ProdutoBase produto) throws SQLException {
		MainLavenderePda.getInstance().runOnMainThread(new Runnable() {
			@Override
			public void run() {
				try {
					abstractBaseCadItemPedidoForm.updateItem(produto, i);
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			}
		}, false);
	}

	private void updateListItem(int i, ProdutoBase produto) {
		MainLavenderePda.getInstance().runOnMainThread(new Runnable() {
			@Override
			public void run() {
				try {
					baseCrudListForm.updateItem(produto,  i);
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			}
		}, false);
	}

}
