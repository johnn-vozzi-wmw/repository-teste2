package br.com.wmw.lavenderepda.presentation.ui.ext;

import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import totalcross.ui.gfx.Graphics;

public class MenuCategoriaScrollContainer extends BaseScrollContainer {

	public static final int LANDSCAPE = 2;
	public static final int PORTRAIT = 1;
	public boolean canPaintScrollBar;
	public boolean wasLandscape;
	public boolean backToList;
	public int orientation;
	public int qtRepositions;
	public boolean fromItensClick;
	public boolean canReposition;

	public MenuCategoriaScrollContainer(boolean allowHoriScroll, boolean allowVertScroll) {
		super(allowHoriScroll, allowVertScroll, false);
	}

	@Override
	public void onPaint(Graphics g) {
		super.onPaint(g);
		Graphics grf = this.sbV.getGraphics();
		if (grf != null) {
			if (canPaintScrollBar) {
				this.sbV.alphaValue = 200;
				grf.alpha = 255;
				this.sbV.onPaint(grf);
				this.sbV.setVisible(true);
			} else {
				this.sbV.setVisible(false);
			}
		}
	}
}
