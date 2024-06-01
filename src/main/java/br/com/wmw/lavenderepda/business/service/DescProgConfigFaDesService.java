package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.DescProgConfigFaDes;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescProgConfigFaDesDbxDao;

public class DescProgConfigFaDesService extends CrudService {

    private static DescProgConfigFaDesService instance;

    private DescProgConfigFaDesService() {}
	public static DescProgConfigFaDesService getInstance() { return instance == null ? instance = new DescProgConfigFaDesService() : instance; }

	@Override protected CrudDao getCrudDao() { return DescProgConfigFaDesDbxDao.getInstance(); }
	@Override public void validate(BaseDomain domain) {}

	public DescProgConfigFaDes findFaixaDescProgByProdutoCliente(ItemPedido itemPedido, boolean descconsideraItemPedido) throws SQLException {
		return DescProgConfigFaDesDbxDao.getInstance().findFaixaDescProgByProdutoClienteConsideraPedidoAtual(itemPedido);
	}

}
