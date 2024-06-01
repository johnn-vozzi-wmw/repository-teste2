package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.StatusOrcamento;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class StatusOrcamentoDbxDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new StatusOrcamento();
	}

	private static StatusOrcamentoDbxDao instance;
	
	public StatusOrcamentoDbxDao() {
		super(StatusOrcamento.TABLE_NAME);
	}
	
	public static StatusOrcamentoDbxDao getInstance() {
		if (instance == null) {
			instance = new StatusOrcamentoDbxDao();
		}
		return instance;
	}
	
	@Override
	protected void addInsertColumns(StringBuffer arg0) throws SQLException {}

	@Override
	protected void addInsertValues(BaseDomain arg0, StringBuffer arg1) throws SQLException {}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" CDEMPRESA,")
		.append(" CDREPRESENTANTE,")
		.append(" CDSTATUSORCAMENTO,")
		.append(" DSSTATUSORCAMENTO,")
		.append(" FLSTATUSCANCELAMENTO,")
		.append(" FLSTATUSINICIAL,")
		.append(" FLPERMITEFECHARPEDIDO,")
		.append(" FLSTATUSPREORCAMENTO,")
		.append(" FLPERMITEALTERARCONDCOMERCIAL");
	}

	@Override
	protected void addUpdateValues(BaseDomain arg0, StringBuffer arg1) throws SQLException {}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		StatusOrcamento statusOrcamento = (StatusOrcamento) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals("CDEMPRESA", statusOrcamento.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("CDREPRESENTANTE", statusOrcamento.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("CDSTATUSORCAMENTO", statusOrcamento.cdStatusOrcamento);
		sqlWhereClause.addAndConditionEquals("FLSTATUSINICIAL", statusOrcamento.flStatusInicial);
		sqlWhereClause.addAndConditionEquals("FLPERMITEFECHARPEDIDO", statusOrcamento.flPermiteFecharPedido);
		sqlWhereClause.addAndConditionEquals("COALESCE(FLSTATUSPREORCAMENTO, 'N')", statusOrcamento.flStatusPreOrcamento);
		sqlWhereClause.addAndConditionEquals("COALESCE(FLSTATUSCANCELAMENTO, 'N')", statusOrcamento.flStatusCancelamento);
		sqlWhereClause.addAndConditionNotEquals("COALESCE(FLSTATUSPREORCAMENTO, 'N')", statusOrcamento.notFlStatusPreOrcamento);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		StatusOrcamento statusOrcamento = new StatusOrcamento();
		statusOrcamento.cdEmpresa = rs.getString("cdEmpresa");
		statusOrcamento.cdRepresentante = rs.getString("cdRepresentante");
		statusOrcamento.cdStatusOrcamento = rs.getString("cdStatusOrcamento");
		statusOrcamento.dsStatusOrcamento = rs.getString("dsStatusOrcamento");
		statusOrcamento.flStatusCancelamento = rs.getString("flStatusCancelamento");
		statusOrcamento.flStatusInicial = rs.getString("flStatusInicial");
		statusOrcamento.flPermiteFecharPedido = rs.getString("flPermiteFecharPedido");
		statusOrcamento.flStatusPreOrcamento = rs.getString("flStatusPreOrcamento");
		statusOrcamento.flPermiteAlterarCondComercial = rs.getString("flPermiteAlterarCondComercial");
		return statusOrcamento;
	}

}
