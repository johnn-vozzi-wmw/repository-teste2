package br.com.wmw.lavenderepda.business.domain.vo;

import br.com.wmw.framework.presentation.ui.BaseCrudForm;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.lavenderepda.presentation.ui.AbstractBaseCadItemPedidoForm;
import totalcross.util.Vector;

public class FotoProdutoLazyLoadVO {
	
	private int startIndex;
	private BaseCrudListForm baseCrudListForm;
	private AbstractBaseCadItemPedidoForm abstractBaseCadItemPedidoForm;
	private Vector produtoList;
	private int imageSize;
	
	public FotoProdutoLazyLoadVO(BaseCrudListForm baseCrudListForm, Vector produtoList, int imageSize, int startIndex) {
		this.baseCrudListForm = baseCrudListForm;
		this.produtoList = produtoList;
		this.imageSize = imageSize;
		this.startIndex = startIndex;
	}
	
	public FotoProdutoLazyLoadVO(AbstractBaseCadItemPedidoForm abstractBaseCadItemPedidoForm, Vector produtoList, int imageSize, int startIndex) {
		this.abstractBaseCadItemPedidoForm = abstractBaseCadItemPedidoForm;
		this.produtoList = produtoList;
		this.imageSize = imageSize;
		this.startIndex = startIndex;
	}
	
	public int getStartIndex() {
		return startIndex;
	}
	
	public BaseCrudListForm getBaseCrudListForm() {
		return baseCrudListForm;
	}
	
	public AbstractBaseCadItemPedidoForm getAbstractBaseCadItemPedidoForm() {
		return abstractBaseCadItemPedidoForm;
	}
	
	public BaseCrudForm getBaseCrudForm() {
		return baseCrudListForm == null ? abstractBaseCadItemPedidoForm : baseCrudListForm;
	}
	
	public Vector getProdutoList() {
		return produtoList;
	}
	
	public int getImageSize() {
		return imageSize;
	}
}
