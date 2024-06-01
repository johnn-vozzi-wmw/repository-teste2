package br.com.wmw.lavenderepda;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.wmw.framework.async.AsyncPool;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AgendaVisita;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.DescontoVenda;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PreferenciaFuncao;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.UsuarioPdaRep;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.PreferenciaFuncaoService;
import br.com.wmw.lavenderepda.business.service.UsuarioConfigService;
import br.com.wmw.lavenderepda.business.service.UsuarioDescService;
import br.com.wmw.lavenderepda.business.service.VisitaService;
import br.com.wmw.lavenderepda.presentation.ui.CadItemPedidoForm;
import br.com.wmw.lavenderepda.sync.async.EnviaPontoGpsRunnable;
import br.com.wmw.lavenderepda.sync.async.SincronizacaoApp2WebRunnable;
import br.com.wmw.lavenderepda.sync.async.SincronizacaoWeb2AppRunnable;
import totalcross.util.ElementNotFoundException;
import totalcross.util.IntHashtable;
import totalcross.util.Vector;
import totalcross.util.concurrent.Lock;

public class SessionLavenderePda {
	
	private static boolean isRunningSync;
	private static boolean isDownloadingMedia;
	private static Lock lock = new Lock();
	
	private static boolean coletaGpsEmAndamento;
	public static boolean isUsuarioAtivo = true;
	public static boolean autorizadoPorSenhaNovoPedidoSemEnvioDados;
	public static boolean autorizadoPorSenhaNovoPedidoSemRecebimentoDados;
	public static boolean autorizadoPorSenhaSistemaSemRecebimentoDados;
	public static boolean autorizadoPorSenhaEnviarPedidosQualquerHorario;
	public static boolean isSistemaLiberadoSenhaGpsOff;
	public static boolean isOcultoInfoRentabilidade;
	public static boolean isDevolverEstoqueAtual;
	public static boolean ignoraControleDataHora;
	public static boolean envioPedidosBloqueadoHoraLimite;
	public static boolean houveErroPedidosRestrito;
	public static boolean autorizadoPorSenhaCoordenadaPesquisaMercado;
	public static boolean autorizadoPorSenhaFotosPesquisaMercado;
	public static boolean isGpsOff;
	public static boolean isWGPSInativo;
	public static boolean isWGPSRunning;
	public static boolean isModoFeira;
	public static String cdTabelaPreco;
	public static String cdEmpresa;
	public static String cdEmpresaOld;
	public static boolean liberadoPorSenhaRelacionarPedidoProducao;
	public static final IntHashtable hashTabelasRepresentanteSupervisor = new IntHashtable(10);
	private static Representante representante;
	public static Representante representanteOld;
	private static Cliente cliente;
	private static AgendaVisita agenda;
	public static UsuarioPdaRep usuarioPdaRep;
	public static Visita visitaAndamento;
	public static Pedido pedidoConsultaUltimosPedidos;
	public static CadItemPedidoForm cadItemPedidoFormConsultaUltimosPedidosInstance;
	public static DescontoVenda descontoVendaSimulado;
	private static ProdutoBase ultimoProdutoFilter;
	private static List<String> pedidoProcessandoFechamentoList = new ArrayList<String>(4);
	public static Map<Integer, PreferenciaFuncao> preferenciaFuncaoMap = new HashMap<Integer, PreferenciaFuncao>();
	public static int nuOrdemLiberacaoUsuario;
	public static boolean validaAcessoSistema;
	
	public static void clearSessionRepresentante() {
		representante = null;
		liberadoPorSenhaRelacionarPedidoProducao = false;
	}

	public static void clearSessionCliente() {
		autorizadoPorSenhaFotosPesquisaMercado = false;
		autorizadoPorSenhaCoordenadaPesquisaMercado = false;
		cliente = null;
	}

	public static boolean isSessionCliente() {
		return cliente != null;
	}
	
