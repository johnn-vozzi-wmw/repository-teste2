package br.com.wmw.lavenderepda.sync;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.config.AppConfig;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.exception.ConnectionException;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.notification.Notification;
import br.com.wmw.framework.notification.NotificationManager;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.sync.SyncUtil;
import br.com.wmw.framework.sync.data.tc2web.SyncTc2WebHttp;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.timer.LogSyncTimer;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DatabaseUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.framework.util.ZipFileUtil;
import br.com.wmw.framework.util.ZipUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.builder.ItemPedidoBuilder;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ClienteAtua;
import br.com.wmw.lavenderepda.business.domain.ColetaInfosPda;
import br.com.wmw.lavenderepda.business.domain.ConfigIntegWebTc;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.DapLaudo;
import br.com.wmw.lavenderepda.business.domain.DapLaudoAtua;
import br.com.wmw.lavenderepda.business.domain.EstoqueRep;
import br.com.wmw.lavenderepda.business.domain.FotoItemTroca;
import br.com.wmw.lavenderepda.business.domain.FotoNovoCliente;
import br.com.wmw.lavenderepda.business.domain.FotoPedido;
import br.com.wmw.lavenderepda.business.domain.FotoPesqMerProdConc;
import br.com.wmw.lavenderepda.business.domain.FotoPesquisaMercado;
import br.com.wmw.lavenderepda.business.domain.InfoFretePedido;
import br.com.wmw.lavenderepda.business.domain.ItemNfce;
import br.com.wmw.lavenderepda.business.domain.ItemNfe;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoAgrSimilar;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoAud;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoRemessa;
import br.com.wmw.lavenderepda.business.domain.LogApp;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.domain.Nfce;
import br.com.wmw.lavenderepda.business.domain.Nfe;
import br.com.wmw.lavenderepda.business.domain.NotificacaoPda;
import br.com.wmw.lavenderepda.business.domain.NovoCliEndereco;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.PagamentoPedido;
import br.com.wmw.lavenderepda.business.domain.ParcelaPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoBoleto;
import br.com.wmw.lavenderepda.business.domain.PedidoDesc;
import br.com.wmw.lavenderepda.business.domain.PesquisaApp;
import br.com.wmw.lavenderepda.business.domain.PesquisaRespApp;
import br.com.wmw.lavenderepda.business.domain.PontoGps;
import br.com.wmw.lavenderepda.business.domain.Recado;
import br.com.wmw.lavenderepda.business.domain.ResumoDia;
import br.com.wmw.lavenderepda.business.domain.VenctoPagamentoPedido;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.service.ClienteAtuaService;
import br.com.wmw.lavenderepda.business.service.ColetaGpsPontosEspecificosService;
import br.com.wmw.lavenderepda.business.service.ColetaGpsTimerService;
import br.com.wmw.lavenderepda.business.service.ColetaInfosPdaService;
import br.com.wmw.lavenderepda.business.service.ConfigIntegWebTcService;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.ErroEnvioService;
import br.com.wmw.lavenderepda.business.service.EstoqueRepService;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.FotoItemTrocaService;
import br.com.wmw.lavenderepda.business.service.FotoPedidoService;
import br.com.wmw.lavenderepda.business.service.FotoPesqMerProdConcService;
import br.com.wmw.lavenderepda.business.service.FotoPesquisaMercadoService;
import br.com.wmw.lavenderepda.business.service.ItemNfeService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoAudService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoRemessaService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.LogAppService;
import br.com.wmw.lavenderepda.business.service.LogPdaService;
import br.com.wmw.lavenderepda.business.service.NfeService;
import br.com.wmw.lavenderepda.business.service.NotificacaoPdaService;
import br.com.wmw.lavenderepda.business.service.NovoClienteService;
import br.com.wmw.lavenderepda.business.service.PagamentoPedidoService;
import br.com.wmw.lavenderepda.business.service.PedidoDescService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.PesquisaRespAppFotoService;
import br.com.wmw.lavenderepda.business.service.PontoGpsService;
import br.com.wmw.lavenderepda.business.service.RecadoService;
import br.com.wmw.lavenderepda.business.service.ResumoDiaService;
import br.com.wmw.lavenderepda.business.service.StatusGpsService;
import br.com.wmw.lavenderepda.business.service.VerbaSaldoService;
import br.com.wmw.lavenderepda.business.service.VisitaFotoService;
import br.com.wmw.lavenderepda.business.service.VisitaService;
import br.com.wmw.lavenderepda.business.validation.DuplicatedCnpjException;
import br.com.wmw.lavenderepda.business.validation.HoraLimitePedidoExceditaException;
import br.com.wmw.lavenderepda.business.validation.LiberacaoDataEntregaPedidoException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DapLaudoAtuaDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FotoItemTrocaDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FotoNovoClienteDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FotoPedidoDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.InfoFretePedidoDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemNfceDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemNfeDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemPedidoAgrSimilarDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemPedidoAudDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemPedidoGradeDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemPedidoRemessaDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NfceDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NfeDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NovoCliEnderecoDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NovoClientePdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PagamentoPedidoDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ParcelaPedidoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoBoletoDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PesquisaAppPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PesquisaRespAppFotoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PesquisaRespAppPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VenctoPagamentoPedidoDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VisitaFotoDao;
import br.com.wmw.lavenderepda.notification.LavendereNotificationConstants;
import br.com.wmw.lavenderepda.presentation.ui.ext.PedidoUiUtil;
import totalcross.io.ByteArrayStream;
import totalcross.io.IOException;
import totalcross.io.LineReader;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.sql.Types;
import totalcross.sys.Convert;
import totalcross.sys.Time;
import totalcross.sys.Vm;
import totalcross.util.Date;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class LavendereTc2Web extends SyncTc2WebHttp {

	public static Vector imagemEnvioErroList = new Vector();
	public static Vector imagemEnvioSucessoList = new Vector();
	public static Vector pedidoDescList = new Vector();

	public LavendereTc2Web() throws SQLException {
		super(HttpConnectionManager.getDefaultParamsSync());
	}
	
	public LavendereTc2Web(ParamsSync paramsSync) {
		super(paramsSync);
	}
	
	public LavendereTc2Web(ParamsSync paramsSync, boolean doValidations) throws SQLException {
		super(paramsSync);
		if (doValidations) {
			doValidations();
		}
	}
	
	private void doValidations() throws SQLException {
	}

	protected void abreConexao() throws Exception {
		LogSync.info(Messages.TC2WEB_ABRINDO_CONEXAO);
		super.abreConexao();
		LogSync.replace(Messages.TC2WEB_CONEXAO_ESTABELECIDA);
	}
	
	
	public void conectaEnviaDadosServidorFull(Hashtable hashTablesEnvio) throws Exception {
		abreConexao();
		try {
			inicioEnvio();
			removeTabelasEnviadasPorOutroServico(hashTablesEnvio);
			super.envieDadosServidor(hashTablesEnvio);
			enviePedidosServidor();
			envieEstoqueRep();
			enviePontoGpsTxt();
			envieNovoClienteServidor();
			envieColetaInfosPda();
			fimEnvio();
		} finally {
			fechaConexao();
		}
	}

	private void envieColetaInfosPda() throws Exception {
		if (!LavenderePdaConfig.usaRadarRepresentante) return;
		LogSyncTimer timer = new LogSyncTimer(Messages.TC2WEB_ENVIO_COLETAINFOSPDA, Messages.TC2WEB_FIM_ENVIO_COLETAINFOSPDA).newLogOnFinish();
		try {
			montaColetaInfosPda();
			ConfigIntegWebTc configIntegWebTcFilter = new ConfigIntegWebTc();
			configIntegWebTcFilter.dsTabela = ColetaInfosPda.TABLE_NAME;
			ConfigIntegWebTc webTc = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
			if (webTc.isAtivo()) {
				envieDadosTable(ColetaInfosPda.TABLE_NAME);
			}
		} finally {
			timer.finish();
		}
		
	}

	public void removeTabelasEnviadasPorOutroServico(Hashtable hashTablesEnvio) {
		// Tabelas de Pedido e Item são enviadas separadas ao servidor
		hashTablesEnvio.remove(Pedido.TABLE_NAME_PEDIDO);
		hashTablesEnvio.remove(ItemPedido.TABLE_NAME_ITEMPEDIDO);
		hashTablesEnvio.remove(ParcelaPedido.TABLE_NAME);
		hashTablesEnvio.remove(ItemPedidoGrade.TABLE_NAME_ITEMPEDIDOGRADE);
		hashTablesEnvio.remove(InfoFretePedido.TABLE_NAME);
		hashTablesEnvio.remove(ItemPedidoAud.TABLE_NAME);
		hashTablesEnvio.remove(PagamentoPedido.TABLE_NAME);
		if (!LavenderePdaConfig.usaDevolucaoTotalEstoqueRemessaRepresentante) { 
			hashTablesEnvio.remove(EstoqueRep.TABLE_NAME);
		}
		hashTablesEnvio.remove(FotoPedido.TABLE_NAME);
		hashTablesEnvio.remove(ItemPedidoRemessaDbxDao.TABLE_NAME);
		if (LavenderePdaConfig.usaValidacaoCPFCNPJBaseWeb) {
			hashTablesEnvio.remove(NovoCliente.TABLE_NAME);
			hashTablesEnvio.remove(NovoCliEndereco.TABLE_NAME);
			hashTablesEnvio.remove(FotoNovoCliente.TABLE_NAME);
		}
		hashTablesEnvio.remove(Nfe.TABLE_NAME);
		hashTablesEnvio.remove(ItemNfe.TABLE_NAME);
		if (LavenderePdaConfig.usaGeracaoBoletoContingencia > 0) {
			hashTablesEnvio.remove(PedidoBoleto.TABLE_NAME);
	}
		if (LavenderePdaConfig.usaVencimentosAdicionaisBoleto) {
			hashTablesEnvio.remove(VenctoPagamentoPedido.TABLE_NAME);
		}
		hashTablesEnvio.remove(ColetaInfosPda.TABLE_NAME);
		hashTablesEnvio.remove(Nfce.TABLE_NAME);
		hashTablesEnvio.remove(ItemNfce.TABLE_NAME);
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			hashTablesEnvio.remove(ItemPedidoAgrSimilar.TABLE_NAME);
	}
	}

	private void envieEstoqueRep() throws Exception {
		if (LavenderePdaConfig.isEnviaEstoqueRepParaServidor()) {
			LogSync.logSection("Envio EstoqueRep");
			EstoqueService.getInstance().devolveEstoqueAtual();
			boolean enviouDados = false;
			if (LavenderePdaConfig.isEnviaEstoqueRepParaServidorDevolveEstoqueAtual()) {
				if (SessionLavenderePda.isDevolverEstoqueAtual) {
					super.envieDadosServidor(SyncManager.getInfoAtualizacaoEstoqueRep(), false);
					enviouDados = true;
				}
			} else {
				super.envieDadosServidor(SyncManager.getInfoAtualizacaoEstoqueRep(), false);
				enviouDados = true;
			}
			if (enviouDados) {
				if (!houveErroSincronizacao) {
					ConfigInternoService.getInstance().addValue(ConfigInterno.devolverEstoqueAtual, SessionLavenderePda.cdEmpresa + SessionLavenderePda.getRepresentante().cdRepresentante, DateUtil.getCurrentDateYYYYMMDD());
				}
				EstoqueRepService.getInstance().deleteRegistroEnviadoServidor();
			}
		}		
	}

	public void executeFimEnvioDados(String cdSessao) throws Exception {
		int oldNuMaxTentativas = paramsSync.nuMaxTentativas;
		paramsSync.nuMaxTentativas = 1;
		try {
			abreConexao();
			try {
				httpSync.fimEnvioDados(cdSessao);
			} catch (Throwable ex) {
				if (ex.getMessage().endsWith("timed out")) {
					throw new Exception(FrameworkMessages.MSG_TIMEOUT_CONEXAO_SERVIDOR);
				}
				throw ex;
			} finally {
				fechaConexao();
			}
		} finally {
			paramsSync.nuMaxTentativas = oldNuMaxTentativas;
		}
	}

	public Vector writeDados(String tableName, ResultSet rs, ByteArrayStream cbasRetorno, boolean insereContador, int[] retContador) throws SQLException {
		try {
			return super.writeDados(tableName, rs, cbasRetorno, insereContador, retContador);
		} catch (IOException e) {
			return new Vector();
		}
	}
	
	public void enviaPedidoServidorBackground(String cdSessao, String nuPedido, int nuLinhas, ByteArrayStream cbas) throws Exception {
		if (nuLinhas <= 0) {
			return;
		}
		validateDataHoraLimiteEnvioPedido();
		if (SessionLavenderePda.envioPedidosBloqueadoHoraLimite) {
			throw new HoraLimitePedidoExceditaException();
		}
		abreConexao();
		try {
			executeEnviaPedido(cdSessao, nuPedido, nuLinhas, getByteArrayStreamZipped(cbas));
			LogAppService.getInstance().logPedido(LogApp.FL_TIPO_LOG_INFO, LogApp.VL_CHAVE_EMPTY, null, LogApp.DS_DETALHES_EMISSAO_PEDIDO_SB);
		} finally {
			fechaConexao();
		}
	}


	public ByteArrayStream enviaDadosServidorBackground(String cdSessao, String tableName, int nuLinhas, ByteArrayStream cbas) throws Exception {
		if (nuLinhas <= 0) {
			return null;
		}
		abreConexao();
		try {
			return httpSync.enviaDados(cdSessao, tableName, nuLinhas, getByteArrayStreamZipped(cbas));
		} finally {
			fechaConexao();
		}
	}

	private ByteArrayStream getByteArrayStreamZipped(ByteArrayStream cbas) {
		cbas.mark();
		return ZipUtil.zipToByteArray(cbas);
	}

	protected void inicioEnvio() throws Exception {
		
	}

	private boolean validateDataHoraLimiteEnvioPedido() throws SQLException, Exception {
		if (SessionLavenderePda.autorizadoPorSenhaEnviarPedidosQualquerHorario) {
			return false;
		}
		long dtHrServidor = obterDataHoraServidorAtualizada();
		if (dtHrServidor <= 0) {
			return true;
		}
		if (LavenderePdaConfig.isHoraLimiteParaEnvioPedidos()) {
			String timeString = ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.dataHoraServidor);
			if (ValueUtil.isNotEmpty(timeString)) {
				return PedidoUiUtil.isPeriodoDeEnvioPedidoUltrapassado(new Time(ValueUtil.getLongValue(timeString)));
			}
		}
		return false;
	}

	public long obterDataHoraServidorAtualizada() throws SQLException, Exception {
		String time = SyncManager.getDataHoraServidor(paramsSync);
		long timeLong = ValueUtil.getLongValue(time);
		if (timeLong > 0) {
			ConfigInternoService.getInstance().addValueGeral(ConfigInterno.dataHoraServidor, time);
		}
		return timeLong;
	}

	protected void antesAtualizarFlTipoAlteracao(String tableName) throws SQLException {
		super.antesAtualizarFlTipoAlteracao(tableName);
		if (Recado.TABLE_NAME.equals(tableName)) {
			LogSync.info(FrameworkMessages.LIMPANDO + tableName + "...");
			RecadoService.getInstance().deleteAllPurged();
		}
	}

	public void antesPegarDadosEnvio(String tableName) {
		if (Recado.TABLE_NAME.equals(tableName)) {
			try {
				RecadoService.getInstance().updateDtHrEnvioRecados();
			} catch (Throwable e) { /* não faz nada, apenas não atualizou nenhum pedido porque não há pedidos a ser enviados */	}
		} else if (ResumoDia.TABLE_NAME.equals(tableName) && !LavenderePdaConfig.naoCalculaDadosResumoDiaPda) {
			try {
				if (!LavenderePdaConfig.usaFechamentoDeVendasPorPeriodo) {
					Vector dataList = PedidoService.getInstance().findDistinctDtEmissaoUltimosPedidosNaoTransmitidos(SessionLavenderePda.getRepresentante().cdRepresentante);
					for (int i = 0; i < dataList.size(); i++) {
						ResumoDiaService.getInstance().calculateAndGetResumoDia((Date)dataList.items[i], SessionLavenderePda.getRepresentante().cdRepresentante);
					}
					Pedido pedidoFilter = new Pedido();
					pedidoFilter.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoFechado;
					int qtPedidosFechados = PedidoService.getInstance().countByExample(pedidoFilter);
					if (qtPedidosFechados > 0) {
						ResumoDiaService.getInstance().calculateAndGetResumoDia(DateUtil.getCurrentDate(), SessionLavenderePda.getRepresentante().cdRepresentante);
					}
				}
			} catch (Throwable ex) { 
				// Não faz nada.
			}
		} else if (NovoCliente.TABLE_NAME.equals(tableName)) {
			try {
				Vector novoClienteList = NovoClienteService.getInstance().findNovoClienteListParaValidacao();
				int size = novoClienteList.size();
				NovoCliente novoCliente;
				int cont = 0;
				for (int i = 0; i < size; i++) {
					novoCliente = (NovoCliente)novoClienteList.items[i];
					try {
						NovoClienteService.getInstance().validaCpfCnpjDuplicado(novoCliente.nuCnpj, ValueUtil.VALOR_NI, true, novoCliente.flTipoCadastro);
					} catch (DuplicatedCnpjException e) {
						if (LavenderePdaConfig.usaValidacaoCPFCNPJBaseWeb) {
							if (cont == 0) {
								LogSync.delete();
							}
							cont++;
							novoCliente.flStatusCadastro = NovoCliente.FLSTATUSCADASTRO_PENDENTE;
							NovoClienteService.getInstance().updateFlStatusCadastro(novoCliente);
							LogSync.error(MessageUtil.getMessage(Messages.NOVOCLIENTE_MSG_LOG_CPFCNPJ, novoCliente.nuCnpj));
						}
					}
				}
				if (cont > 0) {
					LogSync.info(ValueUtil.VALOR_NI);
				}
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
			}
		}
	}

	public void depoisEnvioDados(String tableName) throws SQLException {
		if (LogPda.TABLE_NAME.equals(tableName)) {
			LogPdaService.getInstance().deleteAllByExample(new LogPda());
		} else if (Recado.TABLE_NAME.equals(tableName)) {
			RecadoService.getInstance().updateAllRecadosToEnviados();
		} else if (Visita.TABLE_NAME.equals(tableName)) {
			VisitaService.getInstance().atualizaVisitaAposEnvioServidor();
		} else if (NotificacaoPda.TABLE_NAME.equals(tableName)) {
			NotificacaoPdaService.getInstance().deleteAlteracoesTabelaNotificacoes();
		} else if (ClienteAtua.TABLE_NAME.equals(tableName) && LavenderePdaConfig.habilitaAtualizacaoCadastroCliente) {
			ClienteAtuaService.getInstance().deleteAllByExample(new ClienteAtua());
		}
	}

	@Override
	protected void fimEnvio() throws Exception {
		super.fimEnvio();
		if (!houveErroSincronizacao && LavenderePdaConfig.usaMultiplasLiberacoesDescontoNoPedido()) {
			PedidoDescService.getInstance().updateInfoAposEnvioPedidoDesc(pedidoDescList);
		}
		if (!houveErroSincronizacao && LavenderePdaConfig.apresentaConsumoVerbaDePedidoNaoTransmitido) {
			VerbaSaldoService.getInstance().enviaVerbaSaldoPedidosAbertos();
	}
	}
	
	@Override
	public void antesEnvioDados(String tableName) throws SQLException {
		super.antesEnvioDados(tableName);
		if (PedidoDesc.TABLE_NAME.equals(tableName) && LavenderePdaConfig.usaDescontoPonderadoPedido && LavenderePdaConfig.restringeDescontoPedidoBaseadoMediaPonderada) {
			pedidoDescList = PedidoDescService.getInstance().findAllAlterados();
		}
	}

	public int antesEnvioPedido(Pedido pedido) throws Exception {
		validateHoraLimiteEnvioPedido();
		if (!pedido.isPedidoCancelado()) {
			PedidoService.getInstance().validatePedidoFechado(pedido);
		}
		return PedidoService.getInstance().updatePedidoProcessandoEnvio(pedido);
	}

	private void validateHoraLimiteEnvioPedido() throws SQLException, Exception {
		SessionLavenderePda.envioPedidosBloqueadoHoraLimite = validateDataHoraLimiteEnvioPedido();
		if (SessionLavenderePda.envioPedidosBloqueadoHoraLimite) {
			NotificationManager.putNotification(new Notification(LavendereNotificationConstants.PEDIDO_ENVIOBLOQUEADO, PedidoUiUtil.getMensagemBloqueioEnvioPedidos()) {
				@Override
				public void process() throws Exception {
					SyncActions.showLiberacaoSenhaEnvioPedidoBloqueado(message);
				}
			});
			throw new HoraLimitePedidoExceditaException();
		}
	}

	public void depoisEnvioPedido(Pedido pedido) throws SQLException {
		PedidoService.getInstance().updateInfosPedidoAposEnvioServidor(pedido, paramsSync.baseUrl);
		if (LavenderePdaConfig.usaPesoGerarRealizado && LavenderePdaConfig.usaBloqueioVendaProdutoBaseadoRealizadoMetaGrupoProd && (!pedido.isPedidoBonificacao() || LavenderePdaConfig.usaItensBonificadoCalculoRealizado)) {
			pedido.metaPorGrupoProdHash = new Hashtable("");
			ItemPedidoService.getInstance().updateRealizadoMetaPorGrupoProdAposEnvioServidor(pedido);
		}
	}

	private void geraColetaGpsToTxt() {
		if (LavenderePdaConfig.isColetaDadosGpsRepresentante()) {
			ColetaGpsTimerService.getInstance().gravaEmTxt();
		}
		if (LavenderePdaConfig.isUsaColetaGpsPontosEspecificosSistema()) {
			ColetaGpsPontosEspecificosService.getInstance().gravaEmTxt();
		}
	}

	private void enviePontoGpsTxt() throws Exception {
		if (LavenderePdaConfig.isColetaDadosGpsRepresentante() || LavenderePdaConfig.isUsaColetaGpsPontosEspecificosSistema() || LavenderePdaConfig.isUsaColetaGpsAppExterno()) {
			LogSync.logSection(Messages.TC2WEB_GPS_ENVIO);
			LogSyncTimer timer = new LogSyncTimer(Messages.TC2WEB_GPS_ENVIO);
			try {
				geraColetaGpsToTxt();
			int countRegistrosSucess = 0;
			int countRegistrosErro = 0;
			String errorMessage = null;
			String[] files = PontoGpsService.getInstance().getFiles();
			Vector filesToDelete = new Vector();
			if (ValueUtil.isNotEmpty(files)) {
				ByteArrayStream cbasEnvio = null;
				int sizeFiles = files.length;
				int ultimoArquivo = sizeFiles - 1;
				int qtMinPontoGpsPorArquivo = 1000;
				int countPontoGps = 0;
				for (int i = 0; i < sizeFiles; i++) {
					String file = files[i];
					Vector pontoGpsList;
					try {
						pontoGpsList = PontoGpsService.getInstance().getPontoGpsListFromFile(file);
						} catch (Throwable e1) {
						LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_SYNC_BACKGROUND, e1.getMessage());
						continue;
					}
					int size = pontoGpsList.size();
					if (size > 0) {
						if (countPontoGps == 0) {
							cbasEnvio = new ByteArrayStream(4096);
							SyncUtil.writeLine(cbasEnvio, PontoGpsService.getInstance().addSelectColumns());
						}
						for (int x = 0; x < size; x++) {
							SyncUtil.writeLine(cbasEnvio, PontoGpsService.getInstance().addInsertValues((String[]) pontoGpsList.items[x]));
							countPontoGps++;
						}
						filesToDelete.addElement(file);
						if (countPontoGps > qtMinPontoGpsPorArquivo || i >= ultimoArquivo) {
							try {
								ByteArrayStream retornoServidor = httpSync.enviaDados(PontoGps.TABLE_NAME, countPontoGps, getByteArrayStreamZipped(cbasEnvio));
								LineReader lr = new LineReader(retornoServidor);
								String retorno = lr.readLine();
								if (ValueUtil.isNotEmpty(retorno) && retorno.startsWith("OK")) {
									countRegistrosSucess = countRegistrosSucess + countPontoGps;
									for (int j = 0; j < filesToDelete.size(); j++) {
										String fileToDelete = (String) filesToDelete.items[j];
										PontoGpsService.getInstance().deletePontoGpsFile(fileToDelete);
									}
								} else {
									countRegistrosErro = countRegistrosErro + countPontoGps;
								}
								} catch (Throwable e) {
								houveErroSincronizacao = true;
								LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_SYNC_BACKGROUND, e.getMessage());
								if (paramsSync.stopOnError) {
										LogSync.error(Messages.TC2WEB_GPS_ERRO + e.getMessage());
									throw e;
								} else {
									countRegistrosErro++;
									errorMessage = e.getMessage();
								}
							} finally {
								countPontoGps = 0;
								filesToDelete.removeAllElements();
							}
						} 
					}
				}
				if (countRegistrosSucess > 0) {
						LogSync.sucess(Messages.TC2WEB_GPS_OK + countRegistrosSucess + Messages.TC2WEB_GPS_REGISTROS);
				}
				if (countRegistrosErro > 0) {
						LogSync.error(Messages.TC2WEB_GPS_ERRO + errorMessage);
				}
			}
				LogSync.info(Messages.TC2WEB_FIMENVIO_GPS);
			} finally {
				timer.finish();
		}
	}
	}

	public Vector getPedidoListParaEnvio() throws SQLException {
		Pedido pedidoFilter = new Pedido();
		List<String> cdStatusPedidoList = new ArrayList<>();
		cdStatusPedidoList.add(LavenderePdaConfig.cdStatusPedidoFechado);
		if (LavenderePdaConfig.usaPedidoPerdido) {
			cdStatusPedidoList.add(LavenderePdaConfig.cdStatusPedidoPerdido);
		}
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			cdStatusPedidoList.add(LavenderePdaConfig.cdStatusPedidoCancelado);
			cdStatusPedidoList.add(LavenderePdaConfig.cdStatusPedidoAberto);
			pedidoFilter.usaFiltroTipoAlteracao = true;
		} else if (LavenderePdaConfig.usaCancelamentoAutomaticoPedidoAbertoFechado() || LavenderePdaConfig.getDataBaseCancelamentoAutoPedidoClienteKeyAccount() != null) {
			cdStatusPedidoList.add(LavenderePdaConfig.cdStatusPedidoCancelado);
			pedidoFilter.usaFiltroTipoAlteracao = true;
		}
		if (LavenderePdaConfig.usaValidaConversaoFOB()) {
			cdStatusPedidoList.add(LavenderePdaConfig.getCdStatusPedidoFOBPendenteAprovacao());
		}
		if (LavenderePdaConfig.usaMarcaPedidoPendenteAprovacaoCondPagto()) {
			cdStatusPedidoList.add(LavenderePdaConfig.getCdStatusPendenteCondPagto());
		}
		if (LavenderePdaConfig.usaPedidoPendenteRetornoRepLimCred()) {
			cdStatusPedidoList.add(LavenderePdaConfig.getCdStatusLiberaRepLimCred());
		}
		pedidoFilter.ignoraTipoPedidoIgnoraEnvioErp = true;
		pedidoFilter.cdStatusPedidoFilter = cdStatusPedidoList.toArray(new String[] {});
		return PedidoService.getInstance().findAllByExampleOnlyPda(pedidoFilter);
	}

	public Vector getPedidoListParaAvisoDePerda() throws SQLException {
		Pedido pedidoFilter = new Pedido();
		List<String> cdStatusPedidoList = new ArrayList<>();
		cdStatusPedidoList.add(LavenderePdaConfig.cdStatusPedidoAberto);
		cdStatusPedidoList.add(LavenderePdaConfig.cdStatusPedidoConsignado);
		cdStatusPedidoList.add(LavenderePdaConfig.cdStatusPedidoPendenteAprovacao);
		cdStatusPedidoList.add(LavenderePdaConfig.cdStatusPedidoPendenteAprovacaoLimCredito);
		cdStatusPedidoList.add(LavenderePdaConfig.cdStatusPedidoFechado);
		pedidoFilter.cdStatusPedidoFilter = cdStatusPedidoList.toArray(new String[] {});
		return PedidoService.getInstance().findAllByExampleOnlyPda(pedidoFilter);
	}

	private void enviePedidosServidor() throws Exception {
		LogSync.logSection(Messages.TC2WEB_ENVIOPEDIDOS);
		Vector pedidoList = getPedidoListParaEnvio();
		String nuLoteProtocolo = PedidoService.getInstance().generateIdGlobal();
		int pedidoListSize = pedidoList.size();
		for (int i = 0; i < pedidoListSize; i++) {
			Pedido pedido = (Pedido) pedidoList.items[i];
			if (LavenderePdaConfig.enviaProtocoloPedidosEmailHtml) {
				pedido.nuLoteProtocolo = pedido.cdRepresentante + nuLoteProtocolo;
				pedido.flEnviadoProtocolo = ValueUtil.VALOR_NAO;
				PedidoService.getInstance().updateProtocolo(pedido);
			}
			if (!paramsSync.isFromTelaCadPedidoPrimeiroPlano && SessionLavenderePda.isPedidoProcessandoFechamento(pedido)) {
				continue;
			}
			if (PedidoService.getInstance().isPedidoSemNota(pedido)) continue;
			
			LogSync.info(Messages.TC2WEB_ENVIANDOPEDIDO.concat(pedido.nuPedido).concat("..."));
			if (LavenderePdaConfig.usaRecalculoValoresDosPedidos && PedidoService.getInstance().necessarioRecalculoPedido(pedido)) {
				LogSync.replace(pedido.dsPedidoPrefixo() +  Messages.PEDIDO_DEVE_SER_RECALCULO, LogSync.COR_ERROR);
				continue;
			}
			try {
			int n = ItemPedidoService.getInstance().countByExample(new ItemPedidoBuilder(pedido).build());
			if (n == 0 && !pedido.isPedidoCancelado()) {
					LogSync.replace(pedido.dsPedidoPrefixo() + Messages.TC2WEB_NAO_CONTEM_ITENS, LogSync.COR_WARN);
				continue;
			}
				int result = antesEnvioPedido(pedido);
				if (result == 0) {
					throw new ValidationException(Messages.ENVIAR_PEDIDO_ERRO);
				}
			} catch (HoraLimitePedidoExceditaException ex) {
				LogSync.replace(ex.getMessage(), LogSync.COR_ERROR);
				return;
			} catch (Throwable e) {
				trataPedidoInvalidoParaEnvio(pedido, e);
				continue;
			}
			if (LavenderePdaConfig.usaControleEstoquePorRemessa && LavenderePdaConfig.isVerificaPedidoJaEnviadoReservaEstoqueComTrigger && isPedidoJaEnviadoServidor(pedido)) {
				atualizaInfosPedidoJaEnviadoComRemessaEstoque(pedido);
				continue;
			}
			try (	ByteArrayStream cbas = new ByteArrayStream(11);
					Statement stPedido = getDriver().getStatement();
					Statement stItemPedido = getDriver().getStatement();
					ResultSet rsPedido = stPedido.executeQuery(PedidoPdbxDao.getInstance().findByRowKeySql(pedido.getRowKey()));
					ResultSet rsItensPedido = stItemPedido.executeQuery(ItemPedidoService.getInstance().findSqlItemPedido(pedido))) {
				
				int qtItensPedido = montaPacoteEnvioPedidoServidor(pedido, rsPedido, rsItensPedido, cbas);
				executeEnviaPedido(paramsSync.cdSessao, pedido.nuPedido, qtItensPedido, getByteArrayStreamZipped(cbas));
				depoisEnvioPedido(pedido);
				LogAppService.getInstance().logPedido(LogApp.FL_TIPO_LOG_INFO, pedido.getRowKey(), pedido.cdCliente, LogApp.DS_DETALHES_EMISSAO_PEDIDO_SS, StringUtil.getStringValue(pedido.vlTotalPedido));
				LogSync.replace(pedido.dsPedidoPrefixo() + Messages.TC2WEB_OK + qtItensPedido + Messages.TC2WEB_ITENS, LogSync.COR_INFO);
			} catch (ConnectionException e) {
				liberaAlteracaoPedidoBaseadoReservaEstoque(pedido);
				logErroEnvioPedidoServidor(pedido, e);
			} catch (Throwable e) {
				liberaAlteracaoPedidoBaseadoReservaEstoque(pedido);
				logErroEnvioPedidoServidor(pedido, e);
			}
		}
		LogSync.logSection(Messages.TC2WEB_FIMENVIO_PEDIDOS);
	}

	private void atualizaInfosPedidoJaEnviadoComRemessaEstoque(Pedido pedido) throws SQLException {
		try {
			ItemPedidoRemessaService.getInstance().updateEstoquePdaToERP(pedido, false);
			if (LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento()) {
				NfeService.getInstance().atualizaNfeAposEnvioServidor();
				ItemNfeService.getInstance().atualizaItemNfeAposEnvioServidor();
			}
		} finally {
			PedidoService.getInstance().atualizaPedidoTransmitidoSucesso(pedido, paramsSync.baseUrl);
		}
	}

	private void logErroEnvioPedidoServidor(Pedido pedido, Throwable e) throws SQLException {
		String msgErro = pedido.dsPedidoPrefixo().concat(Messages.TC2WEB_ERRO).concat(e.getMessage());		LogAppService.getInstance().logPedido(LogApp.FL_TIPO_LOG_ERRO, pedido.getRowKey(), pedido.cdCliente, LogApp.DS_DETALHES_EMISSAO_PEDIDO_SE, StringUtil.getStringValue(pedido.vlTotalPedido), msgErro);
		LogSync.replace(msgErro, LogSync.COR_ERROR);
		LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_SYNC_BACKGROUND, msgErro);
		houveErroSincronizacao = true;
		ErroEnvioService.getInstance().saveErroEnvioPedido(pedido.getRowKey(), msgErro);
	}

	private void liberaAlteracaoPedidoBaseadoReservaEstoque(Pedido pedido) {
		try {
			if (!LavenderePdaConfig.isUsaReservaEstoqueCentralizadoAtomico() || paramsSync.isFromTelaCadPedidoPrimeiroPlano || !pedido.isPedidoFechado()) {
				return;
			}
			if (isCausaDoErroRelacionadoReservaEstoque(pedido)) {
				PedidoService.getInstance().updateColumn(pedido.getRowKey(), "FLTIPOALTERACAO", BaseDomain.FLTIPOALTERACAO_ALTERADO, Types.VARCHAR);
			}
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		}
	}
	
	private boolean isCausaDoErroRelacionadoReservaEstoque(Pedido pedido) throws Exception {
		pedido.itemPedidoList = ItemPedidoService.getInstance().findAllByExampleSummaryUnique(new ItemPedidoBuilder(pedido).build());
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			((ItemPedido)pedido.itemPedidoList.items[i]).pedido = pedido;
		}
		String retorno = SyncManager.verificaReservaCadastradaServidor(pedido, pedido.itemPedidoList);
		return !"OK".equals(retorno);
	}

	private void trataPedidoInvalidoParaEnvio(Pedido pedido, Throwable le) throws SQLException {
		String retorno = "";
		try {
			retorno = SyncManager.verificaPedidoEnviadoServidor(pedido);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
		if (Pedido.DSRESPOSTA_PEDIDO_NAO_ENVIADO_SERVIDOR.equals(retorno) && le instanceof LiberacaoDataEntregaPedidoException) {
			pedido.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;
			PedidoService.getInstance().updateColumn(pedido.getRowKey(), "FLTIPOALTERACAO", BaseDomain.FLTIPOALTERACAO_ALTERADO, Types.VARCHAR);
		} else if (Pedido.DSRESPOSTA_PEDIDO_ENVIADO_SERVIDOR.equals(retorno)) {
			depoisEnvioPedido(pedido);
			LogSync.replace(pedido.dsPedidoPrefixo() + "OK ", LogSync.COR_INFO);
			return;
		}
		logErroEnvioPedidoServidor(pedido, le);
	}

	private boolean isPedidoJaEnviadoServidor(Pedido pedido) {
		String retorno = "";
		try {
			retorno = SyncManager.verificaPedidoEnviadoServidor(pedido);
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
		return Pedido.DSRESPOSTA_PEDIDO_ENVIADO_SERVIDOR.equals(retorno);
	}

	private void executeEnviaPedido(String cdSessao, String nuPedido, int nuLinhas,  ByteArrayStream cbas) throws IOException {
		ByteArrayStream httpResult = httpSync.executePostHttp(paramsSync.httpTc2WebUrl + "enviaPedidoServidor/" + httpSync.addParamUrl(cdSessao) + "/" + httpSync.addParamUrl(Session.getCdUsuario()) + "/" + httpSync.addParamUrl(nuPedido) + "/" + nuLinhas, cbas, "application/octet-stream", false);
		LineReader lr = new LineReader(httpResult);
		String msg = lr.readLine();
		if (!"OK".equals(msg)) {
			VmUtil.debug(msg);
			throw new ValidationException(Messages.TC2WEB_ERRO_DESCONHECIDO);
		}
	}

	// retorna o nr de itens do pedido
	public int montaPacoteEnvioPedidoServidor(Pedido pedido, ResultSet rsPedido, ResultSet rsItensPedido, ByteArrayStream cbas) throws IOException, SQLException {
	    int[] nr = new int[1];
		writeDados(Pedido.TABLE_NAME_PEDIDO, rsPedido, cbas, false, null);
    	//-- Itens do Pedido
		SyncUtil.writeLine(cbas, "*ITENSPEDIDO*");
    	writeDados(ItemPedido.TABLE_NAME_ITEMPEDIDO, rsItensPedido, cbas, true, nr);
		writeParcelasPedido(pedido, cbas);
    	writePagamentosPedido(pedido, cbas);
    	writeItemPedidoRemessa(pedido, cbas);
    	writeItemPedidoGrade(pedido, cbas);
    	writeInfoFretePedido(pedido, cbas);
    	writeItemPedidoAud(pedido, cbas);
    	writeFotoPedido(pedido, cbas);
    	writeNfe(pedido, cbas);
    	writeItemNfe(pedido, cbas);
    	writePedidoBoleto(pedido, cbas);
    	writeVenctopPagamentoPedido(pedido, cbas);
    	writeNfce(pedido, cbas);
    	writeItemNfce(pedido, cbas);
    	writeItemPedidoAgrSimilar(pedido, cbas);
		writeFotoItemTroca(pedido, cbas);
    	return nr[0];
    	}

	private void writeItemPedidoAgrSimilar(Pedido pedido, ByteArrayStream cbas) throws IOException, SQLException {
		SyncUtil.writeLine(cbas, "*ITEMPEDIDOAGRSIMILAR*");
    	if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
    		ItemPedidoAgrSimilar filter = new ItemPedidoAgrSimilar();
    		filter.cdEmpresa = pedido.cdEmpresa;
    		filter.cdRepresentante = pedido.cdRepresentante;
    		filter.nuPedido = pedido.nuPedido;
    		filter.flOrigemPedido = pedido.flOrigemPedido;
    		try (Statement st = getDriver().getStatement();
    				ResultSet rs = st.executeQuery(ItemPedidoAgrSimilarDao.getInstance().findAllByExampleSql(filter))) {
    			if (rs.isBeforeFirst()) {
    				int count = ItemPedidoAgrSimilarDao.getInstance().countByExample(filter);
    			SyncUtil.writeLine(cbas, Convert.toString(count));
    				writeDados(ItemPedidoAgrSimilar.TABLE_NAME, rs, cbas, true, null);
    		}
    	}
    	}
	}

	private void writeItemNfce(Pedido pedido, ByteArrayStream cbas) throws IOException, SQLException {
		SyncUtil.writeLine(cbas, "*ITEMNFCE*");
    	if (LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento() && !LavenderePdaConfig.usaNfePorReferencia && !LavenderePdaConfig.isConfigGradeProduto()) {
    		ItemNfce filter = new ItemNfce();
    		filter.cdEmpresa = pedido.cdEmpresa;
    		filter.cdRepresentante = pedido.cdRepresentante;
    		filter.nuPedido = pedido.nuPedido;
    		filter.flOrigemPedido = pedido.flOrigemPedido;
    		try (Statement st = getDriver().getStatement();
    				ResultSet rsItemNfce = st.executeQuery(ItemNfceDbxDao.getInstance().findAllByExampleSql(filter))) {
    			if (rsItemNfce.isBeforeFirst()) {
    				int count = ItemNfceDbxDao.getInstance().countByExample(filter);
    			SyncUtil.writeLine(cbas, Convert.toString(count));
    				writeDados(ItemNfce.TABLE_NAME, rsItemNfce, cbas, true, null);
    		}
    	}
    	}
	}

	private void writeNfce(Pedido pedido, ByteArrayStream cbas) throws IOException, SQLException {
		SyncUtil.writeLine(cbas, "*NFCE*");
    	if (LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento() && !LavenderePdaConfig.usaNfePorReferencia && !LavenderePdaConfig.isConfigGradeProduto()) {
    		Nfce filter = new Nfce();
    		filter.cdEmpresa = pedido.cdEmpresa;
    		filter.cdRepresentante = pedido.cdRepresentante;
    		filter.nuPedido = pedido.nuPedido;
    		filter.flOrigemPedido = pedido.flOrigemPedido;
    		try (Statement st = getDriver().getStatement();
    				ResultSet rsNfce = st.executeQuery(NfceDbxDao.getInstance().findAllByExampleSql(filter))) {
    			if (rsNfce.isBeforeFirst()) {
    				int count = NfceDbxDao.getInstance().countByExample(filter);
	    		SyncUtil.writeLine(cbas, Convert.toString(count));
    				writeDados(Nfce.TABLE_NAME, rsNfce, cbas, true, null);
	    	}
    	}
    	}
	}

	private void writeVenctopPagamentoPedido(Pedido pedido, ByteArrayStream cbas) throws IOException, SQLException {
		SyncUtil.writeLine(cbas, "*VENCTOPAGAMENTOPEDIDO*");
    	if (LavenderePdaConfig.usaVencimentosAdicionaisBoleto) {
    		VenctoPagamentoPedido filter = new VenctoPagamentoPedido();
    		filter.cdEmpresa = pedido.cdEmpresa;
    		filter.cdRepresentante = pedido.cdRepresentante;
    		filter.nuPedido = pedido.nuPedido;
    		filter.flOrigemPedido = pedido.flOrigemPedido;
    		try (Statement st = getDriver().getStatement();
    				ResultSet rsVencto = st.executeQuery(VenctoPagamentoPedidoDao.getInstance().findAllByExampleSql(filter))) {
    			if (rsVencto.isBeforeFirst()) {
    				int count = VenctoPagamentoPedidoDao.getInstance().countByExample(filter);
	    		SyncUtil.writeLine(cbas, Convert.toString(count));
    				writeDados(VenctoPagamentoPedido.TABLE_NAME, rsVencto, cbas, true, null);
	    	}
    	}
    	}
	}

	private void writePedidoBoleto(Pedido pedido, ByteArrayStream cbas) throws IOException, SQLException {
		SyncUtil.writeLine(cbas, "*PEDIDOBOLETO*");
    	if (LavenderePdaConfig.usaGeracaoBoletoContingencia > 0) {
    		PedidoBoleto boletoFilter = new PedidoBoleto();
    		boletoFilter.cdEmpresa = pedido.cdEmpresa;
    		boletoFilter.cdRepresentante = pedido.cdRepresentante;
    		boletoFilter.flOrigemPedido = pedido.flOrigemPedido;
    		boletoFilter.nuPedido = pedido.nuPedido;
    		try (Statement st = getDriver().getStatement();
    				ResultSet rsBoleto = st.executeQuery(PedidoBoletoDao.getInstance().findAllByExampleSql(boletoFilter))) {
    			if (rsBoleto.isBeforeFirst()) {
    				int count = PedidoBoletoDao.getInstance().countByExample(boletoFilter);
	    		SyncUtil.writeLine(cbas, Convert.toString(count));
    				writeDados(PedidoBoleto.TABLE_NAME, rsBoleto, cbas, true, null);
    		}
    	}
    	}
	}

	private void writeItemNfe(Pedido pedido, ByteArrayStream cbas) throws IOException, SQLException {
		SyncUtil.writeLine(cbas, "*ITEMNFE*");
    	if (LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento()) {
    		ItemNfe itemNfeFilter = new ItemNfe();
    		itemNfeFilter.cdEmpresa = pedido.cdEmpresa;
    		itemNfeFilter.cdRepresentante = pedido.cdRepresentante;
    		itemNfeFilter.nuPedido = pedido.nuPedido;
    		itemNfeFilter.flOrigemPedido = pedido.flOrigemPedido;
    		try (Statement st = getDriver().getStatement();
    				ResultSet rsItemNfe = st.executeQuery(ItemNfeService.getInstance().findRSItemNfe(itemNfeFilter))) {
    			if (rsItemNfe.isBeforeFirst()) {
    				int count = ItemNfeDbxDao.getInstance().countByExample(itemNfeFilter);
	    		SyncUtil.writeLine(cbas, Convert.toString(count));
    				writeDados(ItemNfe.TABLE_NAME, rsItemNfe, cbas, true, null);
    		}
    	}
    	}
	}

	private void writeNfe(Pedido pedido, ByteArrayStream cbas) throws IOException, SQLException {
    	SyncUtil.writeLine(cbas, "*NFE*");
    	if (LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento()) {
    		Nfe nfeFilter = new Nfe();
    		nfeFilter.cdEmpresa = pedido.cdEmpresa;
    		nfeFilter.cdRepresentante = pedido.cdRepresentante;
    		nfeFilter.nuPedido = pedido.nuPedido;
    		nfeFilter.flOrigemPedido = pedido.flOrigemPedido;
    		try (Statement st = getDriver().getStatement();
    				ResultSet rsNfe = st.executeQuery(NfeService.getInstance().findByRowKeySql(nfeFilter.getRowKey()))) {
    		if (rsNfe.isBeforeFirst()) {
    				int count = NfeDao.getInstance().countByExample(nfeFilter);
    			SyncUtil.writeLine(cbas, Convert.toString(count));
    			writeDados(Nfe.TABLE_NAME, rsNfe, cbas, true, null);
    		}
    		}
    	}
	}
    		
	private void writeFotoPedido(Pedido pedido, ByteArrayStream cbas) throws IOException, SQLException {
    	SyncUtil.writeLine(cbas, "*FOTOPEDIDO*");
    	if (LavenderePdaConfig.usaFotoPedidoNoSistema) {
    		try (Statement st = getDriver().getStatement();
    				ResultSet rsFotoPedido = st.executeQuery(FotoPedidoService.getInstance().findRSFotoPedidoNaoEnviado(pedido))) {
    			if (rsFotoPedido.isBeforeFirst()) {
    				int count = FotoPedidoDbxDao.getInstance().countByExample(FotoPedidoService.getInstance().getDefaultFotoPedidoFilter(pedido));
    				SyncUtil.writeLine(cbas, Convert.toString(count));
    				writeDados(FotoPedido.TABLE_NAME, rsFotoPedido, cbas, true, null);
    	}
    		}
    	}
	}
    			
	private void writeFotoItemTroca(Pedido pedido, ByteArrayStream cbas) throws IOException, SQLException {
    	SyncUtil.writeLine(cbas, "*FOTOITEMTROCA*");
    	if (ValueUtil.isNotEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher)) {
    		try (Statement st = getDriver().getStatement();
    				ResultSet rsFotoItemTroca = st.executeQuery(FotoItemTrocaService.getInstance().findRSFotoItemTrocaNaoEnviado(pedido))) {
    			if (rsFotoItemTroca.isBeforeFirst()) {
    				int count = FotoItemTrocaDbxDao.getInstance().countByExample(FotoItemTrocaService.getInstance().getFotoItemTrocaPedidoInstance(pedido));
    			SyncUtil.writeLine(cbas, Convert.toString(count));
    				writeDados(FotoItemTroca.TABLE_NAME, rsFotoItemTroca, cbas, true, null);
    		}
    	}
    	}
	}

	private void writeItemPedidoAud(Pedido pedido, ByteArrayStream cbas) throws IOException, SQLException {
    	SyncUtil.writeLine(cbas, "*ITEMPEDIDOAUD*");
    	if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado || LavenderePdaConfig.usaCalculoVerbaComImpostoERentabilidade) {
    		try (Statement st = getDriver().getStatement();
    				ResultSet rsItemPedidoAud = st.executeQuery(ItemPedidoAudService.getInstance().findRSItemPedidoAud(pedido))) {
    			if (rsItemPedidoAud.isBeforeFirst()) {
    				int count = ItemPedidoAudDbxDao.getInstance().countByExample(ItemPedidoAudService.getInstance().createItemPedidoAudFilter(pedido));
    			SyncUtil.writeLine(cbas, Convert.toString(count));
    				writeDados(ItemPedidoAud.TABLE_NAME, rsItemPedidoAud, cbas, true, null);
    		}
    	}
    	}
	}

	private void writeInfoFretePedido(Pedido pedido, ByteArrayStream cbas) throws IOException, SQLException {
    	SyncUtil.writeLine(cbas, "*INFOFRETEPEDIDO*");
    	if (LavenderePdaConfig.isUsaTipoFretePedido()) {
    		InfoFretePedido infoFretePedidoFilter = new InfoFretePedido();
    		infoFretePedidoFilter.cdEmpresa = pedido.cdEmpresa;
    		infoFretePedidoFilter.cdRepresentante = pedido.cdRepresentante;
    		infoFretePedidoFilter.flOrigemPedido = pedido.flOrigemPedido;
    		infoFretePedidoFilter.nuPedido = pedido.nuPedido;
    		try (Statement st = getDriver().getStatement();
    				ResultSet rsInfoFretePedido = st.executeQuery(InfoFretePedidoDao.getInstance().findAllByExampleSql(infoFretePedidoFilter))) {
    			if (rsInfoFretePedido.isBeforeFirst()) {
    				int count = InfoFretePedidoDao.getInstance().countByExample(infoFretePedidoFilter);
    			SyncUtil.writeLine(cbas, Convert.toString(count));
    				writeDados(InfoFretePedido.TABLE_NAME, rsInfoFretePedido, cbas, true, null);
    		}
    	}
	}
	}

	private void writeItemPedidoGrade(Pedido pedido, ByteArrayStream cbas) throws IOException, SQLException {
    	SyncUtil.writeLine(cbas, "*ITENSPEDIDOGRADE*");
		if (LavenderePdaConfig.isConfigGradeProduto()) {
	    	ItemPedidoGrade itemPedidoGradeFilter = new ItemPedidoGrade();
	    	itemPedidoGradeFilter.cdEmpresa = pedido.cdEmpresa;
	    	itemPedidoGradeFilter.cdRepresentante = pedido.cdRepresentante;
	    	itemPedidoGradeFilter.flOrigemPedido = pedido.flOrigemPedido;
	    	itemPedidoGradeFilter.nuPedido = pedido.nuPedido;
	    	try (Statement st = getDriver().getStatement();
	    			ResultSet rsItensPedidoGrade = st.executeQuery(ItemPedidoGradeDbxDao.getInstance().findAllByExampleSql(itemPedidoGradeFilter))) {
	    		if (rsItensPedidoGrade.isBeforeFirst()) {
	    			int count = ItemPedidoGradeDbxDao.getInstance().countByExample(itemPedidoGradeFilter);
	    			SyncUtil.writeLine(cbas, Convert.toString(count));
	    			writeDados(ItemPedidoGrade.TABLE_NAME_ITEMPEDIDOGRADE, rsItensPedidoGrade, cbas, true, null);
	    		}
	    	}
    	}
	}

	private void writeItemPedidoRemessa(Pedido pedido, ByteArrayStream cbas) throws IOException, SQLException {
    	SyncUtil.writeLine(cbas, "*ITEMPEDIDOREMESSA*");
    	if (LavenderePdaConfig.usaControleEstoquePorRemessa) {
    		ItemPedidoRemessa itemPedidoRemessaFilter = ItemPedidoRemessaDbxDao.getInstance().getItemPedidoRemessaFilter(pedido);
    		try (Statement st = getDriver().getStatement();
    				ResultSet rsItemPedidoRemessa = st.executeQuery(ItemPedidoRemessaDbxDao.getInstance().findAllByPedidoRS(itemPedidoRemessaFilter))) {
	    		if (rsItemPedidoRemessa.isBeforeFirst()) {
	    			int count = ItemPedidoRemessaDbxDao.getInstance().countByExample(itemPedidoRemessaFilter);
	    			SyncUtil.writeLine(cbas, Convert.toString(count));
	    			writeDados(ItemPedidoRemessaDbxDao.TABLE_NAME, rsItemPedidoRemessa, cbas, true, null);
	    		}
    		}
    	}
	}

	private void writePagamentosPedido(Pedido pedido, ByteArrayStream cbas) throws IOException, SQLException {
    	SyncUtil.writeLine(cbas, "*PAGAMENTOPEDIDO*");
    	if (LavenderePdaConfig.usaMultiplosPagamentosParaPedido || LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido()) {
    		PagamentoPedido pagamentoPedidoFilter = PagamentoPedidoService.getInstance().getPagamentoPedidoFilter(pedido);
    		try (Statement st = getDriver().getStatement();
    				ResultSet rsPagamentoPedido = st.executeQuery(PagamentoPedidoDbxDao.getInstance().findAllByExampleSql(pagamentoPedidoFilter))) {
	    		if (rsPagamentoPedido.isBeforeFirst()) {
	    			int count = PagamentoPedidoDbxDao.getInstance().countByExample(pagamentoPedidoFilter);
	    			SyncUtil.writeLine(cbas, Convert.toString(count));
	    			writeDados(PagamentoPedido.TABLE_NAME, rsPagamentoPedido, cbas, true, null);
	    		}
    		}
    	}
	}

	private void writeParcelasPedido(Pedido pedido, ByteArrayStream cbas) throws IOException, SQLException {
    	SyncUtil.writeLine(cbas, "*PARCELASPEDIDO*");
    	ParcelaPedido parcelaPedidoFilter = new ParcelaPedido();
    	parcelaPedidoFilter.cdEmpresa = pedido.cdEmpresa;
    	parcelaPedidoFilter.cdRepresentante = pedido.cdRepresentante;
    	parcelaPedidoFilter.flOrigemPedido = pedido.flOrigemPedido;
    	parcelaPedidoFilter.nuPedido = pedido.nuPedido;
    	try (Statement st = getDriver().getStatement();
    			ResultSet rsParcelasPedido = st.executeQuery(ParcelaPedidoPdbxDao.getInstance().findAllByExampleSql(parcelaPedidoFilter))) {
	    	if (rsParcelasPedido.isBeforeFirst()) {
	    		int count = ParcelaPedidoPdbxDao.getInstance().countByExample(parcelaPedidoFilter);
	    		SyncUtil.writeLine(cbas, Convert.toString(count));
		    	writeDados(ParcelaPedido.TABLE_NAME, rsParcelasPedido, cbas, true, null);
	    	}
    	}
	}

	public void enviaArquivoToErp(String pathDados, Vector logList, boolean lanceException, String pathDadosServidor) throws Exception {
		abreConexao();
		try {
    		int listSize = logList.size();
    		String fileName;
    		for (int i = 0; i < listSize; i++) {
    			fileName = (String) logList.items[i];
    			try {
    				int index = LogSync.info("Enviando " + fileName);
    				sendFileToPath(pathDados, fileName, true, pathDadosServidor);
    				LogSync.replaceAtIndex("Enviando " + fileName, index, ColorUtil.sucessColor);
    			} catch (Throwable e) {
    				LogSync.error(fileName + "-" + e.getMessage());
    				if (lanceException) {
    					throw e;
    				}
    				LogPdaService.getInstance().logSyncError(MessageUtil.getMessage(Messages.TC2WEB_ERRO_ENVIO_ARQUIVO, fileName) + e.getMessage());
    			}
    		}
    	} finally {
    		fechaConexao();
    	}
	}

	public void enviaArquivoToErp(String pathDados, Vector arquivoList, boolean lanceException) throws Exception {
		abreConexao();
		try {
			int listSize = arquivoList.size();
			String fileName;
			for (int i = 0; i < listSize; i++) {
				fileName = (String) arquivoList.items[i];
				try {
					envieFile(pathDados, fileName, true, true);
				} catch (Throwable e) {
					if (lanceException) {
						throw e;
					}
				}
			}
		} finally {
			fechaConexao();
		}
	}
	
	public void enviaBancoDadosToErp(String serverFolderPath) throws Exception {
		LogSync.logSection("Inicio envio database para servidor");
		String fileName = AppConfig.DATABASE_NAME;
		LogSyncTimer timer = new LogSyncTimer("Processando").newLogOnFinish();
		String pathDados = DatabaseUtil.getDriverPath();
		try (ByteArrayStream cbas = ZipFileUtil.zipFileToBa(Convert.appendPath(pathDados, fileName))) {
		abreConexao();
			sendFileToPath(cbas, fileName, true, serverFolderPath);
		} catch (Exception e) {
			ExceptionUtil.handle(e);
			throw new Exception(e.getMessage());
		} finally {
			fechaConexao();
			timer.finish();
			LogSync.logSection("Fim envio database para servidor");
		}
	}
	
	public ByteArrayStream enviaBancoDadosRecoverToWeb(String serverFolderPath) throws Exception {
		String fileName = AppConfig.DATABASE_NAME_RECOVER;
		String pathDados = DatabaseUtil.getDriverPath();
		try (ByteArrayStream cbas = ZipFileUtil.zipFileToBa(Convert.appendPath(pathDados, fileName))) {
			abreConexao();
			return httpSync.sendFileRecoverToPath(cbas, fileName, true, serverFolderPath);
		} catch (Exception e) {
			ExceptionUtil.handle(e);
			throw new Exception(e.getMessage());
		} finally {
			fechaConexao();
		}
	}

	private void envieNovoClienteServidor() throws Exception {
		if (LavenderePdaConfig.usaValidacaoCPFCNPJBaseWeb) {
			LogSync.info(Messages.TC2WEB_ENVIO_NOVOCLIENTE);
			ConfigIntegWebTc configIntegWebTcFilter = new ConfigIntegWebTc();
			configIntegWebTcFilter.dsTabela = NovoCliente.TABLE_NAME;
			ConfigIntegWebTc webTc = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
			if (webTc.isAtivo()) {
				envieDadosTable(NovoCliente.TABLE_NAME);
			}
			configIntegWebTcFilter.dsTabela = NovoCliEndereco.TABLE_NAME;
			webTc = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
			if (webTc.isAtivo()) {
				envieDadosTable(NovoCliEndereco.TABLE_NAME);
			}
			configIntegWebTcFilter.dsTabela = FotoNovoCliente.TABLE_NAME;
			webTc = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
			if (webTc.isAtivo()) {
				envieDadosTable(FotoNovoCliente.TABLE_NAME);
			}
			LogSync.info(Messages.TC2WEB_FIM_ENVIO_NOVOCLIENTE);
		}
	}
	public void envieDapLaudoAtuaServidor(boolean enviaFotosAssinatura ) throws Exception {
		abreConexao();
		try {
			inicioEnvio();
			LogSync.info(Messages.TC2WEB_ENVIO_DAPLAUDOATUA);
			ConfigIntegWebTc configIntegWebTcFilter = new ConfigIntegWebTc();
			configIntegWebTcFilter.dsTabela = DapLaudoAtua.TABLE_NAME;
			ConfigIntegWebTc webTc = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
			if (webTc.isAtivo()) {
				if (enviaFotosAssinatura) {
					SyncManager.enviaImagensDapLaudo(paramsSync);
				}
				envieDadosTable(DapLaudoAtua.TABLE_NAME);
			}
			fimEnvio();
		} finally {
			fechaConexao();
			LogSync.info(Messages.TC2WEB_FIM_ENVIO_DAPLAUDOATUA);
		}
	}
	
	public void envieImagens(String pathName, Vector imageList, String tableName, boolean atualizaImagemAposEnvio) throws Exception {
		imagemEnvioErroList = new Vector();
		imagemEnvioSucessoList = new Vector();
		super.envieImagens(pathName, imageList);
		if (VisitaFotoDao.TABLE_NAME.equals(tableName) && atualizaImagemAposEnvio) {
			atualizaImagensAposEnvio();
		}
		if (FotoPedido.TABLE_NAME.equals(tableName) && atualizaImagemAposEnvio) {
			atualizaImagensFotoPedidoAposEnvio();
		}
		if (FotoPesquisaMercado.TABLE_NAME.equals(tableName) && atualizaImagemAposEnvio) {
			atualizaImagensFotoPesquisaMercadoAposEnvio();
		}
		if (FotoPesqMerProdConc.TABLE_NAME.equals(tableName) && atualizaImagemAposEnvio) {
			atualizaImagensFotoPesqMerProdConcAposEnvio();
		}
		if (PesquisaRespAppFotoPdbxDao.TABLE_NAME.equals(tableName) && atualizaImagemAposEnvio) {
			atualizaImagensPesquisaRespAppFotoAposEnvio();
		}
		if (DapLaudoAtua.TABLE_NAME.equals(tableName)) {
			validateImagensComErros();
			deleteImagensLaudo();
		}
		if (FotoItemTroca.TABLE_NAME.equals(tableName)) {
			atualizaImagensFotoItemTrocaAposEnvio();
		}
		imagemEnvioErroList.removeAllElements();
		imagemEnvioSucessoList.removeAllElements();
	}

	private void validateImagensComErros() {
		if (ValueUtil.isNotEmpty(imagemEnvioErroList)) {
			String nameImagems = imagemEnvioErroList.toString(";");
			LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_DAPLAUDO, MessageUtil.getMessage(Messages.SYNC_IMAGES_DAPLAUDOATUA_ERRO_FILE, nameImagems));
			throw new RuntimeException(MessageUtil.getMessage(Messages.SYNC_IMAGES_DAPLAUDOATUA_ERRO_FILE, ""));
		}
	}

	private void deleteImagensLaudo() {
		if (ValueUtil.isNotEmpty(imagemEnvioSucessoList)) {
			for (int i = 0; i < imagemEnvioSucessoList.size(); i++) {
				String nameImagem  = (String) imagemEnvioSucessoList.items[i];
				FileUtil.deleteFile(DapLaudo.getImagePath() + "/" + nameImagem);
			}
			String nameImagems = imagemEnvioSucessoList.toString(";");
			LogPdaService.getInstance().info(LogPda.LOG_CATEGORIA_DAPLAUDO, MessageUtil.getMessage(Messages.SYNC_IMAGES_DAPLAUDOATUA_SUCESSO_FILES, nameImagems));
		}
	}

	private void atualizaImagensFotoPesqMerProdConcAposEnvio() throws SQLException {
		if (ValueUtil.isNotEmpty(imagemEnvioErroList)) {
			FotoPesqMerProdConcService.getInstance().atualizaFotoAposEnvio(imagemEnvioErroList, ValueUtil.VALOR_NAO);
		}
		if (ValueUtil.isNotEmpty(imagemEnvioSucessoList)) {
			FotoPesqMerProdConcService.getInstance().atualizaFotoAposEnvio(imagemEnvioSucessoList, ValueUtil.VALOR_SIM);
		}
	}

	private void atualizaImagensFotoPesquisaMercadoAposEnvio() throws SQLException {
		if (ValueUtil.isNotEmpty(imagemEnvioErroList)) {
			FotoPesquisaMercadoService.getInstance().atualizaFotoAposEnvio(imagemEnvioErroList, ValueUtil.VALOR_NAO);
		}
		if (ValueUtil.isNotEmpty(imagemEnvioSucessoList)) {
			FotoPesquisaMercadoService.getInstance().atualizaFotoAposEnvio(imagemEnvioSucessoList, ValueUtil.VALOR_SIM);
		}
	}

	private void atualizaImagensFotoPedidoAposEnvio() throws SQLException {
		if (ValueUtil.isNotEmpty(imagemEnvioErroList)) {
			FotoPedidoService.getInstance().atualizaFotoAposEnvio(imagemEnvioErroList, ValueUtil.VALOR_NAO);
		}
		if (ValueUtil.isNotEmpty(imagemEnvioSucessoList)) {
			FotoPedidoService.getInstance().atualizaFotoAposEnvio(imagemEnvioSucessoList, ValueUtil.VALOR_SIM);
		}
	}

	private void atualizaImagensFotoItemTrocaAposEnvio() throws SQLException {
		if (ValueUtil.isNotEmpty(imagemEnvioErroList)) {
			FotoItemTrocaService.getInstance().atualizaFotoItemTrocaAposEnvio(imagemEnvioErroList, ValueUtil.VALOR_NAO);
		}
		if (ValueUtil.isNotEmpty(imagemEnvioSucessoList)) {
			FotoItemTrocaService.getInstance().atualizaFotoItemTrocaAposEnvio(imagemEnvioSucessoList, ValueUtil.VALOR_SIM);
		}
	}

	public void atualizaImagensAposEnvio() throws SQLException {
		if (ValueUtil.isNotEmpty(imagemEnvioErroList)) {
			VisitaFotoService.getInstance().atualizaFotoAposEnvio(imagemEnvioErroList, ValueUtil.VALOR_NAO);
		} else if (ValueUtil.isNotEmpty(imagemEnvioSucessoList)) {
			VisitaFotoService.getInstance().atualizaFotoAposEnvio(imagemEnvioSucessoList, ValueUtil.VALOR_SIM);
		}
	}

	private void atualizaImagensPesquisaRespAppFotoAposEnvio() throws SQLException {
		if (ValueUtil.isNotEmpty(imagemEnvioErroList)) {
			PesquisaRespAppFotoService.getInstance().atualizaFotoAposEnvio(imagemEnvioErroList, ValueUtil.VALOR_NAO);
		}
		if (ValueUtil.isNotEmpty(imagemEnvioSucessoList)) {
			PesquisaRespAppFotoService.getInstance().atualizaFotoAposEnvio(imagemEnvioSucessoList, ValueUtil.VALOR_SIM);
		}
	}

	protected void aposEnvioImagemErro(String imageName) {
		imagemEnvioErroList.addElement(imageName);
	}

	protected void aposEnvioImagem(String imageName) {
		imagemEnvioSucessoList.addElement(imageName);
	}

	@Override
	protected boolean validateWriteDados(String tableName, String values, String columns) {
		if (NovoCliente.TABLE_NAME.equals(tableName) && LavenderePdaConfig.isCadastroCoordenadasGeograficasNovoClienteAutomatico() && LavenderePdaConfig.obrigaCadCoordenadasGeograficasNovoCliente) {
			return NovoClienteService.getInstance().validaNovoClienteCoordenadaCadastradaEnvioServidor(tableName, values, columns);
		}
		return super.validateWriteDados(tableName, values, columns);
	}
	
	public void sendBackupApp(String file) throws Exception {
		abreConexao();
		try {
			int index = LogSync.info(Messages.TC2WEB_ENVIANDO_BKP + file);
			httpSync.sendBackupApp(file);
			LogSync.replaceAtIndex(Messages.TC2WEB_BKP + file + Messages.TC2WEB_BKP_ENVIADO_SUCESSO, index, ColorUtil.sucessColor);
		} catch (Throwable e) {
			String message = Messages.TC2WEB_BKP_ERRO_ENVIO + e.getMessage();
			LogSync.replace(message, LogSync.COR_ERROR);
			throw new ValidationException(message);
		} finally {
			fechaConexao();
			LogPdaService.getInstance().log(LogPda.LOG_NIVEL_INFO, LogPda.LOG_CATEGORIA_BACKUP, MessageUtil.getMessage(Messages.BACKUP_MSG_ENVIO_LOGPDA, new Object[] {file}));
		}
	}
	
	public int verificaEntidadePendenteEnvio(String tableName) throws SQLException {
		return getDriver().getInt("SELECT count(1) as qtde FROM " + tableName + " WHERE FLTIPOALTERACAO != ''");
	}
	
	@Override
	protected Vector pegaDadosEnvioServidor(String tableName, ByteArrayStream cbasRetorno) throws IOException, SQLException {
		if (tableName.equalsIgnoreCase(NovoCliente.TABLE_NAME) && LavenderePdaConfig.usaValidacaoCPFCNPJBaseWeb) {
			return writeDados(tableName, NovoClientePdbxDao.getSqlDadosEnvioServidor(), cbasRetorno);
		} else if (tableName.equalsIgnoreCase(NovoCliEndereco.TABLE_NAME) && LavenderePdaConfig.usaValidacaoCPFCNPJBaseWeb) {
			return writeDados(tableName, NovoCliEnderecoDao.getSqlDadosEnvioServidor(), cbasRetorno);
		} else if (tableName.equalsIgnoreCase(FotoNovoCliente.TABLE_NAME) && LavenderePdaConfig.usaValidacaoCPFCNPJBaseWeb) {
			return writeDados(tableName, FotoNovoClienteDbxDao.getSqlDadosEnvioServidor(), cbasRetorno);
		}else if (tableName.equalsIgnoreCase(PesquisaApp.TABLE_NAME)) {
			return writeDados(tableName, PesquisaAppPdbxDao.getSqlDadosEnvioServidor(), cbasRetorno);
		}else if (tableName.equalsIgnoreCase(PesquisaRespApp.TABLE_NAME)) {
			return writeDados(tableName, PesquisaRespAppPdbxDao.getSqlDadosEnvioServidor(), cbasRetorno);
		} else if (tableName.equalsIgnoreCase(DapLaudoAtua.TABLE_NAME)) {
			return writeDados(tableName, DapLaudoAtuaDbxDao.getSqlDadosEnvioServidor(), cbasRetorno);
		}
		return super.pegaDadosEnvioServidor(tableName, cbasRetorno);
	}

	private Vector writeDados(String tableName, String sql, ByteArrayStream cbasRetorno) throws SQLException {
		try (Statement st = getDriver().getStatement();
				ResultSet rs = st.executeQuery(sql)) {
			return writeDados(tableName, rs, cbasRetorno, false, null);
		}
	}
	
	private void montaColetaInfosPda() {
		try {
			ColetaInfosPda coleta = new ColetaInfosPda();
	    	coleta.vlPctBateria = Vm.getRemainingBattery();
	    	coleta.vlPctBateriaInicial = coleta.vlPctBateria;
	    	coleta.hrSyncInicial = TimeUtil.getCurrentTimeHHMM();
	    	coleta.hrSync = TimeUtil.getCurrentTimeHHMM();
	    	coleta.dtAlteracao = DateUtil.getCurrentDate();
	    	coleta.hrAlteracao = coleta.hrSync;
	    	coleta.flTipoAlteracao = ColetaInfosPda.FLTIPOALTERACAO_INSERIDO;
	    	coleta.flGpsAtivo = StatusGpsService.getInstance().isPlataformaSuportadaGps() && PontoGpsService.getInstance().verificaGpsLigado() ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
			ColetaInfosPdaService.getInstance().insertOrUpdateColeta(coleta);
		} catch (Throwable e) {
			throw new ApplicationException(e.getMessage());
		}
	}

}
