package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.Personalizacao;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.business.service.PersonalizacaoService;
import br.com.wmw.framework.presentation.ui.WmwCadWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseEdit;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.StringUtil;
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

public class CadAcessoContatoWindow extends WmwCadWindow { 

	private Image imgLogoSistema;
	private Image imgLogoEmpresa;
	private Image imgLock;
	private ImageControl icLogoSistema;
	private ImageControl icLogoEmpresa;
	private ImageControl icLock;
	private SessionContainer topContainer;
	private SessionContainer bottomContainer;
	private EditText edNome;
	private EditText edEmail;
	private EditText edEmpresa;
	private QtVendedoresComboBox cbVendedores;
	private BaseButton btEntrar;
	
	public CadAcessoContatoWindow() throws SQLException {
		super("");
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
		bottomContainer.setBackColor(ColorUtil.baseForeColorSystem);
		
		icLogoSistema = new ImageControl();
		icLogoSistema.transparentBackground = true;
		icLogoEmpresa = new ImageControl();
		icLogoEmpresa.transparentBackground = true;
		icLock = new ImageControl();
		icLock.transparentBackground = true;
		
		edNome = new EditText("", 50);
		edNome.setText(Messages.ACESSOCONTATO_LABEL_DSNOME);
		edEmail = new EditText("", 50);
		edEmail.setText(Messages.ACESSOCONTATO_LABEL_DSEMAIL);
		edEmpresa = new EditText("", 50);
		edEmpresa.setText(Messages.ACESSOCONTATO_LABEL_CDEMPRESA);
		cbVendedores = new QtVendedoresComboBox();
		
		btEntrar = new BaseButton("Entrar");
		btEntrar.setBackForeColors(ColorUtil.baseForeColorSystem, ColorUtil.baseBackColorSystem);
		setDefaultRect();
	}
	
	public CadAcessoContatoWindow acesso(AcessoContato acessoContato) throws SQLException {
		setDomain(acessoContato);
		domainToScreen(acessoContato);
		disableComponents();
		return this;
	}

	private void disableComponents() {
		edNome.setEditable(false);
		edEmail.setEditable(false);
		edEmpresa.setEditable(false);
		cbVendedores.setEditable(false);
	}

