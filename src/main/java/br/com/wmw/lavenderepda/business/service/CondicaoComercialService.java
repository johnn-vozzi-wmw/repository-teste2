package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CondComCliente;
import br.com.wmw.lavenderepda.business.domain.CondComCondPagto;
import br.com.wmw.lavenderepda.business.domain.CondComSegCli;
import br.com.wmw.lavenderepda.business.domain.CondicaoComercial;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.validation.FilterNotInformedException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CondicaoComercialPdbxDao;
import totalcross.util.Vector;

public class CondicaoComercialService extends CrudService {

    private static CondicaoComercialService instance;

    private CondicaoComercialService() {
        //--
    }

    public static CondicaoComercialService getInstance() {
        if (instance == null) {
            instance = new CondicaoComercialService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CondicaoComercialPdbxDao.getInstance();
    }


    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public CondicaoComercial getCondicaoComercial(String cdCondicaoComercial) throws SQLException {
    	CondicaoComercial condicaoComercialFilter = new CondicaoComercial();
    	condicaoComercialFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	condicaoComercialFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondicaoComercial.class);
    	condicaoComercialFilter.cdCondicaoComercial = cdCondicaoComercial;
    	return (CondicaoComercial) findByRowKey(condicaoComercialFilter.getRowKey());
    }

	public Vector loadCondicaoComercialListForCombo(Pedido pedido) throws SQLException {
		return loadCondicaoComercialListForCombo(pedido, false);
	}
	
	public Vector loadCondicaoComercialListForCombo(Pedido pedido, boolean flCondicaoComercialParaItensPedido) throws SQLException {
		CondicaoComercial condicaoComercial = new CondicaoComercial();
		condicaoComercial.cdEmpresa = SessionLavenderePda.cdEmpresa;
		condicaoComercial.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondicaoComercial.class);
		if (flCondicaoComercialParaItensPedido) {
			condicaoComercial.flCondicaoComercialParaItensPedido = true;
			condicaoComercial.cdCondicaoComercialPedido = pedido.cdCondicaoComercial;
		}
		
		try {
			prepareCondComClienteFilter(pedido, condicaoComercial);
			prepareCondComSegCliFilter(pedido, condicaoComercial);
			prepareCondComCondPagtoFilter(pedido, condicaoComercial);
		} catch (FilterNotInformedException e) {
			return new Vector(0);
		}
		
