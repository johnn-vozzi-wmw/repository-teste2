package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Fornecedor;
import br.com.wmw.lavenderepda.business.domain.GrupoProdForn;
import br.com.wmw.lavenderepda.business.domain.GrupoProdTipoPed;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto1;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.domain.VerbaGrupoSaldo;
import br.com.wmw.lavenderepda.business.validation.FilterNotInformedException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.GrupoProduto1PdbxDao;
import totalcross.util.Vector;

public class GrupoProduto1Service extends CrudService {

    private static GrupoProduto1Service instance;

    private GrupoProduto1Service() {
        //--
    }

    public static GrupoProduto1Service getInstance() {
        if (instance == null) {
            instance = new GrupoProduto1Service();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return GrupoProduto1PdbxDao.getInstance();
    }

    @Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public String getDsGrupoProduto(String cdGrupoProduto1) throws SQLException {
		GrupoProduto1 grupoProduto1Filter = new GrupoProduto1();
		grupoProduto1Filter.cdGrupoproduto1 = cdGrupoProduto1;
		grupoProduto1Filter.dsGrupoproduto1 = findColumnByRowKey(grupoProduto1Filter.getRowKey(), "DSGRUPOPRODUTO1");
   		return grupoProduto1Filter.toString();
    }

    public String getLabelGrupoProduto1() {
		if (!ValueUtil.isEmpty(LavenderePdaConfig.labelGruposProduto) && !ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.labelGruposProduto)) {
			String[] temp = StringUtil.split(LavenderePdaConfig.labelGruposProduto, ';');
			if (temp.length > 0) {
				return temp[0];
			}
		}
    	return Messages.GRUPOPRODUTO1_CDGRUPOPRODUTO1;
    }

    public double getQtItensByGrupoProdutoNoPedido(Pedido pedido, String cdGrupoProduto1) throws SQLException {
		int size = pedido.itemPedidoList.size();
		double qtItensGrupo = 0d;
		if (!ValueUtil.isEmpty(cdGrupoProduto1)) {
			for ( int j = 0 ; j < size; j++ ) {
				ItemPedido itemPedidoTemp = (ItemPedido)pedido.itemPedidoList.items[j];
				if (ValueUtil.valueEquals(cdGrupoProduto1, itemPedidoTemp.getProduto().cdGrupoProduto1)) {
					qtItensGrupo += itemPedidoTemp.getQtItemFisico();
				}
			}
		}
		return qtItensGrupo;
    }


	public Vector findGrupoProdutoPedido(Pedido pedido) throws SQLException {
		Vector listCdGrupoProduto1Ultilizado = new Vector(0);
    	if (ValueUtil.isNotEmpty(pedido.itemPedidoList)) {
    		int size = pedido.itemPedidoList.size();
    		for (int i = 0; i < size; i++) {
    			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
    			if (!itemPedido.isItemBonificacao()) {
    				listCdGrupoProduto1Ultilizado.addElement(itemPedido.getProduto().cdGrupoProduto1);
    			}
			}
    	}
    	//--
    	Vector listCdGrupoProduto1Geral = GrupoProduto1PdbxDao.getInstance().findAllCdGrupoProduto1();
    	Vector listCdGrupoProduto1NaoUltilizado = new Vector(0);
    	if (ValueUtil.isNotEmpty(listCdGrupoProduto1Geral)) {
    		int size = listCdGrupoProduto1Geral.size();
    		for (int i = 0; i < size; i++) {
    			if (listCdGrupoProduto1Ultilizado.indexOf(listCdGrupoProduto1Geral.items[i]) == -1) {
    				listCdGrupoProduto1NaoUltilizado.addElement(listCdGrupoProduto1Geral.items[i]);
    			}
    		}
    	}
    	return listCdGrupoProduto1NaoUltilizado;
    }
	
	public GrupoProduto1 findGrupoProduto1ByItemPedido(ItemPedido itemPedido) throws SQLException {
		GrupoProduto1 grupoProduto1 = new GrupoProduto1();
		grupoProduto1.cdGrupoproduto1 = itemPedido.getProduto().cdGrupoProduto1;
		grupoProduto1 = (GrupoProduto1)findByRowKey(grupoProduto1.getRowKey());
		return grupoProduto1;
	}
	
