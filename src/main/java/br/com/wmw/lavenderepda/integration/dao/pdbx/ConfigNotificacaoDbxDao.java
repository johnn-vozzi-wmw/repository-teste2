package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ConfigNotificacao;
import br.com.wmw.lavenderepda.business.domain.TipoNotificacao;
import totalcross.sql.ResultSet;

import br.com.wmw.framework.business.domain.BaseDomain;
import totalcross.sql.ResultSetMetaData;
import totalcross.sql.Statement;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

import java.sql.SQLException;


public class ConfigNotificacaoDbxDao extends LavendereCrudDbxDao {

	private static ConfigNotificacaoDbxDao instance;

	public ConfigNotificacaoDbxDao() {
		super(ConfigNotificacao.TABLE_NAME);
	}

    @Override
	protected BaseDomain getBaseDomainDefault() {
		return new ConfigNotificacao();
	}

	public static ConfigNotificacaoDbxDao getInstance() {
		if (instance == null) {
			instance = new ConfigNotificacaoDbxDao();
		}
		return instance;
	}

	@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		ConfigNotificacao configNotificacao = new ConfigNotificacao();
		configNotificacao.rowKey = rs.getString("rowkey");
		configNotificacao.flSistema = rs.getString("flSistema");
		configNotificacao.cdConfigNotificacao = rs.getString("cdConfigNotificacao");
		configNotificacao.cdTipoNotificacao = rs.getString("cdTipoNotificacao");
		TipoNotificacao tipoNotificacao =  new TipoNotificacao();
		tipoNotificacao.cdTipoNotificacao = rs.getString("cdTipoNotificacao");
		tipoNotificacao.dsTipoNotificacao = rs.getString("dsTipoNotificacao");
		configNotificacao.tipoNotificacao = tipoNotificacao;
		configNotificacao.nmEntidade = rs.getString("nmEntidade");
		configNotificacao.dsMensagem = rs.getString("dsMensagem");
		configNotificacao.dsSql = rs.getString("dsSql");
		configNotificacao.nuDiasLimpeza = rs.getInt("nuDiasLimpeza");
		configNotificacao.dsConfigJson = rs.getString("dsConfigJson");
		configNotificacao.nuCarimbo = rs.getInt("nuCarimbo");
		configNotificacao.flTipoAlteracao = rs.getString("flTipoAlteracao");
		configNotificacao.cdUsuario = rs.getString("cdUsuario");
		configNotificacao.dtAlteracao = rs.getDate("dtAlteracao");
		configNotificacao.hrAlteracao = rs.getString("hrAlteracao");
		return configNotificacao;
	}

	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
		sql.append(" tb.rowkey,");		
		sql.append(" tb.FLSISTEMA,");
		sql.append(" tb.CDCONFIGNOTIFICACAO,");
		sql.append(" tb.CDTIPONOTIFICACAO,");
		sql.append(" tipo.DSTIPONOTIFICACAO,");
		sql.append(" tb.NMENTIDADE,");
		sql.append(" tb.DSMENSAGEM,");
		sql.append(" tb.DSSQL,");
		sql.append(" tb.NUDIASLIMPEZA,");
		sql.append(" tb.DSCONFIGJSON,");
		sql.append(" tb.NUCARIMBO,");
		sql.append(" tb.FLTIPOALTERACAO,");
		sql.append(" tb.CDUSUARIO,");
		sql.append(" tb.DTALTERACAO,");
		sql.append(" tb.HRALTERACAO");
	}

	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		sql.append(" JOIN ");
		sql.append(TipoNotificacao.TABLE_NAME).append(" tipo ");
		sql.append(" ON tipo.CDTIPONOTIFICACAO = tb.CDTIPONOTIFICACAO ");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) {
		sql.append(" FLSISTEMA,");
		sql.append(" CDCONFIGNOTIFICACAO,");
		sql.append(" CDTIPONOTIFICACAO,");
		sql.append(" NMENTIDADE,");
		sql.append(" DSMENSAGEM,");
		sql.append(" DSSQL,");
		sql.append(" NUDIASLIMPEZA,");
		sql.append(" DSCONFIGJSON,");
		sql.append(" NUCARIMBO,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" CDUSUARIO,");
		sql.append(" DTALTERACAO,");
		sql.append(" HRALTERACAO");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
		ConfigNotificacao configNotificacao = (ConfigNotificacao) domain;
		sql.append(Sql.getValue(configNotificacao.flSistema)).append(",");
		sql.append(Sql.getValue(configNotificacao.cdConfigNotificacao)).append(",");
		sql.append(Sql.getValue(configNotificacao.tipoNotificacao.cdTipoNotificacao)).append(",");
		sql.append(Sql.getValue(configNotificacao.cdTipoNotificacao)).append(",");
		sql.append(Sql.getValue(configNotificacao.nmEntidade)).append(",");
		sql.append(Sql.getValue(configNotificacao.dsMensagem)).append(",");
		sql.append(Sql.getValue(configNotificacao.dsSql)).append(",");
		sql.append(Sql.getValue(configNotificacao.nuDiasLimpeza)).append(",");
		sql.append(Sql.getValue(configNotificacao.dsConfigJson)).append(",");
		sql.append(Sql.getValue(configNotificacao.nuCarimbo)).append(",");
		sql.append(Sql.getValue(configNotificacao.flTipoAlteracao)).append(",");
		sql.append(Sql.getValue(configNotificacao.cdUsuario)).append(",");
		sql.append(Sql.getValue(configNotificacao.dtAlteracao)).append(",");
		sql.append(Sql.getValue(configNotificacao.hrAlteracao));
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
		ConfigNotificacao configNotificacao = (ConfigNotificacao) domain;
		sql.append(" CDTIPONOTIFICACAO = ").append(Sql.getValue(configNotificacao.cdTipoNotificacao)).append(",");
		sql.append(" FLSISTEMA = ").append(Sql.getValue(configNotificacao.flSistema)).append(",");
		sql.append(" NMENTIDADE = ").append(Sql.getValue(configNotificacao.nmEntidade)).append(",");
		sql.append(" DSMENSAGEM = ").append(Sql.getValue(configNotificacao.dsMensagem)).append(",");
		sql.append(" DSSQL = ").append(Sql.getValue(configNotificacao.dsSql)).append(",");
		sql.append(" NUDIASLIMPEZA = ").append(Sql.getValue(configNotificacao.nuDiasLimpeza)).append(",");
		sql.append(" DSCONFIGJSON = ").append(Sql.getValue(configNotificacao.dsConfigJson)).append(",");
		sql.append(" NUCARIMBO = ").append(Sql.getValue(configNotificacao.nuCarimbo)).append(",");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(configNotificacao.flTipoAlteracao)).append(",");
		sql.append(" CDUSUARIO = ").append(Sql.getValue(configNotificacao.cdUsuario)).append(",");
		sql.append(" DTALTERACAO = ").append(Sql.getValue(configNotificacao.dtAlteracao)).append(",");
		sql.append(" HRALTERACAO = ").append(Sql.getValue(configNotificacao.hrAlteracao));
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
		ConfigNotificacao configNotificacao = (ConfigNotificacao) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		if (configNotificacao != null) {
			sqlWhereClause.addAndCondition("tb.FLSISTEMA = ", configNotificacao.flSistema);
			sqlWhereClause.addAndCondition("tb.CDCONFIGNOTIFICACAO = ", configNotificacao.cdConfigNotificacao);
			sqlWhereClause.addAndCondition("tb.NMENTIDADE = ", configNotificacao.nmEntidade);
		}		
		if (configNotificacao != null && configNotificacao.tipoNotificacao != null) {
			sqlWhereClause.addAndCondition("tipo.CDTIPONOTIFICACAO = ", configNotificacao.tipoNotificacao.cdTipoNotificacao);
			sqlWhereClause.addAndCondition("tipo.DSTIPONOTIFICACAO = ", configNotificacao.tipoNotificacao.dsTipoNotificacao);
			if (ValueUtil.getBooleanValue(configNotificacao.tipoNotificacao.flFixo)) {
				sqlWhereClause.addAndCondition("tipo.FLFIXO = ", "S");
			} else {
				addWhereFixoNao(sqlWhereClause);
			}
		} else {
			addWhereFixoNao(sqlWhereClause);
		}
		if (configNotificacao != null && ValueUtil.isNotEmpty(configNotificacao.getNmEntidadeList())) {
			Vector nmEntidadeVector = configNotificacao.getNmEntidadeList();
			int size = nmEntidadeVector.size();
			String[] filterNmEntidades = new String[size];
			nmEntidadeVector.copyInto(filterNmEntidades);
			sqlWhereClause.addAndConditionIn("tb.NMENTIDADE", filterNmEntidades);
		}
		sql.append(sqlWhereClause.getSql());
	}

	private static void addWhereFixoNao(SqlWhereClause sqlWhereClause) {
		sqlWhereClause.addStartAndMultipleCondition();
		sqlWhereClause.addAndConditionIn("tipo.FLFIXO", new String[]{"", "N"});
		sqlWhereClause.addOrIsNullCondition("tipo.FLFIXO");
		sqlWhereClause.addEndMultipleCondition();
	}

	public Vector creatRows(String sql) throws SQLException {
		Vector result = new Vector(0);
		try (Statement st = getCurrentDriver().getStatement(); ResultSet rs = st.executeQuery(sql)) {
			while (rs.next()) {
				result.addElement(populateRow(rs));
			}
		}
		return result;
	}

	private Hashtable populateRow(ResultSet rs) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int qtColumns =  metaData.getColumnCount();
		Hashtable htColumns = new Hashtable(0);
		for (int i = 1; i <= qtColumns; i++) {
			String column = metaData.getColumnName(i);
			htColumns.put(column, rs.getObject(i));
		}
		return htColumns;
	}

	public Vector findColumnUsuarios(String sql) throws SQLException {
		return super.findColumn("CDUSUARIO", new StringBuffer(sql));
	}
	
}