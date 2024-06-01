package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.StatusAtividade;
import totalcross.sql.ResultSet;

public class StatusAtividadeDao extends LavendereCrudDbxDao {

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new StatusAtividade();
	}

    private static StatusAtividadeDao instance;

    public StatusAtividadeDao() {
        super(StatusAtividade.TABLE_NAME);
    }
    
    public static StatusAtividadeDao getInstance() {
        if (instance == null) {
            instance = new StatusAtividadeDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        StatusAtividade statusAtividade = new StatusAtividade();
        statusAtividade.rowKey = rs.getString("rowkey");
        statusAtividade.cdStatusAtividade = rs.getString("cdStatusAtividade");
        statusAtividade.dsStatusAtividade = rs.getString("dsStatusAtividade");
        statusAtividade.nuCarimbo = rs.getInt("nuCarimbo");
        statusAtividade.flTipoAlteracao = rs.getString("flTipoAlteracao");
        statusAtividade.cdUsuario = rs.getString("cdUsuario");
        statusAtividade.dtAlteracao = rs.getDate("dtAlteracao");
        statusAtividade.hrAlteracao = rs.getString("hrAlteracao");
        return statusAtividade;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDSTATUSATIVIDADE,");
        sql.append(" DSSTATUSATIVIDADE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDSTATUSATIVIDADE,");
        sql.append(" DSSTATUSATIVIDADE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        StatusAtividade statusAtividade = (StatusAtividade) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDSTATUSATIVIDADE = ", statusAtividade.cdStatusAtividade);
		//--
		sql.append(sqlWhereClause.getSql());
    }

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) { }

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) { }
    
}