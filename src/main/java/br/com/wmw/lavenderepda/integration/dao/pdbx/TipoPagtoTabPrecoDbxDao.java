package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.TipoPagtoTabPreco;
import totalcross.sql.ResultSet;

public class TipoPagtoTabPrecoDbxDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TipoPagtoTabPreco();
	}

	public static TipoPagtoTabPrecoDbxDao instance;
	
	public static TipoPagtoTabPrecoDbxDao getInstance() {
		if (instance == null) {
			instance = new TipoPagtoTabPrecoDbxDao();
		}
		return instance;
	}

	public TipoPagtoTabPrecoDbxDao() {
		super(TipoPagtoTabPreco.TABLE_NAME);
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		TipoPagtoTabPreco tipoPagtoTabPreco = new TipoPagtoTabPreco();
		tipoPagtoTabPreco.cdEmpresa = rs.getString("cdEmpresa");
		tipoPagtoTabPreco.cdRepresentante = rs.getString("cdRepresentante");
		tipoPagtoTabPreco.cdTabelaPreco = rs.getString("cdTabelaPreco");
		tipoPagtoTabPreco.cdTipoPagamento = rs.getString("cdTipoPagamento");
		return tipoPagtoTabPreco;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" CDTABELAPRECO,");
		sql.append(" CDTIPOPAGAMENTO");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		TipoPagtoTabPreco tipoPagtoTabPreco = (TipoPagtoTabPreco) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionForced(" CDEMPRESA = ", tipoPagtoTabPreco.cdEmpresa);
		sqlWhereClause.addAndConditionForced(" CDREPRESENTANTE = ", tipoPagtoTabPreco.cdRepresentante);
		sqlWhereClause.addAndConditionForced(" CDTABELAPRECO = ", tipoPagtoTabPreco.cdTabelaPreco);
		sql.append(sqlWhereClause.getSql());
	}

}