	public double getQtItensConversaoUniByGrupoProdutoNoPedido(Vector itensList, String cdGrupoProduto1, int grade) throws SQLException {
		int size = itensList.size();
		double qtItensGrupo = 0d;
		if (!ValueUtil.isEmpty(cdGrupoProduto1)) {
			for ( int j = 0 ; j < size; j++ ) {
				ItemPedido itemPedidoTemp = (ItemPedido)itensList.items[j];
				if (cdGrupoProduto1.equals(itemPedidoTemp.getProduto().cdGrupoProduto1)) {
					qtItensGrupo += ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedidoTemp, itemPedidoTemp.getQtItemFisico());
				}
			}
		}
		return qtItensGrupo;
	}
	
	public List<GrupoProduto1> findDistinctGrupoProdPedido(Pedido pedido) throws SQLException {
		return GrupoProduto1PdbxDao.getInstance().findCdGrupoProdutoByPedido(pedido);
	}
	
	public boolean isSameGrupoProduto1(ItemPedido itemPedido, VerbaGrupoSaldo verbaGrupoSaldo) throws SQLException {
		final Produto produto = itemPedido.getProduto();
		return ValueUtil.valueEqualsIfNotNull(produto.cdGrupoProduto1, verbaGrupoSaldo.cdGrupoProduto1);
	}
	
	public boolean isSameGrupoProduto1(ItemPedido itemPedido, ItemPedido anotherItemPedido) throws SQLException {
		final Produto itemPedidoProduto = itemPedido.getProduto();
		final Produto anotherItemPedidoProduto = anotherItemPedido.getProduto();
		return ValueUtil.valueEqualsIfNotNull(itemPedidoProduto.cdGrupoProduto1, anotherItemPedidoProduto.cdGrupoProduto1);
	}
	
	public Vector loadGrupoProduto1(Pedido pedido, Fornecedor fornecedor) throws SQLException {
		GrupoProduto1 filter = new GrupoProduto1();
		try {
			prepareGrupoProdFornFilter(fornecedor, filter);
			prepareGrupoProdTipoPedFilter(pedido, filter);
			prepareGrupoProdExistentesFilter(pedido, filter);
		} catch (FilterNotInformedException e) {
			return new Vector(0);
		}
		return findAllByExample(filter);
	}

	private void prepareGrupoProdExistentesFilter(Pedido pedido, GrupoProduto1 filter) throws SQLException {
		if (LavenderePdaConfig.usaFiltroApenasGruposProdutosExistentesCargaRepresentante) {
			TabelaPreco tabPreco = pedido != null ? pedido.getTabelaPreco() : null;
			String cdTabelaPreco = tabPreco != null ? tabPreco.cdTabelaPreco : null;
			filter.produtoFilter = new Produto();
			filter.produtoFilter.cdTabelaPreco = cdTabelaPreco;
			filter.filterByGrupoProdutoExistente = true;
			if (LavenderePdaConfig.usaRestricaoVendaClienteProduto && pedido != null) {
				filter.restricaoFilter = RestricaoService.getInstance().getRestricaoFilter("PROD.CDPRODUTO", SessionLavenderePda.getCliente().cdCliente, null, 1, null, true, false, false);
				filter.addCteRestricao = true;
			}
		}
	}

	private void prepareGrupoProdFornFilter(Fornecedor fornecedor, GrupoProduto1 filter) {
		if (LavenderePdaConfig.filtraGrupoProdutoPorFornecedor) {
			filter.grupoProdFornFilter = new GrupoProdForn();
			if (fornecedor != null) {
				filter.grupoProdFornFilter.cdEmpresa = fornecedor.cdEmpresa; 
				filter.grupoProdFornFilter.cdFornecedor = fornecedor.cdFornecedor; 
			} else {
				filter.grupoProdFornFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			}
		}
	}

	private void prepareGrupoProdTipoPedFilter(Pedido pedido, GrupoProduto1 filter) throws SQLException {
		if (pedido != null && LavenderePdaConfig.filtraGrupoProdutoPorTipoPedido) {
	    	if (pedido.getTipoPedido() == null || ValueUtil.isEmpty(pedido.getTipoPedido().cdTipoPedido)) {
				throw new FilterNotInformedException();
			}
	    	filter.grupoProdTipoPedFilter = new GrupoProdTipoPed();
	    	filter.grupoProdTipoPedFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
	    	filter.grupoProdTipoPedFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(GrupoProdTipoPed.class);
	    	filter.grupoProdTipoPedFilter.cdTipoPedido = pedido.getTipoPedido().cdTipoPedido;
	    	filter.grupoProdTipoPedFilter.excecaoGrupoProdutoFilter = pedido.getTipoPedido().isFlExcecaoGrupoProduto();
		}
	}
}