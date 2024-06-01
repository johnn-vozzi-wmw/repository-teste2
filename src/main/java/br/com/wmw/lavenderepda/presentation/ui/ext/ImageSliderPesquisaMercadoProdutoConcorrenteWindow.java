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
import br.com.wmw.lavenderepda.business.domain.FotoPesqMerProdConc;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConfig;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoProduto;
import br.com.wmw.lavenderepda.business.service.FotoPesqMerProdConcService;
import totalcross.util.Vector;

public class ImageSliderPesquisaMercadoProdutoConcorrenteWindow extends BaseImageSliderWindow {

	private static final String SEPARADOR = "_";
	private final boolean readOnly;

	private PesquisaMercadoConfig pesquisaMercadoConfig;
	private PesquisaMercadoProduto pesquisaMercadoProduto;

	public ImageSliderPesquisaMercadoProdutoConcorrenteWindow(PesquisaMercadoConfig pesquisaMercadoConfig, PesquisaMercadoProduto pesquisaMercadoProduto, boolean readOnly) throws SQLException {
		super(ValueUtil.VALOR_NI);
		this.pesquisaMercadoConfig = pesquisaMercadoConfig;
		this.pesquisaMercadoProduto = pesquisaMercadoProduto;
		this.readOnly = readOnly;
		imageCarrousel = new ImageCarrousel(geraListaImagemCarrousel(), FotoPesqMerProdConc.getImagePath(), true);
		setDefaultWideRect();
	}

	public ImageSliderPesquisaMercadoProdutoConcorrenteWindow(PesquisaMercadoConfig pesquisaMercadoConfig, boolean readOnly) throws SQLException {
		this(pesquisaMercadoConfig, null, readOnly);
	}

	@Override
	protected void addButtons() {
		if (!readOnly) {
			addButtonPopup(btNovo);
			addButtonPopup(btExcluir);
		}
	}

	private Vector geraListaImagemCarrousel() throws SQLException {
		Vector imgList = new Vector();
		FotoPesqMerProdConc fotoPesqMerProdConcFilter = FotoPesqMerProdConcService.getInstance().getDefaultFotoPesquisaMercadoFilter(pesquisaMercadoConfig, pesquisaMercadoProduto);
		Vector fotoPesquisaMercadoList = FotoPesqMerProdConcService.getInstance().findAllByExample(fotoPesqMerProdConcFilter);
		for (Object obj : fotoPesquisaMercadoList.items) {
			FotoPesqMerProdConc fotoPesqMerProdConc = (FotoPesqMerProdConc) obj;
			if (fotoPesqMerProdConc != null) {
				String nmFoto = fotoPesqMerProdConc.nmFoto.substring(0, fotoPesqMerProdConc.nmFoto.lastIndexOf("."));
				String nmExtensao = fotoPesqMerProdConc.nmFoto.substring(fotoPesqMerProdConc.nmFoto.lastIndexOf("."));
				imgList.addElement(new String[]{nmFoto, getNmFoto(), nmExtensao});
			}
		}
		if (ValueUtil.isEmpty(imgList)) {
			imgList.addElement(new String[]{ValueUtil.VALOR_NI, ValueUtil.VALOR_NI});
		}
		return imgList;
	}

	private String getNmFoto() {
		if (pesquisaMercadoProduto != null) {
			return pesquisaMercadoProduto.getDescription().trim();
		} else {
			return pesquisaMercadoConfig.getDescription().trim();
		}
	}

	@Override
	protected void btNovaFotoClick() throws SQLException {
		int cdFotoPesquisaMercado = FotoPesqMerProdConcService.getInstance().getSequencialCdFotoPesquisaMercado(pesquisaMercadoConfig, pesquisaMercadoProduto) + 1;
		String nmFoto = getFileName(pesquisaMercadoConfig, pesquisaMercadoProduto, cdFotoPesquisaMercado);
		FotoPesqMerProdConc fotoPesqMerProdConc = null;
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
					FotoUtil.copyToSD(pathFoto, FotoPesqMerProdConc.getImagePath(), camera.defaultFileName);
				} else {
					FotoUtil.saveScaledPhoto(pathFoto, FotoPesqMerProdConc.getImagePath(), camera.defaultFileName, camera.cameraType, LavenderePdaConfig.getNuResolucaoFotoCameraNativa(), FotoUtil.DSEXTESAO_PNG);
				}
				fotoPesqMerProdConc = FotoPesqMerProdConcService.getInstance().getDefaultFotoPesquisaMercadoFilter(pesquisaMercadoConfig, pesquisaMercadoProduto);
				fotoPesqMerProdConc.nmFoto = nmFoto + FotoUtil.DSEXTESAO_PNG;
				fotoPesqMerProdConc.cdFoto = cdFotoPesquisaMercado;
				FotoPesqMerProdConcService.getInstance().insert(fotoPesqMerProdConc);
				btFechar.setText(Messages.BT_CONFIRMAR);
				imageCarrousel.setImgList(geraListaImagemCarrousel());
				imageCarrousel.repaintNow();
			}
			repaint();
		} catch (NoFileSelectedException e) {
			ExceptionUtil.handle(e);
		} catch (ImageNoSupportedException e) {
			UiUtil.showErrorMessage(e);
		} catch (Throwable ex) {
			FotoPesqMerProdConcService.getInstance().excluiFotoPesquisaMercado(nmFoto, fotoPesqMerProdConc);
		}
	}

	private String getFileName(PesquisaMercadoConfig pesquisaMercadoConfig, PesquisaMercadoProduto pesquisaMercadoProduto, int cdFotoPesquisaMercado) {
		StringBuilder nmFoto = new StringBuilder();
		nmFoto.append(TimeUtil.getCurrentDateTimeYYYYMMDDHHMMSS()).append(SEPARADOR);
		nmFoto.append(pesquisaMercadoConfig.cdPesquisaMercadoConfig).append(SEPARADOR);
		if (pesquisaMercadoProduto != null) {
			nmFoto.append(pesquisaMercadoProduto.cdProduto);
		} else {
			nmFoto.append(FotoPesqMerProdConc.DEFAULT_CDPRODUTO);
		}
		nmFoto.append(SEPARADOR);
		nmFoto.append(cdFotoPesquisaMercado);
		return nmFoto.toString();
	}

	@Override
	protected void btExcluirClick() throws SQLException {
		if (ValueUtil.isEmpty(imageCarrousel.getSelectedImage()[0])) {
			UiUtil.showWarnMessage(Messages.FOTO_PESQUISA_MERCADO_MSG_NENHUMA_FOTO_DISPONIVEL);
			return;
		}
		if (UiUtil.showConfirmDeleteMessage(Messages.FOTO_PESQUISA_MERCADO_LABEL_EXCLUSAO_FOTO)) {
			String nmFoto = imageCarrousel.getSelectedImage()[0] + imageCarrousel.getSelectedImage()[2];
			if (LavenderePdaConfig.obrigaInclusaoFotoPesquisaMercado() && imageCarrousel.getSelectedImage().length == 1) {
				UiUtil.showWarnMessage(Messages.MSG_EXCLUSAO_FOTO_NAO_PERMITIDA);
			} else {
				FotoPesqMerProdConcService.getInstance().excluiFotoPesquisaMercado(nmFoto, FotoPesqMerProdConcService.getInstance().getDefaultFotoPesquisaMercadoFilter(pesquisaMercadoConfig, pesquisaMercadoProduto));
			}
		}
		imageCarrousel.setImgList(geraListaImagemCarrousel());
		repaint();
	}

}
