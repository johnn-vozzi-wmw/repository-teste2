package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ProdutoClienteCodAtua;
import totalcross.sql.ResultSet;

public class ProdutoClienteCodAtuaDbxDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProdutoClienteCodAtua();
	}

	private static ProdutoClienteCodAtuaDbxDao instance;
	
	public static ProdutoClienteCodAtuaDbxDao getInstance() {
		if (instance == null) {
			instance = new ProdutoClienteCodAtuaDbxDao();
		}
		return instance;
	}

	public ProdutoClienteCodAtuaDbxDao() {
		super(ProdutoClienteCodAtua.TABLE_NAME);
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		ProdutoClienteCodAtua produtoClienteCodAtua = new ProdutoClienteCodAtua();
		produtoClienteCodAtua.cdEmpresa = rs.getString("cdEmpresa");
		produtoClienteCodAtua.cdRepresentante = rs.getString("cdRepresentante");
		produtoClienteCodAtua.cdProduto = rs.getString("cdProduto");
		produtoClienteCodAtua.cdCliente = rs.getString("cdCliente");
		produtoClienteCodAtua.cdProdutoClienteAtua = rs.getString("cdProdutoClienteAtua");
		produtoClienteCodAtua.cdProdutoCliente = rs.getString("cdProdutoCliente");
		produtoClienteCodAtua.flAcao = rs.getString("flAcao");
		produtoClienteCodAtua.flTipoAlteracao = rs.getString("flTipoAlteracao");
		return produtoClienteCodAtua;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDPRODUTOCLIENTEATUA,");
        sql.append(" CDPRODUTOCLIENTE,");
        sql.append(" FLACAO,");
        sql.append(" FLTIPOALTERACAO");
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		ProdutoClienteCodAtua produtoClienteCodAtua = (ProdutoClienteCodAtua) domain;
		sql.append(" CDPRODUTOCLIENTE = ").append(Sql.getValue(produtoClienteCodAtua.cdProdutoCliente)).append(",");
		sql.append(" FLACAO = ").append(Sql.getValue(produtoClienteCodAtua.flAcao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(produtoClienteCodAtua.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(produtoClienteCodAtua.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(produtoClienteCodAtua.flTipoAlteracao));
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		ProdutoClienteCodAtua produtoClienteCodAtua = (ProdutoClienteCodAtua) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" tb.CDEMPRESA = ", produtoClienteCodAtua.cdEmpresa);
		sqlWhereClause.addAndCondition(" tb.CDREPRESENTANTE = ", produtoClienteCodAtua.cdRepresentante);
		sqlWhereClause.addAndCondition(" tb.CDPRODUTO = ", produtoClienteCodAtua.cdProduto);
		sqlWhereClause.addAndCondition(" tb.CDCLIENTE = ", produtoClienteCodAtua.cdCliente);
		sqlWhereClause.addAndCondition(" tb.CDPRODUTOCLIENTEATUA = ", produtoClienteCodAtua.cdProdutoClienteAtua);
		sql.append(sqlWhereClause.getSql());
	}
	
	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDPRODUTOCLIENTEATUA,");
        sql.append(" CDPRODUTOCLIENTE,");
        sql.append(" FLACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
	}
	
	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		ProdutoClienteCodAtua produtoClienteCodAtua = (ProdutoClienteCodAtua) domain;
		sql.append(Sql.getValue(produtoClienteCodAtua.cdEmpresa)).append(",");
        sql.append(Sql.getValue(produtoClienteCodAtua.cdRepresentante)).append(",");
        sql.append(Sql.getValue(produtoClienteCodAtua.cdProduto)).append(",");
        sql.append(Sql.getValue(produtoClienteCodAtua.cdCliente)).append(",");
        sql.append(Sql.getValue(produtoClienteCodAtua.cdProdutoClienteAtua)).append(",");
        sql.append(Sql.getValue(produtoClienteCodAtua.cdProdutoCliente)).append(",");
        sql.append(Sql.getValue(produtoClienteCodAtua.flAcao)).append(",");
        sql.append(Sql.getValue(produtoClienteCodAtua.nuCarimbo)).append(",");
        sql.append(Sql.getValue(produtoClienteCodAtua.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(produtoClienteCodAtua.cdUsuario));
	}
}
