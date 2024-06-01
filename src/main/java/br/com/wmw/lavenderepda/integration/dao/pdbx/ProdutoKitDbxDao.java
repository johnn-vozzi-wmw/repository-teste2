package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ProdutoKit;
import totalcross.sql.ResultSet;

public class ProdutoKitDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProdutoKit();
	}

    private static ProdutoKitDbxDao instance;

    public ProdutoKitDbxDao() {
        super(ProdutoKit.TABLE_NAME);
    }

    public static ProdutoKitDbxDao getInstance() {
        if (instance == null) {
            instance = new ProdutoKitDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ProdutoKit produtoKit = new ProdutoKit();
        produtoKit.rowKey = rs.getString("rowkey");
        produtoKit.cdEmpresa = rs.getString("cdEmpresa");
        produtoKit.cdRepresentante = rs.getString("cdRepresentante");
        produtoKit.cdKit = rs.getString("cdKit");
        produtoKit.cdProduto = rs.getString("cdProduto");
        produtoKit.qtItemFisico = ValueUtil.round(rs.getDouble("qtItemFisico"));
        produtoKit.nuCarimbo = rs.getInt("nuCarimbo");
        produtoKit.flTipoAlteracao = rs.getString("flTipoAlteracao");
        produtoKit.cdUsuario = rs.getString("cdUsuario");
        return produtoKit;
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
        sql.append(" CDKIT,");
        sql.append(" CDPRODUTO,");
        sql.append(" QTITEMFISICO,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" cdUsuario");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDKIT,");
        sql.append(" CDPRODUTO,");
        sql.append(" QTITEMFISICO,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" cdUsuario");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoKit produtoKit = (ProdutoKit) domain;
        sql.append(Sql.getValue(produtoKit.cdEmpresa)).append(",");
        sql.append(Sql.getValue(produtoKit.cdRepresentante)).append(",");
        sql.append(Sql.getValue(produtoKit.cdKit)).append(",");
        sql.append(Sql.getValue(produtoKit.cdProduto)).append(",");
        sql.append(Sql.getValue(produtoKit.qtItemFisico)).append(",");
        sql.append(Sql.getValue(produtoKit.nuCarimbo)).append(",");
        sql.append(Sql.getValue(produtoKit.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(produtoKit.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoKit produtoKit = (ProdutoKit) domain;
        sql.append(" QTITEMFISICO = ").append(Sql.getValue(produtoKit.qtItemFisico)).append(",");
        sql.append(" nuCarimbo = ").append(Sql.getValue(produtoKit.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(produtoKit.flTipoAlteracao)).append(",");
        sql.append(" cdUsuario = ").append(Sql.getValue(produtoKit.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoKit produtoKit = (ProdutoKit) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", produtoKit.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", produtoKit.cdRepresentante);
		sqlWhereClause.addAndCondition("CDKIT = ", produtoKit.cdKit);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", produtoKit.cdProduto);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}