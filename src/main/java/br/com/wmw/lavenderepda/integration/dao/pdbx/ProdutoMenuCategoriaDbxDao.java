package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ProdutoMenuCategoria;
import totalcross.sql.ResultSet;

public class ProdutoMenuCategoriaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProdutoMenuCategoria();
	}


	private static ProdutoMenuCategoriaDbxDao instance;


	public static ProdutoMenuCategoriaDbxDao getInstance() {
		if (instance == null) {
			instance = new ProdutoMenuCategoriaDbxDao();
		}
		return instance;
	}

	public ProdutoMenuCategoriaDbxDao() {
		super(ProdutoMenuCategoria.TABLE_NAME);
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet resultSet) throws SQLException {
		ProdutoMenuCategoria produtoMenuCategoria = new ProdutoMenuCategoria();
		produtoMenuCategoria.rowKey = resultSet.getString("rowkey");
		produtoMenuCategoria.cdEmpresa = resultSet.getString("cdEmpresa");
		produtoMenuCategoria.cdRepresentante = resultSet.getString("cdRepresentante");
		produtoMenuCategoria.cdProduto = resultSet.getString("cdProduto");
		produtoMenuCategoria.cdMenu = resultSet.getString("cdMenu");
		produtoMenuCategoria.cdUsuario = resultSet.getString("cdUsuario");
		produtoMenuCategoria.dtAlteracao = resultSet.getString("dtAlteracao");
		produtoMenuCategoria.hrAlteracao = resultSet.getString("hrAlteracao");
		return produtoMenuCategoria;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		getColumns(sql);
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		getColumns(sql);
	}

	private void getColumns(StringBuffer sql) {
		sql.append(" rowkey,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" CDMENU,");
		sql.append(" CDPRODUTO,");
		sql.append(" CDUSUARIO,");
		sql.append(" NUCARIMBO,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" HRALTERACAO,");
		sql.append(" DTALTERACAO");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		ProdutoMenuCategoria produtoMenuCategoria = (ProdutoMenuCategoria) domain;
		sql.append(Sql.getValue(produtoMenuCategoria.cdEmpresa)).append(",");
		sql.append(Sql.getValue(produtoMenuCategoria.cdRepresentante)).append(",");
		sql.append(Sql.getValue(produtoMenuCategoria.cdMenu)).append(",");
		sql.append(Sql.getValue(produtoMenuCategoria.cdProduto)).append(",");
		sql.append(Sql.getValue(produtoMenuCategoria.flTipoAlteracao)).append(",");
		sql.append(Sql.getValue(produtoMenuCategoria.cdUsuario)).append(",");
		sql.append(Sql.getValue(produtoMenuCategoria.nuCarimbo)).append(",");
		sql.append(Sql.getValue(produtoMenuCategoria.dtAlteracao)).append(",");
		sql.append(Sql.getValue(produtoMenuCategoria.hrAlteracao));
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		/**/
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		ProdutoMenuCategoria produtoMenuCategoria = (ProdutoMenuCategoria) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", produtoMenuCategoria.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", produtoMenuCategoria.cdRepresentante);
		sqlWhereClause.addAndCondition("CDMENU = ", produtoMenuCategoria.cdMenu);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", produtoMenuCategoria.cdProduto);
		sql.append(sqlWhereClause.getSql());
	}
}
