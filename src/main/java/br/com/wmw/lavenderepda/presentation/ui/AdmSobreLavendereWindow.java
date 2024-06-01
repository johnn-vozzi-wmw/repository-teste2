package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.Personalizacao;
import br.com.wmw.framework.business.service.PersonalizacaoService;
import br.com.wmw.framework.presentation.ui.AdmSobreEquipamentoWindow;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.LavendereConfig;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.io.File;
import totalcross.sys.Settings;
import totalcross.ui.Button;
import totalcross.ui.ImageControl;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.ui.font.Font;
import totalcross.ui.image.Image;
import totalcross.util.Date;

public class AdmSobreLavendereWindow extends WmwWindow {

	private LabelValue lbNomeAplicacao;
	private LabelValue lbCopyright;
	private LabelValue lbDirReservados;
	private LabelValue lbTcVersion;
	private LabelValue lbRes;
	private LabelValue lbIosExpireDate;
	private LabelValue lbPlataformaValue;
	private LabelValue lbDeviceIdValue;
	private LabelValue lbDtServidorValue;
	private LabelValue lbSobreWmw;
	private LabelValue lvNuVersaoWtools;
	private LabelName lbPlataforma;
	private LabelName lbDeviceId;
	private LabelName lbDtServidor;
	private Button sep;
	private Button sep2;
	private Image imgLogoEmpresaSoftware;
	private ImageControl icLogoEmpresaSoftware;
	private Image imgLogoPoweredBy;
	private ImageControl icLogoPoweredBy;

	public AdmSobreLavendereWindow() throws SQLException {
		super(Messages.SOBRE_TITULO);
		transparentBackground = true;
		lbSobreWmw = new LabelValue(" ");
		lbSobreWmw.setFont(UiUtil.defaultFontSmall);
		//--
		lbDirReservados = new LabelValue(Messages.SOBRE_DIREITOS);
		lbDirReservados.setFont(UiUtil.fontVerySmall);
		lbCopyright = new LabelValue(Messages.SOBRE_COPYRIGHT + " - " + DateUtil.getYear());
		lbCopyright.setFont(UiUtil.fontVerySmall);
		lbTcVersion = new LabelValue("TotalCross: " + Settings.versionStr);
		lbTcVersion.setFont(UiUtil.defaultFontSmall);
		lbRes = new LabelValue(Messages.INFOPDA_RESOLUCAO + " " + Settings.screenHeight + "x" + Settings.screenWidth + " : " + Settings.screenDensity);
		lbRes.setFont(UiUtil.defaultFontSmall);
		lbIosExpireDate = new LabelValue(" ");
		lbIosExpireDate.setFont(UiUtil.defaultFontSmall);
		lbNomeAplicacao = new LabelValue(" ");
		lbNomeAplicacao.setFont(Font.getFont(lbNomeAplicacao.getFont().name, true, lbNomeAplicacao.getFont().size));
		lbPlataforma = new LabelName(Messages.INFOPDA_PLATAFORMA);
		lbPlataforma.setFont(UiUtil.defaultFontSmall);
		lbPlataformaValue = new LabelValue(String.valueOf(Settings.platform) + (Settings.romVersion == 33554432 ? "" : "  -  " + Settings.romVersion));
		lbPlataformaValue.setFont(UiUtil.defaultFontSmall);
		lbDeviceId = new LabelName("DeviceId");
		lbDeviceId.setFont(UiUtil.defaultFontSmall);
		lbDeviceIdValue = new LabelValue(String.valueOf(Settings.deviceId));
		lbDeviceIdValue.setForeColor(LavendereColorUtil.baseForeColorSystem);
		lbDeviceIdValue.setFont(UiUtil.defaultFontSmall);
		lbDtServidor = new LabelName(Messages.INFOPDA_DATA_SERVIDOR);
		lbDtServidor.setFont(UiUtil.defaultFontSmall);
		Date data = ConfigInternoService.getInstance().getDtServidor();
		String dataServidor = data != null ? data.toString() : "";
		lbDtServidorValue = new LabelValue(dataServidor);
		lbDtServidorValue.setFont(UiUtil.defaultFontSmall);
		if (LavenderePdaConfig.isUsaColetaGpsAppExterno()) {
			lvNuVersaoWtools = new LabelValue(Messages.INFOPDA_NUVERSAO_WTOOLS + StringUtil.getStringValue(LavendereConfig.getInstance().nuVersionWtools)); 
			lvNuVersaoWtools.setFont(UiUtil.defaultFontSmall);
		}
		//--
		File f = null;
		icLogoEmpresaSoftware = new ImageControl();
		icLogoEmpresaSoftware.transparentBackground = true;
		try {
			imgLogoEmpresaSoftware = PersonalizacaoService.getInstance().getImagemPersonalizada(Personalizacao.LOGO_DA_EMPRESA_DETENTORA_DOS_DIREITOS_DO_SOFTWARE);
			if (imgLogoEmpresaSoftware == null) {
				imgLogoEmpresaSoftware = UiUtil.getImage("images/logo_wmw.png");
			}
		} catch (Throwable ex) {
			imgLogoEmpresaSoftware = UiUtil.getImage("images/logo_wmw.png");
		} finally {
			FileUtil.closeFile(f);
		}
		//--
		icLogoPoweredBy = new ImageControl();
		icLogoPoweredBy.transparentBackground = true;
		imgLogoPoweredBy = PersonalizacaoService.getInstance().getImagemPersonalizada(Personalizacao.POWERED_BY_WMW);
		if (imgLogoPoweredBy == null) {
			imgLogoPoweredBy = UiUtil.getImageDefault(FileUtil.PATH_LOGO_POWEREDBY);
		}
		//--
		sep = new Button("");
		sep.setEnabled(false);
		sep2 = new Button("");
		sep2.setEnabled(false);
		setDefaultRect();
	}

