package br.com.wmw.lavenderepda.presentation.ui.ext;

import java.sql.SQLException;

import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.presentation.ui.BaseContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonMenu;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.lavenderepda.business.domain.MenuCategoria;
import br.com.wmw.lavenderepda.business.service.MenuCategoriaService;
import totalcross.ui.Window;
import totalcross.ui.gfx.Color;
import totalcross.ui.gfx.Graphics;
import totalcross.ui.image.Image;
import totalcross.ui.image.ImageException;

public class ButtonMenuCategoria extends ButtonMenu {

	public static int FIXED_BT_VOLTAR_HEIGHT = UiUtil.getButtonPreferredHeight();
	public static double SIZE_MODIFIER = MenuCategoriaService.getInstance().getNuTamanhoPadraoMap();
	public static int DEFAULT_BUTTON_HEIGHT = (int) ((UiUtil.getButtonPreferredHeight()) * SIZE_MODIFIER);
	public static int DEFAULT_BUTTON_HEIGHT_SPACER = (int) ((UiUtil.getButtonPreferredHeight()) * (SIZE_MODIFIER - 0.1));
	public static double DEFAULT_IMAGE_SIZE = (SIZE_MODIFIER - 0.1) * 0.75;
	public MenuCategoria menuCategoria;
	public boolean dynamicButton;
	public boolean isBtVoltar;
	public int defaultBtVoltarWidth;
	public Image nextImage;
	public Image principalImage;
	public String principalText;
	public boolean usaBtPrincipal;


	public static void configureConstants() {
		FIXED_BT_VOLTAR_HEIGHT = UiUtil.getButtonPreferredHeight();
		SIZE_MODIFIER = MenuCategoriaService.getInstance().getNuTamanhoPadraoMap();
		DEFAULT_BUTTON_HEIGHT = (int) ((UiUtil.getButtonPreferredHeight()) * SIZE_MODIFIER);
		DEFAULT_BUTTON_HEIGHT_SPACER = (int) ((UiUtil.getButtonPreferredHeight()) * (SIZE_MODIFIER - 0.1));
		DEFAULT_IMAGE_SIZE = (SIZE_MODIFIER - 0.1) * 0.75;
	}

	public ButtonMenuCategoria(MenuCategoria menuCategoria, boolean dynamicButton) {
		super(menuCategoria.dsMenu);
		this.menuCategoria = menuCategoria;
		this.dynamicButton = dynamicButton;
		this.setPressedColor(ColorUtil.sessionContainerBackColor);
		int	tamImages = (int) (UiUtil.getButtonPreferredHeight() * DEFAULT_IMAGE_SIZE);
		try {
			int nextImageSize = (int) (DEFAULT_BUTTON_HEIGHT * 0.3);
			nextImage = UiUtil.getColorfulImage("images/next.png", nextImageSize, nextImageSize);
			img = UiUtil.getImage(menuCategoria.imFoto);
			img.applyColor2(ColorUtil.baseForeColorSystem);
			img = UiUtil.getSmoothScaledImage(img, tamImages, tamImages);
		} catch (ApplicationException e) {
			img = UiUtil.getColorfulImage("images/menuIconDefault.png", tamImages, tamImages);
		}
		this.setBackColor(ColorUtil.componentsBackColor);
		this.repaintNow();
	}

	public ButtonMenuCategoria(boolean dynamicButton) {
		super(null);
		this.setImage(UiUtil.getColorfulImage("images/back.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
		this.dynamicButton = dynamicButton;
	}

	public ButtonMenuCategoria() {
		super(null);
	}

	@Override
	public void setImage(Image img) {
		this.img = img;
		this.onBoundsChanged(false);
		Window.needsPaint = true;
	}

	@Override
	public String toString() {
		return menuCategoria.dsMenu;
	}

	@Override
	protected void paintText(Graphics g, int tx, int ty) {
		if (dynamicButton && isBtVoltar) {
			super.paintText(g, (this.getWidth() - img.getWidth()) / 2, ty);
		} else if (dynamicButton && usaBtPrincipal) {
			super.paintText(g, (int) (principalImage.getWidth() * 1.5), ty);
		} else if (!dynamicButton) {
			super.paintText(g, (int) (img.getWidth() * 1.5), ty);
		} else {
			super.paintText(g, tx, ty);
		}
	}

	@Override
	protected void paintImage(Graphics g, boolean bkg, int ix, int iy) {
		boolean enabled = isEnabled();
		if (!enabled) {
			if (img != null && imgDis == null) {
				try {
					imgDis = img.getFadedInstance();
					imgDis.applyColor2(Color.BLACK);
				} catch (ImageException e) {
					imgDis = img;
				}
			}
			g.drawImage(imgDis, ix, iy);
		} else {
			if (nextImage != null && !isBtVoltar) {
				g.drawImage(nextImage, (int) (getWidth() - (nextImage.getWidth() * 1.5)), (this.height - nextImage.getHeight()) / 2);
			}
			g.drawImage(armed && pressedImage != null ? pressedImage : img, BaseContainer.WIDTH_GAP, (this.height - img.getHeight()) / 2);
		}
	}

	public void setTextImage(String text, Image image) {
		this.setText(text);
		this.setImage(image);
	}

	public void configuraMenuCategoriaPrincipal() throws SQLException {
		MenuCategoria menuCategoriaPrincipal = MenuCategoriaService.getInstance().getMenuCategoriaPrincipal();
		if (menuCategoriaPrincipal != null) {
			this.usaBtPrincipal = true;
			this.principalText = menuCategoriaPrincipal.dsMenu;
			int tamImages = (int) (UiUtil.getButtonPreferredHeight() * 0.8);
			try {
				int nextImageSize = (int) (FIXED_BT_VOLTAR_HEIGHT * 0.4);
				nextImage = UiUtil.getColorfulImage("images/next.png", nextImageSize, nextImageSize);
				principalImage = UiUtil.getImage(menuCategoriaPrincipal.imFoto);
				principalImage.applyColor2(ColorUtil.baseForeColorSystem);
				principalImage = UiUtil.getSmoothScaledImage(principalImage, tamImages, tamImages);
			} catch (ApplicationException e) {
				principalImage = UiUtil.getColorfulImage("images/menuIconDefault.png", tamImages, tamImages);
			}
		}
	}
}
