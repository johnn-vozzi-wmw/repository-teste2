package br.com.wmw.lavenderepda.presentation.ui.ext;

import java.sql.SQLException;

import br.com.wmw.framework.exception.ImageNoSupportedException;
import br.com.wmw.framework.exception.NoFileSelectedException;
import br.com.wmw.framework.media.camera.BaseCamera;
import br.com.wmw.framework.presentation.ui.ext.ImageCarrousel;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Prospect;
import br.com.wmw.lavenderepda.business.service.FotoProspectService;

public class ImageSliderProspectWindow extends BaseImageSliderWindow {
	
	private Prospect prospect;
	
	public ImageSliderProspectWindow(Prospect prospect) throws SQLException {
		super(ValueUtil.VALOR_NI);
		this.prospect = prospect;
		imageCarrousel = new ImageCarrousel(FotoProspectService.getInstance().geraListaImagemForCarrousel(prospect), Prospect.getPathImg(), true);
		setDefaultWideRect();
	}
	
	@Override
	public void reposition() {
		super.reposition();
		ajustaTamanhoBotoes();
	}
	
	@Override
	protected void addButtons() {
		if (!prospect.isEnviadoServidor()) {
			addButtonPopup(btNovo);
			addButtonPopup(btExcluir);
		}
	}
	
	@Override
	protected void btNovaFotoClick() throws SQLException {
		int cdFotoProspect = FotoProspectService.getInstance().getSequencialCdFotoProspect(prospect) + 1;
		String nmFoto = TimeUtil.getCurrentDateTimeYYYYMMDDHHMMSS() + "_" + prospect.cdProspect + "_" + cdFotoProspect;
		try {
			BaseCamera camera = new BaseCamera();
			camera.setFileName(nmFoto);
			double resolucao = LavenderePdaConfig.getResolucaoCameraRegistroFotos();
			if (resolucao > 0) {
				camera.setResolution(resolucao);
			}
			try {
				setCameraType(camera);
			} catch (Exception e) {
				ExceptionUtil.handle(e);
				return;
			}
			String pathFoto = camera.click();
			if (ValueUtil.isNotEmpty(pathFoto)) {
				if (VmUtil.isSimulador()) {
					FotoUtil.copyToSD(pathFoto, Prospect.getPathImg(), camera.defaultFileName);
				} else {
					FotoUtil.saveScaledPhoto(pathFoto, Prospect.getPathImg(), camera.defaultFileName, camera.cameraType, LavenderePdaConfig.getNuResolucaoFotoCameraNativa(), ".png");
				}
				FotoProspectService.getInstance().insereFotoProspectNaMemoria(prospect, camera.defaultFileName);
				btFechar.setText(Messages.BT_CONFIRMAR);
				imageCarrousel.setImgList(FotoProspectService.getInstance().geraListaImagemForCarrousel(prospect), new String[]{nmFoto, prospect.cdProspect, ".png"});
			}
		} catch (NoFileSelectedException e) {
			ExceptionUtil.handle(e);
		} catch (ImageNoSupportedException e) {
			UiUtil.showErrorMessage(e);
		} catch (Throwable e) {
			FotoProspectService.getInstance().excluiFotoProspect(prospect, nmFoto);
			UiUtil.showErrorMessage(e);
		}
	}
	
	@Override
	protected void setCameraType(BaseCamera camera) throws Exception {
		if (LavenderePdaConfig.permiteInserirFotoCadastradaProspect && (VmUtil.isAndroid() || VmUtil.isIOS())) {
			int result = UiUtil.showMessage(Messages.LABEL_OPCOES_IMAGEM, Messages.MSG_COMPLETAR_ACAO_USANDO, new String[] {Messages.BOTAO_CANCELAR, Messages.BOTAO_CAMERA, Messages.BOTAO_GALERIA});
			if (result == 0) {
				throw new Exception();
			} else if (result == 2) {
				camera.setCameraType(BaseCamera.CAMERA_FROM_GALLERY);
				return;
			}
		}
		if (LavenderePdaConfig.isUsaCameraNativa()) {
			camera.setCameraType(BaseCamera.CAMERA_NATIVE);
		}
	}
	
	@Override
	protected void btExcluirClick() throws SQLException {
		if (ValueUtil.isEmpty(imageCarrousel.getSelectedImage()[0])) {
			UiUtil.showWarnMessage(Messages.PROSPECT_MSG_NENHUMA_FOTO_DISPONIVEL);
			return;
		}
		if (UiUtil.showConfirmDeleteMessage(Messages.PROSPECT_LABEL_EXCLUSAO_FOTO)) {
			String nmFoto = imageCarrousel.getSelectedImage()[0] + imageCarrousel.getSelectedImage()[2];
			if (FotoProspectService.getInstance().isPermiteExcluirFoto(prospect, nmFoto)) {
				FotoProspectService.getInstance().excluiFotoProspect(prospect, nmFoto);
				btFechar.setText(Messages.BT_CONFIRMAR);
			} else {
				UiUtil.showWarnMessage(Messages.CLIENTE_MSG_EXCLUSAO_FOTO_NAO_PERMITIDA);
			}
		}
		imageCarrousel.setImgList(FotoProspectService.getInstance().geraListaImagemForCarrousel(prospect));
		repaint();
	}
	
}
