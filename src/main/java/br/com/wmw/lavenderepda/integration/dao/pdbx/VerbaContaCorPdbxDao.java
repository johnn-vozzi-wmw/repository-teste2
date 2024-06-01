package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.VerbaContaCor;
import totalcross.sql.ResultSet;

public class VerbaContaCorPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VerbaContaCor();
	}

    private static VerbaContaCorPdbxDao instance;

    public VerbaContaCorPdbxDao() {
        super(VerbaContaCor.TABLE_NAME);
    }

    public static VerbaContaCorPdbxDao getInstance() {
        if (instance == null) {
            instance = new VerbaContaCorPdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	VerbaContaCor verbaContaCorrente = new VerbaContaCor();
        verbaContaCorrente.rowKey = rs.getString("rowkey");
        verbaContaCorrente.cdEmpresa = rs.getString("cdEmpresa");
        verbaContaCorrente.cdRepresentante = rs.getString("cdRepresentante");
        verbaContaCorrente.cdContaCorrente = rs.getString("cdContaCorrente");
        verbaContaCorrente.dsContaCorrente = rs.getString("dsContaCorrente");
        return verbaContaCorrente;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCONTACORRENTE,");
        sql.append(" DSCONTACORRENTE");
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VerbaContaCor verbaContaCorrente = (VerbaContaCor) domain;
        sql.append(" where CDEMPRESA = ").append(Sql.getValue(verbaContaCorrente.cdEmpresa));
        if (!ValueUtil.isEmpty(verbaContaCorrente.cdRepresentante)) {
        	sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(verbaContaCorrente.cdRepresentante));
        }
        if (!ValueUtil.isEmpty(verbaContaCorrente.cdContaCorrente)) {
        	sql.append(" and CDCONTACORRENTE = ").append(Sql.getValue(verbaContaCorrente.cdContaCorrente));
        }

    }
}