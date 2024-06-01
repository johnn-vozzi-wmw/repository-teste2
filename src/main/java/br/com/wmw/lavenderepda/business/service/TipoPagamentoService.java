package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.CondTipoPagto;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.PagamentoPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PreferenciaFuncao;
import br.com.wmw.lavenderepda.business.domain.TipoCondPagtoCli;
import br.com.wmw.lavenderepda.business.domain.TipoPagamento;
import br.com.wmw.lavenderepda.business.domain.TipoPagtoCli;
import br.com.wmw.lavenderepda.business.domain.TipoPagtoTabPreco;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoPagamentoPdbxDao;
import totalcross.util.Vector;

public class TipoPagamentoService extends CrudService {

    private static TipoPagamentoService instance;

    private TipoPagamentoService() {
        //--
    }

    public static TipoPagamentoService getInstance() {
        if (instance == null) {
            instance = new TipoPagamentoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return TipoPagamentoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public TipoPagamento getTipoPagamento(String cdTipoPagamento) throws SQLException {
    	TipoPagamento tipoPagamento = new TipoPagamento();
    	tipoPagamento.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	tipoPagamento.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TipoPagamento.class);
    	tipoPagamento.cdTipoPagamento = cdTipoPagamento;
    	return (TipoPagamento) findByRowKey(tipoPagamento.getRowKey());
    }

    public String getDsTipoPagamento(String cdTipoPagamento) throws SQLException {
    	TipoPagamento tipoPagamento = new TipoPagamento();
    	tipoPagamento.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	tipoPagamento.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TipoPagamento.class);
    	tipoPagamento.cdTipoPagamento = cdTipoPagamento;
    	tipoPagamento.dsTipoPagamento = findColumnByRowKey(tipoPagamento.getRowKey(), "DSTIPOPAGAMENTO");
    	return tipoPagamento.toString();
    }
    
    public Vector getTipoPagamentoList(Pedido pedido, boolean telaPagamento) throws SQLException {
    	TipoPagamento tipoPagamento = new TipoPagamento();
		tipoPagamento.cdEmpresa = SessionLavenderePda.cdEmpresa;
		tipoPagamento.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TipoPagamento.class);
		tipoPagamento.cdTipoPagamento = getCdTipoPagamentoFilter(pedido);
		tipoPagamento.notFlRestrito = LavenderePdaConfig.usaTipoPagamentoRestritoVenda ? ValueUtil.VALOR_SIM : null;
		tipoPagamento.notFlPermiteUsoModuloPagamento = telaPagamento ? ValueUtil.VALOR_NAO : null;
		tipoPagamento.notFlOcultoParaNovoCliente = pedido.getCliente().isNovoCliente() ? ValueUtil.VALOR_SIM : null;
		tipoPagamento.notFlIgnoraLimiteCredito = isFiltraPorIgnoraLimiteCredito() ? ValueUtil.VALOR_SIM : null;
		tipoPagamento.flPagamentoAVista = isFiltraPorPagamentoAVista(pedido) ? ValueUtil.VALOR_SIM : null;
		
