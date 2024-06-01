package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoLog;
import totalcross.sql.ResultSet;

public class ItemPedidoLogDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemPedidoLog();
	}

	private static ItemPedidoLogDbxDao instance;
	

	public ItemPedidoLogDbxDao() {
		super(ItemPedidoLog.TABLE_NAME);
	}

	public static ItemPedidoLogDbxDao getInstance() {
		if (instance == null) {
			instance = new ItemPedidoLogDbxDao();
		}
		return instance;
	}

	// @Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		ItemPedidoLog itemPedidoLog = new ItemPedidoLog();
		itemPedidoLog.rowKey = rs.getString("rowkey");
		itemPedidoLog.cdEmpresa = rs.getString("cdEmpresa");
		itemPedidoLog.cdRepresentante = rs.getString("cdRepresentante");
		itemPedidoLog.flOrigemPedido = rs.getString("flOrigemPedido");
		itemPedidoLog.nuPedido = rs.getString("nuPedido");
		itemPedidoLog.cdProduto = rs.getString("cdProduto");
		itemPedidoLog.flTipoItemPedido = rs.getString("flTipoItemPedido");
		itemPedidoLog.nuSeqProduto = rs.getInt("nuSeqProduto");
		itemPedidoLog.dtAcao = rs.getDate("dtAcao");
		itemPedidoLog.hrAcao = rs.getString("hrAcao");
		itemPedidoLog.qtItemFisico = ValueUtil.round(rs.getDouble("qtItemFisico"));
		itemPedidoLog.qtItemFaturamento = ValueUtil.round(rs.getDouble("qtItemFaturamento"));
		itemPedidoLog.cdUnidade = rs.getString("cdUnidade");
		itemPedidoLog.vlItemPedido = ValueUtil.round(rs.getDouble("vlItemPedido"));
		itemPedidoLog.vlTotalItemPedido = ValueUtil.round(rs.getDouble("vlTotalItemPedido"));
		itemPedidoLog.vlPctDesconto = ValueUtil.round(rs.getDouble("vlPctDesconto"));
		itemPedidoLog.vlPctAcrescimo = ValueUtil.round(rs.getDouble("vlPctAcrescimo"));
		itemPedidoLog.vlVerbaItem = ValueUtil.round(rs.getDouble("vlVerbaItem"));
		itemPedidoLog.vlVerbaItemPositivo = ValueUtil.round(rs.getDouble("vlVerbaItemPositivo"));
		itemPedidoLog.cdUsuarioLiberacao = rs.getString("cdUsuarioLiberacao");
		itemPedidoLog.flTipoRegistro = rs.getString("flTipoRegistro");
		itemPedidoLog.flOrigemAcao = rs.getString("flOrigemAcao");
		itemPedidoLog.cdUsuarioCriacao = rs.getString("cdUsuarioCriacao");
		itemPedidoLog.nuCarimbo = rs.getInt("nuCarimbo");
		itemPedidoLog.cdUsuario = rs.getString("cdUsuario");
		itemPedidoLog.flTipoAlteracao = rs.getString("flTipoAlteracao");
		return itemPedidoLog;
	}

	// @Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}

