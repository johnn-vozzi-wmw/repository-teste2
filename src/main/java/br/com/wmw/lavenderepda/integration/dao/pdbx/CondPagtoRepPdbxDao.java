package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.CondPagtoRep;
import totalcross.sql.ResultSet;

public class CondPagtoRepPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CondPagtoRep();
	}
	
	private static CondPagtoRepPdbxDao instance;

	public CondPagtoRepPdbxDao() {
		super(CondPagtoRep.TABLE_NAME);
	}

	public static CondPagtoRepPdbxDao getInstance() {
		if (instance == null) {
			instance = new CondPagtoRepPdbxDao();
		}
		return instance;
	}
	
	protected void addInsertColumns(StringBuffer sql) throws SQLException { }
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException { }
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException { }	

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		CondPagtoRep condPagtoRep = new CondPagtoRep();
		condPagtoRep.rowKey = rs.getString("rowKey");
		condPagtoRep.cdEmpresa = rs.getString("cdEmpresa");
		condPagtoRep.cdRepresentante = rs.getString("cdRepresentante");
		condPagtoRep.cdCondicaoPagamento = rs.getString("cdCondicaoPagamento");
		return condPagtoRep;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowKey,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" CDCONDICAOPAGAMENTO");
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		CondPagtoRep condPagtoRep = (CondPagtoRep) domain;
       	sql.append(" where CDEMPRESA = ").append(Sql.getValue(condPagtoRep.cdEmpresa));
       	if (!ValueUtil.isEmpty(condPagtoRep.cdRepresentante)) {
        	sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(condPagtoRep.cdRepresentante));
        }
        if (!ValueUtil.isEmpty(condPagtoRep.cdCondicaoPagamento)) {
        	sql.append(" and CDCONDICAOPAGAMENTO = ").append(Sql.getValue(condPagtoRep.cdCondicaoPagamento));
        }
	}
}