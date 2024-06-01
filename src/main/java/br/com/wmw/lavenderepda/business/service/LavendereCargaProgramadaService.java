package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Map.Entry;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CargaProgramadaService;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.database.metadata.Column;
import br.com.wmw.framework.database.metadata.Table;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.sync.SyncInfo;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescQuantidade;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.LavendereBaseDomain;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.RepresentanteEmp;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.util.Hashtable;

public class LavendereCargaProgramadaService extends CargaProgramadaService {
	
	private static final String NMCOLUNA_CDREPRESENTANTE = "cdrepresentante";
	private static final String NMCOLUNA_CDEMPRESA = "cdempresa";
	private static final String NMCOLUNA_CDUSUARIO = "cdusuario";
	public Hashtable infoList;
	private StringBuilder rowkeyValue;
	private StringBuilder insertClause;
	private StringBuilder selectClause;
	private boolean existeConfigNotificacao;
	
	public LavendereCargaProgramadaService(boolean inSyncBackground, Hashtable infoList) throws SQLException {
		super(inSyncBackground);
		this.infoList = infoList;
		rowkeyValue = new StringBuilder();
		insertClause = new StringBuilder();
		selectClause = new StringBuilder();
		existeConfigNotificacao = ConfigNotificacaoService.getInstance().existeConfigNotificacao();
	}

	@Override
	protected void processaMaxData(String nmTable) {
		if (existeConfigNotificacao) {
			SyncInfo syncInfo = (SyncInfo) infoList.get(nmTable);
			SyncManager.processaMaxDataHora(syncInfo, driver);
		}
	}
	
	@Override
	protected String getInsertSql(Table table) throws SQLException {
		if (usaUsuarioZero(table)) {
			StringBuilder sql = new StringBuilder();
			mountColumnsClauses(table);
			sql.append("insert or replace into ")
				.append(table.name)
				.append(" (")
				.append(insertClause.toString())
				.append(") ");
			sql.append("select ");
			sql.append(selectClause.toString())
				.append(" from att.")
				.append(table.name)
				.append(" a");
			appendJoin(table, sql);
			return sql.toString();
		}
		return super.getInsertSql(table);
	}

	private void appendJoin(Table table, StringBuilder sql) {
		boolean possuiCdRepresentante = table.existsColumnm(NMCOLUNA_CDREPRESENTANTE);
		boolean possuiCdEmpresa = table.existsColumnm(NMCOLUNA_CDEMPRESA);
		if (possuiCdRepresentante) {
			if (possuiCdEmpresa) {
				if (isDadosParaTodosRepSup(table)) {
					sql.append(" join (select cdempresa, cdsupervisor cdrepresentante from ")
						.append(SupervisorRep.TABLE_NAME)
						.append(" group by cdempresa, cdsupervisor)rep")
						.append(" on rep.cdempresa = a.cdempresa");
				} else {
					sql.append(" join ")
						.append(RepresentanteEmp.TABLE_NAME)
						.append(" rep on rep.cdempresa = a.cdempresa");
				}
			} else {
				sql.append(" join ")
					.append(Representante.TABLE_NAME)
					.append(" rep on 1=1");
			}
		}
		appendExtraJoin(table, sql);
	}

	private boolean isDadosParaTodosRepSup(Table table) {
		return SessionLavenderePda.isUsuarioSupervisor() && ValueUtil.isNotEmpty(LavenderePdaConfig.tabelasDadosParaTodosRepSup) && LavenderePdaConfig.tabelasDadosParaTodosRepSup.contains(";" + table.name.substring(5) + ";");
	}
	
	private void appendExtraJoin(Table table, StringBuilder sql) {
		if (ItemTabelaPreco.TABLE_NAME.equalsIgnoreCase(table.name) && LavenderePdaConfig.replicaDadosItemTabelaPreco0PorTabelaPreco) {
			sql.append(" join ")
				.append(TabelaPreco.TABLE_NAME)
				.append(" tab on tab.cdempresa = a.cdempresa and tab.cdrepresentante = rep.cdrepresentante");
		}
	}
	
	private void mountColumnsClauses(Table table) {
		LinkedList<String> columnsIgnore = appendColumnsReplaceInserClause(table);
		for (Entry<String, Column> entry : table.columns.entrySet()) {
			if (columnsIgnore.contains(entry.getValue().name)) {
				continue;
			}
			insertClause.append(entry.getValue().name)
				.append(",");
			if (BaseDomain.NMCAMPOTIPOALTERACAO.equalsIgnoreCase(entry.getValue().name)) {
				selectClause.append("case when a.")
					.append(entry.getValue().name)
					.append(" != 'E' then '' else a.")
					.append(entry.getValue().name)
					.append(" end");
			} else {
				selectClause.append("a.")
					.append(entry.getValue().name);
			}
			selectClause.append(",");
		}
		insertClause.deleteCharAt(insertClause.length()-1);
		selectClause.deleteCharAt(selectClause.length()-1);
	}
	
