package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ItemGrade;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ItemGradeDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemGrade();
	}

    private static ItemGradeDbxDao instance;

    public ItemGradeDbxDao() {
        super(ItemGrade.TABLE_NAME);
    }

    public static ItemGradeDbxDao getInstance() {
        if (instance == null) {
            instance = new ItemGradeDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ItemGrade itemGrade = new ItemGrade();
        itemGrade.rowKey = rs.getString("rowkey");
        itemGrade.cdEmpresa = rs.getString("cdEmpresa");
        itemGrade.cdRepresentante = rs.getString("cdRepresentante");
        itemGrade.cdTipoItemGrade = rs.getString("cdTipoItemGrade");
        itemGrade.cdItemGrade = rs.getString("cdItemGrade");
        itemGrade.dsItemGrade = rs.getString("dsItemGrade");
        itemGrade.nuSequencia = rs.getInt("nuSequencia");
        itemGrade.nuCarimbo = rs.getInt("nuCarimbo");
        itemGrade.flTipoAlteracao = rs.getString("flTipoAlteracao");
        itemGrade.cdUsuario = rs.getString("cdUsuario");
        return itemGrade;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.cdTipoItemGrade,");
        sql.append(" tb.cdItemGrade,");
        sql.append(" tb.dsItemGrade,");
        sql.append(" tb.nuSequencia,");
        sql.append(" tb.nuCarimbo,");
        sql.append(" tb.FLTIPOALTERACAO,");
        sql.append(" tb.cdUsuario");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" cdTipoItemGrade,");
        sql.append(" cdItemGrade,");
        sql.append(" dsItemGrade,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" cdUsuario");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemGrade itemGrade = (ItemGrade) domain;
        sql.append(Sql.getValue(itemGrade.cdEmpresa)).append(",");
        sql.append(Sql.getValue(itemGrade.cdRepresentante)).append(",");
        sql.append(Sql.getValue(itemGrade.cdTipoItemGrade)).append(",");
        sql.append(Sql.getValue(itemGrade.cdItemGrade)).append(",");
        sql.append(Sql.getValue(itemGrade.dsItemGrade)).append(",");
        sql.append(Sql.getValue(itemGrade.nuCarimbo)).append(",");
        sql.append(Sql.getValue(itemGrade.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(itemGrade.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemGrade itemGrade = (ItemGrade) domain;
        sql.append(" dsItemGrade = ").append(Sql.getValue(itemGrade.dsItemGrade)).append(",");
        sql.append(" nuCarimbo = ").append(Sql.getValue(itemGrade.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(itemGrade.flTipoAlteracao)).append(",");
        sql.append(" cdUsuario = ").append(Sql.getValue(itemGrade.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemGrade itemGrade = (ItemGrade) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", itemGrade.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", itemGrade.cdRepresentante);
		sqlWhereClause.addAndCondition("cdTipoItemGrade = ", itemGrade.cdTipoItemGrade);
		sqlWhereClause.addAndCondition("cdItemGrade = ", itemGrade.cdItemGrade);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    public Vector findGradeEstoque3ForList(String cdProduto, String cdItemGrade1, String cdTabelaPreco) throws SQLException {
    	Vector listGrade = new Vector();
    	StringBuffer sql = new StringBuffer();
    	sql.append("SELECT PRODUTOGRADE.CDITEMGRADE2"); 
    	sql.append(",PRODUTOGRADE.CDITEMGRADE3"); 
    	sql.append(",(SELECT dsitemgrade FROM TBLVPITEMGRADE WHERE cditemgrade = PRODUTOGRADE.CDITEMGRADE2) AS DSITEMGRADE2"); 
    	sql.append(",(SELECT dsitemgrade FROM TBLVPITEMGRADE WHERE cditemgrade = PRODUTOGRADE.CDITEMGRADE3) AS DSITEMGRADE3"); 
    	sql.append(" FROM TBLVPPRODUTOGRADE PRODUTOGRADE");
    	sql.append(" WHERE PRODUTOGRADE.CDPRODUTO = ").append(Sql.getValue(cdProduto));
    	sql.append(" AND PRODUTOGRADE.CDITEMGRADE1 = ").append(Sql.getValue(cdItemGrade1));
    	sql.append(" AND PRODUTOGRADE.CDTABELAPRECO = ").append(Sql.getValue(cdTabelaPreco));
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		ItemGrade itemGrade;
	    	while (rs.next()) {
	    		itemGrade = new ItemGrade();
	    		itemGrade.cdItemGrade2 = rs.getString("CDITEMGRADE2");
	    		itemGrade.cdItemGrade3 = rs.getString("CDITEMGRADE3");
	    		itemGrade.dsItemGrade2 = rs.getString("DSITEMGRADE2");
	    		itemGrade.dsItemGrade3 = rs.getString("DSITEMGRADE3");
	    		listGrade.addElement(itemGrade);
	    	}
    	}
    	return listGrade;
    }
    
    public Vector findGradeEstoque2ForList(String cdProduto, String cdTabelaPreco) throws SQLException {
    	Vector listGrade = new Vector();
    	StringBuffer sql = new StringBuffer();
    	sql.append("SELECT PRODUTOGRADE.CDITEMGRADE1"); 
    	sql.append(",PRODUTOGRADE.CDITEMGRADE2"); 
    	sql.append(",(SELECT dsitemgrade FROM TBLVPITEMGRADE WHERE cditemgrade = PRODUTOGRADE.CDITEMGRADE1) AS DSITEMGRADE1"); 
    	sql.append(",(SELECT dsitemgrade FROM TBLVPITEMGRADE WHERE cditemgrade = PRODUTOGRADE.CDITEMGRADE2) AS DSITEMGRADE2"); 
    	sql.append(" FROM TBLVPPRODUTOGRADE PRODUTOGRADE");
    	sql.append(" WHERE PRODUTOGRADE.CDPRODUTO = ").append(Sql.getValue(cdProduto));
    	sql.append(" AND PRODUTOGRADE.CDTABELAPRECO = ").append(Sql.getValue(cdTabelaPreco));
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		ItemGrade itemGrade;
    		while (rs.next()) {
    			itemGrade = new ItemGrade();
    			itemGrade.dsItemGrade1 = rs.getString("DSITEMGRADE1");
    			itemGrade.dsItemGrade2 = rs.getString("DSITEMGRADE2");
    			itemGrade.cdItemGrade1 = rs.getString("CDITEMGRADE1");
    			itemGrade.cdItemGrade2 = rs.getString("CDITEMGRADE2");
    			listGrade.addElement(itemGrade);
    		}
    	}
    	return listGrade;
    }
    
    
    public Vector findAllItemGradeNivel2(ProdutoGrade produtoGrade) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ");
		addSelectColumns(produtoGrade, sql);
		sql.append(" FROM TBLVPITEMGRADE TB ");
		sql.append(" JOIN TBLVPPRODUTOGRADE PROD ON ");
		sql.append(" TB.CDEMPRESA = PROD.CDEMPRESA ");
		sql.append(" AND TB.CDREPRESENTANTE = PROD.CDREPRESENTANTE ");
		sql.append(" AND TB.CDITEMGRADE = PROD.CDITEMGRADE2 ");
		sql.append(" WHERE "); 
		sql.append(" TB.CDEMPRESA = ").append(Sql.getValue(produtoGrade.cdEmpresa)); 
		sql.append(" AND TB.CDREPRESENTANTE = ").append(Sql.getValue(produtoGrade.cdRepresentante)); 
		sql.append(" AND PROD.CDPRODUTO = ").append(Sql.getValue(produtoGrade.cdProduto)); 
		sql.append(" AND PROD.CDITEMGRADE1 = ").append(Sql.getValue(produtoGrade.cdItemGrade1)); 
		sql.append(" GROUP BY CDITEMGRADE2 ");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector itemGradeList = new Vector();
        	while (rs.next()) {
    			itemGradeList.addElement(populate(produtoGrade, rs));
        	}
        	return itemGradeList;
    	}
	}

}