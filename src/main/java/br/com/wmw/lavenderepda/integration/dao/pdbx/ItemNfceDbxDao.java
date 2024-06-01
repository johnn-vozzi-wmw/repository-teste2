package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import totalcross.sql.ResultSet;
import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ItemNfce;

public class ItemNfceDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemNfce();
	}

    private static ItemNfceDbxDao instance = null;
	public static String TABLE_NAME = "TBLVPITEMNFCE";

    public ItemNfceDbxDao() {
        super(TABLE_NAME);
    }
    
    public static ItemNfceDbxDao getInstance() {
        if (instance == null) {
            instance = new ItemNfceDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        ItemNfce itemNfce = new ItemNfce();
        itemNfce.rowKey = rs.getString("rowkey");
        itemNfce.cdEmpresa = rs.getString("cdEmpresa");
        itemNfce.cdRepresentante = rs.getString("cdRepresentante");
        itemNfce.nuPedido = rs.getString("nuPedido");
        itemNfce.flOrigemPedido = rs.getString("flOrigemPedido");
        itemNfce.cdProduto = rs.getString("cdProduto");
        itemNfce.nuSeqProduto = rs.getInt("nuSeqProduto");
        itemNfce.qtItemFisico = ValueUtil.round(rs.getDouble("qtItemFisico"));
        itemNfce.cdUnidade = rs.getString("cdUnidade");
        itemNfce.vlUnitario = ValueUtil.round(rs.getDouble("vlUnitario"));
        itemNfce.vlTotalItem = ValueUtil.round(rs.getDouble("vlTotalItem"));
        itemNfce.cdUsuario = rs.getString("cdUsuario");
        itemNfce.nuCarimbo = rs.getInt("nuCarimbo");
        itemNfce.flTipoAlteracao = rs.getString("flTipoAlteracao");
        itemNfce.dtAlteracao = rs.getDate("dtAlteracao");
        itemNfce.hrAlteracao = rs.getString("hrAlteracao");
        return itemNfce;
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
        sql.append(" NUPEDIDO,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" CDPRODUTO,");
        sql.append(" NUSEQPRODUTO,");
        sql.append(" QTITEMFISICO,");
        sql.append(" CDUNIDADE,");
        sql.append(" VLUNITARIO,");
        sql.append(" VLTOTALITEM,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" NUPEDIDO,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" CDPRODUTO,");
        sql.append(" NUSEQPRODUTO,");
        sql.append(" QTITEMFISICO,");
        sql.append(" CDUNIDADE,");
        sql.append(" VLUNITARIO,");
        sql.append(" VLTOTALITEM,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        ItemNfce itemNfce = (ItemNfce) domain;
        sql.append(Sql.getValue(itemNfce.cdEmpresa)).append(",");
        sql.append(Sql.getValue(itemNfce.cdRepresentante)).append(",");
        sql.append(Sql.getValue(itemNfce.nuPedido)).append(",");
        sql.append(Sql.getValue(itemNfce.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(itemNfce.cdProduto)).append(",");
        sql.append(Sql.getValue(itemNfce.nuSeqProduto)).append(",");
        sql.append(Sql.getValue(itemNfce.qtItemFisico)).append(",");
        sql.append(Sql.getValue(itemNfce.cdUnidade)).append(",");
        sql.append(Sql.getValue(itemNfce.vlUnitario)).append(",");
        sql.append(Sql.getValue(itemNfce.vlTotalItem)).append(",");
        sql.append(Sql.getValue(itemNfce.cdUsuario)).append(",");
        sql.append(Sql.getValue(itemNfce.nuCarimbo)).append(",");
        sql.append(Sql.getValue(itemNfce.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(itemNfce.dtAlteracao)).append(",");
        sql.append(Sql.getValue(itemNfce.hrAlteracao));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        ItemNfce itemNfce = (ItemNfce) domain;
        sql.append(" QTITEMFISICO = ").append(Sql.getValue(itemNfce.qtItemFisico)).append(",");
        sql.append(" CDUNIDADE = ").append(Sql.getValue(itemNfce.cdUnidade)).append(",");
        sql.append(" VLUNITARIO = ").append(Sql.getValue(itemNfce.vlUnitario)).append(",");
        sql.append(" VLTOTALITEM = ").append(Sql.getValue(itemNfce.vlTotalItem)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(itemNfce.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(itemNfce.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(itemNfce.flTipoAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(itemNfce.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(itemNfce.hrAlteracao));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        ItemNfce itemNfce = (ItemNfce) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", itemNfce.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", itemNfce.cdRepresentante);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", itemNfce.nuPedido);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", itemNfce.flOrigemPedido);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", itemNfce.cdProduto);
		sqlWhereClause.addAndCondition("NUSEQPRODUTO = ", itemNfce.nuSeqProduto);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}