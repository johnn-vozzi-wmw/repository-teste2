package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.DescQuantidadePeso;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescQuantidadePesoDbxDao;
import totalcross.util.Vector;

public class DescQuantidadePesoService extends CrudService {

    private static DescQuantidadePesoService instance = null;
    
    private DescQuantidadePesoService() {
        //--
    }
    
    public static DescQuantidadePesoService getInstance() {
        if (instance == null) {
            instance = new DescQuantidadePesoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return DescQuantidadePesoDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {
    }
    
    private DescQuantidadePeso getNewDescQuantidadePeso(String cdEmpresa) {
    	DescQuantidadePeso descQuantidadePeso = new DescQuantidadePeso();
		descQuantidadePeso.cdEmpresa = cdEmpresa;
		descQuantidadePeso.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		return descQuantidadePeso;
    }

	public void loadFaixasDescPedido(Pedido pedido) throws SQLException {
		DescQuantidadePeso descQuantidadePesoFilter = getNewDescQuantidadePeso(pedido.cdEmpresa);
		descQuantidadePesoFilter.cdTabelaPreco = pedido.cdTabelaPreco;
		pedido.descQuantidadePesoList = findAllByExample(descQuantidadePesoFilter);
	}
	
	public void loadFaixasDescItemPedidoTabelaPreco(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		DescQuantidadePeso descQuantidadePesoFilter = getNewDescQuantidadePeso(pedido.cdEmpresa);
		descQuantidadePesoFilter.cdTabelaPreco = itemPedido.cdTabelaPreco;
		pedido.descQuantidadePesoList = findAllByExample(descQuantidadePesoFilter);
		if (pedido.descQuantidadePesoList.size() == 0) {
			descQuantidadePesoFilter.cdTabelaPreco = ValueUtil.VALOR_ZERO;
			pedido.descQuantidadePesoList = findAllByExample(descQuantidadePesoFilter);
		}
		if (pedido.descQuantidadePeso != null) {
			pedido.descQuantidadePeso.vlPctDesconto = 0d;
		}
	}

	public DescQuantidadePeso getDescQuantidadePesoPedido(Vector descQuantidadePesoList, double qtPesoPedido) {
		DescQuantidadePeso descQuantidadePeso = null;
		for (int i = 0; i < descQuantidadePesoList.size(); i++) {
			DescQuantidadePeso descQtdPeso = (DescQuantidadePeso) descQuantidadePesoList.items[i];
			if (qtPesoPedido >= descQtdPeso.vlPeso) {
				descQuantidadePeso = descQtdPeso;
			}
		}
		return descQuantidadePeso;
	}
	
	public DescQuantidadePeso findByExample(DescQuantidadePeso descQuantidadePeso) throws SQLException {
		return DescQuantidadePesoDbxDao.getInstance().findByExample(descQuantidadePeso);
	}
	
	public double findVlPctDesconto(String cdEmpresa, double vlPeso) throws SQLException {
		DescQuantidadePeso descQuantidadePesoFilter = getNewDescQuantidadePeso(cdEmpresa);
		descQuantidadePesoFilter.vlPeso = vlPeso;
		return DescQuantidadePesoDbxDao.getInstance().findVlPctDesconto(descQuantidadePesoFilter);
	}

	public Vector loadTabelaPrecoDescQuantidadePeso() throws SQLException {
		return DescQuantidadePesoDbxDao.getInstance().loadTabelaPrecoDescQuantidadePeso();
	}
	
}
