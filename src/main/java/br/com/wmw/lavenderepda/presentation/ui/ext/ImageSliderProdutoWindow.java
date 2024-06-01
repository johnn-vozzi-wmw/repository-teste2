package br.com.wmw.lavenderepda.presentation.ui.ext;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.ext.ImageCarrousel;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.lavenderepda.business.domain.FotoProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.service.FotoProdutoService;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.util.Vector;

public class ImageSliderProdutoWindow extends BaseImageSliderWindow {

	public String[] imageSelected;

	public ImageSliderProdutoWindow(ProdutoBase produto) throws SQLException {
		this(produto, false);
	}
	
	public ImageSliderProdutoWindow(ProdutoBase produto, boolean isModoGrade) throws SQLException {
		super(ValueUtil.VALOR_NI);
		final Vector imageList;
		if (isModoGrade) {
			imageList = FotoProdutoService.getInstance().geraListaFotoProdutoAgrupadorGradeToCarrousel(produto, null);
		} else {
			imageList = FotoProdutoService.getInstance().geraListaFotoProdutoToCarroussel(produto);
		}
		imageCarrousel = new ImageCarrousel(imageList, Produto.getPathImg(), true);
		setDefaultWideRect();
	}
	
	public ImageSliderProdutoWindow(ProdutoBase produto, String cdItemGrade1, boolean isOnlyFotoProdutoGrade) throws SQLException {
		super(ValueUtil.VALOR_NI);
		String dirFotos;
		Vector imageList;
		if (isOnlyFotoProdutoGrade) {
			imageList = FotoProdutoService.getInstance().geraListaFotoProdutoGradeToCarrousel(produto);
			dirFotos = FotoProdutoGrade.getPathImg();
		} else {
			imageList = FotoProdutoService.getInstance().geraListaFotoProdutoAgrupadorGradeToCarrousel(produto, cdItemGrade1);
			dirFotos = Produto.getPathImg();
		}
		imageCarrousel = new ImageCarrousel(imageList, dirFotos, true);
		setDefaultWideRect();
	}

	public ImageSliderProdutoWindow(Vector produtoList, int posSlide) throws SQLException {
		this(produtoList, posSlide, 0);
	}
	
	public ImageSliderProdutoWindow(Vector produtoList, int posSlide, int qtPaginas) throws SQLException {
		super(ValueUtil.VALOR_NI);
		scBase = new BaseScrollContainer(false, false);
		Vector imgList = FotoProdutoService.getInstance().geraListaImagemCarrousel(produtoList);
		imageCarrousel = new ImageCarrousel(imgList, Produto.getPathImg(), true, qtPaginas);
		if (posSlide != -1) {
			imageCarrousel.setPosVector(posSlide);
		}
		setDefaultWideRect();
	}
	
	public void carregaMaisProdutosNoImageCarrousel(Vector produtoList) {
		if (imageCarrousel != null) {
			try {
				Vector todasFotosProdutos = VectorUtil.concatVectors(imageCarrousel.imgList, FotoProdutoService.getInstance().geraListaImagemCarrousel(produtoList));
				imageCarrousel.setImgList(todasFotosProdutos, imageCarrousel.getSelectedImage(), false);
			} catch (SQLException e) {
				ExceptionUtil.handle(e);
			}
		}
	}

	public ImageSliderProdutoWindow(Vector produtoList, String[] imageSelected) throws SQLException {
		super(ValueUtil.VALOR_NI);
		Vector imgList = FotoProdutoService.getInstance().geraListaImagemCarrousel(produtoList);
		imageCarrousel = new ImageCarrousel(imgList, Produto.getPathImg(), true);
		imageCarrousel.setImgList(FotoProdutoService.getInstance().geraListaImagemCarrousel(produtoList), imageSelected);
		setDefaultWideRect();
	}

	@Override
	public void onWindowEvent(Event event) throws SQLException {
		super.onWindowEvent(event);
		if (event.type == PenEvent.PEN_UP) {
			if (event.target == imageCarrousel.imageControl && imageCarrousel.onValidImageClick && imageCarrousel.imgList.size() >= 1) {
				imageSelected = imageCarrousel.getSelectedImage();
				unpop();
			}
		}
	}

	@Override
	protected void addButtons() {
		// Não há botões adicionais neste tela
	}

	@Override
	protected void btNovaFotoClick() throws SQLException {
		// Botão não utilizado neste tela
	}

	@Override
	protected void btExcluirClick() throws SQLException {
		// Botão não utilizado neste tela
	}
	
}
