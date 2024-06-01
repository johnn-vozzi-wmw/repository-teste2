package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ComboCliente;
import totalcross.sql.ResultSet;

public class ComboClienteDao extends CrudDbxDao {
	
	private static ComboClienteDao instance;

	public ComboClienteDao() {
		super(ComboCliente.TABLE_NAME);
	}
	
	public static ComboClienteDao getInstance() {
		if (instance == null) {
			instance = new ComboClienteDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domain, ResultSet rs) throws SQLException {
		ComboCliente comboCliente = new ComboCliente();
		comboCliente.rowKey = rs.getString("rowKey");
		comboCliente.cdEmpresa = rs.getString("cdEmpresa");
		comboCliente.cdRepresentante = rs.getString("cdRepresentante");
		comboCliente.cdCombo = rs.getString("cdCombo");
		comboCliente.cdTabelaPreco = rs.getString("cdTabelaPreco");
		comboCliente.cdCliente = rs.getString("cdCliente");
		return comboCliente;
	}

	@Override
	public void addSelectColumns(BaseDomain domain, StringBuffer sql) throws SQLException {
		sql.append("rowkey,")
		.append("CDEMPRESA,")
		.append("CDREPRESENTANTE,")
		.append("CDCOMBO,")
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
		ComboCliente comboCliente = (ComboCliente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", comboCliente.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", comboCliente.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCOMBO = ", comboCliente.cdCombo);
		sqlWhereClause.addAndCondition("CDTABELAPRECO = ", comboCliente.cdTabelaPreco);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", comboCliente.cdCliente);
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ComboCliente();
	}

}
