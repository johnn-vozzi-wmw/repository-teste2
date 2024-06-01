package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.sql.Types;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.MargemContribFaixa;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Verba;
import br.com.wmw.lavenderepda.business.domain.VerbaSaldo;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MargemContribFaixaDbxDao;
import totalcross.util.Vector;

public class MargemContribFaixaService extends CrudService {

    private static MargemContribFaixaService instance;
    
    private MargemContribFaixaService() {
        //--
    }
    
    public static MargemContribFaixaService getInstance() {
        if (instance == null) {
            instance = new MargemContribFaixaService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return MargemContribFaixaDbxDao.getInstance();
    }

	@Override
	public void validate(BaseDomain arg0) throws SQLException {
	}

	public void validaMargemContribuicaoPedido(Pedido pedido) throws SQLException {
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			MargemContribFaixa margemContribFaixa = MargemContribFaixaDbxDao.getInstance().findMargemContribuicaoFaixa(itemPedido.vlPctTotalMargemItem);
			if (margemContribFaixa == null) continue;
			
			if (margemContribFaixa.isBloqueiaPedido()) {
				throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_VLPCTTOTALMARGEMITEM_BLOQUEIA_PEDIDO, new Object[] {itemPedido.getProduto().toString(), StringUtil.getStringValueToInterface(itemPedido.vlPctTotalMargemItem)}));
			}
		}
		MargemContribFaixa margemContribFaixa = MargemContribFaixaDbxDao.getInstance().findMargemContribuicaoFaixa(pedido.vlPctTotalMargem);
		if (margemContribFaixa == null) return;
		
		if (margemContribFaixa.isBloqueiaPedido()) {
			throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_VLPCTTOTALMARGEMITEM_BLOQUEIA_PEDIDO, StringUtil.getStringValueToInterface(pedido.vlPctTotalMargem)));
		}
		if (margemContribFaixa.isConsomeVerba() && margemContribFaixa.vlPctMargemFim != 0) {
			validaSaldoVerbaDisponivelFecharPedido(pedido, margemContribFaixa.vlPctMargemFim);
		}
	}

	private void validaSaldoVerbaDisponivelFecharPedido(Pedido pedido, double vlPctMargemFim) throws SQLException {
		double vlVerbaConsumoPedido = getVlVerbaConsumoPedido(pedido, vlPctMargemFim);
		if (vlVerbaConsumoPedido == 0) return;
		
		String cdRepresentante = SessionLavenderePda.usuarioPdaRep.representante.isSupervisor() ? SessionLavenderePda.usuarioPdaRep.cdRepresentante : pedido.cdRepresentante;
		VerbaSaldo verbaSaldoErp = VerbaSaldoService.getInstance().getVerbaSaldoErpVingenciaAtual(cdRepresentante);
		verbaSaldoErp = verbaSaldoErp !=  null ? verbaSaldoErp : new VerbaSaldo(); 
		VerbaSaldo verbaSaldoPda = VerbaSaldoService.getInstance().getVerbaSaldoInstanced(pedido ,pedido.cdEmpresa, cdRepresentante, Verba.VERBA_PDA);
		verbaSaldoPda = (VerbaSaldo) VerbaSaldoService.getInstance().findByRowKey(verbaSaldoPda.getRowKey());
		verbaSaldoPda = verbaSaldoPda !=  null ? verbaSaldoPda : new VerbaSaldo(); 
		double vlSaldoDisponivel = verbaSaldoErp.vlSaldo + verbaSaldoPda.vlSaldo;
		if (vlSaldoDisponivel < vlVerbaConsumoPedido) {
			throw new ValidationException(MessageUtil.getMessage(Messages.MARGEMCONTRIBUICAO_VERBASALDO_SALDO_NEGATIVO, new Object[] {StringUtil.getStringValueToInterface(vlVerbaConsumoPedido), StringUtil.getStringValueToInterface(vlSaldoDisponivel)}));
		}
	}

	private double getVlVerbaConsumoPedido(Pedido pedido, double vlPctMargemFim) {
		double pctDiferenca = vlPctMargemFim - pedido.vlPctTotalMargem;
		double vlVerbaConsumoPedido = ValueUtil.round((pedido.vlTotalItens * pctDiferenca) / 100);
		return vlVerbaConsumoPedido;
	}

	private double getVlVerbaGeradaPedido(Pedido pedido, double vlPctMargemInicio) {
		double pctDiferenca = pedido.vlPctTotalMargem - vlPctMargemInicio;
		double vlVerbaGeradaPedido = ValueUtil.round((pedido.vlTotalItens * pctDiferenca) / 100);
		return vlVerbaGeradaPedido;
	}

	public void realizaControleVerba(Pedido pedido) throws SQLException {
		MargemContribFaixa margemContribFaixa = MargemContribFaixaDbxDao.getInstance().findMargemContribuicaoFaixa(pedido.vlPctTotalMargem);
		if (margemContribFaixa == null) return;
		
		String cdRepresentante = SessionLavenderePda.usuarioPdaRep.representante.isSupervisor() ? SessionLavenderePda.usuarioPdaRep.cdRepresentante : pedido.cdRepresentante;
		if (margemContribFaixa.isConsomeVerba()) {
			double vlVerbaConsumoPedido = getVlVerbaConsumoPedido(pedido, margemContribFaixa.vlPctMargemFim);
			pedido.vlVerbaPedido = vlVerbaConsumoPedido * -1;
			VerbaSaldo verbaSaldoPda = VerbaSaldoService.getInstance().getVerbaSaldoPda(pedido, cdRepresentante);
			verbaSaldoPda.vlSaldo += pedido.vlVerbaPedido;
			VerbaSaldoService.getInstance().update(verbaSaldoPda);
		} else if (margemContribFaixa.isGeraVerba()) {
			double vlVerbaGeradaPedido = getVlVerbaGeradaPedido(pedido, margemContribFaixa.vlPctMargemInicio);
			pedido.vlVerbaPedidoPositivo = vlVerbaGeradaPedido; 
			VerbaSaldo verbaSaldoPda = VerbaSaldoService.getInstance().getVerbaSaldoPda(pedido, cdRepresentante);
			verbaSaldoPda.vlSaldo += pedido.vlVerbaPedidoPositivo;
			VerbaSaldoService.getInstance().update(verbaSaldoPda);
		}
	}

	public void updateVerbaSaldoPedido(Pedido pedido) throws SQLException {
		String cdRepresentante = SessionLavenderePda.usuarioPdaRep.representante.isSupervisor() ? SessionLavenderePda.usuarioPdaRep.cdRepresentante : pedido.cdRepresentante;
		VerbaSaldo verbaSaldoFilter = new VerbaSaldo();
    	verbaSaldoFilter.setCdEmpresa(SessionLavenderePda.cdEmpresa);
    	verbaSaldoFilter.cdRepresentante = cdRepresentante;
    	verbaSaldoFilter.cdContaCorrente = pedido.getCdContaCorrente();
    	Vector verbaSaldoList = VerbaSaldoService.getInstance().findAllByExample(verbaSaldoFilter);
    	if (verbaSaldoList != null) {
        	for (int  i = 0; i < verbaSaldoList.size(); i++) {
        		VerbaSaldo verbaSaldo = (VerbaSaldo) verbaSaldoList.items[i];
    			if (Verba.VERBA_PDA.equals(verbaSaldo.flOrigemSaldo)) {
    				verbaSaldo.vlSaldo -= pedido.vlVerbaPedido;
    				verbaSaldo.vlSaldo -= pedido.vlVerbaPedidoPositivo;
    				VerbaSaldoService.getInstance().update(verbaSaldo);
    			} else if (Verba.VERBA_ERP.equals(verbaSaldo.flOrigemSaldo)) {
    				verbaSaldo.vlSaldo += pedido.vlVerbaPedido;
    				verbaSaldo.vlSaldo += pedido.vlVerbaPedidoPositivo;
    				VerbaSaldoService.getInstance().update(verbaSaldo);
    			}
        	}
    	}
	}

	public void estornaVerbaSaldo(Pedido pedido) throws SQLException {
		String cdRepresentante = SessionLavenderePda.usuarioPdaRep.representante.isSupervisor() ? SessionLavenderePda.usuarioPdaRep.cdRepresentante : pedido.cdRepresentante;
		VerbaSaldo verbaSaldoPda = VerbaSaldoService.getInstance().getVerbaSaldoPda(pedido, cdRepresentante);
		verbaSaldoPda.vlSaldo -= pedido.vlVerbaPedido;
		verbaSaldoPda.vlSaldo -= pedido.vlVerbaPedidoPositivo;
		VerbaSaldoService.getInstance().update(verbaSaldoPda);
		pedido.vlVerbaPedido = 0;
		pedido.vlVerbaPedidoPositivo = 0;
		PedidoService.getInstance().updateColumn(pedido.getRowKey(), "vlVerbaPedido", 0d, Types.DECIMAL);
		PedidoService.getInstance().updateColumn(pedido.getRowKey(), "vlVerbaPedidoPositivo", 0d, Types.DECIMAL);
	}
 

}