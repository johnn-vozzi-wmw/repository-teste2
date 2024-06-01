package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.Radio;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import totalcross.ui.Container;
import totalcross.ui.ImageControl;
import totalcross.ui.RadioGroupController;
import totalcross.ui.ScrollContainer;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.image.Image;

public class AdmTecladoWindow extends WmwWindow {

	private LabelValue lbMensagemNovidade;
	private LabelValue lbMensagemConfiguracao;
	private LabelValue lbInformation;
	private Radio radio1; 
	private Radio radio2; 
	private RadioGroupController groupController;
	private ButtonPopup btConfirmar;
	private CheckBoolean checkMensagem;
	public boolean telaInicial;
	private Image imgInformation;
	private ImageControl iconInformation;

	public AdmTecladoWindow() throws SQLException {
		super(Messages.TECLADO_NOME_TELA);
		lbMensagemNovidade = new LabelValue("@@@");
		lbMensagemNovidade.align = CENTER;
		lbMensagemConfiguracao = new LabelValue("@@@");
		lbMensagemConfiguracao.align = CENTER;
		lbInformation = new LabelValue(Messages.IMPORTANTE);
		lbInformation.align = CENTER;
		lbInformation.setForeColor(ColorUtil.baseForeColorSystem);
		groupController = new RadioGroupController();
		radio1 = new Radio(Messages.NM_TECLADO_PADRAO, groupController);
		radio2 = new Radio(Messages.NM_TECLADO_WMW, groupController);
		groupController.setSelectedItem(radio1);
		btConfirmar = new ButtonPopup(Messages.BT_CONFIRMAR);
		checkMensagem = new CheckBoolean(Messages.MENSAGEM_NAO_EXIBIR_NOVAMENTE);
		iconInformation = new ImageControl();
		iconInformation.transparentBackground = true;
		imgInformation = UiUtil.getImage("images/information.png");
		imgInformation.applyColor2(ColorUtil.baseForeColorSystem);
		btFechar.setVisible(false);
		scrollable = false;
		setDefaultRect();
		carregaDados();
	}

	private void carregaDados() throws SQLException {
		int index = ConfigInternoService.getInstance().usaTecladoWmw() ? 1 : 0;
		groupController.setSelectedIndex(index);
		checkMensagem.setChecked(ConfigInternoService.getInstance().isNaoMostraMensagemTecladoNativo());
	}

	@Override
	protected void onPopup() {
		try {
			super.onPopup();
			addButtonPopup(btConfirmar);
			ScrollContainer sc = new BaseScrollContainer(false, true, true);
			sc.setBackForeColors(ColorUtil.baseBackColorSystem, ColorUtil.baseForeColorSystem);
			UiUtil.add(this, sc, LEFT, getTop(), FILL, FILL - footerH * (telaInicial ? 2 : 1));
			
			int maxWidth = getWidth() / 7;
			iconInformation.setImage(UiUtil.getSmoothScaledImageForMaxSize(imgInformation, maxWidth, maxWidth));
			UiUtil.add(sc, iconInformation, CENTER, getNextY(), maxWidth, maxWidth);
			UiUtil.add(sc, lbInformation, getLeft(), getNextY(), FILL - WIDTH_GAP_BIG);
			if (telaInicial) {
				UiUtil.add(sc, lbMensagemNovidade, getLeft(), getNextY() + HEIGHT_GAP_BIG * 2, FILL - WIDTH_GAP_BIG);
				lbMensagemNovidade.setMultipleLinesText(Messages.MENSAGEM_TECLADO_NOVO);
			}
			UiUtil.add(sc, radio1, getLeft(), getNextY() + HEIGHT_GAP_BIG * 3);
			UiUtil.add(sc, radio2, getLeft(), getNextY() + HEIGHT_GAP_BIG);
			if (telaInicial) {
				UiUtil.add(sc, lbMensagemConfiguracao, getLeft(), getNextY() + HEIGHT_GAP_BIG * 3, FILL - WIDTH_GAP_BIG);
				Container checkContainer = new Container();
				checkContainer.setBackColor(ColorUtil.formsBackColor);
				checkContainer.borderColor = ColorUtil.componentsBorderColor;
				UiUtil.add(this, checkContainer, LEFT, BOTTOM - footerH, FILL, footerH);
				UiUtil.add(checkContainer, checkMensagem, LEFT + WIDTH_GAP, CENTER);
				lbMensagemConfiguracao.setMultipleLinesText(Messages.MENSAGEM_ALTERAR_CONFIG);
			}
		} catch (Exception ee) {
			ExceptionUtil.handle(ee);
		}
	}

	@Override
	public void onEvent(Event event) {
		try {
			super.onEvent(event);
			switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btConfirmar) {
					ConfigInternoService.getInstance().addValueGeral(ConfigInterno.configTecladoSelecionado, String.valueOf(isTecladoWMW()), "teclado");
					ConfigInternoService.getInstance().addValueGeral(ConfigInterno.mensagemNovidadeTecladoNativo, String.valueOf(checkMensagem.isChecked()), "tecladoMensagem");
					LavenderePdaConfig.carregaParametrosTecladoAntesDeLogar();
					btFecharClick();
					break;
				}
			}
			}
		} catch (Exception ee) {
			ExceptionUtil.handle(ee);
		}
	}

	private boolean isTecladoWMW() {
		return groupController.getSelectedIndex() == 1;
	}

}
