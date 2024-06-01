package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Cultura;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class CulturaDbxDao extends CrudDbxDao  {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Cultura();
	}
	
	public static CulturaDbxDao instance;

	public CulturaDbxDao() {
		super(Cultura.TABLE_NAME);
	}
	
	public static CulturaDbxDao getInstance() {
		if (instance == null) {
			instance = new CulturaDbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		Cultura cultura = new Cultura();
		cultura.cdCultura = rs.getString("cdCultura");
		cultura.dsCultura = rs.getString("dsCultura");
		cultura.dsNomeCientCultura = rs.getString("dsNomeCientCultura");
		cultura.abrevCultura = rs.getString("abrevCultura");
		return cultura;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.CDCULTURA,");
		sql.append(" tb.DSCULTURA,");
		sql.append(" tb.DSNOMECIENTCULTURA,");
		sql.append(" tb.ABREVCULTURA");
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

	}
	
	public Vector findAllCulturaByAplicacaoProduto(BaseDomain filter) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct ");
		addSelectColumns(filter, sql);
		sql.append(" from ").append(tableName);
		sql.append(" tb JOIN TBLVPAPLICACAOPRODUTO aplicacao ON tb.CDCULTURA = aplicacao.CDCULTURA");
		addWhereClauseWithAplicacaoProduto(filter, sql);
		addOrderBy(sql, filter);
		try (Statement st = getCurrentDriver().getStatement();
 				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector culturaList = new Vector();
			while (rs.next()) {
				culturaList.addElement(populate(filter, rs));
			}
			return culturaList;
		}
	}
	
	private void addWhereClauseWithAplicacaoProduto(BaseDomain domain, StringBuffer sql) {
		Cultura cultura = (Cultura) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionForced("aplicacao.CDPRODUTO = ", cultura.cdProduto);
		sqlWhereClause.addAndConditionForced("(tb.CDCULTURA like ", "%"+cultura.dsFiltro+"%");
		sqlWhereClause.addOrConditionForced("tb.DSCULTURA like ", "%"+cultura.dsFiltro+"%");
		sqlWhereClause.addOrConditionForced("tb.DSNOMECIENTCULTURA like ", "%"+cultura.dsFiltro+"%");
		sqlWhereClause.addEndMultipleCondition();
		sql.append(sqlWhereClause.getSql());
	}

}