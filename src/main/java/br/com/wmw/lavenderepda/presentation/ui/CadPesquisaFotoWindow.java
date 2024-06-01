package br.com.wmw.lavenderepda.presentation.ui;


import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.NoFileSelectedException;
import br.com.wmw.framework.media.camera.BaseCamera;
import br.com.wmw.framework.presentation.ui.WmwCadWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.PerguntaResposta;
import br.com.wmw.lavenderepda.business.domain.PesquisaRespAppFoto;
import br.com.wmw.lavenderepda.business.service.PesquisaRespAppFotoService;
import br.com.wmw.lavenderepda.util.Util;
import totalcross.io.File;
import totalcross.ui.ImageControl;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.image.Image;
import totalcross.ui.image.ImageException;

public class CadPesquisaFotoWindow extends WmwCadWindow {

	public PerguntaResposta perguntaResposta;
	public ImageControl imageControl;
	public Image image;
	private ButtonPopup btCancelar;
	String pathFoto;
	BaseCamera camera;

	public CadPesquisaFotoWindow(PerguntaResposta perguntaResposta) {
		super(Messages.RESPOSTA_FOTO);
		this.perguntaResposta = perguntaResposta;
		camera = new BaseCamera();
		btCancelar = new ButtonPopup(FrameworkMessages.BOTAO_CANCELAR);
		setRect(2, 2);
	}
	
	@Override
	public void initUI() {
		super.initUI();
		image = Util.NOIMAGE;
		imageControl = new ImageControl(image);
		imageControl.centerImage = true;
		UiUtil.add(this, imageControl, LEFT + 4, TOP + 4, FILL - 4, FILL - 4);
	}

	protected BaseDomain screenToDomain() throws SQLException {
		return null;
	}

	protected void domainToScreen(BaseDomain domain) throws SQLException {
	}

	protected void clearScreen() throws SQLException {
	}

	protected BaseDomain createDomain() throws SQLException {
		return null;
	}

	protected String getEntityDescription() {
		return Messages.RESPOSTA_FOTO;
	}

	protected CrudService getCrudService() throws SQLException {
		return PesquisaRespAppFotoService.getInstance();
	}

	@Override
	protected void visibleState() throws SQLException {
		super.visibleState();
		btSalvar.setVisible(false);
		btExcluir.setVisible(true);
		ajustaTamanhoBotoes();
	}

	@Override
	protected void addButtons() {
		addButtonPopup(btExcluir);
		addButtonPopup(btSalvarNovo);
		addButtonPopup(btCancelar);
	}

	@Override
	protected void onPopup() {
		super.onPopup();
		btNovaFotoClick();
	}

	@Override
	public void onEvent(Event event) {
	    try {
			super.onEvent(event);
	    	switch (event.type) {
	    		case ControlEvent.PRESSED: {
					if (event.target == btCancelar) {
						PesquisaRespAppFotoService.getInstance().deleteFotoLocal(perguntaResposta.pesquisaRespAppFoto);
						perguntaResposta.pesquisaRespAppFoto = null;
						unpop();
					}
				}
	    	}
    	} catch (Throwable ee) {
    		ee.printStackTrace();
    	}
	}
	
	
	@Override
	protected void salvarNovoClick() throws SQLException {
		if (perguntaResposta.pesquisaRespAppFoto == null) {
			btNovaFotoClick();
		} else {
			fecharWindow();
		}
	}

	@Override
	protected void excluirClick() throws SQLException {
		if (perguntaResposta.pesquisaRespAppFoto == null) {
			UiUtil.showErrorMessage(Messages.MSG_ERRO_NENHUMA_FOTO_EXCLUSAO);
			return;
		}
		if (UiUtil.showConfirmDeleteMessage(getEntityDescription())) {
			PesquisaRespAppFotoService.getInstance().deleteFotoLocal(perguntaResposta.pesquisaRespAppFoto);
			perguntaResposta.pesquisaRespAppFoto = null;
			imageControl.setImage(Util.NOIMAGE);
			repaint();
		}
	}

	private void btNovaFotoClick() {
		try {
			PesquisaRespAppFoto pesquisaRespAppFoto = new PesquisaRespAppFoto();
			pesquisaRespAppFoto.cdEmpresa = SessionLavenderePda.cdEmpresa;
			pesquisaRespAppFoto.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
			if (LavenderePdaConfig.getResolucaoCameraRegistroFotos() > 0) {
				camera.setResolution(LavenderePdaConfig.getResolucaoCameraRegistroFotos());
			}
			try {
				setCameraType();
			} catch (Exception e) {
				return;
			}
			pathFoto = camera.click();
			if (ValueUtil.isNotEmpty(pathFoto)) {
				if (VmUtil.isSimulador()) {
					FotoUtil.copyToSD(pathFoto, PesquisaRespAppFoto.getPathImg(), camera.defaultFileName);
				} else {
					FotoUtil.saveScaledPhoto(pathFoto, PesquisaRespAppFoto.getPathImg(), camera.defaultFileName, camera.cameraType, LavenderePdaConfig.getNuResolucaoFotoCameraNativa(), ".png");
				}
				File arquivoFoto = new File(PesquisaRespAppFoto.getPathImg() + camera.defaultFileName, File.READ_ONLY);
				image = new Image(arquivoFoto);
				resizeImage();
				imageControl.setImage(image);
				arquivoFoto.close();
				pesquisaRespAppFoto.imFoto = camera.defaultFileName;
				pesquisaRespAppFoto.cdPergunta = perguntaResposta.cdPergunta;
				pesquisaRespAppFoto.cdResposta = perguntaResposta.cdResposta;
				perguntaResposta.pesquisaRespAppFoto = pesquisaRespAppFoto;
			}
			repaint();
		} catch (NoFileSelectedException e) {
			ExceptionUtil.handle(e);
		} catch (Exception ex) {
			UiUtil.showErrorMessage(ex);
		}
	}

	private void setCameraType() throws Exception {
		boolean usaCameraNativa = LavenderePdaConfig.isUsaCameraNativa();
		if (usaCameraNativa) {
			camera.setCameraType(BaseCamera.CAMERA_NATIVE);
		}
	}
	
	private void resizeImage() throws ImageException {
		if (image.getWidth() > image.getHeight() && getWidth() < getHeight()) {
			if (image.getWidth() < getWidth()) {
				return;
			} else {
				int scaloned = image.getWidth() - getWidth();
				int percWidth = (scaloned * 100) / image.getWidth();
				int percHeigth = image.getHeight() - ((image.getHeight() * percWidth) / 100);
				image = image.getSmoothScaledInstance(getWidth(), percHeigth);
			}
		} else {
			if (image.getHeight() < getHeight() && image.getWidth() < getWidth()) {
				return;
			} else {
				int scaloned = image.getHeight() - getHeight();
				int percHeight = (scaloned * 100) / image.getHeight();
				int percWidth = image.getWidth() - ((image.getWidth() * percHeight) / 100);
				if ((percWidth - 10) > getWidth()) {
					scaloned = image.getWidth() - getWidth();
					percWidth = (scaloned * 100) / image.getWidth();
					int percHeigth = image.getHeight() - ((image.getHeight() * percWidth) / 100);
					image = image.getSmoothScaledInstance(getWidth(), percHeigth);
				} else {
					image = image.getSmoothScaledInstance(percWidth, getHeight());
				}
			}
		}
	}
}
