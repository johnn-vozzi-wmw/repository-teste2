package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.NotaCredito;
import br.com.wmw.lavenderepda.business.domain.NotaCreditoPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NotaCreditoDao;
import totalcross.util.Vector;

public class NotaCreditoService extends CrudService {

    private static NotaCreditoService instance = null;
    
    private NotaCreditoService() {
        //--
    }
    
    public static NotaCreditoService getInstance() {
        if (instance == null) {
            instance = new NotaCreditoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return NotaCreditoDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) { }

	public boolean existeNotasCreditoParaCliente(String cdEmpresa, String cdRepresentante, String cdCliente) throws SQLException {
		if (LavenderePdaConfig.utilizaNotasCredito()) { 
			int qtd = countByExample(new NotaCredito(cdEmpresa, cdRepresentante, cdCliente));
			return qtd > 0;
		}
		return false;
	}

	public void atualizaNotaCreditoUtilizada(Vector notaCreditoPedidoList, String cdCliente, String flUtilizada) throws SQLException {
		int size = notaCreditoPedidoList.size();
		if (size == 0) return;
		
		NotaCreditoPedido notaCreditoPedido = (NotaCreditoPedido) notaCreditoPedidoList.items[0];
		NotaCredito notaCredito = new NotaCredito(notaCreditoPedido.cdEmpresa, notaCreditoPedido.cdRepresentante, cdCliente);
		for (int i = 0; i < size; i++) {
			notaCreditoPedido = (NotaCreditoPedido) notaCreditoPedidoList.items[i];
			notaCredito.cdNotaCreditoFilter.add(notaCreditoPedido.cdNotaCredito);
		}
		notaCredito.flUtilizada = flUtilizada;
		try {
			NotaCreditoDao.getInstance().atualizaNotaCreditoUtilizada(notaCredito);
		} catch (ApplicationException e) {
			ExceptionUtil.handle(e);
		}
	}

}