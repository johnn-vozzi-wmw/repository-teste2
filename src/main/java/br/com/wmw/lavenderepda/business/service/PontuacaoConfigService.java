package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PontuacaoConfig;
import br.com.wmw.lavenderepda.business.domain.PontuacaoFaixaDias;
import br.com.wmw.lavenderepda.business.domain.PontuacaoFaixaPreco;
import br.com.wmw.lavenderepda.business.domain.PontuacaoProduto;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PontuacaoConfigPdbxDao;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;

import java.sql.SQLException;
import java.sql.Types;

public class PontuacaoConfigService extends CrudPersonLavendereService {

	public static final int TIPO_PONTUACAO_QTITEM = 1;
	public static final int TIPO_PONTUACAO_SEM_QTITEM = 2;
	public static final int TIPO_PONTUACAO_SEM_QTITEMFATURAMENTO = 3;

    private static PontuacaoConfigService instance;

    public static PontuacaoConfigService getInstance() {
        return instance == null ? instance = new PontuacaoConfigService() : instance;
    }

    protected CrudDao getCrudDao() { return PontuacaoConfigPdbxDao.getInstance(); }
    public void validate(BaseDomain domain) throws java.sql.SQLException {}

    //-- Configurações
    public boolean isTipoPontuacaoQtItem() {
    	return TIPO_PONTUACAO_QTITEM == LavenderePdaConfig.tipoCalculoPontuacao;
    }

	public boolean isTipoPontuacaoSemQtItem() {
		return TIPO_PONTUACAO_SEM_QTITEM == LavenderePdaConfig.tipoCalculoPontuacao;
	}

	public boolean isTipoPontuacaoDesconsideraQtItemFaturamento() {
		return TIPO_PONTUACAO_SEM_QTITEMFATURAMENTO == LavenderePdaConfig.tipoCalculoPontuacao;
	}
    
    public PontuacaoConfig findPontuacaoConfigItemPedido(final Pedido pedido, final ItemPedido itemPedido) throws SQLException {
		if (pedido == null || itemPedido == null) return null;
		return PontuacaoConfigPdbxDao.getInstance().findPontuacaoConfigItemPedido(getPontuacaoConfigFilterByItemPedido(pedido, itemPedido));
    }
    
    //-- Calculos
    public double getPontuacaoBaseItem(ItemPedido itemPedido, PontuacaoConfig pontuacaoConfig) throws SQLException {
    	if (itemPedido == null || pontuacaoConfig == null || itemPedido.getQtItemFisico() == 0) return 0d;
    	double qtItem = getQtItem(itemPedido);
    	return ValueUtil.round(PontuacaoProdutoService.getInstance().getVlPesoPontuacaoProduto(itemPedido, pontuacaoConfig) * qtItem, LavenderePdaConfig.nuCasasDecimaisPontuacao);
    }
    
    public double getPontuacaoRealizadaItem(ItemPedido itemPedido, PontuacaoConfig pontuacaoConfig) throws SQLException {
    	if (itemPedido == null || pontuacaoConfig == null || itemPedido.getQtItemFisico() == 0) return 0d;
    	double vlFatorFaixas = getVlFatorCorrecaoFaixas(pontuacaoConfig);
    	double vlPesoProduto = PontuacaoProdutoService.getInstance().getVlPesoPontuacaoProduto(itemPedido, pontuacaoConfig);
    	double qtItem = getQtItem(itemPedido);
    	return ValueUtil.round(vlPesoProduto * qtItem * vlFatorFaixas, LavenderePdaConfig.nuCasasDecimaisPontuacao);
	}

	private double getQtItem(ItemPedido itemPedido) throws SQLException {
    	if (isTipoPontuacaoSemQtItem()) return 1;
		if (!LavenderePdaConfig.usaConversaoUnidadeAlternativaPesoPontuacao && LavenderePdaConfig.usaUnidadeAlternativa) {
			ProdutoUnidade produtoUnidade = itemPedido.getProdutoUnidade();
			double nuConversaoUnidade = ProdutoUnidadeService.getInstance().getNuConversaoUnidade(itemPedido.getItemTabelaPreco(), produtoUnidade);
			return produtoUnidade.isMultiplica() ? ValueUtil.round(itemPedido.getQtItemFisico() * nuConversaoUnidade) : ValueUtil.round(itemPedido.getQtItemFisico() / nuConversaoUnidade);
		}
		if (LavenderePdaConfig.usaConversaoUnidadeAlternativaPesoPontuacao && LavenderePdaConfig.usaUnidadeAlternativa) {
			return itemPedido.getQtItemFisico();
		}
		return LavenderePdaConfig.usaConversaoUnidadesMedida && !isTipoPontuacaoDesconsideraQtItemFaturamento() ? itemPedido.qtItemFaturamento : itemPedido.getQtItemFisico();
	}

	public double getVlFatorCorrecaoFaixas(PontuacaoConfig pontuacaoConfig) {
		return getVlFatorCorrecaoFaixaDia(pontuacaoConfig) * getVlFatorCorrecaoFaixaPreco(pontuacaoConfig);
	}

