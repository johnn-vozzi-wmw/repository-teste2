package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TabPrecoClasseProd;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TabPrecoClasseProdDbxDao;

public class TabPrecoClasseProdService extends CrudService {

	public static TabPrecoClasseProdService instance;
	
	public static TabPrecoClasseProdService getInstance() {
		if (instance == null) {
			instance = new TabPrecoClasseProdService();
		}
		return instance;
	}
	
	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return TabPrecoClasseProdDbxDao.getInstance();
	}

	public TabPrecoClasseProd findByItemPedidoAndCdClasse(ItemPedido itemPedido, String cdClasse) throws SQLException {
		if (ValueUtil.isEmpty(cdClasse) || itemPedido == null) return null;
		TabPrecoClasseProd filter = new TabPrecoClasseProd(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getTabelaPreco() != null ? itemPedido.getTabelaPreco().cdTabelaPreco : null, cdClasse);
		return (TabPrecoClasseProd) findByPrimaryKey(filter);
	}

	public double findTabPrecoClasseProdComMaiorMinimoByClasse(Pedido pedido, String cdClasse) throws SQLException {
		return TabPrecoClasseProdDbxDao.getInstance().findTabPrecoClasseProdComMaiorMinimoByClasse(pedido, cdClasse);
	}

}
