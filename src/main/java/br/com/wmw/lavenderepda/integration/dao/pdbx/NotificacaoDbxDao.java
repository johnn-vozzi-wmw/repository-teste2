package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Notificacao;
import totalcross.sql.PreparedStatement;
import totalcross.sql.ResultSet;

import br.com.wmw.framework.business.domain.BaseDomain;
import totalcross.util.Vector;

import java.sql.SQLException;


public class NotificacaoDbxDao extends LavendereCrudDbxDao {

	private static NotificacaoDbxDao instance;

	public NotificacaoDbxDao() {
		super(Notificacao.TABLE_NAME);
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Notificacao();
	}

	public static NotificacaoDbxDao getInstance() {
		if (instance == null) {
			instance = new NotificacaoDbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		Notificacao notificacao = new Notificacao();
		notificacao.rowKey = rs.getString("rowkey");
		notificacao.cdNotificacao = rs.getString("cdNotificacao");
		notificacao.cdInstante = rs.getString("cdInstante");
		notificacao.dsTipoNotificacao = rs.getString("dsTipoNotificacao");
		notificacao.vlChave = rs.getString("vlChave");
		notificacao.dsMensagem = rs.getString("dsMensagem");
		notificacao.cdUsuarioDestino = rs.getString("cdUsuarioDestino");
		notificacao.flLido = rs.getString("flLido");
		notificacao.nuCarimbo = rs.getInt("nuCarimbo");
		notificacao.flTipoAlteracao = rs.getString("flTipoAlteracao");
		notificacao.cdUsuario = rs.getString("cdUsuario");
		notificacao.dtAlteracao = rs.getDate("dtAlteracao");
		notificacao.hrAlteracao = rs.getString("hrAlteracao");
		return notificacao;
	}

	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		return populate(domainFilter, rs);
	}

	@Override
	protected void addSelectSummaryColumns(BaseDomain domainFilter, StringBuffer sql) {
		addSelectColumns(domainFilter, sql);
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
		sql.append(" rowkey,");
		sql.append(" CDNOTIFICACAO,");
		sql.append(" CDINSTANTE,");
		sql.append(" DSTIPONOTIFICACAO,");
		sql.append(" VLCHAVE,");
		sql.append(" DSMENSAGEM,");
		sql.append(" CDUSUARIODESTINO,");
		sql.append(" FLLIDO,");
		sql.append(" NUCARIMBO,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" CDUSUARIO,");
		sql.append(" DTALTERACAO,");
		sql.append(" HRALTERACAO");
	}
	

	@Override
	protected void addInsertColumns(StringBuffer sql) {
		sql.append(" CDNOTIFICACAO,");
		sql.append(" CDINSTANTE,");
		sql.append(" DSTIPONOTIFICACAO,");
		sql.append(" VLCHAVE,");
		sql.append(" DSMENSAGEM,");
		sql.append(" CDUSUARIODESTINO,");
		sql.append(" FLLIDO,");
		sql.append(" NUCARIMBO,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" CDUSUARIO,");
		sql.append(" DTALTERACAO,");
		sql.append(" HRALTERACAO");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
		Notificacao notificacao = (Notificacao) domain;
		sql.append(Sql.getValue(notificacao.cdNotificacao)).append(",");
		sql.append(Sql.getValue(notificacao.cdInstante)).append(",");
		sql.append(Sql.getValue(notificacao.dsTipoNotificacao)).append(",");
		sql.append("?").append(",");
		sql.append(Sql.getValue(notificacao.dsMensagem)).append(",");
		sql.append(Sql.getValue(notificacao.cdUsuarioDestino)).append(",");
		sql.append(Sql.getValue(notificacao.flLido)).append(",");
		sql.append(Sql.getValue(notificacao.nuCarimbo)).append(",");
		sql.append(Sql.getValue(notificacao.flTipoAlteracao)).append(",");
		sql.append(Sql.getValue(notificacao.cdUsuario)).append(",");
		sql.append(Sql.getValue(notificacao.dtAlteracao)).append(",");
		sql.append(Sql.getValue(notificacao.hrAlteracao));
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
		Notificacao notificacao = (Notificacao) domain;
		sql.append(" DSTIPONOTIFICACAO = ").append(Sql.getValue(notificacao.dsTipoNotificacao)).append(",");
		sql.append(" VLCHAVE = ").append("?").append(",");
		sql.append(" DSMENSAGEM = ").append(Sql.getValue(notificacao.dsMensagem)).append(",");
		sql.append(" CDUSUARIODESTINO = ").append(Sql.getValue(notificacao.cdUsuarioDestino)).append(",");
		sql.append(" FLLIDO = ").append(Sql.getValue(notificacao.flLido)).append(",");
		sql.append(" NUCARIMBO = ").append(Sql.getValue(notificacao.nuCarimbo)).append(",");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(notificacao.flTipoAlteracao)).append(",");
		sql.append(" CDUSUARIO = ").append(Sql.getValue(notificacao.cdUsuario)).append(",");
		sql.append(" DTALTERACAO = ").append(Sql.getValue(notificacao.dtAlteracao)).append(",");
		sql.append(" HRALTERACAO = ").append(Sql.getValue(notificacao.hrAlteracao));
	}
	
	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
		Notificacao notificacao = (Notificacao) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDNOTIFICACAO = ", notificacao.cdNotificacao);
		sqlWhereClause.addAndCondition("CDUSUARIODESTINO = ", notificacao.cdUsuarioDestino);
		sqlWhereClause.addAndCondition("FLLIDO = ", notificacao.flLido);
		sqlWhereClause.addAndCondition("VLCHAVE like ", notificacao.vlChave);
		sqlWhereClause.addAndCondition("DSTIPONOTIFICACAO = ", notificacao.dsTipoNotificacao);
		sqlWhereClause.addAndCondition("DTALTERACAO = ", notificacao.dtAlteracao);
		sqlWhereClause.addAndCondition("DTALTERACAO < ", notificacao.dtLimpeza);

		//--
		sql.append(sqlWhereClause.getSql());
	}

