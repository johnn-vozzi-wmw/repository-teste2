package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.GrupoDescProd;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

import java.sql.SQLException;

public class GrupoDescProdDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new GrupoDescProd();
	}

    private static GrupoDescProdDbxDao instance;

    public GrupoDescProdDbxDao() {
        super(GrupoDescProd.TABLE_NAME);
    }
    
    public static GrupoDescProdDbxDao getInstance() {
    	return (instance == null) ? instance = new GrupoDescProdDbxDao() : instance;
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {}
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
    @Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {}
    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {}
    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {}
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        GrupoDescProd grupoDescProd = new GrupoDescProd();
        grupoDescProd.rowKey = rs.getString("rowkey");
        grupoDescProd.cdEmpresa = rs.getString("cdEmpresa");
        grupoDescProd.cdRepresentante = rs.getString("cdRepresentante");
        grupoDescProd.cdProduto = rs.getString("cdProduto");
        grupoDescProd.cdGrupoDescProd = rs.getString("cdGrupoDescProd");
        grupoDescProd.dsGrupoDescProd = rs.getString("dsGrupoDescProd");
        grupoDescProd.nuPrioridade = rs.getInt("nuPrioridade");
        grupoDescProd.nuCarimbo = rs.getInt("nuCarimbo");
        grupoDescProd.flTipoAlteracao = rs.getString("flTipoAlteracao");
        grupoDescProd.cdUsuario = rs.getString("cdUsuario");
        return grupoDescProd;
    }
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDGRUPODESCPROD,");
        sql.append(" DSGRUPODESCPROD,");
        sql.append(" NUPRIORIDADE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        GrupoDescProd grupoDescProd = (GrupoDescProd) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", grupoDescProd.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", grupoDescProd.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", grupoDescProd.cdProduto);
		sqlWhereClause.addAndCondition("CDGRUPODESCPROD = ", grupoDescProd.cdGrupoDescProd);
		sql.append(sqlWhereClause.getSql());
    }

	public Vector findAllByExampleCombo(GrupoDescProd grupoDescProdFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ");
		addSelectColumns(grupoDescProdFilter, sql);
		sql.append(" FROM ").append(tableName).append(" tb ");
		addWhereByExample(grupoDescProdFilter, sql);
		sql.append(" GROUP BY CDGRUPODESCPROD ");
		addOrderBy(sql, grupoDescProdFilter);
		addLimit(sql, grupoDescProdFilter);
		return findAll(grupoDescProdFilter, sql.toString());
	}

}
