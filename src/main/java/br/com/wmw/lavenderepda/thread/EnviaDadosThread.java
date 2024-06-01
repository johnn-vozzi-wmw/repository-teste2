package br.com.wmw.lavenderepda.thread;

import java.sql.SQLException;

import br.com.wmw.framework.async.AsyncPool;
import br.com.wmw.framework.async.RunnableImpl;
import br.com.wmw.framework.exception.DataBaseValidationException;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.notification.NotificationManager;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ColetaGps;
import br.com.wmw.lavenderepda.business.domain.DadosTc2Web;
import br.com.wmw.lavenderepda.business.domain.ItemLiberacao;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.NotificacaoPda;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoConsignacao;
import br.com.wmw.lavenderepda.business.domain.PedidoDesc;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.domain.VisitaFoto;
import br.com.wmw.lavenderepda.business.domain.VisitaPedido;
import br.com.wmw.lavenderepda.business.service.ColetaGpsService;
import br.com.wmw.lavenderepda.business.service.ErroEnvioService;
import br.com.wmw.lavenderepda.business.service.ItemLiberacaoService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.NotificacaoPdaService;
import br.com.wmw.lavenderepda.business.service.PedidoConsignacaoService;
import br.com.wmw.lavenderepda.business.service.PedidoDescService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.VisitaPedidoService;
import br.com.wmw.lavenderepda.business.service.VisitaService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NotificacaoPdaDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoConsignacaoDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VisitaFotoDao;
import br.com.wmw.lavenderepda.presentation.ui.MainLavenderePda;
import br.com.wmw.lavenderepda.sync.LavendereTc2Web;
import totalcross.io.ByteArrayStream;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.sys.Vm;
import totalcross.util.Hashtable;
import totalcross.util.Vector;
import totalcross.util.concurrent.Lock;

/**
 * Não implementar novas regras. Precisa ser refatorado.
 *
 */
@Deprecated
public class EnviaDadosThread extends RunnableImpl {

	private static EnviaDadosThread instance;

	private String cdSessaoEnviando;
	private Hashtable sessoesEnvioHash = new Hashtable(5);
	public Vector dadosEnviadosSucessoCache = new Vector();
	public Vector dadosPedidoEnviadosSucessoCache = new Vector();
	public Vector dadosEnviadosErroCache = new Vector();
	public Vector erroEnvioDados = new Vector();
	private Lock lock = new Lock();

	public static EnviaDadosThread getInstance() {
		if (instance == null) {
			instance = new EnviaDadosThread();
		}
		return instance;
	}

	public void put(String tableName, int nuLinhas, ByteArrayStream cbas, String cdSessao, String rowKey) throws SQLException {
		put(tableName, nuLinhas, cbas, cdSessao, rowKey, "");
	}

	public void put(String tableName, int nuLinhas, ByteArrayStream cbas, String cdSessao, String rowKey, String cdRegistro) throws SQLException {
		Vector rowKeys = new Vector();
		rowKeys.addElement(rowKey);
		put(tableName, nuLinhas, cbas, cdSessao, rowKeys, cdRegistro, null);
	}
	
	public void put(String tableName, int nuLinhas, ByteArrayStream cbasRetorno, String cdSessao, String rowKey, Vector visitaFotoList) throws SQLException {
		Vector rowKeys = new Vector();
		rowKeys.addElement(rowKey);
		put(tableName, nuLinhas, cbasRetorno, cdSessao, rowKeys, "", visitaFotoList);
	}