		prepareTipoPagtoTabPrecoFilter(pedido, tipoPagamento);
		prepareCondTipoPagtoFilter(pedido, tipoPagamento);
		prepareTipoCondPagtoCliFilter(pedido, tipoPagamento);
		prepareTipoPagtoCliFilter(pedido, tipoPagamento);
		prepareTipoPagtoQuePossuiCondPagto(pedido, tipoPagamento);
		return findAllByExample(tipoPagamento);
    }
    
	private boolean apresentaTodosTiposPagamento(Cliente cliente) throws SQLException {
		return (cliente.flClienteAtrasadoLiberadoSenha || ClienteService.getInstance().isClienteComAtrasoTolerancia(cliente)) && 
				!ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.permitePedidoAVistaClienteBloqueadoAtrasado) && LavenderePdaConfig.isApresentaPopUPPedidoAVistaSenha();
	}
	
	public boolean isBoleto(PagamentoPedido pagamentoPedido) throws SQLException {
		TipoPagamento tipoPagamento = getTipoPagamento(pagamentoPedido.cdTipoPagamento);
		return tipoPagamento != null && tipoPagamento.isBoleto();
	}

	public void validaTipoPagamentoRestrito(Cliente cliente) throws SQLException {
		if (LavenderePdaConfig.usaTipoPagamentoRestritoVenda && cliente != null) {
			Pedido pedidoFilter = new Pedido();
			pedidoFilter.cdCliente = cliente.cdCliente;
			pedidoFilter.setCliente(cliente);
			Vector list = TipoPagamentoService.getInstance().getTipoPagamentoList(pedidoFilter, false);
			if (ValueUtil.isEmpty(list)) {
				throw new ValidationException(Messages.PEDIDO_MSG_ERRO_TIPO_PAGAMENTO_NENHUM_VALIDO);
			}
		}
	}

	public boolean isTipoPagamentoAVista(String cdTipoPagamento) throws SQLException {
		TipoPagamento tipoPagamento = new TipoPagamento();
		tipoPagamento.cdEmpresa = SessionLavenderePda.cdEmpresa;
		tipoPagamento.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TipoPagamento.class);
		tipoPagamento.cdTipoPagamento = cdTipoPagamento;
		String flPagamentoAVista = findColumnByRowKey(tipoPagamento.getRowKey(), "flPagamentoAVista");
		return ValueUtil.VALOR_SIM.equals(flPagamentoAVista);
	}
	
	private boolean isFiltraPorPagamentoAVista(Pedido pedido) throws SQLException {
		return (LavenderePdaConfig.isPermitePedidoAVistaClienteAtrasado() || LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado())
				&& pedido.isPagamentoAVista() && !apresentaTodosTiposPagamento(pedido.getCliente()) && TituloFinanceiroService.getInstance().getDiasAtrasoCliente(pedido.getCliente()) > 0;
	}

	private boolean isFiltraPorIgnoraLimiteCredito() {
		return LavenderePdaConfig.isBloqueiaTipoPagtoIgnoraLimiteCreditoPorFuncao() 
				&& !SessionLavenderePda.hasPreferencia(PreferenciaFuncao.PERMITE_TIPOPAGAMENTO_IGNORA_LIMITE_CREDITO);
	}
	
	private String getCdTipoPagamentoFilter(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaTipoPagamentoRestritoVenda) {
			return pedido.cdTipoPagamento;
		}
		return LavenderePdaConfig.isTipoPagamentoOcultoAndSetaPadraoCliente() ? pedido.getCliente().cdTipoPagamento : null;
	}
	
	protected void prepareTipoPagtoQuePossuiCondPagto(Pedido pedido, TipoPagamento tipoPagamento) throws SQLException {
		if (LavenderePdaConfig.usaCondicaoPagamentoPorTipoPagamento() && (LavenderePdaConfig.usaGrupoCondPagtoCli || LavenderePdaConfig.usaCondicaoPagamentoPorTipoPedido || LavenderePdaConfig.usaCondicaoPagamentoRepresentante)) {
			tipoPagamento.filtraQuandoPossuiCondPagto = true;
			tipoPagamento.condicaoPagamentoFilter = new CondicaoPagamento();
			tipoPagamento.condicaoPagamentoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			tipoPagamento.condicaoPagamentoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondicaoPagamento.class);
			tipoPagamento.condicaoPagamentoFilter.condTipoPagtoFilter = new CondTipoPagto();
			tipoPagamento.condicaoPagamentoFilter.condTipoPagtoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			tipoPagamento.condicaoPagamentoFilter.condTipoPagtoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondTipoPagto.class);
			CondicaoPagamentoService.getInstance().prepareCondTipoPedidoFilter(pedido.getTipoPedido(), tipoPagamento.condicaoPagamentoFilter);
			CondicaoPagamentoService.getInstance().prepareCondPagtoRepFilter(tipoPagamento.condicaoPagamentoFilter);
			CondicaoPagamentoService.getInstance().prepareCondPagtoGrupoCondicao(pedido.getCliente(), tipoPagamento.condicaoPagamentoFilter);
		}
	}

	protected void prepareTipoPagtoCliFilter(Pedido pedido, TipoPagamento tipoPagamento) throws SQLException {
		if (LavenderePdaConfig.usaTipoPagtoPorCondPagtoECondPagtoPorCliente.equals(ValueUtil.VALOR_NAO) && !pedido.getCliente().isNovoCliente() && !pedido.getCliente().isClienteDefaultParaNovoPedido() && ValueUtil.isNotEmpty(pedido.cdCliente)) {
			tipoPagamento.tipoPagtoCliFilter = new TipoPagtoCli();
			tipoPagamento.tipoPagtoCliFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			tipoPagamento.tipoPagtoCliFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TipoPagtoCli.class);
			tipoPagamento.tipoPagtoCliFilter.cdCliente = pedido.cdCliente;
		}
	}

	protected void prepareTipoCondPagtoCliFilter(Pedido pedido, TipoPagamento tipoPagamento) throws SQLException {
		if ((LavenderePdaConfig.usaTipoPagtoPorCondPagtoECondPagtoPorCliente() || LavenderePdaConfig.usaCondPagtoPorTipoPagtoECondPagtoPorCliente()) && !pedido.getCliente().isNovoCliente() && !pedido.getCliente().isClienteDefaultParaNovoPedido()) {
			if (ValueUtil.isNotEmpty(pedido.cdCliente) && (ValueUtil.isNotEmpty(pedido.cdCondicaoPagamento) && LavenderePdaConfig.usaTipoPagtoPorCondPagtoECondPagtoPorCliente() || !LavenderePdaConfig.usaTipoPagtoPorCondPagtoECondPagtoPorCliente())) {
				tipoPagamento.tipoCondPagtoCliFilter = new TipoCondPagtoCli();
				tipoPagamento.tipoCondPagtoCliFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
				tipoPagamento.tipoCondPagtoCliFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TipoCondPagtoCli.class);
				tipoPagamento.tipoCondPagtoCliFilter.cdCondicaoPagamento = LavenderePdaConfig.usaTipoPagtoPorCondPagtoECondPagtoPorCliente() ? pedido.cdCondicaoPagamento : null;
				tipoPagamento.tipoCondPagtoCliFilter.cdCliente = pedido.cdCliente;
			}
		}
	}

	protected void prepareCondTipoPagtoFilter(Pedido pedido, TipoPagamento tipoPagamento) {
		if (LavenderePdaConfig.usaTipoPagamentoPorCondicaoPagamento() && ValueUtil.isNotEmpty(pedido.cdCondicaoPagamento)) {
			tipoPagamento.condTipoPagtoFilter = new CondTipoPagto();
			tipoPagamento.condTipoPagtoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			tipoPagamento.condTipoPagtoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondTipoPagto.class);
			tipoPagamento.condTipoPagtoFilter.cdCondicaoPagamento = pedido.cdCondicaoPagamento;
		}
	}

	protected void prepareTipoPagtoTabPrecoFilter(Pedido pedido, TipoPagamento tipoPagamento) {
		if (LavenderePdaConfig.isUsaTipoPagamentoPorTabelaPreco() && ValueUtil.isNotEmpty(pedido.cdTabelaPreco)) {
			tipoPagamento.tipoPagtoTabPrecoFilter = new TipoPagtoTabPreco();
			tipoPagamento.tipoPagtoTabPrecoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			tipoPagamento.tipoPagtoTabPrecoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TipoPagtoTabPreco.class);
			tipoPagamento.tipoPagtoTabPrecoFilter.cdTabelaPreco = pedido.cdTabelaPreco;
		}
	}

}
