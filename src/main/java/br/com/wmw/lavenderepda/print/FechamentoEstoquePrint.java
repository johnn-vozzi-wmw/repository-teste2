package br.com.wmw.lavenderepda.print;

import java.sql.SQLException;
import java.util.List;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.domain.EstoqueRep;
import br.com.wmw.lavenderepda.business.domain.RemessaEstoque;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import br.com.wmw.lavenderepda.business.service.EstoqueRepService;
import br.com.wmw.lavenderepda.business.service.RemessaEstoqueService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.business.service.TransportadoraService;
import totalcross.sys.Settings;
import totalcross.ui.gfx.Graphics;
import totalcross.ui.image.ImageException;

import static br.com.wmw.lavenderepda.print.AbstractPedidoServicePrint.DEVICE_SCREEN_DENSITY;
import static br.com.wmw.lavenderepda.print.AbstractPedidoServicePrint.PRINT_SCREEN_DENSITY;

public class FechamentoEstoquePrint extends PrintFactory{
	
	private List<EstoqueRep> estoqueRepList;
	private List<RemessaEstoque> remessaEstoqueList;
	private static final String NOME_ARQUIVO = "fechamentoEstoque.png";
	
	public FechamentoEstoquePrint() throws Exception {
		setPrinterParameters();
	}
	
