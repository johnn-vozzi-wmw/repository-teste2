
package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ConfigValorMinimo;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ConfigValorMinimoDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ConfigValorMinimo();
	}

    private static ConfigValorMinimoDao instance;
    
    private String column;

    public ConfigValorMinimoDao() {
        super(ConfigValorMinimo.TABLE_NAME);
    }
    
    public static ConfigValorMinimoDao getInstance() {
        if (instance == null) {
            instance = new ConfigValorMinimoDao();
        }
        return instance;
    }
    
    //@Override
  	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
  	//@Override
  	protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
  	//@Override
  	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
  	//@Override
  	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
  	
  	
  	@Override
  	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { 
  		ConfigValorMinimo configValorMinimo = (ConfigValorMinimo) domain;
  		SqlWhereClause sqlWhereClause = new SqlWhereClause();
  		sqlWhereClause.addAndCondition("CDEMPRESA = ", configValorMinimo.cdEmpresa);
  		sqlWhereClause.addAndCondition("CDREPRESENTANTE = " , configValorMinimo.cdRepresentante);
  		sqlWhereClause.addAndCondition("CDCONFIGVALORMINIMO = ", configValorMinimo.cdConfigValorMinimo);
  		sqlWhereClause.addAndConditionOr("CDTABELAPRECO = ", new String[] {configValorMinimo.cdTabelaPreco, ValueUtil.VALOR_ZERO});
  		sqlWhereClause.addAndConditionOr("CDGRUPOVALORMINPEDIDO = ", new String[] {configValorMinimo.cdGrupoValorMinPedido, ValueUtil.VALOR_ZERO});
  		sqlWhereClause.addAndConditionOr("CDCENTROCUSTO = ", new String[] {configValorMinimo.cdCentroCusto, ValueUtil.VALOR_ZERO});
  		sqlWhereClause.addAndConditionOr("CDPLATAFORMAVENDA = ", new String[] {configValorMinimo.cdPlataformaVenda, ValueUtil.VALOR_ZERO});
  		sqlWhereClause.addAndConditionOr("CDLINHA = ", new String[]{configValorMinimo.cdLinha, ValueUtil.VALOR_ZERO});
  		sql.append(sqlWhereClause.getSql());
  	}

    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	ConfigValorMinimo configValorMinimo = new ConfigValorMinimo();
        configValorMinimo.rowKey = rs.getString("rowkey");
        configValorMinimo.cdEmpresa = rs.getString("cdEmpresa");
        configValorMinimo.cdRepresentante = rs.getString("cdRepresentante");
        configValorMinimo.cdConfigValorMinimo = rs.getString("cdConfigValorMinimo");
        configValorMinimo.cdTabelaPreco = rs.getString("cdTabelaPreco");
        configValorMinimo.cdGrupoValorMinPedido = rs.getString("cdGrupoValorMinPedido");
        configValorMinimo.cdCentroCusto = rs.getString("cdCentroCusto");
        configValorMinimo.cdPlataformaVenda = rs.getString("cdPlataformaVenda");
        configValorMinimo.cdLinha = rs.getString("cdLinha");
        configValorMinimo.vlMinPedido = rs.getDouble("vlMinPedido");
        configValorMinimo.vlMinParcela = rs.getDouble("vlMinParcela");
        configValorMinimo.nuCarimbo = rs.getInt("nuCarimbo");
        configValorMinimo.flTipoAlteracao = rs.getString("flTipoAlteracao");
        configValorMinimo.cdUsuario = rs.getString("cdUsuario");
        return configValorMinimo;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCONFIGVALORMINIMO,");
        sql.append(" CDTABELAPRECO, ");
        sql.append(" CDGRUPOVALORMINPEDIDO,");
        sql.append(" CDCENTROCUSTO,");
        sql.append(" CDPLATAFORMAVENDA,");
        sql.append(" CDLINHA,");
        sql.append(" VLMINPEDIDO,");
        sql.append(" VLMINPARCELA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
	}
	
	@Override
	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		List<String> listaOrdenacaoTabelaConfigMin = LavenderePdaConfig.listaOrdenacaoTabelaConfigMin();
		if (ValueUtil.valueEquals(ValueUtil.VALOR_NAO, listaOrdenacaoTabelaConfigMin.get(0))) {
			super.addOrderBy(sql, domain);
			return;
		}
		boolean possuiTB = sql.indexOf(" tb ") > -1 || sql.indexOf("tb.") > -1; 
		boolean primeiroCampo = true;
		sql.append(" ORDER BY ");
		for (String campo : listaOrdenacaoTabelaConfigMin) {
			sql.append(primeiroCampo ? "" : "|| " );
			sql.append(" CASE WHEN " + (possuiTB ? "tb." + campo : campo)).append(" = '0' THEN '0' ELSE '1' END ");
			primeiroCampo = false;
		}
		sql.append(" DESC ");
	}
    
	public double findConfigValorMinimo(Pedido pedido, String cdGrupoValorMinPedido, String cdLinha) throws SQLException {
		String cdTabelaPreco = ValueUtil.isNotEmpty(pedido.cdTabelaPreco) ? pedido.cdTabelaPreco : ValueUtil.VALOR_ZERO;
		String cdCentroCusto = ValueUtil.isNotEmpty(pedido.cdCentroCusto) ? pedido.cdCentroCusto : ValueUtil.VALOR_ZERO;
		String cdPlataformaVenda = ValueUtil.isNotEmpty(pedido.cdPlataformaVenda) ? pedido.cdPlataformaVenda : ValueUtil.VALOR_ZERO;
		cdGrupoValorMinPedido = ValueUtil.isNotEmpty(cdGrupoValorMinPedido) ? cdGrupoValorMinPedido : ValueUtil.VALOR_ZERO;
		return findConfigValorMinimo(new ConfigValorMinimo(pedido, cdTabelaPreco, cdGrupoValorMinPedido, cdCentroCusto, cdPlataformaVenda, cdLinha));
	}
	
	private double findConfigValorMinimo(ConfigValorMinimo configValorMinimoFilter) throws SQLException {
		Vector configValorMinimoList = findColumnValuesByExample(configValorMinimoFilter, column);
		return ValueUtil.isNotEmpty(configValorMinimoList) ? ValueUtil.getDoubleValue((String) configValorMinimoList.items[0])  : 0;
	}
	
	public void setColumn(String column) {
		this.column = column;
	}

	public HashMap<String, String> findLinhaUsaFlAgrupamento(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT tb.CDLINHA, linha.DSLINHA FROM ").append(tableName).append(" tb");
		sql.append(" JOIN TBLVPLINHA linha on");
		sql.append(" tb.CDLINHA = linha.CDLINHA");
		sql.append(" AND tb.CDEMPRESA = linha.CDEMPRESA");
		sql.append(" WHERE tb.FLAGRUPALINHAPRODUTOS = 'S'");
		sql.append(" AND tb.CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa));
		sql.append(" AND tb.CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante));
		sql.append(" AND tb.CDLINHA IN (");
		int size = pedido.getCdLinhaProdutoList().size();
		for (int i = 0; i < size; i++) {
			sql.append(Sql.getValue(pedido.getCdLinhaProdutoList().items[i])).append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
		try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			HashMap<String, String> configValorMinimoHash = new HashMap<>();
			while (rs.next()) {
				ConfigValorMinimo configValorMinimo = new ConfigValorMinimo();
				configValorMinimo.cdLinha = rs.getString(1);
				configValorMinimo.dsLinha = rs.getString(2);
				configValorMinimoHash.put(configValorMinimo.cdLinha, configValorMinimo.dsLinha);
			}
			return configValorMinimoHash;
		}
	}

	public boolean isPossuiLinhaUsaFlAgrupaLinhaProdutos(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT COUNT(TB.CDLINHA) AS CDLINHA");
		sql.append(" FROM ").append(tableName).append(" tb ");
		sql.append(" WHERE tb.FLAGRUPALINHAPRODUTOS = 'S'");
		sql.append(" AND tb.CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa));
		sql.append(" AND tb.CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante));
		sql.append(" AND tb.CDLINHA IN (");
		int size = pedido.getCdLinhaProdutoList().size();
		for (int i = 0; i < size; i++) {
			sql.append(Sql.getValue(pedido.getCdLinhaProdutoList().items[i])).append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
		return getInt(sql.toString()) > 0;
	}

}