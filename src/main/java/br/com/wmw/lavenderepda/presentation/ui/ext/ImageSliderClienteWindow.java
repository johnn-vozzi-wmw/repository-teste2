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
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.FotoCliente;
import br.com.wmw.lavenderepda.business.domain.FotoClienteErp;
import br.com.wmw.lavenderepda.business.domain.FotoNovoCliente;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.service.FotoClienteErpService;
import br.com.wmw.lavenderepda.business.service.FotoClienteService;
import br.com.wmw.lavenderepda.business.service.FotoNovoClienteService;
import br.com.wmw.lavenderepda.business.service.NovoClienteService;
import totalcross.util.Vector;

public class ImageSliderClienteWindow extends BaseImageSliderWindow {
	
	private Cliente cliente;
	private boolean imageCarrouselVazio;

	public ImageSliderClienteWindow(Cliente cliente) throws SQLException {
		super(ValueUtil.VALOR_NI);
		this.cliente = cliente;
		imageCarrouselVazio = false;
		imageCarrousel = new ImageCarrousel(geraListaImagemCarrousel(), Cliente.getPathImg(), true);
		if (cliente.isNovoCliente() && imageCarrouselVazio) {
			imageCarrousel = new ImageCarrousel(carregaFotosDaTabelaFotoNovoCliente(), NovoCliente.getPathImg(), true);
		}
		setDefaultWideRect();
	}
	
	@Override
	protected void addButtons() {
		if (LavenderePdaConfig.isPermiteRegistrarNovasFotosDeCliente()) {
			addButtonPopup(btNovo);
		}
		if (LavenderePdaConfig.isPermiteExcluirFotosDeCliente()) {
			addButtonPopup(btExcluir);
		}
	}
	
	private Vector geraListaImagemCarrousel() throws SQLException {
		Vector imgList = new Vector();
	    carregaFotosDaTabelaFotoClienteErp(imgList);
	    carregaFotosDaTabelaFotoCliente(imgList);	
	    if (imgList.size() == 0) {
			imgList.addElement(new String[]{"", cliente.toString()});
			imageCarrouselVazio = true;
	    }
		return imgList;
	}
	
	private void carregaFotosDaTabelaFotoClienteErp(Vector imgList) throws SQLException {
		FotoClienteErp fotoClienteErpFilter = new FotoClienteErp();
		fotoClienteErpFilter.cdEmpresa = cliente.cdEmpresa;
		fotoClienteErpFilter.cdRepresentante = cliente.cdRepresentante;
		fotoClienteErpFilter.cdCliente = cliente.cdCliente;
		Vector fotoClienteErpList = FotoClienteErpService.getInstance().findAllByExample(fotoClienteErpFilter);
		for (int i = 0; i < fotoClienteErpList.size(); i++) {
			FotoClienteErp fotoClienteErp = (FotoClienteErp) fotoClienteErpList.items[i];
			String nmFoto = fotoClienteErp.nmFoto.substring(0, fotoClienteErp.nmFoto.lastIndexOf(".")); // para não pegar o .jpg
			String nmExtensao = fotoClienteErp.nmFoto.substring(fotoClienteErp.nmFoto.lastIndexOf("."), fotoClienteErp.nmFoto.length());
			imgList.addElement(new String[]{nmFoto, cliente.toString(), nmExtensao});
		}
	}
	
	private void carregaFotosDaTabelaFotoCliente(Vector imgList) throws SQLException {
		if (LavenderePdaConfig.isPermiteRegistrarNovasFotosDeCliente()) {
			FotoCliente fotoClienteFilter = new FotoCliente();
			fotoClienteFilter.cdCliente = cliente.cdCliente;
			Vector fotoClienteList = FotoClienteService.getInstance().findAllByExample(fotoClienteFilter);
			for (int i = 0; i < fotoClienteList.size(); i++) {
				FotoCliente fotoCliente = (FotoCliente) fotoClienteList.items[i];
				if (!ValueUtil.VALOR_SIM.equals(fotoCliente.flFotoExcluida)) {
					String nmFoto = fotoCliente.nmFoto.substring(0, fotoCliente.nmFoto.lastIndexOf(".")); // para não pegar o .jpg
					String nmExtensao = fotoCliente.nmFoto.substring(fotoCliente.nmFoto.lastIndexOf("."), fotoCliente.nmFoto.length());
					imgList.addElement(new String[]{nmFoto, cliente.toString(), nmExtensao});
				}
			}
		}
	}
	
