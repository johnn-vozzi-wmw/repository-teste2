package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoRemessa;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import totalcross.sql.ResultSet;

public class ItemPedidoRemessaDbxDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemPedidoRemessa();
	}

    private static ItemPedidoRemessaDbxDao instance = null;
	public static String TABLE_NAME = "TBLVPITEMPEDIDOREMESSA";

    public ItemPedidoRemessaDbxDao() {
        super(TABLE_NAME);
    }
    
    public static ItemPedidoRemessaDbxDao getInstance() {
        if (instance == null) {
            instance = new ItemPedidoRemessaDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        ItemPedidoRemessa itemPedidoRemessa = new ItemPedidoRemessa();
        itemPedidoRemessa.rowKey = rs.getString("rowkey");
        itemPedidoRemessa.cdEmpresa = rs.getString("cdEmpresa");
        itemPedidoRemessa.cdRepresentante = rs.getString("cdRepresentante");
        itemPedidoRemessa.flOrigemPedido = rs.getString("flOrigemPedido");
        itemPedidoRemessa.nuPedido = rs.getString("nuPedido");
        itemPedidoRemessa.cdProduto = rs.getString("cdProduto");
        itemPedidoRemessa.flTipoItemPedido = rs.getString("flTipoItemPedido");
        itemPedidoRemessa.nuSeqProduto = rs.getInt("nuSeqProduto");
        itemPedidoRemessa.cdLocalEstoque = rs.getString("cdLocalEstoque");
        itemPedidoRemessa.nuNotaRemessa = rs.getString("nuNotaRemessa");
        itemPedidoRemessa.nuSerieRemessa = rs.getString("nuSerieRemessa");
        itemPedidoRemessa.qtEstoqueConsumido = ValueUtil.round(rs.getDouble("qtEstoqueConsumido"));
        itemPedidoRemessa.cdUsuario = rs.getString("cdUsuario");
        itemPedidoRemessa.nuCarimbo = rs.getInt("nuCarimbo");
        itemPedidoRemessa.flTipoAlteracao = rs.getString("flTipoAlteracao");
        itemPedidoRemessa.dtAlteracao = rs.getDate("dtAlteracao");
        itemPedidoRemessa.hrAlteracao = rs.getString("hrAlteracao");
        return itemPedidoRemessa;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOITEMPEDIDO,");
        sql.append(" NUSEQPRODUTO,");
        sql.append(" CDLOCALESTOQUE,");
        sql.append(" NUNOTAREMESSA,");
        sql.append(" NUSERIEREMESSA,");
        sql.append(" QTESTOQUECONSUMIDO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOITEMPEDIDO,");
        sql.append(" NUSEQPRODUTO,");
        sql.append(" CDLOCALESTOQUE,");
        sql.append(" NUNOTAREMESSA,");
        sql.append(" NUSERIEREMESSA,");
        sql.append(" QTESTOQUECONSUMIDO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        ItemPedidoRemessa itemPedidoRemessa = (ItemPedidoRemessa) domain;
        sql.append(Sql.getValue(itemPedidoRemessa.cdEmpresa)).append(",");
        sql.append(Sql.getValue(itemPedidoRemessa.cdRepresentante)).append(",");
        sql.append(Sql.getValue(itemPedidoRemessa.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(itemPedidoRemessa.nuPedido)).append(",");
        sql.append(Sql.getValue(itemPedidoRemessa.cdProduto)).append(",");
        sql.append(Sql.getValue(itemPedidoRemessa.flTipoItemPedido)).append(",");
        sql.append(Sql.getValue(itemPedidoRemessa.nuSeqProduto)).append(",");
        sql.append(Sql.getValue(itemPedidoRemessa.cdLocalEstoque)).append(",");
        sql.append(Sql.getValue(itemPedidoRemessa.nuNotaRemessa)).append(",");
        sql.append(Sql.getValue(itemPedidoRemessa.nuSerieRemessa)).append(",");
        sql.append(Sql.getValue(itemPedidoRemessa.qtEstoqueConsumido)).append(",");
        sql.append(Sql.getValue(itemPedidoRemessa.cdUsuario)).append(",");
        sql.append(Sql.getValue(itemPedidoRemessa.nuCarimbo)).append(",");
        sql.append(Sql.getValue(itemPedidoRemessa.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(itemPedidoRemessa.dtAlteracao)).append(",");
        sql.append(Sql.getValue(itemPedidoRemessa.hrAlteracao));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        ItemPedidoRemessa itemPedidoRemessa = (ItemPedidoRemessa) domain;
        sql.append(" QTESTOQUECONSUMIDO = ").append(Sql.getValue(itemPedidoRemessa.qtEstoqueConsumido)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(itemPedidoRemessa.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(itemPedidoRemessa.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(itemPedidoRemessa.flTipoAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(itemPedidoRemessa.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(itemPedidoRemessa.hrAlteracao));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        ItemPedidoRemessa itemPedidoRemessa = (ItemPedidoRemessa) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", itemPedidoRemessa.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", itemPedidoRemessa.cdRepresentante);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", itemPedidoRemessa.flOrigemPedido);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", itemPedidoRemessa.nuPedido);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", itemPedidoRemessa.cdProduto);
		sqlWhereClause.addAndCondition("FLTIPOITEMPEDIDO = ", itemPedidoRemessa.flTipoItemPedido);
		sqlWhereClause.addAndCondition("NUSEQPRODUTO = ", itemPedidoRemessa.nuSeqProduto);
		sqlWhereClause.addAndCondition("CDLOCALESTOQUE = ", itemPedidoRemessa.cdLocalEstoque);
		sqlWhereClause.addAndCondition("NUNOTAREMESSA = ", itemPedidoRemessa.nuNotaRemessa);
		sqlWhereClause.addAndCondition("NUSERIEREMESSA = ", itemPedidoRemessa.nuSerieRemessa);
		//--
		sql.append(sqlWhereClause.getSql());
    }

	public String findAllByPedidoRS(ItemPedidoRemessa itemPedidoRemessa ) throws SQLException {
		StringBuffer sql = getSqlBuffer();
    	sql.append(" select * from ");
    	sql.append(tableName);
    	sql.append(" tb ");
    	addWhereByExample(itemPedidoRemessa, sql);
    	return sql.toString();
	}

	public ItemPedidoRemessa getItemPedidoRemessaFilter(Pedido pedido) {
		ItemPedidoRemessa itemPedidoRemessa = new ItemPedidoRemessa();
		itemPedidoRemessa.cdEmpresa = pedido.cdEmpresa;
		itemPedidoRemessa.cdRepresentante = pedido.cdRepresentante;
		itemPedidoRemessa.flOrigemPedido = pedido.flOrigemPedido;
		itemPedidoRemessa.nuPedido = pedido.nuPedido;
		return itemPedidoRemessa;
	}
 
	@Override
	protected boolean isNotSaveTabelasEnvio() {
		return true;
	}
}