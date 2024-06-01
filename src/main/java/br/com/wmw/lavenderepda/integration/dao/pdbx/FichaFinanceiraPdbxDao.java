package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.FichaFinanceira;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class FichaFinanceiraPdbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FichaFinanceira();
	}

    private static FichaFinanceiraPdbxDao instance;

    public FichaFinanceiraPdbxDao() {
        super(FichaFinanceira.TABLE_NAME);
    }

    public static FichaFinanceiraPdbxDao getInstance() {
        if (instance == null) {
            instance = new FichaFinanceiraPdbxDao();
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
        sql.append(" rowKey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" FLSTATUSCLIENTE");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        FichaFinanceira fichaFinanceira = new FichaFinanceira();
        fichaFinanceira.rowKey = rs.getString("rowkey");
        fichaFinanceira.cdEmpresa = rs.getString("cdEmpresa");
        fichaFinanceira.cdRepresentante = rs.getString("cdRepresentante");
        fichaFinanceira.cdCliente = rs.getString("cdCliente");
        fichaFinanceira.flStatusCliente = rs.getString("flStatuscliente");
        return fichaFinanceira;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        FichaFinanceira fichaFinanceira = (FichaFinanceira) domain;
        sql.append(" where CDEMPRESA = ").append(Sql.getValue(fichaFinanceira.cdEmpresa));
        sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(fichaFinanceira.cdRepresentante));
        sql.append(" and CDCLIENTE = ").append(Sql.getValue(fichaFinanceira.cdCliente));

    }


	//-----------------------------------------------------------------
	// ENTIDADES DINAMICAS - METODOS ESPECIFICOS PARA CONSULTAS INCLUINDO COLUNAS DINAMICAS
	//-----------------------------------------------------------------


	protected BasePersonDomain populateColumnsDynFixas(ResultSet rs) throws SQLException {
		FichaFinanceira fichaFinanceira = new FichaFinanceira();
        fichaFinanceira.vlSaldoCliente = ValueUtil.round(rs.getDouble("vlSaldoCliente"));
        fichaFinanceira.cdEmpresa = rs.getString("cdEmpresa");
        fichaFinanceira.cdRepresentante = rs.getString("cdRepresentante");
        fichaFinanceira.cdCliente = rs.getString("cdCliente");
		return fichaFinanceira;
	}

    public Vector findDistinctStatusCliente(FichaFinanceira fichaFinanceira) throws SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append("select distinct FLSTATUSCLIENTE from ");
        sql.append(tableName);
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
        sqlWhereClause.addAndCondition(" CDEMPRESA = ", fichaFinanceira.cdEmpresa);
        sqlWhereClause.addAndCondition(" CDREPRESENTANTE = ", fichaFinanceira.cdRepresentante);
        sql.append(sqlWhereClause.getSql());
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
        	Vector flStatusClientes = new Vector();
            while (rs.next()) {
                flStatusClientes.addElement(rs.getString(1));
            }
            return flStatusClientes;
        }
    }
}