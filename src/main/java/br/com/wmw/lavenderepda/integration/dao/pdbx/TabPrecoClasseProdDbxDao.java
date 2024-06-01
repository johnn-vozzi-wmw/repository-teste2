package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TabPrecoClasseProd;
import totalcross.sql.ResultSet;

public class TabPrecoClasseProdDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TabPrecoClasseProd();
	}

	public static TabPrecoClasseProdDbxDao instance;
	
	public TabPrecoClasseProdDbxDao() {
		super(TabPrecoClasseProd.TABLE_NAME);
	}
	
	public static TabPrecoClasseProdDbxDao getInstance() {
		if (instance == null) {
			instance = new TabPrecoClasseProdDbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		TabPrecoClasseProd tabPrecoClasseProd = new TabPrecoClasseProd();
		tabPrecoClasseProd.cdEmpresa = rs.getString("cdEmpresa");
		tabPrecoClasseProd.cdRepresentante = rs.getString("cdRepresentante");
		tabPrecoClasseProd.cdTabelaPreco = rs.getString("cdTabelaPreco");
		tabPrecoClasseProd.cdClasse = rs.getString("cdClasse");
		tabPrecoClasseProd.qtMinProduto = rs.getDouble("qtMinProduto");
		tabPrecoClasseProd.qtMinPedido = rs.getDouble("qtMinPedido");
		return tabPrecoClasseProd;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.rowkey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDTABELAPRECO,");
		sql.append(" tb.CDCLASSE,");
		sql.append(" tb.QTMINPRODUTO,");
		sql.append(" tb.QTMINPEDIDO");
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
		TabPrecoClasseProd tabPrecoClasseProd = (TabPrecoClasseProd) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", tabPrecoClasseProd.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", tabPrecoClasseProd.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDTABELAPRECO = ", tabPrecoClasseProd.cdTabelaPreco);
		sqlWhereClause.addAndCondition("tb.CDCLASSE = ", tabPrecoClasseProd.cdClasse);
		sql.append(sqlWhereClause.getSql());
	}

	public double findTabPrecoClasseProdComMaiorMinimoByClasse(Pedido pedido, String cdClasse) throws SQLException {
		StringBuilder sql = getSqlBuilder();
		sql.append("SELECT MAX(QTMINPEDIDO) as QTMINPEDIDO FROM ").append(tableName).append(" tb");
		sql.append(" WHERE  CDTABELAPRECO in (");
		sql.append(" SELECT distinct itemPed.CDTABELAPRECO  FROM TBLVPITEMPEDIDO itemPed ");
		sql.append(" join TBLVPPRODUTO prod on itemPed.CDEMPRESA = prod.CDEMPRESA and itemPed.CDREPRESENTANTE = prod.CDREPRESENTANTE and itemPed.CDPRODUTO = prod.CDPRODUTO");
		sql.append(" WHERE itemPed.CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa));
		sql.append(" AND itemPed.CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante));
		sql.append(" AND itemPed.NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido));
		sql.append(" AND prod.CDCLASSE = ").append(Sql.getValue(cdClasse));
		sql.append(" )");
		sql.append(" AND tb.CDCLASSE = ").append(Sql.getValue(cdClasse));
		return getDouble(sql.toString());
	}

}
