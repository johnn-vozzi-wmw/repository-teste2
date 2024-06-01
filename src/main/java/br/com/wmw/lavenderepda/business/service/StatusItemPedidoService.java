package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.business.domain.StatusItemPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.StatusItemPedidoPdbxDao;

import java.sql.SQLException;

public class StatusItemPedidoService extends CrudService {

	private static StatusItemPedidoService instance;

	public static StatusItemPedidoService getInstance() {
		if (instance == null) {
			instance = new StatusItemPedidoService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {}

	@Override
	protected CrudDao getCrudDao() {
		return StatusItemPedidoPdbxDao.getInstance();
	}

}