	public void run() {
		try {
			Settings.screenDensity = PRINT_SCREEN_DENSITY;
			setFonts();
			estoqueRepList = EstoqueRepService.getInstance().findDevolucaoEstoque(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getRepresentante().cdRepresentante);
			if (estoqueRepList.isEmpty()) {
				throw new ValidationException(Messages.FECHAMENTO_ESTOQUE_NENHUM_REGISTRO_ENCONTRADO);
			}
			remessaEstoqueList = RemessaEstoqueService.getInstance().findRemessaEstoqueDevolucao(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getRepresentante().cdRepresentante);
			
			createFile();
			print(LavenderePdaConfig.usaImpressaoDevolucaoTotalEstoqueRemessaRepresentante, NOME_ARQUIVO);
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e.getMessage());
		} finally {
			Settings.screenDensity = DEVICE_SCREEN_DENSITY;
		}
	}
	
	private void createFile() throws ImageException, SQLException {
		int imageHeigth = calculateImageHeight();
		Settings.screenDensity = PRINT_SCREEN_DENSITY;
		try {
			Graphics g = getGrafics(imageHeigth);
			fileLayout(g);
		} finally {
			Settings.screenDensity = DEVICE_SCREEN_DENSITY;
		}
	}

	private int calculateImageHeight() {
		return addLineSize() * (20 + (estoqueRepList.size()*2) + remessaEstoqueList.size());
	}

	private void fileLayout(Graphics g) throws SQLException {
		g.setFont(fontBold);
		
		addTitle(g);
		addEmitente(g);
		addDestinatario(g);
		addRemessas(g);
		addItens(g);
		addAssinatura(g);
	}

	private void addTitle(Graphics g) {
		String titulo = Messages.FECHAMENTO_ESTOQUE + " - " + getFirstEstoqueRep().dtEstoque;
		int alignCenter = getAlignCenter(titulo, IMAGEM_WIDTH);
		lineBreak();
		g.drawText(titulo, alignCenter, currentHeight);
		lineBreak();
	}
	
	private void addEmitente(Graphics g) throws SQLException {
		int x = SPACE_INIT_LINE;
		int cellWidth = IMAGEM_WIDTH / 3;
		x += cellWidth * 2;
		g.drawText(Messages.FECHAMENTO_ESTOQUE_EMITENTE, SPACE_INIT_LINE, currentHeight);
		g.setFont(font);
		
		lineBreak();
		Representante representante = getRepresentante();
		g.drawText(representante.getCdRepresentanteTxt() + " - " + representante.nmRepresentante, SPACE_INIT_LINE, currentHeight);
		String placa = TransportadoraService.getInstance().findPlacaPor(representante.cdRepresentante);
		g.drawText(Messages.FECHAMENTO_DIARIO_PLACA + StringUtil.getStringValue(placa), x, currentHeight);
		
		lineBreak();
		g.drawText(Messages.FECHAMENTO_DIARIO_DATA + getFirstEstoqueRep().dtEstoque, SPACE_INIT_LINE, currentHeight);
		g.drawText(Messages.FECHAMENTO_DIARIO_HORA + getFirstEstoqueRep().hrEstoque, x, currentHeight);
		g.drawRoundRect(0, addLineSize(), IMAGEM_WIDTH, addLineSize() * 3, 8);

		currentHeight += addLineSize() + SPACE_INIT_LINE  + SPACE_INIT_LINE;
	}
	
	private void addDestinatario(Graphics g) throws SQLException {
		int x = SPACE_INIT_LINE;
		
		g.drawRoundRect(0, currentHeight, IMAGEM_WIDTH, addLineSize() * 6, 8);
		
		addTextBold(g, Messages.FECHAMENTO_ESTOQUE_DESTINATARIO, x);
		
		lineBreak();
		Empresa empresa = getEmpresa();
		
		addDrawText(g, empresa.nmEmpresa, x);
		lineBreak();
		addDrawText(g, empresa.dsLogradouro + ", " + empresa.nuLogradouro + " - " + empresa.dsBairro, x);
		lineBreak();
		addDrawText(g, empresa.dsCep + " - " + empresa.dsCidade + " - " + empresa.dsEstado + " - " + empresa.nuFone, x);
		lineBreak();
		addDrawText(g, Messages.FECHAMENTO_ESTOQUE_CNPJ + empresa.nuCnpj, x);
		lineBreak();
		addDrawText(g, Messages.FECHAMENTO_ESTOQUE_IE + empresa.nuInscricaoEstadual, x);
		
		currentHeight += addLineSize() + SPACE_INIT_LINE  + SPACE_INIT_LINE;
	}
	
	private void addRemessas(Graphics g) {
		int col1 = SPACE_INIT_LINE;
		int cellWidth = IMAGEM_WIDTH / 3;
		int col2 = col1 + cellWidth;
		int col3 = IMAGEM_WIDTH;
		
		g.drawRoundRect(0, currentHeight, IMAGEM_WIDTH, addLineSize() * 2, 8);
		
		addTextBold(g, Messages.FECHAMENTO_ESTOQUE_REMESSA, col1);
		lineBreak();
		
		addTextBold(g, Messages.FECHAMENTO_ESTOQUE_REMESSA_NOTA, col1);
		addTextBold(g, Messages.FECHAMENTO_ESTOQUE_REMESSA_SERIE, col2);
		addTextBold(g, Messages.FECHAMENTO_ESTOQUE_REMESSA_LOCALESTOQUE, getAlignRight(Messages.FECHAMENTO_ESTOQUE_REMESSA_LOCALESTOQUE, col3, true));
		
		for (RemessaEstoque remessa : remessaEstoqueList) {
			lineBreak();
			addDrawText(g, StringUtil.getStringValue(remessa.nuNotaRemessa), col1);
			addDrawText(g, StringUtil.getStringValue(remessa.nuSerieRemessa), col2);
			addDrawText(g, StringUtil.getStringValue(remessa.cdLocalEstoque), getAlignRight(StringUtil.getStringValue(remessa.cdLocalEstoque), col3, false));
		}
		
		currentHeight += addLineSize() + SPACE_INIT_LINE  + SPACE_INIT_LINE;
		
	}
	
	private void addItens(Graphics g) {
		int col1 = SPACE_INIT_LINE;
		int cellWidth = IMAGEM_WIDTH / 3;
		int col2 = col1 + cellWidth;
		int col3 = IMAGEM_WIDTH;
		
		g.drawRoundRect(0, currentHeight, IMAGEM_WIDTH, addLineSize() * 2, 8);
		
		addTextBold(g, Messages.FECHAMENTO_ESTOQUE_DESCRICAO, col1);
		lineBreak();
		
		addTextBold(g, Messages.FECHAMENTO_ESTOQUE_QTD, col1);
		addTextBold(g, Messages.FECHAMENTO_ESTOQUE_VL_UNITARIO, col2);
		addTextBold(g, Messages.FECHAMENTO_ESTOQUE_VL_TOTAL, getAlignRight(Messages.FECHAMENTO_ESTOQUE_VL_TOTAL, col3, true));
		
		double quantidade = 0;
		double total = 0;
		for (EstoqueRep estoqueRep : estoqueRepList) {
			lineBreak();
			addTextBold(g, estoqueRep.dsProduto + " - " + estoqueRep.cdProduto, col1);
			lineBreak();
			addDrawText(g, StringUtil.getStringValueToInterface(estoqueRep.qtEstoque), col1);
			addDrawText(g, StringUtil.getStringValueToInterface(estoqueRep.vlUnitario), col2);
			addDrawText(g, StringUtil.getStringValueToInterface(estoqueRep.vlTotal), getAlignRight(StringUtil.getStringValueToInterface(estoqueRep.vlTotal), col3, false));
			quantidade += estoqueRep.qtEstoque;
			total += estoqueRep.vlTotal;
		}
		lineBreak();
		addDrawLine(g);
		addDrawText(g, StringUtil.getStringValueToInterface(quantidade), col1);
		addDrawText(g, StringUtil.getStringValueToInterface(total), getAlignRight(StringUtil.getStringValueToInterface(total), col3, false));
		
		currentHeight += addLineSize() + SPACE_INIT_LINE  + SPACE_INIT_LINE;
	}
	
	private void addAssinatura(Graphics g) {
		lineBreak();
		lineBreak();
		lineBreak();
		g.drawLine(50, currentHeight, IMAGEM_WIDTH - 50, currentHeight);
		String titulo = Messages.FECHAMENTO_ESTOQUE_ASSINATURA;
		int alignCenter = getAlignCenter(titulo, IMAGEM_WIDTH);
		g.drawText(titulo, alignCenter, currentHeight);
		lineBreak();
	}
	
	private Representante getRepresentante() throws SQLException {
		Representante representante = new Representante();
		representante.cdRepresentante = getFirstEstoqueRep().cdRepresentante;
		representante = (Representante) RepresentanteService.getInstance().findByPrimaryKey(representante);
		return representante;
	}

	private EstoqueRep getFirstEstoqueRep() {
		return (EstoqueRep) estoqueRepList.get(0);
	}

	private Empresa getEmpresa() throws SQLException {
		Empresa empresa = new Empresa();
		empresa.cdEmpresa = getFirstEstoqueRep().cdEmpresa;
		empresa = (Empresa) EmpresaService.getInstance().findByPrimaryKey(empresa);
		return empresa;
	}
	
}