	public static String getCdRepresentanteFiltroDados(Class<?> classe) {
		if (!SessionLavenderePda.isUsuarioSupervisor() || LavenderePdaConfig.tabelasDadosParaTodosRepSup.indexOf(";" + classe.getSimpleName().toUpperCase() + ";") == -1) {
			return usuarioPdaRep.cdRepresentante;
		} else {
			String className = classe.getSimpleName().toUpperCase();
			int value = 0;
			try {
				value = hashTabelasRepresentanteSupervisor.get(className);
			} catch (ElementNotFoundException ex) {
				try {
					String sql = "SELECT count(*) as qtde FROM TBLVP" + className + " tb where cdRepresentante != " + Sql.getValue(usuarioPdaRep.cdRepresentante) + " and exists (select 1 from tblvpsupervisorrep sup where sup.cdempresa = tb.cdempresa and sup.cdRepresentante = tb.cdRepresentante and sup.cdsupervisor = " + Sql.getValue(usuarioPdaRep.cdRepresentante) + ")";
					value = CrudDbxDao.getCurrentDriver().getInt(sql);
					hashTabelasRepresentanteSupervisor.put(className, value);
				} catch (Throwable ex2) { /**/ }
			}
			return value == 0 ? usuarioPdaRep.cdRepresentante : getRepresentante().cdRepresentante;
		}
	}

	public static Representante getRepresentante() {
		if (representante == null) {
			return new Representante();
		}
		return representante;
	}

	public static void setRepresentante(Representante rep) throws SQLException {
		representante = rep;
		liberadoPorSenhaRelacionarPedidoProducao = false;
		if (representante != null && LavenderePdaConfig.usaIndiceFinanceiroSupRep) {
			representante.vlIndiceFinanceiro = -1;
		}
		reloadVisitaAndamento();
	}

	public static void setRepresentante(String cdRepresentante, String nmRepresentante) throws SQLException {
		setRepresentante(cdRepresentante, nmRepresentante, null);
	}

	public static void setRepresentante(String cdRepresentante, String nmRepresentante, String cdRepresentanteErp) throws SQLException {
		representante = new Representante();
		representante.cdRepresentante = cdRepresentante;
		representante.nmRepresentante = nmRepresentante;
		representante.cdRepresentanteErp = cdRepresentanteErp;
		if (LavenderePdaConfig.usaIndiceFinanceiroSupRep) {
			representante.vlIndiceFinanceiro = -1;
		}
		setRepresentante(representante);
	}
	
	public static void setRepresentanteOld(String cdRepresentante, String nmRepresentante) throws SQLException {
		representanteOld = new Representante();
		representanteOld.cdRepresentante = cdRepresentante;
		representanteOld.nmRepresentante = nmRepresentante;
	}

	public static Cliente getCliente() {
		return cliente;
	}
	
	public static String getCdCliente() {
		return cliente != null ? cliente.cdCliente : null;
	}

	public static void setCliente(Cliente newCliente) {
		autorizadoPorSenhaFotosPesquisaMercado = false;
		autorizadoPorSenhaCoordenadaPesquisaMercado = false;
		cliente = newCliente;
	}

	public static boolean isUsuarioSupervisor() {
		return usuarioPdaRep != null && usuarioPdaRep.representante != null && usuarioPdaRep.representante.isSupervisor();
	}

	public static AgendaVisita getAgenda() {
		return agenda;
	}

	public static void setAgenda(AgendaVisita agenda) {
		SessionLavenderePda.agenda = agenda;
	}

	public static boolean isColetaGpsEmAndamento() {
		return coletaGpsEmAndamento;
	}

	public static void setColetaGpsEmAndamento(boolean coletaGpsEmAndamento) {
		SessionLavenderePda.coletaGpsEmAndamento = coletaGpsEmAndamento;
	}
	
