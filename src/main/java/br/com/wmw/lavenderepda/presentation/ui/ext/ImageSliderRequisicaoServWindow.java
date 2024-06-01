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
import br.com.wmw.lavenderepda.business.domain.RequisicaoServ;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServImagem;
import br.com.wmw.lavenderepda.business.service.RequisicaoServImagemService;
import totalcross.util.Vector;

public class ImageSliderRequisicaoServWindow extends BaseImageSliderWindow {
	
	private RequisicaoServ requisicaoServ;
	private boolean editing;

	public ImageSliderRequisicaoServWindow(RequisicaoServ requisicaoServ, boolean editing) throws SQLException {
		super(ValueUtil.VALOR_NI);
		this.requisicaoServ = requisicaoServ;
		this.editing = editing;
		imageCarrousel = new ImageCarrousel(geraListaImagemCarrousel(), RequisicaoServImagem.getPathImg(), true);
		setDefaultWideRect();
	}
	
	@Override
	protected void addButtons() {
		if (! editing) {
			addButtonPopup(btNovo);
			addButtonPopup(btExcluir);
		}
	}
	
	private Vector geraListaImagemCarrousel() {
		Vector imgList = new Vector();
	    carregaFotosDaTabelaRequisicaoServImagem(imgList);
	    if (ValueUtil.isEmpty(imgList)) {
			imgList.addElement(new String[]{"", Messages.REQUISICAOSER_IMAGEM_LABEL});
	    }
		return imgList;
	}

	private void carregaFotosDaTabelaRequisicaoServImagem(Vector imgList) {
		Vector reqServImgList = requisicaoServ.getRequisicaoServImagemList();
		for (int i = 0; i < reqServImgList.size(); i++) {
			RequisicaoServImagem requisicaoServImagem = (RequisicaoServImagem) reqServImgList.items[i];
			String nmFoto = requisicaoServImagem.nmImagem.substring(0, requisicaoServImagem.nmImagem.lastIndexOf("."));
			String nmExtensao = requisicaoServImagem.nmImagem.substring(requisicaoServImagem.nmImagem.lastIndexOf("."), requisicaoServImagem.nmImagem.length());
			imgList.addElement(new String[]{nmFoto, Messages.REQUISICAOSER_IMAGEM_LABEL, nmExtensao});
		}
	}
	
	@Override
	protected void btNovaFotoClick() {
		String nmFoto = requisicaoServ.cdRequisicaoServ + "_" + TimeUtil.getCurrentDateTimeYYYYMMDDHHMMSS();
		try {
			BaseCamera camera = new BaseCamera();
			camera.setFileName(nmFoto);
			double resolucaoCameraRegistroFotos = LavenderePdaConfig.getResolucaoCameraRegistroFotos();
			if (resolucaoCameraRegistroFotos > 0) {
				camera.setResolution(resolucaoCameraRegistroFotos);
			}
			try {
				setCameraType(camera);
			} catch (Throwable e) {
				return;
			}
			String pathFoto = camera.click();
			if (ValueUtil.isNotEmpty(pathFoto)) {
				if (VmUtil.isSimulador()) {
					FotoUtil.copyToSD(pathFoto, RequisicaoServImagem.getPathImg(), camera.defaultFileName);
				} else {
					FotoUtil.saveScaledPhoto(pathFoto, RequisicaoServImagem.getPathImg(), camera.defaultFileName, camera.cameraType, LavenderePdaConfig.getNuResolucaoFotoCameraNativa(), ".png");
				}
				RequisicaoServImagemService.getInstance().addImagem(requisicaoServ, camera.defaultFileName);
				btFechar.setText(Messages.BT_CONFIRMAR);
				imageCarrousel.setImgList(geraListaImagemCarrousel(), new String[]{nmFoto, requisicaoServ.nuPedido, ".png"});
			}
			repaint();
		} catch (NoFileSelectedException e) {
			ExceptionUtil.handle(e);
		} catch (ImageNoSupportedException e) {
			UiUtil.showErrorMessage(e);
		} catch (Throwable ex) {
			RequisicaoServImagemService.getInstance().excluiImagem(requisicaoServ, nmFoto);
			UiUtil.showErrorMessage(ex);
		} 
	}

	@Override
	protected void btExcluirClick() {
		if (ValueUtil.isEmpty(imageCarrousel.getSelectedImage()[0])) {
			UiUtil.showWarnMessage(Messages.REQUISICAOSER_MSG_NENHUMA_FOTO_DISPONIVEL);
    		return;
		}
		if (UiUtil.showConfirmDeleteMessage(Messages.REQUISICAOSER_IMAGEM_LABEL)) {
			String nmFoto = imageCarrousel.getSelectedImage()[0] + imageCarrousel.getSelectedImage()[2];
			if (! requisicaoServ.isEnviadoServidor()) {
				RequisicaoServImagemService.getInstance().excluiImagem(requisicaoServ, nmFoto);
				btFechar.setText(Messages.BT_CONFIRMAR);
			} else {
				UiUtil.showWarnMessage(Messages.MSG_EXCLUSAO_IMAGEM_NAO_PERMITIDA);
			}
 			imageCarrousel.setImgList(geraListaImagemCarrousel());
			repaint();
		}
	}
	
}
