package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ConfigValorMinimoDao;
import totalcross.util.Vector;

import java.sql.SQLException;

public class ConfigValorMinimoService extends CrudService {

    private static ConfigValorMinimoService instance;
    
    private ConfigValorMinimoService() {
        //--
    }
    
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException { }
    
    public static ConfigValorMinimoService getInstance() {
        if (instance == null) {
            instance = new ConfigValorMinimoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ConfigValorMinimoDao.getInstance();
    }
    
    public double findVlMinimoParcelaPedido(Pedido pedido) throws SQLException {
    	if (!LavenderePdaConfig.usaTabelaConfigMinParcela() || (LavenderePdaConfig.usaTabelaConfigMinParcela() && pedido.getTipoPedido() != null && pedido.getTipoPedido().isIgnoraVlMinimoParcela())) return 0;
    	
    	return findMaiorValorMinimo(pedido, "VLMINPARCELA");
    }
    
    public double findVlMinimoValorPedido(Pedido pedido) throws SQLException {
    	if (!LavenderePdaConfig.usaTabelaConfigMinPedido() || (LavenderePdaConfig.usaTabelaConfigMinPedido() && pedido.getTipoPedido() != null && pedido.getTipoPedido().isIgnoraVlMinPedido())) return 0;
    	
    	return findMaiorValorMinimo(pedido, "VLMINPEDIDO");
    }
    
    private double findMaiorValorMinimo(Pedido pedido, String nmCampo) throws SQLException {
    	ConfigValorMinimoDao.getInstance().setColumn(nmCampo);
    	Vector configValorMinimoList  = findConfigValorMinimoList(pedido);
    	int size = configValorMinimoList.size();
    	double vlMaiorValorMinimo = 0;
    	for (int i = 0; i < size; i++) {
    		double vlMinimo = (double) configValorMinimoList.items[i];
    		if (vlMaiorValorMinimo >= vlMinimo) continue;
			
    		vlMaiorValorMinimo = vlMinimo;
    	}
    	return vlMaiorValorMinimo;
    }
    
    private Vector findConfigValorMinimoList(Pedido pedido) throws SQLException {
    	String cdGrupoValorMinPedido = ClienteService.getInstance().findColumnByRowKey(pedido.getCliente().getRowKey(), "CDGRUPOVALORMINPEDIDO");
    	
    	Vector cdLinhaProdutoList = pedido.getCdLinhaProdutoList();
    	Vector configValorMinimoList = new Vector();
    	int size = cdLinhaProdutoList.size();
    	for (int i = 0; i < size; i++) {
    		String cdLinha = (String) cdLinhaProdutoList.items[i];
    		double configValorMinimo = ConfigValorMinimoDao.getInstance().findConfigValorMinimo(pedido, cdGrupoValorMinPedido, cdLinha);
    		configValorMinimoList.addElement(configValorMinimo);
		}
    	return configValorMinimoList;
    }

	public boolean isUsaFlAgrupaLinhaProdutos(Pedido pedido) throws SQLException {
		return ConfigValorMinimoDao.getInstance().isPossuiLinhaUsaFlAgrupaLinhaProdutos(pedido);
	}

	public double findValorMinimoPedidoPorLinha(Pedido pedido, String cdLinha) throws SQLException {
		String cdGrupoValorMinPedido = ClienteService.getInstance().findColumnByRowKey(pedido.getCliente().getRowKey(), "CDGRUPOVALORMINPEDIDO");
		ConfigValorMinimoDao.getInstance().setColumn("VLMINPEDIDO");
		return ConfigValorMinimoDao.getInstance().findConfigValorMinimo(pedido, cdGrupoValorMinPedido, cdLinha);
	}

}