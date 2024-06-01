package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Verba;
import br.com.wmw.lavenderepda.business.domain.VerbaGrupoSaldo;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VerbaGrupoSaldoDao;
import totalcross.util.Vector;

public class VerbaGrupoSaldoService extends CrudService {

    private static VerbaGrupoSaldoService instance;

    public static VerbaGrupoSaldoService getInstance() {
        if (instance == null) {
            instance = new VerbaGrupoSaldoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return VerbaGrupoSaldoDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    	// Não faz nada
    }
    
    public double getVlTotalGrupoSaldo(ItemPedido currentItemPedido, VerbaGrupoSaldo verbaGrupoSaldo) throws SQLException {
    	double vlVerbaGrupoSaldo = verbaGrupoSaldo.vlSaldo;
    	Iterable<ItemPedido> iterator = VectorUtil.iterateOver(currentItemPedido.pedido.itemPedidoList, ItemPedido.class);
    	for (ItemPedido itemPedido : iterator) {
    		if (itemPedido.equals(currentItemPedido)) continue;
    		vlVerbaGrupoSaldo += getVlGrupoItemIfSameGrupoProduto1(itemPedido, verbaGrupoSaldo);
    	}
    	return vlVerbaGrupoSaldo;
    }
    
    public double getVlGrupoItemIfSameGrupoProduto1(ItemPedido itemPedido, VerbaGrupoSaldo verbaGrupoSaldo) throws SQLException {
    	return GrupoProduto1Service.getInstance().isSameGrupoProduto1(itemPedido, verbaGrupoSaldo) ? itemPedido.vlVerbaGrupoItem : 0;
    }
    
    public double getVlToleranciaVerGruSaldoIfSameGrupoProduto1(ItemPedido itemPedido, VerbaGrupoSaldo verbaGrupoSaldo) throws SQLException {
    	return GrupoProduto1Service.getInstance().isSameGrupoProduto1(itemPedido, verbaGrupoSaldo) ? itemPedido.vlToleranciaVerGruSaldo : 0;
    }
    
    public double getVlGrupoItemIfSameGrupoProduto1(ItemPedido itemPedido, ItemPedido anotherItemPedido) throws SQLException {
    	return GrupoProduto1Service.getInstance().isSameGrupoProduto1(itemPedido, anotherItemPedido) ? itemPedido.vlVerbaGrupoItem : 0;
    }
    
    public double getVlTotalGrupoSaldo(ItemPedido currentItemPedido) throws SQLException {
    	double vlVerbaGrupoSaldo = getVlSaldo(currentItemPedido.cdEmpresa, currentItemPedido.cdRepresentante, currentItemPedido.getProduto().cdGrupoProduto1);
    	for (ItemPedido itemPedido : VectorUtil.iterateOver(currentItemPedido.pedido.itemPedidoList, ItemPedido.class)) {
    		if (itemPedido.equals(currentItemPedido)) continue;
    		vlVerbaGrupoSaldo += getVlGrupoItemIfSameGrupoProduto1(itemPedido, currentItemPedido);
    	}
    	return vlVerbaGrupoSaldo;
    }
    
    public double getVlTotalGrupoSaldoTolerancia(ItemPedido currentItemPedido) throws SQLException {
    	double vlVerbaGrupoSaldo = getVlSaldo(currentItemPedido.cdEmpresa, currentItemPedido.cdRepresentante, currentItemPedido.getProduto().cdGrupoProduto1);
    	for (ItemPedido itemPedido : VectorUtil.iterateOver(currentItemPedido.pedido.itemPedidoList, ItemPedido.class)) {
    		if (itemPedido.equals(currentItemPedido) || itemPedido.nuSeqItemPedido > currentItemPedido.nuSeqItemPedido) continue;
    		vlVerbaGrupoSaldo += getVlGrupoItemIfSameGrupoProduto1(itemPedido, currentItemPedido);
    	}
    	return vlVerbaGrupoSaldo;
    }
    
    public double getVlSaldoErpRelatorio(String cdEmpresa, String cdRepresentante, String cdGrupoProduto1) throws SQLException {
		if (LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
			return getVlSaldoWeb(cdEmpresa, cdRepresentante, cdGrupoProduto1);
		}
		return getVlSaldoErp(cdEmpresa, cdRepresentante, cdGrupoProduto1);
	}
    
    public double getVlSaldo(String cdEmpresa, String cdRepresentante, String cdGrupoProduto1) throws SQLException {
		return getVlSaldo(cdEmpresa, cdRepresentante, cdGrupoProduto1, false);
	}

	public double getVlSaldo(String cdEmpresa, String cdRepresentante, String cdGrupoProduto1, boolean verbaPersonalizada) throws SQLException {
		if (LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
			return getVlSaldoWeb(cdEmpresa, cdRepresentante, cdGrupoProduto1) + getVlSaldoPda(cdEmpresa, cdRepresentante, cdGrupoProduto1);
		} else {
			return getVlSaldoErp(cdEmpresa, cdRepresentante, cdGrupoProduto1) + getVlSaldoPda(cdEmpresa, cdRepresentante, cdGrupoProduto1);
		}
	}

	public double getVlSaldoDisponivelValidacaoPersonalizada(String cdEmpresa, String cdRepresentante, String cdGrupoProduto1) throws SQLException {
		if (LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
			return getVlSaldoWeb(cdEmpresa, cdRepresentante, cdGrupoProduto1) + getVlSaldoPda(cdEmpresa, cdRepresentante, cdGrupoProduto1);
		} else {
			return getVlSaldoErp(cdEmpresa, cdRepresentante, cdGrupoProduto1) + getVlSaldoPda(cdEmpresa, cdRepresentante, cdGrupoProduto1);
		}
	}

	private double getVlSaldoErp(String cdEmpresa, String cdRepresentante, String cdGrupoProduto1) throws SQLException {
		VerbaGrupoSaldo verbaGrupoSaldoFilter = new VerbaGrupoSaldo();
		verbaGrupoSaldoFilter.cdEmpresa = LavenderePdaConfig.usaUnificaVerbaGrupoSaldoPorEmpresa() ? Empresa.EMPRESA_ZERO : cdEmpresa;
		verbaGrupoSaldoFilter.cdRepresentante = cdRepresentante;
		verbaGrupoSaldoFilter.cdGrupoProduto1 = cdGrupoProduto1;
		verbaGrupoSaldoFilter.flOrigemSaldo = Verba.VERBA_ERP;
		verbaGrupoSaldoFilter.cdVerbaGrupo = VerbaGrupoSaldo.CDVERBAGRUPOPADRAO;
		Vector verbaGrupoSaldoList = findAllByExample(verbaGrupoSaldoFilter);
		if (ValueUtil.isEmpty(verbaGrupoSaldoList)) {
			verbaGrupoSaldoFilter.cdEmpresa = Empresa.EMPRESA_ZERO;
			verbaGrupoSaldoList = findAllByExample(verbaGrupoSaldoFilter);
		}
		if (ValueUtil.isNotEmpty(verbaGrupoSaldoList)) {
			return ValueUtil.getDoubleValue(findColumnByRowKey(verbaGrupoSaldoFilter.getRowKey(), "VLSALDO"));
		}		
		return 0;
	}

	private double getVlSaldoPda(String cdEmpresa, String cdRepresentante, String cdGrupoProduto1) throws SQLException {
		if (LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
			VerbaGrupoSaldo verbaGrupoSaldoFilter = createVerbaGrupoSaldoPdaFilter(cdEmpresa, cdRepresentante, cdGrupoProduto1);
			Vector verbaGrupoSaldoList = findAllByExample(verbaGrupoSaldoFilter);
			if (ValueUtil.isNotEmpty(verbaGrupoSaldoList)) {
				VerbaGrupoSaldo verbaGrupoSaldo = (VerbaGrupoSaldo) verbaGrupoSaldoList.items[0]; 
				return verbaGrupoSaldo.vlSaldo;
			}
			return 0;
		} else {
			VerbaGrupoSaldo verbaGrupoSaldoFilter = new VerbaGrupoSaldo();
			verbaGrupoSaldoFilter.cdEmpresa = LavenderePdaConfig.usaUnificaVerbaGrupoSaldoPorEmpresa() ? Empresa.EMPRESA_ZERO : cdEmpresa;
			verbaGrupoSaldoFilter.cdRepresentante = cdRepresentante;
			verbaGrupoSaldoFilter.cdGrupoProduto1 = cdGrupoProduto1;
			verbaGrupoSaldoFilter.flOrigemSaldo = Verba.VERBA_PDA;
			verbaGrupoSaldoFilter.cdVerbaGrupo = VerbaGrupoSaldo.CDVERBAGRUPOPADRAO;
			return ValueUtil.getDoubleValue(findColumnByRowKey(verbaGrupoSaldoFilter.getRowKey(), "VLSALDO"));
		}
	}
	
	private double getVlSaldoWeb(String cdEmpresa, String cdRepresentante, String cdGrupoProduto1) throws SQLException {
		VerbaGrupoSaldo verbaGrupoSaldoFilter = new VerbaGrupoSaldo();
		verbaGrupoSaldoFilter.cdEmpresa = cdEmpresa;
		verbaGrupoSaldoFilter.cdRepresentante = cdRepresentante;
		verbaGrupoSaldoFilter.cdGrupoProduto1 = cdGrupoProduto1;
		verbaGrupoSaldoFilter.flOrigemSaldo = Verba.VERBA_WEB;
		verbaGrupoSaldoFilter.dtVigenciaInicial = DateUtil.getCurrentDate();
		verbaGrupoSaldoFilter.dtVigenciaFinal = DateUtil.getCurrentDate();
		Vector verbaGrupoSaldoList = findAllByExample(verbaGrupoSaldoFilter);
		if (ValueUtil.isEmpty(verbaGrupoSaldoList)) {
			verbaGrupoSaldoFilter.cdEmpresa = Empresa.EMPRESA_ZERO;
			verbaGrupoSaldoList = findAllByExample(verbaGrupoSaldoFilter);
		}
		if (ValueUtil.isNotEmpty(verbaGrupoSaldoList)) {
			VerbaGrupoSaldo verbaGrupoSaldo = (VerbaGrupoSaldo) verbaGrupoSaldoList.items[0]; 
			return verbaGrupoSaldo.vlSaldo;
		}
		return 0;
	}
	
	public VerbaGrupoSaldo getVerbaGrupoSaldoErp(String cdEmpresa, String cdRepresentante, String cdGrupoProduto1) throws SQLException {
		VerbaGrupoSaldo verbaGrupoSaldoFilter = new VerbaGrupoSaldo();
		verbaGrupoSaldoFilter.cdEmpresa = LavenderePdaConfig.usaUnificaVerbaGrupoSaldoPorEmpresa() ? Empresa.EMPRESA_ZERO : cdEmpresa;
		verbaGrupoSaldoFilter.cdRepresentante = cdRepresentante;
		verbaGrupoSaldoFilter.cdGrupoProduto1 = cdGrupoProduto1;
		verbaGrupoSaldoFilter.flOrigemSaldo = Verba.VERBA_ERP;
		verbaGrupoSaldoFilter.cdVerbaGrupo = VerbaGrupoSaldo.CDVERBAGRUPOPADRAO;
		return (VerbaGrupoSaldo) findByRowKey(verbaGrupoSaldoFilter.getRowKey());
	}

	private VerbaGrupoSaldo getVerbaGrupoSaldoPda(String cdEmpresa, String cdRepresentante, String cdGrupoProduto1) throws SQLException {
		if (LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
			VerbaGrupoSaldo verbaGrupoSaldoFilter = createVerbaGrupoSaldoPdaFilter(cdEmpresa, cdRepresentante, cdGrupoProduto1);
			Vector verbaGrupoSaldoList = findAllByExample(verbaGrupoSaldoFilter);
			return ValueUtil.isNotEmpty(verbaGrupoSaldoList) ? (VerbaGrupoSaldo) verbaGrupoSaldoList.items[0] : null;
		} else {
			VerbaGrupoSaldo verbaGrupoSaldoFilter = new VerbaGrupoSaldo();
			verbaGrupoSaldoFilter.cdEmpresa = LavenderePdaConfig.usaUnificaVerbaGrupoSaldoPorEmpresa() ? Empresa.EMPRESA_ZERO : cdEmpresa;
			verbaGrupoSaldoFilter.cdRepresentante = cdRepresentante;
			verbaGrupoSaldoFilter.cdGrupoProduto1 = cdGrupoProduto1;
			verbaGrupoSaldoFilter.flOrigemSaldo = Verba.VERBA_PDA;
			verbaGrupoSaldoFilter.cdVerbaGrupo = VerbaGrupoSaldo.CDVERBAGRUPOPADRAO;
			return (VerbaGrupoSaldo) findByRowKey(verbaGrupoSaldoFilter.getRowKey());
		}
	}
	
	public boolean hasVerbaGrupoSaldoWeb(final ItemPedido itemPedido) throws SQLException {
		if (itemPedido != null) {
			return getVerbaGrupoSaldoWeb(itemPedido) != null;
		}
		return false;
	}
	
	public VerbaGrupoSaldo getVerbaGrupoSaldoWeb(final ItemPedido itemPedido) throws SQLException {
		if (itemPedido != null) {
			VerbaGrupoSaldo verbaGrupoSaldoFilter = new VerbaGrupoSaldo();
			verbaGrupoSaldoFilter.cdEmpresa = itemPedido.cdEmpresa;
			verbaGrupoSaldoFilter.cdRepresentante = itemPedido.cdRepresentante;
			verbaGrupoSaldoFilter.cdGrupoProduto1 = itemPedido.getProduto().cdGrupoProduto1;
			verbaGrupoSaldoFilter.flOrigemSaldo = Verba.VERBA_WEB;
			verbaGrupoSaldoFilter.dtVigenciaInicial = DateUtil.getCurrentDate();
			verbaGrupoSaldoFilter.dtVigenciaFinal = DateUtil.getCurrentDate();
			Vector verbaGrupoSaldoList = findAllByExample(verbaGrupoSaldoFilter);
			if (ValueUtil.isEmpty(verbaGrupoSaldoList)) {
				verbaGrupoSaldoFilter.cdEmpresa = Empresa.EMPRESA_ZERO;
				verbaGrupoSaldoList = findAllByExample(verbaGrupoSaldoFilter);
			}
			return ValueUtil.isNotEmpty(verbaGrupoSaldoList) ? (VerbaGrupoSaldo) verbaGrupoSaldoList.items[0] : null;
		}
		return null;
	}
	
	public Vector findAllVerbaGrupoSaldoWeb(final String cdEmpresa, final String cdRepresentante) throws SQLException {
		return findAllVerbaGrupoSaldoWeb(cdEmpresa, cdRepresentante, null, null);
	}

	public Vector findAllVerbaGrupoSaldoWeb(final String cdEmpresa, final String cdRepresentante, final String cdGrupoProduto1, final String[] cdGrupoProduto1Array) throws SQLException {
		VerbaGrupoSaldo verbaGrupoSaldoFilter = new VerbaGrupoSaldo();
		verbaGrupoSaldoFilter.cdEmpresa = cdEmpresa;
		verbaGrupoSaldoFilter.cdRepresentante = cdRepresentante;
		verbaGrupoSaldoFilter.flOrigemSaldo = Verba.VERBA_WEB;
		verbaGrupoSaldoFilter.dtVigenciaInicial = DateUtil.getCurrentDate();
		verbaGrupoSaldoFilter.dtVigenciaFinal = DateUtil.getCurrentDate();
		verbaGrupoSaldoFilter.sortAtributte = VerbaGrupoSaldo.NOMECOLUNADTVIGENCIAFINAL;
		verbaGrupoSaldoFilter.sortAsc = ValueUtil.VALOR_SIM;
		if (cdGrupoProduto1 != null) {
			verbaGrupoSaldoFilter.cdGrupoProduto1 = cdGrupoProduto1;
		}
		if (cdGrupoProduto1Array != null) {
			verbaGrupoSaldoFilter.cdGrupoProduto1Array = cdGrupoProduto1Array;
		}
		Vector verbaGrupoSaldoList = findAllByExample(verbaGrupoSaldoFilter);
		verbaGrupoSaldoFilter.cdEmpresa = Empresa.EMPRESA_ZERO;
		Vector verbaGrupoSaldoEmpZeroList = findAllByExample(verbaGrupoSaldoFilter);
		return VectorUtil.concatVectors(verbaGrupoSaldoList, verbaGrupoSaldoEmpZeroList);
	}
	
	public Vector findAllVerbaGrupoSaldoErp(final String cdEmpresa, final String cdRepresentante, String cdGrupoProduto1, String[] cdGrupoProduto1Array) throws SQLException {
		VerbaGrupoSaldo verbaGrupoSaldoFilter = new VerbaGrupoSaldo();
		verbaGrupoSaldoFilter.cdEmpresa = LavenderePdaConfig.usaUnificaVerbaGrupoSaldoPorEmpresa() ? Empresa.EMPRESA_ZERO : cdEmpresa;
		verbaGrupoSaldoFilter.cdRepresentante = cdRepresentante;
		verbaGrupoSaldoFilter.flOrigemSaldo = Verba.VERBA_ERP;
		if (cdGrupoProduto1 != null) {
			verbaGrupoSaldoFilter.cdGrupoProduto1 = cdGrupoProduto1;
		}
		if (cdGrupoProduto1Array != null) {
			verbaGrupoSaldoFilter.cdGrupoProduto1Array = cdGrupoProduto1Array;
		}
		return findAllByExample(verbaGrupoSaldoFilter);
	}
	
	public Vector findAllVerbaGrupoSaldoForAvisoTerminoVigencia(final String cdEmpresa, final String cdRepresentante) throws SQLException {
		Vector verbaGrupoSaldoFinalList = new Vector();
		Vector verbaGrupoSaldoList = findAllVerbaGrupoSaldoWeb(cdEmpresa, cdRepresentante);
		if (ValueUtil.isNotEmpty(verbaGrupoSaldoList)) {
			for (int i = 0; i < verbaGrupoSaldoList.size(); i++) {
				VerbaGrupoSaldo verbaGrupoSaldo = (VerbaGrupoSaldo) verbaGrupoSaldoList.items[i];
				if (ValueUtil.isNotEmpty(verbaGrupoSaldo.dtVigenciaFinal)) {
					int nuDiasRestantesAvisoVigencia = LavenderePdaConfig.nuDiasRestantesAvisoSaldoVerbaGrupo;
					if (verbaGrupoSaldo.isVigente() && DateUtil.getDaysBetween(verbaGrupoSaldo.dtVigenciaFinal, DateUtil.getCurrentDate()) <= nuDiasRestantesAvisoVigencia) {
						verbaGrupoSaldoFinalList.addElement(verbaGrupoSaldo);
					}
				}
			}
		}
		return verbaGrupoSaldoFinalList;
	}
	

	public void insertVlSaldo(Pedido pedido, ItemPedido itemPedido, boolean verbaPersonalizada) throws SQLException {
		VerbaGrupoSaldo verbaGrupoSaldo = getVerbaGrupoSaldoPda(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getProduto().cdGrupoProduto1);
		if (verbaGrupoSaldo != null) {
			verbaGrupoSaldo.vlSaldo += verbaPersonalizada ? Math.min(0d, itemPedido.vlVerbaGrupoItem) : itemPedido.vlVerbaItem;
			verbaGrupoSaldo.vlTolerancia += verbaPersonalizada ? itemPedido.vlToleranciaVerGruSaldo : 0;
			verbaGrupoSaldo.vlTolerancia = verbaGrupoSaldo.vlTolerancia > 0 ? 0d : verbaGrupoSaldo.vlTolerancia;
			if (LavenderePdaConfig.geraVerbaPositiva) {
				verbaGrupoSaldo.vlSaldo += verbaPersonalizada ? Math.max(itemPedido.vlVerbaGrupoItem, 0d) : itemPedido.vlVerbaItemPositivo;
			}
			update(verbaGrupoSaldo);
		} else if (ValueUtil.isNotEmpty(itemPedido.getProduto().cdGrupoProduto1)) {
			VerbaGrupoSaldo verbaGrupoSaldoFilter = new VerbaGrupoSaldo();
			verbaGrupoSaldoFilter.cdRepresentante = itemPedido.cdRepresentante;
			verbaGrupoSaldoFilter.cdGrupoProduto1 = itemPedido.getProduto().cdGrupoProduto1;
			verbaGrupoSaldoFilter.flOrigemSaldo = Verba.VERBA_PDA;
			if (LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
				verbaGrupoSaldoFilter.cdEmpresa = LavenderePdaConfig.usaVerbaUnificada ? Empresa.EMPRESA_ZERO : itemPedido.cdEmpresa;	
			} else {
				verbaGrupoSaldoFilter.cdVerbaGrupo = VerbaGrupoSaldo.CDVERBAGRUPOPADRAO;
				verbaGrupoSaldoFilter.cdEmpresa = LavenderePdaConfig.usaUnificaVerbaGrupoSaldoPorEmpresa() ? Empresa.EMPRESA_ZERO : itemPedido.cdEmpresa;
			}
			verbaGrupoSaldoFilter.vlSaldo += verbaPersonalizada ? Math.min(0d, itemPedido.vlVerbaGrupoItem) : itemPedido.vlVerbaItem;
			verbaGrupoSaldoFilter.vlTolerancia += verbaPersonalizada ? itemPedido.vlToleranciaVerGruSaldo : 0;
			verbaGrupoSaldoFilter.vlTolerancia = verbaGrupoSaldoFilter.vlTolerancia > 0 ? 0d : verbaGrupoSaldoFilter.vlTolerancia;
			if (LavenderePdaConfig.geraVerbaPositiva) {
				verbaGrupoSaldoFilter.vlSaldo += verbaPersonalizada ? Math.max(itemPedido.vlVerbaGrupoItem, 0d) : itemPedido.vlVerbaItemPositivo;
			}
			insert(verbaGrupoSaldoFilter);
		}
	}

	public void updateVlSaldo(Pedido pedido, ItemPedido itemPedido, boolean verbaPersonalizada) throws SQLException {
		VerbaGrupoSaldo verbaGrupoSaldo = getVerbaGrupoSaldoPda(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getProduto().cdGrupoProduto1);
		if (verbaGrupoSaldo == null) return;
		verbaGrupoSaldo.vlSaldo -= verbaPersonalizada ? itemPedido.vlVerbaGrupoOld : itemPedido.vlVerbaItemOld;
		verbaGrupoSaldo.vlSaldo += verbaPersonalizada ? itemPedido.vlVerbaGrupoItem : itemPedido.vlVerbaItem;
		verbaGrupoSaldo.vlTolerancia -= verbaPersonalizada ? itemPedido.vlToleranciaVerGruSaldoOld : 0d;
		verbaGrupoSaldo.vlTolerancia += verbaPersonalizada ? itemPedido.vlToleranciaVerGruSaldo : 0d;
		if (LavenderePdaConfig.geraVerbaPositiva && !verbaPersonalizada) {
			verbaGrupoSaldo.vlSaldo -= itemPedido.vlVerbaItemPositivoOld;
			verbaGrupoSaldo.vlSaldo += itemPedido.vlVerbaItemPositivo;
		}
		update(verbaGrupoSaldo);
		itemPedido.vlVerbaGrupoOld = itemPedido.vlVerbaGrupoItem;
	}

	public void deleteVlSaldo(Pedido pedido, ItemPedido itemPedido, boolean verbaPersonalizada) throws SQLException {
		VerbaGrupoSaldo verbaGrupoSaldo = getVerbaGrupoSaldoPda(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getProduto().cdGrupoProduto1);
		if (verbaGrupoSaldo == null) return;
		verbaGrupoSaldo.vlTolerancia -= verbaPersonalizada ? itemPedido.vlToleranciaVerGruSaldo : 0d;
		verbaGrupoSaldo.vlSaldo -= verbaPersonalizada ? itemPedido.vlVerbaGrupoItem : itemPedido.vlVerbaItemOld;
		if (LavenderePdaConfig.geraVerbaPositiva && !verbaPersonalizada) {
			verbaGrupoSaldo.vlSaldo -= itemPedido.vlVerbaItemPositivoOld;
		}
		update(verbaGrupoSaldo);
	}

	public void recalculateAndUpdateVerbaGrupoSaldoPda() throws SQLException {
		LoadingBoxWindow mbProcessando = UiUtil.createProcessingMessage();
    	mbProcessando.popupNonBlocking();
    	try {
    		VerbaGrupoSaldo verbaGrupoSaldoFilter = new VerbaGrupoSaldo();
    		verbaGrupoSaldoFilter.cdEmpresa = LavenderePdaConfig.usaVerbaUnificada && LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto || LavenderePdaConfig.usaUnificaVerbaGrupoSaldoPorEmpresa() ? Empresa.EMPRESA_ZERO : SessionLavenderePda.cdEmpresa;;
    		verbaGrupoSaldoFilter.flOrigemSaldo = Verba.VERBA_PDA;
    		deleteAllByExample(verbaGrupoSaldoFilter);
    		Pedido pedidoFilter = new Pedido();
    		pedidoFilter.cdStatusPedidoDif = LavenderePdaConfig.cdStatusPedidoTransmitido;
    		Vector pedidoList = PedidoPdbxDao.getInstance().findAllPedidosPdaResumido(pedidoFilter);
    		for (int i = 0; i < pedidoList.size(); i++) {
    			Pedido pedido = (Pedido) pedidoList.items[i];
	    		if (pedido.getTipoPedido() != null && (pedido.isSimulaControleVerba() || pedido.isIgnoraControleVerba() || pedido.isPedidoCriticoOuConversaoFob()) || VerbaGrupoSaldoService.getInstance().ignoraValidacaoVerbaSaldoPorGrupoProduto(pedido)) {
	    			continue;
	    		}
	    		PedidoService.getInstance().findItemPedidoList(pedido);
	    		Vector itemPedidoList = pedido.itemPedidoList;
	    		for (int j = 0; j < itemPedidoList.size(); j++) {
	    			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[j];
	    			insertVlSaldo(pedido, itemPedido, false);
				}
			}
		} finally {
			mbProcessando.unpop();
		}
	}

	public void updateVerbaGrupoSaldoItemPedido(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		VerbaGrupoSaldo verbaGrupoSaldoPda = getVerbaGrupoSaldoPda(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getProduto().cdGrupoProduto1);
		VerbaGrupoSaldo verbaGrupoSaldoErp = LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto ? getVerbaGrupoSaldoWeb(itemPedido) : getVerbaGrupoSaldoErp(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getProduto().cdGrupoProduto1);
		if (verbaGrupoSaldoPda != null) {
			verbaGrupoSaldoPda.vlSaldo -= itemPedido.vlVerbaItem;
			if (LavenderePdaConfig.geraVerbaPositiva) {
				verbaGrupoSaldoPda.vlSaldo -= itemPedido.vlVerbaItemPositivo;
			}
			update(verbaGrupoSaldoPda);
		}
		//-
		if (verbaGrupoSaldoErp != null) {
			verbaGrupoSaldoErp.vlSaldo += itemPedido.vlVerbaItem;
			if (LavenderePdaConfig.geraVerbaPositiva) {
				verbaGrupoSaldoErp.vlSaldo += itemPedido.vlVerbaItemPositivo;
			}
			update(verbaGrupoSaldoErp);
		}
	}
	
	public double getVlVerbaDisponivel(ItemPedido itemPedido) throws SQLException {
		return getVlSaldo(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getProduto().cdGrupoProduto1, false);
	}
	
	public double getVlVerbaDisponivel(ItemPedido itemPedido, boolean verbaPersonalizada) throws SQLException {
		return getVlSaldo(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getProduto().cdGrupoProduto1, verbaPersonalizada);
	}
	
	public double getVlToleranciaDisponivel(final ItemPedido currentItemPedido, final VerbaGrupoSaldo verbaGrupoSaldo, final double vlSaldo) throws SQLException {
		final Pedido pedido = currentItemPedido.pedido;
		double vlTolerancia = getVlTolerancia(pedido.cdEmpresa, pedido.cdRepresentante, verbaGrupoSaldo.cdGrupoProduto1);
		if (LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
			vlTolerancia += getVlToleranciaVerGruSaldoIfSameGrupoProduto1(currentItemPedido, verbaGrupoSaldo);
			for (ItemPedido itemPedido : VectorUtil.iterateOver(pedido.itemPedidoList, ItemPedido.class)) {
				if (itemPedido.equals(currentItemPedido)) continue;
				vlTolerancia += getVlToleranciaVerGruSaldoIfSameGrupoProduto1(itemPedido, verbaGrupoSaldo);
			}
		}
		return vlTolerancia;
	}

	public double getVlVerbaDisponivelValidacaoPersonalizada(ItemPedido itemPedido) throws SQLException {
    	return getVlSaldoDisponivelValidacaoPersonalizada(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getProduto().cdGrupoProduto1);
	}

	public HashMap<String, VerbaGrupoSaldo> mesclaHashComVerbaGruposNaoExistentes(VerbaGrupoSaldo filter, HashMap<String, VerbaGrupoSaldo> hashGrupo) throws SQLException {
		return VerbaGrupoSaldoDao.getInstance().mesclaHashComGruposNaoExistentes(filter, hashGrupo);
	}
	
	public double getVlToleranciaDisponivel(ItemPedido itemPedido) throws SQLException {
		return getVlTolerancia(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getProduto().cdGrupoProduto1);
	}
	
	public double getVlTolerancia(String cdEmpresa, String cdRepresentante, String cdGrupoProduto1) throws SQLException {
		if (LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
			double vlToleranciaWeb = getVlToleranciaWeb(cdEmpresa, cdRepresentante, cdGrupoProduto1);
			return vlToleranciaWeb > 0 ? vlToleranciaWeb + getVlToleranciaPda(cdEmpresa, cdRepresentante, cdGrupoProduto1) : 0;
		} else {
			double vlToleranciaErp = getVlToleranciaErp(cdEmpresa, cdRepresentante, cdGrupoProduto1);
			return vlToleranciaErp > 0 ? vlToleranciaErp + getVlToleranciaPda(cdEmpresa, cdRepresentante, cdGrupoProduto1) : 0;
		}
	}
	
	public double getVlToleranciaErp(String cdEmpresa, String cdRepresentante, String cdGrupoProduto1) throws SQLException {
		VerbaGrupoSaldo verbaGrupoSaldoFilter = new VerbaGrupoSaldo();
		verbaGrupoSaldoFilter.cdEmpresa = LavenderePdaConfig.usaUnificaVerbaGrupoSaldoPorEmpresa() ? Empresa.EMPRESA_ZERO : cdEmpresa;
		verbaGrupoSaldoFilter.cdRepresentante = cdRepresentante;
		verbaGrupoSaldoFilter.cdGrupoProduto1 = cdGrupoProduto1;
		verbaGrupoSaldoFilter.flOrigemSaldo = Verba.VERBA_ERP;
		verbaGrupoSaldoFilter.cdVerbaGrupo = VerbaGrupoSaldo.CDVERBAGRUPOPADRAO;
		return ValueUtil.getDoubleValue(findColumnByRowKey(verbaGrupoSaldoFilter.getRowKey(), "VLTOLERANCIA"));
	}
	
	public double getVltoleranciaInicial(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
			return getVlToleranciaWeb(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getProduto().cdGrupoProduto1);
		} 
		return getVlToleranciaErp(itemPedido.cdEmpresa, itemPedido.cdRepresentante,  itemPedido.getProduto().cdGrupoProduto1);
	}
	
