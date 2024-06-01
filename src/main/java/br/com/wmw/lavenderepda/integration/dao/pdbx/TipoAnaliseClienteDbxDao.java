package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.TipoAnaliseCliente;
import totalcross.sql.ResultSet;

public class TipoAnaliseClienteDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TipoAnaliseCliente();
	}

    private static TipoAnaliseClienteDbxDao instance = null;
	public static String TABLE_NAME = "TBLVPTIPOANALISECLIENTE";

    public TipoAnaliseClienteDbxDao() {
        super(TABLE_NAME);
    }
    
    public static TipoAnaliseClienteDbxDao getInstance() {
        if (instance == null) {
            instance = new TipoAnaliseClienteDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        TipoAnaliseCliente tipoAnaliseCliente = new TipoAnaliseCliente();
        tipoAnaliseCliente.rowKey = rs.getString("rowkey");
        tipoAnaliseCliente.cdTipoAnalise = rs.getString("cdTipoAnalise");
        tipoAnaliseCliente.dsTipoAnalise = rs.getString("dsTipoAnalise");
        tipoAnaliseCliente.nuCarimbo = rs.getInt("nuCarimbo");
        tipoAnaliseCliente.flTipoAlteracao = rs.getString("flTipoAlteracao");
        tipoAnaliseCliente.dtAlteracao = rs.getDate("dtAlteracao");
        tipoAnaliseCliente.hrAlteracao = rs.getString("hrAlteracao");
        tipoAnaliseCliente.cdUsuario = rs.getString("cdUsuario");
        return tipoAnaliseCliente;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDTIPOANALISE,");
        sql.append(" DSTIPOANALISE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDTIPOANALISE,");
        sql.append(" DSTIPOANALISE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        TipoAnaliseCliente tipoAnaliseCliente = (TipoAnaliseCliente) domain;
        sql.append(Sql.getValue(tipoAnaliseCliente.cdTipoAnalise)).append(",");
        sql.append(Sql.getValue(tipoAnaliseCliente.dsTipoAnalise)).append(",");
        sql.append(Sql.getValue(tipoAnaliseCliente.nuCarimbo)).append(",");
        sql.append(Sql.getValue(tipoAnaliseCliente.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(tipoAnaliseCliente.dtAlteracao)).append(",");
        sql.append(Sql.getValue(tipoAnaliseCliente.hrAlteracao)).append(",");
        sql.append(Sql.getValue(tipoAnaliseCliente.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        TipoAnaliseCliente tipoAnaliseCliente = (TipoAnaliseCliente) domain;
        sql.append(" DSTIPOANALISE = ").append(Sql.getValue(tipoAnaliseCliente.dsTipoAnalise)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(tipoAnaliseCliente.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(tipoAnaliseCliente.flTipoAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(tipoAnaliseCliente.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(tipoAnaliseCliente.hrAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(tipoAnaliseCliente.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        TipoAnaliseCliente tipoAnaliseCliente = (TipoAnaliseCliente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDTIPOANALISE = ", tipoAnaliseCliente.cdTipoAnalise);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}