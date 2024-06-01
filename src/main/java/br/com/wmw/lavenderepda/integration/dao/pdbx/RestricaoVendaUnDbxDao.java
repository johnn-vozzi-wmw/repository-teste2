package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.RestricaoVendaUn;
import totalcross.sql.ResultSet;

public class RestricaoVendaUnDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RestricaoVendaUn();
	}

    private static RestricaoVendaUnDbxDao instance;

    public RestricaoVendaUnDbxDao() {
        super(RestricaoVendaUn.TABLE_NAME);
    }

    public static RestricaoVendaUnDbxDao getInstance() {
        if (instance == null) {
            instance = new RestricaoVendaUnDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        RestricaoVendaUn restricaoVendaUn = new RestricaoVendaUn();
        restricaoVendaUn.rowKey = rs.getString("rowkey");
        restricaoVendaUn.cdEmpresa = rs.getString("cdEmpresa");
        restricaoVendaUn.cdRepresentante = rs.getString("cdRepresentante");
        restricaoVendaUn.cdUnidade = rs.getString("cdUnidade");
        restricaoVendaUn.cdProduto = rs.getString("cdProduto");
        restricaoVendaUn.nuCarimbo = rs.getInt("nuCarimbo");
        restricaoVendaUn.flTipoAlteracao = rs.getString("flTipoAlteracao");
        restricaoVendaUn.cdUsuario = rs.getString("cdUsuario");
        restricaoVendaUn.flBloqueiaVenda = rs.getString("flBloqueiaVenda");
        restricaoVendaUn.cdRestricaoVendaUn = rs.getString("cdRestricaoVendaUn");
        restricaoVendaUn.cdCliente = rs.getString("cdCliente");
        restricaoVendaUn.cdTabelaPreco = rs.getString("cdTabelaPreco");
        restricaoVendaUn.cdTipoPedido = rs.getString("cdTipoPedido");
        return restricaoVendaUn;
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
        sql.append(" CDUNIDADE,");
        sql.append(" CDPRODUTO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLBLOQUEIAVENDA,");
        sql.append(" CDRESTRICAOVENDAUN,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" CDTIPOPEDIDO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDUNIDADE,");
        sql.append(" CDPRODUTO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLBLOQUEIAVENDA,");
        sql.append(" CDRESTRICAOVENDAUN,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" CDTIPOPEDIDO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        RestricaoVendaUn restricaoVendaUn = (RestricaoVendaUn) domain;
        sql.append(Sql.getValue(restricaoVendaUn.cdEmpresa)).append(",");
        sql.append(Sql.getValue(restricaoVendaUn.cdRepresentante)).append(",");
        sql.append(Sql.getValue(restricaoVendaUn.cdUnidade)).append(",");
        sql.append(Sql.getValue(restricaoVendaUn.cdProduto)).append(",");
        sql.append(Sql.getValue(restricaoVendaUn.nuCarimbo)).append(",");
        sql.append(Sql.getValue(restricaoVendaUn.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(restricaoVendaUn.cdUsuario)).append(",");
        sql.append(Sql.getValue(restricaoVendaUn.flBloqueiaVenda)).append(",");
        sql.append(Sql.getValue(restricaoVendaUn.cdRestricaoVendaUn)).append(",");
        sql.append(Sql.getValue(restricaoVendaUn.cdCliente)).append(",");
        sql.append(Sql.getValue(restricaoVendaUn.cdTabelaPreco)).append(",");
        sql.append(Sql.getValue(restricaoVendaUn.cdTipoPedido));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        RestricaoVendaUn restricaoVendaUn = (RestricaoVendaUn) domain;
        sql.append(" CDTIPOPEDIDO = ").append(Sql.getValue(restricaoVendaUn.cdTipoPedido)).append(",");
        sql.append(" CDTABELAPRECO = ").append(Sql.getValue(restricaoVendaUn.cdTabelaPreco)).append(",");
        sql.append(" CDPRODUTO = ").append(Sql.getValue(restricaoVendaUn.cdProduto)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(restricaoVendaUn.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(restricaoVendaUn.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(restricaoVendaUn.cdUsuario)).append(",");
        sql.append(" FLBLOQUEIAVENDA = ").append(Sql.getValue(restricaoVendaUn.flBloqueiaVenda));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        RestricaoVendaUn restricaoVendaUn = (RestricaoVendaUn) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", restricaoVendaUn.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", restricaoVendaUn.cdRepresentante);
		sqlWhereClause.addAndCondition("CDUNIDADE = ", restricaoVendaUn.cdUnidade);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", restricaoVendaUn.cdProduto);
		sqlWhereClause.addAndCondition("CDRESTRICAOVENDAUN = ", restricaoVendaUn.cdRestricaoVendaUn);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", restricaoVendaUn.cdCliente);
		sqlWhereClause.addAndCondition("CDTIPOPEDIDO = ", restricaoVendaUn.cdTipoPedido);
		sqlWhereClause.addAndCondition("CDTABELAPRECO = ", restricaoVendaUn.cdTabelaPreco);
		sqlWhereClause.addAndCondition("FLBLOQUEIAVENDA = ", restricaoVendaUn.flBloqueiaVenda);
		if (ValueUtil.isNotEmpty(restricaoVendaUn.cdTipoPedidoOrFilter)) {
			sqlWhereClause.addAndConditionOr("CDTIPOPEDIDO = ", new String[]{restricaoVendaUn.cdTipoPedidoOrFilter, RestricaoVendaUn.CDTIPOPEDIDO_VALOR_PADRAO, null});
		}
		if (ValueUtil.isNotEmpty(restricaoVendaUn.cdTabelaPrecoOrFilter)) {
			sqlWhereClause.addAndConditionOr("CDTABELAPRECO = ", new String[]{restricaoVendaUn.cdTabelaPrecoOrFilter, RestricaoVendaUn.CDTABELAPRECO_VALOR_PADRAO, null});
		}
		if (ValueUtil.isNotEmpty(restricaoVendaUn.cdClienteOrFilter)) {
			sqlWhereClause.addAndConditionOr("CDCLIENTE = ", new String[]{restricaoVendaUn.cdClienteOrFilter, RestricaoVendaUn.CDCLIENTE_VALOR_PADRAO});
		}
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addCacheColumns(StringBuffer sql) {
    	sql.append("CDEMPRESA,")
    	.append("CDREPRESENTANTE,")
    	.append("CDPRODUTO,")
    	.append("CDUNIDADE,")
    	.append("CDRESTRICAOVENDAUN,")
    	.append("CDCLIENTE,")
    	.append("CDTIPOPEDIDO,")
    	.append("CDTABELAPRECO,")
    	.append("FLBLOQUEIAVENDA");
    }
    
    @Override
    protected BaseDomain populateCache(ResultSet rs) throws SQLException {
    	RestricaoVendaUn restricaoVendaUn = new RestricaoVendaUn();
    	restricaoVendaUn.cdEmpresa = rs.getString("cdEmpresa");
    	restricaoVendaUn.cdRepresentante = rs.getString("cdRepresentante");
    	restricaoVendaUn.cdProduto = rs.getString("cdProduto");
    	restricaoVendaUn.cdRestricaoVendaUn = rs.getString("cdRestricaoVendaUn");
    	restricaoVendaUn.cdCliente = rs.getString("cdCliente");
    	restricaoVendaUn.cdTipoPedido = rs.getString("cdTipoPedido");
    	restricaoVendaUn.cdTabelaPreco = rs.getString("cdTabelaPreco");
    	restricaoVendaUn.flBloqueiaVenda = rs.getString("flBloqueiaVenda");
    	return restricaoVendaUn;
    }

}