		return findAllByExample(condicaoComercial);
	}
	
	public boolean isCondicaoComercialPedidoValida(Pedido pedido) throws SQLException {
		if (pedido != null) {
			Vector condComercialList = loadCondicaoComercialListForCombo(pedido);
			if (ValueUtil.isNotEmpty(condComercialList)) {
				for (int i = 0; i < condComercialList.size(); i++) {
					CondicaoComercial condComercial = (CondicaoComercial) condComercialList.items[i];
					if (ValueUtil.valueEquals(condComercial.cdCondicaoComercial, pedido.cdCondicaoComercial)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public Vector findAllCondicaoComercialByEmpAndRepForListProduto() throws SQLException {
		CondicaoComercial condicaoComercial = new CondicaoComercial();
		condicaoComercial.cdEmpresa = SessionLavenderePda.cdEmpresa;
		condicaoComercial.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondicaoComercial.class);
		if (LavenderePdaConfig.isApresentaPrecoCondComercialCli()) {
			condicaoComercial.condComClienteFilter = new CondComCliente();
			condicaoComercial.condComClienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			condicaoComercial.condComClienteFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondComCliente.class);
			condicaoComercial.condComClienteFilter.comparaClientesRepresentante = true;
		}
		return findAllByExample(condicaoComercial);
	}	
	
	public void aplicaIndiceFinanceiroCondComercialProdutoBase(ProdutoBase produto, CondicaoComercial condicaoComercial, boolean arredondaValorAposCalculo) {
		if (produto.itemTabelaPreco != null && condicaoComercial != null) {
			double vlUnitarioCondComercialExcec = produto.itemTabelaPreco.condComercialExcec != null ? produto.itemTabelaPreco.condComercialExcec.vlUnitario : 0d;
			if (vlUnitarioCondComercialExcec > 0d) {
				produto.itemTabelaPreco.vlUnitario = vlUnitarioCondComercialExcec;
				return;
			}
			double vlIndiceFinanceiro = 0d;
			if (condicaoComercial.vlIndiceFinanceiro > 0.0) {
				vlIndiceFinanceiro = condicaoComercial.vlIndiceFinanceiro;
			}
			if (vlIndiceFinanceiro > 0d) {
				produto.itemTabelaPreco.vlUnitario = (produto.itemTabelaPreco.vlUnitario * vlIndiceFinanceiro);
				if (arredondaValorAposCalculo) {
					produto.itemTabelaPreco.vlUnitario = ValueUtil.round(produto.itemTabelaPreco.vlUnitario);
	    		}
			}
		}
	}
	
	public void aplicaIndiceFinanceiroCondComercial(ProdutoBase produto, CondicaoComercial condicaoComercial) throws SQLException {
		if (produto != null && condicaoComercial != null) {
			double vlUnitarioCondComercialExcec = produto.itemTabelaPreco.condComercialExcec != null ? produto.itemTabelaPreco.condComercialExcec.vlUnitario : 0d;
			if (vlUnitarioCondComercialExcec > 0d) {
				produto.vlPrecoPadrao = vlUnitarioCondComercialExcec;
				return;
			}
			double vlIndiceFinanceiro = 0d;
			if (condicaoComercial.vlIndiceFinanceiro > 0.0) {
				vlIndiceFinanceiro = condicaoComercial.vlIndiceFinanceiro;
			}
			if (vlIndiceFinanceiro > 0d) {
				produto.vlPrecoPadrao = ValueUtil.round(produto.vlPrecoPadrao * vlIndiceFinanceiro);
			}
		}
	}
	
	public double findIndiceCondicaoComercial(CondicaoComercial filter) throws SQLException {
		return CondicaoComercialPdbxDao.getInstance().findIndiceCondicaoComercial(filter);
	}
	
	private void prepareCondComCondPagtoFilter(Pedido pedido, CondicaoComercial condicaoComercial) {
		if (LavenderePdaConfig.usaCondComercialPorCondPagto) {
			if (ValueUtil.isEmpty(pedido.cdCondicaoPagamento)) {
				throw new FilterNotInformedException();
			}
			condicaoComercial.condComCondPagtoFilter = new CondComCondPagto();
			condicaoComercial.condComCondPagtoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			condicaoComercial.condComCondPagtoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondComCondPagto.class);
			condicaoComercial.condComCondPagtoFilter.cdCondicaoPagamento = pedido.cdCondicaoPagamento;
		}
	}

	private void prepareCondComSegCliFilter(Pedido pedido, CondicaoComercial condicaoComercial) {
		if (LavenderePdaConfig.usaCondicaoComercialPorSegmentoECliente) {
			if (ValueUtil.isEmpty(pedido.cdSegmento)) {
				throw new FilterNotInformedException();
			}
			condicaoComercial.condComSegCliFilter = new CondComSegCli();
			condicaoComercial.condComSegCliFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			condicaoComercial.condComSegCliFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondComSegCli.class);
			condicaoComercial.condComSegCliFilter.cdCliente = pedido.cdCliente;
			condicaoComercial.condComSegCliFilter.cdSegmento = pedido.cdSegmento;
		}
	}

	private void prepareCondComClienteFilter(Pedido pedido, CondicaoComercial condicaoComercial) throws SQLException {
		if (LavenderePdaConfig.isUsaCondicaoComercialPorCliente() && !pedido.getCliente().isNovoCliente() && !pedido.getCliente().isClienteDefaultParaNovoPedido()) {
			condicaoComercial.condComClienteFilter = new CondComCliente();
			condicaoComercial.condComClienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			condicaoComercial.condComClienteFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondComCliente.class);
			condicaoComercial.condComClienteFilter.cdCliente = pedido.cdCliente;
		}
	}

}
