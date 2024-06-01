package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.MotCancelPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MotCancelPedidoDbxDao;
import totalcross.util.Vector;

public class MotCancelPedidoService extends CrudService {

    private static MotCancelPedidoService instance;
    
    private MotCancelPedidoService() {
        //--
    }
    
    public static MotCancelPedidoService getInstance() {
        if (instance == null) {
            instance = new MotCancelPedidoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return MotCancelPedidoDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException { }
    
    public String findCdMotCancelPedidoDefault() throws SQLException {
    	MotCancelPedido motCancelPedido = new MotCancelPedido();
    	motCancelPedido.flCancelamentoAuto = ValueUtil.VALOR_SIM;
    	Vector motCancelPedidoList = findAllByExample(motCancelPedido);
    	return ValueUtil.isNotEmpty(motCancelPedidoList) ? ((MotCancelPedido) motCancelPedidoList.items[0]).cdMotivoCancelamento : null;
    }

}