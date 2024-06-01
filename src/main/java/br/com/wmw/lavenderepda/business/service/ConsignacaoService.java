package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.Consignacao;
import br.com.wmw.lavenderepda.business.domain.ItemConsignacao;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TipoPagamento;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ConsignacaoPdbxDao;
import totalcross.util.Vector;

public class ConsignacaoService extends CrudService {

    private static ConsignacaoService instance;

    private ConsignacaoService() {
        //--
    }

    public static ConsignacaoService getInstance() {
        if (instance == null) {
            instance = new ConsignacaoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ConsignacaoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public Consignacao findConsignacaoInOpenByCdCliente(String cdCliente) throws SQLException {
    	Consignacao consignacao = new Consignacao();
    	consignacao.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	consignacao.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	consignacao.cdCliente = cdCliente;
    	consignacao = (Consignacao) ConsignacaoPdbxDao.getInstance().findConsignacaoInOpen(consignacao);
    	//--
    	return consignacao;
    }

    public Consignacao findConsignacaoFechadaByCdCliente(String cdCliente) throws SQLException {
    	Consignacao consignacao = new Consignacao();
    	consignacao.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	consignacao.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	consignacao.cdCliente = cdCliente;
    	consignacao = (Consignacao) ConsignacaoPdbxDao.getInstance().findConsignacaoFechada(consignacao);
    	//--
    	return consignacao;
    }

    public void insert(BaseDomain domain) throws SQLException {
    	Consignacao consignacao = (Consignacao) domain;
    	consignacao.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	consignacao.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	consignacao.cdConsignacao = generateIdGlobal();
    	consignacao.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
		//--
    	super.insert(domain);
    }

    public Pedido createNewPedidoByConsignacao(Cliente cliente, Consignacao consignacao) throws SQLException {
    	Pedido pedido = new Pedido();
		convertConsignacaoToPedido(cliente, pedido , consignacao);
		PedidoService.getInstance().insertPedidoAndItensPedido(pedido);
		consignacao.nuPedido = pedido.nuPedido;
		update(consignacao);
		//--
		return pedido;
    }

	public void convertConsignacaoToPedido(Cliente cliente, Pedido pedido , Consignacao consignacao) throws SQLException {
		pedido.setCliente(cliente);
		pedido.dtEmissao = DateUtil.getCurrentDate();
		pedido.hrEmissao = TimeUtil.getCurrentTimeHHMM();
		pedido.dtEntrega = DateUtil.getCurrentDate();
		pedido.hrFimEmissao = TimeUtil.getCurrentTimeHHMM();
//		pedido.dtFechamento = DateUtil.getCurrentDate();
//		pedido.hrFechamento = TimeUtil.getCurrentTimeHHMM();
		pedido.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ORIGINAL;
		pedido.dtTransmissaoPda = null;
		pedido.hrTransmissaoPda = "";
		pedido.flOrigemPedidoRelacionado = ValueUtil.VALOR_NI;
		pedido.cdTabelaPreco = cliente.cdTabelaPreco;
		pedido.cdTipoPedido = findTipoPedido(cliente.cdTipoPedido);
		pedido.cdTipoPagamento = findTipoPagamento(cliente.cdTipoPagamento);
		pedido.cdCondicaoPagamento = findCondPagamento(cliente.cdCondicaoPagamento);
		if (cliente.isNovoCliente() && LavenderePdaConfig.isPermitePedidoNovoCliente()) {
			pedido.flPedidoNovoCliente = ValueUtil.VALOR_SIM;
		}
		//--
		int size = consignacao.itemConsignacaoList.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = ItemConsignacaoService.getInstance().convertItemConsignacaoToItemPedido((ItemConsignacao)consignacao.itemConsignacaoList.items[i] , pedido);
				itemPedido.nuSeqItemPedido = i + 1;
				pedido.itemPedidoList.addElement(itemPedido);
			}
		}
	}

	private String findTipoPedido(String cdTipoPedido) throws SQLException {
		TipoPedido tipoPedido = TipoPedidoService.getInstance().getTipoPedido(cdTipoPedido);
		if (tipoPedido != null) {
			return tipoPedido.cdTipoPedido;
		} else {
			tipoPedido = TipoPedidoService.getInstance().getTipoPedidoDefault();
			if (tipoPedido != null) {
				return tipoPedido.cdTipoPedido;
			} else {
				tipoPedido = TipoPedidoService.getInstance().getFirstTipoPedido();
				if (tipoPedido != null) {
					return tipoPedido.cdTipoPedido;
				}
			}
		}
		return null;
	}

	private String findTipoPagamento(String cdTipoPagamento) throws SQLException {
		TipoPagamento tipoPagamento = TipoPagamentoService.getInstance().getTipoPagamento(cdTipoPagamento);
		if (tipoPagamento != null) {
			return tipoPagamento.cdTipoPagamento;
		}
		return null;
	}

	private String findCondPagamento(String cdCondicaoPagamento) throws SQLException {
		CondicaoPagamento condicaoPagamento = CondicaoPagamentoService.getInstance().getCondicaoPagamento(cdCondicaoPagamento);
		if (condicaoPagamento != null) {
			return condicaoPagamento.cdCondicaoPagamento;
		}
		return null;
	}

	public void retiraNuPedido(Consignacao consignacao) throws SQLException {
		ConsignacaoPdbxDao.getInstance().retiraNuPedido(consignacao);
	}

	public Vector findAllConsigSentAndPaid() throws SQLException {
		Consignacao consignacao = new Consignacao();
		consignacao.cdEmpresa = SessionLavenderePda.cdEmpresa;
		consignacao.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		//--
		return ConsignacaoPdbxDao.getInstance().findAllConsigSentAndPaid(consignacao);
	}
}