	private double getVlToleranciaPda(String cdEmpresa, String cdRepresentante, String cdGrupoProduto1) throws SQLException {
		VerbaGrupoSaldo verbaGrupoSaldoFilter = createVerbaGrupoSaldoPdaFilter(cdEmpresa, cdRepresentante, cdGrupoProduto1);
		if (LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
			Vector verbaGrupoSaldoList = findAllByExample(verbaGrupoSaldoFilter);
			if (ValueUtil.isNotEmpty(verbaGrupoSaldoList)) {
				VerbaGrupoSaldo verbaGrupoSaldo = (VerbaGrupoSaldo) verbaGrupoSaldoList.items[0]; 
				return verbaGrupoSaldo.vlTolerancia;
			}
			return 0;
		} else {
			return ValueUtil.getDoubleValue(findColumnByRowKey(verbaGrupoSaldoFilter.getRowKey(), "VLTOLERANCIA"));
		}
	}

	private VerbaGrupoSaldo createVerbaGrupoSaldoPdaFilter(String cdEmpresa, String cdRepresentante, String cdGrupoProduto1) {
		VerbaGrupoSaldo verbaGrupoSaldoFilter = new VerbaGrupoSaldo();
		verbaGrupoSaldoFilter.cdRepresentante = cdRepresentante;
		verbaGrupoSaldoFilter.cdGrupoProduto1 = cdGrupoProduto1;
		verbaGrupoSaldoFilter.flOrigemSaldo = Verba.VERBA_PDA;
		if (LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
			verbaGrupoSaldoFilter.cdEmpresa =  LavenderePdaConfig.usaVerbaUnificada ? Empresa.EMPRESA_ZERO : cdEmpresa;
			verbaGrupoSaldoFilter.cdVerbaGrupo = VerbaGrupoSaldo.CDVERBAGRUPOPADRAO;
		} else {
			verbaGrupoSaldoFilter.cdEmpresa = LavenderePdaConfig.usaUnificaVerbaGrupoSaldoPorEmpresa() ? Empresa.EMPRESA_ZERO : cdEmpresa;
		}
		return verbaGrupoSaldoFilter;
	}
	
