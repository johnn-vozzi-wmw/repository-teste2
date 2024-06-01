package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.FotoProdutoEmp;
import br.com.wmw.lavenderepda.business.domain.VideoProdutoGrade;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class VideoProdutoGradeDbxDao extends LavendereCrudDbxDao {

	private static VideoProdutoGradeDbxDao instance;

	public static VideoProdutoGradeDbxDao getInstance() {
		if (instance == null) {
			instance = new VideoProdutoGradeDbxDao();
		}
		return instance;
	}

	public VideoProdutoGradeDbxDao() {
		super(VideoProdutoGrade.TABLE_NAME);
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		VideoProdutoGrade videoProdutoGrade = new VideoProdutoGrade();
		videoProdutoGrade.rowKey = rs.getString("rowkey");
		videoProdutoGrade.cdEmpresa = rs.getString("cdEmpresa");
		videoProdutoGrade.dsAgrupadorGrade = rs.getString("dsAgrupadorGrade");
		videoProdutoGrade.nmVideo = rs.getString("nmVideo");
		videoProdutoGrade.cdVideoProdutoGrade = rs.getString("cdVideoProdutoGrade");
		videoProdutoGrade.dtModificacao = rs.getDate("dtModificacao");
		videoProdutoGrade.hrModificacao = rs.getString("hrModificacao");
		videoProdutoGrade.cdUsuario = rs.getString("cdUsuario");
		videoProdutoGrade.nuCarimbo = rs.getInt("nuCarimbo");
		videoProdutoGrade.flTipoAlteracao = rs.getString("flTipoAlteracao");
		return videoProdutoGrade;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.rowkey");
		sql.append(", tb.CDEMPRESA");
		sql.append(", tb.DSAGRUPADORGRADE");
		sql.append(", tb.NMVIDEO");
		sql.append(", tb.CDVIDEOPRODUTOGRADE");
		sql.append(", tb.DTMODIFICACAO");
		sql.append(", tb.HRMODIFICACAO");
		sql.append(", tb.CDUSUARIO");
		sql.append(", tb.NUCARIMBO");
		sql.append(", tb.FLTIPOALTERACAO ");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException { /*nao insere no app*/ }

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException { /*nao insere no app*/ }

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		VideoProdutoGrade videoProdutoGrade = (VideoProdutoGrade) domain;
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(videoProdutoGrade.flTipoAlteracao));
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		VideoProdutoGrade videoProdutoGrade = (VideoProdutoGrade) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" CDEMPRESA = ", videoProdutoGrade.cdEmpresa);
		sqlWhereClause.addAndCondition(" DSAGRUPADORGRADE = ", videoProdutoGrade.dsAgrupadorGrade);
		sqlWhereClause.addAndCondition(" NMVIDEO = ", videoProdutoGrade.nmVideo);
		sqlWhereClause.addAndCondition(" CDVIDEOPRODUTOGRADE = ", videoProdutoGrade.cdVideoProdutoGrade);
		sqlWhereClause.addAndCondition(" DTMODIFICACAO = ", videoProdutoGrade.dtModificacao);
		sqlWhereClause.addAndCondition(" HRMODIFICACAO = ", videoProdutoGrade.hrModificacao);
		sqlWhereClause.addAndCondition(" FLTIPOALTERACAO = ", videoProdutoGrade.flTipoAlteracao);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VideoProdutoGrade();
	}

	@Override
	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		if (domain != null && ValueUtil.isNotEmpty(domain.sortAtributte)) {
			super.addOrderBy(sql, domain);
		} else {
			sql.append(" ORDER BY CAST(CDVIDEOPRODUTOGRADE AS INT) ");
		}
	}
	
	public Vector findCdEmpresaList() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("select cdempresa from ").append(tableName).append(" group by cdempresa");
		return findColumn(FotoProdutoEmp.NMCOLUNA_CDEMPRESA, sql);
	}

}
