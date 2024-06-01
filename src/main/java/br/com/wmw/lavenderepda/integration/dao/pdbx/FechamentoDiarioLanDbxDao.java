package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.FechamentoDiarioLan;
import br.com.wmw.lavenderepda.business.domain.TipoLancamento;
import totalcross.sql.ResultSet;

import java.sql.SQLException;

public class FechamentoDiarioLanDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FechamentoDiarioLan();
	}

	private static FechamentoDiarioLanDbxDao instance;

	public FechamentoDiarioLanDbxDao() {
		super(FechamentoDiarioLan.TABLE_NAME);
	}

	public static FechamentoDiarioLanDbxDao getInstance() {
		if (instance == null) {
			instance = new FechamentoDiarioLanDbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		FechamentoDiarioLan fechamentoDiarioLan = new FechamentoDiarioLan();
		fechamentoDiarioLan.rowKey = rs.getString("rowkey");
		fechamentoDiarioLan.cdEmpresa = rs.getString("cdEmpresa");
		fechamentoDiarioLan.cdRepresentante = rs.getString("cdRepresentante");
		fechamentoDiarioLan.dtFechamentoDiario = rs.getDate("dtFechamentoDiario");
		fechamentoDiarioLan.cdTipoLancamento = rs.getString("cdTipoLancamento");
		fechamentoDiarioLan.dsTipoLancamento = rs.getString("dsTipoLancamento");
		fechamentoDiarioLan.vlTotalLancamento = rs.getDouble("vlTotalLancamento");
		fechamentoDiarioLan.cdUsuario = rs.getString("cdUsuario");
		fechamentoDiarioLan.nuCarimbo = rs.getInt("nuCarimbo");
		fechamentoDiarioLan.flTipoAlteracao = rs.getString("flTipoAlteracao");
		fechamentoDiarioLan.hrAlteracao = rs.getString("hrAlteracao");
		fechamentoDiarioLan.dtAlteracao = rs.getDate("dtAlteracao");
		return fechamentoDiarioLan;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.rowkey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.DTFECHAMENTODIARIO,");
		sql.append(" tb.CDTIPOLANCAMENTO,");
		sql.append(" tipoLancamento.DSTIPOLANCAMENTO,");
		sql.append(" tb.VLTOTALLANCAMENTO,");
		sql.append(" tb.CDUSUARIO,");
		sql.append(" tb.NUCARIMBO,");
		sql.append(" tb.FLTIPOALTERACAO,");
		sql.append(" tb.HRALTERACAO,");
		sql.append(" tb.DTALTERACAO");
	}

	@Override
	protected void addWhereByExample(BaseDomain baseDomain, StringBuffer sql) throws SQLException {
		FechamentoDiarioLan fechamentoDiarioLan = (FechamentoDiarioLan) baseDomain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", fechamentoDiarioLan.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", fechamentoDiarioLan.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.DTFECHAMENTODIARIO  = ", fechamentoDiarioLan.dtFechamentoDiario);
		sqlWhereClause.addAndCondition("tb.CDTIPOLANCAMENTO  = ", fechamentoDiarioLan.cdTipoLancamento);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		if (domain != null && ValueUtil.isNotEmpty(domain.sortAtributte) && ValueUtil.valueEquals("DSTIPOLANCAMENTO", domain.sortAtributte)) {
			sql.append(" order by");
			sql.append(" tipoLancamento." + domain.sortAtributte);
			sql.append(ValueUtil.getBooleanValue(domain.sortAsc) ? " ASC" : " DESC");
			return;
		}
		super.addOrderBy(sql, domain);
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" DTFECHAMENTODIARIO,");
		sql.append(" CDTIPOLANCAMENTO,");
		sql.append(" VLTOTALLANCAMENTO,");
		sql.append(" CDUSUARIO,");
		sql.append(" NUCARIMBO,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" HRALTERACAO,");
		sql.append(" DTALTERACAO");
	}

	@Override
	protected void addInsertValues(BaseDomain baseDomain, StringBuffer sql) throws SQLException {
		FechamentoDiarioLan fechamentoDiarioLan = (FechamentoDiarioLan) baseDomain;
		sql.append(Sql.getValue(fechamentoDiarioLan.cdEmpresa)).append(",");
		sql.append(Sql.getValue(fechamentoDiarioLan.cdRepresentante)).append(",");
		sql.append(Sql.getValue(fechamentoDiarioLan.dtFechamentoDiario)).append(",");
		sql.append(Sql.getValue(fechamentoDiarioLan.cdTipoLancamento)).append(",");
		sql.append(Sql.getValue(fechamentoDiarioLan.vlTotalLancamento)).append(",");
		sql.append(Sql.getValue(fechamentoDiarioLan.cdUsuario)).append(",");
		sql.append(Sql.getValue(fechamentoDiarioLan.nuCarimbo)).append(",");
		sql.append(Sql.getValue(fechamentoDiarioLan.flTipoAlteracao)).append(",");
		sql.append(Sql.getValue(fechamentoDiarioLan.hrAlteracao)).append(",");
		sql.append(Sql.getValue(fechamentoDiarioLan.dtAlteracao));
	}

	@Override
	protected void addUpdateValues(BaseDomain baseDomain, StringBuffer sql) throws SQLException {
		FechamentoDiarioLan fechamentoDiarioLan = (FechamentoDiarioLan) baseDomain;
		sql.append(" VLTOTALLANCAMENTO = ").append(Sql.getValue(fechamentoDiarioLan.vlTotalLancamento));
	}

	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		sql.append(" JOIN ");
		sql.append(TipoLancamento.TABLE_NAME).append(" tipoLancamento ");
		sql.append(" ON tipoLancamento.CDEMPRESA = tb.CDEMPRESA ");
		sql.append(" AND tipoLancamento.CDREPRESENTANTE = tb.CDREPRESENTANTE ");
		sql.append(" AND tipoLancamento.CDTIPOLANCAMENTO = tb.CDTIPOLANCAMENTO ");
	}
}
