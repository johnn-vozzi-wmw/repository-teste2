package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.TabPrecoLoteProd;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TabPrecoLoteProdDao;

public class TabPrecoLoteProdService extends CrudService {
	
	private static TabPrecoLoteProdService instance;

    public static TabPrecoLoteProdService getInstance() {
        if (instance == null) {
            instance = new TabPrecoLoteProdService();
        }
        return instance;
    }

	@Override
	protected CrudDao getCrudDao() {
		return TabPrecoLoteProdDao.getInstance();
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException { }

	public boolean existeLoteProdutoParaTabelaPreco(ItemPedido itemPedido, String cdLoteProduto, String cdTabelaPreco) throws SQLException {
		TabPrecoLoteProd tabPrecoLoteProd = new TabPrecoLoteProd(itemPedido.cdEmpresa, itemPedido.cdRepresentante, cdLoteProduto, cdTabelaPreco);
		return count() == 0 || countByExample(tabPrecoLoteProd) > 0;
	}

}
