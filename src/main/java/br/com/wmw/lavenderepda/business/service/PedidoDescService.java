package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoDesc;
import br.com.wmw.lavenderepda.business.domain.PedidoDescErp;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoDescDbxDao;
import br.com.wmw.lavenderepda.sync.SyncManager;
import br.com.wmw.lavenderepda.thread.EnviaDadosThread;
import totalcross.util.Vector;

public class PedidoDescService extends CrudService {

    private static PedidoDescService instance;
    
    private PedidoDescService() {
        //--
    }
    
    public static PedidoDescService getInstance() {
        if (instance == null) {
            instance = new PedidoDescService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return PedidoDescDbxDao.getInstance();
    }

	@Override
	public void validate(BaseDomain domain) throws java.sql.SQLException {
		//--
	}
	
	public void savePedidoDesc(Pedido pedido, double vlPctDescontoLiberado, double vlDescontoLiberado) throws SQLException {
		if (pedido == null) return; 
			
		PedidoDesc pedidoDesc = new PedidoDesc();
		pedidoDesc.cdEmpresa = pedido.cdEmpresa;
		pedidoDesc.cdRepresentante = pedido.cdRepresentante;
		pedidoDesc.flOrigemPedido = pedido.flOrigemPedido;
		pedidoDesc.nuPedido = pedido.nuPedido;
		pedidoDesc.cdUsuarioLiberacao = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		pedidoDesc.nmUsuario = SessionLavenderePda.usuarioPdaRep.usuario.nmUsuario;
		pedidoDesc.nuSequencia = SessionLavenderePda.nuOrdemLiberacaoUsuario;
		pedidoDesc.vlPctDescontoLiberado = vlPctDescontoLiberado;
		pedidoDesc.vlDescontoLiberado = vlDescontoLiberado;
		pedidoDesc.dtLiberacao = DateUtil.getCurrentDate();
		pedidoDesc.hrLiberacao = TimeUtil.getCurrentTimeHHMM();
		try {
			insert(pedidoDesc);
		} catch (Throwable e) {
			update(pedidoDesc);
		}
	}
	
	public void deletePedidoDesc(Pedido pedido) throws SQLException {
		if (pedido != null) {
			PedidoDesc pedidoDesc = getPedidoDesc(pedido);
			if (pedidoDesc != null) {
				delete(pedidoDesc);
			}
		}
	}
	
	protected PedidoDesc getPedidoDesc(Pedido pedido) throws SQLException {
		if (pedido != null) {
			PedidoDesc pedidoDescFilter = new PedidoDesc();
			pedidoDescFilter.cdEmpresa = pedido.cdEmpresa;
			pedidoDescFilter.cdRepresentante = pedido.cdRepresentante;
			pedidoDescFilter.flOrigemPedido = pedido.flOrigemPedido;
			if (pedido.isPedidoBonificacao()) {
				pedidoDescFilter.nuPedido = pedido.nuPedidoRelBonificacao;
			} else {
				pedidoDescFilter.nuPedido = pedido.nuPedido;
			}
			pedidoDescFilter.cdUsuarioLiberacao = SessionLavenderePda.usuarioPdaRep.cdUsuario;
			return (PedidoDesc) findByRowKey(pedidoDescFilter.getRowKey());
		}
		return null;
	}
	
	public void enviaPedidoDescBackground(String cdSessao, Pedido pedido) throws SQLException {
		PedidoDesc pedidoDesc = getPedidoDesc(pedido);
		if (pedidoDesc != null) {
			EnviaDadosThread.getInstance().envioPedidoDescBackground(cdSessao, pedidoDesc);
		}
	}
	
	public double getVlDescontoLiberado(Pedido pedido) throws SQLException {
		if (pedido != null) {
			PedidoDesc pedidoDesc = getPedidoDesc(pedido);
			if (pedidoDesc != null) {
				return pedidoDesc.vlDescontoLiberado;
			} else if (pedido.isPedidoBonificacao()) {
				return pedido.vlTotalBrutoItens;
			} else {
				return pedido.vlDesconto;
			}
		}
		return 0d;
	}

	public double getVlPctDescontoLiberacaoRestante(Pedido pedido) throws SQLException {
    	if (pedido != null) {
    		double vlTotalPedidoLiberadoAtual = getVlTotalPedidoLiberadoAtual(pedido);
    		if (getVlTotalPedidoForLiberacao(pedido) < vlTotalPedidoLiberadoAtual && vlTotalPedidoLiberadoAtual > 0.0) {
    			return ValueUtil.round((1 - (pedido.getVlTotalBrutoItensComDesconto() / vlTotalPedidoLiberadoAtual)) * 100); 
    		}
    	}
    	return 0d;
    }
	
	public double getVlTotalPedidoLiberadoAtual(Pedido pedido) throws SQLException {
    	if (pedido != null) {
    		double vlTotalPedidoLiberadoAtual = pedido.vlTotalBrutoItens;
    		Vector pedidoDescErpList = PedidoDescErpService.getInstance().getPedidoDescErpList(pedido, OrigemPedido.FLORIGEMPEDIDO_PDA);
    		if (ValueUtil.isNotEmpty(pedidoDescErpList)) {
    			for (int i = 0; i < pedidoDescErpList.size(); i++) {
					PedidoDescErp pedidoDescErp = (PedidoDescErp) pedidoDescErpList.items[i];
					if (pedidoDescErp.vlPctDescontoLiberado > 0) {
						vlTotalPedidoLiberadoAtual -= pedidoDescErp.vlDescontoLiberado;
					}
				}
    		}
			return vlTotalPedidoLiberadoAtual;
		}
    	return 0d;
    }
	
	private double getVlTotalPedidoForLiberacao(Pedido pedido) throws SQLException {
		if (PedidoService.getInstance().isPedidoComBonificacaoRelacionada(pedido)) {
			Pedido pedidoBonificacao = PedidoService.getInstance().getPedidoRelBonificacao(pedido);
			return pedido.vlTotalPedido - (pedidoBonificacao != null ? pedidoBonificacao.vlTotalPedido : pedido.vlTotalPedido);
		}
		return pedido.vlTotalPedido;
	}
	
	public void enviaPedidoDescServidor(Pedido pedido) throws Exception {
		if (pedido != null) {
			if (LavenderePdaConfig.usaEnvioPedidoBackground) {
				enviaPedidoDescBackground(PedidoService.getInstance().generateIdGlobal(), pedido);
			} else {
				SyncManager.envieDados(HttpConnectionManager.getDefaultParamsSync(), SyncManager.getInfoAtualizacaoPedidoDesc());
			}
		}
	}
	
	public void updateInfoAposEnvioPedidoDesc(Vector pedidoDescList) throws SQLException {
		if (ValueUtil.isNotEmpty(pedidoDescList)) {
			for (int i = 0; i < pedidoDescList.size(); i++) {
				PedidoDesc pedidoDesc = (PedidoDesc) pedidoDescList.items[i];
				Pedido pedidoFilter = new Pedido();
				pedidoFilter.cdEmpresa = pedidoDesc.cdEmpresa;
				pedidoFilter.cdRepresentante = pedidoDesc.cdRepresentante;
				pedidoFilter.nuPedido = pedidoDesc.nuPedido;
				if (PedidoDescErpService.getInstance().hasPedidoDescErp(pedidoDesc) > 0) {
					pedidoFilter.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_ERP;
				} else {
					pedidoFilter.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
				}
				Pedido pedido = (Pedido) PedidoService.getInstance().findByRowKey(pedidoFilter.getRowKey());
				if (pedido != null) {
					pedido.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
					UsuarioDescService.getInstance().adicionaVlTotalPedidoUsuarioDescErp(pedido);
					UsuarioDescService.getInstance().recalculaAndUpdateUsuarioDescPda();
				}
			}
		}
	}
	
	
}
