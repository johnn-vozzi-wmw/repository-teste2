package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.VerbaTabelaPreco;
import totalcross.sql.ResultSet;

public class VerbaTabelaPrecoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VerbaTabelaPreco();
	}

    private static VerbaTabelaPrecoPdbxDao instance;

    public VerbaTabelaPrecoPdbxDao() {
        super(VerbaTabelaPreco.TABLE_NAME);
    }

    public static VerbaTabelaPrecoPdbxDao getInstance() {
        if (instance == null) {
            instance = new VerbaTabelaPrecoPdbxDao();
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
    	VerbaTabelaPreco verbaTabelaPreco = new VerbaTabelaPreco();
        verbaTabelaPreco.rowKey = rs.getString("rowkey");
        verbaTabelaPreco.cdEmpresa = rs.getString("cdEmpresa");
        verbaTabelaPreco.cdRepresentante = rs.getString("cdRepresentante");
        verbaTabelaPreco.cdVerba = rs.getString("cdVerba");
        verbaTabelaPreco.cdTabelaPreco = rs.getString("cdTabelaPreco");
        return verbaTabelaPreco;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDVERBA,");
        sql.append(" CDTABELAPRECO");
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VerbaTabelaPreco verbaTabelaPreco = (VerbaTabelaPreco) domain;
        sql.append(" where CDEMPRESA = ").append(Sql.getValue(verbaTabelaPreco.cdEmpresa));
        if (!ValueUtil.isEmpty(verbaTabelaPreco.cdRepresentante)) {
        	sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(verbaTabelaPreco.cdRepresentante));
        }
        if (!ValueUtil.isEmpty(verbaTabelaPreco.cdVerba)) {
        	sql.append(" and CDVERBA = ").append(Sql.getValue(verbaTabelaPreco.cdVerba));
        }
        if (!ValueUtil.isEmpty(verbaTabelaPreco.cdTabelaPreco)) {
        	sql.append(" and CDTABELAPRECO = ").append(Sql.getValue(verbaTabelaPreco.cdTabelaPreco));
        }
    }
}