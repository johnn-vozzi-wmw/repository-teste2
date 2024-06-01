package br.com.wmw.lavenderepda.sync.async;

import br.com.wmw.framework.async.RunnableImpl;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.vo.FotoProdutoLazyLoadVO;
import br.com.wmw.lavenderepda.util.FotoProdutoLazyLoadUtil;
import br.com.wmw.lavenderepda.util.Util;

public class FotoProdutoRunnable extends RunnableImpl {
	
	private FotoProdutoLazyLoadVO fotoProdLzLd;
	private boolean cancelExec;

	public FotoProdutoRunnable(FotoProdutoLazyLoadVO fotoProdLzLd) {
		this.fotoProdLzLd = fotoProdLzLd;
	}
	
	@Override
	public void exec() {
		int size = fotoProdLzLd.getProdutoList().size();
		for (int i = 0; i < size; i++) {
			if (cancelExec) {
				return;
			}
			try {
				int indexContainer = i + fotoProdLzLd.getStartIndex();
				ProdutoBase produto = (ProdutoBase) fotoProdLzLd.getProdutoList().items[i];
				produto.imageProduto = Util.getImageForProdutoList(produto, fotoProdLzLd.getImageSize(), true);
				FotoProdutoLazyLoadUtil.updateItemFromListContainer(fotoProdLzLd.getBaseCrudForm(), produto, indexContainer);
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
			}
		}
		VmUtil.executeGarbageCollector();
		FotoProdutoLazyLoadUtil.removeRunnableFromList(this);
	}
	
	@Override
	public void stopRunning() {
		cancelExec = true;
		super.stopRunning();
	}
}
