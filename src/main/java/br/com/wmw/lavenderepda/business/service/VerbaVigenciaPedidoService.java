package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.VerbaSaldoVigencia;
import br.com.wmw.lavenderepda.business.domain.VerbaVigenciaPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VerbaVigenciaPedidoDbxDao;
import totalcross.util.Vector;

public class VerbaVigenciaPedidoService extends CrudService {

    private static VerbaVigenciaPedidoService instance;
    
    private VerbaVigenciaPedidoService() {
        //--
    }
    
    public static VerbaVigenciaPedidoService getInstance() {
        if (instance == null) {
            instance = new VerbaVigenciaPedidoService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return VerbaVigenciaPedidoDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
    }
    
    private VerbaVigenciaPedido getVerbaVigenciaFilter(String cdEmpresa, String cdRepresentante, String nuPedido) {
    	VerbaVigenciaPedido filter = new VerbaVigenciaPedido();
    	filter.cdEmpresa = cdEmpresa;
    	filter.cdRepresentante = cdRepresentante;
    	filter.nuPedido = nuPedido;
    	return filter;
    }
    
    public Vector getVerbaVigenciaItens(ItemPedido itemPedido) throws SQLException {
    	return findAllByExample(getVerbaVigenciaFilter(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.nuPedido));
    }
    
    public Vector getVerbaVigenciaItens(Pedido pedido) throws SQLException {
    	return findAllByExample(getVerbaVigenciaFilter(pedido.cdEmpresa, pedido.cdRepresentante, pedido.nuPedido));
    }
    
    public double getVlConsumoPedido(Pedido pedido, int cdMes) throws SQLException {
    	VerbaVigenciaPedido filter = getVerbaVigenciaFilter(pedido.cdEmpresa, pedido.cdRepresentante, pedido.nuPedido);
    	filter.cdMesSaldo = cdMes;
    	return ValueUtil.getDoubleValue(findColumnByRowKey(filter.getRowKey(), VerbaSaldoVigencia.NMCOLUNA_VLSALDO));
    }
    
    public void geraVerbaVigenciaPedido(Pedido pedido, double vlSaldo, int nuMes) throws SQLException {
    	VerbaVigenciaPedido verbaVigenciaPedido = getVerbaVigenciaFilter(pedido.cdEmpresa, pedido.cdRepresentante, pedido.nuPedido);
    	verbaVigenciaPedido.cdMesSaldo = nuMes;
    	verbaVigenciaPedido.vlSaldo = vlSaldo;
    	VerbaVigenciaPedido verbaItem = (VerbaVigenciaPedido) findByPrimaryKey(verbaVigenciaPedido);
    	if (verbaItem != null) {
    		update(verbaItem);
    	} else {
    		insert(verbaVigenciaPedido);
    	}
    }

}