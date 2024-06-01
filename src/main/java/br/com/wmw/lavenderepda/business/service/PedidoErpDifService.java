package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoErpDif;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoErpDifPdbxDao;

public class PedidoErpDifService extends CrudService {

    private static PedidoErpDifService instance;

    private PedidoErpDifService() {
        //--
    }

    public static PedidoErpDifService getInstance() {
        if (instance == null) {
            instance = new PedidoErpDifService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return PedidoErpDifPdbxDao.getInstance();
    }

    @Override
    public void validate(BaseDomain domain) throws SQLException {
    }

    public void deleteByPedido(Pedido pedido) {
    	PedidoErpDif pedidoerpdif = new PedidoErpDif();
		pedidoerpdif.nuPedido = pedido.nuPedido;
		pedidoerpdif.cdRepresentante = pedido.cdRepresentante;
		pedidoerpdif.cdEmpresa = pedido.cdEmpresa;
		pedidoerpdif.flOrigemPedido = pedido.flOrigemPedido;
		try {
			delete(pedidoerpdif);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
    }

}