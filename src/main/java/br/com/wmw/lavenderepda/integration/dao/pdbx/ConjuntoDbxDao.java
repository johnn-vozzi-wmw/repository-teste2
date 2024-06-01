package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Conjunto;
import totalcross.sql.ResultSet;

public class ConjuntoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Conjunto();
	}

    private static ConjuntoDbxDao instance;

    public ConjuntoDbxDao() {
        super(Conjunto.TABLE_NAME); 
    }
    
    public static ConjuntoDbxDao getInstance() {
        if (instance == null) {
            instance = new ConjuntoDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        Conjunto conjunto = new Conjunto();
        conjunto.rowKey = rs.getString("rowkey");
        conjunto.cdEmpresa = rs.getString("cdEmpresa");
        conjunto.cdConjunto = rs.getString("cdConjunto");
        conjunto.dsConjunto = rs.getString("dsConjunto");
        conjunto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        conjunto.nuCarimbo = rs.getInt("nuCarimbo");
        conjunto.cdUsuario = rs.getString("cdUsuario");
        return conjunto;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDCONJUNTO,");
        sql.append(" DSCONJUNTO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDCONJUNTO,");
        sql.append(" DSCONJUNTO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        Conjunto conjunto = (Conjunto) domain;
        sql.append(Sql.getValue(conjunto.cdEmpresa)).append(",");
        sql.append(Sql.getValue(conjunto.cdConjunto)).append(",");
        sql.append(Sql.getValue(conjunto.dsConjunto)).append(",");
        sql.append(Sql.getValue(conjunto.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(conjunto.nuCarimbo)).append(",");
        sql.append(Sql.getValue(conjunto.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        Conjunto conjunto = (Conjunto) domain;
        sql.append(" DSCONJUNTO = ").append(Sql.getValue(conjunto.dsConjunto)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(conjunto.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(conjunto.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(conjunto.cdUsuario));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        Conjunto conjunto = (Conjunto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", conjunto.cdEmpresa);
		sqlWhereClause.addAndCondition("CDCONJUNTO = ", conjunto.cdConjunto);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}