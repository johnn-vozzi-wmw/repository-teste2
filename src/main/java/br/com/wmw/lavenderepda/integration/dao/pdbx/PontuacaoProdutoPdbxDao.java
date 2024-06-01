package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.PontuacaoProduto;
import totalcross.sql.ResultSet;

import java.sql.SQLException;

public class PontuacaoProdutoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PontuacaoProduto();
	}

    private static PontuacaoProdutoPdbxDao instance;

    public PontuacaoProdutoPdbxDao() {
        super(PontuacaoProduto.TABLE_NAME);
    }

    public static PontuacaoProdutoPdbxDao getInstance() {
        return instance == null ? instance = new PontuacaoProdutoPdbxDao() : instance;
    }
    
    protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {}
    protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException { return null; }
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {}
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {}
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {}

    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowKey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPONTUACAOCONFIG,");
        sql.append(" CDPRODUTO,");
        sql.append(" VLPESOPONTUACAO");
    }

    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        PontuacaoProduto pontuacaoProduto = new PontuacaoProduto();
        pontuacaoProduto.rowKey = rs.getString("rowkey");
        pontuacaoProduto.cdEmpresa = rs.getString("cdEmpresa");
        pontuacaoProduto.cdRepresentante = rs.getString("cdRepresentante");
        pontuacaoProduto.cdPontuacaoConfig = rs.getString("cdPontuacaoConfig");
        pontuacaoProduto.cdProduto = rs.getString("cdProduto");
        pontuacaoProduto.vlPesoPontuacao = rs.getDouble("vlPesoPontuacao");
        return pontuacaoProduto;
    }
    
    protected void addWhereByExample(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        PontuacaoProduto pontuacaoProdutoFilter = (PontuacaoProduto) domainFilter;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
        sqlWhereClause.addAndCondition("CDEMPRESA = ", pontuacaoProdutoFilter.cdEmpresa);
        sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", pontuacaoProdutoFilter.cdRepresentante);
        sqlWhereClause.addAndCondition("CDPONTUACAOCONFIG = ", pontuacaoProdutoFilter.cdPontuacaoConfig);
        sqlWhereClause.addAndCondition("CDPRODUTO = ", pontuacaoProdutoFilter.cdProduto);
        sql.append(sqlWhereClause.getSql());
    }

}