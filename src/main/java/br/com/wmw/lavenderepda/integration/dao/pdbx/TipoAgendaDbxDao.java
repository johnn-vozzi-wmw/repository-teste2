package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.TipoAgenda;
import totalcross.sql.ResultSet;

public class TipoAgendaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TipoAgenda();
	}

    private static TipoAgendaDbxDao instance;

    public TipoAgendaDbxDao() {
        super(TipoAgenda.TABLE_NAME);
    }
    
    public static TipoAgendaDbxDao getInstance() {
        if (instance == null) {
            instance = new TipoAgendaDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        TipoAgenda tipoAgenda = new TipoAgenda();
        tipoAgenda.rowKey = rs.getString("rowkey");
        tipoAgenda.cdTipoAgenda = rs.getString("cdTipoAgenda");
        tipoAgenda.dsTipoAgenda = rs.getString("dsTipoAgenda");
        tipoAgenda.nuCarimbo = rs.getInt("nuCarimbo");
        tipoAgenda.flTipoAlteracao = rs.getString("flTipoAlteracao");
        tipoAgenda.cdUsuario = rs.getString("cdUsuario");
        return tipoAgenda;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDTIPOAGENDA,");
        sql.append(" DSTIPOAGENDA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoAgenda tipoAgenda = (TipoAgenda) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDTIPOAGENDA = ", tipoAgenda.cdTipoAgenda);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}