package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.HashMap;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Verba;
import br.com.wmw.lavenderepda.business.domain.VerbaSaldo;
import br.com.wmw.lavenderepda.business.validation.VerbaSaldoPedidoExtrapoladoException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VerbaSaldoPdbxDao;
import br.com.wmw.lavenderepda.sync.async.VerbaSaldoAbertaRunnable;
import totalcross.util.Date;
import totalcross.util.Vector;

public class VerbaSaldoService extends CrudService {

    private static VerbaSaldoService instance;

    private VerbaSaldoService() {
        //--
    }

    public static VerbaSaldoService getInstance() {
        if (instance == null) {
            instance = new VerbaSaldoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return VerbaSaldoPdbxDao.getInstance();
    }


    //@Override
    protected void setDadosAlteracao(BaseDomain domain) {
    	//
    }


    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {

    }

    public double getVlSaldo(String cdContaCorrente) throws SQLException {
    	return getVlSaldo(cdContaCorrente, null);
    }

    public double getVlSaldo(String cdContaCorrente, Date dtEmissaoPedido) throws SQLException {
    	VerbaSaldo verbaSaldoFilter = new VerbaSaldo();
    	verbaSaldoFilter.setCdEmpresa(SessionLavenderePda.cdEmpresa);
    	verbaSaldoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
			Date dtDate = ConfigInternoService.getInstance().getDtServidor();
			verbaSaldoFilter.dtVigenciaInicial = dtDate != null ? dtDate : new Date();
		}
		verbaSaldoFilter.cdContaCorrente = cdContaCorrente;
    	Vector verbaSaldoList = findAllByExample(verbaSaldoFilter);
    	//--
		if (!LavenderePdaConfig.usaProvisionamentoConsumoVerbaSaldo() && LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco() && ValueUtil.isNotEmpty(dtEmissaoPedido)) {
			int size = verbaSaldoList.size();
	        for (int i = 0; i < size; i++) {
    			VerbaSaldo verbaSaldo = (VerbaSaldo) verbaSaldoList.items[i];
    			if (verbaSaldo.dtVigenciaInicial.isAfter(dtEmissaoPedido) || verbaSaldo.dtVigenciaFinal.isBefore(dtEmissaoPedido)) {
    				throw new ValidationException(MessageUtil.getMessage(Messages.VERBASALDO_MSG_SALDO_VERBA_VIGENCIA_DTEMISSAO, new String[]{"" + verbaSaldo.dtVigenciaInicial, "" + verbaSaldo.dtVigenciaFinal, "" + dtEmissaoPedido}));
    			}
    		}
		}
		//--
    	if (ValueUtil.isEmpty(verbaSaldoList)) {
    		return 0;
    	} else {
    		boolean hasVerbaSaldoPda = false;
    		int size = verbaSaldoList.size();
            for (int i = 0; i < size; i++) {
            	VerbaSaldo verbaSaldo = (VerbaSaldo) verbaSaldoList.items[i];
    			if (Verba.VERBA_PDA.equals(verbaSaldo.flOrigemSaldo)) {
    				hasVerbaSaldoPda = true;
    				break;
    			}
    		}
    		//Se não tiver verbasaldo pda, cria-se uma
    		if (!hasVerbaSaldoPda && (size > 0)) {
    			VerbaSaldo verbaSaldoErp = (VerbaSaldo) verbaSaldoList.items[0];
    			VerbaSaldo verbaSaldoPda = new VerbaSaldo();
    			verbaSaldoPda.setCdEmpresa(SessionLavenderePda.cdEmpresa);
    			verbaSaldoPda.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    			verbaSaldoPda.cdContaCorrente = cdContaCorrente;
    			if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.informaVerbaManual || LavenderePdaConfig.isPermiteBonificarProdutoPedidoUsandoVerba() || LavenderePdaConfig.usaPedidoBonificacao() || LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao) {
        			verbaSaldoPda.cdContaCorrente = LavenderePdaConfig.permiteHistoricoSaldoAntigo() ? verbaSaldoErp.cdContaCorrente : VerbaSaldo.CDCONTACORRENTE_PADRAO;
        			if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
        				verbaSaldoPda.cdContaCorrente = verbaSaldoErp.cdContaCorrente;
        				verbaSaldoPda.dtVigenciaInicial = verbaSaldoErp.dtVigenciaInicial;
        				verbaSaldoPda.dtVigenciaFinal = verbaSaldoErp.dtVigenciaFinal;
        			}
    			}
    			verbaSaldoPda.flOrigemSaldo = Verba.VERBA_PDA;
    			verbaSaldoPda.dtSaldo = new Date();
    			verbaSaldoPda.vlSaldo = 0;
    			VerbaSaldo vs = (VerbaSaldo) findByRowKey(verbaSaldoPda.getRowKey());
    			if (vs == null) {
    				insert(verbaSaldoPda);
    			} else if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco() && isDataVigenciaVazia(vs)) {
				    atualizaVigenciaVerbaApp(verbaSaldoErp, vs);
			    }
    		}
    		//--
    		double vlSaldoPda = 0;
    		double vlSaldoErpInicial = 0;
    		for (int  i = 0; i < size; i++) {
    			VerbaSaldo verbaSaldo = (VerbaSaldo) verbaSaldoList.items[i];
    			if (Verba.VERBA_PDA.equals(verbaSaldo.flOrigemSaldo)) {
    				vlSaldoPda = verbaSaldo.vlSaldo;
    			} else if (Verba.VERBA_ERP.equals(verbaSaldo.flOrigemSaldo)) {
    				vlSaldoErpInicial = verbaSaldo.vlSaldo;
    			}
    		}
    		return vlSaldoErpInicial + vlSaldoPda;
    	}
    }

	private void atualizaVigenciaVerbaApp(VerbaSaldo verbaSaldoErp, VerbaSaldo vs) {
		vs.dtVigenciaInicial = verbaSaldoErp.dtVigenciaInicial;
		vs.dtVigenciaFinal = verbaSaldoErp.dtVigenciaFinal;
		try {
			VerbaSaldoPdbxDao.getInstance().updateVigenciaSaldoApp(vs);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	private boolean isDataVigenciaVazia(VerbaSaldo vs) {
		return (ValueUtil.isEmpty(vs.dtVigenciaInicial) || ValueUtil.isEmpty(vs.dtVigenciaFinal));
	}

	public VerbaSaldo getVerbaSaldoErpVingenciaAtual(String cdRepresentante) throws SQLException {
		if (ValueUtil.isNotEmpty(cdRepresentante)) {
	    	VerbaSaldo verbaSaldoFilter = new VerbaSaldo();
	    	verbaSaldoFilter.setCdEmpresa(SessionLavenderePda.cdEmpresa);
	    	verbaSaldoFilter.cdRepresentante = cdRepresentante;
	    	verbaSaldoFilter.flOrigemSaldo = Verba.VERBA_ERP;
			if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
				Date dtDate = ConfigInternoService.getInstance().getDtServidor();
				verbaSaldoFilter.dtVigenciaInicial = dtDate != null ? dtDate : new Date();
			}
	    	Vector verbaSaldoList = findAllByExample(verbaSaldoFilter);
			//--
			if (ValueUtil.isNotEmpty(verbaSaldoList)) {
				VerbaSaldo verbaSaldoFinal = new VerbaSaldo();
				//--
				double vlSaldo = 0d;
				for (int i = 0; i < verbaSaldoList.size(); i++) {
					VerbaSaldo verbaSaldo = (VerbaSaldo)verbaSaldoList.items[i];
					vlSaldo += verbaSaldo.vlSaldo;
					verbaSaldoFinal.dtVigenciaInicial = verbaSaldo.dtVigenciaInicial;
					verbaSaldoFinal.dtVigenciaFinal = verbaSaldo.dtVigenciaFinal;
					verbaSaldoFinal.vlSaldoInicial = verbaSaldo.vlSaldoInicial;
				}
				verbaSaldoFinal.vlSaldo = vlSaldo;
				return verbaSaldoFinal;
			}
		}
		return null;
	}

    private VerbaSaldo getVerbaSaldo(VerbaSaldo verbaSaldoFilter, String flOrigemSaldo) throws SQLException {
    	verbaSaldoFilter.setCdEmpresa(SessionLavenderePda.cdEmpresa);
		verbaSaldoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		verbaSaldoFilter.flOrigemSaldo = flOrigemSaldo;
		return (VerbaSaldo) findByRowKey(verbaSaldoFilter.getRowKey());
    }

    public void insertVlSaldo(Pedido pedido, ItemPedido itemPedido, boolean pedidoTransmitido) throws SQLException {
    	VerbaSaldo verbaSaldoFilter = new VerbaSaldo();
    	// Busca qual cdContaCorrente está ativa (seja por vigencia, ou sempre '0')
    	setCdContaCorrenteVerbaItemPedido(itemPedido, verbaSaldoFilter);
    	if (ValueUtil.isEmpty(verbaSaldoFilter.cdContaCorrente)) { 
    		return;
    	}
    	// ----------------------------
    	VerbaSaldo verbaSaldo = getVerbaSaldo(verbaSaldoFilter, Verba.VERBA_PDA);
    	insertVlSaldo(verbaSaldo, verbaSaldoFilter, itemPedido);
    	if (LavenderePdaConfig.apresentaConsumoVerbaDePedidoNaoTransmitido && !pedidoTransmitido) {
    		verbaSaldo = getVerbaSaldo(verbaSaldoFilter, VerbaSaldo.FLORIGEM_ABERTO);
    		insertVlSaldo(verbaSaldo, verbaSaldoFilter, itemPedido);
    	}
    }
    
    private void insertVlSaldo(VerbaSaldo verbaSaldo, VerbaSaldo verbaSaldoFilter, ItemPedido itemPedido) throws SQLException {
    	boolean geraVerbaPositiva = LavenderePdaConfig.geraVerbaPositiva;
    	if (verbaSaldo != null) {
    		setCdContaCorrenteVerbaItemPedido(itemPedido, verbaSaldo);
			verbaSaldo.vlSaldo += itemPedido.vlVerbaItem;
    		if (geraVerbaPositiva) {
    			verbaSaldo.vlSaldo += itemPedido.vlVerbaItemPositivo;
    		}
    		update(verbaSaldo);
    	} else {
    		verbaSaldoFilter.vlSaldo = itemPedido.vlVerbaItem;
    		if (geraVerbaPositiva) {
    			verbaSaldoFilter.vlSaldo += itemPedido.vlVerbaItemPositivo;
    		}
    		insert(verbaSaldoFilter);	
    	}
    }
    
    public void insertVlSaldo(Pedido pedido, double vlSaldoPedido, boolean pedidoTransmitido) throws SQLException {
    	VerbaSaldo verbaSaldoFilter = new VerbaSaldo();
    	verbaSaldoFilter.cdContaCorrente = pedido.getCdContaCorrente();
    	VerbaSaldo verbaSaldo = getVerbaSaldo(verbaSaldoFilter, Verba.VERBA_PDA);
    	insertVlSaldo(verbaSaldo, verbaSaldoFilter, vlSaldoPedido);
    	if (LavenderePdaConfig.apresentaConsumoVerbaDePedidoNaoTransmitido && !pedidoTransmitido) {
    		verbaSaldo = getVerbaSaldo(verbaSaldoFilter, VerbaSaldo.FLORIGEM_ABERTO);
    		insertVlSaldo(verbaSaldo, verbaSaldoFilter, vlSaldoPedido);
    	}
    }
    
    private void insertVlSaldo(VerbaSaldo verbaSaldo, VerbaSaldo verbaSaldoFilter, double vlSaldoPedido) throws SQLException {
    	if (verbaSaldo != null) {
	    	verbaSaldo.vlSaldo += vlSaldoPedido;
	    	update(verbaSaldo);
    	} else {
    		verbaSaldoFilter.vlSaldo += vlSaldoPedido;
	    	insert(verbaSaldoFilter);
    	}
    }

    public void updateVlSaldo(ItemPedido itemPedido) throws SQLException { // Usava Data do Servidor
    	VerbaSaldo verbaSaldoFilter = new VerbaSaldo();
    	// Busca qual cdContaCorrente está ativa (seja por vigencia, ou sempre '0')
    	setCdContaCorrenteVerbaItemPedido(itemPedido, verbaSaldoFilter);
    	if (ValueUtil.isEmpty(verbaSaldoFilter.cdContaCorrente)) { 
    		return; 
    	}
    	// --------------------------------
    	VerbaSaldo verbaSaldo = getVerbaSaldo(verbaSaldoFilter, Verba.VERBA_PDA);
    	updateVlSaldo(verbaSaldo, itemPedido);
    	if (LavenderePdaConfig.apresentaConsumoVerbaDePedidoNaoTransmitido) {
    		verbaSaldo = getVerbaSaldo(verbaSaldoFilter, VerbaSaldo.FLORIGEM_ABERTO);
    		updateVlSaldo(verbaSaldo, itemPedido);
    	}
    }
    
    private void updateVlSaldo(VerbaSaldo verbaSaldo, ItemPedido itemPedido) throws SQLException {
    	if (verbaSaldo != null) {
    		verbaSaldo.vlSaldo -= itemPedido.vlVerbaItemOld;
    		verbaSaldo.vlSaldo += itemPedido.vlVerbaItem;
    		if (LavenderePdaConfig.geraVerbaPositiva) {
    			verbaSaldo.vlSaldo -= itemPedido.vlVerbaItemPositivoOld;
    			verbaSaldo.vlSaldo += itemPedido.vlVerbaItemPositivo;
    		}
    		update(verbaSaldo);
    	}
    }

    public void deleteVlSaldo(ItemPedido itemPedido) throws SQLException { // Usava Data do servidor
    	VerbaSaldo verbaSaldoFilter = new VerbaSaldo();
    	// Busca qual cdContaCorrente está ativa (seja por vigencia, ou sempre '0')
    	setCdContaCorrenteVerbaItemPedido(itemPedido, verbaSaldoFilter);
    	if (ValueUtil.isEmpty(verbaSaldoFilter.cdContaCorrente)) {
    		return;
    	}
    	// --------------------------------
    	String flOrigemVerbaSaldo = Verba.VERBA_PDA;
    	if (LavenderePdaConfig.usaInterpolacaoPrecoProduto && LavenderePdaConfig.usaCriacaoPedidoErpCancelado && PedidoService.getInstance().isExistsPedidoErpCancelado(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.nuPedido)) {
    		flOrigemVerbaSaldo = Verba.VERBA_ERP;
    	}
    	VerbaSaldo verbaSaldo = getVerbaSaldo(verbaSaldoFilter, flOrigemVerbaSaldo);
    	deleteVlSaldo(verbaSaldo, itemPedido);
    	if (LavenderePdaConfig.apresentaConsumoVerbaDePedidoNaoTransmitido && !itemPedido.pedido.isDeletePedidosPdaByPedidosErp) {
    		verbaSaldo = getVerbaSaldo(verbaSaldoFilter, VerbaSaldo.FLORIGEM_ABERTO);
    		deleteVlSaldo(verbaSaldo, itemPedido);
    	}
    }
    
    private void deleteVlSaldo(VerbaSaldo verbaSaldo, ItemPedido itemPedido) throws SQLException {
    	if (verbaSaldo != null) {
    		verbaSaldo.vlSaldo -= itemPedido.vlVerbaItemOld;
    		if (LavenderePdaConfig.geraVerbaPositiva) {
    			verbaSaldo.vlSaldo -= itemPedido.vlVerbaItemPositivoOld;
    		}
    		update(verbaSaldo);
    	}
    }
    
    public void deleteVlSaldo(double vlSaldoPedido) throws SQLException {
    	VerbaSaldo verbaSaldoFilter = new VerbaSaldo();
    	verbaSaldoFilter.cdContaCorrente = null;
    	VerbaSaldo verbaSaldo = getVerbaSaldo(verbaSaldoFilter, Verba.VERBA_PDA);
    	deleteVlSaldo(verbaSaldo, vlSaldoPedido);
    	if (LavenderePdaConfig.apresentaConsumoVerbaDePedidoNaoTransmitido) {
    		verbaSaldo = getVerbaSaldo(verbaSaldoFilter, VerbaSaldo.FLORIGEM_ABERTO);
    		update(verbaSaldo);
    	}
    }
    
    private void deleteVlSaldo(VerbaSaldo verbaSaldo, double vlSaldoPedido) throws SQLException {
    	if (verbaSaldo != null) {
	    	verbaSaldo.vlSaldo -= vlSaldoPedido;
	    	update(verbaSaldo);
    	}
    }

    private void setCdContaCorrenteVerbaItemPedido(ItemPedido itemPedido, VerbaSaldo verbaSaldo) throws SQLException {
	    Pedido pedido = itemPedido.pedido;
	    if (!pedido.isIgnoraControleVerba()) {
    		verbaSaldo.cdContaCorrente = itemPedido.cdContaCorrente;
    		if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.informaVerbaManual || LavenderePdaConfig.isPermiteBonificarProdutoPedidoUsandoVerba() || LavenderePdaConfig.usaPedidoBonificacao() || LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao) {
    			verbaSaldo.cdContaCorrente = pedido.getCdContaCorrente();
    		}
    		if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
    			VerbaSaldo verbaSaldoFilter = new VerbaSaldo();
    			verbaSaldoFilter.setCdEmpresa(SessionLavenderePda.cdEmpresa);
    			verbaSaldoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    			String vlConfig = StringUtil.getStringValue(itemPedido.dtEmissaoPedido);
    			verbaSaldoFilter.dtVigenciaInicial = ValueUtil.isNotEmpty(vlConfig) ? DateUtil.getDateValue(vlConfig) : new Date();
    			verbaSaldoFilter.flOrigemSaldo = Verba.VERBA_ERP;
    			Vector verbaSaldoList = findAllByExample(verbaSaldoFilter);
    			if ((verbaSaldoList != null) && (verbaSaldoList.size() == 1)) {
    				verbaSaldoFilter = (VerbaSaldo)verbaSaldoList.items[0];
    				verbaSaldo.cdContaCorrente = verbaSaldoFilter.cdContaCorrente;
    				verbaSaldo.dtVigenciaInicial = verbaSaldoFilter.dtVigenciaInicial;
    				verbaSaldo.dtVigenciaFinal = verbaSaldoFilter.dtVigenciaFinal;
    			} else {
    				LogPdaService.getInstance().debug(LogPda.LOG_CATEGORIA_SESSAO, Messages.ERRO_VERBA_SALDO_ERP);
    			}
    		}
    	}
    }
    
    public double getVlSaldoDisponivelRelatorio(Pedido pedido) throws SQLException {
    	Vector list =  findAllGrupoByContaC(); 	
    	double vlVerbaDisponivel = 0d;
    	if (ValueUtil.isNotEmpty(list)) {
			VerbaSaldo verbaSaldo = getVerbaSaldo(list);
			if (verbaSaldo != null) {
				if (LavenderePdaConfig.usaVerbaPositivaApenasPedidoCorrente) {
					vlVerbaDisponivel = verbaSaldo.vlSaldo;
				} else {
					vlVerbaDisponivel = verbaSaldo.vlSaldo - pedido.vlVerbaPedido;
				}
			}
    	}
    	return getVlVerbaDisponivel(pedido, vlVerbaDisponivel); 
    }

    public Vector findAllGrupoByContaC() throws SQLException {
    	VerbaSaldo verbFilter = new VerbaSaldo();
    	verbFilter.setCdEmpresa(SessionLavenderePda.cdEmpresa);
    	verbFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	Vector listFull = findAllByExample(verbFilter);
    	//--
    	Vector listVerbaSaldoPda = new Vector();
    	Vector listVerbaSaldoErp = new Vector();
    	Vector list = new Vector();
    	//--
		int size = listFull.size();
        for (int i = 0; i < size; i++) {
    		VerbaSaldo verbaSaldo = (VerbaSaldo) listFull.items[i];
    		if (Verba.VERBA_PDA.equals(verbaSaldo.flOrigemSaldo)) {
    			listVerbaSaldoPda.addElement(verbaSaldo);
    		} else if (Verba.VERBA_ERP.equals(verbaSaldo.flOrigemSaldo)) {
    			listVerbaSaldoErp.addElement(verbaSaldo);
    		}
    	}
    	//--
        int sizeVerbaSaldoPda = listVerbaSaldoPda.size();
        int sizeVerbaSaldoERP = listVerbaSaldoErp.size();
        for (int i = 0; i < sizeVerbaSaldoERP; i++) {
    		VerbaSaldo verbaSaldoErp = (VerbaSaldo) listVerbaSaldoErp.items[i];
    		for (int j = 0; j < sizeVerbaSaldoPda; j++) {
    			VerbaSaldo verbaSaldoPda = (VerbaSaldo) listVerbaSaldoPda.items[j];
    			if (verbaSaldoErp.cdContaCorrente.equals(verbaSaldoPda.cdContaCorrente)) {
    				verbaSaldoErp.vlSaldo += verbaSaldoPda.vlSaldo;
    			}
    		}
    		list.addElement(verbaSaldoErp);
    	}
        listFull = null;
    	return list;
    }

    public void updateVerbaSaldoItemPedido(ItemPedido itemPedido) throws SQLException {
    	if (itemPedido.isIgnoraControleVerba()) {
    		return;
    	}
    	// Busca qual cdContaCorrente está ativa (seja por vigencia, ou sempre '0')
    	String cdContaCorrente = itemPedido.cdContaCorrente;
    	if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.informaVerbaManual || LavenderePdaConfig.isPermiteBonificarProdutoPedidoUsandoVerba() || LavenderePdaConfig.usaPedidoBonificacao() || LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao) {
    		cdContaCorrente = itemPedido.pedido.getCdContaCorrente();
    	}
    	if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
        	VerbaSaldo verbaSaldoFilter = new VerbaSaldo();
        	verbaSaldoFilter.setCdEmpresa(SessionLavenderePda.cdEmpresa);
        	verbaSaldoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
        	String vlConfig = itemPedido.pedido.dtEmissao + "";
        	verbaSaldoFilter.dtVigenciaInicial = vlConfig != null ? DateUtil.getDateValue(vlConfig) : new Date();
        	verbaSaldoFilter.flOrigemSaldo = Verba.VERBA_ERP;
           	Vector verbaSaldoList = findAllByExample(verbaSaldoFilter);
           	if ((verbaSaldoList != null) && (verbaSaldoList.size() == 1)) {
           		verbaSaldoFilter = (VerbaSaldo)verbaSaldoList.items[0];
           		cdContaCorrente = verbaSaldoFilter.cdContaCorrente;
           	} else {
           		LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_SESSAO, Messages.ERRO_VERBA_SALDO_ERP);
           		return;
           	}
    	}
    	// --------------------------------
    	VerbaSaldo verbaSaldoFilter = new VerbaSaldo();
    	verbaSaldoFilter.setCdEmpresa(SessionLavenderePda.cdEmpresa);
    	verbaSaldoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	verbaSaldoFilter.cdContaCorrente = cdContaCorrente;
    	Vector verbaSaldoList = findAllByExample(verbaSaldoFilter);
    	if (verbaSaldoList != null) {
        	for (int  i = 0; i < verbaSaldoList.size(); i++) {
        		VerbaSaldo verbaSaldo = (VerbaSaldo) verbaSaldoList.items[i];
        		if (LavenderePdaConfig.usaProvisionamentoConsumoVerbaSaldo()) {
        			if (LavenderePdaConfig.apresentaConsumoVerbaDePedidoNaoTransmitido && VerbaSaldo.FLORIGEM_ABERTO.equals(verbaSaldo.flOrigemSaldo)) {
        				verbaSaldo.vlSaldo -= itemPedido.vlVerbaItem;
        				if (LavenderePdaConfig.geraVerbaPositiva) {
        					verbaSaldo.vlSaldo -= itemPedido.vlVerbaItemPositivo;
        				}
        				update(verbaSaldo);
        			}
        		} else {
        			if (Verba.VERBA_PDA.equals(verbaSaldo.flOrigemSaldo)) {
        				verbaSaldo.vlSaldo -= itemPedido.vlVerbaItem;
        				if (LavenderePdaConfig.geraVerbaPositiva) {
        					verbaSaldo.vlSaldo -= itemPedido.vlVerbaItemPositivo;
        				}
        				update(verbaSaldo);
        			} else if (Verba.VERBA_ERP.equals(verbaSaldo.flOrigemSaldo)) {
        				verbaSaldo.vlSaldo += itemPedido.vlVerbaItem;
        				if (LavenderePdaConfig.geraVerbaPositiva) {
        					verbaSaldo.vlSaldo += itemPedido.vlVerbaItemPositivo;
        				}
        				update(verbaSaldo);
        			}
        		}
        	}
    	}
    }

	public double findTotalSaldoVerba(Vector vectorAllContas) {
    	double some = 0;
		int size = vectorAllContas.size();
        for (int i = 0; i < size; i++) {
    		VerbaSaldo verba = (VerbaSaldo) vectorAllContas.items[i];
    		some += verba.vlSaldo;
    	}
    	return some;
    }

    public void recalculateAndUpdateVerbaSaldoPda() throws SQLException {
    	LoadingBoxWindow mb = UiUtil.createProcessingMessage();
    	mb.popupNonBlocking();
    	try {
    		VerbaSaldo verbaSaldoFilter = new VerbaSaldo();
    		verbaSaldoFilter.setCdEmpresa(SessionLavenderePda.cdEmpresa);
    		verbaSaldoFilter.flOrigemSaldo = Verba.VERBA_PDA;
    		deleteAllByExample(verbaSaldoFilter);
    		if (LavenderePdaConfig.apresentaConsumoVerbaDePedidoNaoTransmitido) {
    			verbaSaldoFilter.flOrigemSaldo = VerbaSaldo.FLORIGEM_ABERTO;
        		deleteAllByExample(verbaSaldoFilter);
    		}
    		//--
    		HashMap<String, VerbaSaldo> verbaSaldoHash = getVerbasSaldoErpParaRecalculo();
    		Pedido pedidoFilter = new Pedido();
    		if (!LavenderePdaConfig.usaProvisionamentoConsumoVerbaSaldo()) {
    			pedidoFilter.cdStatusPedidoDif = LavenderePdaConfig.cdStatusPedidoTransmitido;
    		}
    		Vector pedidoList = PedidoPdbxDao.getInstance().findAllPedidosPdaResumido(pedidoFilter);
			int sizePedidoList = pedidoList.size();
	        for (int i = 0; i < sizePedidoList; i++) {
	    		Pedido pedido = (Pedido) pedidoList.items[i];
	    		if (pedido.isSimulaControleVerba() || pedido.isIgnoraControleVerba() || !isRecalculaPedidoDentroVerbaVigente(pedido, verbaSaldoHash)) {
	    			continue;
	    		}
	    		PedidoService.getInstance().findItemPedidoList(pedido);
	    		Vector itemPedidoList = pedido.itemPedidoList;
	    		int itemSize = itemPedidoList.size();
	    		
	    		for (int j = 0; j < itemSize; j++) {
	    			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[j];
	    			if (itemPedido.isIgnoraControleVerba()) {
	    				continue;
	    			}
	    			if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
	    				itemPedido.dtEmissaoPedido = pedido.dtEmissao;
	    			}
	    			
	    			if (!LavenderePdaConfig.usaVerbaPositivaApenasPedidoCorrente) {
	    				insertVlSaldo(pedido, itemPedido, ValueUtil.valueEquals(pedido.cdStatusPedido, LavenderePdaConfig.cdStatusPedidoTransmitido));
	    			}
	    		}
	    		
	    		if (LavenderePdaConfig.usaVerbaPositivaApenasPedidoCorrente) {
	    			double vlSaldoPedido = pedido.getVlVerbaPedidoDisponivel();
	    			if (vlSaldoPedido < 0) {
	    				insertVlSaldo(pedido, vlSaldoPedido, ValueUtil.valueEquals(pedido.cdStatusPedido, LavenderePdaConfig.cdStatusPedidoTransmitido));
	    			}
	    		}
	    		
	    	}
    	} finally {
    		mb.unpop();
    	}
    }

	private HashMap<String, VerbaSaldo> getVerbasSaldoErpParaRecalculo() throws SQLException {
		HashMap<String, VerbaSaldo> verbaSaldoHash = new HashMap<String, VerbaSaldo>(8);
		if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco() && LavenderePdaConfig.usaProvisionamentoConsumoVerbaSaldo()) {
			VerbaSaldo verbaSaldoFilter = new VerbaSaldo();
			verbaSaldoFilter.flOrigemSaldo = Verba.VERBA_ERP;
			Vector verbaSaldoList = findAllByExample(verbaSaldoFilter);
			int size = verbaSaldoList.size();
			for (int i = 0; i < size; i++) {
				VerbaSaldo verbaSaldo = (VerbaSaldo) verbaSaldoList.items[i];
				verbaSaldoHash.put(verbaSaldo.getRowKey(), verbaSaldo);
			}
		}
		return verbaSaldoHash;
	}
	   
    private boolean isRecalculaPedidoDentroVerbaVigente(Pedido pedido, HashMap<String, VerbaSaldo> verbaSaldoHash) throws SQLException{
    	if (!LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco() || !LavenderePdaConfig.usaProvisionamentoConsumoVerbaSaldo()) {
    		return true;
    	}
    	VerbaSaldo verbaSaldo = getVerbaSaldoInstanced(pedido, pedido.cdEmpresa, pedido.cdRepresentante, Verba.VERBA_PDA);
    	verbaSaldo.flOrigemSaldo = Verba.VERBA_ERP;
    	verbaSaldo = verbaSaldoHash.get(verbaSaldo.getRowKey());
    	if (verbaSaldo != null && ValueUtil.isNotEmpty(verbaSaldo.dtVigenciaFinal) && ValueUtil.isNotEmpty(verbaSaldo.dtVigenciaInicial)) {
    		if (!pedido.isPedidoTransmitido() || (ValueUtil.isNotEmpty(pedido.dtTransmissaoPda) && !pedido.dtTransmissaoPda.isAfter(verbaSaldo.dtVigenciaFinal) && !pedido.dtTransmissaoPda.isBefore(verbaSaldo.dtVigenciaInicial))) {
    			return true;
    		}
    	}
    	return false;
    }

	public void ajustaVerbaPedidoSugestao(Pedido pedido, boolean usaVerba) throws SQLException {
		if (pedido != null && LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0) {
			if (usaVerba) {
				double vlSaldo = getVlSaldo(((ItemPedido) pedido.itemPedidoList.items[0]).cdContaCorrente, null);
				double vlSaldoFinal = vlSaldo + pedido.vlDesconto * -1;
				if (vlSaldoFinal * -1 > SessionLavenderePda.usuarioPdaRep.representante.vlToleranciaVerba) {
					aplicaVerbaPedidoSugestao(pedido, vlSaldo);
				}
			} else {
				pedido.vlDesconto = 0;
				pedido.vlDescontoOld = 0;
			}
		}
	}

	private void aplicaVerbaPedidoSugestao(Pedido pedido, double vlSaldo) {
		double vlSaldoTotal = ValueUtil.round(vlSaldo + SessionLavenderePda.usuarioPdaRep.representante.vlToleranciaVerba);
		if (vlSaldoTotal > 0) {
			if (pedido.vlDesconto > vlSaldoTotal) {
				pedido.vlDescontoOld = 0;
				pedido.vlDesconto = vlSaldoTotal;
			}
		} else {
			pedido.vlDescontoOld = 0;
			pedido.vlDesconto = 0;
		}
	}

	public void ajustaVerbaItemSugestaoPedido(Pedido pedido, ItemPedido itemPedido, boolean usaVerba) throws SQLException {
		if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao) {
			if (pedido != null && itemPedido != null) {
				if (usaVerba && itemPedido.getProduto().isUtilizaVerba() && !itemPedido.isOportunidade() && !itemPedido.isIgnoraControleVerba()) {
					double vlSaldoAtual = ValueUtil.round(getVlSaldo(itemPedido.pedido.getCdContaCorrente(), null));
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
	}

    private void aplicaVerbaItemSugestaoPedidoPedido(ItemPedido itemPedido, Pedido pedido, double vlVerbaSaldo) {
    	double vlVerbaTotal = ValueUtil.round(vlVerbaSaldo + SessionLavenderePda.usuarioPdaRep.representante.vlToleranciaVerba);
    	double vlVerbaUnitaria = vlVerbaTotal / itemPedido.getQtItemFisico();
    	itemPedido.vlVerbaItemSugestaoPedido = itemPedido.vlVerbaItem;
    	itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido);
    	if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco && itemPedido.isItemVendaNormal() && vlVerbaTotal > 0) {
			itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido - vlVerbaUnitaria);
			itemPedido.vlPctDesconto = ValueUtil.round((1 - (itemPedido.vlItemPedido / itemPedido.vlBaseItemPedido)) * 100);
			itemPedido.vlVerbaItem = ValueUtil.round(vlVerbaUnitaria * itemPedido.getQtItemFisico()) * -1;
    	} else {
    		itemPedido.vlPctDesconto = 0;
    		itemPedido.vlVerbaItem = 0;
    	}
    	pedido.itemPedidoConsumoVerbaSugestaoPedList.addElement(itemPedido);
    }

    public boolean validateSaldo(double vlDesconto, double vlDescontoOld, double vlVerbaPositivaPedido, double vlVerbaPedido) throws SQLException {
    	if (vlDesconto < 0) {
    		double vlSaldo = ValueUtil.round(getVlSaldo("0"));
    		double tot;
    		if (LavenderePdaConfig.usaDescCapaPedidoConsumindoVerbaPositivaApenasPedidoCorrente()) {
    			if (LavenderePdaConfig.isMostraFlexPositivoPedido()) {
    				vlSaldo += vlVerbaPositivaPedido;
    			}
    			tot = ValueUtil.round((vlSaldo + vlDesconto) - vlDescontoOld + vlVerbaPedido);
    		} else {
    			tot = ValueUtil.round((vlSaldo + vlDesconto) - vlDescontoOld);
    		}
    		if (tot < 0) {
    			tot = ValueUtil.round(tot + SessionLavenderePda.usuarioPdaRep.representante.vlToleranciaVerba);
    			if (tot < 0) {
        			String[] args = {StringUtil.getStringValueToInterface(vlDesconto * -1), StringUtil.getStringValueToInterface(vlSaldo - vlDescontoOld)};
        			throw new ValidationException(MessageUtil.getMessage(Messages.VERBASALDO_MSG_SALDO_INDISPONIVEL, args));
				} else {
					return UiUtil.showConfirmYesNoMessage(Messages.VERBASALDO_MSG_VERBA_NEGATIVA_CONFIRMA);
				}
    		}
    	}
    	return true;
    }

    public void updateVlSaldoByPedido(Pedido pedido) throws SQLException {
		if ((pedido.vlDesconto > 0) || pedido.vlDescontoOld > 0) {
			VerbaSaldo verbaSaldoFilter = getVerbaSaldoInstanced(pedido, pedido.cdEmpresa, pedido.cdRepresentante, Verba.VERBA_PDA);
			VerbaSaldo verbaSaldo = (VerbaSaldo) findByRowKey(verbaSaldoFilter.getRowKey());
			updateVlSaldoByPedido(verbaSaldo, verbaSaldoFilter, pedido);
			if (LavenderePdaConfig.apresentaConsumoVerbaDePedidoNaoTransmitido) {
				verbaSaldoFilter = getVerbaSaldoInstanced(pedido, pedido.cdEmpresa, pedido.cdRepresentante, VerbaSaldo.FLORIGEM_ABERTO);
				verbaSaldo = (VerbaSaldo) findByRowKey(verbaSaldoFilter.getRowKey());
				updateVlSaldoByPedido(verbaSaldo, verbaSaldoFilter, pedido);
			}
		}
    }
    
    private void updateVlSaldoByPedido(VerbaSaldo verbaSaldo, VerbaSaldo verbaSaldoFilter, Pedido pedido) throws SQLException {
    	if (verbaSaldo != null) {
			verbaSaldo.vlSaldo -= pedido.vlDescontoOld * -1;
			verbaSaldo.vlSaldo += pedido.vlDesconto * -1;
			update(verbaSaldo);
			pedido.vlDescontoOld = pedido.vlDesconto;
		} else {
			verbaSaldoFilter.vlSaldo = pedido.vlDesconto * -1;
			insert(verbaSaldoFilter);
			pedido.vlDescontoOld = pedido.vlDesconto;
		}
    }

    public void deleteVlSaldoByPedido(Pedido pedido) throws SQLException {
    	if (pedido.vlDesconto > 0) {
    		VerbaSaldo verbaSaldoFilter = getVerbaSaldoInstanced(pedido, pedido.cdEmpresa, pedido.cdRepresentante, Verba.VERBA_PDA);
    		VerbaSaldo verbaSaldo = (VerbaSaldo) findByRowKey(verbaSaldoFilter.getRowKey());
    		deleteVlSaldoByPedido(verbaSaldo, pedido);
    		if (LavenderePdaConfig.apresentaConsumoVerbaDePedidoNaoTransmitido) {
    			verbaSaldoFilter = getVerbaSaldoInstanced(pedido, pedido.cdEmpresa, pedido.cdRepresentante, VerbaSaldo.FLORIGEM_ABERTO);
        		verbaSaldo = (VerbaSaldo) findByRowKey(verbaSaldoFilter.getRowKey());
        		deleteVlSaldoByPedido(verbaSaldo, pedido);
    		}
    	}
    }
    
    private void deleteVlSaldoByPedido(VerbaSaldo verbaSaldo, Pedido pedido) throws SQLException {
    	if (verbaSaldo != null) {
			verbaSaldo.vlSaldo -= pedido.vlDescontoOld * -1;
			update(verbaSaldo);
		}
    }

    public void updateVerbaSaldoForPedidosFechados(Pedido pedido) throws SQLException {
		VerbaSaldo verbaSaldoFilter = new VerbaSaldo();
		verbaSaldoFilter.setCdEmpresa(SessionLavenderePda.cdEmpresa);
		verbaSaldoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		verbaSaldoFilter.cdContaCorrente = pedido.getCdContaCorrente();
		Vector verbaSaldoList = findAllByExample(verbaSaldoFilter);
		if (verbaSaldoList != null) {
			if (verbaSaldoList.size() == 2) {
				VerbaSaldo verbaSaldoErp;
				VerbaSaldo verbaSaldoPda;
				VerbaSaldo verbaSaldo = (VerbaSaldo) verbaSaldoList.items[0];
				//--
				if (Verba.VERBA_ERP.equals(verbaSaldo.flOrigemSaldo)) {
					verbaSaldoErp = verbaSaldo;
					verbaSaldoPda = (VerbaSaldo) verbaSaldoList.items[1];
				} else {
					verbaSaldoPda = verbaSaldo;
					verbaSaldoErp = (VerbaSaldo) verbaSaldoList.items[1];
				}
				//--
				verbaSaldoErp.vlSaldo += pedido.vlDesconto * -1;
				update(verbaSaldoErp);
				verbaSaldoPda.vlSaldo -= pedido.vlDesconto * -1;
				update(verbaSaldoPda);
			}
		}
    }

    public void recalculateVerbaSaldoPdaForPedidos() throws SQLException {
    	LoadingBoxWindow mb = UiUtil.createProcessingMessage();
    	mb.popupNonBlocking();
    	try {
    		Vector listFull = findAll();
    		Vector listVerbaSaldoPda = new Vector();
    		//--
			int size = listFull.size();
	        for (int i = 0; i < size; i++) {
    			VerbaSaldo verbaSaldo = (VerbaSaldo) listFull.items[i];
    			if (Verba.VERBA_PDA.equals(verbaSaldo.flOrigemSaldo)) {
    				listVerbaSaldoPda.addElement(verbaSaldo);
    			}
    		}
    		//--
			int size2 = listVerbaSaldoPda.size();
	        for (int i = 0; i < size2; i++) {
    			VerbaSaldo verbaSaldoPda = (VerbaSaldo) listVerbaSaldoPda.items[i];
    			delete(verbaSaldoPda);
    		}
    		//--
	        HashMap<String, VerbaSaldo> verbaSaldoHash = getVerbasSaldoErpParaRecalculo();
	        Pedido pedidoFilter = new Pedido();
	        pedidoFilter.cdStatusPedidoDif = LavenderePdaConfig.cdStatusPedidoTransmitido;
	        Vector pedidoList = PedidoPdbxDao.getInstance().findAllPedidosPdaResumido(pedidoFilter);
			int sizePed = pedidoList.size();
	        for (int i = 0; i < sizePed; i++) {
    			Pedido pedido = (Pedido) pedidoList.items[i];
    			if (pedido.isSimulaControleVerba() || pedido.isIgnoraControleVerba() || !isRecalculaPedidoDentroVerbaVigente(pedido, verbaSaldoHash)) {
	    			continue;
	    		}
    			if (!LavenderePdaConfig.usaVerbaPositivaApenasPedidoCorrente) {
	    			if ((pedido.vlDesconto > 0) || ((pedido.vlVerbaPedidoPositivo >= 0) && LavenderePdaConfig.geraVerbaPositiva)) {
	    	    		VerbaSaldo verbaSaldoFilter = getVerbaSaldoInstanced(pedido, pedido.cdEmpresa, pedido.cdRepresentante, Verba.VERBA_PDA);
	    	    		VerbaSaldo verbaSaldo = (VerbaSaldo) findByRowKey(verbaSaldoFilter.getRowKey());
	    	    		if (verbaSaldo != null) {
	    	    			verbaSaldo.vlSaldo += pedido.vlDesconto * -1;
	    	    			if (LavenderePdaConfig.geraVerbaPositiva) {
	    	    				verbaSaldo.vlSaldo += pedido.vlVerbaPedidoPositivo;
	    	    			}
	    	    			update(verbaSaldo);
	    	    		} else {
	    	    			verbaSaldoFilter.vlSaldo = pedido.vlDesconto * -1;
	    	    			if (LavenderePdaConfig.geraVerbaPositiva) {
	    	    				verbaSaldoFilter.vlSaldo += pedido.vlVerbaPedidoPositivo;
	    	    			}
	    	    			insert(verbaSaldoFilter);
	    	    		}
	    	    	}
    			}
    			if (LavenderePdaConfig.usaVerbaPositivaApenasPedidoCorrente || LavenderePdaConfig.usaDescCapaPedidoConsumindoVerbaPositivaApenasPedidoCorrente()) {
	    			double vlSaldoPedido = pedido.getVlVerbaPedidoDisponivel();
	    			if (vlSaldoPedido < 0) {
	    				insertVlSaldo(pedido, vlSaldoPedido, ValueUtil.valueEquals(pedido.cdStatusPedido, LavenderePdaConfig.cdStatusPedidoTransmitido));
	    			}
    			}
    		}
    	} finally {
    		mb.unpop();
    	}
    }

    public VerbaSaldo getVerbaSaldoInstanced(Pedido pedido, final String cdEmpresa, final String cdRepresentante, String flOrigemSaldo) throws SQLException {
    	VerbaSaldo verbaSaldoFilter = new VerbaSaldo();
    	verbaSaldoFilter.setCdEmpresa(cdEmpresa);
    	verbaSaldoFilter.cdRepresentante = cdRepresentante;
    	if (pedido != null) {
		    verbaSaldoFilter.cdContaCorrente = pedido.getCdContaCorrente();
	    } else {
	    	pedido = new Pedido();
	    	pedido.cdEmpresa = cdEmpresa;
	    	pedido.cdRepresentante = cdRepresentante;
    		verbaSaldoFilter.cdContaCorrente = pedido.getCdContaCorrente();
	    }
    	verbaSaldoFilter.flOrigemSaldo = flOrigemSaldo;
    	return verbaSaldoFilter;
    }

	public VerbaSaldo getVerbaSaldoInstanced(final String cdEmpresa, final String cdRepresentante, String flOrigemSaldo) throws SQLException {
		return getVerbaSaldoInstanced(null, cdEmpresa, cdRepresentante, flOrigemSaldo);
	}

    public void updateVlSaldoByPedidoByVlVerbaPedido(Pedido pedido) throws SQLException {
    	if (pedido.vlVerbaPedido != 0) {
    		VerbaSaldo verbaSaldoFilter = getVerbaSaldoInstanced(pedido, pedido.cdEmpresa, pedido.cdRepresentante, Verba.VERBA_PDA);
    		VerbaSaldo verbaSaldo = (VerbaSaldo) findByRowKey(verbaSaldoFilter.getRowKey());
    		updateVlSaldoByPedidoByVlVerbaPedido(verbaSaldo, verbaSaldoFilter, pedido);
    		if (LavenderePdaConfig.apresentaConsumoVerbaDePedidoNaoTransmitido) {
    			verbaSaldoFilter = getVerbaSaldoInstanced(pedido, pedido.cdEmpresa, pedido.cdRepresentante, VerbaSaldo.FLORIGEM_ABERTO);
        		verbaSaldo = (VerbaSaldo) findByRowKey(verbaSaldoFilter.getRowKey());
        		updateVlSaldoByPedidoByVlVerbaPedido(verbaSaldo, verbaSaldoFilter, pedido);
    		}
    	}
    }
    
    private void updateVlSaldoByPedidoByVlVerbaPedido(VerbaSaldo verbaSaldo, VerbaSaldo verbaSaldoFilter, Pedido pedido) throws SQLException {
    	if (verbaSaldo != null) {
			verbaSaldo.vlSaldo += pedido.vlVerbaPedido;
			update(verbaSaldo);
		} else {
			verbaSaldoFilter.vlSaldo = pedido.vlVerbaPedido;
			insert(verbaSaldoFilter);
		}
    }
    
    public VerbaSaldo getVerbaSaldo(Vector verbaList) {
    	VerbaSaldo verbaSaldo = (VerbaSaldo)verbaList.items[0];
		if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
			Date dtVigencia = ConfigInternoService.getInstance().getDtServidor();
			dtVigencia = dtVigencia != null ? dtVigencia : new Date();
			int size = verbaList.size();
			VerbaSaldo verbaSaldoTemp;
	        for (int i = 0; i < size; i++) {
				verbaSaldo = null;
				verbaSaldoTemp = (VerbaSaldo)verbaList.items[i];
				if ((verbaSaldoTemp.dtVigenciaInicial != null && verbaSaldoTemp.dtVigenciaFinal != null) && (dtVigencia.isAfter(verbaSaldoTemp.dtVigenciaInicial) || dtVigencia.equals(verbaSaldoTemp.dtVigenciaInicial)) && (dtVigencia.isBefore(verbaSaldoTemp.dtVigenciaFinal) || dtVigencia.equals(verbaSaldoTemp.dtVigenciaFinal))) {
					verbaSaldo = verbaSaldoTemp;
					break;
				}
			}
		}
    	return verbaSaldo;
    }
    
    public double getVlVerbaDisponivel(Pedido pedido, double vlSaldo) throws SQLException {
    	if (pedido.isSimulaControleVerba()) {
    		return vlSaldo;
    	}
    	if (LavenderePdaConfig.usaVerbaPositivaApenasPedidoCorrente) {
    		if (pedido.getVlVerbaPedidoDisponivel() < 0) {
    			if (LavenderePdaConfig.usaDescCapaPedidoConsumindoVerbaPositivaApenasPedidoCorrente()) {
    				return vlSaldo - pedido.getVlVerbaPedidoDisponivel() - pedido.vlDesconto;
    			}
    			return vlSaldo - pedido.getVlVerbaPedidoDisponivel();
    		}
    		return vlSaldo;
    	}
    	return vlSaldo;
    }
    
    public String getDsSaldoFinal(Pedido pedido, double vlSaldo, double vlVerbaPedido, double vlVerbaPositiva) throws SQLException {
    	double saldoFinal = vlSaldo;
    	if (pedido.isSimulaControleVerba()) {
    		if (LavenderePdaConfig.usaVerbaPositivaApenasPedidoCorrente) {
    			double vlVerbaConsumida = pedido.vlVerbaPedido < 0 ? pedido.vlVerbaPedido : 0d;
    			if (LavenderePdaConfig.usaDescCapaPedidoConsumindoVerbaPositivaApenasPedidoCorrente()) {
    				vlVerbaConsumida -= pedido.vlDesconto;
    			}
    			saldoFinal += vlVerbaConsumida;
    			if (LavenderePdaConfig.isMostraFlexPositivoPedido()) {
    				saldoFinal += vlVerbaPositiva;
    			}
    		} else {
    			saldoFinal += vlVerbaPedido + (LavenderePdaConfig.isMostraFlexPositivoPedido() && LavenderePdaConfig.geraVerbaPositiva ? vlVerbaPositiva : 0);
    		}
		} else {
			saldoFinal += vlVerbaPedido;
		}
	    return StringUtil.getStringValueToInterface(saldoFinal);
    }
    
    public void limpaVerbaSaldoPdaForaVigencia() throws SQLException {
    	if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
    		VerbaSaldo verbaSaldoFilter = new VerbaSaldo();
    		verbaSaldoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    		verbaSaldoFilter.setCdEmpresa(SessionLavenderePda.cdEmpresa);
    		Vector verbaSaldoList = findAllByExample(verbaSaldoFilter);
    		int size = verbaSaldoList.size();
		    for (int i = 0; i < size; i++) {
			    VerbaSaldo verbaSaldo = (VerbaSaldo) verbaSaldoList.items[i];
			    limpaVerbaSaldoPdaForaVigencia(verbaSaldo);
			    if (LavenderePdaConfig.apresentaConsumoVerbaDePedidoNaoTransmitido) {
				    verbaSaldo = getVerbaSaldo(verbaSaldo, VerbaSaldo.FLORIGEM_ABERTO);
				    limpaVerbaSaldoPdaForaVigencia(verbaSaldo);
			    }
		    }
    	}
    }
    
    private void limpaVerbaSaldoPdaForaVigencia(VerbaSaldo verbaSaldo) throws SQLException {
    	if (verbaSaldo != null && verbaSaldo.dtVigenciaFinal != null && verbaSaldo.dtVigenciaFinal.isBefore(DateUtil.getCurrentDate())) {
			delete(verbaSaldo);
		}
    }

	public void consomeVerbaPedido(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativo()) {
			return;		
		}
		String cdRepresentante = SessionLavenderePda.usuarioPdaRep.representante.isSupervisor() ? SessionLavenderePda.usuarioPdaRep.cdRepresentante : pedido.cdRepresentante;
		VerbaSaldo verbaSaldoErp = getVerbaSaldoErpVingenciaAtual(cdRepresentante);
		VerbaSaldo verbaSaldoPda = getVerbaSaldoInstanced(pedido, pedido.cdEmpresa, cdRepresentante, Verba.VERBA_PDA);
		verbaSaldoPda = (VerbaSaldo) findByRowKey(verbaSaldoPda.getRowKey());
		if (verbaSaldoPda == null) {
			verbaSaldoPda = new VerbaSaldo();
			verbaSaldoPda.setCdEmpresa(pedido.cdEmpresa);
			verbaSaldoPda.cdRepresentante = cdRepresentante;
			verbaSaldoPda.cdContaCorrente = pedido.getCdContaCorrente();
			verbaSaldoPda.flOrigemSaldo = Verba.VERBA_PDA;
			verbaSaldoPda.dtSaldo = new Date();
			insert(verbaSaldoPda);
		}
		double vlSaldoErp = verbaSaldoErp != null ? verbaSaldoErp.vlSaldo : 0;
		double vlSaldoPda = verbaSaldoPda.vlSaldo;
		double vlTolerancia = VerbaService.getInstance().getVlTolerancia(pedido);
		double vlSaldoDisponivel = vlSaldoErp + vlSaldoPda;
		double vlSaldoDisponivelComTolerancia = vlSaldoErp + vlSaldoPda + vlTolerancia;
		if (LavenderePdaConfig.usaVerbaPositivaApenasPedidoCorrente) {
			double vlSaldoDisponivelPedido = pedido.getVlVerbaPedidoDisponivel();
			if (vlSaldoDisponivelPedido < 0 && vlSaldoDisponivelComTolerancia < vlSaldoDisponivelPedido * -1 && !pedido.consumoVerbaSaldoLiberadoSenha) {
				if (vlTolerancia > 0) {
					String[] args = { StringUtil.getStringValueToInterface(vlSaldoDisponivel), StringUtil.getStringValueToInterface(vlTolerancia), StringUtil.getStringValueToInterface(vlSaldoDisponivel - (vlSaldoDisponivelPedido * -1)) };
					throw new ValidationException(MessageUtil.getMessage(Messages.VERBASALDO_MSG_VERBA_NEGATIVA_ACIMA_TOLERANCIA_CONFIRMA, args));
				}
				vlSaldoDisponivel = vlSaldoDisponivel < 0 ? 0 : vlSaldoDisponivel;
				throw new VerbaSaldoPedidoExtrapoladoException(MessageUtil.getMessage(Messages.CONSUMO_VERBA_EXCEDENTE_FECHAR_PEDIDO, new Object[]{StringUtil.getStringValueToInterface(vlSaldoDisponivelPedido), StringUtil.getStringValueToInterface(vlSaldoDisponivel)}));
			} 
			verbaSaldoPda.vlSaldo += vlSaldoDisponivelPedido;
		} else if (!pedido.consumoVerbaSaldoLiberadoSenha && vlSaldoDisponivel < pedido.vlVerbaPedido * -1) {
			if (!LavenderePdaConfig.liberaComSenhaPedidoBonificacaoComSaldoVerbaExtrapolado) {
				vlSaldoDisponivel = vlSaldoDisponivel < 0 ? 0 : vlSaldoDisponivel;
				throw new ValidationException(MessageUtil.getMessage(Messages.CONSUMO_VERBA_EXCEDENTE_FECHAR_PEDIDO, new Object[]{StringUtil.getStringValueToInterface(pedido.vlVerbaPedido), StringUtil.getStringValueToInterface(vlSaldoDisponivel)}));
			} else if (LavenderePdaConfig.liberaComSenhaPedidoBonificacaoComSaldoVerbaExtrapolado && pedido.isPedidoBonificacao()) {
				throw new VerbaSaldoPedidoExtrapoladoException(Messages.VERBASALDO_SALDO_NEGATIVO);
			}
			verbaSaldoPda.vlSaldo += pedido.vlVerbaPedido;
		}
		update(verbaSaldoPda);
	}
	
	public double getVlMinVerba(Pedido pedido, String cdEmpresa, String cdRepresentante) throws SQLException {
		VerbaSaldo verbaSaldo = getVerbaSaldoInstanced(pedido, cdEmpresa, cdRepresentante, Verba.VERBA_PDA);
		verbaSaldo.flOrigemSaldo = Verba.VERBA_ERP;
		verbaSaldo = (VerbaSaldo) findByRowKey(verbaSaldo.getRowKey());
		if (verbaSaldo != null) {
			if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
				if (verbaSaldo.dtVigenciaFinal != null && verbaSaldo.dtVigenciaInicial != null && !DateUtil.getCurrentDate().isAfter(verbaSaldo.dtVigenciaFinal) && !DateUtil.getCurrentDate().isBefore(verbaSaldo.dtVigenciaInicial)) {
					return verbaSaldo.vlMinVerba;
				} else {
					return 0;
				}
			}
			return verbaSaldo.vlMinVerba;
		}
		return 0;
	}
	
	public void validaVerbaAindaVigente(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaProvisionamentoConsumoVerbaSaldo() && LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco() && pedido.vlVerbaPedido != 0) {
			VerbaSaldo verbaSaldoFilter = new VerbaSaldo();
			verbaSaldoFilter.setCdEmpresa(pedido.cdEmpresa);
			verbaSaldoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Pedido.class);
			Vector verbaSaldoList = findAllByExample(verbaSaldoFilter);
			double saldoPda = 0;
			double saldoErp = 0;
			int size = verbaSaldoList.size();
			for (int i = 0; i < size; i++) {
				VerbaSaldo verbaSaldo = (VerbaSaldo) verbaSaldoList.items[i];
				if (Verba.VERBA_ERP.equals(verbaSaldo.flOrigemSaldo) && verbaSaldo.isVigente()) {
					saldoErp = verbaSaldo.vlSaldo;
				} else if (Verba.VERBA_PDA.equals(verbaSaldo.flOrigemSaldo)) {
					saldoPda = -verbaSaldo.vlSaldo;
				}
			}
			double vlTolerancia = VerbaService.getInstance().getVlTolerancia(pedido);
			if (saldoPda > saldoErp && saldoPda > vlTolerancia) {
				throw new ValidationException(Messages.VERBASALDO_ERRO_SALDO_INSUFICIENTE_ENVIO);
			}
		}
	}
	
	public void enviaVerbaSaldoPedidosAbertos() throws SQLException {
		VerbaSaldoAbertaRunnable.addQueue();
	}
	
	public VerbaSaldo getVerbaSaldoPda(Pedido pedido, String cdRepresentante) throws SQLException {
		VerbaSaldo verbaSaldoPda = getVerbaSaldoInstanced(pedido, pedido.cdEmpresa, cdRepresentante, Verba.VERBA_PDA);
		verbaSaldoPda = (VerbaSaldo) findByRowKey(verbaSaldoPda.getRowKey());
		if (verbaSaldoPda == null) {
			verbaSaldoPda = new VerbaSaldo();
			verbaSaldoPda.setCdEmpresa(pedido.cdEmpresa);
			verbaSaldoPda.cdRepresentante = cdRepresentante;
			verbaSaldoPda.cdContaCorrente = pedido.getCdContaCorrente();
			verbaSaldoPda.flOrigemSaldo = Verba.VERBA_PDA;
			verbaSaldoPda.dtSaldo = new Date();
			insert(verbaSaldoPda);
		}
		return verbaSaldoPda;
	}
	

}
