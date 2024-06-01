package br.com.wmw.lavenderepda.sync;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.json.simple.parser.JSONParser;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.BackupService.BackupFile;
import br.com.wmw.framework.business.service.BackupService.BackupFileDTO;
import br.com.wmw.framework.business.service.CargaProgramadaService;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.business.service.LogSyncBackgroundService;
import br.com.wmw.framework.business.service.PersonalizacaoService;
import br.com.wmw.framework.config.AppConfig;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.AtualizacaoBdDbxDao;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.MensagemExcecDbxDao;
import br.com.wmw.framework.interfaces.FileProperties;
import br.com.wmw.framework.notification.Notification;
import br.com.wmw.framework.notification.NotificationManager;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.sync.SyncInfo;
import br.com.wmw.framework.sync.data.web2tc.SyncWeb2TcHttp;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.sync.transport.http.HttpRequest;
import br.com.wmw.framework.sync.transport.http.HttpResponse;
import br.com.wmw.framework.sync.transport.http.HttpSync;
import br.com.wmw.framework.timer.LogSyncTimer;
import br.com.wmw.framework.util.DatabaseUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.JsonFactory;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TCStreamUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.framework.util.ZipFileUtil;
import br.com.wmw.framework.util.ZipUtil;
import br.com.wmw.lavenderepda.LavendereConfig;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CatalogoExterno;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ClienteInativacao;
import br.com.wmw.lavenderepda.business.domain.ConfigIntegWebTc;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.DapLaudo;
import br.com.wmw.lavenderepda.business.domain.DivulgaInfo;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.EstoqueRep;
import br.com.wmw.lavenderepda.business.domain.FotoClienteErp;
import br.com.wmw.lavenderepda.business.domain.FotoProduto;
import br.com.wmw.lavenderepda.business.domain.FotoProdutoEmp;
import br.com.wmw.lavenderepda.business.domain.FotoProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.ItemNfe;
import br.com.wmw.lavenderepda.business.domain.ItemNfeReferencia;
import br.com.wmw.lavenderepda.business.domain.LogApp;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.domain.MenuCatalogo;
import br.com.wmw.lavenderepda.business.domain.Nfe;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoBoleto;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.ReagendaAgendaVisita;
import br.com.wmw.lavenderepda.business.domain.RemessaEstoque;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.RepresentanteEmp;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.domain.TituloFinanceiro;
import br.com.wmw.lavenderepda.business.domain.UsuarioLavendere;
import br.com.wmw.lavenderepda.business.domain.UsuarioPda;
import br.com.wmw.lavenderepda.business.domain.UsuarioPdaRep;
import br.com.wmw.lavenderepda.business.domain.UsuarioRelRep;
import br.com.wmw.lavenderepda.business.domain.VerbaSaldo;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.domain.dto.ListaLeadsDTO;
import br.com.wmw.lavenderepda.business.domain.dto.RetornoPedidoDTO;
import br.com.wmw.lavenderepda.business.domain.dto.SendValidaSenhaDTO;
import br.com.wmw.lavenderepda.business.domain.dto.SerieNfeDTO;
import br.com.wmw.lavenderepda.business.service.CatalogoExternoService;
import br.com.wmw.lavenderepda.business.service.ClienteInativacaoService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.ConfigIntegWebTcService;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.ConfigNotificacaoService;
import br.com.wmw.lavenderepda.business.service.DapLaudoService;
import br.com.wmw.lavenderepda.business.service.DivulgaInfoService;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.FotoClienteErpService;
import br.com.wmw.lavenderepda.business.service.FotoProdutoEmpService;
import br.com.wmw.lavenderepda.business.service.FotoProdutoGradeService;
import br.com.wmw.lavenderepda.business.service.FotoProdutoService;
import br.com.wmw.lavenderepda.business.service.ItemNfeReferenciaService;
import br.com.wmw.lavenderepda.business.service.ItemNfeService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.LavendereCargaProgramadaService;
import br.com.wmw.lavenderepda.business.service.LogAppService;
import br.com.wmw.lavenderepda.business.service.LogPdaService;
import br.com.wmw.lavenderepda.business.service.MenuCatalogoService;
import br.com.wmw.lavenderepda.business.service.NfeService;
import br.com.wmw.lavenderepda.business.service.PedidoBoletoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.ProducaoProdService;
import br.com.wmw.lavenderepda.business.service.UsuarioPdaService;
import br.com.wmw.lavenderepda.business.service.VisitaService;
import br.com.wmw.lavenderepda.business.validation.EnvioDadosJsonException;
import br.com.wmw.lavenderepda.business.validation.EnvioDadosNfeException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.AtualizacaoBdLavendereDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FotoClienteDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemNfeDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemNfeReferenciaDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MensagemExcecLavendereDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NfeDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoBoletoDao;
import br.com.wmw.lavenderepda.notification.LavendereNotificationConstants;
import br.com.wmw.lavenderepda.util.MediaUtil;
import totalcross.io.ByteArrayStream;
import totalcross.io.File;
import totalcross.io.IOException;
import totalcross.io.LineReader;
import totalcross.json.JSONArray;
import totalcross.json.JSONException;
import totalcross.json.JSONFactory;
import totalcross.json.JSONObject;
import totalcross.net.URI;
import totalcross.sql.Types;
import totalcross.sys.Convert;
import totalcross.sys.Time;
import totalcross.sys.Vm;
import totalcross.ui.image.Image;
import totalcross.util.BigDecimal;
import totalcross.util.Date;
import totalcross.util.Hashtable;
import totalcross.util.IntHashtable;
import totalcross.util.Vector;
import totalcross.util.zip.ZLibStream;

public class LavendereWeb2Tc extends SyncWeb2TcHttp {

	public static final String ACTION_GET_FOTO_PRODUTO = "getFotoProduto";
	public static final String ACTION_GET_FOTO_CLIENTEERP = "getFotoClienteErp";
	public static final String ACTION_GET_FOTO_DIVULGACAO = "getFotoDivulgacao";
	public static final String ACTION_GET_FOTO_PRODUTO_GRADE = "getFotoProdutoGrade";
	public static final String ACTION_GET_CATALOGO_EXTERNO = "getCatalogoExterno";
	public static final String ACTION_GET_VIDEO_PRODUTO = "getVideoProduto";
	public static final String ACTION_GET_VIDEO_PRODUTO_GRADE = "getVideoProdutoGrade";
	
	private static final String ACTION_TESTA_CONEXAOPDA = "testaConexaoPda";
	private static final String ACTION_DATA_SERVIDOR = "dataServidor";
	private static final String ACTION_LINK_DIAWI = "linkDiawi";
	private static final String ACTION_TIPO_ATUALIZACAO_APP = "getTipoAtualizacaoAppNecessario";
	private static final String ACTION_UPDATE_ESTOQUE = "getEstoqueUpdated";
	private static final String ACTION_UPDATE_PROCUCAOPRODREP = "getProducaoProdRep";
	private static final String ACTION_GERA_RESERVA_ESTOQUE = "geraReservaEstoque";
	private static final String ACTION_GERA_PDF_PEDIDO = "geraPdfPedido";
	private static final String ACTION_GERA_CATALOGO_PRODUTO = "geraCatalogoProduto";
	private static final String ACTION_GERA_RATEIO_PRODUCAOPROD = "geraRateioProducaoProd";
	private static final String ACTION_INATIVA_RESERVA_ESTOQUE = "inativaReservaEstoque";
	private static final String ACTION_LIBERA_PEDIDO_PENDENTE = "liberaPedidoPendente";
	private static final String ACTION_DESBLOQUEAR_REMESSA_ESTOQUE = "desbloquearRemessaEstoque";
	private static final String ACTION_VALIDA_RESERVA_ESTOQUE = "validaReservaEstoque";
	private static final String ACTION_VERIFICA_RESERVA_ESTOQUE = "verificaReservaCadastradaServidor";
	private static final String ACTION_VERIFICA_PEDIDO_ENVIADO_SERVIDOR = "verificaPedidoEnviadoServidor";
	private static final String ACTION_VALIDA_INATIVACAO_RESERVA_ESTOQUE = "validaInativacaoReservaEstoque";
	private static final String ACTION_CANCELA_PEDIDO_PENDENTE = "cancelaPedido";
	private static final String ACTION_REVALIDA_RESTRICAO_PRODUTO_ITENSPEDIDO = "revalidaRestricaoProdutoItensPedido";
	private static final String ACTION_GET_SERIE_NFE = "getNuNfeAndSerie";
	private static final String ACTION_GET_CONFIGACESSOSISTEMA = "getConfigAcessoSistema";
	private static final String ACTION_RECEBE_RETORNO_PEDIDO = "recebeRetornoPedido";
	private static final String ACTION_VALIDA_PERIODO_DEVOLUCAO_ESTOQUE = "validaPeriodoDevolucaoEstoque";
	private static final String ACTION_INATIVA_ESTOQUE_BY_REMESAS = "inativaEstoquesByRemessas";
	private static final String ACTION_GERA_REMESSA_ESTOQUE = "geraRemessaEstoque";
	private static final String ACTION_GERA_DEVOLUCAO_ESTOQUE = "geraDevolucaoEstoque";
	private static final String ACTION_VALIDA_CPFCNPJ_NOVO_CLIENTE = "validaCpfCnpjNovoCliente";
	private static final String ACTION_ENVIA_VERBASALDO_ABERTO = "enviaVerbaSaldoAberto"; 
	private static final String ACTION_HAS_TABLE_UPDATES = "hasTableUpdates";
	private static final String PEDIDO_RETORNO_BACKGROUND = "background";
	private static final String PEDIDO_RETORNO_PADRAO = "padrao";
	private static final String ACTION_VALIDA_LOGIN_ONLINE = "validaLoginOnline";
	private static final String ACTION_VERIFICA_USUARIO_EQUIPAMENTO = "verificausuarioequipamento";
	private static final String ACTION_UPDATE_VERBASALDO = "updateVerbaSaldo";
	private static final String ACTION_NEXT_NU_BOLETO = "nextNuBoleto";
	private static final String ACTION_GET_NU_PROTOCOLO_NFE = "getNuProtocoloNfe";
	private static final String ACTION_CONSULTA_CEP_ONLINE = "consultaCepOnline";
	private static final String ACTION_CONSULTA_CNPJ_ONLINE = "consultaws/cnpj";
	private static final String ACTION_ORDENA_AGENDA = "ordenaAgenda";
	private static final String ACTION_RECEBE_FOTOS_CARGA_INICIAL = "getFotosCargaInicial";
	private static final String RETORNO_ERRO_RECEBER_AGENDA = "erroEnvioAgenda";
	private static final String  ACTION_GET_INFORMACAO_COMPLEMENTAR_PEDIDO_WEBSERVICE_SANKHYA = "getInformacoesComplementaresPedidoWebserviceSankhya";
	public static final String SERVER_SEM_CONEXAO = "semConexao";
	private static final String ACTION_GERAR_QRCODE = "gerarQrCode";
	private static final String ACTION_FUSO_HORARIO = "fusoHorario";
	private static final String ACTION_GERA_PDF_DAP = "geraPdfDap";
	private static final String ACTION_GET_ESTOQUE_FILIAIS = "getEstoqueFiliais";
	private static final String ACTION_GET_WTOOLS = "get-wtools";
	private static final String ACTION_GERA_PDF_PEDIDO_BOLETO = "geraPdfPedidoBoleto";
	private static final String ACTION_GERA_PDF_NFE_PEDIDO = "geraPdfNfePedido";
	private static final String ACTION_GERA_PDF_BOLETO_PEDIDO = "geraPdfBoletoPedido";
	private static final String ACTION_POST_COODERNADAS_CLIENTE = "mapa/postmapa";
	private static final String ACTION_GERA_PDF_CATALOGO_ITENS = "geraPdfCatalogoItens";
	private static final String ACTION_GET_SYNC_INFO_FOTOMENUCATALOGO = "getSyncInfoFotoMenuCatalogo";
	private static final String ACTION_GET_FOTO_MENU_CATALOGO = "getFotoMenuCatalogo";
	private static final String ACTION_VERIFICA_ATUALIZACOES_FOTO_MENU_CATALOGO_DISPONIVEIS = "verificaAtualizacoesFotoMenuCatalogoDisponiveis";
	private static final String ACTION_GET_PLACES_NEARBY = "placesApi/getPlacesNearby";


	public boolean ignoreAntesEDepoisReceberDados;
	public boolean syncCargaInicialAppSemUsuario;
	private boolean existeConfigNotificacao;


	public LavendereWeb2Tc() throws SQLException {
		this(HttpConnectionManager.getDefaultParamsSync());
	}
	
	public LavendereWeb2Tc(ParamsSync ps) throws SQLException {
		super(ps, LogPdaService.getInstance());
	}
	

	@Override
	protected void onSyncError(Throwable e) throws SQLException {
		super.onSyncError(e);
		if (syncCargaInicialAppSemUsuario) {
			ConfigInternoService.getInstance().addValueGeral(ConfigInterno.CARGADADOSAPPSEMUSUARIOPENDENTE, ValueUtil.VALOR_SIM);
		}
	}

	public String recebaDateTimeServidor() throws Exception {
		abreConexao();
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(paramsSync.httpWeb2TcUrl);
		strBuf.append(ACTION_DATA_SERVIDOR);
		strBuf.append("/");
		httpSync.addParamUrl(strBuf, paramsSync.cdSessao);
		strBuf.append("/");
		httpSync.addParamUrl(strBuf, getCdUsuario());
		return httpSync.executeHttpAsString(strBuf.toString());
	}

	public String linkDiawi() throws Exception {
		abreConexao();
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("/public/service/");
		strBuf.append(ACTION_LINK_DIAWI);
		strBuf.append("/");
		httpSync.addParamUrl(strBuf, getCdUsuario());
		return httpSync.executeHttpAsString(strBuf.toString());
	}
	
	private String getCdUsuario() throws SQLException {
		Vector usuarios = UsuarioPdaService.getInstance().findAll();
		UsuarioPda usuarioPda = (UsuarioPda) usuarios.items[0];
		return usuarioPda.cdUsuario;
	}

	public String getTipoAtualizacaoAppNecessario() throws Exception {
		abreConexao();
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(paramsSync.httpWeb2TcUrl);
		strBuf.append(ACTION_TIPO_ATUALIZACAO_APP);
		strBuf.append("/");
		httpSync.addParamUrl(strBuf, paramsSync.cdSessao);
		strBuf.append("/");
		httpSync.addParamUrl(strBuf, Session.getCdUsuario());
		strBuf.append("/");
		httpSync.addParamUrl(strBuf, LavendereConfig.getInstance().version);
		return httpSync.executeHttpAsString(strBuf.toString());
	}
	
