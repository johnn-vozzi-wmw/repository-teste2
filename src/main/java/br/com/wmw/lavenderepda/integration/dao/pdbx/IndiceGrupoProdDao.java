package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.IndiceGrupoProd;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class IndiceGrupoProdDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new IndiceGrupoProd();
	}

	private static IndiceGrupoProdDao instance;
	
	public IndiceGrupoProdDao() {
		super(IndiceGrupoProd.TABLE_NAME);
	}
	
	public static IndiceGrupoProdDao getInstance() {
		if (instance == null) {
			instance = new IndiceGrupoProdDao();
		}
		return instance;
	}

	@Override
	protected void addInsertColumns(StringBuffer arg0) throws SQLException {
	}

	@Override
	protected void addInsertValues(BaseDomain arg0, StringBuffer arg1) throws SQLException {
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append("rowKey, CDEMPRESA, CDREPRESENTANTE, CDGRUPOPRODUTO1, CDGRUPOPRODUTO2, CDGRUPOPRODUTO3, CDTABELAPRECO, CDCONDICAOPAGAMENTO, VLINDICEFINANCEIRO");
	}

	@Override
	protected void addUpdateValues(BaseDomain arg0, StringBuffer arg1) throws SQLException {
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		IndiceGrupoProd indiceGrupoProd = (IndiceGrupoProd) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", indiceGrupoProd.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", indiceGrupoProd.cdRepresentante);
		sqlWhereClause.addAndConditionOr("CDGRUPOPRODUTO1 = ", new String[]{indiceGrupoProd.cdGrupoProduto1, "0"});
		sqlWhereClause.addAndConditionOr("CDGRUPOPRODUTO2 = ", new String[]{indiceGrupoProd.cdGrupoProduto2, "0"});
		sqlWhereClause.addAndConditionOr("CDGRUPOPRODUTO3 = ", new String[]{indiceGrupoProd.cdGrupoProduto3, "0"});
		sqlWhereClause.addAndConditionOr("CDTABELAPRECO = ", new String[]{indiceGrupoProd.cdTabelaPreco, "0"});
		sqlWhereClause.addAndConditionOr("CDCONDICAOPAGAMENTO = ", new String[]{indiceGrupoProd.cdCondicaoPagamento, "0"});
		sql.append(sqlWhereClause.toString());
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		IndiceGrupoProd indiceGrupoProd = new IndiceGrupoProd();
		indiceGrupoProd.cdEmpresa = rs.getString("cdEmpresa");
		indiceGrupoProd.cdRepresentante = rs.getString("cdRepresentante");
		indiceGrupoProd.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
		indiceGrupoProd.cdGrupoProduto2 = rs.getString("cdGrupoProduto2");
		indiceGrupoProd.cdGrupoProduto3 = rs.getString("cdGrupoProduto3");
		indiceGrupoProd.cdTabelaPreco = rs.getString("cdTabelaPreco");
		indiceGrupoProd.cdCondicaoPagamento = rs.getString("cdCondicaoPagamento");
		indiceGrupoProd.vlIndiceFinanceiro = rs.getDouble("vlIndiceFinanceiro");
		return indiceGrupoProd;
	}
	
	public IndiceGrupoProd findIndiceFinanceiroMaiorPrioridade(IndiceGrupoProd filter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT CDEMPRESA, CDREPRESENTANTE, CDGRUPOPRODUTO1, CDGRUPOPRODUTO2, CDGRUPOPRODUTO3, CDTABELAPRECO, CDCONDICAOPAGAMENTO, VLINDICEFINANCEIRO FROM (")
		.append("SELECT 1 AS PRIORIDADE, ");
		addSelectColumns(filter, sql);
		sql.append(" FROM " + tableName + " WHERE CDEMPRESA = " + Sql.getValue(filter.cdEmpresa) + " AND CDREPRESENTANTE = " + Sql.getValue(filter.cdRepresentante) +
				" AND CDGRUPOPRODUTO1 = " + Sql.getValue(filter.cdGrupoProduto1) + " AND CDGRUPOPRODUTO2 = " + Sql.getValue(filter.cdGrupoProduto2) + 
				" AND CDGRUPOPRODUTO3 = " + Sql.getValue(filter.cdGrupoProduto3) + " AND CDGRUPOPRODUTO4 = " + Sql.getValue(filter.cdGrupoProduto4) +  
				" AND CDGRUPOPRODUTO5 = " + Sql.getValue(filter.cdGrupoProduto5) + " AND CDTABELAPRECO = " + Sql.getValue(filter.cdTabelaPreco) + 
				" AND CDCONDICAOPAGAMENTO = " + Sql.getValue(filter.cdCondicaoPagamento) + " UNION ALL ");
		sql.append("SELECT 2 AS PRIORIDADE, ");
		addSelectColumns(filter, sql);
		sql.append(" FROM " + tableName + " WHERE CDEMPRESA = " + Sql.getValue(filter.cdEmpresa) + " AND CDREPRESENTANTE = " + Sql.getValue(filter.cdRepresentante) +
				" AND CDGRUPOPRODUTO1 = " + Sql.getValue(filter.cdGrupoProduto1) + " AND CDGRUPOPRODUTO2 = " + Sql.getValue(filter.cdGrupoProduto2) + 
				" AND CDGRUPOPRODUTO3 = " + Sql.getValue(filter.cdGrupoProduto3) + " AND CDGRUPOPRODUTO4 = " + Sql.getValue(filter.cdGrupoProduto4) +
				" AND CDGRUPOPRODUTO5 = " + Sql.getValue(filter.cdGrupoProduto5) + " AND CDTABELAPRECO = " + Sql.getValue(ValueUtil.VALOR_ZERO) + 
				" AND CDCONDICAOPAGAMENTO = " + Sql.getValue(filter.cdCondicaoPagamento) + " UNION ALL ");
		sql.append("SELECT 3 AS PRIORIDADE, ");
		addSelectColumns(filter, sql);
		sql.append(" FROM " + tableName + " WHERE CDEMPRESA = " + Sql.getValue(filter.cdEmpresa) + " AND CDREPRESENTANTE = " + Sql.getValue(filter.cdRepresentante) +
				" AND CDGRUPOPRODUTO1 = " + Sql.getValue(filter.cdGrupoProduto1) + " AND CDGRUPOPRODUTO2 = " + Sql.getValue(filter.cdGrupoProduto2) + 
				" AND CDGRUPOPRODUTO3 = " + Sql.getValue(filter.cdGrupoProduto3) + " AND CDGRUPOPRODUTO4 = " + Sql.getValue(filter.cdGrupoProduto4) +
				" AND CDGRUPOPRODUTO5 = " + Sql.getValue(filter.cdGrupoProduto5) + " AND CDTABELAPRECO = " + Sql.getValue(filter.cdTabelaPreco) + 
				" AND CDCONDICAOPAGAMENTO = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " UNION ALL ");
		sql.append("SELECT 4 AS PRIORIDADE, ");
		addSelectColumns(filter, sql);
		sql.append(" FROM " + tableName + " WHERE CDEMPRESA = " + Sql.getValue(filter.cdEmpresa) + " AND CDREPRESENTANTE = " + Sql.getValue(filter.cdRepresentante) +
				" AND CDGRUPOPRODUTO1 = " + Sql.getValue(filter.cdGrupoProduto1) + " AND CDGRUPOPRODUTO2 = " + Sql.getValue(filter.cdGrupoProduto2) + 
				" AND CDGRUPOPRODUTO3 = " + Sql.getValue(filter.cdGrupoProduto3) + " AND CDGRUPOPRODUTO4 = " + Sql.getValue(filter.cdGrupoProduto4) + 
				" AND CDGRUPOPRODUTO5 = " + Sql.getValue(filter.cdGrupoProduto5) + " AND CDTABELAPRECO = " + Sql.getValue(ValueUtil.VALOR_ZERO) 
				+ " AND CDCONDICAOPAGAMENTO = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " UNION ALL ");
		sql.append("SELECT 5 AS PRIORIDADE, ");
		addSelectColumns(filter, sql);
		sql.append(" FROM " + tableName + " WHERE CDEMPRESA = " + Sql.getValue(filter.cdEmpresa) + " AND CDREPRESENTANTE = " + Sql.getValue(filter.cdRepresentante) +
				" AND CDGRUPOPRODUTO1 = " + Sql.getValue(filter.cdGrupoProduto1) + " AND CDGRUPOPRODUTO2 = " + Sql.getValue(filter.cdGrupoProduto2) + 
				" AND CDGRUPOPRODUTO3 = " + Sql.getValue(filter.cdGrupoProduto3) + " AND CDGRUPOPRODUTO4 = " + Sql.getValue(filter.cdGrupoProduto4) + 
				" AND CDGRUPOPRODUTO5 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDTABELAPRECO = " + Sql.getValue(filter.cdTabelaPreco) + 
				" AND CDCONDICAOPAGAMENTO = " + Sql.getValue(filter.cdCondicaoPagamento) + " UNION ALL ");
		sql.append("SELECT 6 AS PRIORIDADE, ");
		addSelectColumns(filter, sql);
		sql.append(" FROM " + tableName + " WHERE CDEMPRESA = " + Sql.getValue(filter.cdEmpresa) + " AND CDREPRESENTANTE = " + Sql.getValue(filter.cdRepresentante) +
				" AND CDGRUPOPRODUTO1 = " + Sql.getValue(filter.cdGrupoProduto1) + " AND CDGRUPOPRODUTO2 = " + Sql.getValue(filter.cdGrupoProduto2) + 
				" AND CDGRUPOPRODUTO3 = " + Sql.getValue(filter.cdGrupoProduto3) + " AND CDGRUPOPRODUTO4 = " + Sql.getValue(filter.cdGrupoProduto4) +
				" AND CDGRUPOPRODUTO5 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDTABELAPRECO = " + Sql.getValue(ValueUtil.VALOR_ZERO) + 
				" AND CDCONDICAOPAGAMENTO = " + Sql.getValue(filter.cdCondicaoPagamento) + " UNION ALL ");
		sql.append("SELECT 7 AS PRIORIDADE, ");
		addSelectColumns(filter, sql);
		sql.append(" FROM " + tableName + " WHERE CDEMPRESA = " + Sql.getValue(filter.cdEmpresa) + " AND CDREPRESENTANTE = " + Sql.getValue(filter.cdRepresentante) +
				" AND CDGRUPOPRODUTO1 = " + Sql.getValue(filter.cdGrupoProduto1) + " AND CDGRUPOPRODUTO2 = " + Sql.getValue(filter.cdGrupoProduto2) + 
				" AND CDGRUPOPRODUTO3 = " + Sql.getValue(filter.cdGrupoProduto3) + " AND CDGRUPOPRODUTO4 = " + Sql.getValue(filter.cdGrupoProduto4) + 
				" AND CDGRUPOPRODUTO5 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDTABELAPRECO = " + Sql.getValue(filter.cdTabelaPreco) + 
				" AND CDCONDICAOPAGAMENTO = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " UNION ALL ");
		sql.append("SELECT 8 AS PRIORIDADE, ");
		addSelectColumns(filter, sql);
		sql.append(" FROM " + tableName + " WHERE CDEMPRESA = " + Sql.getValue(filter.cdEmpresa) + " AND CDREPRESENTANTE = " + Sql.getValue(filter.cdRepresentante) +
				" AND CDGRUPOPRODUTO1 = " + Sql.getValue(filter.cdGrupoProduto1) + " AND CDGRUPOPRODUTO2 = " + Sql.getValue(filter.cdGrupoProduto2) + 
				" AND CDGRUPOPRODUTO3 = " + Sql.getValue(filter.cdGrupoProduto3) + " AND CDGRUPOPRODUTO4 = " + Sql.getValue(filter.cdGrupoProduto4) + 
				" AND CDGRUPOPRODUTO5 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDTABELAPRECO = " + Sql.getValue(ValueUtil.VALOR_ZERO) +
				" AND CDCONDICAOPAGAMENTO = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " UNION ALL ");
		sql.append("SELECT 9 AS PRIORIDADE, ");
		addSelectColumns(filter, sql);
		sql.append(" FROM " + tableName + " WHERE CDEMPRESA = " + Sql.getValue(filter.cdEmpresa) + " AND CDREPRESENTANTE = " + Sql.getValue(filter.cdRepresentante) +
				" AND CDGRUPOPRODUTO1 = " + Sql.getValue(filter.cdGrupoProduto1) + " AND CDGRUPOPRODUTO2 = " + Sql.getValue(filter.cdGrupoProduto2) + 
				" AND CDGRUPOPRODUTO3 = " + Sql.getValue(filter.cdGrupoProduto3) + " AND CDGRUPOPRODUTO4 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + 
				" AND CDGRUPOPRODUTO5 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDTABELAPRECO = " + Sql.getValue(filter.cdTabelaPreco) +
				" AND CDCONDICAOPAGAMENTO = " + Sql.getValue(filter.cdCondicaoPagamento) + " UNION ALL ");
		sql.append("SELECT 10 AS PRIORIDADE, ");
		addSelectColumns(filter, sql);
		sql.append(" FROM " + tableName + " WHERE CDEMPRESA = " + Sql.getValue(filter.cdEmpresa) + " AND CDREPRESENTANTE = " + Sql.getValue(filter.cdRepresentante) +
				" AND CDGRUPOPRODUTO1 = " + Sql.getValue(filter.cdGrupoProduto1) + " AND CDGRUPOPRODUTO2 = " + Sql.getValue(filter.cdGrupoProduto2) + 
				" AND CDGRUPOPRODUTO3 = " + Sql.getValue(filter.cdGrupoProduto3) + " AND CDGRUPOPRODUTO4 = " + Sql.getValue(ValueUtil.VALOR_ZERO) +
				" AND CDGRUPOPRODUTO5 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDTABELAPRECO = " + Sql.getValue(ValueUtil.VALOR_ZERO) +
				" AND CDCONDICAOPAGAMENTO = " + Sql.getValue(filter.cdCondicaoPagamento) + " UNION ALL ");
		sql.append("SELECT 11 AS PRIORIDADE, ");
		addSelectColumns(filter, sql);
		sql.append(" FROM " + tableName + " WHERE CDEMPRESA = " + Sql.getValue(filter.cdEmpresa) + " AND CDREPRESENTANTE = " + Sql.getValue(filter.cdRepresentante) +
				" AND CDGRUPOPRODUTO1 = " + Sql.getValue(filter.cdGrupoProduto1) + " AND CDGRUPOPRODUTO2 = " + Sql.getValue(filter.cdGrupoProduto2) + 
				" AND CDGRUPOPRODUTO3 = " + Sql.getValue(filter.cdGrupoProduto3) + " AND CDGRUPOPRODUTO4 = " + Sql.getValue(ValueUtil.VALOR_ZERO) +
				" AND CDGRUPOPRODUTO5 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDTABELAPRECO = " + Sql.getValue(filter.cdTabelaPreco) +
				" AND CDCONDICAOPAGAMENTO = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " UNION ALL ");
		sql.append("SELECT 12 AS PRIORIDADE, ");
		addSelectColumns(filter, sql);
		sql.append(" FROM " + tableName + " WHERE CDEMPRESA = " + Sql.getValue(filter.cdEmpresa) + " AND CDREPRESENTANTE = " + Sql.getValue(filter.cdRepresentante) +
				" AND CDGRUPOPRODUTO1 = " + Sql.getValue(filter.cdGrupoProduto1) + " AND CDGRUPOPRODUTO2 = " + Sql.getValue(filter.cdGrupoProduto2) + 
				" AND CDGRUPOPRODUTO3 = " + Sql.getValue(filter.cdGrupoProduto3) + " AND CDGRUPOPRODUTO4 = " + Sql.getValue(ValueUtil.VALOR_ZERO) +
				" AND CDGRUPOPRODUTO5 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDTABELAPRECO = " + Sql.getValue(ValueUtil.VALOR_ZERO) +
				" AND CDCONDICAOPAGAMENTO = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " UNION ALL ");
		sql.append("SELECT 13 AS PRIORIDADE, ");
		addSelectColumns(filter, sql);
		sql.append(" FROM " + tableName + " WHERE CDEMPRESA = " + Sql.getValue(filter.cdEmpresa) + " AND CDREPRESENTANTE = " + Sql.getValue(filter.cdRepresentante) +
				" AND CDGRUPOPRODUTO1 = " + Sql.getValue(filter.cdGrupoProduto1) + " AND CDGRUPOPRODUTO2 = " + Sql.getValue(filter.cdGrupoProduto2) + 
				" AND CDGRUPOPRODUTO3 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDGRUPOPRODUTO4 = " + Sql.getValue(ValueUtil.VALOR_ZERO) +
				" AND CDGRUPOPRODUTO5 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDTABELAPRECO = " + Sql.getValue(filter.cdTabelaPreco) +
				" AND CDCONDICAOPAGAMENTO = " + Sql.getValue(filter.cdCondicaoPagamento) + " UNION ALL ");
		sql.append("SELECT 14 AS PRIORIDADE, ");
		addSelectColumns(filter, sql);
		sql.append(" FROM " + tableName + " WHERE CDEMPRESA = " + Sql.getValue(filter.cdEmpresa) + " AND CDREPRESENTANTE = " + Sql.getValue(filter.cdRepresentante) +
				" AND CDGRUPOPRODUTO1 = " + Sql.getValue(filter.cdGrupoProduto1) + " AND CDGRUPOPRODUTO2 = " + Sql.getValue(filter.cdGrupoProduto2) + 
				" AND CDGRUPOPRODUTO3 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDGRUPOPRODUTO4 = " + Sql.getValue(ValueUtil.VALOR_ZERO) +
				" AND CDGRUPOPRODUTO5 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDTABELAPRECO = " + Sql.getValue(ValueUtil.VALOR_ZERO) +
				" AND CDCONDICAOPAGAMENTO = " + Sql.getValue(filter.cdCondicaoPagamento) + " UNION ALL ");
		sql.append("SELECT 15 AS PRIORIDADE, ");
		addSelectColumns(filter, sql);
		sql.append(" FROM " + tableName + " WHERE CDEMPRESA = " + Sql.getValue(filter.cdEmpresa) + " AND CDREPRESENTANTE = " + Sql.getValue(filter.cdRepresentante) +
				" AND CDGRUPOPRODUTO1 = " + Sql.getValue(filter.cdGrupoProduto1) + " AND CDGRUPOPRODUTO2 = " + Sql.getValue(filter.cdGrupoProduto2) + 
				" AND CDGRUPOPRODUTO3 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDGRUPOPRODUTO4 = " + Sql.getValue(ValueUtil.VALOR_ZERO) +
				" AND CDGRUPOPRODUTO5 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDTABELAPRECO = " + Sql.getValue(filter.cdTabelaPreco) +
				" AND CDCONDICAOPAGAMENTO = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " UNION ALL ");
		sql.append("SELECT 16 AS PRIORIDADE, ");
		addSelectColumns(filter, sql);
		sql.append(" FROM " + tableName + " WHERE CDEMPRESA = " + Sql.getValue(filter.cdEmpresa) + " AND CDREPRESENTANTE = " + Sql.getValue(filter.cdRepresentante) +
				" AND CDGRUPOPRODUTO1 = " + Sql.getValue(filter.cdGrupoProduto1) + " AND CDGRUPOPRODUTO2 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + 
				" AND CDGRUPOPRODUTO3 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDGRUPOPRODUTO4 = " + Sql.getValue(ValueUtil.VALOR_ZERO) +
				" AND CDGRUPOPRODUTO5 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDTABELAPRECO = " + Sql.getValue(filter.cdTabelaPreco) +
				" AND CDCONDICAOPAGAMENTO = " + Sql.getValue(filter.cdCondicaoPagamento) + " UNION ALL ");
		sql.append("SELECT 17 AS PRIORIDADE, ");
		addSelectColumns(filter, sql);
		sql.append(" FROM " + tableName + " WHERE CDEMPRESA = " + Sql.getValue(filter.cdEmpresa) + " AND CDREPRESENTANTE = " + Sql.getValue(filter.cdRepresentante) +
				" AND CDGRUPOPRODUTO1 = " + Sql.getValue(filter.cdGrupoProduto1) + " AND CDGRUPOPRODUTO2 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + 
				" AND CDGRUPOPRODUTO3 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDGRUPOPRODUTO4 = " + Sql.getValue(ValueUtil.VALOR_ZERO) +
				" AND CDGRUPOPRODUTO5 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDTABELAPRECO = " + Sql.getValue(ValueUtil.VALOR_ZERO) +
				" AND CDCONDICAOPAGAMENTO = " + Sql.getValue(filter.cdCondicaoPagamento) + " UNION ALL ");
		sql.append("SELECT 18 AS PRIORIDADE, ");
		addSelectColumns(filter, sql);
		sql.append(" FROM " + tableName + " WHERE CDEMPRESA = " + Sql.getValue(filter.cdEmpresa) + " AND CDREPRESENTANTE = " + Sql.getValue(filter.cdRepresentante) +
				" AND CDGRUPOPRODUTO1 = " + Sql.getValue(filter.cdGrupoProduto1) + " AND CDGRUPOPRODUTO2 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + 
				" AND CDGRUPOPRODUTO3 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDGRUPOPRODUTO4 = " + Sql.getValue(ValueUtil.VALOR_ZERO) +
				" AND CDGRUPOPRODUTO5 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDTABELAPRECO = " + Sql.getValue(filter.cdTabelaPreco) +
				" AND CDCONDICAOPAGAMENTO = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " UNION ALL ");
		sql.append("SELECT 19 AS PRIORIDADE, ");
		addSelectColumns(filter, sql);
		sql.append(" FROM " + tableName + " WHERE CDEMPRESA = " + Sql.getValue(filter.cdEmpresa) + " AND CDREPRESENTANTE = " + Sql.getValue(filter.cdRepresentante) +
				" AND CDGRUPOPRODUTO1 = " + Sql.getValue(filter.cdGrupoProduto1) + " AND CDGRUPOPRODUTO2 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + 
				" AND CDGRUPOPRODUTO3 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDGRUPOPRODUTO4 = " + Sql.getValue(ValueUtil.VALOR_ZERO) +
				" AND CDGRUPOPRODUTO5 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDTABELAPRECO = " + Sql.getValue(ValueUtil.VALOR_ZERO) +
				" AND CDCONDICAOPAGAMENTO = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " UNION ALL ");
		sql.append("SELECT 20 AS PRIORIDADE, ");
		addSelectColumns(filter, sql);
		sql.append(" FROM " + tableName + " WHERE CDEMPRESA = " + Sql.getValue(filter.cdEmpresa) + " AND CDREPRESENTANTE = " + Sql.getValue(filter.cdRepresentante) +
				" AND CDGRUPOPRODUTO1 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDGRUPOPRODUTO2 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + 
				" AND CDGRUPOPRODUTO3 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDGRUPOPRODUTO4 = " + Sql.getValue(ValueUtil.VALOR_ZERO) +
				" AND CDGRUPOPRODUTO5 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDTABELAPRECO = " + Sql.getValue(filter.cdTabelaPreco) +
				" AND CDCONDICAOPAGAMENTO = " + Sql.getValue(filter.cdCondicaoPagamento) + " UNION ALL ");
		sql.append("SELECT 21 AS PRIORIDADE, ");
		addSelectColumns(filter, sql);
		sql.append(" FROM " + tableName + " WHERE CDEMPRESA = " + Sql.getValue(filter.cdEmpresa) + " AND CDREPRESENTANTE = " + Sql.getValue(filter.cdRepresentante) +
				" AND CDGRUPOPRODUTO1 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDGRUPOPRODUTO2 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + 
				" AND CDGRUPOPRODUTO3 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDGRUPOPRODUTO4 = " + Sql.getValue(ValueUtil.VALOR_ZERO) +
				" AND CDGRUPOPRODUTO5 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDTABELAPRECO = " + Sql.getValue(ValueUtil.VALOR_ZERO) +
				" AND CDCONDICAOPAGAMENTO = " + Sql.getValue(filter.cdCondicaoPagamento) + " UNION ALL ");
		sql.append("SELECT 22 AS PRIORIDADE, ");
		addSelectColumns(filter, sql);
		sql.append(" FROM " + tableName + " WHERE CDEMPRESA = " + Sql.getValue(filter.cdEmpresa) + " AND CDREPRESENTANTE = " + Sql.getValue(filter.cdRepresentante) +
				" AND CDGRUPOPRODUTO1 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDGRUPOPRODUTO2 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + 
				" AND CDGRUPOPRODUTO3 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDGRUPOPRODUTO4 = " + Sql.getValue(ValueUtil.VALOR_ZERO) +
				" AND CDGRUPOPRODUTO5 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDTABELAPRECO = " + Sql.getValue(filter.cdTabelaPreco) +
				" AND CDCONDICAOPAGAMENTO = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " UNION ALL ");
		sql.append("SELECT 23 AS PRIORIDADE, ");
		addSelectColumns(filter, sql);
		sql.append(" FROM " + tableName + " WHERE CDEMPRESA = " + Sql.getValue(filter.cdEmpresa) + " AND CDREPRESENTANTE = " + Sql.getValue(filter.cdRepresentante) +
				" AND CDGRUPOPRODUTO1 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDGRUPOPRODUTO2 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + 
				" AND CDGRUPOPRODUTO3 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDGRUPOPRODUTO4 = " + Sql.getValue(ValueUtil.VALOR_ZERO) +
				" AND CDGRUPOPRODUTO5 = " + Sql.getValue(ValueUtil.VALOR_ZERO) + " AND CDTABELAPRECO = " + Sql.getValue(ValueUtil.VALOR_ZERO) +
				" AND CDCONDICAOPAGAMENTO = " + Sql.getValue(ValueUtil.VALOR_ZERO) + ") ORDER BY PRIORIDADE ASC LIMIT 1");
		Vector elements = findAll(filter, sql.toString());
		return elements.size() > 0 ? (IndiceGrupoProd)elements.items[0] : null;
	}

}
