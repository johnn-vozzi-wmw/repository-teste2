package br.com.wmw.lavenderepda.sync;

import br.com.wmw.framework.sync.SyncComponent;

public class AtualizaTabelaWebTc extends SyncComponent {
//	
//	private SQLiteDriver driver;
//	private final SyncLogService log;
//	
//	private final ByteArrayStream bas;
//	private final String tablename;
//	private int countAtualizacoes;
//	private SyncInfo syncInfo;
//	
//	private int indexCampNuCarimbo;
//	private int maxNuCarimbo;
//	private final int maxNuCarimboOld;
//	
//	public AtualizaTabelaWebTc(final ByteArrayStream syncDataByteArrayStream, final String tablename, final int nuCarimbo, final SyncLogService log) {
//		this.bas = syncDataByteArrayStream;
//		this.tablename = tablename;
//		this.log = log == null ? LogPdaService.getInstance() : log;
//		this.maxNuCarimboOld = nuCarimbo;
//		this.maxNuCarimbo = nuCarimbo;
//	}
//	
//	private boolean verificaParamsInvalidos() {
//		return bas == null || ValueUtil.isEmpty(tablename); 
//	}
//	
//	public int execute() throws Exception {
//		if (verificaParamsInvalidos()) {
//			return 0;
//		}
//		LineReader lr = new LineReader(bas);
//		String protocolo = lr.readLine();
//		if (protocolo == null || "FIM".equals(protocolo)) {
//			return 0;
//		}
//		this.syncInfo = SyncManager.getSyncInfo(tablename);
//		String[] campos = StringUtil.split(lr.readLine(), SEPARADOR_ATUALIZACOES);
//		IntHashtable hashColsPos = new IntHashtable(campos.length);
//		for (int i = 0; i < campos.length; i++) {
//			hashColsPos.put(campos[i].toLowerCase(), i);
//		}
//		indexCampNuCarimbo = hashColsPos.get(BaseDomain.NMCAMPONUCARIMBO.toLowerCase());
//		try (PreparedStatement pstmtConsulta = driver.prepareStatement(getSqlConsulta(tablename));
//				PreparedStatement pstmtUpdate = driver.prepareStatement(getSqlAtualizacaoSemRowKey(tablename, campos));
//				PreparedStatement pstmtInsert = driver.prepareStatement(getSqlInsercao(tablename, campos));
//				PreparedStatement pstmtExclusao = driver.prepareStatement(getSqlExclusao(tablename));
//				PreparedStatement pstmtUpdateByCarimbo = driver.prepareStatement(getSqlAlteracaoNuCarimbo(tablename));
//				Statement st = getDriver().getStatement();
//				ResultSet rs = st.executeQuery(getSqlConsultaNovo(tablename))) {
//			ResultSetMetaData rsMetaData = rs.getMetaData();
//			int columnCount = rsMetaData.getColumnCount();
//			IntHashtable hashColsPosTypes = getColsType(rsMetaData, columnCount);
//			readUpdateLines(lr, hashColsPos, rsMetaData, hashColsPosTypes);
//		} catch (Exception e) {
//			log.logSyncError(SyncUtil.getEntityName(tablename) + " ERRO: " + e.getMessage());
//			throw e;
//		}
//		return countAtualizacoes;
//	}
//
//	private void readUpdateLines(LineReader lr, IntHashtable hashColsPos, ResultSetMetaData rsMetaData, IntHashtable hashColsPosTypes) throws SQLException, Exception, IOException {
//		String atualizacaoRow = lr.readLine();
//		CrudDbxDao.getCurrentDriver().startTransaction();
//		try {
//			while (atualizacaoRow != null) {
//				updateDbRow(hashColsPos, rsMetaData, hashColsPosTypes, atualizacaoRow);
//				if (countAtualizacoes % 500 == 0) {
//					pstmtUpdate.executeBatch();
//					pstmtExclusao.executeBatch();
//					pstmtInsert.executeBatch();
//					CrudDbxDao.getCurrentDriver().commit();
//				}
//				atualizacaoRow = lr.readLine();
//			}
//			updateCarimbo();
//			CrudDbxDao.getCurrentDriver().commit();
//		} finally {
//			CrudDbxDao.getCurrentDriver().finishTransaction();
//		}
//		
//	}
//
//	private void updateCarimbo() throws Exception {
//		executeAtualizacaoNuCarimbo(pstmtUpdateByCarimbo, maxNuCarimbo, tablename, BaseDomain.NUCARIMBO_ORIGINAL);
//		executeAtualizacaoNuCarimbo(pstmtUpdateByCarimbo, maxNuCarimbo, tablename, maxNuCarimboOld);
//	}
//
//	private void updateDbRow(IntHashtable hashColsPos, ResultSetMetaData rsMetaData, IntHashtable hashColsPosTypes, String atualizacaoRow) throws Exception {
//		String[] valuesRow = StringUtil.split(atualizacaoRow, SEPARADOR_ATUALIZACOES, true);
//		int nuCarimboAtual = ValueUtil.getIntegerValue(valuesRow[indexCampNuCarimbo]);
//		if (nuCarimboAtual > maxNuCarimbo) {
//			maxNuCarimbo = nuCarimboAtual;
//		}
//		String rowKey = getRowKey(valuesRow, hashColsPos, syncInfo.keys);
//		String flTipoAlteracao = valuesRow[hashColsPos.get(BaseDomain.NMCAMPOTIPOALTERACAO.toLowerCase())];
//		if (BaseDomain.FLTIPOALTERACAO_EXCLUIDO.equals(flTipoAlteracao)) {
//			executeExclusao(pstmtExclusao, rowKey);
//			countAtualizacoes++;
//			return;
//		}
//		ResultSet rsPalm = executeConsulta(pstmtConsulta, rowKey);
//		try {
//			if (rsPalm.next()) {
//				executeAtualizacao(pstmtUpdate, valuesRow, hashColsPos, rsMetaData, rowKey, log, hashColsPosTypes);
//			} else {
//				executeInsercao(pstmtInsert, valuesRow, hashColsPos, rsMetaData, rowKey, log, hashColsPosTypes);
//			}
//			countAtualizacoes++;
//		} finally {
//			rsPalm.close();
//		}
//	}
//
//	private IntHashtable getColsType(ResultSetMetaData rsMetaData, int columnCount) throws SQLException {
//		IntHashtable hashColsPosTypes = new IntHashtable(columnCount);
//		for (int i = 1; i <= columnCount; i++) {
//			int type = SQLiteDriver.getColumnType(rsMetaData, i);
//			hashColsPosTypes.put(i, type);
//		}
//		return hashColsPosTypes;
//	}

}
