package br.com.wmw.lavenderepda.presentation.ui.ext;

import java.sql.SQLException;
import java.util.Map;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseContainer;
import br.com.wmw.framework.presentation.ui.event.KeyboardEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseLabel;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item.ItemToolTip;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.ValueChooser;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoTabPreco;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.domain.SugVendaPerson;
import br.com.wmw.lavenderepda.business.service.ConversaoUnidadeService;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.PlataformaVendaProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoUnidadeService;
import br.com.wmw.lavenderepda.business.service.RemessaEstoqueService;
import br.com.wmw.lavenderepda.business.service.SugVendaPersonService;
import br.com.wmw.lavenderepda.presentation.ui.combo.ProdutoUnidadeComboBox;
import br.com.wmw.lavenderepda.util.Util;
import totalcross.sys.Convert;
import totalcross.sys.SpecialKeys;
import totalcross.ui.Button;
import totalcross.ui.Control;
import totalcross.ui.Edit;
import totalcross.ui.ImageControl;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.ui.event.PenEvent;
import totalcross.ui.gfx.Color;
import totalcross.ui.gfx.Graphics;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class ItemContainer extends BaseContainer {
	
	public enum CAMPOS_LISTA {
		CAMPO_FIXO,
		CAMPO_ESTOQUE,
		CAMPO_VLUN,
		CAMPO_VLT,
		CAMPO_VEP,
		CAMPO_QE,
		CAMPO_QTHIS,
		CAMPO_VLHIST,
		CAMPO_VUE,
		CAMPO_ESTPREVISTO,
		CAMPO_VAZIO;
	}
	
	private static Map<String, SugVendaPerson> sugVendaPersonMap;
	
	public static void setSugVendaPersonMap(Map<String, SugVendaPerson> sugVendaPersonMapItems) {
		sugVendaPersonMap = sugVendaPersonMapItems; 
	}
	
	public double qtdValue, descValue, vlItem, vlItemOriginal, vlBrutoItemOriginal;
	public ValueChooser chooserQtd, chooserDescAcresc, chooserEstoqueCliente;
	public EditNumberFrac edVlBrutoItem, edVlItemPedido;
	public ProdutoUnidadeComboBox cbUnidadeAlternativa;
	public ItemPedido itemPedido;
	private LabelValue lbTitle;
	private LabelName dsQtd, dsDescAcresc, dsUnA, lbVl, dsEstoqueCliente, dsValor;
	private LabelName lbVlt, lbVep, lbQe, lbEstoque, lbQtHist, lbVlHist, lbVue, lbEstPrevisto;
	public ImageControl img;
	private String[] items;
	public ProdutoBase produto;
	private Image[] iconsLegend;
	private boolean enableImageClick, notRollingList, isSugVendaPerson, cadCargaProduto;
	private double max;
	private GridListContainer parent;
	private CAMPOS_LISTA[] posInfos;
	public int index;
	private Image image;
	private int imageSize;
	public Produto produtoItem;
	private int lastWidth;
	
	public boolean changedValue;
	public boolean usaDesconto;
	public boolean usaEstoqueCliente;
	public boolean permiteEditarValorBase;
	public boolean permiteEditarValorUnitario;
	public boolean apresentaUnidadeAlternativa;

	public ItemContainer(String[] items, boolean usaDesc, boolean ocultaDesconto, Image image, CAMPOS_LISTA[] campos, double max, ProdutoBase produto, GridListContainer parent, Vector produtoUnidadeList, int index, boolean isSugVendaPerson, String toolTip) {
		super("");
		this.usaDesconto = usaDesc;
		this.usaEstoqueCliente = LavenderePdaConfig.exibeHistoricoEstoqueCliente;
		this.permiteEditarValorBase = LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao();
		this.permiteEditarValorUnitario = LavenderePdaConfig.isPermiteEditarValorUnitarioMultiplaInsercao();
		this.apresentaUnidadeAlternativa = LavenderePdaConfig.usaUnidadeAlternativa && !LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido();

		this.items = items;
		posInfos = campos;
		boolean quebraLinha = LavenderePdaConfig.usaQuebraLinhaDescricaoProdutoNaLista;
		boolean possuiFoto = LavenderePdaConfig.isExibeFotoInsercaoMultiplos() || LavenderePdaConfig.isMostraFotoProdutoListSugPerson() || LavenderePdaConfig.isMostraFotoProdutoNaListaProdutosDentroPedido();
		int precision = LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : LavenderePdaConfig.nuCasasDecimais;
		chooserQtd = new ValueChooser(0, 1, -1, precision, false);
		chooserQtd.setID("chooserQtd");
		chooserQtd.edF.appId = 1;
		if (usaDesc) {
			if (isHabilitaAcrescimo()) {
				chooserDescAcresc = new ValueChooser(0.01, LavenderePdaConfig.nuCasasDecimais, possuiFoto, true, false);
			} else {
				chooserDescAcresc = new ValueChooser(0, 0.01, max, LavenderePdaConfig.nuCasasDecimais, false);
			}
			this.max = max;
			chooserDescAcresc.inc = 1;
			chooserDescAcresc.edF.appId = 2;
			chooserDescAcresc.setVisible(!ocultaDesconto);
			chooserDescAcresc.setID("chooserDescAcresc");
		}
		if (usaEstoqueCliente) {
			chooserEstoqueCliente = new ValueChooser(1, precision, possuiFoto, false, false);
			chooserEstoqueCliente.setID("chooserEstoqueCliente");
			dsEstoqueCliente = new LabelName(Messages.ITEMPEDIDO_LIST_LABEL_ESTOQUE_ATUAL_CLIENTE);
		}
		transparentBackground = true;
		lbTitle = new LabelValue(items[0] + items[1]);
		new ItemContainerToolTip(lbTitle, quebraLinha ? toolTip : lbTitle.getText());
		dsQtd = new LabelName(Messages.ITEMPEDIDO_LIST_LABEL_QTD);
		if (usaDesc) {
			dsDescAcresc = new LabelName(isHabilitaAcrescimo() ?  getMessageDescAcresc()  : Messages.ITEMPEDIDO_HISTORICO_GRID_DESCONTO);
			dsDescAcresc.setVisible(!ocultaDesconto);
		}
		if (image != null && image != Util.NOIMAGE) {
			imageSize = image.getWidth();
			this.image = UiUtil.getSmoothScaledImage(image, image.getWidth(), image.getHeight());
			img = new ImageControl();
			img.centerImage = true;
			img.setImage(this.image);
			enableImageClick = true;
		} else if (LavenderePdaConfig.isExibeFotoInsercaoMultiplos() || (LavenderePdaConfig.isMostraFotoProdutoListSugPerson() && isSugVendaPerson)) {
			imageSize = UiUtil.getLabelPreferredHeight() * 3;
			Util.prepareDefaultImage(imageSize);
			this.image = UiUtil.getSmoothScaledImage(Util.NOIMAGE, imageSize, imageSize);
			img = new ImageControl();
			img.centerImage = true;
			img.setImage(this.image);
			enableImageClick = false;
		}
		if (permiteEditarValorBase) {
			edVlBrutoItem = new EditNumberFrac("999999999", 9, 2);
			edVlBrutoItem.setID("edVlBrutoItem");
			edVlBrutoItem.appId = 3;
			dsValor = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_ITEMPEDIDO_BRUTO);
		} else if (permiteEditarValorUnitario) {
			dsValor = new LabelName(Messages.ITEMPEDIDO_LABEL_VL);
			edVlItemPedido = new EditNumberFrac("999999999", 9, 2);
			edVlItemPedido.setID("edVlItemPedido");
//			edVlItemPedido.disableValueChangeEvent();
			edVlItemPedido.appId = 3;
		}
		if (apresentaUnidadeAlternativa) {
			cbUnidadeAlternativa = new ProdutoUnidadeComboBox();
			cbUnidadeAlternativa.setID("cbUnidadeAlternativa_item");
			if (ValueUtil.isNotEmpty(produtoUnidadeList)) {
				cbUnidadeAlternativa.add(produtoUnidadeList);
			}
			dsUnA = new LabelName(Messages.ITEMPEDIDO_LABEL_UNIDADE_ALTERNATIVA);
		}
		this.produto = produto;
		this.parent = parent;
		this.index = index;
		configureChooserQtdIncrement();
		setNextTabControlEdits();
	}
	
	private String getMessageDescAcresc() {
		return isHabilitaModoCompacto() ? Messages.ITEM_PEDIDO_MULTIPLA_INSERCAO_ACRESCIMO_DESCONTO : Messages.ITEM_PEDIDO_MULTIPLA_INSERCAO_ACRESCIMO_DESCONTO_VERT;
	}

	private boolean screenResolutionChanged() {
		return lastWidth == 0 || lastWidth != width;
	}

	private void setNextTabControlEdits() {
		setNextTabControlEdit(chooserQtd.edF, edVlBrutoItem != null ? edVlBrutoItem : edVlItemPedido, chooserDescAcresc != null ? chooserDescAcresc.edF : null);
	}

	private void setNextTabControlEdit(Control chooserQtd, Control edVlItemPedido, Control chooserDescAcresc) {
		if (LavenderePdaConfig.isUsaModoExibicaoCompactoMultiplaInsercao()) {
			if (edVlItemPedido != null) {
				chooserQtd.nextTabControl = edVlItemPedido;
				if (chooserDescAcresc != null) {
					edVlItemPedido.nextTabControl = chooserDescAcresc;
					chooserDescAcresc.nextTabControl = chooserQtd;
				} else {
					edVlItemPedido.nextTabControl = chooserQtd;
				}
			} else if (chooserDescAcresc != null) {
				chooserDescAcresc.nextTabControl = chooserQtd;
				chooserQtd.nextTabControl = chooserDescAcresc;
			}
		} else {
			if (edVlItemPedido != null) {
				edVlItemPedido.nextTabControl = chooserQtd;
				if (chooserDescAcresc != null) {
					chooserQtd.nextTabControl = chooserDescAcresc;
					chooserDescAcresc.nextTabControl = edVlItemPedido;
				} else {
					chooserQtd.nextTabControl = edVlItemPedido;
				}
			} else if (chooserDescAcresc != null) {
				chooserDescAcresc.nextTabControl = chooserQtd;
				chooserQtd.nextTabControl = chooserDescAcresc;
			}
		}
	}

	public ItemContainer(String[] items, boolean usaDesc, Image image, CAMPOS_LISTA[] campos, double max, ProdutoBase produto, GridListContainer parent, Vector produtoUnidadeList, int index, boolean isSugVendaPerson) {
		this(items, usaDesc, false, image, campos, max, produto, parent, produtoUnidadeList, index, isSugVendaPerson, "");
		cadCargaProduto = true;
	}
	
	public ItemContainer(String[] items, boolean usaDesc, Image image, CAMPOS_LISTA[] campos, double max, ProdutoBase produto, GridListContainer parent, Vector produtoUnidadeList, ItemPedido itemPedido, int index) {
		this(items, usaDesc, false, image, campos, max, produto, parent, produtoUnidadeList, index, true, "");
		this.itemPedido = itemPedido;
		isSugVendaPerson = true;
	}
	
	@Override
	public void initUI() {
		boolean usaScrollLateral = LavenderePdaConfig.usaScroolLateralListasProdutos && !isSugVendaPerson;
		int size = items.length;
		int leftPos =  LEFT + WIDTH_GAP;
		int rigthPos = usaScrollLateral ? (UiUtil.getFormWidth() - WIDTH_GAP_BIG) : (RIGHT - WIDTH_GAP);
		int controlPrefHeight = UiUtil.getControlPreferredHeight();
		int espacoReservadoIcone = getEspacoReservadoIcones();

		add(lbTitle, leftPos + espacoReservadoIcone, TOP, PREFERRED, PREFERRED);
		if (LavenderePdaConfig.usaQuebraLinhaDescricaoProdutoNaLista) {
			setTitleQuebraLinha();
		}
		if (image != null) {
			if (!isSugVendaPerson) {
				add(img, SAME - espacoReservadoIcone, AFTER, img.getPreferredHeight(), img.getPreferredHeight());
				leftPos = img.getX2() + WIDTH_GAP;
			} else if (LavenderePdaConfig.isMostraFotoProdutoListSugPerson()) {
				leftPos = (imageSize) + WIDTH_GAP * 2;
			}
		}
		if (size > 1 && LavenderePdaConfig.usaQuebraLinhaDescricaoProdutoNaLista) {
			addLabelOnListItem(2, posInfos[2], items[2], leftPos + espacoReservadoIcone, rigthPos, usaScrollLateral);
			}
		addLabelsOnListItem(usaScrollLateral, size, leftPos, rigthPos);
		if (isHabilitaModoCompacto()) {
			adicionaCamposNaMesmaLinha(size, leftPos);
		} else {
			int posicaoYEditValor = 0;
			if (LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao() && !cadCargaProduto) {
			addEditVlItem(usaScrollLateral, size, leftPos, rigthPos, controlPrefHeight, edVlBrutoItem, Messages.ITEMPEDIDO_LABEL_VL_ITEMPEDIDO_BRUTO);
				posicaoYEditValor = edVlBrutoItem.getY();
			} else if (LavenderePdaConfig.isPermiteEditarValorUnitarioMultiplaInsercao() && !cadCargaProduto) {
			addEditVlItem(usaScrollLateral, size, leftPos, rigthPos, controlPrefHeight, edVlItemPedido, Messages.ITEMPEDIDO_LABEL_VL);
				posicaoYEditValor = edVlItemPedido.getY();
		} else {
				int calculaPosicaoX = dsQtd.fm.stringWidth(dsQtd.getText()) + leftPos;
			if (size <= 2) {
					int calculaPosicaoY = img != null ? imageSize - chooserQtd.getPreferredHeight() : AFTER + HEIGHT_GAP;
					add(chooserQtd, calculaPosicaoX, calculaPosicaoY, PREFERRED, controlPrefHeight);
			} else {
					add(chooserQtd, calculaPosicaoX, AFTER, PREFERRED, controlPrefHeight);
			}
		}
		add(dsQtd, leftPos, CENTER_OF, PREFERRED, PREFERRED, chooserQtd);
		

			if (chooserDescAcresc != null) {
				int posicaoY = AFTER + HEIGHT_GAP;
				if (!alinhaLbVlAoLadoEditValor() && posicaoYEditValor != 0) {
					posicaoY = posicaoYEditValor;
		}
			if (!LavenderePdaConfig.exibeHistoricoEstoqueCliente) {
					posicaoY = chooserQtd.getY();
					if (!isHabilitaModoCompacto() && apresentaUnidadeAlternativa
							&& (LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao() || LavenderePdaConfig.isPermiteEditarValorUnitarioMultiplaInsercao()) 
							&& !cadCargaProduto) {
						posicaoY = LavenderePdaConfig.isPermiteEditarValorUnitarioMultiplaInsercao() ? edVlItemPedido.getY() : edVlBrutoItem.getY();
			}
		}
				int calculaPosicaoX = rigthPos - (usaScrollLateral ? chooserDescAcresc.getPreferredWidth() : 0);
				add(chooserDescAcresc, calculaPosicaoX, posicaoY, PREFERRED, PREFERRED, chooserEstoqueCliente);
				chooserDescAcresc.setEnabled(false);
		}
			if (dsDescAcresc != null) {
				add(dsDescAcresc, BEFORE, CENTER_OF, Control.PREFERRED, controlPrefHeight);
			}
			if (LavenderePdaConfig.exibeHistoricoEstoqueCliente) {
				int calculaPosicaoX = rigthPos - (usaScrollLateral ? chooserEstoqueCliente.getPreferredWidth() : 0);
				int posicaoY = chooserQtd.getY();
				if (!alinhaLbVlAoLadoEditValor() && posicaoYEditValor != 0 && chooserDescAcresc == null) {
					posicaoY = posicaoYEditValor;
				}
				add(chooserEstoqueCliente, calculaPosicaoX, posicaoY, PREFERRED, controlPrefHeight);
				add(dsEstoqueCliente, BEFORE, CENTER_OF, Control.PREFERRED, controlPrefHeight);
			}
			if (apresentaUnidadeAlternativa && cbUnidadeAlternativa != null) {
				int calculaPosicaoX = rigthPos - (usaScrollLateral ? chooserQtd.getWidth() : 0);
				if (posicaoYEditValor == 0 && LavenderePdaConfig.exibeHistoricoEstoqueCliente) {
					calculaPosicaoX = dsUnA.fm.stringWidth(dsUnA.getText()) + leftPos;
				}
				if (chooserDescAcresc == null) {
					int posicaoY = LavenderePdaConfig.exibeHistoricoEstoqueCliente ? AFTER : chooserQtd.getY();
					add(cbUnidadeAlternativa, calculaPosicaoX, posicaoY, chooserQtd.getWidth(), controlPrefHeight);
			} else {
					add(cbUnidadeAlternativa, calculaPosicaoX, AFTER, chooserQtd.getWidth(), controlPrefHeight);
			}
			add(dsUnA, BEFORE, CENTER_OF, PREFERRED, controlPrefHeight);
			cbUnidadeAlternativa.setSelectedIndex(0);
		}
	}

	}

	private void addLabelsOnListItem(boolean usaScrollLateral, int size, int leftPos, int rigthPos) {
		if (LavenderePdaConfig.usaQuebraLinhaDescricaoProdutoNaLista) {
			if (size > 2) {
				for (int i = 3; i < size; i++) {
					addLabelOnListItem(i, posInfos[i], items[i], leftPos, rigthPos, usaScrollLateral);
				}
			}
		} else {
			for (int i = 2; i < size; i++) {
				addLabelOnListItem(i, posInfos[i], items[i], leftPos, rigthPos, usaScrollLateral);
			}
		}
	}

	private void adicionaCamposNaMesmaLinha(int size, int leftPos) {
		add(dsQtd, leftPos, AFTER, PREFERRED, PREFERRED);
		if (size <= 2) {
			add(chooserQtd, leftPos, img != null ? imageSize - chooserQtd.getPreferredHeight() : AFTER + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
		} else {
			add(chooserQtd, leftPos, AFTER, PREFERRED, UiUtil.getControlPreferredHeight());
		}
		int sameLineGap = (UiUtil.getFormWidth() - chooserQtd.getPreferredWidth() * 3)/3;
		if (LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao() && !cadCargaProduto) {
			addEditVlItemPedido(edVlBrutoItem, dsValor, sameLineGap);
		} else if (LavenderePdaConfig.isPermiteEditarValorUnitarioMultiplaInsercao() && !cadCargaProduto) {
			addEditVlItemPedido(edVlItemPedido, dsValor, sameLineGap);
		}
		if (dsDescAcresc != null) {
			add(dsDescAcresc, chooserQtd.getX2() + sameLineGap * 2 + chooserQtd.getPreferredWidth(), dsQtd.getY(), PREFERRED, PREFERRED);
		}
		if (chooserDescAcresc != null) {
			add(chooserDescAcresc, SAME, AFTER, PREFERRED, PREFERRED, chooserEstoqueCliente);
			chooserDescAcresc.setEnabled(false);
		}
	}

	private void addEditVlItemPedido(EditNumberFrac editValor, LabelName dsValor, int sameLineGap) {
		add(dsValor, AFTER + sameLineGap, dsQtd.getY(), PREFERRED, PREFERRED);
		add(editValor, SAME, AFTER, chooserQtd.getPreferredWidth(), UiUtil.getControlPreferredHeight());
		editValor.setEnabled(false);
	}

	private void configureChooserQtdIncrement() {
		if (!LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecial()) return;
		try {
			double nuMultiploEspecialProduto = 0;
			if (this.produto instanceof Produto) {
				nuMultiploEspecialProduto = ((Produto) this.produto).nuMultiploEspecial;
			} else if (this.produto instanceof ProdutoTabPreco) {
				nuMultiploEspecialProduto = ((ProdutoTabPreco) this.produto).nuMultiploEspecialProduto;
			}
			double nuMultiploEspecial;
			if (LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecialUnidadeAlternativa()) {
				nuMultiploEspecial = ProdutoUnidadeService.getInstance().getMultiploEspecialUnidadeAlternativa(itemPedido, produto, cbUnidadeAlternativa.getValue());
				if (produtoItem != null) produtoItem.nuMultiploEspecialUnidadeAlternativa = nuMultiploEspecial;
				if (nuMultiploEspecial != 0) {
					chooserQtd.inc = nuMultiploEspecial;
					return;
				}
			}
			if (produto.cdProduto != null && nuMultiploEspecialProduto != 0) {
				chooserQtd.inc = nuMultiploEspecialProduto;
			}
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
	}

	public void addEditVlItem(boolean usaScrollLateral, int size, int leftPos, int rigthPos, int controlPrefHeight, Control control, String label) {
		if (size == 4 && lbVl != null &&  lbVl.isDisplayed() && !lbVlt.isDisplayed()) {
			add(control, dsQtd.fm.stringWidth(dsQtd.getText()) + leftPos, lbTitle.getY2() + WIDTH_GAP, chooserQtd.getPreferredWidth(), controlPrefHeight);
		} else if (size <= 2) {
			add(control, dsQtd.fm.stringWidth(dsQtd.getText()) + leftPos, img != null ? imageSize - chooserQtd.getPreferredHeight() : AFTER + HEIGHT_GAP, PREFERRED, controlPrefHeight);
		} else {
			add(control, dsQtd.fm.stringWidth(dsQtd.getText()) + leftPos, AFTER, chooserQtd.getPreferredWidth(), controlPrefHeight);
		}
		control.setEnabled(false);
		add(new LabelName(label), leftPos, CENTER_OF, PREFERRED, PREFERRED, control);
		if (lbVl != null && alinhaLbVlAoLadoEditValor()) {
			add(lbVl, rigthPos - (usaScrollLateral ? fm.stringWidth(lbVl.getText()) : 0), control.getY());
		}
		if (lbVlt != null && alinhaLbVlAoLadoEditValor()) {
		add(lbVlt, rigthPos - (usaScrollLateral ? fm.stringWidth(lbVlt.getText()) : 0), lbVl != null ? AFTER : control.getY());
	}
		add(chooserQtd, dsQtd.fm.stringWidth(dsQtd.getText()) + leftPos, AFTER, PREFERRED, controlPrefHeight, control);
	}
	
	@Override
	public void reposition() {
		super.reposition();
		if (LavenderePdaConfig.usaQuebraLinhaDescricaoProdutoNaLista && screenResolutionChanged()) {
			setTitleQuebraLinha();
		}
		if (apresentaUnidadeAlternativa && cbUnidadeAlternativa != null && !isHabilitaModoCompacto()) {
			boolean isCbUnidadeNaEsquerda = !LavenderePdaConfig.isPermiteEditarValorUnitarioMultiplaInsercao() && !LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao();
			isCbUnidadeNaEsquerda &= !cadCargaProduto && LavenderePdaConfig.exibeHistoricoEstoqueCliente;
			if (isCbUnidadeNaEsquerda) {
				cbUnidadeAlternativa.setRect(KEEP, KEEP, chooserQtd.getWidth(), KEEP);
			} else { 
				cbUnidadeAlternativa.setRect(RIGHT - WIDTH_GAP, KEEP, chooserQtd.getWidth(), KEEP);
			}
			dsUnA.setRect(BEFORE, KEEP, PREFERRED, KEEP);
		}
		if (LavenderePdaConfig.usaScroolLateralListasProdutos && !isSugVendaPerson) {
			for (Control control = children; control != null; control = control.getNext()) {
				int screenX = UiUtil.getFormWidth() / 3;
				if (control == dsUnA) {
					control.setRect(UiUtil.getFormWidth() - control.getWidth() - WIDTH_GAP_BIG - cbUnidadeAlternativa.getWidth(), KEEP, KEEP, KEEP);
				} else if (control == dsDescAcresc) {
					control.setRect(UiUtil.getFormWidth() - control.getWidth() - chooserDescAcresc.getPreferredWidth(), KEEP, KEEP, KEEP);
				} else if (control.getX() > screenX && control != dsDescAcresc) {
					control.setRect(UiUtil.getFormWidth() - WIDTH_GAP_BIG - control.getWidth(), KEEP, KEEP, KEEP);
				}
			}
		}
		if (isHabilitaModoCompacto()) {
			int sameLineGap = (UiUtil.getFormWidth() - chooserQtd.getPreferredWidth() * 3)/3;
			int widthGap = chooserQtd.getX2() + sameLineGap * 2 + chooserQtd.getPreferredWidth();
		if (edVlBrutoItem != null) {
				dsValor.setRect(chooserQtd.getX2() + sameLineGap, KEEP, KEEP, KEEP);
				edVlBrutoItem.setRect(SAME, KEEP, chooserQtd.getPreferredWidth(), KEEP);
			}
			if (edVlItemPedido != null) {
				dsValor.setRect(chooserQtd.getX2() + sameLineGap, KEEP, KEEP, KEEP);
				edVlItemPedido.setRect(SAME, KEEP, chooserQtd.getPreferredWidth(), KEEP);
			}
			if (chooserDescAcresc != null) {
				dsDescAcresc.setRect(widthGap, KEEP, KEEP, KEEP);
				chooserDescAcresc.setRect(SAME, KEEP, KEEP, KEEP);
			}
		} else {
			if (edVlBrutoItem != null) {
			edVlBrutoItem.setRect(KEEP, KEEP, chooserQtd.getPreferredWidth(), KEEP);
		}
		if (edVlItemPedido != null) {
			edVlItemPedido.setRect(KEEP, KEEP, chooserQtd.getPreferredWidth(), KEEP);
		}
	}
	}
	
	protected void setTitleQuebraLinha() {
		int width = this.width - getEspacoReservadoIcones();
		String title = Convert.insertLineBreak(width, fm, items[0] + items[1]) ;
		int index = title.indexOf('\n');
		if (index > 0) {
			title = title.substring(0, index + 1) + StringUtil.getStringAbreviada(title.substring(index + 1), width, font);
		}
		lbTitle.setText(Convert.insertLineBreak(width, fm, title));
		lastWidth = width;
	}

	@Override
	public int getPreferredHeight() {
		if (isHabilitaModoCompacto()) {
			return chooserQtd.getY2() + HEIGHT_GAP_BIG;
		}
		int size1 = (chooserDescAcresc != null ? chooserDescAcresc.getY2(): chooserQtd.getY2()) + HEIGHT_GAP_BIG;
		if (apresentaUnidadeAlternativa && cbUnidadeAlternativa != null) {
			size1 = cbUnidadeAlternativa.getY2() + HEIGHT_GAP_BIG;
		}
		int size2 = 0;
		if (img != null) {
			size2 = lbTitle.getPreferredHeight() + img.getPreferredHeight() + HEIGHT_GAP_BIG;
		}
		return size1 > size2 ? size1 : size2;
	}
	
	public void setlbVl(String text) {
		if (lbVl != null) {
			lbVl.setText(text);
		}
	}
	
	public void setTotalItem(String text) {
		if (lbVlt != null) {
		lbVlt.setText(text);
			}
		}
	
	public void setValuesOnList(ItemPedido itemPedido) throws SQLException {
		setChooserQtdValue(itemPedido.getQtItemFisico());
		if (LavenderePdaConfig.exibeHistoricoEstoqueCliente) {
			chooserEstoqueCliente.setValue(itemPedido.qtEstoqueCliente);
		}
		boolean reposition = false;
		if (edVlBrutoItem != null) {
			edVlBrutoItem.setEnabled(itemPedido.getQtItemFisico() > 0);
		}

		if (edVlItemPedido != null) {
			edVlItemPedido.setEnabled(itemPedido.getQtItemFisico() > 0);
		}

		refreshEnableState(itemPedido);

		ProdutoUnidade produtoUnidade = itemPedido.getProdutoUnidade();
		for (int i = 0; i < posInfos.length; i++) {
			switch (posInfos[i]) {
			case CAMPO_VLUN:
				lbVl.setText(Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(itemPedido.vlItemPedido));
				reposition = lbVl.getX() > width / 2;
				break;
			case CAMPO_ESTOQUE:
				if (itemPedido.getProduto().estoque != null) {
					double qtEstoque = itemPedido.getProduto().estoque.qtEstoque;
					if (LavenderePdaConfig.isPermiteSelecaoUnidadeAlternativa() && LavenderePdaConfig.usaUnidadeAlternativa && !LavenderePdaConfig.usaControleEstoquePorRemessa) {
						qtEstoque = EstoqueService.getInstance().calculaEstoqueByProdutoUnidade(itemPedido.getItemTabelaPreco(), itemPedido.getProdutoUnidade(), qtEstoque);
					}
					lbEstoque.setText(EstoqueService.getInstance().getEstoqueToString(qtEstoque) + Messages.PRODUTO_LABEL_EM_ESTOQUE);
				} else {
					lbEstoque.setText(EstoqueService.getInstance().getEstoqueToString(getQtEstoqueList(itemPedido)) + Messages.PRODUTO_LABEL_EM_ESTOQUE);
				}
				break;
			case CAMPO_QE:
				lbQe.setText(Messages.ITEMPEDIDO_LABEL_CAIXAPADRAO + " " + itemPedido.getQtEmbalagemElementarToInterface());
				reposition = lbQe.getX() > width / 2;
				break;
			case CAMPO_VEP:
				lbVep.setText(Messages.ITEMPEDIDO_LABEL_PRECOEMBPRIMARIA + " " + StringUtil.getStringValueToInterface(itemPedido.vlEmbalagemElementar));
				reposition = lbVep.getX() > width / 2;
				break;
			case CAMPO_VLT:
				lbVlt.setText(Messages.ITEMPEDIDO_LABEL_VLTABELAITEMPEDIDO + " " + StringUtil.getStringValueToInterface(itemPedido.vlTotalItemPedido));
				reposition = lbVlt.getX() > width / 2;
				break;
			case CAMPO_QTHIS:
				setQtHist(produtoUnidade);
				reposition = lbQtHist.getX() > width / 2;
				break;
			case CAMPO_VLHIST:
				setVlHist(produtoUnidade);
				reposition = lbVlHist.getX() > width / 2;
				break;
			case CAMPO_VUE:
				lbVue.setText(Messages.ITEMPEDIDO_LABEL_VL_UNIDADE_RENTABILIDADE + " " + StringUtil.getStringValueToInterface(ItemPedidoService.getInstance().calculaVlUnidadePorEmbalagemWhenProdutoBase(itemPedido, itemPedido.pedido, produto, null))); 
				reposition = lbVue.getX() > width / 2;
				break;
			case CAMPO_ESTPREVISTO:
				Estoque previsto;
				if (itemPedido.getProduto().estoque != null) {
					previsto = itemPedido.getProduto().estoque;
				} else {
					previsto = EstoqueService.getInstance().findDadosPrevistoParaEstoqueToInterface(itemPedido);
				}
				if (previsto != null) {
					lbEstPrevisto.setText(EstoqueService.getInstance().getEstoquePrevistoToString(previsto.qtEstoquePrevisto) + Messages.PRODUTO_LABEL_ESTOQUE_PREVISTO);
					reposition = lbEstPrevisto.getX() > width / 2;
				}
				break;
			default:
				break;
			}
		}
		if (reposition) {
			reposition();
		}
		if (lbVl != null) {
			lbVl.setText(Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(itemPedido.vlItemPedido));
		}
		if (lbVlt != null) {
			setTotalItem(Messages.ITEMPEDIDO_LABEL_VLTABELAITEMPEDIDO + " " + StringUtil.getStringValueToInterface(itemPedido.vlTotalItemPedido));
		}
		if (LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao()) {
			if (LavenderePdaConfig.usaUnidadeAlternativa) {
				setVlBrutoItem(itemPedido.vlUnidadePadrao);
			} else {
				setVlBrutoItem(itemPedido.vlBaseItemTabelaPreco);
			}
		} else if (LavenderePdaConfig.isPermiteEditarValorUnitarioMultiplaInsercao()) {
			setEdVlItemPedido(itemPedido.vlItemPedido);
		}
		if (LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao()) {
			this.itemPedido = (ItemPedido) itemPedido.clone();
		}
		if (LavenderePdaConfig.isPermiteSelecaoUnidadeAlternativa()) {
			cbUnidadeAlternativa.setValue(itemPedido.cdUnidade, itemPedido.cdProduto, itemPedido.cdItemGrade1);
			configureChooserQtdIncrement();
		}
		setColorsEditsByConversaoUnidade();
	}

	public void refreshEnableState(ItemPedido itemPedido) {
		refreshChooserDescAcresc(itemPedido);
		if (edVlBrutoItem != null) {
			edVlBrutoItem.setEnabled(itemPedido.qtItemFisico > 0);
		}

		if (edVlItemPedido != null) {
			edVlItemPedido.setEnabled(itemPedido.qtItemFisico > 0);
		}
	}

	public void refreshChooserDescAcresc(ItemPedido itemPedido) {
		if (chooserDescAcresc != null) {
			if (itemPedido.getQtItemFisico() > 0) {
				setChooserDesc(itemPedido.getVlPctDescontoAcrescimo());
				chooserDescAcresc.setEnabled(true);
				chooserDescAcresc.edF.setEnabled(true);
			} else {
				try {
					double vlPctDescontoAcrescimoDefaultItem = itemPedido.getVlPctDescontoAcrescimoDefaultItem();
					double vlPctMaxDescontoItemTabelaPreco = itemPedido.getItemTabelaPreco() != null ? itemPedido.getItemTabelaPreco().getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto()) : 0d;
					if (vlPctMaxDescontoItemTabelaPreco < vlPctDescontoAcrescimoDefaultItem) {
						setChooserDesc(vlPctMaxDescontoItemTabelaPreco);
					} else {
						setChooserDesc(vlPctDescontoAcrescimoDefaultItem);
					}
					chooserDescAcresc.setEnabled(false);
					chooserDescAcresc.edF.setEnabled(false);
				} catch (Exception e) {
					ExceptionUtil.handle(e, e.getMessage());
				}
			}
		}
	}

	public void setQtHist(ProdutoUnidade produtoUnidade) {
		if (produtoUnidade != null && lbQtHist != null) {
			lbQtHist.setText(Messages.MULTIPLASSUGESTOES_LABEL_QTHIST + " " + StringUtil.getStringValueToInterface(produtoUnidade.isMultiplica() ? produto.qtdMediaHistorico * produtoUnidade.nuConversaoUnidade : produto.qtdMediaHistorico / produtoUnidade.nuConversaoUnidade));
		}
	}

	public void setVlHist(ProdutoUnidade produtoUnidade) {
		if (produtoUnidade != null && lbVlHist != null) {
			lbVlHist.setText(Messages.MULTIPLASSUGESTOES_LABEL_VLHIST + " " + StringUtil.getStringValueToInterface(produtoUnidade.isMultiplica() ? produto.vlMedioHistorico * produtoUnidade.nuConversaoUnidade : produto.vlMedioHistorico / produtoUnidade.nuConversaoUnidade));
		}
	}
	
	public void setQe(String text) {
		if (lbQe != null) {
			lbQe.setText(text);
		}
	}
	
	public void setVep(String text) {
		if (lbVep != null) {
			lbVep.setText(text);
		}
	}
	
	public void setVue(String text) {
		if (lbVue != null) {
			lbVue.setText(text);
		}
	}
	
	public void setQtEstoque(String text) {
		if (lbEstoque != null) {
			lbEstoque.setText(text);
		}
	}
	
	public void setVisibleLbEstoque(boolean isVisible) {
		if (lbEstoque != null) {
			lbEstoque.setVisible(isVisible);
		}
	}
	public void setVisibleLbsVlItemPedido(boolean isVisible) {
		if (lbVlt != null) {
			lbVlt.setVisible(isVisible);
		}
		if (lbVl != null) {
			lbVl.setVisible(isVisible);
		}
	}

	public void clear(ItemPedido item) {
		if (chooserDescAcresc != null) {
			setChooserDesc(item.getVlPctDescontoAcrescimo());
			chooserDescAcresc.setEnabled(false);
			chooserDescAcresc.edF.setEnabled(false);
		}
		setChooserQtdValue(0);
		if (LavenderePdaConfig.exibeHistoricoEstoqueCliente) {
			chooserEstoqueCliente.setValue(0);
		}
		chooserQtd.setEdForeColor(ColorUtil.componentsForeColor);
		if (lbVl != null) {
			lbVl.setText(Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(item.vlItemPedido));
		}
		if (lbVlt != null) {
			lbVlt.setText(Messages.ITEMPEDIDO_LABEL_VLTABELAITEMPEDIDO + " " + StringUtil.getStringValueToInterface(0d));
		}
		if (edVlBrutoItem != null) {
			setVlBrutoItem(item.vlBaseItemPedido);
	}
		if (edVlItemPedido != null) {
			setEdVlItemPedido(item.vlItemPedido);
		}
	}

	@Override
	protected void onFormStart() {
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target instanceof Button) {
					if (chooserQtd != null && (event.target == chooserQtd.btMais || event.target == chooserQtd.btMenos)) {
						changeEnableValorItemEDescAcresc(false);
						}

					if (isSugVendaPerson) {
						buttonClickSugVenda();
					}
				}
				if (event.target == cbUnidadeAlternativa) {
					changeUnidadeAlternativa(event);
				}
				break;
			case PenEvent.PEN_DOWN:
				notRollingList = true;
				break;
			case KeyboardEvent.KEYBOARD_PRESS:
				setColorsEditsByConversaoUnidade();
				break;
			case KeyEvent.KEY_PRESS:
				setColorsEditsByConversaoUnidade();
				if (chooserQtd != null && event.target == chooserQtd.edF) {
					changeEnableValorItemEDescAcresc(true);
				}
				break;
			case KeyEvent.SPECIAL_KEY_PRESS: {
				KeyEvent ke = (KeyEvent) event;
				Control parent = ((Control)event.target).getParent();
				if (ke.key == SpecialKeys.BACKSPACE && parent == chooserQtd) {
					setColorsEditsByConversaoUnidade();
				}
//				if ((ke.isDownKey() || ke.isUpKey()) && parent instanceof ValueChooser) {
//					((ValueChooser)parent).postArrowKeyEvent();
//				}
				break;
			}
			case PenEvent.PEN_DRAG_START:
				notRollingList = false;
				break;
			case ControlEvent.FOCUS_OUT:
				if (event.target instanceof EditNumberFrac) {
					if (chooserQtd != null && event.target == chooserQtd.edF) {
						changeEnableValorItemEDescAcresc(false);
						}

					Control c = (Control)event.target;
					if (c.getParent() instanceof ValueChooser) {
						if (isSugVendaPerson) {
							buttonClickSugVenda();
						}
						if (c.getParent() == chooserDescAcresc) {
							if (chooserDescAcresc.getValue() > max && max > 0) {
								chooserDescAcresc.setValue(max);
								UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_ERRO_ULTRAPASSOU_MAX_DESC_CLIENTE, max));
							}
						}
					}
				}
				break;
			case PenEvent.PEN_UP:
				if (notRollingList) {
					if (event.target == img && enableImageClick) {
					imageClick();
				} else if (event.target == this || event.target instanceof BaseButton) {
					parent.postEvent(event);
				} else if (event.target instanceof BaseLabel) {
					event.target = this;
					parent.postEvent(event);
				}
				} else {
					if (!(event.target instanceof Edit)) {
						event.consumed = true;
					}
				}
				break;
		}
	}

	private void changeEnableValorItemEDescAcresc(boolean numberPress) {
		double quantidade = numberPress ? ValueUtil.getIntegerValue(chooserQtd.edF.getText()) : chooserQtd.getValue() ;
		boolean possuiQuantidade = quantidade > 0;

		if (chooserDescAcresc != null) {
			if (possuiQuantidade) {
				chooserDescAcresc.setEnabled(true);
				chooserDescAcresc.edF.setEnabled(true);
			} else {
				if(!LavenderePdaConfig.isUsaDescontoOuAcrescimoAplicadoPorItem()) setChooserDesc(0);
				chooserDescAcresc.setEnabled(false);
				chooserDescAcresc.edF.setEnabled(false);
			}
		}

		if (edVlBrutoItem != null) {
			edVlBrutoItem.setEnabled(possuiQuantidade);
		}

		if (edVlItemPedido != null) {
			edVlItemPedido.setEnabled(possuiQuantidade);
		}
	}

	public void setColorsEditsByConversaoUnidade() throws SQLException {
		if ((LavenderePdaConfig.isConsisteConversaoUnidades() && !cadCargaProduto) || LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecial()) {
			if (produtoItem == null) {
				produtoItem = ProdutoService.getInstance().getProduto(produto.cdProduto);
				if (LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecialUnidadeAlternativa()) {
					produtoItem.nuMultiploEspecialUnidadeAlternativa = ProdutoUnidadeService.getInstance().getMultiploEspecialUnidadeAlternativa(itemPedido, produto, cbUnidadeAlternativa.getValue());
			}
			}
			if (produtoItem != null && (!(ValueUtil.VALOR_NAO.equals(produtoItem.flConsisteConversaoUnidade))) && chooserQtd.getValue() != 0) {
				if (LavenderePdaConfig.isConsisteConversaoUnidades() || LavenderePdaConfig.avisaConversaoUnidades || LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecial()) {
					double nuConversaoUnidade = LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecial() ? produtoItem.nuMultiploEspecial : produtoItem.nuConversaoUnidadesMedida;
					nuConversaoUnidade = LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecialUnidadeAlternativa() && produtoItem.nuMultiploEspecialUnidadeAlternativa != 0d ? produtoItem.nuMultiploEspecialUnidadeAlternativa : nuConversaoUnidade;
					if (nuConversaoUnidade != 0) {
						if (ConversaoUnidadeService.getInstance().isConversaoUnidadeCompleta(nuConversaoUnidade, chooserQtd.getValue())) {
							int softGreen;
							if(LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecial()){
								softGreen = Color.darker(ColorUtil.softGreen);
						} else {
								softGreen = ColorUtil.isLightTheme() ? Color.darker(ColorUtil.softGreen) : ColorUtil.softGreen;
						}
							chooserQtd.setEdForeColor(softGreen);
						} else {
							int softRed;
							if(LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecial()){
								softRed = Color.darker(ColorUtil.softRed);
							}else {
								softRed = ColorUtil.isLightTheme() ? Color.darker(ColorUtil.softRed) : ColorUtil.softRed;
					}
							chooserQtd.setEdForeColor(softRed);
				}
					}
				}
			} else {
				chooserQtd.setEdForeColor(ColorUtil.isLightTheme() ? Color.darker(ColorUtil.componentsForeColor) : ColorUtil.componentsForeColor);
			}
		}
	}
	
	private void buttonClickSugVenda() throws SQLException {
		if (itemPedido == null) return;
		if (LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda() && PlataformaVendaProdutoService.getInstance().isNotExistsProdutoInPlataformaVendaProduto(itemPedido.pedido, itemPedido)) {
			chooserQtd.setValue(0);
			throw new ValidationException(Messages.PRODUTO_SEM_PLATAFORMA_ERRO);
		}
		itemPedido.setQtItemFisico(chooserQtd.getValue());
		PedidoService.getInstance().calculateItemPedido(itemPedido.pedido, itemPedido, false);
		setValuesOnList(itemPedido);
		boolean isChecked = !parent.isChecked(index);
		if (itemPedido.getQtItemFisico() == 0 && isChecked || !isChecked) {
			parent.checkLine(index, itemPedido.getQtItemFisico() > 0);
		}
	}
	
	private void imageClick() throws SQLException {
		ImageSliderProdutoWindow imageSliderProdutoWindow = new ImageSliderProdutoWindow(produto);
		imageSliderProdutoWindow.popup();
	}
	
	private void changeUnidadeAlternativa(Event event) throws SQLException {
		if (isSugVendaPerson) {
			ItemPedidoService.getInstance().changeUnidadeAlternativa(itemPedido, cbUnidadeAlternativa.getValue());
			setValuesOnList(itemPedido);
			reposition();
		} else {
			parent.setSelectedIndex(index);
		}
		configureChooserQtdIncrement();
	}
	
	private void addLabelOnListItem(int i, CAMPOS_LISTA campoPos, String dsLabel, int leftPos, int rigthPos, boolean usaScrollLateral) {
		dsLabel = dsLabel != null ? dsLabel : "-";
		boolean left = i % 2 == 0;
		switch (campoPos) {
			case CAMPO_VLUN:
				if (alinhaLbVlAoLadoEditValor()) {
					lbVl = new LabelName(dsLabel);
				} else {
					if (i < 4) {
						add(lbVl = new LabelName(dsLabel), left ? leftPos : rigthPos - (usaScrollLateral ? fm.stringWidth(dsLabel) : 0), left ? lbTitle.getY2() : SAME);
					} else {
						add(lbVl = new LabelName(dsLabel), left ? leftPos : rigthPos - (usaScrollLateral ? fm.stringWidth(dsLabel) : 0), left ? AFTER : SAME);
					}
				}
				break;
			case CAMPO_ESTOQUE:
				if (i < 4) {
					add(lbEstoque = new LabelName(dsLabel), left ? leftPos : rigthPos - (usaScrollLateral ? fm.stringWidth(dsLabel) : 0), left ? lbTitle.getY2() : SAME);
				} else {
					add(lbEstoque = new LabelName(dsLabel), left ? leftPos : rigthPos - (usaScrollLateral ? fm.stringWidth(dsLabel) : 0), left ? AFTER : SAME);
				}
				break;
			case CAMPO_VLT:
				if (alinhaLbVlAoLadoEditValor()) {
					lbVlt = new LabelName(dsLabel);
				} else {
					if (i < 4) {
						add(lbVlt = new LabelName(dsLabel), left ? leftPos : rigthPos - (usaScrollLateral ? fm.stringWidth(dsLabel) : 0), left ? lbTitle.getY2() : SAME);
					} else {
						add(lbVlt = new LabelName(dsLabel), left ? leftPos : rigthPos - (usaScrollLateral ? fm.stringWidth(dsLabel) : 0), left ? AFTER : SAME);
					}
				}
				lbVlt.setID("lbVlt");
				break;
			case CAMPO_QE:
				if (i < 4) {
					add(lbQe = new LabelName(dsLabel), left ? leftPos : rigthPos - (usaScrollLateral ? fm.stringWidth(dsLabel) : 0), left ? lbTitle.getY2() : SAME);
				} else {
					add(lbQe = new LabelName(dsLabel), left ? leftPos : rigthPos - (usaScrollLateral ? fm.stringWidth(dsLabel) : 0), left ? AFTER : SAME);
				}
				break;
			case CAMPO_VEP:
				if (i < 4) {
					add(lbVep = new LabelName(dsLabel), left ? leftPos : rigthPos - (usaScrollLateral ? fm.stringWidth(dsLabel) : 0), left ? lbTitle.getY2() : SAME);
				} else {
					add(lbVep = new LabelName(dsLabel), left ? leftPos : rigthPos - (usaScrollLateral ? fm.stringWidth(dsLabel) : 0), left ? AFTER : SAME);
				}
				break;
			case CAMPO_FIXO:
				if (i < 4) {
					LabelName lb = new LabelName(dsLabel);
					if(LavenderePdaConfig.usaQuebraLinhaDescricaoProdutoNaLista && i == 2) {
						lb.setFont(UiUtil.defaultTabFont);
						lb.setForeColor(lbTitle.getForeColor());
					}
					add(lb,  left ? leftPos : rigthPos - (usaScrollLateral ? fm.stringWidth(dsLabel) : 0), left ? lbTitle.getY2() : SAME);
				} else {
					add(new LabelName(dsLabel),  left ? leftPos : rigthPos - (usaScrollLateral ? fm.stringWidth(dsLabel) : 0), left ? AFTER : SAME);
				}
				break;
			case CAMPO_QTHIS:
				if (i < 4) {
					add(lbQtHist = new LabelName(dsLabel), left ? leftPos : rigthPos - (usaScrollLateral ? fm.stringWidth(dsLabel) : 0), left ? lbTitle.getY2() : SAME);
				} else {
					add(lbQtHist = new LabelName(dsLabel), left ? leftPos : rigthPos - (usaScrollLateral ? fm.stringWidth(dsLabel) : 0), left ? AFTER : SAME);
				}
				break;
			case CAMPO_VLHIST:
				if (i < 4) {
					add(lbVlHist = new LabelName(dsLabel), left ? leftPos : rigthPos - (usaScrollLateral ? fm.stringWidth(dsLabel) : 0), left ? lbTitle.getY2() : SAME);
				} else {
					add(lbVlHist = new LabelName(dsLabel), left ? leftPos : rigthPos - (usaScrollLateral ? fm.stringWidth(dsLabel) : 0), left ? AFTER : SAME);
				}
			break;
			case CAMPO_VUE:
				if (i < 4) {
					add(lbVue = new LabelName(dsLabel), left ? leftPos : rigthPos - (usaScrollLateral ? fm.stringWidth(dsLabel) : 0), left ? lbTitle.getY2() : SAME);
				} else {
					add(lbVue = new LabelName(dsLabel), left ? leftPos : rigthPos - (usaScrollLateral ? fm.stringWidth(dsLabel) : 0), left ? AFTER : SAME);
				}
			break;
			case CAMPO_ESTPREVISTO:
				if (i < 4) {
					add(lbEstPrevisto = new LabelName(dsLabel), left ? leftPos : rigthPos - (usaScrollLateral ? fm.stringWidth(dsLabel) : 0), left ? lbTitle.getY2() : SAME);
				} else {
					add(lbEstPrevisto = new LabelName(dsLabel), left ? leftPos : rigthPos - (usaScrollLateral ? fm.stringWidth(dsLabel) : 0), left ? AFTER : SAME);
				}
			break;
		default:
			break;
		}
		
	}
	
	private boolean alinhaLbVlAoLadoEditValor() {
		return (LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao() || LavenderePdaConfig.isPermiteEditarValorUnitarioMultiplaInsercao())
				&& getQtComponentesEditaveis() < 4;
	}
	
	private int getQtComponentesEditaveis() {
		int size = 0;
		if (isHabilitaModoCompacto()) {
			return size;
		}
		size = chooserQtd != null ? ++size : size;
		size = chooserDescAcresc != null ? ++size : size;
		size = LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao() || LavenderePdaConfig.isPermiteEditarValorUnitarioMultiplaInsercao() ? ++size : size;
		size = LavenderePdaConfig.exibeHistoricoEstoqueCliente ? ++size : size;
		size = apresentaUnidadeAlternativa && cbUnidadeAlternativa != null ? ++size : size;

		return size;
	}

	public void setIconsLegend(Image[] iconsLegend) {
		this.iconsLegend = iconsLegend;
	}

	private int getEspacoReservadoIcones() {
		int espaco = 0;

		if (isSugVendaPerson) {
		int iconSize = (UiUtil.getLabelPreferredHeight() + WIDTH_GAP / 2);
		for (String string : produto.cdsSugVendaPerson) {
			SugVendaPerson sugestao = sugVendaPersonMap.get(string);
			if (sugestao != null && !sugestao.unchecked && sugestao.image != null) {
				espaco += iconSize;
			}
		}
		}

		if (ValueUtil.isNotEmpty(iconsLegend)) {
			espaco += (iconsLegend[0].getHeight() + BaseContainer.WIDTH_GAP) * iconsLegend.length;
		}

		return espaco;
	}
	
	private double getQtEstoqueList(ItemPedido itemPedido) throws SQLException {
		String cdLocalEstoque = itemPedido.pedido!= null ? itemPedido.pedido.getCdLocalEstoque() : Estoque.CD_LOCAL_ESTOQUE_PADRAO;
		if (isSugVendaPerson) {
			double qtEstoque = EstoqueService.getInstance().getQtEstoque(itemPedido.cdProduto, cdLocalEstoque);
			return SugVendaPersonService.getInstance().getEstoqueToGridUnidadeAlternativa(itemPedido, produto, qtEstoque, itemPedido.pedido);
		} else if (LavenderePdaConfig.usaControleEstoquePorRemessa && !itemPedido.pedido.utilizaEstoquePorLocalEstoqueDaEmpresa()) {
			return RemessaEstoqueService.getInstance().getEstoqueDisponivelProduto(itemPedido);
		} else {
			double qtEstoque = EstoqueService.getInstance().getQtEstoque(itemPedido.cdProduto, cdLocalEstoque);
			if (LavenderePdaConfig.usaControleEstoquePrevistoParcial()) {
				qtEstoque = EstoqueService.getInstance().getQtEstoqueErpPdaComParcialPrevisto(itemPedido);
			}
			return EstoqueService.getInstance().calculaEstoqueByProdutoUnidade(itemPedido.getItemTabelaPreco(), itemPedido.getProdutoUnidade(), qtEstoque);
		}
	}
	
	@Override
	public void onPaint(Graphics g) {
		super.onPaint(g);

		int x = WIDTH_GAP + insets.left;

		if (isSugVendaPerson) {
			int xGap = lbTitle.getHeight() + WIDTH_GAP / 2;
			for (String string : produto.cdsSugVendaPerson) {
				SugVendaPerson sugestao = sugVendaPersonMap.get(string);
				if (sugestao != null && !sugestao.unchecked && sugestao.image != null) {
					g.drawImage(sugestao.image, x, WIDTH_GAP);
					x += xGap;
				}
			}
			if (image != null) {
				g.drawImage(image, WIDTH_GAP, lbTitle.getY2());
				image.lockChanges();
			}
		}

		if (ValueUtil.isNotEmpty(iconsLegend)) {
			for (int i = 0; i < iconsLegend.length; i++) {
				g.drawImage(iconsLegend[i], x, WIDTH_GAP);
				x += WIDTH_GAP + iconsLegend[i].getWidth();
	}
		}
	}

	public final String[] getItems() {
		return items;
	}
	
	private class ItemContainerToolTip extends ItemToolTip {

		public ItemContainerToolTip(Control ctr, String tip) {
			super(ctr, tip);
}

		public void show() {
			if (notRollingList) {
				super.show();
			}
		}

		public void penDown(PenEvent e) {
			if (notRollingList) {
				super.penDown(e);
			}
		}

	}

	private boolean isHabilitaAcrescimo() {
		return LavenderePdaConfig.isPermiteEditarDescontoAcrescimoMultiplaInsercao() && !cadCargaProduto && !isSugVendaPerson;
	}

	private boolean isHabilitaModoCompacto() {
		return LavenderePdaConfig.isUsaModoExibicaoCompactoMultiplaInsercao() && !cadCargaProduto && !isSugVendaPerson;
	}

	public void setVisibleDescAcrescChooser(boolean visible) {
		chooserDescAcresc.setVisible(visible);
		dsDescAcresc.setVisible(visible);
	}

	public void requestFocusByAppId(int appId) {
		switch (appId) {
		case 1:
			chooserQtd.edF.requestFocus();
			break;
		case 2:
			if (chooserDescAcresc.isEnabled()) {
				chooserDescAcresc.edF.requestFocus();
			} else {
				chooserQtd.edF.requestFocus();
			}
			break;
		case 3:
			if (edVlItemPedido != null && edVlItemPedido.isEnabled()) {
				edVlItemPedido.requestFocus();
			} else if (edVlBrutoItem != null && edVlBrutoItem.isEnabled()) {
				edVlBrutoItem.requestFocus();
			} else {
				chooserQtd.edF.requestFocus();
			}
			break;
		}
	}

	public void setChooserQtdValue(double value) {
		chooserQtd.setValue(value);
		qtdValue = value;
	}

	public void setChooserDesc(double value) {
		chooserDescAcresc.setValue(value);
		descValue = value;
	}

	public void setVlBrutoItem(double value) {
		edVlBrutoItem.setValue(value);
		vlItem = value;
	}

	public void setEdVlItemPedido(double value) {
		edVlItemPedido.setValue(value);
		vlItem = value;
		int strValuelenght = edVlItemPedido.getText().length();
		edVlItemPedido.setCursorPos(strValuelenght, strValuelenght);
	}

	public boolean changed() {
		return qtdValue != chooserQtd.getValue() ||
				(chooserDescAcresc != null && descValue != chooserDescAcresc.getValue()) ||
				(edVlBrutoItem != null && vlItem != edVlBrutoItem.getValueDouble()) ||
				(edVlItemPedido != null && vlItem != edVlItemPedido.getValueDouble());
	}

	public void disableAllControls() {
		setControlsEnabled(false, false, false, false, false, false, false, false, false, false, false);
	}

	public void setControlsEnabledByState(ItemContainerControlState itemContainerState) {
		setControlsEnabled(itemContainerState.choserQtdBtMenosEnabled, itemContainerState.choserQtdBtMaisEnabled,
				itemContainerState.choserQtdEdQtdEnabled, itemContainerState.edVlItemPedidoEnabled,
				itemContainerState.edVlBrutoItemPedidoEnabled, itemContainerState.choserDescAscBtMenosEnabled,
				itemContainerState.choserDescAscBtMaisEnabled, itemContainerState.choserDescAscEdQtdEnabled,
				itemContainerState.choserEstoqueClienteBtMenosEnabled, itemContainerState.choserEstoqueClienteBtMaisEnabled, itemContainerState.choserEstoqueClienteEdQtdEnabled);
	}

	public void setControlsEnabled(boolean choserQtdBtMenosEnabled, boolean choserQtdBtMaisEnabled, boolean choserQtdEdQtdEnabled, boolean edVlItemPedidoEnabled, boolean edVlBrutoItemPedidoEnabled, boolean choserDescAscBtMenosEnabled, boolean choserDescAscBtMaisEnabled, boolean choserDescAscEdQtdEnabled, boolean choserEstoqueClienteBtMenosEnabled, boolean choserEstoqueClienteBtMaisEnabled, boolean choserEstoqueClienteEdQtdEnabled) {
		if (chooserQtd != null) {
			chooserQtd.btMenos.setEnabled(choserQtdBtMenosEnabled);
			chooserQtd.btMais.setEnabled(choserQtdBtMaisEnabled);
			chooserQtd.edF.setEnabled(choserQtdEdQtdEnabled);
		}
		if (edVlItemPedido != null) {
			edVlItemPedido.setEnabled(edVlItemPedidoEnabled);
		}
		if (edVlBrutoItem != null) {
			edVlBrutoItem.setEnabled(edVlBrutoItemPedidoEnabled);
		}
		if (chooserDescAcresc != null) {
			chooserDescAcresc.btMenos.setEnabled(choserDescAscBtMenosEnabled);
			chooserDescAcresc.btMais.setEnabled(choserDescAscBtMaisEnabled);
			chooserDescAcresc.edF.setEnabled(choserDescAscEdQtdEnabled);
		}
		if (chooserEstoqueCliente != null) {
			chooserEstoqueCliente.btMenos.setEnabled(choserEstoqueClienteBtMenosEnabled);
			chooserEstoqueCliente.btMais.setEnabled(choserEstoqueClienteBtMaisEnabled);
			chooserEstoqueCliente.edF.setEnabled(choserEstoqueClienteEdQtdEnabled);
		}
	}

}