	public int insertOrIgnore(Vector list, boolean controleChave) throws SQLException {
		if (ValueUtil.isEmpty(list)) {
			return 0;
		}
		StringBuffer sql = getSqlBuffer();
		sql.append("INSERT ");
		sql.append(" OR IGNORE ");
		sql.append("INTO ");
		sql.append(tableName);
		sql.append(" (");
		addInsertColumns(sql);
		sql.append(",rowkey) ");
		if (controleChave) {
			createValuesExistsPers(sql, list);
		} else {
			createValues(sql, list);
		}
		try (PreparedStatement insert = getCurrentDriver().prepareStatement(sql.toString())) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				Notificacao notificacao = (Notificacao) list.elementAt(i);
				insert.setString(i + 1, notificacao.vlChave);
			}
			return insert.executeUpdate();
		}		
	}

	private void createValues(StringBuffer sql, Vector list) {
		sql.append("values ");
		String vir = ",";
		int size = list.size();
		for (int i = 0; i < size; i++) {
			BaseDomain domain = (BaseDomain) list.elementAt(i);
			sql.append("(");
			BaseDomain.setDadosAlteracao(domain);
			beforeInsert(domain);
			addInsertValues(domain, sql);
			sql.append(",'");
			sql.append(domain.rowKey);
			if (i == size - 1) {
				vir = "";
			}
			sql.append("')").append(vir);
		}
	}

	private void createValuesExistsPers(StringBuffer sql, Vector list) {
		String union = "union";
		int size = list.size();
		for (int i = 0; i < size; i++) {
			BaseDomain domain = (BaseDomain) list.elementAt(i);
			BaseDomain.setDadosAlteracao(domain);
			beforeInsert(domain);
			sql.append(" select ");
			addInsertValues(domain, sql);
			sql.append(",'");
			sql.append(domain.rowKey);
			sql.append("' ");
			addSqlExists(sql, domain);
			if (i == size - 1) {
				union = "";
			}
			sql.append(union);
		}
	}

	private void addSqlExists(StringBuffer sql, BaseDomain domain) {
		Notificacao notificacao = (Notificacao) domain;
		sql.append(" where not exists(select 1 from ")
				.append(tableName)
				.append(" where cdnotificacao = ")
				.append(Sql.getValue(notificacao.cdNotificacao))
				.append(")");
	}

	public int marcarComoLido(Notificacao domain) throws SQLException {
		beforeUpdate(domain);
		StringBuffer sql = getSqlBuffer();
		sql.append("update ");
		sql.append(tableName);
		sql.append(" set ");
		sql.append("fllido = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		addWhereMarcarComoLido(sql, domain);
		return updateAll(sql.toString());		
	}

	private void addWhereMarcarComoLido(StringBuffer sql, Notificacao domain) {
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("DSTIPONOTIFICACAO = ", domain.dsTipoNotificacao);
		sqlWhereClause.addAndCondition("FLLIDO = ", ValueUtil.VALOR_NAO);
		sqlWhereClause.addAndCondition("ROWKEY = ", domain.rowKey);
		sql.append(sqlWhereClause.getSql());
	}
}