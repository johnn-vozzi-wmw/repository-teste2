package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto1;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.VerbaCliente;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VerbaClientePdbxDao;
import totalcross.util.Vector;

public class VerbaClienteService extends CrudService {

    private static VerbaClienteService instance;

    private VerbaClienteService() {
        //--
    }

    public static VerbaClienteService getInstance() {
        if (instance == null) {
            instance = new VerbaClienteService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return VerbaClientePdbxDao.getInstance();
    }

    public void deleteVlSaldo(ItemPedido itemPedido) throws SQLException {
    	if (!itemPedido.pedido.isIgnoraControleVerba() && !itemPedido.pedido.isSimulaControleVerba()) {
    		if ((itemPedido.vlVerbaItem <= 0) || ((itemPedido.vlVerbaItem>=0) && LavenderePdaConfig.geraVerbaSaldo)) {
    			VerbaCliente verbaClienteFilter = getVerbaClienteFilter(VerbaCliente.VERBASALDO_PDA, SessionLavenderePda.getCliente().cdCliente);
    			if (LavenderePdaConfig.permiteEscolhaSaldoVerbaAConsumir) {
    				verbaClienteFilter.cdVerbaSaldoCliente = itemPedido.cdVerbaSaldoCliente;
    				verbaClienteFilter.cdGrupoProduto1 = itemPedido.getProduto().cdGrupoProduto1;
    			}
    			VerbaCliente verbaCliente = (VerbaCliente) VerbaClienteService.getInstance().findByRowKey(verbaClienteFilter.getRowKey());
    			if (verbaCliente != null) {
    				verbaCliente.vlSaldo -= itemPedido.vlVerbaItemOld;
    				update(verbaCliente);
    			}
    		}
    	}
    }

	private VerbaCliente getVerbaClienteFilter(String flOrigemSaldo, String cdCliente) {
		VerbaCliente verbaClienteFilter = new VerbaCliente();
		verbaClienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		verbaClienteFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		verbaClienteFilter.flOrigemSaldo = flOrigemSaldo;
		verbaClienteFilter.cdCliente = cdCliente;
		verbaClienteFilter.cdVerbaSaldoCliente = VerbaCliente.CDVERBASALDOCLIENTE_DEFAULT;
		verbaClienteFilter.cdGrupoProduto1 = GrupoProduto1.CD_GRUPO_PRODUTO_VAZIO;
		return verbaClienteFilter;
	}

    public void recalculateAndUpdateVerbaClientePda() throws SQLException {
    	try {
    		VerbaCliente verbaCliente = new VerbaCliente();
    		verbaCliente.flOrigemSaldo = VerbaCliente.VERBASALDO_PDA;
    		verbaCliente.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		verbaCliente.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
	    	Vector listVerbaClientePda = findAllByExample(verbaCliente);
	    	//--
	    	if (listVerbaClientePda != null) {
	    		int size = listVerbaClientePda.size();
	    		for (int i = 0; i < size; i++) {
	    			VerbaCliente verbaClientePda = (VerbaCliente) listVerbaClientePda.items[i];
	    			delete(verbaClientePda);
	    		}
	    	}
	    	//--
	    	Pedido pedidoFilter = new Pedido();
	    	pedidoFilter.cdStatusPedidoDif = LavenderePdaConfig.cdStatusPedidoTransmitido;
	    	Vector pedidoList = PedidoPdbxDao.getInstance().findAllPedidosPdaResumido(pedidoFilter);
			int sizePedidoList = pedidoList.size();
	        for (int i = 0; i < sizePedidoList; i++) {
	    		Pedido pedido = (Pedido) pedidoList.items[i];
	    		if (!pedido.isIgnoraControleVerba()) {
	    			PedidoService.getInstance().findItemPedidoList(pedido);
	    			Vector itemPedidoList = pedido.itemPedidoList;
	    			int itemSize = itemPedidoList.size();
	    			for (int j=0; j < itemSize; j++) {
	    				ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[j];
	    				if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
	    					itemPedido.dtEmissaoPedido = pedido.dtEmissao;
	    				}
	    				insertVlSaldo(itemPedido , pedido.cdCliente);
	    			}
	    		}
	    	}
    	} catch (Throwable e ) {
    		LogSync.error(e.getMessage());
    		ExceptionUtil.handle(e);
    	}
    }

