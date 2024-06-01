package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.SugVendaPerson;
import br.com.wmw.lavenderepda.business.domain.SugVendaPersonCli;
import totalcross.sql.ResultSet;

public class SugVendaPersonCliDbxDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new SugVendaPersonCli();
	}

	private static SugVendaPersonCliDbxDao instance;
	
	public static SugVendaPersonCliDbxDao getInstance() {
		if (instance == null) {
			instance = new SugVendaPersonCliDbxDao();
		}
		return instance;
	}

	public SugVendaPersonCliDbxDao() {
		super(SugVendaPersonCli.TABLE_NAME);
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		SugVendaPerson sugVendaPerson = new SugVendaPerson();
		sugVendaPerson.cdSugVendaPerson = rs.getString(1);
		sugVendaPerson.dsSugVendaPerson = rs.getString(2);
		sugVendaPerson.dsSugVendaPersonAbrev = rs.getString(3);
		sugVendaPerson.imSugVendaPerson = rs.getBytes(4);
		return sugVendaPerson;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append("sug.CDSUGVENDAPERSON,")
		.append("sug.DSSUGVENDAPERSON,")
		.append("sug.DSSUGVENDAPERSONABREV,")
		.append("sug.IMSUGVENDAPERSON");
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
		SugVendaPersonCli sugVendaPersonCli = (SugVendaPersonCli) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals("tb.CDCLIENTE", sugVendaPersonCli.cdCliente);
		sqlWhereClause.addAndConditionEquals("tb.CDEMPRESA", sugVendaPersonCli.cdEmpresa);
		sql.append(sqlWhereClause.getSql());
	}
	
	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		sql.append(" JOIN TBLVPSUGVENDAPERSON sug ON sug.CDSUGVENDAPERSON = tb.CDSUGVENDAPERSON");
	}

}