	private LinkedList<String> appendColumnsReplaceInserClause(Table table) {
		boolean cdRepresentanteColumn = table.existsColumnm(NMCOLUNA_CDREPRESENTANTE);
		boolean cdUsuarioColumn = table.existsColumnm(NMCOLUNA_CDUSUARIO);
		boolean replicaTabPrecoZero = ItemTabelaPreco.TABLE_NAME.equals(table.name) && LavenderePdaConfig.replicaDadosItemTabelaPreco0PorTabelaPreco;
		boolean descQuantidadeCdDesconto = DescQuantidade.TABLE_NAME.equalsIgnoreCase(table.name) && table.existsColumnm(DescQuantidade.CAMPO_CDDESCONTO);
		boolean rowKeyColumn = table.existsColumnm(BaseDomain.NMCAMPOROWKEY);
		LinkedList<String> addedColumns = new LinkedList<>();
		if (rowKeyColumn) {
			rowkeyValue = new StringBuilder();
			SyncInfo sync = (SyncInfo) infoList.get(table.name);
			for (String key : sync.keys) {
				if (NMCOLUNA_CDREPRESENTANTE.equalsIgnoreCase(key)) {
					rowkeyValue.append("rep.");
				} else if (replicaTabPrecoZero && ItemTabelaPreco.NOMECOLUNA_CDTABELAPRECO.equalsIgnoreCase(key)) {
					rowkeyValue.append("tab.");
				} else {
					rowkeyValue.append("a.");
				}
				rowkeyValue.append(key)
					.append(" || ';' || ");
			}
			rowkeyValue.delete(rowkeyValue.length() -3, rowkeyValue.length());
			insertClause.append(BaseDomain.NMCAMPOROWKEY).append(",");
			selectClause.append(rowkeyValue.toString())
				.append(BaseDomain.NMCAMPOROWKEY)
				.append(",");
			addedColumns.add(BaseDomain.NMCAMPOROWKEY);
		}
		if (cdRepresentanteColumn) {
			insertClause.append(NMCOLUNA_CDREPRESENTANTE).append(",");
			selectClause.append("rep.")
						.append(NMCOLUNA_CDREPRESENTANTE)
						.append(",");
			addedColumns.add(NMCOLUNA_CDREPRESENTANTE);
		}
		if (cdUsuarioColumn) {
			insertClause.append(LavendereBaseDomain.NMCOLUNA_CDUSUARIO).append(",");
			selectClause.append(Sql.getValue(Session.getCdUsuario()))
				.append(" ")
				.append(LavendereBaseDomain.NMCOLUNA_CDUSUARIO)
				.append(",");
			addedColumns.add(NMCOLUNA_CDUSUARIO);
		}
		if (replicaTabPrecoZero) {
			insertClause.append(ItemTabelaPreco.NOMECOLUNA_CDTABELAPRECO).append(",");
			selectClause.append("tab.").append(ItemTabelaPreco.NOMECOLUNA_CDTABELAPRECO)
				.append(",");
			addedColumns.add(ItemTabelaPreco.NOMECOLUNA_CDTABELAPRECO);
		}
		if (descQuantidadeCdDesconto) {
			insertClause.append(DescQuantidade.CAMPO_CDDESCONTO).append(",");
			selectClause.append("a.cdempresa")
				.append(" || ';' || ")
				.append("rep.cdrepresentante")
				.append(" || ';' || ")
				.append("a.cdtabelapreco")
				.append(" || ';' || ")
				.append("a.cdproduto ")
				.append(DescQuantidade.CAMPO_CDDESCONTO)
				.append(",");
			addedColumns.add(DescQuantidade.CAMPO_CDDESCONTO);
		}
		return addedColumns;
	}

	@Override
	protected void afterInsert(Table table) throws SQLException {
		if (usaUsuarioZero(table)) {
			int c = driver.executeUpdate("delete from " + table.name + " where fltipoalteracao = 'E' or cdusuario = '0'");
			if (c > 0) {
				LogSync.sucess(table.name.substring(5).concat(" - ").concat(String.valueOf(c)).concat(" registro(s) excluído(s)"));
			}
		}
		super.afterInsert(table);
	}

	@Override
	protected boolean deleteDataBeforeInsert(Table table) {
		return !usaUsuarioZero(table);
	}

	private boolean usaUsuarioZero(Table table) {
		return LavenderePdaConfig.isEntidadeSyncWebAppRepZero(table.name.substring(5));
	}
	
}
