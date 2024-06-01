package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;

import javax.lang.model.element.Element;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.IteratorVector;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pagamento;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ResumoDia;
import br.com.wmw.lavenderepda.business.domain.SaldoVendaRep;
import br.com.wmw.lavenderepda.business.domain.ValorizacaoProd;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ResumoDiaDbxDao;
import totalcross.util.Date;
import totalcross.util.InvalidDateException;
import totalcross.util.Vector;

public class ResumoDiaService extends CrudService {

    private static ResumoDiaService instance;

    private ResumoDiaService() {
        //--
    }

    public static ResumoDiaService getInstance() {
        if (instance == null) {
            instance = new ResumoDiaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ResumoDiaDbxDao.getInstance();
    }

	public void validate(BaseDomain domain) throws java.sql.SQLException {
		if (LavenderePdaConfig.usaFechamentoDeVendasPorPeriodo) {
			ResumoDia resumoDia = (ResumoDia) domain;
	        if (ValueUtil.isEmpty(resumoDia.dtResumo)) {
	        	throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FECHAMENTO_VENDAS_LABEL_DTRESUMO);
			}
	        if (ValueUtil.isEmpty(resumoDia.dtUltimofechamento)) {
	        	throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FECHAMENTO_VENDAS_LABEL_DTULTRESUMO);
			}
		}
	}

	public ResumoDia findResumoDia(Date data, String cdRepresentante) throws SQLException {
		if (ValueUtil.isNotEmpty(data)) {
			ResumoDia resumoDiaFilter = new ResumoDia();
			resumoDiaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			resumoDiaFilter.cdRepresentante = cdRepresentante;
			resumoDiaFilter.dtResumo = data;
			return (ResumoDia) findByRowKey(resumoDiaFilter.getRowKey());
		}
		return null;
	}

	public ResumoDia calculateAndGetResumoDia(Date date, String cdRepresentate) throws SQLException {
		return calculateAndGetResumoDia(date, cdRepresentate, null, null);
	}
	
	public ResumoDia calculateAndGetResumoDia(Date date, String cdRepresentate, String cdTipoPedido, String[] cdStatusPedido) throws SQLException {
		double vlTotalVendido = 0;
		int qtPedidos = 0;
		double vlVerbaConsumida = 0;
		double vlTotalBonificacao = 0;
		double vlVerbaBonificacao = 0;
		int vlQtItensVendidosBonificados = 0;
		double vlTotalPontuacaoBase = 0;
		double vlTotalPontuacaoRealizado = 0;
		double vlTotalPeso = 0;
		double vlTotalVolume = 0;
		if (ValueUtil.isNotEmpty(date)) {
			
			Pedido pedidoFilter = new Pedido();
			pedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			pedidoFilter.cdRepresentante = cdRepresentate;
			pedidoFilter.dtEmissao = date;
			pedidoFilter.cdTipoPedido = cdTipoPedido;
			pedidoFilter.onResumoDiario = true;
			if (LavenderePdaConfig.usaFiltroStatusExcecaoResumoDia()) {
				pedidoFilter.cdStatusExcecaoList = cdStatusPedido;
			}
			
			Vector pedidoList = PedidoService.getInstance().findAllByExample(pedidoFilter);
			if (ValueUtil.isNotEmpty(pedidoList)) {
				Pedido pedido;
				Vector cdProdutoDistinctList = new Vector();
				ArrayList<String> itensBonificados = new ArrayList<>();
				
				for (int i = 0; i < pedidoList.size(); i++) {
					pedido = (Pedido) pedidoList.items[i];
					pedido.cdProdutoDistinctList = cdProdutoDistinctList;
					pedido.itensBonificados = itensBonificados;
					PedidoService.getInstance().findItemPedidoList(pedido);
					if (LavenderePdaConfig.isUsaVerba()) {
						vlVerbaConsumida += pedido.vlVerbaPedido;
					}
					if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
						vlTotalVolume += pedido.vlVolumePedido;
					}
					qtPedidos++;
					if (pedido.isPedidoBonificacao() || pedido.isPedidoTroca() || pedido.isOportunidade()) {
						if (pedido.isPedidoBonificacao()) {
							vlTotalBonificacao += pedido.vlBonificacaoPedido;
							vlVerbaBonificacao += pedido.vlVerbaPedido;
						}
						continue;
					}
					vlTotalVendido += pedido.vlTotalPedido;
					vlTotalPontuacaoBase += pedido.vlTotalPontuacaoBase;
					vlTotalPontuacaoRealizado += pedido.vlTotalPontuacaoRealizado;
					vlTotalPeso += pedido.qtPeso;
				}
				
				HashSet<String> itemPedidoHashList = new HashSet<>(itensBonificados);
				vlQtItensVendidosBonificados = itemPedidoHashList.size();
				
				ResumoDia resumoDia = new ResumoDia();
				resumoDia.cdEmpresa = SessionLavenderePda.cdEmpresa;
				resumoDia.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
				resumoDia.dtResumo = date;
				resumoDia.vlTotalVendido = vlTotalVendido;
				resumoDia.qtItensVendidos = cdProdutoDistinctList.size();
				resumoDia.qtPedidos = qtPedidos;
				resumoDia.vlVerbaConsumida = vlVerbaConsumida;
				resumoDia.vlTotalBonificacao = vlTotalBonificacao;
				resumoDia.vlVerbaBonificacao = vlVerbaBonificacao;
				resumoDia.qtItensVendidosBonificados = vlQtItensVendidosBonificados;
				resumoDia.vlTotalPontuacaoBase = vlTotalPontuacaoBase;
				resumoDia.vlTotalPontuacaoRealizado = vlTotalPontuacaoRealizado;
				resumoDia.vlTotalPeso = vlTotalPeso;
				resumoDia.vlTotalVolume = vlTotalVolume;
				if (findByRowKey(resumoDia.getRowKey()) == null) {
					insert(resumoDia);
				} else {
					update(resumoDia);
				}
				return resumoDia;
			}
		}
		return null;
	}

	
	public ResumoDia calculateAndGetFechamentoVendas(Date comecoPeriodo, Date fimPeriodo) throws SQLException, InvalidDateException {
		if (comecoPeriodo == null) {
			return null;
		}
		ResumoDia resumoFilter = new ResumoDia();
		resumoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		resumoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		resumoFilter.dtResumo = fimPeriodo;
		ResumoDia resumoDia = (ResumoDia) ResumoDiaService.getInstance().findByRowKey(resumoFilter.getRowKey());
		
		//--A verificação "resumoDia.dtUltimofechamento == null" é feita para o caso de o registro encontrado na tabela ResumoDia seja referente a um 'Resumo Dia' e não a um 'Fechamento de Vendas'
		if (resumoDia != null && resumoDia.dtUltimofechamento != null) {
			return resumoDia;
		}
		
		//--Saldo Anterior
		SaldoVendaRep saldoVendaFilter = new SaldoVendaRep();
		saldoVendaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		saldoVendaFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		saldoVendaFilter.dtUltimoSaldo = new Date(comecoPeriodo.toString());
		DateUtil.decDay(saldoVendaFilter.dtUltimoSaldo, 1);
		SaldoVendaRep saldoVenda = (SaldoVendaRep) SaldoVendaRepService.getInstance().findByRowKey(saldoVendaFilter.getRowKey());
		resumoDia = resumoFilter;
		resumoDia.vlSaldoAnterior = (saldoVenda != null) ? saldoVenda.vlUltimoSaldo : 0;
		resumoDia.dtUltimofechamento = comecoPeriodo;

		//--Total de Pagamentos:
		Pagamento pagamentoFilter = new Pagamento();
		pagamentoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pagamentoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		pagamentoFilter.dtPagamentoInicialFilter = comecoPeriodo;
		pagamentoFilter.dtPagamentoFinalFilter = fimPeriodo;
		resumoDia.vlTotalPagamento = PagamentoService.getInstance().sumByExample(pagamentoFilter, "vlPago");

		//--Total de Vendas e Total Peso:
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedidoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		pedidoFilter.dtEmissaoInicialFilter = comecoPeriodo;
		pedidoFilter.dtEmissaoFinalFilter = fimPeriodo;
		pedidoFilter.cdStatusPedidoFilter = new String[] {LavenderePdaConfig.cdStatusPedidoFechado, LavenderePdaConfig.cdStatusPedidoTransmitido};
		Vector pedidos = PedidoService.getInstance().findAllByExample(pedidoFilter);
		resumoDia.vlTotalVendido = 0;
		final int size = pedidos.size();
		for (int i = 0; i < size; i++) {
			final Pedido pedido = (Pedido) pedidos.items[i];
			resumoDia.vlTotalVendido += pedido.vlTotalPedido;
			resumoDia.vlTotalPontuacaoBase += pedido.vlTotalPontuacaoBase;
			resumoDia.vlTotalPontuacaoRealizado += pedido.vlTotalPontuacaoRealizado;
			resumoDia.vlTotalPeso += pedido.qtPeso;
		}

		//--Total de Valorizações:
		ValorizacaoProd valorizacaoProdFilter = new ValorizacaoProd();
		valorizacaoProdFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		valorizacaoProdFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		valorizacaoProdFilter.dtValorizacaoFiltroInic = comecoPeriodo;
		valorizacaoProdFilter.dtValorizacaoFiltroFim = fimPeriodo;
		resumoDia.vlTotalValorizacao = ValorizacaoProdService.getInstance().sumByExample(valorizacaoProdFilter, "vlTotalItem");
		
		//--Saldo Final:
		resumoDia.vlSaldoFinal = retornaSaldoFechamentoVendas(resumoDia.vlTotalPromissoria, resumoDia.vlSaldoAnterior, resumoDia.vlTotalPagamento, resumoDia.vlTotalVendido, resumoDia.vlTotalValorizacao);
		
		return resumoDia;
	}
	
	public double retornaSaldoFechamentoVendas(double vlTotalPromissoria, double vlSaldoAnterior, double vlTotalPagamento, double vlTotalVendido, double vlTotalValorizacao) {
		return vlTotalPromissoria - (vlSaldoAnterior - vlTotalPagamento + vlTotalVendido + vlTotalValorizacao);
	}

}