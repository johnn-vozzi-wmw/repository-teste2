
package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.domain.Tributacao;
import br.com.wmw.lavenderepda.business.domain.TributacaoConfig;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;

public class TributacaoConfigDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TributacaoConfig();
	}

    private static TributacaoConfigDao instance;

    public TributacaoConfigDao() {
        super(TributacaoConfig.TABLE_NAME);
    }
    
    public static TributacaoConfigDao getInstance() {
        if (instance == null) {
            instance = new TributacaoConfigDao();
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
  		TributacaoConfig tributacaoConfig = (TributacaoConfig) domain;
  		SqlWhereClause sqlWhereClause = new SqlWhereClause();
  		sqlWhereClause.addAndCondition("CDEMPRESA = ", tributacaoConfig.cdEmpresa);
  		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", tributacaoConfig.cdRepresentante);
  		sqlWhereClause.addAndCondition("CDTIPOPEDIDO = ", tributacaoConfig.cdTipoPedido);
  		sql.append(sqlWhereClause.getSql());
  	}

    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	TributacaoConfig tributacaoConfig = new TributacaoConfig();
        tributacaoConfig.rowKey = rs.getString("rowkey");
        tributacaoConfig.cdEmpresa = rs.getString("cdEmpresa");
        tributacaoConfig.cdRepresentante = rs.getString("cdRepresentante");
        tributacaoConfig.cdTributacaoConfig = rs.getString("cdTributacaoConfig");
        tributacaoConfig.cdTributacaoCliente = rs.getString("cdTributacaoCliente");
        tributacaoConfig.cdTributacaoProduto = rs.getString("cdTributacaoProduto");
        tributacaoConfig.cdTipoPedido = rs.getString("cdTipoPedido");
        tributacaoConfig.cdCliente = rs.getString("cdCliente");
        tributacaoConfig.cdProduto = rs.getString("cdProduto");
        tributacaoConfig.cdUf = rs.getString("cdUf");
        tributacaoConfig.flCalculaIpi = rs.getString("flCalculaIpi");
        tributacaoConfig.flFreteBaseIpi = rs.getString("flFreteBaseIpi");
        tributacaoConfig.flTipoCalculoPisCofins = rs.getString("flTipoCalculoPisCofins");
        tributacaoConfig.flFreteBasePisCofins = rs.getString("flFreteBasePisCofins");
        tributacaoConfig.flCalculaIcms = rs.getString("flCalculaIcms");
        tributacaoConfig.flFreteBaseIcms = rs.getString("flFreteBaseIcms");
        tributacaoConfig.flIpiBaseIcms = rs.getString("flIpiBaseIcms");
        tributacaoConfig.flCalculaSt = rs.getString("flCalculaSt");
        tributacaoConfig.flFreteBaseSt = rs.getString("flFreteBaseSt");
        tributacaoConfig.flIpiBaseSt = rs.getString("flIpiBaseSt");
        tributacaoConfig.flVerificaValorItem = rs.getString("flVerificaValorItem");
        tributacaoConfig.nuCarimbo = rs.getInt("nuCarimbo");
        tributacaoConfig.flTipoAlteracao = rs.getString("flTipoAlteracao");
        tributacaoConfig.cdUsuario = rs.getString("cdUsuario");
        tributacaoConfig.flAplicaReducaoBaseIcmsRetido = rs.getString("flAplicaReducaoBaseIcmsRetido");
        tributacaoConfig.flUtilizaValorFixoImpostos = rs.getString("flUtilizaValorFixoImpostos");
		tributacaoConfig.cdClassificFiscal = rs.getString("cdClassificFiscal");
        tributacaoConfig.flReduzIcmsPisCofins = rs.getString("flReduzIcmsPisCofins");
        return tributacaoConfig;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		addSelectColumns(sql, ValueUtil.VALOR_NI);
	}
	
    public void addSelectColumns(StringBuffer sql, String aliasTable) {
    	aliasTable = ValueUtil.isNotEmpty(aliasTable) ? aliasTable + "." : "";
        sql.append(aliasTable).append("rowkey, ");
        sql.append(aliasTable).append("CDEMPRESA, ");
        sql.append(aliasTable).append("CDREPRESENTANTE, ");
        sql.append(aliasTable).append("CDTRIBUTACAOCONFIG, ");
        sql.append(aliasTable).append("CDTRIBUTACAOCLIENTE, ");
        sql.append(aliasTable).append("CDTRIBUTACAOPRODUTO, ");
        sql.append(aliasTable).append("CDTIPOPEDIDO, ");
        sql.append(aliasTable).append("CDCLIENTE, ");
        sql.append(aliasTable).append("CDPRODUTO, ");
        sql.append(aliasTable).append("CDUF, ");
        sql.append(aliasTable).append("FLCALCULAIPI, ");
        sql.append(aliasTable).append("FLFRETEBASEIPI, ");
        sql.append(aliasTable).append("FLTIPOCALCULOPISCOFINS, ");
        sql.append(aliasTable).append("FLFRETEBASEPISCOFINS, ");
        sql.append(aliasTable).append("FLCALCULAICMS, ");
        sql.append(aliasTable).append("FLFRETEBASEICMS, ");
        sql.append(aliasTable).append("FLIPIBASEICMS, ");
        sql.append(aliasTable).append("FLCALCULAST, ");
        sql.append(aliasTable).append("FLFRETEBASEST, ");
        sql.append(aliasTable).append("FLIPIBASEST, ");
        sql.append(aliasTable).append("FLVERIFICAVALORITEM, ");
        sql.append(aliasTable).append("NUCARIMBO, ");
        sql.append(aliasTable).append("FLTIPOALTERACAO, ");
        sql.append(aliasTable).append("CDUSUARIO, ");
        sql.append(aliasTable).append("FLAPLICAREDUCAOBASEICMSRETIDO, ");
        sql.append(aliasTable).append("FLUTILIZAVALORFIXOIMPOSTOS, ");
		sql.append(aliasTable).append("CDCLASSIFICFISCAL, ");
	    sql.append(aliasTable).append("FLREDUZICMSPISCOFINS ");
    }
    
    @Override
    protected void addCacheColumns(StringBuffer sql) throws SQLException {
    	sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDTRIBUTACAOCONFIG,");
        sql.append(" CDTRIBUTACAOCLIENTE,");
        sql.append(" CDTRIBUTACAOPRODUTO,");
        sql.append(" CDTIPOPEDIDO,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDUF,");
        sql.append(" FLCALCULAIPI,");
        sql.append(" FLFRETEBASEIPI,");
        sql.append(" FLTIPOCALCULOPISCOFINS,");
        sql.append(" FLFRETEBASEPISCOFINS,");
        sql.append(" FLCALCULAICMS,");
        sql.append(" FLFRETEBASEICMS,");
        sql.append(" FLIPIBASEICMS,");
        sql.append(" FLCALCULAST,");
        sql.append(" FLFRETEBASEST,");
        sql.append(" FLIPIBASEST,");
        sql.append(" FLVERIFICAVALORITEM,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLAPLICAREDUCAOBASEICMSRETIDO,");
        sql.append(" FLUTILIZAVALORFIXOIMPOSTOS,");
	    sql.append(" FLREDUZICMSPISCOFINS,");
		sql.append(" CDCLASSIFICFISCAL ");
    }
	
	@Override
	protected BaseDomain populateCache(ResultSet rs) throws SQLException {
    	TributacaoConfig tributacaoConfig = new TributacaoConfig();
        tributacaoConfig.rowKey = rs.getString(1);
    	tributacaoConfig.cdEmpresa = rs.getString(2);
        tributacaoConfig.cdRepresentante = rs.getString(3);
        tributacaoConfig.cdTributacaoConfig = rs.getString(4);
        tributacaoConfig.cdTributacaoCliente = rs.getString(5);
        tributacaoConfig.cdTributacaoProduto = rs.getString(6);
        tributacaoConfig.cdTipoPedido = rs.getString(7);
        tributacaoConfig.cdCliente = rs.getString(8);
        tributacaoConfig.cdProduto = rs.getString(9);
        tributacaoConfig.cdUf = rs.getString(10);
        tributacaoConfig.flCalculaIpi = rs.getString(11);
        tributacaoConfig.flFreteBaseIpi = rs.getString(12);
        tributacaoConfig.flTipoCalculoPisCofins = rs.getString(13);
        tributacaoConfig.flFreteBasePisCofins = rs.getString(14);
        tributacaoConfig.flCalculaIcms = rs.getString(15);
        tributacaoConfig.flFreteBaseIcms = rs.getString(16);
        tributacaoConfig.flIpiBaseIcms = rs.getString(17);
        tributacaoConfig.flCalculaSt = rs.getString(18);
        tributacaoConfig.flFreteBaseSt = rs.getString(19);
        tributacaoConfig.flIpiBaseSt = rs.getString(20);
        tributacaoConfig.flVerificaValorItem = rs.getString(21);
        tributacaoConfig.cdUsuario = rs.getString(22);
        tributacaoConfig.flAplicaReducaoBaseIcmsRetido = rs.getString(23);
        tributacaoConfig.flUtilizaValorFixoImpostos = rs.getString(24);
		tributacaoConfig.flReduzIcmsPisCofins = rs.getString(25);
		tributacaoConfig.cdClassificFiscal = rs.getString(25);
        return tributacaoConfig;
	}
	
	public TributacaoConfig findTributacaoConfigProduto(Cliente cliente, Produto produto, TipoPedido tipoPedido) throws java.sql.SQLException {
		TributacaoConfig tributacaoConfig;
		String cdTipoPedido = tipoPedido == null || tipoPedido.isUsaTributacaoGeral() ? Tributacao.CDTIPOPEDIDOVALORPADRAO : tipoPedido.cdTipoPedido;
		String cdUf =  ValueUtil.isNotEmpty(cliente.cdEstadoComercial)  ?  cliente.cdEstadoComercial : ValueUtil.VALOR_ZERO;
		tributacaoConfig = findTributacaoConfigProduto(new TributacaoConfig(ValueUtil.VALOR_ZERO, ValueUtil.VALOR_ZERO, cdTipoPedido, cliente.cdCliente, produto.cdProduto, cdUf));
		if (tributacaoConfig == null) {
			tributacaoConfig = findTributacaoConfigProduto(new TributacaoConfig(ValueUtil.VALOR_ZERO, produto.cdTributacaoProduto, cdTipoPedido, cliente.cdCliente, ValueUtil.VALOR_ZERO, cdUf));
		}
		if (tributacaoConfig == null) {
			tributacaoConfig = findTributacaoConfigProduto(new TributacaoConfig(cliente.cdTributacaoCliente, ValueUtil.VALOR_ZERO, cdTipoPedido, ValueUtil.VALOR_ZERO, produto.cdProduto, cdUf));
		}
		if (tributacaoConfig == null && cliente.cdTributacaoCliente2 != null) {
			tributacaoConfig = findTributacaoConfigProduto(new TributacaoConfig(cliente.cdTributacaoCliente2, ValueUtil.VALOR_ZERO, cdTipoPedido, ValueUtil.VALOR_ZERO, produto.cdProduto, cdUf));
		}
		if (tributacaoConfig == null) {
			tributacaoConfig = findTributacaoConfigProduto(new TributacaoConfig(cliente.cdTributacaoCliente, produto.cdTributacaoProduto, cdTipoPedido, ValueUtil.VALOR_ZERO, ValueUtil.VALOR_ZERO, cdUf));
		}
		if (tributacaoConfig == null && cliente.cdTributacaoCliente2 != null) {
			tributacaoConfig = findTributacaoConfigProduto(new TributacaoConfig(cliente.cdTributacaoCliente2, produto.cdTributacaoProduto, cdTipoPedido, ValueUtil.VALOR_ZERO, ValueUtil.VALOR_ZERO, cdUf));
		}
		if (tributacaoConfig == null) {
			tributacaoConfig = findTributacaoConfigProduto(new TributacaoConfig(ValueUtil.VALOR_ZERO, ValueUtil.VALOR_ZERO, cdTipoPedido, cliente.cdCliente, ValueUtil.VALOR_ZERO, cdUf));
		}
		if (tributacaoConfig == null) {
			tributacaoConfig = findTributacaoConfigProduto(new TributacaoConfig(ValueUtil.VALOR_ZERO, ValueUtil.VALOR_ZERO, cdTipoPedido, ValueUtil.VALOR_ZERO, produto.cdProduto, cdUf));
		}
		if (tributacaoConfig == null) {
			tributacaoConfig = findTributacaoConfigProduto(new TributacaoConfig(cliente.cdTributacaoCliente, ValueUtil.VALOR_ZERO, cdTipoPedido, ValueUtil.VALOR_ZERO, ValueUtil.VALOR_ZERO, cdUf));
		}
		if (tributacaoConfig == null && cliente.cdTributacaoCliente2 != null) {
			tributacaoConfig = findTributacaoConfigProduto(new TributacaoConfig(cliente.cdTributacaoCliente2, ValueUtil.VALOR_ZERO, cdTipoPedido, ValueUtil.VALOR_ZERO, ValueUtil.VALOR_ZERO, cdUf));
		}
		if (tributacaoConfig == null) {
			tributacaoConfig = findTributacaoConfigProduto(new TributacaoConfig(ValueUtil.VALOR_ZERO, produto.cdTributacaoProduto, cdTipoPedido, ValueUtil.VALOR_ZERO, ValueUtil.VALOR_ZERO, cdUf));
		}
		if (tributacaoConfig == null) {
			tributacaoConfig = findTributacaoConfigProduto(new TributacaoConfig(ValueUtil.VALOR_ZERO, ValueUtil.VALOR_ZERO, cdTipoPedido, ValueUtil.VALOR_ZERO, ValueUtil.VALOR_ZERO, cdUf));
		}
		return tributacaoConfig;
	}
	
	public TributacaoConfig findTributacaoConfigProduto(TributacaoConfig tributacaoConfigFilter) throws SQLException { 
		if (tributacaoConfigFilter.cdTributacaoCliente == null || tributacaoConfigFilter.cdTributacaoProduto == null) return null;
        TributacaoConfig tributacaoConfig = (TributacaoConfig) findByRowKey(tributacaoConfigFilter.getRowKey()); 
        if (tributacaoConfig == null) { 
             tributacaoConfigFilter.cdUf = ValueUtil.VALOR_ZERO; 
             tributacaoConfig = (TributacaoConfig) findByRowKey(tributacaoConfigFilter.getRowKey()); 
        } 
        return tributacaoConfig; 
   }
	public void addJoinTributacaoConfigByPrioridadeProdutoTabPreco(StringBuffer sql, int prioridade, String cdTributacaoClienteFilter, String cdClienteFilter, String cdProdutoFilter) {
		String alias = " priori";
		sql.append(" SELECT ").append(prioridade).append(" AS PRIORIDADE, ");
		addSelectColumns(sql, alias);
		sql.append(" FROM ").append(DaoUtil.ALIAS_CTETRIBUTACAOCONFIG).append(alias);
		if (prioridade <= 11 && prioridade != 7 && prioridade != 9 && prioridade != 10) {
			sql.append(" JOIN TBLVPPRODUTO tb on tb.CDEMPRESA = priori.CDEMPRESA AND")
				.append(" tb.CDREPRESENTANTE = ").append(Sql.getValue(SessionLavenderePda.getCdRepresentanteFiltroDados(Produto.class)));
		if (prioridade ==  1 || prioridade == 3  || prioridade == 4 || prioridade == 8) {
				sql.append(" AND priori.CDPRODUTO = tb.CDPRODUTO ");
		} else {
				sql.append(" AND priori.CDTRIBUTACAOPRODUTO = tb.CDTRIBUTACAOPRODUTO ");
		}
		}
		sql.append(" WHERE ");
		sql.append(alias).append(".CDTRIBUTACAOCLIENTE = ").append(Sql.getValue(cdTributacaoClienteFilter))
			.append(" AND").append(alias).append(".CDCLIENTE = ").append(Sql.getValue(cdClienteFilter));
		if (!(prioridade == 2 || prioridade == 5 || prioridade == 6 || prioridade == 11)) {
			sql.append(" AND").append(alias).append(".CDTRIBUTACAOPRODUTO = ").append(Sql.getValue(ValueUtil.VALOR_ZERO));
		} 
		if (!(prioridade == 1 || prioridade == 3 || prioridade == 4 || prioridade == 8)) {
			sql.append(" AND").append(alias).append(".CDPRODUTO = ").append(Sql.getValue(cdProdutoFilter));
		}
	}
	
	
	
}
