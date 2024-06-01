package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ComiRentabilidade;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ComiRentabilidadeDbxDao;
import totalcross.util.Vector;

public class ComiRentabilidadeService extends CrudService {

    private static ComiRentabilidadeService instance;
    
    private ComiRentabilidadeService() {
        //--
    }
    
    public static ComiRentabilidadeService getInstance() {
        if (instance == null) {
            instance = new ComiRentabilidadeService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ComiRentabilidadeDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    	//--
    }
    
    public ComiRentabilidade getComiRentabilidadeAtingida(String cdEmpresa, String cdRepresentante, double vlPctRentabilidade) throws SQLException {
    	ComiRentabilidade comiRentabilidade = new ComiRentabilidade();
    	if (ValueUtil.isNotEmpty(cdEmpresa) && ValueUtil.isNotEmpty(cdRepresentante)) {
    		Vector comiRentabilidadeList = findAllFaixasComiRentabilidade(cdEmpresa, cdRepresentante);
    		if (ValueUtil.isNotEmpty(comiRentabilidadeList)) {
    			for (int i = 0; i < comiRentabilidadeList.size(); i++) {
					ComiRentabilidade comiRentabilidadeTemp = (ComiRentabilidade) comiRentabilidadeList.items[i];
					if (vlPctRentabilidade >= comiRentabilidadeTemp.vlPctRentabilidade) {
						comiRentabilidade = comiRentabilidadeTemp;
					} else {
						break;
					}
				}
    		}
    	}
    	return comiRentabilidade;
    }
    
    public ComiRentabilidade getComiRentabilidadeMinimaAtingida(final ItemPedido itemPedido, final Vector comiRentabilidadeList) throws SQLException {
    	ComiRentabilidade comiRentabilidade = new ComiRentabilidade();
    	if (itemPedido != null) {
    		double vlPctRentabilidadeMinima = ItemPedidoService.getInstance().getVlPctRentabilidadeMinima(itemPedido);
    		if (ValueUtil.isNotEmpty(comiRentabilidadeList)) {
    			for (int i = 0; i < comiRentabilidadeList.size(); i++) {
					ComiRentabilidade comiRentabilidadeTemp = (ComiRentabilidade) comiRentabilidadeList.items[i];
					double vlPctRentabilidadeLiquida = comiRentabilidadeTemp.vlPctRentabilidade - comiRentabilidadeTemp.vlPctComissao;
					if (vlPctRentabilidadeMinima >= vlPctRentabilidadeLiquida) {
						comiRentabilidade = comiRentabilidadeTemp;
					} else {
						break;
					}
				}
    		}
    	}
    	return comiRentabilidade;
    }
    
    public Vector findAllFaixasComiRentabilidade(String cdEmpresa, String cdRepresentante) throws SQLException {
    	Vector comiRentabilidadeList = new Vector();
    	if (ValueUtil.isNotEmpty(cdEmpresa) && ValueUtil.isNotEmpty(cdRepresentante)) {
    		ComiRentabilidade comiRentabilidadeFilter = new ComiRentabilidade();
    		comiRentabilidadeFilter.cdEmpresa = cdEmpresa;
    		comiRentabilidadeFilter.cdRepresentante = cdRepresentante;
    		comiRentabilidadeFilter.sortAtributte = ComiRentabilidade.NOME_COLUNA_VLPCTRENTABILIDADE;
			comiRentabilidadeFilter.sortAsc = ValueUtil.VALOR_SIM;
    		comiRentabilidadeList = findAllByExample(comiRentabilidadeFilter);
    	}
    	return comiRentabilidadeList;
    }
    
    public String getEscalaFaixaByPctRentabilidadePedido(final Pedido pedido) throws SQLException {
    	if (pedido != null) {
    		Vector comiRentabilidadeList = findAllFaixasComiRentabilidade(pedido.cdEmpresa, pedido.cdRepresentante);
    		return getEscalaFaixaByPctRentabilidade(comiRentabilidadeList, getComiRentabilidadeAtingida(pedido.cdEmpresa, pedido.cdRepresentante, pedido.getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false)));
    	}
    	return ValueUtil.VALOR_NI;
    }
    
    public String getEscalaFaixaByPctRentabilidadeItemPedido(final ItemPedido itemPedido) throws SQLException {
    	if (itemPedido != null) {
    		Vector comiRentabilidadeList = findAllFaixasComiRentabilidade(itemPedido.cdEmpresa, itemPedido.cdRepresentante);
    		return getEscalaFaixaByPctRentabilidade(comiRentabilidadeList, getComiRentabilidadeAtingida(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false)));
    	}
    	return ValueUtil.VALOR_NI;
    }
    
    public String getEscalaFaixaByPctRentabilidadeMinima(final ItemPedido itemPedido) throws SQLException {
    	if (itemPedido != null) {
    		Vector comiRentabilidadeList = findAllFaixasComiRentabilidade(itemPedido.cdEmpresa, itemPedido.cdRepresentante);
    		return getEscalaFaixaByPctRentabilidade(comiRentabilidadeList, getComiRentabilidadeMinimaAtingida(itemPedido, comiRentabilidadeList));
    	}
    	return ValueUtil.VALOR_NI;
    }
    
    public String getEscalaFaixaByPctRentabilidade(final Vector comiRentabilidadeList, final ComiRentabilidade comiRentabilidade) {
		if (ValueUtil.isNotEmpty(comiRentabilidadeList)) {
			int size = comiRentabilidadeList.size();
			for (int i = 1; i <= size; i++) {
				ComiRentabilidade comiRentabilidadeTemp = (ComiRentabilidade)comiRentabilidadeList.items[i - 1];
				if (comiRentabilidade.vlPctRentabilidade == comiRentabilidadeTemp.vlPctRentabilidade) {
					return i + "/" + size;
				}
			}
		}
		return ValueUtil.VALOR_NI;
	}

}