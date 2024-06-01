package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CondPagtoTabPreco;
import totalcross.sql.ResultSet;

public class CondPagtoTabPrecoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CondPagtoTabPreco();
	}

    private static CondPagtoTabPrecoPdbxDao instance;

    public CondPagtoTabPrecoPdbxDao() {
        super(CondPagtoTabPreco.TABLE_NAME);
    }

    public static CondPagtoTabPrecoPdbxDao getInstance() {
        if (instance == null) {
            instance = new CondPagtoTabPrecoPdbxDao();
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
        sql.append(" CDTABELAPRECO,");
        sql.append(" CDCONDICAOPAGAMENTO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" QTMINVALOR,");
        sql.append(" QTMINPRODUTO,");
        sql.append(" DTINICIAL,");
        sql.append(" DTFINAL");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        CondPagtoTabPreco condPagtoTabPreco = new CondPagtoTabPreco();
        condPagtoTabPreco.rowKey = rs.getString("rowkey");
        condPagtoTabPreco.cdEmpresa = rs.getString("cdEmpresa");
        condPagtoTabPreco.cdRepresentante = rs.getString("cdRepresentante");
        condPagtoTabPreco.cdTabelaPreco = rs.getString("cdTabelaPreco");
        condPagtoTabPreco.cdCondicaoPagamento = rs.getString("cdCondicaoPagamento");
        condPagtoTabPreco.flTipoAlteracao = rs.getString("flTipoAlteracao");
        condPagtoTabPreco.qtMinValor = rs.getDouble("qtMinValor");
        condPagtoTabPreco.qtMinProduto = rs.getInt("qtMinProduto");
        return condPagtoTabPreco;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CondPagtoTabPreco condPagtoTabPreco = (CondPagtoTabPreco) domain;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
       	sqlWhereClause.addAndCondition("CDEMPRESA = ", condPagtoTabPreco.cdEmpresa);
        sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", condPagtoTabPreco.cdRepresentante);
        sqlWhereClause.addAndCondition("CDTABELAPRECO = ", condPagtoTabPreco.cdTabelaPreco);
        sqlWhereClause.addAndCondition("CDCONDICAOPAGAMENTO = ", condPagtoTabPreco.cdCondicaoPagamento);
        if (LavenderePdaConfig.usaCondPagtoTabPrecoComVigencia) {
        	sqlWhereClause.addAndCondition("(DTINICIAL is null or DTINICIAL <= "+ Sql.getValue(condPagtoTabPreco.dtInicial)+")");
        	sqlWhereClause.addAndCondition("(DTFINAL is null or DTFINAL >= "+ Sql.getValue(condPagtoTabPreco.dtFinal)+")");
        }
        sql.append(sqlWhereClause.getSql());
    }

}