package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;

public class CalculaEmbalagensService {

	public static final String EMBALAGEM_FECHADA_QTD_MENOR = "menor";
	public static final String EMBALAGEM_FECHADA_QTD_MAIOR = "maior";
	
	private static CalculaEmbalagensService instance;
	
	private CalculaEmbalagensService() { }
	
	public static CalculaEmbalagensService getInstance() {
		if (instance == null) {
			instance = new CalculaEmbalagensService();
		}
		return instance;
	}
	
	public EmbalagensResultantes getEmbalagensResultantes(double[] tamanhosEmbalagensList, double qtItemFisico, boolean calculaSugestaoEmb) {
		EmbalagensResultantes embalagensResultantes = new EmbalagensResultantes();
		Map<String, Integer> qtdsPorTamanhoEmbalagemMap = getQtdsPorTamanhoEmbalagemMap(tamanhosEmbalagensList, qtItemFisico, calculaSugestaoEmb);
		setEmbalagensResultantes(embalagensResultantes, qtItemFisico, tamanhosEmbalagensList, qtdsPorTamanhoEmbalagemMap);
		return embalagensResultantes;
	}
	
	public Map<String, Integer> getQtdsPorTamanhoEmbalagemMap(double[] tamanhosEmbalagensList, double qtItemFisico, boolean calculaSugestaoEmb) {
		Map<String, Integer> qtdsPorTamanhoEmbalagemMap = new HashMap<String, Integer>();
		int totalItensRestantes = calculateAndStoreQtdsPorTamanhoEmbalagem(tamanhosEmbalagensList, qtItemFisico, qtdsPorTamanhoEmbalagemMap);
		if (totalItensRestantes > 0 && calculaSugestaoEmb) {
			populateQtdsPorTamanhoEmbalagemMapComMenorMaior(tamanhosEmbalagensList, qtItemFisico, totalItensRestantes, qtdsPorTamanhoEmbalagemMap);
		}
		return qtdsPorTamanhoEmbalagemMap;
	}

	private int calculateAndStoreQtdsPorTamanhoEmbalagem(double[] tamanhosEmbalagensList, double qtItemFisico, Map<String, Integer> qtdsPorTamanhoEmbalagemMap) {
		int totalItensRestantes = (int) qtItemFisico;
		if (tamanhosEmbalagensList == null) {
			return totalItensRestantes;
		}
		for (double qtdEmbalagem : tamanhosEmbalagensList) {
			int qtdEmbalagensCompletas = (int) (totalItensRestantes / (qtdEmbalagem == 0 ? 1 : qtdEmbalagem));
			if (qtdEmbalagensCompletas == 0) continue;
			//--
			qtdsPorTamanhoEmbalagemMap.put(StringUtil.getStringValue(qtdEmbalagem), (int) (qtdEmbalagensCompletas * qtdEmbalagem));
			totalItensRestantes = (int) (totalItensRestantes % qtdEmbalagem);
			if (totalItensRestantes == 0) break;
		}
		return totalItensRestantes;
	}

	private void populateQtdsPorTamanhoEmbalagemMapComMenorMaior(double[] tamanhosEmbalagensList, double qtItemFisico, int totalItensRestantes, Map<String, Integer> qtdsPorTamanhoEmbalagemMap) {
		int qtdParaFechamento = 0;
		double qtdItemFisicoProximaEmbalagem = new Double(qtItemFisico);
		double totalItensRestantesProximaEmbalagem = new Double(totalItensRestantes);;
		Map<String, Integer> qtdsProximaEmbalagemMap = new HashMap<String, Integer>();
		while (totalItensRestantesProximaEmbalagem > 0) {
			qtdItemFisicoProximaEmbalagem++;
			qtdsProximaEmbalagemMap = new HashMap<String, Integer>();
			totalItensRestantesProximaEmbalagem = calculateAndStoreQtdsPorTamanhoEmbalagem(tamanhosEmbalagensList, qtdItemFisicoProximaEmbalagem, qtdsProximaEmbalagemMap);
		}
		//--
		for (int i = tamanhosEmbalagensList.length - 1; i >= 0; i--) {
			if (qtdsProximaEmbalagemMap.get(StringUtil.getStringValue(tamanhosEmbalagensList[i])) == null) continue;
			qtdParaFechamento += qtdsProximaEmbalagemMap.get(StringUtil.getStringValue(tamanhosEmbalagensList[i]));
		}
		qtdsPorTamanhoEmbalagemMap.put(EMBALAGEM_FECHADA_QTD_MAIOR, qtdParaFechamento);
		qtdParaFechamento = 0;
		for (int i = tamanhosEmbalagensList.length - 1; i >= 0; i--) {
			if (qtdsPorTamanhoEmbalagemMap.get(StringUtil.getStringValue(tamanhosEmbalagensList[i])) == null) continue;
			qtdParaFechamento += qtdsPorTamanhoEmbalagemMap.get(StringUtil.getStringValue(tamanhosEmbalagensList[i]));
		}
		qtdsPorTamanhoEmbalagemMap.put(EMBALAGEM_FECHADA_QTD_MENOR, qtdParaFechamento);
	}
	