	public static void reloadVisitaAndamento() throws SQLException {
		if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
			SessionLavenderePda.visitaAndamento = VisitaService.getInstance().findVisitaEmAndamento();
		}
	}
	public static void loadModoFeira() throws SQLException {
		if (LavenderePdaConfig.usaEscolhaModoFeira) {
			SessionLavenderePda.isModoFeira = ConfigInternoService.getInstance().isModoFeiraUltimoSelecionado(cdEmpresa, usuarioPdaRep.cdRepresentante);
		}
	}
	
	public static boolean isUsuarioLiberaItemPendente() throws SQLException {
		return usuarioPdaRep != null && UsuarioConfigService.getInstance().isLiberaItemPendente();
	}
	
	public static void loadPreferenciaFuncaoMap() throws SQLException {
    	Vector preferenciaFuncaoList = PreferenciaFuncaoService.getInstance().findAllPreferenciaFuncao();
    	if (preferenciaFuncaoList.size() > 0) {
    		PreferenciaFuncao preferenciaFuncao;
    		for (int i = 0; i < preferenciaFuncaoList.size(); i++) {
    			preferenciaFuncao = (PreferenciaFuncao) preferenciaFuncaoList.elementAt(i);
    			preferenciaFuncaoMap.put(preferenciaFuncao.cdPreferencia, preferenciaFuncao);
    		}			
		} else {
			preferenciaFuncaoMap.clear();
		}
	}
	
	public static boolean hasPreferencia(int cdPreferencia) {
		return preferenciaFuncaoMap.get(cdPreferencia) != null;
	}

	public static void addPedidoProcessandoFechamento(Pedido pedido) {
		if (pedido != null) pedidoProcessandoFechamentoList.add(pedido.getRowKey());
	}

	public static void removePedidoProcessandoFechamento(Pedido pedido) {
		if (pedido != null && isPedidoProcessandoFechamento(pedido)) pedidoProcessandoFechamentoList.remove(pedido.getRowKey());
	}

	public static boolean isPedidoProcessandoFechamento(Pedido pedido) {
		return pedidoProcessandoFechamentoList.indexOf(pedido.getRowKey()) >= 0;
	}

	public static void clearPedidoProcessandoFechamentoList() {
		pedidoProcessandoFechamentoList = new ArrayList<String>(4);
	}

	public static void removePedidoProcessandoFechamento(Vector pedidoList) {
		for (int i = 0; i < pedidoList.size(); i++) {
			removePedidoProcessandoFechamento((Pedido) pedidoList.items[i]);
		}
	}

	public static void loadNuOrdemLiberacaoUsuario() throws SQLException {
		nuOrdemLiberacaoUsuario = UsuarioDescService.getInstance().getNuOrdemLiberacao();
	}

	public static ProdutoBase getUltimoProdutoFilter() {
		return ultimoProdutoFilter;
	}

	public static void setUltimoProdutoFilter(ProdutoBase ultimoProdutoFilter) {
		SessionLavenderePda.ultimoProdutoFilter = ultimoProdutoFilter;
	}

	public static boolean isLigadoSincronizacaoBackground() {
		return SincronizacaoWeb2AppRunnable.getInstance().isSyncAutomaticoLigado() 
				|| SincronizacaoApp2WebRunnable.getInstance().isSyncAutomaticoLigado();
	}
	
	public static void startBackgroundServices() {
		SincronizacaoWeb2AppRunnable.addQueue();
		SincronizacaoApp2WebRunnable.addQueue();
		EnviaPontoGpsRunnable.addQueue();
		
	}
	
	public static void stopBackgroundServices() {
		SincronizacaoWeb2AppRunnable.removeQueue();
		SincronizacaoApp2WebRunnable.removeQueue();
		EnviaPontoGpsRunnable.removeQueue();
	}

	public static boolean isRunningSync() {
		return isRunningSync;
	}
	
	public static boolean initLockSync() {
		synchronized (lock) {
			if (!isRunningSync) {
				SessionLavenderePda.isRunningSync = true;
				return true;
			}
			return false;
		}
	}
	
	public static boolean releaseLockSync() {
		synchronized (lock) {
			if (isRunningSync) {
				SessionLavenderePda.isRunningSync = false;
				return true;
			}
			return false;
		}
	}
	
	public static boolean isDownloadingMedia() {
		return isDownloadingMedia;
	}
	
	public static void setDownloadingMedia(boolean b) {
		synchronized (lock) {
			isDownloadingMedia = b;
		}
	}

	public static void setRepresentanteByUsuarioPdaRep() throws SQLException {
		if (LavenderePdaConfig.usaCodigoRepresentanteErp) {
			setRepresentante(usuarioPdaRep.representante.cdRepresentante, usuarioPdaRep.representante.nmRepresentante, usuarioPdaRep.representante.cdRepresentanteErp);
		} else {
			setRepresentante(usuarioPdaRep.representante.cdRepresentante, usuarioPdaRep.representante.nmRepresentante);
		}
	}
}
