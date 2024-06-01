package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.AcaoNovidade;
import br.com.wmw.lavenderepda.business.domain.Novidade;
import br.com.wmw.lavenderepda.business.service.AcaoNovidadeService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.MouseEvent;

public class NovidadeWindow extends WmwWindow {
	
	private LabelValue lbMensagem;
	private LabelValue lbUrl;
	private LabelValue lbNovidade;
	private CheckBoolean ckNaoMostrarNovamente;
	private Novidade novidade;
	
	public NovidadeWindow(Novidade novidade) {
		super(Messages.NOVIDADE_TITULO);
		this.novidade = novidade;
		ckNaoMostrarNovamente = new CheckBoolean(Messages.NOVIDADE_BOTAO_NAO_EXIBIR_NOVAMENTE);
		lbMensagem = new LabelValue();
		lbNovidade = new LabelValue();
		lbUrl = new LabelValue();
		lbNovidade.setForeColor(ColorUtil.baseForeColorSystem);
		setRect(CENTER, CENTER, SCREENSIZE + 92, UiUtil.getControlPreferredHeight() * 9);
	}
	
	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, lbMensagem, getLeft(), TOP, getWFill(), UiUtil.getControlPreferredHeight() * 2);
		UiUtil.add(this, lbNovidade, getLeft(), AFTER + HEIGHT_GAP, getWFill(), UiUtil.getControlPreferredHeight() * 3);
		UiUtil.add(this, ckNaoMostrarNovamente, getLeft(), BOTTOM - HEIGHT_GAP);
		setLabelValues();
	}

	private void setLabelValues() {
		lbMensagem.setMultipleLinesText(Messages.NOVIDADE_TEXTO_POPUP);
		lbNovidade.setMultipleLinesText(novidade.dsNovidade);
		lbUrl.setMultipleLinesText(novidade.dsUrl);
	}
	
	@Override
	public void reposition() {
		super.reposition();
		removeAll();
		initUI();
	}
	
	@Override
	public void onEvent(Event event) {
	   try {
		super.onEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btFechar) {
					if (ckNaoMostrarNovamente.isChecked()) {
						AcaoNovidadeService.getInstance().insertAcaoNovidade(AcaoNovidade.NOVIDADE_NAO_MOSTRAR_NOVAMENTE, SessionLavenderePda.usuarioPdaRep.cdRepresentante, novidade.cdNovidade);
					} else {
						AcaoNovidadeService.getInstance().insertAcaoNovidade(AcaoNovidade.NOVIDADE_IGNORADA, SessionLavenderePda.usuarioPdaRep.cdRepresentante, novidade.cdNovidade);
					}
					unpop();
				}
				break;
			}
			case MouseEvent.PEN_DOWN: {
				if (event.target == lbNovidade) {
					if (ValueUtil.isNotEmpty(lbUrl.getValue())) {
						AcaoNovidadeService.getInstance().insertAcaoNovidade(AcaoNovidade.NOVIDADE_LIDA, SessionLavenderePda.usuarioPdaRep.cdRepresentante, novidade.cdNovidade);
						UiUtil.openBrowser(lbUrl.getValue());
						unpop();
					}
					
				} 
				break;
			}
		}
		} catch (Throwable ee) {ee.printStackTrace();}
	}
	
}
