package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.DescComiFaixa;
import totalcross.sql.ResultSet;

public class DescComiFaixaPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescComiFaixa();
	}

    private static DescComiFaixaPdbxDao instance;

    public DescComiFaixaPdbxDao() {
        super(DescComiFaixa.TABLE_NAME);
    }

    public static DescComiFaixaPdbxDao getInstance() {
        if (instance == null) {
            instance = new DescComiFaixaPdbxDao();
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
        sql.append(" CDDESCCOMI,");
        sql.append(" QTITEM,");
        sql.append(" VLPCTDESCONTO,");
        sql.append(" vlPctComissao,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        DescComiFaixa desccomiprod = new DescComiFaixa();
        desccomiprod.rowKey = rs.getString("rowkey");
        desccomiprod.cdEmpresa = rs.getString("cdEmpresa");
        desccomiprod.cdDescComi = rs.getString("cdDescComi");
        desccomiprod.qtItem = ValueUtil.round(rs.getDouble("qtItem"));
        desccomiprod.vlPctDesconto = ValueUtil.round(rs.getDouble("vlPctDesconto"));
        desccomiprod.vlPctComissao = ValueUtil.round(rs.getDouble("vlPctComissao"));
        desccomiprod.flTipoAlteracao = rs.getString("flTipoAlteracao");
        desccomiprod.cdUsuario = rs.getString("cdUsuario");
        return desccomiprod;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        DescComiFaixa desccomiprod = (DescComiFaixa) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", desccomiprod.cdEmpresa);
		sqlWhereClause.addAndCondition("CDDESCCOMI = ", desccomiprod.cdDescComi);
		sqlWhereClause.addAndCondition("VLPCTCOMISSAO = ", desccomiprod.vlPctComissao);
		sqlWhereClause.addAndCondition("QTITEM <= ", desccomiprod.qtItem);
		sqlWhereClause.addAndCondition("VLPCTDESCONTO >= ", desccomiprod.vlPctDesconto);
		//--
		sql.append(sqlWhereClause.getSql());
    }

    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	sql.append(" order by VLPCTCOMISSAO desc, QTITEM, VLPCTDESCONTO");
    }

}