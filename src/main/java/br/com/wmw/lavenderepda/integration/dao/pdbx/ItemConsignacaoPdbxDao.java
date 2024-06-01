package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ItemConsignacao;
import totalcross.sql.ResultSet;

public class ItemConsignacaoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemConsignacao();
	}

    private static ItemConsignacaoPdbxDao instance;

    public ItemConsignacaoPdbxDao() {
        super(ItemConsignacao.TABLE_NAME);
    }

    public static ItemConsignacaoPdbxDao getInstance() {
        if (instance == null) {
            instance = new ItemConsignacaoPdbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ItemConsignacao itemConsignacao = new ItemConsignacao();
        itemConsignacao.rowKey = rs.getString("rowkey");
        itemConsignacao.cdEmpresa = rs.getString("cdEmpresa");
        itemConsignacao.cdRepresentante = rs.getString("cdRepresentante");
        itemConsignacao.cdCliente = rs.getString("cdCliente");
        itemConsignacao.cdConsignacao = rs.getString("cdConsignacao");
        itemConsignacao.cdProduto = rs.getString("cdProduto");
        itemConsignacao.cdTabelaPreco = rs.getString("cdTabelaPreco");
        itemConsignacao.vlItem = ValueUtil.round(rs.getDouble("vlItem"));
        itemConsignacao.qtItemConsignado = ValueUtil.round(rs.getDouble("qtItemconsignado"));
        itemConsignacao.qtItemSobra = ValueUtil.round(rs.getDouble("qtItemsobra"));
        itemConsignacao.flTipoAlteracao = rs.getString("flTipoAlteracao");
        itemConsignacao.nuCarimbo = rs.getInt("nuCarimbo");
        itemConsignacao.cdUsuario = rs.getString("cdUsuario");
        return itemConsignacao;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDCONSIGNACAO,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" VLITEM,");
        sql.append(" QTITEMCONSIGNADO,");
        sql.append(" QTITEMSOBRA,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDCONSIGNACAO,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" VLITEM,");
        sql.append(" QTITEMCONSIGNADO,");
        sql.append(" QTITEMSOBRA,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemConsignacao itemConsignacao = (ItemConsignacao) domain;
        sql.append(Sql.getValue(itemConsignacao.cdEmpresa)).append(",");
        sql.append(Sql.getValue(itemConsignacao.cdRepresentante)).append(",");
        sql.append(Sql.getValue(itemConsignacao.cdCliente)).append(",");
        sql.append(Sql.getValue(itemConsignacao.cdConsignacao)).append(",");
        sql.append(Sql.getValue(itemConsignacao.cdProduto)).append(",");
        sql.append(Sql.getValue(itemConsignacao.cdTabelaPreco)).append(",");
        sql.append(Sql.getValue(itemConsignacao.vlItem)).append(",");
        sql.append(Sql.getValue(itemConsignacao.qtItemConsignado)).append(",");
        sql.append(Sql.getValue(itemConsignacao.qtItemSobra)).append(",");
        sql.append(Sql.getValue(itemConsignacao.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(1)).append(",");
        sql.append(Sql.getValue(itemConsignacao.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemConsignacao itemConsignacao = (ItemConsignacao) domain;
        sql.append(" CDTABELAPRECO = ").append(Sql.getValue(itemConsignacao.cdTabelaPreco)).append(",");
        sql.append(" VLITEM = ").append(Sql.getValue(itemConsignacao.vlItem)).append(",");
        sql.append(" QTITEMCONSIGNADO = ").append(Sql.getValue(itemConsignacao.qtItemConsignado)).append(",");
        sql.append(" QTITEMSOBRA = ").append(Sql.getValue(itemConsignacao.qtItemSobra)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(itemConsignacao.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(itemConsignacao.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(itemConsignacao.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemConsignacao itemConsignacao = (ItemConsignacao) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", itemConsignacao.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", itemConsignacao.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", itemConsignacao.cdCliente);
		sqlWhereClause.addAndCondition("CDCONSIGNACAO = ", itemConsignacao.cdConsignacao);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", itemConsignacao.cdProduto);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}