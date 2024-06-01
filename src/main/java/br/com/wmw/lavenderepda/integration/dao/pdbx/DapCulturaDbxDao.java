package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.DapCultura;
import totalcross.sql.ResultSet;

public class DapCulturaDbxDao extends CrudDbxDao {
	
	private static DapCulturaDbxDao instance;
	
	public static DapCulturaDbxDao getInstance() {
		if (instance == null) {
			instance = new DapCulturaDbxDao();
		}
		return instance;
	}

	public DapCulturaDbxDao() {
		super(DapCultura.TABLE_NAME);
	}

	@Override
	protected BaseDomain populate(BaseDomain domain, ResultSet rs) throws SQLException {
		DapCultura dapCultura = new DapCultura();
		dapCultura.rowKey = rs.getString("rowKey");
		dapCultura.cdEmpresa = rs.getString("cdEmpresa");
		dapCultura.cdCliente = rs.getString("cdCliente");
		dapCultura.cdSafra = rs.getString("cdSafra");
		dapCultura.cdDapMatricula = rs.getString("cdDapMatricula");
		dapCultura.cdDapCultura = rs.getString("cdDapCultura");
		dapCultura.dsDapCultura = rs.getString("dsDapCultura");
		dapCultura.qtArea = rs.getDouble("qtArea");
		return dapCultura;
	}

	@Override
	public void addSelectColumns(BaseDomain domain, StringBuffer sql) throws SQLException {
		sql.append(" rowKey,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDCLIENTE,");
		sql.append(" CDSAFRA,");
		sql.append(" CDDAPMATRICULA,");
		sql.append(" CDDAPCULTURA,");
		sql.append(" DSDAPCULTURA,");
		sql.append(" QTAREA");
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
		DapCultura dapCultura = (DapCultura) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals(" CDEMPRESA ", dapCultura.cdEmpresa);
		sqlWhereClause.addAndConditionEquals(" CDCLIENTE ", dapCultura.cdCliente);
		sqlWhereClause.addAndConditionEquals(" CDDAPMATRICULA ", dapCultura.cdDapMatricula);
		sqlWhereClause.addAndConditionEquals(" CDSAFRA ", dapCultura.cdSafra);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DapCultura();
	}

}
