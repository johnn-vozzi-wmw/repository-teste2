package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.FechamentoDiario;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FechamentoDiarioDao;
import totalcross.sys.Time;
import totalcross.util.Date;
import totalcross.util.Vector;

import java.sql.SQLException;

public class FechamentoDiarioService extends CrudService {

	private static FechamentoDiarioService instance;

	private FechamentoDiarioService() {
	}

	public static FechamentoDiarioService getInstance() {
		if (instance == null) {
			instance = new FechamentoDiarioService();
		}
		return instance;
	}

	@Override
	protected CrudDao getCrudDao() {
		return FechamentoDiarioDao.getInstance();
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
		FechamentoDiario fechamentoDiario = (FechamentoDiario) domain;
		validaDataFechamentoDiario(fechamentoDiario.dtFechamentoDiario);
		if (!fechamentoDiario.isLiberadoPorSenha() && !fechamentoDiario.atualizandoNuImpressao) {
			int countAbertos = PedidoService.getInstance().countPedidosEmAberto();
			int countNaoTransmitidos = LavenderePdaConfig.isBloqueiaFechamentoDiarioPedidosNaoTransmitidos() ? PedidoService.getInstance().countPedidosNaoTransmitidosFechamentoDiario() : 0;
			if (countAbertos > 0 && countNaoTransmitidos > 0) {
				throw new ValidationException(Messages.FECHAMENTO_DIARIO_MSG_PEDIDOS_ABERTOS_E_NAO_TRANSMITIDOS);
			} else if (countAbertos > 0) {
				throw new ValidationException(Messages.FECHAMENTO_DIARIO_MSG_PEDIDO_ABERTO);
			} else if (countNaoTransmitidos > 0) {
				throw new ValidationException(Messages.FECHAMENTO_DIARIO_MSG_PEDIDO_NAO_TRANSMITIDO);
			}
		}
	}

	public void validaDataFechamentoDiario(Date dtFechamentoDiario) {
		if (ValueUtil.isEmpty(dtFechamentoDiario)) {
			throw new ValidationException(Messages.FECHAMENTO_DIARIO_MSG_SEM_DATA_FECHAMENTO);
		}
	}

	public boolean isHouveFechamentoDiario(Date dateFilter) throws SQLException {
		FechamentoDiario fechamentoDiarioFilter = new FechamentoDiario(dateFilter);
		return countByExample(fechamentoDiarioFilter) > 0;
	}


	public void insereFechamentoDiario(double vlTotalDepositoBancario, Date dataUltimoFechamentoDiarioPorSenha, Date dtFechamentoDiario, boolean liberadoSenha, FechamentoDiario veiculoQuilometragem) throws SQLException {
		FechamentoDiario fechamentoDiario = new FechamentoDiario(dtFechamentoDiario);
		fechamentoDiario.hrFechamentoDiario = TimeUtil.getCurrentTimeHHMM();
		fechamentoDiario.dtFinalizacao = new Date();
		fechamentoDiario.hrFinalizacao = TimeUtil.getCurrentTimeHHMM();
		fechamentoDiario.vlTotalDepositoBancario = vlTotalDepositoBancario;
		fechamentoDiario.flLiberadoSenha = StringUtil.getStringValue(liberadoSenha);
		fechamentoDiario.flAgrupado = StringUtil.getStringValue(!fechamentoDiario.isLiberadoPorSenha() && dataUltimoFechamentoDiarioPorSenha.isBefore(dtFechamentoDiario));
		if (LavenderePdaConfig.usaPermiteInformarVeiculoFechamento() && veiculoQuilometragem != null) {
			fechamentoDiario.dsPlacaVeiculo = veiculoQuilometragem.dsPlacaVeiculo;
			fechamentoDiario.kmInicialVeiculo = veiculoQuilometragem.kmInicialVeiculo;
			fechamentoDiario.kmFinalVeiculo = veiculoQuilometragem.kmFinalVeiculo;
		}
		if (liberadoSenha) {
			setaValoresFechamentoDiarioLiberadoPorSenha(fechamentoDiario);
			String dataHoraAtual = StringUtil.getStringValue(new Date()) + " " + new Time();
			String log = MessageUtil.getMessage(Messages.FECHAMENTO_DIARIO_LOG, new String[]{StringUtil.getStringValue(dtFechamentoDiario), fechamentoDiario.cdRepresentante, dataHoraAtual});
			LogPdaService.getInstance().log(LogPda.LOG_NIVEL_INFO, LogPda.LOG_LIBERACAO_SENHA_FECHAMENTO_DIARIO, log);
		} else {
			setaValoresFechamentoDiarioPadrao(fechamentoDiario, dataUltimoFechamentoDiarioPorSenha);
		}

		insert(fechamentoDiario);
	}

