package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.EmpresaFilialImp;
import totalcross.sql.ResultSet;

import java.sql.SQLException;

public class EmpresaFilialImpDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new EmpresaFilialImp();
	}

	private static EmpresaFilialImpDbxDao instance;

	public EmpresaFilialImpDbxDao() {
		super(EmpresaFilialImp.TABLE_NAME);
	}

	public static EmpresaFilialImpDbxDao getInstance() {
		if (instance == null) {
			instance = new EmpresaFilialImpDbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		EmpresaFilialImp empresaFilialImp = new EmpresaFilialImp();
		empresaFilialImp.rowKey = rs.getString("rowkey");
		empresaFilialImp.cdEmpresa = rs.getString("cdEmpresa");
		empresaFilialImp.cdRepresentante = rs.getString("cdRepresentante");
		empresaFilialImp.cdFilialImp = rs.getString("cdFilialImp");
		empresaFilialImp.dsRazaoSocialFilial = rs.getString("dsRazaoSocialFilial");
		empresaFilialImp.nuTelefone = rs.getString("nuTelefone");
		empresaFilialImp.nuCelular = rs.getString("nuCelular");
		empresaFilialImp.cdUsuario = rs.getString("cdUsuario");
		empresaFilialImp.nuCarimbo = rs.getInt("nuCarimbo");
		empresaFilialImp.flTipoAlteracao = rs.getString("flTipoAlteracao");
		empresaFilialImp.hrAlteracao = rs.getString("hrAlteracao");
		empresaFilialImp.dtAlteracao = rs.getDate("dtAlteracao");
		return empresaFilialImp;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowkey,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" CDFILIALIMP,");
		sql.append(" DSRAZAOSOCIALFILIAL,");
		sql.append(" NUTELEFONE,");
		sql.append(" NUCELULAR,");
		sql.append(" CDUSUARIO,");
		sql.append(" NUCARIMBO,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" HRALTERACAO,");
		sql.append(" DTALTERACAO");
	}

	@Override
	protected void addWhereByExample(BaseDomain baseDomain, StringBuffer sql) throws SQLException {
		EmpresaFilialImp empresaFilialImp = (EmpresaFilialImp) baseDomain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", empresaFilialImp.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", empresaFilialImp.cdRepresentante);
		sqlWhereClause.addAndCondition("CDFILIALIMP  = ", empresaFilialImp.cdFilialImp);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected void addInsertColumns(StringBuffer stringBuffer) throws SQLException {

	}

	@Override
	protected void addInsertValues(BaseDomain baseDomain, StringBuffer stringBuffer) throws SQLException {

	}

	@Override
	protected void addUpdateValues(BaseDomain baseDomain, StringBuffer stringBuffer) throws SQLException {

	}


}
