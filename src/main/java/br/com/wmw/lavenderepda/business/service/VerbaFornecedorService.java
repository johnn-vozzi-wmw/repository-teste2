package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.IVector;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Fornecedor;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Verba;
import br.com.wmw.lavenderepda.business.domain.VerbaFornecedor;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VerbaFornecedorDbxDao;
import totalcross.util.Vector;

public class VerbaFornecedorService extends CrudService {

    private static VerbaFornecedorService instance;
    
    private VerbaFornecedorService() {
        //--
    }
    
    public static VerbaFornecedorService getInstance() {
        if (instance == null) {
            instance = new VerbaFornecedorService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return VerbaFornecedorDbxDao.getInstance();
    }
    
    public void insertVlSaldo(ItemPedido itemPedido) throws SQLException {
    	if (!validaFornecedor(itemPedido)) {
    		return;
    	}
		VerbaFornecedor newVerbaFornecedor = new VerbaFornecedor();
		newVerbaFornecedor.cdEmpresa = itemPedido.cdEmpresa;
		newVerbaFornecedor.cdRepresentante = itemPedido.cdRepresentante;
		newVerbaFornecedor.cdFornecedor = itemPedido.getProduto().cdFornecedor;
		newVerbaFornecedor.flOrigemVerba = Verba.VERBA_PDA;
		VerbaFornecedor verbaFornecedor = (VerbaFornecedor) findByRowKey(newVerbaFornecedor.getRowKey());
    	if (verbaFornecedor != null) {
    		verbaFornecedor.vlSaldo += itemPedido.vlVerbaItem;
    		if (LavenderePdaConfig.geraVerbaPositiva) {
    			verbaFornecedor.vlSaldo += itemPedido.vlVerbaItemPositivo;
    		}
    		update(verbaFornecedor);
    	} else {
    		newVerbaFornecedor.vlSaldo = itemPedido.vlVerbaItem;
    		if (LavenderePdaConfig.geraVerbaPositiva) {
    			newVerbaFornecedor.vlSaldo += itemPedido.vlVerbaItemPositivo;
    		}
    		insert(newVerbaFornecedor);
    	}
    }
    
    public void updateVlSaldo(ItemPedido itemPedido) throws SQLException {
    	if (!validaFornecedor(itemPedido)) {
    		return;
    	}
		VerbaFornecedor verbaFornecedor = getVerbaFornecedorFilter(itemPedido);
    	if (verbaFornecedor != null) {
    		verbaFornecedor.vlSaldo -= itemPedido.vlVerbaItemOld;
    		verbaFornecedor.vlSaldo += itemPedido.vlVerbaItem;
    		if (LavenderePdaConfig.geraVerbaPositiva) {
    			verbaFornecedor.vlSaldo -= itemPedido.vlVerbaItemPositivoOld;
    			verbaFornecedor.vlSaldo += itemPedido.vlVerbaItemPositivo;
    		}
    		update(verbaFornecedor);
    	}
    }
    
    public void deleteVlSaldo(ItemPedido itemPedido) throws SQLException {
    	if (!validaFornecedor(itemPedido)) {
    		return;
    	}
		VerbaFornecedor verbaFornecedor = getVerbaFornecedorFilter(itemPedido);
    	if (verbaFornecedor != null) {
    		verbaFornecedor.vlSaldo -= itemPedido.vlVerbaItemOld;
    		if (LavenderePdaConfig.geraVerbaPositiva) {
    			verbaFornecedor.vlSaldo -= itemPedido.vlVerbaItemPositivoOld;
    		}
    		update(verbaFornecedor);
    	}
    }
    
    private VerbaFornecedor getVerbaFornecedorFilter(ItemPedido itemPedido) throws SQLException {
    	VerbaFornecedor verbaFornecedorFilter = new VerbaFornecedor();
		verbaFornecedorFilter.cdEmpresa = itemPedido.cdEmpresa;
		verbaFornecedorFilter.cdRepresentante = itemPedido.cdRepresentante;
		verbaFornecedorFilter.cdFornecedor = itemPedido.getProduto().cdFornecedor;
		verbaFornecedorFilter.flOrigemVerba = Verba.VERBA_PDA;
		return (VerbaFornecedor) findByRowKey(verbaFornecedorFilter.getRowKey());
    }
    
    public double getVlSaldo(String cdEmpresa, String cdRepresentante, String cdFornecedor) throws SQLException {
    	if (ValueUtil.isEmpty(cdFornecedor)) {
    		return 0d;
    	}
    	VerbaFornecedor verbaFornecedorFilter = new VerbaFornecedor();
    	verbaFornecedorFilter.cdEmpresa = cdEmpresa;
    	verbaFornecedorFilter.cdRepresentante = cdRepresentante;
    	verbaFornecedorFilter.cdFornecedor = cdFornecedor;
    	//--
    	Vector verbaSaldoList = findAllByExample(verbaFornecedorFilter);
    	//--
    	if (verbaSaldoList == null) {
    		return 0;
    	} else {
    		boolean hasVerbaSaldoPda = false;
    		int size = verbaSaldoList.size();
            for (int i = 0; i < size; i++) {
            	VerbaFornecedor verbaFornecedor = (VerbaFornecedor) verbaSaldoList.items[i];
    			if (Verba.VERBA_PDA.equals(verbaFornecedor.flOrigemVerba)) {
    				hasVerbaSaldoPda = true;
    				break;
    			}
    		}
    		//Se não tiver verbaFornecedor pda, cria-se uma
    		if (!hasVerbaSaldoPda && (size > 0)) {
    			VerbaFornecedor verbaFornecedorErp = (VerbaFornecedor) verbaSaldoList.items[0];
    			VerbaFornecedor verbaFornecedorPda = new VerbaFornecedor();
    			verbaFornecedorPda.cdEmpresa = cdEmpresa;
    			verbaFornecedorPda.cdRepresentante = cdRepresentante;
    			verbaFornecedorPda.cdFornecedor = cdFornecedor;
    			verbaFornecedorPda.flOrigemVerba = Verba.VERBA_PDA;
    			verbaFornecedorPda.vlSaldo = 0;
    			VerbaFornecedor vf = (VerbaFornecedor) findByRowKey(verbaFornecedorPda.getRowKey());
    			if (vf == null) {
    				insert(verbaFornecedorPda);
    			}
    			return verbaFornecedorErp.vlSaldo;
    		}
    		//--
    		double vlSaldoPda = 0;
    		double vlSaldoErpInicial = 0;
    		for (int  i = 0; i < size; i++) {
    			VerbaFornecedor verbaFornecedor = (VerbaFornecedor) verbaSaldoList.items[i];
    			if (Verba.VERBA_PDA.equals(verbaFornecedor.flOrigemVerba)) {
    				vlSaldoPda = verbaFornecedor.vlSaldo;
    			} else if (Verba.VERBA_ERP.equals(verbaFornecedor.flOrigemVerba)) {
    				vlSaldoErpInicial = verbaFornecedor.vlSaldo;
    			}
    		}
    		return vlSaldoErpInicial + vlSaldoPda;
    	}
    }
    
    public void updateVerbaSaldoItemPedidoAposEnvioServidor(ItemPedido itemPedido) throws SQLException {
    	if (ValueUtil.isEmpty(itemPedido.getProduto().cdFornecedor)) {
    		return;
    	}
    	VerbaFornecedor verbaSaldoFilter = new VerbaFornecedor();
    	verbaSaldoFilter.cdEmpresa = itemPedido.cdEmpresa;
    	verbaSaldoFilter.cdRepresentante = itemPedido.cdRepresentante;
    	verbaSaldoFilter.cdFornecedor = itemPedido.getProduto().cdFornecedor;
    	Vector verbaSaldoList = findAllByExample(verbaSaldoFilter);
    	if (verbaSaldoList != null) {
        	for (int  i = 0; i < verbaSaldoList.size(); i++) {
        		VerbaFornecedor verbaSaldo = (VerbaFornecedor) verbaSaldoList.items[i];
        		if (Verba.VERBA_PDA.equals(verbaSaldo.flOrigemVerba)) {
        			verbaSaldo.vlSaldo -= itemPedido.vlVerbaItem;
        			if (LavenderePdaConfig.geraVerbaPositiva) {
        				verbaSaldo.vlSaldo -= itemPedido.vlVerbaItemPositivo;
        			}
        			update(verbaSaldo);
        		} else if (Verba.VERBA_ERP.equals(verbaSaldo.flOrigemVerba)) {
        			verbaSaldo.vlSaldo += itemPedido.vlVerbaItem;
        			if (LavenderePdaConfig.geraVerbaPositiva) {
        				verbaSaldo.vlSaldo += itemPedido.vlVerbaItemPositivo;
        			}
        			update(verbaSaldo);
        		}
        	}
    	}
    }
    
    
    public void recalculateAndUpdateVerbaFornecedorPda() throws SQLException {
    	LoadingBoxWindow mb = UiUtil.createProcessingMessage();
    	mb.popupNonBlocking();
    	try {
    		VerbaFornecedor verbaFornecedorFilter = new VerbaFornecedor();
    		verbaFornecedorFilter.flOrigemVerba = Verba.VERBA_PDA;
	    	deleteAllByExample(verbaFornecedorFilter);
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
			if (usaVerba && itemPedido.getProduto().isUtilizaVerba() && !itemPedido.isOportunidade() && ValueUtil.isNotEmpty(itemPedido.getProduto().cdFornecedor)) {
				double vlSaldoAtual = ValueUtil.round(getVlSaldo(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getProduto().cdFornecedor));
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
    
	public Vector findAllVerbaFornecedorErp() throws SQLException {
		VerbaFornecedor verbaFornecedorFilter = new VerbaFornecedor();
    	verbaFornecedorFilter.flOrigemVerba = Verba.VERBA_ERP;
		return findAllByExample(verbaFornecedorFilter);
	}
	
	public Vector filtraFornecedoresNoPedido(Vector verbaFornecedorList, Vector itemPedidoList) throws SQLException {
		Vector verbaFornecedorValidoList = new Vector();
		for (int j = 0; j < verbaFornecedorList.size(); j++) {
			VerbaFornecedor verbaFornecedor = (VerbaFornecedor) verbaFornecedorList.items[j];
			for (int i = 0; i < itemPedidoList.size(); i++) {
				ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
				if (itemPedido.getProduto() != null && verbaFornecedor.cdFornecedor.equals(itemPedido.getProduto().cdFornecedor) && (itemPedido.vlVerbaItem != 0 || itemPedido.vlVerbaItemPositivo != 0)) {
					verbaFornecedorValidoList.addElement(verbaFornecedor);
					break;
				}
			}
		}
		return verbaFornecedorValidoList;
	}
	
	public double getVerbaFornecedorConsumidoPedido(Pedido pedido, VerbaFornecedor verbaFornecedor) throws SQLException {
		double saldo = 0d;
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (itemPedido.getProduto().cdFornecedor.equals(verbaFornecedor.cdFornecedor)) {
				saldo += LavenderePdaConfig.geraVerbaPositiva && itemPedido.vlVerbaItem != 0 ? itemPedido.vlVerbaItemPositivo : itemPedido.vlVerbaItem;
			}
		}
		return saldo;
	}
	
	public double getVlVerbaFornecedorPositiva(ItemPedido itemPedido) throws SQLException {
		if (ValueUtil.isEmpty(itemPedido.getProduto().cdFornecedor)) {
			return 0;
		} else {
			return itemPedido.vlVerbaItemPositivo;
		}
	}
	
	public boolean validaFornecedor(ItemPedido itemPedido) throws SQLException {
		return ValueUtil.isNotEmpty(itemPedido.getProduto().cdFornecedor) && existeFornecedor(itemPedido);
	}
	
	public boolean existeFornecedor(ItemPedido itemPedido) throws SQLException {
		Fornecedor fornecedor = new Fornecedor();
		fornecedor.cdEmpresa = itemPedido.getProduto().cdEmpresa;
		fornecedor.cdFornecedor = itemPedido.getProduto().cdFornecedor;
		fornecedor = (Fornecedor) FornecedorService.getInstance().findByRowKey(fornecedor.getRowKey());
		return fornecedor != null;
	}

	//@Override
	public void validate(BaseDomain domain) throws java.sql.SQLException {
		
	}
	
	public double getVlVerbaDisponivel(ItemPedido itemPedido) throws SQLException {
		return getVlSaldo(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getProduto().cdFornecedor); 
	}
	
	public double getVerbaConsumidaPedidoCorrente(final Pedido pedido) {
		double vlVerba = 0d;
		if (pedido == null) return vlVerba;		
		return vlVerba;
	}
	
	public double getMaxConsumoFornecedor(ItemPedido itemPedido) throws SQLException {
		ItemTabelaPreco itemTab = itemPedido.getItemTabelaPreco();
		if (itemTab == null || itemPedido == null) return 0d;
		return itemPedido.getQtItemFisico() * itemTab.vlMaxVerbaConsFor;
	}

}