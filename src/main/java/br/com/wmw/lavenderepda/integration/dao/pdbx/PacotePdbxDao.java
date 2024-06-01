package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Pacote;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class PacotePdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Pacote();
	}

    private static PacotePdbxDao instance;

    public PacotePdbxDao() {
        super(Pacote.TABLE_NAME);
    }

    public static PacotePdbxDao getInstance() {
        if (instance == null) {
            instance = new PacotePdbxDao();
        }
        return instance;
    }
    
    protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {}
    protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException { return null; }
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {}
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {}
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {}

    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowKey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPACOTE,");
        sql.append(" DSPACOTE");
    }

    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        Pacote pacote = new Pacote();
        pacote.rowKey = rs.getString("rowkey");
        pacote.cdEmpresa = rs.getString("cdEmpresa");
        pacote.cdRepresentante = rs.getString("cdRepresentante");
        pacote.cdPacote = rs.getString("cdPacote");
        pacote.dsPacote = rs.getString("dsPacote");
        return pacote;
    }
    
    protected void addWhereByExample(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        Pacote pacotefilter = (Pacote) domainFilter;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
        sqlWhereClause.addAndCondition("CDEMPRESA = ", pacotefilter.cdEmpresa);
        sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", pacotefilter.cdRepresentante);
        sqlWhereClause.addAndCondition("CDPACOTE = ", pacotefilter.cdPacote);
        sql.append(sqlWhereClause.getSql());
    }
    
    public Vector findAllPacoteByItemPedido(Pacote pacoteFilter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" SELECT TB.rowkey, TB.CDEMPRESA, TB.CDREPRESENTANTE, TB.CDPACOTE, TB.DSPACOTE FROM TBLVPPACOTE TB ")
    	.append(" JOIN TBLVPDESCONTOPACOTE DESCONTOPACOTE ON ")
    	.append("     TB.CDEMPRESA = DESCONTOPACOTE.CDEMPRESA")
    	.append(" AND TB.CDREPRESENTANTE = DESCONTOPACOTE.CDREPRESENTANTE")
    	.append(" AND TB.CDPACOTE = DESCONTOPACOTE.CDPACOTE")
    	.append(" AND DESCONTOPACOTE.CDPRODUTO = ").append(Sql.getValue(pacoteFilter.cdProdutoFilter))
    	.append(" AND DESCONTOPACOTE.CDTABELAPRECO = ").append(Sql.getValue(pacoteFilter.cdTabelaPrecoFilter));
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
        sqlWhereClause.addAndCondition("TB.CDEMPRESA = ", pacoteFilter.cdEmpresa);
        sqlWhereClause.addAndCondition("TB.CDREPRESENTANTE = ", pacoteFilter.cdRepresentante);
        sql.append(sqlWhereClause.getSql())	
    	.append(" GROUP BY TB.rowkey, TB.CDEMPRESA, TB.CDREPRESENTANTE, TB.CDPACOTE, TB.DSPACOTE");
        
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector(50);
			while (rs.next()) {
				result.addElement(populate(pacoteFilter, rs));
			}
			return result;
		}
    }

}