//	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
		sql.append(" rowkey,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" FLORIGEMPEDIDO,");
		sql.append(" NUPEDIDO,");
		sql.append(" CDPRODUTO,");
		sql.append(" FLTIPOITEMPEDIDO,");
		sql.append(" NUSEQPRODUTO,");
		sql.append(" DTACAO,");
		sql.append(" HRACAO,");
		sql.append(" QTITEMFISICO,");
		sql.append(" QTITEMFATURAMENTO,");
		sql.append(" CDUNIDADE,");
		sql.append(" VLITEMPEDIDO,");
		sql.append(" VLTOTALITEMPEDIDO,");
		sql.append(" VLPCTDESCONTO,");
		sql.append(" VLPCTACRESCIMO,");
		sql.append(" VLVERBAITEM,");
		sql.append(" VLVERBAITEMPOSITIVO,");
		sql.append(" CDUSUARIOLIBERACAO,");
		sql.append(" FLTIPOREGISTRO,");
		sql.append(" FLORIGEMACAO,");
		sql.append(" CDUSUARIOCRIACAO,");
		sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO");
	}

	// @Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

	// @Override
	protected void addInsertColumns(StringBuffer sql) {
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" FLORIGEMPEDIDO,");
		sql.append(" NUPEDIDO,");
		sql.append(" CDPRODUTO,");
		sql.append(" FLTIPOITEMPEDIDO,");
		sql.append(" NUSEQPRODUTO,");
		sql.append(" DTACAO,");
		sql.append(" HRACAO,");
		sql.append(" QTITEMFISICO,");
		sql.append(" QTITEMFATURAMENTO,");
		sql.append(" CDUNIDADE,");
		sql.append(" VLITEMPEDIDO,");
		sql.append(" VLTOTALITEMPEDIDO,");
		sql.append(" VLPCTDESCONTO,");
		sql.append(" VLPCTACRESCIMO,");
		sql.append(" VLVERBAITEM,");
		sql.append(" VLVERBAITEMPOSITIVO,");
		sql.append(" CDUSUARIOLIBERACAO,");
		sql.append(" FLTIPOREGISTRO,");
		sql.append(" FLORIGEMACAO,");
		sql.append(" CDUSUARIOCRIACAO,");
		sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO");
	}

	// @Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
		ItemPedidoLog itemPedidoLog = (ItemPedidoLog) domain;
		sql.append(Sql.getValue(itemPedidoLog.cdEmpresa)).append(",");
		sql.append(Sql.getValue(itemPedidoLog.cdRepresentante)).append(",");
		sql.append(Sql.getValue(itemPedidoLog.flOrigemPedido)).append(",");
		sql.append(Sql.getValue(itemPedidoLog.nuPedido)).append(",");
		sql.append(Sql.getValue(itemPedidoLog.cdProduto)).append(",");
		sql.append(Sql.getValue(itemPedidoLog.flTipoItemPedido)).append(",");
		sql.append(Sql.getValue(itemPedidoLog.nuSeqProduto)).append(",");
		sql.append(Sql.getValue(itemPedidoLog.dtAcao)).append(",");
		sql.append(Sql.getValue(itemPedidoLog.hrAcao)).append(",");
		sql.append(Sql.getValue(itemPedidoLog.qtItemFisico)).append(",");
		sql.append(Sql.getValue(itemPedidoLog.qtItemFaturamento)).append(",");
		sql.append(Sql.getValue(itemPedidoLog.cdUnidade)).append(",");
		sql.append(Sql.getValue(itemPedidoLog.vlItemPedido)).append(",");
		sql.append(Sql.getValue(itemPedidoLog.vlTotalItemPedido)).append(",");
		sql.append(Sql.getValue(itemPedidoLog.vlPctDesconto)).append(",");
		sql.append(Sql.getValue(itemPedidoLog.vlPctAcrescimo)).append(",");
		sql.append(Sql.getValue(itemPedidoLog.vlVerbaItem)).append(",");
		sql.append(Sql.getValue(itemPedidoLog.vlVerbaItemPositivo)).append(",");
		sql.append(Sql.getValue(itemPedidoLog.cdUsuarioLiberacao)).append(",");
		sql.append(Sql.getValue(itemPedidoLog.flTipoRegistro)).append(",");
		sql.append(Sql.getValue(itemPedidoLog.flOrigemAcao)).append(",");
		sql.append(Sql.getValue(itemPedidoLog.cdUsuarioCriacao)).append(",");
		sql.append(Sql.getValue(itemPedidoLog.nuCarimbo)).append(",");
        sql.append(Sql.getValue(itemPedidoLog.cdUsuario)).append(",");
        sql.append(Sql.getValue(itemPedidoLog.flTipoAlteracao));
	}

	// @Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
		ItemPedidoLog itemPedidoLog = (ItemPedidoLog) domain;
		sql.append(" QTITEMFISICO = ").append(Sql.getValue(itemPedidoLog.qtItemFisico)).append(",");
		sql.append(" QTITEMFATURAMENTO = ").append(Sql.getValue(itemPedidoLog.qtItemFaturamento)).append(",");
		sql.append(" CDUNIDADE = ").append(Sql.getValue(itemPedidoLog.cdUnidade)).append(",");
		sql.append(" VLITEMPEDIDO = ").append(Sql.getValue(itemPedidoLog.vlItemPedido)).append(",");
		sql.append(" VLTOTALITEMPEDIDO = ").append(Sql.getValue(itemPedidoLog.vlTotalItemPedido)).append(",");
		sql.append(" VLPCTDESCONTO = ").append(Sql.getValue(itemPedidoLog.vlPctDesconto)).append(",");
		sql.append(" VLPCTACRESCIMO = ").append(Sql.getValue(itemPedidoLog.vlPctAcrescimo)).append(",");
		sql.append(" VLVERBAITEM = ").append(Sql.getValue(itemPedidoLog.vlVerbaItem)).append(",");
		sql.append(" VLVERBAITEMPOSITIVO = ").append(Sql.getValue(itemPedidoLog.vlVerbaItemPositivo)).append(",");
		sql.append(" CDUSUARIOLIBERACAO = ").append(Sql.getValue(itemPedidoLog.cdUsuarioLiberacao)).append(",");
		sql.append(" FLTIPOREGISTRO = ").append(Sql.getValue(itemPedidoLog.flTipoRegistro)).append(",");
		sql.append(" FLORIGEMACAO = ").append(Sql.getValue(itemPedidoLog.flOrigemAcao)).append(",");
		sql.append(" CDUSUARIOCRIACAO = ").append(Sql.getValue(itemPedidoLog.cdUsuarioCriacao)).append(",");
		sql.append(" NUCARIMBO = ").append(Sql.getValue(itemPedidoLog.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(itemPedidoLog.cdUsuario)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(itemPedidoLog.flTipoAlteracao));
	}

	// @Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
		ItemPedidoLog itemPedidoLog = (ItemPedidoLog) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", itemPedidoLog.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", itemPedidoLog.cdRepresentante);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", itemPedidoLog.flOrigemPedido);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", itemPedidoLog.nuPedido);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", itemPedidoLog.cdProduto);
		sqlWhereClause.addAndCondition("FLTIPOITEMPEDIDO = ", itemPedidoLog.flTipoItemPedido);
		sqlWhereClause.addAndCondition("NUSEQPRODUTO = ", itemPedidoLog.nuSeqProduto);
		sqlWhereClause.addAndCondition("FLTIPOREGISTRO = ", itemPedidoLog.flTipoRegistro);
		if (itemPedidoLog.onlyEnviadosServidorFilter) {
    		sqlWhereClause.addAndConditionForced("FLTIPOALTERACAO = ", itemPedidoLog.flTipoAlteracao);
    	}
		// --
		sql.append(sqlWhereClause.getSql());
	}
	
}