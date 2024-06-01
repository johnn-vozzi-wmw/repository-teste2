package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.VerbaProduto;
import totalcross.sql.ResultSet;

public class VerbaProdutoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VerbaProduto();
	}

    private static VerbaProdutoPdbxDao instance;

    public VerbaProdutoPdbxDao() {
        super(VerbaProduto.TABLE_NAME);
    }

    public static VerbaProdutoPdbxDao getInstance() {
        if (instance == null) {
            instance = new VerbaProdutoPdbxDao();
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
    	VerbaProduto verbaProduto = new VerbaProduto();
        verbaProduto.rowKey = rs.getString("rowkey");
        verbaProduto.cdEmpresa = rs.getString("cdEmpresa");
        verbaProduto.cdRepresentante = rs.getString("cdRepresentante");
        verbaProduto.cdVerba = rs.getString("cdVerba");
        verbaProduto.cdProduto = rs.getString("cdProduto");
        verbaProduto.vlMultiplo = ValueUtil.round(rs.getDouble("vlMultiplo"));
        verbaProduto.vlVerba = ValueUtil.round(rs.getDouble("vlVerba"));
        verbaProduto.flGeraVerba = rs.getString("flGeraVerba");
        return verbaProduto;
    }

    //@Override
    protected BaseDomain populateCache(ResultSet rs) throws java.sql.SQLException {
    	VerbaProduto verbaProduto = new VerbaProduto();
        verbaProduto.rowKey = rs.getString("rowkey");
        verbaProduto.cdEmpresa = rs.getString("cdEmpresa");
        verbaProduto.cdRepresentante = rs.getString("cdRepresentante");
        verbaProduto.cdVerba = rs.getString("cdVerba");
        verbaProduto.cdProduto = rs.getString("cdProduto");
        verbaProduto.vlMultiplo = ValueUtil.round(rs.getDouble("vlMultiplo"));
        verbaProduto.vlVerba = ValueUtil.round(rs.getDouble("vlVerba"));
        verbaProduto.flGeraVerba = rs.getString("flGeraVerba");
        return verbaProduto;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDVERBA,");
        sql.append(" CDPRODUTO,");
        sql.append(" VLMULTIPLO,");
        sql.append(" VLVERBA,");
        sql.append(" FLGERAVERBA");
    }

    //@Override
    protected void addCacheColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDVERBA,");
        sql.append(" CDPRODUTO,");
        sql.append(" VLMULTIPLO,");
        sql.append(" VLVERBA,");
        sql.append(" FLGERAVERBA");
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VerbaProduto verbaProduto = (VerbaProduto) domain;
        sql.append(" where CDEMPRESA = ").append(Sql.getValue(verbaProduto.cdEmpresa));
        if (!ValueUtil.isEmpty(verbaProduto.cdRepresentante)) {
        	sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(verbaProduto.cdRepresentante));
        }
        if (!ValueUtil.isEmpty(verbaProduto.cdVerba)) {
        	sql.append(" and CDVERBA = ").append(Sql.getValue(verbaProduto.cdVerba));
        }
        if (!ValueUtil.isEmpty(verbaProduto.cdProduto)) {
        	sql.append(" and CDPRODUTO = ").append(Sql.getValue(verbaProduto.cdProduto));
        }
        if (!ValueUtil.isEmpty(verbaProduto.flGeraVerba)) {
        	sql.append(" and FLGERAVERBA = ").append(Sql.getValue(verbaProduto.flGeraVerba));
        }
    }
}