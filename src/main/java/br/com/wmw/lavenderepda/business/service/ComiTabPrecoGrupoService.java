package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ComiTabPrecoGrupo;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ComiTabPrecoGrupoDao;

public class ComiTabPrecoGrupoService extends CrudService {
	
	private static ComiTabPrecoGrupoService instance;
	
	public static ComiTabPrecoGrupoService getInstance() {
		if (instance == null) {
			instance = new ComiTabPrecoGrupoService();
		}
		return instance;
	}
	
	@Override
	public void validate(BaseDomain domain) {
	
	}

	@Override
	protected CrudDao getCrudDao() {
		return ComiTabPrecoGrupoDao.getInstance();
	}
	
	public double getPctComissaoByItemPedido(ItemPedido itemPedido) throws SQLException {
		ComiTabPrecoGrupo comiTabPrecoGrupo = new ComiTabPrecoGrupo();
		comiTabPrecoGrupo.cdEmpresa = itemPedido.cdEmpresa;
		comiTabPrecoGrupo.cdRepresentante = itemPedido.cdRepresentante;
		comiTabPrecoGrupo.cdTabelaPreco = itemPedido.cdTabelaPreco;
		comiTabPrecoGrupo.cdGrupoProduto1 = itemPedido.getProduto().cdGrupoProduto1;
		return ValueUtil.getDoubleValue(getCrudDao().findColumnByRowKey(comiTabPrecoGrupo.getRowKey(), "vlPctComissao"));
	}

}
