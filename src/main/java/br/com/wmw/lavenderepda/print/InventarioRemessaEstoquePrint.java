package br.com.wmw.lavenderepda.print;

import java.sql.SQLException;
import java.util.List;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.print.MonoImage;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.RemessaEstoque;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.service.RemessaEstoqueService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.business.service.TransportadoraService;
import totalcross.sys.Settings;
import totalcross.ui.font.Font;
import totalcross.ui.gfx.Color;
import totalcross.ui.gfx.Graphics;
import totalcross.ui.image.ImageException;

import static br.com.wmw.lavenderepda.print.AbstractPedidoServicePrint.DEVICE_SCREEN_DENSITY;
import static br.com.wmw.lavenderepda.print.AbstractPedidoServicePrint.PRINT_SCREEN_DENSITY;

public class InventarioRemessaEstoquePrint extends PrintFactory{
	
	private List<RemessaEstoque> remessaEstoqueList;
	List<Object[]> movimentosRemessa;
	private static final String NOME_ARQUIVO = "inventario.png";
	private String cdEmpresa, cdRepresentante;
	private double totalRemessa = 0, totalSaida = 0, totalRetorno = 0, totalSaldo = 0;
	
	public InventarioRemessaEstoquePrint(String cdEmpresa, String cdRepresentante) throws Exception {
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		setPrinterParameters();
	}
	
