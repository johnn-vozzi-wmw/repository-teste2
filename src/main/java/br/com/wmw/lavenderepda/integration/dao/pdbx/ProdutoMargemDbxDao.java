package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ProdutoMargem;
import totalcross.sql.ResultSet;

public class ProdutoMargemDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProdutoMargem();
	}

    private static ProdutoMargemDbxDao instance;
	
    public ProdutoMargemDbxDao() {
        super(ProdutoMargem.TABLE_NAME);
    }
    
    public static ProdutoMargemDbxDao getInstance() {
        if (instance == null) {
            instance = new ProdutoMargemDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ProdutoMargem produtoMargem = new ProdutoMargem();
        produtoMargem.rowKey = rs.getString("rowkey");
        produtoMargem.cdEmpresa = rs.getString("cdEmpresa");
        produtoMargem.cdRepresentante = rs.getString("cdRepresentante");
        produtoMargem.cdProduto = rs.getString("cdProduto");
        produtoMargem.cdRamoAtividade = rs.getString("cdRamoAtividade");
        produtoMargem.vlPctMargemDesconto = ValueUtil.round(rs.getDouble("vlPctMargemDesconto"));
        produtoMargem.nuCarimbo = rs.getInt("nuCarimbo");
        produtoMargem.flTipoAlteracao = rs.getString("flTipoAlteracao");
        produtoMargem.cdUsuario = rs.getString("cdUsuario");
        return produtoMargem;
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
        sql.append(" CDRAMOATIVIDADE,");
        sql.append(" VLPCTMARGEMDESCONTO,");
        sql.append(" NUCARIMBO,");
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
        sql.append(" CDPRODUTO,");
        sql.append(" CDRAMOATIVIDADE,");
        sql.append(" VLPCTMARGEMDESCONTO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoMargem produtoMargem = (ProdutoMargem) domain;
        sql.append(Sql.getValue(produtoMargem.cdEmpresa)).append(",");
        sql.append(Sql.getValue(produtoMargem.cdRepresentante)).append(",");
        sql.append(Sql.getValue(produtoMargem.cdProduto)).append(",");
        sql.append(Sql.getValue(produtoMargem.cdRamoAtividade)).append(",");
        sql.append(Sql.getValue(produtoMargem.vlPctMargemDesconto)).append(",");
        sql.append(Sql.getValue(produtoMargem.nuCarimbo)).append(",");
        sql.append(Sql.getValue(produtoMargem.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(produtoMargem.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoMargem produtoMargem = (ProdutoMargem) domain;
        sql.append(" VLPCTMARGEMDESCONTO = ").append(Sql.getValue(produtoMargem.vlPctMargemDesconto)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(produtoMargem.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(produtoMargem.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(produtoMargem.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoMargem produtoMargem = (ProdutoMargem) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", produtoMargem.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", produtoMargem.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", produtoMargem.cdProduto);
		sqlWhereClause.addAndCondition("CDRAMOATIVIDADE = ", produtoMargem.cdRamoAtividade);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}