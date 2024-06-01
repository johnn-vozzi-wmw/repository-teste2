package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Rede;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RedeDbxDao;
import totalcross.util.Vector;

public class RedeService extends CrudService {

	private static RedeService instance;

	private RedeService() {
		//--
	}

	public static RedeService getInstance() {
		if (instance == null) {
			instance = new RedeService();
		}
		return instance;
	}

	//@Override
	protected CrudDao getCrudDao() {
		return RedeDbxDao.getInstance();
	}

	//@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	public Rede findRedeByCliente(Cliente cliente) throws SQLException {
		Rede redeCliente = new Rede();
		redeCliente.cdEmpresa = cliente.cdEmpresa;
		redeCliente.cdRepresentante = cliente.cdRepresentante;
		redeCliente.cdRede = cliente.cdRede;
		redeCliente = (Rede) findByRowKey(redeCliente.getRowKey());
		return redeCliente;
	}
	
	public Rede findRedeByClienteIgnoraEmpresa(Cliente cliente) throws SQLException {
		Rede rede = new Rede();
		rede.cdRepresentante = cliente.cdRepresentante;
		rede.cdRede = cliente.cdRede;
		if (ValueUtil.isNotEmpty(rede.cdRede)) {
			Vector list = findAllByExample(rede);
			return list.size() > 0 ? (Rede) list.items[0] : null;
		}
		return null;
	}
}