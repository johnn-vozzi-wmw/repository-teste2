package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.CorSistema;
import br.com.wmw.framework.business.domain.Tela;
import br.com.wmw.framework.business.service.AcaoTelaService;
import br.com.wmw.framework.business.service.TemaSistemaService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.ListRelDinamicoForm;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.WmwMessageBox.TYPE_MESSAGE;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.ResourcesWmw;
import br.com.wmw.lavenderepda.business.service.AcaoTelaLavendereService;
import br.com.wmw.lavenderepda.business.service.AcessoMaterialService;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.CorSistemaLavendereService;
import br.com.wmw.lavenderepda.business.service.ResourcesWmwService;
import br.com.wmw.lavenderepda.report.pdf.PdfReportManager;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.sys.Convert;
import totalcross.sys.Vm;
import totalcross.ui.gfx.Color;
import totalcross.xml.SyntaxException;

public class ListRelDinamicoLavendereForm extends ListRelDinamicoForm {

	private static String NMPARAMETRO_EMPRESA_SESSAO = "cdEmpresaSessao";
	private static String NMPARAMETRO_REPRESENTANTE_SESSAO = "cdRepresentanteSessao";
	private static String NMPARAMETRO_USUARIO_SESSAO = "cdUsuarioSessao";
	private static final String SYSTEM_VIEWER = "viewer";

	public ListRelDinamicoLavendereForm(Tela tela) throws SQLException {
		super(tela);
	}

	protected String getDsSqlFixed(String dsExpressao) {
		String value = super.getDsSqlFixed(dsExpressao);
		int indexOf = value.indexOf(NMPARAMETRO_EMPRESA_SESSAO);
		while (indexOf != -1) {
			value = value.substring(0, indexOf) + SessionLavenderePda.cdEmpresa + value.substring(indexOf+NMPARAMETRO_EMPRESA_SESSAO.length(), value.length());
			indexOf = value.indexOf(NMPARAMETRO_EMPRESA_SESSAO);
		}
		indexOf = value.indexOf(NMPARAMETRO_REPRESENTANTE_SESSAO);
		while (indexOf != -1) {
			value = value.substring(0, indexOf) + SessionLavenderePda.usuarioPdaRep.cdRepresentante + value.substring(indexOf+NMPARAMETRO_REPRESENTANTE_SESSAO.length(), value.length());
			indexOf = value.indexOf(NMPARAMETRO_REPRESENTANTE_SESSAO);
		}
		indexOf = value.indexOf(NMPARAMETRO_USUARIO_SESSAO);
		while (indexOf != -1) {
			value = value.substring(0, indexOf) + SessionLavenderePda.usuarioPdaRep.cdUsuario + value.substring(indexOf+NMPARAMETRO_USUARIO_SESSAO.length(), value.length());
			indexOf = value.indexOf(NMPARAMETRO_USUARIO_SESSAO);
		}
		return value;
	}
	
	@Override
	protected AcaoTelaService getAcaoTelaService() {
		return AcaoTelaLavendereService.getInstance();
	}
	
	@Override
	protected void showRelDinamico(Tela tela) throws SQLException {
		show(new ListRelDinamicoLavendereForm(tela));
	}
	
