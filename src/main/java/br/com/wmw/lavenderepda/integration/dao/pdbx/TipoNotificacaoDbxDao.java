package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.TipoNotificacao;
import totalcross.sql.ResultSet;

import br.com.wmw.framework.business.domain.BaseDomain;

import java.sql.SQLException;

public class TipoNotificacaoDbxDao extends LavendereCrudDbxDao {

	private static TipoNotificacaoDbxDao instance;

	public TipoNotificacaoDbxDao() {
		super(TipoNotificacao.TABLE_NAME);
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TipoNotificacao();
	}

	public static TipoNotificacaoDbxDao getInstance() {
		if (instance == null) {
			instance = new TipoNotificacaoDbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		TipoNotificacao tipoNotificacao = new TipoNotificacao();
		tipoNotificacao.rowKey = rs.getString("rowkey");
		tipoNotificacao.cdTipoNotificacao = rs.getString("cdTipoNotificacao");
		tipoNotificacao.dsTipoNotificacao = rs.getString("dsTipoNotificacao");
		tipoNotificacao.flFixo = rs.getString("flFixo");
		tipoNotificacao.nuCarimbo = rs.getInt("nuCarimbo");
		tipoNotificacao.flTipoAlteracao = rs.getString("flTipoAlteracao");
		tipoNotificacao.cdUsuario = rs.getString("cdUsuario");
		tipoNotificacao.dtAlteracao = rs.getDate("dtAlteracao");
		tipoNotificacao.hrAlteracao = rs.getString("hrAlteracao");
		return tipoNotificacao;
	}

	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
		sql.append(" rowkey,");
		sql.append(" CDTIPONOTIFICACAO,");
		sql.append(" DSTIPONOTIFICACAO,");
		sql.append(" FLFIXO,");
		sql.append(" NUCARIMBO,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" CDUSUARIO,");
		sql.append(" DTALTERACAO,");
		sql.append(" HRALTERACAO");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) {
		sql.append(" CDTIPONOTIFICACAO,");
		sql.append(" DSTIPONOTIFICACAO,");
		sql.append(" FLFIXO,");
		sql.append(" NUCARIMBO,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" CDUSUARIO,");
		sql.append(" DTALTERACAO,");
		sql.append(" HRALTERACAO");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
		TipoNotificacao tipoNotificacao = (TipoNotificacao) domain;
		sql.append(Sql.getValue(tipoNotificacao.cdTipoNotificacao)).append(",");
		sql.append(Sql.getValue(tipoNotificacao.dsTipoNotificacao)).append(",");
		sql.append(Sql.getValue(tipoNotificacao.flFixo)).append(",");
		sql.append(Sql.getValue(tipoNotificacao.nuCarimbo)).append(",");
		sql.append(Sql.getValue(tipoNotificacao.flTipoAlteracao)).append(",");
		sql.append(Sql.getValue(tipoNotificacao.cdUsuario)).append(",");
		sql.append(Sql.getValue(tipoNotificacao.dtAlteracao)).append(",");
		sql.append(Sql.getValue(tipoNotificacao.hrAlteracao));
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
		TipoNotificacao tipoNotificacao = (TipoNotificacao) domain;
		sql.append(" DSTIPONOTIFICACAO = ").append(Sql.getValue(tipoNotificacao.dsTipoNotificacao)).append(",");
		sql.append(" FLFIXO = ").append(Sql.getValue(tipoNotificacao.flFixo)).append(",");
		sql.append(" NUCARIMBO = ").append(Sql.getValue(tipoNotificacao.nuCarimbo)).append(",");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(tipoNotificacao.flTipoAlteracao)).append(",");
		sql.append(" CDUSUARIO = ").append(Sql.getValue(tipoNotificacao.cdUsuario)).append(",");
		sql.append(" DTALTERACAO = ").append(Sql.getValue(tipoNotificacao.dtAlteracao)).append(",");
		sql.append(" HRALTERACAO = ").append(Sql.getValue(tipoNotificacao.hrAlteracao));
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
		TipoNotificacao tipoNotificacao = (TipoNotificacao) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDTIPONOTIFICACAO = ", tipoNotificacao.cdTipoNotificacao);
		sqlWhereClause.addAndCondition("DSTIPONOTIFICACAO = ", tipoNotificacao.dsTipoNotificacao);
		//--
		sql.append(sqlWhereClause.getSql());
	}

}