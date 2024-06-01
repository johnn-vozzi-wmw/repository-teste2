package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.HashMap;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.DomainUtil;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.VariavelCalculo;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VariavelCalculoDbxDao;
import totalcross.util.Vector;

public class VariavelCalculoService extends CrudService {

    private static VariavelCalculoService instance;
    
    private VariavelCalculoService() { }
    
    public static VariavelCalculoService getInstance() {
        if (instance == null) {
            instance = new VariavelCalculoService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return VariavelCalculoDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {  }
    
    public HashMap<String, Object> getHashVariavelCalculo(VariavelCalculo variavelCalculo) throws SQLException {
    	HashMap<String, Object> hash = new HashMap<>();
    	Vector results = VariavelCalculoDbxDao.getInstance().findAllByExample(variavelCalculo);
    	if (ValueUtil.isEmpty(results)) return hash;
    	for (int i = 0; i < results.size(); i++) {
    		VariavelCalculo variavel = (VariavelCalculo) results.items[i];
    		hash.put(variavel.dsVariavel, variavel.getValorVariavelCalculo());
    	}
    	return hash;
    }

    public HashMap<String, Object> getHashVariavelCalculoItemPedido(Pedido pedido, ItemPedido itemPedido) throws SQLException {
    	HashMap<String, Object> hash = new HashMap<>();
    	if (itemPedido != null && pedido != null) {
			hash.putAll(DomainUtil.getProperties(pedido, true));
			hash.putAll(DomainUtil.getProperties(pedido.getCondicaoPagamento(), true));
			hash.putAll(DomainUtil.getProperties(itemPedido, true));
			hash.putAll(getHashVariavelCalculo(new VariavelCalculo(itemPedido.cdEmpresa, itemPedido.cdRepresentante, VariavelCalculo.ENTIDADE_EMPRESA, null, null)));
			hash.putAll(getHashVariavelCalculo(new VariavelCalculo(itemPedido.cdEmpresa, itemPedido.cdRepresentante, VariavelCalculo.ENTIDADE_CLIENTE, itemPedido.cdCliente, null)));
			hash.putAll(getHashVariavelCalculo(new VariavelCalculo(itemPedido.cdEmpresa, itemPedido.cdRepresentante, VariavelCalculo.ENTIDADE_PRODUTO, null, itemPedido.cdProduto)));
		}
		return hash;
	}

    public HashMap<String, Object> getHashVariavelCalculoPedido(Pedido pedido) throws SQLException {
    	HashMap<String, Object> hash = new HashMap<>();
    	//Variaveis da condição de pagamento
		hash.putAll(DomainUtil.getProperties(pedido.getCondicaoPagamento(), true));
		//Variaveis de pedido
		hash.putAll(DomainUtil.getProperties(pedido, true));
		hash.putAll(getHashVariavelCalculo(new VariavelCalculo(pedido.cdEmpresa, pedido.cdRepresentante, VariavelCalculo.ENTIDADE_EMPRESA, null, null)));
		hash.putAll(getHashVariavelCalculo(new VariavelCalculo(pedido.cdEmpresa, pedido.cdRepresentante, VariavelCalculo.ENTIDADE_CLIENTE, pedido.cdCliente, null)));
		return hash;
	}
 
	public void populateItemPedidoWithHashValues(Pedido pedido, ItemPedido itemPedido, HashMap<String, Object> variaveisToSave) throws SQLException, ValidationException {
    	if (ValueUtil.isEmpty(variaveisToSave)) return;
    	
    	for (String key : variaveisToSave.keySet()) {
    		if (key.startsWith("Pedido.") && pedido != null) {
    			DomainUtil.set(pedido, key.substring(7), variaveisToSave.get(key));
    		} else if (key.startsWith("ItemPedido.") && itemPedido != null) {
    			DomainUtil.set(itemPedido, key.substring(11), variaveisToSave.get(key));
    		}
		}
    }
	
	public void populatePedidoWithHashValues(Pedido pedido, HashMap<String, Object> variaveisToSave) throws SQLException, ValidationException {
    	if (ValueUtil.isEmpty(variaveisToSave) || pedido == null) return;
    	
    	for (String key : variaveisToSave.keySet()) {
    		if (key.startsWith("Pedido.")) {
    			DomainUtil.set(pedido, key.substring(7), variaveisToSave.get(key));
    		}
		}
    }
}