	public void put(String tableName, int nuLinhas, ByteArrayStream cbas, String cdSessao, Vector rowKey, String cdRegistro, Vector visitaFotoList) throws SQLException {
		DadosTc2Web dadosTc2Web = new DadosTc2Web();
		dadosTc2Web.cbas = cbas;
		dadosTc2Web.nuLinhas = nuLinhas;
		dadosTc2Web.tableName = tableName;
		dadosTc2Web.rowKeys = rowKey;
		dadosTc2Web.cdRegistro = cdRegistro;
		dadosTc2Web.imageList = visitaFotoList;
		//--
		while (cdSessao.equals(cdSessaoEnviando)) {
			Vm.sleep(500);
		}
		//--
		synchronized (lock) {
			Vector dadosTc2WebList = (Vector) sessoesEnvioHash.get(cdSessao);
			if (dadosTc2WebList == null) {
				dadosTc2WebList = new Vector();
				sessoesEnvioHash.put(cdSessao, dadosTc2WebList);
			}
			dadosTc2WebList.addElement(dadosTc2Web);
		}
	}

	private void executeEnvio() {
		try {
			while (!SessionLavenderePda.initLockSync()) {
				Vm.safeSleep(1000);
			}
			while (sessoesEnvioHash.size() != 0) {
				try {
					Vector keys = sessoesEnvioHash.getKeys();
					keys.qsort();
					cdSessaoEnviando = (String) keys.items[0];
					Vector dadosTc2WebList = (Vector) sessoesEnvioHash.get(cdSessaoEnviando);
					int size = dadosTc2WebList.size();
					LavendereTc2Web tc2Web = new LavendereTc2Web();
					for (int i = 0; i < size; i++) {
						DadosTc2Web dadosTc2Web = (DadosTc2Web) dadosTc2WebList.items[i];
						try {
							if (Pedido.TABLE_NAME_PEDIDO.equals(dadosTc2Web.tableName)) {
								tc2Web.enviaPedidoServidorBackground(cdSessaoEnviando, dadosTc2Web.cdRegistro, dadosTc2Web.nuLinhas, dadosTc2Web.cbas);
								dadosPedidoEnviadosSucessoCache.addElement(dadosTc2Web);
							} else if (VisitaFotoDao.TABLE_NAME.equals(dadosTc2Web.tableName)) {
								enviaImagensVisitas(tc2Web.paramsSync, dadosTc2Web);
							} else { 
								tc2Web.enviaDadosServidorBackground(cdSessaoEnviando, dadosTc2Web.tableName, dadosTc2Web.nuLinhas, dadosTc2Web.cbas);
								dadosEnviadosSucessoCache.addElement(dadosTc2Web);
							}
						} catch (Throwable e) {
							dadosEnviadosErroCache.addElement(dadosTc2Web);
							if (Pedido.TABLE_NAME_PEDIDO.equals(dadosTc2Web.tableName)) {
								addErrorPedidoNaoEnviado(dadosTc2Web.cdRegistro, dadosTc2Web.rowKeys.items[0].toString());
							} else {
								addErrorDadosNaoEnviados();
							}
						} 
					}
					while (EnviaDadosThread.getInstance().dadosPedidoEnviadosSucessoCache.size() > 0) {
						DadosTc2Web dadosTc2Web = (DadosTc2Web) EnviaDadosThread.getInstance().dadosPedidoEnviadosSucessoCache.items[0];
						int sizeRowKeys = dadosTc2Web.rowKeys.size();
				    	for (int j = 0; j < sizeRowKeys; j++) {
							PedidoService.getInstance().updateInfosPedidoAposEnvioServidor((Pedido)PedidoService.getInstance().findByRowKey((String)dadosTc2Web.rowKeys.items[j]), tc2Web.paramsSync.baseUrl);
				    	}
				    	EnviaDadosThread.getInstance().dadosPedidoEnviadosSucessoCache.removeElement(dadosTc2Web);
				    	//--
					}
					tc2Web.executeFimEnvioDados(cdSessaoEnviando);
				} catch (Throwable e) {
					addErrorDadosNaoEnviados();
				} finally {
					sessoesEnvioHash.remove(cdSessaoEnviando);
				}
			}
		} catch (Throwable e) {
			try {
				addErrorDadosNaoEnviados();
			} catch (Throwable ee) {
				ExceptionUtil.handle(ee);
			}
		} finally {
			SessionLavenderePda.releaseLockSync();
			cdSessaoEnviando = "";
		}
	}
	
