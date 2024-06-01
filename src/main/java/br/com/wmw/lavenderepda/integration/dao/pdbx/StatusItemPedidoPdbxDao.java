package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.StatusItemPedido;
import totalcross.sql.ResultSet;

import java.sql.SQLException;

public class StatusItemPedidoPdbxDao extends LavendereCrudDbxDao {

	private static StatusItemPedidoPdbxDao instance;

	public StatusItemPedidoPdbxDao() {
		super(StatusItemPedido.TABLE_NAME);
	}

	public static StatusItemPedidoPdbxDao getInstance() {
		if (instance == null) {
			instance = new StatusItemPedidoPdbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		StatusItemPedido statusItemPedido = new StatusItemPedido();
		statusItemPedido.rowKey = rs.getString("rowkey");
		statusItemPedido.cdEmpresa = rs.getString("cdEmpresa");
		statusItemPedido.cdStatusItemPedido = rs.getString("cdStatusItemPedido");
		statusItemPedido.dsStatusItemPedido = rs.getString("dsStatusItemPedido");
		statusItemPedido.cdStatusItemPedidoErp = rs.getString("cdStatusItemPedidoErp");
		return statusItemPedido;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" ROWKEY,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDSTATUSITEMPEDIDO,");
		sql.append(" DSSTATUSITEMPEDIDO,");
		sql.append(" CDSTATUSITEMPEDIDOERP");
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
		StatusItemPedido statusItemPedido = (StatusItemPedido) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", statusItemPedido.cdEmpresa);
		sqlWhereClause.addAndCondition("CDSTATUSITEMPEDIDO = ", statusItemPedido.cdStatusItemPedido);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new StatusItemPedido();
	}

}
