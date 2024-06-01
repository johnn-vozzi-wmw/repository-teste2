package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.PesquisaRespAppHistCli;
import totalcross.sql.ResultSet;

public class PesquisaRespAppHistCliDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PesquisaRespAppHistCli();
	}

	private static PesquisaRespAppHistCliDbxDao instance;

	public PesquisaRespAppHistCliDbxDao() {
		super(PesquisaRespAppHistCli.TABLE_NAME); 
	}

	public static PesquisaRespAppHistCliDbxDao getInstance() {
		if (instance == null) {
			instance = new PesquisaRespAppHistCliDbxDao();
		}
		return instance;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
		sql.append(" rowkey,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDCLIENTE,");
		sql.append(" CDQUESTIONARIO,");
		sql.append(" CDPERGUNTA,");
		sql.append(" CDRESPOSTA,");
		sql.append(" DSRESPOSTA,");
		sql.append(" DSOBSERVACAO,");
		sql.append(" CDUSUARIO,");
		sql.append(" NUCARIMBO,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" DTALTERACAO,");
		sql.append(" HRALTERACAO" );
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
		PesquisaRespAppHistCli pesquisaRespAppHistCli = new PesquisaRespAppHistCli();
		pesquisaRespAppHistCli.rowKey = rs.getString("rowkey");
		pesquisaRespAppHistCli.cdEmpresa = rs.getString("cdEmpresa");
		pesquisaRespAppHistCli.cdCliente = rs.getString("cdCliente");
		pesquisaRespAppHistCli.cdQuestionario = rs.getString("cdQuestionario");
		pesquisaRespAppHistCli.cdPergunta = rs.getString("cdPergunta");
		pesquisaRespAppHistCli.cdResposta = rs.getString("cdResposta");
		pesquisaRespAppHistCli.dsResposta = rs.getString("dsResposta");
		pesquisaRespAppHistCli.dsObservacao = rs.getString("dsObservacao");
		pesquisaRespAppHistCli.cdUsuario = rs.getString("cdUsuario");
		pesquisaRespAppHistCli.nuCarimbo = rs.getInt("nuCarimbo");
		pesquisaRespAppHistCli.flTipoAlteracao = rs.getString("flTipoAlteracao");
		return pesquisaRespAppHistCli;
	}

	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}

	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) { }

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) { }

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) { }

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
		PesquisaRespAppHistCli pesquisaRespAppHistCli = (PesquisaRespAppHistCli) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", pesquisaRespAppHistCli.cdEmpresa);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", pesquisaRespAppHistCli.cdCliente);
		sqlWhereClause.addAndCondition("CDQUESTIONARIO = ", pesquisaRespAppHistCli.cdQuestionario);
		sqlWhereClause.addAndCondition("CDPERGUNTA = ", pesquisaRespAppHistCli.cdPergunta);
		sqlWhereClause.addAndCondition("CDRESPOSTA = ", pesquisaRespAppHistCli.cdResposta);
		sql.append(sqlWhereClause.getSql());
	}

}