	private void setaValoresFechamentoDiarioPadrao(FechamentoDiario fechamentoDiario, Date dataUltimoFechamentoDiarioPorSenha) throws SQLException {
		if (LavenderePdaConfig.consideraValorPedidoAtualRetornado()) {
			setValoresFechamentoDiarioPedido(fechamentoDiario);
		} else {
			fechamentoDiario.vlTotalPedidos = PagamentoPedidoService.getInstance().somaTotalPedidosPorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiarioPorSenha, fechamentoDiario.dtFechamentoDiario);
			fechamentoDiario.vlTotalReceitas = PagamentoPedidoService.getInstance().somaTotalReceitasPorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiarioPorSenha, fechamentoDiario.dtFechamentoDiario);
			fechamentoDiario.vlTotalVendas = PagamentoPedidoService.getInstance().somaTotalVendasPorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiarioPorSenha, fechamentoDiario.dtFechamentoDiario);
			fechamentoDiario.vlTotalVendasDinheiro = PagamentoPedidoService.getInstance().somaDinheiroPedidoPorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiarioPorSenha, fechamentoDiario.dtFechamentoDiario);
			fechamentoDiario.vlTotalVendasCheque = PagamentoPedidoService.getInstance().somaChequePedidoPorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiarioPorSenha, fechamentoDiario.dtFechamentoDiario);
			fechamentoDiario.vlTotalVendasBoleto = PagamentoPedidoService.getInstance().somaBoletoPedidoPorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiarioPorSenha, fechamentoDiario.dtFechamentoDiario);
			fechamentoDiario.vlTotalVendasOutros = PagamentoPedidoService.getInstance().somaOutrosPedidoPorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiarioPorSenha, fechamentoDiario.dtFechamentoDiario);
			fechamentoDiario.vlTotalBonificacao = PagamentoPedidoService.getInstance().somaTotalBonificacaoPorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiarioPorSenha, fechamentoDiario.dtFechamentoDiario);
		}
		fechamentoDiario.vlTotalPagamentos = PagamentoService.getInstance().somaTotalPagamentosPorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiarioPorSenha, fechamentoDiario.dtFechamentoDiario);
		fechamentoDiario.vlTotalPagamentosDinheiro = PagamentoService.getInstance().somaTotalDinheiroPorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiarioPorSenha, fechamentoDiario.dtFechamentoDiario);
		fechamentoDiario.vlTotalPagamentosCheque = PagamentoService.getInstance().somaTotalChequePorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiarioPorSenha, fechamentoDiario.dtFechamentoDiario);
		fechamentoDiario.vlTotalPagamentosBoleto = PagamentoService.getInstance().somaTotalBoletoPorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiarioPorSenha, fechamentoDiario.dtFechamentoDiario);
		fechamentoDiario.vlTotalCreditoCliente = PedidoService.getInstance().somaVlTotalNotaCredito(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiarioPorSenha, fechamentoDiario.dtFechamentoDiario);
		fechamentoDiario.vlTotalDesconto = PedidoService.getInstance().somaVlTotalDesconto(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiarioPorSenha, fechamentoDiario.dtFechamentoDiario);
		fechamentoDiario.vlTotalPagamentosOutros = PagamentoService.getInstance().somaTotalOutrosPorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiarioPorSenha, fechamentoDiario.dtFechamentoDiario);
	}
	
	private void setValoresFechamentoDiarioPedido(FechamentoDiario fechamentoDiario) throws SQLException {
		Pedido pedidoSum = PedidoService.getInstance().getPedidoSumFechamentoDiario(fechamentoDiario.dtFechamentoDiario);
		fechamentoDiario.vlTotalPedidos = pedidoSum.vlTotalPedido + pedidoSum.vlBonificacaoPedido;
		fechamentoDiario.vlTotalReceitas = pedidoSum.vlTotalPedido;
		fechamentoDiario.vlTotalVendas = fechamentoDiario.vlTotalReceitas;
		fechamentoDiario.vlTotalVendasDinheiro = pedidoSum.vlTotalDinheiro;
		fechamentoDiario.vlTotalVendasCheque = pedidoSum.vlTotalCheque;
		fechamentoDiario.vlTotalVendasBoleto = pedidoSum.vlTotalBoleto;
		fechamentoDiario.vlTotalVendasOutros = pedidoSum.vlTotalPedido - pedidoSum.vlTotalDinheiro - pedidoSum.vlTotalCheque - pedidoSum.vlTotalBoleto;
		fechamentoDiario.vlTotalBonificacao = pedidoSum.vlBonificacaoPedido;
	}

	private void setaValoresFechamentoDiarioLiberadoPorSenha(FechamentoDiario fechamentoDiario) {
		fechamentoDiario.vlTotalPedidos = 0;
		fechamentoDiario.vlTotalPagamentos = 0;
		fechamentoDiario.vlTotalReceitas = 0;
		fechamentoDiario.vlTotalVendas = 0;
		fechamentoDiario.vlTotalVendasDinheiro = 0;
		fechamentoDiario.vlTotalVendasCheque = 0;
		fechamentoDiario.vlTotalVendasBoleto = 0;
		fechamentoDiario.vlTotalPagamentosDinheiro = 0;
		fechamentoDiario.vlTotalPagamentosCheque = 0;
		fechamentoDiario.vlTotalPagamentosBoleto = 0;
		fechamentoDiario.vlTotalBonificacao = 0;
		fechamentoDiario.vlTotalCreditoCliente = 0;
		fechamentoDiario.vlTotalDesconto = 0;
		fechamentoDiario.vlTotalVendasOutros = 0;
		fechamentoDiario.vlTotalPagamentosOutros = 0;
	}

	public void atualizaNuImpressao(FechamentoDiario fechamentoDiario) throws SQLException {
		fechamentoDiario.nuImpressao = getNextNuImpressao(fechamentoDiario);
		fechamentoDiario.atualizandoNuImpressao = true;
		update(fechamentoDiario);
		fechamentoDiario.atualizandoNuImpressao = false;
	}

	private int getNextNuImpressao(FechamentoDiario fechamentoDiario) throws SQLException {
		String valor = maxByExample(fechamentoDiario, "NUIMPRESSAO");
		if (ValueUtil.isNotEmpty(valor)) {
			return Integer.parseInt(valor) + 1;
		}
		return 1;
	}

	public FechamentoDiario findFechamentoDiarioPorData(Date dateFilter) throws SQLException {
		FechamentoDiario fechamentoDiarioFilter = new FechamentoDiario(dateFilter);
		return (FechamentoDiario) findByRowKey(fechamentoDiarioFilter.getRowKey());
	}

	public boolean isFechamentoDiarioExecutado(Date dateFilter) throws SQLException {
		FechamentoDiario fechamentoDiarioFilter = new FechamentoDiario(dateFilter);
		String dtFechamentoDiario = findColumnByRowKey(fechamentoDiarioFilter.getRowKey(), "DTFECHAMENTODIARIO");
		return ValueUtil.isNotEmpty(dtFechamentoDiario);
	}

	protected Date getDataFechamentoDiarioNaoExcluido(Date dateFilter) throws SQLException {
		Date dataFechamentoNaoExcluido = findDataPosteriorADataExclusaoFechamentoDiario(dateFilter);
		if (ValueUtil.isNotEmpty(dataFechamentoNaoExcluido)) {
			Vector fechamentoDiarioList = findfechamentoDiarioOrdenadosPorDataFechamentoDecrescente(dataFechamentoNaoExcluido);
			if (ValueUtil.isNotEmpty(fechamentoDiarioList)) {
				excluiFechamentoDiarioMantendoUltimoFechamentoDiario(fechamentoDiarioList, dataFechamentoNaoExcluido);
			}
		}
		return dataFechamentoNaoExcluido;
	}


	private void excluiFechamentoDiarioMantendoUltimoFechamentoDiario(Vector fechamentoDiarioList, Date dataFechamentoNaoExcluido) throws SQLException {
		int size = fechamentoDiarioList.size();
		for (int i = 0; i < size; i++) {
			FechamentoDiario fechamentoDiario = (FechamentoDiario) fechamentoDiarioList.items[i];
			if (!ValueUtil.valueEquals(dataFechamentoNaoExcluido, fechamentoDiario.dtFechamentoDiario)) {
				delete(fechamentoDiario);
			}
		}
	}

	private Date findDataPosteriorADataExclusaoFechamentoDiario(Date dateFilter) throws SQLException {
		FechamentoDiario fechamentoDiarioFilter = new FechamentoDiario(null);
		fechamentoDiarioFilter.dtFechamentoDiarioFilter = dateFilter;
		return FechamentoDiarioDao.getInstance().findDataPosteriorADataExclusaoFechamentoDiario(fechamentoDiarioFilter);
	}

	private Vector findfechamentoDiarioOrdenadosPorDataFechamentoDecrescente(Date dateFilter) throws SQLException {
		FechamentoDiario fechamentoDiarioFilter = new FechamentoDiario(null);
		fechamentoDiarioFilter.dtFechamentoDiarioFilter = dateFilter;
		return findAllByExample(fechamentoDiarioFilter);
	}

	public Date getDataUltimoFechamentoDiario(String cdEmpresa, String cdRepresentante, String flLiberadoSenha) throws SQLException {
		if (LavenderePdaConfig.isLiberaFechamentoDiarioComSenha() || (LavenderePdaConfig.consideraValorPedidoAtualRetornado() && LavenderePdaConfig.nuDiasConsideraValorPedidoAtualRetornadoPagamentos() > 0)) {
			FechamentoDiario fechamentoDiarioFilter = new FechamentoDiario(null);
			fechamentoDiarioFilter.flLiberadoSenha = flLiberadoSenha;
			Date dataUltimoFechamentoDiarioPorSenha = null;
			if (ValueUtil.getBooleanValue(flLiberadoSenha)) {
				dataUltimoFechamentoDiarioPorSenha = FechamentoDiarioDao.getInstance().findDataMinimaFechamentoDiario(fechamentoDiarioFilter);
			} else {
				dataUltimoFechamentoDiarioPorSenha = FechamentoDiarioDao.getInstance().findDataPosteriorADataExclusaoFechamentoDiario(fechamentoDiarioFilter);
				DateUtil.addDay(dataUltimoFechamentoDiarioPorSenha, 1);
			}
			return dataUltimoFechamentoDiarioPorSenha;

		}
		return null;
	}

	public FechamentoDiario findUltimoFechamentoDiario() throws SQLException {
		FechamentoDiario fechamentoDiarioFilter = new FechamentoDiario();
		fechamentoDiarioFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		fechamentoDiarioFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		return FechamentoDiarioDao.getInstance().findUltimoFechamentoDiario(fechamentoDiarioFilter);
	}

	public boolean validaFechamentoDiarioObrigatorioVeiculo(FechamentoDiario fechamentoDiarioVeiculo) {
		return fechamentoDiarioVeiculo != null && ValueUtil.isNotEmpty(fechamentoDiarioVeiculo.dsPlacaVeiculo) && fechamentoDiarioVeiculo.kmInicialVeiculo != 0 && fechamentoDiarioVeiculo.kmFinalVeiculo != 0;
	}

	public void validaQuilometragemVeiculo(double kmInicialVeiculo, double kmFinalVeiculo) {
		if (kmInicialVeiculo != 0 && kmFinalVeiculo != 0 && kmInicialVeiculo > kmFinalVeiculo) {
			throw new ValidationException(Messages.VEICULO_QUILOMETRAGEM_KM_FINAL_MAIOR_QUE_INICIAL);
		}
	}
	
	public String [][] fechamentoDiarioClienteToGridList(Vector fechamentoDiarioClienteList) throws SQLException {
		int size = fechamentoDiarioClienteList.size();
		String [][] items = new String[size + 1][5];
		double totalDinheiro = 0;
		double totalCheque = 0;
		double totalBoleto = 0;
		double totalSaldo = 0;
		for (int i = 0; i < size; i++) {
			Pedido pedido = (Pedido) fechamentoDiarioClienteList.items[i];
			Cliente cliente = pedido.getCliente();
			items[i][0] = cliente.cdCliente + " - " + cliente.nmRazaoSocial;
			items[i][1] = StringUtil.getStringValueToInterface(pedido.vlTotalDinheiro);
			items[i][2] = StringUtil.getStringValueToInterface(pedido.vlTotalCheque);
			items[i][3] = StringUtil.getStringValueToInterface(pedido.vlTotalBoleto);
			items[i][4] = StringUtil.getStringValueToInterface(pedido.vlTotalDinheiro + pedido.vlTotalCheque + pedido.vlTotalBoleto);
			totalDinheiro += pedido.vlTotalDinheiro;
			totalCheque += pedido.vlTotalCheque;
			totalBoleto += pedido.vlTotalBoleto;
			totalSaldo += pedido.vlTotalDinheiro + pedido.vlTotalCheque + pedido.vlTotalBoleto;
		}
		items[size][0] = Messages.FECHAMENTO_DIARIO_EST_TOTAL;
		items[size][1] = StringUtil.getStringValueToInterface(totalDinheiro);
		items[size][2] = StringUtil.getStringValueToInterface(totalCheque);
		items[size][3] = StringUtil.getStringValueToInterface(totalBoleto);
		items[size][4] = StringUtil.getStringValueToInterface(totalSaldo);
		return items;
	}

	public Date getDataConsideraValorPedidoAtualRetornadoPagamentos(Date dataBase) {
		int nuDiasConsideraValorPedidoAtualRetornadoPagamentos = LavenderePdaConfig.nuDiasConsideraValorPedidoAtualRetornadoPagamentos();
		if (nuDiasConsideraValorPedidoAtualRetornadoPagamentos <= 0) return null;
		dataBase = dataBase == null ? DateUtil.getCurrentDate() : dataBase;
		DateUtil.decDay(dataBase, nuDiasConsideraValorPedidoAtualRetornadoPagamentos);
		return dataBase;
	}

}