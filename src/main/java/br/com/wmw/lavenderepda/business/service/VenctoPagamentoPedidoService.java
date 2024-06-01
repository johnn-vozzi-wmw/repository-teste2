package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.PagamentoPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.VenctoPagamentoPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VenctoPagamentoPedidoDao;
import totalcross.util.Date;
import totalcross.util.Vector;

public class VenctoPagamentoPedidoService extends CrudService {

    private static VenctoPagamentoPedidoService instance;
    
    private VenctoPagamentoPedidoService() {
        //--
    }
    
    public static VenctoPagamentoPedidoService getInstance() {
        if (instance == null) {
            instance = new VenctoPagamentoPedidoService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return VenctoPagamentoPedidoDao.getInstance();
    }

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}
	
	public int countQtVctosPagamentoPedido(PagamentoPedido pagamentoPedido) throws SQLException {
		VenctoPagamentoPedido filter = getFilterByPagamentoPedido(pagamentoPedido);
		return countByExample(filter);
	}

	public VenctoPagamentoPedido getFilterByPagamentoPedido(PagamentoPedido pagamentoPedido) {
		VenctoPagamentoPedido filter = new VenctoPagamentoPedido();
		filter.cdEmpresa = pagamentoPedido.cdEmpresa;
		filter.cdRepresentante = pagamentoPedido.cdRepresentante;
		filter.nuPedido = pagamentoPedido.nuPedido;
		filter.flOrigemPedido = pagamentoPedido.flOrigemPedido;
		filter.cdPagamentoPedido = pagamentoPedido.cdPagamentoPedido;
		return filter;
	}
	
	public void findVctosPagamentoPedido(PagamentoPedido pagamentoPedido) throws SQLException {
		VenctoPagamentoPedido filter = getFilterByPagamentoPedido(pagamentoPedido);
		Vector list = findAllByExample(filter);
		int size = list.size();
		List<VenctoPagamentoPedido> venctoPagPedidoList = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			venctoPagPedidoList.add((VenctoPagamentoPedido) list.items[i]);
		}
		pagamentoPedido.venctoPagamentoPedidos = venctoPagPedidoList;
	}
	
	public void validateVctosFechamentoPedido(Pedido pedido) throws SQLException {
		Vector pagamentoPedidoList = pedido.pagamentoPedidoList;
		int size = pagamentoPedidoList.size();
		Date dtMin = DateUtil.getCurrentDate();
		PagamentoPedido pagamentoPedido;
		for (int i = 0; i < size; i++) {
			pagamentoPedido = (PagamentoPedido)pagamentoPedidoList.items[i];
			findVctosPagamentoPedido(pagamentoPedido);
			List<VenctoPagamentoPedido> vctoList = pagamentoPedido.venctoPagamentoPedidos;
			for (VenctoPagamentoPedido venctoPagamentoPedido : vctoList) {
				if (venctoPagamentoPedido.dtVencimento.isBefore(dtMin)) {
					throw new ValidationException(MessageUtil.getMessage(Messages.VENCTOADIC_DATA_VCTO_ANTERIOR_MINIMA_FECHAMENTO, venctoPagamentoPedido.nuSeqVenctoPagamentoPedido));
				}
			}
		}
	}
	
	public void deleteVctosByPagamentoPedido(PagamentoPedido pagamentoPedido) throws SQLException {
		VenctoPagamentoPedido filter = getFilterByPagamentoPedido(pagamentoPedido);
		deleteAllByExample(filter);
	}
	
	public Vector findVenctosByPedido(Pedido pedido) throws SQLException {
		VenctoPagamentoPedido filter = new VenctoPagamentoPedido();
		filter.cdEmpresa = pedido.cdEmpresa;
		filter.cdRepresentante = pedido.cdRepresentante;
		filter.nuPedido = pedido.nuPedido;
		filter.flOrigemPedido = pedido.flOrigemPedido;
		return findAllByExample(filter);
	}
    
	
}