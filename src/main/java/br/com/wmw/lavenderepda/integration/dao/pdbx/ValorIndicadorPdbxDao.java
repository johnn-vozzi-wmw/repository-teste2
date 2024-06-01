package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ValorIndicador;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ValorIndicadorPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ValorIndicador();
	}

    private static ValorIndicadorPdbxDao instance;

    public ValorIndicadorPdbxDao() {
        super(ValorIndicador.TABLE_NAME);
    }

    public static ValorIndicadorPdbxDao getInstance() {
        if (instance == null) {
            instance = new ValorIndicadorPdbxDao();
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
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        ValorIndicador valorIndicador = new ValorIndicador();
        valorIndicador.rowKey = rs.getString("rowKey");
        valorIndicador.cdEmpresa = rs.getString("cdEmpresa");
        valorIndicador.cdRepresentante = rs.getString("cdRepresentante");
        valorIndicador.dsPeriodo = rs.getString("dsPeriodo");
        valorIndicador.cdIndicador = rs.getString("cdIndicador");
        valorIndicador.dsVlIndicador = rs.getString("dsVlIndicador");
        return valorIndicador;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowKey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDINDICADOR,");
        sql.append(" DSPERIODO,");
        sql.append(" DSVLINDICADOR,");
        sql.append(" DTREFERENCIA");
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ValorIndicador valorIndicador = (ValorIndicador) domain;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
       	sqlWhereClause.addAndCondition("CDEMPRESA = ", valorIndicador.cdEmpresa);
       	sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", valorIndicador.cdRepresentante);
       	sqlWhereClause.addAndCondition("CDINDICADOR = ", valorIndicador.cdIndicador);
       	sqlWhereClause.addAndCondition("DSPERIODO = ", valorIndicador.dsPeriodo);
        sql.append(sqlWhereClause.getSql());
    }

    //@Override
    public Vector findAllDistinctPeriodo() throws SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append(" select distinct DSPERIODO FROM ");
        sql.append(tableName);
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector(50);
			while (rs.next()) {
		        result.addElement(rs.getString(1));
			}
			return result;
		}
    }
    
	public Vector findAllValorIndicador(BaseDomain domain) throws SQLException {
		ValorIndicador valorIndicadorFilter = (ValorIndicador) domain;
		StringBuffer sql = getSqlBuffer();
		sql.append(" select tb.*, ind.DSMASCARAFORMATO from ");
		sql.append(tableName);
		sql.append(" tb");
		sql.append(" join TBLVPINDICADOR ind on (tb.cdindicador = ind.cdindicador)");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" tb.CDEMPRESA = ", valorIndicadorFilter.cdEmpresa);
		sqlWhereClause.addAndCondition(" tb.CDREPRESENTANTE = ", valorIndicadorFilter.cdRepresentante);
		sqlWhereClause.addAndCondition(" tb.CDINDICADOR = ", valorIndicadorFilter.cdIndicador);
		sqlWhereClause.addAndCondition(" tb.DSPERIODO = ", valorIndicadorFilter.dsPeriodo);
		sql.append(sqlWhereClause.getSql());
		sql.append("order by ind.nuSequencia");
		return findAllValorIndicador(sql);
	}

	private Vector findAllValorIndicador(StringBuffer sql) throws SQLException {
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector();
			ValorIndicador valorIndicador;
			while (rs.next()) {
				valorIndicador = new ValorIndicador();
				valorIndicador.rowKey = rs.getString("rowKey");
				valorIndicador.cdEmpresa = rs.getString("cdEmpresa");
				valorIndicador.cdRepresentante = rs.getString("cdRepresentante");
				valorIndicador.dsPeriodo = rs.getString("dsPeriodo");
				valorIndicador.cdIndicador = rs.getString("cdIndicador");
				valorIndicador.dsVlIndicador = rs.getString("dsVlIndicador");
				valorIndicador.dsMascaraFormato = rs.getString("dsMascaraFormato");
				result.addElement(valorIndicador);
			}
			return result;
		}
	}
	
}