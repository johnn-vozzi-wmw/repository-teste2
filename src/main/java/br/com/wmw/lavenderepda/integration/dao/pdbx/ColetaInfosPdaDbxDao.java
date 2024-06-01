package br.com.wmw.lavenderepda.integration.dao.pdbx;
import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ColetaInfosPda;
import totalcross.sql.ResultSet;

public class ColetaInfosPdaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ColetaInfosPda();
	}

    private static ColetaInfosPdaDbxDao instance;

    public ColetaInfosPdaDbxDao() {
        super(ColetaInfosPda.TABLE_NAME); 
    }
    
    public static ColetaInfosPdaDbxDao getInstance() {
        if (instance == null) {
            instance = new ColetaInfosPdaDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        ColetaInfosPda radarcoletarep = new ColetaInfosPda();
        radarcoletarep.rowKey = rs.getString("rowkey");
        radarcoletarep.cdUsuario = rs.getString("cdUsuario");
        radarcoletarep.dtSync = rs.getDate("dtSync");
        radarcoletarep.vlPctBateria = rs.getInt("vlPctBateria");
        radarcoletarep.vlPctBateriaInicial = rs.getInt("vlPctBateriaInicial");
        radarcoletarep.hrSyncInicial = rs.getString("hrSyncInicial");
        radarcoletarep.hrSync = rs.getString("hrSync");
        radarcoletarep.flGpsAtivo = rs.getString("flGpsAtivo");
        radarcoletarep.nuCarimbo = rs.getInt("nuCarimbo");
        radarcoletarep.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return radarcoletarep;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDUSUARIO,");
        sql.append(" DTSYNC,");
        sql.append(" VLPCTBATERIA,");
        sql.append(" VLPCTBATERIAINICIAL,");
        sql.append(" HRSYNCINICIAL,");
        sql.append(" HRSYNC,");
        sql.append(" FLGPSATIVO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDUSUARIO,");
        sql.append(" DTSYNC,");
        sql.append(" VLPCTBATERIA,");
        sql.append(" VLPCTBATERIAINICIAL,");
        sql.append(" HRSYNCINICIAL,");
        sql.append(" HRSYNC,");
        sql.append(" FLGPSATIVO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        ColetaInfosPda radarcoletarep = (ColetaInfosPda) domain;
        sql.append(Sql.getValue(radarcoletarep.cdUsuario)).append(",");
        sql.append(Sql.getValue(radarcoletarep.dtSync)).append(",");
        sql.append(Sql.getValue(radarcoletarep.vlPctBateria)).append(",");
        sql.append(Sql.getValue(radarcoletarep.vlPctBateriaInicial)).append(",");
        sql.append(Sql.getValue(radarcoletarep.hrSyncInicial)).append(",");
        sql.append(Sql.getValue(radarcoletarep.hrSync)).append(",");
        sql.append(Sql.getValue(radarcoletarep.flGpsAtivo)).append(",");
        sql.append(Sql.getValue(radarcoletarep.nuCarimbo)).append(",");
        sql.append(Sql.getValue(radarcoletarep.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(radarcoletarep.dtAlteracao)).append(",");
        sql.append(Sql.getValue(radarcoletarep.hrAlteracao));

    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        ColetaInfosPda radarcoletarep = (ColetaInfosPda) domain;
        sql.append(" VLPCTBATERIA = ").append(Sql.getValue(radarcoletarep.vlPctBateria)).append(",");
        sql.append(" HRSYNC = ").append(Sql.getValue(radarcoletarep.hrSync)).append(",");
        sql.append(" FLGPSATIVO = ").append(Sql.getValue(radarcoletarep.flGpsAtivo)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(radarcoletarep.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(radarcoletarep.flTipoAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(radarcoletarep.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(radarcoletarep.hrAlteracao));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        ColetaInfosPda radarcoletarep = (ColetaInfosPda) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDUSUARIO = ", radarcoletarep.cdUsuario);
		sqlWhereClause.addAndCondition("DTSYNC = ", radarcoletarep.dtSync);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}