	public void run() {
		Settings.screenDensity = PRINT_SCREEN_DENSITY;
		try {
			setFonts();
			setRemessaEstoqueList(RemessaEstoqueService.getInstance().buscaRemessaLiberadasPor(cdEmpresa, cdRepresentante));
			if (getRemessaEstoqueList().isEmpty()) {
				throw new ValidationException(Messages.REMESSAESTOQUE_MSG_NENHUM_REGISTRO_ENCONTRADO);
			}
			setMovimentosRemessa(RemessaEstoqueService.getInstance().buscaProdutosRemessaPor(cdEmpresa, cdRepresentante));
			createFile();
			print(LavenderePdaConfig.usaImpressaoDevolucaoTotalEstoqueRemessaRepresentante, NOME_ARQUIVO);
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e.getMessage());
		} finally {
			Settings.screenDensity = DEVICE_SCREEN_DENSITY;
		}
	}
	
	private void createFile() throws ImageException, SQLException {
		int imageHeigth = getImageHeight();
		Settings.screenDensity = PRINT_SCREEN_DENSITY;
		try {
			Graphics g = getGrafics(imageHeigth);
			fileLayout(g);
		} finally {
			Settings.screenDensity = DEVICE_SCREEN_DENSITY;
		}
	}

	@Override
	protected Graphics getGrafics(int imageHeigth) throws ImageException {
		img = new MonoImage(IMAGEM_WIDTH, imageHeigth + SPACE_FINAL);
		Graphics g = img.getGraphics();
		g.backColor = Color.WHITE;
		g.foreColor = Color.BLACK;
		g.fillRect(0, currentHeight, IMAGEM_WIDTH, imageHeigth + SPACE_FINAL);
		g.drawRect(0, currentHeight, IMAGEM_WIDTH, imageHeigth);
		return g;
	}

	private int getImageHeight() {
		return addLineSize() * (10 + remessaEstoqueList.size() + (movimentosRemessa.size()) * 2);
	}
	
	private void fileLayout(Graphics g) throws SQLException {
		addTitle(g);
		addVendeddor(g);
		addRemessas(g);
		addProdutos(g);
		addTotalizador(g);
	}

	private void addTitle(Graphics g) {
		g.setFont(fontBold);
		lineBreak();
		g.drawText(Messages.RELATORIO_REMESSA_ESTOQUE, SPACE_INIT_LINE, currentHeight);
		lineBreak();
		g.drawText(Messages.RELATORIO_REMESSA_ESTOQUE_PARCIAL_DESDE + " " + DateUtil.formatDateDDMMYYYY(getRemessaEstoqueList().get(0).dtRemessa), SPACE_INIT_LINE, currentHeight);
		lineBreak();
		addDrawLine(g);
	}
	
	private void addVendeddor(Graphics g) throws SQLException {
		int x = SPACE_INIT_LINE;
		int cellWidth = IMAGEM_WIDTH / 3;
		x += cellWidth * 2;
		g.setFont(font);
		
		Representante representante = getRepresentante();
		g.drawText(Messages.RELATORIO_REMESSA_VENDEDOR + ": " + representante.getCdRepresentanteTxt() + " - " + representante.nmRepresentante, SPACE_INIT_LINE, currentHeight);
		
		lineBreak();
		g.drawText(Messages.RELATORIO_REMESSA_DATA + DateUtil.getCurrentDate(), SPACE_INIT_LINE, currentHeight);
		String placa = TransportadoraService.getInstance().findPlacaPor(representante.cdRepresentante);
		placa = ValueUtil.isEmpty(placa) ? "" : placa;
		g.drawText(Messages.RELATORIO_REMESSA_PLACA + placa, x, currentHeight);

		currentHeight += addLineSize() + SPACE_INIT_LINE  + SPACE_INIT_LINE;
	}
	
	private void addRemessas(Graphics g) {
		int col1 = SPACE_INIT_LINE;
		int cellWidth = IMAGEM_WIDTH / 3;
		int col2 = col1 + cellWidth;
		int col3 = IMAGEM_WIDTH;
		
		g.drawRoundRect(0, currentHeight, IMAGEM_WIDTH, addLineSize() * 2, 8);

		addTextBold(g, Messages.RELATORIO_REMESSA_DESCRICAO, col1);
		lineBreak();
		
		addTextBold(g, Messages.RELATORIO_REMESSA_NOTA, col1);
		addTextBold(g, Messages.RELATORIO_REMESSA_SERIE, getAlignRight(Messages.RELATORIO_REMESSA_SERIE, col2, true));
		addTextBold(g, Messages.RELATORIO_REMESSA_LOCAL_ESTOQUE, getAlignRight(Messages.RELATORIO_REMESSA_LOCAL_ESTOQUE, col3, true));
		
		for (RemessaEstoque remessa : remessaEstoqueList) {
			lineBreak();
			addDrawText(g, remessa.nuNotaRemessa, col1);
			addDrawText(g, remessa.nuSerieRemessa, getAlignRight(remessa.nuSerieRemessa, col2, false));
			addDrawText(g, remessa.cdLocalEstoque, getAlignRight(remessa.cdLocalEstoque, col3, false));
		}
		
		currentHeight += addLineSize() + SPACE_INIT_LINE  + SPACE_INIT_LINE;
	}
	

	private void addProdutos(Graphics g) throws SQLException {
		int col1 = SPACE_INIT_LINE;
		int cellWidth = IMAGEM_WIDTH / 5;
		int col2 = col1 + cellWidth + 130;
		int col3 = col2 + cellWidth - 10;
		int col4 = col3 + cellWidth;
		int col5 = IMAGEM_WIDTH;
		
		g.drawRoundRect(0, currentHeight, IMAGEM_WIDTH, addLineSize() * 2, 8);
		addTextBold(g, Messages.RELATORIO_REMESSA_PRODUTOS, col1);
		lineBreak();

		addTextBold(g, Messages.RELATORIO_REMESSA_DESC_PRODUTO, col1);
		addTextBold(g, Messages.RELATORIO_REMESSA_QTD, getAlignRight(Messages.RELATORIO_REMESSA_QTD, col2, true));
		addTextBold(g, Messages.RELATORIO_REMESSA_SAIDA, getAlignRight(Messages.RELATORIO_REMESSA_SAIDA, col3, true));
		addTextBold(g, Messages.RELATORIO_REMESSA_RETORNO, getAlignRight(Messages.RELATORIO_REMESSA_RETORNO, col4, true));
		addTextBold(g, Messages.RELATORIO_REMESSA_SALDO, getAlignRight(Messages.RELATORIO_REMESSA_SALDO, col5, true));

		for (Object[] array : movimentosRemessa) {
			lineBreak();
			addTextBold(g, array[0].toString(), col1);
			lineBreak();
			
			final double remessa = (double) array[2];
			final double saida = (double) array[3];
			final double retorno = (double) array[4];
			final double saldo = (double) array[5];

			addDrawText(g, array[1].toString(), col1);
			addDrawText(g, StringUtil.getStringValueToInterface(remessa), getAlignRight(StringUtil.getStringValueToInterface(remessa), col2, false));
			addDrawText(g, StringUtil.getStringValueToInterface(saida), getAlignRight(StringUtil.getStringValueToInterface(saida), col3, false));
			addDrawText(g, StringUtil.getStringValueToInterface(retorno), getAlignRight(StringUtil.getStringValueToInterface(retorno), col4, false));
			addDrawText(g, StringUtil.getStringValueToInterface(saldo), getAlignRight(StringUtil.getStringValueToInterface(saldo), col5, false));

			somaTotais(remessa, saida, retorno, saldo);
		}
	}

	private void somaTotais(final double remessa, final double saida, final double retorno, final double saldo) {
		totalRemessa += remessa;
		totalSaida += saida;
		totalRetorno += retorno;
		totalSaldo += saldo;
	}

	private void addTotalizador(Graphics g) {
		int col1 = SPACE_INIT_LINE;
		int cellWidth = IMAGEM_WIDTH / 5;
		int col2 = col1 + cellWidth + 130;
		int col3 = col2 + cellWidth -10;
		int col4 = col3 + cellWidth;
		int col5 = IMAGEM_WIDTH;
		
		lineBreak();
		lineBreak();
		addDrawLine(g); 
		
		addDrawText(g, Messages.RELATORIO_REMESSA_TOTAL, col1);
		addDrawText(g, StringUtil.getStringValueToInterface(totalRemessa), getAlignRight(StringUtil.getStringValueToInterface(totalRemessa), col2, false));
		addDrawText(g, StringUtil.getStringValueToInterface(totalSaida), getAlignRight(StringUtil.getStringValueToInterface(totalSaida), col3, false));
		addDrawText(g, StringUtil.getStringValueToInterface(totalRetorno), getAlignRight(StringUtil.getStringValueToInterface(totalRetorno), col4, false));
		addDrawText(g, StringUtil.getStringValueToInterface(totalSaldo), getAlignRight(StringUtil.getStringValueToInterface(totalSaldo), col5, false));
	}

	private Representante getRepresentante() throws SQLException {
		return RepresentanteService.getInstance().getRepresentanteById(cdRepresentante);
	}
	
	public List<RemessaEstoque> getRemessaEstoqueList() {
		return remessaEstoqueList;
	}

	public void setRemessaEstoqueList(List<RemessaEstoque> remessaEstoqueList) {
		this.remessaEstoqueList = remessaEstoqueList;
	}

	public List<Object[]> getMovimentosRemessa() {
		return movimentosRemessa;
	}

	public void setMovimentosRemessa(List<Object[]> movimentosRemessa) {
		this.movimentosRemessa = movimentosRemessa;
	}
}
