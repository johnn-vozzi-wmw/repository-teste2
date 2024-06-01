package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.EstoqueIndustriaGeral;
import br.com.wmw.lavenderepda.business.domain.InsumoProduto;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

import java.sql.SQLException;

public class EstoqueIndustriaGeralDbxDao extends LavendereCrudDbxDao {

    private static EstoqueIndustriaGeralDbxDao instance;

    public EstoqueIndustriaGeralDbxDao() {
        super(EstoqueIndustriaGeral.TABLE_NAME);
    }
    
    public static EstoqueIndustriaGeralDbxDao getInstance() {
        if (instance == null) {
            instance = new EstoqueIndustriaGeralDbxDao();
        }
        return instance;
    }

    @Override
    protected BaseDomain getBaseDomainDefault() {
        return new EstoqueIndustriaGeral();
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        EstoqueIndustriaGeral estoqueIndustriaGeral = new EstoqueIndustriaGeral();
        estoqueIndustriaGeral.rowKey = rs.getString("rowkey");
        estoqueIndustriaGeral.cdEmpresa = rs.getString("cdEmpresa");
        estoqueIndustriaGeral.cdRepresentante = rs.getString("cdRepresentante");
        estoqueIndustriaGeral.cdInsumo = rs.getString("cdInsumo");
        estoqueIndustriaGeral.cdLocalEstoque = rs.getString("cdLocalEstoque");
        estoqueIndustriaGeral.flOrigemEstoque = rs.getString("flOrigemEstoque");
        estoqueIndustriaGeral.dtEstoque = rs.getDate("dtEstoque");
        estoqueIndustriaGeral.qtEstoque = rs.getDouble("qtEstoque");
        estoqueIndustriaGeral.cdCentroCusto = rs.getString("cdCentroCusto");
        return estoqueIndustriaGeral;
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
        sql.append(" CDCENTROCUSTO ");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        EstoqueIndustriaGeral estoqueIndustriaGeral = (EstoqueIndustriaGeral) domain;
        sql.append(Sql.getValue(estoqueIndustriaGeral.cdEmpresa)).append(",");
        sql.append(Sql.getValue(estoqueIndustriaGeral.cdRepresentante)).append(",");
        sql.append(Sql.getValue(estoqueIndustriaGeral.cdInsumo)).append(",");
        sql.append(Sql.getValue(estoqueIndustriaGeral.cdLocalEstoque)).append(",");
        sql.append(Sql.getValue(estoqueIndustriaGeral.flOrigemEstoque)).append(",");
        sql.append(Sql.getValue(estoqueIndustriaGeral.dtEstoque)).append(",");
        sql.append(Sql.getValue(estoqueIndustriaGeral.qtEstoque)).append(",");
        sql.append(Sql.getValue(estoqueIndustriaGeral.cdCentroCusto)).append(",");
        sql.append(Sql.getValue(estoqueIndustriaGeral.nuCarimbo)).append(",");
        sql.append(Sql.getValue(estoqueIndustriaGeral.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(estoqueIndustriaGeral.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        EstoqueIndustriaGeral estoqueIndustriaGeral = (EstoqueIndustriaGeral) domain;
        sql.append(" QTESTOQUE = ").append(Sql.getValue(estoqueIndustriaGeral.qtEstoque)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(estoqueIndustriaGeral.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(estoqueIndustriaGeral.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(estoqueIndustriaGeral.cdUsuario));
    }

    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        EstoqueIndustriaGeral estoqueIndustriaGeral = (EstoqueIndustriaGeral) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", estoqueIndustriaGeral.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", estoqueIndustriaGeral.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDINSUMO = ", estoqueIndustriaGeral.cdInsumo);
		sqlWhereClause.addAndCondition("tb.CDLOCALESTOQUE = ", estoqueIndustriaGeral.cdLocalEstoque);
		sqlWhereClause.addAndCondition("tb.FLORIGEMESTOQUE = ", estoqueIndustriaGeral.flOrigemEstoque);
		sqlWhereClause.addAndCondition("tb.DTESTOQUE = ", estoqueIndustriaGeral.dtEstoque);
        sqlWhereClause.addAndCondition("tb.CDCENTROCUSTO = ", estoqueIndustriaGeral.cdCentroCusto);
		//--
		sql.append(sqlWhereClause.getSql());
    }

    @Override
    protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
        EstoqueIndustriaGeral estoqueIndustriaGeral = (EstoqueIndustriaGeral) domainFilter;
        sql.append(" JOIN ").append(InsumoProduto.TABLE_NAME).append(" ").append(DaoUtil.ALIAS_INSUMOPRODUTO).append(" ON ");
        sql.append(DaoUtil.ALIAS_INSUMOPRODUTO).append(".CDEMPRESA = tb.CDEMPRESA AND ");
        sql.append(DaoUtil.ALIAS_INSUMOPRODUTO).append(".CDREPRESENTANTE = tb.CDREPRESENTANTE AND ");
        sql.append(DaoUtil.ALIAS_INSUMOPRODUTO).append(".CDINSUMO = tb.CDINSUMO AND ");
        sql.append(DaoUtil.ALIAS_INSUMOPRODUTO).append(".CDPRODUTO = '").append( estoqueIndustriaGeral.cdProduto).append("' ");

    }

	@Override
	protected void addGroupBy(StringBuffer sql) throws SQLException {
		super.addGroupBy(sql);
		sql.append(" GROUP BY ");
	        sql.append(" tb.CDEMPRESA,");
	        sql.append(" tb.CDREPRESENTANTE,");
	        sql.append(" tb.CDINSUMO,");
	        sql.append(" tb.CDLOCALESTOQUE,");
	        sql.append(" tb.CDCENTROCUSTO,");
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
	
	public Vector findAllNaoVencidosSomados(EstoqueIndustriaGeral estoqueIndustriaGeral) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" select ");
		   sql.append(" tb.rowkey,");
	        sql.append(" tb.CDEMPRESA,");
	        sql.append(" tb.CDREPRESENTANTE,");
	        sql.append(" tb.CDINSUMO,");
	        sql.append(" tb.CDLOCALESTOQUE,");
	        sql.append(" tb.FLORIGEMESTOQUE,");
	        sql.append(" tb.DTESTOQUE,");
	        sql.append(" sum(tb.QTESTOQUE) as QTESTOQUE,");
	        sql.append(" tb.CDCENTROCUSTO");
	  		sql.append(" from ");
		sql.append(tableName);
		sql.append(" tb ");
		addJoin(estoqueIndustriaGeral, sql);
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", estoqueIndustriaGeral.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", estoqueIndustriaGeral.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDINSUMO = ", estoqueIndustriaGeral.cdInsumo);
		sqlWhereClause.addAndCondition("tb.CDLOCALESTOQUE = ", estoqueIndustriaGeral.cdLocalEstoque);
		sqlWhereClause.addAndCondition("tb.FLORIGEMESTOQUE = ", estoqueIndustriaGeral.flOrigemEstoque);
		sqlWhereClause.addAndCondition("tb.DTESTOQUE >= ", estoqueIndustriaGeral.dtEstoque);
		sqlWhereClause.addAndCondition("tb.CDCENTROCUSTO = ", estoqueIndustriaGeral.cdCentroCusto);
		sql.append(sqlWhereClause.getSql());
		sql.append(" GROUP BY ");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDINSUMO,");
        sql.append(" tb.CDLOCALESTOQUE,");
        sql.append(" tb.FLORIGEMESTOQUE,");
        sql.append(" tb.DTESTOQUE ");
        addOrderBy(sql, estoqueIndustriaGeral);
		addLimit(sql, estoqueIndustriaGeral);
		return findAll(estoqueIndustriaGeral, sql.toString());
	}
    
}