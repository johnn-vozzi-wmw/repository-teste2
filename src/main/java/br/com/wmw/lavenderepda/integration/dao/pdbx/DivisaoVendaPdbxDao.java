package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.DivisaoVenda;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class DivisaoVendaPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DivisaoVenda();
	}

    private static DivisaoVendaPdbxDao instance;

    public DivisaoVendaPdbxDao() {
        super(DivisaoVenda.TABLE_NAME);
    }

    public static DivisaoVendaPdbxDao getInstance() {
        if (instance == null) {
            instance = new DivisaoVendaPdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
	
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey, ");
        sql.append(" cdEmpresa, ");
        sql.append(" cdRepresentante, ");
        sql.append(" cdDivisaoVenda, ");
        sql.append(" dsDivisaoVenda, ");
        sql.append(" flIgnoraVerbaGrpProd, ");
        sql.append(" flTipoAlteracao");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        DivisaoVenda divisaoVenda = new DivisaoVenda();
        divisaoVenda.rowKey = rs.getString("rowkey");
        divisaoVenda.cdEmpresa = rs.getString("cdEmpresa");
        divisaoVenda.cdRepresentante = rs.getString("cdRepresentante");
        divisaoVenda.cdDivisaoVenda = rs.getString("cdDivisaoVenda");
        divisaoVenda.dsDivisaoVenda = rs.getString("dsDivisaoVenda");
        divisaoVenda.flIgnoraVerbaGrpProd = rs.getString("flIgnoraVerbaGrpProd");
        divisaoVenda.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return divisaoVenda;
    }
    
    @Override
    protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        DivisaoVenda divisaoVenda = new DivisaoVenda();
        divisaoVenda.rowKey = rs.getString("rowkey");
        divisaoVenda.cdEmpresa = rs.getString("cdEmpresa");
        divisaoVenda.cdRepresentante = rs.getString("cdRepresentante");
        divisaoVenda.cdDivisaoVenda = rs.getString("cdDivisaoVenda");
        divisaoVenda.dsDivisaoVenda = rs.getString("dsDivisaoVenda");
        divisaoVenda.flIgnoraVerbaGrpProd = rs.getString("flIgnoraVerbaGrpProd");
        divisaoVenda.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return divisaoVenda;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        DivisaoVenda divisaoVenda = (DivisaoVenda) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals(" tb.cdEmpresa ", divisaoVenda.cdEmpresa);
		sqlWhereClause.addAndConditionEquals(" tb.cdRepresentante ", divisaoVenda.cdRepresentante);
		sqlWhereClause.addAndConditionEquals(" tb.cdDivisaoVenda ", divisaoVenda.cdDivisaoVenda);
		sql.append(sqlWhereClause.getSql());
    }

}