	@Override
	protected Vector prioritizeTablesCarimbosToSync(Vector keys) {
		if (syncCargaInicialAppSemUsuario) {
			return prioritizeTableCargaDefault(keys);
		}
		return keys;
	}

	private Vector prioritizeTableCargaDefault(Vector keys) {
		Vector priorityList = new Vector(8);
		setPriority(keys, priorityList, Empresa.TABLE_NAME);
		setPriority(keys, priorityList, SupervisorRep.TABLE_NAME);
		setPriority(keys, priorityList, RepresentanteEmp.TABLE_NAME);
		setPriority(keys, priorityList, Representante.TABLE_NAME);
		setPriority(keys, priorityList, UsuarioRelRep.TABLE_NAME);
		setPriority(keys, priorityList, UsuarioPdaRep.TABLE_NAME);
		setPriority(keys, priorityList, UsuarioPda.TABLE_NAME);
		setPriority(keys, priorityList, UsuarioLavendere.TABLE_NAME);
		if (!priorityList.isEmpty()) {
			return VectorUtil.concatVectors(priorityList, keys);
		}
		return keys;
	}

	private void setPriority(Vector keys, Vector priorityList, String tableName) {
		if (keys.removeElement(tableName)) {
			priorityList.addElement(tableName);
		}
	}
	
	public String getEstoqueServidorAndUpdatePda(String cdEmpresa, String cdRepresentante, String cdProduto, String cdLocalEstoque) throws Exception {
		return getEstoqueServidorAndUpdatePda(cdEmpresa, cdRepresentante, cdProduto, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, cdLocalEstoque);
	}
	public String getEstoqueServidorAndUpdatePda(String cdEmpresa, String cdRepresentante, String cdProduto, String cdItemGrade1, String cdItemGrade2, String cdItemGrade3, String cdLocalEstoque) throws Exception {
		abreConexao();
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(paramsSync.httpWeb2TcUrl);
		strBuf.append(ACTION_UPDATE_ESTOQUE);
		strBuf.append("/");
		JSONObject json = new JSONObject();
		json.put("cdPda", Session.getCdUsuario());
		json.put("cdEmpresa", cdEmpresa);
		json.put("cdRepresentante", cdRepresentante);
		json.put("cdProduto", cdProduto);
		json.put("cdItemGrade1", cdItemGrade1);
		json.put("cdItemGrade2", cdItemGrade2);
		json.put("cdItemGrade3", cdItemGrade3);
		json.put("cdLocalEstoque", cdLocalEstoque);
		String novoEstoque = null;
		try {
			ByteArrayStream basRetorno = httpSync.executePostBy(strBuf.toString(), json.toString(), HttpSync.CONTENTTYPE_JSON, false);
			LineReader lr = new LineReader(basRetorno);
			novoEstoque = lr.readLine();
		} finally {
			fechaConexao();
		}
		if (novoEstoque == null) {
			throw new ValidationException(Messages.WEB2TC_ERRO_ESTOQUE);
		}
		EstoqueService.getInstance().updateEstoqueByEstoqueOnLine(cdProduto, cdItemGrade1, cdItemGrade2, cdItemGrade3, ValueUtil.getDoubleValue(novoEstoque));
		return novoEstoque;
	}

	public String getProducaoProdRep(String cdEmpresa, String cdRepresentante, String cdProduto, String dtInicial, String dtFinal) throws Exception {
		abreConexao();
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(paramsSync.httpWeb2TcUrl);
		strBuf.append(ACTION_UPDATE_PROCUCAOPRODREP);
		strBuf.append("/");
		JSONObject json = new JSONObject();
		json.put("cdPda", Session.getCdUsuario());
		json.put("cdEmpresa", cdEmpresa);
		json.put("cdRepresentante", cdRepresentante);
		json.put("cdProduto", cdProduto);
		json.put("dtInicial", dtInicial);
		json.put("dtFinal", dtFinal);
		String novaProducaoProd = null;
		try {
			ByteArrayStream basRetorno = httpSync.executePostBy(strBuf.toString(), json.toString(), HttpSync.CONTENTTYPE_JSON, false);
			LineReader lr = new LineReader(basRetorno);
			novaProducaoProd = lr.readLine();
		} finally {
			fechaConexao();
		}
		if (novaProducaoProd == null) {
			throw new ValidationException(Messages.WEB2TC_ERRO_PRODUCAOPROD);
		}
		return novaProducaoProd;
	}

	public void getEstoqueAtualizacoesAndUpdateDb() throws Exception {
		abreConexao();
		try {
			Hashtable infoAtualizacaoEstoque = SyncManager.getInfoAtualizacaoEstoque();
			recebeDadosDisponiveisServidor(infoAtualizacaoEstoque);
		} finally {
			fechaConexao();
		}
	}

	@Override
	protected void antesReceberDadosDisponiveisNoServidor(Hashtable infoList) throws Exception {
		super.antesReceberDadosDisponiveisNoServidor(infoList);
		verificaERecebeCargaProgramada(infoList);
		existeConfigNotificacao = ConfigNotificacaoService.getInstance().existeConfigNotificacao();
	}

	@Override
	protected void antesAtualizacao(SyncInfo syncInfo) {
		super.antesAtualizacao(syncInfo);
		processaMaxData(syncInfo);
	}

	private void processaMaxData(SyncInfo syncInfo) {
		if (existeConfigNotificacao) {
			SyncManager.processaMaxDataHora(syncInfo);
		}
	}

	@Override
	protected void depoisReceberDadosDisponiveisNoServidor(Hashtable infoList) throws SQLException {
		super.depoisReceberDadosDisponiveisNoServidor(infoList);
		if (ignoreAntesEDepoisReceberDados) return;
		verificaERecebeCargaProgramada(infoList);
		processaNotificacao(infoList);

	}

	private void processaNotificacao(Hashtable infoList) {
		ConfigNotificacaoService.getInstance().startNotificacao(tabelasAtualizadasSucesso, infoList);
	}
	
	@Override
	protected void beforeExecuteExclusao(String tableName, String rowKey) throws SQLException {
		super.beforeExecuteExclusao(tableName, rowKey);
		if (FotoProduto.TABLE_NAME.equals(tableName)) {
			FotoProduto fotoProduto = (FotoProduto) FotoProdutoService.getInstance().findByRowKey(rowKey);
			if ((fotoProduto != null) && ValueUtil.isNotEmpty(fotoProduto.nmFoto)) {
				FileUtil.deleteFile(Produto.getPathImg() + fotoProduto.nmFoto);
			}
		}
		if (FotoClienteDao.TABLE_NAME.equals(tableName)) {
			FotoClienteErp fotoClienteErp = (FotoClienteErp) FotoClienteErpService.getInstance().findByRowKey(rowKey);
			if ((fotoClienteErp != null) && ValueUtil.isNotEmpty(fotoClienteErp.nmFoto)) {
				FileUtil.deleteFile(Cliente.getPathImg() + fotoClienteErp.nmFoto);
			}
		}
		if (FotoProdutoEmp.TABLE_NAME.equals(tableName)) {
			FotoProdutoEmp fotoProdutoEmp = (FotoProdutoEmp) FotoProdutoEmpService.getInstance().findByRowKey(rowKey);
			if (fotoProdutoEmp != null && ValueUtil.isNotEmpty(fotoProdutoEmp.nmFoto)) {
				FileUtil.deleteFile(Produto.getPathImg() + fotoProdutoEmp.nmFoto);
			}
		}
		if (DivulgaInfo.TABLE_NAME.equals(tableName)) {
			DivulgaInfo divulgaInfo = (DivulgaInfo) DivulgaInfoService.getInstance().findByRowKey(rowKey);
			if (divulgaInfo != null && ValueUtil.isNotEmpty(divulgaInfo.nmImagem)) {
				FileUtil.deleteFile(Convert.appendPath(DivulgaInfo.getPathImg(), divulgaInfo.nmImagem));
			}
		}
		if (ReagendaAgendaVisita.TABLE_NAME.equals(tableName)) {
			deleteVisitaGeradaPorReagendamento(rowKey);
		}
		if (FotoProdutoGrade.TABLE_NAME.equals(tableName)) {
			FotoProdutoGrade fotoProdutoGrade = (FotoProdutoGrade) FotoProdutoGradeService.getInstance().findByRowKey(rowKey);
			if (fotoProdutoGrade != null && ValueUtil.isNotEmpty(fotoProdutoGrade.nmFoto)) {
				FileUtil.deleteFile(Convert.appendPath(FotoProdutoGrade.getPathImg(), fotoProdutoGrade.nmFoto));
			}
		}
		if (CatalogoExterno.TABLE_NAME.equals(tableName)) {
			CatalogoExterno catalogoExterno = (CatalogoExterno) CatalogoExternoService.getInstance().findByRowKey(rowKey);
			if (catalogoExterno != null && ValueUtil.isNotEmpty(catalogoExterno.nmArquivo)) {
				FileUtil.deleteFile(Convert.appendPath(CatalogoExterno.getPathCatalogoExterno(), catalogoExterno.nmArquivo));
			}
		}
		if (Cliente.TABLE_NAME.equals(tableName) && LavenderePdaConfig.permiteInativarClienteProspeccao) {
			Cliente cliente = (Cliente) ClienteService.getInstance().findByRowKey(rowKey);
			if (cliente != null) {
				ClienteInativacaoService.getInstance().delete(new ClienteInativacao(cliente));
			}
		}
	}

	private void deleteVisitaGeradaPorReagendamento(String rowKey) {
		try {
			String[] reagendamentoRowKey = rowKey.split(";");
			Visita visitaFilter = new Visita();
			visitaFilter.dtVisita = new Date(Integer.parseInt(reagendamentoRowKey[5].replaceAll("-", "")));
			if (visitaFilter.dtVisita.isBefore(new Date())) return;
			visitaFilter.cdEmpresa = reagendamentoRowKey[0];
			visitaFilter.cdRepresentante = reagendamentoRowKey[1];
			visitaFilter.cdCliente = reagendamentoRowKey[2];
			visitaFilter.flVisitaReagendada = ValueUtil.VALOR_SIM;
			VisitaService.getInstance().deleteAllByExample(visitaFilter);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}
	
	public void downloadFileEntidade(CrudService service) throws Exception {
		int received = 0;
		int error = 0;
		int exists = 0;
		try {
			int total = service.countAllNaoAlterados();
			abreConexao();
			paramsSync.nuMaxTentativas = 1;
			FileProperties file;
			boolean sucesso = false;
			LogSync.addProgressBar(total, "arquivos");
			total = 0;
			while (SessionLavenderePda.isDownloadingMedia() && (file = ((FileProperties) service.findNextNaoAlterado())) != null) {
				LogSync.updateProgressBar(total++);
				try {
					if (MediaUtil.isArquivoFisicoAtualizado(file)) {
						exists++;
						sucesso = true;
					} else {
						sucesso = downloadFile(file);
						received += sucesso ? 1 : 0; 
					}
					if (sucesso) {
						service.updateColumnByWhereColumnNoValidate(BaseDomain.NMCAMPOTIPOALTERACAO, BaseDomain.FLTIPOALTERACAO_ALTERADO, file.getNmCampoUpdateRecebimento(), file.getVlCampoUpdateRecebimento(), Types.VARCHAR);
						continue;
					}
					error++;
					service.updateColumnByWhereColumnNoValidate(BaseDomain.NMCAMPOTIPOALTERACAO, "W", file.getNmCampoUpdateRecebimento(), file.getVlCampoUpdateRecebimento(), Types.VARCHAR);
					Vm.sleep(50);
					if (total % 10 == 0) {
						VmUtil.executeGarbageCollector();
					}
				} catch (Exception e) {
					LogSync.error(e.getMessage());
				}
			}
			service.updateColumnByWhereColumnNoValidate(BaseDomain.NMCAMPOTIPOALTERACAO, ValueUtil.VALOR_NI, BaseDomain.NMCAMPOTIPOALTERACAO, "W", Types.VARCHAR);
		} finally {
			LogSync.removeProgressBar();
			if (received > 0) {
				LogSync.sucess("Recebidos com sucesso: " + received);
			}
			if (error > 0) {
				LogSync.error("Não recebidos: " + error);
			}
			if (exists > 0) {
				LogSync.warn("Arquivos já existentes: " + exists);
			}
			fechaConexao();
			NotificationManager.checkNotificationAndRemove(LavendereNotificationConstants.STOP_SYNC_MEDIA);
		}
	}

	private boolean downloadFile(FileProperties file) {
		String[] params = new String[] {file.getClass().getSimpleName(), file.getFileName()};
		int index = LogSync.info(MessageUtil.getMessage(Messages.SYNC_FILE_RECEBENDO, params));
		JSONObject json = file.getRequestJson();
		json.put("cdSessao", paramsSync.cdSessao);
		json.put("cdPda", Session.getCdUsuario());
		VmUtil.debug(file.getFileName());
		HttpRequest web2Tc = new HttpRequest(paramsSync);
		try (HttpResponse resp = web2Tc.postWeb2Tc(file.getHttpEndpoint() + "/", json, HttpSync.CONTENTTYPE_JSON_UTF8)) {
			VmUtil.debug(json.toString());
			resp.asFile(file.getAbsolutePath());
			LogSync.delete();
			return true;
		} catch (Exception e) {
			VmUtil.debug(e);
			LogSync.replaceAtIndex(MessageUtil.getMessage(Messages.SYNC_FILE_ERRO, new String[] { params[0], params[1], e.getMessage()}), index, LogSync.COR_ERROR);
			return false;
		}
	}
	
	public void logWarnDefaultOrSyncBackGround(String message) throws SQLException {
		if (inSyncBackground) {
			LogSyncBackgroundService.getInstance().insertLogInfo(message);
			return;
		}
		LogSync.warn(message);
	}
	
	public void logErrorDefaultOrSyncBackGround(String message) throws SQLException {
		if (inSyncBackground) {
			LogSyncBackgroundService.getInstance().insertLogError(message);
			return;
		}
		LogSync.error(message);
	}
	
	public String validaEquipamentoLiberadoNoServidor(String cdEquipamento, String cdUsuario) throws Exception {
		abreConexao();
		try {
			return validaEquipamentoLiberado(cdEquipamento, cdUsuario);
		} finally {
			fechaConexao();
		}
	}
	
	public String validaUsuarioAtivoNoSistema() throws Exception {
		abreConexao();
		try {
			ByteArrayStream cbas = validaUsuarioAtivo();
			cbas = ZipUtil.unZip(cbas);
			LineReader lr = new LineReader(cbas);
			return lr.readLine();
		} finally {
			fechaConexao();
		}
	}

	@Override
	protected void depoisSincronismoSucesso(String tableName) throws SQLException {
		if (MensagemExcecLavendereDao.getInstance().getTableName().equals(tableName)) {
			tableName = MensagemExcecDbxDao.TABLE_NAME;
		} else if (AtualizacaoBdLavendereDbxDao.getInstance().getTableName().equals(tableName)) {
			tableName = AtualizacaoBdDbxDao.TABLE_NAME;
		} else if ("TBLVPPERSONALIZACAO".equals(tableName)) {
			PersonalizacaoService.getInstance().reloadImagensPersonalizadas();
		}
		super.depoisSincronismoSucesso(tableName);
	}
	
	public String validaInativacaoReservaEstoqueItensPedido(Pedido pedido) throws Exception {
		String retorno = "";
		if (pedido != null) {
			abreConexao();
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl);
			url.append(ACTION_VALIDA_INATIVACAO_RESERVA_ESTOQUE);
			url.append("/");
			httpSync.addParamUrl(url, pedido.cdEmpresa);
			url.append("/");
			httpSync.addParamUrl(url, pedido.cdRepresentante);
			url.append("/");
			httpSync.addParamUrl(url, pedido.flOrigemPedido);
			url.append("/");
			httpSync.addParamUrl(url, pedido.nuPedido);
			url.append("/"); 
			try {
				retorno = httpSync.executeHttpAsString(url.toString());
			} finally {
				fechaConexao();
			}
		}
		return retorno;
	}
	
