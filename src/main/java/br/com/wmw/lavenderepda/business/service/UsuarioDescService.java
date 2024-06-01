package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.UsuarioDesc;
import br.com.wmw.lavenderepda.integration.dao.pdbx.UsuarioDescDbxDao;
import totalcross.util.Vector;

public class UsuarioDescService extends CrudService {

    private static UsuarioDescService instance;
    
    private UsuarioDescService() {
        //--
    }
    
    public static UsuarioDescService getInstance() {
        if (instance == null) {
            instance = new UsuarioDescService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return UsuarioDescDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    	//--
    }
    
    public double getVlPctMaxDescontoUsuario(boolean alcada, final boolean isValidandoDesconto) throws SQLException {
    	if (!LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacaoMaxDescUsuarioWorkflow() || !isValidandoDesconto) {
    		return ValueUtil.round(ValueUtil.getDoubleValue(findColumnByRowKey(getUsuarioDescFilter(UsuarioDesc.USUARIODESC_ERP).getRowKey(), alcada ? UsuarioDesc.NMCOLUNA_VLPCTFIMALCADA : UsuarioDesc.NMCOLUNA_VLPCTMAXDESCONTO)));
    	}
    	UsuarioDesc usuarioMaxDesc = getUsuarioDesc(UsuarioDesc.USUARIODESC_ERP);
    	return (usuarioMaxDesc != null) ? usuarioMaxDesc.vlPctMaxDescUsu : 0d;
    }

    public int getNuOrdemLiberacao() throws SQLException {
    	return ValueUtil.getIntegerValue(findColumnByRowKey(getUsuarioDescFilter(UsuarioDesc.USUARIODESC_ERP).getRowKey(), UsuarioDesc.NMCOLUNA_NUORDEMLIBERACAO));
    }
    
    public boolean isProximoUsuarioLiberarPedido(Pedido pedido) throws SQLException {
    	if (pedido != null) {
    		if (SessionLavenderePda.nuOrdemLiberacaoUsuario == 0) return false;
    		int nuMaxSequencia = PedidoDescErpService.getInstance().getMaxNuSequenciaLiberacao(pedido);
    		boolean proximoUsuarioLiberarPedido = nuMaxSequencia != 0 && nuMaxSequencia + 1 == SessionLavenderePda.nuOrdemLiberacaoUsuario;
    		if (LavenderePdaConfig.isUsaMultiplasLiberacoesParaPedidoPendente() || LavenderePdaConfig.isUsaMotivoPendencia() || LavenderePdaConfig.isUsaMultiplasLiberacoesRespeitandoHierarquiaPercentualDesconto()) {
    			proximoUsuarioLiberarPedido = SessionLavenderePda.nuOrdemLiberacaoUsuario - nuMaxSequencia == 1;
    			if (LavenderePdaConfig.isUsaMotivoPendencia() && !proximoUsuarioLiberarPedido) {
    				proximoUsuarioLiberarPedido = ItemPedidoService.getInstance().permiteLiberarPedido(pedido, nuMaxSequencia);
    			}
    		}
    		return proximoUsuarioLiberarPedido || (LavenderePdaConfig.permiteLiberacaoPedidoPendenteOutraOrdemLiberacao && UsuarioConfigService.getInstance().isLiberaPedidoOutraOrdem() && nuMaxSequencia + 1 < SessionLavenderePda.nuOrdemLiberacaoUsuario);
    	}
    	return false;
    }
    
    public double getVlPctMaxDescontoPonderadoUsuario() throws SQLException {
    	return ValueUtil.getDoubleValue(findColumnByRowKey(getUsuarioDescFilter(UsuarioDesc.USUARIODESC_ERP).getRowKey(), UsuarioDesc.NMCOLUNA_VLPCTMAXDESCPONDERADO));
    }
    
    public double getVlPctMedioDescontoPonderadoComPedidoAtual(Pedido pedido) throws SQLException {
    	double vlPctMediaDescontos = 0;
    	UsuarioDesc usuarioDescFilter = new UsuarioDesc();
    	usuarioDescFilter.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
    	Vector usuarioDescList = findAllByExample(usuarioDescFilter);
    	if (ValueUtil.isNotEmpty(usuarioDescList)) {
    		double vlTotalFaturado = 0;
    		double vlTotalDesconto = 0;
    		for (int i = 0; i < usuarioDescList.size(); i++) {
				UsuarioDesc usuarioDesc = (UsuarioDesc) usuarioDescList.items[i];
				vlTotalFaturado += usuarioDesc.vlTotalFaturado;
				vlTotalDesconto += usuarioDesc.vlTotalDesconto;
			}
    		if (pedido != null) {
    			vlTotalFaturado += pedido.vlTotalBrutoItens; 
    			vlTotalDesconto += pedido.vlDesconto;
    		}
    		try {
    			vlPctMediaDescontos = ValueUtil.round((vlTotalDesconto / vlTotalFaturado) * 100);
    		} catch (ApplicationException ex) {
    			return 0;
    		}
    	}
    	return vlPctMediaDescontos;
    }
    
    public double getMaxVlPctDescontoPonderadoLiberado(Pedido pedido) throws SQLException {
    	double maxVlPctDescontoPonderadoLiberado = 0;
    	if (pedido != null) {
    		double vlPctMediaDescontos = 0;
    		double vlTotalDesconto = 0;
    		double vlTotalDescontoAtual = 0;
    		UsuarioDesc usuarioDescFilter = new UsuarioDesc();
        	usuarioDescFilter.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
        	Vector usuarioDescList = findAllByExample(usuarioDescFilter);
        	if (ValueUtil.isNotEmpty(usuarioDescList)) {
        		double vlTotalFaturado = 0;
        		for (int i = 0; i < usuarioDescList.size(); i++) {
    				UsuarioDesc usuarioDesc = (UsuarioDesc) usuarioDescList.items[i];
    				vlTotalFaturado += usuarioDesc.vlTotalFaturado;
    				vlTotalDesconto += usuarioDesc.vlTotalDesconto;
    				vlTotalDescontoAtual += usuarioDesc.vlTotalDesconto;
    			}
    			vlTotalFaturado += pedido.vlTotalBrutoItens; 
    			vlTotalDesconto += pedido.vlDesconto;
        		if (vlTotalFaturado > 0) {
        			vlPctMediaDescontos = ValueUtil.round((vlTotalDesconto / vlTotalFaturado) * 100);
        		}
        	}
        	double vlPctMaxDescontoPonderadoUsuario =  getVlPctMaxDescontoPonderadoUsuario();
        	try {
	        	double vlMaxDescontoPonderado = ValueUtil.round((vlTotalDesconto * vlPctMaxDescontoPonderadoUsuario) / vlPctMediaDescontos);
	    		double vlMaxDescontoPedidoAceita = vlMaxDescontoPonderado - vlTotalDescontoAtual;
	    		if (vlMaxDescontoPedidoAceita > 0) {
	    			maxVlPctDescontoPonderadoLiberado = ValueUtil.round((vlMaxDescontoPedidoAceita * pedido.vlPctDesconto) / pedido.vlDesconto);
	    		}
        	} catch (ApplicationException ex) {
        		return 0;
        	}
		}
    	return maxVlPctDescontoPonderadoLiberado;
    }
    
    public void adicionaVlTotalPedidoUsuarioDescPda(Pedido pedido) throws SQLException {
    	recalculaAndUpdateUsuarioDescPda();
    }
    
    public void adicionaVlTotalPedidoUsuarioDescErp(Pedido pedido) throws SQLException {
    	if (!pedido.isPedidoBonificacao() || LavenderePdaConfig.isUsaMotivoPendencia()) {
    		adicionaVlTotalPedidoUsuarioDescOrigem(pedido, UsuarioDesc.USUARIODESC_ERP);
		}
    }
    
    private void adicionaVlTotalPedidoUsuarioDescOrigem(Pedido pedido, String flOrigemDesconto) throws SQLException {
    	if (pedido == null) return;
		double vlDescontoLiberado = PedidoDescService.getInstance().getVlDescontoLiberado(pedido);
		UsuarioDesc usuarioDesc = getUsuarioDesc(flOrigemDesconto);
		if (usuarioDesc != null) {
			if (pedido.isPedidoBonificacao()) {
				usuarioDesc.vlTotalDesconto += vlDescontoLiberado;
			} else {
				usuarioDesc.vlTotalFaturado += pedido.vlTotalBrutoItens;
				usuarioDesc.vlTotalDesconto += vlDescontoLiberado;
			}
			update(usuarioDesc);
		} else {
			usuarioDesc = new UsuarioDesc();
			usuarioDesc.flOrigemDesconto = flOrigemDesconto;
			usuarioDesc.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
			if (pedido.isPedidoBonificacao()) {
				usuarioDesc.vlTotalDesconto += vlDescontoLiberado;
			} else {
				usuarioDesc.vlTotalFaturado = pedido.vlTotalBrutoItens;
				usuarioDesc.vlTotalDesconto = vlDescontoLiberado;
			}
			insert(usuarioDesc);
		}
    }
    
    public void retiraVlTotalPedidoUsuarioDescPda(Pedido pedido) throws SQLException {
    	if (pedido == null) return;
		double vlDescontoLiberado = PedidoDescService.getInstance().getVlDescontoLiberado(pedido);
		UsuarioDesc usuarioDesc = getUsuarioDesc(UsuarioDesc.USUARIODESC_PDA);
		if (usuarioDesc == null) return;
		usuarioDesc.vlTotalFaturado -= pedido.vlTotalBrutoItens;
		usuarioDesc.vlTotalDesconto -= vlDescontoLiberado;
		update(usuarioDesc);
    }
    
    public void recalculaAndUpdateUsuarioDescPda() throws SQLException {
    	UsuarioDesc usuarioDescPda = getUsuarioDesc(UsuarioDesc.USUARIODESC_PDA);
    	if (usuarioDescPda != null) {
    		deleteAllByExample(usuarioDescPda);
    	}
    	Pedido pedidoFilter = new Pedido();
    	pedidoFilter.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoFechado;
    	Vector pedidoList = PedidoService.getInstance().findAllByExample(pedidoFilter);
    	if (ValueUtil.isNotEmpty(pedidoList)) {
    		for (int i = 0; i < pedidoList.size(); i++) {
    			Pedido pedido = (Pedido) pedidoList.items[i];
    			if (!pedido.isPedidoBonificacao() || LavenderePdaConfig.isUsaMotivoPendencia() || (PedidoDescService.getInstance().getPedidoDesc(pedido) == null && (!pedido.getTipoPedido().isFlTipoCreditoFrete() && !pedido.getTipoPedido().isFlTipoCreditoCondicao()))) {
    				adicionaVlTotalPedidoUsuarioDescOrigem(pedido, UsuarioDesc.USUARIODESC_PDA);
    			}
			}
    	}
    }
    
    public UsuarioDesc getUsuarioDesc(String flOrigemDesconto) throws SQLException {
    	return (UsuarioDesc) findByRowKey(getUsuarioDescFilter(flOrigemDesconto).getRowKey());
    }
    
    public boolean isUsuarioNaFaixaDescontoLiberacaoAlcada(double vlFaixa) throws SQLException {
    	UsuarioDesc usuarioDescFilter = getUsuarioDescFilter(UsuarioDesc.USUARIODESC_ERP);
    	usuarioDescFilter.vlPctFimAlcada = vlFaixa;
    	return UsuarioDescDbxDao.getInstance().isUsuarioLiberacaoAlcada(usuarioDescFilter);
    }

	private UsuarioDesc getUsuarioDescFilter(final String flOrigemDesconto) {
		UsuarioDesc usuarioDescFilter = new UsuarioDesc();
    	usuarioDescFilter.flOrigemDesconto = UsuarioDesc.USUARIODESC_ERP;
    	usuarioDescFilter.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		return usuarioDescFilter;
	}
    
}