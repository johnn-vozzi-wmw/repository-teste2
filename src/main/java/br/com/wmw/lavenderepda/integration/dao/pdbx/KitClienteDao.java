package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.KitCliente;
import totalcross.sql.ResultSet;

public class KitClienteDao extends CrudDbxDao {
	
	private static KitClienteDao instance;

	public KitClienteDao() {
		super(KitCliente.TABLE_NAME);
	}
	
	public static KitClienteDao getInstance() {
		if (instance == null) {
			instance = new KitClienteDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domain, ResultSet rs) throws SQLException {
		KitCliente kitCliente = new KitCliente();
		kitCliente.rowKey = rs.getString("rowKey");
		kitCliente.cdEmpresa = rs.getString("cdEmpresa");
		kitCliente.cdRepresentante = rs.getString("cdRepresentante");
		kitCliente.cdKit = rs.getString("cdKit");
		kitCliente.cdTabelaPreco = rs.getString("cdTabelaPreco");
		kitCliente.cdCliente = rs.getString("cdCliente");
		return kitCliente;
	}

	@Override
	public void addSelectColumns(BaseDomain domain, StringBuffer sql) throws SQLException {
		sql.append("rowkey,")
		.append("CDEMPRESA,")
		.append("CDREPRESENTANTE,")
		.append("CDKIT,")
		.append("CDTABELAPRECO,")
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
		KitCliente kitCliente = (KitCliente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", kitCliente.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", kitCliente.cdRepresentante);
		sqlWhereClause.addAndCondition("CDKIT = ", kitCliente.cdKit);
		sqlWhereClause.addAndCondition("CDTABELAPRECO = ", kitCliente.cdTabelaPreco);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", kitCliente.cdCliente);
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new KitCliente();
	}

}
