package br.com.wmw.lavenderepda.thread;

import br.com.wmw.framework.async.AsyncPool;
import br.com.wmw.framework.async.RunnableImpl;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.sync.SyncInfo;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ConfigNotificacao;
import br.com.wmw.lavenderepda.business.domain.Notificacao;
import br.com.wmw.lavenderepda.business.service.ConfigNotificacaoService;
import br.com.wmw.lavenderepda.business.service.NotificacaoService;
import br.com.wmw.lavenderepda.presentation.ui.ListNotificacaoForm;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.io.ByteArrayStream;
import totalcross.io.CRC32Stream;
import totalcross.io.DataStream;
import totalcross.io.IOException;
import totalcross.sys.Convert;
import totalcross.sys.Time;
import totalcross.sys.Vm;
import totalcross.util.Date;
import totalcross.util.Hashtable;
import totalcross.util.Vector;
import totalcross.util.concurrent.Lock;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class ServicoNotificacaoThread extends RunnableImpl {

	public static final String ALL_TABLES = "allTables";
	public static final String ALIAS_CDUSUARIODESTINO = "CDUSUARIODESTINO";
	public static final String COLUMN_DTALTERACAO = "TB.DTALTERACAO";
	public static final String COLUMN_HRALTERACAO = "TB.HRALTERACAO";
	public static final String ALIAS_DSCHAVE = "DSCHAVE";

	public static final String PRM_DSMENSAGEM = ":DSMENSAGEM";
	public static final String PRM_COLUNASCHAVE = ":COLUNASCHAVE";
	public static final String PRM_AGGCOLUNASCHAVE = ":AGGCOLUNASCHAVE";
	public static final String PRM_CONTROLEDATA = ":CONTROLEDATA";
	public static final String USUARIOSESSAO = ":USUARIOSESSAO";
	public static final String QTNOTIFICACAO = ":QTNOTIFICACAO";
	public static final String ALIAS_TB = "TB.";

	private LinkedHashSet<String> classNames;
	private boolean controleChave;
	private boolean controleDataHoraAlteracao;
	private boolean substituir;
	private boolean comPrmColumnskeys = false;
	private boolean comColunasChaveAgg = false;
	private int qtNotificacaoGerada = -1;
	private boolean comCHAVE = false;
	private Hashtable infoList;
	private static final Lock LOCK = new Lock();

	private ServicoNotificacaoThread(LinkedHashSet<String> classNames, Hashtable newInfoList) {
		this.classNames = classNames;
		this.infoList = newInfoList;
	}

	private ServicoNotificacaoThread() {
		this.classNames = new LinkedHashSet<>();
		classNames.add(ALL_TABLES);
		this.infoList = null;
	}

	@Override
	public void exec() {
		synchronized (LOCK) {
			startNotificacao();
		}
	}

	public static void asyncPoolExecute(LinkedHashSet<String> classNames, Hashtable newInfoList) {
		new ServicoNotificacaoThread(classNames, newInfoList).execute();
	}

	public static void asyncPoolExecute() {
		new ServicoNotificacaoThread().execute();
	}

	private void execute() {
		if (getConfigNotificacaoSistemaService().existeConfigNotificacao()) {
			AsyncPool.getInstance().execute(this);
		}
	}

	private void startNotificacao() {
		try {
			ConfigNotificacao filterConfigNotificacao = new ConfigNotificacao();
			filterConfigNotificacao.flSistema = ConfigNotificacao.FLSISTEMA_APP;
			if (classNames == null || classNames.isEmpty()) {
				return;
			} else if (!classNames.contains(ALL_TABLES)) {
				Vector newclassNames = new Vector();
				for (String className : classNames) {
					String entidade = className.replace(CrudDbxDao.PREFIXO_TABLE_APP_VENDAS, "");
					newclassNames.addElement(entidade);
				}
				filterConfigNotificacao.setNmEntidadeList(newclassNames);
			}
			Vector configNotificacaoSistemas = getConfigNotificacaoSistemaService().findAllByExample(filterConfigNotificacao);
			int size = configNotificacaoSistemas.size();
			StringBuilder dsSql = new StringBuilder();
			for (int i = 0; i < size; i++) {
				try {
					ConfigNotificacao configNotificacao = (ConfigNotificacao) configNotificacaoSistemas.elementAt(i);
					loadParametros(configNotificacao);
					dsSql.setLength(0);
					dsSql.append(configNotificacao.dsSql.replace('&', '\''));
					prepareMensagem(dsSql, configNotificacao);
					addStringAggChaves(dsSql, configNotificacao);
					addColumnsKeys(dsSql, configNotificacao);
					addFiltroDataHoraAlteracao(dsSql, configNotificacao.getNmEntidadeComPrefix());
					addFiltroUsuario(dsSql);
					marcarComoLido(configNotificacao);
					createByOffSet(dsSql, configNotificacao);
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			}
			if (qtNotificacaoGerada > 0) {
				ListNotificacaoForm.showNotificacaoIfHasNotificacaoNaoLidos();
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	private void addStringAggChaves(StringBuilder dsSql, ConfigNotificacao configNotificacao) throws SQLException {
		String nmEntidade = configNotificacao.getNmEntidadeComPrefix();
		if (comColunasChaveAgg && ValueUtil.isEmpty(nmEntidade)) {
			throw new ValidationException(MessageUtil.getMessage(Messages.NOTIFICACAO_ERRO_SQL_SEM_ENTIDADE, new Object[]{PRM_AGGCOLUNASCHAVE, configNotificacao.cdConfigNotificacao}));
		}
		if (comColunasChaveAgg && ValueUtil.isNotEmpty(nmEntidade)) {
			String[] keys = getStringColumnsKeys(nmEntidade).split(",");
			StringBuilder stringAgg = new StringBuilder();
			stringAgg.append("group_concat(");
			String concat = "";
			for (String column : keys) {
				stringAgg.append(concat).append(column.trim());
				concat = " || ';' || ";
			}
			stringAgg.append(",'|') as ").append(ALIAS_DSCHAVE);
			replaceAll(dsSql, PRM_AGGCOLUNASCHAVE, stringAgg.toString());
		}
	}

	private void createByOffSet(StringBuilder dsSql, ConfigNotificacao configNotificacao) throws SQLException {
		long limit = 1000;
		long offset = 0;
		boolean loop = true;
		StringBuilder sqlOffSet = new StringBuilder();
		while (loop) {
			sqlOffSet.setLength(0);
			sqlOffSet.append("select * from (\n");
			sqlOffSet.append(dsSql);
			sqlOffSet.append("\n)");
			sqlOffSet.append(" limit ").append(limit);
			sqlOffSet.append(" offset ").append(offset);
			Vector rowsNotificacoesSistema = getConfigNotificacaoSistemaService().creatRows(sqlOffSet.toString());
			loop = !rowsNotificacoesSistema.isEmpty();
			if (!loop) {
				break;
			}
			createNotificacaoUsuarios(configNotificacao, rowsNotificacoesSistema);
			offset += limit;
		}
	}

	private void createNotificacaoUsuarios(ConfigNotificacao configNotificacao, Vector rowsNotificacoesSistema) throws SQLException {
		int sizeNotificacao = rowsNotificacoesSistema.size();
		Vector list = new Vector();
		for (int y = 0; y < sizeNotificacao; y++) {
			Hashtable rowNotificacao = (Hashtable) rowsNotificacoesSistema.elementAt(y);
			Notificacao notificacao = criaNotificacao(configNotificacao, rowNotificacao);
			String cdUsuario = (String) rowNotificacao.get(ALIAS_CDUSUARIODESTINO);
			criaNotificaUsuario(notificacao, cdUsuario, configNotificacao, rowNotificacao);
			list.addElement(notificacao);
			if (list.size() == 500 || isLast(y, sizeNotificacao)) {
				int qtInsert = NotificacaoService.getInstance().insertOrIgnore(list, controleChave);
				houverNotificacao(qtInsert);
				list = new Vector();
				if (isNotLast(y, sizeNotificacao)) {
					Vm.sleep(300);
				}
			}
		}
	}

	private boolean isNotLast(int index, int size) {
		return !isLast(index, size);
	}

	private boolean isLast(int index, int size) {
		return index == size - 1;
	}

	private void houverNotificacao(int qtInsert) {
		if (qtNotificacaoGerada <= 0) {
			qtNotificacaoGerada = qtInsert;
		}
	}

	private void criaNotificaUsuario(Notificacao notificacao, String cdUsuario, ConfigNotificacao configNotificacao, Hashtable rowNotificacao) throws SQLException {
		String vlchave = getRowkeyValues(rowNotificacao, configNotificacao, cdUsuario);
		notificacao.cdNotificacao = getCRC32(vlchave);
		notificacao.vlChave = vlchave;
		notificacao.cdInstante = StringUtil.getStringValue(TimeUtil.getCurrentTime().getSQLLong());
		notificacao.cdUsuarioDestino = cdUsuario;
		notificacao.dtAlteracao = new Date();
		notificacao.hrAlteracao = TimeUtil.getCurrentTimeHHMMSS();
	}

	private String getCRC32(String vlchave) {
		CRC32Stream crc = new CRC32Stream(new ByteArrayStream(0));
		crc.reset();
		try (DataStream ds = new DataStream(crc)) {
			ds.writeCString(vlchave);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return Convert.toString(crc.getValue());
	}

	private void loadParametros(ConfigNotificacao configNotificacao) {
		controleChave = configNotificacao.isPropJsonLigado("controleChave");
		controleDataHoraAlteracao = configNotificacao.dsSql.contains(PRM_CONTROLEDATA);
		substituir = configNotificacao.isPropJsonLigado("substituir");
		comPrmColumnskeys = configNotificacao.dsSql.contains(PRM_COLUNASCHAVE);
		comCHAVE = configNotificacao.dsSql.contains(ALIAS_DSCHAVE);
		comColunasChaveAgg = configNotificacao.dsSql.contains(PRM_AGGCOLUNASCHAVE);
		if (!comColunasChaveAgg) {
			comPrmColumnskeys = configNotificacao.dsSql.contains(PRM_COLUNASCHAVE);
		}
	}

	private void marcarComoLido(ConfigNotificacao configNotificacao) throws SQLException {
		if (substituir) {
			Notificacao notificacaoFilter = new Notificacao();
			notificacaoFilter.dsTipoNotificacao = configNotificacao.tipoNotificacao.dsTipoNotificacao;
			getNotificacaoSistemaService().marcarComoLido(notificacaoFilter);
		}
	}

	private NotificacaoService getNotificacaoSistemaService() {
		return NotificacaoService.getInstance();
	}

	private ConfigNotificacaoService getConfigNotificacaoSistemaService() {
		return ConfigNotificacaoService.getInstance();
	}

	private Notificacao criaNotificacao(ConfigNotificacao configNotificacao, Hashtable row) {
		Notificacao notificacao = new Notificacao();
		notificacao.dsMensagem = row.getString("DSMENSAGEM");
		notificacao.flLido = "N";
		notificacao.dsTipoNotificacao = configNotificacao.tipoNotificacao.dsTipoNotificacao;
		return notificacao;
	}

	private void addFiltroDataHoraAlteracao(StringBuilder dsSql, String nmEntidade) throws Exception {
		if (controleDataHoraAlteracao) {
			SyncInfo syncInfo = getSyncInfo(nmEntidade);
			StringBuilder sqlControleDataHora = new StringBuilder();
			if (syncInfo.maxDataHoraAlteracao != null) {
				Time dataLimiteNotificacao = syncInfo.maxDataHoraAlteracao;
				String strData = new Date(dataLimiteNotificacao).getSQLString();
				sqlControleDataHora.append(" (").append(COLUMN_DTALTERACAO).append(" > '").append(strData).append("'");
				sqlControleDataHora.append(" OR (");
				sqlControleDataHora.append(COLUMN_DTALTERACAO).append(" = '").append(strData).append("'");
				sqlControleDataHora.append(" AND ");
				String strHora = TimeUtil.formatTimeString(dataLimiteNotificacao, true, true);
				sqlControleDataHora.append(COLUMN_HRALTERACAO).append(" >= '").append(strHora).append("'");
				sqlControleDataHora.append(" ))");
			} else {
				sqlControleDataHora.append("(1 = 2)");
			}
			replaceAll(dsSql, PRM_CONTROLEDATA, sqlControleDataHora.toString());
		}
	}

	private void addFiltroUsuario(StringBuilder dsSql) {
		replaceAll(dsSql, USUARIOSESSAO, "'" + SessionLavenderePda.usuarioPdaRep.cdUsuario + "'");
	}

	private void prepareMensagem(StringBuilder dsSql, ConfigNotificacao configNotificacao) {
		if (ValueUtil.isNotEmpty(configNotificacao.dsMensagem)) {
			String dsmensagem = configNotificacao.dsMensagem.replace('&', '\'');
			replaceAll(dsSql, PRM_DSMENSAGEM, dsmensagem);
		}
	}

	private void replaceAll(StringBuilder sb, String oldChar, String newChar) {
		int start = sb.indexOf(oldChar);
		while (start >= 0) {
			int end = start + oldChar.length();
			sb.replace(start, end, newChar);
			start += newChar.length();
			start = sb.indexOf(oldChar, start);
		}
	}

	private void addColumnsKeys(StringBuilder dsSql, ConfigNotificacao configNotificacao) throws SQLException {
		String nmEntidade = configNotificacao.getNmEntidadeComPrefix();
		if (comPrmColumnskeys && ValueUtil.isEmpty(nmEntidade)) {
			throw new ValidationException(MessageUtil.getMessage(Messages.NOTIFICACAO_ERRO_SQL_SEM_ENTIDADE, new Object[]{PRM_COLUNASCHAVE, configNotificacao.cdConfigNotificacao}));
		}
		if (comPrmColumnskeys) {
			replaceAll(dsSql, PRM_COLUNASCHAVE, getStringColumnsKeys(nmEntidade));
		}
	}

	private String getRowkeyValues(Hashtable row, ConfigNotificacao configNotificacao, String cdUsuario) throws SQLException {
		return getRowkeyValues(row, configNotificacao, cdUsuario, true);
	}

	private String getRowkeyValues(Hashtable row, ConfigNotificacao configNotificacao, String cdUsuario, boolean comChaveDomain) throws SQLException {
		String nmEntidade = configNotificacao.getNmEntidadeComPrefix();
		Key key = new Key();
		if (comChaveDomain) {
			if (comPrmColumnskeys) {
				SyncInfo syncInfo = getSyncInfo(nmEntidade);
				String[] keys = syncInfo.keys;
				for (String column : keys) {
					key.keysDomain.put(column, row.get(column));
				}
			} else if (comCHAVE || comColunasChaveAgg) {
				key.keysDomain.put(ALIAS_DSCHAVE, row.get(ALIAS_DSCHAVE));
			}
		}
		key.tableName = nmEntidade;
		key.user = cdUsuario;
		key.keysConfig.put("rowKey", configNotificacao.getRowKey());
		return key.toJson();
	}


	private String getStringColumnsKeys(String nmEntidade) throws SQLException {
		SyncInfo syncInfo = getSyncInfo(nmEntidade);
		StringBuilder newColumns = new StringBuilder();
		String[] keys = syncInfo.keys;
		for (int i = 0; i < keys.length; i++) {
			String column = keys[i];
			boolean last = isLast(i, keys.length);
			String vir = !last ? ", " : "";
			newColumns.append(ALIAS_TB).append(column + vir);
		}
		return newColumns.toString();
	}

	private SyncInfo getSyncInfo(String nmTabela) throws SQLException {
		if (infoList != null) {
			return (SyncInfo) infoList.get(nmTabela);
		} else {
			return SyncManager.getSyncInfo(nmTabela);
		}
	}

	public static class Key {
		
		private String user;
		private String tableName;
		private Map<String, Object> keysConfig = new LinkedHashMap<>();
		private Map<String, Object> keysDomain = new LinkedHashMap<>();

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}

		public Map<String, Object> getKeysDomain() {
			return keysDomain;
		}

		public Map<String, Object> getKeysConfig() {
			return keysConfig;
		}

		public String getTableName() {
			return tableName;
		}

		public void setTableName(String tableName) {
			this.tableName = tableName;
		}

		public String toJson() {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("user", user);
			map.put("tableName", tableName);
			map.put("keysConfig", keysConfig);
			map.put("keysDomain", keysDomain);
			return map.toString();
		}
	}
	

}
