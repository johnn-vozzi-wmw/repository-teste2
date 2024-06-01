package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.RepresentanteEmp;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class RepresentanteEmpPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RepresentanteEmp();
	}

    private static RepresentanteEmpPdbxDao instance;

    public RepresentanteEmpPdbxDao() {
        super(RepresentanteEmp.TABLE_NAME);
    }

    public static RepresentanteEmpPdbxDao getInstance() {
        if (instance == null) {
            instance = new RepresentanteEmpPdbxDao();
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
		sql.append(" tb.rowKey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.FLDEFAULT");
	}

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	RepresentanteEmp representanteEmp = new RepresentanteEmp();
        representanteEmp.rowKey = rs.getString("rowkey");
        representanteEmp.cdEmpresa = rs.getString("cdEmpresa");
        representanteEmp.cdRepresentante = rs.getString("cdRepresentante");
        representanteEmp.flDefault = rs.getString("flDefault");
        return representanteEmp;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        RepresentanteEmp representanteEmp = (RepresentanteEmp) domain;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
        sqlWhereClause.addAndCondition("tb.CDEMPRESA =", representanteEmp.cdEmpresa);
        sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE =", representanteEmp.cdRepresentante);
        //--
        sql.append(sqlWhereClause.getSql());
    }

    public Vector findAllByExampleJoinUsuarioPdaRep(BaseDomain domain) throws SQLException {
        RepresentanteEmp representanteEmp = (RepresentanteEmp) domain;
        //--
        StringBuffer sql = getSqlBuffer();
        sql.append(" select ");
        addSelectColumns(domain, sql);
        sql.append(" from ");
        sql.append(tableName);
        sql.append(" tb ");
        sql.append(" , TBLVPUSUARIOPDAREP rep ");
        sql.append(" where ");
        if (ValueUtil.isNotEmpty(representanteEmp.cdEmpresa)) {
        	sql.append(" tb.CDEMPRESA = '" + representanteEmp.cdEmpresa + "' and");
        }
        sql.append(" tb.CDREPRESENTANTE = rep.CDREPRESENTANTE ");
        addOrderBy(sql, domain);
        return findAll(null, sql.toString());
    }
    
    public Vector findAllOutrosCdEmpresas(String cdEmpresaNegacao, String cdRepresentante) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT CDEMPRESA FROM ");
		sql.append(tableName);
		sql.append(" WHERE CDEMPRESA != ").append(Sql.getValue(cdEmpresaNegacao));
        sql.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(cdRepresentante));
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector cdEmpresas = new Vector(3);
        	while (rs.next()) {
        		cdEmpresas.addElement(rs.getString(1));
        	}
        	return cdEmpresas;
    	}
	}
}