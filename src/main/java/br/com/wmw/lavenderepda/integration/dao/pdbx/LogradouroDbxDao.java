package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Logradouro;
import totalcross.sql.ResultSet;

public class LogradouroDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Logradouro();
	}

    private static LogradouroDbxDao instance;

    public LogradouroDbxDao() {
        super(Logradouro.TABLE_NAME);
    }
    
    public static LogradouroDbxDao getInstance() {
        if (instance == null) {
            instance = new LogradouroDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Logradouro logradouro = new Logradouro();
        logradouro.rowKey = rs.getString("rowkey");
        logradouro.cdLogradouro = rs.getString("cdLogradouro");
        logradouro.dsLogradouro = rs.getString("dsLogradouro");
        logradouro.dsTipoLogradouro = rs.getString("dsTipoLogradouro");
        logradouro.nuCarimbo = rs.getInt("nuCarimbo");
        logradouro.flTipoAlteracao = rs.getString("flTipoAlteracao");
        logradouro.cdUsuario = rs.getString("cdUsuario");
        return logradouro;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDLOGRADOURO,");
        sql.append(" DSLOGRADOURO,");
        sql.append(" DSTIPOLOGRADOURO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDLOGRADOURO,");
        sql.append(" DSLOGRADOURO,");
        sql.append(" DSTIPOLOGRADOURO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Logradouro logradouro = (Logradouro) domain;
        sql.append(Sql.getValue(logradouro.cdLogradouro)).append(",");
        sql.append(Sql.getValue(logradouro.dsLogradouro)).append(",");
        sql.append(Sql.getValue(logradouro.dsTipoLogradouro)).append(",");
        sql.append(Sql.getValue(logradouro.nuCarimbo)).append(",");
        sql.append(Sql.getValue(logradouro.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(logradouro.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Logradouro logradouro = (Logradouro) domain;
        sql.append(" DSLOGRADOURO = ").append(Sql.getValue(logradouro.dsLogradouro)).append(",");
        sql.append(" DSTIPOLOGRADOURO = ").append(Sql.getValue(logradouro.dsTipoLogradouro)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(logradouro.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(logradouro.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(logradouro.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Logradouro logradouro = (Logradouro) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDLOGRADOURO = ", logradouro.cdLogradouro);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}