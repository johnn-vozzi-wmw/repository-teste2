package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.CondComCondPagto;
import br.com.wmw.lavenderepda.business.domain.CondPagtoCli;
import br.com.wmw.lavenderepda.business.domain.CondPagtoRep;
import br.com.wmw.lavenderepda.business.domain.CondPagtoSeg;
import br.com.wmw.lavenderepda.business.domain.CondPagtoTabPreco;
import br.com.wmw.lavenderepda.business.domain.CondTipoPagto;
import br.com.wmw.lavenderepda.business.domain.CondTipoPedido;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TipoCondPagtoCli;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.validation.BloqueioCondPagtoPorDiasClienteException;
import br.com.wmw.lavenderepda.business.validation.FilterNotInformedException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CondicaoPagamentoPdbxDao;
import totalcross.util.Vector;

public class CondicaoPagamentoService extends CrudService {

    private static CondicaoPagamentoService instance;

    private CondicaoPagamentoService() {
        //--
    }

    public static CondicaoPagamentoService getInstance() {
        if (instance == null) {
            instance = new CondicaoPagamentoService();
        }
        return instance;
    }

	public Vector findAllByExampleOrderByQtDiasMedio(BaseDomain domain) throws SQLException {
		return CondicaoPagamentoPdbxDao.getInstance().findAllByExampleOrderByQtDiasMedio(domain);
	}

    //@Override
    protected CrudDao getCrudDao() {
        return CondicaoPagamentoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public Vector loadCondicoesPagamento(Cliente cliente, TipoPedido tipoPedido, String cdTabelaPreco, String cdSegmento, String cdCondicaoComercial, boolean flPagamentoAVista, double qtMinProduto, String orderByColumn) throws SQLException {
    	return loadCondicoesPagamento(cliente, tipoPedido, cdTabelaPreco, cdSegmento, cdCondicaoComercial, null, flPagamentoAVista, -1, qtMinProduto, null ,-1, orderByColumn);
    }

    private Vector loadCondicoesPagamento(Cliente cliente, TipoPedido tipoPedido, String cdTabelaPreco, String cdSegmento, String cdCondicaoComercial, String cdTipoPagamento, boolean flPagamentoAVista, double vlTotalPedido, double qtMinProduto, String flClienteAtrasadoLiberadoSenha , int qtProdutosPedido, String orderByColumn) throws SQLException {
    	CondicaoPagamento cond = new CondicaoPagamento();
    	cond.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	cond.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondicaoPagamento.class);
    	if (LavenderePdaConfig.isBloqueiaCondPagtoPorDiasCliente() && cliente != null) {
    		filterByQtDias(cond, cliente.qtDiasMaximoPagamento);
    	}
    	if (pedidoAvistaQtdDiasMedioPagto(cliente, flPagamentoAVista, flClienteAtrasadoLiberadoSenha)) {
    		cond.filterByQtDiasMedioPagto = true;
			cond.qtDiasMediosPagamento = 0;
		}
    	cond.flAbertura = cliente != null ? cliente.isAbertura ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NI : null;
    	cond.flFundacao =  cliente != null ? cliente.isFundacao ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NI : null;
    	if (ValueUtil.isNotEmpty(orderByColumn)) {
    		cond.sortAtributte = orderByColumn;
    		cond.sortAsc = ValueUtil.VALOR_SIM;
    	}
		try {
	    	prepareCondTipoPedidoFilter(tipoPedido, cond);
	    	prepareCondPagtoTabPrecoFilter(cond, cdTabelaPreco, vlTotalPedido, qtMinProduto);
	    	prepareCondPagtoValoresMinimos(cond, vlTotalPedido, qtMinProduto);
	    	prepareCondPagtoCliFilter(cliente, cond);
	    	prepareTipoCondPagtoCliFilter(cliente, cond, tipoPedido, cdTipoPagamento);
	    	prepareCondPagtoTipoPagtoPadraoCliente(cliente, cond);
	    	prepareCondPagtoTipoCondPagtoContratoEspecial(cliente, cond);
	    	prepareCondPagtoSegFilter(cdSegmento, cond);
	    	prepareCondComCondPagtoFilter(cdCondicaoComercial, cond);
	    	prepareCondTipoPagtoFilter(tipoPedido, cdTipoPagamento, cond);
	    	prepareCondPagtoRepFilter(cond);
	    	prepareCondPagtoQtMinMixProduto(qtProdutosPedido, cond);
	    	prepareCondPagtoGrupoCondicao(cliente, cond);
		} catch (FilterNotInformedException ex) {
			return new Vector(0);
		}
    	return findAllByExample(cond);
    }

