package br.com.wmw.lavenderepda.presentation.ui.ext;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.exception.ImageNoSupportedException;
import br.com.wmw.framework.exception.NoFileSelectedException;
import br.com.wmw.framework.media.camera.BaseCamera;
import br.com.wmw.framework.presentation.ui.ext.ImageCarrousel;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DocumentoAnexo;
import br.com.wmw.framework.util.TypeFileUtil;
import br.com.wmw.lavenderepda.business.service.DocumentoAnexoService;
import totalcross.util.Vector;

public class ImageSliderDocumentoAnexoWindow extends BaseImageSliderWindow {
	
	private Vector documentoAnexoList;
	private int[] indexes;
	
	public ImageSliderDocumentoAnexoWindow(Vector documentoAnexoList) throws SQLException {
		super(ValueUtil.VALOR_NI);
		this.documentoAnexoList = documentoAnexoList;
		imageCarrousel = new ImageCarrousel(getOnlyImagesFromList(documentoAnexoList), DocumentoAnexo.getPathDoc(), true);
		setDefaultWideRect();
	}
	
	private Vector getOnlyImagesFromList(Vector documentoAnexoList) {
		Vector imageList = new Vector();
		int size = documentoAnexoList.size();
		indexes = new int[size];
		for (int i = 0; i < size; i++) {
			DocumentoAnexo docAnexo = (DocumentoAnexo)documentoAnexoList.items[i];
			if (isImageFormat(docAnexo.baArquivo)) {
				imageList.addElement(getInfoImage(docAnexo));
				indexes[i] = imageList.size()-1;
			}
		}
		return imageList;
	}
	
	private void btNovaFotoClick(BaseDomain domain, String nmEntidade) throws SQLException {
		DocumentoAnexo docAnexo = new DocumentoAnexo();
		docAnexo.cdDocumentoAnexo = DocumentoAnexoService.getInstance().getIdGlobal();
		String nmFoto = StringUtil.getStringValue(docAnexo.cdDocumentoAnexo) + TimeUtil.getCurrentDateTimeYYYYMMDDHHMMSS();
		try {
			BaseCamera camera = configureCameraDevice(nmFoto);
			if (camera == null) return;

			String pathFoto = camera.click();
			if (ValueUtil.isNotEmpty(pathFoto)) {
				if (VmUtil.isSimulador()) {
					FotoUtil.copyToSD(pathFoto, DocumentoAnexo.getPathDoc(), camera.defaultFileName);
				} else {
					FotoUtil.saveScaledPhoto(pathFoto,  DocumentoAnexo.getPathDoc(), camera.defaultFileName, camera.cameraType, LavenderePdaConfig.getNuResolucaoFotoCameraNativa(), ".png");
				}
				try {
					fillDocAnexo(docAnexo, nmEntidade, domain.getRowKey(), pathFoto, camera.defaultFileName);
					docAnexo = DocumentoAnexoService.getInstance().anexarFotoDocumento(docAnexo);
					documentoAnexoList.addElement(docAnexo);
					imageCarrousel.setImgList(getOnlyImagesFromList(documentoAnexoList), getInfoImage(docAnexo));
				} catch (SQLException e) {
					UiUtil.showErrorMessage(e);
				}
				UiUtil.showInfoMessage(Messages.BOTAO_ANEXAR_DOC_SUCESSO);
			}
			repaint();
		} catch (NoFileSelectedException e) {
			ExceptionUtil.handle(e);
		} catch (ImageNoSupportedException e) {
			UiUtil.showErrorMessage(e);
		} catch (Throwable ex) {
			DocumentoAnexoService.getInstance().delete(docAnexo);
			UiUtil.showErrorMessage(ex);
		} 
	}
	
	public void setSelectedImage(int index) {
		imageCarrousel.setPosVector(indexes[index]);
	}
	
	public void novaFotoFromCamera(BaseDomain domain, String nmEntidade) throws SQLException {
		btNovaFotoClick(domain, nmEntidade);
	}
	
	private boolean isImageFormat(String fileName) {
		return TypeFileUtil.isImageTcFormat(fileName);
	}
	
	private void fillDocAnexo(DocumentoAnexo documentoAnexo, String nmEntidade, String dsChave, String dsPath, String nmArquivo) throws SQLException {
		DocumentoAnexo docAnexo = documentoAnexo;
		docAnexo.nmEntidade = nmEntidade;
		docAnexo.dsChave = dsChave;
		docAnexo.dsPath = dsPath;
		docAnexo.nmArquivo = nmArquivo;
	}
	
	private BaseCamera configureCameraDevice(String nmFoto) {
		BaseCamera camera = new BaseCamera();
		camera.setFileName(nmFoto);
		double resolucaoCameraRegistroFotos = LavenderePdaConfig.getResolucaoCameraRegistroFotos();
		if (resolucaoCameraRegistroFotos > 0) {
			camera.setResolution(resolucaoCameraRegistroFotos);
		}
		try {
			setCameraType(camera);
		} catch (Throwable e) {
			return null;
		}
		return camera;
	}
	
	private String [] getInfoImage(DocumentoAnexo docAnexo) {
		String nmFoto = docAnexo.baArquivo.substring(0, docAnexo.baArquivo.lastIndexOf("."));
		String dsExtensao = docAnexo.baArquivo.substring(docAnexo.baArquivo.lastIndexOf("."), docAnexo.baArquivo.length());
		return new String[] {nmFoto, docAnexo.baArquivo, dsExtensao};
	}

	@Override
	protected void addButtons() {
		// Não há botões adicionais neste tela
	}

	@Override
	protected void btNovaFotoClick() throws SQLException {
		// Botão não utilizado neste tela
	}

	@Override
	protected void btExcluirClick() throws SQLException {
		// Botão não utilizado neste tela
	}

}