	public double getVlFatorCorrecaoFaixaPreco(PontuacaoConfig pontuacaoConfig) {
		return (pontuacaoConfig != null && pontuacaoConfig.pontuacaoFaixaPreco != null) ? pontuacaoConfig.pontuacaoFaixaPreco.vlFatorCorrecao: 1d;
	}

	public double getVlFatorCorrecaoFaixaDia(PontuacaoConfig pontuacaoConfig) {
		return (pontuacaoConfig != null && pontuacaoConfig.pontuacaoFaixaDiaCondicaoPagto != null) ? pontuacaoConfig.pontuacaoFaixaDiaCondicaoPagto.vlFatorCorrecao: 1d;
	}

	public void reloadPontuacaoValorTotal(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (ValueUtil.isEmpty(pedido.itemPedidoList)) {
			pedido.showMessagePontuacao = true;
			if (pedido.necessitaRecalculoPontuacao()) {
				pedido.flCalculaPontuacao = ValueUtil.VALOR_NAO;
				PedidoService.getInstance().updateColumn(pedido.rowKey, "flCalculaPontuacao", ValueUtil.VALOR_NAO, Types.VARCHAR);
			}
			return;
		}
		if (pedido.getQtItensLista() <= 10 || (pedido.showMessagePontuacao && UiUtil.showConfirmYesNoMessage(Messages.RECALCULO_PONTUACAO_TITULO, Messages.RECALCULO_PONTUACAO_MESSAGE))) {
			reloadPontuacaoPedido(pedido, itemPedido);
		} else {
			pedido.showMessagePontuacao = false;
			if (!pedido.necessitaRecalculoPontuacao()) {
				pedido.flCalculaPontuacao = ValueUtil.VALOR_SIM;
				PedidoService.getInstance().updateColumn(pedido.rowKey, "flCalculaPontuacao", ValueUtil.VALOR_SIM, Types.VARCHAR);
			}
		}
	}

