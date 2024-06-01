package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.RelInadimpRep;
import totalcross.sql.ResultSet;

public class RelInadimpRepPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RelInadimpRep();
	}

    private static RelInadimpRepPdbxDao instance;

    public RelInadimpRepPdbxDao() {
        super(RelInadimpRep.TABLE_NAME);
    }

    public static RelInadimpRepPdbxDao getInstance() {
        if (instance == null) {
            instance = new RelInadimpRepPdbxDao();
        }
        return instance;
    }

    @Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

    protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	RelInadimpRep relInadimpRep = new RelInadimpRep();
        relInadimpRep.rowKey = rs.getString("rowkey");
        relInadimpRep.cdEmpresa = rs.getString("cdEmpresa");
        relInadimpRep.cdRepresentante = rs.getString("cdRepresentante");
        relInadimpRep.qtClientes = rs.getInt("qtClientes");
        relInadimpRep.qtTitulos = rs.getInt("qtTitulos");
        relInadimpRep.vlTitulos = ValueUtil.round(rs.getDouble("vlTitulos"));
        relInadimpRep.nmRepresentante = rs.getString("NMREPRESENTANTE");
        return relInadimpRep;
	}

	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
		sql.append(" TB.rowKey,");
		sql.append(" TB.CDEMPRESA,");
		sql.append(" TB.CDREPRESENTANTE,");
		sql.append(" TB.QTCLIENTES,");
		sql.append(" TB.QTTITULOS,");
		sql.append(" TB.VLTITULOS,");
		sql.append(" REP.NMREPRESENTANTE");
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
		sql.append(" TB.rowKey,");
		sql.append(" TB.CDEMPRESA,");
		sql.append(" TB.CDREPRESENTANTE,");
		sql.append(" TB.QTCLIENTES,");
		sql.append(" TB.QTTITULOS,");
		sql.append(" TB.VLTITULOS");
	}

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	RelInadimpRep relInadimpRep = new RelInadimpRep();
        relInadimpRep.rowKey = rs.getString("rowkey");
        relInadimpRep.cdEmpresa = rs.getString("cdEmpresa");
        relInadimpRep.cdRepresentante = rs.getString("cdRepresentante");
        relInadimpRep.qtClientes = rs.getInt("qtClientes");
        relInadimpRep.qtTitulos = rs.getInt("qtTitulos");
        relInadimpRep.vlTitulos = ValueUtil.round(rs.getDouble("vlTitulos"));
        return relInadimpRep;
    }

    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        RelInadimpRep relInadimpRep = (RelInadimpRep) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addJoin(" JOIN TBLVPREPRESENTANTE REP WHERE TB.CDREPRESENTANTE = REP.CDREPRESENTANTE");
		sqlWhereClause.addAndCondition("TB.CDEMPRESA = ", relInadimpRep.cdEmpresa);
		sqlWhereClause.addAndCondition("TB.CDREPRESENTANTE = ", relInadimpRep.cdRepresentante);
		sql.append(sqlWhereClause.getSql());
    }

	protected void addSelectGridColumns(StringBuffer sql) {
		sql.append(" TB.rowKey,");
		sql.append(" TB.CDREPRESENTANTE,");
		sql.append(" REP.NMREPRESENTANTE,");
		sql.append(" TB.QTTITULOS,");
		sql.append(" TB.VLTITULOS");
	}
	
	@Override
	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		if ((domain != null) && !ValueUtil.isEmpty(domain.sortAtributte)) {
			sql.append("order by ");
			if ("NMREPRESENTANTE".equalsIgnoreCase(domain.sortAtributte)) {
				sql.append("REP." + domain.sortAtributte);
			} else {
				sql.append("TB." + domain.sortAtributte);
			}
			if (ValueUtil.isNotEmpty(domain.sortAsc)) {
				sql.append(ValueUtil.getBooleanValue(domain.sortAsc) ? " ASC" : " DESC");
			}
		}
	}
}