package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RelPedidoDataEntregaPdbxDao;
import totalcross.util.Vector;

public class RelPedidoDataEntregaService extends CrudService{

	protected CrudDao getCrudDao() {
		return RelPedidoDataEntregaPdbxDao.getInstance();
	}

	public void validate(BaseDomain domain) throws java.sql.SQLException {
		
	}

	public Vector findAllByExample(BaseDomain domain) throws java.sql.SQLException {
		Vector result = getCrudDao().findAllByExample(domain);
		result.addElements(((RelPedidoDataEntregaPdbxDao)getCrudDao()).findAllByExampleErp(domain).items);
		return result;
	}

}
