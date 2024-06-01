package br.com.wmw.lavenderepda.presentation.ui;


import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.NoFileSelectedException;
import br.com.wmw.framework.media.camera.BaseCamera;
import br.com.wmw.framework.presentation.ui.WmwCadWindow;
import br.com.wmw.framework.presentation.ui.event.ButtonOptionsEvent;
import br.com.wmw.framework.presentation.ui.event.ImageCarrouselEvent;
import br.com.wmw.framework.presentation.ui.ext.ButtonOptions;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.ImageCarrousel;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.domain.VisitaFoto;
import br.com.wmw.lavenderepda.business.service.VisitaFotoService;
import br.com.wmw.lavenderepda.business.service.VisitaService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class CadVisitaFotoWindow extends WmwCadWindow {

	public Visita visita;
	private ButtonPopup btNovo;
	private ButtonOptions btMenuInferior;
	private ImageCarrousel imageCarrouselVisitaFoto;
	private boolean fromRegistroChegadaSaida;

	public CadVisitaFotoWindow(Visita visita) {
		this(visita, false);
	}

	public CadVisitaFotoWindow(Visita visita, boolean fromRegistroChegadaSaida) {
		super(Messages.VISITA_FOTO_LABEL);
		this.visita = visita;
		this.fromRegistroChegadaSaida = fromRegistroChegadaSaida;
		btNovo = new ButtonPopup(FrameworkMessages.BOTAO_NOVO);
		imageCarrouselVisitaFoto = new ImageCarrousel(geraListaImagemCarrousel(visita.getVisitaFotoList()), VisitaFoto.getPathImg(), false);
		imageCarrouselVisitaFoto.hasBotaoRodape = false;
		btMenuInferior = new ButtonOptions();
		btMenuInferior.transparentBackground = false;
		btMenuInferior.setBackColor(ColorUtil.formsBackColor);
		addItensOnButtonMenu();
		setRect(2, 2);
	}

	protected BaseDomain screenToDomain() throws SQLException {
		return null;
	}

	protected void domainToScreen(BaseDomain domain) throws SQLException {
	}

	protected void clearScreen() throws java.sql.SQLException {
	}

	protected BaseDomain createDomain() throws SQLException {
		return null;
	}

	protected String getEntityDescription() {
		return Messages.VISITA_FOTO_LABEL;
	}

	protected CrudService getCrudService() throws java.sql.SQLException {
		return VisitaFotoService.getInstance();
	}

	protected void visibleState() throws java.sql.SQLException {
		btSalvar.setVisible(false);
		btExcluir.setVisible(false);
	}

	protected void addButtons() {
		addButtonPopup(btNovo);
		addButtonPopup(btFechar);
		addButtonOptions(btMenuInferior);
	}

	//@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, imageCarrouselVisitaFoto, LEFT, AFTER, FILL, FILL);
	}

	protected void onPopup() {
		super.onPopup();
		btNovaFotoClick();
	}

	//@Override
	public void onEvent(Event event) {
	    try {
			super.onEvent(event);
	    	switch (event.type) {
				case ControlEvent.PRESSED: {
					if (event.target == btNovo) {
						btNovaFotoClick();
					}
					break;
				}
				case ButtonOptionsEvent.OPTION_PRESS : {
					if (event.target == btMenuInferior) {
						if (btMenuInferior.selectedItem.equals(FrameworkMessages.BOTAO_EXCLUIR)) {
							excluirClick();
						}
					}
					break;
				}
				case ImageCarrouselEvent.IMAGE_CHANGED : {
					if (event.target == imageCarrouselVisitaFoto) {
						visibleState();
						break;
					}
				}
	    	}
    	} catch (Throwable ee) {
    		ee.printStackTrace();
    	}
	}

	private void addItensOnButtonMenu() {
		btMenuInferior.removeAll();
		if (!fromRegistroChegadaSaida) {
			btMenuInferior.addItem(FrameworkMessages.BOTAO_EXCLUIR);
		}
	}

	protected void excluirClick() throws SQLException {
		if (ValueUtil.isEmpty(imageCarrouselVisitaFoto.getSelectedImage())) {
			UiUtil.showWarnMessage(Messages.VISITA_MSG_NENHUMA_FOTO_DISPONIVEL);
    		return;
		}
		if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita && visita.isEnviadoServidor()) {
			UiUtil.showErrorMessage(Messages.MSG_ERRO_VISITA_JA_ENVIADA_SERVIDOR);
			return;
		}
		if (UiUtil.showConfirmDeleteMessage(getEntityDescription())) {
			int size = visita.getVisitaFotoList().size();
			int cdFoto = ValueUtil.getIntegerValue(imageCarrouselVisitaFoto.getSelectedImage()[1]);
			for (int i = 0; i < size; i++) {
				VisitaFoto visitaFoto = (VisitaFoto) visita.getVisitaFotoList().items[i];
				if (cdFoto == visitaFoto.cdFoto) {
					visita.getVisitaFotoExcluidasList().addElement(visitaFoto);
					visita.getVisitaFotoList().removeElement(visitaFoto);
					break;
				}	
			}
			imageCarrouselVisitaFoto.setImgList(geraListaImagemCarrousel(visita.getVisitaFotoList()));
            visibleState();
			repaint();
			if (ValueUtil.isEmpty(visita.getVisitaFotoList())) {
				btFecharClick();
			}
		}
	}

	private void btNovaFotoClick() {
		try {
			VisitaFoto visitaFoto = new VisitaFoto();
			visitaFoto.cdEmpresa = SessionLavenderePda.cdEmpresa;
			visitaFoto.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
			visitaFoto.flOrigemVisita = OrigemPedido.FLORIGEMPEDIDO_PDA;
			if (ValueUtil.isNotEmpty(visita.cdVisita)) {
				visitaFoto.cdVisita = visita.cdVisita;
			} else {
				visita.cdVisita = VisitaService.getInstance().generateIdGlobal();
				visitaFoto.cdVisita = visita.cdVisita;
			}
			BaseCamera camera = new BaseCamera();
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
					FotoUtil.copyToSD(pathFoto, VisitaFoto.getPathImg(), camera.defaultFileName);
				} else {
					FotoUtil.saveScaledPhoto(pathFoto, VisitaFoto.getPathImg(), camera.defaultFileName, camera.cameraType, LavenderePdaConfig.getNuResolucaoFotoCameraNativa(), ".png");
				}
				visitaFoto.cdFoto = visita.getVisitaFotoList().size() + 1;
				visitaFoto.imFoto = camera.defaultFileName;
				visita.getVisitaFotoList().addElement(visitaFoto);
				imageCarrouselVisitaFoto.setImgList(geraListaImagemCarrousel(visita.getVisitaFotoList()), new String[]{camera.defaultFileName, StringUtil.getStringValue(visitaFoto.cdFoto), ".png"});
			} else if (ValueUtil.isNotEmpty(visita.getVisitaFotoList())) { 
				imageCarrouselVisitaFoto.setImgList(geraListaImagemCarrousel(visita.getVisitaFotoList()), new String[]{"", "", ""});
			}
			visibleState();
			repaint();
		} catch (NoFileSelectedException e) {
			ExceptionUtil.handle(e);
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(ex);
		}
	}

	private void setCameraType(BaseCamera camera) throws Exception {
		boolean usaCameraNativa = LavenderePdaConfig.isUsaCameraNativa();
		boolean permiteSelecionarFotoArmazenadaCadastroFoto = LavenderePdaConfig.permiteSelecionarFotoArmazenadaCadastroFoto;
		
		if (permiteSelecionarFotoArmazenadaCadastroFoto) {
			if (VmUtil.isAndroid() || VmUtil.isIOS()) {
				int result = UiUtil.showMessage(Messages.LABEL_OPCOES_IMAGEM, Messages.MSG_COMPLETAR_ACAO_USANDO, new String[] {Messages.BOTAO_CANCELAR, Messages.BOTAO_CAMERA, Messages.BOTAO_GALERIA});
				switch (result) {
					case 0:
						throw new Exception();
					case 2:
						camera.setCameraType(BaseCamera.CAMERA_FROM_GALLERY);
						return;
				}
			} 
		}
		
		if (usaCameraNativa) {
			camera.setCameraType(BaseCamera.CAMERA_NATIVE);
		}
	}
	
	private Vector geraListaImagemCarrousel(Vector visitaFotoList) {
		Vector imgList = new Vector();
		String cdFoto = "";
		String nmFoto = "";
		String nmExtensao = "";
		for (int i = 0; i < visitaFotoList.size(); i++) {
			VisitaFoto visitaFoto = (VisitaFoto) visitaFotoList.items[i];
			cdFoto = StringUtil.getStringValue(visitaFoto.cdFoto);
			nmFoto = visitaFoto.imFoto.substring(0, visitaFoto.imFoto.lastIndexOf(".")); // para não pegar o .jpg
			nmExtensao = visitaFoto.imFoto.substring(visitaFoto.imFoto.lastIndexOf("."), visitaFoto.imFoto.length());
			imgList.addElement(new String[]{nmFoto, cdFoto, nmExtensao});		
		}
		return imgList;
	}
	
}
