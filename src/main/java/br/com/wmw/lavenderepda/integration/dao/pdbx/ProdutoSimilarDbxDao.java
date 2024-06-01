package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ProdutoSimilar;
import totalcross.sql.ResultSet;

public class ProdutoSimilarDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProdutoSimilar();
	}

    private static ProdutoSimilarDbxDao instance;

    public ProdutoSimilarDbxDao() {
        super(ProdutoSimilar.TABLE_NAME);
    }

    public static ProdutoSimilarDbxDao getInstance() {
        if (instance == null) {
            instance = new ProdutoSimilarDbxDao();
        }
        return instance;
    }


    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ProdutoSimilar produtoSimilar = new ProdutoSimilar();
        produtoSimilar.rowKey = rs.getString("rowkey");
        produtoSimilar.cdEmpresa = rs.getString("cdEmpresa");
        produtoSimilar.cdRepresentante = rs.getString("cdRepresentante");
        produtoSimilar.cdProduto = rs.getString("cdProduto");
        produtoSimilar.cdProdutoSimilar = rs.getString("cdProdutoSimilar");
        produtoSimilar.nuCarimbo = rs.getInt("nuCarimbo");
        produtoSimilar.flTipoAlteracao = rs.getString("flTipoAlteracao");
        produtoSimilar.cdUsuario = rs.getString("cdUsuario");
        return produtoSimilar;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDPRODUTO,");
        sql.append(" tb.CDPRODUTOSIMILAR,");
        sql.append(" tb.nuCarimbo,");
        sql.append(" tb.FLTIPOALTERACAO,");
        sql.append(" tb.cdUsuario");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDPRODUTOSIMILAR,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" cdUsuario");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoSimilar produtoSimilar = (ProdutoSimilar) domain;
        sql.append(Sql.getValue(produtoSimilar.cdEmpresa)).append(",");
        sql.append(Sql.getValue(produtoSimilar.cdRepresentante)).append(",");
        sql.append(Sql.getValue(produtoSimilar.cdProduto)).append(",");
        sql.append(Sql.getValue(produtoSimilar.cdProdutoSimilar)).append(",");
        sql.append(Sql.getValue(produtoSimilar.nuCarimbo)).append(",");
        sql.append(Sql.getValue(produtoSimilar.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(produtoSimilar.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoSimilar produtoSimilar = (ProdutoSimilar) domain;
        sql.append(" nuCarimbo = ").append(Sql.getValue(produtoSimilar.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(produtoSimilar.flTipoAlteracao)).append(",");
        sql.append(" cdUsuario = ").append(Sql.getValue(produtoSimilar.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoSimilar produtoSimilar = (ProdutoSimilar) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", produtoSimilar.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", produtoSimilar.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", produtoSimilar.cdProduto);
		sqlWhereClause.addAndCondition("tb.CDPRODUTOSIMILAR = ", produtoSimilar.cdProdutoSimilar);
		sql.append(sqlWhereClause.getSql());
    }

}