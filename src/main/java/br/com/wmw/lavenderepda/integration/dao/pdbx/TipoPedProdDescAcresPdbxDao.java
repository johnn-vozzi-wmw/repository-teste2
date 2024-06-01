package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.TipoPedProdDescAcres;
import totalcross.sql.ResultSet;

import java.sql.SQLException;

public class TipoPedProdDescAcresPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TipoPedProdDescAcres();
	}

	private static TipoPedProdDescAcresPdbxDao instance;

	public TipoPedProdDescAcresPdbxDao(String newTableName) {
		super(newTableName);
	}

	public TipoPedProdDescAcresPdbxDao() {
		super(TipoPedProdDescAcres.TABLE_NAME);
	}

	public static TipoPedProdDescAcresPdbxDao getInstance() {
		if (instance == null) {
			instance = new TipoPedProdDescAcresPdbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet resultSet) throws SQLException {
		TipoPedProdDescAcres tipoPedProdDescAcres = new TipoPedProdDescAcres();
		tipoPedProdDescAcres.rowKey = resultSet.getString("rowkey");
		tipoPedProdDescAcres.cdEmpresa = resultSet.getString("cdEmpresa");
		tipoPedProdDescAcres.cdRepresentante = resultSet.getString("cdRepresentante");
		tipoPedProdDescAcres.cdTipoPedido = resultSet.getString("cdTipoPedido");
		tipoPedProdDescAcres.cdProduto = resultSet.getString("cdProduto");
		tipoPedProdDescAcres.vlPctMaxDesconto = resultSet.getDouble("vlPctMaxDesconto");
		tipoPedProdDescAcres.vlPctMaxAcrescimo = resultSet.getDouble("vlPctMaxAcrescimo");
		tipoPedProdDescAcres.vlBase = resultSet.getDouble("vlBase");
		return tipoPedProdDescAcres;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.rowKey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDTIPOPEDIDO,");
		sql.append(" tb.CDPRODUTO,");
		sql.append(" tb.VLPCTMAXDESCONTO,");
		sql.append(" tb.VLPCTMAXACRESCIMO,");
		sql.append(" tb.VLBASE");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" CDTIPOPEDIDO,");
		sql.append(" CDPRODUTO,");
		sql.append(" VLPCTMAXDESCONTO,");
		sql.append(" VLPCTMAXACRESCIMO,");
		sql.append(" VLBASE");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		TipoPedProdDescAcres tipoPedProdDescAcres = (TipoPedProdDescAcres) domain;
		sql.append(Sql.getValue(tipoPedProdDescAcres.cdEmpresa)).append(",");
		sql.append(Sql.getValue(tipoPedProdDescAcres.cdRepresentante)).append(",");
		sql.append(Sql.getValue(tipoPedProdDescAcres.cdTipoPedido)).append(",");
		sql.append(Sql.getValue(tipoPedProdDescAcres.cdProduto)).append(",");
		sql.append(Sql.getValue(tipoPedProdDescAcres.vlPctMaxDesconto)).append(",");
		sql.append(Sql.getValue(tipoPedProdDescAcres.vlPctMaxAcrescimo)).append(",");
		sql.append(Sql.getValue(tipoPedProdDescAcres.vlBase)).append(",");
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		TipoPedProdDescAcres tipoPedProdDescAcres = (TipoPedProdDescAcres) domain;
		sql.append(" VLPCTMAXDESCONTO = ").append(Sql.getValue(tipoPedProdDescAcres.vlPctMaxDesconto)).append(",");
		sql.append(" VLPCTMAXACRESCIMO = ").append(Sql.getValue(tipoPedProdDescAcres.vlPctMaxAcrescimo)).append(",");
		sql.append(" VLBASE = ").append(Sql.getValue(tipoPedProdDescAcres.vlBase));
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		TipoPedProdDescAcres tipoPedProdDescAcres = (TipoPedProdDescAcres) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", tipoPedProdDescAcres.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", tipoPedProdDescAcres.cdRepresentante);
		sqlWhereClause.addAndCondition("CDTIPOPEDIDO = ", tipoPedProdDescAcres.cdTipoPedido);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", tipoPedProdDescAcres.cdProduto);
		//--
		sql.append(sqlWhereClause.getSql());
	}
}
