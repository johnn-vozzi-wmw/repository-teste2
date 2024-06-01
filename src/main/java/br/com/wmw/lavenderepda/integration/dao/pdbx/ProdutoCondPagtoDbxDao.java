package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ProdutoCondPagto;
import br.com.wmw.lavenderepda.business.domain.ProdutoTipoRelacaoBase;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ProdutoCondPagtoDbxDao extends AbstractProdutoTipoRelacaoBaseDbxDao {

	private static ProdutoCondPagtoDbxDao instance;

	public static ProdutoCondPagtoDbxDao getInstance() {
		if (instance == null) {
			instance = new ProdutoCondPagtoDbxDao();
		}
		return instance;
	}
	
	public ProdutoCondPagtoDbxDao() {
		super(ProdutoCondPagto.TABLE_NAME);
	}
	
	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProdutoCondPagto();
	}

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
    	ProdutoCondPagto produtoCondPagto = new ProdutoCondPagto();
        produtoCondPagto.rowKey = rs.getString("rowkey");
        produtoCondPagto.cdEmpresa = rs.getString("cdEmpresa");
        produtoCondPagto.cdRepresentante = rs.getString("cdRepresentante");
        produtoCondPagto.cdCondicaoPagamento = rs.getString("cdCondicaoPagamento");
        produtoCondPagto.cdProduto = rs.getString("cdProduto");
        produtoCondPagto.flTipoRelacao = rs.getString("flTipoRelacao");
        produtoCondPagto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        produtoCondPagto.nuCarimbo = rs.getInt("nuCarimbo");
        produtoCondPagto.cdUsuario = rs.getString("cdUsuario");
        return produtoCondPagto;
    }

	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		return null;
	}

    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCONDICAOPAGAMENTO,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPORELACAO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }

	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { /**/ }

    @Override
    protected void addInsertColumns(StringBuffer sql) throws SQLException { /**/ }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException { /**/ }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException { /**/ }

    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
    	ProdutoCondPagto clienteProduto = (ProdutoCondPagto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", clienteProduto.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", clienteProduto.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", clienteProduto.cdProduto);
		addFlTipoRelacaoDomainCondition(clienteProduto, sqlWhereClause);
		//--
		sql.append(sqlWhereClause.getSql());
    }

	@Override
	protected String getAliasCteFlTipoRelacao() {
		return DaoUtil.ALIAS_CTEPRODCONDPAGTOTIPORELACAO;
	}
	
	@Override
	protected String getAliasCTEDomain() {
		return DaoUtil.ALIAS_CTEPRODCONDPAGTO;
	}
	
	public Vector findProdutoCondPagtoListProdutoFilter(ProdutoTipoRelacaoBase produtoCondPagtoFilter) throws SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append(" select distinct CDPRODUTO from ").append(tableName).append(" tb ");
        addWhereByExample(produtoCondPagtoFilter, sql);
        Vector result = new Vector(50);
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
        	while (rs.next()) {
        		result.addElement(rs.getString(1));
        	}
		}
        return result;
    }

}