	private void enviaImagensVisitas(ParamsSync ps, DadosTc2Web dadosTc2Web) throws Exception {
		if (dadosTc2Web.imageList.size() > 0) {
			LavendereTc2Web pdaToErpHttp = new LavendereTc2Web();
			pdaToErpHttp.envieImagens(VisitaFoto.getPathImg(), dadosTc2Web.imageList, VisitaFotoDao.TABLE_NAME, false);
			pdaToErpHttp.enviaDadosServidorBackground(cdSessaoEnviando, dadosTc2Web.tableName, dadosTc2Web.nuLinhas, dadosTc2Web.cbas);
			dadosEnviadosSucessoCache.addElement(dadosTc2Web);
		}
	}
	
	public static void addErrorUsuarioInativado() throws SQLException  {
		String msgErro = Messages.THREAD_ENVIO_DADOS_USUARIO_INATIVO;
		addMsgAlerta(msgErro);
		getInstance().erroEnvioDados.addElement(msgErro);
	}

	private void addErrorDadosNaoEnviados() throws SQLException {
		String msgErro = Messages.THREAD_ENVIO_DADOS_ENVIAR_MANUAL;
		addMsgAlerta(msgErro);
		erroEnvioDados.addElement(msgErro);
	}

	public static void addErrorPedidoNaoEnviado(String nuPedido, String rowkey) throws SQLException {
		String msgErro = Messages.THREAD_ENVIO_DADOS_ERRO_ENVIAR_PEDIDO;
		int indexMsgJaInserida = -1;
		for (int i = 0; i < BaseUIForm.getMsgAlerta().size(); i++) {
			if (((String)BaseUIForm.getMsgAlerta().items[i]).startsWith(msgErro)) {
				indexMsgJaInserida = i;
			}
		}
		getInstance().erroEnvioDados.addElement(msgErro + nuPedido);
		if (indexMsgJaInserida == -1) {
			addMsgAlerta(msgErro + nuPedido);
		} else {
			String msgAtual = (String)BaseUIForm.getMsgAlerta().items[indexMsgJaInserida];
			msgAtual += ", " + nuPedido;
			BaseUIForm.getMsgAlerta().removeElementAt(indexMsgJaInserida);
			addMsgAlerta(msgAtual);
		}
		ErroEnvioService.getInstance().saveErroEnvioPedido(rowkey, msgErro);
	}

