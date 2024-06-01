package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ComiTabPrecoGrupo;
import totalcross.sql.ResultSet;

public class ComiTabPrecoGrupoDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ComiTabPrecoGrupo();
	}

	public ComiTabPrecoGrupoDao() {
		super(ComiTabPrecoGrupo.TABLE_NAME);
	}
	
	private static ComiTabPrecoGrupoDao instance;
	
	public static ComiTabPrecoGrupoDao getInstance() {
		if (instance == null) {
			instance = new ComiTabPrecoGrupoDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		ComiTabPrecoGrupo comiTabPrecoGrupo = new ComiTabPrecoGrupo();
		comiTabPrecoGrupo.cdEmpresa = rs.getString("cdEmpresa");
		comiTabPrecoGrupo.cdRepresentante = rs.getString("cdRepresentante");
		comiTabPrecoGrupo.cdTabelaPreco = rs.getString("cdTabelaPreco");
		comiTabPrecoGrupo.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
		comiTabPrecoGrupo.vlPctComissao = rs.getDouble("vlPctComissao");
		return comiTabPrecoGrupo;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
		sql.append("rowKey, CDEMPRESA, CDREPRESENTANTE, CDTABELAPRECO, CDGRUPOPRODUTO1, VLPCTCOMISSAO");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) {

	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) {

	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {

	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
		ComiTabPrecoGrupo comiTabPrecoGrupo = (ComiTabPrecoGrupo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		if (ValueUtil.isNotEmpty(comiTabPrecoGrupo.cdEmpresa)) {
			sqlWhereClause.addAndConditionEquals("CDEMPRESA", comiTabPrecoGrupo.cdEmpresa);
		}
		if (ValueUtil.isNotEmpty(comiTabPrecoGrupo.cdRepresentante)) {
			sqlWhereClause.addAndConditionEquals("CDREPRESENTANTE", comiTabPrecoGrupo.cdRepresentante);
		}
		if (ValueUtil.isNotEmpty(comiTabPrecoGrupo.cdTabelaPreco)) {
			sqlWhereClause.addAndConditionEquals("CDTABELAPRECO", comiTabPrecoGrupo.cdTabelaPreco);
		}
		if (ValueUtil.isNotEmpty(comiTabPrecoGrupo.cdGrupoProduto1)) {
			sqlWhereClause.addAndConditionEquals("CDGRUPOPRODUTO1", comiTabPrecoGrupo.cdGrupoProduto1);
		}
		sql.append(sqlWhereClause.toString());
	}

}
