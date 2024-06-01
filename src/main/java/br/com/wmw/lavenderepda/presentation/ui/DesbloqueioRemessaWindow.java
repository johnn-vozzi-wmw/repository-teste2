package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.event.ButtonOptionsEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.ButtonOptions;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ScannerCameraUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.RemessaEstoque;
import br.com.wmw.lavenderepda.business.service.RemessaEstoqueService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;

public class DesbloqueioRemessaWindow extends WmwWindow{

	private EditText edFiltro;
	private ButtonPopup btDesbloquear;
	private ButtonOptions bmOpcoes;
	private BaseButton btLimpar;
	private RemessaEstoque remessaEstoque;
	public boolean desbloqueou;
	
	public DesbloqueioRemessaWindow(RemessaEstoque remessaEstoque) {
		super(Messages.REMESSAESTOQUE_LABEL_DESBLOQUEAR_REMESSA);
		this.remessaEstoque = remessaEstoque;
		edFiltro = new EditText("999999999", 50);
		edFiltro.setEnabled(false);
		btFechar.setText(Messages.BT_CANCELAR);
		btDesbloquear = new ButtonPopup(Messages.REMESSAESTOQUE_LABEL_DESBLOQUEAR);
		bmOpcoes = new ButtonOptions();
		bmOpcoes.transparentBackground = false;
		bmOpcoes.setBackColor(ColorUtil.formsBackColor);
		btLimpar = new BaseButton(UiUtil.getSmoothScaledImage(UiUtil.getImage("images/erase.png"), UiUtil.getButtonPreferredHeight(), UiUtil.getButtonPreferredHeight()));
		btLimpar.setBackColor(Color.getRGB(255, 64, 64));
		btLimpar.useBorder = false;
		addItensMenuInferior();
		setDefaultRect();
	}
	

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, edFiltro, getLeft(), TOP + HEIGHT_GAP, FILL - UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG + 1);
		UiUtil.add(this, btLimpar, AFTER - 1, SAME, UiUtil.getControlPreferredHeight(), edFiltro.getHeight());
		addButtonPopup(btDesbloquear);
		addButtonPopup(btFechar);
		addButtonOptions(bmOpcoes);
	}
	
	private void addItensMenuInferior() {
		bmOpcoes.removeAll();
		if (LavenderePdaConfig.usaCameraParaLeituraCdBarras()) {
			bmOpcoes.addItem(Messages.REMESSAESTOQUE_LABEL_LEITOR_CDBARRAS);
		}
		bmOpcoes.addItem(Messages.REMESSAESTOQUE_LABEL_INSERIR_MANUALMENTE);
	}
	
	public void onEvent(Event event) {
		super.onEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED : {
				if (event.target == btLimpar) {
					edFiltro.setText("");
				} else if (event.target == btDesbloquear) {
					btDesbloquearClick();
				}
				break;
			}
			case ButtonOptionsEvent.OPTION_PRESS : {
				if (event.target == bmOpcoes) {
					if (bmOpcoes.selectedItem.equals(Messages.REMESSAESTOQUE_LABEL_INSERIR_MANUALMENTE)) {
						edFiltro.setEnabled(true);
						edFiltro.requestFocus();
					} else if (bmOpcoes.selectedItem.equals(Messages.REMESSAESTOQUE_LABEL_LEITOR_CDBARRAS)) {
						realizaLeituraCamera();
					}
				}
				break;
			}
		}
	}


	private void btDesbloquearClick() {
		try {
			RemessaEstoqueService.getInstance().desbloquearRemessa(remessaEstoque, edFiltro.getText());
			desbloqueou = true;
			fecharWindow();
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e);
		}
	}



	private void realizaLeituraCamera() {
		String dsFiltro = ScannerCameraUtil.realizaLeitura(ScannerCameraUtil.MODO_SOMENTE_CODIGO_BARRAS, Messages.REMESSAESTOQUE_LABEL_LER_CDBARRAS);
		if (ValueUtil.isNotEmpty(dsFiltro)) {
			edFiltro.setText(dsFiltro);
			btDesbloquearClick();
		}
	}
	
}