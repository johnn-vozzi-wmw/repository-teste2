package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonTransparent;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.DivulgaInfo;
import br.com.wmw.lavenderepda.business.service.DivulgaInfoService;
import br.com.wmw.lavenderepda.util.Util;
import totalcross.io.File;
import totalcross.sys.Convert;
import totalcross.sys.SpecialKeys;
import totalcross.ui.Container;
import totalcross.ui.ImageControl;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.ui.event.PenEvent;
import totalcross.ui.image.Image;
import totalcross.ui.image.ImageException;
import totalcross.util.Vector;

public class RelDivulgaInfoWindow extends WmwWindow {
	
	public Vector listDivulgaInfo;
	public DivulgaInfo divulgaInfoAtual;
	public Cliente cliente;
	public ImageControl imageControl;
	public Container containerTexto;
	public LabelName labelTexto;
    private ButtonTransparent btNext;
    private ButtonTransparent btPrevious;
    private ButtonTransparent btPaginacao;
    public int nuDivulgacaoAtual;
	public boolean aberturaManual;

	public RelDivulgaInfoWindow(Cliente clienteFilter, boolean aberturaManual) {
		super(Messages.DIVULGA_INFO_TITULO);
		listDivulgaInfo = null;
		nuDivulgacaoAtual = 1;
		this.cliente = clienteFilter;
		this.aberturaManual = aberturaManual;
		createNextButton();
		createBackButton();
		createPaginationButton();
		setDefaultRect();
	}
	
	private void createPaginationButton() {
		btPaginacao	= new ButtonTransparent("0" + Messages.DIVULGA_INFO_TOTAL_SLIDE_SEPARATOR + "0");	
		btPaginacao.setEnabled(false);
		btPaginacao.setVisible(true);
	}

	private void createBackButton() {
		Image imgPrevious = UiUtil.getImage("images/previous.png");
	    imgPrevious.applyColor2(ColorUtil.baseForeColorSystem);
	    btPrevious = new ButtonTransparent(UiUtil.getSmoothScaledImage(imgPrevious, UiUtil.getControlPreferredHeight() / 2, UiUtil.getControlPreferredHeight() / 2));
	    btPrevious.setEnabled(false);		
	}

	private void createNextButton() {
		Image imgNext = UiUtil.getImage("images/next.png");
		imgNext.applyColor2(ColorUtil.baseForeColorSystem);
	    btNext = new ButtonTransparent(UiUtil.getSmoothScaledImage(imgNext, UiUtil.getControlPreferredHeight() / 2, UiUtil.getControlPreferredHeight() / 2));
	    btNext.setEnabled(false);		
	}

	private void loadDivulgacao() {
		listDivulgaInfo = ValueUtil.isEmpty(listDivulgaInfo) ? findDivulgainfo() : listDivulgaInfo;
		if (ValueUtil.isEmpty(listDivulgaInfo)) {
			UiUtil.showErrorMessage(Messages.DIVULGA_INFO_EMPTY_ERROR);
			unpop();
			return;
		}
		divulgaInfoAtual = (DivulgaInfo) listDivulgaInfo.items[nuDivulgacaoAtual-1];
		divulgaInfoAtual.visualizado = true;
		carregaDivulgaInfo();
		controlaArrows();
		controlaBtPaginacao();
		controlaBtFechar();
	}

	private Vector findDivulgainfo() {
		LoadingBoxWindow lb = null;
		try {
			lb = new LoadingBoxWindow(Messages.MSG_CARREGANDO_DIVULGA_INFO);
			lb.popupNonBlocking();
			return DivulgaInfoService.getInstance().findAllByCliente(cliente);
		} finally {
			if (lb != null) lb.unpop();
		}
	}

	private void controlaBtPaginacao() {
		btPaginacao.setText(nuDivulgacaoAtual + Messages.DIVULGA_INFO_TOTAL_SLIDE_SEPARATOR + listDivulgaInfo.size());
	}

	private void controlaBtFechar() {
		btFechar.setEnabled(isTodasDivulgacoesVisualizadas());
	}

	private void carregaDivulgaInfo() {
		limpaContainers();
		if (divulgaInfoAtual.isTipoTexto()) {
			adicionaComponentesTipoTexto();
		} else if (divulgaInfoAtual.isTipoImagem() || divulgaInfoAtual.isTipoLink()) {
			adicionaComponentesTipoImagem();
		}
	}

	private void limpaContainers() {
		if (containerTexto != null) remove(containerTexto);
		if (imageControl != null) remove(imageControl);
	}

	private void adicionaComponentesTipoImagem() {
		int maxWidth = width - (btNext.getWidth() * 2) - WIDTH_GAP * 2;
		int maxHeight = height - (btFechar.getHeight() * 2) - WIDTH_GAP * 2;
		try {
			if (divulgaInfoAtual.isTipoLink()) {
				if (LavenderePdaConfig.baixaImagemLinkAoExibir && divulgaInfoAtual.imageUrlLink == null) {
					divulgaInfoAtual.imageUrlLink = DivulgaInfoService.getInstance().downloadImageFromLink(divulgaInfoAtual.nmUrlImagemLink, true);
				}
				if (divulgaInfoAtual.imageUrlLink != null) {
					imageControl = new ImageControl(UiUtil.getSmoothScaledImageForMaxSize(divulgaInfoAtual.imageUrlLink, maxWidth, maxHeight));
				} else {
					throw new ImageException(Messages.MSG_IMAGE_NOT_FOUND);
				}
			} else {
				imageControl = new ImageControl(UiUtil.getSmoothScaledImageForMaxSize(new Image(new File(Convert.appendPath(DivulgaInfo.getPathImg(), divulgaInfoAtual.nmImagem), File.READ_ONLY).readAndClose()), maxWidth, maxHeight));
			}
			imageControl.centerImage = true;
			imageControl.setEventsEnabled(true);
			imageControl.setRect(LEFT + btPrevious.getWidth() + WIDTH_GAP, TOP + WIDTH_GAP, maxWidth, maxHeight);
			UiUtil.add(this, imageControl);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			addErrorImage(maxWidth);
		}
	}

