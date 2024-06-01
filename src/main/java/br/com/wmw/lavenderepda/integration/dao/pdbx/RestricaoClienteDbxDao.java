package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.RestricaoCliente;
import totalcross.sql.ResultSet;

public class RestricaoClienteDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RestricaoCliente();
	}

	private static RestricaoClienteDbxDao instance;

	public static RestricaoClienteDbxDao getInstance() { return instance == null ? instance = new RestricaoClienteDbxDao() : instance; }

	public RestricaoClienteDbxDao() { super(RestricaoCliente.TABLE_NAME); }

	@Override protected void addInsertColumns(StringBuffer stringBuffer) throws SQLException {}
	@Override protected void addInsertValues(BaseDomain baseDomain, StringBuffer stringBuffer) throws SQLException {}
	@Override protected void addUpdateValues(BaseDomain baseDomain, StringBuffer stringBuffer) throws SQLException {}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet resultSet) throws SQLException {
		RestricaoCliente restricaoCliente = new RestricaoCliente();
		restricaoCliente.rowKey = resultSet.getString("rowkey");
		restricaoCliente.cdEmpresa = resultSet.getString("cdEmpresa");
		restricaoCliente.cdRepresentante = resultSet.getString("cdRepresentante");
		restricaoCliente.cdRestricao = resultSet.getString("cdRestricao");
		restricaoCliente.cdCliente = resultSet.getString("cdCliente");
		restricaoCliente.cdUsuario = resultSet.getString("cdUsuario");
		restricaoCliente.flTipoAlteracao = resultSet.getString("flTipoAlteracao");
		return restricaoCliente;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowkey,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" CDRESTRICAO,");
		sql.append(" CDCLIENTE,");
		sql.append(" CDUSUARIO,");
		sql.append(" FLTIPOALTERACAO");
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		RestricaoCliente restricaoCliente = (RestricaoCliente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", restricaoCliente.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", restricaoCliente.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDRESTRICAO = ", restricaoCliente.cdRestricao);
		sqlWhereClause.addAndCondition("tb.CDCLIENTE = ", restricaoCliente.cdCliente);
		sql.append(sqlWhereClause.getSql());
	}

}