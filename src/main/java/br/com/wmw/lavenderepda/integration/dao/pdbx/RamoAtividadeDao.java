package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.RamoAtividade;
import totalcross.sql.ResultSet;

public class RamoAtividadeDao extends LavendereCrudDbxDao {

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RamoAtividade();
	}

	private static RamoAtividadeDao instance;

    public RamoAtividadeDao() {
        super(RamoAtividade.TABLE_NAME);
    }

    public static RamoAtividadeDao getInstance() {
        if (instance == null) {
            instance = new RamoAtividadeDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	RamoAtividade centroCusto = new RamoAtividade();
        centroCusto.rowKey = rs.getString("rowkey");
        centroCusto.cdEmpresa = rs.getString("cdEmpresa");
        centroCusto.cdRepresentante = rs.getString("cdRepresentante");
        centroCusto.cdRamoAtividade = rs.getString("cdRamoAtividade");
        centroCusto.dsRamoAtividade = rs.getString("dsRamoAtividade");
        return centroCusto;
    }


    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDRAMOATIVIDADE,");
        sql.append(" DSRAMOATIVIDADE");
    }

	//@Override
	protected void addInsertColumns(StringBuffer arg0) {
	}

	//@Override
	protected void addInsertValues(BaseDomain arg0, StringBuffer arg1) {
	}

	//@Override
	protected void addUpdateValues(BaseDomain arg0, StringBuffer arg1) {
	}

	//@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		RamoAtividade centroCusto = (RamoAtividade) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", centroCusto.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", centroCusto.cdRepresentante);
		sqlWhereClause.addAndCondition("CDRAMOATIVIDADE = ", centroCusto.cdRamoAtividade);
		sql.append(sqlWhereClause.getSql());
	}

}