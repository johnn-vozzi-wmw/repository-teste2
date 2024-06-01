package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.TipoEntrega;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class TipoEntregaPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TipoEntrega();
	}

    private static TipoEntregaPdbxDao instance;

    public TipoEntregaPdbxDao() {
        super(TipoEntrega.TABLE_NAME);
    }

    public static TipoEntregaPdbxDao getInstance() {
        if (instance == null) {
            instance = new TipoEntregaPdbxDao();
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
        TipoEntrega tipoentrega = new TipoEntrega();
        tipoentrega.rowKey = rs.getString("rowkey");
        tipoentrega.cdEmpresa = rs.getString("cdEmpresa");
        tipoentrega.cdTipoentrega = rs.getString("cdTipoentrega");
        tipoentrega.dsTipoentrega = rs.getString("dsTipoentrega");
        return tipoentrega;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDTIPOENTREGA,");
        sql.append(" DSTIPOENTREGA");
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoEntrega tipoentrega = (TipoEntrega) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", tipoentrega.cdEmpresa);
		sqlWhereClause.addAndCondition("CDTIPOENTREGA = ", tipoentrega.cdTipoentrega);
		//--
		sql.append(sqlWhereClause.getSql());
    }

	public Vector findAllTipoEntregaSemEmpresa() throws SQLException {
		String sql = "select CDTIPOENTREGA, DSTIPOENTREGA from TBLVPTIPOENTREGA tb";
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql)) {
			Vector list = new Vector();
			while (rs.next()) {
				TipoEntrega tipoEntrega = new TipoEntrega();
				tipoEntrega.cdTipoentrega = rs.getString(1);
				tipoEntrega.dsTipoentrega = rs.getString(2);
				list.addElement(tipoEntrega);
			}
			return list;
		}
	}
}