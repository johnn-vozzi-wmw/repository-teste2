package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.Personalizacao;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.business.service.PersonalizacaoService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.BaseEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelTitle;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.AcessoContato;
import br.com.wmw.lavenderepda.business.service.AcessoContatoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.QtVendedoresComboBox;
import totalcross.io.File;
import totalcross.sys.Settings;
import totalcross.ui.ImageControl;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.font.Font;
import totalcross.ui.image.Image;

public class CadAcessoSistemaForm extends BaseCrudCadForm {
	
	private Image imgLogoSistema;
	private Image imgLogoEmpresa;
	private Image imgLock;
	private ImageControl icLogoSistema;
	private ImageControl icLogoEmpresa;
	private ImageControl icLock;
	private SessionContainer topContainer;
	private SessionContainer bottomContainer, bottomContainer2;
	private EditText dsNome;
	private EditText dsEmail;
	private EditText dsEmpresa;
	private QtVendedoresComboBox cbVendedores;
	private ButtonPopup btEntrar;
	
	
	public CadAcessoSistemaForm() throws SQLException {
		super("Titulo");
		scrollable = true;
		setBackForeColors(ColorUtil.formsBackColor, ColorUtil.sessionContainerForeColor);
		File f = null;
		imgLogoEmpresa = PersonalizacaoService.getInstance().getImagemPersonalizada(Personalizacao.LOGO_DA_EMPRESA);
		if (imgLogoEmpresa == null) {
			imgLogoEmpresa = UiUtil.getImageDefault(FileUtil.PATH_LOGO_EMPRESA);
		}
		//--
		imgLogoSistema = PersonalizacaoService.getInstance().getImagemPersonalizada(Personalizacao.LOGO_DO_SISTEMA);
		if (imgLogoSistema == null) {
			imgLogoSistema = UiUtil.getImageDefault(FileUtil.PATH_LOGO_SISTEMA);
		}
		//--
		try {
			f = FileUtil.openFile(Settings.appPath + "/lock.png", File.READ_ONLY);
			imgLock = new Image(f.read());
		} catch (Throwable e) {
			imgLock = UiUtil.getImage("images/lock.png");
		} finally {
			FileUtil.closeFile(f);
		}
		topContainer = new SessionContainer();
		topContainer.setBackColor(ColorUtil.baseForeColorSystem);
		
		bottomContainer = new SessionContainer();
		bottomContainer2 = new SessionContainer();
		bottomContainer2.setBackColor(ColorUtil.baseForeColorSystem);
		
		icLogoSistema = new ImageControl();
		icLogoSistema.transparentBackground = true;
		icLogoEmpresa = new ImageControl();
		icLogoEmpresa.transparentBackground = true;
		icLock = new ImageControl();
		icLock.transparentBackground = true;
		
		dsNome = new EditText("", 50);
		dsNome.setText("Nome*");
		dsEmail = new EditText("", 50);
		dsEmail.setText("E-mail*");
		dsEmpresa = new EditText("", 50);
		dsEmpresa.setText("Empresa");
		cbVendedores = new QtVendedoresComboBox();
		
		btEntrar = new ButtonPopup("Entrar");
		btEntrar.setBackForeColors(ColorUtil.baseForeColorSystem, ColorUtil.baseBackColorSystem);
		
		setRect(LEFT, 0, FILL, FILL);
		
	}
	


	@Override
	protected BaseDomain screenToDomain() throws SQLException {
		AcessoContato acessoContato = (AcessoContato) getDomain();
		acessoContato.dsEmpresa = dsEmpresa.getText();
		acessoContato.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		acessoContato.dsNome = dsNome.getText();
		acessoContato.dsEmail = dsEmail.getText();
		acessoContato.nuFuncionarios = cbVendedores.getValue();
		return acessoContato;
	}

	@Override
	protected void domainToScreen(BaseDomain domain) {	}

	@Override
	protected void clearScreen() {	}

	@Override
	protected BaseDomain createDomain() {
		return new AcessoContato();
	}

	@Override
	protected String getEntityDescription() {
		return null;
	}

	@Override
	protected CrudService getCrudService() {
		return AcessoContatoService.getInstance();
	}

