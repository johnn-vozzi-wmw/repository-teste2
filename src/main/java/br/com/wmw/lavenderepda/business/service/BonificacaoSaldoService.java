package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.BonificacaoSaldo;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.validation.ValidationBonificacaoSaldoException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.BonificacaoSaldoDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import totalcross.util.Vector;

public class BonificacaoSaldoService extends CrudService {

    private static BonificacaoSaldoService instance;
    
    private BonificacaoSaldoService() {
        //--
    }
    
    public static BonificacaoSaldoService getInstance() {
        if (instance == null) {
            instance = new BonificacaoSaldoService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return BonificacaoSaldoDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
    }

	public void consomeSaldoBonificacaoPedido(Pedido pedido, boolean validaSaldoDisponivel) throws SQLException {
		BonificacaoSaldo bonificacaoSaldoErp = getBonificacaoSaldo(pedido, BonificacaoSaldo.FL_ORIGEM_ERP);
		BonificacaoSaldo bonificacaoSaldoPda = getBonificacaoSaldo(pedido, BonificacaoSaldo.FL_ORIGEM_PDA);
		if (bonificacaoSaldoPda == null) {
			bonificacaoSaldoPda = new BonificacaoSaldo();
			bonificacaoSaldoPda.cdEmpresa = SessionLavenderePda.cdEmpresa;
			bonificacaoSaldoPda.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(BonificacaoSaldo.class);
			bonificacaoSaldoPda.flOrigemSaldo = BonificacaoSaldo.FL_ORIGEM_PDA;
			insert(bonificacaoSaldoPda);
		}
		double vlSaldoErp = bonificacaoSaldoErp != null ? bonificacaoSaldoErp.vlSaldo : 0;
		double vlSaldoPda = bonificacaoSaldoPda.vlSaldo;
		double vlSaldoDisponivel = vlSaldoErp + vlSaldoPda;
		if (validaSaldoDisponivel && vlSaldoDisponivel < pedido.vlTotalPedido) {
			throw new ValidationBonificacaoSaldoException(MessageUtil.getMessage(Messages.CONSUMO_SALDOBONIFICACAO_EXCEDENTE_FECHAR_PEDIDO, new Object[]{StringUtil.getStringValueToInterface(pedido.vlTotalPedido), StringUtil.getStringValueToInterface(vlSaldoDisponivel)}));
		}
		bonificacaoSaldoPda.vlSaldo -= pedido.vlTotalPedido;
		update(bonificacaoSaldoPda);
	}

	private BonificacaoSaldo getBonificacaoSaldo(Pedido pedido, String flOrigemSaldo) throws SQLException {
		BonificacaoSaldo bonificacaoSaldoErp = new BonificacaoSaldo();
		bonificacaoSaldoErp.cdEmpresa = pedido.cdEmpresa;
		bonificacaoSaldoErp.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(BonificacaoSaldo.class);
		bonificacaoSaldoErp.flOrigemSaldo = flOrigemSaldo;
		return (BonificacaoSaldo) findByRowKey(bonificacaoSaldoErp.getRowKey());
	}

	public void updateBonificacaoSaldoForPedidosFechados(Pedido pedido) throws SQLException {
		BonificacaoSaldo bonificacaoSaldoFilter = new BonificacaoSaldo();
		bonificacaoSaldoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		bonificacaoSaldoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		Vector bonificacaoSaldoList = findAllByExample(bonificacaoSaldoFilter);
		if (bonificacaoSaldoList != null) {
			BonificacaoSaldo bonificacaoSaldoErp;
			BonificacaoSaldo bonificacaoSaldoPda;
			BonificacaoSaldo bonificacaoSaldo = (BonificacaoSaldo) bonificacaoSaldoList.items[0];
			if (bonificacaoSaldoList.size() == 1) {
				bonificacaoSaldoErp = new BonificacaoSaldo();
				bonificacaoSaldoErp.cdEmpresa = SessionLavenderePda.cdEmpresa;
				bonificacaoSaldoErp.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(BonificacaoSaldo.class);
				bonificacaoSaldoErp.flOrigemSaldo = BonificacaoSaldo.FL_ORIGEM_ERP;
				insert(bonificacaoSaldoErp);
				bonificacaoSaldoPda = bonificacaoSaldo;
			} else {
				if (BonificacaoSaldo.FL_ORIGEM_ERP.equals(bonificacaoSaldo.flOrigemSaldo)) {
					bonificacaoSaldoErp = bonificacaoSaldo;
					bonificacaoSaldoPda = (BonificacaoSaldo) bonificacaoSaldoList.items[1];
				} else {
					bonificacaoSaldoPda = bonificacaoSaldo;
					bonificacaoSaldoErp = (BonificacaoSaldo) bonificacaoSaldoList.items[1];
				}
			}
			bonificacaoSaldoErp.vlSaldo += pedido.vlTotalPedido * -1;
			update(bonificacaoSaldoErp);
			bonificacaoSaldoPda.vlSaldo -= pedido.vlTotalPedido * -1;
			update(bonificacaoSaldoPda);
		}
	}

	public void recalculaSaldoPda() throws SQLException {
		LoadingBoxWindow mb = UiUtil.createProcessingMessage();
    	mb.popupNonBlocking();
    	try {
    		BonificacaoSaldo bonificacaoSaldo = new BonificacaoSaldo();
    		bonificacaoSaldo.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		bonificacaoSaldo.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    		bonificacaoSaldo.flOrigemSaldo = BonificacaoSaldo.FL_ORIGEM_PDA;
	    	Vector bonificacaoSaldoList = findAllByExample(bonificacaoSaldo);
	    	//--
	    	if (bonificacaoSaldoList != null) {
	    		int size = bonificacaoSaldoList.size();
	    		for (int i = 0; i < size; i++) {
	    			BonificacaoSaldo bonificacaoSaldoPda = (BonificacaoSaldo) bonificacaoSaldoList.items[i];
	    			delete(bonificacaoSaldoPda);
	    		}
	    	}
	    	//--
	    	Pedido pedidoFilter = new Pedido();
	    	pedidoFilter.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoFechado;
	    	Vector pedidoList = PedidoPdbxDao.getInstance().findAllPedidosPdaResumido(pedidoFilter);
			int sizePedidoList = pedidoList.size();
	        for (int i = 0; i < sizePedidoList; i++) {
	    		Pedido pedido = (Pedido) pedidoList.items[i];
	    		if (pedido.getTipoPedido() != null && pedido.getTipoPedido().isBonificacao()) {
	    			BonificacaoSaldo bonificacaoSaldoFilter = getBonificacaoSaldo(pedido, BonificacaoSaldo.FL_ORIGEM_PDA);
	    	    	if (bonificacaoSaldoFilter != null) {
	    	    		bonificacaoSaldoFilter.vlSaldo -= pedido.vlTotalPedido;
	    		    	update(bonificacaoSaldoFilter);
	    	    	} else {
	    	    		bonificacaoSaldoFilter = new BonificacaoSaldo();
	    	    		bonificacaoSaldoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
	    				bonificacaoSaldoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(BonificacaoSaldo.class);
	    				bonificacaoSaldoFilter.flOrigemSaldo = BonificacaoSaldo.FL_ORIGEM_PDA;
	    	    		bonificacaoSaldoFilter.vlSaldo -= pedido.vlTotalPedido;
	    		    	insert(bonificacaoSaldoFilter);
	    	    	}
	    		}
	    	}
    	} finally {
    		mb.unpop();
    	}
	}

	public Vector getBonificacaoSaldoRepList() throws SQLException {
		BonificacaoSaldo bonificacaoSaldo = new BonificacaoSaldo();
		bonificacaoSaldo.cdEmpresa = SessionLavenderePda.cdEmpresa;
		bonificacaoSaldo.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(BonificacaoSaldo.class);
		return findAllByExample(bonificacaoSaldo);	
	}

}