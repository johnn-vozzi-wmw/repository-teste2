package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.KitTabPreco;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class KitTabPrecoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new KitTabPreco();
	}

    private static KitTabPrecoPdbxDao instance;

    public KitTabPrecoPdbxDao() {
        super(KitTabPreco.TABLE_NAME);
    }

    public static KitTabPrecoPdbxDao getInstance() {
        if (instance == null) {
            instance = new KitTabPrecoPdbxDao();
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
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDKIT,");
        sql.append(" CDTABELAPRECO");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        KitTabPreco kitTabPreco = new KitTabPreco();
        kitTabPreco.rowKey = rs.getString("rowkey");
        kitTabPreco.cdEmpresa = rs.getString("cdEmpresa");
        kitTabPreco.cdRepresentante = rs.getString("cdRepresentante");
        kitTabPreco.cdKit = rs.getString("cdKit");
        kitTabPreco.cdTabelaPreco = rs.getString("cdTabelaPreco");
        return kitTabPreco;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        KitTabPreco kitTabPreco = (KitTabPreco) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", kitTabPreco.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", kitTabPreco.cdRepresentante);
		sqlWhereClause.addAndCondition("CDKIT = ", kitTabPreco.cdKit);
		sqlWhereClause.addAndCondition("CDTABELAPRECO = ", kitTabPreco.cdTabelaPreco);
		sql.append(sqlWhereClause.getSql());
    }
    
    public Vector findAllCdTabelaPreco(String cdKit) throws SQLException {
    	StringBuffer sql = new StringBuffer();
    	sql.append("SELECT CDTABELAPRECO FROM TBLVPKITTABPRECO");
    	sql.append(" WHERE CDEMPRESA =").append(Sql.getValue(SessionLavenderePda.cdEmpresa));
    	sql.append(" AND CDREPRESENTANTE =").append(Sql.getValue(SessionLavenderePda.getCdRepresentanteFiltroDados(KitTabPreco.class)));
    	sql.append(" AND CDKIT = ").append(Sql.getValue(cdKit));
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector cdTabelaPrecoList = new Vector();
        	while (rs.next()) {
        		cdTabelaPrecoList.addElement(rs.getString(1));
        	}
        	return cdTabelaPrecoList;
    	}
    }

}