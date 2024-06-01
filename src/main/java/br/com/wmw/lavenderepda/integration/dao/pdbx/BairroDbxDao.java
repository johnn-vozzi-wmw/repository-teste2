package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Bairro;
import totalcross.sql.ResultSet;

public class BairroDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Bairro();
	}

    private static BairroDbxDao instance;

    public BairroDbxDao() {
        super(Bairro.TABLE_NAME);
    }
    
    public static BairroDbxDao getInstance() {
        if (instance == null) {
            instance = new BairroDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        Bairro bairro = new Bairro();
        bairro.rowKey = rs.getString("rowkey");
        bairro.cdBairro = rs.getString("cdBairro");
        bairro.dsBairro = rs.getString("dsBairro");
        bairro.nuCarimbo = rs.getInt("nuCarimbo");
        bairro.flTipoAlteracao = rs.getString("flTipoAlteracao");
        bairro.cdUsuario = rs.getString("cdUsuario");
        return bairro;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDBAIRRO,");
        sql.append(" DSBAIRRO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDBAIRRO,");
        sql.append(" DSBAIRRO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Bairro bairro = (Bairro) domain;
        sql.append(Sql.getValue(bairro.cdBairro)).append(",");
        sql.append(Sql.getValue(bairro.dsBairro)).append(",");
        sql.append(Sql.getValue(bairro.nuCarimbo)).append(",");
        sql.append(Sql.getValue(bairro.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(bairro.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Bairro bairro = (Bairro) domain;
        sql.append(" DSBAIRRO = ").append(Sql.getValue(bairro.dsBairro)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(bairro.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(bairro.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(bairro.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Bairro bairro = (Bairro) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDBAIRRO = ", bairro.cdBairro);
		sqlWhereClause.addAndCondition("DSBAIRRO = ", bairro.dsBairro);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}