	@Override
	protected void onFormStart() {
		int heigthContainerImages = Settings.screenHeight / 7;
		int heigthContainerBase = UiUtil.getLabelPreferredHeight();
		UiUtil.add(this, topContainer, LEFT, TOP, FILL, heigthContainerImages);
		int widthDisponivelImage = (Settings.screenWidth / 2) - WIDTH_GAP_BIG * 2;
		if ((imgLogoEmpresa != null && imgLogoEmpresa.getWidth() > 0) && (imgLogoSistema != null && imgLogoSistema.getWidth() > 0)) {
			icLogoEmpresa.setImage(UiUtil.getSmoothScaledImageForMaxSize(imgLogoEmpresa, widthDisponivelImage, heigthContainerImages / 2));
			UiUtil.add(topContainer, icLogoEmpresa, CENTER, CENTER);
			widthDisponivelImage = Settings.screenWidth - icLogoEmpresa.getX2() - WIDTH_GAP_BIG * 2;
			icLogoSistema.setImage(UiUtil.getSmoothScaledImageForMaxSize(imgLogoSistema, widthDisponivelImage, icLogoEmpresa.getHeight())); 
			UiUtil.add(topContainer, icLogoSistema, AFTER + WIDTH_GAP_BIG, CENTER);
			//Centraliza
			centerImgs();
		}
		
		cbVendedores.loadCombo();
		cbVendedores.setSelectedIndex(0);
		int recuoLateral = ValueUtil.getIntegerValue(Settings.screenWidth * 0.1);

		UiUtil.add(this, Messages.LABEL_ACESSOCONTATO_APRES_LINE1, CENTER, TOP + topContainer.getHeight() + WIDTH_GAP_BIG);
		UiUtil.add(this, Messages.LABEL_ACESSOCONTATO_APRES_LINE2, CENTER, AFTER);
		//UiUtil.add(this, Messages.LABEL_ACESSOCONTATO_APRES_LINE3, CENTER, AFTER);
		UiUtil.add(this, dsNome, LEFT + recuoLateral, AFTER + HEIGHT_GAP_BIG * 3, FILL - recuoLateral, PREFERRED + HEIGHT_GAP_BIG);
		UiUtil.add(this, dsEmail, LEFT + recuoLateral, AFTER + HEIGHT_GAP,  FILL - recuoLateral, PREFERRED + HEIGHT_GAP_BIG);
		UiUtil.add(this, dsEmpresa, LEFT + recuoLateral, AFTER + HEIGHT_GAP,  FILL - recuoLateral, PREFERRED + HEIGHT_GAP_BIG);
		UiUtil.add(this, cbVendedores, LEFT + recuoLateral, AFTER + HEIGHT_GAP,  FILL - recuoLateral, PREFERRED + HEIGHT_GAP_BIG);
		UiUtil.add(this, btEntrar, RIGHT - recuoLateral, AFTER + WIDTH_GAP_BIG + HEIGHT_GAP);
		UiUtil.add(this, bottomContainer, LEFT, BOTTOM, FILL, heigthContainerBase);
		UiUtil.add(this, bottomContainer2, LEFT, BEFORE, FILL, heigthContainerBase / 3);
		LabelName lbPrivacNeg = new LabelName(Messages.LABEL_ACESSOCONTATO_PRIVAC_LINE1);
		LabelName lbPrivacNorm = new LabelName(Messages.LABEL_ACESSOCONTATO_PRIVAC_LINE2);
		//LabelName lbPrivacNorm2 = new LabelName(Messages.LABEL_ACESSOCONTATO_PRIVAC_LINE3);
		lbPrivacNeg.setFont(Font.getFont(true, UiUtil.defaultFontSmall.size));
		lbPrivacNorm.setFont(UiUtil.defaultFontSmall);
		//lbPrivacNorm2.setFont(UiUtil.defaultFontSmall);
		//UiUtil.add(this, lbPrivacNorm2, LEFT + recuoLateral * 2, BEFORE - HEIGHT_GAP_BIG * 2);
		UiUtil.add(this, lbPrivacNorm, SAME, BEFORE + HEIGHT_GAP);
		UiUtil.add(this, lbPrivacNeg, SAME, BEFORE + HEIGHT_GAP);
		if (imgLock != null) {
			icLock.setImage(UiUtil.getSmoothScaledImage(imgLock, lbPrivacNeg.getHeight() + lbPrivacNorm.getHeight(), lbPrivacNeg.getHeight() + lbPrivacNorm.getHeight()));
		}
		UiUtil.add(this, icLock, BEFORE - WIDTH_GAP, SAME);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == btEntrar) {
					BaseDomain domain = screenToDomain();
					getCrudService().insert(domain);
					afterSave();
				}
				break;
			case ControlEvent.FOCUS_IN:
				if (event.target instanceof BaseEdit) {
					((BaseEdit) event.target).setText("");
				}
				break;
			case ControlEvent.FOCUS_OUT:
				if (event.target instanceof BaseEdit) {
					if (ValueUtil.isEmpty(((BaseEdit) event.target).getText())) {
						if (event.target == dsNome) {
							dsNome.setText("Nome*");
						} else if (event.target == dsEmail) {
							dsEmail.setText("E-mail*");
						} else if (event.target == dsEmpresa) {
							dsEmpresa.setText("Empresa");
						}
					} else {
						((BaseEdit) event.target).setText(((BaseEdit) event.target).getText());
					}
				}
				break;
		}
	}
	
	
	@Override
	public void initCabecalhoRodape() {
		lbTitle = new LabelTitle(getTitle());
		
	}
	
	
	@Override
	protected void afterSave() throws SQLException {
		try {
			AcessoContatoService.getInstance().sendDadosServidor();
			if (AcessoContatoService.getInstance().isEnviado()) {
				close();
			} else {
				throw new ValidationException(Messages.ERRO_ENVIO_DADOS_SERVIDOR);
			}
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e);
		}
	}
	
	@Override
	public void reposition() {
		super.reposition();
		centerImgs();
	}
	
	public void centerImgs() {
		int widthTotalImgs = icLogoEmpresa.getWidth() + WIDTH_GAP_BIG + icLogoSistema.getWidth();
		icLogoEmpresa.setRect(Settings.screenWidth / 2 - widthTotalImgs / 2, CENTER, PREFERRED, PREFERRED);
		icLogoSistema.setRect(AFTER + WIDTH_GAP_BIG, CENTER, PREFERRED, PREFERRED);
	}
	
}
