package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.FechamentoDiarioEst;
import totalcross.sql.ResultSet;

import java.sql.SQLException;

public class FechamentoDiarioEstDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FechamentoDiarioEst();
	}

	private static FechamentoDiarioEstDbxDao instance;

	public FechamentoDiarioEstDbxDao() {
		super(FechamentoDiarioEst.TABLE_NAME);
	}

	public static FechamentoDiarioEstDbxDao getInstance() {
		if (instance == null) {
			instance = new FechamentoDiarioEstDbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		FechamentoDiarioEst fechamentoDiarioEst = new FechamentoDiarioEst();
		fechamentoDiarioEst.rowKey = rs.getString("rowkey");
		fechamentoDiarioEst.cdEmpresa = rs.getString("cdEmpresa");
		fechamentoDiarioEst.cdRepresentante = rs.getString("cdRepresentante");
		fechamentoDiarioEst.dtMovimentacao = rs.getDate("dtMovimentacao");
		fechamentoDiarioEst.cdProduto = rs.getString("cdProduto");
		fechamentoDiarioEst.qtRemessa = rs.getDouble("qtRemessa");
		fechamentoDiarioEst.qtRetorno = rs.getDouble("qtRetorno");
		fechamentoDiarioEst.qtVendido = rs.getDouble("qtVendido");
		fechamentoDiarioEst.vlSaldo = rs.getDouble("vlSaldo");
		fechamentoDiarioEst.cdUsuario = rs.getString("cdUsuario");
		fechamentoDiarioEst.nuCarimbo = rs.getInt("nuCarimbo");
		fechamentoDiarioEst.flTipoAlteracao = rs.getString("flTipoAlteracao");
		fechamentoDiarioEst.hrAlteracao = rs.getString("hrAlteracao");
		fechamentoDiarioEst.dtAlteracao = rs.getDate("dtAlteracao");
		return fechamentoDiarioEst;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowkey,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" DTMOVIMENTACAO,");
		sql.append(" CDPRODUTO,");
		sql.append(" QTREMESSA,");
		sql.append(" QTRETORNO,");
		sql.append(" QTVENDIDO,");
		sql.append(" VLSALDO,");
		sql.append(" CDUSUARIO,");
		sql.append(" NUCARIMBO,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" HRALTERACAO,");
		sql.append(" DTALTERACAO");
	}

	@Override
	protected void addWhereByExample(BaseDomain baseDomain, StringBuffer sql) throws SQLException {
		FechamentoDiarioEst fechamentoDiarioEst = (FechamentoDiarioEst) baseDomain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", fechamentoDiarioEst.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", fechamentoDiarioEst.cdRepresentante);
		sqlWhereClause.addAndCondition("DTMOVIMENTACAO  = ", fechamentoDiarioEst.dtMovimentacao);
		sqlWhereClause.addAndCondition("CDPRODUTO  = ", fechamentoDiarioEst.cdProduto);
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
