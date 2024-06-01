package br.com.wmw.lavenderepda.presentation.ui.ext;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ImageNoSupportedException;
import br.com.wmw.framework.exception.NoFileSelectedException;
import br.com.wmw.framework.media.camera.BaseCamera;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.ImageCarrousel;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.FotoPesquisaMercado;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercado;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.service.FotoPesquisaMercadoService;
import br.com.wmw.lavenderepda.business.service.ProdutoConcorrenteService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.presentation.ui.AdmSenhaDinamicaWindow;
import totalcross.sys.SpecialKeys;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.util.Vector;

public class ImageSliderPesquisaMercadoWindow extends BaseImageSliderWindow {

	private PesquisaMercado pesquisaMercado;
	private ButtonPopup btSenha;
	private boolean enabled;
	private boolean editing;
	private Cliente cliente;
	
	public ImageSliderPesquisaMercadoWindow(Cliente cliente, PesquisaMercado pesquisaMercado, boolean enabled, boolean editing) throws SQLException {
		super(ValueUtil.VALOR_NI);
		this.pesquisaMercado = pesquisaMercado;
		this.enabled = enabled;
		this.editing = editing;
		this.cliente = cliente;
		imageCarrousel = new ImageCarrousel(geraListaImagemCarrousel(), FotoPesquisaMercado.getPathImg(), true);
		btSenha = new ButtonPopup(FrameworkMessages.BOTAO_SENHA);
		setDefaultWideRect();
	}
		
	@Override
	protected void addButtons() {
		if (enabled) {
			addButtonPopup(btNovo);
			addButtonPopup(btExcluir);
		}
		if ((LavenderePdaConfig.obrigaCadastroFotoProdutoNaPesquisaDeMercado() && !SessionLavenderePda.autorizadoPorSenhaFotosPesquisaMercado) && enabled) {
			addButtonPopup(btSenha);
		}
	}
		
	private Vector geraListaImagemCarrousel() throws SQLException {
		Vector imgList = new Vector();
	    carregaFotosDaTabelaFotoPesquisaMercado(imgList);
	    if (ValueUtil.isEmpty(imgList)) {
			imgList.addElement(new String[] {"", getNmFoto()});
	    }
		return imgList;
	}

	private String getNmFoto() throws SQLException {
		if (pesquisaMercado.isTipoPesquisaGondola() || pesquisaMercado.isTipoPesquisaValor()) {
			return ProdutoConcorrenteService.getInstance().getProdutoByPesqMercado(pesquisaMercado).toString();
		} else {
			return ProdutoService.getInstance().getDsProduto(pesquisaMercado.cdProduto);
		}
	}
	
	private void carregaFotosDaTabelaFotoPesquisaMercado(Vector imgList) throws SQLException {
		FotoPesquisaMercado fotoPesquisaMercadoFilter = FotoPesquisaMercadoService.getInstance().getDefaultFotoPesquisaMercadoFilter(pesquisaMercado);
		Vector fotoPesquisaMercadoList = new Vector();
		Vector fotosBanco = FotoPesquisaMercadoService.getInstance().findAllByExample(fotoPesquisaMercadoFilter);
		if (!fotosBanco.isEmpty()) {
			fotoPesquisaMercadoList.addElements(fotosBanco.items);
		}
		if (!editing && pesquisaMercado.fotoList.size() > 0) {
			fotoPesquisaMercadoList.addElements(pesquisaMercado.fotoList.items);
		}
		int size = fotoPesquisaMercadoList.size();
		for (int i = 0; i < size; i++) {
			FotoPesquisaMercado fotoPesquisaMercado = (FotoPesquisaMercado) fotoPesquisaMercadoList.items[i];
			if (fotoPesquisaMercado == null) {
				continue;
			}
			String nmFoto = fotoPesquisaMercado.nmFoto.substring(0, fotoPesquisaMercado.nmFoto.lastIndexOf("."));
			String nmExtensao = fotoPesquisaMercado.nmFoto.substring(fotoPesquisaMercado.nmFoto.lastIndexOf("."));
			imgList.addElement(new String[] {nmFoto, getNmFoto(), nmExtensao});
		}
	}

