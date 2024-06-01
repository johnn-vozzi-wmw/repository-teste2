package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.HistoricoTrocaDevolucaoCli;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class HistoricoTrocaDevolucaoCliPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new HistoricoTrocaDevolucaoCli();
	}

	private static HistoricoTrocaDevolucaoCliPdbxDao instance;

	public HistoricoTrocaDevolucaoCliPdbxDao() {
		super(HistoricoTrocaDevolucaoCli.TABLE_NAME);
	}

	public HistoricoTrocaDevolucaoCliPdbxDao(String newTableName) {
		super(newTableName);
	}

	public static HistoricoTrocaDevolucaoCliPdbxDao getInstance() {
		if (HistoricoTrocaDevolucaoCliPdbxDao.instance == null) {
			return new HistoricoTrocaDevolucaoCliPdbxDao(HistoricoTrocaDevolucaoCli.TABLE_NAME);
		}
		return HistoricoTrocaDevolucaoCliPdbxDao.instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		HistoricoTrocaDevolucaoCli historicoTrocaDevolucaoCli = new HistoricoTrocaDevolucaoCli();
		historicoTrocaDevolucaoCli.rowKey = rs.getString(1);
		historicoTrocaDevolucaoCli.cdEmpresa = rs.getString(2);
		historicoTrocaDevolucaoCli.cdRepresentante = rs.getString(3);
		historicoTrocaDevolucaoCli.cdProduto = rs.getString(4);
		historicoTrocaDevolucaoCli.cdCliente = rs.getString(5);
		historicoTrocaDevolucaoCli.dsProduto = rs.getString(6);
		historicoTrocaDevolucaoCli.percTrocaDevSeisMeses = rs.getDouble(7);
		historicoTrocaDevolucaoCli.percTrocaDevTresMeses = rs.getDouble(8);
		historicoTrocaDevolucaoCli.percTrocaDevTrintaDias = rs.getDouble(9);
		historicoTrocaDevolucaoCli.diasSemCompras = rs.getInt(10);
		historicoTrocaDevolucaoCli.flTotalCliente = rs.getString(11);
		return historicoTrocaDevolucaoCli;
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
		sql.append(" tb.DIASSEMCOMPRAS,");
		sql.append(" tb.FLTOTALCLIENTE");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException { }

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException { }

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException { }

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		HistoricoTrocaDevolucaoCli historicoTrocaDevolucaoCli = (HistoricoTrocaDevolucaoCli) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("TB.CDEMPRESA = ", historicoTrocaDevolucaoCli.cdEmpresa);
		sqlWhereClause.addAndCondition("TB.CDREPRESENTANTE = ", historicoTrocaDevolucaoCli.cdRepresentante);
		sqlWhereClause.addAndCondition("TB.CDCLIENTE = ", historicoTrocaDevolucaoCli.cdCliente);
		sql.append(sqlWhereClause.getSql());
	}

	public Vector findAllHistDevolucaoTrocaCli(HistoricoTrocaDevolucaoCli historicoTrocaDevolucaoCliFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT tb.CDEMPRESA, tb.CDREPRESENTANTE, tb.CDCLIENTE, tb.CDPRODUTO, tb.DSPRODUTO, tb.PERCTROCADEVSEISMESES, ")
				.append("tb.PERCTROCADEVTRESMESES, tb.PERCTROCADEVTRINTADIAS, tb.DIASSEMCOMPRAS, tb.ROWKEY, tb.FLTOTALCLIENTE");
		sql.append(" FROM ").append(tableName).append(" tb ");
		addWhereByExample(historicoTrocaDevolucaoCliFilter, sql);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector listHistDevTrocaCli = new Vector();
			HistoricoTrocaDevolucaoCli historicoTrocaDevolucaoCli;
			while (rs.next()) {
				historicoTrocaDevolucaoCli = new HistoricoTrocaDevolucaoCli();
				historicoTrocaDevolucaoCli.cdEmpresa = rs.getString("cdEmpresa");
				historicoTrocaDevolucaoCli.cdRepresentante = rs.getString("cdRepresentante");
				historicoTrocaDevolucaoCli.cdCliente = rs.getString("cdCliente");
				historicoTrocaDevolucaoCli.cdProduto = rs.getString("cdProduto");
				historicoTrocaDevolucaoCli.dsProduto = rs.getString("dsProduto");
				historicoTrocaDevolucaoCli.percTrocaDevSeisMeses = rs.getDouble("percTrocaDevSeisMeses");
				historicoTrocaDevolucaoCli.percTrocaDevTresMeses = rs.getDouble("percTrocaDevTresMeses");
				historicoTrocaDevolucaoCli.percTrocaDevTrintaDias = rs.getDouble("percTrocaDevTrintaDias");
				historicoTrocaDevolucaoCli.diasSemCompras = rs.getInt("diasSemCompras");
				historicoTrocaDevolucaoCli.flTotalCliente = rs.getString("flTotalCliente");
				historicoTrocaDevolucaoCli.rowKey = rs.getString("rowKey");
				listHistDevTrocaCli.addElement(historicoTrocaDevolucaoCli);
			}
			return listHistDevTrocaCli;
		}
	}
}
