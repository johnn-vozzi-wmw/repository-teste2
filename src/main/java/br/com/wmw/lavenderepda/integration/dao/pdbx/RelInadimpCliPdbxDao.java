package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.RelInadimpCli;
import totalcross.sql.ResultSet;

public class RelInadimpCliPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RelInadimpCli();
	}

    private static RelInadimpCliPdbxDao instance;

    public RelInadimpCliPdbxDao() {
        super(RelInadimpCli.TABLE_NAME);
    }

    public static RelInadimpCliPdbxDao getInstance() {
        if (instance == null) {
            instance = new RelInadimpCliPdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

    protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	RelInadimpCli relInadimpCli = new RelInadimpCli();
        relInadimpCli.rowKey = rs.getString("rowkey");
        relInadimpCli.cdEmpresa = rs.getString("cdEmpresa");
        relInadimpCli.cdRepresentante = rs.getString("cdRepresentante");
        relInadimpCli.cdCliente = rs.getString("cdCliente");
        relInadimpCli.nmRazaoSocial = rs.getString("NMRAZAOSOCIAL");
        relInadimpCli.qtTitulos = rs.getInt("qtTitulos");
        relInadimpCli.vlTitulos = ValueUtil.round(rs.getDouble("vlTitulos"));
        relInadimpCli.qtDiasMaior = rs.getInt("qtDiasMaior");
        return relInadimpCli;
	}
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
        sql.append(" TB.rowKey,");
        sql.append(" TB.CDEMPRESA,");
        sql.append(" TB.CDREPRESENTANTE,");
        sql.append(" CLI.NMRAZAOSOCIAL,");
        sql.append(" TB.CDCLIENTE,");
        sql.append(" TB.QTTITULOS,");
        sql.append(" TB.VLTITULOS,");
        sql.append(" TB.QTDIASMAIOR");
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" TB.rowKey,");
        sql.append(" TB.CDEMPRESA,");
        sql.append(" TB.CDREPRESENTANTE,");
        sql.append(" TB.CDCLIENTE,");
        sql.append(" TB.QTTITULOS,");
        sql.append(" TB.VLTITULOS,");
        sql.append(" TB.QTDIASMAIOR");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	RelInadimpCli relInadimpCli = new RelInadimpCli();
        relInadimpCli.rowKey = rs.getString("rowkey");
        relInadimpCli.cdEmpresa = rs.getString("cdEmpresa");
        relInadimpCli.cdRepresentante = rs.getString("cdRepresentante");
        relInadimpCli.cdCliente = rs.getString("cdCliente");
        relInadimpCli.qtTitulos = rs.getInt("qtTitulos");
        relInadimpCli.vlTitulos = ValueUtil.round(rs.getDouble("vlTitulos"));
        relInadimpCli.qtDiasMaior = rs.getInt("qtDiasMaior");
        return relInadimpCli;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        RelInadimpCli relInadimpCli = (RelInadimpCli) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addJoin(" JOIN TBLVPCLIENTE CLI WHERE TB.CDEMPRESA = CLI.CDEMPRESA AND TB.CDREPRESENTANTE = CLI.CDREPRESENTANTE AND TB.CDCLIENTE = CLI.CDCLIENTE");
		sqlWhereClause.addAndCondition("TB.CDEMPRESA = ", relInadimpCli.cdEmpresa);
		sqlWhereClause.addAndCondition("TB.CDREPRESENTANTE = ", relInadimpCli.cdRepresentante);
		sqlWhereClause.addAndCondition("TB.CDCLIENTE = ", relInadimpCli.cdCliente);
		sql.append(sqlWhereClause.getSql());
    }

	protected void addSelectGridColumns(StringBuffer sql) {
		sql.append(" TB.rowKey,");
		sql.append(" TB.CDCLIENTE,");
		sql.append(" CLI.NMRAZAOSOCIAL,");
		sql.append(" TB.QTTITULOS,");
		sql.append(" TB.VLTITULOS");
	}
	
	@Override
	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		if ((domain != null) && !ValueUtil.isEmpty(domain.sortAtributte)) {
			sql.append("order by ");
			if ("NMRAZAOSOCIAL".equalsIgnoreCase(domain.sortAtributte)) {
				sql.append("CLI." + domain.sortAtributte);
			} else {
				sql.append("TB." + domain.sortAtributte);
			}
			if (ValueUtil.isNotEmpty(domain.sortAsc)) {
				sql.append(ValueUtil.getBooleanValue(domain.sortAsc) ? " ASC" : " DESC");
			}
		}
	}

}