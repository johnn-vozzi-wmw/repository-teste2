package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ProdutoRetirada;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;

public class ProdutoRetiradaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProdutoRetirada();
	}

    private static ProdutoRetiradaDbxDao instance = null;

    public ProdutoRetiradaDbxDao() {
        super(ProdutoRetirada.TABLE_NAME);
    }
    
    public static ProdutoRetiradaDbxDao getInstance() {
        if (instance == null) {
            instance = new ProdutoRetiradaDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        ProdutoRetirada produtoRetirada = new ProdutoRetirada();
        produtoRetirada.rowKey = rs.getString("rowkey");
        produtoRetirada.cdEmpresa = rs.getString("cdEmpresa");
        produtoRetirada.cdRepresentante = rs.getString("cdRepresentante");
        produtoRetirada.cdCliente = rs.getString("cdCliente");
        produtoRetirada.cdProduto = rs.getString("cdProduto");
        produtoRetirada.dsProduto = rs.getString("dsProduto");
        produtoRetirada.dtCompra = rs.getDate("dtCompra");
        produtoRetirada.nuNota = rs.getInt("nuNota");
        produtoRetirada.qtCompra = rs.getInt("qtCompra");
        produtoRetirada.qtRestante = rs.getInt("qtRestante");
        produtoRetirada.nuContrato = rs.getInt("nuContrato");
        produtoRetirada.dtMaxRetirada = rs.getDate("dtMaxRetirada");
        produtoRetirada.cdUsuario = rs.getString("cdUsuario");
        produtoRetirada.flTipoAlteracao = rs.getString("flTipoAlteracao");
        produtoRetirada.nuCarimbo = rs.getInt("nuCarimbo");
        return produtoRetirada;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDCLIENTE,");
        sql.append(" tb.CDPRODUTO,");
        sql.append(" "+DaoUtil.ALIAS_PRODUTO+".DSPRODUTO,");
        sql.append(" tb.DTCOMPRA,");
        sql.append(" tb.NUNOTA,");
        sql.append(" tb.QTCOMPRA,");
        sql.append(" tb.QTRESTANTE,");
        sql.append(" tb.NUCONTRATO,");
        sql.append(" tb.DTMAXRETIRADA,");
        sql.append(" tb.CDUSUARIO,");
        sql.append(" tb.FLTIPOALTERACAO,");
        sql.append(" tb.NUCARIMBO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDCLIENTE,");
        sql.append(" tb.CDPRODUTO,");
        sql.append(" tb.DTCOMPRA,");
        sql.append(" tb.NUNOTA,");
        sql.append(" tb.QTCOMPRA,");
        sql.append(" tb.DTMAXRETIRADA,");
        sql.append(" tb.CDUSUARIO,");
        sql.append(" tb.FLTIPOALTERACAO,");
        sql.append(" tb.NUCARIMBO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        ProdutoRetirada produtoRetirada = (ProdutoRetirada) domain;
        sql.append(Sql.getValue(produtoRetirada.cdEmpresa)).append(",");
        sql.append(Sql.getValue(produtoRetirada.cdRepresentante)).append(",");
        sql.append(Sql.getValue(produtoRetirada.cdCliente)).append(",");
        sql.append(Sql.getValue(produtoRetirada.cdProduto)).append(",");
        sql.append(Sql.getValue(produtoRetirada.dtCompra)).append(",");
        sql.append(Sql.getValue(produtoRetirada.nuNota)).append(",");
        sql.append(Sql.getValue(produtoRetirada.qtCompra)).append(",");
        sql.append(Sql.getValue(produtoRetirada.dtMaxRetirada)).append(",");
        sql.append(Sql.getValue(produtoRetirada.cdUsuario)).append(",");
        sql.append(Sql.getValue(produtoRetirada.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(produtoRetirada.nuCarimbo));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        ProdutoRetirada produtoRetirada = (ProdutoRetirada) domain;
        sql.append(" tb.NUNOTA = ").append(Sql.getValue(produtoRetirada.nuNota)).append(",");
        sql.append(" tb.QTCOMPRA = ").append(Sql.getValue(produtoRetirada.qtCompra)).append(",");
        sql.append(" tb.DTMAXRETIRADA = ").append(Sql.getValue(produtoRetirada.dtMaxRetirada)).append(",");
        sql.append(" tb.CDUSUARIO = ").append(Sql.getValue(produtoRetirada.cdUsuario)).append(",");
        sql.append(" tb.FLTIPOALTERACAO = ").append(Sql.getValue(produtoRetirada.flTipoAlteracao)).append(",");
        sql.append(" tb.NUCARIMBO = ").append(Sql.getValue(produtoRetirada.nuCarimbo));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        ProdutoRetirada produtoRetirada = (ProdutoRetirada) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", produtoRetirada.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", produtoRetirada.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDCLIENTE = ", produtoRetirada.cdCliente);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", produtoRetirada.cdProduto);
		sqlWhereClause.addAndCondition("tb.DTCOMPRA = ", produtoRetirada.dtCompra);
		sqlWhereClause.addAndCondition("tb.DTMAXRETIRADA >= ", produtoRetirada.dtMaxRetiradaStartFilter);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
    	super.addJoin(domainFilter, sql);
    	DaoUtil.addJoinProduto(sql, DaoUtil.ALIAS_PRODUTO, true);
    }
    
}