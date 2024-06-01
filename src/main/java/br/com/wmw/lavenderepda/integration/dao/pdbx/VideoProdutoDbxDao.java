package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.FotoProdutoEmp;
import br.com.wmw.lavenderepda.business.domain.VideoProduto;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

import java.sql.SQLException;

public class VideoProdutoDbxDao extends LavendereCrudDbxDao {

	private static VideoProdutoDbxDao instance;

	public static VideoProdutoDbxDao getInstance() {
		if (instance == null) {
			instance = new VideoProdutoDbxDao();
		}
		return instance;
	}

	public VideoProdutoDbxDao() {
		super(VideoProduto.TABLE_NAME);
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		VideoProduto videoProduto = new VideoProduto();
		videoProduto.rowKey = rs.getString("rowkey");
		videoProduto.cdEmpresa = rs.getString("cdEmpresa");
		videoProduto.cdProduto = rs.getString("cdProduto");
		videoProduto.nmVideo = rs.getString("nmVideo");
		videoProduto.cdVideoProduto = rs.getString("cdVideoProduto");
		videoProduto.dtModificacao = rs.getDate("dtModificacao");
		videoProduto.hrModificacao = rs.getString("hrModificacao");
		videoProduto.cdUsuario = rs.getString("cdUsuario");
		videoProduto.nuCarimbo = rs.getInt("nuCarimbo");
		videoProduto.flTipoAlteracao = rs.getString("flTipoAlteracao");
		return videoProduto;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.rowkey");
		sql.append(", tb.CDEMPRESA");
		sql.append(", tb.CDPRODUTO");
		sql.append(", tb.NMVIDEO");
		sql.append(", tb.CDVIDEOPRODUTO");
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
		VideoProduto videoProduto = (VideoProduto) domain;
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(videoProduto.flTipoAlteracao));
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		VideoProduto videoProduto = (VideoProduto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" CDEMPRESA = ", videoProduto.cdEmpresa);
		sqlWhereClause.addAndCondition(" CDPRODUTO = ", videoProduto.cdProduto);
		sqlWhereClause.addAndCondition(" NMVIDEO = ", videoProduto.nmVideo);
		sqlWhereClause.addAndCondition(" CDVIDEOPRODUTO = ", videoProduto.cdVideoProduto);
		sqlWhereClause.addAndCondition(" DTMODIFICACAO = ", videoProduto.dtModificacao);
		sqlWhereClause.addAndCondition(" HRMODIFICACAO = ", videoProduto.hrModificacao);
		sqlWhereClause.addAndCondition(" FLTIPOALTERACAO = ", videoProduto.flTipoAlteracao);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VideoProduto();
	}

	@Override
	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		if (domain != null && ValueUtil.isNotEmpty(domain.sortAtributte)) {
			super.addOrderBy(sql, domain);
		} else {
			sql.append(" ORDER BY CAST(CDVIDEOPRODUTO AS INT) ");
		}
	}
	
	public Vector findCdEmpresaList() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("select cdempresa from ").append(tableName).append(" group by cdempresa");
		return findColumn(FotoProdutoEmp.NMCOLUNA_CDEMPRESA, sql);
	}

}
