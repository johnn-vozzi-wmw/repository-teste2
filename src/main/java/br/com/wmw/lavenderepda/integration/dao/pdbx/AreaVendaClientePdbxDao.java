package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.AreaVendaCliente;
import totalcross.sql.ResultSet;

public class AreaVendaClientePdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new AreaVendaCliente();
	}

    private static AreaVendaClientePdbxDao instance;

    public AreaVendaClientePdbxDao() {
        super(AreaVendaCliente.TABLE_NAME);
    }

    public static AreaVendaClientePdbxDao getInstance() {
        if (instance == null) {
            instance = new AreaVendaClientePdbxDao();
        }
        return instance;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" cdEmpresa,");
        sql.append(" cdRepresentante,");
        sql.append(" cdAreaVenda,");
        sql.append(" cdCliente,");
        sql.append(" flTipoAlteracao");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        AreaVendaCliente areavendacliente = new AreaVendaCliente();
        areavendacliente.rowKey = rs.getString("rowkey");
        areavendacliente.cdEmpresa = rs.getString("cdEmpresa");
        areavendacliente.cdRepresentante = rs.getString("cdRepresentante");
        areavendacliente.cdAreavenda = rs.getString("cdAreavenda");
        areavendacliente.cdCliente = rs.getString("cdCliente");
        areavendacliente.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return areavendacliente;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        AreaVendaCliente areavendacliente = (AreaVendaCliente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("cdEmpresa = ", areavendacliente.cdEmpresa);
		sqlWhereClause.addAndCondition("cdRepresentante = ", areavendacliente.cdRepresentante);
		sqlWhereClause.addAndCondition("cdAreaVenda = ", areavendacliente.cdAreavenda);
		sqlWhereClause.addAndCondition("cdCliente = ", areavendacliente.cdCliente);
		//--
		sql.append(sqlWhereClause.getSql());
    }

    public String[] findCdsAreaVendaByExample(BaseDomain domain) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT CDAREAVENDA FROM ");
    	sql.append(tableName);
    	addWhereByExample(domain, sql);
 	   return getCurrentDriver().getStrings1(sql.toString()); 
    }
}