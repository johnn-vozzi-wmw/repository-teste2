package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.TabPrecoGrupoProd;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TabPrecoGrupoProdDbxDao;

public class TabPrecoGrupoProdService extends CrudService {
	
	public static TabPrecoGrupoProdService instance;
	
	public static TabPrecoGrupoProdService getInstance() {
		if (instance == null) {
			instance = new TabPrecoGrupoProdService();
		}
		return instance;
	}
	
	@Override
	public void validate(BaseDomain domain) throws SQLException {

	}

	@Override
	protected CrudDao getCrudDao() {
		return TabPrecoGrupoProdDbxDao.getInstance();
	}
	
	public TabPrecoGrupoProd findByItemPedidoAndCdGrupo(ItemPedido itemPedido, String cdGrupoProduto1) throws SQLException {
		TabPrecoGrupoProd filter = new TabPrecoGrupoProd();
		filter.cdEmpresa = itemPedido.cdEmpresa;
		filter.cdRepresentante = itemPedido.cdRepresentante;
		filter.cdTabelaPreco = itemPedido.getTabelaPreco() != null ? itemPedido.getTabelaPreco().cdTabelaPreco : null;
		if (ValueUtil.isNotEmpty(cdGrupoProduto1)) {
			filter.cdGrupoProduto1 = cdGrupoProduto1;
			return (TabPrecoGrupoProd)findByRowKey(filter.getRowKey());
		}
		return null;
	}

}
