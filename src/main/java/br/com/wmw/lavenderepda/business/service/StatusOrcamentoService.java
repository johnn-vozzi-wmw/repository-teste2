package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.StatusOrcamento;
import br.com.wmw.lavenderepda.integration.dao.pdbx.StatusOrcamentoDbxDao;
import totalcross.util.Vector;

public class StatusOrcamentoService extends CrudService {
	
	private static StatusOrcamentoService instance;
	
	public static StatusOrcamentoService getInstance() {
		if (instance == null) {
			instance = new StatusOrcamentoService();
		}
		return instance;
	}

	@Override
	protected CrudDao getCrudDao() {
		return StatusOrcamentoDbxDao.getInstance();
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {/**/}
	
	
	public StatusOrcamento findByCdDomain(String cdDomain) throws SQLException {
		return (StatusOrcamento) findByPrimaryKey(getStatusOrcamentoFilter(1, cdDomain));
	}
	
	public StatusOrcamento findStatusOrcamentoInicial() throws SQLException {
		Vector list = findAllByExample(getStatusOrcamentoFilter(2, ValueUtil.VALOR_SIM));
		return ValueUtil.isEmpty(list) ? null : (StatusOrcamento) list.elementAt(0);
	}
	
	public StatusOrcamento findStatusOrcamentoCancelado() throws SQLException {
		Vector list = findAllByExample(getStatusOrcamentoFilter(3, ValueUtil.VALOR_SIM));
		return ValueUtil.isNotEmpty(list) ? (StatusOrcamento) list.elementAt(0) : null;
	}
	
	public StatusOrcamento findStatusPreOrcamento() throws SQLException {
		Vector list = findAllByExample(getStatusOrcamentoFilter(4, ValueUtil.VALOR_SIM));
		return ValueUtil.isEmpty(list) ? null : (StatusOrcamento) list.elementAt(0);
	}
	
	public void fillStatusOrcamentoPedido(Pedido pedido) throws SQLException {
		StatusOrcamento status;
		if (ValueUtil.isNotEmpty(pedido.cdStatusOrcamento)) {
			status = findByCdDomain(pedido.cdStatusOrcamento);
		} else if (LavenderePdaConfig.usaNovoPedidoOrcamentoSemRegistroChegada && pedido.getCliente().somentePedidoPreOrcamento) {
			status = findStatusPreOrcamento();
		} else {
			status = findStatusOrcamentoInicial();
		}
		if (status == null) {
			status = new StatusOrcamento();
		}
		pedido.statusOrcamento = status;
		pedido.cdStatusOrcamento = status.cdStatusOrcamento;
	}
	
	private StatusOrcamento getStatusOrcamentoFilter(int filterType, String value) {
		StatusOrcamento filter = new StatusOrcamento();
		filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		filter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(StatusOrcamento.class);
		filter.limit = 1;
		switch (filterType) {
		case 1:
			filter.cdStatusOrcamento = value;
			break;
		case 2:
			filter.flStatusInicial = value;
			filter.notFlStatusPreOrcamento = ValueUtil.VALOR_SIM;
			break;
		case 3:
			filter.flStatusCancelamento = value;
			break;
		case 4:
			filter.flStatusPreOrcamento = value;
			break;
		default:
			break;
		}
		return filter;
	}
	
	public boolean permiteFechamentoPedido(Pedido pedido) throws SQLException {
		StatusOrcamento statusOrcamentoFilter = new StatusOrcamento();
		statusOrcamentoFilter.cdEmpresa = pedido.cdEmpresa;
		statusOrcamentoFilter.cdRepresentante = ValueUtil.isEmpty(pedido.cdSupervisor) ? pedido.cdRepresentante: pedido.cdSupervisor;
		statusOrcamentoFilter.cdStatusOrcamento = pedido.cdStatusOrcamento;
		return ValueUtil.VALOR_SIM.equals((getInstance().findColumnByRowKey(statusOrcamentoFilter.getRowKey(), StatusOrcamento.NMCOLUNA_FLPERMITEFECHARPEDIDO)));
	}

	public StatusOrcamento getStatusOrcamentoInicialPedidoBySugestao(String cdStatusOrcamento) throws SQLException {
		StatusOrcamento statusOrcamento = findByCdDomain(cdStatusOrcamento);
		if (statusOrcamento.isStatusCancelamento() && SessionLavenderePda.getCliente().somentePedidoPreOrcamento) {
			return StatusOrcamentoService.getInstance().findStatusPreOrcamento();
		} else if (statusOrcamento.isStatusCancelamento()) {
			return StatusOrcamentoService.getInstance().findStatusOrcamentoInicial();
		}
		return statusOrcamento;
	}

}
