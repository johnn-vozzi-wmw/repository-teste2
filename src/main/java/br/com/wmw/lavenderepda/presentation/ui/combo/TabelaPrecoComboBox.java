package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.service.DescQuantidadePesoService;
import br.com.wmw.lavenderepda.business.service.KitTabPrecoService;
import br.com.wmw.lavenderepda.business.service.TabelaPrecoService;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class TabelaPrecoComboBox extends BaseComboBox {

	public Vector listTabelaPrecoListaColuna;

	public TabelaPrecoComboBox() {
		this(false);
	}
	
	public TabelaPrecoComboBox(boolean usaTodos) {
		super(Messages.TABELAPRECO_NOME_TABPRECO);
		if(usaTodos) {
			defaultItemType = DefaultItemType_ALL;
		}
	}

	public String getValue() {
		TabelaPreco tabelaPreco = (TabelaPreco)getSelectedItem();
		if (tabelaPreco != null) {
			return tabelaPreco.cdTabelaPreco;
		} else {
			return null;
		}
	}

	public TabelaPreco getTabelaSelecionada() {
		TabelaPreco tabelaPreco = (TabelaPreco)getSelectedItem();
		if (tabelaPreco != null) {
			return tabelaPreco;
		} else {
			return new TabelaPreco();
		}
	}

	public String[] getCdsTabelaPreco() {
		Object[] tabelaPreco = getItems();
		if (tabelaPreco == null) {
			return new String[]{};
		}
		String[] cdTabelaPreco = new String[tabelaPreco.length];
		for (int i = 0; i < tabelaPreco.length; i++) {
			TabelaPreco tab = (TabelaPreco)tabelaPreco[i];
			if (tab != null) {
				cdTabelaPreco[i] = StringUtil.getStringValue(tab.cdTabelaPreco);
			}
		}
		return cdTabelaPreco;
	}
	
	public Hashtable getCdsTabelaPrecoHash() {
		Object[] tabelaPreco = getItems();
		if (tabelaPreco == null) {
			return new Hashtable(0);
		}
		Hashtable cdsTabelaPreco = new Hashtable(tabelaPreco.length);
		for (int i = 0; i < tabelaPreco.length; i++) {
			TabelaPreco tab = (TabelaPreco)tabelaPreco[i];
			cdsTabelaPreco.put(tab.cdTabelaPreco, tab);
		}
		return cdsTabelaPreco;
	}

	public void setValue(String value) {
		select(getNewTabelaPreco(value));
	}

	private TabelaPreco getNewTabelaPreco(String value) {
		TabelaPreco tabelaPreco = new TabelaPreco();
		tabelaPreco.cdEmpresa = SessionLavenderePda.cdEmpresa;
		tabelaPreco.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TabelaPreco.class);
		tabelaPreco.cdTabelaPreco = ValueUtil.isEmpty(value) ? ValueUtil.VALOR_EMBRANCO : value;
		return tabelaPreco;
	}

	public void loadForListProduto() throws SQLException {
		loadForListProduto(false);
	}

	public void loadForListProduto(boolean usaTabelaPrecoApenasCatalogo) throws SQLException {
		removeAll();
		add(TabelaPrecoService.getInstance().findAllByEmpAndRep(usaTabelaPrecoApenasCatalogo));
		qsort();
	}
	
	public void loadTabelaPrecoByProduto(Produto produto) throws SQLException {
		add(TabelaPrecoService.getInstance().findAllByProdutoComPreco(produto));
		qsort();
	}
	
	public void loadTabelaPrecoPorKit(String cdKit) throws SQLException {
		Vector cdTabelaPrecoList = KitTabPrecoService.getInstance().findAllCdTabelaPreco(cdKit);
		removeAll();
		add(TabelaPrecoService.getInstance().carregaTabelaPreco(cdTabelaPrecoList));
		qsort();
	}

	
	public void carregaTabelaPrecoTroca(Cliente cliente) throws SQLException {
		removeAll();
		TabelaPreco tabPrecoFilter = new TabelaPreco();
		tabPrecoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		tabPrecoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TabelaPreco.class);
		tabPrecoFilter.flTroca = ValueUtil.VALOR_SIM;
		add(TabelaPrecoService.getInstance().findAllByExample(tabPrecoFilter));
		
		if (size() == 0) {
			if (!ValueUtil.isEmpty(cliente.cdTabelaPreco)) {
				TabelaPreco tab = TabelaPrecoService.getInstance().getTabelaPreco(cliente.cdTabelaPreco);
				if (cliente.cdTabelaPreco.equals(tab.cdTabelaPreco)) {
					add(tab);
				}
			}
		}
		if (size() == 0) {
			if (!ValueUtil.isEmpty(LavenderePdaConfig.tabelaPrecoParaTroca)) {
				TabelaPreco tab = TabelaPrecoService.getInstance().getTabelaPreco(LavenderePdaConfig.tabelaPrecoParaTroca);
				if (LavenderePdaConfig.tabelaPrecoParaTroca.equals(tab.cdTabelaPreco)) {
					add(tab);
				}
			}
		}
		if (size() == 0) {
			tabPrecoFilter.flTroca = null;
			tabPrecoFilter.limit = 1;
			Vector list = TabelaPrecoService.getInstance().findAllByExample(tabPrecoFilter);
			if (ValueUtil.isNotEmpty(list)) {
				TabelaPreco tab = (TabelaPreco)list.items[0];
				add(tab);
			}
		}
	}

	public void loadTabelasPrecos(Pedido pedido) throws SQLException {
		loadTabelasPrecos(pedido, false, ValueUtil.VALOR_NI);
	}

	public void loadTabelasPrecos(Pedido pedido, boolean flTabelasPrecoParaItensPedido) throws SQLException {
		loadTabelasPrecos(pedido, flTabelasPrecoParaItensPedido, ValueUtil.VALOR_NI);
	}
	
	public void loadTabelasPrecos(Pedido pedido, boolean flTabelasPrecoParaItensPedido, String tabsPrecoValidaProduto) throws SQLException {
		removeAll();
		if (pedido.isFlOrigemPedidoErp()) {
			TabelaPreco tabelaPreco = pedido.getTabelaPreco();
			tabelaPreco = (tabelaPreco == null || tabelaPreco.cdTabelaPreco == null) ? getNewTabelaPreco(pedido.cdTabelaPreco) : tabelaPreco;
			add(tabelaPreco);
			return;
		}
		if (LavenderePdaConfig.restringeTabelaPrecoPorCondicaoComercial && (!(flTabelasPrecoParaItensPedido && LavenderePdaConfig.permiteCondComercialItemDiferentePedido))) {
			carregaTabelaPrecoCondicaoComercial(pedido);
			return;
		}
		if (pedido.isPedidoBonificacao() && !LavenderePdaConfig.permiteTodasTabelasPedidoBonificacao) {
			carregaTabelaPrecoBonificacao(pedido.getCliente());
			return;
		}
		if (flTabelasPrecoParaItensPedido && LavenderePdaConfig.isUsaTabelaPrecoPedido() && !LavenderePdaConfig.permiteTabPrecoItemDiferentePedido() && !LavenderePdaConfig.filtraTabelaPrecoPelaListaDoCliente) {
			add(pedido.getTabelaPreco());
			return;
		}
		if (pedido.isPedidoTroca()) {
			carregaTabelaPrecoTroca(pedido.getCliente());
			return;
		}
		if ((LavenderePdaConfig.usaListaColunaPorTabelaPreco && flTabelasPrecoParaItensPedido) || LavenderePdaConfig.filtraTabelaPrecoPelaListaDoCliente) {
			setListTabelaPrecoListaColuna(pedido.getCliente().cdTabelaPreco);
			add(listTabelaPrecoListaColuna);
		} else {
			add(TabelaPrecoService.getInstance().loadTabelasPrecos(pedido, flTabelasPrecoParaItensPedido, tabsPrecoValidaProduto));
		}
		//--
		if (!LavenderePdaConfig.isOrdenaTabelasPrecoPorPesoMinimo()) {
			qsort();
		}
	}

	private void carregaTabelaPrecoCondicaoComercial(Pedido pedido) throws SQLException {
		add(TabelaPrecoService.getInstance().carregaTabelaPrecoCondicaoComercial(pedido));
	}

	public void carregaTabelaPrecoBonificacao(Cliente cliente) throws SQLException {
		removeAll();
		add(TabelaPrecoService.getInstance().carregaTabelaPrecoBonificacao(cliente));
	}

	public void setListTabelaPrecoListaColuna(String cdTabelaPreco) throws SQLException {
		listTabelaPrecoListaColuna = TabelaPrecoService.getInstance().setListTabelaPrecoListaColuna(cdTabelaPreco);
	}
	
	public void loadTabelaPrecoDescQuantidadePeso() throws SQLException {
		Vector list = DescQuantidadePesoService.getInstance().loadTabelaPrecoDescQuantidadePeso();
		removeAll();
		add(TabelaPrecoService.getInstance().carregaTabelaPreco(list));
	}
}
