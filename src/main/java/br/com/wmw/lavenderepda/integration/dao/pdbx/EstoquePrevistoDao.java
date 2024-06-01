package br.com.wmw.lavenderepda.integration.dao.pdbx;


import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.EstoquePrevisto;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class EstoquePrevistoDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new EstoquePrevisto();
	}

    private static EstoquePrevistoDao instance = null;

    public EstoquePrevistoDao() {
        super(EstoquePrevisto.TABLE_NAME);
    }
    
    public static EstoquePrevistoDao getInstance() {
        if (instance == null) {
            instance = new EstoquePrevistoDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        EstoquePrevisto estoquePrevisto = new EstoquePrevisto();
        estoquePrevisto.rowKey = rs.getString("rowkey");
        estoquePrevisto.cdEmpresa = rs.getString("cdEmpresa");
        estoquePrevisto.cdRepresentante = rs.getString("cdRepresentante");
        estoquePrevisto.cdProduto = rs.getString("cdProduto");
        estoquePrevisto.cdItemGrade1 = rs.getString("cdItemGrade1");
        estoquePrevisto.cdItemGrade2 = rs.getString("cdItemGrade2");
        estoquePrevisto.cdItemGrade3 = rs.getString("cdItemGrade3");
        estoquePrevisto.cdLocalEstoque = rs.getString("cdLocalEstoque");
        estoquePrevisto.flOrigemEstoque = rs.getString("flOrigemEstoque");
        estoquePrevisto.dtEstoque = rs.getDate("dtEstoque");
        estoquePrevisto.qtEstoque = ValueUtil.round(rs.getDouble("qtEstoque"));
        estoquePrevisto.nuCarimbo = rs.getInt("nuCarimbo");
        estoquePrevisto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        estoquePrevisto.cdUsuario = rs.getString("cdUsuario");
        estoquePrevisto.dtAlteracao = rs.getDate("dtAlteracao");
        estoquePrevisto.hrAlteracao = rs.getString("hrAlteracao");
        return estoquePrevisto;
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
        sql.append(" CDPRODUTO,");
        sql.append(" CDITEMGRADE1,");
        sql.append(" CDITEMGRADE2,");
        sql.append(" CDITEMGRADE3,");
        sql.append(" CDLOCALESTOQUE,");
        sql.append(" FLORIGEMESTOQUE,");
        sql.append(" DTESTOQUE,");
        sql.append(" QTESTOQUE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) { }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) { }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) { }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        EstoquePrevisto estoquePrevisto = (EstoquePrevisto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", estoquePrevisto.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", estoquePrevisto.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", estoquePrevisto.cdProduto);
		sqlWhereClause.addAndCondition("CDITEMGRADE1 = ", estoquePrevisto.cdItemGrade1);
		sqlWhereClause.addAndCondition("CDITEMGRADE2 = ", estoquePrevisto.cdItemGrade2);
		sqlWhereClause.addAndCondition("CDITEMGRADE3 = ", estoquePrevisto.cdItemGrade3);
		sqlWhereClause.addAndCondition("CDLOCALESTOQUE = ", estoquePrevisto.cdLocalEstoque);
		sqlWhereClause.addAndCondition("FLORIGEMESTOQUE = ", estoquePrevisto.flOrigemEstoque);
		sqlWhereClause.addAndCondition("DTESTOQUE = ", estoquePrevisto.dtEstoque);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    public Double getQtdEstoquePrevisto(EstoquePrevisto estoquePrevistoFilter) throws SQLException {
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(getScriptQtdEstoquePrevisto(estoquePrevistoFilter).toString())) {
    		if (rs.next()) {
    			return rs.getDouble(1);
        	}
		}
    	return null;
    }

	private StringBuffer getScriptQtdEstoquePrevisto(EstoquePrevisto estoquePrevistoFilter) {
		StringBuffer sql = new StringBuffer();
    	sql.append(" SELECT ");
    	sql.append(" ESTOQUEPREVISTO.QTESTOQUE - (SELECT ");
    	sql.append("   IFNULL(SUM(QTITEMFISICO), 0)  QTDE ");
    	sql.append(" FROM TBLVPITEMPEDIDO ITEMPEDIDO ");
    	sql.append(" WHERE ITEMPEDIDO.CDEMPRESA = ").append(Sql.getValue(estoquePrevistoFilter.cdEmpresa));
    	sql.append(" AND ITEMPEDIDO.CDREPRESENTANTE = ").append(Sql.getValue(estoquePrevistoFilter.cdRepresentante));
    	sql.append(" AND ITEMPEDIDO.CDPRODUTO = ").append(Sql.getValue(estoquePrevistoFilter.cdProduto));
    	sql.append(" AND ITEMPEDIDO.FLORIGEMPEDIDO = ").append(Sql.getValue(OrigemPedido.FLORIGEMPEDIDO_PDA));
		sql.append(" AND ITEMPEDIDO.NUPEDIDO IN (SELECT ");
		sql.append("   PEDIDO.NUPEDIDO ");
		sql.append(" FROM TBLVPPEDIDO PEDIDO ");
		sql.append(" WHERE PEDIDO.CDEMPRESA = ITEMPEDIDO.CDEMPRESA ");
		sql.append(" AND PEDIDO.CDREPRESENTANTE = ITEMPEDIDO.CDREPRESENTANTE ");
		sql.append(" AND PEDIDO.NUPEDIDO = ITEMPEDIDO.NUPEDIDO ");
		sql.append(" AND PEDIDO.FLORIGEMPEDIDO = ITEMPEDIDO.FLORIGEMPEDIDO ");
		sql.append(" AND (PEDIDO.CDSTATUSPEDIDO = " ).append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoFechado)).append(" OR PEDIDO.CDSTATUSPEDIDO = ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoTransmitido)).append(")");
    	sql.append(" AND PEDIDO.DTENTREGA <= ").append(getSubSQLCalculaDataEntregaPedidoEstimada(estoquePrevistoFilter)).append("))");
    	
    	sql.append(" QTDE FROM TBLVPESTOQUEPREVISTO ESTOQUEPREVISTO ");
    	addWhereByExample(estoquePrevistoFilter, sql);
    	return sql;
	}
	
	private String getSubSQLCalculaDataEntregaPedidoEstimada(EstoquePrevisto estoquePrevisto) {
		StringBuffer sql = new StringBuffer();
		sql.append(" (SELECT DATE(MAX(DTESTOQUE), 'START OF DAY', '");
		sql.append(LavenderePdaConfig.nuDiasPrevisaoEntrega).append(" DAY',");
		sql.append("'-").append(EstoquePrevisto.NUDIAS_FIXO).append(" DAY')");
		
		sql.append(" FROM TBLVPESTOQUEPREVISTO ESTOQUEPREVISTO ");
		
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("ESTOQUEPREVISTO.CDEMPRESA = ", estoquePrevisto.cdEmpresa);
		sqlWhereClause.addAndCondition("ESTOQUEPREVISTO.CDREPRESENTANTE = ", estoquePrevisto.cdRepresentante);
		sqlWhereClause.addAndCondition("ESTOQUEPREVISTO.CDPRODUTO = ", estoquePrevisto.cdProduto);
		sqlWhereClause.addAndCondition("ESTOQUEPREVISTO.CDITEMGRADE1 = ", estoquePrevisto.cdItemGrade1);
		sqlWhereClause.addAndCondition("ESTOQUEPREVISTO.CDITEMGRADE2 = ", estoquePrevisto.cdItemGrade2);
		sqlWhereClause.addAndCondition("ESTOQUEPREVISTO.CDITEMGRADE3 = ", estoquePrevisto.cdItemGrade3);
		sqlWhereClause.addAndCondition("ESTOQUEPREVISTO.CDLOCALESTOQUE = ", estoquePrevisto.cdLocalEstoque);
		sqlWhereClause.addAndCondition("ESTOQUEPREVISTO.FLORIGEMESTOQUE = ", estoquePrevisto.flOrigemEstoque);
		
		sql.append(sqlWhereClause.getSql());
		sql.append(")");
		
		return sql.toString();
	}
    
	public Vector findAllNaoVencidos(EstoquePrevisto estoquePrevisto) throws SQLException {
	   	StringBuffer sql = getSqlBuffer();
	  	sql.append(" select ");
	   	addSelectColumns(estoquePrevisto, sql);
	   	sql.append(" from ");
	   	sql.append(tableName);
	   	sql.append(" tb ");
	   	addJoin(estoquePrevisto, sql);
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", estoquePrevisto.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", estoquePrevisto.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", estoquePrevisto.cdProduto);
		sqlWhereClause.addAndCondition("CDITEMGRADE1 = ", estoquePrevisto.cdItemGrade1);
		sqlWhereClause.addAndCondition("CDITEMGRADE2 = ", estoquePrevisto.cdItemGrade2);
		sqlWhereClause.addAndCondition("CDITEMGRADE3 = ", estoquePrevisto.cdItemGrade3);
		sqlWhereClause.addAndCondition("CDLOCALESTOQUE = ", estoquePrevisto.cdLocalEstoque);
		sqlWhereClause.addAndCondition("FLORIGEMESTOQUE = ", estoquePrevisto.flOrigemEstoque);
		sqlWhereClause.addAndCondition("tb.DTESTOQUE >= ", estoquePrevisto.dtEstoque);
		sql.append(sqlWhereClause.getSql());
	   	addGroupBy(sql);
	   	addOrderBy(sql, estoquePrevisto);
	   	addLimit(sql, estoquePrevisto);
	   	return findAll(estoquePrevisto, sql.toString());
	}
	
}