	private Vector carregaFotosDaTabelaFotoNovoCliente() throws SQLException {
		Vector imgList = new Vector();
		Vector fotoClienteErpList = new Vector();
		NovoCliente novoClienteFilter = NovoClienteService.getInstance().getNovoClienteByCliente(cliente);
		FotoNovoCliente fotoNovoClienteFilter = new FotoNovoCliente();
		if (novoClienteFilter != null) {
			fotoNovoClienteFilter.cdEmpresa = cliente.cdEmpresa;
			fotoNovoClienteFilter.cdRepresentante = cliente.cdRepresentante;
			fotoNovoClienteFilter.cdCliente = novoClienteFilter.cdNovoCliente;
			fotoClienteErpList = FotoNovoClienteService.getInstance().findAllByExample(fotoNovoClienteFilter);
		}
		for (int i = 0; i < fotoClienteErpList.size(); i++) {
			FotoNovoCliente fotoClienteErp = (FotoNovoCliente) fotoClienteErpList.items[i];
			String nmFoto = fotoClienteErp.nmFoto.substring(0, fotoClienteErp.nmFoto.lastIndexOf(".")); // para não pegar o .jpg
			String nmExtensao = fotoClienteErp.nmFoto.substring(fotoClienteErp.nmFoto.lastIndexOf("."), fotoClienteErp.nmFoto.length());
			imgList.addElement(new String[]{nmFoto, cliente.toString(), nmExtensao});
		}
	    if (imgList.size() == 0) {
			imgList.addElement(new String[]{"", cliente.toString()});
	    }
	    return imgList;
	}
	
	@Override
	protected void btNovaFotoClick() throws SQLException {
		int cdFotoCliente = FotoClienteService.getInstance().getSequencialCdFotoCliente(cliente) + 1;
		String nmFoto = TimeUtil.getCurrentDateTimeYYYYMMDDHHMMSS() + "_" + cliente.cdCliente + "_" + cdFotoCliente;
		try {
			BaseCamera camera = new BaseCamera();
			camera.setFileName(nmFoto);
			if (LavenderePdaConfig.getResolucaoCameraRegistroFotos() > 0) {
				camera.setResolution(LavenderePdaConfig.getResolucaoCameraRegistroFotos());
			}
			if (LavenderePdaConfig.isUsaCameraNativa()) {
				camera.setCameraType(BaseCamera.CAMERA_NATIVE);
			}
			String pathFoto = camera.click();
			if (ValueUtil.isNotEmpty(pathFoto)) {
				if (VmUtil.isSimulador()) {
					FotoUtil.copyToSD(pathFoto, Cliente.getPathImg(), camera.defaultFileName);
				} else {
					FotoUtil.saveScaledPhoto(pathFoto, Cliente.getPathImg(), camera.defaultFileName, camera.cameraType, LavenderePdaConfig.getNuResolucaoFotoCameraNativa(), ".png");
				}
				FotoClienteService.getInstance().insereFotoCliente(cliente, camera.defaultFileName, cdFotoCliente);
				btFechar.setText(Messages.BT_CONFIRMAR);
				imageCarrousel.setImgList(geraListaImagemCarrousel(), new String[]{nmFoto, cliente.toString(), ".png"});
			} 
			repaint();
		} catch (NoFileSelectedException e) {
			ExceptionUtil.handle(e);
		} catch (ImageNoSupportedException e) {
			UiUtil.showErrorMessage(e);
		} catch (Throwable ex) {
			FotoClienteService.getInstance().excluiFotoCliente(cliente, nmFoto);
			UiUtil.showErrorMessage(ex);
		}
	}
	
	@Override
	protected void btExcluirClick() throws SQLException {
		if (imageCarrousel.getSelectedImage() == null || ValueUtil.isEmpty(imageCarrousel.getSelectedImage()[0])) {
			UiUtil.showWarnMessage(Messages.CLIENTE_MSG_NENHUMA_FOTO_DISPONIVEL);
    		return;
		}
		if (UiUtil.showConfirmDeleteMessage(Messages.CLIENTE_LABEL_EXCLUSAO_FOTO)) {
			String nmFoto = imageCarrousel.getSelectedImage()[0] + imageCarrousel.getSelectedImage()[2];
			if (FotoClienteErpService.getInstance().isExcluiFotoClienteErp(cliente, nmFoto)) {
				FotoClienteErpService.getInstance().excluiFotoClienteErp(cliente, nmFoto);
			} else if (FotoClienteService.getInstance().isPermiteExcluirFoto(cliente, nmFoto)) {
				FotoClienteService.getInstance().excluiFotoCliente(cliente, nmFoto);
				btFechar.setText(Messages.BT_CONFIRMAR);
			} else {
				UiUtil.showWarnMessage(Messages.CLIENTE_MSG_EXCLUSAO_FOTO_NAO_PERMITIDA);
			}
 			imageCarrousel.setImgList(geraListaImagemCarrousel());
			repaint();
		}
	}

}
