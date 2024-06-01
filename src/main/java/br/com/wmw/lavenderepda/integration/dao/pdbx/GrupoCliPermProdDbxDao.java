package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.GrupoCliPermProd;
import totalcross.sql.ResultSet;

public class GrupoCliPermProdDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new GrupoCliPermProd();
	}

    private static GrupoCliPermProdDbxDao instance;

    public GrupoCliPermProdDbxDao() {
        super(GrupoCliPermProd.TABLE_NAME);
    }
    
    public static GrupoCliPermProdDbxDao getInstance() {
        if (instance == null) {
            instance = new GrupoCliPermProdDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        GrupoCliPermProd grupoCliPermProd = new GrupoCliPermProd();
        grupoCliPermProd.rowKey = rs.getString("rowkey");
        grupoCliPermProd.cdGrupoCliente = rs.getString("cdGrupoCliente");
        grupoCliPermProd.cdProduto = rs.getString("cdProduto");
        grupoCliPermProd.flTipoAlteracao = rs.getString("flTipoAlteracao");
        grupoCliPermProd.nuCarimbo = rs.getInt("nuCarimbo");
        grupoCliPermProd.cdUsuario = rs.getString("cdUsuario");
        return grupoCliPermProd;
    }
    
    @Override
    protected BaseDomain populateCache(ResultSet rs) throws java.sql.SQLException {
    	GrupoCliPermProd grupoCliPermProd = new GrupoCliPermProd();
        grupoCliPermProd.cdGrupoCliente = rs.getString("cdGrupoCliente");
        grupoCliPermProd.cdProduto = rs.getString("cdProduto");
        grupoCliPermProd.flTipoAlteracao = rs.getString("flTipoAlteracao");
        grupoCliPermProd.nuCarimbo = rs.getInt("nuCarimbo");
        grupoCliPermProd.cdUsuario = rs.getString("cdUsuario");
    	return grupoCliPermProd;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDGRUPOCLIENTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }
    
    @Override
    protected void addCacheColumns(StringBuffer sql) {
        sql.append(" CDGRUPOCLIENTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDGRUPOCLIENTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        GrupoCliPermProd grupoCliPermProd = (GrupoCliPermProd) domain;
        sql.append(Sql.getValue(grupoCliPermProd.cdGrupoCliente)).append(",");
        sql.append(Sql.getValue(grupoCliPermProd.cdProduto)).append(",");
        sql.append(Sql.getValue(grupoCliPermProd.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(grupoCliPermProd.nuCarimbo)).append(",");
        sql.append(Sql.getValue(grupoCliPermProd.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        GrupoCliPermProd grupoCliPermProd = (GrupoCliPermProd) domain;
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(grupoCliPermProd.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(grupoCliPermProd.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(grupoCliPermProd.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        GrupoCliPermProd grupoCliPermProd = (GrupoCliPermProd) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDGRUPOCLIENTE = ", grupoCliPermProd.cdGrupoCliente);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", grupoCliPermProd.cdProduto);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}