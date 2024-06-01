package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.IndiceGrupoProd;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.integration.dao.pdbx.IndiceGrupoProdDao;

public class IndiceGrupoProdService extends CrudService {
	
	private static IndiceGrupoProdService instance;
	
	public static IndiceGrupoProdService getInstance() {
		if (instance == null) {
			instance = new IndiceGrupoProdService();
		}
		return instance;
	}
	
	@Override
	protected CrudDao getCrudDao() {
		return IndiceGrupoProdDao.getInstance();
	}

	@Override
	public void validate(BaseDomain arg0) throws SQLException {
	}
	
	public IndiceGrupoProd escolheIndicePorItemPedido(ItemPedido itemPedido) throws SQLException {
		IndiceGrupoProd filter = new IndiceGrupoProd();
		filter.cdEmpresa = itemPedido.cdEmpresa;
		filter.cdRepresentante = itemPedido.cdRepresentante;
		filter.cdTabelaPreco = itemPedido.cdTabelaPreco;
		filter.cdCondicaoPagamento = itemPedido.pedido.cdCondicaoPagamento;
		filter.cdGrupoProduto1 = itemPedido.getProduto().cdGrupoProduto1;
		filter.cdGrupoProduto2 = itemPedido.getProduto().cdGrupoProduto2;
		filter.cdGrupoProduto3 = itemPedido.getProduto().cdGrupoProduto3;
		filter.cdGrupoProduto4 = itemPedido.getProduto().cdGrupoProduto4;
		filter.cdGrupoProduto5 = itemPedido.getProduto().cdGrupoProduto5;
		return IndiceGrupoProdDao.getInstance().findIndiceFinanceiroMaiorPrioridade(filter);
	}
	
	public IndiceGrupoProd escolheIndicePorItemTabPreco(ItemTabelaPreco itemTabPreco) throws SQLException{
		IndiceGrupoProd filter = new IndiceGrupoProd();
		filter.cdEmpresa = itemTabPreco.cdEmpresa;
		filter.cdRepresentante = itemTabPreco.cdRepresentante;
		filter.cdTabelaPreco = itemTabPreco.cdTabelaPreco;
		filter.cdCondicaoPagamento = "0";
		filter.cdGrupoProduto1 = itemTabPreco.getProduto().cdGrupoProduto1;
		filter.cdGrupoProduto2 = itemTabPreco.getProduto().cdGrupoProduto2;
		filter.cdGrupoProduto3 = itemTabPreco.getProduto().cdGrupoProduto3;
		filter.cdGrupoProduto4 = itemTabPreco.getProduto().cdGrupoProduto4;
		filter.cdGrupoProduto5 = itemTabPreco.getProduto().cdGrupoProduto5;
		return IndiceGrupoProdDao.getInstance().findIndiceFinanceiroMaiorPrioridade(filter);
	}

}
