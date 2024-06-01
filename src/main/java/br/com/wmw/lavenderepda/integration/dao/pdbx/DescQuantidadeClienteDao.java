package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.DescQuantidadeCliente;
import totalcross.sql.ResultSet;

public class DescQuantidadeClienteDao extends CrudDbxDao {
	
	private static DescQuantidadeClienteDao instance;

	public DescQuantidadeClienteDao() {
		super(DescQuantidadeCliente.TABLE_NAME);
	}
	
	public static DescQuantidadeClienteDao getInstance() {
		if (instance == null) {
			instance = new DescQuantidadeClienteDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domain, ResultSet rs) throws SQLException {
		DescQuantidadeCliente descQuantidadeCliente = new DescQuantidadeCliente();
		descQuantidadeCliente.rowKey = rs.getString("rowKey");
		descQuantidadeCliente.cdEmpresa = rs.getString("cdEmpresa");
		descQuantidadeCliente.cdRepresentante = rs.getString("cdRepresentante");
		descQuantidadeCliente.cdTabelaPreco = rs.getString("cdTabelaPreco");
		descQuantidadeCliente.cdProduto = rs.getString("cdProduto");
		descQuantidadeCliente.cdCliente = rs.getString("cdCliente");
		return descQuantidadeCliente;
	}

	@Override
	public void addSelectColumns(BaseDomain domain, StringBuffer sql) throws SQLException {
		sql.append("rowkey,")
		.append("CDEMPRESA,")
		.append("CDREPRESENTANTE,")
		.append("CDTABELAPRECO,")
		.append("CDPRODUTO,")
		.append("CDCLIENTE");
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
		DescQuantidadeCliente descQuantidadeCliente = (DescQuantidadeCliente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", descQuantidadeCliente.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", descQuantidadeCliente.cdRepresentante);
		sqlWhereClause.addAndCondition("CDTABELAPRECO = ", descQuantidadeCliente.cdTabelaPreco);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", descQuantidadeCliente.cdProduto);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", descQuantidadeCliente.cdCliente);
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescQuantidadeCliente();
	}

}
