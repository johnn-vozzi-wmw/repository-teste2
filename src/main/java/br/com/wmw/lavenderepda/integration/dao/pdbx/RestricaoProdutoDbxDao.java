package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Restricao;
import br.com.wmw.lavenderepda.business.domain.RestricaoProduto;
import totalcross.sql.ResultSet;

public class RestricaoProdutoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RestricaoProduto();
	}

	private static RestricaoProdutoDbxDao instance;

	public static RestricaoProdutoDbxDao getInstance() { return instance == null ? instance = new RestricaoProdutoDbxDao() : instance; }
	public RestricaoProdutoDbxDao() { super(RestricaoProduto.TABLE_NAME); }

	@Override protected void addInsertColumns(StringBuffer stringBuffer) throws SQLException {}
	@Override protected void addInsertValues(BaseDomain baseDomain, StringBuffer stringBuffer) throws SQLException {}
	@Override protected void addUpdateValues(BaseDomain baseDomain, StringBuffer stringBuffer) throws SQLException {}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet resultSet) throws SQLException {
		RestricaoProduto restricaoProduto = new RestricaoProduto();
		restricaoProduto.rowKey = resultSet.getString("rowkey");
		restricaoProduto.cdEmpresa = resultSet.getString("cdEmpresa");
		restricaoProduto.cdRepresentante = resultSet.getString("cdRepresentante");
		restricaoProduto.cdRestricao = resultSet.getString("cdRestricao");
		restricaoProduto.cdProduto = resultSet.getString("cdProduto");
		restricaoProduto.cdGrupoProduto1 = resultSet.getString("cdGrupoProduto1");
		restricaoProduto.cdGrupoProduto2 = resultSet.getString("cdGrupoProduto2");
		restricaoProduto.cdGrupoProduto3 = resultSet.getString("cdGrupoProduto3");
		restricaoProduto.cdFornecedor = resultSet.getString("cdFornecedor");
		restricaoProduto.qtdRestricao = resultSet.getInt("qtdRestricao");
		restricaoProduto.cdUsuario = resultSet.getString("cdUsuario");
		restricaoProduto.flTipoAlteracao = resultSet.getString("flTipoAlteracao");
		return restricaoProduto;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowkey,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" CDRESTRICAO,");
		sql.append(" CDPRODUTO,");
		sql.append(" CDGRUPOPRODUTO1,");
		sql.append(" CDGRUPOPRODUTO2,");
		sql.append(" CDGRUPOPRODUTO3,");
		sql.append(" CDFORNECEDOR,");
		sql.append(" QTDRESTRICAO,");
		sql.append(" CDUSUARIO,");
		sql.append(" FLTIPOALTERACAO");
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		RestricaoProduto restricaoProduto = (RestricaoProduto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", restricaoProduto.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", restricaoProduto.cdRepresentante);
		sqlWhereClause.addAndCondition("CDRESTRICAO = ", restricaoProduto.cdRestricao);

		addWhereClauseColunasComVariacao(sqlWhereClause, "CDPRODUTO", restricaoProduto.cdProduto, Restricao.VALOR_PADRAO);
		addWhereClauseColunasComVariacao(sqlWhereClause, "CDGRUPOPRODUTO1", restricaoProduto.cdGrupoProduto1, Restricao.VALOR_PADRAO);
		addWhereClauseColunasComVariacao(sqlWhereClause, "CDGRUPOPRODUTO2", restricaoProduto.cdGrupoProduto2, Restricao.VALOR_PADRAO);
		addWhereClauseColunasComVariacao(sqlWhereClause, "CDGRUPOPRODUTO3", restricaoProduto.cdGrupoProduto3, Restricao.VALOR_PADRAO);
		addWhereClauseColunasComVariacao(sqlWhereClause, "CDFORNECEDOR", restricaoProduto.cdFornecedor, Restricao.VALOR_PADRAO);

		sqlWhereClause.addAndCondition("QTDRESTRICAO = ", restricaoProduto.qtdRestricao);
		sql.append(sqlWhereClause.getSql());
	}

	public void addWhereClauseColunasComVariacao(SqlWhereClause sqlWhereClause, final String nomeColuna, final String valorColuna, final String valorPadrao) {
		if (ValueUtil.isNotEmpty(valorColuna)) {
			sqlWhereClause.addAndCondition("(" + nomeColuna + " = " + Sql.getValue(valorColuna) + " OR " + nomeColuna + " = " + Sql.getValue(valorPadrao) + " )");
		} else {
			sqlWhereClause.addAndCondition(nomeColuna + " = ", Sql.getValue(valorPadrao));
		}
	}
	
}