	public void reloadPontuacaoPedido(Pedido pedido, ItemPedido itemPedidoInserindo) throws SQLException {
		if (pedido == null || !pedido.isPedidoAberto()) return;
		if (ValueUtil.isEmpty(pedido.itemPedidoList)) {
			PedidoService.getInstance().findItemPedidoList(pedido);
		}
		if (ValueUtil.isEmpty(pedido.itemPedidoList)) return;
		int size = pedido.itemPedidoList.size();
		pedido.vlTotalPontuacaoBase = 0;
		pedido.vlTotalPontuacaoRealizado = 0;
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (itemPedidoInserindo == null || !ValueUtil.valueEquals(itemPedido.getPrimaryKey(), itemPedidoInserindo.getPrimaryKey())) {
				ItemPedidoService.getInstance().calculaPontuacaoItemPedido(itemPedido, pedido);
				ItemPedidoService.getInstance().updatePontuacaoItemPedido(itemPedido);
			}
			pedido.vlTotalPontuacaoBase += itemPedido.vlPontuacaoBaseItem;
			pedido.vlTotalPontuacaoRealizado += itemPedido.vlPontuacaoRealizadoItem;
		}
		pedido.flCalculaPontuacao = ValueUtil.VALOR_NAO;
		PedidoService.getInstance().updatePontuacaoPedido(pedido);
	}

	//-- Filtros
	private PontuacaoConfig getPontuacaoConfigFilterByItemPedido(final Pedido pedido, final ItemPedido itemPedido) throws SQLException {
		final Cliente cliente = pedido.getCliente();
		PontuacaoConfig pontuacaoConfigFilter = new PontuacaoConfig();
		pontuacaoConfigFilter.cdEmpresa = pedido.cdEmpresa;
		pontuacaoConfigFilter.cdRepresentante = pedido.cdRepresentante;
		pontuacaoConfigFilter.cdSegmento = cliente.cdSegmento;
		pontuacaoConfigFilter.cdCanal = cliente.cdCanal;
		pontuacaoConfigFilter.dsCidadeComercial = cliente.dsCidadeComercial;
		pontuacaoConfigFilter.dtInicioVigencia = pontuacaoConfigFilter.dtFimVigencia = DateUtil.getCurrentDate();
		pontuacaoConfigFilter.pontuacaoProduto = getPontuacaoProdutoFilterByItemPedido(itemPedido);
		pontuacaoConfigFilter.pontuacaoFaixaDiaCondicaoPagto = getPontuacaoFaixaDiaCondPagtoFilter(pedido, itemPedido);	
		pontuacaoConfigFilter.pontuacaoFaixaPreco = getPontuacaoFaixaPrecoFilter(itemPedido);
		return pontuacaoConfigFilter;
	}

	private PontuacaoFaixaPreco getPontuacaoFaixaPrecoFilter(final ItemPedido itemPedido) {
		double pctDifFaixaPreco = getPctDifFaixaPrecoPontuacao(itemPedido);
		PontuacaoFaixaPreco pontuacaoFaixaPreco = new PontuacaoFaixaPreco();
		pontuacaoFaixaPreco.cdEmpresa = itemPedido.cdEmpresa;
		pontuacaoFaixaPreco.cdRepresentante = itemPedido.cdRepresentante; 	
		pontuacaoFaixaPreco.flTipoFatorCorrecao = pctDifFaixaPreco < 0 ? PontuacaoFaixaPreco.FL_FATOR_CORRECAO_DESC : pctDifFaixaPreco > 0 ? PontuacaoFaixaPreco.FL_FATOR_CORRECAO_ACRESC : PontuacaoFaixaPreco.FL_FATOR_CORRECAO_NULO;
		pontuacaoFaixaPreco.vlPctFaixa = !pontuacaoFaixaPreco.isFatorCorrecaoNulo() ? Math.abs(pctDifFaixaPreco) : 0d;
		return pontuacaoFaixaPreco;
	}

	private PontuacaoFaixaDias getPontuacaoFaixaDiaCondPagtoFilter(final Pedido pedido, final ItemPedido itemPedido) throws SQLException {
		PontuacaoFaixaDias pontuacaoFaixaDiaCondicaoPagto = new PontuacaoFaixaDias();
		pontuacaoFaixaDiaCondicaoPagto.cdEmpresa = itemPedido.cdEmpresa;
		pontuacaoFaixaDiaCondicaoPagto.cdRepresentante = itemPedido.cdRepresentante; 
		pontuacaoFaixaDiaCondicaoPagto.flTipoFatorCorrecao = PontuacaoFaixaDias.FL_FATOR_CORRECAO_CONDICAO_PAGTO;
		pontuacaoFaixaDiaCondicaoPagto.qtDiasMedios = pedido.getCondicaoPagamento().qtDiasMediosPagamento;
		if (LavenderePdaConfig.usaValorTotalPedidoFaixaDias) {
			pontuacaoFaixaDiaCondicaoPagto.vlTotalPedido = getVlTotalPedido(pedido, itemPedido);
		}
		return pontuacaoFaixaDiaCondicaoPagto;
	}

	private double getVlTotalPedido(Pedido pedido, ItemPedido itemPedido) {
		double vlTotalPedido = itemPedido.vlTotalItemPedido;
		if (ValueUtil.isNotEmpty(pedido.itemPedidoList)) {
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido item = (ItemPedido) pedido.itemPedidoList.items[i];
				if (item.equals(itemPedido)) continue;
				vlTotalPedido += item.vlTotalItemPedido;
			}
		}
		return vlTotalPedido;
	}

	private PontuacaoProduto getPontuacaoProdutoFilterByItemPedido(final ItemPedido itemPedido) {
		PontuacaoProduto pontuacaoProduto = new PontuacaoProduto();
		pontuacaoProduto.cdEmpresa = itemPedido.cdEmpresa;
		pontuacaoProduto.cdRepresentante = itemPedido.cdRepresentante;
		pontuacaoProduto.cdProduto = itemPedido.cdProduto;
		return pontuacaoProduto;
	}

	public int getPontuacaoColor(final double vlRealizado, final double vlBase, final boolean apresentaBase, final boolean apresentaRealizado, final boolean isExtrato) {
		if (vlBase == 0 || (apresentaBase && !apresentaRealizado)) return ColorUtil.componentsForeColor;
		final int corPositivo = isExtrato ? LavendereColorUtil.COR_EXTRATO_VALOR_PONTUACAO_POSITIVO : LavendereColorUtil.COR_CAPA_VALOR_PONTUACAO_POSITIVO;
		final int corNegativo = isExtrato ? LavendereColorUtil.COR_EXTRATO_VALOR_PONTUACAO_NEGATIVO : LavendereColorUtil.COR_CAPA_VALOR_PONTUACAO_NEGATIVO;
		return (vlRealizado >= vlBase) ? corPositivo : corNegativo;
	}
	
	public String getPontuacaoBaseRealizada(final double vlRealizado, final double vlBase, final boolean apresentaBase, final boolean apresentaRealizado) {
		if (!apresentaBase && !apresentaRealizado) return null;
		if (apresentaBase && apresentaRealizado) {
			return StringUtil.getStringValueToInterface(vlRealizado, LavenderePdaConfig.nuCasasDecimaisPontuacao) + Messages.PONTUACAO_PEDIDO_SEPARADOR + StringUtil.getStringValueToInterface(vlBase, LavenderePdaConfig.nuCasasDecimaisPontuacao);
		} else if (apresentaBase) {
			return StringUtil.getStringValueToInterface(vlBase, LavenderePdaConfig.nuCasasDecimaisPontuacao);
		} else if (apresentaRealizado) {
			return StringUtil.getStringValueToInterface(vlRealizado, LavenderePdaConfig.nuCasasDecimaisPontuacao);
		}
		return null;
	}

	public double getPctDifFaixaPrecoPontuacao(ItemPedido itemPedido) {
		if (itemPedido == null || itemPedido.vlItemPedido == 0 || itemPedido.vlBasePontuacao == 0) return 0d;
		return ValueUtil.round(((itemPedido.vlItemPedido - itemPedido.vlBasePontuacao) * 100) / itemPedido.vlBasePontuacao);
	}
		
}
