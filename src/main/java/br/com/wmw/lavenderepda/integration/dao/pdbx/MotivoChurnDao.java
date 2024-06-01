package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.MotivoChurn;
import totalcross.sql.ResultSet;

public class MotivoChurnDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MotivoChurn();
	}

    private static MotivoChurnDao instance = null;

    public MotivoChurnDao() {
        super(MotivoChurn.TABLE_NAME);
    }
    
    public static MotivoChurnDao getInstance() {
        if (instance == null) {
            instance = new MotivoChurnDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        MotivoChurn motivoChurn = new MotivoChurn();
        motivoChurn.rowKey = rs.getString("rowkey");
        motivoChurn.cdMotivoChurn = rs.getString("cdMotivoChurn");
        motivoChurn.dsMotivoChurn = rs.getString("dsMotivoChurn");
        motivoChurn.cdUsuario = rs.getString("cdUsuario");
        motivoChurn.nuCarimbo = rs.getInt("nuCarimbo");
        motivoChurn.flTipoAlteracao = rs.getString("flTipoAlteracao");
        motivoChurn.dtAlteracao = rs.getDate("dtAlteracao");
        motivoChurn.hrAlteracao = rs.getString("hrAlteracao");
        return motivoChurn;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDMOTIVOCHURN,");
        sql.append(" DSMOTIVOCHURN,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) { }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) { }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) { }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        MotivoChurn motivoChurn = (MotivoChurn) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDMOTIVOCHURN = ", motivoChurn.cdMotivoChurn);
		sql.append(sqlWhereClause.getSql());
    }

}