	@Override
	protected BaseDomain screenToDomain() throws SQLException {
		AcessoContato acessoContato = (AcessoContato) getDomain();
		acessoContato.dsEmpresa = edEmpresa.getText();
		acessoContato.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		acessoContato.dsNome = edNome.getText();
		acessoContato.dsEmail = edEmail.getText();
		acessoContato.nuFuncionarios = cbVendedores.getValue();
		return acessoContato;
	}

	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		if (domain != null) {
			AcessoContato acessoContato = (AcessoContato) getDomain();
			this.edNome.setText(acessoContato.dsNome);
			this.edEmail.setText(acessoContato.dsEmail);
			this.edEmpresa.setText(acessoContato.dsEmpresa);
			this.cbVendedores.setSelectedItem(StringUtil.getStringValue(acessoContato.nuFuncionarios));
			
		}
		
	}

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
	public void initUI() {
		addBottomContainer();
		super.initUI();
		remove(cFundoFooter);
		int heigthContainerImages = Settings.screenHeight / 7;
		UiUtil.add(this, topContainer, LEFT, TOP, FILL, heigthContainerImages);
		int widthDisponivelImage = (Settings.screenWidth / 2) - WIDTH_GAP_BIG * 2;
		if ((imgLogoEmpresa != null && imgLogoEmpresa.getWidth() > 0) && (imgLogoSistema != null && imgLogoSistema.getWidth() > 0)) {
			icLogoEmpresa.setImage(UiUtil.getSmoothScaledImageForMaxSize(imgLogoEmpresa, widthDisponivelImage, heigthContainerImages / 2));
			UiUtil.add(topContainer, icLogoEmpresa, LEFT + WIDTH_GAP_BIG * 2, CENTER);
			widthDisponivelImage = Settings.screenWidth - icLogoEmpresa.getX2() - WIDTH_GAP_BIG * 2;
			icLogoSistema.setImage(UiUtil.getSmoothScaledImageForMaxSize(imgLogoSistema, widthDisponivelImage, icLogoEmpresa.getHeight())); 
			UiUtil.add(topContainer, icLogoSistema, AFTER + WIDTH_GAP_BIG, CENTER);
			//Centraliza
			centerImg();
		}
		
		cbVendedores.loadCombo();
		cbVendedores.setSelectedIndex(0);
		int recuoLateral = ValueUtil.getIntegerValue(Settings.screenWidth * 0.1);
		
		UiUtil.add(this, Messages.LABEL_ACESSOCONTATO_APRES_LINE1, CENTER, TOP + topContainer.getHeight() + WIDTH_GAP_BIG);
		UiUtil.add(this, Messages.LABEL_ACESSOCONTATO_APRES_LINE2, CENTER, AFTER);
		UiUtil.add(this, Messages.LABEL_ACESSOCONTATO_APRES_LINE3, CENTER, AFTER);
		UiUtil.add(this, edNome, LEFT + recuoLateral, AFTER + HEIGHT_GAP_BIG * 3, FILL - recuoLateral, PREFERRED + HEIGHT_GAP_BIG);
		UiUtil.add(this, edEmail, LEFT + recuoLateral, AFTER + HEIGHT_GAP,  FILL - recuoLateral, PREFERRED + HEIGHT_GAP_BIG);
		UiUtil.add(this, edEmpresa, LEFT + recuoLateral, AFTER + HEIGHT_GAP,  FILL - recuoLateral, PREFERRED + HEIGHT_GAP_BIG);
		UiUtil.add(this, cbVendedores, LEFT + recuoLateral, AFTER + HEIGHT_GAP,  FILL - recuoLateral, PREFERRED + HEIGHT_GAP_BIG);
		UiUtil.add(this, btEntrar, CENTER, AFTER + HEIGHT_GAP_BIG);
		LabelName lbPrivacNeg = new LabelName(Messages.LABEL_ACESSOCONTATO_PRIVAC_LINE1);
		LabelValue lbPrivacNorm = new LabelValue();
		lbPrivacNorm.setForeColor(ColorUtil.labelNameForeColor);
		lbPrivacNeg.setFont(Font.getFont(true, UiUtil.defaultFontSmall.size));
		lbPrivacNorm.setFont(UiUtil.defaultFontSmall);
		UiUtil.add(this, lbPrivacNorm, LEFT + recuoLateral * 2, AFTER + HEIGHT_GAP_BIG * HEIGHT_GAP_BIG);
		lbPrivacNorm.setMultipleLinesText(Messages.LABEL_ACESSOCONTATO_PRIVAC_LINE2);
		UiUtil.add(this, lbPrivacNeg, SAME, BEFORE + HEIGHT_GAP);
		if (imgLock != null) {
			icLock.setImage(UiUtil.getSmoothScaledImage(imgLock, ValueUtil.getIntegerValue(lbPrivacNeg.getHeight() * 2.5), ValueUtil.getIntegerValue(lbPrivacNeg.getHeight() * 2.5)));
		}
		UiUtil.add(this, icLock, BEFORE - WIDTH_GAP, SAME);
		
		
	}
	
	@Override
	public void onEvent(Event event) {
		switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == btEntrar) {
					try {
						BaseDomain domain = screenToDomain();
						AcessoContato acessoContato = (AcessoContato) domain;
						if (AcessoContatoService.getInstance().findByRowKey(acessoContato.getRowKey()) == null)
							getCrudService().insert(domain);
						disableComponents();
						afterSave();
					} catch (Throwable e) {
						UiUtil.showErrorMessage(e);
					}
				}
				break;
			case ControlEvent.FOCUS_IN:
				if (event.target == edNome && edNome.getText().equals(Messages.ACESSOCONTATO_LABEL_DSNOME)) {
					edNome.setText("");
				} else if (event.target == edEmail && edEmail.getText().equals(Messages.ACESSOCONTATO_LABEL_DSEMAIL)) {
					edEmail.setText("");
				} else if (event.target == edEmpresa && edEmpresa.getText().equals(Messages.ACESSOCONTATO_LABEL_CDEMPRESA)) {
					edEmpresa.setText("");
				}
				break;
			case ControlEvent.FOCUS_OUT:
				if (event.target instanceof BaseEdit) {
					if (ValueUtil.isEmpty(((BaseEdit) event.target).getText())) {
						if (event.target == edNome) {
							edNome.setText(Messages.ACESSOCONTATO_LABEL_DSNOME);
						} else if (event.target == edEmail) {
							edEmail.setText(Messages.ACESSOCONTATO_LABEL_DSEMAIL);
						} else if (event.target == edEmpresa) {
							edEmpresa.setText(Messages.ACESSOCONTATO_LABEL_CDEMPRESA);
						}
					} else {
						((BaseEdit) event.target).setText(((BaseEdit) event.target).getText());
					}
				}
				break;
		}
	}
	
	
	
	@Override
	protected void afterSave() throws SQLException {
		try {
			AcessoContatoService.getInstance().sendDadosServidor();
			if (AcessoContatoService.getInstance().isEnviado()) {
				unpop();
			} else {
				UiUtil.showErrorMessage(Messages.ERRO_ENVIO_DADOS_SERVIDOR);
			}
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e);
		}
	}
	
	@Override
	public void reposition() {
		super.reposition();
		centerImg();
		scBase.scrollToControl(topContainer);
	}
	
	private void centerImg() {
		int widthTotalImgs = icLogoEmpresa.getWidth() + WIDTH_GAP_BIG + icLogoSistema.getWidth();
		icLogoEmpresa.setRect(Settings.screenWidth / 2 - widthTotalImgs / 2, CENTER, PREFERRED, PREFERRED);
		icLogoSistema.setRect(AFTER + WIDTH_GAP_BIG, CENTER, PREFERRED, PREFERRED);
	}
	
	private void addBottomContainer() {
		scrollable = false;
		int heigthContainerBase = UiUtil.getLabelPreferredHeight();
		UiUtil.add(this, bottomContainer, LEFT, BOTTOM - heigthContainerBase, FILL, heigthContainerBase / 3);
		footerH = heigthContainerBase + bottomContainer.getHeight();
		scrollable = true;
	}
	
	@Override
	protected void addButtons() {
		//Nenhum botão padrão adicionado
	}
	
}
