package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoErpDif;
import br.com.wmw.lavenderepda.business.domain.Produto;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ItemPedidoErpDifPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemPedidoErpDif();
	}

    private static ItemPedidoErpDifPdbxDao instance;

    public ItemPedidoErpDifPdbxDao() {
        super(ItemPedidoErpDif.TABLE_NAME);
    }

    public static ItemPedidoErpDifPdbxDao getInstance() {
        if (instance == null) {
            instance = new ItemPedidoErpDifPdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { 
    	sql.append(" CDEMPRESA,")
    	.append(" CDREPRESENTANTE,")
    	.append(" FLORIGEMPEDIDO,")
    	.append(" NUPEDIDO,")
    	.append(" CDPRODUTO,")
    	.append(" FLTIPOITEMPEDIDO,")
    	.append(" NUSEQPRODUTO,")
    	.append(" QTITEMFISICOORG,")
    	.append(" QTITEMFISICOERP,")
    	.append(" DSOBSERVACAOORG,")
    	.append(" DSOBSERVACAOERP,")
    	.append(" CDUNIDADE,")
    	.append(" NUSEQITEMPEDIDO");
    }
    
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { 
    	ItemPedidoErpDif itemPedidoErpDif = (ItemPedidoErpDif) domain;
    	sql.append(Sql.getValue(itemPedidoErpDif.cdEmpresa)).append(",")
    	.append(Sql.getValue(itemPedidoErpDif.cdRepresentante)).append(",")
    	.append(Sql.getValue(itemPedidoErpDif.flOrigemPedido)).append(",")
    	.append(Sql.getValue(itemPedidoErpDif.nuPedido)).append(",")
    	.append(Sql.getValue(itemPedidoErpDif.cdProduto)).append(",")
    	.append(Sql.getValue(itemPedidoErpDif.flTipoItemPedido)).append(",")
    	.append(Sql.getValue(itemPedidoErpDif.nuSeqProduto)).append(",")
    	.append(Sql.getValue(itemPedidoErpDif.qtItemfisicoOrg)).append(",")
    	.append(Sql.getValue(itemPedidoErpDif.qtItemFisicoErp)).append(",")
    	.append(Sql.getValue(itemPedidoErpDif.dsObservacaoOrg)).append(",")
    	.append(Sql.getValue(itemPedidoErpDif.dsObservacaoErp)).append(",")
    	.append(Sql.getValue(itemPedidoErpDif.cdUnidade)).append(",")
    	.append(Sql.getValue(itemPedidoErpDif.nuSeqItemPedido));
    }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.FLORIGEMPEDIDO,");
        sql.append(" tb.NUPEDIDO,");
        sql.append(" tb.CDPRODUTO,");
        sql.append(" tb.FLTIPOITEMPEDIDO,");
        sql.append(" tb.NUSEQPRODUTO,");
        sql.append(" tb.QTITEMFISICOORG,");
        sql.append(" tb.QTITEMFISICOERP,");
        sql.append(" tb.DSOBSERVACAOORG,");
        sql.append(" tb.CDUNIDADE,");
        sql.append(" tb.DSOBSERVACAOERP");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ItemPedidoErpDif itemPedidoErpDif = new ItemPedidoErpDif();
        itemPedidoErpDif.rowKey = rs.getString("rowkey");
        itemPedidoErpDif.cdEmpresa = rs.getString("cdEmpresa");
        itemPedidoErpDif.cdRepresentante = rs.getString("cdRepresentante");
        itemPedidoErpDif.flOrigemPedido = rs.getString("flOrigemPedido");
        itemPedidoErpDif.nuPedido = rs.getString("nuPedido");
        itemPedidoErpDif.cdProduto = rs.getString("cdProduto");
        itemPedidoErpDif.flTipoItemPedido = rs.getString("flTipoItemPedido");
        itemPedidoErpDif.nuSeqProduto = rs.getInt("nuSeqProduto");
        itemPedidoErpDif.qtItemfisicoOrg = ValueUtil.round(rs.getDouble("qtItemfisicoorg"));
        itemPedidoErpDif.qtItemFisicoErp = ValueUtil.round(rs.getDouble("qtItemFisicoErp"));
        itemPedidoErpDif.dsObservacaoOrg = rs.getString("dsObservacaoOrg");
        itemPedidoErpDif.dsObservacaoErp = rs.getString("dsObservacaoErp");
        itemPedidoErpDif.cdUnidade = rs.getString("cdUnidade");
        return itemPedidoErpDif;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
	    addWhereExample((ItemPedidoErpDif) domain, sql, ValueUtil.VALOR_NI);
    }

	private void addWhereExample(ItemPedidoErpDif domain, StringBuffer sql, String alias) {
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(alias + "CDEMPRESA = ", domain.cdEmpresa);
		sqlWhereClause.addAndCondition(alias + "CDREPRESENTANTE = ", domain.cdRepresentante);
		sqlWhereClause.addAndCondition(alias + "FLORIGEMPEDIDO = ", domain.flOrigemPedido);
		sqlWhereClause.addAndCondition(alias + "NUPEDIDO = ", domain.nuPedido);
		sqlWhereClause.addAndCondition(alias + "CDPRODUTO = ", domain.cdProduto);
		sqlWhereClause.addAndCondition(alias + "FLTIPOITEMPEDIDO = ", domain.flTipoItemPedido);
		sqlWhereClause.addAndCondition(alias + "NUSEQPRODUTO = ", domain.nuSeqProduto);
		//--
		sql.append(sqlWhereClause.getSql());
	}

	//@Override
    public Vector findAllNaoAlterados() throws java.sql.SQLException {
		BaseDomain domainFilter = getBaseDomainDefault();
		StringBuffer sql = getSqlBuffer();
    	sql.append(" select ");
    	addSelectColumns(domainFilter, sql);
    	sql.append(" from ");
        sql.append(tableName);
        sql.append(" tb ");
    	sql.append(" where fltipoalteracao = ");
    	sql.append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ORIGINAL));
    	addOrderBy(sql, domainFilter);
    	return findAll(domainFilter, sql.toString());
    }

	public void updateItemPedidoDifLidos() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" update ").append(ItemPedidoErpDif.TABLE_NAME).append(" set");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ALTERADO));
		sql.append(" where FLTIPOALTERACAO != ").append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ALTERADO));
		//--
		executeUpdate(sql.toString());
	}

	private void addSelectColumnsByPedidoErp(ItemPedidoErpDif domainFilter, StringBuffer sql) throws SQLException {
		addSelectColumns(domainFilter, sql);
		sql.append(" , ITEMPEDIDOERP.DSPRODUTO ");
	}

	public Vector findAllByPedidoErp(ItemPedidoErpDif domainFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT ");
		addSelectColumnsByPedidoErp(domainFilter, sql);
		sql.append(" FROM ");
		sql.append(tableName);
		sql.append(" tb ");
		sql.append(" LEFT JOIN TBLVPITEMPEDIDOERP ITEMPEDIDOERP ON ");
		sql.append(" TB.CDEMPRESA = ITEMPEDIDOERP.CDEMPRESA");
		sql.append(" AND TB.CDEMPRESA = ITEMPEDIDOERP.CDEMPRESA");
		sql.append(" AND TB.CDREPRESENTANTE = ITEMPEDIDOERP.CDREPRESENTANTE");
		sql.append(" AND TB.FLORIGEMPEDIDO = ITEMPEDIDOERP.FLORIGEMPEDIDO");
		sql.append(" AND TB.NUPEDIDO = ITEMPEDIDOERP.NUPEDIDO");
		sql.append(" AND TB.CDPRODUTO = ITEMPEDIDOERP.CDPRODUTO");
		sql.append(" AND TB.FLTIPOITEMPEDIDO = ITEMPEDIDOERP.FLTIPOITEMPEDIDO");
		sql.append(" AND TB.NUSEQPRODUTO = ITEMPEDIDOERP.NUSEQPRODUTO");
		addWhereExample(domainFilter, sql, "TB.");
		addOrderBy(sql, null);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector list = new Vector();
			ItemPedidoErpDif itemPedidoErpDif;
			Produto produto;
			while (rs.next()) {
				itemPedidoErpDif = (ItemPedidoErpDif) populate(domainFilter, rs);
				produto = new Produto();
				produto.cdProduto = itemPedidoErpDif.cdProduto;
				produto.dsProduto = rs.getString("DSPRODUTO");
				itemPedidoErpDif.produto = produto;
				list.addElement(itemPedidoErpDif);
			}
			return list;
		}
	}

}