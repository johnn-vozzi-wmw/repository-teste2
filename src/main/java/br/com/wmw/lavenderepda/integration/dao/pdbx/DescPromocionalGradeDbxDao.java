package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescPromocional;
import br.com.wmw.lavenderepda.business.domain.DescPromocionalGrade;
import br.com.wmw.lavenderepda.business.domain.ItemGrade;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class DescPromocionalGradeDbxDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescPromocionalGrade();
	}
	
	public static DescPromocionalGradeDbxDao instance;
	
	public static DescPromocionalGradeDbxDao getInstance() {
		if (instance == null) {
			instance = new DescPromocionalGradeDbxDao();
		}
		return instance;
	}

	public DescPromocionalGradeDbxDao() {
		super(DescPromocionalGrade.TABLE_NAME);
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		DescPromocionalGrade descPromocionalGrade = new DescPromocionalGrade();
		descPromocionalGrade.cdEmpresa = rs.getString("cdEmpresa");
		descPromocionalGrade.cdRepresentante = rs.getString("cdRepresentante");
		DescPromocional descPromocional = new DescPromocional();
		descPromocional.cdCliente = rs.getString("cdCliente");
		descPromocional.cdProduto = rs.getString("cdProduto");
		descPromocional.cdGrupoDescCli = rs.getString("cdGrupoDescCli");
		descPromocional.cdGrupoDescProd = rs.getString("cdGrupoDescProd");
		descPromocional.qtItem = rs.getInt("qtItem");
		descPromocional.cdTabelaPreco = rs.getString("cdTabelaPreco");
		descPromocional.cdCondicaoComercial = rs.getString("cdCondicaoComercial");
		descPromocionalGrade.descPromocional = descPromocional;
		descPromocionalGrade.cdItemGrade2 = rs.getString("cdItemGrade2");
		descPromocionalGrade.nuMultiploEspecial = rs.getInt("nuMultiploEspecial");
		return descPromocionalGrade;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append("tb.CDEMPRESA,"
				+ "tb.CDREPRESENTANTE,"
				+ "tb.CDCLIENTE,"
				+ "tb.CDPRODUTO,"
				+ "tb.CDGRUPODESCCLI,"
				+ "tb.CDGRUPODESCPROD,"
				+ "tb.QTITEM,"
				+ "tb.CDTABELAPRECO,"
				+ "tb.CDCONDICAOCOMERCIAL,"
				+ "tb.CDITEMGRADE2,"
				+ "tb.NUMULTIPLOESPECIAL,"
				+ "tb.FLTIPOALTERACAO,"
				+ "tb.NUCARIMBO");
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
	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		if (LavenderePdaConfig.usaOrdenacaoNuSequenciaGradeProduto) {
			sql.append(" ORDER BY ig.NUSEQUENCIA ASC");
		} else {
			sql.append(" ORDER BY ig.DSITEMGRADE ASC");
		}
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		DescPromocionalGrade descPromocionalGrade = (DescPromocionalGrade) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", descPromocionalGrade.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", descPromocionalGrade.cdRepresentante);
		if (descPromocionalGrade.descPromocional != null) {
			sqlWhereClause.addAndConditionForced("tb.CDCLIENTE = ", descPromocionalGrade.descPromocional.cdCliente);
			sqlWhereClause.addAndConditionForced("tb.CDPRODUTO = ", Sql.getValue(descPromocionalGrade.descPromocional.cdProduto));
			sqlWhereClause.addAndConditionForced("tb.CDGRUPODESCCLI = ", descPromocionalGrade.descPromocional.cdGrupoDescCli);
			sqlWhereClause.addAndConditionForced("tb.CDGRUPODESCPROD = ", descPromocionalGrade.descPromocional.cdGrupoDescProd);
			sqlWhereClause.addAndConditionForced("tb.QTITEM = ", descPromocionalGrade.descPromocional.qtItem);
			sqlWhereClause.addAndConditionForced("tb.CDTABELAPRECO = ", descPromocionalGrade.descPromocional.cdTabelaPreco);
			sqlWhereClause.addAndConditionForced("tb.CDCONDICAOCOMERCIAL = ", descPromocionalGrade.descPromocional.cdCondicaoComercial);
		}
		sql.append(sqlWhereClause.getSql());
		addGradeFilters(descPromocionalGrade, sql);
		addGroupByCols(sql);
	}
	
	private void addGradeFilters(DescPromocionalGrade filter, StringBuffer sql) {
		if (filter.itemGradeList != null) {
			Vector gradesList = filter.itemGradeList;
			int size = gradesList.size();
			boolean virgula = false;
			if (size > 0) {
				sql.append(" AND tb.CDITEMGRADE2 IN (");
				for (int i = 0; i < size; i++) {
					ItemGrade itemGrade = (ItemGrade)gradesList.items[i];
					if (ValueUtil.isNotEmpty(itemGrade.cdItemGrade)) {
						sql.append(virgula ? "," : "").append(Sql.getValue(itemGrade.cdItemGrade));
						virgula = true;
					}
				}
				sql.append(")");
			}
		}
	}
	
	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		sql.append(" JOIN TBLVPPRODUTOGRADE pg ON tb.CDEMPRESA = pg.CDEMPRESA AND tb.CDREPRESENTANTE = pg.CDREPRESENTANTE AND tb.CDPRODUTO = pg.CDPRODUTO ")
		.append(" AND ").append((LavenderePdaConfig.usaGradeProdutoPorTabelaPreco ? "tb.CDTABELAPRECO = pg.CDTABELAPRECO " : "pg.CDTABELAPRECO = " + Sql.getValue(ProdutoGrade.CDTABELAPRECO_PADRAO))).append(" AND tb.CDITEMGRADE2 = pg.CDITEMGRADE2 JOIN TBLVPITEMGRADE ig ON pg.CDEMPRESA = ig.CDEMPRESA AND pg.CDREPRESENTANTE = ig.CDREPRESENTANTE ")
		.append(" AND pg.CDTIPOITEMGRADE2 = ig.CDTIPOITEMGRADE AND pg.CDITEMGRADE2 = ig.CDITEMGRADE");
	}
	
	protected void addGroupByCols(StringBuffer sql) throws SQLException {
		sql.append(" GROUP BY ");
		addSelectColumns(null, sql);
	}

}
