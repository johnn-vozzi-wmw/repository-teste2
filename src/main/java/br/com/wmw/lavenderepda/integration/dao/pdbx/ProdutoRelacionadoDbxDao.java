package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ProdutoRelacionado;
import totalcross.sql.ResultSet;

public class ProdutoRelacionadoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProdutoRelacionado();
	}

    private static ProdutoRelacionadoDbxDao instance;

    public ProdutoRelacionadoDbxDao() {
        super(ProdutoRelacionado.TABLE_NAME);
    }

    public static ProdutoRelacionadoDbxDao getInstance() {
        if (instance == null) {
            instance = new ProdutoRelacionadoDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ProdutoRelacionado produtoRelacionado = new ProdutoRelacionado();
        produtoRelacionado.rowKey = rs.getString("rowkey");
        produtoRelacionado.cdEmpresa = rs.getString("cdEmpresa");
        produtoRelacionado.cdRepresentante = rs.getString("cdRepresentante");
        produtoRelacionado.cdProduto = rs.getString("cdProduto");
        produtoRelacionado.cdProdutoRelacionado = rs.getString("cdProdutoRelacionado");
        produtoRelacionado.vlPctVendaMin = ValueUtil.round(rs.getDouble("vlPctVendaMin"));
        produtoRelacionado.nuCarimbo = rs.getInt("nuCarimbo");
        produtoRelacionado.flTipoAlteracao = rs.getString("flTipoAlteracao");
        produtoRelacionado.cdUsuario = rs.getString("cdUsuario");
        return produtoRelacionado;
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
        sql.append(" CDPRODUTORELACIONADO,");
        sql.append(" VLPCTVENDAMIN,");
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
        sql.append(" CDPRODUTORELACIONADO,");
        sql.append(" VLPCTVENDAMIN,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoRelacionado produtoRelacionado = (ProdutoRelacionado) domain;
        sql.append(Sql.getValue(produtoRelacionado.cdEmpresa)).append(",");
        sql.append(Sql.getValue(produtoRelacionado.cdRepresentante)).append(",");
        sql.append(Sql.getValue(produtoRelacionado.cdProduto)).append(",");
        sql.append(Sql.getValue(produtoRelacionado.cdProdutoRelacionado)).append(",");
        sql.append(Sql.getValue(produtoRelacionado.vlPctVendaMin)).append(",");
        sql.append(Sql.getValue(produtoRelacionado.nuCarimbo)).append(",");
        sql.append(Sql.getValue(produtoRelacionado.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(produtoRelacionado.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoRelacionado produtoRelacionado = (ProdutoRelacionado) domain;
        sql.append(" VLPCTVENDAMIN = ").append(Sql.getValue(produtoRelacionado.vlPctVendaMin)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(produtoRelacionado.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(produtoRelacionado.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(produtoRelacionado.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoRelacionado produtoRelacionado = (ProdutoRelacionado) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", produtoRelacionado.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", produtoRelacionado.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", produtoRelacionado.cdProduto);
		sqlWhereClause.addAndCondition("CDPRODUTORELACIONADO = ", produtoRelacionado.cdProdutoRelacionado);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}