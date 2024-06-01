package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Marca;
import totalcross.sql.ResultSet;

public class MarcaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Marca();
	}

    private static MarcaDbxDao instance;

    public MarcaDbxDao() {
        super(Marca.TABLE_NAME); 
    }
    
    public static MarcaDbxDao getInstance() {
        if (instance == null) {
            instance = new MarcaDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        Marca marca = new Marca();
        marca.rowKey = rs.getString("rowkey");
        marca.cdEmpresa = rs.getString("cdEmpresa");
        marca.cdMarca = rs.getString("cdMarca");
        marca.dsMarca = rs.getString("dsMarca");
        marca.flTipoAlteracao = rs.getString("flTipoAlteracao");
        marca.cdUsuario = rs.getString("cdUsuario");
        return marca;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDMARCA,");
        sql.append(" DSMARCA,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDMARCA,");
        sql.append(" DSMARCA,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        Marca marca = (Marca) domain;
        sql.append(Sql.getValue(marca.cdEmpresa)).append(",");
        sql.append(Sql.getValue(marca.cdMarca)).append(",");
        sql.append(Sql.getValue(marca.dsMarca)).append(",");
        sql.append(Sql.getValue(marca.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(marca.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        Marca marca = (Marca) domain;
        sql.append(" DSMARCA = ").append(Sql.getValue(marca.dsMarca)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(marca.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(marca.cdUsuario));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        Marca marca = (Marca) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", marca.cdEmpresa);
		sqlWhereClause.addAndCondition("CDMARCA = ", marca.cdMarca);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}