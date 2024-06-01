package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ProdutoBloqueado;
import totalcross.sql.ResultSet;

public class ProdutoBloqueadoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProdutoBloqueado();
	}

	private static ProdutoBloqueadoDbxDao instance;

	public ProdutoBloqueadoDbxDao() {
		super(ProdutoBloqueado.TABLE_NAME);
	}

	public static ProdutoBloqueadoDbxDao getInstance() {
		if (instance == null) {
			instance = new ProdutoBloqueadoDbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet resultSet) throws SQLException {
		ProdutoBloqueado produtoBloqueado = new ProdutoBloqueado();
		produtoBloqueado.rowKey = resultSet.getString("rowkey");
		produtoBloqueado.cdEmpresa = resultSet.getString("cdEmpresa");
		produtoBloqueado.cdRepresentante = resultSet.getString("cdRepresentante");
		produtoBloqueado.cdProduto = resultSet.getString("cdProduto");
		produtoBloqueado.cdTabelaPreco = resultSet.getString("cdTabelaPreco");
		produtoBloqueado.cdUsuarioBloqueio = resultSet.getString("cdUsuarioBloqueio");
		produtoBloqueado.cdUsuario = resultSet.getString("cdUsuario");
		produtoBloqueado.flTipoAlteracao = resultSet.getString("flTipoAlteracao");
		return produtoBloqueado;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowkey,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" CDPRODUTO,");
		sql.append(" CDTABELAPRECO,");
		sql.append(" CDUSUARIOBLOQUEIO,");
		sql.append(" CDUSUARIO,");
		sql.append(" FLTIPOALTERACAO,");
	}

	@Override
	protected void addInsertColumns(StringBuffer stringBuffer) throws SQLException {

	}

	@Override
	protected void addInsertValues(BaseDomain baseDomain, StringBuffer stringBuffer) throws SQLException {

	}

	@Override
	protected void addUpdateValues(BaseDomain baseDomain, StringBuffer stringBuffer) throws SQLException {

	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		ProdutoBloqueado produtoBloqueado = (ProdutoBloqueado) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", produtoBloqueado.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", produtoBloqueado.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", produtoBloqueado.cdProduto);
		sqlWhereClause.addAndCondition("tb.CDTABELAPRECO = ", produtoBloqueado.cdTabelaPreco);
		sqlWhereClause.addAndConditionIn("tb.CDTABELAPRECO", produtoBloqueado.cdTabelaPrecoInFilter);
		sql.append(sqlWhereClause.getSql());
	}

	public int countProdutoBloqueadoForAnyTabelaPreco(ProdutoBloqueado produtoBloqueado) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(*) as qtde FROM ");
		sql.append(tableName);
		sql.append(" tb ");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", produtoBloqueado.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", produtoBloqueado.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", produtoBloqueado.cdProduto);
		sqlWhereClause.addAndCondition(" (tb.CDTABELAPRECO != '0' AND tb.CDTABELAPRECO != '' AND tb.CDTABELAPRECO IS NOT NULL)");
		sql.append(sqlWhereClause.getSql());
		return getInt(sql.toString());
	}
}
