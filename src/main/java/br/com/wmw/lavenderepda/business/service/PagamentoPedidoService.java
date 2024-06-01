package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.PagamentoPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoBoleto;
import br.com.wmw.lavenderepda.business.domain.TipoPagamento;
import br.com.wmw.lavenderepda.business.domain.VenctoPagamentoPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PagamentoPedidoDbxDao;
import totalcross.sys.Convert;
import totalcross.sys.InvalidNumberException;
import totalcross.util.Date;
import totalcross.util.Vector;

public class PagamentoPedidoService extends CrudService {

    private static PagamentoPedidoService instance;
    
    private PagamentoPedidoService() {
        //--
    }
    
    public static PagamentoPedidoService getInstance() {
        if (instance == null) {
            instance = new PagamentoPedidoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return PagamentoPedidoDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    	if (!LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido()) {
    		PagamentoPedido pagamentoPedido = (PagamentoPedido) domain;
    		//cdEmpresa
    		if (ValueUtil.isEmpty(pagamentoPedido.cdEmpresa)) {
    			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PAGAMENTOPEDIDO_LABEL_CDEMPRESA);
    		}
    		//cdRepresentante
    		if (ValueUtil.isEmpty(pagamentoPedido.cdRepresentante)) {
    			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PAGAMENTOPEDIDO_LABEL_CDREPRESENTANTE);
    		}
    		//cdPagamentoPedido
    		if (ValueUtil.isEmpty(pagamentoPedido.cdPagamentoPedido)) {
    			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PAGAMENTOPEDIDO_LABEL_CDPAGAMENTOPEDIDO);
    		}
    		//nuPedido
    		if (ValueUtil.isEmpty(pagamentoPedido.nuPedido)) {
    			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PAGAMENTOPEDIDO_LABEL_NUPEDIDO);
    		}
    		//flOrigemPedido
    		if (ValueUtil.isEmpty(pagamentoPedido.flOrigemPedido)) {
    			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PAGAMENTOPEDIDO_LABEL_FLORIGEMPEDIDO);
    		}
    		//cdTipoPagamento
    		if (ValueUtil.isEmpty(pagamentoPedido.cdTipoPagamento) && ! LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido()) {
    			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PAGAMENTOPEDIDO_LABEL_CDTIPOPAGAMENTO);
    		}
    		//cdCondicaoPagamento
    		if (ValueUtil.isEmpty(pagamentoPedido.cdCondicaoPagamento) && ! LavenderePdaConfig.isOcultaSelecaoCondicaoPagamentoPagamentoPedido()) {
    			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PAGAMENTOPEDIDO_LABEL_CDCONDICAOPAGAMENTO);
    		}
    		//vlPagamentoPedido
    		if (pagamentoPedido.vlPagamentoPedido == 0 && ! LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido()) {
    			if (LavenderePdaConfig.usaIndiceFinanceiroTipoPagamentoPagamentoPedido) {
    				throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PAGAMENTOPEDIDO_LABEL_VLPAGAMENTOBRUTO);
    			} else {
    			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PAGAMENTOPEDIDO_LABEL_VLPAGAMENTOPEDIDO);
    		}
    		}
    		//cdUsuario
    		if (ValueUtil.isEmpty(pagamentoPedido.cdUsuario)) {
    			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PAGAMENTOPEDIDO_LABEL_CDUSUARIO);
    		}
    		if (LavenderePdaConfig.usaDescontoMultiplosPagamentosParaPedido && pagamentoPedido.vlDesconto > pagamentoPedido.vlPagamentoPedido) {
    			throw new ValidationException(Messages.PAGAMENTOPEDIDO_VLDESCONTO_MAIOR_VLPAGAMENTO);
    		}
    		if (pagamentoPedido.vlPagamentoPedido > pagamentoPedido.vlPedidoAberto) {
    			throw new ValidationException(Messages.PAGAMENTOPEDIDO_VLPAGAMENTO_MAIOR_VLABERTO);
    		}
    		if (LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagto() && pagamentoPedido.vlMinimo > 0 && pagamentoPedido.vlPagamentoPedido < pagamentoPedido.vlMinimo) {
    			throw new ValidationException(Messages.PAGAMENTOPEDIDO_VLTOTAL_VLPAGAMENTO_MENOR_VLMINIMO);
    		}
    		double vlDescontoMax = (pagamentoPedido.vlPctDescontoMaxCondicaoPagamento / 100) * pagamentoPedido.vlPagamentoPedido;
    		if (LavenderePdaConfig.usaPctMaxDescontoPagamentoPorCondPagto && vlDescontoMax < pagamentoPedido.vlDesconto) {
    			throw new ValidationException(Messages.PAGAMENTOPEDIDO_VLTOTAL_PCT_DESCONTO_MAIOR_PERMITIDO);
    		}
    		validaTipoPagamento(pagamentoPedido);
    		validaInformacoesRelacionadasACheque(pagamentoPedido);
    		validaInformacoesRelacionadasAoVencimento(pagamentoPedido);
    	}
    }
    
    private void validaTipoPagamento(PagamentoPedido pagamentoPedido) throws SQLException {
		TipoPagamento tipoPagamento = TipoPagamentoService.getInstance().getTipoPagamento(pagamentoPedido.cdTipoPagamento);
		if (tipoPagamento != null && !tipoPagamento.isPermiteMultiplosPagamentos()) {
			PagamentoPedido pagamentoPedidoFilter = (PagamentoPedido) pagamentoPedido.clone();
			pagamentoPedidoFilter.cdPagamentoPedido = null;
			Vector pagamentoPedidoList = findAllByExample(pagamentoPedidoFilter);
			if (ValueUtil.isNotEmpty(pagamentoPedidoList) && !pagamentoPedidoList.contains(pagamentoPedido)) {
				throw new ValidationException(Messages.PAGAMENTO_PEDIDO_TIPOPAGAMENTO_DUPLICADO);
			}
		}
	}
   
	private void validaInformacoesRelacionadasACheque(PagamentoPedido pagamentoPedido) {
    	if (pagamentoPedido.validaInformacoesRelacionadasACheque) {
    		if (LavenderePdaConfig.isUsaDominioPagamentoCheque(PagamentoPedido.CODIGO_BANCO) &&  ValueUtil.isEmpty(pagamentoPedido.nuBanco)) {
    			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PAGAMENTOPEDIDO_LABEL_CODIGO_BANCO);
    		}
    		if (LavenderePdaConfig.isUsaDominioPagamentoCheque(PagamentoPedido.AGENCIA) && ValueUtil.isEmpty(pagamentoPedido.nuAgencia)) {
    			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PAGAMENTOPEDIDO_LABEL_AGENCIA);
    		}
    		if (LavenderePdaConfig.isUsaDominioPagamentoCheque(PagamentoPedido.CONTA) && ValueUtil.isEmpty(pagamentoPedido.nuConta)) {
    			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PAGAMENTOPEDIDO_LABEL_CONTA);
    		}
    		if (LavenderePdaConfig.isUsaDominioPagamentoCheque(PagamentoPedido.COMPLEMENTO)  && ValueUtil.isEmpty(pagamentoPedido.nuComplemento)) {
    			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PAGAMENTOPEDIDO_LABEL_COMPLEMENTO);
    		}
    		if (LavenderePdaConfig.isUsaDominioPagamentoCheque(PagamentoPedido.CHEQUE)  && ValueUtil.isEmpty(pagamentoPedido.nuCheque)) {
    			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PAGAMENTOPEDIDO_LABEL_CHEQUE);
    		}
    		if (LavenderePdaConfig.isUsaDominioPagamentoCheque(PagamentoPedido.CHEQUE_TERCEIRO)  &&  ValueUtil.valueEquals(ValueUtil.VALOR_SIM, pagamentoPedido.flChequeTerceiro) && ValueUtil.isEmpty(pagamentoPedido.dsEmitente)) {
    			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PAGAMENTOPEDIDO_LABEL_EMITENTE);
    		}
    		if (LavenderePdaConfig.isUsaDominioPagamentoCheque(PagamentoPedido.REFERENTE_A)  &&  ValueUtil.isEmpty(pagamentoPedido.dsReferenteCheque)) {
    			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PAGAMENTOPEDIDO_LABEL_REFERENTE_CHEQUE);
    		}
    		if (LavenderePdaConfig.isUsaDominioPagamentoCheque(PagamentoPedido.DESCRICAO_BANCO)  &&  ValueUtil.isEmpty(pagamentoPedido.dsBanco)) {
    			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PAGAMENTOPEDIDO_LABEL_BANCO_CHEQUE);
    		}
    		if (LavenderePdaConfig.isUsaDominioPagamentoCheque(PagamentoPedido.DATA_CHEQUE)  &&  ValueUtil.isEmpty(pagamentoPedido.dtCheque)) {
    			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PAGAMENTOPEDIDO_LABEL_DATA_CHEQUE);
    		}
    		
    	}
    	
    	
	}
	
	private void validaInformacoesRelacionadasAoVencimento(PagamentoPedido pagamentoPedido) {
		if (pagamentoPedido.validaInformacoesRelacionadasAoVencimento) {
        	if (ValueUtil.isEmpty(pagamentoPedido.dtVencimento)) {
    			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PAGAMENTOPEDIDO_LABEL_DTVENCIMENTO);
    		}
    		if (pagamentoPedido.dtVencimento.isBefore(DateUtil.getCurrentDate())) {
    			throw new ValidationException(Messages.PAGAMENTOPEDIDO_MSG_DTVENCIMENTO_ANTERIOR_DATAATUAL);
    		}
    		if ((LavenderePdaConfig.permiteAlterarVencimentoConformeCondPagto || LavenderePdaConfig.permiteAlterarVencimentoConformeCliente) && pagamentoPedido.dtVencimento.isAfter(pagamentoPedido.dtVencimentoBase)) {
    			throw new ValidationException(MessageUtil.getMessage(Messages.PAGAMENTOPEDIDO_MSG_DTVENCIMENTO_LIMITE_CONDICAO_PAGAMENTO_ULTRAPASSADO, pagamentoPedido.dtVencimentoBase));
    		} else if (LavenderePdaConfig.nuDiasMaximoVencimentoToleranciaPagamento > 0) {
	    		if (pagamentoPedido.dtVencimento.isBefore(pagamentoPedido.dtVencimentoBase) && DateUtil.getDaysBetween(pagamentoPedido.dtVencimentoBase, pagamentoPedido.dtVencimento) > LavenderePdaConfig.nuDiasMaximoVencimentoToleranciaPagamento) {
	    			throw new ValidationException(MessageUtil.getMessage(Messages.PAGAMENTOPEDIDO_MSG_DTVENCIMENTO_LIMITE_ULTRAPASSADO, new String[]{pagamentoPedido.dtVencimentoBase.toString(), StringUtil.getStringValue(LavenderePdaConfig.nuDiasMaximoVencimentoToleranciaPagamento)}));
	    		}
	    		if (pagamentoPedido.dtVencimento.isAfter(pagamentoPedido.dtVencimentoBase) && DateUtil.getDaysBetween(pagamentoPedido.dtVencimento, pagamentoPedido.dtVencimentoBase) > LavenderePdaConfig.nuDiasMaximoVencimentoToleranciaPagamento) {
	    			throw new ValidationException(MessageUtil.getMessage(Messages.PAGAMENTOPEDIDO_MSG_DTVENCIMENTO_LIMITE_ULTRAPASSADO, new String[]{pagamentoPedido.dtVencimentoBase.toString(), StringUtil.getStringValue(LavenderePdaConfig.nuDiasMaximoVencimentoToleranciaPagamento)}));
	    		}
    		}
		}
	}

    
    @Override
    public void insert(BaseDomain domain) throws SQLException {
    	final PagamentoPedido pagamentoPedido = (PagamentoPedido) domain;
		pagamentoPedido.cdPagamentoPedido = PagamentoPedidoService.getInstance().generateIdGlobal();
    	super.insert(pagamentoPedido);
    	insertVencimentos(pagamentoPedido, false);
    }

    @Override
    public void update(BaseDomain domain) throws SQLException {
    	final PagamentoPedido pagamentoPedido = (PagamentoPedido) domain;
		super.update(pagamentoPedido);
    	insertVencimentos(pagamentoPedido, true);
    }
    
    @Override
    public void delete(BaseDomain domain) throws SQLException {
    	final PagamentoPedido pagamentoPedido = (PagamentoPedido) domain;
    	super.delete(pagamentoPedido);
    	deleteVencimentos(pagamentoPedido);
    	
    }

	private void deleteVencimentos(final PagamentoPedido pagamentoPedido) throws SQLException {
    	VenctoPagamentoPedidoService.getInstance().deleteVctosByPagamentoPedido(pagamentoPedido);
	}
    
	private void insertVencimentos(final PagamentoPedido pagamentoPedido, boolean edit) throws SQLException {
		if (edit) {
			deleteVencimentos(pagamentoPedido);
		}
		if (pagamentoPedido.venctoPagamentoPedidos != null && !pagamentoPedido.venctoPagamentoPedidos.isEmpty()) {
			for (VenctoPagamentoPedido venctoPagamentoPedido : pagamentoPedido.venctoPagamentoPedidos) {
				venctoPagamentoPedido.cdPagamentoPedido = pagamentoPedido.cdPagamentoPedido;
				VenctoPagamentoPedidoService.getInstance().insert(venctoPagamentoPedido);
			}
		}
	}
    
    
	public double getVlPagamentosPedido(Pedido pedido) throws SQLException {
    	return pedido.isFlOrigemPedidoPda() ? sumByExample(getPagamentoPedidoFilter(pedido), PagamentoPedido.NM_COLUNA_VLPAGAMENTOPEDIDO) :  PagamentoPedidoDbxDao.getInstanceErp().sumByExample(getPagamentoPedidoFilter(pedido), PagamentoPedido.NM_COLUNA_VLPAGAMENTOPEDIDO);
	}

	public void deleteByPedido(Pedido pedido) throws SQLException {
    	deleteAllByExample(getPagamentoPedidoFilter(pedido));
	}

	public PagamentoPedido getPagamentoPedidoFilter(Pedido pedido) {
		PagamentoPedido pagamentoPedidoFilter = new PagamentoPedido();
    	pagamentoPedidoFilter.cdEmpresa = pedido.cdEmpresa;
    	pagamentoPedidoFilter.cdRepresentante = pedido.cdRepresentante;
    	pagamentoPedidoFilter.flOrigemPedido = pedido.flOrigemPedido;
    	pagamentoPedidoFilter.nuPedido = pedido.nuPedido;
		return pagamentoPedidoFilter;
	}
	
	public PagamentoPedido getPagamentoPedidoFilter(PedidoBoleto pedidoBoleto) {
		PagamentoPedido pagamentoPedidoFilter = new PagamentoPedido();
    	pagamentoPedidoFilter.cdEmpresa = pedidoBoleto.cdEmpresa;
    	pagamentoPedidoFilter.cdRepresentante = pedidoBoleto.cdRepresentante;
    	pagamentoPedidoFilter.flOrigemPedido = pedidoBoleto.flOrigemPedido;
    	pagamentoPedidoFilter.nuPedido = pedidoBoleto.nuPedido;
    	pagamentoPedidoFilter.cdPagamentoPedido = pedidoBoleto.cdPagamentoPedido;
		return pagamentoPedidoFilter;
	}

	public void validaPagamentosPedido(Pedido pedido) throws SQLException {
		double vlPedido = arredondaValor(LavenderePdaConfig.detalhaInfoTributariaPedidoEItemPedido ? pedido.vlFinalPedidoDescTribFrete : pedido.vlTotalPedido);
		if (vlPedido > 0) {
			vlPedido -= LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && pedido.isPedidoVenda() ? pedido.vlTotalTrocaPedido : 0;
			vlPedido = arredondaValor(vlPedido);
		}
		double vlPagamentosPedido = arredondaValor(getVlPagamentosPedido(pedido));
		if (vlPedido > vlPagamentosPedido) {
			throw new ValidationException(Messages.PAGAMENTOPEDIDO_VLABERTO_FECHAMENTO_PEDIDO);
		}
		if (vlPedido < vlPagamentosPedido) {
			throw new ValidationException(Messages.PAGAMENTOPEDIDO_VLTOTAL_PARAMENTOS_MAIOR_VLPEDIDO);
		}
	}

	//TODO esta sendo feito dessa maneira porque estamos com problema de diferenca de arredondamento da tela com o tocurrency para o round do valueUtil
	public double arredondaValor(double valor) {
    	try {
			return Convert.toDouble(ValueUtil.removeCurrency(StringUtil.getStringValueSimple(valor, LavenderePdaConfig.nuCasasDecimais, true)));
		} catch (InvalidNumberException e) {
			throw new RuntimeException(e);
		}
	}
 
	public PagamentoPedido getPagamentoPedido(Pedido pedido) throws SQLException {
		PagamentoPedido pagamentoPedido;
		if (LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido()) {
			Vector pagamentoPedidoList = findAllByExample(getPagamentoPedidoFilter(pedido));
			if (pagamentoPedidoList.size() > 0) {
				pagamentoPedido = (PagamentoPedido) pagamentoPedidoList.items[0];
				return pagamentoPedido;
			}
		}
		pagamentoPedido = new PagamentoPedido();
		pagamentoPedido.isNovo = true;
		return pagamentoPedido;
	}
	
	public PagamentoPedido getPagamentoPedido(PedidoBoleto pedidoBoleto) throws SQLException {
		PagamentoPedido pagamentoPedido = (PagamentoPedido)findByPrimaryKey(getPagamentoPedidoFilter(pedidoBoleto));
		if (pagamentoPedido != null) {
			return pagamentoPedido;
		}
		pagamentoPedido = new PagamentoPedido();
		pagamentoPedido.isNovo = true;
		return pagamentoPedido;
	}
 
	@Override
	public Vector findAllByExample(BaseDomain domain) throws java.sql.SQLException {
		if (OrigemPedido.FLORIGEMPEDIDO_PDA.equals(((PagamentoPedido) domain).flOrigemPedido)) {
			return super.findAllByExample(domain);
		} else {
			return PagamentoPedidoDbxDao.getInstanceErp().findAllByExample(domain);
		}
	}
		
	public void insertDebitoBancario(PagamentoPedido pagamentoPedido, Pedido pedido) throws SQLException {
		pagamentoPedido.cdEmpresa = pedido.cdEmpresa;
		pagamentoPedido.cdRepresentante = pedido.cdRepresentante;
		pagamentoPedido.nuPedido = pedido.nuPedido;
		pagamentoPedido.flOrigemPedido = pedido.flOrigemPedido;
		pagamentoPedido.cdCondicaoPagamento = pedido.cdCondicaoPagamento;
		pagamentoPedido.cdUsuario = pedido.cdUsuario;
		insert(pagamentoPedido);
	}
	
	public void updateDebitoBancario(PagamentoPedido pagamentoPedido) throws SQLException {
		try {
			validateDuplicated(pagamentoPedido);
			insert(pagamentoPedido);
		} catch (ValidationException e) {
			update(pagamentoPedido);
		}
	}
	
	public double getSomaDinheiroPedidoPorData(Date dtFechamentoInicial, Date dtFechamentoFinal) throws SQLException {
		String cdEmpresa = SessionLavenderePda.cdEmpresa;
		String cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(PagamentoPedido.class);
		return somaDinheiroPedidoPorData(cdEmpresa, cdRepresentante, dtFechamentoInicial, dtFechamentoFinal);
	}
	
	public double somaDinheiroPedidoPorData(String cdEmpresa, String cdRepresentante, Date dtFechamentoInicial, Date dtFechamentoFinal) throws SQLException {
		return PagamentoPedidoDbxDao.getInstance().somaDinheiroPedidoPorData(cdEmpresa, cdRepresentante, dtFechamentoInicial, dtFechamentoFinal);
	}
	
	public double somaChequePedidoPorData(String cdEmpresa, String cdRepresentante, Date dtFechamentoInicial, Date dtFechamentoFinal) throws SQLException {
		return PagamentoPedidoDbxDao.getInstance().somaChequePedidoPorData(cdEmpresa, cdRepresentante, dtFechamentoInicial, dtFechamentoFinal);
	}

	public double somaBoletoPedidoPorData(String cdEmpresa, String cdRepresentante, Date dtFechamentoInicial, Date dtFechamentoFinal) throws SQLException {
		return PagamentoPedidoDbxDao.getInstance().somaBoletoPedidoPorData(cdEmpresa, cdRepresentante, dtFechamentoInicial, dtFechamentoFinal);
	}
	
	public double somaOutrosPedidoPorData(String cdEmpresa, String cdRepresentante, Date dtEmissaoInicial, Date dtEmissaoFinal) throws SQLException {
		return PagamentoPedidoDbxDao.getInstance().somaOutrosPedidoPorData(cdEmpresa, cdRepresentante, dtEmissaoInicial, dtEmissaoFinal);
	}	

	public double somaTotalVendasPorData(String cdEmpresa, String cdRepresentante,  Date dtFechamentoInicial,  Date dtFechamentoFinal) throws SQLException {
		return PagamentoPedidoDbxDao.getInstance().somaTotalVendasPorData(cdEmpresa, cdRepresentante, dtFechamentoInicial, dtFechamentoFinal);
	}

	public double somaTotalPedidosPorData(String cdEmpresa, String cdRepresentante, Date dtFechamentoInicial, Date dtFechamentoFinal) throws SQLException {
		double vlTotalPedidoPorData = PagamentoPedidoDbxDao.getInstance().somaTotalPedidosPorData(cdEmpresa, cdRepresentante, dtFechamentoInicial, dtFechamentoFinal);
		vlTotalPedidoPorData += LavenderePdaConfig.utilizaNotasCredito() ? PedidoService.getInstance().somaVlTotalNotaCredito(cdEmpresa, cdRepresentante, dtFechamentoInicial, dtFechamentoFinal) : 0;
		return vlTotalPedidoPorData + PedidoService.getInstance().somaVlTotalDesconto(cdEmpresa, cdRepresentante, dtFechamentoInicial, dtFechamentoFinal);
	}

	public double somaTotalReceitasPorData(String cdEmpresa, String cdRepresentante, Date dtFechamentoInicial, Date dtFechamentoFinal) throws SQLException {
		return PagamentoPedidoDbxDao.getInstance().somaTotalReceitasPorData(cdEmpresa, cdRepresentante, dtFechamentoInicial, dtFechamentoFinal);
	}

	public double somaTotalBonificacaoPorData(String cdEmpresa, String cdRepresentante, Date dtFechamentoInicial, Date dtFechamentoFinal) throws SQLException {
		return PagamentoPedidoDbxDao.getInstance().somaTotalBonificacaoPorData(cdEmpresa, cdRepresentante, dtFechamentoInicial, dtFechamentoFinal);
	}

	
	protected boolean isPedidoContemMultiplosPagamentos(Pedido pedido) throws SQLException {
		return countByExample(getPagamentoPedidoFilter(pedido)) > 0;
	}
	
	protected Vector findPagamentoPedidoByPedidoList(Pedido pedido) throws SQLException {
		return findAllByExample(getPagamentoPedidoFilter(pedido));
	}
	
	public boolean isAnyTipoPagamentoGeraBoletoFromPagamentoPedidoList(Pedido pedido) throws SQLException {
		for (int i = 0; i < pedido.pagamentoPedidoList.size(); i++) {
			PagamentoPedido pagamentoPedido = (PagamentoPedido) pedido.pagamentoPedidoList.items[i];
			TipoPagamento tipoPagamento = TipoPagamentoService.getInstance().getTipoPagamento(pagamentoPedido.cdTipoPagamento);
			if (tipoPagamento != null && ValueUtil.VALOR_SIM.equals(tipoPagamento.flBoleto)) {
				return true;
			}
		}
		return false;
	}
		
	public List<PagamentoPedido> buscaDadosChequePor(String cdEmpresa, String cdRepresentante, Date dtFechamentoInicial, Date dtFechamentoFinal) throws java.sql.SQLException {
		return PagamentoPedidoDbxDao.getInstance().buscaDadosChequePor(cdEmpresa, cdRepresentante, dtFechamentoInicial,dtFechamentoFinal);
	}
}