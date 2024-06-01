package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

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
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Consignacao;
import br.com.wmw.lavenderepda.business.domain.FichaFinanceira;
import br.com.wmw.lavenderepda.business.domain.ItemConsignacao;
import br.com.wmw.lavenderepda.business.domain.Pagamento;
import br.com.wmw.lavenderepda.business.domain.PagamentoPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PagamentoPdbxDao;
import totalcross.util.Date;
import totalcross.util.Vector;

public class PagamentoService extends CrudService {

    private static PagamentoService instance;

    private PagamentoService() {
        //--Construtor privado
    }

    public static PagamentoService getInstance() {
        if (instance == null) {
            instance = new PagamentoService();
        }
        return instance;
    }

    protected CrudDao getCrudDao() {
        return PagamentoPdbxDao.getInstance();
    }

    public void insert(BaseDomain domain) throws SQLException {
    	Pagamento pagamento = (Pagamento) domain;
    	pagamento.cdPagamento = generateIdGlobal();
    	super.insert(domain);
    }

    public void validate(BaseDomain domain) throws java.sql.SQLException {
        Pagamento pagamento = (Pagamento) domain;
        if (ValueUtil.isEmpty(pagamento.cdTipoPagamento)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPOPAGTO_LABEL_TIPOPAGTO);
        }
        if (pagamento.vlPago == 0) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.LABEL_VALOR);
        }
        validaPagamento(pagamento);
    }

    public void validaPagamento(Pagamento pagamento) throws SQLException {
		FichaFinanceira fichaFinanceira = FichaFinanceiraService.getInstance().getFichaFinanceira(SessionLavenderePda.getCliente());
        fichaFinanceira.getTitulos();
        double vlTotalPedidoConsignacao = 0;
        Consignacao consignacao = ConsignacaoService.getInstance().findConsignacaoFechadaByCdCliente(SessionLavenderePda.getCliente().cdCliente);
    	if (consignacao != null) {
    		Vector itemConsignacaoList = ItemConsignacaoService.getInstance().findItemConsignacaoList(consignacao);
    		int size = itemConsignacaoList.size();
    		for (int i = 0; i < size; i++) {
    			ItemConsignacao itemConsignacao = (ItemConsignacao)itemConsignacaoList.items[i];
    			vlTotalPedidoConsignacao += itemConsignacao.getQtItemVenda() * itemConsignacao.vlItem;
			}
    	}
	    double vlTotalPedidosAberto = 0;
	    if (!LavenderePdaConfig.usaMultiplosPagamentosParaPedido) {
		    vlTotalPedidosAberto = FichaFinanceiraService.getInstance().getValuesPedidos(SessionLavenderePda.getCliente());
	    }
    	double vlPago = PagamentoPdbxDao.getInstance().sumByExample(newPagamentoFilter(), "VLPAGO");
	    double vlPagamentoAtual = pagamento.editing ? pagamento.vlPago : 0;
        double vlTotal = ValueUtil.round(fichaFinanceira.vlTotalAberto + (vlTotalPedidosAberto - vlTotalPedidoConsignacao) - vlPago) + vlPagamentoAtual;
        if (vlTotal == 0) {
        	throw new ValidationException(Messages.PAGAMENTO_SEM_PENDENCIA_ABERTA);
        }
        if (ValueUtil.round(pagamento.vlPago) > vlTotal) {
        	throw new ValidationException(MessageUtil.getMessage(Messages.PAGAMENTO_ULTRAPASSOU_VALOR_PERMITIDO, new String[] {StringUtil.getStringValueToInterface(vlTotal)}));
        }
    }
    
    private Pagamento newPagamentoFilter() {
    	Pagamento pagamentoFilter = new Pagamento();
    	pagamentoFilter.cdCliente = SessionLavenderePda.getCliente().cdCliente;
    	pagamentoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	pagamentoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	return pagamentoFilter;
    }
    

    public Vector findAllNotSentByExample() throws SQLException {
    	Pagamento pagamentoFilter = newPagamentoFilter();
    	pagamentoFilter.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ORIGINAL;
    	return PagamentoPdbxDao.getInstance().findAllNotSentByExample(pagamentoFilter);
    }

    public Vector findAllGroupByTipoPagamento(BaseDomain domain) throws SQLException {
    	return PagamentoPdbxDao.getInstance().findAllGroupByTipoPagamento(domain);
    }

    public double getVlTotalPagamento(Cliente cliente) throws SQLException {
    	Pagamento pagamentoFilter = new Pagamento();
		pagamentoFilter.cdEmpresa = cliente.cdEmpresa;
		pagamentoFilter.cdRepresentante = cliente.cdRepresentante;
		pagamentoFilter.cdCliente = cliente.cdCliente;
		pagamentoFilter.flFilterTipoAlteracaoOriginal = true;
    	return sumByExample(pagamentoFilter, "VLPAGO");
    }
   
    private double getVlTotalPagamento(Date dateFilter) throws SQLException {
    	Pagamento pagamentoFilter = new Pagamento();
    	pagamentoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	pagamentoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(PagamentoPedido.class);
    	pagamentoFilter.dtPagamentoInicialFilter = dateFilter;
    	pagamentoFilter.dtPagamentoFinalFilter = dateFilter;
    	return sumByExample(pagamentoFilter, "VLPAGO");
    }
    
    public int countPagamento() throws SQLException {
    	Pagamento pagamentoFilter = new Pagamento();
    	pagamentoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	pagamentoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(PagamentoPedido.class);
    	pagamentoFilter.dtPagamento = DateUtil.getCurrentDate();
    	pagamentoFilter.flFiltraFechamentoDiario = true;
    	return countByExample(pagamentoFilter);
    	
    }
    
    public boolean isExistePagamento(Date dateFilter) throws SQLException {
    	return getVlTotalPagamento(dateFilter) > 0;
    }
    
    public double getSomaTotalDinheiroPorData(Date dtPagamentoInicial, Date dtPagamentoFinal) throws java.sql.SQLException {
    	String cdEmpresa = SessionLavenderePda.cdEmpresa;
		String cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(PagamentoPedido.class);
    	return somaTotalDinheiroPorData(cdEmpresa, cdRepresentante, dtPagamentoInicial, dtPagamentoFinal);
    }

    public double somaTotalDinheiroPorData(String cdEmpresa, String cdRepresentante, Date dtPagamentoInicial, Date dtPagamentoFinal) throws java.sql.SQLException {
    	return PagamentoPdbxDao.getInstance().somaTotalDinheiroPorData(cdEmpresa, cdRepresentante, dtPagamentoInicial, dtPagamentoFinal);
    }

    public double somaTotalChequePorData(String cdEmpresa, String cdRepresentante,  Date dtPagamentoInicial,  Date dtPagamentoFinal) throws java.sql.SQLException {
    	return PagamentoPdbxDao.getInstance().somaTotalChequePorData(cdEmpresa, cdRepresentante, dtPagamentoInicial, dtPagamentoFinal);
    }
    
    public double somaTotalBoletoPorData(String cdEmpresa, String cdRepresentante, Date dtPagamentoInicial, Date dtPagamentoFinal) throws java.sql.SQLException {
    	return PagamentoPdbxDao.getInstance().somaTotalBoletoPorData(cdEmpresa, cdRepresentante, dtPagamentoInicial, dtPagamentoFinal);
    }
    
    public double somaTotalOutrosPorData(String cdEmpresa, String cdRepresentante, Date dtPagamentoInicial, Date dtPagamentoFinal) throws java.sql.SQLException {
    	return PagamentoPdbxDao.getInstance().somaTotalOutrosPorData(cdEmpresa, cdRepresentante, dtPagamentoInicial, dtPagamentoFinal);
    }

    public double somaTotalPagamentosPorData(String cdEmpresa, String cdRepresentante, Date dtPagamentoInicial, Date dtPagamentoFinal) throws java.sql.SQLException {
    	return PagamentoPdbxDao.getInstance().somaTotalPagamentosPorData(cdEmpresa, cdRepresentante, dtPagamentoInicial, dtPagamentoFinal);
    }
    
}