package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.DescProgressivo;
import totalcross.sql.ResultSet;

public class DescProgressivoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescProgressivo();
	}

    private static DescProgressivoPdbxDao instance;

    public DescProgressivoPdbxDao() {
        super(DescProgressivo.TABLE_NAME);
    }

    public static DescProgressivoPdbxDao getInstance() {
        if (instance == null) {
            instance = new DescProgressivoPdbxDao();
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
        sql.append(" CDTABELAPRECO,");
        sql.append(" CDCONDICAOCOMERCIAL,");
        sql.append(" VLINICIOFAIXA,");
        sql.append(" VLFINALFAIXA,");
        sql.append(" VLPCTDESCONTO");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        DescProgressivo descontoProgressivo = new DescProgressivo();
        descontoProgressivo.rowKey = rs.getString("rowkey");
        descontoProgressivo.cdEmpresa = rs.getString("cdEmpresa");
        descontoProgressivo.cdRepresentante = rs.getString("cdRepresentante");
        descontoProgressivo.cdTabelaPreco = rs.getString("cdTabelaPreco");
        descontoProgressivo.cdCondicaoComercial = rs.getString("cdCondicaoComercial");
        descontoProgressivo.vlInicioFaixa = ValueUtil.round(rs.getDouble("vlInicioFaixa"));
        descontoProgressivo.vlFinalFaixa = ValueUtil.round(rs.getDouble("vlFinalFaixa"));
        descontoProgressivo.vlPctDesconto = ValueUtil.round(rs.getDouble("vlPctDesconto"));
        return descontoProgressivo;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        DescProgressivo descontoProgressivo = (DescProgressivo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", descontoProgressivo.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", descontoProgressivo.cdRepresentante);
		sqlWhereClause.addAndCondition("CDTABELAPRECO = ", descontoProgressivo.cdTabelaPreco);
		sqlWhereClause.addAndCondition("CDCONDICAOCOMERCIAL = ", descontoProgressivo.cdCondicaoComercial);
		sqlWhereClause.addAndCondition("VLINICIOFAIXA < ", descontoProgressivo.vlInicioFaixa);
		sqlWhereClause.addAndCondition("VLFINALFAIXA > ", descontoProgressivo.vlFinalFaixa);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}