	private static void addMsgAlerta(String msgAtual) throws SQLException {
		MainLavenderePda.getInstance().runOnMainThread(new Runnable() {
			@Override
			public void run() {
				try {
					BaseUIForm.addMsgAlerta(msgAtual);
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			}
		});
	}

	@Override
	public void exec() {
		executeEnvio();
	}
	
	public void addQueue() {
		AsyncPool.getInstance().execute(this);
	}
	
	public void enviaPedido(String cdSessao, Pedido pedido) {
		AsyncPool.getInstance().execute(new RunnableImpl() {
			
			@Override
			public void exec() {
				if (PedidoService.getInstance().isPedidoSemNota(pedido)) {
					return;
				}
				try (	ByteArrayStream cbasRetorno = new ByteArrayStream(4096);
						Statement stPedido = CrudDbxDao.getCurrentDriver().getStatement();
						Statement stItemPedido = CrudDbxDao.getCurrentDriver().getStatement();
						ResultSet rsPedido = stPedido.executeQuery(PedidoPdbxDao.getInstance().findByRowKeySql(pedido.getRowKey()));
						ResultSet rsItensPedido = stItemPedido.executeQuery(ItemPedidoService.getInstance().findSqlItemPedido(pedido))) {
					
					try {
						LavendereTc2Web lavendereTc2Web = new LavendereTc2Web();
						int nuLinhas = lavendereTc2Web.montaPacoteEnvioPedidoServidor(pedido, rsPedido, rsItensPedido, cbasRetorno);
						int result = lavendereTc2Web.antesEnvioPedido(pedido);
						if (result > 0) {
							put(Pedido.TABLE_NAME_PEDIDO, nuLinhas, cbasRetorno, cdSessao, pedido.getRowKey(), pedido.nuPedido);
							addQueue();
						}
					} catch (ValidationException | DataBaseValidationException e) {
						addErrorPedidoNaoEnviado(pedido.nuPedido, pedido.getRowKey());
						return;
					}
				} catch (Throwable e) {
					NotificationManager.putNotification(NotificationManager.getErrorNotification(e.getMessage()));
				}
				
			}
		});
	}
	
	public void enviaVisita(String cdSessao, Visita visita) {
		if (visita != null && ValueUtil.isNotEmpty(visita.rowKey)) {
			AsyncPool.getInstance().execute(new RunnableImpl() {
				@Override
				public void exec() {
					try (Statement st = CrudDbxDao.getCurrentDriver().getStatement();
							ResultSet visitaRS = st.executeQuery(VisitaService.getInstance().findByRowKeySql(visita.rowKey))) {
						if (visitaRS.isBeforeFirst()) {
							LavendereTc2Web lavendereTc2Web = new LavendereTc2Web(new ParamsSync());
							ByteArrayStream cbasRetorno = new ByteArrayStream(4096);
							int nuLinhas = lavendereTc2Web.writeDados(Visita.TABLE_NAME, visitaRS, cbasRetorno, false, null).size();
							if (nuLinhas > 0 ) {
								put(Visita.TABLE_NAME, nuLinhas, cbasRetorno, cdSessao, visita.rowKey);
								addQueue();
							}
						}
					} catch (Throwable e) {
						NotificationManager.putNotification(NotificationManager.getErrorNotification(e.getMessage()));
					}
				}
			});
		}
	}
	
	public void enviaVisitaPedido(String cdSessao, VisitaPedido visitaPedido) {
		if (visitaPedido != null && ValueUtil.isNotEmpty(visitaPedido.rowKey)) {
			AsyncPool.getInstance().execute(new RunnableImpl() {
				@Override
				public void exec() {
					try (Statement st = CrudDbxDao.getCurrentDriver().getStatement();
							ResultSet visitaPedidoRS = st.executeQuery(VisitaPedidoService.getInstance().findByRowKeySql(visitaPedido.rowKey))) {
						if (visitaPedidoRS.isBeforeFirst()) {
							LavendereTc2Web lavendereTc2Web = new LavendereTc2Web(new ParamsSync());
							ByteArrayStream cbasRetorno = new ByteArrayStream(4096);
							int nuLinhas = lavendereTc2Web.writeDados(VisitaPedido.TABLE_NAME, visitaPedidoRS, cbasRetorno, false, null).size();
							if (nuLinhas > 0 ) {
								put(VisitaPedido.TABLE_NAME, nuLinhas, cbasRetorno, cdSessao, visitaPedido.rowKey);
								addQueue();
							}
						}
					} catch (Throwable e) {
						NotificationManager.putNotification(NotificationManager.getErrorNotification(e.getMessage()));
					}
				}
			});
		}
	}
	
	public void montaDadosEnvioNotificaoBackGround(String cdSessao, String rowKey) throws SQLException {
		if (LavenderePdaConfig.enviaInformacoesVisitaOnline) {
			AsyncPool.getInstance().execute(new RunnableImpl() {
				@Override
				public void exec() {
					try (Statement st = CrudDbxDao.getCurrentDriver().getStatement();
							ResultSet notificacaoRS = st.executeQuery(NotificacaoPdaService.getInstance().findByRowKeySql(rowKey));
							ByteArrayStream cbasRetorno = new ByteArrayStream(4096)) {
						if (notificacaoRS.isBeforeFirst()) {
							LavendereTc2Web lavendereTc2Web = new LavendereTc2Web(new ParamsSync());
							NotificacaoPdaDbxDao.getInstance().putNotificacaoOnCache(rowKey);
							int nuLinhas = lavendereTc2Web.writeDados(NotificacaoPda.TABLE_NAME, notificacaoRS, cbasRetorno, false, null).size();
							if (nuLinhas > 0 ) {
								put(NotificacaoPda.TABLE_NAME, nuLinhas, cbasRetorno, cdSessao, rowKey);
								addQueue();
							}
						}
					} catch (Throwable e) {
						NotificationManager.putNotification(NotificationManager.getErrorNotification(e.getMessage()));
					}
				}
			});
		}
	}
	
	public void enviaPedidoConsignadoBackground(Vector pedidoConsignacaoDevolvidoList) throws SQLException {
		if (pedidoConsignacaoDevolvidoList.isEmpty()) {
			return;
		}
		AsyncPool.getInstance().execute(new RunnableImpl() {
			@Override
			public void exec() {
				PedidoConsignacao pedidoConsignacao;
				for (int i = 0; i < pedidoConsignacaoDevolvidoList.size(); i++) {
					pedidoConsignacao = (PedidoConsignacao) pedidoConsignacaoDevolvidoList.items[i];
					try (Statement st = PedidoConsignacaoDao.getCurrentDriver().getStatement();
							ResultSet rsPedidoConsignacaoRS = st.executeQuery(PedidoConsignacaoService.getInstance().findByRowKeySql(pedidoConsignacao.getRowKey()))) {
						if (rsPedidoConsignacaoRS.isBeforeFirst()) {
							LavendereTc2Web lavendereTc2Web = new LavendereTc2Web(new ParamsSync());
							ByteArrayStream cbasRetorno = new ByteArrayStream(4096);
							int  nuLinhas = lavendereTc2Web.writeDados(PedidoConsignacao.TABLE_NAME, rsPedidoConsignacaoRS, cbasRetorno, false, null).size();
							String cdSessao = PedidoConsignacaoService.getInstance().generateIdGlobal();
							put(PedidoConsignacao.TABLE_NAME, nuLinhas, cbasRetorno, cdSessao, pedidoConsignacao.getRowKey());
							PedidoConsignacaoService.getInstance().updatePedidoConsignacaoProcessandoTransmissao(pedidoConsignacao.getRowKey());
						}
					} catch (Throwable e) {
						NotificationManager.putNotification(NotificationManager.getErrorNotification(e.getMessage()));
					}
				}
				addQueue();
			}
		});
	}
	
    public void envioColetaGpsBackground(ColetaGps coletaGps) throws SQLException {
		if (coletaGps != null && ValueUtil.isNotEmpty(coletaGps.rowKey)) {
			AsyncPool.getInstance().execute(new RunnableImpl() {
				@Override
				public void exec() {
					try (Statement st = CrudDbxDao.getCurrentDriver().getStatement();
							ResultSet coletaGpsRS = st.executeQuery(ColetaGpsService.getInstance().findByRowKeySql(coletaGps.rowKey))) {
						if (coletaGpsRS.isBeforeFirst()) {
							LavendereTc2Web lavendereTc2Web = new LavendereTc2Web(new ParamsSync());
							ByteArrayStream cbas = new ByteArrayStream(4096);
							int nuLinhas = lavendereTc2Web.writeDados(ColetaGps.TABLE_NAME, coletaGpsRS, cbas, false, null).size();
							put(ColetaGps.TABLE_NAME, nuLinhas, cbas, ColetaGpsService.getInstance().generateIdGlobal(), coletaGps.rowKey);
							addQueue();
						}
					} catch (Throwable e) {
						NotificationManager.putNotification(NotificationManager.getErrorNotification(e.getMessage()));
					}
				}
			});
		}
	}
    
	public void montaDadosEnvioItemLiberacaoBackground(String cdSessao, ItemLiberacao itemLiberacao) throws SQLException {
		if (itemLiberacao != null && ValueUtil.isNotEmpty(itemLiberacao.rowKey)) {
			AsyncPool.getInstance().execute(new RunnableImpl() {
				@Override
				public void exec() {
					try (Statement st = CrudDbxDao.getCurrentDriver().getStatement();
							ResultSet itemLiberacaoRS = st.executeQuery(ItemLiberacaoService.getInstance().findByRowKeySql(itemLiberacao.rowKey))) {
						if (itemLiberacaoRS.isBeforeFirst()) {
							LavendereTc2Web lavendereTc2Web = new LavendereTc2Web(new ParamsSync());
							ByteArrayStream cbasRetorno = new ByteArrayStream(4096);
							int nuLinhas = lavendereTc2Web.writeDados(ItemLiberacao.TABLE_NAME, itemLiberacaoRS, cbasRetorno, false, null).size();
							if (nuLinhas > 0) {
								put(ItemLiberacao.TABLE_NAME, nuLinhas, cbasRetorno, cdSessao, itemLiberacao.rowKey);
								addQueue();
							}
						}
					} catch (Throwable e) {
						NotificationManager.putNotification(NotificationManager.getErrorNotification(e.getMessage()));
					}
				}
			});
		}
	}
	
	public void enviaItemPedidoBackground(ItemPedido itemPedido) {
		if (itemPedido == null || ValueUtil.isEmpty(itemPedido.rowKey)) {
			return;
		}
		AsyncPool.getInstance().execute(new RunnableImpl() {
			@Override
			public void exec() {
				try (Statement st = CrudDbxDao.getCurrentDriver().getStatement();
						ResultSet itemPedidoRS = st.executeQuery(ItemPedidoService.getInstance().findByRowKeySql(itemPedido.rowKey))) {
					if (itemPedidoRS.isBeforeFirst()) {
						LavendereTc2Web lavendereTc2Web = new LavendereTc2Web(new ParamsSync());
						ByteArrayStream cbasRetorno = new ByteArrayStream(4096);
						int nuLinhas = lavendereTc2Web.writeDados(ItemPedido.TABLE_NAME_ITEMPEDIDO, itemPedidoRS, cbasRetorno, false, null).size();
						if (nuLinhas > 0 ) {
							put(ItemPedido.TABLE_NAME_ITEMPEDIDO, nuLinhas, cbasRetorno, ItemPedidoService.getInstance().generateIdGlobal(), itemPedido.rowKey);
							addQueue();
						}
					}
				} catch (Throwable e) {
					NotificationManager.putNotification(NotificationManager.getErrorNotification(e.getMessage()));
				}
			}
		});
	}
	
	public void envioPedidoDescBackground(String cdSessao, PedidoDesc pedidoDesc) throws SQLException {
		if (pedidoDesc != null && ValueUtil.isNotEmpty(pedidoDesc.rowKey)) {
			AsyncPool.getInstance().execute(new RunnableImpl() {
				@Override
				public void exec() {
					try (Statement st = CrudDbxDao.getCurrentDriver().getStatement();
							ResultSet pedidoDescRS = st.executeQuery(PedidoDescService.getInstance().findByRowKeySql(pedidoDesc.rowKey))) {
						if (pedidoDescRS.isBeforeFirst()) {
							LavendereTc2Web lavendereTc2Web = new LavendereTc2Web(new ParamsSync());
							ByteArrayStream cbasRetorno = new ByteArrayStream(4096);
							int nuLinhas = lavendereTc2Web.writeDados(PedidoDesc.TABLE_NAME, pedidoDescRS, cbasRetorno, false, null).size();
							if (nuLinhas > 0 ) {
								put(PedidoDesc.TABLE_NAME, nuLinhas, cbasRetorno, cdSessao, pedidoDesc.rowKey);
								addQueue();
							}
						}
					} catch (Throwable e) {
						NotificationManager.putNotification(NotificationManager.getErrorNotification(e.getMessage()));
					}
				}
			});
		}
	}

}
