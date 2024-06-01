package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoAud;
import totalcross.sql.ResultSet;

public class ItemPedidoAudDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemPedidoAud();
	}

    private static ItemPedidoAudDbxDao instance;

    public ItemPedidoAudDbxDao() {
        super(ItemPedidoAud.TABLE_NAME);
    }
    
    public static ItemPedidoAudDbxDao getInstance() {
        if (instance == null) {
            instance = new ItemPedidoAudDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ItemPedidoAud itemPedidoAud = new ItemPedidoAud();
        itemPedidoAud.rowKey = rs.getString("rowkey");
        itemPedidoAud.cdEmpresa = rs.getString("cdEmpresa");
        itemPedidoAud.cdRepresentante = rs.getString("cdRepresentante");
        itemPedidoAud.nuPedido = rs.getString("nuPedido");
        itemPedidoAud.flOrigemPedido = rs.getString("flOrigemPedido");
        itemPedidoAud.cdProduto = rs.getString("cdProduto");
        itemPedidoAud.flTipoItemPedido = rs.getString("flTipoItemPedido");
        itemPedidoAud.nuSeqProduto = rs.getInt("nuSeqProduto");
        itemPedidoAud.vlPctMargemItem = ValueUtil.round(rs.getDouble("vlPctMargemItem"));
        itemPedidoAud.vlPctMargemRentabilidade = ValueUtil.round(rs.getDouble("vlPctMargemRentabilidade"));
        itemPedidoAud.vlMargemItem = ValueUtil.round(rs.getDouble("vlMargemItem"));
        itemPedidoAud.vlReceitaVirtual = ValueUtil.round(rs.getDouble("vlReceitaVirtual"));
        itemPedidoAud.vlReceitaLiquida = ValueUtil.round(rs.getDouble("vlReceitaLiquida"));
        itemPedidoAud.vlVerbaEmpresa = ValueUtil.round(rs.getDouble("vlVerbaEmpresa"));
        itemPedidoAud.vlVerbaNecessaria = ValueUtil.round(rs.getDouble("vlVerbaNecessaria"));
        itemPedidoAud.vlPctPis = ValueUtil.round(rs.getDouble("vlPctPis"));
        itemPedidoAud.vlPctIcms = ValueUtil.round(rs.getDouble("vlPctIcms"));
        itemPedidoAud.vlItemPedidoNeutro = ValueUtil.round(rs.getDouble("vlItemPedidoNeutro"));
        itemPedidoAud.dtInsercaoItem = rs.getDate("dtInsercaoItem");
        itemPedidoAud.dtVerificacaoCusto = rs.getDate("dtVerificacaoCusto");
        itemPedidoAud.flGeraVerba = rs.getString("flGeraVerba");
        itemPedidoAud.cdUsuario = rs.getString("cdUsuario");
        itemPedidoAud.nuCarimbo = rs.getInt("nuCarimbo");
        itemPedidoAud.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return itemPedidoAud;
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
        sql.append(" NUPEDIDO,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOITEMPEDIDO,");
        sql.append(" NUSEQPRODUTO,");
        sql.append(" VLPCTMARGEMITEM,");
        sql.append(" VLPCTMARGEMRENTABILIDADE,");
        sql.append(" VLMARGEMITEM,");
        sql.append(" VLRECEITAVIRTUAL,");
        sql.append(" VLRECEITALIQUIDA,");
        sql.append(" VLVERBAEMPRESA,");
        sql.append(" VLVERBANECESSARIA,");
        sql.append(" VLPCTPIS,");
        sql.append(" VLPCTICMS,");
        sql.append(" VLITEMPEDIDONEUTRO,");
        sql.append(" DTINSERCAOITEM,");
        sql.append(" DTVERIFICACAOCUSTO,");
        sql.append(" FLGERAVERBA,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" NUPEDIDO,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOITEMPEDIDO,");
        sql.append(" NUSEQPRODUTO,");
        sql.append(" VLPCTMARGEMITEM,");
        sql.append(" VLPCTMARGEMRENTABILIDADE,");
        sql.append(" VLMARGEMITEM,");
        sql.append(" VLRECEITAVIRTUAL,");
        sql.append(" VLRECEITALIQUIDA,");
        sql.append(" VLVERBAEMPRESA,");
        sql.append(" VLVERBANECESSARIA,");
        sql.append(" VLPCTPIS,");
        sql.append(" VLPCTICMS,");
        sql.append(" VLITEMPEDIDONEUTRO,");
        sql.append(" DTINSERCAOITEM,");
        sql.append(" DTVERIFICACAOCUSTO,");
        sql.append(" FLGERAVERBA,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemPedidoAud itemPedidoAud = (ItemPedidoAud) domain;
        sql.append(Sql.getValue(itemPedidoAud.cdEmpresa)).append(",");
        sql.append(Sql.getValue(itemPedidoAud.cdRepresentante)).append(",");
        sql.append(Sql.getValue(itemPedidoAud.nuPedido)).append(",");
        sql.append(Sql.getValue(itemPedidoAud.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(itemPedidoAud.cdProduto)).append(",");
        sql.append(Sql.getValue(itemPedidoAud.flTipoItemPedido)).append(",");
        sql.append(Sql.getValue(itemPedidoAud.nuSeqProduto)).append(",");
        sql.append(Sql.getValue(itemPedidoAud.vlPctMargemItem)).append(",");
        sql.append(Sql.getValue(itemPedidoAud.vlPctMargemRentabilidade)).append(",");
        sql.append(Sql.getValue(itemPedidoAud.vlMargemItem)).append(",");
        sql.append(Sql.getValue(itemPedidoAud.vlReceitaVirtual)).append(",");
        sql.append(Sql.getValue(itemPedidoAud.vlReceitaLiquida)).append(",");
        sql.append(Sql.getValue(itemPedidoAud.vlVerbaEmpresa)).append(",");
        sql.append(Sql.getValue(itemPedidoAud.vlVerbaNecessaria)).append(",");
        sql.append(Sql.getValue(itemPedidoAud.vlPctPis)).append(",");
        sql.append(Sql.getValue(itemPedidoAud.vlPctIcms)).append(",");
        sql.append(Sql.getValue(itemPedidoAud.vlItemPedidoNeutro)).append(",");
        sql.append(Sql.getValue(itemPedidoAud.dtInsercaoItem)).append(",");
        sql.append(Sql.getValue(itemPedidoAud.dtVerificacaoCusto)).append(",");
        sql.append(Sql.getValue(itemPedidoAud.flGeraVerba)).append(",");
        sql.append(Sql.getValue(itemPedidoAud.cdUsuario)).append(",");
        sql.append(Sql.getValue(itemPedidoAud.nuCarimbo)).append(",");
        sql.append(Sql.getValue(itemPedidoAud.flTipoAlteracao));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemPedidoAud itemPedidoAud = (ItemPedidoAud) domain;
        sql.append(" VLPCTMARGEMITEM = ").append(Sql.getValue(itemPedidoAud.vlPctMargemItem)).append(",");
        sql.append(" VLPCTMARGEMRENTABILIDADE = ").append(Sql.getValue(itemPedidoAud.vlPctMargemRentabilidade)).append(",");
        sql.append(" VLMARGEMITEM = ").append(Sql.getValue(itemPedidoAud.vlMargemItem)).append(",");
        sql.append(" VLRECEITAVIRTUAL = ").append(Sql.getValue(itemPedidoAud.vlReceitaVirtual)).append(",");
        sql.append(" VLRECEITALIQUIDA = ").append(Sql.getValue(itemPedidoAud.vlReceitaLiquida)).append(",");
        sql.append(" VLVERBAEMPRESA = ").append(Sql.getValue(itemPedidoAud.vlVerbaEmpresa)).append(",");
        sql.append(" VLVERBANECESSARIA = ").append(Sql.getValue(itemPedidoAud.vlVerbaNecessaria)).append(",");
        sql.append(" VLPCTPIS = ").append(Sql.getValue(itemPedidoAud.vlPctPis)).append(",");
        sql.append(" VLPCTICMS = ").append(Sql.getValue(itemPedidoAud.vlPctIcms)).append(",");
        sql.append(" VLITEMPEDIDONEUTRO = ").append(Sql.getValue(itemPedidoAud.vlItemPedidoNeutro)).append(",");
        sql.append(" DTINSERCAOITEM = ").append(Sql.getValue(itemPedidoAud.dtInsercaoItem)).append(",");
        sql.append(" DTVERIFICACAOCUSTO = ").append(Sql.getValue(itemPedidoAud.dtVerificacaoCusto)).append(",");
        sql.append(" FLGERAVERBA = ").append(Sql.getValue(itemPedidoAud.flGeraVerba)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(itemPedidoAud.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(itemPedidoAud.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(itemPedidoAud.flTipoAlteracao));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemPedidoAud itemPedidoAud = (ItemPedidoAud) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", itemPedidoAud.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", itemPedidoAud.cdRepresentante);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", itemPedidoAud.nuPedido);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", itemPedidoAud.flOrigemPedido);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", itemPedidoAud.cdProduto);
		sqlWhereClause.addAndCondition("FLTIPOITEMPEDIDO = ", itemPedidoAud.flTipoItemPedido);
		sqlWhereClause.addAndCondition("NUSEQPRODUTO = ", itemPedidoAud.nuSeqProduto);
		//--
		sql.append(sqlWhereClause.getSql());
    }
 
	@Override
	protected boolean isNotSaveTabelasEnvio() {
		return true;
	}
}