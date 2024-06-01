package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.PoliticaComercialFaixa;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PoliticaComercialFaixaDbxDao;

public class PoliticaComercialFaixaService extends CrudService {

    private static PoliticaComercialFaixaService instance;
    
    private Map<String, PoliticaComercialFaixa> hashCachePolitica = new HashMap<String, PoliticaComercialFaixa>();
    
    private PoliticaComercialFaixaService() {
        //--
    }
    
    public static PoliticaComercialFaixaService getInstance() {
        if (instance == null) {
            instance = new PoliticaComercialFaixaService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return PoliticaComercialFaixaDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) { }

	private boolean skipSearchPoliticaComercialFaixa(ItemPedido itemPedido) {
		return itemPedido == null || ValueUtil.isEmpty(itemPedido.cdEmpresa) || ValueUtil.isEmpty(itemPedido.cdPoliticaComercial);
	}
    
    public double findPctComissaoByPoliticaComercialItemPedido(ItemPedido itemPedido) throws SQLException {
		if (skipSearchPoliticaComercialFaixa(itemPedido)) return 0;
		return PoliticaComercialFaixaDbxDao.getInstance().findPctComissaoByPoliticaComercialItemPedido(new PoliticaComercialFaixa(itemPedido));
    }

	public PoliticaComercialFaixa findByItemPedido(ItemPedido itemPedido) throws SQLException {
		if (skipSearchPoliticaComercialFaixa(itemPedido)) return null;
		return PoliticaComercialFaixaDbxDao.getInstance().findPoliticaComercialFaixaFromItemPedido(new PoliticaComercialFaixa(itemPedido));
    }

	public PoliticaComercialFaixa findPoliticaComercialFaixaByPoliticaComercial(ItemPedido itemPedido) throws SQLException {
	    if (skipSearchPoliticaComercialFaixa(itemPedido)) return null;
	    double descontoAcrescimo = (itemPedido.vlPctDesconto + itemPedido.vlPctFaixaDescQtd) - itemPedido.vlPctAcrescimo;
    	PoliticaComercialFaixa domain = new PoliticaComercialFaixa(itemPedido.cdEmpresa, itemPedido.cdPoliticaComercial, descontoAcrescimo);
    	if (hashCachePolitica.containsKey(domain.getRowKey())) return hashCachePolitica.get(domain.getRowKey());
		PoliticaComercialFaixa faixa = PoliticaComercialFaixaDbxDao.getInstance().findPoliticaComercialFaixa(domain);
		hashCachePolitica.put(domain.getRowKey(), faixa);
		return faixa;
	}
	
	public PoliticaComercialFaixa findPoliticaComercialFaixaByltemPedido(ItemPedido itemPedido) throws SQLException {
	    if (skipSearchPoliticaComercialFaixa(itemPedido)) return null;
	    double descontoAcrescimo = (itemPedido.vlPctDesconto + itemPedido.vlPctFaixaDescQtd) - itemPedido.vlPctAcrescimo;
    	PoliticaComercialFaixa domain = new PoliticaComercialFaixa(itemPedido.cdEmpresa, itemPedido.cdPoliticaComercial, descontoAcrescimo);
		PoliticaComercialFaixa faixa = PoliticaComercialFaixaDbxDao.getInstance().findPoliticaComercialFaixa(domain);
		return faixa;
	}
    
}