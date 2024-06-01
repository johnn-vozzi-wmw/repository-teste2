package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.TipoNovidade;
import totalcross.sql.ResultSet;

public class TipoNovidadeDao extends LavendereCrudDbxDao {

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TipoNovidade();
	}

    private static TipoNovidadeDao instance;

    public TipoNovidadeDao() {
        super(TipoNovidade.TABLE_NAME);
    }

    public static TipoNovidadeDao getInstance() {
        if (instance == null) {
            instance = new TipoNovidadeDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        TipoNovidade tipoNovidade = new TipoNovidade();
        tipoNovidade.rowKey = rs.getString("rowkey");
        tipoNovidade.cdEmpresa = rs.getString("cdEmpresa");
        tipoNovidade.cdTipoNovidade = rs.getString("cdTipoNovidade");
        tipoNovidade.dsTipoNovidade = rs.getString("dsTipoNovidade");
        return tipoNovidade;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDTIPONOVIDADE,");
        sql.append(" DSTIPONOVIDADE");
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoNovidade tipoNovidade = (TipoNovidade) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", tipoNovidade.cdEmpresa);
		sqlWhereClause.addAndCondition("CDTIPONOVIDADE = ", tipoNovidade.cdTipoNovidade);
		sql.append(sqlWhereClause.getSql());
    }

    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		sql.append(" order by DSTIPONOVIDADE");
    }

}