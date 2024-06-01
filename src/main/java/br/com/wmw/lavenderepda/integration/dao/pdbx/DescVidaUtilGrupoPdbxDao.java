package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.DescVidaUtilGrupo;
import totalcross.sql.ResultSet;

public class DescVidaUtilGrupoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescVidaUtilGrupo();
	}

    private static DescVidaUtilGrupoPdbxDao instance;

    public DescVidaUtilGrupoPdbxDao() {
        super(DescVidaUtilGrupo.TABLE_NAME);
    }

    public static DescVidaUtilGrupoPdbxDao getInstance() {
        if (instance == null) {
            instance = new DescVidaUtilGrupoPdbxDao();
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
        sql.append(" cdGrupoProduto1,");
        sql.append(" vlPctVidaUtilMinimo,");
        sql.append(" vlPctVidaUtilMaximo,");
        sql.append(" vlPctDesconto");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        DescVidaUtilGrupo descvidautilgrupo = new DescVidaUtilGrupo();
        descvidautilgrupo.rowKey = rs.getString("rowkey");
        descvidautilgrupo.cdGrupoproduto1 = rs.getString("cdGrupoproduto1");
        descvidautilgrupo.vlPctvidautilminimo = ValueUtil.round(rs.getDouble("vlPctvidautilminimo"));
        descvidautilgrupo.vlPctvidautilmaximo = ValueUtil.round(rs.getDouble("vlPctvidautilmaximo"));
        descvidautilgrupo.vlPctDesconto = ValueUtil.round(rs.getDouble("vlPctDesconto"));
        return descvidautilgrupo;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        DescVidaUtilGrupo descvidautilgrupo = (DescVidaUtilGrupo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("cdGrupoProduto1 = ", descvidautilgrupo.cdGrupoproduto1);
		sqlWhereClause.addAndCondition("vlPctVidaUtilMinimo <= ", descvidautilgrupo.vlPctvidautilminimo);
		sqlWhereClause.addAndCondition("vlPctvidautilmaximo >= ", descvidautilgrupo.vlPctvidautilmaximo);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}