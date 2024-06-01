package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Verba;
import totalcross.sql.ResultSet;

public class VerbaPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Verba();
	}

    private static VerbaPdbxDao instance;

    public VerbaPdbxDao() {
        super(Verba.TABLE_NAME);
    }

    public static VerbaPdbxDao getInstance() {
        if (instance == null) {
            instance = new VerbaPdbxDao();
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
    	Verba verba = new Verba();
        verba.rowKey = rs.getString("rowkey");
        verba.cdEmpresa = rs.getString("cdEmpresa");
        verba.cdRepresentante = rs.getString("cdRepresentante");
        verba.cdVerba = rs.getString("cdVerba");
        verba.cdTipoGeracao = rs.getString("cdTipoGeracao");
        verba.cdContaCorrente = rs.getString("cdContaCorrente");
        verba.cdTipoMultiplo = rs.getString("cdTipoMultiplo");
        verba.dsVerba = rs.getString("dsVerba");
        verba.flMixObrigatorio = rs.getString("flMixObrigatorio");
        return verba;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDVERBA,");
        sql.append(" CDTIPOGERACAO,");
        sql.append(" CDCONTACORRENTE,");
        sql.append(" CDTIPOMULTIPLO,");
        sql.append(" DSVERBA,");
        sql.append(" FLMIXOBRIGATORIO");
    }

     //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Verba verba = (Verba) domain;
        sql.append(" where CDEMPRESA = ").append(Sql.getValue(verba.cdEmpresa));
        if (!ValueUtil.isEmpty(verba.cdRepresentante)) {
        	sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(verba.cdRepresentante));
        }
        if (!ValueUtil.isEmpty(verba.cdVerba)) {
        	sql.append(" and CDVERBA = ").append(Sql.getValue(verba.cdVerba));
        }
    }
}