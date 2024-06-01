package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.TabPrecoLoteProd;
import totalcross.sql.ResultSet;

public class TabPrecoLoteProdDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TabPrecoLoteProd();
	}

	public static TabPrecoLoteProdDao instance;

	public TabPrecoLoteProdDao() {
		super(TabPrecoLoteProd.TABLE_NAME);
	}
	
	public static TabPrecoLoteProdDao getInstance() {
		if (instance == null) {
			instance = new TabPrecoLoteProdDao();
		}
		return instance;
	}

	@Override
	protected void addInsertColumns(StringBuffer arg0) throws SQLException {}

	@Override
	protected void addInsertValues(BaseDomain arg0, StringBuffer arg1) throws SQLException {}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" CDEMPRESA,"
				+ " CDREPRESENTANTE,"
				+ " CDTABELAPRECO,"
				+ " CDLOTEPRODUTO,"
				+ " FLTIPOALTERACAO,"
				+ " CDUSUARIO,"
				+ " NUCARIMBO");
	}

	@Override
	protected void addUpdateValues(BaseDomain arg0, StringBuffer arg1) throws SQLException { }

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		TabPrecoLoteProd tabPrecoLoteProd = (TabPrecoLoteProd) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", tabPrecoLoteProd.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", tabPrecoLoteProd.cdRepresentante);
		sqlWhereClause.addAndCondition("CDTABELAPRECO = ", tabPrecoLoteProd.cdTabelaPreco);
		sqlWhereClause.addAndCondition("CDLOTEPRODUTO = ", tabPrecoLoteProd.cdLoteProduto);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		TabPrecoLoteProd tabPrecoLoteProd = new TabPrecoLoteProd();
		tabPrecoLoteProd.cdEmpresa = rs.getString("cdEmpresa");
		tabPrecoLoteProd.cdRepresentante = rs.getString("cdRepresentante");
		tabPrecoLoteProd.cdTabelaPreco = rs.getString("cdTabelaPreco");
		tabPrecoLoteProd.cdLoteProduto = rs.getString("cdLoteProduto");
		tabPrecoLoteProd.flTipoAlteracao = rs.getString("flTipoAlteracao");
		tabPrecoLoteProd.cdUsuario = rs.getString("cdUsuario");
		tabPrecoLoteProd.nuCarimbo = rs.getInt("nuCarimbo");
		return tabPrecoLoteProd;
	}

}
