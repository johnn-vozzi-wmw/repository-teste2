package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Fornecedor;
import totalcross.sql.ResultSet;

public class FornecedorPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Fornecedor();
	}

    private static FornecedorPdbxDao instance;

    public FornecedorPdbxDao() {
        super(Fornecedor.TABLE_NAME);
    }

    public static FornecedorPdbxDao getInstance() {
        if (instance == null) {
            instance = new FornecedorPdbxDao();
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
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDFORNECEDOR,");
        sql.append(" tb.NMRAZAOSOCIAL,");
        sql.append(" tb.NMFANTASIA");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.rowKey = rs.getString("rowkey");
        fornecedor.cdEmpresa = rs.getString("cdEmpresa");
        fornecedor.cdFornecedor = rs.getString("cdFornecedor");
        fornecedor.nmRazaoSocial = rs.getString("nmRazaoSocial");
        fornecedor.nmFantasia = rs.getString("nmFantasia");
        return fornecedor;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Fornecedor fornecedor = (Fornecedor) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", fornecedor.cdEmpresa);
		if (ValueUtil.isNotEmpty(fornecedor.cdRepresentante)) {
			sqlWhereClause.addAndCondition("FREP.CDREPRESENTANTE = ", fornecedor.cdRepresentante);
		}
		sql.append(sqlWhereClause.getSql());
    }

    //@Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	sql.append(" order by NMFANTASIA ");
    }
    
    @Override
    protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
    	Fornecedor filter = (Fornecedor)domainFilter;
    	if (filter != null && ValueUtil.isNotEmpty(filter.cdRepresentante)) {
    		sql.append(" JOIN TBLVPFORNECEDORREP FREP ON")
    		.append(" tb.CDEMPRESA = FREP.CDEMPRESA AND")
    		.append(" tb.CDFORNECEDOR = FREP.CDFORNECEDOR");
    	}
    }

}