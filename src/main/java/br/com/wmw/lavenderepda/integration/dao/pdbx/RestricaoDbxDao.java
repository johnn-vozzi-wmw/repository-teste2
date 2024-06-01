package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Restricao;
import br.com.wmw.lavenderepda.business.domain.RestricaoProduto;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;

public class RestricaoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Restricao();
	}

	private static RestricaoDbxDao instance;

	public RestricaoDbxDao() {
		super(Restricao.TABLE_NAME);
	}

	public static RestricaoDbxDao getInstance() {
		return instance == null ? instance = new RestricaoDbxDao() : instance;
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
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet resultSet) throws SQLException {
		Restricao restricao = new Restricao();
		restricao.rowKey = resultSet.getString("rowkey");
		restricao.cdEmpresa = resultSet.getString("cdEmpresa");
		restricao.cdRepresentante = resultSet.getString("cdRepresentante");
		restricao.cdRestricao = resultSet.getString("cdRestricao");
		restricao.nuTipoVigenciaRestricao = resultSet.getInt("nuTipoVigenciaRestricao");
		restricao.nuNivelCliente = resultSet.getInt("nuNivelCliente");
		restricao.nuNivelProduto = resultSet.getInt("nuNivelProduto");
		restricao.dsRestricao = resultSet.getString("dsRestricao");
		restricao.dtInicialVigencia = resultSet.getDate("dtInicialVigencia");
		restricao.dtFimVigencia = resultSet.getDate("dtFimVigencia");
		restricao.cdUsuario = resultSet.getString("cdUsuario");
		restricao.flTipoAlteracao = resultSet.getString("flTipoAlteracao");
		return restricao;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.rowkey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDRESTRICAO,");
		sql.append(" tb.NUTIPOVIGENCIARESTRICAO,");
		sql.append(" tb.NUNIVELCLIENTE,");
		sql.append(" tb.NUNIVELPRODUTO,");
		sql.append(" tb.DSRESTRICAO,");
		sql.append(" tb.DTINICIALVIGENCIA,");
		sql.append(" tb.DTFIMVIGENCIA,");
		sql.append(" tb.CDUSUARIO,");
		sql.append(" tb.FLTIPOALTERACAO");
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		sql.append(getSqlWhereClause((Restricao) domain, "TB.").getSql());
	}

	@Override
	protected void addWhereDeleteByExample(BaseDomain domain, StringBuffer sql) {
		sql.append(getSqlWhereClause((Restricao) domain, "").getSql());
	}

	private SqlWhereClause getSqlWhereClause(final Restricao restricao, String alias) {
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(alias + "CDEMPRESA = ", restricao.cdEmpresa);
		sqlWhereClause.addAndCondition(alias + "CDREPRESENTANTE = ", restricao.cdRepresentante);
		sqlWhereClause.addAndCondition(alias + "CDRESTRICAO = ", restricao.cdRestricao);
		sqlWhereClause.addAndCondition(alias + "NUTIPOVIGENCIARESTRICAO = ", restricao.nuTipoVigenciaRestricao);
		sqlWhereClause.addAndCondition(alias + "NUNIVELCLIENTE = ", restricao.nuNivelCliente);
		sqlWhereClause.addAndCondition(alias + "NUNIVELPRODUTO = ", restricao.nuNivelProduto);
		sqlWhereClause.addAndCondition(alias + "DTINICIALVIGENCIA <= ", restricao.dtInicialVigencia);
		sqlWhereClause.addAndCondition(alias + "DTFIMVIGENCIA >= ", restricao.dtFimVigencia);
		return sqlWhereClause;
	}

	public RestricaoProduto findRestricaoProdutoByProdutoCliente(Restricao restricaoFilter) throws SQLException {
		String sql = getSqlRestricaoForJoin(restricaoFilter);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {
				RestricaoProduto restricaoProduto = new RestricaoProduto();
				restricaoProduto.cdEmpresa = rs.getString("cdEmpresa");
				restricaoProduto.cdRepresentante = rs.getString("cdRepresentante");
				restricaoProduto.cdRestricao = rs.getString("cdRestricao");
				restricaoProduto.cdProduto = rs.getString("cdProduto");
				restricaoProduto.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
				restricaoProduto.cdGrupoProduto2 = rs.getString("cdGrupoProduto2");
				restricaoProduto.cdGrupoProduto3 = rs.getString("cdGrupoProduto3");
				restricaoProduto.cdFornecedor = rs.getString("cdFornecedor");
				restricaoProduto.qtdRestricao = rs.getInt("qtdRestricao");
				restricaoProduto.qtItemComprando = restricaoFilter.restricaoProdutoFilter.qtItemComprando;
				restricaoProduto.qtItemDisponivel = rs.getDouble("qtdDisponivel");
				if (restricaoProduto.qtItemDisponivel < 0) {
					restricaoProduto.qtItemDisponivel = 0;
				}
				return restricaoProduto;
			}
		}
		return null;
	}

	public String getSqlRestricaoForJoin(Restricao restricaoFilter) {
		StringBuffer sql = getSqlBuffer();
		appendSqlCountRestricao(restricaoFilter, sql);
		sql.append(" FROM TBLVPRESTRICAO RESTRICAO ");
		appendJoinProduto(restricaoFilter, sql);
		appendJoinRestricaoCliente(restricaoFilter, sql);
		appendJoinRestricaoProduto(restricaoFilter, sql);
		if (restricaoFilter.restricaoClienteFilter != null) {
			sql.append(getSqlQtdVendido(restricaoFilter));
		}
		appendWhereRestricao(restricaoFilter, sql);
		appendOrderByRestricao(sql);
		sql.append(" ) OUT WHERE ")
				.append(" QTDTOTAL < 0 ")
				.append(" LIMIT 1 ");
		return sql.toString();
	}

	private void appendWhereRestricao(Restricao restricaoFilter, StringBuffer sql) {
		 sql.append(" WHERE ")
				.append(" RESTRICAO.CDEMPRESA = ").append(Sql.getValue(restricaoFilter.cdEmpresa))
				.append(" AND RESTRICAO.CDREPRESENTANTE = ").append(Sql.getValue(restricaoFilter.cdRepresentante))
				.append(" AND ( ")
				.append(" (RESTRICAO.NUTIPOVIGENCIARESTRICAO = ").append(Sql.getValue(Restricao.NUTIPOVIGENCIA_PERSONALIZADA)).append(" AND (RESTRICAO.DTINICIALVIGENCIA <= ").append(Sql.getValue(restricaoFilter.dtInicialVigenciaFilterPersonalizado)).append(" OR RESTRICAO.DTINICIALVIGENCIA IS NULL OR RESTRICAO.DTINICIALVIGENCIA = '') AND (RESTRICAO.DTFIMVIGENCIA >= ").append(Sql.getValue(restricaoFilter.dtFimVigenciaFilterPersonalizado)).append(" OR RESTRICAO.DTFIMVIGENCIA IS NULL OR RESTRICAO.DTFIMVIGENCIA = '')) ")
				.append(" OR (RESTRICAO.NUTIPOVIGENCIARESTRICAO <> ").append(Sql.getValue(Restricao.NUTIPOVIGENCIA_PERSONALIZADA)).append(" ) ")
				.append(" ) ");
	}

	private void appendJoinRestricaoCliente(Restricao restricaoFilter, StringBuffer sql) {
		if (restricaoFilter.restricaoClienteFilter != null) {
			sql.append(" JOIN TBLVPRESTRICAOCLIENTE RESTRICAOCLIENTE ON ")
					.append(" RESTRICAOCLIENTE.CDEMPRESA = ").append(Sql.getValue(restricaoFilter.restricaoClienteFilter.cdEmpresa))
					.append(" AND RESTRICAOCLIENTE.CDREPRESENTANTE = ").append(Sql.getValue(restricaoFilter.restricaoClienteFilter.cdRepresentante))
					.append(" AND RESTRICAOCLIENTE.CDRESTRICAO = RESTRICAO.CDRESTRICAO ");
			if (restricaoFilter.dynamicCliente) {
				sql.append(" AND (RESTRICAOCLIENTE.CDCLIENTE = ").append(restricaoFilter.restricaoClienteFilter.cdCliente).append(" OR RESTRICAOCLIENTE.CDCLIENTE = ").append(Sql.getValue(Restricao.VALOR_PADRAO)).append(") ");
			} else {
				sql.append(" AND (RESTRICAOCLIENTE.CDCLIENTE = ").append(Sql.getValue(restricaoFilter.restricaoClienteFilter.cdCliente)).append(" OR RESTRICAOCLIENTE.CDCLIENTE = ").append(Sql.getValue(Restricao.VALOR_PADRAO)).append(") ");
			}
		}
	}

	private void appendSqlCountRestricao(Restricao restricaoFilter, StringBuffer sql) {
		sql.append(" SELECT *, QTDRESTRICAO - QTDVENDIDO AS QTDDISPONIVEL, QTDRESTRICAO - (QTDVENDIDO + QTDCOMPRANDO) AS QTDTOTAL")
				.append(" FROM ( ")
				.append(" SELECT ")
				.append(" RESTRICAOPRODUTO.*, ");
		if (restricaoFilter.restricaoClienteFilter != null) {
			sql.append("(IFNULL(SUM(ITEMPEDIDO.QTITEMFISICO), 0) + ");
			sql.append("IFNULL(SUM(ITEMPEDIDOERP.QTITEMFISICO), 0)) AS QTDVENDIDO,");
		} else {
			sql.append("0 AS QTDVENDIDO, ");
		}
		if (restricaoFilter.dynamicQuantidade) {
			sql.append(restricaoFilter.qtdDynamicFilter).append(" AS QTDCOMPRANDO ");
		} else {
			sql.append(Sql.getValue(restricaoFilter.restricaoProdutoFilter.qtItemComprando)).append(" AS QTDCOMPRANDO ");
		}
	}

	private void appendJoinProduto(Restricao restricaoFilter, StringBuffer sql) {
		sql.append(" JOIN TBLVPPRODUTO PRODUTO ON ")
				.append(" PRODUTO.CDEMPRESA = ").append(Sql.getValue(restricaoFilter.restricaoProdutoFilter.cdEmpresa))
				.append(" AND PRODUTO.CDREPRESENTANTE = ").append(Sql.getValue(restricaoFilter.restricaoProdutoFilter.cdRepresentante));
		if (restricaoFilter.dynamicProduto) {
			sql.append(" AND PRODUTO.CDPRODUTO = ").append(restricaoFilter.restricaoProdutoFilter.cdProduto);
		} else {
			sql.append(" AND PRODUTO.CDPRODUTO = ").append(Sql.getValue(restricaoFilter.restricaoProdutoFilter.cdProduto));
		}
	}

	private void appendJoinRestricaoProduto(Restricao restricaoFilter, StringBuffer sql) {
		sql.append(" JOIN TBLVPRESTRICAOPRODUTO RESTRICAOPRODUTO ON ");
		sql.append(" RESTRICAOPRODUTO.CDEMPRESA = ").append(Sql.getValue(restricaoFilter.restricaoProdutoFilter.cdEmpresa));
		sql.append(" AND RESTRICAOPRODUTO.CDREPRESENTANTE = ").append(Sql.getValue(restricaoFilter.restricaoProdutoFilter.cdRepresentante));
		sql.append(" AND RESTRICAOPRODUTO.CDRESTRICAO = RESTRICAO.CDRESTRICAO ");
		sql.append(" AND (RESTRICAOPRODUTO.CDPRODUTO = PRODUTO.CDPRODUTO OR RESTRICAOPRODUTO.CDPRODUTO = ").append(Sql.getValue(Restricao.VALOR_PADRAO)).append(") ");
		sql.append(" AND (RESTRICAOPRODUTO.CDGRUPOPRODUTO1 = PRODUTO.CDGRUPOPRODUTO1 OR RESTRICAOPRODUTO.CDGRUPOPRODUTO1 = ").append(Sql.getValue(Restricao.VALOR_PADRAO)).append(") ");
		sql.append(" AND (RESTRICAOPRODUTO.CDGRUPOPRODUTO2 = PRODUTO.CDGRUPOPRODUTO2 OR RESTRICAOPRODUTO.CDGRUPOPRODUTO2 = ").append(Sql.getValue(Restricao.VALOR_PADRAO)).append(") ");
		sql.append(" AND (RESTRICAOPRODUTO.CDGRUPOPRODUTO3 = PRODUTO.CDGRUPOPRODUTO3 OR RESTRICAOPRODUTO.CDGRUPOPRODUTO3 = ").append(Sql.getValue(Restricao.VALOR_PADRAO)).append(") ");
		sql.append(" AND (RESTRICAOPRODUTO.CDFORNECEDOR = PRODUTO.CDFORNECEDOR OR RESTRICAOPRODUTO.CDFORNECEDOR = ").append(Sql.getValue(Restricao.VALOR_PADRAO)).append(") ");
	}

	private String getSqlQtdVendido(Restricao restricaoFilter) {
		StringBuffer sql = getSqlBuffer();
		boolean addPedidosAbertos = ValueUtil.isNotEmpty(restricaoFilter.itemPedidoFilter.nuPedido) && restricaoFilter.dynamicQuantidade;
		//TBLVPPEDIDO
		sql.append(" LEFT JOIN TBLVPPEDIDO PEDIDO ON ");
		appendConditionsForFilterPedido(restricaoFilter, sql);
		appendPedidoVigenciaFilter(restricaoFilter, sql, addPedidosAbertos);
		appendLeftJoinItemPedido(restricaoFilter, sql);

		//TBLVPPEDIDOERP
		sql.append(" LEFT JOIN TBLVPPEDIDOERP PEDIDOERP ON ");
		appendConditionsForFilterPedidoErp(restricaoFilter, sql);


		appendLeftJoinItemPedidoErp(restricaoFilter, sql);
		return sql.toString();
	}

	private void appendConditionsForFilterPedidoErp(Restricao restricaoFilter, StringBuffer sql) {
		sql.append(" PEDIDOERP.CDEMPRESA = ").append(Sql.getValue(restricaoFilter.cdEmpresa));
		sql.append(" AND PEDIDOERP.CDREPRESENTANTE = ").append(Sql.getValue(restricaoFilter.cdRepresentante));
		sql.append(" AND PEDIDOERP.FLORIGEMPEDIDO = ").append(Sql.getValue(OrigemPedido.FLORIGEMPEDIDO_ERP));
		if (restricaoFilter.dynamicCliente) {
			sql.append(" AND PEDIDOERP.CDCLIENTE = ").append(restricaoFilter.itemPedidoFilter.cdCliente);
		} else {
			sql.append(" AND PEDIDOERP.CDCLIENTE = ").append(Sql.getValue(restricaoFilter.itemPedidoFilter.cdCliente));
		}
		sql.append(" AND PEDIDOERP.CDSTATUSPEDIDO <> ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoCancelado));
		sql.append(" AND ((RESTRICAO.NUTIPOVIGENCIARESTRICAO = ").append(Sql.getValue(Restricao.NUTIPOVIGENCIA_DIARIO)).append(" AND PEDIDOERP.DTFECHAMENTO >= ").append(Sql.getValue(restricaoFilter.dtInicialVigenciaFilterDiario)).append(" AND PEDIDOERP.DTFECHAMENTO <= ").append(Sql.getValue(restricaoFilter.dtFimVigenciaFilterDiario)).append(" ) ");
		sql.append(" OR (RESTRICAO.NUTIPOVIGENCIARESTRICAO = ").append(Sql.getValue(Restricao.NUTIPOVIGENCIA_SEMANAL)).append(" AND PEDIDOERP.DTFECHAMENTO >= ").append(Sql.getValue(restricaoFilter.dtInicialVigenciaFilterSemanal)).append(" AND PEDIDOERP.DTFECHAMENTO <= ").append(Sql.getValue(restricaoFilter.dtFimVigenciaFilterSemanal)).append(" ) ");
		sql.append(" OR (RESTRICAO.NUTIPOVIGENCIARESTRICAO = ").append(Sql.getValue(Restricao.NUTIPOVIGENCIA_MENSAL)).append(" AND PEDIDOERP.DTFECHAMENTO >= ").append(Sql.getValue(restricaoFilter.dtInicialVigenciaFilterMensal)).append(" AND PEDIDOERP.DTFECHAMENTO <= ").append(Sql.getValue(restricaoFilter.dtFimVigenciaFilterMensal)).append(" ) ");
		sql.append(" OR (RESTRICAO.NUTIPOVIGENCIARESTRICAO = ").append(Sql.getValue(Restricao.NUTIPOVIGENCIA_PERSONALIZADA)).append(" AND PEDIDOERP.DTFECHAMENTO >= ").append(Sql.getValue(restricaoFilter.dtInicialVigenciaFilterPersonalizado)).append(" AND PEDIDOERP.DTFECHAMENTO <= ").append(Sql.getValue(restricaoFilter.dtFimVigenciaFilterPersonalizado)).append(" ) ");
		sql.append(" ) ");
	}

	public void appendConditionsForFilterPedido(Restricao restricaoFilter, StringBuffer sql) {
		sql.append(" PEDIDO.CDEMPRESA = ").append(Sql.getValue(restricaoFilter.cdEmpresa));
		sql.append(" AND PEDIDO.CDREPRESENTANTE = ").append(Sql.getValue(restricaoFilter.cdRepresentante));
		sql.append(" AND PEDIDO.FLORIGEMPEDIDO = ").append(Sql.getValue(OrigemPedido.FLORIGEMPEDIDO_PDA));
		if (restricaoFilter.dynamicCliente) {
			sql.append(" AND PEDIDO.CDCLIENTE = ").append(restricaoFilter.itemPedidoFilter.cdCliente);
		} else {
			sql.append(" AND PEDIDO.CDCLIENTE = ").append(Sql.getValue(restricaoFilter.itemPedidoFilter.cdCliente));
		}
	}

	private void appendLeftJoinItemPedidoErp(Restricao restricaoFilter, StringBuffer sql) {
		sql.append(" LEFT JOIN TBLVPITEMPEDIDOERP ITEMPEDIDOERP ");
		sql.append(" ON ITEMPEDIDOERP.CDEMPRESA = ").append(Sql.getValue(restricaoFilter.cdEmpresa));
		sql.append(" AND ITEMPEDIDOERP.CDREPRESENTANTE = ").append(Sql.getValue(restricaoFilter.cdRepresentante));
		sql.append(" AND ITEMPEDIDOERP.FLORIGEMPEDIDO = PEDIDOERP.FLORIGEMPEDIDO ");
		sql.append(" AND ITEMPEDIDOERP.NUPEDIDO = PEDIDOERP.NUPEDIDO ");
		if (restricaoFilter.dynamicProduto) {
			sql.append(" AND ITEMPEDIDOERP.CDPRODUTO = ").append(restricaoFilter.itemPedidoFilter.cdProduto);
		} else {
			sql.append(" AND ITEMPEDIDOERP.CDPRODUTO = ").append(Sql.getValue(restricaoFilter.itemPedidoFilter.cdProduto));
		}
		sql.append(" AND ITEMPEDIDOERP.FLORIGEMPEDIDO = PEDIDOERP.FLORIGEMPEDIDO");
	}

	private void appendLeftJoinItemPedido(Restricao restricaoFilter, StringBuffer sql) {
		sql.append(" LEFT JOIN TBLVPITEMPEDIDO ITEMPEDIDO ");
		sql.append(" ON ITEMPEDIDO.CDEMPRESA = ").append(Sql.getValue(restricaoFilter.cdEmpresa));
		sql.append(" AND ITEMPEDIDO.CDREPRESENTANTE = ").append(Sql.getValue(restricaoFilter.cdRepresentante));
		sql.append(" AND ITEMPEDIDO.FLORIGEMPEDIDO = PEDIDO.FLORIGEMPEDIDO ");
		sql.append(" AND ITEMPEDIDO.NUPEDIDO = PEDIDO.NUPEDIDO ");
		if (restricaoFilter.dynamicProduto) {
			sql.append(" AND ITEMPEDIDO.CDPRODUTO = ").append(restricaoFilter.itemPedidoFilter.cdProduto);
		} else {
			sql.append(" AND ITEMPEDIDO.CDPRODUTO = ").append(Sql.getValue(restricaoFilter.itemPedidoFilter.cdProduto));
		}
		sql.append(" AND ITEMPEDIDO.FLORIGEMPEDIDO = ").append(Sql.getValue(restricaoFilter.itemPedidoFilter.flOrigemPedido));
	}

	private void appendPedidoVigenciaFilter(Restricao restricaoFilter, StringBuffer sql, boolean addPedidosAbertos) {
		sql.append(" AND (((RESTRICAO.NUTIPOVIGENCIARESTRICAO = ").append(Sql.getValue(Restricao.NUTIPOVIGENCIA_DIARIO)).append(" AND PEDIDO.DTFECHAMENTO >= ").append(Sql.getValue(restricaoFilter.dtInicialVigenciaFilterDiario)).append(" AND PEDIDO.DTFECHAMENTO <= ").append(Sql.getValue(restricaoFilter.dtFimVigenciaFilterDiario));
		addCdStatusPedidoFilter(restricaoFilter, sql, addPedidosAbertos);
		sql.append(" ) ").append(" OR ((RESTRICAO.NUTIPOVIGENCIARESTRICAO = ").append(Sql.getValue(Restricao.NUTIPOVIGENCIA_SEMANAL)).append(" AND PEDIDO.DTFECHAMENTO >= ").append(Sql.getValue(restricaoFilter.dtInicialVigenciaFilterSemanal)).append(" AND PEDIDO.DTFECHAMENTO <= ").append(Sql.getValue(restricaoFilter.dtFimVigenciaFilterSemanal));
		addCdStatusPedidoFilter(restricaoFilter, sql, addPedidosAbertos);
		sql.append(" ) ").append(" OR ((RESTRICAO.NUTIPOVIGENCIARESTRICAO = ").append(Sql.getValue(Restricao.NUTIPOVIGENCIA_MENSAL)).append(" AND PEDIDO.DTFECHAMENTO >= ").append(Sql.getValue(restricaoFilter.dtInicialVigenciaFilterMensal)).append(" AND PEDIDO.DTFECHAMENTO <= ").append(Sql.getValue(restricaoFilter.dtFimVigenciaFilterMensal));
		addCdStatusPedidoFilter(restricaoFilter, sql, addPedidosAbertos);
		sql.append(" ) ").append(" OR ((RESTRICAO.NUTIPOVIGENCIARESTRICAO = ").append(Sql.getValue(Restricao.NUTIPOVIGENCIA_PERSONALIZADA)).append(" AND PEDIDO.DTFECHAMENTO >= ").append(Sql.getValue(restricaoFilter.dtInicialVigenciaFilterPersonalizado)).append(" AND PEDIDO.DTFECHAMENTO <= ").append(Sql.getValue(restricaoFilter.dtFimVigenciaFilterPersonalizado));
		addCdStatusPedidoFilter(restricaoFilter, sql, addPedidosAbertos);
		sql.append("))");
	}

	private void addCdStatusPedidoFilter(Restricao restricaoFilter, StringBuffer sql, boolean addPedidosAbertos) {
		sql.append(" AND PEDIDO.CDSTATUSPEDIDO <> ").append(Sql.getValue(restricaoFilter.itemPedidoFilter.cdStatusPedidoFilter));
		if (addPedidosAbertos) {
			sql.append(" ) OR (PEDIDO.CDSTATUSPEDIDO = ").append(Sql.getValue(restricaoFilter.itemPedidoFilter.cdStatusPedidoFilter)).append(" AND PEDIDO.NUPEDIDO = ").append(Sql.getValue(restricaoFilter.itemPedidoFilter.nuPedido)).append(" ) ");
		} else {
			sql.append(" )");
		}
	}


	public void appendCTERestricao(StringBuffer sql, Restricao restricaoFilter) {
		sql.append("WITH RESTRICAO AS ( ");
		sql.append(" SELECT RESTRICAO.CDRESTRICAO, RESTRICAO.NUTIPOVIGENCIARESTRICAO ");
		sql.append(" FROM TBLVPRESTRICAO RESTRICAO ");
		appendJoinRestricaoCliente(restricaoFilter, sql);
		appendWhereRestricao(restricaoFilter, sql);
		appendOrderByRestricao(sql);

		sql.append("), PEDIDO AS ( ");
		sql.append(" SELECT PEDIDO.FLORIGEMPEDIDO, PEDIDO.NUPEDIDO, RESTRICAO.CDRESTRICAO FROM TBLVPPEDIDO PEDIDO ");
		sql.append(" JOIN RESTRICAO ON 1=1 WHERE ");
		appendConditionsForFilterPedido(restricaoFilter, sql);
		appendPedidoVigenciaFilter(restricaoFilter, sql, false);

		sql.append("), PEDIDOERP AS ( ");
		sql.append(" SELECT PEDIDOERP.FLORIGEMPEDIDO, PEDIDOERP.NUPEDIDO, RESTRICAO.CDRESTRICAO FROM TBLVPPEDIDOERP PEDIDOERP ");
		sql.append(" JOIN RESTRICAO ON 1=1 WHERE ");
		appendConditionsForFilterPedidoErp(restricaoFilter, sql);
		sql.append(" ) ");
	}

	private void appendOrderByRestricao(StringBuffer sql) {
		sql.append(" ORDER BY RESTRICAO.NUNIVELCLIENTE DESC, RESTRICAO.NUNIVELPRODUTO DESC, RESTRICAO.NUTIPOVIGENCIARESTRICAO ");
	}

	public void addConditionRestricao(StringBuffer sql, Restricao restricaoFilter) {
		sql.append(" AND NOT EXISTS( ");
		appendSqlCountRestricao(restricaoFilter, sql);
		sql.append(" FROM RESTRICAO ");

		appendJoinProduto(restricaoFilter, sql);
		appendJoinRestricaoProduto(restricaoFilter, sql);

		sql.append(" LEFT JOIN PEDIDO ON PEDIDO.CDRESTRICAO = RESTRICAO.CDRESTRICAO");
		sql.append(" LEFT JOIN PEDIDOERP ON PEDIDOERP.CDRESTRICAO = RESTRICAO.CDRESTRICAO ");

		appendLeftJoinItemPedido(restricaoFilter, sql);
		appendLeftJoinItemPedidoErp(restricaoFilter, sql);
		sql.append(" ) OUT ");
		sql.append(" WHERE QTDTOTAL < 0)");
	}

}