package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.PesquisaApp;
import br.com.wmw.lavenderepda.business.domain.PesquisaRespApp;
import br.com.wmw.lavenderepda.business.domain.Questionario;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class PesquisaAppPdbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PesquisaApp();
	}

	private static PesquisaAppPdbxDao instance;

	public PesquisaAppPdbxDao() {
		super(PesquisaApp.TABLE_NAME);
	}

	public static PesquisaAppPdbxDao getInstance() {
		if (instance == null) {
			instance = new PesquisaAppPdbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		PesquisaApp pesquisaApp = new PesquisaApp();
		pesquisaApp.rowKey = rs.getString(1);
		pesquisaApp.cdEmpresa = rs.getString(2);
		pesquisaApp.cdRepresentante = rs.getString(3);
		pesquisaApp.cdPesquisaApp = rs.getString(4);
		pesquisaApp.cdQuestionario = rs.getString(5);
		pesquisaApp.cdCliente = rs.getString(6);
		pesquisaApp.dtInicioPesquisa = rs.getDate(7);
		pesquisaApp.hrInicioPesquisa = rs.getString(8);
		pesquisaApp.dtFimPesquisa = rs.getDate(9);
		pesquisaApp.hrFimPesquisa = rs.getString(10);
		pesquisaApp.flPesquisaNovoCliente = rs.getString(11);
		pesquisaApp.cdVisita = rs.getString(12);
		return pesquisaApp;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" TB.ROWKEY,");
		sql.append(" TB.CDEMPRESA,");
		sql.append(" TB.CDREPRESENTANTE,");
		sql.append(" TB.CDPESQUISAAPP,");
		sql.append(" TB.CDQUESTIONARIO,");
		sql.append(" TB.CDCLIENTE,");
		sql.append(" TB.DTINICIOPESQUISA,");
		sql.append(" TB.HRINICIOPESQUISA,");
		sql.append(" TB.DTFIMPESQUISA,");
		sql.append(" TB.HRFIMPESQUISA,");
		sql.append(" TB.FLPESQUISANOVOCLIENTE,");
		sql.append(" TB.CDVISITA");
	}

	@Override
	protected void addUpdateValues(BaseDomain baseDomain, StringBuffer sql) throws SQLException {
		PesquisaApp pesquisaApp = (PesquisaApp) baseDomain;
		sql.append(" DTFIMPESQUISA = ").append(Sql.getValue(pesquisaApp.dtFimPesquisa)).append(",");
		sql.append(" HRFIMPESQUISA = ").append(Sql.getValue(pesquisaApp.hrFimPesquisa)).append(",");
		sql.append(" nuCarimbo = ").append(Sql.getValue(pesquisaApp.nuCarimbo)).append(",");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(pesquisaApp.flTipoAlteracao)).append(",");
		sql.append(" FLPESQUISANOVOCLIENTE = ").append(Sql.getValue(pesquisaApp.flPesquisaNovoCliente)).append(",");
		sql.append(" CDCLIENTE = ").append(Sql.getValue(pesquisaApp.cdCliente)).append(",");
		sql.append(" cdUsuario = ").append(Sql.getValue(pesquisaApp.cdUsuario));
	}

	@Override
	protected void addWhereByExample(BaseDomain baseDomain, StringBuffer sql) throws SQLException {
		PesquisaApp pesquisaApp = (PesquisaApp) baseDomain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals("TB.CDEMPRESA", pesquisaApp.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("TB.CDREPRESENTANTE", pesquisaApp.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("TB.CDPESQUISAAPP", pesquisaApp.cdPesquisaApp);
		sqlWhereClause.addAndConditionEquals("TB.CDQUESTIONARIO", pesquisaApp.cdQuestionario);
		sqlWhereClause.addAndConditionEquals("TB.CDCLIENTE", pesquisaApp.cdCliente);
		sqlWhereClause.addAndConditionEquals("TB.DTINICIOPESQUISA", pesquisaApp.dtInicioPesquisa);
		sqlWhereClause.addAndConditionEquals("TB.DTFIMPESQUISA", pesquisaApp.dtFimPesquisa);
		sqlWhereClause.addAndConditionEquals("TB.CDVISITA", pesquisaApp.cdVisita);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected String getInsertValue(String columnName, int columnType, int columnSize, BaseDomain domain) {
		PesquisaApp pesquisaApp = (PesquisaApp) domain;
		if ("ROWKEY".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pesquisaApp.getRowKey());
		}
		if ("CDEMPRESA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pesquisaApp.cdEmpresa);
		}
		if ("CDREPRESENTANTE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pesquisaApp.cdRepresentante);
		}
		if ("CDPESQUISAAPP".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pesquisaApp.cdPesquisaApp);
		}
		if ("CDQUESTIONARIO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pesquisaApp.cdQuestionario);
		}
		if ("CDCLIENTE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pesquisaApp.cdCliente);
		}
		if ("DTINICIOPESQUISA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pesquisaApp.dtInicioPesquisa);
		}
		if ("HRINICIOPESQUISA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pesquisaApp.hrInicioPesquisa);
		}
		if ("DTFIMPESQUISA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pesquisaApp.dtFimPesquisa);
		}
		if ("HRFIMPESQUISA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pesquisaApp.hrFimPesquisa);
		}
		if ("FLPESQUISANOVOCLIENTE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pesquisaApp.flPesquisaNovoCliente);
		}
		if ("CDVISITA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pesquisaApp.cdVisita);
		}
		if ("NUCARIMBO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT);
		}
		if ("FLTIPOALTERACAO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(domain.flTipoAlteracao);
		}
		if ("CDUSUARIO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(domain.cdUsuario);
		}
		return super.getInsertValue(columnName, columnType, columnSize, domain);
	}

	public PesquisaApp findLast(BaseDomain baseDomain) throws SQLException {
		baseDomain.sortAtributte = "CDPESQUISAAPP";
		baseDomain.sortAsc = "N";
		StringBuffer sql = getSqlBuffer();
		sql.append(" select ");
		addSelectColumns(baseDomain, sql);
		sql.append(" from ");
		sql.append(tableName);
		sql.append(" tb ");
		addWhereByExample(baseDomain, sql);
		sql.append(" order by cast(tb.CDPESQUISAAPP as unsigned) ");
		sql.append(ValueUtil.VALOR_SIM.equals(baseDomain.sortAsc) ? " ASC" : " DESC");
		Vector result = findAll(baseDomain, sql.toString());
		return ValueUtil.isEmpty(result) ? null : (PesquisaApp) result.items[0];
	}

	@Override
	protected void beforeInsert(BaseDomain domain) {
		PesquisaApp novaPesquisa = (PesquisaApp) domain;
		novaPesquisa.dtInicioPesquisa = DateUtil.getCurrentDate();
		novaPesquisa.hrInicioPesquisa = TimeUtil.getCurrentTimeHHMMSS();
		super.beforeInsert(novaPesquisa);
	}

	public static String getSqlDadosEnvioServidor() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM TBLVPPESQUISAAPP tb ");
		sb.append(" WHERE tb.").append(PesquisaRespApp.NMCAMPOTIPOALTERACAO);
		sb.append(" <> ").append(Sql.getValue(PesquisaRespApp.FLTIPOALTERACAO_ORIGINAL));
		sb.append(" AND tb.DTFIMPESQUISA is not null");
		return sb.toString();
	}

	@Override
	protected void addWhereDeleteByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		PesquisaApp pesquisaApp = (PesquisaApp) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals("CDEMPRESA", pesquisaApp.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("CDREPRESENTANTE", pesquisaApp.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("CDPESQUISAAPP", pesquisaApp.cdPesquisaApp);
		sqlWhereClause.addAndConditionEquals("CDQUESTIONARIO", pesquisaApp.cdQuestionario);
		sqlWhereClause.addAndConditionEquals("CDCLIENTE", pesquisaApp.cdCliente);
		sqlWhereClause.addAndConditionEquals("DTINICIOPESQUISA", pesquisaApp.dtInicioPesquisa);
		sqlWhereClause.addAndConditionEquals("DTFIMPESQUISA", pesquisaApp.dtFimPesquisa);
		sqlWhereClause.addAndConditionEquals("CDVISITA", pesquisaApp.cdVisita);
		sql.append(sqlWhereClause.getSql());
	}

	public PesquisaApp findDataFimPesquisaByPesquisaApp(PesquisaApp pesquisaApp, Questionario questionario) throws SQLException {
		StringBuffer sql = getSqlBuffer();
			sql.append(" SELECT tb.CDEMPRESA, tb.CDREPRESENTANTE, tb.CDCLIENTE, qt.CDQUESTIONARIO, tb.DTFIMPESQUISA");
			sql.append(" FROM ").append(tableName).append(" tb ");
			sql.append(" JOIN TBLVPQUESTIONARIO qt ")
					.append("on tb.CDQUESTIONARIO = qt.CDQUESTIONARIO AND ")
					.append(" tb.CDEMPRESA = qt.CDEMPRESA ");
			sql.append(" WHERE tb.CDCLIENTE = ").append(Sql.getValue(pesquisaApp.cdCliente));
			sql.append(" AND tb.CDEMPRESA = ").append(Sql.getValue(pesquisaApp.cdEmpresa));
			sql.append(" AND tb.CDREPRESENTANTE = ").append(Sql.getValue(pesquisaApp.cdRepresentante));
			sql.append(" AND tb.CDQUESTIONARIO = ").append(questionario.cdQuestionario);
			if (ValueUtil.isNotEmpty(pesquisaApp.cdVisita)) {
				sql.append(" AND tb.CDVISITA = ").append(pesquisaApp.cdVisita);
			}
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {
				pesquisaApp.cdEmpresa = rs.getString("cdempresa");
				pesquisaApp.cdRepresentante = rs.getString("cdrepresentante");
				pesquisaApp.cdQuestionario = rs.getString("cdquestionario");
				pesquisaApp.cdCliente = rs.getString("cdcliente");
				pesquisaApp.dtFimPesquisa = rs.getDate("dtfimpesquisa");
			}
			return pesquisaApp;
		} catch (Throwable e) {
			return new PesquisaApp();
		}
	}
	
}
