package br.com.wmw.lavenderepda.report.pdf;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.Tela;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.report.pdf.tagmap.Layout;
import totalcross.io.IOException;
import totalcross.sys.Settings;
import totalcross.xml.SyntaxException;

public class PdfReportManager {
	
	private String msgErro;
	private String filePath;
	private boolean arquivoGerado;
	
	public void interpretaLayoutPedido(Pedido pedido, byte[] conteudo) throws SyntaxException, SQLException, IOException {
		List<Layout> layoutList = getLayoutList(Pedido.class.getSimpleName(), conteudo);
		SemanticInterpreter rel = new SemanticInterpreter();
		setParametrosPedidoInterpretador(pedido, rel);
		VmUtil.executeGarbageCollector();
		rel.interpretaElementosEGeraArquivo(pedido.getDsFilePathPdfPedido(), pedido.getNomeArquivoPdf(), layoutList);
		arquivoGerado = rel.isArquivoGerado();
		msgErro = rel.getMsgErro();
		filePath = rel.getFilePath();
	}
	
	private void setParametrosPedidoInterpretador(Pedido pedido, SemanticInterpreter rel) throws SQLException {
		rel.addParamsInternos("cdEmpresa", StringUtil.getStringValue(pedido.cdEmpresa));
		rel.addParamsInternos("cdRepresentante", StringUtil.getStringValue(pedido.cdRepresentante));
		rel.addParamsInternos("nuPedido", StringUtil.getStringValue(pedido.nuPedido));
		rel.addParamsInternos("flOrigemPedido", StringUtil.getStringValue(pedido.flOrigemPedido));
	}
	
	public void interpretaLayoutDinamico(Tela tela, Map<String, String> params, byte[] conteudo) throws SyntaxException, SQLException, IOException {
		List<Layout> layoutList = getLayoutList("Tela." + tela.cdTela, conteudo);
		SemanticInterpreter rel = new SemanticInterpreter();
		setParametrosDinamicosInterpretador(params, rel);
		VmUtil.executeGarbageCollector();
		rel.interpretaElementosEGeraArquivo(getDsFilePathPdfDinamico(), getNomeArquivoPdfDinamico(tela.cdTela), layoutList);
		arquivoGerado = rel.isArquivoGerado();
		msgErro = rel.getMsgErro();
		filePath = rel.getFilePath();
	}
	
	private List<Layout> getLayoutList(String entidade, byte[] conteudo) throws SyntaxException {
		PdfReportMapper relPdfMapper = new PdfReportMapper();
		relPdfMapper.setEntidadeRelacionada(entidade);
		LayoutReader leitorXml = new LayoutReader();
		leitorXml.setContentHandler(relPdfMapper);
		leitorXml.parse(conteudo, 0, conteudo.length);
		return relPdfMapper.getLayoutList();
	}
	
	private void setParametrosDinamicosInterpretador(Map<String, String> params, SemanticInterpreter rel) throws SQLException {
		for (String key : params.keySet()) {
			rel.addParamsInternos(key, params.get(key));
		}
	}
	
	public boolean isArquivoGerado() {
		return arquivoGerado;
	}

	public String getFilePath() {
		return filePath;
	}
	
	public String getMsgErro() {
		return StringUtil.getStringValue(msgErro);
	}
	
	private String getDsFilePathPdfDinamico() {
		String filePath = BaseDomain.getDirBaseArquivosInCard() + "PdfDinamico/";
		if (VmUtil.isAndroid()) {
			return "/sdcard/" + filePath;
		} else {
			return Settings.appPath + "/" + filePath;
		}
	}
	
	private String getNomeArquivoPdfDinamico(int cdTela) throws SQLException {
		return "Rel_" + cdTela + "_" + StringUtil.getStringValue(TimeUtil.getTimeAsLong());
	}
	
}
