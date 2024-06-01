package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.TabPrecoGrupoProd;
import totalcross.sql.ResultSet;

public class TabPrecoGrupoProdDbxDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TabPrecoGrupoProd();
	}

	public static TabPrecoGrupoProdDbxDao instance;
	
	public TabPrecoGrupoProdDbxDao() {
		super(TabPrecoGrupoProd.TABLE_NAME);
	}
	
	public static TabPrecoGrupoProdDbxDao getInstance() {
		if (instance == null) {
			instance = new TabPrecoGrupoProdDbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		TabPrecoGrupoProd tabPrecoGrupoProd = new TabPrecoGrupoProd();
		tabPrecoGrupoProd.cdEmpresa = rs.getString("cdEmpresa");
		tabPrecoGrupoProd.cdRepresentante = rs.getString("cdRepresentante");
		tabPrecoGrupoProd.cdTabelaPreco = rs.getString("cdTabelaPreco");
		tabPrecoGrupoProd.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
		tabPrecoGrupoProd.qtMinProduto = rs.getDouble("qtMinProduto");
		tabPrecoGrupoProd.qtMinPedido = rs.getDouble("qtMinPedido");
		tabPrecoGrupoProd.qtMinGrade1 = rs.getDouble("qtMinGrade1");
		tabPrecoGrupoProd.qtMinGrade2 = rs.getDouble("qtMinGrade2");
		tabPrecoGrupoProd.flTipoAlteracao = rs.getString("flTipoAlteracao");
		tabPrecoGrupoProd.cdUsuario = rs.getString("cdUsuario");
		tabPrecoGrupoProd.nuCarimbo = rs.getInt("nuCarimbo");
		return tabPrecoGrupoProd;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" CDEMPRESA,"
				+ " CDREPRESENTANTE,"
				+ " CDTABELAPRECO,"
				+ " CDGRUPOPRODUTO1,"
				+ " QTMINPRODUTO,"
				+ " QTMINPEDIDO,"
				+ " QTMINGRADE1,"
				+ " QTMINGRADE2,"
				+ " FLTIPOALTERACAO,"
				+ " CDUSUARIO,"
				+ " NUCARIMBO");
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
		TabPrecoGrupoProd tabPrecoGrupoProd = (TabPrecoGrupoProd) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", tabPrecoGrupoProd.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", tabPrecoGrupoProd.cdRepresentante);
		sqlWhereClause.addAndCondition("CDTABELAPRECO = ", tabPrecoGrupoProd.cdTabelaPreco);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO1", tabPrecoGrupoProd.cdGrupoProduto1);
		sql.append(sqlWhereClause.getSql());
	}

}
