package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.TabelaPrecoReg;
import totalcross.sql.ResultSet;

public class TabelaPrecoRegDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TabelaPrecoReg();
	}

    private static TabelaPrecoRegDbxDao instance;

    public TabelaPrecoRegDbxDao() {
        super(TabelaPrecoReg.TABLE_NAME);
    }

    public static TabelaPrecoRegDbxDao getInstance() {
        if (instance == null) {
            instance = new TabelaPrecoRegDbxDao();
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
		sql.append(" cdTabelaPreco,");
		sql.append(" cdRegiao");
	}

	//@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        TabelaPrecoReg tabelaPrecoReg = new TabelaPrecoReg();
        tabelaPrecoReg.rowKey = rs.getString("rowkey");
        tabelaPrecoReg.cdEmpresa = rs.getString("cdEmpresa");
        tabelaPrecoReg.cdRepresentante = rs.getString("cdRepresentante");
        tabelaPrecoReg.cdTabelaPreco = rs.getString("cdTabelaPreco");
        tabelaPrecoReg.cdRegiao = rs.getString("cdRegiao");
        return tabelaPrecoReg;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TabelaPrecoReg tabelaPrecoReg = (TabelaPrecoReg) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", tabelaPrecoReg.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", tabelaPrecoReg.cdRepresentante);
		sqlWhereClause.addAndCondition("cdTabelaPreco = ", tabelaPrecoReg.cdTabelaPreco);
		sqlWhereClause.addAndCondition("cdRegiao = ", tabelaPrecoReg.cdRegiao);
		//--
		sql.append(sqlWhereClause.getSql());
    }
}