	@Override
	protected void setLineForeColor(int i, String cdCor) throws SQLException {
		if (posicaoColunaCorFundoLinha == -1 || cdCor == null) return;
		Integer color = colorHash.get(cdCor);
		if (color == null) {
			String cdEsquemaCorAtual = ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.temaPadraoSistema);
			if (ValueUtil.isEmpty(cdEsquemaCorAtual)) {
				cdEsquemaCorAtual = String.valueOf(TemaSistemaService.getInstance().getTemaAtual().cdEsquemaCor);
			}
			CorSistema corSistema = new CorSistema();
			corSistema.cdEsquemaCor = ValueUtil.getIntegerValue(cdEsquemaCorAtual);
			corSistema.cdCor = ValueUtil.getIntegerValue(cdCor);
			corSistema = (CorSistema) CorSistemaLavendereService.getInstance().findByRowKey(corSistema.getRowKey());
			color = corSistema != null ? Color.getRGB(corSistema.vlR, corSistema.vlG, corSistema.vlB) : LavendereColorUtil.componentsForeColor;
			colorHash.put(cdCor, color);
		}
		gridEdit.gridController.setRowForeColor(color, i);
	}

	@Override
	protected void btExportarPdfClick() throws SQLException {
		super.btExportarPdfClick();
		geraPdfOffline();
	}
	
	private void geraPdfOffline() throws SQLException {
		ResourcesWmw resourcesWmw = ResourcesWmwService.getInstance().getResourcesWmwRelatorioPdf();
		if (resourcesWmw == null) {
			UiUtil.showWarnMessage(Messages.RELATORIO_PDF_OFFLINE_ERRO_LAYOUT_NAO_ENCONTRADO);
			return;
		}
		LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		mb.popupNonBlocking();
		try {
			PdfReportManager geradorPdf = new PdfReportManager();
			geradorPdf.interpretaLayoutDinamico(tela, getParamsToPdf(), resourcesWmw.baConteudo);
			aposGerarPdf(geradorPdf.isArquivoGerado(), geradorPdf.getFilePath(), geradorPdf.getMsgErro(), TYPE_MESSAGE.WARN);
		} catch (ValidationException e) {
			UiUtil.showMessage(Messages.RELATORIO_PDF_OFFLINE_ERRO_NAO_GERADO + e.getMessage(), TYPE_MESSAGE.WARN);
		} catch (SyntaxException e) {
			UiUtil.showMessage(Messages.RELATORIO_PDF_OFFLINE_ERRO_NAO_GERADO + Messages.RELATORIO_PDF_OFFLINE_ERRO_DEFINICAO_INCORRETA, TYPE_MESSAGE.WARN);
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
			UiUtil.showMessage(Messages.RELATORIO_PDF_OFFLINE_ERRO_NAO_GERADO + Messages.RELATORIO_PDF_OFFLINE_ERRO_SINTAXE_INVALIDA_SQL, TYPE_MESSAGE.WARN);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			UiUtil.showMessage(Messages.RELATORIO_PDF_OFFLINE_ERRO_NAO_GERADO, TYPE_MESSAGE.WARN);
		} finally {
			mb.unpop();
		}
	}

	private void aposGerarPdf(boolean arquivoGerado, String filePath, String msgErro, TYPE_MESSAGE typeMsg) throws SQLException {
		if (!arquivoGerado) {
			UiUtil.showMessage(Messages.RELATORIO_PDF_OFFLINE_ERRO_NAO_GERADO + StringUtil.getStringValue(msgErro), typeMsg);
			return;
		}
		if (UiUtil.showConfirmYesNoMessage(Messages.RELATORIO_PDF_OFFLINE_GERADO_SUCESSO)) {
			String path = Convert.appendPath((VmUtil.isSimulador() ? "file:///" : ""), filePath);
			int abriuVisualizador = Vm.exec(SYSTEM_VIEWER, path, 0, true);
			if (abriuVisualizador == -1 && !VmUtil.isJava()) {
				UiUtil.showWarnMessage(Messages.RELATORIO_PDF_OFFLINE_PREVISUALIZACAO_INDISPONIVEL + MessageUtil.getMessage(Messages.RELATORIO_PDF_OFFLINE_LOCAL_PDF, filePath));
			}
		} else {
			UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.RELATORIO_PDF_OFFLINE_LOCAL_PDF, filePath));
		}
	}
	
	@Override
	protected void insereAcessoMaterial(String string) {
		super.insereAcessoMaterial(string);
		try {
			AcessoMaterialService.getInstance().insereAcesso(string);
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
	}
}
