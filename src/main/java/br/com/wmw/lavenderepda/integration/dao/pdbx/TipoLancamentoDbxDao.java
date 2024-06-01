package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.TipoLancamento;
import totalcross.sql.ResultSet;

import java.sql.SQLException;

public class TipoLancamentoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TipoLancamento();
	}

	private static TipoLancamentoDbxDao instance;

	public TipoLancamentoDbxDao() {
		super(TipoLancamento.TABLE_NAME);
	}

	public static TipoLancamentoDbxDao getInstance() {
		if (instance == null) {
			instance = new TipoLancamentoDbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		TipoLancamento tipoLancamento = new TipoLancamento();
		tipoLancamento.rowKey = rs.getString("rowkey");
		tipoLancamento.cdEmpresa = rs.getString("cdEmpresa");
		tipoLancamento.cdRepresentante = rs.getString("cdRepresentante");
		tipoLancamento.cdTipoLancamento = rs.getString("cdTipoLancamento");
		tipoLancamento.dsTipoLancamento = rs.getString("dsTipoLancamento");
		tipoLancamento.cdUsuario = rs.getString("cdUsuario");
		tipoLancamento.nuCarimbo = rs.getInt("nuCarimbo");
		tipoLancamento.flTipoAlteracao = rs.getString("flTipoAlteracao");
		tipoLancamento.hrAlteracao = rs.getString("hrAlteracao");
		tipoLancamento.dtAlteracao = rs.getDate("dtAlteracao");
		return tipoLancamento;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowkey,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" CDTIPOLANCAMENTO,");
		sql.append(" DSTIPOLANCAMENTO,");
		sql.append(" CDUSUARIO,");
		sql.append(" NUCARIMBO,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" HRALTERACAO,");
		sql.append(" DTALTERACAO");
	}

	@Override
	protected void addWhereByExample(BaseDomain baseDomain, StringBuffer sql) throws SQLException {
		TipoLancamento tipoLancamento = (TipoLancamento) baseDomain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", tipoLancamento.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", tipoLancamento.cdRepresentante);
		sqlWhereClause.addAndCondition("CDTIPOLANCAMENTO  = ", tipoLancamento.cdTipoLancamento);
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
