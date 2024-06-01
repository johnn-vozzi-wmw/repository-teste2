package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Verba;
import br.com.wmw.lavenderepda.business.domain.VerbaUsuario;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VerbaUsuarioPdbxDao;
import totalcross.util.Vector;

public class VerbaUsuarioService extends CrudService {

    private static VerbaUsuarioService instance;
    
    private VerbaUsuarioService() {
        //--
    }
    
    public static VerbaUsuarioService getInstance() {
        if (instance == null) {
            instance = new VerbaUsuarioService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return VerbaUsuarioPdbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
    public void insertVlSaldo(ItemPedido itemPedido) throws SQLException {
		VerbaUsuario newVerbaUsuario = new VerbaUsuario();
		newVerbaUsuario.cdUsuario = Session.getCdUsuario();
		newVerbaUsuario.flOrigemSaldo = Verba.VERBA_PDA;
		VerbaUsuario verbaUsuario = (VerbaUsuario) findByRowKey(newVerbaUsuario.getRowKey());
    	if (verbaUsuario != null) {
    		verbaUsuario.vlSaldo += itemPedido.vlVerbaItem;
    		if (LavenderePdaConfig.geraVerbaPositiva) {
    			verbaUsuario.vlSaldo += itemPedido.vlVerbaItemPositivo;
    		}
    		update(verbaUsuario);
    	} else {
    		newVerbaUsuario.vlSaldo = itemPedido.vlVerbaItem;
    		if (LavenderePdaConfig.geraVerbaPositiva) {
    			newVerbaUsuario.vlSaldo += itemPedido.vlVerbaItemPositivo;
    		}
    		insert(newVerbaUsuario);
    	}
    }
    
    public void updateVlSaldo(ItemPedido itemPedido) throws SQLException {
    	VerbaUsuario verbaUsuario = getVerbaUsuarioFilter(itemPedido);
    	if (verbaUsuario != null) {
    		verbaUsuario.vlSaldo -= itemPedido.vlVerbaItemOld;
    		verbaUsuario.vlSaldo += itemPedido.vlVerbaItem;
    		if (LavenderePdaConfig.geraVerbaPositiva) {
    			verbaUsuario.vlSaldo -= itemPedido.vlVerbaItemPositivoOld;
    			verbaUsuario.vlSaldo += itemPedido.vlVerbaItemPositivo;
    		}
    		update(verbaUsuario);
    	}
    }
    
    public void deleteVlSaldo(ItemPedido itemPedido) throws SQLException {
    	VerbaUsuario verbaUsuario = getVerbaUsuarioFilter(itemPedido);
    	if (verbaUsuario != null) {
    		verbaUsuario.vlSaldo -= itemPedido.vlVerbaItemOld;
    		if (LavenderePdaConfig.geraVerbaPositiva) {
    			verbaUsuario.vlSaldo -= itemPedido.vlVerbaItemPositivoOld;
    		}
    		update(verbaUsuario);
    	}
    }
    
    private VerbaUsuario getVerbaUsuarioFilter(ItemPedido itemPedido) throws SQLException {
    	VerbaUsuario verbaUsuarioFilter = new VerbaUsuario();
		verbaUsuarioFilter.cdUsuario = Session.getCdUsuario();
		verbaUsuarioFilter.flOrigemSaldo = Verba.VERBA_PDA;
		return (VerbaUsuario) findByRowKey(verbaUsuarioFilter.getRowKey());
    }
    
    public double getVlSaldo() throws SQLException {
    	VerbaUsuario verbaUsuarioFilter = new VerbaUsuario();
    	verbaUsuarioFilter.cdUsuario = Session.getCdUsuario();
    	//--
    	Vector verbaSaldoList = findAllByExample(verbaUsuarioFilter);
    	//--
    	if (verbaSaldoList == null) {
    		return 0;
    	} else {
    		boolean hasVerbaSaldoPda = false;
    		int size = verbaSaldoList.size();
            for (int i = 0; i < size; i++) {
            	VerbaUsuario verbaUsuario = (VerbaUsuario) verbaSaldoList.items[i];
    			if (Verba.VERBA_PDA.equals(verbaUsuario.flOrigemSaldo)) {
    				hasVerbaSaldoPda = true;
    				break;
    			}
    		}
    		//Se não tiver verbaFornecedor pda, cria-se uma
    		if (!hasVerbaSaldoPda && (size > 0)) {
    			VerbaUsuario verbaUsuarioErp = (VerbaUsuario) verbaSaldoList.items[0];
    			VerbaUsuario verbaUsuarioPda = new VerbaUsuario();
    			verbaUsuarioPda.flOrigemSaldo = Verba.VERBA_PDA;
    			verbaUsuarioPda.vlSaldo = 0;
    			VerbaUsuario vf = (VerbaUsuario) findByRowKey(verbaUsuarioPda.getRowKey());
    			if (vf == null) {
    				insert(verbaUsuarioPda);
    			}
    			return verbaUsuarioErp.vlSaldo;
    		}
    		//--
    		double vlSaldoPda = 0;
    		double vlSaldoErpInicial = 0;
    		for (int  i = 0; i < size; i++) {
    			VerbaUsuario verbaUsuario = (VerbaUsuario) verbaSaldoList.items[i];
    			if (Verba.VERBA_PDA.equals(verbaUsuario.flOrigemSaldo)) {
    				vlSaldoPda = verbaUsuario.vlSaldo;
    			} else if (Verba.VERBA_ERP.equals(verbaUsuario.flOrigemSaldo)) {
    				vlSaldoErpInicial = verbaUsuario.vlSaldo;
    			}
    		}
    		return vlSaldoErpInicial + vlSaldoPda;
    	}
    }
    
    
    
    
    public void updateVerbaSaldoItemPedidoAposEnvioServidor(ItemPedido itemPedido) throws SQLException {
    	VerbaUsuario verbaUsuarioFilter = new VerbaUsuario();
    	verbaUsuarioFilter.cdUsuario = Session.getCdUsuario();
    	Vector verbaUsuarioList = findAllByExample(verbaUsuarioFilter);
    	if (verbaUsuarioList != null) {
        	for (int  i = 0; i < verbaUsuarioList.size(); i++) {
        		VerbaUsuario verbaUsuario = (VerbaUsuario) verbaUsuarioList.items[i];
        		if (Verba.VERBA_PDA.equals(verbaUsuario.flOrigemSaldo)) {
        			verbaUsuario.vlSaldo -= itemPedido.vlVerbaItem;
        			if (LavenderePdaConfig.geraVerbaPositiva) {
        				verbaUsuario.vlSaldo -= itemPedido.vlVerbaItemPositivo;
        			}
        			update(verbaUsuario);
        		} else if (Verba.VERBA_ERP.equals(verbaUsuario.flOrigemSaldo)) {
        			verbaUsuario.vlSaldo += itemPedido.vlVerbaItem;
        			if (LavenderePdaConfig.geraVerbaPositiva) {
        				verbaUsuario.vlSaldo += itemPedido.vlVerbaItemPositivo;
        			}
        			update(verbaUsuario);
        		}
        	}
    	}
    }
    
    
    public void recalculateAndUpdateVerbaUsuarioPda() throws SQLException {
    	LoadingBoxWindow mb = UiUtil.createProcessingMessage();
    	mb.popupNonBlocking();
    	try {
    		VerbaUsuario verbaUsuarioFilter = new VerbaUsuario();
    		verbaUsuarioFilter.flOrigemSaldo = Verba.VERBA_PDA;
	    	deleteAllByExample(verbaUsuarioFilter);
	    	//--
	        Pedido pedidoFilter = new Pedido();
	        pedidoFilter.cdStatusPedidoDif = LavenderePdaConfig.cdStatusPedidoTransmitido;
	        Vector pedidoList = PedidoPdbxDao.getInstance().findAllPedidosPdaResumido(pedidoFilter);
			int sizePedidoList = pedidoList.size();
	        for (int i = 0; i < sizePedidoList; i++) {
	    		Pedido pedido = (Pedido) pedidoList.items[i];
	    		if (pedido.getTipoPedido() != null && (pedido.isSimulaControleVerba() || pedido.isIgnoraControleVerba())) {
	    			continue;
	    		}
	    		PedidoService.getInstance().findItemPedidoList(pedido);
	    		Vector itemPedidoList = pedido.itemPedidoList;
	    		int itemSize = itemPedidoList.size();
	    		for (int j = 0; j < itemSize; j++) {
	    			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[j];
	    			insertVlSaldo(itemPedido);
	    		}
	    	}
    	} finally {
    		mb.unpop();
    	}
    }
    
	public void ajustaVerbaItemSugestaoPedido(Pedido pedido, ItemPedido itemPedido, boolean usaVerba) throws SQLException {
		if (pedido != null && itemPedido != null) {
			if (usaVerba && itemPedido.getProduto().isUtilizaVerba() && !itemPedido.isOportunidade()) {
				double vlSaldoAtual = ValueUtil.round(getVlSaldo());
				double vlSaldofinal = ValueUtil.round(vlSaldoAtual + itemPedido.vlVerbaItem);
				if (vlSaldofinal * -1 > SessionLavenderePda.usuarioPdaRep.representante.vlToleranciaVerba) {
					aplicaVerbaItemSugestaoPedidoPedido(itemPedido, pedido, vlSaldoAtual);
				}
			} else {
				itemPedido.vlPctDesconto = 0;
				itemPedido.vlPctVerba = 0;
				itemPedido.vlVerbaItem = 0;
				itemPedido.vlVerbaItemOld = 0;
			}
		}
	}

    private void aplicaVerbaItemSugestaoPedidoPedido(ItemPedido itemPedido, Pedido pedido, double vlVerbaSaldo) {
    	double vlVerbaTotal = ValueUtil.round(vlVerbaSaldo + SessionLavenderePda.usuarioPdaRep.representante.vlToleranciaVerba);
    	double vlVerbaUnitaria = vlVerbaTotal / itemPedido.getQtItemFisico();
    	itemPedido.vlVerbaItemSugestaoPedido = itemPedido.vlVerbaItem;
    	itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido);
    	if (itemPedido.isItemVendaNormal() && vlVerbaTotal > 0) {
			itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido - vlVerbaUnitaria);
			itemPedido.vlPctDesconto = ValueUtil.round((1 - (itemPedido.vlItemPedido / itemPedido.vlBaseItemPedido)) * 100);
			itemPedido.vlVerbaItem = ValueUtil.round(vlVerbaUnitaria * itemPedido.getQtItemFisico()) * -1;
    	} else {
    		itemPedido.vlPctDesconto = 0;
    		itemPedido.vlVerbaItem = 0;
    	}
    	pedido.itemPedidoConsumoVerbaSugestaoPedList.addElement(itemPedido);
    }

}