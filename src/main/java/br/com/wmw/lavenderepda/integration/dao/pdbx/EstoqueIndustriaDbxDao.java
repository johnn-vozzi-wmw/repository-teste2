package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.EstoqueIndustria;
import br.com.wmw.lavenderepda.business.domain.InsumoProduto;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

import java.sql.SQLException;

public class EstoqueIndustriaDbxDao extends LavendereCrudDbxDao {

    private static EstoqueIndustriaDbxDao instance;

    public EstoqueIndustriaDbxDao() {
        super(EstoqueIndustria.TABLE_NAME); 
    }
    
    public static EstoqueIndustriaDbxDao getInstance() {
        if (instance == null) {
            instance = new EstoqueIndustriaDbxDao();
        }
        return instance;
    }

    @Override
    protected BaseDomain getBaseDomainDefault() {
        return new EstoqueIndustria();
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        EstoqueIndustria estoqueIndustria = new EstoqueIndustria();
        estoqueIndustria.rowKey = rs.getString("rowkey");
        estoqueIndustria.cdEmpresa = rs.getString("cdEmpresa");
        estoqueIndustria.cdRepresentante = rs.getString("cdRepresentante");
        estoqueIndustria.cdInsumo = rs.getString("cdInsumo");
        estoqueIndustria.cdLocalEstoque = rs.getString("cdLocalEstoque");
        estoqueIndustria.flOrigemEstoque = rs.getString("flOrigemEstoque");
        estoqueIndustria.dtEstoque = rs.getDate("dtEstoque");
        estoqueIndustria.qtEstoque = rs.getDouble("qtEstoque");
        estoqueIndustria.cdCentroCusto = rs.getString("cdCentroCusto");
        return estoqueIndustria;
    }

    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDINSUMO,");
        sql.append(" tb.CDLOCALESTOQUE,");
        sql.append(" tb.FLORIGEMESTOQUE,");
        sql.append(" tb.DTESTOQUE,");
        sql.append(" tb.QTESTOQUE,");
        sql.append(" tb.CDCENTROCUSTO");
    }

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDINSUMO,");
        sql.append(" CDLOCALESTOQUE,");
        sql.append(" FLORIGEMESTOQUE,");
        sql.append(" DTESTOQUE,");
        sql.append(" QTESTOQUE,");
        sql.append(" CDCENTROCUSTO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        EstoqueIndustria estoqueIndustria = (EstoqueIndustria) domain;
        sql.append(Sql.getValue(estoqueIndustria.cdEmpresa)).append(",");
        sql.append(Sql.getValue(estoqueIndustria.cdRepresentante)).append(",");
        sql.append(Sql.getValue(estoqueIndustria.cdInsumo)).append(",");
        sql.append(Sql.getValue(estoqueIndustria.cdLocalEstoque)).append(",");
        sql.append(Sql.getValue(estoqueIndustria.flOrigemEstoque)).append(",");
        sql.append(Sql.getValue(estoqueIndustria.dtEstoque)).append(",");
        sql.append(Sql.getValue(estoqueIndustria.qtEstoque)).append(",");
        sql.append(Sql.getValue(estoqueIndustria.cdCentroCusto)).append(",");
        sql.append(Sql.getValue(estoqueIndustria.nuCarimbo)).append(",");
        sql.append(Sql.getValue(estoqueIndustria.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(estoqueIndustria.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        EstoqueIndustria estoqueIndustria = (EstoqueIndustria) domain;
        sql.append(" CDREPRESENTANTE = ").append(Sql.getValue(estoqueIndustria.cdRepresentante)).append(",");
        sql.append(" QTESTOQUE = ").append(Sql.getValue(estoqueIndustria.qtEstoque)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(estoqueIndustria.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(estoqueIndustria.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(estoqueIndustria.cdUsuario));
    }

    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        EstoqueIndustria estoqueIndustria = (EstoqueIndustria) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", estoqueIndustria.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDINSUMO = ", estoqueIndustria.cdInsumo);
		sqlWhereClause.addAndCondition("tb.CDLOCALESTOQUE = ", estoqueIndustria.cdLocalEstoque);
		sqlWhereClause.addAndCondition("tb.FLORIGEMESTOQUE = ", estoqueIndustria.flOrigemEstoque);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", estoqueIndustria.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.DTESTOQUE = ", estoqueIndustria.dtEstoque);
        sqlWhereClause.addAndCondition("tb.CDCENTROCUSTO = ", estoqueIndustria.cdCentroCusto);
		//--
		sql.append(sqlWhereClause.getSql());
    }

    @Override
    protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
        EstoqueIndustria estoqueIndustria = (EstoqueIndustria) domainFilter;
        sql.append(" JOIN ").append(InsumoProduto.TABLE_NAME).append(" ").append(DaoUtil.ALIAS_INSUMOPRODUTO).append(" ON ");
        sql.append(DaoUtil.ALIAS_INSUMOPRODUTO).append(".CDEMPRESA = tb.CDEMPRESA AND ");
        sql.append(DaoUtil.ALIAS_INSUMOPRODUTO).append(".CDINSUMO = tb.CDINSUMO AND ");
        sql.append(DaoUtil.ALIAS_INSUMOPRODUTO).append(".CDPRODUTO = '").append( estoqueIndustria.cdProduto).append("' ");

    }

	@Override
	protected void addGroupBy(StringBuffer sql) throws SQLException {
		super.addGroupBy(sql);
		sql.append(" GROUP BY ");
	        sql.append(" tb.CDEMPRESA,");
	        sql.append(" tb.CDREPRESENTANTE,");
	        sql.append(" tb.CDINSUMO,");
	        sql.append(" tb.CDLOCALESTOQUE,");
	        sql.append(" tb.FLORIGEMESTOQUE,");
	        sql.append(" tb.DTESTOQUE,");
	        sql.append(" tb.QTESTOQUE");
	}

	@Override
	protected void addOrderByGrid(StringBuffer sql) {
		super.addOrderByGrid(sql);
		sql.append(" ORDER BY ");
        sql.append(" tb.DTESTOQUE");
	}
	
	public Vector findAllNaoVencidos(EstoqueIndustria estoqueIndustria) throws SQLException {
	   	StringBuffer sql = getSqlBuffer();
	  	sql.append(" select ");
	   	addSelectColumns(estoqueIndustria, sql);
	   	sql.append(" from ");
	   	sql.append(tableName);
	   	sql.append(" tb ");
	   	addJoin(estoqueIndustria, sql);
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", estoqueIndustria.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDINSUMO = ", estoqueIndustria.cdInsumo);
		sqlWhereClause.addAndCondition("tb.CDLOCALESTOQUE = ", estoqueIndustria.cdLocalEstoque);
		sqlWhereClause.addAndCondition("tb.FLORIGEMESTOQUE = ", estoqueIndustria.flOrigemEstoque);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", estoqueIndustria.cdRepresentante);
        sqlWhereClause.addAndCondition("tb.CDCENTROCUSTO = ", estoqueIndustria.cdCentroCusto);
		sqlWhereClause.addAndCondition("tb.DTESTOQUE >= ", estoqueIndustria.dtEstoque);
		sql.append(sqlWhereClause.getSql());
	   	addGroupBy(sql);
	   	addOrderBy(sql, estoqueIndustria);
	   	addLimit(sql, estoqueIndustria);
	   	return findAll(estoqueIndustria, sql.toString());
	}
    
}