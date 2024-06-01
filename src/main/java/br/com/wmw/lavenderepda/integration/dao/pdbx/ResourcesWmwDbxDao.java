package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ResourcesWmw;
import totalcross.sql.ResultSet;

public class ResourcesWmwDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ResourcesWmw();
	}

    private static ResourcesWmwDbxDao instance;

    public ResourcesWmwDbxDao() {
        super(ResourcesWmw.TABLE_NAME); 
    }
    
    public static ResourcesWmwDbxDao getInstance() {
        if (instance == null) {
            instance = new ResourcesWmwDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        ResourcesWmw resourcesWmw = new ResourcesWmw();
        resourcesWmw.rowKey = rs.getString("rowkey");
        resourcesWmw.cdChave = rs.getString("cdChave");
        resourcesWmw.baConteudo = rs.getBytes("baConteudo");
        resourcesWmw.flTipoAlteracao = rs.getString("flTipoAlteracao");
        resourcesWmw.dtAlteracao = rs.getDate("dtAlteracao");
        resourcesWmw.hrAlteracao = rs.getString("hrAlteracao");
        resourcesWmw.nuCarimbo = rs.getInt("nuCarimbo");
        resourcesWmw.cdUsuario = rs.getString("cdUsuario");
        return resourcesWmw;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDCHAVE,");
        sql.append(" BACONTEUDO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDCHAVE,");
        sql.append(" BACONTEUDO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        ResourcesWmw resourcesWmw = (ResourcesWmw) domain;
        sql.append(Sql.getValue(resourcesWmw.cdChave)).append(",");
        sql.append(Sql.getValue(resourcesWmw.baConteudo)).append(",");
        sql.append(Sql.getValue(resourcesWmw.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(resourcesWmw.dtAlteracao)).append(",");
        sql.append(Sql.getValue(resourcesWmw.hrAlteracao)).append(",");
        sql.append(Sql.getValue(resourcesWmw.nuCarimbo)).append(",");
        sql.append(Sql.getValue(resourcesWmw.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        ResourcesWmw resourcesWmw = (ResourcesWmw) domain;
        sql.append(" BACONTEUDO = ").append(Sql.getValue(resourcesWmw.baConteudo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(resourcesWmw.flTipoAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(resourcesWmw.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(resourcesWmw.hrAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(resourcesWmw.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(resourcesWmw.cdUsuario));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        ResourcesWmw resourcesWmw = (ResourcesWmw) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDCHAVE = ", resourcesWmw.cdChave);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}