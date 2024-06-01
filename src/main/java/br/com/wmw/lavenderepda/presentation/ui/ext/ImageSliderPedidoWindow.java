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
import br.com.wmw.lavenderepda.business.domain.FotoPedido;
import br.com.wmw.lavenderepda.business.domain.FotoPedidoErp;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.FotoPedidoErpService;
import br.com.wmw.lavenderepda.business.service.FotoPedidoService;
import totalcross.util.Vector;

public class ImageSliderPedidoWindow extends BaseImageSliderWindow {
	
	private Pedido pedido;
	private boolean editing;

	public ImageSliderPedidoWindow(Pedido pedido, boolean editing) throws SQLException {
		super(ValueUtil.VALOR_NI);
		this.pedido = pedido;
		this.editing = editing;
		imageCarrousel = new ImageCarrousel(geraListaImagemCarrousel(), FotoPedido.getPathImg(), true);
		setDefaultWideRect();
	}
	
	protected void addButtons() {
		if (pedido.isPedidoAberto() || (pedido.isPedidoFechado() && !pedido.isEnviadoServidor())) {
			addButtonPopup(btNovo);
			addButtonPopup(btExcluir);
		}
	}
	
	private Vector geraListaImagemCarrousel() throws SQLException {
		Vector imgList = new Vector();
	    carregaFotosDaTabelaFotoPedidoErp(imgList);
	    carregaFotosDaTabelaFotoPedido(imgList);
	    if (ValueUtil.isEmpty(imgList)) {
			imgList.addElement(new String[]{"", getNmFoto()});
	    }
		return imgList;
	}

//	private String getNmFoto() {
//		if (!editing) {
//			return pedido.cdEmpresa + ";" + pedido.cdRepresentante;
//		}
//		return pedido.toString();
//	}
	
	
	private String getNmFoto() throws SQLException {
		String nmFoto = "";
			Vector fotoPedidoList = carregaListaDeFotoPedido();
			for(int i = 0; i < fotoPedidoList.size(); i++) {
				FotoPedido fotoPedido = (FotoPedido) fotoPedidoList.items[i];
				nmFoto = fotoPedido.nmFoto.substring(0, fotoPedido.nmFoto.lastIndexOf(".")); // para não pegar o .jpg
			}
		
		return nmFoto;
	
	}
	
	private Vector carregaListaDeFotoPedido() throws SQLException {
		FotoPedido fotoPedidoFilter = new FotoPedido();
		fotoPedidoFilter.cdEmpresa = pedido.cdEmpresa;
		fotoPedidoFilter.cdRepresentante = pedido.cdRepresentante;
		fotoPedidoFilter.nuPedido = pedido.nuPedido;
		fotoPedidoFilter.flOrigemPedido = pedido.flOrigemPedido;
		fotoPedidoFilter.forceFilter = true;
		Vector fotoPedidoList = FotoPedidoService.getInstance().findAllByExample(fotoPedidoFilter);
		return fotoPedidoList;
	}
	
	private void carregaFotosDaTabelaFotoPedido(Vector imgList) throws SQLException {
//		FotoPedido fotoPedidoFilter = new FotoPedido();
//		fotoPedidoFilter.cdEmpresa = pedido.cdEmpresa;
//		fotoPedidoFilter.cdRepresentante = pedido.cdRepresentante;
//		fotoPedidoFilter.nuPedido = pedido.nuPedido;
//		fotoPedidoFilter.flOrigemPedido = pedido.flOrigemPedido;
//		fotoPedidoFilter.forceFilter = true;
		Vector fotoPedidoList = carregaListaDeFotoPedido();
		for (int i = 0; i < fotoPedidoList.size(); i++) {
			FotoPedido fotoPedido = (FotoPedido) fotoPedidoList.items[i];
			String nmFoto = fotoPedido.nmFoto.substring(0, fotoPedido.nmFoto.lastIndexOf(".")); // para não pegar o .jpg
			System.out.println(nmFoto);
			String nmExtensao = fotoPedido.nmFoto.substring(fotoPedido.nmFoto.lastIndexOf("."), fotoPedido.nmFoto.length());
			imgList.addElement(new String[]{nmFoto, getNmFoto(), nmExtensao});
		}
	}
	
