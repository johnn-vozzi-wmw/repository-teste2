package br.com.wmw.lavenderepda.integration.dao.pdbx;


import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.EstoquePrevistoGeral;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class EstoquePrevistoGeralDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new EstoquePrevistoGeral();
	}

    private static EstoquePrevistoGeralDao instance = null;

    public EstoquePrevistoGeralDao() {
        super(EstoquePrevistoGeral.TABLE_NAME);
    }
    
    public static EstoquePrevistoGeralDao getInstance() {
        if (instance == null) {
            instance = new EstoquePrevistoGeralDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		EstoquePrevistoGeral estoquePrevistoGeral = new EstoquePrevistoGeral();
        estoquePrevistoGeral.rowKey = rs.getString("rowkey");
        estoquePrevistoGeral.cdEmpresa = rs.getString("cdEmpresa");
        estoquePrevistoGeral.cdRepresentante = rs.getString("cdRepresentante");
        estoquePrevistoGeral.cdProduto = rs.getString("cdProduto");
        estoquePrevistoGeral.cdItemGrade1 = rs.getString("cdItemGrade1");
        estoquePrevistoGeral.cdItemGrade2 = rs.getString("cdItemGrade2");
        estoquePrevistoGeral.cdItemGrade3 = rs.getString("cdItemGrade3");
        estoquePrevistoGeral.cdLocalEstoque = rs.getString("cdLocalEstoque");
        estoquePrevistoGeral.flOrigemEstoque = rs.getString("flOrigemEstoque");
        estoquePrevistoGeral.dtEstoque = rs.getDate("dtEstoque");
        estoquePrevistoGeral.qtEstoque = ValueUtil.round(rs.getDouble("qtEstoque"));
        estoquePrevistoGeral.flEstoqueFisico = rs.getString("flEstoqueFisico");
        return estoquePrevistoGeral;
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
        sql.append(" FLESTOQUEFISICO,");
        sql.append(" CDUSUARIO");

    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
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
        sql.append(" FLESTOQUEFISICO,");
        sql.append(" CDUSUARIO");

    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
    	 EstoquePrevistoGeral estoquePrevistoGeral = (EstoquePrevistoGeral) domain;
         sql.append(Sql.getValue(estoquePrevistoGeral.cdEmpresa)).append(",");
         sql.append(Sql.getValue(estoquePrevistoGeral.cdRepresentante)).append(",");
         sql.append(Sql.getValue(estoquePrevistoGeral.cdProduto)).append(",");
         sql.append(Sql.getValue(estoquePrevistoGeral.cdItemGrade1)).append(",");
         sql.append(Sql.getValue(estoquePrevistoGeral.cdItemGrade2)).append(",");
         sql.append(Sql.getValue(estoquePrevistoGeral.cdItemGrade3)).append(",");
         sql.append(Sql.getValue(estoquePrevistoGeral.cdLocalEstoque)).append(",");
         sql.append(Sql.getValue(estoquePrevistoGeral.flOrigemEstoque)).append(",");
         sql.append(Sql.getValue(estoquePrevistoGeral.dtEstoque)).append(",");
         sql.append(Sql.getValue(estoquePrevistoGeral.qtEstoque)).append(",");
         sql.append(Sql.getValue(estoquePrevistoGeral.flEstoqueFisico)).append(",");
         sql.append(Sql.getValue(estoquePrevistoGeral.cdUsuario));
   }
    
	//@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
    	 EstoquePrevistoGeral estoquePrevistoGeral = (EstoquePrevistoGeral) domain;
         sql.append(" CDEMPRESA = ").append(Sql.getValue(estoquePrevistoGeral.cdEmpresa)).append(",");
         sql.append(" CDREPRESENTANTE = ").append(Sql.getValue(estoquePrevistoGeral.cdRepresentante)).append(",");
         sql.append(" CDPRODUTO = ").append(Sql.getValue(estoquePrevistoGeral.cdProduto)).append(",");
         sql.append(" CDITEMGRADE1 = ").append(Sql.getValue(estoquePrevistoGeral.cdItemGrade1)).append(",");
         sql.append(" CDITEMGRADE2 = ").append(Sql.getValue(estoquePrevistoGeral.cdItemGrade2)).append(",");
         sql.append(" CDITEMGRADE3 = ").append(Sql.getValue(estoquePrevistoGeral.cdItemGrade3)).append(",");
         sql.append(" CDLOCALESTOQUE = ").append(Sql.getValue(estoquePrevistoGeral.cdLocalEstoque)).append(",");
         sql.append(" FLORIGEMESTOQUE = ").append(Sql.getValue(estoquePrevistoGeral.flOrigemEstoque)).append(",");
         sql.append(" DTESTOQUE = ").append(Sql.getValue(estoquePrevistoGeral.dtEstoque)).append(",");
         sql.append(" QTESTOQUE = ").append(Sql.getValue(estoquePrevistoGeral.qtEstoque)).append(",");
         sql.append(" FLESTOQUEFISICO = ").append(Sql.getValue(estoquePrevistoGeral.flEstoqueFisico));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
		EstoquePrevistoGeral estoquePrevistoGeral = (EstoquePrevistoGeral) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", estoquePrevistoGeral.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", estoquePrevistoGeral.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", estoquePrevistoGeral.cdProduto);
		sqlWhereClause.addAndCondition("CDITEMGRADE1 = ", estoquePrevistoGeral.cdItemGrade1);
		sqlWhereClause.addAndCondition("CDITEMGRADE2 = ", estoquePrevistoGeral.cdItemGrade2);
		sqlWhereClause.addAndCondition("CDITEMGRADE3 = ", estoquePrevistoGeral.cdItemGrade3);
		if (ValueUtil.isEmpty(estoquePrevistoGeral.cdLocalEstoqueList)) {
			sqlWhereClause.addAndCondition("CDLOCALESTOQUE = ", estoquePrevistoGeral.cdLocalEstoque);
		} else {
			sqlWhereClause.addAndConditionIn("CDLOCALESTOQUE", estoquePrevistoGeral.cdLocalEstoqueList);
		}
		sqlWhereClause.addAndCondition("FLORIGEMESTOQUE = ", estoquePrevistoGeral.flOrigemEstoque);
		sqlWhereClause.addAndCondition("tb.DTESTOQUE = ", estoquePrevistoGeral.dtEstoque);
		sqlWhereClause.addAndCondition("tb.FLESTOQUEFISICO = ", estoquePrevistoGeral.flEstoqueFisico);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    public Double getQtdEstoquePrevisto(EstoquePrevistoGeral estoquePrevistoGeralFilter) throws SQLException {
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(getScriptQtdEstoquePrevisto(estoquePrevistoGeralFilter).toString())) {
    		if (rs.next()) {
    			return rs.getDouble(1);
        	}
		}
    	return null;
    }

	private StringBuffer getScriptQtdEstoquePrevisto(EstoquePrevistoGeral estoquePrevistoGeralFilter) {
		StringBuffer sql = new StringBuffer();
    	sql.append(" SELECT ");
    	sql.append(" ESTOQUEPREVISTO.QTESTOQUE - (SELECT ");
    	sql.append("   IFNULL(SUM(QTITEMFISICO), 0)  QTDE ");
    	sql.append(" FROM TBLVPITEMPEDIDO ITEMPEDIDO ");
    	sql.append(" WHERE ITEMPEDIDO.CDEMPRESA = ").append(Sql.getValue(estoquePrevistoGeralFilter.cdEmpresa));
    	sql.append(" AND ITEMPEDIDO.CDPRODUTO = ").append(Sql.getValue(estoquePrevistoGeralFilter.cdProduto));
    	sql.append(" AND ITEMPEDIDO.FLORIGEMPEDIDO = ").append(Sql.getValue(OrigemPedido.FLORIGEMPEDIDO_PDA));
    	sql.append(" AND ITEMPEDIDO.NUPEDIDO IN (SELECT ");
    	sql.append("   PEDIDO.NUPEDIDO ");
    	sql.append(" FROM TBLVPPEDIDO PEDIDO ");
    	sql.append(" WHERE PEDIDO.CDEMPRESA = ITEMPEDIDO.CDEMPRESA ");
    	sql.append(" AND PEDIDO.CDREPRESENTANTE = ITEMPEDIDO.CDREPRESENTANTE ");
    	sql.append(" AND PEDIDO.NUPEDIDO = ITEMPEDIDO.NUPEDIDO ");
    	sql.append(" AND PEDIDO.FLORIGEMPEDIDO = ITEMPEDIDO.FLORIGEMPEDIDO ");
    	sql.append(" AND (PEDIDO.CDSTATUSPEDIDO = " ).append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoFechado)).append(" OR PEDIDO.CDSTATUSPEDIDO = ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoTransmitido)).append(")");
    	sql.append(" AND PEDIDO.DTENTREGA <= ").append(getSubSQLCalculaDataEntregaPedidoEstimada(estoquePrevistoGeralFilter)).append("))");
    	
    	sql.append(" QTDE FROM TBLVPESTOQUEPREVISTOGERAL ESTOQUEPREVISTO ");
    	addWhereByExample(estoquePrevistoGeralFilter, sql);
    	return sql;
	}
	
	private String getSubSQLCalculaDataEntregaPedidoEstimada(EstoquePrevistoGeral estoquePrevistoGeral) {
		StringBuffer sql = new StringBuffer();
		sql.append(" (SELECT DATE(MAX(DTESTOQUE), 'START OF DAY', '");
		sql.append(LavenderePdaConfig.nuDiasPrevisaoEntrega).append(" DAY',");
		sql.append("'-").append(EstoquePrevistoGeral.NUDIAS_FIXO).append(" DAY')");
		
		sql.append(" FROM TBLVPESTOQUEPREVISTOGERAL ESTOQUEPREVISTO ");
		
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("ESTOQUEPREVISTO.CDEMPRESA = ", estoquePrevistoGeral.cdEmpresa);
		sqlWhereClause.addAndCondition("ESTOQUEPREVISTO.CDPRODUTO = ", estoquePrevistoGeral.cdProduto);
		sqlWhereClause.addAndCondition("ESTOQUEPREVISTO.CDITEMGRADE1 = ", estoquePrevistoGeral.cdItemGrade1);
		sqlWhereClause.addAndCondition("ESTOQUEPREVISTO.CDITEMGRADE2 = ", estoquePrevistoGeral.cdItemGrade2);
		sqlWhereClause.addAndCondition("ESTOQUEPREVISTO.CDITEMGRADE3 = ", estoquePrevistoGeral.cdItemGrade3);
		sqlWhereClause.addAndCondition("ESTOQUEPREVISTO.CDLOCALESTOQUE = ", estoquePrevistoGeral.cdLocalEstoque);
		sqlWhereClause.addAndCondition("ESTOQUEPREVISTO.FLORIGEMESTOQUE = ", estoquePrevistoGeral.flOrigemEstoque);
		sql.append(sqlWhereClause.getSql());
		sql.append(")");
		
		return sql.toString();
	}
    
	public Vector findAllNaoVencidosSomados(EstoquePrevistoGeral estoquePrevistoGeralFilter) throws SQLException {
	  	StringBuffer sql = getSqlBuffer();
	  	sql.append(" select ");
	    sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDPRODUTO,");
        sql.append(" tb.CDITEMGRADE1,");
        sql.append(" tb.CDITEMGRADE2,");
        sql.append(" tb.CDITEMGRADE3,");
        sql.append(" tb.CDLOCALESTOQUE,");
        sql.append(" tb.FLORIGEMESTOQUE,");
        sql.append(" tb.DTESTOQUE,");
        sql.append(" tb.FLESTOQUEFISICO,");
        sql.append(" sum(tb.QTESTOQUE) as QTESTOQUE");
        sql.append(" from ");
	   	sql.append(tableName);
	   	sql.append(" tb ");
	   	addJoin(estoquePrevistoGeralFilter, sql);
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", estoquePrevistoGeralFilter.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", estoquePrevistoGeralFilter.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", estoquePrevistoGeralFilter.cdProduto);
		if (!LavenderePdaConfig.usaReservaEstoqueCorrenteR3() ){
			sqlWhereClause.addAndCondition("tb.CDITEMGRADE1 = ", estoquePrevistoGeralFilter.cdItemGrade1);
			sqlWhereClause.addAndCondition("tb.CDITEMGRADE2 = ", estoquePrevistoGeralFilter.cdItemGrade2);
			sqlWhereClause.addAndCondition("tb.CDITEMGRADE3 = ", estoquePrevistoGeralFilter.cdItemGrade3);
		}
		sqlWhereClause.addAndCondition("tb.FLORIGEMESTOQUE = ", estoquePrevistoGeralFilter.flOrigemEstoque);
		sqlWhereClause.addAndCondition("tb.DTESTOQUE >= ", estoquePrevistoGeralFilter.dtEstoque);
		if (LavenderePdaConfig.usaReservaEstoqueCorrenteR3()) {
			if (ValueUtil.isEmpty(estoquePrevistoGeralFilter.cdLocalEstoqueList)) {
				sqlWhereClause.addAndCondition("CDLOCALESTOQUE = ", estoquePrevistoGeralFilter.cdLocalEstoque);
			} else {
				sqlWhereClause.addAndConditionIn("CDLOCALESTOQUE", estoquePrevistoGeralFilter.cdLocalEstoqueList);
			}	
		}
		sqlWhereClause.addAndCondition("tb.FLESTOQUEFISICO = ", estoquePrevistoGeralFilter.flEstoqueFisico);
		sql.append(sqlWhereClause.getSql());
		sql.append(" GROUP BY ");
		sql.append(" tb.CDEMPRESA,");
	    sql.append(" tb.CDREPRESENTANTE,");
	    sql.append(" tb.CDPRODUTO,");
		sql.append(" tb.CDITEMGRADE1,");
		sql.append(" tb.CDITEMGRADE2,");
		sql.append(" tb.CDITEMGRADE3,");
		sql.append(" tb.DTESTOQUE ");
		addOrderBy(sql, estoquePrevistoGeralFilter);
		addLimit(sql, estoquePrevistoGeralFilter);
		return findAll(estoquePrevistoGeralFilter, sql.toString());
	}
	
}