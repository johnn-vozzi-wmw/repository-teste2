package br.com.wmw.lavenderepda.presentation.ui.ext;


import br.com.wmw.framework.presentation.ui.BaseMainWindow;
import br.com.wmw.framework.presentation.ui.event.SortButtonEvent;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class SortButtonsContainer extends Container {
	
	public static final int DIRECAO_CIMA = 1;
	public static final int DIRECAO_BAIXO = 2;
	
	public ButtonAction btUp;
	public ButtonAction btDown;
	public int direcao;
	public int index;

	public SortButtonsContainer(int index) {
		btUp = new ButtonAction("images/moverparacima.png", ColorUtil.baseForeColorSystem);
		btUp.transparentBackground = true;
		btDown = new ButtonAction("images/moverparabaixo.png", ColorUtil.baseForeColorSystem);
		btDown.transparentBackground = true;
		transparentBackground = true;
		this.index = index;
	}

	@Override
	public void initUI() {
		add(btUp, RIGHT, CENTER, UiUtil.getButtonPreferredHeight(), UiUtil.getButtonPreferredHeight());
		add(btDown, BEFORE, CENTER, UiUtil.getButtonPreferredHeight(), UiUtil.getButtonPreferredHeight());
	}

	@Override
	public void onEvent(Event event) {
		switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == btUp) {
				SortButtonEvent sortEvent = new SortButtonEvent(this);		
				direcao = 1;
				geraEventoSortButton(sortEvent);
			}
			if (event.target == btDown) {
				SortButtonEvent sortEvent = new SortButtonEvent(this);		
				direcao = 2;
				geraEventoSortButton(sortEvent);
			}
			direcao = 0;
			break;
		}
	}

	public void geraEventoSortButton(Event event) {
		BaseMainWindow.getBaseMainWindowInstance().geraEvento(event);
	}
	
	@Override
	public int getPreferredHeight() {
		return UiUtil.getButtonPreferredHeight();
	}
	
	@Override
	public int getPreferredWidth() {
		return UiUtil.getButtonPreferredHeight() * 2;
	}
	
}
