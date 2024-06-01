package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescQtdeAgrSimilar;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescQtdeAgrSimilarDao;
import totalcross.util.Vector;

public class DescQtdeAgrSimilarService extends CrudService {
	
	private static DescQtdeAgrSimilarService instance;
	
	public static DescQtdeAgrSimilarService getInstance() {
		if (instance == null) {
			instance = new DescQtdeAgrSimilarService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return DescQtdeAgrSimilarDao.getInstance();
	}
	
	public double getSumQtItemFisicoSimilares(ItemPedido itemPedido, String cdProduto) throws SQLException {
		Vector itensSimilares = getProdutoItensPedidoSimilares(itemPedido, cdProduto, LavenderePdaConfig.permiteTabPrecoItemDiferentePedido());
		int size = itensSimilares.size();
		double sumQtItemFisico = 0;
		for(int i=0; i < size; i++) {
			sumQtItemFisico += ((ItemPedido)itensSimilares.items[i]).qtItemFisico;
		}
		return sumQtItemFisico;
	}
	
	public Vector findDescSimilaresByFilter(ItemPedido itemPedido, String cdProduto, boolean desconsideraTabPreco) throws SQLException {
		DescQtdeAgrSimilar filter = getDomainByItemPedido(itemPedido, cdProduto);
		filter.desconsideraTabPreco = desconsideraTabPreco;
		return DescQtdeAgrSimilarDao.getInstance().findDescSimilaresByFilter(filter, itemPedido);
	}
	
	public Vector getProdutoItensPedidoSimilares(ItemPedido itemPedido, String cdProduto, boolean desconsideraTabPreco) throws SQLException {
		DescQtdeAgrSimilar filter = getDomainByItemPedido(itemPedido, cdProduto);
		filter.desconsideraTabPreco = desconsideraTabPreco;
		return DescQtdeAgrSimilarDao.getInstance().findProdutosItemPedidoSimilar(filter, itemPedido);
	}
	
	private DescQtdeAgrSimilar getDomainByItemPedido(ItemPedido itemPedido, String cdProduto) throws SQLException {
		DescQtdeAgrSimilar domain = new DescQtdeAgrSimilar();
		domain.cdEmpresa = itemPedido.cdEmpresa;
		domain.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(DescQtdeAgrSimilar.class);
		domain.cdTabelaPreco = LavenderePdaConfig.permiteTabPrecoItemDiferentePedido() ? itemPedido.cdTabelaPreco : TabelaPrecoService.getInstance().getCdTabelaPreco(itemPedido.pedido);
		domain.cdProduto = cdProduto;
		domain.cdProdutoExcept = itemPedido.cdProduto;
		return domain;
	}
}
