package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.UsuarioPdaRep;
import totalcross.sql.ResultSet;

public class UsuarioPdaRepPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new UsuarioPdaRep();
	}

    private static UsuarioPdaRepPdbxDao instance;

    public UsuarioPdaRepPdbxDao() {
        super(UsuarioPdaRep.TABLE_NAME);
    }

    public static UsuarioPdaRepPdbxDao getInstance() {
        if (instance == null) {
            instance = new UsuarioPdaRepPdbxDao();
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
        UsuarioPdaRep usuariopdarep = new UsuarioPdaRep();
        usuariopdarep.rowKey = rs.getString("rowkey");
        usuariopdarep.cdUsuario = rs.getString("cdUsuario");
        usuariopdarep.cdRepresentante = rs.getString("cdRepresentante");
        return usuariopdarep;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" cdUsuario,");
        sql.append(" CDREPRESENTANTE");
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        UsuarioPdaRep usuariopdarep = (UsuarioPdaRep) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("cdUsuario = ", usuariopdarep.cdUsuario);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", usuariopdarep.cdRepresentante);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}