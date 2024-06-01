package br.com.wmw.lavenderepda.sync;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import br.com.wmw.framework.async.AsyncExecution;
import br.com.wmw.framework.async.AsyncWindow;
import br.com.wmw.framework.business.service.BackupService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.UsuarioBloqueadoException;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SQLiteDriver;
import br.com.wmw.framework.notification.Notification;
import br.com.wmw.framework.notification.NotificationManager;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.HttpConnection;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.sync.SyncInfo;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.timer.LogSyncTimer;
import br.com.wmw.framework.util.DatabaseUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.EncodeUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.JsonFactory;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.framework.util.WtoolsUtil;
import br.com.wmw.framework.util.ZipFileUtil;
import br.com.wmw.lavenderepda.LavendereConfig;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AgendaVisita;
import br.com.wmw.lavenderepda.business.domain.BonificacaoSaldo;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ClienteChurn;
import br.com.wmw.lavenderepda.business.domain.ConfigAcessoSistema;
import br.com.wmw.lavenderepda.business.domain.ConfigIntegWebTc;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.ContatoErp;
import br.com.wmw.lavenderepda.business.domain.DapLaudo;
import br.com.wmw.lavenderepda.business.domain.DapLaudoAtua;
import br.com.wmw.lavenderepda.business.domain.DescPromocional;
import br.com.wmw.lavenderepda.business.domain.DivulgaInfo;
import br.com.wmw.lavenderepda.business.domain.DocumentoAnexo;
import br.com.wmw.lavenderepda.business.domain.EquipamentoUsuarioLavendere;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.EstoquePrevisto;
import br.com.wmw.lavenderepda.business.domain.EstoquePrevistoGeral;
import br.com.wmw.lavenderepda.business.domain.EstoqueRep;
import br.com.wmw.lavenderepda.business.domain.Feriado;
import br.com.wmw.lavenderepda.business.domain.FotoClienteErp;
import br.com.wmw.lavenderepda.business.domain.FotoItemTroca;
import br.com.wmw.lavenderepda.business.domain.FotoNovoCliente;
import br.com.wmw.lavenderepda.business.domain.FotoPedido;
import br.com.wmw.lavenderepda.business.domain.FotoPesqMerProdConc;
import br.com.wmw.lavenderepda.business.domain.FotoPesquisaMercado;
import br.com.wmw.lavenderepda.business.domain.FotoProspect;
import br.com.wmw.lavenderepda.business.domain.FuncaoConfig;
import br.com.wmw.lavenderepda.business.domain.InfoEntregaPed;
import br.com.wmw.lavenderepda.business.domain.ItemKitPedido;
import br.com.wmw.lavenderepda.business.domain.ItemLiberacao;
import br.com.wmw.lavenderepda.business.domain.ItemNfce;
import br.com.wmw.lavenderepda.business.domain.ItemNfe;
import br.com.wmw.lavenderepda.business.domain.ItemNfeReferencia;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoBonifCfg;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoErpDif;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.LimiteOportunidade;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.domain.Nfce;
import br.com.wmw.lavenderepda.business.domain.Nfe;
import br.com.wmw.lavenderepda.business.domain.NotificacaoPda;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.NovoClienteAna;
import br.com.wmw.lavenderepda.business.domain.ParcelaPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoBoleto;
import br.com.wmw.lavenderepda.business.domain.PedidoDesc;
import br.com.wmw.lavenderepda.business.domain.PedidoErpDif;
import br.com.wmw.lavenderepda.business.domain.PesquisaRespAppFoto;
import br.com.wmw.lavenderepda.business.domain.PlatVendaMeta;
import br.com.wmw.lavenderepda.business.domain.PoliticaComercial;
import br.com.wmw.lavenderepda.business.domain.PoliticaComercialFaixa;
import br.com.wmw.lavenderepda.business.domain.PontExtItemPed;
import br.com.wmw.lavenderepda.business.domain.PontExtPed;
import br.com.wmw.lavenderepda.business.domain.PontoGps;
import br.com.wmw.lavenderepda.business.domain.PreferenciaFuncao;
import br.com.wmw.lavenderepda.business.domain.Prospect;
import br.com.wmw.lavenderepda.business.domain.ReagendaAgendaVisita;
import br.com.wmw.lavenderepda.business.domain.Recado;
import br.com.wmw.lavenderepda.business.domain.RelNovSolAutorizacao;
import br.com.wmw.lavenderepda.business.domain.RelNovidadeCli;
import br.com.wmw.lavenderepda.business.domain.RelNovidadeNovoCli;
import br.com.wmw.lavenderepda.business.domain.RelNovidadePesquisaMercado;
import br.com.wmw.lavenderepda.business.domain.RelNovidadeProd;
import br.com.wmw.lavenderepda.business.domain.RemessaEstoque;
import br.com.wmw.lavenderepda.business.domain.RentabilidadeSaldo;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServImagem;
import br.com.wmw.lavenderepda.business.domain.SolAutorizacao;
import br.com.wmw.lavenderepda.business.domain.TabelaDb;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.domain.TituloFinanceiro;
import br.com.wmw.lavenderepda.business.domain.UsuarioDesc;
import br.com.wmw.lavenderepda.business.domain.UsuarioHistAlteracaoLavendere;
import br.com.wmw.lavenderepda.business.domain.UsuarioLavendere;
import br.com.wmw.lavenderepda.business.domain.UsuarioPda;
import br.com.wmw.lavenderepda.business.domain.ValorIndicador;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import br.com.wmw.lavenderepda.business.domain.VerbaCliente;
import br.com.wmw.lavenderepda.business.domain.VerbaFornecedor;
import br.com.wmw.lavenderepda.business.domain.VerbaGrupoSaldo;
import br.com.wmw.lavenderepda.business.domain.VerbaSaldo;
import br.com.wmw.lavenderepda.business.domain.VerbaSaldoVigencia;
import br.com.wmw.lavenderepda.business.domain.VerbaUsuario;
import br.com.wmw.lavenderepda.business.domain.VerbaVigenciaPedido;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.domain.VisitaFoto;
import br.com.wmw.lavenderepda.business.domain.VisitaPedido;
import br.com.wmw.lavenderepda.business.domain.dto.ListaLeadsDTO;
import br.com.wmw.lavenderepda.business.domain.dto.RetValidaSenhaDTO;
import br.com.wmw.lavenderepda.business.domain.dto.SendValidaSenhaDTO;
import br.com.wmw.lavenderepda.business.domain.dto.SerieNfeDTO;
import br.com.wmw.lavenderepda.business.service.AgendaVisitaCliHistService;
import br.com.wmw.lavenderepda.business.service.BonificacaoSaldoService;
import br.com.wmw.lavenderepda.business.service.CampoLavendereService;
import br.com.wmw.lavenderepda.business.service.ConfigIntegWebTcService;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.ContatoService;
import br.com.wmw.lavenderepda.business.service.DapLaudoAtuaService;
import br.com.wmw.lavenderepda.business.service.DescPromocionalService;
import br.com.wmw.lavenderepda.business.service.DivulgaInfoService;
import br.com.wmw.lavenderepda.business.service.DocumentoAnexoService;
import br.com.wmw.lavenderepda.business.service.EquipamentoUsuarioLavendereService;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.FotoClienteService;
import br.com.wmw.lavenderepda.business.service.FotoItemTrocaService;
import br.com.wmw.lavenderepda.business.service.FotoNovoClienteService;
import br.com.wmw.lavenderepda.business.service.FotoPedidoService;
import br.com.wmw.lavenderepda.business.service.FotoPesqMerProdConcService;
import br.com.wmw.lavenderepda.business.service.FotoPesquisaMercadoService;
import br.com.wmw.lavenderepda.business.service.FotoProspectService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoErpDifService;
import br.com.wmw.lavenderepda.business.service.LavendereBackupService;
import br.com.wmw.lavenderepda.business.service.LimiteOportunidadeService;
import br.com.wmw.lavenderepda.business.service.LogPdaService;
import br.com.wmw.lavenderepda.business.service.NovoClienteAnaService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.PesquisaRespAppFotoService;
import br.com.wmw.lavenderepda.business.service.PlatVendaMetaAtuaService;
import br.com.wmw.lavenderepda.business.service.PontoGpsService;
import br.com.wmw.lavenderepda.business.service.ReagendaAgendaVisitaService;
import br.com.wmw.lavenderepda.business.service.RecadoService;
import br.com.wmw.lavenderepda.business.service.RentabilidadeSaldoService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.business.service.RequisicaoServImagemService;
import br.com.wmw.lavenderepda.business.service.UsuarioDescService;
import br.com.wmw.lavenderepda.business.service.VerbaClienteService;
import br.com.wmw.lavenderepda.business.service.VerbaSaldoService;
import br.com.wmw.lavenderepda.business.service.VerbaService;
import br.com.wmw.lavenderepda.business.service.VisitaFotoService;
import br.com.wmw.lavenderepda.business.validation.AtualizaAppException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.AgendaVisitaDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.AreaVendaPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.AtualizacaoBdLavendereDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.AutorizacaoLavendereDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CCCliPorTipoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CampoLavendereDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CestaPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CestaPositClientePdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CestaPositProdutoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CestaProdutoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ClientePdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CondPagtoCliPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CondPagtoLinhaPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CondPagtoTabPrecoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CondTipoPagtoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CondicaoPagamentoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ConexaoPdaPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ConfigIntegWebTcDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ConfigInternoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ContaCorrenteCliDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ContratoClienteDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ContratoProdEstDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ContratoProdutoDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescComiFaixaPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescProgressivoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescPromocionalDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescQuantidadePdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescontoCCPPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.EmpresaPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.EstoquePdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FaceamentoEstoquePdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FaceamentoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FichaFinanceiraPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FornecedorPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FotoClienteDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.GrupoProdFornDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.GrupoProduto1PdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.GrupoProduto2PdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.GrupoProduto3PdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.IndicadorPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemKitPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemPedidoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemTabelaPrecoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.KitPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.KitTabPrecoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.LogPdaPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MenuLavendereDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MetaPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MotRegistroVisitaPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MotivoTrocaPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ParcelaPedidoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PontuacaoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoGradeDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoTabPrecoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RecadoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RelInadimpCliPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RelInadimpRepPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RelNovidadeProdPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RepresentanteEmpPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RepresentantePdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RotaEntregaPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.StatusPedidoPdaPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.StatusRentCliDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.SugVendaPersonCliDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.SugVendaPersonDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.SugVendaPersonProdDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.SupervisorRepPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TabelaPrecoCliPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TabelaPrecoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TabelaPrecoRepPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TelaLavendereDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoFretePdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoNovidadeDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoPagamentoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoPedidoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TituloFinanceiroPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.UsuarioPdaPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.UsuarioWebPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ValorIndicadorPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VerbaClientePdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VerbaContaCorPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VerbaItemPedidoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VerbaPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VerbaProdutoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VerbaSaldoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VerbaTabelaPrecoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VisitaFotoDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VisitaPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VisitaSupervisorPdbxDao;
import br.com.wmw.lavenderepda.notification.LavendereNotificationConstants;
import br.com.wmw.lavenderepda.presentation.ui.CadItemPedidoForm;
import br.com.wmw.lavenderepda.presentation.ui.ListItemPedidoForm;
import br.com.wmw.lavenderepda.presentation.ui.MainLavenderePda;
import br.com.wmw.lavenderepda.presentation.ui.MainMenu;
import totalcross.io.File;
import totalcross.json.JSONObject;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.sys.Convert;
import totalcross.sys.Settings;
import totalcross.sys.Vm;
import totalcross.ui.image.Image;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class SyncManager {

	private static Hashtable infoAtualizacao = null;
	private static String linkDiawi;

	public static void clearInfoAtualizacao() {
		infoAtualizacao = null;
	}

	public static Hashtable getInfoAtualizacaoFixo() {
		Hashtable infoAtualizacaoFixo = new Hashtable(5);
		createSyncInfo(infoAtualizacaoFixo, AtualizacaoBdLavendereDbxDao.TABLE_NAME, new String[] {"cdScript", "nuSeqExec"}, true, false, true);
		createSyncInfo(infoAtualizacaoFixo, ConfigInterno.TABLE_NAME, new String[] {"cdEmpresa", "cdConfigInterno"}, false, false, true);
		createSyncInfo(infoAtualizacaoFixo, ConfigIntegWebTc.TABLE_NAME, new String[] {"dstabela"}, true, false, true);
		return infoAtualizacaoFixo;
	}

	public static Hashtable getInfoAtualizacao(Vector web2SyncList, boolean usaDadosMemoria) {
		if (usaDadosMemoria && infoAtualizacao != null) return infoAtualizacao;

		infoAtualizacao = new Hashtable(40);
		//--
		Hashtable infoAtualizacaoFixos = getInfoAtualizacaoFixo();
		Vector infoAtualizacaoFixo = infoAtualizacaoFixos.getKeys();
		for (int i = 0; i < infoAtualizacaoFixo.size(); i++) {
			String tableName = (String)infoAtualizacaoFixo.items[i];
			infoAtualizacao.put(tableName, infoAtualizacaoFixos.get(tableName));
		}
		//--
		int size = web2SyncList.size();
		for (int i = 0; i < size; i++) {
			createSyncInfo((ConfigIntegWebTc)web2SyncList.items[i]);
		}
		return infoAtualizacao;
	}

	public static Hashtable getInfoAtualizacaoByWeb2SyncList(Vector web2SyncList) {
		Hashtable hashTablesEnvio = new Hashtable(2);
		for (int i = 0; i < web2SyncList.size(); i++) {
			ConfigIntegWebTc configIntegWebTc = (ConfigIntegWebTc)web2SyncList.items[i];
			if (configIntegWebTc != null) {
				createSyncInfo(hashTablesEnvio, configIntegWebTc.dsTabela, configIntegWebTc.getChaves(), configIntegWebTc.isRemessa(), configIntegWebTc.isRetorno(), configIntegWebTc.isAtivo());
			}
		}
		return hashTablesEnvio;
	}

	public static Hashtable getInfoAtualizacaoAtualizado() {
		infoAtualizacao = null;
		return getInfoAtualizacao(true);
	}

	public static Hashtable getInfoAtualizacao(boolean usaDadosMemoria) {
		if (usaDadosMemoria && infoAtualizacao != null) return infoAtualizacao;

		Vector web2SyncList = new Vector(0);
		try {
			web2SyncList = ConfigIntegWebTcService.getInstance().findAll();
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
		return getInfoAtualizacao(web2SyncList, usaDadosMemoria);
	}

	public static SyncInfo getSyncInfo(String nmTabela) throws SQLException {
		if (ValueUtil.isNotEmpty(nmTabela)) {
			ConfigIntegWebTc configIntegWebTcFilter = new ConfigIntegWebTc();
			configIntegWebTcFilter.dsTabela = nmTabela;
			ConfigIntegWebTc configIntegWebTc = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
			Hashtable hash = new Hashtable(1);
			createSyncInfo(hash, configIntegWebTc);
			return (SyncInfo) hash.get(nmTabela);
		}
		return null;
	}

	public static void createSyncInfo(ConfigIntegWebTc configIntegWebTc) {
		createSyncInfo(infoAtualizacao, configIntegWebTc.dsTabela, configIntegWebTc.getChaves(), configIntegWebTc.isRemessa(), configIntegWebTc.isRetorno(), configIntegWebTc.isAtivo());
	}

	public static void createSyncInfo(Hashtable hashtable, ConfigIntegWebTc configIntegWebTc) {
		if (configIntegWebTc != null) {
			createSyncInfo(hashtable, configIntegWebTc.dsTabela, configIntegWebTc.getChaves(), configIntegWebTc.isRemessa(), configIntegWebTc.isRetorno(), configIntegWebTc.isAtivo());
		}
	}
	
	public static void createSyncInfo(String tableName, String[] chaves, boolean flRemessa, boolean flRetorno, boolean flAtivo) {
		createSyncInfo(infoAtualizacao, tableName, chaves, flRemessa, flRetorno, flAtivo);
	}

	public static void createSyncInfo(Hashtable hash, String tableName, String[] chaves, boolean flRemessa, boolean flRetorno, boolean flAtivo) {
		SyncInfo syncInfo = new SyncInfo();
		syncInfo.tableName = tableName.toUpperCase();
		syncInfo.keys = chaves;
		syncInfo.flRemessa = flRemessa;
		syncInfo.flRetorno = flRetorno;
		syncInfo.flAtivo = flAtivo;
		hash.put(syncInfo.tableName, syncInfo);
	}

	public static Hashtable getInfoAtualizacaoPedidos() throws SQLException {
		Hashtable hashPedido = new Hashtable(20);
		//--
		Vector web2SyncList = ConfigIntegWebTcService.getInstance().findAll();
		int size = web2SyncList.size();
		Hashtable hashWeb2Sync = new Hashtable((int)(size * 1.2));
		ConfigIntegWebTc configIntegWebTc;
		StringBuffer strBuffer = new StringBuffer(512);
        for (int i = 0; i < size; i++) {
        	configIntegWebTc = (ConfigIntegWebTc)web2SyncList.items[i];
        	strBuffer.setLength(0);
        	strBuffer.append(configIntegWebTc.dsTabela);
			hashWeb2Sync.put(strBuffer.toString(), configIntegWebTc);
		}
        //--
		createSyncInfo(hashPedido, (ConfigIntegWebTc)hashWeb2Sync.get(NovoCliente.TABLE_NAME));
		createSyncInfo(hashPedido, (ConfigIntegWebTc)hashWeb2Sync.get(Pedido.TABLE_NAME_PEDIDO));
		createSyncInfo(hashPedido, (ConfigIntegWebTc)hashWeb2Sync.get(ItemPedido.TABLE_NAME_ITEMPEDIDO));
		createSyncInfo(hashPedido, (ConfigIntegWebTc)hashWeb2Sync.get(ItemKitPedido.TABLE_NAME_ITEMKITPEDIDO));
		createSyncInfo(hashPedido, (ConfigIntegWebTc)hashWeb2Sync.get(ItemPedidoGrade.TABLE_NAME_ITEMPEDIDOGRADE));
		createSyncInfo(hashPedido, (ConfigIntegWebTc)hashWeb2Sync.get(ParcelaPedido.TABLE_NAME));
		if (LavenderePdaConfig.isUsaColetaGpsPontosEspecificosSistema() || LavenderePdaConfig.isColetaDadosGpsRepresentante()) {
			createSyncInfo(hashPedido, (ConfigIntegWebTc)hashWeb2Sync.get(PontoGps.TABLE_NAME));
		}
		if (LavenderePdaConfig.enviaInformacoesVisitaOnline) {
			createSyncInfo(hashPedido, (ConfigIntegWebTc)hashWeb2Sync.get(Visita.TABLE_NAME));
			createSyncInfo(hashPedido, (ConfigIntegWebTc)hashWeb2Sync.get(VisitaPedido.TABLE_NAME));
			createSyncInfo(hashPedido, (ConfigIntegWebTc)hashWeb2Sync.get(VisitaFotoDao.TABLE_NAME));
			createSyncInfo(hashPedido, (ConfigIntegWebTc)hashWeb2Sync.get(NotificacaoPda.TABLE_NAME));
		}
		if (LavenderePdaConfig.usaDescontoPonderadoPedido && LavenderePdaConfig.usaMultiplasLiberacoesDescontoNoPedido()) {
			createSyncInfo(hashPedido, (ConfigIntegWebTc)hashWeb2Sync.get(PedidoDesc.TABLE_NAME));
			if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacaoMaxDescItem()) {
				createSyncInfo(hashPedido, (ConfigIntegWebTc)hashWeb2Sync.get(ItemLiberacao.TABLE_NAME));
			}
		}
		if (LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento()) {
			createSyncInfo(hashPedido, (ConfigIntegWebTc)hashWeb2Sync.get(Nfe.TABLE_NAME));
			createSyncInfo(hashPedido, (ConfigIntegWebTc)hashWeb2Sync.get(ItemNfe.TABLE_NAME));
		}
		if (LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
			createSyncInfo(hashPedido, (ConfigIntegWebTc)hashWeb2Sync.get(VerbaVigenciaPedido.TABLE_NAME));
		}
		if (LavenderePdaConfig.usaControlePontuacao) {
			createSyncInfo(hashPedido, (ConfigIntegWebTc)hashWeb2Sync.get(PontExtItemPed.TABLE_NAME));
			createSyncInfo(hashPedido, (ConfigIntegWebTc)hashWeb2Sync.get(PontExtPed.TABLE_NAME));
		}
		if (LavenderePdaConfig.isUsaPoliticaBonificacao()) {
			createSyncInfo(hashPedido, (ConfigIntegWebTc) hashWeb2Sync.get(ItemPedidoBonifCfg.TABLE_NAME));
		}

        //--
        return hashPedido;
	}

	public static Hashtable getInfoAtualizacaoRetornoPedidos() throws SQLException {
		Hashtable hashPedido = new Hashtable(20);
		Hashtable hashWeb2Sync = getConfigIntegWebTcHash();
		//--
		createSyncInfo(hashPedido, (ConfigIntegWebTc)hashWeb2Sync.get(Pedido.TABLE_NAME_PEDIDOERP));
		createSyncInfo(hashPedido, (ConfigIntegWebTc)hashWeb2Sync.get(ItemPedido.TABLE_NAME_ITEMPEDIDOERP));
		createSyncInfo(hashPedido, (ConfigIntegWebTc)hashWeb2Sync.get(PedidoErpDif.TABLE_NAME));
		createSyncInfo(hashPedido, (ConfigIntegWebTc)hashWeb2Sync.get(ItemPedidoErpDif.TABLE_NAME));
		createSyncInfo(hashPedido, (ConfigIntegWebTc)hashWeb2Sync.get(Estoque.TABLE_NAME));
		//--
		return hashPedido;
	}

	private static Hashtable getConfigIntegWebTcHash() throws SQLException {
		Vector web2SyncList = ConfigIntegWebTcService.getInstance().findAll();
		int size = web2SyncList.size();
		Hashtable hashWeb2Sync = new Hashtable((int)(size * 1.2));
		ConfigIntegWebTc configIntegWebTc;
		StringBuffer strBuffer = new StringBuffer(512);
		for (int i = 0; i < size; i++) {
			configIntegWebTc = (ConfigIntegWebTc)web2SyncList.items[i];
			strBuffer.setLength(0);
			strBuffer.append(configIntegWebTc.dsTabela);
			hashWeb2Sync.put(strBuffer.toString(), configIntegWebTc);
		}
		return hashWeb2Sync;
	}

	public static Hashtable getInfoAtualizacao(boolean nfe, boolean boleto, boolean erpDif, boolean nfce) throws SQLException {
		Vector web2SyncList = new Vector();
		//Nfe
		ConfigIntegWebTc configIntegWebTcFilter;
		configIntegWebTcFilter = new ConfigIntegWebTc();
		if (nfe) {
			//Nfe
			configIntegWebTcFilter.dsTabela = Nfe.TABLE_NAME;
			ConfigIntegWebTc webTcNfe = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
			web2SyncList.addElement(webTcNfe);
			//ItemNfe
			configIntegWebTcFilter.dsTabela = LavenderePdaConfig.usaNfePorReferencia ? ItemNfeReferencia.TABLE_NAME : ItemNfe.TABLE_NAME;
			ConfigIntegWebTc webTcItemNfe = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
			web2SyncList.addElement(webTcItemNfe);
		}
		if (boleto) {
			//PedidoBoleto
			configIntegWebTcFilter.dsTabela = PedidoBoleto.TABLE_NAME;
			ConfigIntegWebTc webTcPedidoBoleto = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
			web2SyncList.addElement(webTcPedidoBoleto);
		}
		if (erpDif) {
			//PedidoErpDif
			configIntegWebTcFilter.dsTabela = PedidoErpDif.TABLE_NAME;
			ConfigIntegWebTc webTcPedidoErpDif = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
			web2SyncList.addElement(webTcPedidoErpDif);
			//ItemPedidoErpDif
			configIntegWebTcFilter.dsTabela = ItemPedidoErpDif.TABLE_NAME;
			ConfigIntegWebTc webTcItemPedidoErpDif = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
			web2SyncList.addElement(webTcItemPedidoErpDif);
			//PedidoErp
			configIntegWebTcFilter.dsTabela = Pedido.TABLE_NAME_PEDIDOERP;
			ConfigIntegWebTc webTcPedidoErp = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
			web2SyncList.addElement(webTcPedidoErp);
			//ItemPedidoErp
			configIntegWebTcFilter.dsTabela = ItemPedido.TABLE_NAME_ITEMPEDIDOERP;
			ConfigIntegWebTc webTcItemPedidoErp = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
			web2SyncList.addElement(webTcItemPedidoErp);
			//ItemPedidoGradeErp
			configIntegWebTcFilter.dsTabela = ItemPedidoGrade.TABLE_NAME_ITEMPEDIDOGRADEERP;
			ConfigIntegWebTc webTcItemPedidoGradeErp = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
			web2SyncList.addElement(webTcItemPedidoGradeErp);
		}
		if (nfce) {
			//Nfce
			configIntegWebTcFilter.dsTabela = Nfce.TABLE_NAME;
			ConfigIntegWebTc webTcNfce = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
			web2SyncList.addElement(webTcNfce);
			//ItemNfce
			configIntegWebTcFilter.dsTabela = ItemNfce.TABLE_NAME;
			ConfigIntegWebTc webTcItemNfce = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
			web2SyncList.addElement(webTcItemNfce);
		}
		return getInfoAtualizacaoByWeb2SyncList(web2SyncList);
	}

	public static Hashtable getInfoAtualizacaoUsuario() throws SQLException {
		Hashtable hashUsuario = new Hashtable(20);
		Hashtable hashWeb2Sync = getConfigIntegWebTcHash();
		createSyncInfo(hashUsuario, (ConfigIntegWebTc)hashWeb2Sync.get(UsuarioLavendere.TABLE_NAME));
		createSyncInfo(hashUsuario, (ConfigIntegWebTc)hashWeb2Sync.get(UsuarioHistAlteracaoLavendere.TABLE_NAME));
		return hashUsuario;
	}

	public static Hashtable getInfoAtualizacaoPedidoDesc() throws SQLException {
		Vector web2SyncList = new Vector();
		ConfigIntegWebTc configIntegWebTcFilter = new ConfigIntegWebTc();
		//PedidoDesc
		configIntegWebTcFilter.dsTabela = PedidoDesc.TABLE_NAME;
		ConfigIntegWebTc webTcPedidoDesc = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
		web2SyncList.addElement(webTcPedidoDesc);
		return getInfoAtualizacaoByWeb2SyncList(web2SyncList);
	}

	public static Hashtable getInfoAtualizacaoSolAutorizacao(boolean addNovidade) throws SQLException {
		Vector web2SyncList = new Vector();
		ConfigIntegWebTc configIntegWebTcFilter = new ConfigIntegWebTc();
		configIntegWebTcFilter.dsTabela = SolAutorizacao.TABLE_NAME;
		web2SyncList.addElement(ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey()));
		if (LavenderePdaConfig.geraNovidadeAutorizacao && addNovidade) {
			configIntegWebTcFilter.dsTabela = RelNovSolAutorizacao.TABLE_NAME;
			web2SyncList.addElement(ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey()));
		}
		return getInfoAtualizacaoByWeb2SyncList(web2SyncList);
	}

	public static Hashtable getInfoAtualizacaoAtualizacaoBd() throws SQLException {
		Vector web2SyncList = new Vector();
		ConfigIntegWebTc configIntegWebTcFilter = new ConfigIntegWebTc();
		//AtualizacaoBd
		configIntegWebTcFilter.dsTabela = AtualizacaoBdLavendereDbxDao.TABLE_NAME;
		ConfigIntegWebTc configIntegWebTcAtualizacaoBd = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
		web2SyncList.addElement(configIntegWebTcAtualizacaoBd);
		return getInfoAtualizacaoByWeb2SyncList(web2SyncList);
	}

	public static Hashtable getInfoAtualizacaoEstoqueRep() throws SQLException {
		Vector web2SyncList = new Vector();
		ConfigIntegWebTc configIntegWebTcFilter = new ConfigIntegWebTc();
		//EstoqueRep
		configIntegWebTcFilter.dsTabela = EstoqueRep.TABLE_NAME;
		ConfigIntegWebTc webTcPedidoDesc = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
		web2SyncList.addElement(webTcPedidoDesc);
		return getInfoAtualizacaoByWeb2SyncList(web2SyncList);
	}
	
	public static Hashtable getInfoAtualizacaoEstoque() throws SQLException {
		Vector web2SyncList = new Vector();
		ConfigIntegWebTc configIntegWebTcFilter = new ConfigIntegWebTc();
		//EstoqueRep
		configIntegWebTcFilter.dsTabela = Estoque.TABLE_NAME;
		ConfigIntegWebTc webTcPedidoDesc = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
		web2SyncList.addElement(webTcPedidoDesc);
		return getInfoAtualizacaoByWeb2SyncList(web2SyncList);
	}

	public static Hashtable getInfoAtualizacaoEstoquePrevistoGeral() throws SQLException {
		Vector web2SyncList = new Vector();
		ConfigIntegWebTc configIntegWebTcFilter = new ConfigIntegWebTc();
		//EstoqueRep
		configIntegWebTcFilter.dsTabela = EstoquePrevistoGeral.TABLE_NAME;
		ConfigIntegWebTc webTcPedidoDesc = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
		web2SyncList.addElement(webTcPedidoDesc);
		return getInfoAtualizacaoByWeb2SyncList(web2SyncList);
	}

	public static Hashtable getInfoAtualizacaoEstoquePrevisto() throws SQLException {
		Vector web2SyncList = new Vector();
		ConfigIntegWebTc configIntegWebTcFilter = new ConfigIntegWebTc();
		//EstoqueRep
		configIntegWebTcFilter.dsTabela = EstoquePrevisto.TABLE_NAME;
		ConfigIntegWebTc webTcPedidoDesc = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
		web2SyncList.addElement(webTcPedidoDesc);
		return getInfoAtualizacaoByWeb2SyncList(web2SyncList);
	}
	
	public static Hashtable getInfoAtualizacaoItemLiberacao() throws SQLException {
		Vector web2SyncList = new Vector();
		ConfigIntegWebTc configIntegWebTcFilter = new ConfigIntegWebTc();
		configIntegWebTcFilter.dsTabela = ItemLiberacao.TABLE_NAME;
		ConfigIntegWebTc webTcItemLiberacao = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
		web2SyncList.addElement(webTcItemLiberacao);
		return getInfoAtualizacaoByWeb2SyncList(web2SyncList);
	}
	
	public static void limpeCaches() {
		AgendaVisitaDbxDao.getInstance().clearCache();
		AreaVendaPdbxDao.getInstance().clearCache();
		CCCliPorTipoPdbxDao.getInstance().clearCache();
		CestaPdbxDao.getInstance().clearCache();
		CestaPositClientePdbxDao.getInstance().clearCache();
		CestaPositProdutoPdbxDao.getInstance().clearCache();
		CestaProdutoPdbxDao.getInstance().clearCache();
        ClientePdbxDao.getInstance().clearCache();
        CondicaoPagamentoPdbxDao.getInstance().clearCache();
        CondTipoPagtoPdbxDao.getInstance().clearCache();
        CondPagtoCliPdbxDao.getInstance().clearCache();
        CondPagtoLinhaPdbxDao.getInstance().clearCache();
        CondPagtoTabPrecoPdbxDao.getInstance().clearCache();
        ConexaoPdaPdbxDao.getInstance().clearCache();
        CampoLavendereDbxDao.getInstance().clearCache();
        ConfigInternoPdbxDao.getInstance().clearCache();
        ConfigIntegWebTcDbxDao.getInstance().clearCache();
        ContaCorrenteCliDbxDao.getInstance().clearCache();
		ContratoClienteDbxDao.getInstance().clearCache();
		ContratoProdEstDbxDao.getInstance().clearCache();
		ContratoProdutoDbxDao.getInstance().clearCache();
        DescontoCCPPdbxDao.getInstance().clearCache();
        DescProgressivoPdbxDao.getInstance().clearCache();
		DescQuantidadePdbxDao.getInstance().clearCache();
		DescComiFaixaPdbxDao.getInstance().clearCache();
		DescPromocionalDbxDao.getInstance().clearCache();
        EmpresaPdbxDao.getInstance().clearCache();
        EstoquePdbxDao.getInstance().clearCache();
        FaceamentoPdbxDao.getInstance().clearCache();
        FaceamentoEstoquePdbxDao.getInstance().clearCache();
        FichaFinanceiraPdbxDao.getInstance().clearCache();
        FornecedorPdbxDao.getInstance().clearCache();
        GrupoProduto1PdbxDao.getInstance().clearCache();
        GrupoProduto2PdbxDao.getInstance().clearCache();
        GrupoProduto3PdbxDao.getInstance().clearCache();
        IndicadorPdbxDao.getInstance().clearCache();
        ItemKitPdbxDao.getInstance().clearCache();
        ItemPedidoPdbxDao.getInstance().clearCache();
        ItemTabelaPrecoPdbxDao.getInstance().clearCache();
        KitPdbxDao.getInstance().clearCache();
        KitTabPrecoPdbxDao.getInstance().clearCache();
        LogPdaPdbxDao.getInstance().clearCache();
        MetaPdbxDao.getInstance().clearCache();
        MotRegistroVisitaPdbxDao.getInstance().clearCache();
        MotivoTrocaPdbxDao.getInstance().clearCache();
        ParcelaPedidoPdbxDao.getInstance().clearCache();
        PedidoPdbxDao.getInstance().clearCache();
		PontuacaoPdbxDao.getInstance().clearCache();
        ProdutoPdbxDao.getInstance().clearCache();
        ProdutoTabPrecoPdbxDao.getInstance().clearCache();
        ProdutoGradeDbxDao.getInstance().clearCache();
        RecadoPdbxDao.getInstance().clearCache();
        RelInadimpCliPdbxDao.getInstance().clearCache();
        RelInadimpRepPdbxDao.getInstance().clearCache();
        RelNovidadeProdPdbxDao.getInstance().clearCache();
        RepresentanteEmpPdbxDao.getInstance().clearCache();
        RepresentantePdbxDao.getInstance().clearCache();
        RotaEntregaPdbxDao.getInstance().clearCache();
        StatusPedidoPdaPdbxDao.getInstance().clearCache();
        StatusRentCliDbxDao.getInstance().clearCache();
        SupervisorRepPdbxDao.getInstance().clearCache();
        TabelaPrecoCliPdbxDao.getInstance().clearCache();
        TabelaPrecoPdbxDao.getInstance().clearCache();
        TabelaPrecoRepPdbxDao.getInstance().clearCache();
        TipoNovidadeDao.getInstance().clearCache();
        TipoPagamentoPdbxDao.getInstance().clearCache();
        TipoPedidoPdbxDao.getInstance().clearCache();
        TipoFretePdbxDao.getInstance().clearCache();
        TituloFinanceiroPdbxDao.getInstance().clearCache();
        UsuarioPdaPdbxDao.getInstance().clearCache();
        UsuarioWebPdbxDao.getInstance().clearCache();
        ValorIndicadorPdbxDao.getInstance().clearCache();
        VerbaPdbxDao.getInstance().clearCache();
        VerbaClientePdbxDao.getInstance().clearCache();
        VerbaContaCorPdbxDao.getInstance().clearCache();
        VerbaItemPedidoPdbxDao.getInstance().clearCache();
        VerbaProdutoPdbxDao.getInstance().clearCache();
        VerbaSaldoPdbxDao.getInstance().clearCache();
        VerbaTabelaPrecoPdbxDao.getInstance().clearCache();
        VisitaPdbxDao.getInstance().clearCache();
        VisitaSupervisorPdbxDao.getInstance().clearCache();
        SugVendaPersonCliDbxDao.getInstance().clearCache();
        SugVendaPersonDbxDao.getInstance().clearCache();
        SugVendaPersonProdDbxDao.getInstance().clearCache();
        GrupoProdFornDao.getInstance().clearCache();
	}

	public static boolean recebaDados(ParamsSync ps) throws Exception {
		return recebaDados(ps, getInfoAtualizacao(true));
	}
	
	public static boolean recebaDados(ParamsSync ps, Hashtable hashTablesRecebimento) throws Exception {
		return recebaDados(ps, hashTablesRecebimento, false);
	}

	public static boolean recebaDados(ParamsSync ps, Hashtable hashTablesRecebimento, boolean ignoreAntesEDepoisReceberDados) throws Exception {
		LogSyncTimer timer = new LogSyncTimer("Início recebimento dados... ", "Fim recebimento dados... ").newLogOnFinish();
		try {
	        LogPdaService.getInstance().logMemoria(Messages.LOGPDA_MSG_MEMORIA_ANTES_SYNC_RECECER);
			LavendereWeb2Tc web2Tc = new LavendereWeb2Tc(ps);
			web2Tc.ignoreAntesEDepoisReceberDados = ignoreAntesEDepoisReceberDados;
			try {
				web2Tc.bloqueiaSyncPalmPendente = true;
				web2Tc.recebeDadosDisponiveisServidor(hashTablesRecebimento);
				web2Tc.processaRecebimentoArquivos();
				if (!web2Tc.houveErroRecebimento) {
					salvaDataHoraUltimoRecebimentoSucesso();
				}
			} catch (Throwable ex) {
				LogSync.error(ex.getMessage());
				LogPdaService.getInstance().logSyncError(ex.getMessage());
				throw ex;
			} finally {
				aposReceberDados(web2Tc.tabelasAtualizadasSucesso);
			}
	        limpeCaches();
			LogPdaService.getInstance().logMemoria(Messages.LOGPDA_MSG_MEMORIA_DEPOIS_SYNC_RECECER);
			return web2Tc.houveErroRecebimento;
		} finally {
			timer.finish();
		}
	}

	public static void salvaDataHoraUltimoRecebimentoSucesso() throws SQLException {
		if (LavenderePdaConfig.isObrigaReceberDadosBloqueiaNovoPedido() || LavenderePdaConfig.isObrigaReceberDadosBloqueiaUsoSistema() || LavenderePdaConfig.usaRecalculoValoresDosPedidos) {
			ConfigInternoService.getInstance().addValueGeral(ConfigInterno.dataHoraUtimoRecebimentoDados, StringUtil.getStringValue(TimeUtil.getTimeAsLong()));
		}
	}
	
	public static void aposReceberDados(Set<String> tables) throws SQLException {
		if (tables.isEmpty()) {
			return;
		}
		boolean isPrimeiroSyncDia = isPrimeiroReceberDadosDoDia();
		for (String tableName : tables) {
			try {
				processaTabela(tableName, isPrimeiroSyncDia);
			} catch (Throwable e) {
				LogSync.error(e.getMessage());
			}
		}
	}

	private static void processaTabela(String tableName, boolean isPrimeiroSyncDia) throws SQLException {
		switch (tableName) {
		case EquipamentoUsuarioLavendere.TABLE_NAME:
			if (!EquipamentoUsuarioLavendereService.getInstance().verificaEquipamentoLiberadoUsuario(HttpConnectionManager.getDefaultParamsSync())) {
				LogSync.error(Messages.ERRO_EQUIPAMENTO_NAO_LIBERADO);
				NotificationManager.putNotification(SyncActions.getUsuarioBloqueadoNotification());
			}
			break;
		case ValorParametro.TABLE_NAME:
			reloadSessionInfos();
			if (LavenderePdaConfig.usaConfigAcessoSistema) {
				SessionLavenderePda.validaAcessoSistema = true;
			}
			break;
		case ConfigIntegWebTc.TABLE_NAME:
			SyncManager.clearInfoAtualizacao();
			break;
		case CampoLavendereDbxDao.TABLE_NAME:
			CampoLavendereService.getCampoLavendereInstance().loadConfigPersonCadList();
			break;
		case ReagendaAgendaVisita.TABLE_NAME:
			ReagendaAgendaVisitaService.getInstance().criaVisitaAgendaReagendadaServidor();
			break;
		case VerbaCliente.TABLE_NAME:
			VerbaClienteService.getInstance().recalculateAndUpdateVerbaClientePda();
			break;
		case RentabilidadeSaldo.TABLE_NAME:
			RentabilidadeSaldoService.getInstance().recalculateAndUpdateRentabilidadeSaldoPda();
			break;
		case BonificacaoSaldo.TABLE_NAME:
			BonificacaoSaldoService.getInstance().recalculaSaldoPda();
			break;
		case VerbaSaldo.TABLE_NAME:
		case VerbaFornecedor.TABLE_NAME:
		case VerbaGrupoSaldo.TABLE_NAME:
		case VerbaUsuario.TABLE_NAME:
		case VerbaSaldoVigencia.TABLE_NAME:
			if (LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0) {
				VerbaSaldoService.getInstance().recalculateVerbaSaldoPdaForPedidos();
			}
			if (LavenderePdaConfig.isLigadoAlgumaRegraVerba()) {
				VerbaService.getInstance().recalculateAndUpdateVerbaPda();
			}
			break;
		case TipoPedido.TABLE_NAME:
			if (LavenderePdaConfig.isPermiteInserirMultiplosItensPorVezNoPedido() && LavenderePdaConfig.isOcultaInterfaceNegociacaoMultiplosItens()) {
				CadItemPedidoForm.invalidateInstance();
			}
			break;
		case Estoque.TABLE_NAME:
			if (LavenderePdaConfig.atualizarEstoqueInterno) {
				EstoqueService.getInstance().recalculaEstoqueConsumido(null, true);
			}
			if (LavenderePdaConfig.isEnviaEstoqueRepParaServidor()) {
				ConfigInternoService.getInstance().removeConfigInterno(ConfigInterno.devolverEstoqueAtual, SessionLavenderePda.cdEmpresa + SessionLavenderePda.getRepresentante().cdRepresentante);
			}
			break;
		case LimiteOportunidade.TABLE_NAME:
			if (LavenderePdaConfig.usaControleSaldoOportunidade) {
				LimiteOportunidadeService.getInstance().recalculateAndUpdateLimiteOportunidadePda();
			}
			break;
		case ContatoErp.TABLE_NAME:
			ContatoService.getInstance().deleteContatosPdaByContatosErp();
			break;
		case FotoClienteErp.TABLE_NAME:
			FotoClienteService.getInstance().deleteFotoClienteByFotoClienteErp();
			break;
		case Representante.TABLE_NAME:
			RepresentanteService.getInstance().updateRepresentanteInSession();
			break;
		case UsuarioDesc.TABLE_NAME:
			if (LavenderePdaConfig.usaDescontoPonderadoPedido && LavenderePdaConfig.restringeDescontoPedidoBaseadoMediaPonderada) {
				UsuarioDescService.getInstance().recalculaAndUpdateUsuarioDescPda();
			}
			break;
		case PreferenciaFuncao.TABLE_NAME:
			SessionLavenderePda.loadPreferenciaFuncaoMap();
			break;
		case Pedido.TABLE_NAME_PEDIDOERP:
		case ItemPedidoErpDif.TABLE_NAME:
			if (LavenderePdaConfig.usaEstoqueOnline && !LavenderePdaConfig.geraDiferencasPedidoPdaIntegracaoPorView) {
				ItemPedidoErpDifService.getInstance().updateFlagDiferencaPedidos();
			}
			if (Pedido.TABLE_NAME_PEDIDOERP.equals(tableName)) {
				LogSyncTimer timer = new LogSyncTimer(Messages.PEDIDO_MSG_ATUALIZANDO);
				try {
					PedidoService.getInstance().deletePedidosPdaByPedidosErp();
				} finally {
					timer.finish();
				}
			}
			if (!LavenderePdaConfig.geraNovoPedidoDiferencas && LavenderePdaConfig.relDiferencasPedido) {
				NotificationManager.putNotification(new Notification(LavendereNotificationConstants.REL_DIFERENCA_PEDIDO) {
					@Override
					public void process() throws Exception {
						SyncActions.showRelDiferencaPedidos();
					}
				});
			}
			if (LavenderePdaConfig.geraNovoPedidoDiferencas) {
				NotificationManager.putNotification(new Notification(LavendereNotificationConstants.LIST_PEDIDO_NOVOPEDIDO_DIF) {
					@Override
					public void process() throws Exception {
						SyncActions.showListPedidoParaGerarNovoPedidoDiferencas();
					}
				});
			}
			break;
		case Cliente.TABLE_NAME:
			LogSync.info(Messages.NOVOCLIENTE_MSG_ATUALIZANDO);
			NotificationManager.putNotification(new Notification(LavendereNotificationConstants.ATUALIZACAO_CLIENTE, Messages.NOVOCLIENTE_MSG_ATUALIZANDO) {
				@Override
				public void process() throws Exception {
					SyncActions.showRelNovoCliente();
				}
			});
			break;
		case DivulgaInfo.TABLE_NAME:
			if (LavenderePdaConfig.nuApresentaDivulgacao > 0) {
				DivulgaInfoService.getInstance().resetaVisualizacoesCliente();
			}
			break;
		case DescPromocional.TABLE_NAME:
			DescPromocionalDbxDao.getInstance().clearCache();
			DescPromocionalService.getInstance().clearDescPromoPorProdutoHash();
			break;
		case PoliticaComercial.TABLE_NAME:
		case PoliticaComercialFaixa.TABLE_NAME:
			if (LavenderePdaConfig.usaPoliticaComercial()) {
				PedidoService.getInstance().reloadPoliticaComercialItensByPedidosAberto();
				LogSync.sucess(Messages.POLITICAS_COMERCIAIS_ATUALIZADAS_SINCRONIZACAO);
			}
			break;
		case UsuarioPda.TABLE_NAME:
		case MenuLavendereDbxDao.TABLE_NAME_LVP:
		case AutorizacaoLavendereDbxDao.TABLE_NAME_LVP:
		case TelaLavendereDbxDao.TABLE_NAME_LVP:
			remontaMenu();
			break;
		case PlatVendaMeta.TABLE_NAME:
			if (LavenderePdaConfig.usaConfigCadastroMetasPlataformaVenda()) {
				PlatVendaMetaAtuaService.getInstance().deleteAllEnviadosServidor();
			}
			break;
		case Nfe.TABLE_NAME:
		case PedidoBoleto.TABLE_NAME:
		case Nfce.TABLE_NAME:
			LavenderePdaConfig.loadParametrosExtras();
			break;
		case Recado.TABLE_NAME:
			if (LavenderePdaConfig.usaMuralDeRecados && RecadoService.getInstance().existeRecadosNaoLidos()) {
				NotificationManager.putNotification(new Notification(LavendereNotificationConstants.OPEN_RECADO) {
					@Override
					public void process() throws Exception {
						SyncActions.showListRecado();
					}
				});
			}
			break;
		case RelNovidadeProd.TABLE_NAME:
		case RelNovidadeCli.TABLE_NAME:
		case RelNovidadeNovoCli.TABLE_NAME:
		case RelNovidadePesquisaMercado.TABLE_NAME:
		case RelNovSolAutorizacao.TABLE_NAME:
			if (LavenderePdaConfig.isMostraRelNovidadeAposPrimeiroSync(isPrimeiroSyncDia)) {
				NotificationManager.putNotification(new Notification(LavendereNotificationConstants.OPEN_NOVIDADE) {
					@Override
					public void process() throws Exception {
						SyncActions.showNovidades();
					}
				});
			}
			break;
		case ValorIndicador.TABLE_NAME:
			if (LavenderePdaConfig.mostraRelIndicadoresDeProdutividadeAposSync) {
				NotificationManager.putNotification(new Notification(LavendereNotificationConstants.OPEN_RELINDICADOR) {
					@Override
					public void process() throws Exception {
						SyncActions.showListValorIndicador();
					}
				});
			}
			break;
		case ItemTabelaPreco.TABLE_NAME:
			if (isPrimeiroSyncDia && LavenderePdaConfig.nuDiasValidadeCustoItem > 0) {
				NotificationManager.putNotification(new Notification(LavendereNotificationConstants.OPEN_ITEMCUSTO) {
					@Override
					public void process() throws Exception {
						SyncActions.showAtualizacaoItemTabelaPreco();
					}
				});
			}
			break;
		case InfoEntregaPed.TABLE_NAME:
			if (LavenderePdaConfig.usaRelInfoEntregaPedido) {
				NotificationManager.putNotification(new Notification(LavendereNotificationConstants.OPEN_RELINFOENTREGA) {
					@Override
					public void process() throws Exception {
						SyncActions.showRelInfoEntregaPedido();
					}
				});
			}
			break;
		case NovoClienteAna.TABLE_NAME:
			if (NovoClienteAnaService.getInstance().hasNovoClientePendenteEdicao()) {
				NotificationManager.putNotification(new Notification(LavendereNotificationConstants.OPEN_NOVOCLIANALISE) {
					@Override
					public void process() throws Exception {
						SyncActions.showNovoClienteAnalise();
					}
				});
			}
			break;
		case ClienteChurn.TABLE_NAME:
			if (LavenderePdaConfig.usaConfigModuloRiscoChurn()) {
				NotificationManager.putNotification(new Notification(LavendereNotificationConstants.AVISO_CLIENTE_CHURN) {
					@Override
					public void process() throws Exception {
						SyncActions.exibeMensagemClienteChurn();
					}
				});
			}
			break;
		case AgendaVisita.TABLE_NAME:
			if (LavenderePdaConfig.isRegistraAgendaCliHist()) {
				AgendaVisitaCliHistService.getInstance().geraAgendaHistCli();
			}
			break;
		case ConfigAcessoSistema.TABLE_NAME:
		case Feriado.TABLE_NAME:
		case FuncaoConfig.TABLE_NAME:
			if (LavenderePdaConfig.usaConfigAcessoSistema) {
				SessionLavenderePda.validaAcessoSistema = true;
			}
			break;
		default:
			break;
		}
	}
	
	public static boolean isPrimeiroReceberDadosDoDia() throws SQLException {
		String vlConfigInterno = ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.dataPrimeiroRecebimentoDados);
		if (vlConfigInterno != null) {
			if (!vlConfigInterno.equals(DateUtil.getCurrentDateYYYYMMDD())) {
				ConfigInternoService.getInstance().addValue(ConfigInterno.dataPrimeiroRecebimentoDados, DateUtil.getCurrentDateYYYYMMDD());
				return true;
			}
		} else {
			ConfigInternoService.getInstance().addValue(ConfigInterno.dataPrimeiroRecebimentoDados, DateUtil.getCurrentDateYYYYMMDD());
			return true;
		}
		return false;
	}

	private static void reloadSessionInfos() throws SQLException {
		SessionLavenderePda.hashTabelasRepresentanteSupervisor.clear();
		LavenderePdaConfig.loadParametros();
		CadItemPedidoForm.invalidateInstance();
		ListItemPedidoForm.invalidateInstance();
		SessionLavenderePda.startBackgroundServices();
	}

	private static void remontaMenu() throws SQLException {
		MainMenu menuP = MainLavenderePda.getInstance().instanceMenuPrincipal();
		MainLavenderePda.getInstance().setNewMainForm(menuP, false);
	}

	public static boolean envieDados(ParamsSync ps) throws Exception {
        return envieDados(ps, getInfoAtualizacao(true));
	}

	public static boolean envieDados(ParamsSync ps, Hashtable hashTablesEnvio) throws Exception {
		LogSyncTimer timer = new LogSyncTimer("Enviando dados...", "Envio dados finalizado").newLogOnFinish();
		try {
			enviaDocumentoAnexo(ps);
			enviaImagensVisitas(ps);
			enviaImagensClientes(ps);
			enviaImagensNovosClientes(ps);
			enviaImagensPedidos(ps);
			enviaImagensPesquisaMercado(ps);
			enviaImagensProspect(ps);
			enviaImagensPesquisaMercadoProdutoConcorrente(ps);
			enviaImagensDapLaudo(ps);
			enviaImagensRequisicaoServ(ps);
			enviaImagensPesquisaRespApp(ps);
			enviaLogsWGpsDoDiaServidor(ps);
			enviaImagensFotosItemTroca(ps);
			LavendereTc2Web pdaToErp = new LavendereTc2Web(ps);
			pdaToErp.conectaEnviaDadosServidorFull(hashTablesEnvio);
			return pdaToErp.houveErroSincronizacao;
		} finally {
			timer.finish();
		}
	}

	private static void enviaImagensFotosItemTroca(ParamsSync ps) throws Exception {
		try {
			if (ValueUtil.isNotEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher)) {
				Vector imageList = FotoItemTrocaService.getInstance().getImageListToSync();
				if (ValueUtil.isNotEmpty(imageList)) {
					LavendereTc2Web pdaToErpHttp = new LavendereTc2Web(ps);
					pdaToErpHttp.envieImagens(FotoItemTroca.getPathImg(), imageList);
					LogSync.sucess(Messages.SYNC_IMAGES_PEDIDO_SUCESSO);
				}
			}
		} catch (Throwable ex) {
			LogSync.error(Messages.SYNC_IMAGES_PEDIDO_ERRO + ex.getMessage());
			throw ex;
		}
	}

	private static void enviaImagensRequisicaoServ(ParamsSync ps) throws Exception {
		try {
			if (MainMenu.isTelaAutorizada(MainMenu.CDTELA_REQUISICAO_SERV)) {
				Vector imageList = RequisicaoServImagemService.getInstance().getImageListToSync();
				if (ValueUtil.isNotEmpty(imageList)) {
					LavendereTc2Web pdaToErpHttp = new LavendereTc2Web(ps);
					pdaToErpHttp.envieImagens(RequisicaoServImagem.getPathImg(), imageList, RequisicaoServImagem.TABLE_NAME, false);
					LogSync.info(Messages.SYNC_IMAGES_REQSERV_SUCESSO);
				}
			}
		} catch (Throwable ex) {
			LogSync.info(Messages.SYNC_IMAGES_REQSERV_ERRO + ex.getMessage());
			throw ex;
		}
	}

	public static void enviaImagensDapLaudo(ParamsSync ps) throws Exception {
		try {
			if (LavenderePdaConfig.isUsaLaudoDap()) {
				Vector imageList = DapLaudoAtuaService.getInstance().getImageListToSync();
				if (ValueUtil.isNotEmpty(imageList)) {
					LavendereTc2Web pdaToErpHttp = new LavendereTc2Web(ps);
					pdaToErpHttp.envieImagens(DapLaudo.getImagePath(), imageList, DapLaudoAtua.TABLE_NAME, false);
					LogSync.info(Messages.SYNC_IMAGES_DAPLAUDOATUA);
				}
			}
		} catch (Throwable ex) {
			LogSync.info(Messages.SYNC_IMAGES_DAPLAUDOATUA_ERRO + ex.getMessage());
			throw ex;
		}
	}

	private static void enviaImagensPesquisaMercadoProdutoConcorrente(ParamsSync ps) throws Exception {
		try {
			if (LavenderePdaConfig.usaInclusaoFotoProdPesquisaMercado() || LavenderePdaConfig.usaInclusaoFotoPesquisaMercado()) {
				Vector imageList = FotoPesqMerProdConcService.getInstance().getImageListToSync();
				if (ValueUtil.isNotEmpty(imageList)) {
					LavendereTc2Web pdaToErpHttp = new LavendereTc2Web(ps);
					pdaToErpHttp.envieImagens(FotoPesqMerProdConc.getImagePath(), imageList, FotoPesqMerProdConc.TABLE_NAME, true);
					LogSync.info(Messages.SYNC_IMAGES_PESQUISA_MERCADO_SUCESSO);
				}
			}
		} catch (Throwable ex) {
			LogSync.info(Messages.SYNC_IMAGES_PESQUISA_MERCADO_ERRO + ex.getMessage());
			throw ex;
		}
	}

	private static void enviaImagensPesquisaMercado(ParamsSync ps) throws Exception {
		try {
			if (LavenderePdaConfig.usaCadastroFotoProdutoNaPesquisaDeMercado()) {
				Vector imageList = FotoPesquisaMercadoService.getInstance().getImageListToSync();
				if (ValueUtil.isNotEmpty(imageList)) {
					LavendereTc2Web pdaToErpHttp = new LavendereTc2Web(ps);
					pdaToErpHttp.envieImagens(FotoPesquisaMercado.getPathImg(), imageList, FotoPesquisaMercado.TABLE_NAME, true);
					LogSync.info(Messages.SYNC_IMAGES_PESQUISA_MERCADO_SUCESSO);
				}
			}
		} catch (Throwable ex) {
			LogSync.info(Messages.SYNC_IMAGES_PESQUISA_MERCADO_ERRO + ex.getMessage());
			throw ex;
		}
	}

	public static boolean validaEquipamentoBloqueado(ParamsSync sync) throws SQLException {
		if (!EquipamentoUsuarioLavendereService.getInstance().verificaEquipamentoLiberadoUsuario(sync)) {
			throw new UsuarioBloqueadoException(Messages.ERRO_EQUIPAMENTO_NAO_LIBERADO);
		}
		return true;
	}

	private static void enviaImagensVisitas(ParamsSync ps) throws Exception {
		if (LavenderePdaConfig.usaVisitaFoto) {
			Vector imageList = VisitaFotoService.getInstance().getImageListToSync();
			if (imageList.size() > 0) {
				LavendereTc2Web pdaToErpHttp = new LavendereTc2Web(ps);
				pdaToErpHttp.envieImagens(VisitaFoto.getPathImg(), imageList, VisitaFotoDao.TABLE_NAME, true);
				LogSync.info(Messages.SYNC_IMAGES_VISITA_SUCESSO);
			}
		}
	}
	
	private static void enviaImagensPesquisaRespApp(ParamsSync ps) throws Exception {
		ConfigIntegWebTc configIntegWebTcFilter = new ConfigIntegWebTc();
		configIntegWebTcFilter.dsTabela = PesquisaRespAppFoto.TABLE_NAME;
		ConfigIntegWebTc webTc = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
		if (webTc != null && webTc.isAtivo()) {
			Vector imageList = PesquisaRespAppFotoService.getInstance().getImageListToSync();
			if (imageList.size() > 0) {
				LavendereTc2Web pdaToErpHttp = new LavendereTc2Web(ps);
				pdaToErpHttp.envieImagens(PesquisaRespAppFoto.getPathImg(), imageList, PesquisaRespAppFoto.TABLE_NAME, true);
				LogSync.info(Messages.SYNC_IMAGES_VISITA_SUCESSO);
			}
		}
	}

	private static void enviaDocumentoAnexo(ParamsSync ps) throws Exception {
		try {
			if (LavenderePdaConfig.usaSelecaoDocAnexo()) {
				Vector docAnexoList = DocumentoAnexoService.getInstance().getDocAnexoListToSync();
				if (docAnexoList.size() > 0) {
					LavendereTc2Web pdaToErpHttp = new LavendereTc2Web(ps);
					pdaToErpHttp.enviaArquivoToErp(DocumentoAnexo.getPathDoc(), docAnexoList, true);
					LogSync.sucess(Messages.SYNC_DOCS_ANEXOS_SUCESSO);
				}
			} 
		} catch (Throwable ex) {
			LogSync.error(Messages.SYNC_ERRO_ENVIAR_ANEXO + ex.getMessage());
			throw ex;
		}
	}
	
	private static void enviaImagensClientes(ParamsSync ps) throws Exception {
		try {
			if (LavenderePdaConfig.usaFotoCliente() && LavenderePdaConfig.isPermiteRegistrarNovasFotosDeCliente()) {
				Vector imageList = FotoClienteService.getInstance().getImageListToSync();
				if (ValueUtil.isNotEmpty(imageList)) {
					LavendereTc2Web pdaToErpHttp = new LavendereTc2Web(ps);
					pdaToErpHttp.envieImagens(Cliente.getPathImg(), imageList, FotoClienteDao.TABLE_NAME, false);
					LogSync.sucess(Messages.SYNC_IMAGES_CLIENTE_SUCESSO);
				}
			}
		} catch (Throwable ex) {
			LogSync.error(Messages.SYNC_IMAGES_CLIENTE_ERRO + ex.getMessage());
			throw ex;
		}
	}
	
	private static void enviaImagensNovosClientes(ParamsSync ps) throws Exception {
		try {
			if (LavenderePdaConfig.isUsaCadastroFotoNovoCliente()) {
				Vector imageList = FotoNovoClienteService.getInstance().getImageListToSync();
				if (ValueUtil.isNotEmpty(imageList)) {
					LavendereTc2Web pdaToErpHttp = new LavendereTc2Web(ps);
					pdaToErpHttp.envieImagens(NovoCliente.getPathImg(), imageList, FotoNovoCliente.TABLE_NAME, false);
					LogSync.sucess(Messages.SYNC_IMAGES_NOVOCLIENTE_SUCESSO);
				}
			}
		} catch (Throwable ex) {
			LogSync.error(Messages.SYNC_IMAGES_NOVOCLIENTE_ERRO + ex.getMessage());
			throw ex;
		}
	}
	
	private static void enviaImagensProspect(ParamsSync ps) throws Exception {
		try {
			if (LavenderePdaConfig.permiteTirarFotoProspect()) {
				Vector imageList = FotoProspectService.getInstance().getImageListSync();
				if (ValueUtil.isNotEmpty(imageList)) {
					LavendereTc2Web pdaToErp = new LavendereTc2Web(ps);
					pdaToErp.envieImagens(Prospect.getPathImg(), imageList, FotoProspect.TABLE_NAME, false);
					LogSync.sucess(Messages.SYNC_IMAGES_PROSPECT_SUCESSO);
				}
			}
		} catch (Throwable e) {
			LogSync.error(Messages.SYNC_IMAGES_PROSPECT_ERRO + e.getMessage());
			throw e;
		}
	}

	private static void enviaImagensPedidos(ParamsSync ps) throws Exception {
		try {
			if (LavenderePdaConfig.usaFotoPedidoNoSistema) {
				Vector imageList = FotoPedidoService.getInstance().getImageListToSync();
				if (ValueUtil.isNotEmpty(imageList)) {
					LavendereTc2Web pdaToErpHttp = new LavendereTc2Web(ps);
					pdaToErpHttp.envieImagens(FotoPedido.getPathImg(), imageList, FotoPedido.TABLE_NAME, true);
					LogSync.sucess(Messages.SYNC_IMAGES_PEDIDO_SUCESSO);
				}
			}
		} catch (Throwable ex) {
			LogSync.error(Messages.SYNC_IMAGES_PEDIDO_ERRO + ex.getMessage());
			throw ex;
		}
	}

	public static String getDataHoraServidor(ParamsSync ps) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(ps);
		return erpToPda.recebaDateTimeServidor();
	}
	
	public static boolean isVersaoAppAtualizada(ParamsSync ps) throws SQLException {
		try {
			String tipoAtualizacaoAppNecessario = SyncManager.getTipoAtualizacaoAppNecessario(ps);
			if (ValueUtil.isNotEmpty(tipoAtualizacaoAppNecessario)) {
				String[] tipoAtualizacaoVersaoAppSplited = StringUtil.split(tipoAtualizacaoAppNecessario, '*', true);
				if (tipoAtualizacaoVersaoAppSplited.length == 2) {
					String tipoAtualizacao = tipoAtualizacaoVersaoAppSplited[0];
					String nuVersaoAtual = tipoAtualizacaoVersaoAppSplited[1];
					boolean isAtualizacaoAppCargaCompleta = LavendereConfig.TIPO_ATUALIZACAO_COMPLETA.equalsIgnoreCase(tipoAtualizacao);
					boolean isAtualizacaoAppNormal = !Session.isModoSuporte && LavendereConfig.TIPO_ATUALIZACAO_NORMAL.equalsIgnoreCase(tipoAtualizacao);
					if (ValueUtil.isNotEmpty(nuVersaoAtual)) {
						ConfigInternoService.getInstance().addValueGeral(ConfigInterno.nuVersaoAtual, nuVersaoAtual);
					}
					if (VmUtil.isJava()) {
						return true;
					}
					return !isAtualizacaoAppCargaCompleta && !isAtualizacaoAppNormal;
				}
			}
		} catch (Throwable ex) {
			LogSync.error(ex.getMessage());
			LogPdaService.getInstance().logSyncError(ex);
		}
		return true;
}
	
	
	public static boolean verificaAtualizacaoVersaoPda(ParamsSync ps) throws SQLException {
		if (!isVersaoAppAtualizada(ps)) {
			LogSync.info(MessageUtil.getMessage(Messages.SISTEMA_MSG_NUVERSAO, new Object[]{LavendereConfig.getInstance().version, ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.nuVersaoAtual)}));
			LogSync.info(Messages.SINCRONIZACAO_MSG_RECEBENDO_NOVA_APLICACAO);
			try {
				recebeAtualizacaoApp(ps, ValueUtil.VALOR_SIM);
			} catch (Throwable ex) {
				LogSync.error(Messages.SINCRONIZACAO_MSG_ERRO_RECEBIMENTO);
				LogPdaService.getInstance().logSyncError(ex);
			}
			return true;
		}
		return false;
	}
	
	public static void downloadInstallWtools() {
		new AsyncWindow(new AsyncExecution() {
			boolean instala = false;
			@Override
			public void executeAsync() {
				LogSync.info(Messages.SINCRONIZACAO_MSG_RECEBENDO_NOVA_APLICACAO_WTOOLS);
				try {
					downloadWtools();
					instala = true;
				} catch (Exception e) {
					LogSync.error(Messages.SINCRONIZACAO_MSG_ERRO_RECEBIMENTO_WTOOLS);
				}
			}
			@Override
			public boolean beforeExecuteAsync() throws Exception {
				return true;
			}
			
			@Override
			public void afterExecuteASync() {
				if (instala) {
					instalaWtools();
				}
			}
		}).popup();
	}
	
	
	public static void recebeAtualizacaoApp(ParamsSync ps, String cargaUnificada) throws SQLException {
		LogSyncTimer timer = new LogSyncTimer(Messages.SINCRONIZACAO_MSG_RECEBENDO_NOVA_APLICACAO).newLogOnFinish();
		try {
			if (VmUtil.isAndroid()) {
				recebeNovaVersaoAndroid(ps, cargaUnificada);
			} else if (VmUtil.isSimulador()) {
				recebeNovaVersaoSimulador(ps);
			} else if (VmUtil.isIOS()) {
				recebeLinkDiawiIOS();
			} else {
				LogSync.warn(Messages.SINCRONIZACAO_MSG_PLATAFORMA_NAO_SUPORTADA);
				return;
			}
			LogSync.sucess(Messages.SISTEMA_MSG_VERSAO_REINICIAR);
			NotificationManager.putNotification(SyncActions.getAppUpdateNotification(Messages.SISTEMA_MSG_VERSAO_REINICIAR));
		} catch (AtualizaAppException e) {
			LogSync.error(e.getMessage());
			throw e;
		} catch (Throwable ex) {
			LogSync.error(Messages.SINCRONIZACAO_MSG_ERRO_RECEBIMENTO);
			NotificationManager.putNotification(SyncActions.getAppUpdateNotification(ex.getMessage()));
			LogPdaService.getInstance().logSyncError(ex);
		} finally {
			timer.finish();
		}
	}

	private static void recebeLinkDiawiIOS() throws Exception {
		LavendereWeb2Tc lavendereWeb2Tc = new LavendereWeb2Tc();
		linkDiawi = lavendereWeb2Tc.linkDiawi();
		if (linkDiawi == null || !linkDiawi.startsWith("http")) {
			throw new ValidationException(Messages.USUARIO_ERRO_LINK_DIAWI_NAO_DISPONIVEL);
		}
	}
	
	public static void instalaNewApp() throws Exception {
		if (!realizaBackupBeforeAtualizacao()) {
			UiUtil.showErrorMessage(Messages.SINCRONIZACAO_MSG_ERRO_BACKUP);
			return;
		}
		String[] args = getArgsVmExec();
		try {
			UiUtil.showStartScreen();
			MainLavenderePda.getInstance().simpleExit();
		} finally {
			Vm.sleep(3000);
			UiUtil.closeStartScreen();
			Vm.exec(args[0], args[1], 0, false);
		}
		
	}
	
	private static String[] getArgsVmExec() throws Exception {
		String[] args = new String[2];
		args[0] = "updateApp.bat";
		args[1] = null;
		if (VmUtil.isAndroid()) {
			args[0] = Settings.dataPath + FileUtil.getAppFileName();
			args[1] = null;
		} else if (VmUtil.isIOS()) {
			args[0] = "url";
			args[1] = linkDiawi;
		}
		
		return args;
	}
	
	public static void instalaWtools() {
		instalaAndroidApp(MessageUtil.getMessage(LavendereConfig.NMAPP_WTOOLS, LavendereConfig.getInstance().nuVersionWtools));
	}

	private static void instalaAndroidApp(String nmApp) {
		if (VmUtil.isAndroid()) {
			Vm.exec(Convert.appendPath(Settings.dataPath, nmApp), null, 0, true);
		}
	}
	
	public static String getTipoAtualizacaoAppNecessario(ParamsSync ps) throws Exception  {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(ps);
		return erpToPda.getTipoAtualizacaoAppNecessario();
	}

	public static void recebeAtualizacaoTabelas(Vector tabelaList) throws Exception {
		Vector web2SyncList = new Vector();
		ConfigIntegWebTc configIntegWebTcFilter = new ConfigIntegWebTc();
		int size = tabelaList.size();
		String[] tableNames = new String[size];
		for (int i = 0; i < size; i++) {
			TabelaDb tabela = (TabelaDb)tabelaList.items[i];
			tableNames[i] = tabela.nmTabela;
			configIntegWebTcFilter.dsTabela = tabela.nmTabela;
			ConfigIntegWebTc webTcProducaoProd = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
			web2SyncList.addElement(webTcProducaoProd);
			LogPdaService.getInstance().logRecuperarDadosRemoto(tabela.nmTabela);
		}
        LavendereWeb2Tc web2Tc = new LavendereWeb2Tc(HttpConnectionManager.getDefaultParamsSync());
        web2Tc.recebeDadosDisponiveisServidor(SyncManager.getInfoAtualizacaoByWeb2SyncList(web2SyncList));
		LogPdaService.getInstance().logRecuperarDadosRemotoSucesso(StringUtil.toString(tableNames));
}

	public static void atualizarAppByBrowser() throws SQLException {
		atualizarAppByBrowser(HttpConnectionManager.getHttpConnection());
	}

	public static void atualizarAppByBrowser(HttpConnection conexao) throws SQLException {
		if (conexao != null) {
			//--
			LogPdaService.getInstance().logMemoria(Messages.LOGPDA_MSG_MEMORIA_SAIDA_SISTEMA);
			LogPdaService.getInstance().info(LogPda.LOG_CATEGORIA_SESSAO, Messages.LOGPDA_MSG_SAIDA_SISTEMA_PELO_ATUALIZARAPP);
			//--
			String action = "/pda";
			String dsUrlWebService = StringUtil.getStringValue(conexao.getBaseUrl());
			if (VmUtil.isWinCEPocketPc()) {
				Vm.exec("iexplore", dsUrlWebService + action, 0, false);
				MainLavenderePda.getInstance().simpleExit();
			} else if (VmUtil.isAndroid() || VmUtil.isIOS()) {
				Vm.exec("url", dsUrlWebService + action, 0, true);
				MainLavenderePda.getInstance().simpleExit();
			} else if (VmUtil.isSimulador()) {
				int i = -1;
				i = Vm.exec("iexplore", dsUrlWebService + action, 0, false);
				if (i == 0) {
					MainLavenderePda.getInstance().simpleExit();
					return;
				}
				i = Vm.exec("C:/Program Files/Internet Explorer/iexplore", dsUrlWebService + action, 0, false);
				if (i == 0) {
					MainLavenderePda.getInstance().simpleExit();
					return;
				}
				i = Vm.exec("C:/Arquivos de Programas/Internet Explorer/iexplore", dsUrlWebService + action, 0, false);
				if (i == 0) {
					MainLavenderePda.getInstance().simpleExit();
					return;
				}
				UiUtil.showErrorMessage(FrameworkMessages.MSG_VALIDACAO_NENHUM_BROWSER);
			}
		}
	}

	public static boolean isConexaoPdaDisponivel() throws SQLException {
		try {
			return HttpConnectionManager.getDefaultParamsSync() != null;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	public static void recebeNovaVersaoSimulador(ParamsSync ps) throws Exception {
		File fileDirDownload = FileUtil.openFile(DatabaseUtil.getPathDados() + FileUtil.getDiretorioAtualizacaoApp(), File.DONT_OPEN);
		if (!fileDirDownload.exists()) {
			fileDirDownload.createDir();
		}
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(ps);
		FileUtil.createDirIfNecessary(DatabaseUtil.getPathDados() + "novo");
		String fileName = "novo/" + FileUtil.getAppFileName();
		erpToPda.recebaAppFileAuto(fileName, ValueUtil.VALOR_SIM, UsuarioPda.TIPOPDA_SIMULADOR);
		ZipFileUtil.unzip(DatabaseUtil.getPathDados() + "novo", FileUtil.getAppFileName());
		FileUtil.deleteFile(Convert.appendPath(DatabaseUtil.getPathDados() + "novo", FileUtil.getAppFileName()));
	}

	public static void recebeNovaVersaoAndroid(ParamsSync ps, String cargaUnificada) throws Exception {
        LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(ps);
        String fileName = FileUtil.getAppFileName();
		erpToPda.recebaAppFileAuto(fileName, cargaUnificada, UsuarioPda.TIPOPDA_ANDROID);
	}
	

	public static void atualizaApp() throws SQLException {
		try {
			ParamsSync ps = HttpConnectionManager.getDefaultParamsSync();
			recebeAtualizacaoApp(ps, ValueUtil.VALOR_SIM);
		} catch (Throwable ex) {
			LogSync.error(ex.getMessage());
		}
	}

	public static void downloadWtools() throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc();
		erpToPda.getWtools();
	}
	
	public static String revalidaRestricaoProdutoItensPedido(Vector itemPedidoList) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(HttpConnectionManager.getDefaultParamsSync());
		return erpToPda.revalidaRestricaoProdutoItensPedido(itemPedidoList);
	}
	
	public static String validaReservaEstoqueItensPedido(Pedido pedido, Vector itemPedidoList) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(HttpConnectionManager.getDefaultParamsSync());
		return erpToPda.validaReservaEstoqueItensPedido(itemPedidoList, pedido.cdEmpresa, pedido.cdRepresentante);
	}
	
	public static String validaInativacaoReservaEstoqueItensPedido(Pedido pedido) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(HttpConnectionManager.getDefaultParamsSync());
		return erpToPda.validaInativacaoReservaEstoqueItensPedido(pedido);
	}
	
	public static String verificaReservaCadastradaServidor(Pedido pedido, Vector itemPedidoList) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(HttpConnectionManager.getDefaultParamsSync());
		return erpToPda.verificaReservaCadastradaServidor(itemPedidoList, pedido.cdEmpresa, pedido.cdRepresentante);
	}
	
	public static String verificaPedidoEnviadoServidor(Pedido pedido) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(HttpConnectionManager.getDefaultParamsSync());
		return erpToPda.verificaPedidoEnviadoServidor(pedido);
	}
	
	public static String geraReservaEstoqueItensPedido(Pedido pedido, Vector itemPedidoList) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(HttpConnectionManager.getDefaultParamsSync());
		return erpToPda.geraReservaEstoqueItensPedido(pedido, itemPedidoList);
	}
	
	public static String inativaReservaEstoqueItensPedido(Pedido pedido) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(HttpConnectionManager.getDefaultParamsSync());
		return erpToPda.inativaReservaEstoqueItensPedido(pedido);
	}

	public static String geraPdfPedido(Pedido pedido, boolean viaCliente) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(HttpConnectionManager.getDefaultParamsSync());
		return erpToPda.geraPdfPedido(pedido, viaCliente);
	}
	
	public static String geraPdfDapLaudo(DapLaudo dapLaudo) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(HttpConnectionManager.getDefaultParamsSync());
		return erpToPda.geraPdfDapLaudo(dapLaudo);
	}
	
	public static void enviaDapLaudoAtua(boolean enviaFotosAssinatura) throws SQLException, Exception {
		enviaDapLaudoAtuaServidor(enviaFotosAssinatura);
	}

	private static void enviaDapLaudoAtuaServidor(boolean enviaFotosAssinatura) throws SQLException, Exception {
		LavendereTc2Web erpToPda = new LavendereTc2Web(HttpConnectionManager.getDefaultParamsSync());
		erpToPda.envieDapLaudoAtuaServidor(enviaFotosAssinatura);
	}

	public static String consultaCepOnline(String cep) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(HttpConnectionManager.getDefaultParamsSync());
		return erpToPda.consultaCepOnline(cep);
	}

	public static JSONObject consultaCnpjOnline(String cnpj) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(HttpConnectionManager.getDefaultParamsSync());
		return erpToPda.consultaCnpjOnline(cnpj);
	}

	public static void geraCatalogoProduto(JSONObject catalogoParams) throws Exception {
		ParamsSync ps = HttpConnectionManager.getDefaultParamsSync();
		ps.readTimeout = LavenderePdaConfig.getValorTimeOutCatalogoProduto();
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(ps);
		erpToPda.geraCatalogoProduto(catalogoParams);
	}

	public static String geraRateioProducaoProd(Vector producaoProdList) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(HttpConnectionManager.getDefaultParamsSync());
		return erpToPda.geraRateioProducaoProd(producaoProdList);
	}

	public static String liberaPedidoPendenteForaDeOrdem(Pedido pedido) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(HttpConnectionManager.getDefaultParamsSync());
		return erpToPda.liberaPedidoPendenteForaDeOrdem(pedido);
	}
	
	public static String cancelaPedido(Pedido pedido) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc();
		return erpToPda.cancelaPedido(pedido);
	}

	public static String validaPeriodoDevolucaoEstoque(String nuDiasIntDevolEstoque) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc();
		return erpToPda.validaPeriodoDevolucaoEstoque(nuDiasIntDevolEstoque);
	}

	public static String desbloqueiaRemessaEstoque(RemessaEstoque remessaEstoque) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc();
		return erpToPda.desbloqueiaRemessaEstoque(remessaEstoque);
	}
	
	public static String processaDevolucaoEstoqueWeb(Vector listRemessaEstoque) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc();
		return erpToPda.processaDevolucaoEstoqueWeb(listRemessaEstoque);
	}
	
	public static List<SerieNfeDTO> getSerieAndProximoNumero(String cdEmpresa, String cdRepresentante, String flContingencia) throws Exception {
		return getSerieAndProximoNumero(cdEmpresa, cdRepresentante, flContingencia, "1");
	}
	public static List<SerieNfeDTO> getSerieAndProximoNumero(String cdEmpresa, String cdRepresentante, String flContingencia, String cdTipoNota) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc();
		return erpToPda.getNuSerieNfeAndProxNumero(cdEmpresa, cdRepresentante, flContingencia, cdTipoNota);
	}
	
	public static String getConfigAcessoSistema(String cdFuncao) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc();
		return erpToPda.getConfigAcessoSistema(cdFuncao);
	}
	
	private static boolean realizaBackupBeforeAtualizacao() {
		try {
			UiUtil.showProcessingMessage("Realizando backup antes da atualização");
			String fileName = LavendereBackupService.getInstance().realizaBackup(BackupService.BackupFile.FLTIPOBACKUP_VERSAO);
			LavendereTc2Web pdaToErp = new LavendereTc2Web(getParamsSyncBackup());
			pdaToErp.sendBackupApp(fileName);
			return true;
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e);
		} finally {
			UiUtil.unpopProcessingMessage();
		}
		return false;
	}
	
	public static void sendBackupFiles(ParamsSync paramsSync) {
		try {
			LogSync.logSection(Messages.BACKUP_LOG_SYNC);
			Vector backupsToSend = BackupService.listBackupsToSend();
			int size = backupsToSend.size();
			if (size > 0) {
				LavendereTc2Web pdaToErp = new LavendereTc2Web(paramsSync);
				for (int i = 0; i < size; i++) {
					pdaToErp.sendBackupApp((String)backupsToSend.items[i]);
				}
			}
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		} finally {
			LogSync.logSection(Messages.BACKUP_LOG_SYNC_FIM);
		}
	}
	
	public static boolean isNovoClienteValidoWeb(String cdEmpresa, String nuCnpj, String flValidaProspect) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(HttpConnectionManager.getDefaultParamsSync().configReadTimeout(LavenderePdaConfig.vlTimeOutValidacaoCPFCNPJDuplicado * 10000));
		return erpToPda.validaCpfCnpjNovoCliente(nuCnpj, cdEmpresa, flValidaProspect);
	}

	public static ParamsSync getParamsSyncBackup() throws SQLException {
		return HttpConnectionManager.getDefaultParamsSync().configOpenTimeout(10000).configNuMaxTentativasFimEnvio(1);
	}

	public static String sendDadosPedidoEstoque(String pedidoEstoqueJson, boolean devolucao) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(HttpConnectionManager.getDefaultParamsSync().configReadTimeout(LavenderePdaConfig.valorTimeOutRetornoDadosRelativosRemessaEstoque * 1000));
		return erpToPda.sendDadosPedidoEstoque(pedidoEstoqueJson, devolucao);
	}
	
	public static void enviaDadosVerbaSaldoPedidoAberto(String json) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc();
		erpToPda.envieDadosVerbaSaldoPedidoAberto(json);
	}
	
	public static boolean hasTableUpdate(String tableName, int nuCarimbo) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc();
		return ValueUtil.VALOR_SIM.equals(erpToPda.hasTableUpdates(tableName, nuCarimbo));
	}
	
	public static void enviaLogsWGpsDoDiaServidor(ParamsSync paramsSync) throws Exception {
		if (LavenderePdaConfig.isUsaColetaGpsAppExterno()) {
			enviaLogsWGpsServidor(true);
		}
	}

	public static void enviaLogsWGpsServidor(boolean currentDay) throws Exception {
		try {
			File file = WtoolsUtil.getLogDir();
			Vector logNameList = PontoGpsService.getInstance().getLogFiles(file, currentDay);
			new LavendereTc2Web().enviaArquivoToErp(file.getPath(), logNameList, true, "/wtools/log/");
		} catch (Throwable e) {
			LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_ERRO_COMUNICACAO_WGPS, "Erro ao enviar os arquivos de log do WMWGPS. Detalhes: " + e.getMessage());
		}
	}

	public static HashMap<String, Object> getInformacoesComplementaresPedidoWebserviceSankhya(String body) throws Exception {
		ParamsSync ps = HttpConnectionManager.getDefaultParamsSync();
		ps.readTimeout = LavenderePdaConfig.timeOutWebserviceSankhyaComplementaPedido;
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(ps);
		return erpToPda.getInformacoesComplementaresPedidoWebserviceSankhya(body);
	}

	public static Image geraQrCode(String url) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc();
		return erpToPda.geraQrCode(url);
	}

	public static String fusoHorario(String uf) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc();
		return erpToPda.fusoHorario(uf);
	}

	private static String validaLoginOnlineSend(final SendValidaSenhaDTO sendValidaSenhaDTO) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc();
		return erpToPda.validaLoginOnline(sendValidaSenhaDTO);
	}
	
	public static RetValidaSenhaDTO validaLoginOnline(final String cdUsuario, final String dsSenha) throws Exception {
		String senha = ValueUtil.getBooleanValue(LavenderePdaConfig.usaSenhaDoUsuarioPdaSensivelAoCase) ? dsSenha : dsSenha.toUpperCase();
		SendValidaSenhaDTO sendValidaSenhaDTO = new SendValidaSenhaDTO();
		sendValidaSenhaDTO.setCdUsuario(cdUsuario);
		sendValidaSenhaDTO.setDsSenha(EncodeUtil.encodeToSHA1(senha));
		String retorno = validaLoginOnlineSend(sendValidaSenhaDTO);
		return JsonFactory.parse(StringUtil.getStringValue(retorno), RetValidaSenhaDTO.class);
	}

	public static void processaLoginBloqueadoOnline(final String cdUsuario) throws Exception {
		SendValidaSenhaDTO sendValidaSenhaDTO = new SendValidaSenhaDTO();
		sendValidaSenhaDTO.setSendEmailBloqueadoLogin(true);
		sendValidaSenhaDTO.setCdUsuario(cdUsuario);
		validaLoginOnlineSend(sendValidaSenhaDTO);
	}
	
	public static String buscaUsuarioEquipamento(String cdEquipamento) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc();
		return erpToPda.buscaUsuarioEquipamento(cdEquipamento);
	}

	public static String buscaEstoqueFiliais(String cdProduto) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc();
		return erpToPda.buscaEstoqueFiliais(cdProduto);
	}

	public static String geraPdfCatalogoPedido(Pedido pedido) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc();
		return erpToPda.geraPdfCatalogoPedido(pedido);
	}
	public static String geraPdfPedidoBoleto(Pedido pedido) throws Exception {
		ParamsSync ps = HttpConnectionManager.getDefaultParamsSync();
		ps.readTimeout = LavenderePdaConfig.getValorTimeOutCatalogoProduto();
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(ps);
		return erpToPda.geraPdfPedidoBoleto(pedido);
	}

	public static String geraPdfTituloNfe(TituloFinanceiro domain) throws Exception {
		ParamsSync ps = HttpConnectionManager.getDefaultParamsSync();
		ps.readTimeout = LavenderePdaConfig.getValorTimeOutCatalogoProduto();
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(ps);
		return erpToPda.geraPdfNfeTituloFinanceiro(domain);
	}

	public static String geraPdfTituloBoleto(TituloFinanceiro tituloFinanceiro) throws Exception {
		ParamsSync ps = HttpConnectionManager.getDefaultParamsSync();
		ps.readTimeout = LavenderePdaConfig.getValorTimeOutCatalogoProduto();
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(ps);
		return erpToPda.geraPdfTituloPedidoBoleto(tituloFinanceiro);
	}

	public static String postCoodernadasClient(String jsonObject) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(HttpConnectionManager.getDefaultParamsSync());
		return erpToPda.postCoodernadasClient(jsonObject);
	}

	public static void processaMaxDataHora(final SyncInfo syncInfo) {
		try {
			processaMaxDataHora(syncInfo, CrudDbxDao.getCurrentDriver());
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	public static void processaMaxDataHora(final SyncInfo syncInfo, final SQLiteDriver driver) {
		String nmTable = syncInfo.tableName;
		String sql = "select  max(datetime(DTALTERACAO || ' ' || HRALTERACAO)) as DT_HR_ALTERACAO from "
				+ nmTable + " where CDUSUARIO = '" + Session.getCdUsuario() + "'";
		
		try (Statement stmt = driver.getStatement(); ResultSet resultSet = stmt.executeQuery(sql)) {
			if (resultSet.next()) {
				syncInfo.maxDataHoraAlteracao = resultSet.getTime("DT_HR_ALTERACAO");
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}
	
	public static String geraListaLeads(ListaLeadsDTO listaLeadsDTO) throws Exception {
		ParamsSync ps = HttpConnectionManager.getDefaultParamsSync();
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc(ps);
		return erpToPda.buscaPlacesWeb(listaLeadsDTO);
	}
	
}
