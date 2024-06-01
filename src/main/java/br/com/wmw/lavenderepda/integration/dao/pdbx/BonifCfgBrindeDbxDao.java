package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.BonifCfgBrinde;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;

public class BonifCfgBrindeDbxDao extends CrudDbxDao {

	private static BonifCfgBrindeDbxDao instance;
	
	public static BonifCfgBrindeDbxDao getInstance() {
		if (instance == null) {
			instance = new BonifCfgBrindeDbxDao();
		}
		return instance;
	}
	
	public BonifCfgBrindeDbxDao() {
		super(BonifCfgBrinde.TABLE_NAME);
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		BonifCfgBrinde bonifCfgBrinde = new BonifCfgBrinde();
		bonifCfgBrinde.rowKey = rs.getString("rowKey");
		bonifCfgBrinde.cdEmpresa = rs.getString("cdEmpresa");
		bonifCfgBrinde.cdBonifCfg = rs.getString("cdBonifCfg");
		bonifCfgBrinde.cdProduto = rs.getString("cdProduto");
		bonifCfgBrinde.flOpcional = rs.getString("flOpcional");
		bonifCfgBrinde.qtBrinde = rs.getDouble("qtBrinde");
		bonifCfgBrinde.getProduto().dsProduto = rs.getString("dsProduto");
		return bonifCfgBrinde;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.rowKey,");
		sql.append(" tb.cdEmpresa,");
		sql.append(" tb.cdBonifCfg,");
		sql.append(" tb.cdProduto,");
		sql.append(" tb.flOpcional,");
		sql.append(" tb.qtBrinde, ");
		sql.append(DaoUtil.ALIAS_PRODUTO + ".dsProduto ");
		
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		//Essa entidade não é cadastrada pelo app		
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		//Essa entidade não é cadastrada pelo app
		
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		//Essa entidade não é atualizada pelo app
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		BonifCfgBrinde bonifCfgBrinde = (BonifCfgBrinde) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals(" tb.CDEMPRESA ", bonifCfgBrinde.cdEmpresa);
		sqlWhereClause.addAndConditionEquals(" tb.CDBONIFCFG ", bonifCfgBrinde.cdBonifCfg);
		sqlWhereClause.addAndConditionEquals(" tb.CDPRODUTO ", bonifCfgBrinde.cdProduto);
		sqlWhereClause.addAndConditionEquals(" tb.FLOPCIONAL ", bonifCfgBrinde.flOpcional);
		sqlWhereClause.addAndCondition(" tb.QTBRINDE = ", bonifCfgBrinde.qtBrinde);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new BonifCfgBrinde();
	}
	
	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		sql.append(" JOIN TBLVPPRODUTO ").append(DaoUtil.ALIAS_PRODUTO).append(" ON ");
		sql.append(DaoUtil.ALIAS_PRODUTO).append(".CDEMPRESA = tb.CDEMPRESA AND ");
		sql.append(DaoUtil.ALIAS_PRODUTO).append(".CDREPRESENTANTE = ")
			.append(Sql.getValue(SessionLavenderePda.getCdRepresentanteFiltroDados(Produto.class))).append(" AND ");
		sql.append(DaoUtil.ALIAS_PRODUTO).append(".CDPRODUTO = tb.CDPRODUTO ");
	}
	
	public int countBonifCfgBrinde(BonifCfgBrinde filter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT COUNT(1) FROM TBLVPBONIFCFGBRINDE tb ");
		addWhereByExample(filter, sql);
		return getInt(sql.toString());
	}

}
