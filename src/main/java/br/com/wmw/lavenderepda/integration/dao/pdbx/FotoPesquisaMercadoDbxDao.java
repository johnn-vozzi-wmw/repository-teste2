package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.FotoPesquisaMercado;
import totalcross.sql.ResultSet;

public class FotoPesquisaMercadoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FotoPesquisaMercado();
	}

	private static FotoPesquisaMercadoDbxDao instance = null;

	public static FotoPesquisaMercadoDbxDao getInstance() {
		if (instance == null) {
			instance = new FotoPesquisaMercadoDbxDao();
		}
		return instance;
	}

	public FotoPesquisaMercadoDbxDao() {
		super(FotoPesquisaMercado.TABLE_NAME);
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		FotoPesquisaMercado fotoPesquisaMercado = new FotoPesquisaMercado();
		fotoPesquisaMercado.rowKey = rs.getString("rowKey");
		fotoPesquisaMercado.cdEmpresa = rs.getString("cdEmpresa");
		fotoPesquisaMercado.cdRepresentante = rs.getString("cdRepresentante");
		fotoPesquisaMercado.flOrigemPesquisaMercado = rs.getString("flOrigemPesquisaMercado");
		fotoPesquisaMercado.cdPesquisaMercado = rs.getString("cdPesquisaMercado");
		fotoPesquisaMercado.nmFoto = rs.getString("nmFoto");
		fotoPesquisaMercado.cdFoto = rs.getInt("cdFoto");
		fotoPesquisaMercado.flEnviadoServidor = rs.getString("flEnviadoServidor");
		fotoPesquisaMercado.cdUsuario = rs.getString("cdUsuario");
		fotoPesquisaMercado.flTipoAlteracao = rs.getString("flTipoAlteracao");
		fotoPesquisaMercado.nuCarimbo = rs.getInt("nuCarimbo");
		return fotoPesquisaMercado;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowkey,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" FLORIGEMPESQUISAMERCADO,");
		sql.append(" CDPESQUISAMERCADO,");
		sql.append(" NMFOTO,");
		sql.append(" CDFOTO,");
		sql.append(" FLENVIADOSERVIDOR,");
		sql.append(" CDUSUARIO,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" NUCARIMBO");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" FLORIGEMPESQUISAMERCADO,");
		sql.append(" CDPESQUISAMERCADO,");
		sql.append(" NMFOTO,");
		sql.append(" CDFOTO,");
		sql.append(" FLENVIADOSERVIDOR,");
		sql.append(" CDUSUARIO,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" NUCARIMBO");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		FotoPesquisaMercado fotoPesquisaMercado = (FotoPesquisaMercado) domain;
		sql.append(Sql.getValue(fotoPesquisaMercado.cdEmpresa)).append(",");
		sql.append(Sql.getValue(fotoPesquisaMercado.cdRepresentante)).append(",");
		sql.append(Sql.getValue(fotoPesquisaMercado.flOrigemPesquisaMercado)).append(",");
		sql.append(Sql.getValue(fotoPesquisaMercado.cdPesquisaMercado)).append(",");
		sql.append(Sql.getValue(fotoPesquisaMercado.nmFoto)).append(",");
		sql.append(Sql.getValue(fotoPesquisaMercado.cdFoto)).append(",");
		sql.append(Sql.getValue(fotoPesquisaMercado.flEnviadoServidor)).append(",");
		sql.append(Sql.getValue(fotoPesquisaMercado.cdUsuario)).append(",");
		sql.append(Sql.getValue(fotoPesquisaMercado.flTipoAlteracao)).append(",");
		sql.append(Sql.getValue(fotoPesquisaMercado.nuCarimbo));
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		FotoPesquisaMercado fotoPesquisaMercado = (FotoPesquisaMercado) domain;
		sql.append(" CDUSUARIO = ").append(Sql.getValue(fotoPesquisaMercado.cdUsuario)).append(",");
		sql.append(" FLENVIADOSERVIDOR = ").append(Sql.getValue(fotoPesquisaMercado.flEnviadoServidor)).append(",");
		sql.append(" FLORIGEMPESQUISAMERCADO = ").append(Sql.getValue(fotoPesquisaMercado.flOrigemPesquisaMercado)).append(",");
		sql.append(" NMFOTO = ").append(Sql.getValue(fotoPesquisaMercado.nmFoto)).append(",");
		sql.append(" CDPESQUISAMERCADO = ").append(Sql.getValue(fotoPesquisaMercado.cdPesquisaMercado)).append(",");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(fotoPesquisaMercado.flTipoAlteracao)).append(",");
		sql.append(" NUCARIMBO = ").append(Sql.getValue(fotoPesquisaMercado.nuCarimbo));
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		FotoPesquisaMercado fotoPesquisaMercado = (FotoPesquisaMercado) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", fotoPesquisaMercado.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", fotoPesquisaMercado.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPESQUISAMERCADO = ", fotoPesquisaMercado.cdPesquisaMercado);
		sqlWhereClause.addAndCondition("FLORIGEMPESQUISAMERCADO = ", fotoPesquisaMercado.flOrigemPesquisaMercado);
		sqlWhereClause.addAndCondition("NMFOTO = ", fotoPesquisaMercado.nmFoto); 
		// --
		sql.append(sqlWhereClause.getSql());
	}

}
