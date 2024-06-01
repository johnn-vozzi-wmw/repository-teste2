package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.CampanhaPublicitaria;
import br.com.wmw.lavenderepda.business.domain.FreteConfig;
import br.com.wmw.lavenderepda.business.domain.Transportadora;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

import java.sql.SQLException;

public class CampanhaPublicitariaPdbxDao extends LavendereCrudDbxDao {

	private static CampanhaPublicitariaPdbxDao instance;

	public CampanhaPublicitariaPdbxDao() {
		super(CampanhaPublicitaria.TABLE_NAME);
	}

	public static CampanhaPublicitariaPdbxDao getInstance() {
		if (instance == null) instance = new CampanhaPublicitariaPdbxDao();
		return instance;
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CampanhaPublicitaria();
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		CampanhaPublicitaria campanhaPublicitaria = new CampanhaPublicitaria();
		campanhaPublicitaria.rowKey = rs.getString("rowkey");
		campanhaPublicitaria.cdEmpresa = rs.getString("cdEmpresa");
		campanhaPublicitaria.cdRepresentante = rs.getString("cdRepresentante");
		campanhaPublicitaria.cdCampanhaPublicitaria = rs.getString("cdCampanhaPublicitaria");
		campanhaPublicitaria.cdCliente = rs.getString("cdCliente");
		campanhaPublicitaria.dsCampanhaPublicitaria = rs.getString("dsCampanhaPublicitaria");
		return campanhaPublicitaria;
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		CampanhaPublicitaria campanhaPublicitaria = (CampanhaPublicitaria) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", campanhaPublicitaria.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", campanhaPublicitaria.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", campanhaPublicitaria.cdCliente);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowKey,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" CDCAMPANHAPUBLICITARIA,");
		sql.append(" CDCLIENTE,");
		sql.append(" DSCAMPANHAPUBLICITARIA");
	}

	public Vector findAllByExample(CampanhaPublicitaria filter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" select ");
		addSelectColumns(filter, sql);
		sql.append(" from ");
		sql.append(tableName);
		sql.append(" tb ");
		addWhereByExample(filter, sql);
		return findAll(filter, sql.toString());
	}

	public BaseDomain findByRowKey(String rowKey) throws java.sql.SQLException {
		return super.findByRowKey(rowKey);
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

}
