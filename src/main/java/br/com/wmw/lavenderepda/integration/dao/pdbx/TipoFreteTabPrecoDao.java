package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.TipoFreteTabPreco;
import totalcross.sql.ResultSet;

public class TipoFreteTabPrecoDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TipoFreteTabPreco();
	}

	private static TipoFreteTabPrecoDao instance;

	public TipoFreteTabPrecoDao() {
		super(TipoFreteTabPreco.TABLE_NAME);
	}

	public static TipoFreteTabPrecoDao getInstance() {
		if (instance == null) {
			instance = new TipoFreteTabPrecoDao();
		}
		return instance;
	}

	//@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		TipoFreteTabPreco tipoFreteTabPreco = new TipoFreteTabPreco();
		tipoFreteTabPreco.rowKey = rs.getString("rowkey");
		tipoFreteTabPreco.cdEmpresa = rs.getString("cdEmpresa");
		tipoFreteTabPreco.cdRepresentante = rs.getString("cdRepresentante");
		tipoFreteTabPreco.cdTipoFrete = rs.getString("cdTipoFrete");
		tipoFreteTabPreco.cdTabelaPreco = rs.getString("cdTabelaPreco");
		tipoFreteTabPreco.cdProduto = rs.getString("cdProduto");
		tipoFreteTabPreco.qtPeso = rs.getDouble("qtPeso");
		tipoFreteTabPreco.vlPctFrete = ValueUtil.round(rs.getDouble("vlPctFrete"));
		tipoFreteTabPreco.vlPctCredito = rs.getDouble("vlPctCredito");
		tipoFreteTabPreco.vlFrete = rs.getDouble("vlFrete");
		tipoFreteTabPreco.cdUnidade = rs.getString("cdUnidade");
		tipoFreteTabPreco.nuCarimbo = rs.getInt("nuCarimbo");
		tipoFreteTabPreco.flTipoAlteracao = rs.getString("flTipoAlteracao");
		tipoFreteTabPreco.cdUsuario = rs.getString("cdUsuario");
		return tipoFreteTabPreco;
	}

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		return null;
	}

	//@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowkey,")
		.append(" CDEMPRESA,")
		.append(" CDREPRESENTANTE,")
		.append(" CDTIPOFRETE,")
		.append(" CDTABELAPRECO,")
		.append(" CDPRODUTO,")
		.append(" QTPESO,")
		.append(" VLPCTFRETE,")
        .append(" VLPCTCREDITO,")
        .append(" VLFRETE,")
        .append(" CDUNIDADE,")
		.append(" NUCARIMBO,")
		.append(" FLTIPOALTERACAO,")
		.append(" CDUSUARIO");
	}

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

	//@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" CDTIPOFRETE,");
		sql.append(" CDTABELAPRECO,");
		sql.append(" CDPRODUTO,");
		sql.append(" QTPESO,");
		sql.append(" VLPCTFRETE,");
        sql.append(" VLPCTCREDITO,");
		sql.append(" NUCARIMBO,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" CDUSUARIO");
	}

	//@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		TipoFreteTabPreco tipoFreteTabPreco = (TipoFreteTabPreco) domain;
		sql.append(Sql.getValue(tipoFreteTabPreco.cdEmpresa)).append(",");
		sql.append(Sql.getValue(tipoFreteTabPreco.cdRepresentante)).append(",");
		sql.append(Sql.getValue(tipoFreteTabPreco.cdTipoFrete)).append(",");
		sql.append(Sql.getValue(tipoFreteTabPreco.cdTabelaPreco)).append(",");
		sql.append(Sql.getValue(tipoFreteTabPreco.cdProduto)).append(",");
		sql.append(Sql.getValue(tipoFreteTabPreco.qtPeso)).append(",");
		sql.append(Sql.getValue(tipoFreteTabPreco.vlPctFrete)).append(",");
        sql.append(Sql.getValue(tipoFreteTabPreco.vlPctCredito)).append(",");
		sql.append(Sql.getValue(tipoFreteTabPreco.nuCarimbo)).append(",");
		sql.append(Sql.getValue(tipoFreteTabPreco.flTipoAlteracao)).append(",");
		sql.append(Sql.getValue(tipoFreteTabPreco.cdUsuario));
	}

	//@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		TipoFreteTabPreco tipoFreteTabPreco = (TipoFreteTabPreco) domain;
		sql.append(" VLPCTFRETE = ").append(Sql.getValue(tipoFreteTabPreco.vlPctFrete)).append(",");
        sql.append(" VLPCTCREDITO = ").append(Sql.getValue(tipoFreteTabPreco.vlPctCredito)).append(",");
		sql.append(" NUCARIMBO = ").append(Sql.getValue(tipoFreteTabPreco.nuCarimbo)).append(",");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(tipoFreteTabPreco.flTipoAlteracao)).append(",");
		sql.append(" CDUSUARIO = ").append(Sql.getValue(tipoFreteTabPreco.cdUsuario));
	}

	//@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		TipoFreteTabPreco tipoFreteTabPreco = (TipoFreteTabPreco) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", tipoFreteTabPreco.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", tipoFreteTabPreco.cdRepresentante);
		sqlWhereClause.addAndCondition("CDTIPOFRETE = ", tipoFreteTabPreco.cdTipoFrete);
		sqlWhereClause.addAndCondition("CDTABELAPRECO = ", tipoFreteTabPreco.cdTabelaPreco);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", tipoFreteTabPreco.cdProduto);
		sqlWhereClause.addAndCondition("VLPCTCREDITO = ", tipoFreteTabPreco.vlPctCredito);
		sqlWhereClause.addAndCondition("CDUNIDADE = ", tipoFreteTabPreco.cdUnidade);
		//--
		sql.append(sqlWhereClause.getSql());
	}
	
}