	@Override
	public void initUI() {
		try {
			super.initUI();
			int widthNumVersao = lbNomeAplicacao.fm.stringWidth(" " + LavendereConfig.getInstance().version);
			lbNomeAplicacao.setText(StringUtil.getStringAbreviada(Messages.NOME_SISTEMA_VERSAO, width - widthNumVersao - WIDTH_GAP_BIG, lbNomeAplicacao.getFont()) + " " + LavendereConfig.getInstance().version);
			UiUtil.add(this, lbNomeAplicacao, CENTER, getTop() + (HEIGHT_GAP * 5), PREFERRED, PREFERRED);
			UiUtil.add(this, lbTcVersion, CENTER, AFTER);
			UiUtil.add(this, lbRes, CENTER, AFTER);
			if (VmUtil.isIOS()) {
				String iosExpireDate = getIosExpireDate();
				if (ValueUtil.isNotEmpty(iosExpireDate)) {
					lbIosExpireDate.setValue(Messages.INFOPDA_CERTIFICADO_IOS + iosExpireDate);
					UiUtil.add(this, lbIosExpireDate, CENTER, AFTER);
				}
			}
			if (LavenderePdaConfig.isUsaColetaGpsAppExterno()) {
				UiUtil.add(this, lvNuVersaoWtools, CENTER, AFTER);
			}
			int fmwidth = lbNomeAplicacao.getWidth();
			add(sep, CENTER, AFTER + (HEIGHT_GAP * 2), fmwidth, 1);
			addDadosPda();
			if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
				add(sep2, CENTER, lbDtServidorValue.getY2() + (HEIGHT_GAP * 2), fmwidth, 1);
			} else {
				add(sep2, CENTER, lbDeviceIdValue.getY2() + (HEIGHT_GAP * 2), fmwidth, 1);
			}
			int maxWidth = getWidth() / 4;
			if (imgLogoEmpresaSoftware != null && imgLogoEmpresaSoftware.getWidth() > 0) {
				icLogoEmpresaSoftware.setImage(UiUtil.getSmoothScaledImageForMaxSize(imgLogoEmpresaSoftware, maxWidth, maxWidth));
				UiUtil.add(this, icLogoEmpresaSoftware, CENTER, AFTER + (HEIGHT_GAP_BIG * 4));
			}
			lbSobreWmw.setText(StringUtil.getStringAbreviada(Messages.SOBRE_WMW, width, lbSobreWmw.getFont()));
			UiUtil.add(this, lbSobreWmw, CENTER, AFTER + HEIGHT_GAP_BIG, PREFERRED, PREFERRED);
			UiUtil.add(this, lbCopyright, CENTER, AFTER, PREFERRED, PREFERRED);
			UiUtil.add(this, lbDirReservados, CENTER, AFTER - 2, PREFERRED, PREFERRED);
			boolean usaLogoPoweredBy = (imgLogoPoweredBy != null && imgLogoPoweredBy.getWidth() > 0) && !LavenderePdaConfig.getValorParametroPorSistema(ValorParametro.USANOMESISTEMAPERSONALIZADO).equals(ValueUtil.VALOR_NAO);
			if (usaLogoPoweredBy) {
				icLogoPoweredBy.setImage(UiUtil.getSmoothScaledImageForMaxSize(imgLogoPoweredBy, maxWidth, maxWidth));
				UiUtil.add(this, icLogoPoweredBy, CENTER, AFTER + HEIGHT_GAP_BIG);
			}
		} catch (Throwable ee) {
			ExceptionUtil.handle(ee);
		}
	}

	private String getIosExpireDate() {
		try {
			return new Date(Settings.iosCertDate).toString();
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
		return ValueUtil.VALOR_NI;
	}

	@Override
	public void reposition() {
		super.reposition();
		initUI();
	}

	private void addDadosPda() {
		UiUtil.add(this, lbPlataforma, BaseUIForm.CENTEREDLABEL, sep.getY2() + HEIGHT_GAP, PREFERRED, PREFERRED);
		UiUtil.add(this, lbPlataformaValue, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED, PREFERRED);
		UiUtil.add(this, lbDeviceId, BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
		UiUtil.add(this, lbDeviceIdValue, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED, PREFERRED);
		if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
			UiUtil.add(this, lbDtServidor, BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
			UiUtil.add(this, lbDtServidorValue, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED, PREFERRED);
		}
	}

	@Override
	public void onWindowEvent(Event event) throws SQLException {
		super.onWindowEvent(event);
		if (event.type == PenEvent.PEN_UP && event.target == lbDeviceIdValue) {
			new AdmSobreEquipamentoWindow().popup();
		}
	}

}
