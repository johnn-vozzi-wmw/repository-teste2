package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ProdutoComplemento;
import totalcross.sql.ResultSet;

public class ProdutoComplementoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProdutoComplemento();
	}

    private static ProdutoComplementoDbxDao instance;

    public ProdutoComplementoDbxDao() {
        super(ProdutoComplemento.TABLE_NAME);
    }
    
    public static ProdutoComplementoDbxDao getInstance() {
        if (instance == null) {
            instance = new ProdutoComplementoDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ProdutoComplemento produtoComplementar = new ProdutoComplemento();
        produtoComplementar.rowKey = rs.getString("rowkey");
        produtoComplementar.cdEmpresa = rs.getString("cdEmpresa");
        produtoComplementar.cdRepresentante = rs.getString("cdRepresentante");
        produtoComplementar.cdProduto = rs.getString("cdProduto");
        produtoComplementar.cdProdutoComplemento = rs.getString("cdProdutoComplemento");
        produtoComplementar.nuCarimbo = rs.getInt("nuCarimbo");
        produtoComplementar.flTipoAlteracao = rs.getString("flTipoAlteracao");
        produtoComplementar.cdUsuario = rs.getString("cdUsuario");
        return produtoComplementar;
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
        sql.append(" CDPRODUTO,");
        sql.append(" CDPRODUTOCOMPLEMENTO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDPRODUTOCOMPLEMENTO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoComplemento produtoComplementar = (ProdutoComplemento) domain;
        sql.append(Sql.getValue(produtoComplementar.cdEmpresa)).append(",");
        sql.append(Sql.getValue(produtoComplementar.cdRepresentante)).append(",");
        sql.append(Sql.getValue(produtoComplementar.cdProduto)).append(",");
        sql.append(Sql.getValue(produtoComplementar.cdProdutoComplemento)).append(",");
        sql.append(Sql.getValue(produtoComplementar.nuCarimbo)).append(",");
        sql.append(Sql.getValue(produtoComplementar.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(produtoComplementar.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoComplemento produtoComplementar = (ProdutoComplemento) domain;
        sql.append(" NUCARIMBO = ").append(Sql.getValue(produtoComplementar.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(produtoComplementar.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(produtoComplementar.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoComplemento produtoComplementar = (ProdutoComplemento) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", produtoComplementar.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", produtoComplementar.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", produtoComplementar.cdProduto);
		sqlWhereClause.addAndCondition("CDPRODUTOCOMPLEMENTO = ", produtoComplementar.cdProdutoComplemento);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}