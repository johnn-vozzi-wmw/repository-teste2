package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ComissaoGrupoFaixaNeg;
import totalcross.sql.ResultSet;

public class ComissaoGrupoFaixaNegDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ComissaoGrupoFaixaNeg();
	}

	private static ComissaoGrupoFaixaNegDao instance;

	public ComissaoGrupoFaixaNegDao() {
		super(ComissaoGrupoFaixaNeg.TABLE_NAME);
	}

	public static ComissaoGrupoFaixaNegDao getInstance() {
		if (instance == null) {
			instance = new ComissaoGrupoFaixaNegDao();
		}
		return instance;
	}

	//@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException { }
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException { }
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

	//@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowkey,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" CDGRUPOPRODUTO1,");
		sql.append(" CDGRUPOPRODUTO2,");
		sql.append(" CDGRUPOPRODUTO3,");
		sql.append(" VLFAIXADESCONTO,");
		sql.append(" VLINDICE,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" CDUSUARIO");
	}

	//@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		ComissaoGrupoFaixaNeg comissaoGrupoFaixaDesc = new ComissaoGrupoFaixaNeg();
		comissaoGrupoFaixaDesc.rowKey = rs.getString("rowkey");
		comissaoGrupoFaixaDesc.cdEmpresa = rs.getString("cdEmpresa");
		comissaoGrupoFaixaDesc.cdRepresentante = rs.getString("cdRepresentante");
		comissaoGrupoFaixaDesc.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
		comissaoGrupoFaixaDesc.cdGrupoProduto2 = rs.getString("cdGrupoProduto2");
		comissaoGrupoFaixaDesc.cdGrupoProduto3 = rs.getString("cdGrupoProduto3");
		comissaoGrupoFaixaDesc.vlIndice = ValueUtil.round(rs.getDouble("vlIndice"));
		comissaoGrupoFaixaDesc.vlPctComissao = ValueUtil.round(rs.getDouble("vlPctComissao"));
		comissaoGrupoFaixaDesc.flTipoAlteracao = rs.getString("flTipoAlteracao");
		comissaoGrupoFaixaDesc.cdUsuario = rs.getString("cdUsuario");
		return comissaoGrupoFaixaDesc;
	}

	//@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		ComissaoGrupoFaixaNeg comissaoGrupoFaixaDes = (ComissaoGrupoFaixaNeg) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", comissaoGrupoFaixaDes.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", comissaoGrupoFaixaDes.cdRepresentante);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO1 = ", comissaoGrupoFaixaDes.cdGrupoProduto1);
		sqlWhereClause.addAndConditionOr("CDGRUPOPRODUTO2 = ", new String[]{comissaoGrupoFaixaDes.cdGrupoProduto2, "0"});
		sqlWhereClause.addAndConditionOr("CDGRUPOPRODUTO3 = ", new String[]{comissaoGrupoFaixaDes.cdGrupoProduto3, "0"});
		sqlWhereClause.addAndConditionForced("VLINDICE <= ", comissaoGrupoFaixaDes.vlIndice);
		sql.append(sqlWhereClause.getSql());
	}

	public double findVlPctComissaoByGrupoProduto(ComissaoGrupoFaixaNeg comissaoGrupoFaixaNeg) throws SQLException {
		comissaoGrupoFaixaNeg.limit = 1;
		StringBuffer sql = getSqlBuffer();
		sql.append(" select vlPctComissao ");
		sql.append(" from ");
		sql.append(tableName);
		sql.append(" tb ");
		addWhereByExample(comissaoGrupoFaixaNeg, sql);
		sql.append(" order by CDGRUPOPRODUTO1 desc, CDGRUPOPRODUTO2 desc, CDGRUPOPRODUTO3 desc, VLINDICE desc");
		addLimit(sql, comissaoGrupoFaixaNeg);
		return getDouble(sql.toString());
	}

}