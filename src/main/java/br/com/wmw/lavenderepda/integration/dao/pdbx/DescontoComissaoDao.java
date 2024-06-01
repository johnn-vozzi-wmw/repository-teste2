package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.DescontoComissao;
import totalcross.sql.ResultSet;

public class DescontoComissaoDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescontoComissao();
	}

	private static DescontoComissaoDao instance;

	public DescontoComissaoDao() {
		super(DescontoComissao.TABLE_NAME);
	}
	
	public static DescontoComissaoDao getInstance() {
		if (instance == null) {
			instance = new DescontoComissaoDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		DescontoComissao descontoComissao = new DescontoComissao();
		descontoComissao.cdEmpresa = rs.getString("cdEmpresa");
		descontoComissao.cdRepresentante = rs.getString("cdRepresentante");
		descontoComissao.vlFaixaDesconto = rs.getDouble("vlFaixaDesconto");
		descontoComissao.vlPctComissao = rs.getDouble("vlPctComissao");
		descontoComissao.qtItem = rs.getInt("qtItem");
		descontoComissao.vlPctAplicado = rs.getDouble("vlPctAplicado");
		descontoComissao.flAplicaIndiceDesconto = rs.getString("flAplicaIndiceDesconto");
		descontoComissao.vlIndiceDesconto = rs.getDouble("vlIndiceDesconto");
		descontoComissao.vlPctDescontoMin = rs.getDouble("vlPctDescontoMin");
		descontoComissao.vlPctComissaoMin = rs.getDouble("vlPctComissaoMin");
		descontoComissao.nuCarimbo = rs.getInt("nuCarimbo");
		descontoComissao.flTipoAlteracao = rs.getString("flTipoAlteracao");
		descontoComissao.cdUsuario = rs.getString("cdUsuario");
		return descontoComissao;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" CDEMPRESA,")
		.append(" CDREPRESENTANTE,")
		.append(" VLFAIXADESCONTO,")
		.append(" VLPCTCOMISSAO,")
		.append(" QTITEM,")
		.append(" VLPCTAPLICADO,")
		.append(" FLAPLICAINDICEDESCONTO,")
		.append(" VLINDICEDESCONTO,")
		.append(" VLPCTDESCONTOMIN,")
		.append(" VLPCTCOMISSAOMIN,")
		.append(" NUCARIMBO,")
		.append(" FLTIPOALTERACAO,")
		.append(" CDUSUARIO");
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
		DescontoComissao descontoComissao = (DescontoComissao) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", descontoComissao.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", descontoComissao.cdRepresentante);
		sqlWhereClause.addAndConditionForced("VLFAIXADESCONTO <= ", descontoComissao.vlFaixaDesconto);
		sql.append(sqlWhereClause.getSql());
	}

}