	private void adicionaComponentesTipoTexto() {
		containerTexto = new Container();
		containerTexto.setRect(LEFT + btPrevious.getWidth(), TOP, FILL - (btNext.getWidth() * 2), FILL - (UiUtil.getControlPreferredHeight() * 3));
		divulgaInfoAtual.dsTipoDivulgaInfoTxt = MessageUtil.quebraLinhas(divulgaInfoAtual.dsTipoDivulgaInfoTxt, ValueUtil.getIntegerValue(containerTexto.getWidth() - WIDTH_GAP_BIG));
		labelTexto = new LabelName(divulgaInfoAtual.dsTipoDivulgaInfoTxt);
		labelTexto.align = CENTER;
		labelTexto.setForeColor(ColorUtil.baseForeColorSystem);
		UiUtil.add(this, containerTexto);			
		UiUtil.add(containerTexto, labelTexto, containerTexto.getX(), containerTexto.getY(), FILL, FILL);
		labelTexto.setRect(CENTER, ((containerTexto.getHeight() - btPaginacao.getHeight()) / 2) - (labelTexto.getHeight() / 2) , labelTexto.getWidth(), labelTexto.getHeight());
	}


	private void addErrorImage(int imageSize) {
		imageControl = new ImageControl();
		imageControl.centerImage = true;
		imageControl.setEventsEnabled(true);
		imageControl.setRect(LEFT + btPrevious.getWidth() + WIDTH_GAP, TOP + WIDTH_GAP, FILL - (btNext.getWidth() * 2) - WIDTH_GAP, FILL - (UiUtil.getControlPreferredHeight() * 3));
		imageControl.setImage(Util.getDefaultNoImage(imageSize));
		UiUtil.add(this, imageControl);
	}

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, btPrevious, LEFT - 1, CENTER, UiUtil.getControlPreferredHeight(), UiUtil.getControlPreferredHeight());
		UiUtil.add(this, btNext, RIGHT + 1, CENTER, UiUtil.getControlPreferredHeight(), UiUtil.getControlPreferredHeight());
		UiUtil.add(this, btPaginacao, CENTER, BOTTOM - HEIGHT_GAP, UiUtil.getControlPreferredHeight(), UiUtil.getLabelPreferredHeight());
		loadDivulgacao();
	}
	
	@Override
	public void unpop() {
		if (!isTodasDivulgacoesVisualizadas()) return;
		atualizaClienteVisualizouSlide();
		super.unpop();
	}

	private void atualizaClienteVisualizouSlide() {
		try {
			if (aberturaManual) return;
			DivulgaInfoService.getInstance().acrescentaVisualizacao(cliente);
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
	}

	private boolean isTodasDivulgacoesVisualizadas() {
		if (!LavenderePdaConfig.obrigaVerTodosDivulgaInformacao || aberturaManual) return true;
		int size = listDivulgaInfo.size();
		for (int i = 0; i < size; i++) {
			DivulgaInfo divulgaInfo = (DivulgaInfo) listDivulgaInfo.items[i];
			if (!divulgaInfo.visualizado) return false;
		}
		return true;
	}

	@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case PenEvent.PEN_DOWN:
				if (event.target == imageControl || event.target == labelTexto) {
					openUrl();
				}
			break;
			case ControlEvent.PRESSED:
				if (event.target == btNext) {
					btNextClick();
				} else if (event.target == btPrevious) {
					btPreviousClick();
				}
			break;
			case KeyEvent.SPECIAL_KEY_PRESS: {
				if (((KeyEvent)event).key == SpecialKeys.RIGHT) {
					if (btNext.isEnabled()) {
						btNextClick();
					}
				} else if (((KeyEvent)event).key == SpecialKeys.LEFT) {
					if (btPrevious.isEnabled()) {
						btPreviousClick();
					}
				}
				break;
			}
		}
	}

	private void openUrl() {
		if (ValueUtil.isEmpty(divulgaInfoAtual.nmUrl)) return;
		UiUtil.openBrowser(divulgaInfoAtual.nmUrl);
	}

	private void btPreviousClick() {
		nuDivulgacaoAtual--;
		loadDivulgacao();
	}

	private void btNextClick() {
		nuDivulgacaoAtual++;
		loadDivulgacao();
	}
	
	@Override
	public void reposition() {
		super.reposition();
		loadDivulgacao();
	}
	
	private void controlaArrows() {
		btPrevious.setEnabled(nuDivulgacaoAtual > 1);
		btPrevious.setVisible(nuDivulgacaoAtual > 1);	
		btNext.setEnabled(nuDivulgacaoAtual < listDivulgaInfo.size());
		btNext.setVisible(btNext.isEnabled());
	}
	
	@Override
	protected void onUnpop() {
		super.onUnpop();
		Util.resetImages();
	}
}