	public String revalidaRestricaoProdutoItensPedido(Vector itemPedidoList) throws Exception {
		abreConexao();
		StringBuffer url = new StringBuffer();
		url.append(paramsSync.httpWeb2TcUrl);
		url.append(ACTION_REVALIDA_RESTRICAO_PRODUTO_ITENSPEDIDO);
		url.append("/");
		httpSync.addParamUrl(url, StringUtil.getStringValue(LavenderePdaConfig.geraFlSituacaoRestritoProdutoNoSync));
		url.append("/"); 
		String retorno = "";
		try {
			String produtoListJson = ItemPedidoService.getInstance().getDadosProdutoRestritoNosItensPedidoFormatoJson(itemPedidoList);
			ByteArrayStream cbas = httpSync.executePostHttp(url.toString(), ZipUtil.zipToByteArray(produtoListJson), false);
			LineReader lr = new LineReader(cbas);
			retorno = lr.readLine();
		} finally {
			fechaConexao();
		}
		return retorno;
	}
	
	public String validaReservaEstoqueItensPedido(Vector itemPedidoList, String cdEmpresa, String cdRepresentante) throws Exception {
		abreConexao();
		StringBuffer url = new StringBuffer();
		url.append(paramsSync.httpWeb2TcUrl);
		url.append(ACTION_VALIDA_RESERVA_ESTOQUE);
		url.append("/");
		url.append(cdEmpresa);
		url.append("/");
		url.append(cdRepresentante);
		url.append("/");
		String retorno = "";
		try {
			String itemPedidoListJson = ItemPedidoService.getInstance().getDadosItensPedidoReservaEstoqueFormatoJson(itemPedidoList);
			ByteArrayStream cbas = httpSync.executePostHttp(url.toString(), ZipUtil.zipToByteArray(itemPedidoListJson), false);
			LineReader lr = new LineReader(cbas);
			retorno = lr.readLine();
		} finally {
			fechaConexao();
		}
		return retorno;
	}
	
	public String verificaReservaCadastradaServidor(Vector itemPedidoList, String cdEmpresa, String cdRepresentante) throws Exception {
		abreConexao();
		StringBuffer url = new StringBuffer();
		url.append(paramsSync.httpWeb2TcUrl);
		url.append(ACTION_VERIFICA_RESERVA_ESTOQUE);
		url.append("/");
		url.append(cdEmpresa);
		url.append("/");
		url.append(cdRepresentante);
		url.append("/");
		String retorno = "";
		try {
			String itemPedidoListJson = ItemPedidoService.getInstance().getDadosItensPedidoReservaEstoqueFormatoJson(itemPedidoList);
			ByteArrayStream cbas = httpSync.executePostHttp(url.toString(), ZipUtil.zipToByteArray(itemPedidoListJson), false);
			LineReader lr = new LineReader(cbas);
			retorno = lr.readLine();
		} finally {
			fechaConexao();
		}
		return retorno;
	}
	
	public String verificaPedidoEnviadoServidor(Pedido pedido) throws Exception {
		abreConexao();
		StringBuffer url = new StringBuffer();
		url.append(paramsSync.httpWeb2TcUrl);
		url.append(ACTION_VERIFICA_PEDIDO_ENVIADO_SERVIDOR);
		url.append("/");
		httpSync.addParamUrl(url, pedido.cdEmpresa);
		url.append("/");
		httpSync.addParamUrl(url, pedido.cdRepresentante);
		url.append("/");
		httpSync.addParamUrl(url, pedido.flOrigemPedido);
		url.append("/");
		httpSync.addParamUrl(url, pedido.nuPedido);
		url.append("/"); 
		String retorno = "";
		try {
			retorno = httpSync.executeHttpAsString(url.toString());
		} finally {
			fechaConexao();
		}
		return retorno;
	}
	
	public String geraReservaEstoqueItensPedido(Pedido pedido, Vector itemPedidoList) throws Exception {
		abreConexao();
		StringBuffer url = new StringBuffer();
		url.append(paramsSync.httpWeb2TcUrl);
		url.append(ACTION_GERA_RESERVA_ESTOQUE);
		url.append("/");
		url.append(pedido.cdEmpresa);
		url.append("/");
		url.append(pedido.cdRepresentante);
		url.append("/");
		String retorno = "";
		try {
			String itemPedidoListJson = ItemPedidoService.getInstance().getDadosItensPedidoReservaEstoqueFormatoJson(itemPedidoList);
			ByteArrayStream cbas = httpSync.executePostHttp(url.toString(), ZipUtil.zipToByteArray(itemPedidoListJson), false);
			LineReader lr = new LineReader(cbas);
			retorno = lr.readLine();
		} finally {
			fechaConexao();
		}
		return retorno;
	}

	public String geraRateioProducaoProd(Vector producaoProdList) throws Exception {
		abreConexao();
		StringBuffer url = new StringBuffer();
		url.append(paramsSync.httpWeb2TcUrl);
		url.append(ACTION_GERA_RATEIO_PRODUCAOPROD);
		url.append("/"); 
		httpSync.addParamUrl(url, SessionLavenderePda.usuarioPdaRep.cdUsuario);
		url.append("/"); 
		String retorno = "";
		try {
			String producaoProdListJson = ProducaoProdService.getInstance().getDadosProducaoProdFormatoJson(producaoProdList);
			ByteArrayStream cbas = httpSync.executePostHttp(url.toString(), ZipUtil.zipToByteArray(producaoProdListJson), false);
			LineReader lr = new LineReader(cbas);
			retorno = lr.readLine();
		} finally {
			fechaConexao();
		}
		return retorno;
	}
	
