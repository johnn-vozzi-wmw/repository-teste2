package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ItemGrade;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGradeErpDif;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ItemPedidoGradeErpDifDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemPedidoGradeErpDif();
	}

    private static ItemPedidoGradeErpDifDbxDao instance;

    public ItemPedidoGradeErpDifDbxDao() {
        super(ItemPedidoGradeErpDif.TABLE_NAME);
    }
    
    public static ItemPedidoGradeErpDifDbxDao getInstance() {
        if (instance == null) {
            instance = new ItemPedidoGradeErpDifDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	ItemPedidoGradeErpDif itemPedidoGradeErpDif = new ItemPedidoGradeErpDif();
    	itemPedidoGradeErpDif.rowKey = rs.getString("rowkey");
    	itemPedidoGradeErpDif.cdEmpresa = rs.getString("cdEmpresa");
    	itemPedidoGradeErpDif.cdRepresentante = rs.getString("cdRepresentante");
    	itemPedidoGradeErpDif.flOrigemPedido = rs.getString("flOrigemPedido");
    	itemPedidoGradeErpDif.nuPedido = rs.getString("nuPedido");
    	itemPedidoGradeErpDif.cdProduto = rs.getString("cdProduto");
    	itemPedidoGradeErpDif.flTipoItemPedido = rs.getString("flTipoItemPedido");
    	itemPedidoGradeErpDif.nuSeqProduto = rs.getInt("nuSeqProduto");
    	itemPedidoGradeErpDif.cdItemGradeOrg1 = rs.getString("cdItemGradeOrg1");
    	itemPedidoGradeErpDif.cdItemGradeErp1 = rs.getString("cdItemGradeErp1");
    	itemPedidoGradeErpDif.cdItemGradeOrg2 = rs.getString("cdItemGradeOrg2");
    	itemPedidoGradeErpDif.cdItemGradeErp2 = rs.getString("cdItemGradeErp2");
    	itemPedidoGradeErpDif.cdItemGradeOrg3 = rs.getString("cdItemGradeOrg3");
    	itemPedidoGradeErpDif.cdItemGradeErp3 = rs.getString("cdItemGradeErp3");
    	itemPedidoGradeErpDif.qtItemFisicoOrg = rs.getDouble("qtItemFisicoOrg");
    	itemPedidoGradeErpDif.qtItemFisicoErp = rs.getDouble("qtItemFisicoErp");
    	itemPedidoGradeErpDif.flEmailEnviado = rs.getString("flEmailEnviado");
    	itemPedidoGradeErpDif.dsObservacaoOrg = rs.getString("dsObservacaoOrg");
    	itemPedidoGradeErpDif.dsObservacaoErp = rs.getString("dsObservacaoErp");
    	itemPedidoGradeErpDif.cdUnidade = rs.getString("cdUnidade");
    	itemPedidoGradeErpDif.nuCarimbo = rs.getInt("nuCarimbo");
    	itemPedidoGradeErpDif.flTipoAlteracao = rs.getString("flTipoAlteracao");
    	itemPedidoGradeErpDif.cdUsuario = rs.getString("cdUsuario");
        return itemPedidoGradeErpDif;
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
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOITEMPEDIDO,");
        sql.append(" NUSEQPRODUTO,");
        sql.append(" CDITEMGRADEORG1,");
        sql.append(" CDITEMGRADEERP1,");
        sql.append(" CDITEMGRADEORG2,");
        sql.append(" CDITEMGRADEERP2,");
        sql.append(" CDITEMGRADEORG3,");
        sql.append(" CDITEMGRADEERP3,");
        sql.append(" NUSEQITEMPEDIDO,");
        sql.append(" QTITEMFISICOORG,");
        sql.append(" QTITEMFISICOERP,");
        sql.append(" FLEMAILENVIADO,");
        sql.append(" DSOBSERVACAOORG,");
        sql.append(" DSOBSERVACAOERP,");
        sql.append(" CDUNIDADE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOITEMPEDIDO,");
        sql.append(" NUSEQPRODUTO,");
        sql.append(" CDITEMGRADEORG1,");
        sql.append(" CDITEMGRADEERP1,");
        sql.append(" CDITEMGRADEORG2,");
        sql.append(" CDITEMGRADEERP2,");
        sql.append(" CDITEMGRADEORG3,");
        sql.append(" CDITEMGRADEERP3,");
        sql.append(" NUSEQITEMPEDIDO,");
        sql.append(" QTITEMFISICOORG,");
        sql.append(" QTITEMFISICOERP,");
        sql.append(" FLEMAILENVIADO,");
        sql.append(" DSOBSERVACAOORG,");
        sql.append(" DSOBSERVACAOERP,");
        sql.append(" CDUNIDADE,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	ItemPedidoGradeErpDif itemPedidoGradeErpDif = (ItemPedidoGradeErpDif) domain;
    	sql.append(Sql.getValue(itemPedidoGradeErpDif.cdEmpresa)).append(",");
    	sql.append(Sql.getValue(itemPedidoGradeErpDif.cdRepresentante)).append(",");
    	sql.append(Sql.getValue(itemPedidoGradeErpDif.flOrigemPedido)).append(",");
    	sql.append(Sql.getValue(itemPedidoGradeErpDif.nuPedido)).append(",");
    	sql.append(Sql.getValue(itemPedidoGradeErpDif.cdProduto)).append(",");
    	sql.append(Sql.getValue(itemPedidoGradeErpDif.flTipoItemPedido)).append(",");
    	sql.append(Sql.getValue(itemPedidoGradeErpDif.nuSeqProduto)).append(",");
    	sql.append(Sql.getValue(itemPedidoGradeErpDif.cdItemGradeOrg1)).append(",");
    	sql.append(Sql.getValue(itemPedidoGradeErpDif.cdItemGradeErp1)).append(",");
    	sql.append(Sql.getValue(itemPedidoGradeErpDif.cdItemGradeOrg2)).append(",");
    	sql.append(Sql.getValue(itemPedidoGradeErpDif.cdItemGradeErp2)).append(",");
    	sql.append(Sql.getValue(itemPedidoGradeErpDif.cdItemGradeOrg3)).append(",");
    	sql.append(Sql.getValue(itemPedidoGradeErpDif.cdItemGradeErp3)).append(",");
    	sql.append(Sql.getValue(itemPedidoGradeErpDif.qtItemFisicoOrg)).append(",");
    	sql.append(Sql.getValue(itemPedidoGradeErpDif.qtItemFisicoErp)).append(",");
    	sql.append(Sql.getValue(itemPedidoGradeErpDif.flEmailEnviado)).append(",");
    	sql.append(Sql.getValue(itemPedidoGradeErpDif.dsObservacaoOrg)).append(",");
    	sql.append(Sql.getValue(itemPedidoGradeErpDif.dsObservacaoErp)).append(",");
    	sql.append(Sql.getValue(itemPedidoGradeErpDif.cdUnidade)).append(",");
    	sql.append(Sql.getValue(itemPedidoGradeErpDif.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	ItemPedidoGradeErpDif itemPedidoGradeErpDif = (ItemPedidoGradeErpDif) domain;
        sql.append(" QTITEMFISICOORG = ").append(Sql.getValue(itemPedidoGradeErpDif.qtItemFisicoOrg)).append(",");
        sql.append(" QTITEMFISICOERP = ").append(Sql.getValue(itemPedidoGradeErpDif.qtItemFisicoErp)).append(",");
        sql.append(" DSOBSERVACAOORG = ").append(Sql.getValue(itemPedidoGradeErpDif.dsObservacaoOrg)).append(",");
        sql.append(" DSOBSERVACAOERP = ").append(Sql.getValue(itemPedidoGradeErpDif.dsObservacaoErp)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(itemPedidoGradeErpDif.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(itemPedidoGradeErpDif.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(itemPedidoGradeErpDif.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		ItemPedidoGradeErpDif itemPedidoGradeErpDif = (ItemPedidoGradeErpDif) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", itemPedidoGradeErpDif.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", itemPedidoGradeErpDif.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.FLORIGEMPEDIDO = ", itemPedidoGradeErpDif.flOrigemPedido);
		sqlWhereClause.addAndCondition("tb.NUPEDIDO = ", itemPedidoGradeErpDif.nuPedido);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", itemPedidoGradeErpDif.cdProduto);
		sqlWhereClause.addAndCondition("tb.FLTIPOITEMPEDIDO = ", itemPedidoGradeErpDif.flTipoItemPedido);
		sqlWhereClause.addAndCondition("tb.NUSEQPRODUTO = ", itemPedidoGradeErpDif.nuSeqProduto);
		sqlWhereClause.addAndCondition("tb.CDITEMGRADEORG1 = ", itemPedidoGradeErpDif.cdItemGradeOrg1);
		sqlWhereClause.addAndCondition("tb.CDITEMGRADEERP1 = ", itemPedidoGradeErpDif.cdItemGradeErp1);
		sqlWhereClause.addAndCondition("tb.CDITEMGRADEORG2 = ", itemPedidoGradeErpDif.cdItemGradeOrg2);
		sqlWhereClause.addAndCondition("tb.CDITEMGRADEERP2 = ", itemPedidoGradeErpDif.cdItemGradeErp2);
		sqlWhereClause.addAndCondition("tb.CDITEMGRADEORG3 = ", itemPedidoGradeErpDif.cdItemGradeOrg3);
		sqlWhereClause.addAndCondition("tb.CDITEMGRADEERP3 = ", itemPedidoGradeErpDif.cdItemGradeErp3);
		sqlWhereClause.addAndCondition("tb.QTITEMFISICOORG = ", itemPedidoGradeErpDif.qtItemFisicoOrg);
		sqlWhereClause.addAndCondition("tb.QTITEMFISICOERP = ", itemPedidoGradeErpDif.qtItemFisicoErp);
		sqlWhereClause.addAndCondition("tb.FLEMAILENVIADO = ", itemPedidoGradeErpDif.flEmailEnviado);
		sqlWhereClause.addAndCondition("tb.DSOBSERVACAOORG = ", itemPedidoGradeErpDif.dsObservacaoOrg);
		sqlWhereClause.addAndCondition("tb.DSOBSERVACAOERP = ", itemPedidoGradeErpDif.dsObservacaoErp);
		sqlWhereClause.addAndCondition("tb.CDUNIDADE = ", itemPedidoGradeErpDif.cdUnidade);
		sqlWhereClause.addAndCondition("tb.CDUSUARIO = ", itemPedidoGradeErpDif.cdUsuario);
		//--
		sql.append(sqlWhereClause.getSql());
    }
  
    public Vector findAllItemPedidoGradePorItemPedido(ItemPedidoGradeErpDif itemPedidoGradeErpDifFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT DISTINCT tb.*, ITEMGRADE1.CDTIPOITEMGRADE CDTIPOITEMGRADE1, ITEMGRADE2.CDTIPOITEMGRADE CDTIPOITEMGRADE2, ITEMGRADE3.CDTIPOITEMGRADE CDTIPOITEMGRADE3, ITEMGRADE1.DSITEMGRADE ITEMGRADE1, ITEMGRADE2.DSITEMGRADE ITEMGRADE2, ITEMGRADE3.DSITEMGRADE ITEMGRADE3, ITEMGRADE1.NUSEQUENCIA NUSEQUENCIA1, ITEMGRADE2.NUSEQUENCIA NUSEQUENCIA2, ITEMGRADE3.NUSEQUENCIA NUSEQUENCIA3, PRODUTOGRADE.NUCODIGOBARRAS, PRODUTOGRADE.CDITEMGRADE1 CDITEMGRADE1, PRODUTOGRADE.CDITEMGRADE2 CDITEMGRADE2, PRODUTOGRADE.CDITEMGRADE3 CDITEMGRADE3 ")
		   .append(" FROM TBLVPITEMPEDIDOGRADEERPDIF tb ")
		   .append(" JOIN TBLVPPRODUTOGRADE PRODUTOGRADE ON ") 
		   .append(" PRODUTOGRADE.CDEMPRESA = tb.CDEMPRESA ") 
		   .append(" AND PRODUTOGRADE.CDPRODUTO = tb.CDPRODUTO ")
		   .append(" AND ( ")
		   		.append(" (PRODUTOGRADE.CDITEMGRADE1 = CASE WHEN(tb.CDITEMGRADEORG1 == '0') THEN tb.CDITEMGRADEERP1 ELSE tb.CDITEMGRADEORG1 END) ")
		   	.append(" AND (PRODUTOGRADE.CDITEMGRADE2 = CASE WHEN(tb.CDITEMGRADEORG2 == '0') THEN tb.CDITEMGRADEERP2 ELSE tb.CDITEMGRADEORG2 END) ")
		   	.append(" AND (PRODUTOGRADE.CDITEMGRADE3 = CASE WHEN(tb.CDITEMGRADEORG3 == '0') THEN tb.CDITEMGRADEERP3 ELSE tb.CDITEMGRADEORG3 END) ")
		   .append(" ) ") 
		   .append(" LEFT JOIN TBLVPITEMGRADE ITEMGRADE1 ON ") 
		       .append(" ITEMGRADE1.CDEMPRESA = PRODUTOGRADE.CDEMPRESA ") 
		   .append(" AND ITEMGRADE1.CDREPRESENTANTE = PRODUTOGRADE.CDREPRESENTANTE ") 
		   .append(" AND ITEMGRADE1.CDTIPOITEMGRADE = PRODUTOGRADE.CDTIPOITEMGRADE1 ") 
		   .append(" AND ITEMGRADE1.CDITEMGRADE = PRODUTOGRADE.CDITEMGRADE1 ")
		   .append(" LEFT JOIN TBLVPITEMGRADE ITEMGRADE2 ON ")
		       .append(" ITEMGRADE2.CDEMPRESA = PRODUTOGRADE.CDEMPRESA ") 
		   .append(" AND ITEMGRADE2.CDREPRESENTANTE = PRODUTOGRADE.CDREPRESENTANTE ") 
		   .append(" AND ITEMGRADE2.CDTIPOITEMGRADE = PRODUTOGRADE.CDTIPOITEMGRADE2 ") 
		   .append(" AND ITEMGRADE2.CDITEMGRADE = PRODUTOGRADE.CDITEMGRADE2 ")
		   .append(" LEFT JOIN TBLVPITEMGRADE ITEMGRADE3 ON ") 
		   	   .append(" ITEMGRADE3.CDEMPRESA = PRODUTOGRADE.CDEMPRESA ") 
		   .append(" AND ITEMGRADE3.CDREPRESENTANTE = PRODUTOGRADE.CDREPRESENTANTE ") 
		   .append(" AND ITEMGRADE3.CDTIPOITEMGRADE = PRODUTOGRADE.CDTIPOITEMGRADE3 ") 
		   .append(" AND ITEMGRADE3.CDITEMGRADE = PRODUTOGRADE.CDITEMGRADE3 ");
		addWhereByExample(itemPedidoGradeErpDifFilter, sql);
		sql.append(" ORDER BY ITEMGRADE1.NUSEQUENCIA ASC, ITEMGRADE2.NUSEQUENCIA ASC, ITEMGRADE3.NUSEQUENCIA ASC");
		Vector itemPedidoGradeList = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
        	ItemPedidoGradeErpDif itemPedidoGradeResult;
			while (rs.next()) {
				itemPedidoGradeResult = new ItemPedidoGradeErpDif();
				itemPedidoGradeResult.cdEmpresa = itemPedidoGradeErpDifFilter.cdEmpresa;
				itemPedidoGradeResult.cdRepresentante = itemPedidoGradeErpDifFilter.cdRepresentante;
				itemPedidoGradeResult.cdProduto = itemPedidoGradeErpDifFilter.cdProduto;
				itemPedidoGradeResult.flOrigemPedido = itemPedidoGradeErpDifFilter.flOrigemPedido;
				itemPedidoGradeResult.nuPedido = itemPedidoGradeErpDifFilter.nuPedido;
				itemPedidoGradeResult.flTipoItemPedido = itemPedidoGradeErpDifFilter.flTipoItemPedido;
				itemPedidoGradeResult.nuSeqProduto = itemPedidoGradeErpDifFilter.nuSeqProduto;
				itemPedidoGradeResult.qtItemFisicoOrg = rs.getDouble("QTITEMFISICOORG");
				itemPedidoGradeResult.qtItemFisicoErp = rs.getDouble("QTITEMFISICOERP");
				itemPedidoGradeResult.cdItemGradeOrg1 = rs.getString("CDITEMGRADEORG1");
				itemPedidoGradeResult.cdItemGradeOrg1 = rs.getString("CDITEMGRADEERP1");
				itemPedidoGradeResult.cdItemGradeOrg2 = rs.getString("CDITEMGRADEORG2");
				itemPedidoGradeResult.cdItemGradeOrg2 = rs.getString("CDITEMGRADEERP2");
				itemPedidoGradeResult.cdItemGradeOrg3 = rs.getString("CDITEMGRADEORG3");
				itemPedidoGradeResult.cdItemGradeOrg3 = rs.getString("CDITEMGRADEERP3");
				itemPedidoGradeResult.itemGrade1 = new ItemGrade();
				itemPedidoGradeResult.itemGrade1.dsItemGrade = rs.getString("ITEMGRADE1");
				itemPedidoGradeResult.itemGrade1.nuSequencia = rs.getInt("NUSEQUENCIA1");
				itemPedidoGradeResult.itemGrade1.cdTipoItemGrade = rs.getString("CDTIPOITEMGRADE1");
				itemPedidoGradeResult.itemGrade2 = new ItemGrade();
				itemPedidoGradeResult.itemGrade2.dsItemGrade = rs.getString("ITEMGRADE2");
				itemPedidoGradeResult.itemGrade2.nuSequencia = rs.getInt("NUSEQUENCIA2");
				itemPedidoGradeResult.itemGrade2.cdTipoItemGrade = rs.getString("CDTIPOITEMGRADE2");
				itemPedidoGradeResult.itemGrade3 = new ItemGrade();
				itemPedidoGradeResult.itemGrade3.dsItemGrade = rs.getString("ITEMGRADE3");
				itemPedidoGradeResult.itemGrade3.nuSequencia = rs.getInt("NUSEQUENCIA3");
				itemPedidoGradeResult.itemGrade3.cdTipoItemGrade = rs.getString("CDTIPOITEMGRADE3");
				itemPedidoGradeResult.produtoGrade = new ProdutoGrade();
				itemPedidoGradeResult.produtoGrade.nuCodigoBarras = rs.getString("NUCODIGOBARRAS");
				itemPedidoGradeList.addElement(itemPedidoGradeResult);
	        }
		}
    	return itemPedidoGradeList;
    }

}