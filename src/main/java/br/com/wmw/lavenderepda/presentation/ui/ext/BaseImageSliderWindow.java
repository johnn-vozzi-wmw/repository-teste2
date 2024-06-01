package br.com.wmw.lavenderepda.presentation.ui.ext;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.media.camera.BaseCamera;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.ImageCarrousel;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public abstract class BaseImageSliderWindow extends WmwWindow {
	
	protected ButtonPopup btNovo;
	protected ButtonPopup btExcluir;
	protected ImageCarrousel imageCarrousel;

	public BaseImageSliderWindow(String title) throws SQLException {
		super(title);
		setBackForeColors(ColorUtil.popupBackColor, ColorUtil.componentsForeColor);
		btNovo = new ButtonPopup(FrameworkMessages.BOTAO_NOVO);
		btExcluir = new ButtonPopup(FrameworkMessages.BOTAO_EXCLUIR);
		btExcluir.setForeColor(ColorUtil.buttonExcluirForeColor);
		fadeOtherWindows = true;
		makeUnmovable();
	}

	protected abstract void addButtons();
	protected abstract void btNovaFotoClick() throws SQLException;
	protected abstract void btExcluirClick() throws SQLException;
	
	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(scBase, imageCarrousel, LEFT, TOP, FILL, FILL);
		addButtons();
	}
	
	@Override
	public void onWindowEvent(Event event) throws SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btNovo) {
					btNovaFotoClick();
				}
				if (event.target == btExcluir) {
					btExcluirClick();
				}
				break;
			}
		}
	}
	
	protected void setCameraType(BaseCamera camera) throws Exception {
		if (LavenderePdaConfig.permiteSelecionarFotoArmazenadaCadastroFoto && (VmUtil.isAndroid() || VmUtil.isIOS())) {
			int result = UiUtil.showMessage(Messages.LABEL_OPCOES_IMAGEM, Messages.MSG_COMPLETAR_ACAO_USANDO, new String[] {Messages.BOTAO_CANCELAR, Messages.BOTAO_CAMERA, Messages.BOTAO_GALERIA});
			switch (result) {
			case 0:
				throw new Exception();
			case 2:
				camera.setCameraType(BaseCamera.CAMERA_FROM_GALLERY);
				return;
			}
		}
		if (LavenderePdaConfig.isUsaCameraNativa()) {
			camera.setCameraType(BaseCamera.CAMERA_NATIVE);
		}
	}

}
