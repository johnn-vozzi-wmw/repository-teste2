package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.TributacaoVlBase;
import totalcross.sql.ResultSet;

public class TributacaoVlBasePdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TributacaoVlBase();
	}

    private static TributacaoVlBasePdbxDao instance;

    public TributacaoVlBasePdbxDao() {
        super(TributacaoVlBase.TABLE_NAME);
    }

    public static TributacaoVlBasePdbxDao getInstance() {
        if (instance == null) {
            instance = new TributacaoVlBasePdbxDao();
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
        TributacaoVlBase tributacaovlbase = new TributacaoVlBase();
        tributacaovlbase.rowKey = rs.getString("rowkey");
        tributacaovlbase.cdEmpresa = rs.getString("cdEmpresa");
        tributacaovlbase.cdRepresentante = rs.getString("cdRepresentante");
        tributacaovlbase.cdTributacaoCliente = rs.getString("cdTributacaocliente");
        tributacaovlbase.cdTributacaoProduto = rs.getString("cdTributacaoproduto");
        tributacaovlbase.cdProduto = rs.getString("cdProduto");
        tributacaovlbase.vlBaseIcms = ValueUtil.round(rs.getDouble("vlBaseicms"));
        tributacaovlbase.vlBaseIcmsCalcRetido = ValueUtil.round(rs.getDouble("vlBaseicmscalcretido"));
        tributacaovlbase.vlBaseIcmsRetidoCalcRetido = ValueUtil.round(rs.getDouble("vlBaseicmsretidocalcretido"));
        tributacaovlbase.vlPrecoMedioPonderado = ValueUtil.round(rs.getDouble("vlPrecoMedioPonderado"));
        return tributacaovlbase;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" cdTributacaoCliente,");
        sql.append(" cdTributacaoProduto,");
        sql.append(" cdProduto,");
        sql.append(" vlBaseIcms,");
        sql.append(" vlBaseIcmsCalcRetido,");
        sql.append(" vlBaseIcmsRetidoCalcRetido,");
        sql.append(" vlPrecoMedioPonderado");
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TributacaoVlBase tributacaovlbase = (TributacaoVlBase) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", tributacaovlbase.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", tributacaovlbase.cdRepresentante);
		sqlWhereClause.addAndCondition("cdTributacaoCliente = ", tributacaovlbase.cdTributacaoCliente);
		sqlWhereClause.addAndCondition("cdTributacaoProduto = ", tributacaovlbase.cdTributacaoProduto);
		sqlWhereClause.addAndCondition("cdProduto = ", tributacaovlbase.cdProduto);
		sql.append(sqlWhereClause.getSql());
    }
}