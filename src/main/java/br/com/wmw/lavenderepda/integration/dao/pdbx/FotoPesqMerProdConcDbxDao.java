package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.FotoPesqMerProdConc;
import totalcross.sql.ResultSet;

public class FotoPesqMerProdConcDbxDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FotoPesqMerProdConc();
	}

	private static FotoPesqMerProdConcDbxDao instance;

	public FotoPesqMerProdConcDbxDao() {
		super(FotoPesqMerProdConc.TABLE_NAME);
	}

	public static FotoPesqMerProdConcDbxDao getInstance() {
		if (instance == null) {
			instance = new FotoPesqMerProdConcDbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		FotoPesqMerProdConc fotoPesqMerProdConc = new FotoPesqMerProdConc();
		fotoPesqMerProdConc.rowKey = rs.getString("rowkey");
		fotoPesqMerProdConc.cdEmpresa = rs.getString("cdEmpresa");
		fotoPesqMerProdConc.cdRepresentante = rs.getString("cdRepresentante");
		fotoPesqMerProdConc.cdPesquisaMercadoConfig = rs.getString("cdPesquisaMercadoConfig");
		fotoPesqMerProdConc.nmFoto = rs.getString("nmFoto");
		fotoPesqMerProdConc.cdProduto = rs.getString("cdProduto");
		fotoPesqMerProdConc.cdCliente = rs.getString("cdCliente");
		fotoPesqMerProdConc.cdFoto = rs.getInt("cdFoto");
		fotoPesqMerProdConc.flEnviadoServidor = rs.getString("flEnviadoServidor");
		return fotoPesqMerProdConc;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
		getColumns(sql);
		sql.append(" , rowkey");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) {
		getColumns(sql);
	}

	private void getColumns(StringBuffer sql) {
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" CDPESQUISAMERCADOCONFIG,");
		sql.append(" NMFOTO,");
		sql.append(" CDPRODUTO,");
		sql.append(" CDCLIENTE,");
		sql.append(" CDFOTO,");
		sql.append(" FLENVIADOSERVIDOR,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" NUCARIMBO,");
		sql.append(" CDUSUARIO");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		FotoPesqMerProdConc fotoPesqMerProdConc = (FotoPesqMerProdConc) domain;
		sql.append(Sql.getValue(fotoPesqMerProdConc.cdEmpresa)).append(",");
		sql.append(Sql.getValue(fotoPesqMerProdConc.cdRepresentante)).append(",");
		sql.append(Sql.getValue(fotoPesqMerProdConc.cdPesquisaMercadoConfig)).append(",");
		sql.append(Sql.getValue(fotoPesqMerProdConc.nmFoto)).append(",");
		sql.append(Sql.getValue(fotoPesqMerProdConc.cdProduto)).append(",");
		sql.append(Sql.getValue(fotoPesqMerProdConc.cdCliente)).append(",");
		sql.append(Sql.getValue(fotoPesqMerProdConc.cdFoto)).append(",");
		sql.append(Sql.getValue(fotoPesqMerProdConc.flEnviadoServidor)).append(",");
		sql.append(Sql.getValue(fotoPesqMerProdConc.flTipoAlteracao)).append(",");
		sql.append(Sql.getValue(fotoPesqMerProdConc.nuCarimbo)).append(",");
		sql.append(Sql.getValue(fotoPesqMerProdConc.cdUsuario));
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		FotoPesqMerProdConc fotoPesqMerProdConc = (FotoPesqMerProdConc) domain;
		sql.append(" FLENVIADOSERVIDOR = ").append(Sql.getValue(fotoPesqMerProdConc.flEnviadoServidor)).append(",");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(fotoPesqMerProdConc.flTipoAlteracao));
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		FotoPesqMerProdConc fotoPesqMerProdConc = (FotoPesqMerProdConc) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", fotoPesqMerProdConc.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", fotoPesqMerProdConc.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDPESQUISAMERCADOCONFIG = ", fotoPesqMerProdConc.cdPesquisaMercadoConfig);
		sqlWhereClause.addAndCondition("tb.NMFOTO = ", fotoPesqMerProdConc.nmFoto);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", fotoPesqMerProdConc.cdProduto);
		sqlWhereClause.addAndCondition("tb.CDCLIENTE = ", fotoPesqMerProdConc.cdCliente);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected void addWhereDeleteByExample(BaseDomain domain, StringBuffer sql) {
		FotoPesqMerProdConc fotoPesqMerProdConc = (FotoPesqMerProdConc) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" CDEMPRESA = ", fotoPesqMerProdConc.cdEmpresa);
		sqlWhereClause.addAndCondition(" CDREPRESENTANTE = ", fotoPesqMerProdConc.cdRepresentante);
		sqlWhereClause.addAndCondition(" CDPESQUISAMERCADOCONFIG = ", fotoPesqMerProdConc.cdPesquisaMercadoConfig);
		sqlWhereClause.addAndCondition(" NMFOTO = ", fotoPesqMerProdConc.nmFoto);
		sqlWhereClause.addAndCondition(" CDPRODUTO = ", fotoPesqMerProdConc.cdProduto);
		sqlWhereClause.addAndCondition(" CDCLIENTE = ", fotoPesqMerProdConc.cdCliente);
		sql.append(sqlWhereClause.getSql());
	}

	public void updateFlTipoAlteracaoByExample(FotoPesqMerProdConc fotoPesqMerProdConc) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("update ");
		sql.append(FotoPesqMerProdConc.TABLE_NAME);
		sql.append(" set FLTIPOALTERACAO = ").append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ALTERADO));
		addWhereDeleteByExample(fotoPesqMerProdConc, sql);
		try {
			executeUpdate(sql.toString());
		} catch (ApplicationException e) {
			//nenhum registro afetado
		}
	}

	public void updateFlEnviadoServidor(FotoPesqMerProdConc fotoPesqMerProdConc, String flEnviadoServidor) {
		StringBuffer sql = getSqlBuffer();
		sql.append("update ");
		sql.append(FotoPesqMerProdConc.TABLE_NAME);
		sql.append(" set FLENVIADOSERVIDOR = ").append(Sql.getValue(flEnviadoServidor));
		addWhereDeleteByExample(fotoPesqMerProdConc, sql);
		try {
			executeUpdate(sql.toString());
		} catch (ApplicationException | SQLException e) {
			//nenhum registro afetado
		}
	}
}
