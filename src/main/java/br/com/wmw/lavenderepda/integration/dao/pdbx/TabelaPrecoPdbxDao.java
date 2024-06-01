package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class TabelaPrecoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TabelaPreco();
	}

    private static TabelaPrecoPdbxDao instance;

    public TabelaPrecoPdbxDao() {
        super(TabelaPreco.TABLE_NAME);
    }

    public static TabelaPrecoPdbxDao getInstance() {
        if (instance == null) {
            instance = new TabelaPrecoPdbxDao();
        }
        return instance;
    }

    @Override
    protected void addInsertColumns(StringBuffer sql) throws SQLException { }
    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException { }
    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException { }
	
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowKey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDTABELAPRECO,");
		if (LavenderePdaConfig.exibeRelatorioComissaoAoSelecionarItem || LavenderePdaConfig.usaConfigCalculoComissao()) {
			sql.append(" VLPCTCOMISSAO,");
		}
		if (LavenderePdaConfig.isUsaTabelaPrecoPedido() && LavenderePdaConfig.permiteTabPrecoItemDiferentePedido()) {
			sql.append(" FLACESSAOUTRASTAB,");
			sql.append(" FLACESSIVELOUTRASTAB,");
		}
		if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
			sql.append(" FLDESCONTOQTDAUTO,");
		}
		if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido && LavenderePdaConfig.permiteDescAdicionalPorTabPreco) {
			sql.append(" VLPCTMAXDESCADICIONALITEM,");
		}
		if (LavenderePdaConfig.isValorMinimoParaPedidoPorTabelaPreco()
				|| LavenderePdaConfig.isValorMinimoParaPedidoPorTabelaPrecoAgrupadoPorItem()) {
			sql.append(" QTMINVALOR,");
		}
        if (LavenderePdaConfig.usaListaColunaPorTabelaPreco || LavenderePdaConfig.filtraTabelaPrecoPelaListaDoCliente || LavenderePdaConfig.usaHistoricoVendasPorListaColunaTabelaPreco) {
            sql.append(" CDLISTATABELAPRECO,");
            sql.append(" CDCOLUNATABELAPRECO,");
        }
        if (LavenderePdaConfig.restringeTabPrecoEspecialConformePadraoCliente || LavenderePdaConfig.restringeTabPrecoCondPagtoClienteEspecial) {
    		sql.append(" FLESPECIAL,");
    	}
		if (LavenderePdaConfig.usaModuloTrocaNoPedido || !ValueUtil.isEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher)) {
			sql.append(" FLTROCA,");
		}
		if (LavenderePdaConfig.isUsaPedidoBonificacao()) {
			sql.append(" FLBONIFICACAO,");
		}
		if (LavenderePdaConfig.usaLocalEstoquePorTabelaPreco()) {
			sql.append(" CDLOCALESTOQUE,");
		}
		if (LavenderePdaConfig.isUsaPrecoItemPorPesoMinimoPedido()
				|| LavenderePdaConfig.isValidaTabelaPrecoFechamentoPedido()
				|| LavenderePdaConfig.isValidaTabelaPrecoFechamentoItemPedido()) {
			sql.append(" QTPESOMIN,");
		}
		if (LavenderePdaConfig.gravaCodigoVerbaItemPedidoPorTabelaPreco) {
			sql.append(" CDVERBA,");
		}
		if (LavenderePdaConfig.usaQtMinProdTabPreco() || LavenderePdaConfig.isValidaTabelaPrecoFechamentoPedido()) {
			sql.append(" QTMINPRODUTO,")
			.append("QTMINPEDIDO,");
			if (LavenderePdaConfig.usaGradeProduto2()) {
				sql.append(" QTMINGRADE1,")
				.append("QTMINGRADE2,");
			}
		}
		if (LavenderePdaConfig.usaTabelaPrecoComVigencia) {
			sql.append(" DTINICIAL,");
			sql.append(" DTFINAL,");
		}
		if (LavenderePdaConfig.isAplicaDescontoCategoria()) {
			sql.append(" FLPERMITEDESCONTO,");
		}
		if (LavenderePdaConfig.permiteIndicarDataEntregaManualQuandoUsaCadastroEntrega) {
			sql.append(" FLINDICADTENTREGAMANUAL,");
		}
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			sql.append(" FLIGNORAINDICECONDTIPOPAGTO,");
			sql.append(" FLBLOQUEIADESC2,");			
		}
		if (LavenderePdaConfig.usaTabelaPrincipal()) {
			sql.append(" CDTABELAPRECOPRINCIPAL,");
		}
		if (LavenderePdaConfig.aplicaBaseadoTabelaPreco) {
			sql.append(" FLDMAXVLBASEITEMPED,");
		}
		sql.append(" FLPROMOCIONAL,");
		sql.append(" DSMSGALERTA,");
		sql.append(" DSTABELAPRECO,");
		sql.append(" FLDEVOLUCAOESTOQUE,");
		sql.append(" VLINDICECOMISSAOREPINTERNO,");
		sql.append(" FLBLOQUEIANEGOCIACAO");
	}

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
    	TabelaPreco tabelaPreco = new TabelaPreco();
        tabelaPreco.rowKey = rs.getString("rowkey");
        tabelaPreco.cdEmpresa = rs.getString("cdEmpresa");
        tabelaPreco.cdRepresentante = rs.getString("cdRepresentante");
        tabelaPreco.cdTabelaPreco = rs.getString("cdTabelaPreco");
        tabelaPreco.dsTabelaPreco = rs.getString("dsTabelaPreco");
		if (LavenderePdaConfig.exibeRelatorioComissaoAoSelecionarItem || LavenderePdaConfig.usaConfigCalculoComissao()) {
			tabelaPreco.vlPctComissao = ValueUtil.round(rs.getDouble("vlPctComissao"));
		}
		if (LavenderePdaConfig.isUsaTabelaPrecoPedido() && LavenderePdaConfig.permiteTabPrecoItemDiferentePedido()) {
			tabelaPreco.flAcessaOutrasTab = rs.getString("flAcessaOutrasTab");
			tabelaPreco.flAcessivelOutrasTab = rs.getString("flAcessivelOutrasTab");
		}
		if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
			tabelaPreco.flDescontoQtdAuto = rs.getString("flDescontoQtdAuto");
		}
		if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido && LavenderePdaConfig.permiteDescAdicionalPorTabPreco) {
			tabelaPreco.vlPctMaxDescAdicionalItem = ValueUtil.round(rs.getDouble("vlPctMaxDescAdicionalItem"));
			if (tabelaPreco.vlPctMaxDescAdicionalItem < 0.0) {
				tabelaPreco.vlPctMaxDescAdicionalItem = 0.0;
			}
		}
		if (LavenderePdaConfig.isValorMinimoParaPedidoPorTabelaPreco()
				|| LavenderePdaConfig.isValorMinimoParaPedidoPorTabelaPrecoAgrupadoPorItem()) {
			tabelaPreco.qtMinValor = ValueUtil.round(rs.getDouble("qtMinValor"));
		}
        if (LavenderePdaConfig.usaListaColunaPorTabelaPreco || LavenderePdaConfig.filtraTabelaPrecoPelaListaDoCliente || LavenderePdaConfig.usaHistoricoVendasPorListaColunaTabelaPreco) {
            tabelaPreco.cdListaTabelaPreco = rs.getInt("cdListaTabelaPreco");
            tabelaPreco.cdColunaTabelaPreco = rs.getInt("cdColunaTabelaPreco");
        }
        if (LavenderePdaConfig.restringeTabPrecoEspecialConformePadraoCliente || LavenderePdaConfig.restringeTabPrecoCondPagtoClienteEspecial) {
    		tabelaPreco.flEspecial = rs.getString("flEspecial");
    	}
		if (LavenderePdaConfig.usaModuloTrocaNoPedido || !ValueUtil.isEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher)) {
			tabelaPreco.flTroca = rs.getString("flTroca");
		}
		if (LavenderePdaConfig.isUsaPedidoBonificacao()) {
			tabelaPreco.flBonificacao = rs.getString("flBonificacao");
		}
		if (LavenderePdaConfig.usaLocalEstoquePorTabelaPreco()) {
			tabelaPreco.cdLocalEstoque = rs.getString("cdLocalEstoque");
		}
		if (LavenderePdaConfig.isUsaPrecoItemPorPesoMinimoPedido()
				|| LavenderePdaConfig.isValidaTabelaPrecoFechamentoPedido()
				|| LavenderePdaConfig.isValidaTabelaPrecoFechamentoItemPedido()) {
			tabelaPreco.qtPesoMin = rs.getDouble("qtPesoMin");
		}
		tabelaPreco.flPromocional = rs.getString("flPromocional");
		tabelaPreco.dsMsgAlerta = rs.getString("dsMsgAlerta");
		if (LavenderePdaConfig.gravaCodigoVerbaItemPedidoPorTabelaPreco) {
			tabelaPreco.cdVerba = rs.getString("cdVerba");
		}
		if (LavenderePdaConfig.usaQtMinProdTabPreco() || LavenderePdaConfig.isValidaTabelaPrecoFechamentoPedido()) {
			tabelaPreco.qtMinProduto = rs.getDouble("qtMinProduto");
			tabelaPreco.qtMinPedido = rs.getDouble("qtMinPedido");
			if (LavenderePdaConfig.usaGradeProduto2()) {
				tabelaPreco.qtMinGrade1 = rs.getDouble("qtMinGrade1");
				tabelaPreco.qtMinGrade2 = rs.getDouble("qtMinGrade2");
			}
		}
		if (LavenderePdaConfig.isAplicaDescontoCategoria()) {
			tabelaPreco.flPermiteDesconto = rs.getString("flPermiteDesconto");
		}
		if (LavenderePdaConfig.permiteIndicarDataEntregaManualQuandoUsaCadastroEntrega) {
			tabelaPreco.flIndicaDtEntregaManual = rs.getString("flIndicaDtEntregaManual");
		}
		tabelaPreco.flDevolucaoEstoque = rs.getString("flDevolucaoEstoque");
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			tabelaPreco.flIgnoraIndiceCondTipoPagto = rs.getString("flIgnoraIndiceCondTipoPagto");
			tabelaPreco.flBloqueiaDesc2 = rs.getString("flBloqueiaDesc2");
		}
		if (LavenderePdaConfig.usaTabelaPrincipal()) {
			tabelaPreco.cdTabelaPrecoPrincipal = rs.getString("CDTABELAPRECOPRINCIPAL");
		}
	    if (LavenderePdaConfig.aplicaBaseadoTabelaPreco) {
		    tabelaPreco.flDMaxVlBaseItemPed = rs.getString("FLDMAXVLBASEITEMPED");
	    }
		tabelaPreco.vlIndiceComissaoRepInterno = rs.getDouble("VLINDICECOMISSAOREPINTERNO");
		tabelaPreco.flBloqueiaNegociacao = rs.getString("flBloqueiaNegociacao");

        return tabelaPreco;
    }
    
	@Override
	protected void addCacheColumns(StringBuffer sql) throws SQLException {
		sql.append(" CDEMPRESA, CDREPRESENTANTE, CDTABELAPRECO, DSTABELAPRECO, FLPROMOCIONAL");
	}
    
    @Override
    protected BaseDomain populateCache(ResultSet rs) throws SQLException {
    	TabelaPreco tabelaPreco = new TabelaPreco();
        tabelaPreco.rowKey = rs.getString(1);
        tabelaPreco.cdEmpresa = rs.getString(2);
        tabelaPreco.cdRepresentante = rs.getString(3);
        tabelaPreco.cdTabelaPreco = rs.getString(4);
        tabelaPreco.dsTabelaPreco = rs.getString(5);
        tabelaPreco.flPromocional = rs.getString(6);
        return tabelaPreco;
    }

    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
        TabelaPreco tabelaPreco = (TabelaPreco) domain;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
       	sqlWhereClause.addAndConditionEquals("CDEMPRESA", tabelaPreco.cdEmpresa);
       	sqlWhereClause.addAndConditionEquals("CDREPRESENTANTE", tabelaPreco.cdRepresentante);
       	sqlWhereClause.addAndConditionEquals("CDTABELAPRECO", tabelaPreco.cdTabelaPreco);
       	sqlWhereClause.addAndConditionEquals("CDLOCALESTOQUE", tabelaPreco.cdLocalEstoque);
		sqlWhereClause.addAndCondition("VLINDICECOMISSAOREPINTERNO = ", tabelaPreco.vlIndiceComissaoRepInterno);
		sqlWhereClause.addAndConditionEquals("FLTROCA", tabelaPreco.flTroca);
		sqlWhereClause.addAndConditionEquals("FLBONIFICACAO", tabelaPreco.flBonificacao);
       	if (LavenderePdaConfig.usaTabelaPrecoComVigencia) {
       		sqlWhereClause.addAndCondition("(DTINICIAL is null or DTINICIAL <= " + Sql.getValue(tabelaPreco.dtInicial) + ")");
       		sqlWhereClause.addAndCondition("(DTFINAL is null or DTFINAL >= " + Sql.getValue(tabelaPreco.dtFinal) + ")");
       	}
       	if (LavenderePdaConfig.isUsaTabelaPrecoSomenteCatalogo()) {
       		if (tabelaPreco.usaTabelaPrecoApenasCatalogo) {
       			sqlWhereClause.addAndConditionEquals("FLSOMENTECATALOGO ", ValueUtil.VALOR_SIM);
       		} else {
       			sqlWhereClause.addStartAndMultipleCondition();
       			sqlWhereClause.addAndConditionNotEquals("FLSOMENTECATALOGO", ValueUtil.VALOR_SIM);
       			sqlWhereClause.addOrCondition("FLSOMENTECATALOGO IS NULL");
       			sqlWhereClause.addEndMultipleCondition();
       		}
       	}
       	if (LavenderePdaConfig.isUsaTabelaPrecoPedido() && LavenderePdaConfig.permiteTabPrecoItemDiferentePedido() && ValueUtil.isNotEmpty(tabelaPreco.cdTabelaPrecoPedidoFilter)) {
       		sqlWhereClause.addStartAndMultipleCondition();
       		sqlWhereClause.addAndConditionNotEquals("COALESCE(FLACESSIVELOUTRASTAB, " + Sql.getValue(ValueUtil.VALOR_SIM) + ")", tabelaPreco.notFlAcessivelOutrasTab);
       		sqlWhereClause.addOrCondition("CDTABELAPRECO = " + Sql.getValue(tabelaPreco.cdTabelaPrecoPedidoFilter));
       		sqlWhereClause.addEndMultipleCondition();
       	}
       	if (LavenderePdaConfig.usaModuloTrocaNoPedido || ValueUtil.isNotEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher)) {
       		sqlWhereClause.addAndConditionNotEquals("COALESCE(FLTROCA, " + Sql.getValue(ValueUtil.VALOR_NAO) + ")", tabelaPreco.notFlTroca);
       	}
       	if (LavenderePdaConfig.isUsaPedidoBonificacao()) {
       		sqlWhereClause.addAndConditionNotEquals("COALESCE(FLBONIFICACAO, " + Sql.getValue(ValueUtil.VALOR_NAO) + ")", tabelaPreco.notFlBonificacao);
       	}
       	if (LavenderePdaConfig.restringeTabPrecoEspecialConformePadraoCliente && ValueUtil.isNotEmpty(tabelaPreco.notFlEspecial)) {
       		sqlWhereClause.addStartAndMultipleCondition();
       		sqlWhereClause.addAndConditionNotEquals("COALESCE(FLESPECIAL, " + Sql.getValue(ValueUtil.VALOR_NAO) + ")", tabelaPreco.notFlEspecial);
       		sqlWhereClause.addOrCondition("CDTABELAPRECO = " + Sql.getValue(tabelaPreco.cdTabelaPrecoClienteFilter));
       		sqlWhereClause.addEndMultipleCondition();
       	}
       	if (tabelaPreco.filterByHierarquiaTabelasPreco && ValueUtil.isNotEmpty(tabelaPreco.cdTabelaPrecoPedidoFilter)) {
       		sqlWhereClause.addStartAndMultipleCondition();
       		sqlWhereClause.addAndConditionEquals("CDTABELAPRECO", tabelaPreco.cdTabelaPrecoPedidoFilter);
       		sqlWhereClause.addOrCondition("CDTABELAPRECOPRINCIPAL =", tabelaPreco.cdTabelaPrecoPedidoFilter);
       		sqlWhereClause.addEndMultipleCondition();
       	}
       	if (tabelaPreco.filterByCdListaTabelaPreco) {
       		sqlWhereClause.addAndCondition("CDLISTATABELAPRECO = " + Sql.getValue(tabelaPreco.cdListaTabelaPreco));
       	}
        sql.append(sqlWhereClause.getSql());
        
        if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco() && ValueUtil.isNotEmpty(tabelaPreco.cdTabelaPrecoList)) {
       		addLoopInSql(sql, new Vector(tabelaPreco.cdTabelaPrecoList), "CDTABELAPRECO", 400);
       	}
        if (LavenderePdaConfig.usaTabelaPrecoPorCondicaoPagamento && tabelaPreco.condPagtoTabPrecoFilter != null) {
        	DaoUtil.addExistsCondPagtoTabPreco(sql, tabelaPreco.condPagtoTabPrecoFilter, false);
        }
        if (LavenderePdaConfig.usaTabelaPrecoPorCliente && tabelaPreco.tabelaPrecoCliFilter != null) {
        	DaoUtil.addExistsTabelaPrecoCli(sql, tabelaPreco.tabelaPrecoCliFilter);
        }
        if (LavenderePdaConfig.usaTabelaPrecoPorRepresentante && tabelaPreco.tabelaPrecoRepFilter != null) {
        	DaoUtil.addExistsTabelaPrecoRep(sql, tabelaPreco.tabelaPrecoRepFilter);
        }
        if (LavenderePdaConfig.filtraTabelaPrecoPelaRegiaoDoCliente && tabelaPreco.tabelaPrecoRegFilter != null) {
        	DaoUtil.addExistsTabelaPrecoReg(sql, tabelaPreco.tabelaPrecoRegFilter);
        }
        if (LavenderePdaConfig.usaTabelaPrecoPorSegmento && tabelaPreco.tabelaPrecoSegFilter != null) {
        	DaoUtil.addExistsTabelaPrecoSeg(sql, tabelaPreco.tabelaPrecoSegFilter);
        }
        if (LavenderePdaConfig.usaTabelaPrecoPorTipoPedido && tabelaPreco.tabPrecTipoPedidoFilter != null) {
        	DaoUtil.addExistsTabPrecTipoPedido(sql, tabelaPreco.tabPrecTipoPedidoFilter);
        }
        if (LavenderePdaConfig.usaTabelaPrecoPorCliente && LavenderePdaConfig.isEntidadeSyncWebAppRepZero(ItemTabelaPreco.TABLE_NAME.substring(5))) {
        	TabelaPrecoCliPdbxDao.addTabPrecoCliFilter(sql, false);
        }
    }

    public String[] findCdsTabPrecoByExample(BaseDomain domain) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT CDTABELAPRECO FROM ");
    	sql.append(tableName);
    	addWhereByExample(domain, sql);
    	return getCurrentDriver().getStrings1(sql.toString());
    }

	@Override
	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		if (LavenderePdaConfig.isOrdenaTabelasPrecoPorPesoMinimo()) {
			sql.append(" ORDER BY QTPESOMIN, DSTABELAPRECO ");
		} else {
			super.addOrderBy(sql, domain);
		}
	}
}
