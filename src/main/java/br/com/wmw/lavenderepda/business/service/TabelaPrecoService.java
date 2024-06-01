package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.CondPagtoTabPreco;
import br.com.wmw.lavenderepda.business.domain.CondicaoComercial;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoTabPreco;
import br.com.wmw.lavenderepda.business.domain.TabPrecTipoPedido;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.domain.TabelaPrecoCli;
import br.com.wmw.lavenderepda.business.domain.TabelaPrecoReg;
import br.com.wmw.lavenderepda.business.domain.TabelaPrecoRep;
import br.com.wmw.lavenderepda.business.domain.TabelaPrecoSeg;
import br.com.wmw.lavenderepda.business.validation.FilterNotInformedException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TabelaPrecoPdbxDao;
import totalcross.util.Vector;

public class TabelaPrecoService extends CrudService {

    private static TabelaPrecoService instance;

    private TabelaPrecoService() {
        //--
    }

    public static TabelaPrecoService getInstance() {
        if (instance == null) {
            instance = new TabelaPrecoService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return TabelaPrecoPdbxDao.getInstance();
    }

    @Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
    @Override
    public Vector findAllByExample(BaseDomain domain) throws SQLException {
    	TabelaPreco tabelaPreco = (TabelaPreco) domain;
    	if (LavenderePdaConfig.usaTabelaPrecoComVigencia) {
        	tabelaPreco.dtInicial = DateUtil.getCurrentDate();
    		tabelaPreco.dtFinal = DateUtil.getCurrentDate();
    	}
    	return super.findAllByExample(tabelaPreco);
    }
    
    public Vector findAllByEmpAndRep() throws SQLException {
    	return findAllByEmpAndRep(false);
    }

    public Vector findAllByEmpAndRep(boolean usaTabelaPrecoApenasCatalogo) throws SQLException {
    	TabelaPreco tabelaPreco = getTabelaPrecoInstance();
    	tabelaPreco.usaTabelaPrecoApenasCatalogo = usaTabelaPrecoApenasCatalogo;
		return findAllByExample(tabelaPreco);
    }
    
    public Vector findAllByProdutoComPreco(Produto produto) throws SQLException {
    	Vector tabelaPrecoList = findAllByExample(getTabelaPrecoInstance());
    	ItemTabelaPreco itemTabelaPrecoFilter = new ItemTabelaPreco();
		itemTabelaPrecoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		itemTabelaPrecoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		itemTabelaPrecoFilter.cdProduto = produto.cdProduto;
		Vector itemTabPrecoList = ItemTabelaPrecoService.getInstance().findAllByExample(itemTabelaPrecoFilter);
		
		tabelaPrecoList = mergeTabPrecoComItemTabPreco(tabelaPrecoList, itemTabPrecoList);
		return tabelaPrecoList;
    }
    
    private Vector mergeTabPrecoComItemTabPreco(Vector tabelaPrecoList, Vector itemTabPrecoList) {
		Vector newTabelaPrecoList = new Vector();
		if (tabelaPrecoList != null && itemTabPrecoList != null) {
			for (int i = 0; i < tabelaPrecoList.size(); i++) {
				TabelaPreco tabelaPreco = (TabelaPreco)tabelaPrecoList.items[i];
				for (int j = 0; j < itemTabPrecoList.size(); j++) {
					ItemTabelaPreco itemTabelaPreco = (ItemTabelaPreco)itemTabPrecoList.items[j];
					if (StringUtil.getStringValue(tabelaPreco.cdTabelaPreco).equals(StringUtil.getStringValue(itemTabelaPreco.cdTabelaPreco))) {
						newTabelaPrecoList.addElement(tabelaPreco);
						break;
					}
				}
			}
		}
		return newTabelaPrecoList;
	}

    public String[] findAllCdsTabPrecoByExample() throws SQLException {
    	String[] cdsTabPreco = TabelaPrecoPdbxDao.getInstance().findCdsTabPrecoByExample(getTabelaPrecoInstance());
		return cdsTabPreco != null ? cdsTabPreco : new String[]{};
    }

    public TabelaPreco getTabelaPreco(String cdTabelaPreco) throws SQLException {
    	TabelaPreco tabelaPreco = getTabelaPrecoInstance();
    	tabelaPreco.cdTabelaPreco = cdTabelaPreco;
    	tabelaPreco = (TabelaPreco) findByRowKey(tabelaPreco.getRowKey());
    	if (tabelaPreco == null) {
    		tabelaPreco = new TabelaPreco();
    	}
    	return tabelaPreco;
    }

    public String getDsTabelaPreco(String cdTabelaPreco) throws SQLException {
    	if (ValueUtil.isNotEmpty(cdTabelaPreco)) {
    		TabelaPreco tabelaPreco = getTabelaPrecoInstance();
    		tabelaPreco.cdTabelaPreco = cdTabelaPreco;
    		tabelaPreco = (TabelaPreco) findByRowKeyInCache(tabelaPreco);
    		if (tabelaPreco != null) {
    			return ValueUtil.isNotEmpty(tabelaPreco.dsTabelaPreco) ? tabelaPreco.dsTabelaPreco : tabelaPreco.cdTabelaPreco;
    		}
    	}
    	return cdTabelaPreco;
    }

	private TabelaPreco getTabelaPrecoInstance() {
		TabelaPreco tabelaPreco = new TabelaPreco();
		tabelaPreco.cdEmpresa = SessionLavenderePda.cdEmpresa;
		tabelaPreco.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TabelaPreco.class);
		if (LavenderePdaConfig.usaTabelaPrecoComVigencia) {
        	tabelaPreco.dtInicial = DateUtil.getCurrentDate();
    		tabelaPreco.dtFinal = DateUtil.getCurrentDate();
    	}
		return tabelaPreco;
	}

