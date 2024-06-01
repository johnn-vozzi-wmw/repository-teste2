package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.FreteConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FreteConfigPdbxDao;
import totalcross.util.Vector;

public class FreteConfigService extends CrudService {

    private static FreteConfigService instance;

    private FreteConfigService() {}
    
    @Override
    public void validate(BaseDomain domain) throws SQLException {}

    public static FreteConfigService getInstance() {
    	return (instance == null) ? instance = new FreteConfigService() : instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return FreteConfigPdbxDao.getInstance();
    }
    
    public Vector buscaFreteConfigListByPedido(final Pedido pedido) throws SQLException {
    	return buscaFreteConfigListByPedido(pedido, null);
    }
    
    public Vector buscaFreteConfigListByPedido(final Pedido pedido, BaseDomain orderBy) throws SQLException {
    	if (pedido == null) return null;
    	FreteConfig filter = new FreteConfig(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdTransportadora);
    	filter.cdTipoFrete = pedido.cdTipoFrete;
    	Cliente cliente = pedido.getCliente();
		filter.cdRegiao = cliente.cdRegiao;
		String cepTratado = cliente.dsCepComercial.replace("-", "");
		filter.nuCepInicio = ValueUtil.getIntegerValue(cepTratado);
		filter.nuCepFim = ValueUtil.getIntegerValue(cepTratado);
		if (orderBy != null) {
			filter.sortAsc = orderBy.sortAsc;
			filter.sortAtributte = orderBy.sortAtributte;
		}
		Vector listFreteConfig = findAllByExample(filter);
		listFreteConfig = filterListByCliente(listFreteConfig, pedido.getCliente());
    	int size = listFreteConfig.size();
    	for (int i = 0; i < size; i++) {
			FreteConfig freteConfig = (FreteConfig) listFreteConfig.items[i];
			freteConfig.listFreteCalculo = FreteCalculoService.getInstance().findAllFreteCalculoByFreteConfig(freteConfig);
			FreteCalculoService.getInstance().calculaFreteCalculoList(pedido, freteConfig, freteConfig.listFreteCalculo);
		}
    	if (orderBy.sortAtributte.equalsIgnoreCase("vlPrecoFreteCalculado")) {
			SortUtil.qsortDouble(listFreteConfig.items, 0, listFreteConfig.size()-1, ValueUtil.getBooleanValue(filter.sortAsc));
		}
    	return listFreteConfig;
    }
    
    public FreteConfig findFreteConfigByPrimaryKey(FreteConfig domain, boolean loadFreteCalculo) throws SQLException {
    	FreteConfig result = (FreteConfig) findByPrimaryKey(domain);
    	if (loadFreteCalculo && result != null) {
    		result.listFreteCalculo = FreteCalculoService.getInstance().findAllFreteCalculoByFreteConfig(result);
    	}
    	return result;
    }

	private Vector filterListByCliente(final Vector listFrete, final Cliente cliente) {
		Vector list = new Vector();
		if (cliente == null) return list;
		int size = listFrete.size();
		for (int i = 0; i < size; i++) {
			FreteConfig freteConfig = (FreteConfig) listFrete.items[i];
			if (freteConfig.isTipoTabFreteCep() && cliente.dsCepComercial != null) {
				int cep = ValueUtil.getIntegerValue(cliente.dsCepComercial.replace("-", ""));
				if (cep >= freteConfig.nuCepInicio && cep <= freteConfig.nuCepFim) list.addElement(freteConfig);
			} else if (freteConfig.isTipoTabFreteRegiao()) {
				if (freteConfig.cdRegiao.equals(cliente.cdRegiao)) list.addElement(freteConfig);
			}
		}
		return list;
	}
    
}