	private void carregaFotosDaTabelaFotoPedidoErp(Vector imgList) throws SQLException {
		FotoPedidoErp fotoPedidoErpFilter = new FotoPedidoErp();
		fotoPedidoErpFilter.cdEmpresa = pedido.cdEmpresa;
		fotoPedidoErpFilter.cdRepresentante = pedido.cdRepresentante;
		fotoPedidoErpFilter.nuPedido = pedido.nuPedido;
		fotoPedidoErpFilter.flOrigemPedido = pedido.flOrigemPedido;
		Vector fotoPedidoErpList = FotoPedidoErpService.getInstance().findAllByExample(fotoPedidoErpFilter);
		for (int i = 0; i < fotoPedidoErpList.size(); i++) {
			FotoPedidoErp fotoPedidoErp = (FotoPedidoErp) fotoPedidoErpList.items[i];
			String nmFoto = fotoPedidoErp.nmFoto.substring(0, fotoPedidoErp.nmFoto.lastIndexOf(".")); // para não pegar o .jpg
			String nmExtensao = fotoPedidoErp.nmFoto.substring(fotoPedidoErp.nmFoto.lastIndexOf("."), fotoPedidoErp.nmFoto.length());
			imgList.addElement(new String[]{nmFoto, getNmFoto(), nmExtensao});
		}
	}
	
	@Override
	protected void btNovaFotoClick() throws SQLException {
		int cdFotoPedido = FotoPedidoService.getInstance().getSequencialCdFotoPedido(pedido) + 1;
		String nmFoto = TimeUtil.getCurrentDateTimeYYYYMMDDHHMMSS() + "_" + pedido.nuPedido + "_" + cdFotoPedido;
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
					FotoUtil.copyToSD(pathFoto, FotoPedido.getPathImg(), camera.defaultFileName);
				} else {
					FotoUtil.saveScaledPhoto(pathFoto, FotoPedido.getPathImg(), camera.defaultFileName, camera.cameraType, LavenderePdaConfig.getNuResolucaoFotoCameraNativa(), ".png");
				}
				FotoPedido fotoPedido = FotoPedidoService.getInstance().insereFotoPedido(pedido, camera.defaultFileName, cdFotoPedido);
				if (!editing) {
					pedido.fotoList.addElement(fotoPedido);
				}
				btFechar.setText(Messages.BT_CONFIRMAR);
				imageCarrousel.setImgList(geraListaImagemCarrousel(), new String[]{nmFoto, pedido.nuPedido, ".png"});
			}
			repaint();
		} catch (NoFileSelectedException e) {
			ExceptionUtil.handle(e);
		} catch (ImageNoSupportedException e) {
			UiUtil.showErrorMessage(e);
		} catch (Throwable ex) {
			FotoPedidoService.getInstance().excluiFotoPedido(pedido, nmFoto, true);
			UiUtil.showErrorMessage(ex);
		} 
	}

	@Override
	protected void btExcluirClick() throws SQLException {
		if (ValueUtil.isEmpty(imageCarrousel.getSelectedImage()[0])) {
			UiUtil.showWarnMessage(Messages.FOTOPEDIDO_MSG_NENHUMA_FOTO_DISPONIVEL);
    		return;
		}
		if (UiUtil.showConfirmDeleteMessage(Messages.FOTOPEDIDO_LABEL_EXCLUSAO_FOTO)) {
			String nmFoto = imageCarrousel.getSelectedImage()[0] + imageCarrousel.getSelectedImage()[2];
			if (FotoPedidoService.getInstance().isPermiteExcluirFoto(pedido, nmFoto)) {
				FotoPedidoService.getInstance().excluiFotoPedido(pedido, nmFoto, true);
				btFechar.setText(Messages.BT_CONFIRMAR);
			} else {
				UiUtil.showWarnMessage(Messages.MSG_EXCLUSAO_FOTO_NAO_PERMITIDA);
			}
 			imageCarrousel.setImgList(geraListaImagemCarrousel());
			repaint();
		}
	}
	
}
