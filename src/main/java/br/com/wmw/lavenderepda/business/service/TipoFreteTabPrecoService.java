package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.TipoFreteTabPreco;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoFreteTabPrecoDao;
import totalcross.util.Vector;

public class TipoFreteTabPrecoService extends CrudService {

	private static TipoFreteTabPrecoService instance;

	private TipoFreteTabPrecoService() {
	}

	public static TipoFreteTabPrecoService getInstance() {
		if (instance == null) {
			instance = new TipoFreteTabPrecoService();
		}
		return instance;
	}

	//@Override
	protected CrudDao getCrudDao() {
		return TipoFreteTabPrecoDao.getInstance();
	}

	//@Override
	public void validate(BaseDomain domain) throws java.sql.SQLException {
	}

	public TipoFreteTabPreco getTipoFreteTabPreco(ItemPedido itemPedido, String cdTipoFrete, double qtPesoPedido) throws SQLException {
		TipoFreteTabPreco tipoFreteTabPrecoFilter = new TipoFreteTabPreco();
		tipoFreteTabPrecoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		tipoFreteTabPrecoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		tipoFreteTabPrecoFilter.cdTipoFrete = cdTipoFrete;
		tipoFreteTabPrecoFilter.cdTabelaPreco = itemPedido.cdTabelaPreco;
		tipoFreteTabPrecoFilter.cdProduto = itemPedido.cdProduto;
		if (LavenderePdaConfig.usaFreteValorUnidade) {
			tipoFreteTabPrecoFilter.cdUnidade = itemPedido.cdUnidade;
		}
		tipoFreteTabPrecoFilter.sortAtributte = "qtPeso";
		tipoFreteTabPrecoFilter.sortAsc = ValueUtil.VALOR_NAO;
		Vector tipoFreteTabPrecoList =  findAllByExample(tipoFreteTabPrecoFilter);
		int size = tipoFreteTabPrecoList.size();
		itemPedido.qtPesoTipoFreteTabPrecoAtual = 0;
		itemPedido.qtPesoTipoFreteTabPrecoPosterior = 0;
		if (LavenderePdaConfig.usaFreteValorUnidade) {
			if (size > 0) return (TipoFreteTabPreco) tipoFreteTabPrecoList.items[0];
		} else {
			for (int i = 0; i < size; i++) {
				TipoFreteTabPreco tipoFreteTabPreco = (TipoFreteTabPreco) tipoFreteTabPrecoList.items[i];
				if (qtPesoPedido >= tipoFreteTabPreco.qtPeso && tipoFreteTabPreco.qtPeso != 0) {
					itemPedido.qtPesoTipoFreteTabPrecoAtual = ((TipoFreteTabPreco) tipoFreteTabPrecoList.items[i]).qtPeso;
					itemPedido.qtPesoTipoFreteTabPrecoPosterior = i == 0 ? itemPedido.qtPesoTipoFreteTabPrecoAtual : ((TipoFreteTabPreco) tipoFreteTabPrecoList.items[i - 1]).qtPeso;
					tipoFreteTabPreco.qtPesoPedido = qtPesoPedido;
					return tipoFreteTabPreco;
				} else if (tipoFreteTabPreco.qtPeso == 0) {
					return tipoFreteTabPreco;
				}
				if (i == size - 1) {
					itemPedido.qtPesoTipoFreteTabPrecoPosterior = ((TipoFreteTabPreco) tipoFreteTabPrecoList.items[size - 1]).qtPeso;
				}
			}
		}
		return new TipoFreteTabPreco(qtPesoPedido);
	}

	public void calculateFreteItemPedidoByTipoFreteTabPrecoEPeso(TipoFreteTabPreco tipoFreteTabPreco, ItemPedido itemPedido) throws SQLException {
		TipoPedido tipoPedido = itemPedido.pedido.getTipoPedido();
		if (tipoFreteTabPreco != null && tipoPedido != null && !tipoPedido.isIgnoraCalculoFrete() || LavenderePdaConfig.isPermiteInserirFreteManualItemPedido()) {
			double vlItemPedidoFrete = getVlItemPedidoFrete(tipoFreteTabPreco, itemPedido);
			itemPedido.vlItemPedidoFrete = getVlFreteArredondado(vlItemPedidoFrete);
			itemPedido.vlTotalItemPedidoFrete = getVlFreteArredondado(itemPedido.vlItemPedidoFrete * itemPedido.getQtItemFisico());
		} else {
			itemPedido.vlItemPedidoFrete = 0;
			itemPedido.vlTotalItemPedidoFrete = 0;
		}
	}

	public double getVlItemPedidoFrete(TipoFreteTabPreco tipoFreteTabPreco, ItemPedido itemPedido) throws SQLException {
		double vlBaseCalculo = "2".equals(LavenderePdaConfig.usaPctFretePorTipoPedidoTabPrecoEPeso) ? itemPedido.vlBaseItemTabelaPreco : itemPedido.vlItemPedido;
		vlBaseCalculo = "3".equals(LavenderePdaConfig.usaPctFretePorTipoPedidoTabPrecoEPeso) ? itemPedido.getItemTabelaPreco().vlBase : vlBaseCalculo;
		double vlItemPedidoFrete = LavenderePdaConfig.usaFreteValorUnidade ? tipoFreteTabPreco.vlFrete : ValueUtil.round(tipoFreteTabPreco.vlPctFrete / 100 * vlBaseCalculo);
		return vlItemPedidoFrete;
	}

	public TipoFreteTabPreco getTipoFreteTabPreco(ItemPedido itemPedido, String cdTipoFrete) throws SQLException {
		TipoFreteTabPreco tipoFreteTabPrecoFilter = new TipoFreteTabPreco();
		tipoFreteTabPrecoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		tipoFreteTabPrecoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		tipoFreteTabPrecoFilter.cdTipoFrete = cdTipoFrete;
		tipoFreteTabPrecoFilter.cdTabelaPreco = itemPedido.cdTabelaPreco;
		tipoFreteTabPrecoFilter.cdProduto = itemPedido.cdProduto;
		tipoFreteTabPrecoFilter.sortAtributte = "qtPeso";
		tipoFreteTabPrecoFilter.sortAsc = ValueUtil.VALOR_SIM;
		Vector tipoFreteTabPrecoList =  findAllByExample(tipoFreteTabPrecoFilter);
		if (ValueUtil.isNotEmpty(tipoFreteTabPrecoList)) {
			return (TipoFreteTabPreco) tipoFreteTabPrecoList.items[0];
		}
		return new TipoFreteTabPreco();
	}
	
	private double getVlFreteArredondado(double vlFrete) {
		if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
			return ValueUtil.round(vlFrete, LavenderePdaConfig.nuArredondamentoRegraInterpolacaoUnit);
		}
		return vlFrete;
	}

}