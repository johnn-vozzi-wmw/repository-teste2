package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class SupervisorRepPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new SupervisorRep();
	}

    private static SupervisorRepPdbxDao instance;

    public SupervisorRepPdbxDao() {
        super(SupervisorRep.TABLE_NAME);
    }

    public static SupervisorRepPdbxDao getInstance() {
        if (instance == null) {
            instance = new SupervisorRepPdbxDao();
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
		sql.append(" tb.CDSUPERVISOR");
	}

	//@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	SupervisorRep supervisorRep = new SupervisorRep();
        supervisorRep.rowKey = rs.getString("rowkey");
        supervisorRep.cdEmpresa = rs.getString("cdEmpresa");
        supervisorRep.cdRepresentante = rs.getString("cdRepresentante");
        supervisorRep.cdSupervisor = rs.getString("cdSupervisor");
        return supervisorRep;
    }

    //@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		SupervisorRep supervisorRep = (SupervisorRep) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" CDEMPRESA = ", supervisorRep.cdEmpresa);
		sqlWhereClause.addAndCondition("CDSUPERVISOR = ", supervisorRep.cdSupervisor);
		sql.append(sqlWhereClause.getSql());
	}
    
    public Vector findAllRepresentantesLiberadosSenhaSupervisor(int cdparametro) throws SQLException { 
    	StringBuffer sql = new StringBuffer();
    	sql.append(" select ");
    	addSelectColumns(null, sql);
    	sql.append(" FROM TBLVPSUPERVISORREP tb")
    	.append(" JOIN TBLVPVALORPARAMETRO parametro ON '['||tb.CDREPRESENTANTE||']' = parametro.VLCHAVE")
    	.append(" WHERE CDPARAMETRO = ").append(Sql.getValue(cdparametro));
    	return findAll(null, sql.toString());
    }
    
}