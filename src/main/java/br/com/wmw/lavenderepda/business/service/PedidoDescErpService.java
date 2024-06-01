package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoDesc;
import br.com.wmw.lavenderepda.business.domain.PedidoDescErp;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoDescErpDbxDao;
import totalcross.util.Vector;

public class PedidoDescErpService extends CrudService {

    private static PedidoDescErpService instance;
    
    private PedidoDescErpService() {
        //--
    }
    
    public static PedidoDescErpService getInstance() {
        if (instance == null) {
            instance = new PedidoDescErpService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return PedidoDescErpDbxDao.getInstance();
    }

	@Override
	public void validate(BaseDomain domain) throws java.sql.SQLException {
		//--
	}
	
	public int hasPedidoDescErp(PedidoDesc pedidoDesc) throws SQLException {
		if (pedidoDesc != null) {
			PedidoDescErp pedidoDescErpFilter = new PedidoDescErp();
			pedidoDescErpFilter.cdEmpresa = pedidoDesc.cdEmpresa;
			pedidoDescErpFilter.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_ERP;
			pedidoDescErpFilter.cdRepresentante = pedidoDesc.cdRepresentante;
			pedidoDescErpFilter.nuPedido = pedidoDesc.nuPedido;
			return countByExample(pedidoDescErpFilter);
		}
		return 0;
	}
	
	public Vector getPedidoDescErpList(Pedido pedido, String origemFilter) throws SQLException {
		if (pedido != null) {
			PedidoDescErp pedidoDescErpFilter = new PedidoDescErp();
			pedidoDescErpFilter.cdEmpresa = pedido.cdEmpresa;
			pedidoDescErpFilter.cdRepresentante = pedido.cdRepresentante;
			pedidoDescErpFilter.flOrigemPedido = origemFilter;
			pedidoDescErpFilter.nuPedido = pedido.nuPedido;
			pedidoDescErpFilter.sortAsc = ValueUtil.VALOR_SIM;
			pedidoDescErpFilter.sortAtributte = PedidoDesc.NMCOLUNA_NUSEQUENCIA;
			return findAllByExample(pedidoDescErpFilter);
		}
		return null;
	}
	
	public int getMaxNuSequenciaLiberacao(Pedido pedido) throws SQLException {
    	if (pedido != null) {
    		PedidoDescErp pedidoDescErpFilter = new PedidoDescErp();
    		pedidoDescErpFilter.cdEmpresa = pedido.cdEmpresa;
    		pedidoDescErpFilter.cdRepresentante = pedido.cdRepresentante; 
    		pedidoDescErpFilter.flOrigemPedido = pedido.flOrigemPedido;
    		pedidoDescErpFilter.nuPedido = pedido.nuPedido;
    		return findMaxKey(pedidoDescErpFilter, PedidoDesc.NMCOLUNA_NUSEQUENCIA);
    	}
    	return 0;
    }
	
	public void savePedidoDescErp(Pedido pedido, double vlPctDescontoLiberado, double vlDescontoLiberado, String flOrigemPedido) throws SQLException {
		if (pedido == null) return;
		
		PedidoDescErp pedidoDescErp = new PedidoDescErp();
		pedidoDescErp.cdEmpresa = pedido.cdEmpresa;
		pedidoDescErp.cdRepresentante = pedido.cdRepresentante;
		pedidoDescErp.flOrigemPedido = flOrigemPedido;
		pedidoDescErp.nuPedido = pedido.nuPedido;
		pedidoDescErp.cdUsuarioLiberacao = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		pedidoDescErp.nmUsuario = SessionLavenderePda.usuarioPdaRep.usuario.nmUsuario;
		pedidoDescErp.nuSequencia = SessionLavenderePda.nuOrdemLiberacaoUsuario;
		pedidoDescErp.vlPctDescontoLiberado = vlPctDescontoLiberado;
		pedidoDescErp.vlDescontoLiberado = vlDescontoLiberado;
		pedidoDescErp.dtLiberacao = DateUtil.getCurrentDate();
		pedidoDescErp.hrLiberacao = TimeUtil.getCurrentTimeHHMM();

		try {
			insert(pedidoDescErp);
		} catch (Throwable e) {
			update(pedidoDescErp);
		}
	}
	
    public void deleteCopiaPedidoDescErp(Pedido pedido) throws SQLException {
    	if (pedido != null) {
    		PedidoDescErp pedidoDescErpFilter = new PedidoDescErp();
			pedidoDescErpFilter.cdEmpresa = pedido.cdEmpresa;
			pedidoDescErpFilter.cdRepresentante = pedido.cdRepresentante;
			pedidoDescErpFilter.flOrigemPedido = pedido.flOrigemPedido;
			pedidoDescErpFilter.nuPedido = pedido.nuPedido;
			deleteAllByExample(pedidoDescErpFilter);
    	}
    }
	
}