	private double getVlToleranciaWeb(String cdEmpresa, String cdRepresentante, String cdGrupoProduto1) throws SQLException {
		VerbaGrupoSaldo verbaGrupoSaldoFilter = new VerbaGrupoSaldo();
		verbaGrupoSaldoFilter.cdEmpresa = cdEmpresa;
		verbaGrupoSaldoFilter.cdRepresentante = cdRepresentante;
		verbaGrupoSaldoFilter.cdGrupoProduto1 = cdGrupoProduto1;
		verbaGrupoSaldoFilter.flOrigemSaldo = Verba.VERBA_WEB;
		verbaGrupoSaldoFilter.dtVigenciaInicial = DateUtil.getCurrentDate();
		verbaGrupoSaldoFilter.dtVigenciaFinal = DateUtil.getCurrentDate();
		Vector verbaGrupoSaldoList = findAllByExample(verbaGrupoSaldoFilter);
		if (ValueUtil.isEmpty(verbaGrupoSaldoList)) {
			verbaGrupoSaldoFilter.cdEmpresa = Empresa.EMPRESA_ZERO;
			verbaGrupoSaldoList = findAllByExample(verbaGrupoSaldoFilter);
		}
		if (ValueUtil.isNotEmpty(verbaGrupoSaldoList)) {
			VerbaGrupoSaldo verbaGrupoSaldo = (VerbaGrupoSaldo) verbaGrupoSaldoList.items[0]; 
			return verbaGrupoSaldo.vlTolerancia;
		}
		return 0;
	}

