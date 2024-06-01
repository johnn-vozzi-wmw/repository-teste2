package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ProdutoTipoPed;
import totalcross.sql.ResultSet;

public class ProdutoTipoPedDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProdutoTipoPed();
	}

    private static ProdutoTipoPedDbxDao instance;

    public ProdutoTipoPedDbxDao() {
        super(ProdutoTipoPed.TABLE_NAME);
    }

    public static ProdutoTipoPedDbxDao getInstance() {
        if (instance == null) {
            instance = new ProdutoTipoPedDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ProdutoTipoPed produtoTipoPed = new ProdutoTipoPed();
        produtoTipoPed.rowKey = rs.getString("rowkey");
        produtoTipoPed.cdEmpresa = rs.getString("cdEmpresa");
        produtoTipoPed.cdRepresentante = rs.getString("cdRepresentante");
        produtoTipoPed.cdTipoPedido = rs.getString("cdTipoPedido");
        produtoTipoPed.cdProduto = rs.getString("cdProduto");
        produtoTipoPed.nuCfopProduto = rs.getString("nuCfopProduto");
        produtoTipoPed.nuCarimbo = rs.getInt("nuCarimbo");
        produtoTipoPed.flTipoAlteracao = rs.getString("flTipoAlteracao");
        produtoTipoPed.cdUsuario = rs.getString("cdUsuario");
        return produtoTipoPed;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDTIPOPEDIDO,");
        sql.append(" CDPRODUTO,");
        sql.append(" NUCFOPPRODUTO,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDTIPOPEDIDO,");
        sql.append(" CDPRODUTO,");
        sql.append(" NUCFOPPRODUTO,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoTipoPed produtoTipoPed = (ProdutoTipoPed) domain;
        sql.append(Sql.getValue(produtoTipoPed.cdEmpresa)).append(",");
        sql.append(Sql.getValue(produtoTipoPed.cdRepresentante)).append(",");
        sql.append(Sql.getValue(produtoTipoPed.cdTipoPedido)).append(",");
        sql.append(Sql.getValue(produtoTipoPed.cdProduto)).append(",");
        sql.append(Sql.getValue(produtoTipoPed.nuCfopProduto)).append(",");
        sql.append(Sql.getValue(produtoTipoPed.nuCarimbo)).append(",");
        sql.append(Sql.getValue(produtoTipoPed.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(produtoTipoPed.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoTipoPed produtoTipoPed = (ProdutoTipoPed) domain;
        sql.append(" nuCarimbo = ").append(Sql.getValue(produtoTipoPed.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(produtoTipoPed.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(produtoTipoPed.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoTipoPed produtoTipoPed = (ProdutoTipoPed) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", produtoTipoPed.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", produtoTipoPed.cdRepresentante);
		sqlWhereClause.addAndCondition("CDTIPOPEDIDO = ", produtoTipoPed.cdTipoPedido);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", produtoTipoPed.cdProduto);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}