	@Override
	protected void btNovaFotoClick() throws SQLException {
		int cdFotoPesquisaMercado = FotoPesquisaMercadoService.getInstance().getSequencialCdFotoPesquisaMercado(pesquisaMercado) + 1;
		String nmFoto = TimeUtil.getCurrentDateTimeYYYYMMDDHHMMSS() + "_" + pesquisaMercado.cdPesquisaMercado + "_" + cdFotoPesquisaMercado;
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
					FotoUtil.copyToSD(pathFoto, FotoPesquisaMercado.getPathImg(), camera.defaultFileName);
				} else {
					FotoUtil.saveScaledPhoto(pathFoto, FotoPesquisaMercado.getPathImg(), camera.defaultFileName, camera.cameraType, LavenderePdaConfig.getNuResolucaoFotoCameraNativa(), ".png");
				}
				FotoPesquisaMercado fotoPesquisaMercado = FotoPesquisaMercadoService.getInstance().getFotoPesquisaMercado(pesquisaMercado, camera.defaultFileName, cdFotoPesquisaMercado);
				if (editing) {
					FotoPesquisaMercadoService.getInstance().insert(fotoPesquisaMercado);
				}
				pesquisaMercado.fotoList.addElement(fotoPesquisaMercado);
				btFechar.setText(Messages.BT_CONFIRMAR);
				imageCarrousel.setImgList(geraListaImagemCarrousel(), new String[]{nmFoto, pesquisaMercado.cdPesquisaMercado, ".png"});
			}
			repaint();
		} catch (NoFileSelectedException e) {
			ExceptionUtil.handle(e);
		} catch (ImageNoSupportedException e) {
			UiUtil.showErrorMessage(e);
		} catch (Throwable ex) {
			FotoPesquisaMercadoService.getInstance().excluiFotoPesquisaMercado(pesquisaMercado, nmFoto, editing);
			UiUtil.showErrorMessage(ex);
		} 
	}

	@Override
	protected void btExcluirClick() throws SQLException {
		if (ValueUtil.isEmpty(imageCarrousel.getSelectedImage()[0])) {
			UiUtil.showWarnMessage(Messages.FOTO_PESQUISA_MERCADO_MSG_NENHUMA_FOTO_DISPONIVEL);
    		return;
		}
		if (editing && enabled && LavenderePdaConfig.obrigaCadastroFotoProdutoNaPesquisaDeMercado() && imageCarrousel.imgList.size() == 1 && !SessionLavenderePda.autorizadoPorSenhaFotosPesquisaMercado) {
			UiUtil.showErrorMessage(Messages.FOTO_PESQUISA_MERCADO_EXCLUSAO_NAO_PERMITIDA);
		} else {
			if (UiUtil.showConfirmDeleteMessage(Messages.FOTO_PESQUISA_MERCADO_LABEL_EXCLUSAO_FOTO)) {
				String nmFoto = imageCarrousel.getSelectedImage()[0] + imageCarrousel.getSelectedImage()[2];
				if (FotoPesquisaMercadoService.getInstance().isPermiteExcluirFoto(pesquisaMercado, nmFoto, editing)) {
					FotoPesquisaMercadoService.getInstance().excluiFotoPesquisaMercado(pesquisaMercado, nmFoto, editing);
					btFechar.setText(Messages.BT_CONFIRMAR);
				} else {
					UiUtil.showWarnMessage(Messages.MSG_EXCLUSAO_FOTO_NAO_PERMITIDA);
				}
				imageCarrousel.setImgList(geraListaImagemCarrousel());
				repaint();
			}
		}
	}

	private void btSenhaClick() throws SQLException {
		AdmSenhaDinamicaWindow senha = new AdmSenhaDinamicaWindow();
		senha.setMensagem(Messages.FOTO_PESQUISA_MERCADO_SENHA);
		senha.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_CAD_FOTOS_PESQUISA_MERCADO);
		senha.setCdCliente(cliente.cdCliente);
		if (senha.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
			SessionLavenderePda.autorizadoPorSenhaFotosPesquisaMercado = true;
			UiUtil.showSucessMessage(Messages.FOTO_PESQUISA_MERCADO_SUCESS);
			unpop();
		}
	}
	
	@Override
	protected void btFecharClick() throws SQLException {
		fecharWindow();
		closedByBtFechar = !SessionLavenderePda.autorizadoPorSenhaFotosPesquisaMercado;
	}

	@Override
	public void onWindowEvent(Event event) throws SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btSenha) {
					try {
						btSenhaClick();
					} catch (Throwable e) {
						ExceptionUtil.handle(e);
					}
				}
				break;
			}
			case KeyEvent.SPECIAL_KEY_PRESS: {
				if (((KeyEvent) event).key == SpecialKeys.ESCAPE) {
					closedByBtFechar = !SessionLavenderePda.autorizadoPorSenhaFotosPesquisaMercado;
					unpop();
				}
				break;
			}
		}
	}

}
