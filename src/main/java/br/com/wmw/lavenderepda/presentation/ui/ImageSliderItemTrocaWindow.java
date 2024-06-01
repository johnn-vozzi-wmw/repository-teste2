package br.com.wmw.lavenderepda.presentation.ui;

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
import br.com.wmw.lavenderepda.business.domain.FotoItemTroca;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.service.FotoItemTrocaService;
import br.com.wmw.lavenderepda.presentation.ui.ext.BaseImageSliderWindow;
import totalcross.util.Vector;

import java.sql.SQLException;

public class ImageSliderItemTrocaWindow extends BaseImageSliderWindow {
	
	private ItemPedido itemTroca;
	private boolean editing;
	
	public ImageSliderItemTrocaWindow(ItemPedido itemTroca, boolean editing) throws SQLException {
		super(Messages.TITLE_FOTOS_ITEM_TROCA);
		this.itemTroca = itemTroca;
		this.editing = editing;
		imageCarrousel = new ImageCarrousel(geraListaImagemCarrousel(), FotoItemTroca.getPathImg(), true);
		setDefaultWideRect();
	}

	@Override
	protected void addButtons() {
		if (itemTroca.pedido.isPedidoAberto() || (itemTroca.pedido.isPedidoFechado() && !itemTroca.pedido.isEnviadoServidor())) {
			addButtonPopup(btNovo);
			addButtonPopup(btExcluir);
		}
	}

	private Vector geraListaImagemCarrousel() throws SQLException {
		Vector imgList = new Vector();
		carregaFotosDaTabela(imgList);
		if (ValueUtil.isEmpty(imgList)) {
			imgList.addElement(new String[]{"", getNmFoto()});
		}
		return imgList;
	}

	private String getNmFoto() {
		if (!editing) {
			return itemTroca.cdEmpresa + ";" + itemTroca.cdRepresentante + ";" + itemTroca.cdProduto;
		}
		return itemTroca.toString();
	}

	private void carregaFotosDaTabela(Vector imgList) throws SQLException {
		FotoItemTroca fotoItemTrocaFilter = new FotoItemTroca();
		fotoItemTrocaFilter.cdEmpresa = itemTroca.cdEmpresa;
		fotoItemTrocaFilter.cdRepresentante = itemTroca.cdRepresentante;
		fotoItemTrocaFilter.nuPedido = itemTroca.nuPedido;
		fotoItemTrocaFilter.flOrigemPedido = itemTroca.flOrigemPedido;
		fotoItemTrocaFilter.cdProduto = itemTroca.cdProduto;
		fotoItemTrocaFilter.flTipoItemPedido = itemTroca.flTipoItemPedido;
		fotoItemTrocaFilter.nuSeqProduto = itemTroca.nuSeqProduto;
		Vector fotoItemTrocaList = FotoItemTrocaService.getInstance().findAllByExample(fotoItemTrocaFilter);
		int size = fotoItemTrocaList.size();
		for (int i = 0; i < size; i++) {
			FotoItemTroca fotoItemTroca = (FotoItemTroca) fotoItemTrocaList.items[i];
			String nmFoto = fotoItemTroca.nmFoto.substring(0, fotoItemTroca.nmFoto.lastIndexOf("."));
			String nmExtensao = fotoItemTroca.nmFoto.substring(fotoItemTroca.nmFoto.lastIndexOf("."));
			imgList.addElement(new String[]{nmFoto, getNmFoto(), nmExtensao});
		}
	}
	
	@Override
	protected void btNovaFotoClick() throws SQLException {
		int cdFotoItemTroca = FotoItemTrocaService.getInstance().getSequencialCdFotoItemTroca(itemTroca) + 1;
		String nmFoto = TimeUtil.getCurrentDateTimeYYYYMMDDHHMMSS() + "_" + itemTroca.nuPedido + "_" + itemTroca.cdProduto + "_" + cdFotoItemTroca;
		boolean isPopNeeded = false;
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
					FotoUtil.copyToSD(pathFoto, FotoItemTroca.getPathImg(), camera.defaultFileName);
				} else {
					FotoUtil.saveScaledPhoto(pathFoto, FotoItemTroca.getPathImg(), camera.defaultFileName, camera.cameraType, LavenderePdaConfig.getNuResolucaoFotoCameraNativa(), ".png");
				}
				FotoItemTroca fotoItemTroca = FotoItemTrocaService.getInstance().insereFotoItemTroca(itemTroca, camera.defaultFileName, cdFotoItemTroca);
				if (!editing) {
					if (itemTroca.fotoItemTrocaList == null) {
						itemTroca.fotoItemTrocaList = new Vector();
					}
					itemTroca.fotoItemTrocaList.addElement(fotoItemTroca);
					isPopNeeded = true;
				}
				btFechar.setText(Messages.BT_CONFIRMAR);
				imageCarrousel.setImgList(geraListaImagemCarrousel(), new String[]{nmFoto, itemTroca.nuPedido + "_" + itemTroca.cdProduto, ".png"});
			}
			repaint();
		} catch (NoFileSelectedException e) {
			ExceptionUtil.handle(e);
		} catch (ImageNoSupportedException e) {
			UiUtil.showErrorMessage(e);
		} catch (Throwable ex) {
			FotoItemTrocaService.getInstance().excluiFotoItemTroca(itemTroca, nmFoto, cdFotoItemTroca);
			if (isPopNeeded) {
				try {
					itemTroca.fotoItemTrocaList.pop();
				} catch (Throwable e) {
					ExceptionUtil.handle(e);
				}
			}
			UiUtil.showErrorMessage(ex);
		}
	}

	@Override
	protected void btExcluirClick() throws SQLException {
		if (ValueUtil.isEmpty(imageCarrousel.getSelectedImage()[0])) {
			UiUtil.showWarnMessage(Messages.MSG_FOTOITEMTROCA_NENHUMA_FOTO_DISPONIVEL);
			return;
		}
		if (UiUtil.showConfirmDeleteMessage(Messages.MSG_EXCLUSAO_FOTO_ITEM_TROCA)) {
			String nmFoto = imageCarrousel.getSelectedImage()[0] + imageCarrousel.getSelectedImage()[2];
			int cdFotoItemTroca = ValueUtil.getIntegerValue(imageCarrousel.getSelectedImage()[0].substring(imageCarrousel.getSelectedImage()[0].lastIndexOf("_") + 1));
			if (FotoItemTrocaService.getInstance().isPermiteExcluirFoto(itemTroca, cdFotoItemTroca)) {
				FotoItemTrocaService.getInstance().excluiFotoItemTroca(itemTroca, nmFoto, cdFotoItemTroca);
				btFechar.setText(Messages.BT_CONFIRMAR);
			} else {
				UiUtil.showWarnMessage(Messages.MSG_EXCLUSAO_FOTO_NAO_PERMITIDA);
			}
			imageCarrousel.setImgList(geraListaImagemCarrousel());
			repaint();
		}
	}
}