	public Vector loadTabelasPrecos(Pedido pedido) throws SQLException {
		return loadTabelasPrecos(pedido, false);
	}

    public Vector loadTabelasPrecos(Pedido pedido, boolean flTabelasPrecoParaItensPedido) throws SQLException {
		return loadTabelasPrecos(pedido, flTabelasPrecoParaItensPedido, ValueUtil.VALOR_NI);
    }

	public Vector loadTabelasPrecos(Pedido pedido, boolean flTabelasPrecoParaItensPedido, String tabsPrecoValidaProduto) throws SQLException {
		Cliente cliente = pedido.getCliente();
		Vector list;
		TabelaPreco tabPrecoFilter = getTabelaPrecoInstance();
		TabelaPreco tabPrecoPedido = pedido.getTabelaPreco();
		if (tabPrecoPedido != null && flTabelasPrecoParaItensPedido && LavenderePdaConfig.isUsaTabelaPrecoPedido() && LavenderePdaConfig.permiteTabPrecoItemDiferentePedido()) {
			if (tabPrecoPedido.isFlAcessaOutrasTab()) {
				tabPrecoFilter.notFlAcessivelOutrasTab = ValueUtil.VALOR_NAO;
			}
			tabPrecoFilter.cdTabelaPrecoPedidoFilter = tabPrecoPedido.cdTabelaPreco;
		}
		if (LavenderePdaConfig.usaModuloTrocaNoPedido || ValueUtil.isNotEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher)) {
			tabPrecoFilter.notFlTroca = ValueUtil.VALOR_SIM;
		}
		if (LavenderePdaConfig.isUsaPedidoBonificacao() && !pedido.isPedidoBonificacao()) {
			tabPrecoFilter.notFlBonificacao = ValueUtil.VALOR_SIM;
		}
		if (LavenderePdaConfig.restringeTabPrecoEspecialConformePadraoCliente && ValueUtil.isNotEmpty(cliente.cdTabelaPreco)) {
			tabPrecoFilter.cdTabelaPrecoClienteFilter = cliente.cdTabelaPreco;
			tabPrecoFilter.notFlEspecial = ValueUtil.VALOR_SIM;
		}
		if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco() && ValueUtil.isNotEmpty(tabsPrecoValidaProduto)) {
			tabPrecoFilter.cdTabelaPrecoList = StringUtil.split(tabsPrecoValidaProduto, ProdutoTabPreco.SEPARADOR_CAMPOS);
		}
		if (LavenderePdaConfig.usaTabelaPrincipal() && flTabelasPrecoParaItensPedido && tabPrecoPedido != null && ValueUtil.isNotEmpty(tabPrecoPedido.cdTabelaPreco)) {
			tabPrecoFilter.filterByHierarquiaTabelasPreco = true;
			tabPrecoFilter.cdTabelaPrecoPedidoFilter = tabPrecoPedido.cdTabelaPreco;
		}
		try {
			prepareCondPagtoTabPrecoFilter(pedido, tabPrecoFilter, LavenderePdaConfig.usaTabelaPrecoPorClienteOuCondPagto);
			prepareTabelaPrecoCliFilter(cliente, tabPrecoFilter);
			prepareTabelaPrecoRepFilter(tabPrecoFilter);
			prepareTabelaPrecoRegFilter(cliente, tabPrecoFilter);
			prepareTabelaPrecoSegFilter(pedido, tabPrecoFilter);
			prepareTabPrecTipoPedidoFilter(pedido, tabPrecoFilter);
			
			list = super.findAllByExample(tabPrecoFilter);
			
			if (LavenderePdaConfig.usaTabelaPrecoPorClienteOuCondPagto) {
				if (tabPrecoFilter.tabelaPrecoCliFilter != null && ValueUtil.isEmpty(list)) {
					pedido.usandoTabelaPrecoPorCondicaoPagamento = true;
					tabPrecoFilter.tabelaPrecoCliFilter = null;
					prepareCondPagtoTabPrecoFilter(pedido, tabPrecoFilter, false);
					list = super.findAllByExample(tabPrecoFilter);
				} else {
					pedido.usandoTabelaPrecoPorCondicaoPagamento = false;
				}
			}
			if (cliente.isNovoCliente() && LavenderePdaConfig.mostraTodasTabelaPrecoPedidoNovoCliente && ValueUtil.isEmpty(list)) {
				list = findAllByEmpAndRep();
			}
		} catch (FilterNotInformedException e) {
			return new Vector(0);
		}
		//--
		return list;
    }
	
	public Vector loadTabelaPrecoFiltrandoPorListaDeCodigos(String [] cdTabelaPrecoList) throws SQLException {
		Vector tabelaPrecoList = new Vector();
		if (ValueUtil.isEmpty(cdTabelaPrecoList)) return tabelaPrecoList;
		
		TabelaPreco tabelaPrecoFilter = getTabelaPrecoInstance();
	    for (int i = 0; i < cdTabelaPrecoList.length; i++) {
			tabelaPrecoFilter.cdTabelaPreco = cdTabelaPrecoList[i];
			TabelaPreco tabelaPreco = (TabelaPreco) findByRowKey(tabelaPrecoFilter.getRowKey());
			if (tabelaPreco == null || ValueUtil.isEmpty(tabelaPreco.cdTabelaPreco)) continue; 
			
			tabelaPrecoList.addElement(tabelaPreco);
		}
		return tabelaPrecoList;
	}

	public Vector setListTabelaPrecoListaColuna(String cdTabelaPreco) throws SQLException {
		if (LavenderePdaConfig.usaListaColunaPorTabelaPreco || LavenderePdaConfig.filtraTabelaPrecoPelaListaDoCliente) {
			TabelaPreco tabelaPrecoFilter = getTabelaPrecoInstance();
			tabelaPrecoFilter.filterByCdListaTabelaPreco = true;
			if (ValueUtil.isNotEmpty(cdTabelaPreco)) {
				TabelaPreco tabelaPrecoCdListaFilter = new TabelaPreco();
				tabelaPrecoCdListaFilter.cdTabelaPreco = cdTabelaPreco;
				tabelaPrecoCdListaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
				tabelaPrecoCdListaFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TabelaPreco.class);
				tabelaPrecoFilter.cdListaTabelaPreco = ValueUtil.getIntegerValue(findColumnByRowKey(tabelaPrecoCdListaFilter.getRowKey(), "CDLISTATABELAPRECO"));
				if (tabelaPrecoFilter.cdListaTabelaPreco == 0) {
					String cdTabPreco = StringUtil.getStringValue(cdTabelaPreco);
					tabelaPrecoFilter.cdListaTabelaPreco = ValueUtil.getIntegerValue(cdTabPreco.substring(0, cdTabPreco.length() - LavenderePdaConfig.qtCaracteresColunaTabelaPreco));
				}
				return super.findAllByExample(tabelaPrecoFilter);
			}
		}
		return new Vector(0);
	}
	
	public Vector carregaTabelaPrecoBonificacao(Cliente cliente) throws SQLException {
		TabelaPreco tabPrecoFilter = getTabelaPrecoInstance();
		tabPrecoFilter.flBonificacao = ValueUtil.VALOR_SIM;
		if (LavenderePdaConfig.permiteTabPrecoItemDiferentePedido()) {
			prepareTabelaPrecoCliFilter(cliente, tabPrecoFilter);
		}
		Vector listTabPreco = super.findAllByExample(tabPrecoFilter);
		if (ValueUtil.isEmpty(listTabPreco)) {
			if (ValueUtil.isNotEmpty(cliente.cdTabelaPreco)) {
				TabelaPreco tab = getTabelaPreco(cliente.cdTabelaPreco);
				if (ValueUtil.isNotEmpty(tab.cdTabelaPreco) && tab.cdTabelaPreco.equals(cliente.cdTabelaPreco)) {
					listTabPreco.addElement(tab);
				}
			}
		}
		return listTabPreco;
	}

	public boolean isTabelaPrecoPedidoValida(Pedido pedido) throws SQLException {
		if (pedido != null) {
			Vector tabelaPrecoList = loadTabelasPrecos(pedido);
			if (ValueUtil.isNotEmpty(tabelaPrecoList)) {
				for (int i = 0; i < tabelaPrecoList.size(); i++) {
					TabelaPreco tabPreco = (TabelaPreco) tabelaPrecoList.items[i];
					if (ValueUtil.valueEquals(tabPreco.cdTabelaPreco, pedido.cdTabelaPreco)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public Vector carregaTabelaPrecoCondicaoComercial(Pedido pedido) throws SQLException {
		Vector tabPrecoList = new Vector();
		CondicaoComercial condicaoComercialFilter = new CondicaoComercial();
		condicaoComercialFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		condicaoComercialFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondicaoComercial.class);
		condicaoComercialFilter.cdCondicaoComercial = pedido.cdCondicaoComercial;
		String cdTabelaPreco = CondicaoComercialService.getInstance().findColumnByRowKey(condicaoComercialFilter.getRowKey(), "cdTabelaPreco");
		if (ValueUtil.isNotEmpty(cdTabelaPreco)) {
			TabelaPreco tabelaPrecoFilter = getTabelaPrecoInstance();
			tabelaPrecoFilter.cdTabelaPreco = cdTabelaPreco;
			tabelaPrecoFilter = (TabelaPreco) findByRowKey(tabelaPrecoFilter.getRowKey());
			if (tabelaPrecoFilter != null) {
				tabPrecoList.addElement(tabelaPrecoFilter);
			}
		}
		return tabPrecoList;
	}
	
	public Vector carregaTabelaPreco(Vector cdTabelaPrecoList) throws SQLException {
		if (ValueUtil.isEmpty(cdTabelaPrecoList)) {
			return new Vector();
		}
		TabelaPreco tabPrecoFilter = getTabelaPrecoInstance();
		tabPrecoFilter.cdTabelaPrecoList = (String[]) cdTabelaPrecoList.toObjectArray();
		return findAllByExample(tabPrecoFilter);
		
	}
    
    public String getFlPromocionalFromCache(ItemPedido itemPedido) throws SQLException {
    	String flPromocional = ValueUtil.VALOR_NAO;
    	TabelaPreco tabelaPreco = new TabelaPreco();
		tabelaPreco.cdEmpresa = itemPedido.cdEmpresa;
		tabelaPreco.cdRepresentante = itemPedido.cdRepresentante; 
		tabelaPreco.cdTabelaPreco = itemPedido.cdTabelaPreco;
		tabelaPreco = (TabelaPreco) findByRowKeyInCache(tabelaPreco);
		if (tabelaPreco != null) {
			flPromocional = tabelaPreco.flPromocional;
		}
		return flPromocional;
    }
    
    public String getCdTabelaPreco(Pedido pedido) throws SQLException {
    	if (LavenderePdaConfig.ocultaTabelaPrecoPedido) {
			Vector tabPrecoList = loadTabelasPrecos(pedido);
			return ValueUtil.isNotEmpty(tabPrecoList) ? ((TabelaPreco) tabPrecoList.items[0]).cdTabelaPreco : "";
		}
		return pedido.cdTabelaPreco;
    }
    
    private void prepareTabPrecTipoPedidoFilter(Pedido pedido, TabelaPreco tabPrecoFilter) throws SQLException {
		if (LavenderePdaConfig.usaTabelaPrecoPorTipoPedido && ValueUtil.isNotEmpty(pedido.cdTipoPedido)) {
			tabPrecoFilter.tabPrecTipoPedidoFilter = new TabPrecTipoPedido();
			tabPrecoFilter.tabPrecTipoPedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			tabPrecoFilter.tabPrecTipoPedidoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TabPrecTipoPedido.class);
			tabPrecoFilter.tabPrecTipoPedidoFilter.cdTipoPedido = pedido.cdTipoPedido;
			tabPrecoFilter.tabPrecTipoPedidoFilter.excecaoTipoPedidoFilter = ValueUtil.getBooleanValue(pedido.getTipoPedido().flExcecaoTabPreco);
		}
	}

	private void prepareTabelaPrecoSegFilter(Pedido pedido, TabelaPreco tabPrecoFilter) {
		if (LavenderePdaConfig.usaSegmentoNoPedido && LavenderePdaConfig.usaTabelaPrecoPorSegmento) {
			if (ValueUtil.isEmpty(pedido.cdSegmento)) {
				throw new FilterNotInformedException();
			}
			tabPrecoFilter.tabelaPrecoSegFilter = new TabelaPrecoSeg();
			tabPrecoFilter.tabelaPrecoSegFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			tabPrecoFilter.tabelaPrecoSegFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TabelaPrecoSeg.class);
			tabPrecoFilter.tabelaPrecoSegFilter.cdSegmento = pedido.cdSegmento;
		}
	}

	private void prepareTabelaPrecoRegFilter(Cliente cliente, TabelaPreco tabPrecoFilter) {
		if (LavenderePdaConfig.filtraTabelaPrecoPelaRegiaoDoCliente && ValueUtil.isNotEmpty(cliente.cdRegiao)) {
			tabPrecoFilter.tabelaPrecoRegFilter = new TabelaPrecoReg();
			tabPrecoFilter.tabelaPrecoRegFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			tabPrecoFilter.tabelaPrecoRegFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TabelaPrecoReg.class);
			tabPrecoFilter.tabelaPrecoRegFilter.cdRegiao = cliente.cdRegiao;
		}
	}

	private void prepareTabelaPrecoRepFilter(TabelaPreco tabPrecoFilter) {
		if (LavenderePdaConfig.usaTabelaPrecoPorRepresentante && SessionLavenderePda.isUsuarioSupervisor()) {
			tabPrecoFilter.tabelaPrecoRepFilter = new TabelaPrecoRep();
			tabPrecoFilter.tabelaPrecoRepFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			tabPrecoFilter.tabelaPrecoRepFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		}
	}

	private void prepareTabelaPrecoCliFilter(Cliente cliente, TabelaPreco tabPrecoFilter) {
		if (LavenderePdaConfig.usaTabelaPrecoPorCliente && cliente != null && !cliente.isNovoCliente() && !cliente.isClienteDefaultParaNovoPedido()) {
			tabPrecoFilter.tabelaPrecoCliFilter = new TabelaPrecoCli();
			tabPrecoFilter.tabelaPrecoCliFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			tabPrecoFilter.tabelaPrecoCliFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TabelaPrecoCli.class);
			tabPrecoFilter.tabelaPrecoCliFilter.cdCliente = cliente.cdCliente;
		}
	}

	private void prepareCondPagtoTabPrecoFilter(Pedido pedido, TabelaPreco tabPrecoFilter, boolean ignoraFiltroPorCondPagto) {
		if (LavenderePdaConfig.usaTabelaPrecoPorCondicaoPagamento && !ignoraFiltroPorCondPagto) {
			if (ValueUtil.isEmpty(pedido.cdCondicaoPagamento)) {
				throw new FilterNotInformedException();
			}
			tabPrecoFilter.condPagtoTabPrecoFilter = new CondPagtoTabPreco();
			tabPrecoFilter.condPagtoTabPrecoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			tabPrecoFilter.condPagtoTabPrecoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondPagtoTabPreco.class);
			tabPrecoFilter.condPagtoTabPrecoFilter.cdCondicaoPagamento = pedido.cdCondicaoPagamento;
		}
	}

}