	public void setEmbalagensResultantes(EmbalagensResultantes embalagensResultantes, double qtItemFisico, double[] tamanhosEmbalagensList, Map<String, Integer> qtdsPorTamanhoEmbalagemMap) {
		double qtdEmbalagemMenor = qtdsPorTamanhoEmbalagemMap.get(EMBALAGEM_FECHADA_QTD_MENOR) != null ? qtdsPorTamanhoEmbalagemMap.get(EMBALAGEM_FECHADA_QTD_MENOR) : 0;
		double qtdEmbalagemMaior = qtdsPorTamanhoEmbalagemMap.get(EMBALAGEM_FECHADA_QTD_MAIOR) != null ? qtdsPorTamanhoEmbalagemMap.get(EMBALAGEM_FECHADA_QTD_MAIOR) : 0;
		double qtdEmbalagensSum = 0;
		for (double tamanhoEmbalagem : tamanhosEmbalagensList) {
			qtdEmbalagensSum += qtdsPorTamanhoEmbalagemMap.get(StringUtil.getStringValue(tamanhoEmbalagem)) != null ? qtdsPorTamanhoEmbalagemMap.get(StringUtil.getStringValue(tamanhoEmbalagem)) : 0;
		}
		embalagensResultantes.setResultadoEmbalagemCompleta(qtdEmbalagensSum == qtItemFisico ? (int) qtdEmbalagensSum : 0);
		if (qtdEmbalagemMenor > 0 || qtdEmbalagemMaior > 0) {
			embalagensResultantes.setResultadoEmbalagemCompletaMenor((int) qtdEmbalagemMenor);
			embalagensResultantes.setResultadoEmbalagemCompletaMaior((int) qtdEmbalagemMaior);
		}
	}
	
	public EmbalagensResultantes createEmbalagem(int qtMenorDescQuantidade) {
		EmbalagensResultantes embalagensResultantes = new EmbalagensResultantes();
		embalagensResultantes.setResultadoEmbalagemCompletaMaior(qtMenorDescQuantidade);
		return embalagensResultantes;
	}
	
	public class EmbalagensResultantes {
		
		private int resultadoEmbalagemCompletaMenor;
		private int resultadoEmbalagemCompletaMaior;
		private int resultadoEmbalagemCompleta;
		
		public EmbalagensResultantes() { }

		public void setResultadoEmbalagemCompletaMaior(int resultadoEmbalagemCompletaMaior) {
			this.resultadoEmbalagemCompletaMaior = resultadoEmbalagemCompletaMaior;
		}

		public void setResultadoEmbalagemCompletaMenor(int resultadoEmbalagemCompletaMenor) {
			this.resultadoEmbalagemCompletaMenor = resultadoEmbalagemCompletaMenor;
		}

		public void setResultadoEmbalagemCompleta(int resultadoEmbalagemCompleta) {
			this.resultadoEmbalagemCompleta = resultadoEmbalagemCompleta;
		}

		public int getResultadoEmbalagemCompletaMenor() {
			return this.resultadoEmbalagemCompletaMenor;
		}

		public int getResultadoEmbalagemCompletaMaior() {
			return this.resultadoEmbalagemCompletaMaior;
		}

		public int getResultadoEmbalagemCompleta() {
			return this.resultadoEmbalagemCompleta;
		}
		
		public boolean gerouEmbalagemCompleta() {
			return resultadoEmbalagemCompletaMenor == 0 && resultadoEmbalagemCompletaMaior == 0 && resultadoEmbalagemCompleta != 0;
		}
	}
	
	public EmbalagensResultantes calculaDescQtdEmbalagemCompleta(ItemPedido itemPedido, final boolean calculaSugestaoEmb) throws SQLException {
		EmbalagensResultantes embalagensResultantes = null;
		if (LavenderePdaConfig.usaDescQuantidadeApenasEmbalagemCompleta && ValueUtil.isNotEmpty(itemPedido.getItemTabelaPreco().descontoQuantidadeList) && itemPedido.getQtItemFisico() != 0) {
			ProdutoUnidade produtoUnidadeFilter = new ProdutoUnidade();
			produtoUnidadeFilter.cdEmpresa = itemPedido.cdEmpresa;
			produtoUnidadeFilter.cdRepresentante = itemPedido.cdRepresentante;
			produtoUnidadeFilter.cdProduto = itemPedido.cdProduto;
			double[] tamanhosEmbalagensList = ProdutoUnidadeService.getInstance().getTamanhosEmbalagensByExample(produtoUnidadeFilter);
			if (tamanhosEmbalagensList != null) {
				boolean qtdDigitadaFormaEmbalagemCompleta = false;
				for (double tamanhoEmbalagem : tamanhosEmbalagensList) {
					if (tamanhoEmbalagem == itemPedido.getQtItemFisico()) {
						qtdDigitadaFormaEmbalagemCompleta = true;
						break;
					}
				}
				if (!qtdDigitadaFormaEmbalagemCompleta) {
					embalagensResultantes = CalculaEmbalagensService.getInstance().getEmbalagensResultantes(tamanhosEmbalagensList, itemPedido.getQtItemFisico(), calculaSugestaoEmb);
					if (embalagensResultantes.gerouEmbalagemCompleta()) {
						itemPedido.gerouEmbalagemCompleta = true;
					} else {
						itemPedido.gerouEmbalagemCompleta = false;
					}
				} else {
					itemPedido.gerouEmbalagemCompleta = true;
				}
			}
		}
		return embalagensResultantes;
	}

}