	public String inativaReservaEstoqueItensPedido(Pedido pedido) throws Exception {
		String retorno = "";
		if (pedido != null) {
			abreConexao();
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl);
			url.append(ACTION_INATIVA_RESERVA_ESTOQUE);
			url.append("/");
			httpSync.addParamUrl(url, pedido.cdEmpresa);
			url.append("/");
			httpSync.addParamUrl(url, pedido.cdRepresentante);
			url.append("/");
			httpSync.addParamUrl(url, pedido.flOrigemPedido);
			url.append("/");
			httpSync.addParamUrl(url, pedido.nuPedido);
			url.append("/");
			httpSync.addParamUrl(url, pedido.getCdLocalEstoque());
			url.append("/");
			try {
				retorno = httpSync.executeHttpAsString(url.toString());
			} finally {
				fechaConexao();
			}
		}
		return retorno;
	}

	public String validaPeriodoDevolucaoEstoque(String nuDiasIntDevolEstoque) throws Exception {
		String retorno = "";
		abreConexao();
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(paramsSync.httpWeb2TcUrl);
		strBuf.append(ACTION_VALIDA_PERIODO_DEVOLUCAO_ESTOQUE);
		strBuf.append("/");
		httpSync.addParamUrl(strBuf, SessionLavenderePda.cdEmpresa);
		strBuf.append("/");
		httpSync.addParamUrl(strBuf, SessionLavenderePda.getCdRepresentanteFiltroDados(EstoqueRep.class));
		strBuf.append("/");
		httpSync.addParamUrl(strBuf, StringUtil.getStringValue(StringUtil.getStringValue(nuDiasIntDevolEstoque)));
		try {
			retorno =  httpSync.executeHttpAsString(strBuf.toString());
		} finally {
			fechaConexao();
		}
		return retorno;
	}
	
	public String processaDevolucaoEstoqueWeb(Vector listRemessaEstoque) throws Exception {
		String retorno = "";
		abreConexao();
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(paramsSync.httpWeb2TcUrl);
		strBuf.append(ACTION_INATIVA_ESTOQUE_BY_REMESAS);
		strBuf.append("/");
		try {
			String listArray = getListRemessaEstoqueArray(listRemessaEstoque);
			ByteArrayStream cbas = httpSync.executePostHttp(strBuf.toString(), ZipUtil.zipToByteArray(listArray), false);
			LineReader lr = new LineReader(cbas);
			retorno = lr.readLine();
		} finally {
			fechaConexao();
		}
		return retorno;
	}
	
	private String getListRemessaEstoqueArray(Vector listRemessaEstoque) {
		JSONArray jsonList = new JSONArray();
		JSONObject jsonObject = null;
		int size = listRemessaEstoque.size();
		for (int i=0; i < size; i++) {
			RemessaEstoque remessaEstoque = (RemessaEstoque) listRemessaEstoque.items[i];
			jsonObject = new JSONObject();
			jsonObject.put("cdEmpresa", remessaEstoque.cdEmpresa);
			jsonObject.put("cdRepresentante", remessaEstoque.cdRepresentante);
			jsonObject.put("nuNotaRemessa", remessaEstoque.nuNotaRemessa);
			jsonObject.put("nuSerieRemessa", remessaEstoque.nuSerieRemessa);
			jsonObject.put("cdLocalEstoque", remessaEstoque.cdLocalEstoque);
			jsonList.put(jsonObject);
		}
		return jsonList.toString();
	}
	
	public String cancelaPedido(Pedido pedido) throws Exception {
		String retorno = "";
		abreConexao();
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(paramsSync.httpWeb2TcUrl);
		strBuf.append(ACTION_CANCELA_PEDIDO_PENDENTE);
		strBuf.append("/");
		JSONObject json = new JSONObject();
		json.put("cdEmpresa", pedido.cdEmpresa);
		json.put("cdRepresentante", pedido.cdRepresentante);
		json.put("cdMotivoCancelamento", pedido.cdMotivoCancelamento);
		json.put("nuPedido", pedido.nuPedido);
		json.put("cdUsuarioCancelamento", SessionLavenderePda.usuarioPdaRep.cdUsuario);
		json.put("dsJustificativaCancelamento", ValueUtil.isEmpty(pedido.dsJustificativaCancelamento) ? "" : pedido.dsJustificativaCancelamento);
		try {
			retorno = httpSync.executePostAsString(strBuf.toString(), json.toString(), HttpSync.CONTENTTYPE_JSON,  false);
		} finally {
			fechaConexao();
		}
		return retorno;
	}
	
	public Image geraQrCode(String url) throws Exception {
		url = url.replace("/", "?");
		abreConexao();
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(paramsSync.httpWeb2TcUrl);
		strBuf.append(ACTION_GERAR_QRCODE);
		strBuf.append("/");
		httpSync.addParamUrl(strBuf, url);
		strBuf.append("/");
		httpSync.addParamUrl(strBuf, Session.getCdUsuario());
		ByteArrayStream basUnziped = null;
		ZLibStream inputStream = null;
		try {
			ByteArrayStream cbas = httpSync.executeHttp(strBuf.toString());
			basUnziped = new ByteArrayStream(4096);
			if (cbas != null) {
				inputStream = new ZLibStream(cbas, ZLibStream.INFLATE);
				basUnziped.readFully(inputStream, 0, cbas.available());
				Image image = new Image(basUnziped.getBuffer());
				return image;
			}
		} finally {
			fechaConexao();
			TCStreamUtil.closeStream(inputStream);
			TCStreamUtil.closeStream(basUnziped);
		}
		return null;
	}

	public String liberaPedidoPendenteForaDeOrdem(Pedido pedido) throws Exception {
		String retorno = "";
		if (pedido != null) {
			abreConexao();
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl);
			url.append(ACTION_LIBERA_PEDIDO_PENDENTE);
			url.append("/");
			httpSync.addParamUrl(url, pedido.cdEmpresa);
			url.append("/");
			httpSync.addParamUrl(url, pedido.cdRepresentante);
			url.append("/");
			httpSync.addParamUrl(url, pedido.nuPedido);
			url.append("/");
			httpSync.addParamUrl(url, pedido.flOrigemPedido);
			url.append("/");
			httpSync.addParamUrl(url, SessionLavenderePda.usuarioPdaRep.cdUsuario);
			url.append("/");
			try {
				retorno = httpSync.executeHttpAsString(url.toString());
			} finally {
				fechaConexao();
			}
		}
		return retorno;
	}
	
	public String desbloqueiaRemessaEstoque(RemessaEstoque remessaEstoque) throws Exception {
		String retorno = "";
		if (remessaEstoque != null) {
			abreConexao();
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl);
			url.append(ACTION_DESBLOQUEAR_REMESSA_ESTOQUE);
			url.append("/");
			httpSync.addParamUrl(url, remessaEstoque.cdEmpresa);
			url.append("/");
			httpSync.addParamUrl(url, remessaEstoque.cdRepresentante);
			url.append("/");
			httpSync.addParamUrl(url, remessaEstoque.nuNotaRemessa);
			url.append("/");
			httpSync.addParamUrl(url, remessaEstoque.nuSerieRemessa);
			url.append("/");
			try {
				retorno = httpSync.executeHttpAsString(url.toString());
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
				retorno = SERVER_SEM_CONEXAO;
			} finally {
				fechaConexao();
			}
		}
		return retorno;
	}

	
	public void getAtualizacoesAndUpdate(String table) throws Exception {
		abreConexao();
		try {
			ByteArrayStream cbas = getAtualizacoes(table);
			if (cbas == null) {
				return;
			}
			cbas = ZipUtil.unZip(cbas);
			LineReader lr = new LineReader(cbas);
			String protocolo = lr.readLine();
			if ((protocolo != null) && !protocolo.equals("FIM")) {
				String[] campos = StringUtil.split(lr.readLine(), SEPARADOR_ATUALIZACOES);
				IntHashtable hashColsPos = new IntHashtable(campos.length);
				for (int i = 0; i < campos.length; i++) {
					hashColsPos.put(campos[i].toUpperCase(), i);
				}
				updateRegistros(lr, hashColsPos, table);
			}
		} finally {
			fechaConexao();
		}
	}
	
	private void updateRegistros(LineReader lr, IntHashtable hashColsPos, String table) throws Exception {
		if (Nfe.TABLE_NAME.equalsIgnoreCase(table)) {
			updateRegistrosNfe(lr, hashColsPos);
		} else if (ItemNfe.TABLE_NAME.equalsIgnoreCase(table)) {
			updateRegistrosItemNfe(lr, hashColsPos);
		} else if (ItemNfeReferencia.TABLE_NAME.equalsIgnoreCase(table)) {
			updateRegistrosItemNfeReferencia(lr, hashColsPos);
		} else if (PedidoBoleto.TABLE_NAME.equalsIgnoreCase(table)) {
			updateRegistrosPedidoBoleto(lr, hashColsPos);
		}
	}
	
	private ByteArrayStream getAtualizacoes(String tableName) throws Exception {
		String itemNfe = LavenderePdaConfig.usaNfePorReferencia ? ItemNfeReferencia.TABLE_NAME : ItemNfe.TABLE_NAME;
		if (Nfe.TABLE_NAME.equalsIgnoreCase(tableName)) {
			String tableCarimbo = StringUtil.getStringValue(NfeService.getInstance().getMaxCarimbo()) + SEPARADOR_ATUALIZACOES + Nfe.TABLE_NAME + SEPARADOR_ATUALIZACOES;
			return getProximaAtualizacao(NfeService.getInstance().generateIdGlobal(), tableCarimbo, false, false);
		} else if (itemNfe.equalsIgnoreCase(tableName)) {
			String itemNfeMaxCarimbo = StringUtil.getStringValue(LavenderePdaConfig.usaNfePorReferencia ? ItemNfeReferenciaService.getInstance().getMaxCarimbo() : ItemNfeService.getInstance().getMaxCarimbo());
			String globalId = LavenderePdaConfig.usaNfePorReferencia ? ItemNfeReferenciaService.getInstance().generateIdGlobal() : ItemNfeService.getInstance().generateIdGlobal();
			String tableCarimbo = itemNfeMaxCarimbo + SEPARADOR_ATUALIZACOES + itemNfe + SEPARADOR_ATUALIZACOES;
			return getProximaAtualizacao(globalId, tableCarimbo, false, false);
		} else if (PedidoBoleto.TABLE_NAME.equalsIgnoreCase(tableName)) {
			String tableCarimbo = StringUtil.getStringValue(PedidoBoletoService.getInstance().getMaxCarimbo()) + SEPARADOR_ATUALIZACOES + PedidoBoleto.TABLE_NAME + SEPARADOR_ATUALIZACOES;
			return getProximaAtualizacao(PedidoBoletoService.getInstance().generateIdGlobal(), tableCarimbo, false, false);
		}
		return null;
	}
	
	public String geraPdfPedido(Pedido pedido, boolean viaCliente) throws Exception {
		abreConexao();
		try {
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl);
			url.append(ACTION_GERA_PDF_PEDIDO);
			url.append("/"); 
			String pedidoJson = PedidoService.getInstance().getDadosPedidoFormatoJson(pedido, false, viaCliente);
			ByteArrayStream cbas = httpSync.executePostHttp(url.toString(), ZipUtil.zipToByteArray(pedidoJson), false);
			
			String dsCaminhoDestino = pedido.getDsFilePathPdfPedido();
			String filePath = dsCaminhoDestino + "/" + pedido.getNomeArquivoPdf() + ".pdf";
			filePath = filePath.replace("\\", "/").replace("//", "/");
			FileUtil.createDirIfNecessary(dsCaminhoDestino);
			FileUtil.deleteFile(filePath);
			cbas = ZipUtil.unZip(cbas);
			FileUtil.createWriteFile(filePath, cbas);
			return filePath;
		} finally {
			fechaConexao();
		}
	}

	public String geraPdfPedidoBoleto(Pedido pedido) throws Exception {
		abreConexao();
		try {
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl);
			url.append(ACTION_GERA_PDF_PEDIDO_BOLETO);
			url.append("/");
			String pedidoJson = PedidoService.getInstance().getDadosPedidoFormatoJson(pedido, false);
			ByteArrayStream cbas = httpSync.executePostHttp(url.toString(), ZipUtil.zipToByteArray(pedidoJson), false);

			LineReader lineReader = new LineReader(cbas);
			String readLine = lineReader.readLine();
			if ("nenhum produto encontrado".equalsIgnoreCase(readLine)) {
				lineReader.getStream().close();
				return "nenhum produto encontrado";
			}
			lineReader.getStream().close();
			cbas.reset();

			String dsCaminhoDestino = pedido.getDsFilePathPdfPedidoBoleto();
			String filePath = Convert.appendPath(dsCaminhoDestino, pedido.getNomeArquivoPdf() + ".pdf");
			filePath = filePath.replace("\\", "/").replace("//", "/");
			FileUtil.createDirIfNecessary(dsCaminhoDestino);
			FileUtil.deleteFile(filePath);
			ZipUtil.unZipToDisk(cbas, filePath);
			return filePath;
		} finally {
			fechaConexao();
		}
	}
	
	public String geraPdfDapLaudo(DapLaudo dapLaudo) throws Exception {
		abreConexao();
		try {
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl);
			url.append(ACTION_GERA_PDF_DAP);
			url.append("/"); 
			String dapLaudoJson = DapLaudoService.getInstance().getDapLaudoJson(dapLaudo);
			ByteArrayStream cbas = httpSync.executePostHttp(url.toString(), ZipUtil.zipToByteArray(dapLaudoJson), false);
			
			String dsCaminhoDestino = dapLaudo.getDsFilePathPdfDapLaudo();
			String filePath = Convert.appendPath(dsCaminhoDestino, dapLaudo.getNomeArquivoPdf() + ".pdf");
			filePath = filePath.replace("\\", "/").replace("//", "/");
			FileUtil.createDirIfNecessary(dsCaminhoDestino);
			FileUtil.deleteFile(filePath);
			cbas = ZipUtil.unZip(cbas);
			FileUtil.createWriteFile(filePath, cbas);
			return filePath;
		} finally {
			fechaConexao();
		}
	}

	public String consultaCepOnline(String cep) throws Exception {
		abreConexao();
		try {
			StringBuilder url = new StringBuilder();
			url.append(paramsSync.httpWeb2TcUrl);
			url.append(ACTION_CONSULTA_CEP_ONLINE).append("/");
			url.append((httpSync.addParamUrl(cep))).append("/");
			ByteArrayStream cbas = httpSync.executeHttp(url.toString());
			ByteArrayStream unZip = ZipUtil.unZip(cbas);
			return new String(Convert.charConverter.bytes2chars(unZip.getBuffer(), 0, unZip.getBuffer().length));
		} finally {
			fechaConexao();
		}
	}

	public JSONObject consultaCnpjOnline(String cnpj) throws Exception {
		abreConexao();
		StringBuffer url = new StringBuffer();
		try {
			url.append(paramsSync.basePublicServiceUrl);
			url.append(ACTION_CONSULTA_CNPJ_ONLINE);
			url.append("/");
			URI.encode(cnpj, url);
			url.append("/");
			URI.encode(SessionLavenderePda.cdEmpresa, url);
			url.append("/");
			URI.encode(SessionLavenderePda.getRepresentante().cdRepresentante, url);
			ByteArrayStream cbas = httpSync.executeHttp(url.toString());
			if (cbas.available() == 0) {
				throw new ValidationException(MessageUtil.getMessage(Messages.NOVOCLIENTE_MSG_NENHUMA_INFO_CONSULTA_CNPJ, cnpj));
			}
			return new JSONObject(new String(Convert.charConverter.bytes2chars(cbas.getBuffer(), 0, cbas.getBuffer().length)));
		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			throw new ValidationException(MessageUtil.getMessage(Messages.NOVOCLIENTE_ERRO_WSRECEITA, url.toString()));
		} finally {
			fechaConexao();
		}
	}

	public String getConfigAcessoSistema(String cdFuncao) throws Exception {
		abreConexao();
		try {
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl)
			.append(ACTION_GET_CONFIGACESSOSISTEMA).append("/")
			.append(httpSync.addParamUrl(cdFuncao)).append("/")
			.append(httpSync.addParamUrl(TimeUtil.getCurrentTimeHHMM())).append("/");
			ByteArrayStream cbas = httpSync.executeHttp(url.toString());
			cbas = ZipUtil.unZip(cbas);
			return new String(cbas.getBuffer());
		} finally {
			fechaConexao();
		}
	}
	
	public List<SerieNfeDTO> getNuSerieNfeAndProxNumero(String cdEmpresa, String cdRepresentante, String flContingencia, String cdTipoNota) throws Exception {
		abreConexao();
		try {
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl)
			.append(ACTION_GET_SERIE_NFE).append("/")
			.append(httpSync.addParamUrl(cdEmpresa)).append("/")
			.append(httpSync.addParamUrl(cdRepresentante)).append("/")
			.append(httpSync.addParamUrl(flContingencia)).append("/")
			.append(httpSync.addParamUrl(cdTipoNota)).append("/");
			ByteArrayStream cbas = httpSync.executeHttp(url.toString());
			cbas = ZipUtil.unZip(cbas);
			List<SerieNfeDTO> serieNfeList = JSONFactory.asList(new String(cbas.getBuffer()), SerieNfeDTO.class);
			return serieNfeList;
		} finally {
			fechaConexao();
		}
	}
	
	public boolean recebeRetornoPedido(Pedido pedido, boolean usaRetornoBackground) throws Exception {
		abreConexao();
		try {
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl);
			url.append(ACTION_RECEBE_RETORNO_PEDIDO).append("/")
				.append(usaRetornoBackground ? PEDIDO_RETORNO_BACKGROUND : PEDIDO_RETORNO_PADRAO);
			int tempoInicio = Vm.getTimeStamp();
			String pedidoJson = PedidoService.getInstance().getDadosPedidoFormatoJson(pedido, LavenderePdaConfig.usaGeracaoTxtNfe);
			LogPdaService.getInstance().info(LogPda.LOG_CATEGORIA_SYNC, MessageUtil.getMessage(Messages.PEDIDO_MSG_INFO_ENVIO_DADOS_PEDIDO, new String[] {pedido.nuPedido, DateUtil.formatDateDDMMYYYY(DateUtil.getCurrentDate())}));
			if (!LavenderePdaConfig.usaGeracaoTxtNfe) {
				pedido.flTipoAlteracao = Pedido.FLTIPOALTERACAO_PROCESSANDO_ENVIO;
				PedidoService.getInstance().updatePedidoProcessandoTransmissao(pedido.getRowKey());
			}
			ByteArrayStream cbas = httpSync.executePostHttp(url.toString(), ZipUtil.zipToByteArray(pedidoJson), false);
			cbas = ZipUtil.unZip(cbas);
			RetornoPedidoDTO retornoPedidoDTO = getNewRetornoPedidoDTO(new String(Convert.charConverter.bytes2chars(cbas.getBuffer(), 0, cbas.getBuffer().length)));  
			String erroRetorno = getErroRetorno(retornoPedidoDTO);
			if (ValueUtil.isNotEmpty(erroRetorno)) {
				LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_SYNC, erroRetorno);
				LogAppService.getInstance().logPedido(LogApp.FL_TIPO_LOG_ERRO, pedido.getRowKey(), pedido.cdCliente, LogApp.DS_DETALHES_EMISSAO_PEDIDO_SE, StringUtil.getStringValue(pedido.vlTotalPedido), erroRetorno);
				throw new ApplicationException(erroRetorno);
			}
			LogAppService.getInstance().logPedido(LogApp.FL_TIPO_LOG_INFO, pedido.getRowKey(), pedido.cdCliente, LogApp.DS_DETALHES_EMISSAO_PEDIDO_SS, StringUtil.getStringValue(pedido.vlTotalPedido));
			if (!retornoPedidoDTO.isRetornoWs()) {
				if (retornoPedidoDTO.getNfe() != null && LavenderePdaConfig.usaRetornoAutomaticoValidacaoSEFAZ) {
				NfeService.getInstance().processaRetornoNfe(retornoPedidoDTO.getNfe());
			} else {
				PedidoService.getInstance().insereDadosRetornoPedido(retornoPedidoDTO, pedido);
			}
			}
			String tempoFinal = StringUtil.getStringValueToInterface(Vm.getTimeStamp() - tempoInicio);
			if (LavenderePdaConfig.usaRetornoAutomaticoDadosErpDif && !usaRetornoBackground) {
				PedidoService.getInstance().deletePedidosPdaByPedidosErp();
			}
			if (!LavenderePdaConfig.usaRetornoAutomaticoValidacaoSEFAZ || (LavenderePdaConfig.usaRetornoAutomaticoValidacaoSEFAZ && !LavenderePdaConfig.usaGeracaoTxtNfe)) {
				PedidoService.getInstance().updateInfosPedidoAposEnvioServidor(pedido, url.toString());
			}
			LogPdaService.getInstance().info(LogPda.LOG_CATEGORIA_SYNC, MessageUtil.getMessage(Messages.PEDIDO_MSG_INFO_RECEBIMENTO_SUCESSO, new String[] {DateUtil.formatDateDDMMYYYY(DateUtil.getCurrentDate()), tempoFinal}));
			return true;
		} catch (EnvioDadosJsonException | EnvioDadosNfeException e) {
			LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_SYNC, pedido.dsPedidoPrefixo() + e.getMessage());
			throw e;
		} catch (JSONException e) {
			LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_SYNC, pedido.dsPedidoPrefixo() + Messages.PEDIDO_MSG_ERRO_RECEBIMENTO_RETORNO_PEDIDO);
			throw new ApplicationException(Messages.PEDIDO_MSG_ERRO_RECEBIMENTO_RETORNO_PEDIDO);
		} catch (Throwable e) {
			LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_SYNC, pedido.dsPedidoPrefixo() + Messages.PEDIDO_MSG_ERRO_RECEBIMENTO_RETORNO_PEDIDO);
			throw e;
		} finally {
			fechaConexao();
		}
	}
	
	protected RetornoPedidoDTO getNewRetornoPedidoDTO(String json) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, JSONException {
		return JsonFactory.parse(json, RetornoPedidoDTO.class);
	}
	
	protected String getErroRetorno(RetornoPedidoDTO retornoPedidoDTO) {
		if (retornoPedidoDTO != null && ValueUtil.isNotEmpty(retornoPedidoDTO.getErroRetorno())) {
			String erroRetorno = MessageUtil.getMessage(Messages.PEDIDO_MSG_ERRO_RECEBIMENTO_RETORNO_PEDIDO_ERRO, retornoPedidoDTO.getErroRetorno());
			return erroRetorno;
		}
		return null;
		
	}

	private void updateRegistrosNfe(LineReader lr, IntHashtable hashColsPos) throws Exception {
		CrudDbxDao.getCurrentDriver().startTransaction();
		try {
			String atualizacaoRow = lr.readLine();
	    	String[] valuesRow;
	    	while (atualizacaoRow != null) {
				valuesRow = StringUtil.split(atualizacaoRow, SEPARADOR_ATUALIZACOES, true);
				//--
				Nfe nfe = new Nfe();
				nfe.cdEmpresa = valuesRow[hashColsPos.get("CDEMPRESA")];
				nfe.cdRepresentante = valuesRow[hashColsPos.get("CDREPRESENTANTE")];
				nfe.nuPedido = valuesRow[hashColsPos.get("NUPEDIDO")];
				nfe.flOrigemPedido = valuesRow[hashColsPos.get("FLORIGEMPEDIDO")];
				nfe.cdStatusNfe = valuesRow[hashColsPos.get("CDSTATUSNFE")];
				nfe.dsNaturezaOperacao = valuesRow[hashColsPos.get("DSNATUREZAOPERACAO")];
				nfe.vlChaveAcesso = valuesRow[hashColsPos.get("VLCHAVEACESSO")];
				nfe.dtSolicitacao = DateUtil.toDate(valuesRow[hashColsPos.get("DTSOLICITACAO")]);
				nfe.dsSerieNfe = valuesRow[hashColsPos.get("DSSERIENFE")];
				nfe.dtResposta = DateUtil.toDate(valuesRow[hashColsPos.get("DTRESPOSTA")]);
				nfe.nuLote = ValueUtil.getIntegerValue(valuesRow[hashColsPos.get("NULOTE")]);
				nfe.dsTipoEmissao = valuesRow[hashColsPos.get("DSTIPOEMISSAO")];
				nfe.dsObservacao = valuesRow[hashColsPos.get("DSOBSERVACAO")];
				nfe.nuNfe =  ValueUtil.getIntegerValue(valuesRow[hashColsPos.get("NUNFE")]);
				nfe.cdTipoOperacaoNfe = valuesRow[hashColsPos.get("CDTIPOOPERACAONFE")];
				nfe.dtEmissao = DateUtil.toDate(valuesRow[hashColsPos.get("DTEMISSAO")]);
				nfe.dtSaida = DateUtil.toDate(valuesRow[hashColsPos.get("DTSAIDA")]);
				nfe.vlIbpt = ValueUtil.getDoubleValue(valuesRow[hashColsPos.get("VLIBPT")]);
				nfe.vlTotalNfe = ValueUtil.getDoubleValue(valuesRow[hashColsPos.get("VLTOTALNFE")]);
				nfe.vlTotalIcms = ValueUtil.getDoubleValue(valuesRow[hashColsPos.get("VLTOTALICMS")]);
				nfe.vlTotalSt = ValueUtil.getDoubleValue(valuesRow[hashColsPos.get("VLTOTALST")]);
				nfe.vlTotalIpi = ValueUtil.getDoubleValue(valuesRow[hashColsPos.get("VLTOTALIPI")]);
				nfe.vlTotalFrete = ValueUtil.getDoubleValue(valuesRow[hashColsPos.get("VLTOTALFRETE")]);
				nfe.vlTotalSeguro = ValueUtil.getDoubleValue(valuesRow[hashColsPos.get("VLTOTALSEGURO")]);
				nfe.vlTotalDesconto = ValueUtil.getDoubleValue(valuesRow[hashColsPos.get("VLTOTALDESCONTO")]);
				nfe.vlTotalProdutosNfe = ValueUtil.getDoubleValue(valuesRow[hashColsPos.get("VLTOTALPRODUTOSNFE")]);
				nfe.nuCarimbo = ValueUtil.getIntegerValue(valuesRow[hashColsPos.get("NUCARIMBO")]);
				nfe.cdUsuario = valuesRow[hashColsPos.get("CDUSUARIO")];
				String flTipoAlteracao = valuesRow[hashColsPos.get(BaseDomain.NMCAMPOTIPOALTERACAO.toUpperCase())];
				if (!StringUtil.getStringValue(BaseDomain.FLTIPOALTERACAO_EXCLUIDO).equals(flTipoAlteracao)) {
					nfe.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;
				} else {
					nfe.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_EXCLUIDO;
				}
				atualizacaoRow = lr.readLine();
				boolean exclusao = BaseDomain.FLTIPOALTERACAO_EXCLUIDO.equals(nfe.flTipoAlteracao);
				if (NfeDao.getInstance().findByRowKey(nfe.getRowKey()) == null) {
					if (!exclusao) {
						NfeDao.getInstance().insert(nfe);
						NfeDao.houveRecebimentoNfeBackground = true;
					}
				} else {
					if (exclusao) {
						NfeDao.getInstance().delete(nfe);
					} else {
						NfeDao.getInstance().update(nfe);
						NfeDao.houveRecebimentoNfeBackground = true;
					}
				}
			}
	    	CrudDbxDao.getCurrentDriver().commit();
		} finally {
			CrudDbxDao.getCurrentDriver().finishTransaction();
		}
	}
	
	private void updateRegistrosItemNfe(LineReader lr, IntHashtable hashColsPos) throws Exception {
		CrudDbxDao.getCurrentDriver().startTransaction();
		try {
			String atualizacaoRow = lr.readLine();
			String[] valuesRow;
			while (atualizacaoRow != null) {
				valuesRow = StringUtil.split(atualizacaoRow, SEPARADOR_ATUALIZACOES, true);
				//--
				ItemNfe itemNfe = new ItemNfe();
				itemNfe.cdEmpresa = valuesRow[hashColsPos.get("CDEMPRESA")];
				itemNfe.cdRepresentante = valuesRow[hashColsPos.get("CDREPRESENTANTE")];
				itemNfe.nuPedido = valuesRow[hashColsPos.get("NUPEDIDO")];
				itemNfe.flOrigemPedido = valuesRow[hashColsPos.get("FLORIGEMPEDIDO")];
				itemNfe.cdProduto = valuesRow[hashColsPos.get("CDPRODUTO")];
				itemNfe.flTipoItemPedido = valuesRow[hashColsPos.get("FLTIPOITEMPEDIDO")];
				itemNfe.nuSeqProduto = ValueUtil.getIntegerValue(valuesRow[hashColsPos.get("NUSEQPRODUTO")]);
				itemNfe.cdItemGrade1 = valuesRow[hashColsPos.get("CDITEMGRADE1")];
				itemNfe.cdItemGrade2 = valuesRow[hashColsPos.get("CDITEMGRADE2")];
				itemNfe.cdItemGrade3 = valuesRow[hashColsPos.get("CDITEMGRADE3")];
				itemNfe.cdClassificFiscal = valuesRow[hashColsPos.get("CDCLASSIFICFISCAL")];
				itemNfe.cdUnidade = valuesRow[hashColsPos.get("CDUNIDADE")];
				itemNfe.qtItemFisico = ValueUtil.getDoubleValue(valuesRow[hashColsPos.get("QTITEMFISICO")]);
				itemNfe.vlBaseItemTabelaPreco = ValueUtil.getDoubleValue(valuesRow[hashColsPos.get("VLBASEITEMTABELAPRECO")]);
				itemNfe.vlItemPedido = ValueUtil.getDoubleValue(valuesRow[hashColsPos.get("VLITEMPEDIDO")]);
				itemNfe.vlTotalIcmsItem = ValueUtil.getDoubleValue(valuesRow[hashColsPos.get("VLTOTALICMSITEM")]);
				itemNfe.vlPctIcms = ValueUtil.getDoubleValue(valuesRow[hashColsPos.get("VLPCTICMS")]);
				itemNfe.vlTotalItemPedido = ValueUtil.getDoubleValue(valuesRow[hashColsPos.get("VLTOTALITEMPEDIDO")]);
				itemNfe.vlTotalStItem = ValueUtil.getDoubleValue(valuesRow[hashColsPos.get("VLTOTALSTITEM")]);
				itemNfe.nuCarimbo = ValueUtil.getIntegerValue(valuesRow[hashColsPos.get("NUCARIMBO")]);
				itemNfe.cdUsuario = valuesRow[hashColsPos.get("CDUSUARIO")];
				String flTipoAlteracao = valuesRow[hashColsPos.get(BaseDomain.NMCAMPOTIPOALTERACAO.toUpperCase())];
				if (!StringUtil.getStringValue(BaseDomain.FLTIPOALTERACAO_EXCLUIDO).equals(flTipoAlteracao)) {
					itemNfe.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;
				} else {
					itemNfe.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_EXCLUIDO;
				}
				atualizacaoRow = lr.readLine();
				boolean exclusao = BaseDomain.FLTIPOALTERACAO_EXCLUIDO.equals(itemNfe.flTipoAlteracao);
				if (ItemNfeDbxDao.getInstance().findByRowKey(itemNfe.getRowKey()) == null) {
					if (!exclusao) {
						ItemNfeDbxDao.getInstance().insert(itemNfe);
						ItemNfeDbxDao.houveRecebimentoItemNfeBackground = true;
					}
				} else {
					if (exclusao) {
						ItemNfeDbxDao.getInstance().delete(itemNfe);
					} else {
						ItemNfeDbxDao.getInstance().update(itemNfe);
						ItemNfeDbxDao.houveRecebimentoItemNfeBackground = true;
					}
				}
			}
			CrudDbxDao.getCurrentDriver().commit();
		} finally {
			CrudDbxDao.getCurrentDriver().finishTransaction();	
		}
	}
	
	private void updateRegistrosItemNfeReferencia(LineReader lr, IntHashtable hashColsPos) throws Exception {
		CrudDbxDao.getCurrentDriver().startTransaction();
		try { 
			String atualizacaoRow = lr.readLine();
			String[] valuesRow;
			while (atualizacaoRow != null) {
				valuesRow = StringUtil.split(atualizacaoRow, SEPARADOR_ATUALIZACOES, true);
				//--
				ItemNfeReferencia itemNfeReferencia = new ItemNfeReferencia();
				itemNfeReferencia.cdEmpresa = valuesRow[hashColsPos.get("CDEMPRESA")];
				itemNfeReferencia.cdRepresentante = valuesRow[hashColsPos.get("CDREPRESENTANTE")];
				itemNfeReferencia.nuPedido = valuesRow[hashColsPos.get("NUPEDIDO")];
				itemNfeReferencia.flOrigemPedido = valuesRow[hashColsPos.get("FLORIGEMPEDIDO")];
				itemNfeReferencia.flTipoItemPedido = valuesRow[hashColsPos.get("FLTIPOITEMPEDIDO")];
				itemNfeReferencia.nuSeqProduto = ValueUtil.getIntegerValue(valuesRow[hashColsPos.get("NUSEQPRODUTO")]);
				itemNfeReferencia.cdTributacaoConfig = valuesRow[hashColsPos.get("CDTRIBUTACAOCONFIG")];
				itemNfeReferencia.cdReferencia = valuesRow[hashColsPos.get("CDREFERENCIA")];
				itemNfeReferencia.dsReferencia = valuesRow[hashColsPos.get("DSREFERENCIA")];
				itemNfeReferencia.cdCfop = valuesRow[hashColsPos.get("CDCFOP")];
				itemNfeReferencia.cdClassificFiscal = valuesRow[hashColsPos.get("CDCLASSIFICFISCAL")];
				itemNfeReferencia.cdUnidade = valuesRow[hashColsPos.get("CDUNIDADE")];
				itemNfeReferencia.qtItemFisico = ValueUtil.getDoubleValue(valuesRow[hashColsPos.get("QTITEMFISICO")]);
				itemNfeReferencia.vlBaseItemTabelaPreco = ValueUtil.getDoubleValue(valuesRow[hashColsPos.get("VLBASEITEMTABELAPRECO")]);
				itemNfeReferencia.vlItemPedido = ValueUtil.getDoubleValue(valuesRow[hashColsPos.get("VLITEMPEDIDO")]);
				itemNfeReferencia.vlTotalIcmsItem = ValueUtil.getDoubleValue(valuesRow[hashColsPos.get("VLTOTALICMSITEM")]);
				itemNfeReferencia.vlPctIcms = ValueUtil.getDoubleValue(valuesRow[hashColsPos.get("VLPCTICMS")]);
				itemNfeReferencia.vlTotalItemPedido = ValueUtil.getDoubleValue(valuesRow[hashColsPos.get("VLTOTALITEMPEDIDO")]);
				itemNfeReferencia.nuCarimbo = ValueUtil.getIntegerValue(valuesRow[hashColsPos.get("NUCARIMBO")]);
				itemNfeReferencia.cdUsuario = valuesRow[hashColsPos.get("CDUSUARIO")];
				String flTipoAlteracao = valuesRow[hashColsPos.get(BaseDomain.NMCAMPOTIPOALTERACAO.toUpperCase())];
				if (!StringUtil.getStringValue(BaseDomain.FLTIPOALTERACAO_EXCLUIDO).equals(flTipoAlteracao)) {
					itemNfeReferencia.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;
				} else {
					itemNfeReferencia.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_EXCLUIDO;
				}
				atualizacaoRow = lr.readLine();
				boolean exclusao = BaseDomain.FLTIPOALTERACAO_EXCLUIDO.equals(itemNfeReferencia.flTipoAlteracao);
				if (ItemNfeReferenciaDao.getInstance().findByRowKey(itemNfeReferencia.getRowKey()) == null) {
					if (!exclusao) {
						ItemNfeReferenciaDao.getInstance().insert(itemNfeReferencia);
						ItemNfeReferenciaDao.houveRecebimentoItemNfeReferenciaBackground = true;
					}
				} else {
					if (exclusao) {
						ItemNfeReferenciaDao.getInstance().delete(itemNfeReferencia);
					} else {
						ItemNfeReferenciaDao.getInstance().update(itemNfeReferencia);
						ItemNfeReferenciaDao.houveRecebimentoItemNfeReferenciaBackground = true;
					}
				}
			}
			CrudDbxDao.getCurrentDriver().commit();
		} finally {
			CrudDbxDao.getCurrentDriver().finishTransaction();
		}
	}
	
	private void updateRegistrosPedidoBoleto(LineReader lr, IntHashtable hashColsPos) throws Exception {
		CrudDbxDao.getCurrentDriver().startTransaction();
		try { 
			String atualizacaoRow = lr.readLine();
			String[] valuesRow;
			while (atualizacaoRow != null) {
				valuesRow = StringUtil.split(atualizacaoRow, SEPARADOR_ATUALIZACOES, true);
				//--
				PedidoBoleto pedidoBoleto = new PedidoBoleto();
				pedidoBoleto.cdEmpresa = valuesRow[hashColsPos.get("CDEMPRESA")];
				pedidoBoleto.cdRepresentante = valuesRow[hashColsPos.get("CDREPRESENTANTE")];
				pedidoBoleto.nuPedido = valuesRow[hashColsPos.get("NUPEDIDO")];
				pedidoBoleto.flOrigemPedido = valuesRow[hashColsPos.get("FLORIGEMPEDIDO")];
				pedidoBoleto.nuSequenciaBoletoPedido = ValueUtil.getIntegerValue(valuesRow[hashColsPos.get("NUSEQUENCIABOLETOPEDIDO")]);
				pedidoBoleto.cdBoletoConfig = valuesRow[hashColsPos.get("CDBOLETOCONFIG")];
				pedidoBoleto.cdBarras = valuesRow[hashColsPos.get("CDBARRAS")];
				pedidoBoleto.dsLinhasDigitavel = valuesRow[hashColsPos.get("DSLINHASDIGITAVEL")];
				pedidoBoleto.dtVencimento = DateUtil.toDate(valuesRow[hashColsPos.get("DTVENCIMENTO")]);
				pedidoBoleto.nuAgenciaCodigoCedente = valuesRow[hashColsPos.get("NUAGENCIACODIGOCEDENTE")];
				pedidoBoleto.dtDocumento = DateUtil.toDate(valuesRow[hashColsPos.get("DTDOCUMENTO")]);
				pedidoBoleto.nuDocumento = new BigDecimal(valuesRow[hashColsPos.get("NUDOCUMENTO")]);
				pedidoBoleto.vlBoleto = ValueUtil.getDoubleValue(valuesRow[hashColsPos.get("VLBOLETO")]);
				pedidoBoleto.nuNossoNumero = valuesRow[hashColsPos.get("NUNOSSONUMERO")];
				pedidoBoleto.dsLocalPagamento = valuesRow[hashColsPos.get("DSLOCALPAGAMENTO")];
				pedidoBoleto.nuCarteira = valuesRow[hashColsPos.get("NUCARTEIRA")];
				pedidoBoleto.dsEspecieDocumento = valuesRow[hashColsPos.get("DSESPECIEDOCUMENTO")];
				pedidoBoleto.dsObsCedente = valuesRow[hashColsPos.get("DSOBSCEDENTE")];
				pedidoBoleto.flAceite = valuesRow[hashColsPos.get("FLACEITE")];
				pedidoBoleto.dsEspecie = valuesRow[hashColsPos.get("DSESPECIE")];
				pedidoBoleto.dtProcessamento = DateUtil.toDate(valuesRow[hashColsPos.get("DTPROCESSAMENTO")]);
				pedidoBoleto.nuCarimbo = ValueUtil.getIntegerValue(valuesRow[hashColsPos.get("NUCARIMBO")]);
				pedidoBoleto.cdUsuario = valuesRow[hashColsPos.get("CDUSUARIO")];
				String flTipoAlteracao = valuesRow[hashColsPos.get(BaseDomain.NMCAMPOTIPOALTERACAO.toUpperCase())];
				if (!StringUtil.getStringValue(BaseDomain.FLTIPOALTERACAO_EXCLUIDO).equals(flTipoAlteracao)) {
					pedidoBoleto.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;
				} else {
					pedidoBoleto.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_EXCLUIDO;
				}
				atualizacaoRow = lr.readLine();
				boolean exclusao = BaseDomain.FLTIPOALTERACAO_EXCLUIDO.equals(pedidoBoleto.flTipoAlteracao);
				if (PedidoBoletoDao.getInstance().findByRowKey(pedidoBoleto.getRowKey()) == null) {
					if (!exclusao) {
						PedidoBoletoDao.getInstance().insert(pedidoBoleto);
						PedidoBoletoDao.houveRecebimentoBoletoBackground = true;
					}
				} else {
					if (exclusao) {
						PedidoBoletoDao.getInstance().delete(pedidoBoleto);
					} else {
						PedidoBoletoDao.getInstance().update(pedidoBoleto);
						PedidoBoletoDao.houveRecebimentoBoletoBackground = true;
					}
				}
			}
			CrudDbxDao.getCurrentDriver().commit();
		} finally {
			CrudDbxDao.getCurrentDriver().finishTransaction();
		}
	}
	
	public String sendDadosPedidoEstoque(String pedidoRemessaJson, boolean devolucao) throws Exception {
		abreConexao();
		try {
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl)
			.append(devolucao ? ACTION_GERA_DEVOLUCAO_ESTOQUE : ACTION_GERA_REMESSA_ESTOQUE);
			ByteArrayStream cbas = httpSync.executePostHttp(url.toString(), ZipUtil.zipToByteArray(pedidoRemessaJson), false);
			cbas = ZipUtil.unZip(cbas);
			return new String(cbas.getBuffer());
		} finally {
			fechaConexao();
		}
	}
	
	@Override
	protected SyncInfo getSyncInfo(String nmTabela) throws SQLException {
		return SyncManager.getSyncInfo(nmTabela);
	}
	
	public Vector listBackupServidor() throws Exception {
		abreConexao();
		try {
			Vector listBackupServidor = new Vector();
			JSONArray listBackupApp = httpSync.listBackupApp();
			if (listBackupApp != null) {
				for (int i = 0; i < listBackupApp.length(); i++) {
					JSONObject json = (JSONObject) listBackupApp.get(i);
					BackupFile backupFile = new BackupFile(JsonFactory.parse(json, BackupFileDTO.class));
					listBackupServidor.addElement(backupFile);
				}
				return listBackupServidor;
			}
		} finally {
			fechaConexao();
		}
		return null;
	}
	
	@Override
	public void downloadAndUnzipBackupAppFile(String fileName) throws Exception {
		abreConexao();
		try {
			ByteArrayStream cbas = httpSync.downloadBackupAppFile(fileName.replaceAll(DatabaseUtil.EXTENSAO_BKP, ValueUtil.VALOR_NI));
			String name = Convert.appendPath(DatabaseUtil.getDriverBackupPath(), fileName);
			FileUtil.deleteFile(name);
			ZipUtil.unZipToFile(cbas, name);
			if (cbas != null) {
				cbas.close();
			}
		} finally {
			fechaConexao();
		}
	}
	
	public String getLastBackupFileName() throws Exception {
		abreConexao();
		try {
			return httpSync.getLastBackupFileName();
		} finally {
			fechaConexao();
		}
	}
	
	public boolean validaCpfCnpjNovoCliente(String cnpjCliente, String cdEmpresa, String flValidaProspect) throws Exception {
		abreConexao();
		try {
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl).append(ACTION_VALIDA_CPFCNPJ_NOVO_CLIENTE).append("/");
			httpSync.addParamUrl(url, cdEmpresa);
			url.append("/");
			httpSync.addParamUrl(url, flValidaProspect);
			ByteArrayStream cbas = httpSync.executePostHttp(url.toString(), ZipUtil.zipToByteArray(cnpjCliente), true);
			cbas = ZipUtil.unZip(cbas);
			LineReader lineReader = new LineReader(cbas);
			String readLine = lineReader.readLine();
			if (cbas != null) {
				cbas.close();
			}
			return !"INVALIDO".equals(readLine);
		} finally {
			fechaConexao();
		}
	}
	
	private void verificaERecebeCargaProgramada(Hashtable infoList) throws SQLException {
		boolean nenhumaCargaAIntegrar = false;
		LogSyncTimer timer = new LogSyncTimer(Messages.SINCRONIZACAO_MSG_CARGA_PROGRAMADA_INICIO).newLogOnFinish();
		try {
			logInicioRecebimentoCargasProgramadas();
			do {
				if (recebeuCargaProgramada() || existeCargaProgramadaPendente()) {
					salvaDadosCargaProgramada(infoList);
				} else {
					nenhumaCargaAIntegrar = true;
				}
			} while (! nenhumaCargaAIntegrar);
		} finally {
			timer.finish();
			logFimRecebimentoCargasProgramadas();
		}
	}
	
	private void logInicioRecebimentoCargasProgramadas() throws SQLException {
		LogSync.logSection(Messages.SINCRONIZACAO_MSG_CARGA_PROGRAMADA_INICIO);
	}

	private void logFimRecebimentoCargasProgramadas() throws SQLException {
		LogSync.logSection(Messages.SINCRONIZACAO_MSG_CARGA_PROGRAMADA_FINAL);
	}

	private void logErroRecebimentoTabelaProgramada() throws SQLException {
		if (inSyncBackground) {
			LogSyncBackgroundService.getInstance().insertLogError(Messages.SINCRONIZACAO_MSG_ERRO_CARGA_PROGRAMADA);
		}
		LogSync.logSection(Messages.SINCRONIZACAO_MSG_ERRO_CARGA_PROGRAMADA);
	}

	private boolean recebeuCargaProgramada() throws SQLException {
		if (!LavenderePdaConfig.isGeraCargaQuandoAtualizacaoMuitoGrande()) {
			return false;
		}
		try {
			ByteArrayStream cbas;
			LineReader lineReader;
			String readLine;
			LogSyncTimer timer = null;
			try {
				int limit = 200;
				do {
					cbas = httpSync.downloadNextCargaProgramadaFile();
					lineReader = new LineReader(cbas);
					readLine = lineReader.readLine();
					if ("wait".equals(readLine)) {
						LogSync.setOmit(false);
						lineReader.getStream().close();
						limit--;
						if (timer == null) {
							timer = new LogSyncTimer("Aguardando geração servidor");
						}
						if (limit <= 0) {
							LogSync.error("Tempo máximo de espera de carga programada extrapolado. Tente mais tarde.");
							return false;
						}
						Vm.safeSleep(3000);
						LogSync.setOmit(true);
					} else {
						break;
					}
				} while (true);
			} finally {
				LogSync.setOmit(false);
				if (timer != null) {
					timer.finish();
				}
			}
			if (CargaProgramadaService.NENHUMA_CARGA_ENCONTRADA.equalsIgnoreCase(readLine)) {
				lineReader.getStream().close();
				return false;
			}
			cbas.reset();
			final String path = CargaProgramadaService.getCargaProgramadaFilePath();
			FileUtil.deleteFile(path);
			ZipUtil.unZipToFile(cbas, path);
			cbas.close();
		} catch (Throwable e) {
			houveErroRecebimento = true;
			logErroRecebimentoTabelaProgramada();
			return false;
		}
		return true;
	}

	private void salvaDadosCargaProgramada(Hashtable infoList) throws SQLException {
		try {
			LogPdaService.getInstance().logSyncDebug(Messages.SINCRONIZACAO_LOG_INICIO_RECEBIMENTO_TABELA_CARGA_PROGRAMADA);
			LavendereCargaProgramadaService cps = new LavendereCargaProgramadaService(inSyncBackground, infoList);
			cps.restauraCargaProgramada();
			LogPdaService.getInstance().logSyncDebug(Messages.SINCRONIZACAO_LOG_FIM_RECEBIMENTO_TABELA_CARGA_PROGRAMADA);
			aposRestaurarCarga(cps);
		} catch (Throwable e) {
			logErroRecebimentoTabelaProgramada();
			LogPdaService.getInstance().logSyncError(Messages.SINCRONIZACAO_LOG_ERRO_RECEBIMENTO_TABELA_CARGA_PROGRAMADA + e);
			ExceptionUtil.handle(e);
			houveErroRecebimento = true;
		} finally {
			FileUtil.deleteFile(CargaProgramadaService.getCargaProgramadaFilePath());
		}
	}

	private void aposRestaurarCarga(LavendereCargaProgramadaService cps) throws SQLException {
		tabelasAtualizadasSucesso.addAll(cps.getTabelasAtualizadasList());
	}
	
	public boolean existeCargaProgramadaPendente() {
		return LavenderePdaConfig.isGeraCargaQuandoAtualizacaoMuitoGrande() && CargaProgramadaService.existeCargaProgramadaPendente();
	}

	public void geraCatalogoProduto(JSONObject catalogoParamsJson) throws Exception {
		abreConexao();
		int httpCode;
		try {
			HttpRequest httpRequest = new HttpRequest(paramsSync);
			int sleep = 1000;
			do {
				String message;
				try (HttpResponse response = httpRequest.postWeb2Tc(ACTION_GERA_CATALOGO_PRODUTO, catalogoParamsJson, HttpSync.CONTENTTYPE_JSON_UTF8)) {
					httpCode = response.getResponseCode();
					if (response.getResponseCode() == 200) {
						FileUtil.createDirIfNecessary(Produto.getDsFilePathPdfProduto());
						String filename = Convert.appendPath(Produto.getDsFilePathPdfProduto(), "catalogoProduto_" + catalogoParamsJson.getString("id") + ".pdf");
						response.asFile(filename);
						NotificationManager.putNotification(new Notification(LavendereNotificationConstants.CATALOGO_PRODUTO, "Catalogo Gerado com sucesso!") {
							@Override
							public void process() throws Exception {
								if (UiUtil.showConfirmYesNoMessage("Download de catálogo concluído. Deseja abrir agora?")) {
									VmUtil.viewer(filename);
								}
							}
						});
						return;
					} else if (httpCode == 500) {
						throw new ValidationException(response.asString());
					}
					message = response.asString();
				}
				LogSync.warn(message);
				Vm.sleep(sleep);
				if (sleep < 10000) {
					sleep = sleep * 2;
				}
			} while (httpCode == 202);
		} finally {
			fechaConexao();
		}
	}
	
	public void envieDadosVerbaSaldoPedidoAberto(String json) throws Exception {
		abreConexao();
		try {
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl)
				.append(ACTION_ENVIA_VERBASALDO_ABERTO);
			httpSync.executePostHttp(url.toString(), ZipUtil.zipToByteArray(json), false);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		} finally {
			fechaConexao();
		}
	}
	
	public String getNextNuBoleto(String json) throws Exception {
		abreConexao();
		try {
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl).append(ACTION_NEXT_NU_BOLETO);
			ByteArrayStream basRetorno = httpSync.executePostBy(url.toString(), json, HttpSync.CONTENTTYPE_JSON, false);
			LineReader lr = new LineReader(basRetorno);
			return lr.readLine();
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			return null;
		} finally {
			fechaConexao();
		}
	}
	
	public String getNuProtocoloNfe(String json) throws Exception {
		abreConexao();
		try {
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl).append(ACTION_GET_NU_PROTOCOLO_NFE);
			ByteArrayStream basRetorno = httpSync.executePostBy(url.toString(), json, HttpSync.CONTENTTYPE_JSON, false);
			LineReader lr = new LineReader(basRetorno);
			return lr.readLine();
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			return null;
		} finally {
			fechaConexao();
		}
	}
	
	public String hasTableUpdates(String tableName, int nuCarimbo) throws Exception {
		abreConexao();
		try {
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl).append(ACTION_HAS_TABLE_UPDATES).append("/");
			httpSync.addParamUrl(url, tableName);
			url.append("/");
			httpSync.addParamUrl(url, String.valueOf(nuCarimbo));
			url.append("/");
			httpSync.addParamUrl(url, getCdUsuario());
			return httpSync.executeHttpAsString(url.toString());
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			return ValueUtil.VALOR_NAO;
		} finally {
			fechaConexao();
		}
	}
	
	public void importaVerbaSaldoERPAndUpdatePDA(final String cdEmpresa, final String cdRepresentante) throws Exception {
		abreConexao();
		try {
			validateRetornoVerbaSaldo(cdRepresentante, montaUrlAtualizaVerbaSaldo(cdEmpresa, cdRepresentante));
			recebeAtualizacaoVerbaSaldo();
		} finally {
			fechaConexao();
		}
	}
	
	public String validaLoginOnline(SendValidaSenhaDTO sendValidaSenhaDTO) throws Exception {
		String retorno = "";
		abreConexao();
		StringBuffer url = new StringBuffer();
		url.append(paramsSync.httpWeb2TcUrl);
		url.append(ACTION_VALIDA_LOGIN_ONLINE);
		try {
			retorno = httpSync.executePostAsString(url.toString(), getJsonUsuario(sendValidaSenhaDTO), HttpSync.CONTENTTYPE_JSON, false);
		} finally {
			fechaConexao();
		}
		return retorno;
	}

	private String getJsonUsuario(SendValidaSenhaDTO sendValidaSenhaDTO) {
		return new JSONObject(sendValidaSenhaDTO).toString();
	}
	
	public String buscaUsuarioEquipamento(final String cdEquipamento) throws Exception {
		StringBuffer url = new StringBuffer();
		url.append(paramsSync.httpWeb2TcUrl);
		url.append(ACTION_VERIFICA_USUARIO_EQUIPAMENTO);
		url.append("/");
		url.append(cdEquipamento);
		abreConexao();
		try {
			return httpSync.executeHttpAsString(url.toString());
		} finally {
			fechaConexao();
		}
	}

	private StringBuffer montaUrlAtualizaVerbaSaldo(final String cdEmpresa, final String cdRepresentante) {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(paramsSync.httpWeb2TcUrl);
		strBuf.append(ACTION_UPDATE_VERBASALDO);
		strBuf.append("/");
		httpSync.addParamUrl(strBuf, Session.getCdUsuario());
		strBuf.append("/");
		httpSync.addParamUrl(strBuf, cdEmpresa);
		strBuf.append("/");
		httpSync.addParamUrl(strBuf, cdRepresentante);
		return strBuf;
	}

	private void validateRetornoVerbaSaldo(final String cdRepresentante, StringBuffer strBuf)
			throws IOException, Exception {
		String retorno = null;
		try {
			ByteArrayStream basRetorno = httpSync.executeHttp(strBuf.toString());
			LineReader lr = new LineReader(basRetorno);
			retorno = lr.readLine();
		} finally {
			fechaConexao();
		}
		if (retorno == null || !retorno.equalsIgnoreCase("OK")) {
			throw new ValidationException(MessageUtil.getMessage(Messages.WEB2TC_ERRO_ESTOQUE, cdRepresentante));
		}
	}

	private void recebeAtualizacaoVerbaSaldo() throws Exception {
    	ConfigIntegWebTc configIntegWebTcFilter;
		configIntegWebTcFilter = new ConfigIntegWebTc();
		Vector web2SyncList = new Vector();
    	try {
    		//--
    		configIntegWebTcFilter.dsTabela = VerbaSaldo.TABLE_NAME;
			ConfigIntegWebTc webTCVerbaSaldo = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
			web2SyncList.addElement(webTCVerbaSaldo);
			recebeDadosDisponiveisServidor(SyncManager.getInfoAtualizacaoByWeb2SyncList(web2SyncList));
		} catch (Throwable ex) {
			throw ex;
		}	
	}
	
	public boolean ordenaAgendaVisita(double cdLatitude, double cdLongitude, String listAgendaJson) throws Exception {
		abreConexao();
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(paramsSync.httpWeb2TcUrl)
			.append(ACTION_ORDENA_AGENDA)
			.append("/");
			httpSync.addParamUrl(sb, String.valueOf(cdLatitude));
			sb.append("/");
			httpSync.addParamUrl(sb, String.valueOf(cdLongitude));
			sb.append("/"); 
			httpSync.addParamUrl(sb, SessionLavenderePda.usuarioPdaRep.cdUsuario);
			String retorno = httpSync.executePostAsString(sb.toString(), listAgendaJson, HttpSync.CONTENTTYPE_JSON, false);
			if (ValueUtil.valueEquals("OK", retorno)) {
				return true;
			}
			if (ValueUtil.valueEquals(RETORNO_ERRO_RECEBER_AGENDA, retorno)) {
				throw new ValidationException(Messages.MSG_ERRO_RECEBER_AGENDA);
			}
			throw new ValidationException(MessageUtil.getMessage(Messages.MSG_ERRO_CALCULO_AGENDA, retorno));
		} finally {
			fechaConexao();
		}
	}

	private void processaCargaInicialFotos() throws SQLException {
		if (SyncCargaInicialFotos.existeConfigInternoCargaPendente() && (LavenderePdaConfig.isMostraFotoProduto() || LavenderePdaConfig.usaFotoCliente() || LavenderePdaConfig.usaDivulgaInformacao || LavenderePdaConfig.isUsaFotoProdutoGrade())) {
			NotificationManager.putNotification(new Notification(LavendereNotificationConstants.RECEBE_CARGA_FOTOPRODUTO) {
				@Override
				public void process() throws Exception {
					SyncActions.showRecebimentoCargaFoto();
				}
			});
		}
	}
	
	public void processaRecebimentoArquivos() throws SQLException {
		processaCargaInicialFotos();
		SyncMediaManager mediaManager = new SyncMediaManager(this);
		mediaManager.verificaAtualizacaoMedia();
	}
	
	public boolean recebeFotosCargaInicial() throws Exception {
		String url = paramsSync.httpWeb2TcUrl + ACTION_RECEBE_FOTOS_CARGA_INICIAL + "/" + SessionLavenderePda.usuarioPdaRep.cdUsuario;
		String newZipFileName = "fotos.zip";
		FileUtil.createDirIfNecessary(FotoUtil.getPathImg());
		String fileAbsolutePath = Convert.appendPath(FotoUtil.getPathImg(), newZipFileName);
		FileUtil.deleteFile(fileAbsolutePath);
		abreConexao();
		try {
			httpSync.executeFileHttp(url, fileAbsolutePath);
		} finally {
			fechaConexao();
		}
		LogSync.info(Messages.FOTOS_CARGA_INICIAL_ARMAZENANDO);
		LogSyncTimer timer = new LogSyncTimer(Messages.FOTOS_CARGA_INICIAL_DESCOMPACTANDO);
		VmUtil.executeGarbageCollector();
		try {
			ZipFileUtil.unzip(FotoUtil.getPathImg(), newZipFileName);
			FileUtil.deleteFile(fileAbsolutePath);
		} finally {
			timer.finish();
		}
		return true;
	}

	public HashMap<String, Object> getInformacoesComplementaresPedidoWebserviceSankhya(String body) throws Exception {
		abreConexao();
		try {
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl).append(ACTION_GET_INFORMACAO_COMPLEMENTAR_PEDIDO_WEBSERVICE_SANKHYA);
			ByteArrayStream cbas = httpSync.executePostHttp(url.toString(), ZipUtil.zipToByteArray(body), false);
			cbas = ZipUtil.unZip(cbas);
			return (HashMap<String, Object>) new JSONParser().parse(new LineReader(cbas).readLine());
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			return new HashMap<>();
		} finally {
			fechaConexao();
		}

	}

	public String fusoHorario(String uf) throws Exception {
		abreConexao();
		try {
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl);
			url.append(ACTION_FUSO_HORARIO).append("/");
			url.append((httpSync.addParamUrl(uf))).append("/");
			return httpSync.executeHttpAsString(url.toString());
		} finally {
			fechaConexao();
		}
	}

	public String buscaEstoqueFiliais(String cdProduto) throws Exception {
		abreConexao();
		StringBuffer url = new StringBuffer();
		url.append(paramsSync.httpWeb2TcUrl);
		url.append(ACTION_GET_ESTOQUE_FILIAIS);
		url.append("/");
		JSONObject json = new JSONObject();
		json.put("cdProduto", cdProduto);
		json.put("cdRepresentante", SessionLavenderePda.getCdRepresentanteFiltroDados(Estoque.class));
		String retorno = "";
		try {
			ByteArrayStream cbas = httpSync.executePostBy(url.toString(), json.toString(), HttpSync.CONTENTTYPE_JSON, false);
			LineReader lr = new LineReader(cbas);
			retorno = lr.readLine();
		} finally {
			fechaConexao();
		}
		return retorno;
	}

	public String geraPdfCatalogoPedido(Pedido pedido) throws Exception {
		abreConexao();
		try {
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl);
			url.append(ACTION_GERA_PDF_CATALOGO_ITENS).append("/");
			String catalogoPedidoJson = PedidoService.getInstance().getDadosPedidoFormatoJson(pedido, false);
			ByteArrayStream cbas = httpSync.executePostHttp(url.toString(), ZipUtil.zipToByteArray(catalogoPedidoJson), false);
			String dsCaminhoDestino = pedido.getDsFilePathCatalogoPedidoPdf();
			String filePath = dsCaminhoDestino + "/" + pedido.getNomeArquivoCatalogoPedidoPdf() + ".pdf";
			filePath = filePath.replace("\\", "/").replace("//", "/");
			FileUtil.createDirIfNecessary(dsCaminhoDestino);
			FileUtil.deleteFile(filePath);
			cbas = ZipUtil.unZip(cbas);
			FileUtil.createWriteFile(filePath, cbas);
			return filePath;
		} finally {
			fechaConexao();
		}
	}


	public String geraPdfNfeTituloFinanceiro(TituloFinanceiro titulo) throws Exception {
		abreConexao();
		try {
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl);
			url.append(ACTION_GERA_PDF_NFE_PEDIDO);
			url.append("/");
			JSONObject dadosTituloJson  = getDadosTituloJson(titulo);
			ByteArrayStream cbas = httpSync.executePostBy(url.toString(), dadosTituloJson.toString(), HttpSync.CONTENTTYPE_JSON, false);

			LineReader lineReader = new LineReader(cbas);
			String readLine = lineReader.readLine();
			if ("nenhum produto encontrado".equalsIgnoreCase(readLine)) {
				lineReader.getStream().close();
				return "nenhum produto encontrado";
			}
			lineReader.getStream().close();
			cbas.reset();

			String dsCaminhoDestino = titulo.getDsFilePathPdfNfPedido();
			String filePath = dsCaminhoDestino + "/" + titulo.getNomeArquivoPdf() + ".pdf";
			filePath = filePath.replace("\\", "/").replace("//", "/");
			FileUtil.createDirIfNecessary(dsCaminhoDestino);
			FileUtil.deleteFile(filePath);
			cbas = ZipUtil.unZip(cbas);
			FileUtil.createWriteFile(filePath, cbas);
			return filePath;
		}catch (Exception e) {
				e.printStackTrace();
		} finally {
			fechaConexao();
		}
		return null;
	}

	private JSONObject getDadosTituloJson(TituloFinanceiro tituloFinanceiro) {
		JSONObject json = new JSONObject();
		json.put("cdEmpresa", tituloFinanceiro.cdEmpresa);
		json.put("cdRepresentante", tituloFinanceiro.cdRepresentante);
		json.put("cdCliente", tituloFinanceiro.cdCliente);
		json.put("nuNf", tituloFinanceiro.nuNf);
		json.put("nuSerie", tituloFinanceiro.nuSerie);
		json.put("nuTitulo", tituloFinanceiro.nuTitulo);
		json.put("nuSubDoc", tituloFinanceiro.nuSubDoc);
		return json;
	}

	public String geraPdfTituloPedidoBoleto(TituloFinanceiro titulo) throws Exception {
		abreConexao();
		try {
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl);
			url.append(ACTION_GERA_PDF_BOLETO_PEDIDO);
			url.append("/");
			JSONObject dadosTituloJson  = getDadosTituloJson(titulo);
			ByteArrayStream cbas = httpSync.executePostBy(url.toString(), dadosTituloJson.toString(), HttpSync.CONTENTTYPE_JSON, false);

			LineReader lineReader = new LineReader(cbas);
			String readLine = lineReader.readLine();
			if ("nenhum produto encontrado".equalsIgnoreCase(readLine)) {
				lineReader.getStream().close();
				return "nenhum produto encontrado";
			}
			lineReader.getStream().close();
			cbas.reset();

			String dsCaminhoDestino = titulo.getDsFilePathPdfTituloBoletoPedido();
			String filePath = dsCaminhoDestino + "/" + titulo.getNomeArquivoPdf() + ".pdf";
			filePath = filePath.replace("\\", "/").replace("//", "/");
			FileUtil.createDirIfNecessary(dsCaminhoDestino);
			FileUtil.deleteFile(filePath);
			cbas = ZipUtil.unZip(cbas);
			FileUtil.createWriteFile(filePath, cbas);
			return filePath;
		}catch (Exception e) {
				e.printStackTrace();
		} finally {
			fechaConexao();
		}
		return null;
	}

	public void getWtools() throws Exception {
		getAplicativo(ACTION_GET_WTOOLS, MessageUtil.getMessage(LavendereConfig.NMAPP_WTOOLS, LavendereConfig.getInstance().nuVersionWtools));
	}

	public void getAplicativo(String endpoint, String file) throws Exception {
		abreConexao();
		StringBuffer url = new StringBuffer();
		url.append(paramsSync.httpWeb2TcUrl);
		url.append(endpoint);
		url.append("/");
		httpSync.addParamUrl(url, Session.getCdUsuario());
		ByteArrayStream cbas = null;
		try {
			cbas = httpSync.executeHttp(url.toString());
			String name = DatabaseUtil.getPathDados() + file;
			FileUtil.deleteFile(name);
			ZipUtil.unZipToFile(cbas, name);
			LogSync.info("Recebido com sucesso " + file);
		} finally {
			fechaConexao();
			TCStreamUtil.closeStream(cbas);
		}
	}

	public String postCoodernadasClient(String jsonObject) throws Exception {
		abreConexao();
		StringBuffer url = new StringBuffer();
		url.append(paramsSync.basePublicServiceUrl);
		url.append(ACTION_POST_COODERNADAS_CLIENTE);
		url.append("/").append(SessionLavenderePda.usuarioPdaRep.cdUsuario);
		String retorno = "";
		try {
			ByteArrayStream cbas = httpSync.executePostBy(url.toString(), jsonObject, HttpSync.CONTENTTYPE_JSON_UTF8, false);
			LineReader lr = new LineReader(cbas);
			retorno = lr.readLine();
		} finally {
			fechaConexao();
		}
		return retorno;
	}

	public String getInsertsUpdatesDeletesFotoMenuCatalogo(String nmEntidade) {
		String path = FotoUtil.getPathImg(nmEntidade);
		JSONArray fotoMenuJsonList = new JSONArray();
		FileUtil.createDirIfNecessaryQuietly(path);
		String[] fileList = FileUtil.listFiles(path, null, true);
		if (fileList != null) {
			for (String filePath : fileList) {
				try (File foto = new File(filePath, File.READ_ONLY)) {
					montaRequisicaoFotoMenuCatalogoList(fotoMenuJsonList, foto);
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			}
		}
		String retorno = ValueUtil.VALOR_NI;
		JSONObject jsonRequest = new JSONObject();
		jsonRequest.put("cdPda", SessionLavenderePda.usuarioPdaRep.cdUsuario);
		jsonRequest.put("nmEntidade", nmEntidade);
		jsonRequest.put("fotosList", fotoMenuJsonList.toString());
		try {
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl);
			url.append(ACTION_GET_SYNC_INFO_FOTOMENUCATALOGO);
			url.append("/");
			abreConexao();
			try {
				ByteArrayStream cbas = httpSync.executePostBy(url.toString(), jsonRequest.toString(), HttpSync.CONTENTTYPE_JSON_UTF8, false);
				LineReader lr = new LineReader(cbas);
				retorno = lr.readLine();
			} finally {
				fechaConexao();
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
		return retorno;
	}
	
	private void recebeFotosMenuCatalogo(JSONArray fotosList, String nmEntidade, String ultimaDtHrAtualizacao) throws SQLException {
		try {
			String pathImage = FotoUtil.getPathImg(nmEntidade);
			FileUtil.createDirIfNecessaryQuietly(pathImage);
			LogSync.info("");
			int size = fotosList.length();
			for (int i = 0; i < size; i++) {
				String nmFoto = (String) fotosList.get(i);
				LogSync.replace(MessageUtil.getMessage(Messages.WEB2TC_RECEBENDO_FOTO, new Object[]{StringUtil.getStringValue(i + 1), StringUtil.getStringValue(size)}));
				LogSync.info(MessageUtil.getMessage(Messages.SYNC_FILE_RECEBENDO, new String[] {nmEntidade, nmFoto}));
				int oldNuMaxTentativas = paramsSync.nuMaxTentativas;
				paramsSync.nuMaxTentativas = 1;
				try {
					JSONObject json = new JSONObject();
					json.put("cdSessao", paramsSync.cdSessao);
					json.put("cdPda", Session.getCdUsuario());
					json.put("nmEntidade", nmEntidade);
					json.put("fileName", nmFoto);
					json.put("cdEmpresa", SessionLavenderePda.cdEmpresa);
					json.put("cdRepresentante", SessionLavenderePda.usuarioPdaRep.cdRepresentante);
					HttpRequest web2Tc = new HttpRequest(paramsSync);
					try (HttpResponse resp = web2Tc.postWeb2Tc(ACTION_GET_FOTO_MENU_CATALOGO + "/", json, HttpSync.CONTENTTYPE_JSON_UTF8)) {
						resp.asFile(pathImage + nmFoto);
					}
				} finally {
					LogSync.delete();
					paramsSync.nuMaxTentativas = oldNuMaxTentativas;
				}
			}
			LogSync.replace(size + MessageUtil.getMessage(Messages.WEB2TC_ATUALIZACOES_FOTOS_MENU_CATALOGO, nmEntidade), LogSync.COR_INFO);
			ConfigInternoService.getInstance().addValueGeral(ConfigInterno.ULTIMADTHRATUALIZACAOFOTOMENUCATALOGO, ultimaDtHrAtualizacao, nmEntidade);
		} catch (Exception ex) {
			if (ex.getMessage().startsWith("WARN")) {
				LogSync.warn(ex.getMessage());
			} else {
				LogSync.error(ex.getMessage());
			}
		} finally {
			LogSync.info(MessageUtil.getMessage(Messages.WEB2TC_FIM_RECEB_FOTO_MENU_CATALOGO, nmEntidade));
		}
	}
	
	private String verificaSeHaAtualizacaoFotoMenuCatalogoDisponivelServidor(String nmEntidade) throws SQLException {
		JSONObject jsonRequest = new JSONObject();
		jsonRequest.put("cdPda", SessionLavenderePda.usuarioPdaRep.cdUsuario);
		String dtHrAtualizacao = ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.ULTIMADTHRATUALIZACAOFOTOMENUCATALOGO, nmEntidade);
		if (dtHrAtualizacao == null) {
			dtHrAtualizacao = ValueUtil.VALOR_NI;
		}
		jsonRequest.put("dtHrAtualizacao", dtHrAtualizacao);
		jsonRequest.put("nmEntidade", nmEntidade);
		String retorno = ValueUtil.VALOR_NI;
		try {
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl);
			url.append(ACTION_VERIFICA_ATUALIZACOES_FOTO_MENU_CATALOGO_DISPONIVEIS);
			url.append("/");
			abreConexao();
			try {
				ByteArrayStream cbas = httpSync.executePostBy(url.toString(), jsonRequest.toString(), HttpSync.CONTENTTYPE_JSON_UTF8, false);
				LineReader lr = new LineReader(cbas);
				retorno = lr.readLine();
			} finally {
				fechaConexao();
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
		return retorno;
	}
	
	public String getAtualizacaoMenuCatalogo(MenuCatalogo menuCatalogo) throws SQLException {
		String ultimaDtHrAtualizacao = verificaSeHaAtualizacaoFotoMenuCatalogoDisponivelServidor(menuCatalogo.nmEntidade);
		if (ValueUtil.valueNotEqualsIfNotNull(ultimaDtHrAtualizacao, ValueUtil.VALOR_NAO)) {
			return getInsertsUpdatesDeletesFotoMenuCatalogo(menuCatalogo.nmEntidade);
		}
		return ValueUtil.VALOR_NAO;
	}
	
	public void processaFotoMenuCatalogo() throws SQLException {
		Vector menuCatalogoList = MenuCatalogoService.getInstance().findAllEntidadesAgrupadas();
		int size = menuCatalogoList.size();
		for (int i = 0; i < size; i++) {
			if (!SessionLavenderePda.isDownloadingMedia()) {
				break;
			}
			MenuCatalogo menuCatalogo = (MenuCatalogo) menuCatalogoList.elementAt(i);
			String ultimaDtHrAtualizacao = verificaSeHaAtualizacaoFotoMenuCatalogoDisponivelServidor(menuCatalogo.nmEntidade);
			if (ValueUtil.valueNotEqualsIfNotNull(ultimaDtHrAtualizacao, ValueUtil.VALOR_NAO)) {
				String retorno = getInsertsUpdatesDeletesFotoMenuCatalogo(menuCatalogo.nmEntidade);
				if (possuiAtualizacaoMenuCatalogo(retorno) > 0) {
					processaInsertsUpdatesDeletesFotoMenuCatalogo(retorno, menuCatalogo.nmEntidade, ultimaDtHrAtualizacao);
				}
			}
		}
	}

	private void processaInsertsUpdatesDeletesFotoMenuCatalogo(String retorno, String nmEntidade, String ultimaDtHrAtualizacao) throws SQLException {
		JSONObject retornoJson = new JSONObject(retorno);
		if (retornoJson.keySet().contains("updates")) {
			JSONArray updatesJson = (JSONArray) retornoJson.get("updates");
			if (updatesJson.length() > 0) {
				recebeFotosMenuCatalogo(updatesJson, nmEntidade, ultimaDtHrAtualizacao);
				processaDeletesFotoMenuCatalogo(retornoJson, nmEntidade);
			}
		}
	}
	
	public int possuiAtualizacaoMenuCatalogo(String retorno) {
		try {
			if (ValueUtil.valueNotEqualsIfNotNull(ValueUtil.VALOR_NAO, retorno)) {
				JSONObject retornoJson = new JSONObject(retorno);
				if (retornoJson.keySet().contains("updates")) {
					JSONArray updatesJson = (JSONArray) retornoJson.get("updates");
					return updatesJson.length();
				}
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
		return 0;
		
	}

	private void processaDeletesFotoMenuCatalogo(JSONObject retornoJson, String nmEntidade) {
		if (retornoJson.keySet().contains("deletes")) {
			JSONArray deletesJson = (JSONArray) retornoJson.get("deletes");
			if (deletesJson.length() > 0) {
				LogSync.info(MessageUtil.getMessage(Messages.WEB2TC_REMOVENDO_FOTOS_MENU_CATALOGO, new String[]{StringUtil.getStringValueToInterface(deletesJson.length()), nmEntidade}));
				removeFotosMenuCatalogo(deletesJson, nmEntidade);
			}
		}
	}

	private void removeFotosMenuCatalogo(JSONArray deletesJson, String nmEntidade) {
		String pathImage = FotoUtil.getPathImg(nmEntidade);
		File diretorio = null;
		try {
			diretorio = new File(pathImage, File.DONT_OPEN);
			if (diretorio.exists()) {
				int size = deletesJson.length();
				for (int i = 0; i < size; i++) {
					String nmFoto = (String) deletesJson.get(i);
					FileUtil.deleteFile(Convert.appendPath(pathImage, nmFoto));
				}
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		} finally {
			FileUtil.closeFile(diretorio);
		}
	}

	private void montaRequisicaoFotoMenuCatalogoList(JSONArray fotoMenuJsonList, File foto) throws java.io.IOException {
		long size = foto.getSize();
		String name = FileUtil.getNomeArquivo(foto.getPath());
		JSONObject fotojson = new JSONObject();
		fotojson.put("nmFoto", name);
		fotojson.put("nuTamanho", size);
		Time time = foto.getTime(File.TIME_MODIFIED);
		JSONObject dtModificacaoJSON = new JSONObject();
		dtModificacaoJSON.put("year", time.year);
		dtModificacaoJSON.put("month", time.month);
		dtModificacaoJSON.put("day", time.day);
		dtModificacaoJSON.put("hour", time.hour);
		dtModificacaoJSON.put("minute", time.minute);
		dtModificacaoJSON.put("second", time.second);
		fotojson.put("dtModificacao", dtModificacaoJSON);
		fotoMenuJsonList.put(fotojson);
	}
	
	public void downloadResetAppDatabase() throws Exception {
		abreConexao();
		try {
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.basePublicServiceUrl).append("app-database-version").append("/").append("generateresetdb");
			ByteArrayStream cbas = httpSync.executeHttp(url.toString());
			unzipNewDB(cbas);
		} finally {
			fechaConexao();
		}
	}

	public void downloadRecoverAppDatabase() throws Exception {
		LavendereTc2Web pdaToErp = new LavendereTc2Web();
		pdaToErp.paramsSync.readTimeout = 900000;
		pdaToErp.paramsSync.nuMaxTentativas = 1;
		ByteArrayStream cbas = pdaToErp.enviaBancoDadosRecoverToWeb(DatabaseUtil.DIR_BACKUP_DATABASE_PROBLEMA_RECOVER);
		String dbRecover = Convert.appendPath(DatabaseUtil.getDriverPath(), AppConfig.DATABASE_NAME_RECOVER);
		FileUtil.deleteFile(dbRecover);
		unzipNewDB(cbas);
	}

	private static void unzipNewDB(ByteArrayStream cbas) throws IOException {
		String dbDestino = Convert.appendPath(DatabaseUtil.getDriverPath(), AppConfig.DATABASE_NAME);
		try (File fileDb = new File(dbDestino)) {
			fileDb.delete();
		} catch (Exception e) {
			//ignore
		}
		if (FileUtil.exists(dbDestino)) {
			String dirPath = Convert.appendPath(DatabaseUtil.getDriverPath(), DatabaseUtil.DIR_BACKUP_DATABASE_RECUPERADO);
			FileUtil.createDirIfNecessary(dirPath);
			dbDestino = Convert.appendPath(dirPath, AppConfig.DATABASE_NAME);
		}
		ZipUtil.unZipToFile(cbas, dbDestino);
	}
	
	public String buscaPlacesWeb(ListaLeadsDTO listLeadsDTO) throws Exception {
		abreConexao();
		String retorno;
		try {
			StringBuffer url = new StringBuffer();
			url.append(paramsSync.httpWeb2TcUrl);
			url.append(ACTION_GET_PLACES_NEARBY);
			url.append("/");
			url.append(SessionLavenderePda.usuarioPdaRep.cdUsuario);
			url.append("/");
			if (VmUtil.isSimulador()) {
				url.append(listLeadsDTO.enderecoFilter);
				url.append("/");
			} else {
				url.append(listLeadsDTO.cdLatitude);
				url.append("/");
				url.append(listLeadsDTO.cdLongitude);
				url.append("/");
			}
			url.append(listLeadsDTO.raio);
			url.append("/");
			url.append(listLeadsDTO.textFilter);
			url.append("/");
			if (LavenderePdaConfig.isUsaCategoriaLeads() && ValueUtil.isNotEmpty(listLeadsDTO.categoria)) {
				url.append(listLeadsDTO.categoria);
			} else {
				url.append("naousa");
			}
			
			JSONObject jsonObject = new JSONObject();
			ByteArrayStream cbas = httpSync.executePostBy(url.toString(), jsonObject.toString(), HttpSync.CONTENTTYPE_JSON, false);
			LineReader lr = new LineReader(cbas);
			retorno = lr.readLine();
		} finally {
			fechaConexao();
		}
		return retorno;
	}
	
}
