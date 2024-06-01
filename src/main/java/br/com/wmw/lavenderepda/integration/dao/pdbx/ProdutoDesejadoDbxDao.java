package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ProdutoDesejado;
import totalcross.sql.ResultSet;

public class ProdutoDesejadoDbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProdutoDesejado();
	}

    private static ProdutoDesejadoDbxDao instance = null;

    public ProdutoDesejadoDbxDao() {
        super(ProdutoDesejado.TABLE_NAME);
    }
    
    public static ProdutoDesejadoDbxDao getInstance() {
        if (instance == null) {
            instance = new ProdutoDesejadoDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        ProdutoDesejado produtoDesejado = new ProdutoDesejado();
        produtoDesejado.rowKey = rs.getString("rowkey");
        produtoDesejado.cdProdutoDesejado = rs.getString("cdProdutoDesejado");
        produtoDesejado.dsProdutoDesejado = rs.getString("dsProdutoDesejado");
        produtoDesejado.cdEmpresa = rs.getString("cdEmpresa");
        produtoDesejado.cdRepresentante = rs.getString("cdRepresentante");
        produtoDesejado.flOrigemPedido = rs.getString("flOrigemPedido");
        produtoDesejado.nuPedido = rs.getString("nuPedido");
        produtoDesejado.cdConcorrente = rs.getString("cdConcorrente");
        produtoDesejado.dsOutroConcorrente = rs.getString("dsOutroConcorrente");
        produtoDesejado.flOutroConcorrente = rs.getString("flOutroConcorrente");
        produtoDesejado.cdUsuarioEmissao = rs.getString("cdUsuarioEmissao");
        produtoDesejado.dtEmissaoPedido = rs.getDate("dtEmissaoPedido");
        produtoDesejado.flOrigemRegistro = rs.getString("flOrigemRegistro");
        produtoDesejado.nuCarimbo = rs.getInt("nuCarimbo");
        produtoDesejado.flTipoAlteracao = rs.getString("flTipoAlteracao");
        produtoDesejado.cdUsuario = rs.getString("cdUsuario");
        populatePerson(rs, produtoDesejado);
        return produtoDesejado;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey," +
        	" CDPRODUTODESEJADO," +
        	" DSPRODUTODESEJADO," +
        	" CDEMPRESA," +
        	" CDREPRESENTANTE," +
        	" FLORIGEMPEDIDO," +
        	" NUPEDIDO," +
        	" CDCONCORRENTE," +
        	" DSOUTROCONCORRENTE," +
        	" FLOUTROCONCORRENTE," +
        	" CDUSUARIOEMISSAO," +
        	" DTEMISSAOPEDIDO," +
        	" FLORIGEMREGISTRO," +
        	" NUCARIMBO," +
        	" FLTIPOALTERACAO," +
        	" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}
	
	@Override
	protected String getInsertValue(String columnName, int columnType, int columnSize, BaseDomain domain) {
		ProdutoDesejado produtoDesejado =  (ProdutoDesejado) domain;
		if (columnName.equalsIgnoreCase("ROWKEY")) {
			return Sql.getValue(produtoDesejado.getRowKey());
		}
		if (columnName.equalsIgnoreCase("CDPRODUTODESEJADO")) {
			return Sql.getValue(produtoDesejado.cdProdutoDesejado);
		}
		if (columnName.equalsIgnoreCase("DSPRODUTODESEJADO")) {
			return Sql.getValue(produtoDesejado.dsProdutoDesejado);
		}
		if (columnName.equalsIgnoreCase("CDEMPRESA")) {
			return Sql.getValue(produtoDesejado.cdEmpresa);
		}
		if (columnName.equalsIgnoreCase("CDREPRESENTANTE")) {
			return Sql.getValue(produtoDesejado.cdRepresentante);
		}
		if (columnName.equalsIgnoreCase("NUPEDIDO")) {
			return Sql.getValue(produtoDesejado.nuPedido);
		}
		if (columnName.equalsIgnoreCase("FLORIGEMPEDIDO")) {
			return Sql.getValue(produtoDesejado.flOrigemPedido);
		}
		if (columnName.equalsIgnoreCase("CDUSUARIOEMISSAO")) {
			return Sql.getValue(produtoDesejado.cdUsuarioEmissao);
		}
		if (columnName.equalsIgnoreCase("DTEMISSAOPEDIDO")) {
			return Sql.getValue(produtoDesejado.dtEmissaoPedido);
		}
		if (columnName.equalsIgnoreCase("FLORIGEMREGISTRO")) {
			return Sql.getValue(produtoDesejado.flOrigemRegistro);
		}
		if (columnName.equalsIgnoreCase("CDCONCORRENTE")) {
			return Sql.getValue(produtoDesejado.cdConcorrente);
		}
		if (columnName.equalsIgnoreCase("DSOUTROCONCORRENTE")) {
			return Sql.getValue(produtoDesejado.dsOutroConcorrente);
		}
		if (columnName.equalsIgnoreCase("FLOUTROCONCORRENTE")) {
			return Sql.getValue(produtoDesejado.flOutroConcorrente);
		}
		if (columnName.equalsIgnoreCase("NUCARIMBO")) {
			return Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT);
		}
		if (columnName.equalsIgnoreCase("FLTIPOALTERACAO")) {
			return Sql.getValue(produtoDesejado.flTipoAlteracao);
		}
		if (columnName.equalsIgnoreCase("CDUSUARIO")) {
			return Sql.getValue(produtoDesejado.cdUsuario);
		}
		return super.getInsertValue(columnName, columnType, columnSize, domain);
	}

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
        ProdutoDesejado produtoDesejado = (ProdutoDesejado) domain;
        addUpdateValuesPerson(produtoDesejado, sql);
        sql.append(" DSPRODUTODESEJADO = ").append(Sql.getValue(produtoDesejado.dsProdutoDesejado)).append(",")
        	.append(" CDCONCORRENTE = ").append(Sql.getValue(produtoDesejado.cdConcorrente)).append(",")
        	.append(" DSOUTROCONCORRENTE = ").append(Sql.getValue(produtoDesejado.dsOutroConcorrente)).append(",")
        	.append(" FLOUTROCONCORRENTE = ").append(Sql.getValue(produtoDesejado.flOutroConcorrente)).append(",")
        	.append(" NUCARIMBO = ").append(Sql.getValue(produtoDesejado.nuCarimbo)).append(",")
        	.append(" FLTIPOALTERACAO = ").append(Sql.getValue(produtoDesejado.flTipoAlteracao)).append(",")
        	.append(" CDUSUARIO = ").append(Sql.getValue(produtoDesejado.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        ProdutoDesejado produtoDesejado = (ProdutoDesejado) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDPRODUTODESEJADO = ", produtoDesejado.cdProdutoDesejado);
		sqlWhereClause.addAndCondition("CDEMPRESA = ", produtoDesejado.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", produtoDesejado.cdRepresentante);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", produtoDesejado.nuPedido);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", produtoDesejado.flOrigemPedido);
		sqlWhereClause.addAndCondition("CDUSUARIOEMISSAO = ", produtoDesejado.cdUsuarioEmissao);
		sqlWhereClause.addAndCondition("DTEMISSAOPEDIDO = ", produtoDesejado.dtEmissaoPedido);
		sqlWhereClause.addAndCondition("DTEMISSAOPEDIDO <= ", produtoDesejado.dtEmissaoPedidoFilter);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    public void updateFlTipoAlteracaoByExample(BaseDomain domain) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE ")
			.append(tableName)
			.append(" SET FLTIPOALTERACAO = ").append(Sql.getValue(domain.flTipoAlteracao))
			.append(" ");
		addWhereByExample(domain, sql);
		executeUpdate(sql.toString());
    }
    
}