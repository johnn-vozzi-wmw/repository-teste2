package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.MotivoPendenciaJust;
import totalcross.sql.ResultSet;

public class MotivoPendenciaJustDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MotivoPendenciaJust();
	}

    private static MotivoPendenciaJustDbxDao instance;

    public MotivoPendenciaJustDbxDao() {
        super(MotivoPendenciaJust.TABLE_NAME); 
    }
    
    public static MotivoPendenciaJustDbxDao getInstance() {
        if (instance == null) {
            instance = new MotivoPendenciaJustDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        MotivoPendenciaJust motivoPendenciaJust = new MotivoPendenciaJust();
        motivoPendenciaJust.rowKey = rs.getString("rowkey");
        motivoPendenciaJust.cdEmpresa = rs.getString("cdEmpresa");
        motivoPendenciaJust.cdRepresentante = rs.getString("cdRepresentante");
        motivoPendenciaJust.cdMotivoPendencia = rs.getString("cdMotivoPendencia");
        motivoPendenciaJust.cdMotivoPendenciaJust = rs.getString("cdMotivoPendenciaJust");
        motivoPendenciaJust.dsMotivoPendenciaJust = rs.getString("dsMotivoPendenciaJust");
        motivoPendenciaJust.flUsaObservacao = rs.getString("flUsaObservacao");
        motivoPendenciaJust.cdUsuario = rs.getString("cdUsuario");
        motivoPendenciaJust.nuCarimbo = rs.getInt("nuCarimbo");
        motivoPendenciaJust.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return motivoPendenciaJust;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		return populate(domainFilter, rs);
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDMOTIVOPENDENCIA,");
        sql.append(" CDMOTIVOPENDENCIAJUST,");
        sql.append(" DSMOTIVOPENDENCIAJUST,");
        sql.append(" FLUSAOBSERVACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
		addSelectColumns(null, sql);
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {}

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {}

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {}
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        MotivoPendenciaJust motivoPendenciaJust = (MotivoPendenciaJust) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", motivoPendenciaJust.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", motivoPendenciaJust.cdRepresentante);
		sqlWhereClause.addAndCondition("CDMOTIVOPENDENCIA = ", motivoPendenciaJust.cdMotivoPendencia);
		sqlWhereClause.addAndCondition("CDMOTIVOPENDENCIAJUST = ", motivoPendenciaJust.cdMotivoPendenciaJust);
		sql.append(sqlWhereClause.getSql());
    }
}