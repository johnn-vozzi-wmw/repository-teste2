package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ProdutoFalta;
import totalcross.sql.ResultSet;

public class ProdutoFaltaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProdutoFalta();
	}

    private static ProdutoFaltaDbxDao instance;
	

    public ProdutoFaltaDbxDao() {
        super(ProdutoFalta.TABLE_NAME);
    }
    
    public static ProdutoFaltaDbxDao getInstance() {
        if (instance == null) {
            instance = new ProdutoFaltaDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ProdutoFalta produtoFalta = new ProdutoFalta();
        produtoFalta.rowKey = rs.getString("rowkey");
        produtoFalta.cdEmpresa = rs.getString("cdEmpresa");
        produtoFalta.cdRepresentante = rs.getString("cdRepresentante");
        produtoFalta.cdCliente = rs.getString("cdCliente");
        produtoFalta.cdProdutoFalta = rs.getString("cdProdutoFalta");
        produtoFalta.cdProduto = rs.getString("cdProduto");
        produtoFalta.cdTabelaPreco = rs.getString("cdTabelaPreco");
        produtoFalta.cdCampanha = rs.getString("cdCampanha");
        produtoFalta.dtFaltante = rs.getDate("dtSolicitada");
        produtoFalta.qtFaltante = rs.getInt("qtSolicitada");
        produtoFalta.qtAtendida = rs.getInt("qtAtendida");
        produtoFalta.vlPctDesconto = ValueUtil.round(rs.getDouble("vlPctDesconto"));
        produtoFalta.dtRegistro = rs.getDate("dtRegistro");
        produtoFalta.nuCarimbo = rs.getInt("nuCarimbo");
        produtoFalta.flTipoAlteracao = rs.getString("flTipoAlteracao");
        produtoFalta.nuPedido = rs.getString("nuPedido");
        produtoFalta.dtEntregaPedido = rs.getDate("dtEntregaPedido");
        produtoFalta.cdUsuario = rs.getString("cdUsuario");
        produtoFalta.cdUnidade = rs.getString("cdUnidade");
        return produtoFalta;
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
        sql.append(" CDCLIENTE,");
        sql.append(" CDPRODUTOFALTA,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" CDCAMPANHA,");
        sql.append(" DTFALTANTE,");
        sql.append(" QTFALTANTE,");
        sql.append(" QTATENDIDA,");
        sql.append(" VLPCTDESCONTO,");
        sql.append(" DTREGISTRO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUPEDIDO,");
        sql.append(" DTENTREGAPEDIDO,");
        sql.append(" CDUSUARIO,");
        sql.append(" CDUNIDADE");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDPRODUTOFALTA,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" CDCAMPANHA,");
        sql.append(" DTFALTANTE,");
        sql.append(" QTFALTANTE,");
        sql.append(" QTATENDIDA,");
        sql.append(" VLPCTDESCONTO,");
        sql.append(" DTREGISTRO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUPEDIDO,");
        sql.append(" DTENTREGAPEDIDO,");
        sql.append(" CDUSUARIO,");
        sql.append(" CDUNIDADE");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoFalta produtoFalta = (ProdutoFalta) domain;
        sql.append(Sql.getValue(produtoFalta.cdEmpresa)).append(",");
        sql.append(Sql.getValue(produtoFalta.cdRepresentante)).append(",");
        sql.append(Sql.getValue(produtoFalta.cdCliente)).append(",");
        sql.append(Sql.getValue(produtoFalta.cdProdutoFalta)).append(",");
        sql.append(Sql.getValue(produtoFalta.cdProduto)).append(",");
        sql.append(Sql.getValue(produtoFalta.cdTabelaPreco)).append(",");
        sql.append(Sql.getValue(produtoFalta.cdCampanha)).append(",");
        sql.append(Sql.getValue(produtoFalta.dtFaltante)).append(",");
        sql.append(Sql.getValue(produtoFalta.qtFaltante)).append(",");
        sql.append(Sql.getValue(produtoFalta.qtAtendida)).append(",");
        sql.append(Sql.getValue(produtoFalta.vlPctDesconto)).append(",");
        sql.append(Sql.getValue(produtoFalta.dtRegistro)).append(",");
        sql.append(Sql.getValue(produtoFalta.nuCarimbo)).append(",");
        sql.append(Sql.getValue(produtoFalta.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(produtoFalta.nuPedido)).append(",");
        sql.append(Sql.getValue(produtoFalta.dtEntregaPedido)).append(",");
        sql.append(Sql.getValue(produtoFalta.cdUsuario)).append(",");
        sql.append(Sql.getValue(produtoFalta.cdUnidade));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoFalta produtoFalta = (ProdutoFalta) domain;
        sql.append(" CDTABELAPRECO = ").append(Sql.getValue(produtoFalta.cdTabelaPreco)).append(",");
        sql.append(" CDCAMPANHA = ").append(Sql.getValue(produtoFalta.cdCampanha)).append(",");
        sql.append(" DTFALTANTE = ").append(Sql.getValue(produtoFalta.dtFaltante)).append(",");
        sql.append(" QTFALTANTE = ").append(Sql.getValue(produtoFalta.qtFaltante)).append(",");
        sql.append(" QTATENDIDA = ").append(Sql.getValue(produtoFalta.qtAtendida)).append(",");
        sql.append(" VLPCTDESCONTO = ").append(Sql.getValue(produtoFalta.vlPctDesconto)).append(",");
        sql.append(" DTREGISTRO = ").append(Sql.getValue(produtoFalta.dtRegistro)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(produtoFalta.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(produtoFalta.flTipoAlteracao)).append(",");
        sql.append(" NUPEDIDO = ").append(Sql.getValue(produtoFalta.nuPedido)).append(",");
        sql.append(" DTENTREGAPEDIDO = ").append(Sql.getValue(produtoFalta.dtEntregaPedido)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(produtoFalta.cdUsuario)).append(",");
        sql.append(" CDUNIDADE = ").append(Sql.getValue(produtoFalta.cdUnidade));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoFalta produtoFalta = (ProdutoFalta) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", produtoFalta.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", produtoFalta.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", produtoFalta.cdCliente);
		sqlWhereClause.addAndCondition("CDPRODUTOFALTA = ", produtoFalta.cdProdutoFalta);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", produtoFalta.cdProduto);
		sqlWhereClause.addAndCondition("DTFALTANTE = ", produtoFalta.dtFaltante);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", produtoFalta.nuPedido);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    public int countProdutoFaltaEstoquePrevisto(BaseDomain domain) throws SQLException {
    	StringBuffer sql = new StringBuffer();
    	sql.append(" SELECT COUNT(1) AS QTDEXISTENTE FROM ").append(tableName).append(" ");
    	addWhereByEstoquePrevisto(domain, sql);
    	return getInt(sql.toString());
    }
    
    private void addWhereByEstoquePrevisto(BaseDomain domain, StringBuffer sql) {
    	ProdutoFalta produtoFalta = (ProdutoFalta) domain;
    	SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", produtoFalta.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", produtoFalta.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", produtoFalta.cdCliente);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", produtoFalta.cdProduto);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", produtoFalta.nuPedido);
		sqlWhereClause.addAndCondition("DTENTREGAPEDIDO = ", produtoFalta.dtEntregaPedido);
		//--
		sql.append(sqlWhereClause.getSql());
    }
 
}