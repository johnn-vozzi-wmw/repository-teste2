package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.HistoricoTrocaDevolucaoProd;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;

public class HistoricoTrocaDevolucaoProdPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new HistoricoTrocaDevolucaoProd();
	}

	private static HistoricoTrocaDevolucaoProdPdbxDao instance;

	public HistoricoTrocaDevolucaoProdPdbxDao() {
		super(HistoricoTrocaDevolucaoProd.TABLE_NAME);
	}

	public HistoricoTrocaDevolucaoProdPdbxDao(String newTableName) {
		super(newTableName);
	}

	public static HistoricoTrocaDevolucaoProdPdbxDao getInstance() {
		if (HistoricoTrocaDevolucaoProdPdbxDao.instance == null) {
			return new HistoricoTrocaDevolucaoProdPdbxDao(HistoricoTrocaDevolucaoProd.TABLE_NAME);
		}
		return HistoricoTrocaDevolucaoProdPdbxDao.instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		HistoricoTrocaDevolucaoProd historicoTrocaDevolucaoProd = new HistoricoTrocaDevolucaoProd();
		historicoTrocaDevolucaoProd.rowKey = rs.getString(1);
		historicoTrocaDevolucaoProd.cdEmpresa = rs.getString(2);
		historicoTrocaDevolucaoProd.cdRepresentante = rs.getString(3);
		historicoTrocaDevolucaoProd.cdProduto = rs.getString(4);
		historicoTrocaDevolucaoProd.cdCliente = rs.getString(5);
		historicoTrocaDevolucaoProd.dsProduto = rs.getString(6);
		historicoTrocaDevolucaoProd.percTrocaDevSeisMeses = rs.getDouble(7);
		historicoTrocaDevolucaoProd.percTrocaDevTresMeses = rs.getDouble(8);
		historicoTrocaDevolucaoProd.percTrocaDevTrintaDias = rs.getDouble(9);
		historicoTrocaDevolucaoProd.qtVendaSeisMeses = rs.getString(10);
		historicoTrocaDevolucaoProd.qtVendaTresMeses = rs.getString(11);
		historicoTrocaDevolucaoProd.qtVendaTrintaDias = rs.getString(12);
		historicoTrocaDevolucaoProd.diasSemCompras = rs.getInt(13);
		return historicoTrocaDevolucaoProd;
	}


	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.rowkey, ");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDPRODUTO,");
		sql.append(" tb.CDCLIENTE,");
		sql.append(" tb.DSPRODUTO,");
		sql.append(" tb.PERCTROCADEVSEISMESES,");
		sql.append(" tb.PERCTROCADEVTRESMESES,");
		sql.append(" tb.PERCTROCADEVTRINTADIAS,");
		sql.append(" tb.QTVENDASEISMESES,");
		sql.append(" tb.QTVENDATRESMESES,");
		sql.append(" tb.QTVENDATRINTADIAS,");
		sql.append(" tb.DIASSEMCOMPRAS");
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
		HistoricoTrocaDevolucaoProd historicoTrocaDevolucaoProd = (HistoricoTrocaDevolucaoProd) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("TB.CDEMPRESA = ", historicoTrocaDevolucaoProd.cdEmpresa);
		sqlWhereClause.addAndCondition("TB.CDREPRESENTANTE = ", historicoTrocaDevolucaoProd.cdRepresentante);
		sqlWhereClause.addAndCondition("TB.CDCLIENTE = ", historicoTrocaDevolucaoProd.cdCliente);
		sqlWhereClause.addAndCondition("TB.CDPRODUTO = ", historicoTrocaDevolucaoProd.cdProduto);
		sql.append(sqlWhereClause.getSql());
	}

	public HistoricoTrocaDevolucaoProd findByPrimaryKey(HistoricoTrocaDevolucaoProd historicoTrocaDevolucaoProdilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT tb.CDEMPRESA, tb.CDREPRESENTANTE, tb.CDCLIENTE, tb.CDPRODUTO, tb.DSPRODUTO, tb.PERCTROCADEVSEISMESES, ")
				.append("tb.PERCTROCADEVTRESMESES, tb.PERCTROCADEVTRINTADIAS, tb.QTVENDASEISMESES, tb.QTVENDATRESMESES, tb.QTVENDATRINTADIAS, tb.DIASSEMCOMPRAS, tb.ROWKEY");
		sql.append(" FROM ").append(tableName).append(" tb ");
		addWhereByExample(historicoTrocaDevolucaoProdilter, sql);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			HistoricoTrocaDevolucaoProd historicoTrocaDevolucaoProd = new HistoricoTrocaDevolucaoProd();
			if (rs.next()) {
				historicoTrocaDevolucaoProd.cdEmpresa = rs.getString("cdEmpresa");
				historicoTrocaDevolucaoProd.cdRepresentante = rs.getString("cdRepresentante");
				historicoTrocaDevolucaoProd.cdCliente = rs.getString("cdCliente");
				historicoTrocaDevolucaoProd.cdProduto = rs.getString("cdProduto");
				historicoTrocaDevolucaoProd.dsProduto = rs.getString("dsProduto");
				historicoTrocaDevolucaoProd.percTrocaDevSeisMeses = rs.getDouble("percTrocaDevSeisMeses");
				historicoTrocaDevolucaoProd.percTrocaDevTresMeses = rs.getDouble("percTrocaDevTresMeses");
				historicoTrocaDevolucaoProd.percTrocaDevTrintaDias = rs.getDouble("percTrocaDevTrintaDias");
				historicoTrocaDevolucaoProd.qtVendaSeisMeses = rs.getString("qtVendaSeisMeses");
				historicoTrocaDevolucaoProd.qtVendaTresMeses = rs.getString("qtVendaTresMeses");
				historicoTrocaDevolucaoProd.qtVendaTrintaDias = rs.getString("qtVendaTrintaDias");
				historicoTrocaDevolucaoProd.diasSemCompras = rs.getInt("diasSemCompras");
				historicoTrocaDevolucaoProd.possuiHistorico = true;
				historicoTrocaDevolucaoProd.rowKey = rs.getString("rowKey");
			}
			return historicoTrocaDevolucaoProd;
		}
	}
}
