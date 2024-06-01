package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.NotaCreditoPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NotaCreditoPedidoDao;
import totalcross.util.Vector;

public class NotaCreditoPedidoService extends CrudService {

    private static NotaCreditoPedidoService instance = null;
    
    private NotaCreditoPedidoService() {
        //--
    }
    
    public static NotaCreditoPedidoService getInstance() {
        if (instance == null) {
            instance = new NotaCreditoPedidoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return NotaCreditoPedidoDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) { }

	public void insereNotaCreditoPedido(Vector notaCreditoPedidoList, String nuPedido, String flOrigemPedido) throws SQLException {
		int size = notaCreditoPedidoList.size();
		for (int i = 0; i < size; i++) {
			NotaCreditoPedido notaCreditoPedido = (NotaCreditoPedido) notaCreditoPedidoList.items[i];
			notaCreditoPedido.nuPedido = nuPedido;
			notaCreditoPedido.flOrigemPedido = flOrigemPedido;
			insert(notaCreditoPedido);
		}
	}

	public void deleteByPedido(Pedido pedido) throws SQLException {
		pedido.notaCreditoPedidoList =  buscaNotaCreditoPedidoList(pedido);
		NotaCreditoPedido notaCreditoPedido = new NotaCreditoPedido(pedido, null);
		try {
			deleteAllByExample(notaCreditoPedido);
		} catch (ApplicationException e) {
			ExceptionUtil.handle(e);
		}
		
	}

	protected Vector buscaNotaCreditoPedidoList(Pedido pedido) throws SQLException {
		NotaCreditoPedido notaCreditoPedidoFilter = new NotaCreditoPedido(pedido, null);
		return findAllByExample(notaCreditoPedidoFilter);
	}

	public String getMensagemNotaCredito(Pedido pedido) throws SQLException {
		if (pedido.vlTotalNotaCredito == 0) return null;
		
		NotaCreditoPedido notaCreditoPedidoFilter = new NotaCreditoPedido(pedido, null);
		Vector cdNotasCreditoList =NotaCreditoPedidoDao.getInstance().findCdNotasCreditoUtilizaNoPedido(notaCreditoPedidoFilter);
		String cdNotasCredito = ValueUtil.VALOR_NI;
		int size = cdNotasCreditoList.size();
		for (int i = 0; i < size; i++) {
			if (i == 0) {
				cdNotasCredito =  (String) cdNotasCreditoList.items[i];
				continue;
			}
			cdNotasCredito+= "," + cdNotasCreditoList.items[i]; 
		}
		return MessageUtil.getMessage(Messages.MENSAGEM_NOTA_CREDITO,new Object[]{StringUtil.getStringValueToInterface(pedido.vlTotalNotaCredito), cdNotasCredito});		
	}

}