    public void updateVlSaldo(ItemPedido itemPedido) throws SQLException {
    	if (!itemPedido.pedido.isIgnoraControleVerba() && !itemPedido.pedido.isSimulaControleVerba()) {
    		if ((itemPedido.vlVerbaItem <= 0) || ((itemPedido.vlVerbaItem>=0) && LavenderePdaConfig.geraVerbaSaldo)) {
    			VerbaCliente verbaSaldoFilter = getVerbaClienteFilter(VerbaCliente.VERBASALDO_PDA, SessionLavenderePda.getCliente().cdCliente);
    			if (LavenderePdaConfig.permiteEscolhaSaldoVerbaAConsumir) {
    				verbaSaldoFilter.cdVerbaSaldoCliente = itemPedido.cdVerbaSaldoCliente;
    				verbaSaldoFilter.cdGrupoProduto1 = itemPedido.getProduto().cdGrupoProduto1;
    			}
    			VerbaCliente verbaSaldo = (VerbaCliente) findByRowKey(verbaSaldoFilter.getRowKey());
    			if (verbaSaldo != null) {
    				verbaSaldo.vlSaldo -= itemPedido.vlVerbaItemOld;
    				verbaSaldo.vlSaldo += itemPedido.vlVerbaItem;
    				update(verbaSaldo);
    			} else if (LavenderePdaConfig.permiteEscolhaSaldoVerbaAConsumir) {
    				insertVlSaldo(itemPedido, SessionLavenderePda.getCliente().cdCliente);
    			}
    		}
    	}
    }

    public void insertVlSaldo(ItemPedido itemPedido , String cdCliente) throws SQLException {
    	if (!itemPedido.pedido.isIgnoraControleVerba() && !itemPedido.pedido.isSimulaControleVerba()) {
    		if ((itemPedido.vlVerbaItem <= 0) || ((itemPedido.vlVerbaItem>=0) && LavenderePdaConfig.geraVerbaSaldo)) {
    			VerbaCliente verbaClienteFilter = getVerbaClienteFilter(VerbaCliente.VERBASALDO_PDA, cdCliente);
    			if (LavenderePdaConfig.permiteEscolhaSaldoVerbaAConsumir) {
    				verbaClienteFilter.cdVerbaSaldoCliente = itemPedido.cdVerbaSaldoCliente;
    				verbaClienteFilter.cdGrupoProduto1 = itemPedido.getProduto().cdGrupoProduto1;
    			}
    			VerbaCliente verbaCliente = (VerbaCliente) findByRowKey(verbaClienteFilter.getRowKey());
    			if (verbaCliente != null) {
    				verbaCliente.vlSaldo += itemPedido.vlVerbaItem;
    				update(verbaCliente);
    			} else {
    				verbaClienteFilter.vlSaldo = itemPedido.vlVerbaItem;
    				insert(verbaClienteFilter);
    			}
    			itemPedido.vlVerbaItemOld = itemPedido.vlVerbaItem;
    		}
    	}
    }

