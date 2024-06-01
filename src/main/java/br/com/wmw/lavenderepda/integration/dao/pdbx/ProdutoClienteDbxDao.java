package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ProdutoCliente;
import br.com.wmw.lavenderepda.business.domain.ProdutoTipoRelacaoBase;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ProdutoClienteDbxDao extends AbstractProdutoTipoRelacaoBaseDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProdutoCliente();
	}

    private static ProdutoClienteDbxDao instance;

    public ProdutoClienteDbxDao() {
        super(ProdutoCliente.TABLE_NAME);
    }

    public static ProdutoClienteDbxDao getInstance() {
        if (instance == null) {
            instance = new ProdutoClienteDbxDao();
        }
        return instance;
    }

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ProdutoCliente produtoCliente = new ProdutoCliente();
        produtoCliente.rowKey = rs.getString("rowkey");
        produtoCliente.cdEmpresa = rs.getString("cdEmpresa");
        produtoCliente.cdRepresentante = rs.getString("cdRepresentante");
        produtoCliente.cdProduto = rs.getString("cdProduto");
        produtoCliente.cdCliente = rs.getString("cdCliente");
        produtoCliente.flTipoRelacao = rs.getString("flTipoRelacao");
        produtoCliente.vlRetornoProduto = rs.getDouble("vlRetornoProduto");
        produtoCliente.vlPctFidelidade = rs.getDouble("vlPctFidelidade");
        produtoCliente.flTipoAlteracao = rs.getString("flTipoAlteracao");
        produtoCliente.nuCarimbo = rs.getInt("nuCarimbo");
        produtoCliente.cdUsuario = rs.getString("cdUsuario");
        return produtoCliente;
    }

	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDCLIENTE,");
        sql.append(" FLTIPORELACAO,");
        sql.append(" VLRETORNOPRODUTO,");
        sql.append(" VLPCTFIDELIDADE,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }

	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) throws SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDCLIENTE,");
        sql.append(" FLTIPORELACAO,");
        sql.append(" VLRETORNOPRODUTO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoCliente produtoCliente = (ProdutoCliente) domain;
        sql.append(Sql.getValue(produtoCliente.cdEmpresa)).append(",");
        sql.append(Sql.getValue(produtoCliente.cdRepresentante)).append(",");
        sql.append(Sql.getValue(produtoCliente.cdProduto)).append(",");
        sql.append(Sql.getValue(produtoCliente.cdCliente)).append(",");
        sql.append(Sql.getValue(produtoCliente.flTipoRelacao)).append(",");
        sql.append(Sql.getValue(produtoCliente.vlRetornoProduto)).append(",");
        sql.append(Sql.getValue(produtoCliente.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(produtoCliente.nuCarimbo)).append(",");
        sql.append(Sql.getValue(produtoCliente.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoCliente produtoCliente = (ProdutoCliente) domain;
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(produtoCliente.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(produtoCliente.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(produtoCliente.cdUsuario));
    }

    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoCliente produtoCliente = (ProdutoCliente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", produtoCliente.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", produtoCliente.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", produtoCliente.cdProduto);
		sqlWhereClause.addAndCondition("VLRETORNOPRODUTO = ", produtoCliente.vlRetornoProduto);
		addFlTipoRelacaoDomainCondition(produtoCliente, sqlWhereClause);
		//--
		sql.append(sqlWhereClause.getSql());
		addLoopInSql(sql, produtoCliente.cdProdutoFilterList, "CDPRODUTO", 400);
    }
    
    public Vector findProdutoClienteListProdutoFilter(ProdutoTipoRelacaoBase produtoClienteFilter) throws SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append(" select distinct CDPRODUTO from ").append(tableName).append(" tb ");
        addWhereByExample(produtoClienteFilter, sql);
        Vector result = new Vector(50);
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
        	while (rs.next()) {
        		result.addElement(rs.getString(1));
        	}
		}
        return result;
    }

	@Override
	protected String getAliasCteFlTipoRelacao() {
		return DaoUtil.ALIAS_CTEPROCLIFLTIPORELACAO;
	}

	@Override
	protected String getAliasCTEDomain() {
		return DaoUtil.ALIAS_CTEPRODUTOCLIENTE;
	}

}