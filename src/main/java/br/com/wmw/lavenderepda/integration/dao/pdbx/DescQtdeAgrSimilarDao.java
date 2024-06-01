package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescQtdeAgrSimilar;
import br.com.wmw.lavenderepda.business.domain.DescQuantidadeCliente;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class DescQtdeAgrSimilarDao extends CrudDbxDao {

	private static DescQtdeAgrSimilarDao instance;

	public DescQtdeAgrSimilarDao() {
		super(DescQtdeAgrSimilar.TABLE_NAME);
	}

	public static DescQtdeAgrSimilarDao getInstance() {
		if (instance == null) {
			instance = new DescQtdeAgrSimilarDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		DescQtdeAgrSimilar desc = new DescQtdeAgrSimilar();
		desc.rowKey = rs.getString("rowkey");
		desc.cdEmpresa = rs.getString("cdEmpresa");
		desc.cdRepresentante = rs.getString("cdRepresentante");
		desc.cdTabelaPreco = rs.getString("cdTabelaPreco");
		desc.cdProduto = rs.getString("cdProduto");
		desc.cdProdutoSimilar = rs.getString("cdProdutoSimilar");
		return desc;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append("tb.rowkey")
		.append("tb.CDEMPRESA,")
		.append("tb.CDREPRESENTANTE,")
		.append("tb.CDTABELAPRECO,")
		.append("tb.CDPRODUTO,")
		.append("tb.CDPRODUTOSIMILAR");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		DescQtdeAgrSimilar desc = (DescQtdeAgrSimilar) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", desc.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", desc.cdRepresentante);
		sqlWhereClause.addAndCondition("CDTABELAPRECO = ", desc.cdTabelaPreco);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", desc.cdProduto);
		sqlWhereClause.addAndCondition("CDPRODUTOSIMILAR = ", desc.cdProdutoSimilar);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescQtdeAgrSimilar();
	}

	private void addWhereTablesSimilares(DescQtdeAgrSimilar filter, ItemPedido itemPedido, StringBuffer sql, boolean isEspecificaProduto) {
		sql.append(" AND EXISTS (")
		.append(" SELECT 1 FROM TBLVPDESCQTDEAGRSIMILAR descq JOIN TBLVPDESCQUANTIDADE desc ON ")
		.append(" descq.CDEMPRESA = desc.CDEMPRESA AND descq.CDREPRESENTANTE = desc.CDREPRESENTANTE AND ")
		.append(" descq.CDTABELAPRECO = desc.CDTABELAPRECO AND descq.CDPRODUTO = desc.CDPRODUTO AND ");
		if(isEspecificaProduto) {
			sql.append(" desc.CDPRODUTO = ").append(Sql.getValue(filter.cdProduto)).append(" AND ");
		}
		sql.append(" desc.FLAGRUPADORSIMILARIDADE = 'S' ");
		if (LavenderePdaConfig.permiteVincularCliente) {
			addJoinDescQuantidadeCliente(sql, itemPedido.pedido.cdCliente);
		}
		sql.append(" WHERE descq.CDEMPRESA = tb.CDEMPRESA AND ")
		.append(" descq.CDREPRESENTANTE = ").append(Sql.getValue(filter.cdRepresentante)).append(" AND ");
		if (!filter.desconsideraTabPreco) {
			sql.append(" descq.CDTABELAPRECO = ").append(Sql.getValue(filter.cdTabelaPreco)).append(" AND ");
		}
		sql.append(" (descq.CDPRODUTO = tb.CDPRODUTO OR descq.CDPRODUTOSIMILAR = tb.CDPRODUTO)");
		if (LavenderePdaConfig.usaVigenciaDescQuantidade) {
			sql.append(" AND ").append(Sql.getValue(DateUtil.getCurrentDate())).append(" BETWEEN desc.DTINICIALVIGENCIA AND desc.DTFIMVIGENCIA");
		}
		if (LavenderePdaConfig.permiteVincularCliente) {
			sql.append(" AND (descCliente.CDCLIENTE IS NOT NULL OR ")
			.append(" NOT EXISTS (SELECT 1 FROM TBLVPDESCQUANTIDADECLIENTE WHERE ")
			.append(" CDEMPRESA = tb.CDEMPRESA AND ")
			.append(" CDREPRESENTANTE = ").append(Sql.getValue(SessionLavenderePda.getCdRepresentanteFiltroDados(DescQuantidadeCliente.class))).append(" AND ")
			.append(" CDPRODUTO = tb.CDPRODUTO AND ");
			if (!filter.desconsideraTabPreco) {
				sql.append(" CDTABELAPRECO = ").append(Sql.getValue(filter.cdTabelaPreco)).append(" AND ");
			}
			sql.append(" CDPRODUTO <> ").append(Sql.getValue(itemPedido.pedido.cdCliente)).append(")")
			.append(")");
		}
		sql.append(")");
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && !isEspecificaProduto) {
			sql.append(" AND (SOLAUTORIZACAO.CDPRODUTO IS NULL OR SOLAUTORIZACAO.FLAUTORIZADO = 'S')");
		}
		if (ValueUtil.isNotEmpty(filter.cdProdutoExcept)) {
			sql.append("AND tb.CDPRODUTO <> ").append(Sql.getValue(filter.cdProdutoExcept));
		}
		sql.append(" AND COALESCE(tb.CDKIT, '') = '' AND COALESCE(tb.CDCOMBO,'') = ''");
		sql.append(" AND tb.FLIGNORADESCQTD <> 'S'");
	}
	
	private void addWhereEmpRepFindDescSimilaresByFilter(StringBuffer sql, ItemPedido itemPedido) {
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals("tb.CDEMPRESA", itemPedido.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("tb.CDREPRESENTANTE", itemPedido.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("tb.NUPEDIDO", itemPedido.nuPedido);
		sqlWhereClause.addAndConditionEquals("tb.FLORIGEMPEDIDO", itemPedido.flOrigemPedido);
		sql.append(sqlWhereClause.getSql());
	}

	public Vector findDescSimilaresByFilter(DescQtdeAgrSimilar filter, ItemPedido itemPedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT tb.CDPRODUTO from TBLVPITEMPEDIDO tb");
		addJoinSolAutorizacao(sql, itemPedido);
		addWhereEmpRepFindDescSimilaresByFilter(sql, itemPedido);
		addWhereTablesSimilares(filter, itemPedido, sql, false);
		Vector list = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
 				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				list.addElement(rs.getString(1));
			}
			return list;
		}
	}
	
	private void addJoinSolAutorizacao(StringBuffer sql, ItemPedido itemPedido) {
		sql.append(" LEFT JOIN (")
		.append(" SELECT FLAUTORIZADO, CDPRODUTO, MAX(CDSOLAUTORIZACAO) AS CDSOLAUTORIZACAO FROM TBLVPSOLAUTORIZACAO")
		.append(" WHERE CDEMPRESA = ").append(Sql.getValue(itemPedido.cdEmpresa)).append(" AND ")
		.append(" CDREPRESENTANTE = ").append(Sql.getValue(itemPedido.cdRepresentante)).append(" AND ")
		.append(" NUPEDIDO = ").append(Sql.getValue(itemPedido.nuPedido)).append(" AND ")
		.append(" FLORIGEMPEDIDO = ").append(Sql.getValue(itemPedido.flOrigemPedido)).append(" AND ")
		.append(" CDCLIENTE = ").append(Sql.getValue(itemPedido.pedido.cdCliente)).append(" AND ")
		.append(" FLEXCLUIDO <> 'S'")
		.append(" GROUP BY CDPRODUTO")
		.append(") SOLAUTORIZACAO ON tb.CDPRODUTO = SOLAUTORIZACAO.CDPRODUTO");
	}
	
	private void addJoinDescQuantidadeCliente(StringBuffer sql, String cdCliente) {
    	sql.append(" LEFT JOIN TBLVPDESCQUANTIDADECLIENTE descCliente ON ")
    	.append(" desc.CDEMPRESA = descCliente.CDEMPRESA AND ")
    	.append(" desc.CDREPRESENTANTE = descCliente.CDREPRESENTANTE AND ")
    	.append(" desc.CDTABELAPRECO = descCliente.CDTABELAPRECO AND ")
    	.append(" desc.CDPRODUTO = descCliente.CDPRODUTO AND ")
    	.append(" descCliente.CDCLIENTE = ").append(Sql.getValue(cdCliente));
    }
	
	private void addWhereEmpRepFindProdutosItemPedidoSimilar(StringBuffer sql) {
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals("tb.CDEMPRESA", SessionLavenderePda.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("tb.CDREPRESENTANTE", SessionLavenderePda.getCdRepresentanteFiltroDados(Produto.class));
		sql.append(sqlWhereClause.getSql());
	}
	
	public Vector findProdutosItemPedidoSimilar(DescQtdeAgrSimilar filter, ItemPedido itemPedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		baseSelectProdutoJoinItemPedido(sql, itemPedido);
		addWhereEmpRepFindProdutosItemPedidoSimilar(sql);
		addWhereTablesSimilares(filter, itemPedido, sql, true);
		try (Statement st = getCurrentDriver().getStatement();
 				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector itensPedidoSimilares = new Vector();
			ItemPedido itemPedidoResult;
			while (rs.next()) {
				itemPedidoResult = new ItemPedido();
				itemPedidoResult.cdEmpresa = rs.getString(1);
				itemPedidoResult.cdRepresentante = rs.getString(2);
				itemPedidoResult.nuPedido = rs.getString(3);
				itemPedidoResult.cdProduto = rs.getString(4);
				itemPedidoResult.qtItemFisico = rs.getDouble(5);
				itensPedidoSimilares.addElement(itemPedidoResult);
			}
			return itensPedidoSimilares;
		}
	}
	
	private void baseSelectProdutoJoinItemPedido(StringBuffer sql, ItemPedido itemPedido) {
		sql.append("SELECT tb.CDEMPRESA, tb.CDREPRESENTANTE, tb.NUPEDIDO, tb.CDPRODUTO, COALESCE(tb.QTITEMFISICO, 0) QTITEMFISICO FROM (")
		.append("SELECT PROD.CDEMPRESA, PROD.CDREPRESENTANTE, ITEMPED.NUPEDIDO, PROD.CDPRODUTO, ITEMPED.QTITEMFISICO, ITEMPED.CDKIT, ")
		.append("ITEMPED.CDCOMBO, COALESCE(ITEMPED.FLIGNORADESCQTD, 'N') FLIGNORADESCQTD ")
		.append("FROM TBLVPPRODUTO PROD LEFT JOIN (")
		.append("SELECT ITEMAUX.CDEMPRESA, ITEMAUX.CDREPRESENTANTE, ITEMAUX.CDPRODUTO, ITEMAUX.NUPEDIDO, ")
		.append("ITEMAUX.FLORIGEMPEDIDO, ITEMAUX.CDKIT, ITEMAUX.CDCOMBO, ITEMAUX.FLIGNORADESCQTD, ITEMAUX.QTITEMFISICO ")
		.append("FROM TBLVPITEMPEDIDO ITEMAUX LEFT JOIN (SELECT SOLAUX.CDPRODUTO, MAX(SOLAUX.FLAUTORIZADO) AS FLAUTORIZADO, ")
		.append("MAX(SOLAUX.CDSOLAUTORIZACAO) AS CDSOLAUTORIZACAO FROM TBLVPSOLAUTORIZACAO SOLAUX ")
		.append("WHERE SOLAUX.CDEMPRESA = ").append(Sql.getValue(itemPedido.cdEmpresa)).append(" AND ")
		.append("SOLAUX.CDREPRESENTANTE = ").append(Sql.getValue(itemPedido.cdRepresentante)).append(" AND ")
		.append("SOLAUX.NUPEDIDO = ").append(Sql.getValue(itemPedido.nuPedido)).append(" AND ")
		.append("SOLAUX.FLORIGEMPEDIDO = ").append(Sql.getValue(itemPedido.flOrigemPedido)).append(" AND ")
		.append("SOLAUX.CDCLIENTE = ").append(Sql.getValue(itemPedido.pedido.cdCliente)).append(" AND ")
		.append("SOLAUX.FLEXCLUIDO <> 'S' GROUP BY SOLAUX.CDPRODUTO) ")
		.append("SOLAUTORIZACAO ON SOLAUTORIZACAO.CDPRODUTO = ITEMAUX.CDPRODUTO ")
		.append("WHERE SOLAUTORIZACAO.CDSOLAUTORIZACAO IS NULL OR SOLAUTORIZACAO.FLAUTORIZADO = 'S'")
		.append(") ITEMPED ON ITEMPED.CDEMPRESA = PROD.CDEMPRESA ")
		.append("AND ITEMPED.CDREPRESENTANTE = ").append(Sql.getValue(itemPedido.cdRepresentante)).append(" AND ")
		.append("ITEMPED.CDPRODUTO = PROD.CDPRODUTO ").append(" AND ");
		if(LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
			sql.append("COALESCE(PROD.FLKIT, 'N') <> 'S'").append(" AND ");
		}
		sql.append("ITEMPED.NUPEDIDO = ").append(Sql.getValue(itemPedido.nuPedido)).append(") tb ");
	}
}