    public void updateVerbaClienteItemPedido(Pedido pedido) throws SQLException {
		if (!pedido.isIgnoraControleVerba() && !pedido.isSimulaControleVerba()) {
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
					VerbaCliente verbaClienteFilter = getVerbaClienteFilter(VerbaCliente.VERBASALDO_ERP, pedido.cdCliente);
					verbaClienteFilter = (VerbaCliente) findByRowKey(verbaClienteFilter.getRowKey());
					if (verbaClienteFilter == null) {
						LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_SESSAO, Messages.ERRO_VERBA_SALDO_ERP);
						return;
					}
				}
				// --------------------------------
				VerbaCliente verbaClienteFilter = new VerbaCliente();
				verbaClienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
				verbaClienteFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
				verbaClienteFilter.cdCliente = pedido.cdCliente;
				Vector verbaSaldoList = findAllByExample(verbaClienteFilter);
				if (verbaSaldoList != null) {
					int size2 = verbaSaldoList.size();
					for (int  j = 0; j < size2; j++) {
						VerbaCliente verbaCliente = (VerbaCliente) verbaSaldoList.items[j];
						if (VerbaCliente.VERBASALDO_PDA.equals(verbaCliente.flOrigemSaldo)) {
							verbaCliente.vlSaldo -= itemPedido.vlVerbaItem;
							update(verbaCliente);
						} else if (VerbaCliente.VERBASALDO_ERP.equals(verbaCliente.flOrigemSaldo)) {
							verbaCliente.vlSaldo += itemPedido.vlVerbaItem;
							update(verbaCliente);
						}
					}
				}
			}
		}
    }
    
    public void updateVerbaClienteGrupoProduto(Pedido pedido) throws SQLException {
    	if (!pedido.isIgnoraControleVerba() && !pedido.isSimulaControleVerba()) {
    		if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
				VerbaCliente verbaClienteFilter = getVerbaClienteFilter(VerbaCliente.VERBASALDO_ERP, pedido.cdCliente);
				verbaClienteFilter.cdVerbaSaldoCliente = verbaClienteFilter.cdGrupoProduto1 = null;
				if (countByExample(verbaClienteFilter) <= 1) {
					LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_SESSAO, Messages.ERRO_VERBA_SALDO_ERP);
					return;
				}
			}
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				VerbaCliente verbaClienteFilter = new VerbaCliente();
				verbaClienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
				verbaClienteFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
				verbaClienteFilter.cdCliente = pedido.cdCliente;
				verbaClienteFilter.cdGrupoProduto1 = itemPedido.getProduto().cdGrupoProduto1;
				verbaClienteFilter.cdVerbaSaldoCliente = itemPedido.cdVerbaSaldoCliente;
				Vector verbaSaldoList = findAllByExample(verbaClienteFilter);
				if (verbaSaldoList != null) {
					int size2 = verbaSaldoList.size();
					for (int  j = 0; j < size2; j++) {
						VerbaCliente verbaCliente = (VerbaCliente) verbaSaldoList.items[j];
						if (VerbaCliente.VERBASALDO_PDA.equals(verbaCliente.flOrigemSaldo)) {
							verbaCliente.vlSaldo -= itemPedido.vlVerbaItem;
							update(verbaCliente);
						} else if (VerbaCliente.VERBASALDO_ERP.equals(verbaCliente.flOrigemSaldo)) {
							verbaCliente.vlSaldo += itemPedido.vlVerbaItem;
							update(verbaCliente);
						}
					}
				}
			}
    	}
    }
    
    public double getVlSaldo() throws SQLException {
    	return getVlSaldo(ValueUtil.VALOR_ZERO, ValueUtil.VALOR_ZERO);
    }

    public double getVlSaldo(String cdVerbaSaldoCliente, String cdGrupoProduto) throws SQLException {
    	String cdCliente = SessionLavenderePda.getCliente().cdCliente;
    	//--
    	VerbaCliente verbaClienteFilter = new VerbaCliente();
    	verbaClienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	verbaClienteFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	verbaClienteFilter.cdCliente = cdCliente;
    	verbaClienteFilter.cdVerbaSaldoCliente = cdVerbaSaldoCliente;
    	verbaClienteFilter.cdGrupoProduto1 = cdGrupoProduto;
    	//--
    	Vector verbaSaldoList = findAllByExample(verbaClienteFilter);
    	//--
    	if (verbaSaldoList == null) {
    		return 0;
    	} else {
    		boolean hasVerbaSaldoPda = false;
    		int size = verbaSaldoList.size();
            for (int i = 0; i < size; i++) {
            	VerbaCliente verbaCliente = (VerbaCliente) verbaSaldoList.items[i];
    			if (VerbaCliente.VERBASALDO_PDA.equals(verbaCliente.flOrigemSaldo)) {
    				hasVerbaSaldoPda = true;
    				break;
    			}
    		}
    		//Se não tiver verbasaldo pda, cria-se uma
    		if (!hasVerbaSaldoPda && (size > 0)) {
    			VerbaCliente verbaSaldoErp = (VerbaCliente) verbaSaldoList.items[0];
    			VerbaCliente verbaSaldoPda = getVerbaClienteFilter(VerbaCliente.VERBASALDO_PDA, cdCliente);
    			verbaSaldoPda.cdVerbaSaldoCliente = cdVerbaSaldoCliente;
    			verbaSaldoPda.cdGrupoProduto1 = cdGrupoProduto;
    			verbaSaldoPda.vlSaldo = 0;
    			VerbaCliente vs = (VerbaCliente) findByRowKey(verbaSaldoPda.getRowKey());
    			if (vs == null) {
    				insert(verbaSaldoPda);
    			}
    			return verbaSaldoErp.vlSaldo;
    		}
    		//--
    		double vlSaldoPda = 0;
    		double vlSaldoErpInicial = 0;
    		for (int  i = 0; i < size; i++) {
    			VerbaCliente verbaSaldo = (VerbaCliente) verbaSaldoList.items[i];
    			if (VerbaCliente.VERBASALDO_PDA.equals(verbaSaldo.flOrigemSaldo)) {
    				vlSaldoPda = verbaSaldo.vlSaldo;
    			} else if (VerbaCliente.VERBASALDO_ERP.equals(verbaSaldo.flOrigemSaldo)) {
    				vlSaldoErpInicial = verbaSaldo.vlSaldo;
    			}
    		}
    		return vlSaldoErpInicial + vlSaldoPda;
    	}
    }

    public void validateSaldo(ItemPedido itemPedido) throws SQLException {
    	if (itemPedido.vlVerbaItem < 0 && !itemPedido.pedido.isIgnoraControleVerba() && !itemPedido.pedido.isSimulaControleVerba()) {
    		String cdVerbaSaldoCliente = LavenderePdaConfig.permiteEscolhaSaldoVerbaAConsumir ? itemPedido.cdVerbaSaldoCliente : ValueUtil.VALOR_ZERO;
    		String cdGrupoProduto1 = LavenderePdaConfig.permiteEscolhaSaldoVerbaAConsumir ? itemPedido.getProduto().cdGrupoProduto1 : GrupoProduto1.CD_GRUPO_PRODUTO_VAZIO;
			double vlSaldo = getVlSaldo(cdVerbaSaldoCliente, cdGrupoProduto1);
    		double tot = vlSaldo + itemPedido.vlVerbaItem - itemPedido.vlVerbaItemOld;
    		if (tot < 0) {
    			double vlVerbaItem = itemPedido.vlVerbaItem < 0 ? itemPedido.vlVerbaItem * -1 : itemPedido.vlVerbaItem;
    			String args[] = { StringUtil.getStringValueToInterface(vlVerbaItem), StringUtil.getStringValueToInterface(vlSaldo - itemPedido.vlVerbaItemOld) };
    			throw new ValidationException(MessageUtil.getMessage(Messages.VERBASALDO_MSG_SALDO_INDISPONIVEL, args));
    		}
    	}
    }
    
    public Vector findCdVerbaSaldoCliente(final String cbGrupoProduto1) throws SQLException {
    	VerbaCliente verbaCliente = getVerbaClienteFilter(VerbaCliente.VERBASALDO_ERP, SessionLavenderePda.getCdCliente());
    	verbaCliente.cdVerbaSaldoCliente = null;
    	verbaCliente.cdGrupoProduto1 = cbGrupoProduto1;
    	return VerbaClientePdbxDao.getInstance().findCdVerbaSaldoCliente(verbaCliente);
    }
    
    public Vector findVerbaClienteGrupoProduto1ByExample(String cdGrupoProduto1, String cdCliente, String cdVerbaSaldoCliente) throws SQLException {
    	VerbaCliente verbaCliente = getVerbaClienteFilter(VerbaCliente.VERBASALDO_ERP, cdCliente);
    	verbaCliente.cdGrupoProduto1 = cdGrupoProduto1;
    	verbaCliente.cdVerbaSaldoCliente = cdVerbaSaldoCliente;
    	return VerbaClientePdbxDao.getInstance().findVerbaClienteGrupoProduto1ByExample(verbaCliente);
    }
    
    public boolean isPossuiVerbaClienteGrupoProduto1(String cdGrupoProduto1, String cdCliente) throws SQLException {
    	VerbaCliente verbaCliente = getVerbaClienteFilter(VerbaCliente.VERBASALDO_ERP, cdCliente);
    	verbaCliente.cdGrupoProduto1 = cdGrupoProduto1;
    	verbaCliente.cdVerbaSaldoCliente = null;
    	return VerbaClientePdbxDao.getInstance().countVerbaClienteGrupoProduto1ByExample(verbaCliente) > 0;
    }
    
    public void changeCdVerbaSaldoCliente(ItemPedido itemPedido, VerbaCliente verbaCliente) throws SQLException {
    	if (ValueUtil.isEmpty(itemPedido.cdVerbaSaldoCliente)) {
    		itemPedido.cdVerbaSaldoCliente = verbaCliente.cdVerbaSaldoCliente;
    		return;
    	}
    	if (ValueUtil.valueEquals(itemPedido.cdVerbaSaldoCliente, verbaCliente.cdVerbaSaldoCliente)) return;
    	double oldVlSaldo = itemPedido.vlVerbaItemOld;
    	itemPedido.vlVerbaItemOld = 0d;
    	String cdVerbaSaldoOld = itemPedido.cdVerbaSaldoCliente;
    	itemPedido.cdVerbaSaldoCliente = verbaCliente.cdVerbaSaldoCliente;
    	try {
    		VerbaClienteService.getInstance().validateSaldo(itemPedido);
    		itemPedido.cdVerbaSaldoCliente = cdVerbaSaldoOld;
    		itemPedido.vlVerbaItemOld = oldVlSaldo;
    		deleteVlSaldo(itemPedido);
    		itemPedido.vlVerbaItemOld = 0d;
    		itemPedido.cdVerbaSaldoCliente = verbaCliente.cdVerbaSaldoCliente;
    	} catch (Throwable e) {
    		itemPedido.cdVerbaSaldoCliente = cdVerbaSaldoOld;
    		itemPedido.vlVerbaItemOld = oldVlSaldo;
    		throw e;
		}
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
}