	private boolean pedidoAvistaQtdDiasMedioPagto(Cliente cliente, boolean flPagamentoAVista, String flClienteAtrasadoLiberadoSenha) throws SQLException {
		return (flPagamentoAVista && (LavenderePdaConfig.isApresentaPopUPPedidoAVista() || LavenderePdaConfig.isApresentaPopUPPedidoAVistaSenha())) || cliente != null && 
    	   (((LavenderePdaConfig.isPermitePedidoAVistaClienteAtrasado() && cliente.isStatusAtrasado()) || (LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado() && (cliente.isStatusBloqueado() || cliente.isStatusBloqueadoPorAtraso())))) &&
    	   !apresentaTodasCondicoesPagamento(flClienteAtrasadoLiberadoSenha, cliente);
	}
    
    private boolean apresentaTodasCondicoesPagamento(String flClienteAtrasadoLiberadoSenha, Cliente cliente) throws SQLException {
    	return (ValueUtil.VALOR_SIM.equalsIgnoreCase(flClienteAtrasadoLiberadoSenha) || ClienteService.getInstance().isClienteComAtrasoTolerancia(cliente)) 
    			&& !ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.permitePedidoAVistaClienteBloqueadoAtrasado);
	}

	private void filterByQtDias(CondicaoPagamento cond, int qtDias) {
		if (LavenderePdaConfig.isLiberaComSenhaCondPagamento() || LavenderePdaConfig.usaMarcaPedidoPendenteAprovacaoQtdDiasCondPagto()) {
			return;
		}
    	if (LavenderePdaConfig.isBloqueiaCondPagtoPorDiasMediosCliente()) {
    		cond.filterByQtDiasMedioPagto = true;
			cond.qtDiasMediosPagamento = qtDias;
		} else if (LavenderePdaConfig.isBloqueiaCondPagtoPorDiasMaximoCliente()) {
			cond.filterByQtDiasMaximoPagto = true;
			cond.qtDiasMaximoPagamento = qtDias;
		}
    }

	public Vector loadCondicoesPagamento(Pedido pedido) throws SQLException {
		return loadCondicoesPagamento(pedido, null);
	}

	public Vector loadCondicoesPagamento(Pedido pedido, String orderByColumn) throws SQLException {
		boolean isPagamentoAVista = LavenderePdaConfig.liberaTipoCondPagtoPedidoDias ? !ClienteService.getInstance().isClienteComAtrasoTolerancia(pedido.getCliente()) : pedido.isPagamentoAVista();
		double vlPedido = pedido.vlTotalPedido;
		double qtProduto = 0;
		Vector itemPedidoList = pedido.itemPedidoList;
		int qtItensPedido;
		if (!(LavenderePdaConfig.usaBotaoIgnorarValidacoesPedido && pedido.isPendente())) {
			qtItensPedido = itemPedidoList.size();
			for (int i = 0; i < qtItensPedido; i++) {
				ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
				qtProduto += ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico());
			}
		} else {
			qtItensPedido = -1;
			vlPedido = -1;
			qtProduto = -1;
		}

    	return loadCondicoesPagamento(pedido.getCliente(), pedido.getTipoPedido(), pedido.cdTabelaPreco, pedido.cdSegmento, pedido.cdCondicaoComercial, pedido.cdTipoPagamento, isPagamentoAVista, vlPedido, qtProduto, pedido.flClienteAtrasadoLiberadoSenha, qtItensPedido, orderByColumn);
    }
	
    public CondicaoPagamento getCondicaoPagamento(String cdCondicaoPagamento) throws SQLException {
    	if (cdCondicaoPagamento == null) {
    		return new CondicaoPagamento();
    	}
    	CondicaoPagamento condicaoPagamentoFilter = getCondicaoPagamentoFilter(cdCondicaoPagamento);
    	return (CondicaoPagamento) findByRowKey(condicaoPagamentoFilter.getRowKey());
    }
    
    
    public double getPctDescMaxCondicaoPagamento(String cdCondicaoPagamento) throws SQLException {
    	CondicaoPagamento condicaoPagamentoFilter = getCondicaoPagamentoFilter(cdCondicaoPagamento);
    	return ValueUtil.getDoubleValue(findColumnByRowKey(condicaoPagamentoFilter.getRowKey(), CondicaoPagamento.NM_COLUNA_VLPCTDESCONTOTOTALPEDIDO));
    }

	private CondicaoPagamento getCondicaoPagamentoFilter(String cdCondicaoPagamento) {
		CondicaoPagamento condicaoPagamentoFilter = new CondicaoPagamento();
    	condicaoPagamentoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	condicaoPagamentoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondicaoPagamento.class);
    	condicaoPagamentoFilter.cdCondicaoPagamento = cdCondicaoPagamento;
		return condicaoPagamentoFilter;
	}
    
    public double getQtVlMinCondicaoPagamento(String cdCondicaoPagamento) throws SQLException {
    	CondicaoPagamento condicaoPagamentoFilter = getCondicaoPagamentoFilter(cdCondicaoPagamento);
    	return ValueUtil.getDoubleValue(findColumnByRowKey(condicaoPagamentoFilter.getRowKey(), CondicaoPagamento.NM_COLUNA_QTVLMINVALOR));
    }
    
    public boolean isCondicaoPagamentoPedidoValida(Pedido pedido) throws SQLException {
    	if (pedido != null) {
    		Vector condicaoPagamentoList = loadCondicoesPagamento(pedido);
    		if (ValueUtil.isNotEmpty(condicaoPagamentoList)) {
    			for (int i = 0; i < condicaoPagamentoList.size(); i++) {
					CondicaoPagamento condicaoPagamento = (CondicaoPagamento) condicaoPagamentoList.items[i];
					if (ValueUtil.valueEquals(condicaoPagamento.cdCondicaoPagamento, pedido.cdCondicaoPagamento)) {
						return true;
					}
				}
    		}
    	}
    	return false;
    }

    public double loadVlPctDescAcresCondPagto(Pedido pedido) throws SQLException {
    	double vlIndiceCondPagto = pedido.getCondicaoPagamento().vlIndiceFinanceiro; 
    	if (LavenderePdaConfig.usaIndiceCondPagtoClienteConformePrazoMedio()) {
    		vlIndiceCondPagto = ItemPedidoService.getInstance().getVlIndiceFinanceiroCondicaoPagamentoConformePrazoMedio(pedido.getCondicaoPagamento(), pedido.getCliente());
		}
    	return loadVlPctDescAcresCondPagto(vlIndiceCondPagto);
    }
    
	private double loadVlPctDescAcresCondPagto(double vlIndiceCondPagto) throws SQLException {
		
		return (vlIndiceCondPagto == 0 || vlIndiceCondPagto == 1) ? 0 : (100 - (vlIndiceCondPagto * 100)) * -1;
	}
	
	public void validateCondPagtoPorDiasCliente(CondicaoPagamento condicaoPagamento, int qtDiasMaximoPagamentoCli) {
		int qtDias = LavenderePdaConfig.isBloqueiaCondPagtoPorDiasMediosCliente() ? condicaoPagamento.qtDiasMediosPagamento : condicaoPagamento.qtDiasMaximoPagamento;
		if (qtDiasMaximoPagamentoCli < qtDias) {
			throw new BloqueioCondPagtoPorDiasClienteException(Messages.CONDICAO_PAGAMENTO_BLOQUEADA);
		}
	}

	public int findQtDiasMediosPagamento(String cdEmpresa, String cdRepresentante, String cdCondicaoPagamento) throws SQLException {
		CondicaoPagamento condicaoPagamentoFilter = new CondicaoPagamento(cdEmpresa,  cdRepresentante, cdCondicaoPagamento);
		String qtDiasMediosPagamento = findColumnByRowKey(condicaoPagamentoFilter.getRowKey(), "qtDiasMediosPagamento");
		return ValueUtil.getIntegerValue(qtDiasMediosPagamento);
	}

	public boolean isCondicaoPadraoCliente(Cliente cliente, String cdCondicaoPagamento) {
		if (cliente == null) return false; 
		return ValueUtil.valueEquals(cliente.cdCondicaoPagamento, cdCondicaoPagamento);
	}
	
	private void prepareCondPagtoQtMinMixProduto(int qtProdutosPedido, CondicaoPagamento cond) {
		if (qtProdutosPedido > 0 && LavenderePdaConfig.usaQtdeMinimaProdutoPorCondPagamentoEQtMixProduto() && LavenderePdaConfig.isQtdeMinimaProdutoPorCondPagamentoFormaValidacaoPadrao()) {
    		cond.qtMinMixProduto = qtProdutosPedido;
    		cond.filtraQtMinMixProduto = true;
    	}
	}

	protected void prepareCondPagtoRepFilter(CondicaoPagamento cond) {
		if (LavenderePdaConfig.usaCondicaoPagamentoRepresentante && SessionLavenderePda.isUsuarioSupervisor()) {
    		cond.condPagtoRepFilter = new CondPagtoRep();
    		cond.condPagtoRepFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		cond.condPagtoRepFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	}
	}

	protected void prepareCondTipoPagtoFilter(TipoPedido tipoPedido, String cdTipoPagamento, CondicaoPagamento cond) {
		if (LavenderePdaConfig.usaCondicaoPagamentoPorTipoPagamento() && tipoPedido != null && !tipoPedido.isBonificacao()) {
    		if (ValueUtil.isEmpty(cdTipoPagamento)) {
    			throw new FilterNotInformedException();
    		}
    		cond.condTipoPagtoFilter = new CondTipoPagto();
    		cond.condTipoPagtoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		cond.condTipoPagtoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondTipoPagto.class);
    		cond.condTipoPagtoFilter.cdTipoPagamento = cdTipoPagamento;
    	}
	}

	private void prepareCondComCondPagtoFilter(String cdCondicaoComercial, CondicaoPagamento cond) {
		if (LavenderePdaConfig.usaCondicaoPagamentoPorCondicaoComercial) {
    		if (ValueUtil.isEmpty(cdCondicaoComercial)) {
    			throw new FilterNotInformedException();
    		}
    		cond.condComCondPagtoFilter = new CondComCondPagto();
    		cond.condComCondPagtoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		cond.condComCondPagtoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondComCondPagto.class);
    		cond.condComCondPagtoFilter.cdCondicaoComercial = cdCondicaoComercial;
    	}
	}

	private void prepareCondPagtoSegFilter(String cdSegmento, CondicaoPagamento cond) {
		if (LavenderePdaConfig.usaSegmentoNoPedido && LavenderePdaConfig.usaCondicaoPagamentoPorSegmento) {
    		if (ValueUtil.isEmpty(cdSegmento)) {
    			throw new FilterNotInformedException();
    		}
    		cond.condPagtoSegFilter = new CondPagtoSeg();
    		cond.condPagtoSegFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		cond.condPagtoSegFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondPagtoSeg.class);
    		cond.condPagtoSegFilter.cdSegmento = cdSegmento;
    		
    	}
	}

	private void prepareCondPagtoTipoCondPagtoContratoEspecial(Cliente cliente, CondicaoPagamento cond) {
		if (!LavenderePdaConfig.clienteComContratoExigeSetorPedido || cliente == null || ValueUtil.isNotEmpty(cliente.cdContratoEspecial)) {
			return;
		}
		cond.notCdTipoCondPagto = StringUtil.getStringValue(CondicaoPagamento.TIPOCONDPAGTO_VENCIMENTO);
	}

	private void prepareCondPagtoTipoPagtoPadraoCliente(Cliente cliente, CondicaoPagamento cond) {
		if (ValueUtil.isEmpty(LavenderePdaConfig.usaCondPagtoPorTipoPagtoPadraoCliente) || cliente == null || cliente.isNovoCliente() || cliente.isClienteDefaultParaNovoPedido()) {
			return;
		}
		cond.cdTipoPagamento = cliente.cdTipoPagamento;
	}

	private void prepareCondPagtoCliFilter(Cliente cliente, CondicaoPagamento cond) {
		if (!LavenderePdaConfig.usaCondicaoPagamentoPorCliente || cliente == null || cliente.isNovoCliente() || ValueUtil.isEmpty(cliente.cdCliente) || cliente.isClienteDefaultParaNovoPedido()) {
			return;
		}
		cond.condPagtoCliFilter = new CondPagtoCli();
		cond.condPagtoCliFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		cond.condPagtoCliFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondPagtoCli.class);
		cond.condPagtoCliFilter.cdCliente = cliente.cdCliente;
	}
	
	private void prepareTipoCondPagtoCliFilter(Cliente cliente, CondicaoPagamento cond, TipoPedido tipoPedido, String cdTipoPagamento) {
		if (LavenderePdaConfig.usaCondicaoPagamentoPorCliente || LavenderePdaConfig.usaTipoPagtoPorCondPagtoECondPagtoPorCliente.equals(ValueUtil.VALOR_NAO) || cliente == null || cliente.isNovoCliente() || cliente.isClienteDefaultParaNovoPedido()) {
			return;
		}
		if (ValueUtil.isEmpty(cliente.cdCliente)) {
			throw new FilterNotInformedException();
		}
		cond.tipoCondPagtoCliFilter = new TipoCondPagtoCli();
		if (LavenderePdaConfig.usaCondPagtoPorTipoPagtoECondPagtoPorCliente() && !tipoPedido.isBonificacao()) {
			if (ValueUtil.isEmpty(cdTipoPagamento)) {
    			throw new FilterNotInformedException();
    		}
			cond.tipoCondPagtoCliFilter.cdTipoPagamento = cdTipoPagamento;
		}
		cond.tipoCondPagtoCliFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		cond.tipoCondPagtoCliFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TipoCondPagtoCli.class);
		cond.tipoCondPagtoCliFilter.cdCliente = cliente.cdCliente;
	}

	private void prepareCondPagtoTabPrecoFilter(CondicaoPagamento cond, String cdTabelaPreco, double vlTotalPedido, double qtMinProduto) {
		if (LavenderePdaConfig.usarCondicaoPagtoPorTabelaPreco && ValueUtil.isNotEmpty(cdTabelaPreco)) {

			cond.condPagtoTabPrecoFilter = new CondPagtoTabPreco();
			cond.condPagtoTabPrecoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			cond.condPagtoTabPrecoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondPagtoTabPreco.class);
			cond.condPagtoTabPrecoFilter.cdTabelaPreco = cdTabelaPreco;
    		if (LavenderePdaConfig.usaValidacaoMinimosCondPagtoPorCondPagtoTabPreco) {
    			boolean validaQtMinValorCondPagtoSeparado = LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagtoRetiraOpcoesInvalidasCombo();
    			boolean validaQtMinProdutoCondPagtoSeparado = LavenderePdaConfig.usaQtdeMinimaProdutoPorCondPagamentoEQtProduto() && LavenderePdaConfig.isQtdeMinimaProdutoPorCondPagamentoFormaValidacaoPadrao();
    			if (!validaQtMinValorCondPagtoSeparado && !validaQtMinProdutoCondPagtoSeparado && vlTotalPedido != -1 && qtMinProduto >= 0) {
	    			cond.condPagtoTabPrecoFilter.qtMinProduto = (int)qtMinProduto;
	    			cond.condPagtoTabPrecoFilter.qtMinValor =  vlTotalPedido;
    			} else {
    				if (validaQtMinValorCondPagtoSeparado && vlTotalPedido != -1) {
    					cond.condPagtoTabPrecoFilter.qtMinValor =  vlTotalPedido;
    				}
    				if (validaQtMinProdutoCondPagtoSeparado && qtMinProduto != 0) {
    					cond.condPagtoTabPrecoFilter.qtMinProduto = (int)qtMinProduto;
    				}
    			}
    		}
		}
	}

	private void prepareCondPagtoValoresMinimos(CondicaoPagamento cond, double vlTotalPedido, double qtMinProduto) {
		if (LavenderePdaConfig.usaValidacaoMinimosCondPagtoPorCondPagtoTabPreco) {
			return;
		}
		if (LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagtoRetiraOpcoesInvalidasCombo() && vlTotalPedido != -1) {
			cond.setQtMinValor(vlTotalPedido);
			cond.filtraQtMinValor = true;
		}
		if (LavenderePdaConfig.usaQtdeMinimaProdutoPorCondPagamentoEQtProduto() && LavenderePdaConfig.isQtdeMinimaProdutoPorCondPagamentoFormaValidacaoPadrao() && qtMinProduto >= 0) {
			cond.setQtMinProduto((int)qtMinProduto);
			cond.filtraQtMinProduto = true;
		}
	}
	

	protected void prepareCondTipoPedidoFilter(TipoPedido tipoPedido, CondicaoPagamento condPagto) {
		if (LavenderePdaConfig.usaCondicaoPagamentoPorTipoPedido && tipoPedido != null) {
    		condPagto.condTipoPedidoFilter = new CondTipoPedido();
    		condPagto.condTipoPedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		condPagto.condTipoPedidoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondTipoPedido.class);
    		condPagto.condTipoPedidoFilter.cdTipoPedido = tipoPedido.cdTipoPedido;
    		condPagto.condTipoPedidoFilter.excecaoCondPagtoFilter = tipoPedido.isFlExcecaoCondPagto();
    	}
	}
	
	protected void prepareCondPagtoGrupoCondicao(Cliente cliente, CondicaoPagamento cond) {
		if (LavenderePdaConfig.usaGrupoCondPagtoCli && cliente != null && !cliente.isNovoCliente() && !cliente.isClienteDefaultParaNovoPedido() && ValueUtil.isNotEmpty(cliente.cdGrupoCondicao)) {
			cond.cdGrupoCondicao = cliente.cdGrupoCondicao;
		}
	}
}