	public double getVlVerbaPositivaByGrupoFromItemPedidoList(Vector itemPedidoList, String cdGrupoProduto1) throws SQLException {
		int size = itemPedidoList.size();
		double somaVerba = 0;
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			if (ValueUtil.valueEquals(itemPedido.getProduto().cdGrupoProduto1, cdGrupoProduto1)) {
				somaVerba += itemPedido.vlVerbaItemPositivo;
			}
		}
		return somaVerba;
	}
	
	public boolean ignoraValidacaoVerbaSaldoPorGrupoProduto(final Pedido pedido) throws SQLException {
		return LavenderePdaConfig.usaConfigVerbaSaldoPorGrupoProduto() && (pedido.getCliente().isFlIgnoraVerbaGrpProd() || DivisaoVendaService.getInstance().isIgnoraVerbaGrupoSaldoPorDivisaoVenda(pedido));
	}
	
	public boolean isUsaVerbaSaldoPorGrupoProduto(final Pedido pedido) throws SQLException {
		return LavenderePdaConfig.usaConfigVerbaSaldoPorGrupoProduto() && !pedido.getCliente().isFlIgnoraVerbaGrpProd() && !DivisaoVendaService.getInstance().isIgnoraVerbaGrupoSaldoPorDivisaoVenda(pedido);
	}
	
	public void estornaVerbaSaldo(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		estornaVerbaSaldo(pedido, itemPedido, false);
	}
	
	public void estornaVerbaSaldo(Pedido pedido, ItemPedido itemPedido, boolean saldoVerbaExtrapolado) {
		pedido.vlVerbaPedido -= itemPedido.vlVerbaItem;
		itemPedido.vlVerbaItem = 0d;
		try {
			PedidoService.getInstance().updateColumn(pedido.getRowKey(), "vlVerbaPedido", 0d, Types.DECIMAL);
			ItemPedidoService.getInstance().updateColumn(itemPedido.getRowKey(), "vlVerbaItem", 0d, Types.DECIMAL);
			if (!LavenderePdaConfig.isUsaMotivosPendenciaVerbaDeGrupoExtrapolada() && saldoVerbaExtrapolado) {
				itemPedido.vlPctDesconto = 0d;
				ItemPedidoService.getInstance().updateColumn(itemPedido.getRowKey(), "vlPctDesconto", 0d, Types.DECIMAL);
			}
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}
	
	public double getSaldoConsumidoVerbaGrupoPedidosAbertos(final ItemPedido itemPedido) throws SQLException {
		return getConsumoVerbaPedidosAbertos(itemPedido, false)[0];
	}
	
	public double[] getConsumoVerbaGrupoPedidosAbertos(final ItemPedido itemPedido, final VerbaGrupoSaldo verbaGrupoSaldo, boolean buscarTolerancia) throws SQLException {
		return getConsumoVerbaPedidosAbertos(itemPedido, verbaGrupoSaldo, buscarTolerancia);
	}
	
	private double[] getConsumoVerbaPedidosAbertos(final ItemPedido itemPedido, boolean buscarTolerancia) throws SQLException {
		return getConsumoVerbaPedidosAbertos(itemPedido, null, buscarTolerancia);
	}
	
	private double[] getConsumoVerbaPedidosAbertos(final ItemPedido itemPedido, VerbaGrupoSaldo verbaGrupoSaldo, boolean buscarTolerancia) throws SQLException {
		final ItemPedido itemPedidoFilter = createBuscaItensConsumindoVerbaFilter(itemPedido);
		if (verbaGrupoSaldo != null) {
			itemPedidoFilter.cdGrupoProduto1 = verbaGrupoSaldo.cdGrupoProduto1;
		}
		if (buscarTolerancia) {
			return ItemPedidoService.getInstance().sumColumnsByExample(itemPedidoFilter, "tb.VLVERBAGRUPOITEM", "tb.VLTOLERANCIAVERGRUSALDO");
		} else {
			return ItemPedidoService.getInstance().sumColumnsByExample(itemPedidoFilter, "tb.VLVERBAGRUPOITEM");
		}
	}
	
	public ItemPedido createBuscaItensConsumindoVerbaFilter(final ItemPedido itemPedido) throws SQLException {
		final ItemPedido itemPedidoFilter = new ItemPedido();
		if (!LavenderePdaConfig.usaUnificaVerbaGrupoSaldoPorEmpresa()) {
			itemPedidoFilter.cdEmpresa = itemPedido.cdEmpresa;
		}
		itemPedidoFilter.cdRepresentante = itemPedido.cdRepresentante;
		itemPedidoFilter.flOrigemPedido = itemPedido.pedido.flOrigemPedido;
		itemPedidoFilter.cdGrupoProduto1 = itemPedido.getProduto().cdGrupoProduto1;
		itemPedidoFilter.nuPedido = itemPedido.pedido.nuPedido;
		itemPedidoFilter.buscarItensConsumindoVerba = true;
		return itemPedidoFilter;
	}
	
}
