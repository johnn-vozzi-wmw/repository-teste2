package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.MotivoPerda;
import totalcross.sql.ResultSet;

public class MotivoPerdaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MotivoPerda();
	}

    private static MotivoPerdaDbxDao instance;

    public MotivoPerdaDbxDao() {
        super(MotivoPerda.TABLE_NAME); 
    }
    
    public static MotivoPerdaDbxDao getInstance() {
        if (instance == null) {
            instance = new MotivoPerdaDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        MotivoPerda motivoPerda = new MotivoPerda();
        motivoPerda.rowKey = rs.getString("rowkey");
        motivoPerda.cdEmpresa = rs.getString("cdEmpresa");
        motivoPerda.cdRepresentante = rs.getString("cdRepresentante");
        motivoPerda.cdMotivoPerda = rs.getString("cdMotivoPerda");
        motivoPerda.dsMotivoPerda = rs.getString("dsMotivoPerda");
        motivoPerda.cdUsuario = rs.getString("cdUsuario");
        motivoPerda.nuCarimbo = rs.getInt("nuCarimbo");
        motivoPerda.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return motivoPerda;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDMOTIVOPERDA,");
        sql.append(" DSMOTIVOPERDA,");
        sql.append(" CDUSUARIO,");
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
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDMOTIVOPERDA,");
        sql.append(" DSMOTIVOPERDA,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        MotivoPerda motivoPerda = (MotivoPerda) domain;
        sql.append(Sql.getValue(motivoPerda.cdEmpresa)).append(",");
        sql.append(Sql.getValue(motivoPerda.cdRepresentante)).append(",");
        sql.append(Sql.getValue(motivoPerda.cdMotivoPerda)).append(",");
        sql.append(Sql.getValue(motivoPerda.dsMotivoPerda)).append(",");
        sql.append(Sql.getValue(motivoPerda.cdUsuario)).append(",");
        sql.append(Sql.getValue(motivoPerda.nuCarimbo)).append(",");
        sql.append(Sql.getValue(motivoPerda.flTipoAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        MotivoPerda motivoPerda = (MotivoPerda) domain;
        sql.append(" DSMOTIVOPERDA = ").append(Sql.getValue(motivoPerda.dsMotivoPerda)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(motivoPerda.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(motivoPerda.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(motivoPerda.flTipoAlteracao));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        MotivoPerda motivoPerda = (MotivoPerda) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", motivoPerda.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", motivoPerda.cdRepresentante);
		sqlWhereClause.addAndCondition("CDMOTIVOPERDA = ", motivoPerda.cdMotivoPerda);
		sql.append(sqlWhereClause.getSql());
    }
    
}