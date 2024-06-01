package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Praga;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class PragaDbxDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Praga();
	}
	
	public static PragaDbxDao instance;
	
	public PragaDbxDao() {
		super(Praga.TABLE_NAME);
	}
	
	public static PragaDbxDao getInstance() {
		if (instance == null) {
			instance = new PragaDbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		Praga praga = new Praga();
		praga.cdPraga = rs.getString("cdPraga");
		praga.dsPraga = rs.getString("dsPraga");
		praga.dsNomeCientPraga = rs.getString("dsNomeCientPraga");
		return praga;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.CDPRAGA,");
		sql.append(" tb.DSPRAGA,");
		sql.append(" tb.DSNOMECIENTPRAGA");
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
	
	public Vector findAllPragaByAplicacaoProduto(BaseDomain filter) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		addSelectColumns(filter, sql);
		sql.append(" from ").append(tableName);
		sql.append(" tb JOIN TBLVPAPLICACAOPRODUTO aplicacao ON tb.CDPRAGA = aplicacao.CDPRAGA");
		addWhereClauseWithAplicacaoProduto(filter, sql);
		addOrderBy(sql, filter);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector pragaList = new Vector();
			while (rs.next()) {
				pragaList.addElement(populate(filter, rs));
			}
			return pragaList;
		}
	}
	
	private void addWhereClauseWithAplicacaoProduto(BaseDomain filter, StringBuffer sql) {
		Praga praga = (Praga) filter;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionForced("aplicacao.CDPRODUTO = ", praga.cdProduto);
		sqlWhereClause.addAndConditionForced("aplicacao.CDCULTURA = ", praga.cdCultura);
		sqlWhereClause.addAndConditionForced("(tb.CDPRAGA like ", "%"+praga.dsFiltro+"%");
		sqlWhereClause.addOrConditionForced("tb.DSPRAGA like ", "%"+praga.dsFiltro+"%");
		sqlWhereClause.addOrConditionForced("tb.DSNOMECIENTPRAGA like ", "%"+praga.dsFiltro+"%");
		sqlWhereClause.addEndMultipleCondition();
		sql.append(sqlWhereClause.getSql());
	}

}
