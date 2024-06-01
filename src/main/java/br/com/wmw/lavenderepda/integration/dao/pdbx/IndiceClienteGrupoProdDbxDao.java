package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.IndiceClienteGrupoProd;
import totalcross.sql.ResultSet;

public class IndiceClienteGrupoProdDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new IndiceClienteGrupoProd();
	}

    private static IndiceClienteGrupoProdDbxDao instance;

    public IndiceClienteGrupoProdDbxDao() {
        super(IndiceClienteGrupoProd.TABLE_NAME); 
    }
    
    public static IndiceClienteGrupoProdDbxDao getInstance() {
        if (instance == null) {
            instance = new IndiceClienteGrupoProdDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        IndiceClienteGrupoProd indiceClienteGrupoProd = new IndiceClienteGrupoProd();
        indiceClienteGrupoProd.rowKey = rs.getString("rowkey");
        indiceClienteGrupoProd.cdEmpresa = rs.getString("cdEmpresa");
        indiceClienteGrupoProd.cdRepresentante = rs.getString("cdRepresentante");
        indiceClienteGrupoProd.cdCliente = rs.getString("cdCliente");
        indiceClienteGrupoProd.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
        indiceClienteGrupoProd.cdGrupoProduto2 = rs.getString("cdGrupoProduto2");
        indiceClienteGrupoProd.cdProduto = rs.getString("cdProduto");
        indiceClienteGrupoProd.vlIndiceFinanceiro = rs.getDouble("vlIndiceFinanceiro");
        indiceClienteGrupoProd.cdUsuario = rs.getString("cdUsuario");
        indiceClienteGrupoProd.nuCarimbo = rs.getInt("nuCarimbo");
        indiceClienteGrupoProd.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return indiceClienteGrupoProd;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDGRUPOPRODUTO1,");
        sql.append(" CDGRUPOPRODUTO2,");
        sql.append(" CDPRODUTO,");
        sql.append(" VLINDICEFINANCEIRO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO ");
    }
    
    @Override
    protected void addCacheColumns(StringBuffer sql) throws SQLException {
    	sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDGRUPOPRODUTO1,");
        sql.append(" CDGRUPOPRODUTO2,");
        sql.append(" CDPRODUTO,");
        sql.append(" VLINDICEFINANCEIRO ");
    }
    
    @Override
    protected BaseDomain populateCache(ResultSet rs) throws SQLException {
    	IndiceClienteGrupoProd indiceClienteGrupoProd = new IndiceClienteGrupoProd();
    	indiceClienteGrupoProd.rowKey = rs.getString("rowkey");
        indiceClienteGrupoProd.cdEmpresa = rs.getString("cdEmpresa");
        indiceClienteGrupoProd.cdRepresentante = rs.getString("cdRepresentante");
        indiceClienteGrupoProd.cdCliente = rs.getString("cdCliente");
        indiceClienteGrupoProd.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
        indiceClienteGrupoProd.cdGrupoProduto2 = rs.getString("cdGrupoProduto2");
        indiceClienteGrupoProd.cdProduto = rs.getString("cdProduto");
        indiceClienteGrupoProd.vlIndiceFinanceiro = rs.getDouble("vlIndiceFinanceiro");
        return indiceClienteGrupoProd;
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDGRUPOPRODUTO1,");
        sql.append(" CDGRUPOPRODUTO2,");
        sql.append(" CDPRODUTO,");
        sql.append(" VLINDICEFINANCEIRO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO ");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        IndiceClienteGrupoProd indiceClienteGrupoProd = (IndiceClienteGrupoProd) domain;
        sql.append(Sql.getValue(indiceClienteGrupoProd.cdEmpresa)).append(",");
        sql.append(Sql.getValue(indiceClienteGrupoProd.cdRepresentante)).append(",");
        sql.append(Sql.getValue(indiceClienteGrupoProd.cdCliente)).append(",");
        sql.append(Sql.getValue(indiceClienteGrupoProd.cdGrupoProduto1)).append(",");
        sql.append(Sql.getValue(indiceClienteGrupoProd.cdGrupoProduto2)).append(",");
        sql.append(Sql.getValue(indiceClienteGrupoProd.cdProduto)).append(",");
        sql.append(Sql.getValue(indiceClienteGrupoProd.vlIndiceFinanceiro)).append(",");
        sql.append(Sql.getValue(indiceClienteGrupoProd.cdUsuario)).append(",");
        sql.append(Sql.getValue(indiceClienteGrupoProd.nuCarimbo)).append(",");
        sql.append(Sql.getValue(indiceClienteGrupoProd.flTipoAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        IndiceClienteGrupoProd indiceClienteGrupoProd = (IndiceClienteGrupoProd) domain;
        sql.append(" VLINDICEFINANCEIRO = ").append(Sql.getValue(indiceClienteGrupoProd.vlIndiceFinanceiro)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(indiceClienteGrupoProd.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(indiceClienteGrupoProd.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(indiceClienteGrupoProd.flTipoAlteracao)).append(",");
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        IndiceClienteGrupoProd indiceClienteGrupoProd = (IndiceClienteGrupoProd) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", indiceClienteGrupoProd.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", indiceClienteGrupoProd.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", indiceClienteGrupoProd.cdCliente);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO1 = ", indiceClienteGrupoProd.cdGrupoProduto1);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO2 = ", indiceClienteGrupoProd.cdGrupoProduto2);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", indiceClienteGrupoProd.cdProduto);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected String getKeyCache(ResultSet rs, BaseDomain domain) throws SQLException {
    	return domain.getRowKey();
    }
}