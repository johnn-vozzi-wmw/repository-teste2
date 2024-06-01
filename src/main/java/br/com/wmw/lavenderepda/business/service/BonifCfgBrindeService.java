package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.BonifCfgBrinde;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.BonifCfgBrindeDbxDao;

public class BonifCfgBrindeService extends CrudService {

	private static BonifCfgBrindeService instance;
	
	public static BonifCfgBrindeService getInstance() {
		if (instance == null) {
			instance = new BonifCfgBrindeService();
		}
		return instance;
	}
	
	@Override
	public void validate(BaseDomain domain) throws SQLException {
		//Entidade nao é cadastrada no app
	}

	@Override
	protected CrudDao getCrudDao() {
		return BonifCfgBrindeDbxDao.getInstance();
	}
	
	public boolean isContemBonifCfgBrinde(ItemPedido itemPedido) throws SQLException {
		BonifCfgBrinde filter = new BonifCfgBrinde();
		filter.cdEmpresa = itemPedido.cdEmpresa;
		filter.cdProduto = itemPedido.cdProduto;
		return BonifCfgBrindeDbxDao.getInstance().countBonifCfgBrinde(filter) > 0;
	}

}
