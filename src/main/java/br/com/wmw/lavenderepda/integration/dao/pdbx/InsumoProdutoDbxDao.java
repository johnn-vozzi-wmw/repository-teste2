package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.lavenderepda.business.domain.InsumoProduto;
import totalcross.sql.ResultSet;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;

public class InsumoProdutoDbxDao extends LavendereCrudDbxDao {

    private static InsumoProdutoDbxDao instance;

    public InsumoProdutoDbxDao() {
        super(InsumoProduto.TABLE_NAME); 
    }
    
    public static InsumoProdutoDbxDao getInstance() {
        if (instance == null) {
            instance = new InsumoProdutoDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        InsumoProduto insumoProduto = new InsumoProduto();
        insumoProduto.rowKey = rs.getString("rowkey");
        insumoProduto.cdEmpresa = rs.getString("cdEmpresa");
        insumoProduto.cdRepresentante = rs.getString("cdRepresentante");
        insumoProduto.cdInsumo = rs.getString("cdInsumo");
        insumoProduto.cdProduto = rs.getString("cdProduto");
        insumoProduto.dsInsumo = rs.getString("dsInsumo");
        insumoProduto.nuCarimbo = rs.getInt("nuCarimbo");
        insumoProduto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        insumoProduto.cdUsuario = rs.getString("cdUsuario");
        return insumoProduto;
    }

    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDINSUMO,");
        sql.append(" CDPRODUTO,");
        sql.append(" DSINSUMO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDINSUMO,");
        sql.append(" CDPRODUTO,");
        sql.append(" DSINSUMO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        InsumoProduto insumoProduto = (InsumoProduto) domain;
        sql.append(Sql.getValue(insumoProduto.cdEmpresa)).append(",");
        sql.append(Sql.getValue(insumoProduto.cdRepresentante)).append(",");
        sql.append(Sql.getValue(insumoProduto.cdInsumo)).append(",");
        sql.append(Sql.getValue(insumoProduto.cdProduto)).append(",");
        sql.append(Sql.getValue(insumoProduto.dsInsumo)).append(",");
        sql.append(Sql.getValue(insumoProduto.nuCarimbo)).append(",");
        sql.append(Sql.getValue(insumoProduto.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(insumoProduto.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        InsumoProduto insumoProduto = (InsumoProduto) domain;
        sql.append(" DSINSUMO = ").append(Sql.getValue(insumoProduto.dsInsumo)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(insumoProduto.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(insumoProduto.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(insumoProduto.cdUsuario));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        InsumoProduto insumoProduto = (InsumoProduto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", insumoProduto.cdEmpresa);
        sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", insumoProduto.cdRepresentante);
		sqlWhereClause.addAndCondition("CDINSUMO = ", insumoProduto.cdInsumo);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", insumoProduto.cdProduto);
		//--
		sql.append(sqlWhereClause.getSql());
    }

    @Override
    protected BaseDomain getBaseDomainDefault() {
        return null;
    }

}