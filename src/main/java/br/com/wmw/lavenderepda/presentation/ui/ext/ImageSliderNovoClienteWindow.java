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
import br.com.wmw.lavenderepda.business.domain.FotoNovoCliente;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.service.FotoNovoClienteService;
import totalcross.util.Vector;

public class ImageSliderNovoClienteWindow extends BaseImageSliderWindow {
	
	private NovoCliente novoCliente;
	private boolean fromAnaliseNovoCli;
	private boolean ocultaBtExcluir;

	public ImageSliderNovoClienteWindow(NovoCliente novoCliente, boolean fromAnaliseNovoCli, boolean ocultaBtExcluir) throws SQLException {
		super(ValueUtil.VALOR_NI);
		this.novoCliente = novoCliente;
		imageCarrousel = new ImageCarrousel(geraListaImagemCarrousel(), NovoCliente.getPathImg(), true);
		this.fromAnaliseNovoCli = fromAnaliseNovoCli;
		this.ocultaBtExcluir = ocultaBtExcluir;
		setDefaultWideRect();
	}
	
	@Override
	protected void addButtons() {
		if ((!novoCliente.isEnviadoServidor() || LavenderePdaConfig.isValidaCadastroDuasEtapas()) && !fromAnaliseNovoCli) {
			addButtonPopup(btNovo);
			if (!ocultaBtExcluir) {
				addButtonPopup(btExcluir);
			}
		}
	}
	
	private Vector geraListaImagemCarrousel() throws SQLException {
		Vector imgList = new Vector();
	    carregaFotosDaTabelaFotoNovoCliente(imgList);
	    if (imgList.size() == 0) {
			imgList.addElement(new String[]{"", Messages.FOTO_NOVO_CLIENTE});
	    }
		return imgList;
	}
	
	private void carregaFotosDaTabelaFotoNovoCliente(Vector imgList) throws SQLException {
		Vector fotoClienteErpList = novoCliente.getFotoNovoClienteList();
		for (int i = 0; i < fotoClienteErpList.size(); i++) {
			FotoNovoCliente fotoClienteErp = (FotoNovoCliente) fotoClienteErpList.items[i];
			String nmFoto = fotoClienteErp.nmFoto.substring(0, fotoClienteErp.nmFoto.lastIndexOf(".")); // para não pegar o .jpg
			String nmExtensao = fotoClienteErp.nmFoto.substring(fotoClienteErp.nmFoto.lastIndexOf("."), fotoClienteErp.nmFoto.length());
			imgList.addElement(new String[]{nmFoto, Messages.FOTO_NOVO_CLIENTE, nmExtensao});
		}
	}
	
	@Override
	protected void btNovaFotoClick() throws SQLException {
		int cdFotoNovoCliente = FotoNovoClienteService.getInstance().getSequencialCdFotoNovoCliente(novoCliente) + 1;
		String nmFoto = TimeUtil.getCurrentDateTimeYYYYMMDDHHMMSS() + "_" + novoCliente.cdNovoCliente + "_" + cdFotoNovoCliente;
		try {
			BaseCamera camera = new BaseCamera();
			camera.setFileName(nmFoto);
			if (LavenderePdaConfig.getResolucaoCameraRegistroFotos() > 0) {
				camera.setResolution(LavenderePdaConfig.getResolucaoCameraRegistroFotos());
			}
			try {
				setCameraType(camera);
			} catch (Throwable e) {
				return;
			}
			String pathFoto = camera.click();
			if (ValueUtil.isNotEmpty(pathFoto)) {
				if (VmUtil.isSimulador()) {
					FotoUtil.copyToSD(pathFoto, NovoCliente.getPathImg(), camera.defaultFileName);
				} else {
					FotoUtil.saveScaledPhoto(pathFoto, NovoCliente.getPathImg(), camera.defaultFileName, camera.cameraType, LavenderePdaConfig.getNuResolucaoFotoCameraNativa(), ".png");
				}
				FotoNovoClienteService.getInstance().insereFotoNovoClienteNaMemoria(novoCliente, camera.defaultFileName);
				btFechar.setText(Messages.BT_CONFIRMAR);
				imageCarrousel.setImgList(geraListaImagemCarrousel(), new String[]{nmFoto, Messages.FOTO_NOVO_CLIENTE, ".png"});
			}
		} catch (NoFileSelectedException e) {
			ExceptionUtil.handle(e);
		} catch (ImageNoSupportedException e) {
			UiUtil.showErrorMessage(e);
		} catch (Throwable ex) {
			FotoNovoClienteService.getInstance().excluiFotoNovoCliente(novoCliente, nmFoto);
			UiUtil.showErrorMessage(ex);
		} 
	}

	@Override
	protected void btExcluirClick() throws SQLException {
		if (ValueUtil.isEmpty(imageCarrousel.getSelectedImage()[0])) {
			UiUtil.showWarnMessage(Messages.NOVOCLIENTE_MSG_NENHUMA_FOTO_DISPONIVEL);
    		return;
		}
		if (UiUtil.showConfirmDeleteMessage(Messages.NOVOCLIENTE_LABEL_EXCLUSAO_FOTO)) {
			String nmFoto = imageCarrousel.getSelectedImage()[0] + imageCarrousel.getSelectedImage()[2];
			if (FotoNovoClienteService.getInstance().isPermiteExcluirFoto(novoCliente, nmFoto)) {
				FotoNovoClienteService.getInstance().excluiFotoNovoCliente(novoCliente, nmFoto);
				btFechar.setText(Messages.BT_CONFIRMAR);
			} else {
				UiUtil.showWarnMessage(Messages.CLIENTE_MSG_EXCLUSAO_FOTO_NAO_PERMITIDA);
			}
 			imageCarrousel.setImgList(geraListaImagemCarrousel());
			repaint();
		}
	}
	
}
