package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Cesta;
import totalcross.sql.ResultSet;

public class CestaPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Cesta();
	}

    private static CestaPdbxDao instance;

    public CestaPdbxDao() {
        super(Cesta.TABLE_NAME);
    }

    public static CestaPdbxDao getInstance() {
        if (instance == null) {
            instance = new CestaPdbxDao();
        }
        return instance;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" TB.rowkey,");
        sql.append(" TB.CDEMPRESA,");
        sql.append(" TB.CDREPRESENTANTE,");
        sql.append(" TB.CDCESTA,");
        sql.append(" TB.DSCESTA,");
        sql.append(" TB.FLTIPOALTERACAO");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Cesta cesta = new Cesta();
        cesta.rowKey = rs.getString("rowkey");
        cesta.cdEmpresa = rs.getString("cdEmpresa");
        cesta.cdRepresentante = rs.getString("cdRepresentante");
        cesta.cdCesta = rs.getString("cdCesta");
        cesta.dsCesta = rs.getString("dsCesta");
        cesta.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return cesta;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Cesta cesta = (Cesta) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("TB.CDEMPRESA = ", cesta.cdEmpresa);
		sqlWhereClause.addAndCondition("TB.CDREPRESENTANTE = ", cesta.cdRepresentante);
		sqlWhereClause.addAndCondition("TB.CDCESTA = ", cesta.cdCesta);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", cesta.cdClienteFilter);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
    	super.addJoin(domainFilter, sql);
    	Cesta cesta = (Cesta) domainFilter;
    	if (cesta != null && ValueUtil.isNotEmpty(cesta.cdClienteFilter)) {
    		sql.append(" LEFT OUTER JOIN TBLVPCESTAPOSITCLIENTE CESTACLI ON ");
    		sql.append(" TB.CDEMPRESA = CESTACLI.CDEMPRESA ");
    		sql.append(" AND TB.CDREPRESENTANTE = CESTACLI.CDREPRESENTANTE ");
    		sql.append(" AND TB.CDCESTA = CESTACLI.CDCESTA ");
    	}
    }

}