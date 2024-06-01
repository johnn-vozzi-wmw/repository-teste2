package br.com.wmw.lavenderepda.integration.dao.pdbx;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.CorSistema;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ClienteSetorOrigem;
import br.com.wmw.lavenderepda.business.domain.CondComercialExcec;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.EstoqueDisponivel;
import br.com.wmw.lavenderepda.business.domain.FotoProduto;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoDestaque;
import br.com.wmw.lavenderepda.business.domain.ProdutoTabPreco;
import br.com.wmw.lavenderepda.business.domain.TributacaoConfig;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ProdutoTabPrecoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProdutoTabPreco();
	}

    private static ProdutoTabPrecoPdbxDao instance;

    public ProdutoTabPrecoPdbxDao() {
        super(ProdutoTabPreco.TABLE_NAME);
    }

    public static ProdutoTabPrecoPdbxDao getInstance() {
        if (instance == null) {
            instance = new ProdutoTabPrecoPdbxDao();
        }
        return instance;
    }

    @Override
    protected void addInsertColumns(StringBuffer sql) throws SQLException { /**/ }
    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException { /**/ }

    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" DSPRODUTO,");
        if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto()) {
        	sql.append(" DSPRINCIPIOATIVO,");
        }
        if (LavenderePdaConfig.isMostraProdutoPromocionalDestacadoTelaItemPedido()) {
        	sql.append(" DSTABPRECOPROMOLIST,");
        }
        if (LavenderePdaConfig.isMostraProdutoDescPromocionalDestacadoTelaItemPedido() || LavenderePdaConfig.naoConsideraProdutoDescPromocionalComoPromocional) {
        	sql.append(" DSTABPRECODESCPROMOCIONALLIST,");
        }
        if (LavenderePdaConfig.usaOportunidadeVenda) {
        	sql.append(" DSTABPRECOOPORTUNIDADELIST,");
        }
        if (LavenderePdaConfig.usaFiltroFornecedor) {
        	sql.append(" CDFORNECEDOR,");
        }
        if (LavenderePdaConfig.isPermiteBonificarProduto()) {
        	sql.append(" FLBONIFICACAO,");
        }
        if (LavenderePdaConfig.usaAvisoPreAlta) {
			sql.append(" FLPREALTACUSTO,");
		}
        if (LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
        	sql.append(" FLKIT,");
        }
        if (LavenderePdaConfig.usaFiltroMarcaProduto || LavenderePdaConfig.apresentaMarcaNasListasNoPedido()) {
        	sql.append(" DSMARCA,");
        }
        if (LavenderePdaConfig.isMostraDescricaoReferencia() || LavenderePdaConfig.usaDsReferenciaNmFoto) {
			sql.append(" DSREFERENCIA,");
        }
        if (LavenderePdaConfig.isMostraQtVendasProdutoNoPeriodo()) {
        	sql.append(" QTVENDASPERIODO,");
        }
        if (LavenderePdaConfig.filtraProdutosPorOrigemPedido) {
        	sql.append(" CDORIGEMSETOR,");
        }
        if (LavenderePdaConfig.isUsaGrupoDestaqueProduto()) {
        	sql.append(" CDGRUPODESTAQUE,");
        }
        if (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional()) {
        	sql.append(" CDGRUPODESCPROD,");
        }
        if (!ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.usaPrecoPadraoListaProdutos)) {
			sql.append(" VLPRECOPADRAO,");
		}
        if (LavenderePdaConfig.usaDestaqueItensVendidosMesCorrente) {
        	sql.append(" FLVENDIDO,");
        }
        if (LavenderePdaConfig.usaSugestaoVendaProdutosPorFoto > 0) {
			sql.append(" CDFATORCOMUM,");
		}
        if (LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito()) {
        	sql.append(" FLSITUACAO,");
        }
		sql.append(" CDUNIDADE,");
        sql.append(" DSTABPRECOLIST");
    }

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        ProdutoTabPreco produtoTabPreco = new ProdutoTabPreco();
        produtoTabPreco.rowKey = rs.getString("rowkey");
        produtoTabPreco.cdEmpresa = rs.getString("cdEmpresa");
        produtoTabPreco.cdRepresentante = rs.getString("cdRepresentante");
        produtoTabPreco.cdProduto = rs.getString("cdProduto");
        produtoTabPreco.dsProduto = rs.getString("dsProduto");
        produtoTabPreco.dsTabPrecoList = rs.getString("dsTabPrecoList");
        if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto()) {
        	produtoTabPreco.dsPrincipioAtivo = rs.getString("dsPrincipioAtivo");
        }
        if (LavenderePdaConfig.isMostraProdutoPromocionalDestacadoTelaItemPedido()) {
        	produtoTabPreco.dsTabPrecoPromoList = rs.getString("dsTabPrecoPromoList");
        }
        if (LavenderePdaConfig.isMostraProdutoDescPromocionalDestacadoTelaItemPedido() || LavenderePdaConfig.naoConsideraProdutoDescPromocionalComoPromocional) {
        	produtoTabPreco.dsTabPrecoDescPromocionalList = rs.getString("dsTabPrecoDescPromocionalList");
        }
        if (LavenderePdaConfig.usaOportunidadeVenda) {
        	produtoTabPreco.dsTabPrecoOportunidadeList = rs.getString("dsTabPrecoOportunidadeList");
        }
        if (LavenderePdaConfig.usaFiltroFornecedor) {
        	produtoTabPreco.cdFornecedor = rs.getString("cdFornecedor");
        }
        if (LavenderePdaConfig.isPermiteBonificarProduto()) {
        	produtoTabPreco.flBonificacao = rs.getString("flBonificacao");
        }
        if (LavenderePdaConfig.usaAvisoPreAlta) {
        	produtoTabPreco.flPreAltaCusto = rs.getString("flPreAltaCusto");
		}
        if (LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
        	produtoTabPreco.flKit = rs.getString("flKit");
        }
        if (LavenderePdaConfig.usaFiltroMarcaProduto || LavenderePdaConfig.apresentaMarcaNasListasNoPedido()) {
        	produtoTabPreco.dsMarca = rs.getString("dsMarca");
        }
        if (LavenderePdaConfig.isMostraDescricaoReferencia() || LavenderePdaConfig.usaDsReferenciaNmFoto) {
        	produtoTabPreco.dsReferencia = rs.getString("dsReferencia");
		}
        if (LavenderePdaConfig.isMostraQtVendasProdutoNoPeriodo()) {
        	produtoTabPreco.qtVendasPeriodo = rs.getInt("qtVendasPeriodo");
        }
        if (LavenderePdaConfig.filtraProdutosPorOrigemPedido) {
        	produtoTabPreco.cdOrigemSetor = rs.getString("cdOrigemSetor");
        }
        if (LavenderePdaConfig.isUsaGrupoDestaqueProduto()) {
        	produtoTabPreco.cdGrupoDestaque = rs.getString("cdGrupoDestaque");
        }
        if (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional()) {
        	produtoTabPreco.cdGrupoDescProd = rs.getString("cdGrupoDescProd");
        }
        if (!ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.usaPrecoPadraoListaProdutos)) {
        	produtoTabPreco.vlPrecoPadrao = rs.getDouble("vlPrecoPadrao");
		}
        if (LavenderePdaConfig.usaDestaqueItensVendidosMesCorrente) {
        	produtoTabPreco.flVendido = rs.getString("flVendido");
        }
        if (LavenderePdaConfig.usaSugestaoVendaProdutosPorFoto > 0) {
			produtoTabPreco.cdFatorComum = rs.getString("cdFatorComum");
		}
        if (LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito()) {
        	produtoTabPreco.flSituacao = rs.getInt("flSituacao");
        }
        produtoTabPreco.cdUnidade = rs.getString("cdUnidade");
        return produtoTabPreco;
    }

    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
        ProdutoTabPreco produtoTabPreco = (ProdutoTabPreco) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", produtoTabPreco.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", produtoTabPreco.cdRepresentante);
       	sqlWhereClause.addAndCondition("tb.CDFORNECEDOR = ", produtoTabPreco.cdFornecedor);
       	if (LavenderePdaConfig.usaFiltroGrupoProduto == 4) {
       		sqlWhereClause.addAndCondition("tb.CDGRUPOPRODUTO4 = ", produtoTabPreco.cdGrupoProduto4);
       	}
       	if (LavenderePdaConfig.usaFiltroGrupoProduto >= 3) {
       		sqlWhereClause.addAndCondition("tb.CDGRUPOPRODUTO3 = ", produtoTabPreco.cdGrupoProduto3);
       	}
       	if (LavenderePdaConfig.usaFiltroGrupoProduto >= 2) {
       		sqlWhereClause.addAndCondition("tb.CDGRUPOPRODUTO2 = ", produtoTabPreco.cdGrupoProduto2);
       	}
       	if (LavenderePdaConfig.usaFiltroGrupoProduto >= 1) {
       		sqlWhereClause.addAndCondition("tb.CDGRUPOPRODUTO1 = ", produtoTabPreco.cdGrupoProduto1);
       	}
       	if (LavenderePdaConfig.isUsaGrupoDestaqueProduto()) {
	        String columnName = LavenderePdaConfig.usaGrupoDestaqueItemTabPreco ? DaoUtil.ALIAS_ITEMTABELAPRECO + ".CDGRUPODESTAQUE" : "tb.CDGRUPODESTAQUE";
	        DaoUtil.addAndMultipleOrLikeCondition(sqlWhereClause, StringUtil.split(produtoTabPreco.cdGrupoDestaque, ';'), columnName, StringUtil.getStringValue(ProdutoTabPreco.SEPARADOR_CAMPOS));
       	}
		if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPrecoDisponivel() && produtoTabPreco.fromListItemPedido) {
			sqlWhereClause.addAndCondition("instr(tb.DSTABPRECOLIST, \'" + produtoTabPreco.cdTabelaPreco + "\')");
		} else {
			sqlWhereClause.addAndLikeCondition("tb.DSTABPRECOLIST", produtoTabPreco.dsTabPrecoList, false);
		}
       	if (LavenderePdaConfig.usaAvisoPreAlta && (ValueUtil.VALOR_SIM.equals(produtoTabPreco.flPreAltaCusto))) {
       		sqlWhereClause.addAndCondition("tb.flPreAltaCusto = ", produtoTabPreco.flPreAltaCusto);
       	}
       	if (LavenderePdaConfig.filtraProdutosPorOrigemPedido && !ClienteSetorOrigem.CLIENTE_SETOR_ORIGEM_SEM_CONTRATO.equals(produtoTabPreco.cdOrigemSetor)) {
       		sqlWhereClause.addAndCondition("tb.cdOrigemSetor = ", produtoTabPreco.cdOrigemSetor);
        }
       	if ((ValueUtil.VALOR_SIM.equals(produtoTabPreco.flFiltraProdutoPromocional)) && (LavenderePdaConfig.isUsaFiltraPorProdutosPromocionalItemPedido())) {
       		if (ValueUtil.isEmpty(produtoTabPreco.dsTabPrecoPromoList)) {
       			sqlWhereClause.addAndLikeCondition("tb.DSTABPRECOPROMOLIST", "|%", false);
       			sqlWhereClause.addAndLikeCondition("tb.DSTABPRECOPROMOLIST", "%|", false);
       		} else {
       			sqlWhereClause.addAndLikeCondition("tb.DSTABPRECOPROMOLIST", produtoTabPreco.dsTabPrecoPromoList, false);
       		}
       	}
       	if (ValueUtil.VALOR_SIM.equals(produtoTabPreco.flFiltraProdutoDescPromocional) && (LavenderePdaConfig.isUsaFiltroProdutoDescPromocional())) {
       		if (ValueUtil.isEmpty(produtoTabPreco.dsTabPrecoDescPromocionalList)) {
       			sqlWhereClause.addAndLikeCondition("tb.DSTABPRECODESCPROMOCIONALLIST", "|%", false);
       			sqlWhereClause.addAndLikeCondition("tb.DSTABPRECODESCPROMOCIONALLIST", "%|", false);
       		} else {
       			sqlWhereClause.addAndLikeCondition("tb.DSTABPRECODESCPROMOCIONALLIST", produtoTabPreco.dsTabPrecoDescPromocionalList, false);
       		}
       	}
       	if (ValueUtil.VALOR_SIM.equals(produtoTabPreco.flFiltraProdutoOportunidade) && LavenderePdaConfig.usaOportunidadeVenda) {
       		if (ValueUtil.isEmpty(produtoTabPreco.dsTabPrecoOportunidadeList)) {
       			sqlWhereClause.addAndLikeCondition("tb.DSTABPRECOOPORTUNIDADELIST", "|%", false);
       			sqlWhereClause.addAndLikeCondition("tb.DSTABPRECOOPORTUNIDADELIST", "%|", false);
       		} else {
       			sqlWhereClause.addAndLikeCondition("tb.DSTABPRECOOPORTUNIDADELIST", produtoTabPreco.dsTabPrecoOportunidadeList, false);
       		}
       	}
       	if (LavenderePdaConfig.isUsaKitBaseadoNoProduto() && (ValueUtil.VALOR_SIM.equals(produtoTabPreco.flKit))) {
       		sqlWhereClause.addAndCondition("tb.flKit = ", produtoTabPreco.flKit);
       	}
       	if (LavenderePdaConfig.usaDestaqueItensVendidosMesCorrente && (ValueUtil.VALOR_SIM.equals(produtoTabPreco.flVendido))) {
       		String[] values = {ValueUtil.VALOR_SIM, OrigemPedido.FLORIGEMPEDIDO_PDA};
       		sqlWhereClause.addAndConditionOr("tb.flVendido = ", values);
       	}
       	if (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional()) {
       		sqlWhereClause.addAndCondition("tb.cdGrupoDescProd = ", produtoTabPreco.cdGrupoDescProd);
       	}
       	if (produtoTabPreco.forceCdFatorComumFilter) {
       		if (ValueUtil.isNotEmpty(produtoTabPreco.cdFatorComum)) {
       			sqlWhereClause.addAndConditionForced("tb.CDFATORCOMUM = ", produtoTabPreco.cdFatorComum);
       		} else {
       			sqlWhereClause.addStartAndMultipleCondition();
       			sqlWhereClause.addAndCondition("tb.CDFATORCOMUM is null");
       			sqlWhereClause.addOrConditionForced("tb.CDFATORCOMUM = ", produtoTabPreco.cdFatorComum);
       			sqlWhereClause.addEndMultipleCondition();
       		}
		}
       	if (LavenderePdaConfig.usaProdutoRestrito) {
       		Cliente cliente = SessionLavenderePda.getCliente();
       		if (cliente != null && !cliente.isPossuiDescontoExtraProdutoRestrito()) {
       			sqlWhereClause.addStartAndMultipleCondition();
	   			sqlWhereClause.addAndCondition("prod.FLPRODUTORESTRITO is null ");
	   			sqlWhereClause.addOrConditionForced("prod.FLPRODUTORESTRITO = ", "");
	   			sqlWhereClause.addOrCondition("prod.FLPRODUTORESTRITO = ", ValueUtil.VALOR_NAO);
	   			sqlWhereClause.addEndMultipleCondition();
       		}
       	}
       	if (LavenderePdaConfig.usaPedidoProdutoCritico && produtoTabPreco.filterToItemPedido) {
	        if (produtoTabPreco.isProdutoCritico()) {
	       		sqlWhereClause.addAndCondition("prod.FLCRITICO = ", produtoTabPreco.flCritico);
	       	} else if (LavenderePdaConfig.filtraProdutosPedidoNaoCritico){
	       		sqlWhereClause.addAndCondition("(prod.FLCRITICO = 'N' OR prod.FLCRITICO = '' OR prod.FLCRITICO IS NULL)");
	       	}
       	}
       	if ((LavenderePdaConfig.usaDescPromo || (LavenderePdaConfig.isConfigValorMinimoDescPromocional() && produtoTabPreco.filterToItemPedido)) && !produtoTabPreco.joinDescPromocional) {
       		sqlWhereClause.addAndCondition(DaoUtil.ALIAS_DESCPROMOCIONAL + ".CDPRODUTO IS NULL");
       	}
	    if (LavenderePdaConfig.usaDescProgressivoPersonalizado && produtoTabPreco.descProgressivoConfigFilter != null) {
		    sqlWhereClause.addAndCondition(DescProgressivoConfigDbxDao.getInstance().getSqlCdProdutoInDescProgressivoConfig("tb.CDPRODUTO", produtoTabPreco.descProgressivoConfigFilter, produtoTabPreco.descProgConfigFamFilter));
	    }
       	sqlWhereClause.addStartAndMultipleCondition();
       	boolean adicionouInicioBloco = false;
       	adicionouInicioBloco |= sqlWhereClause.addAndLikeCondition("tb.DSPRODUTO", toUpper(produtoTabPreco.dsProduto), false);
       	adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("tb.DSPRINCIPIOATIVO", produtoTabPreco.dsPrincipioAtivo, false);
       	if (LavenderePdaConfig.isMostraDescricaoReferencia()) {
       		adicionouInicioBloco |= sqlWhereClause.addOrCondition("tb.DSREFERENCIA = ", produtoTabPreco.dsReferencia);
       		adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("tb.DSREFERENCIA", produtoTabPreco.dsReferenciaLikeFilter, false);
       	}
       	adicionouInicioBloco |= sqlWhereClause.addOrCondition("tb.CDPRODUTO = ", produtoTabPreco.cdProduto);
       	adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("tb.CDPRODUTO", toUpper(produtoTabPreco.cdProdutoLikeFilter), false);
       	adicionouInicioBloco |= sqlWhereClause.addOrCondition("tb.NUCODIGOBARRAS = ", produtoTabPreco.nuCodigoBarras);
       	adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("tb.DSMARCA", produtoTabPreco.dsMarca, false);
       	if (LavenderePdaConfig.isUsaFiltroAplicacaoDoProduto()) {
       		adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("prod.DSAPLICACAOPRODUTO", produtoTabPreco.dsAplicacaoProduto, false);
       	}
       	
       	if (LavenderePdaConfig.usaFiltroCodigoAlternativoProduto) {
       		adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("prod.DSCODIGOALTERNATIVO", produtoTabPreco.dsCodigoAlternativo, false);
       	}
       	if (adicionouInicioBloco) {
   			sqlWhereClause.addEndMultipleCondition();
   		} else {
   			sqlWhereClause.removeStartMultipleCondition();
   		}
       	if (LavenderePdaConfig.apresentaFiltroDescQtd && produtoTabPreco.filtraProdutoDescQtd) {
       		Cliente cliente = SessionLavenderePda.getCliente();
       		StringBuffer sb = getSqlBuffer();
	    	sb.append("(EXISTS (SELECT 1 FROM TBLVPDESCQUANTIDADE DESC WHERE CDEMPRESA = TB.CDEMPRESA AND")
	    	.append(" CDREPRESENTANTE = TB.CDREPRESENTANTE AND ")
	    	.append(" CDPRODUTO = TB.CDPRODUTO ");
			if (LavenderePdaConfig.usaVigenciaDescQuantidade) {
				sb.append(" AND ").append(Sql.getValue(DateUtil.getCurrentDate())).append(" BETWEEN DTINICIALVIGENCIA AND DTFIMVIGENCIA ");
			}
			if (LavenderePdaConfig.isFiltraItemTabelaPrecoListaProduto() || (LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial && LavenderePdaConfig.usaGradeProduto1())) {
			    sb.append(" AND CDTABELAPRECO = ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".CDTABELAPRECO");
			}
			if (LavenderePdaConfig.permiteVincularCliente && cliente != null) {
				sb.append(" AND (descCliente.CDCLIENTE IS NOT NULL OR ")
				.append(" NOT EXISTS (SELECT 1 FROM TBLVPDESCQUANTIDADECLIENTE WHERE ")
				.append(" CDEMPRESA = desc.CDEMPRESA AND ")
				.append(" CDREPRESENTANTE = desc.CDREPRESENTANTE AND ")
				.append(" CDPRODUTO = desc.CDPRODUTO AND ")
				.append(" CDTABELAPRECO = desc.CDTABELAPRECO AND ")
				.append(" CDCLIENTE <> ").append(Sql.getValue(cliente.cdCliente)).append(")")
				.append(")");
			}
			sb.append(")");
			if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
		    	sb.append(" OR EXISTS (SELECT 1 FROM TBLVPDESCQTDEAGRSIMILAR dq JOIN ")
		    	.append(" TBLVPDESCQUANTIDADE d ON d.CDEMPRESA = dq.CDEMPRESA AND ")
		    	.append(" d.CDREPRESENTANTE = dq.CDREPRESENTANTE AND ")
		    	.append(" d.CDTABELAPRECO =  dq.CDTABELAPRECO AND ")
		    	.append(" d.CDPRODUTO = dq.CDPRODUTO ");
		    	if (cliente != null) {
			    	sb.append(" LEFT JOIN TBLVPDESCQUANTIDADECLIENTE descCliente ON ")
				    	.append(" dq.CDEMPRESA = descCliente.CDEMPRESA AND ")
				    	.append(" dq.CDREPRESENTANTE = descCliente.CDREPRESENTANTE AND ")
				    	.append(" descCliente.CDTABELAPRECO = dq.CDTABELAPRECO AND")
				    	.append(" d.CDPRODUTO = descCliente.CDPRODUTO AND ")
				    	.append(" descCliente.CDCLIENTE = ").append(Sql.getValue(cliente.cdCliente));
		    	}
		    	sb.append(" WHERE dq.CDEMPRESA = tb.CDEMPRESA AND ")
		    	.append(" dq.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
		    	.append(" dq.CDPRODUTOSIMILAR = tb.CDPRODUTO AND ")
		    	.append(Sql.getValue(DateUtil.getCurrentDate())).append(" BETWEEN DTINICIALVIGENCIA AND DTFIMVIGENCIA");
				if (LavenderePdaConfig.isFiltraItemTabelaPrecoListaProduto() || (LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial && LavenderePdaConfig.usaGradeProduto1())) {
				    sb.append(" AND dq.CDTABELAPRECO = ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".CDTABELAPRECO");
			    }
		    	if (LavenderePdaConfig.permiteVincularCliente && cliente != null) {
					sb.append(" AND (descCliente.CDCLIENTE IS NOT NULL OR ")
					.append(" NOT EXISTS (SELECT 1 FROM TBLVPDESCQUANTIDADECLIENTE WHERE ")
					.append(" CDEMPRESA = d.CDEMPRESA AND ")
					.append(" CDREPRESENTANTE = d.CDREPRESENTANTE AND ")
					.append(" CDPRODUTO = d.CDPRODUTO AND ")
					.append(" CDTABELAPRECO = d.CDTABELAPRECO AND ")
					.append(" CDCLIENTE <> ").append(Sql.getValue(cliente.cdCliente)).append(")")
					.append(")");
				}
		    	sb.append(")");
		    }
	    	sb.append(")");
	    	sqlWhereClause.addAndCondition(sb.toString());
	    }
       	if ((LavenderePdaConfig.apresentaMarcadorProdutoLista || LavenderePdaConfig.apresentaMarcadorProdutoInsercao) && ValueUtil.isNotEmpty(produtoTabPreco.cdMarcadores)) {
	        DaoUtil.addAndLikeOrConditionMarcadores(sqlWhereClause, produtoTabPreco.cdMarcadores);
	    }
	    if (ValueUtil.isNotEmpty(produtoTabPreco.cdStatusEstoque)) {
	    	addFilterStatusEstoque(sqlWhereClause, produtoTabPreco.cdStatusEstoque);
	    }
       	if (LavenderePdaConfig.filtraProdutoPorTipoPedido) {
       		DaoUtil.addAndFlExcecaoProdutoTipoPedCondition(produtoTabPreco, sqlWhereClause);
		}
       	if (produtoTabPreco.cestaPositProdutoFilter != null) {
       		sqlWhereClause.addAndCondition(DaoUtil.getCestaProdutoMetaNaoAtingidaCondition(produtoTabPreco.cestaPositProdutoFilter));
       	}
       	if (produtoTabPreco.cestaProdutoFilter != null) {
       		sqlWhereClause.addAndCondition(DaoUtil.getCestaProdutoCondition(produtoTabPreco.cestaProdutoFilter));
       	}
       	if (produtoTabPreco.grupoProdTipoPedFilter != null) {
       		sqlWhereClause.addAndCondition(DaoUtil.getExistsGrupoProdTipoPedProdutoListCondition(produtoTabPreco.grupoProdTipoPedFilter));
       	}
    	if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido() && produtoTabPreco.produtoUnidadeFilter != null) {
       		sqlWhereClause.addStartAndMultipleCondition();
       		sqlWhereClause.addAndConditionEquals("TB.CDUNIDADE", produtoTabPreco.produtoUnidadeFilter.cdUnidade);
       		sqlWhereClause.addOrCondition(DaoUtil.getExistsProdutoUnidadeCondition(produtoTabPreco.produtoUnidadeFilter));
       		sqlWhereClause.addEndMultipleCondition();
       	}
       	if (LavenderePdaConfig.usaFiltroComissao && produtoTabPreco.itemTabelaPreco != null) {
       		if (produtoTabPreco.itemTabelaPreco.filterByVlPctComissaoMinFilter) {
       			sqlWhereClause.addAndCondition(DaoUtil.ALIAS_ITEMTABELAPRECO + ".VLPCTCOMISSAO >= ", produtoTabPreco.itemTabelaPreco.vlPctComissaoMinFilter);
       		}
       		if (produtoTabPreco.itemTabelaPreco.filterByVlPctComissaoMaxFilter) {
       			sqlWhereClause.addAndCondition(DaoUtil.ALIAS_ITEMTABELAPRECO + ".VLPCTCOMISSAO <= ", produtoTabPreco.itemTabelaPreco.vlPctComissaoMaxFilter);
       		}
       	}
       	if (LavenderePdaConfig.filtraProdutoClienteRepresentante && produtoTabPreco.produtoClienteFilter != null) {
       		sqlWhereClause.addAndCondition(ProdutoClienteDbxDao.getInstance().getFlTipoRelacaoCteCondition(false));
       	}
       	if (LavenderePdaConfig.filtraClientePorProdutoRepresentante && produtoTabPreco.clienteProdutoFilter != null) {
       		sqlWhereClause.addAndCondition(ClienteProdutoDbxDao.getInstance().getFlTipoRelacaoCteCondition(false));
       	}
       	if (LavenderePdaConfig.usaFiltroProdutoCondicaoPagamentoRepresentante && produtoTabPreco.produtoCondPagtoFilter != null) {
       		sqlWhereClause.addAndCondition(ProdutoCondPagtoDbxDao.getInstance().getFlTipoRelacaoCteCondition(false));
       	}
       	if ((LavenderePdaConfig.usaCategoriaInsercaoItem() || LavenderePdaConfig.usaCategoriaMenuProdutos()) && produtoTabPreco.produtoMenuCategoriaFilter != null) {
       		sqlWhereClause.addAndCondition(DaoUtil.getExistsProdutoMenuCategoriaCondition(produtoTabPreco.produtoMenuCategoriaFilter));
       	}
       	if (LavenderePdaConfig.naoConsideraProdutoDescPromocionalComoPromocional && produtoTabPreco.excetoDescPromocional) {
       		sqlWhereClause.addAndCondition(DaoUtil.ALIAS_DESCPROMOCIONAL.concat(".CDEMPRESA IS NULL"));
       	}
       	if (LavenderePdaConfig.usaApenasItemPedidoOriginalNaBonificacaoTroca && produtoTabPreco.itemPedidoFilter != null) {
       		sqlWhereClause.addAndCondition(DaoUtil.getExistsItemPedidoOrItemPedidoErpCondition(produtoTabPreco.itemPedidoFilter, true, false));
       	}
       	if (LavenderePdaConfig.usaListaMultiplaInsercaoItensNoPedidoPorGiroProduto && produtoTabPreco.giroProdutoFilter != null) {
       		sqlWhereClause.addAndCondition(DaoUtil.getExistsGiroProdutoCondition(produtoTabPreco.giroProdutoFilter));
       	}
       	sql.append(sqlWhereClause.getSql());
	    if (LavenderePdaConfig.usaRestricaoVendaClienteProduto && SessionLavenderePda.getCliente() != null) {
		    DaoUtil.addNotExistsRestricaoProduto(sql, "TB.CDPRODUTO", SessionLavenderePda.getCliente().cdCliente, null,1, null, true, false, false, false);
	    }
    }
    
	private void addFilterStatusEstoque(SqlWhereClause sqlWhereClause, String cdStatusEstoque) {
		boolean usaMultiplicaCondicaoEstoquePrevisto = false;
		sqlWhereClause.addStartAndMultipleCondition();
		if (cdStatusEstoque.contains(EstoqueDisponivel.ESTOQUE_DISPONIVEL_COMBO_OPCAO_1) ) {
			sqlWhereClause.addOrCondition("ifnull("+DaoUtil.ALIAS_ESTOQUE_ERP+".QTESTOQUE, 0) - "+"ifnull("+DaoUtil.ALIAS_ESTOQUE_PDA+".QTESTOQUE,0) > 0");
			usaMultiplicaCondicaoEstoquePrevisto = true;
		}
		if (cdStatusEstoque.contains(EstoqueDisponivel.ESTOQUE_DISPONIVEL_COMBO_OPCAO_2)) {
			sqlWhereClause.addOrCondition("ifnull("+DaoUtil.ALIAS_ESTOQUE_ERP+".QTESTOQUE, 0) - "+"ifnull("+DaoUtil.ALIAS_ESTOQUE_PDA+".QTESTOQUE,0) <= 0");
			usaMultiplicaCondicaoEstoquePrevisto = true;
		} 
		if (cdStatusEstoque.contains(EstoqueDisponivel.ESTOQUE_DISPONIVEL_COMBO_OPCAO_3)) {
			if (usaMultiplicaCondicaoEstoquePrevisto) sqlWhereClause.addStartOrMultipleCondition();	
			sqlWhereClause.addAndCondition(DaoUtil.ALIAS_ESTOQUE_ERP+".QTESTOQUEPREVISTO > 0");
			sqlWhereClause.addAndCondition(DaoUtil.ALIAS_ESTOQUE_ERP+".DTESTOQUEPREVISTO IS NOT NULL");
			sqlWhereClause.addAndCondition(DaoUtil.ALIAS_ESTOQUE_ERP+".DTESTOQUEPREVISTO >= " , DateUtil.getCurrentDate());
			if (usaMultiplicaCondicaoEstoquePrevisto) sqlWhereClause.addEndMultipleCondition();
		}
		sqlWhereClause.addEndMultipleCondition();
	}

	@Override
    protected void addSelectGridColumns(StringBuffer sql, BaseDomain domain) {
    	sql.append(" ROWKEY,");
        sql.append(" CDPRODUTO,");
        sql.append(" DSPRODUTO,");
        sql.append(" DSPRINCIPIOATIVO");
        if (((ProdutoTabPreco)domain).filterToItemPedido) {
        	sql.append(", FLBONIFICACAO");
        	sql.append(", DSTABPRECOPROMOLIST");
        	sql.append(", DSTABPRECODESCPROMOCIONALLIST");
        	sql.append(", DSTABPRECOLIST");
        }
        if (LavenderePdaConfig.isMostraDescricaoReferencia()) {
        	sql.append(", DSREFERENCIA");
        }
        if (LavenderePdaConfig.usaAvisoPreAlta) {
        	sql.append(", FLPREALTACUSTO");
        }
        if (!ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.usaPrecoPadraoListaProdutos)) {
			sql.append(", VLPRECOPADRAO");
        }
        if (LavenderePdaConfig.apresentaMarcaNasListasNoPedido()) {
        	sql.append(", DSMARCA");
        }
    }

	@Override
    protected void addOrderByGrid(StringBuffer sql) {
    	sql.append(" order by ");
    	if (LavenderePdaConfig.usaOrdenacaoPorCodigoListaProdutos) {
    		sql.append("cdProduto");
    	} else {
    		sql.append("dsProduto");
    	}
    }

	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
		ProdutoBase produto = (ProdutoBase) domain;
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDPRODUTO,");
        sql.append(" DSTABPRECOLIST,");
        sql.append(" tb.CDGRUPOPRODUTO1,");
        sql.append(" tb.CDGRUPOPRODUTO2,");
        sql.append(" tb.CDGRUPOPRODUTO3,");
        sql.append(" tb.CDUNIDADE,");
        sql.append(" tb.NUCODIGOBARRAS");
        if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto() || LavenderePdaConfig.usaSugestaoVendaProdutoMesmoPrincipioAtivo) {
            sql.append(", tb.DSPRINCIPIOATIVO");
        }
        if (LavenderePdaConfig.isMostraProdutoPromocionalDestacadoTelaItemPedido()) {
        	sql.append(", tb.DSTABPRECOPROMOLIST");
        }
        if (LavenderePdaConfig.isMostraProdutoDescPromocionalDestacadoTelaItemPedido() || LavenderePdaConfig.naoConsideraProdutoDescPromocionalComoPromocional) {
        	sql.append(", tb.DSTABPRECODESCPROMOCIONALLIST");
        }
        if (LavenderePdaConfig.usaOportunidadeVenda) {
        	sql.append(", tb.DSTABPRECOOPORTUNIDADELIST");
        }
        if (LavenderePdaConfig.isPermiteBonificarProduto()) {
            sql.append(", tb.FLBONIFICACAO");
        }
        if (LavenderePdaConfig.usaFiltroFornecedor) {
        	sql.append(", tb.CDFORNECEDOR");
        }
        if (LavenderePdaConfig.usaAvisoPreAlta) {
        	sql.append(", tb.FLPREALTACUSTO");
        }
        if (LavenderePdaConfig.isMostraDescricaoReferencia() || LavenderePdaConfig.usaDsReferenciaNmFoto) {
        	sql.append(", tb.DSREFERENCIA");
        }
        if (LavenderePdaConfig.isMostraQtVendasProdutoNoPeriodo()) {
        	sql.append(", tb.QTVENDASPERIODO");
        }
        if (LavenderePdaConfig.filtraProdutosPorOrigemPedido) {
        	sql.append(", tb.CDORIGEMSETOR");
        }
        if (LavenderePdaConfig.isUsaGrupoDestaqueProduto()) {
			if (LavenderePdaConfig.usaGrupoDestaqueItemTabPreco) {
				sql.append(", ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".CDGRUPODESTAQUE");
			} else {
				sql.append(", tb.CDGRUPODESTAQUE");
			}
			sql.append(", ").append(DaoUtil.ALIAS_CORSISTEMA).append(".CDESQUEMACOR CDESQUEMACORPRODEST");
       		sql.append(", ").append(DaoUtil.ALIAS_CORSISTEMA).append(".CDCOR CDCORPRODEST");
       		sql.append(", ").append(DaoUtil.ALIAS_CORSISTEMA).append(".VLR VLRPRODEST");
       		sql.append(", ").append(DaoUtil.ALIAS_CORSISTEMA).append(".VLG VLGPRODEST");
       		sql.append(", ").append(DaoUtil.ALIAS_CORSISTEMA).append(".VLB VLBPRODEST");
        }
        if (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional()) {
        	sql.append(", tb.CDGRUPODESCPROD");
        }
        if (!ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.usaPrecoPadraoListaProdutos)) {
			sql.append(", tb.VLPRECOPADRAO");
		}
        if (LavenderePdaConfig.usaDestaqueItensVendidosMesCorrente) {
        	sql.append(", tb.FLVENDIDO");
        }
        if (LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito()) {
        	sql.append(", tb.FLSITUACAO");
        }
        if (LavenderePdaConfig.grifaProdutoComVidaUtilLoteCriticoNaLista) {
        	sql.append(", prod.FLLOTEPRODUTOCRITICO");
        }
        if (LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto) {
        	sql.append(", prod.CDUNIDADEFRACAO")
			.append(", prod.NUFRACAO")
			.append(", prod.FLUSAUNIDADEBASEDSFRACAO");
        }
        if (LavenderePdaConfig.usaProdutoRestrito ) {
        	sql.append(", prod.FLPRODUTORESTRITO");
        }
        if (usaCTETributacaoConfig()) {
        	sql.append(", prod.CDTRIBUTACAOPRODUTO");
        	if (produto.fromListItemPedido) {
        		sql.append(", ").append(DaoUtil.ALIAS_TRIBUTACAOCONFIG).append(".FLFRETEBASEIPI");
        		sql.append(", ").append(DaoUtil.ALIAS_TRIBUTACAOCONFIG).append(".FLTIPOCALCULOPISCOFINS");
        		sql.append(", ").append(DaoUtil.ALIAS_TRIBUTACAOCONFIG).append(".FLCALCULAIPI");
        	} else {
        		sql.append(", 0 AS ").append("FLFRETEBASEIPI");
        		sql.append(", 0 AS ").append("FLTIPOCALCULOPISCOFINS");
        		sql.append(", 0 AS ").append("FLCALCULAIPI");
        	}
        }
        if (LavenderePdaConfig.usaPedidoProdutoCritico) {
        	sql.append(", prod.FLCRITICO");
        }
        if (LavenderePdaConfig.apresentaMarcaNasListasNoPedido()) {
        	sql.append(", tb.DSMARCA");
        }
        
        if (LavenderePdaConfig.usaControleEstoquePorRemessa && LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoForaPedido()) {
        	DaoUtil.getSelectSumEstoquePorRemessa(sql, produto.cdRepresentante);
        } else if (LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoForaPedido() || LavenderePdaConfig.isMostraEstoquePrevistoNaListaProdutosForaPedido()) {
        	sql.append(", ((ifnull(").append(DaoUtil.ALIAS_ESTOQUE_ERP).append(".QTESTOQUE, 0)) - (ifnull(").append(DaoUtil.ALIAS_ESTOQUE_PDA).append(".QTESTOQUE, 0))) AS QTESTOQUE");
        } else if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() || LavenderePdaConfig.destacaProdutoQtMinEstoqueLista) {
        	sql.append(", ((ifnull(").append(DaoUtil.ALIAS_ESTOQUE_ERP).append(".QTESTOQUE, 0))) AS QTESTOQUE");
	        sql.append(", ((ifnull(").append(DaoUtil.ALIAS_ESTOQUE_ERP).append(".QTESTOQUEMIN, 0))) AS QTESTOQUEMIN");
        }
        if (LavenderePdaConfig.destacaProdutoQtMinEstoqueLista || LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido()) {
        	sql.append(", ((ifnull(").append(DaoUtil.ALIAS_ESTOQUE_ERP).append(".QTESTOQUEMIN, 0))) AS QTESTOQUEMIN");
        }
        if (LavenderePdaConfig.isMostraEstoquePrevistoNaListaProdutosForaPedido()) {
	        sql.append(", ").append(DaoUtil.ALIAS_ESTOQUE_ERP).append(".QTESTOQUEPREVISTO");      	
        }
        if (isAddJoinItemTabelaPreco(produto)) {
	        if (LavenderePdaConfig.isFiltraItemTabelaPrecoListaProduto() || LavenderePdaConfig.usaFiltroComissao || LavenderePdaConfig.mostraBonusListaProduto) {
	        	sql.append(", ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".CDTABELAPRECO");      	
	        	sql.append(", ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".VLUNITARIO");
	        	if (LavenderePdaConfig.mostraBonusListaProduto) {
	            	sql.append(", ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".VLBASE");
	        	}
	        	sql.append(", ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".CDITEMGRADE1");
	        	if (LavenderePdaConfig.usaFiltroComissao) {
	        		sql.append(", ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".VLPCTCOMISSAO");
	        	}
				if (LavenderePdaConfig.ignoraIndiceFinanceiroCondComercialProdutoPromocional) {
					sql.append(", ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".FLPROMOCAO");
				}
	        }
	        if (LavenderePdaConfig.destacaProdutoQuantidadeMaximaVenda) {
				sql.append(", ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".QTMAXVENDA");
			}
        }
        if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() && !produto.findListCombo) {
        	DaoUtil.getSelectSomaQtEstoqueNenhumaGrade(sql);
        }
        if (LavenderePdaConfig.grifaProdutoSemEstoqueEmUmaGradeNaLista && !produto.findListCombo) {
        	DaoUtil.getSelectEstoqueEmAlgumaGrade(sql);
        }
		if (LavenderePdaConfig.usaFotoProdutoPorEmpresa) {
			DaoUtil.getSelectNmFotoProdutoEmp(sql);
		} else if (LavenderePdaConfig.mostraFotoProduto || LavenderePdaConfig.isLoadImagesOnProdutoList()) {
			DaoUtil.getSelectNmFotoProduto(sql);
        }
        sql.append(", tb.DSPRODUTO");
        if (LavenderePdaConfig.showInformacoesComplementaresInsercMultipla()) {
        	sql.append(", ").append(DaoUtil.ALIAS_GIROPRODUTO).append(".VLMEDIOHISTORICO")
        	.append(", ").append(DaoUtil.ALIAS_GIROPRODUTO).append(".QTDMEDIAHISTORICO");
        }
		if (LavenderePdaConfig.exibeHistoricoEstoqueCliente || LavenderePdaConfig.exibeHistoricoQtdeVendaDoItem) {
			DaoUtil.getSelectHistoricoEstoqueItemPedido(sql, produto.cdClienteFilter);
			DaoUtil.getSelectHistoricoItemFisicoItemPedido(sql, produto.cdClienteFilter);
		}
		if (LavenderePdaConfig.isMostraCampoValorUnitarioEmbalagemListaProdutosDentroPedido()) {
			sql.append(", ").append(DaoUtil.ALIAS_PRODUTO).append(".VLST");
			sql.append(", ").append(DaoUtil.ALIAS_PRODUTO).append(".NUCONVERSAOUNIDADESMEDIDA");
		}
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			sql.append(", ").append(DaoUtil.ALIAS_PRODUTO).append(".CDUNIDADEPREFERENCIAL");
		}
		if (LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial) {
			sql.append(", ").append(DaoUtil.ALIAS_CONDCOMERCIALEXCEC).append(".CDEMPRESA AS CDEMPRESACONDCOMEXCEC");
			sql.append(", ").append(DaoUtil.ALIAS_CONDCOMERCIALEXCEC).append(".CDREPRESENTANTE AS CDREPRESENTANTECONDCOMEXCEC");
			sql.append(", ").append(DaoUtil.ALIAS_CONDCOMERCIALEXCEC).append(".CDPRODUTO AS CDPRODUTOCONDCOMEXCEC");
			sql.append(", ").append(DaoUtil.ALIAS_CONDCOMERCIALEXCEC).append(".CDCONDICAOCOMERCIAL AS CDCONDICAOCOMERCIALEXCEC");
			sql.append(", ").append(DaoUtil.ALIAS_CONDCOMERCIALEXCEC).append(".CDITEMGRADE1 AS CDITEMGRADE1CONDCOMEXCEC");
			sql.append(", ").append(DaoUtil.ALIAS_CONDCOMERCIALEXCEC).append(".VLUNITARIO AS VLUNITARIOCONDCOMEXCEC ");
			if (LavenderePdaConfig.usaGradeProduto1()) {
				sql.append(", ").append(DaoUtil.ALIAS_CONDCOMERCIALEXCEC).append(".CDITEMGRADE1 AS CDITEMGRADE1CONDCOMEXCEC");
			}
		}
		if (LavenderePdaConfig.mostraQtdPorEmbalagemProduto && LavenderePdaConfig.mostraPrecoUnidadeItem) {
			sql.append(", ").append(DaoUtil.ALIAS_PRODUTO).append(".NUCONVERSAOUNIDADESMEDIDA");
		}
		if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
			sql.append(", ").append(DaoUtil.ALIAS_PRODUTO).append(".CDFAMILIADESCPROG");
		}
		if (LavenderePdaConfig.usaFamiliaPadraoPoliticaComercial() || LavenderePdaConfig.isUsaPoliticaBonificacao()) {
			sql.append(", ").append(DaoUtil.ALIAS_PRODUTO).append(".CDFAMILIAPADRAO");
		}
		if (LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecial()) {
			sql.append(", ").append(DaoUtil.ALIAS_PRODUTO).append(".NUMULTIPLOESPECIAL ");
		}
		if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao || LavenderePdaConfig.apresentaMarcadorProdutoLista) {
			sql.append(", ").append(DaoUtil.ALIAS_MARCPROD1).append(".CDMARCADOR");
		}
		if (LavenderePdaConfig.isUsaFlIgnoraValidaco()) {
			sql.append(", ").append(DaoUtil.ALIAS_PRODUTO).append(".FLIGNORAVALIDACAO");
		}
		if (produto.produtoIndustriaFilter != null && LavenderePdaConfig.isInformaQtdSugeridaProdutoIndustria()) {
			sql.append(", ").append(DaoUtil.ALIAS_PRODUTO).append(".VLLITRO");
		}
		if (produto.contaProdutosListados) {
			sql.append(", COUNT(1) OVER() AS QTPRODUTOSLISTADOS");
		}
	}

	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		ProdutoBase produto = (ProdutoBase)domainFilter;
        ProdutoTabPreco produtoTabPreco = new ProdutoTabPreco();
        produtoTabPreco.cdEmpresa = rs.getString(1);
        produtoTabPreco.cdRepresentante = rs.getString(2);
        produtoTabPreco.cdProduto = rs.getString(3);
        produtoTabPreco.dsTabPrecoList = rs.getString(4);
        produtoTabPreco.cdGrupoProduto1 = rs.getString(5);
        produtoTabPreco.cdGrupoProduto2 = rs.getString(6);
        produtoTabPreco.cdGrupoProduto3 = rs.getString(7);
        produtoTabPreco.cdUnidade = rs.getString(8);
        produtoTabPreco.nuCodigoBarras = rs.getString(9);
        int i = 10;
        if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto() || LavenderePdaConfig.usaSugestaoVendaProdutoMesmoPrincipioAtivo) {
        	produtoTabPreco.dsPrincipioAtivo = rs.getString(i++);
        }
        if (LavenderePdaConfig.isMostraProdutoPromocionalDestacadoTelaItemPedido()) {
        	produtoTabPreco.dsTabPrecoPromoList = rs.getString(i++);
        }
        if (LavenderePdaConfig.isMostraProdutoDescPromocionalDestacadoTelaItemPedido() || LavenderePdaConfig.naoConsideraProdutoDescPromocionalComoPromocional) {
        	produtoTabPreco.dsTabPrecoDescPromocionalList = rs.getString(i++);
        }
        if (LavenderePdaConfig.usaOportunidadeVenda) {
        	produtoTabPreco.dsTabPrecoOportunidadeList = rs.getString(i++);
        }
        if (LavenderePdaConfig.isPermiteBonificarProduto()) {
        	produtoTabPreco.flBonificacao = rs.getString(i++);
        }
        if (LavenderePdaConfig.usaFiltroFornecedor) {
        	produtoTabPreco.cdFornecedor = rs.getString(i++);
        }
        if (LavenderePdaConfig.usaAvisoPreAlta) {
        	produtoTabPreco.flPreAltaCusto = rs.getString(i++);
        }
        if (LavenderePdaConfig.isMostraDescricaoReferencia() || LavenderePdaConfig.usaDsReferenciaNmFoto) {
        	produtoTabPreco.dsReferencia = rs.getString(i++);
        }
        if (LavenderePdaConfig.isMostraQtVendasProdutoNoPeriodo()) {
			produtoTabPreco.qtVendasPeriodo = rs.getInt(i++);
        }
        if (LavenderePdaConfig.filtraProdutosPorOrigemPedido) {
        	produtoTabPreco.cdOrigemSetor = rs.getString(i++);
        }
        if (LavenderePdaConfig.isUsaGrupoDestaqueProduto()) {
        	produtoTabPreco.cdGrupoDestaque = rs.getString(i++);
        	produtoTabPreco.produtoDestaque = new ProdutoDestaque();
        	produtoTabPreco.produtoDestaque.cdEmpresa = produtoTabPreco.cdEmpresa;
        	produtoTabPreco.produtoDestaque.cdRepresentante = produtoTabPreco.cdRepresentante;
        	produtoTabPreco.produtoDestaque.cdGrupoDestaque = produtoTabPreco.cdGrupoDestaque;
        	produtoTabPreco.produtoDestaque.corSistema = new CorSistema();
        	produtoTabPreco.produtoDestaque.corSistema.cdEsquemaCor = rs.getInt(i++);
        	produtoTabPreco.produtoDestaque.corSistema.cdCor = rs.getInt(i++);
        	produtoTabPreco.produtoDestaque.corSistema.vlR = rs.getInt(i++);
        	produtoTabPreco.produtoDestaque.corSistema.vlG = rs.getInt(i++);
        	produtoTabPreco.produtoDestaque.corSistema.vlB = rs.getInt(i++);
        }
        if (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional()) {
        	produtoTabPreco.cdGrupoDescProd = rs.getString(i++);
        }
        if (!ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.usaPrecoPadraoListaProdutos)) {
        	produtoTabPreco.vlPrecoPadrao = rs.getDouble(i++);
		}
        if (LavenderePdaConfig.usaDestaqueItensVendidosMesCorrente) {
        	produtoTabPreco.flVendido = rs.getString(i++);
        }
        if (LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito()) {
        	produtoTabPreco.flSituacao = rs.getInt(i++);
        }
        if (LavenderePdaConfig.grifaProdutoComVidaUtilLoteCriticoNaLista) {
        	produtoTabPreco.flLoteProdutoCritico = rs.getString(i++);
        }
        if (LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto) {
        	produtoTabPreco.cdUnidadeFracao = rs.getString(i++);
        	produtoTabPreco.nuFracao = rs.getInt(i++);
        	produtoTabPreco.flUsaUnidadeBaseDsFracao = rs.getString(i++);
        }
        if (LavenderePdaConfig.usaProdutoRestrito) {
        	produtoTabPreco.flProdutoRestrito = rs.getString(i++);
        }
        if (usaCTETributacaoConfig()) {
        	produtoTabPreco.cdTributacaoProduto = rs.getString(i++);
        	TributacaoConfig tribuConfRs = new TributacaoConfig();
        	tribuConfRs.flFreteBaseIpi = rs.getString(i++);
        	tribuConfRs.flTipoCalculoPisCofins = rs.getString(i++);
        	tribuConfRs.flCalculaIpi = rs.getString(i++);
        	produtoTabPreco.tributacaoConfig = tribuConfRs;
        	
        }
        if (LavenderePdaConfig.usaPedidoProdutoCritico) {
        	produtoTabPreco.flCritico = rs.getString(i++);
        }
        if (LavenderePdaConfig.apresentaMarcaNasListasNoPedido()) {
        	produtoTabPreco.dsMarca = rs.getString(i++);
        }
        if (LavenderePdaConfig.usaControleEstoquePorRemessa && LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoForaPedido()) {
			setInstanceEstoque(produtoTabPreco);
        	produtoTabPreco.estoque.qtEstoque = rs.getDouble(i++);
        } else if (LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoForaPedido() || LavenderePdaConfig.isMostraEstoquePrevistoNaListaProdutosForaPedido()) {
			setInstanceEstoque(produtoTabPreco);
			produtoTabPreco.estoque.qtEstoque = rs.getDouble(i++);
        } else if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() || LavenderePdaConfig.destacaProdutoQtMinEstoqueLista) {
			setInstanceEstoque(produtoTabPreco);
			produtoTabPreco.estoque.qtEstoque = rs.getDouble(i++);
        	produtoTabPreco.estoque.qtEstoqueMin = rs.getDouble(i++);
        }
		if (LavenderePdaConfig.destacaProdutoQtMinEstoqueLista || LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido()) {
			setInstanceEstoque(produtoTabPreco);
			produtoTabPreco.estoque.qtEstoqueMin = rs.getDouble(i++);
		}
        if (LavenderePdaConfig.isMostraEstoquePrevistoNaListaProdutosForaPedido()) {
			setInstanceEstoque(produtoTabPreco);
			produtoTabPreco.estoque.qtEstoquePrevisto = rs.getDouble(i++);
        }
        if (LavenderePdaConfig.isFiltraItemTabelaPrecoListaProduto() || LavenderePdaConfig.usaFiltroComissao || LavenderePdaConfig.mostraBonusListaProduto) {
	        setInstanceItemTabelaPreco(produtoTabPreco);
	        produtoTabPreco.itemTabelaPreco.cdTabelaPreco = rs.getString(i++);
        	produtoTabPreco.itemTabelaPreco.vlUnitario = rs.getDouble(i++);
        	if (LavenderePdaConfig.mostraBonusListaProduto) {
        		produtoTabPreco.itemTabelaPreco.vlBase = rs.getDouble(i++);
        	}
        	produtoTabPreco.itemTabelaPreco.cdItemGrade1 = rs.getString(i++);
        	if (LavenderePdaConfig.usaFiltroComissao) {
        		produtoTabPreco.itemTabelaPreco.vlPctComissao = rs.getDouble(i++);
        	}
			if (LavenderePdaConfig.ignoraIndiceFinanceiroCondComercialProdutoPromocional) {
				produtoTabPreco.itemTabelaPreco.flPromocao = rs.getString(i++);
			}
        }
        if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido()) {
        	produtoTabPreco.sumEstoqueGrades = rs.getDouble(i++);
        }
        if (LavenderePdaConfig.grifaProdutoSemEstoqueEmUmaGradeNaLista) {
        	produtoTabPreco.isAlgumaGradeSemEstoque = ValueUtil.VALOR_ZERO.equals(rs.getString(i++));
        }
        if (LavenderePdaConfig.mostraFotoProduto || LavenderePdaConfig.isLoadImagesOnProdutoList() || LavenderePdaConfig.usaFotoProdutoPorEmpresa) {
        	produtoTabPreco.fotoProduto = new FotoProduto();
        	produtoTabPreco.fotoProduto.nmFoto = rs.getString(i++);
        }
        produtoTabPreco.dsProduto = rs.getString(i++);
        if (LavenderePdaConfig.showInformacoesComplementaresInsercMultipla()) {
        	produtoTabPreco.qtdMediaHistorico = rs.getDouble(i++);
        	produtoTabPreco.vlMedioHistorico = rs.getDouble(i++);
        }
        if (LavenderePdaConfig.exibeHistoricoEstoqueCliente || LavenderePdaConfig.exibeHistoricoQtdeVendaDoItem) {
        	DaoUtil.populateHistoricoEstoqueCliente(rs.getString(i++), produtoTabPreco);
        	DaoUtil.populateHistoricoItemFisicoCliente(rs.getString(i++), produtoTabPreco);
        }
		if (LavenderePdaConfig.isMostraCampoValorUnitarioEmbalagemListaProdutosDentroPedido()) {
			produtoTabPreco.vlSt = rs.getDouble(i++);
			produtoTabPreco.nuConversaoUnidadesMedida = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			produtoTabPreco.cdUnidadePreferencial = rs.getString(i++);
		}
		if (LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial) {
			if (produtoTabPreco.itemTabelaPreco == null) {
				setInstanceItemTabelaPreco(produtoTabPreco);
			}
			produtoTabPreco.itemTabelaPreco.condComercialExcec = new CondComercialExcec();
			produtoTabPreco.itemTabelaPreco.condComercialExcec.cdEmpresa = rs.getString(i++);
			produtoTabPreco.itemTabelaPreco.condComercialExcec.cdRepresentante = rs.getString(i++);
			produtoTabPreco.itemTabelaPreco.condComercialExcec.cdProduto = rs.getString(i++);
			produtoTabPreco.itemTabelaPreco.condComercialExcec.cdCondicaoComercial = rs.getString(i++);
			produtoTabPreco.itemTabelaPreco.condComercialExcec.cdItemGrade1 = rs.getString(i++);
			produtoTabPreco.itemTabelaPreco.condComercialExcec.vlUnitario = rs.getDouble(i++);
			if (LavenderePdaConfig.usaGradeProduto1()) {
				produtoTabPreco.itemTabelaPreco.condComercialExcec.cdItemGrade1 = rs.getString(i++);
			}
		}
		if (LavenderePdaConfig.mostraQtdPorEmbalagemProduto && LavenderePdaConfig.mostraPrecoUnidadeItem) {
			produtoTabPreco.nuConversaoUnidadesMedida = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
			produtoTabPreco.cdFamiliaDescProg = rs.getString(i++);
		}
		if (LavenderePdaConfig.usaFamiliaPadraoPoliticaComercial() || LavenderePdaConfig.isUsaPoliticaBonificacao()) {
			produtoTabPreco.cdFamiliaPadrao = rs.getString(i++);
		}
		if (LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecial()) {
			produtoTabPreco.nuMultiploEspecialProduto = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao || LavenderePdaConfig.apresentaMarcadorProdutoLista) {
			populateMarcador(rs, produtoTabPreco);
			i++;
		}
		if (LavenderePdaConfig.isUsaFlIgnoraValidaco()) {
			produtoTabPreco.flIgnoraValidacao = rs.getString(i++);
		}
		if (produto.produtoIndustriaFilter != null && LavenderePdaConfig.isInformaQtdSugeridaProdutoIndustria()) {
			produtoTabPreco.vlLitro = rs.getDouble(i++);
			produtoTabPreco.produtoIndustriaSugestao = produto.produtoIndustriaFilter;
		}
		if (LavenderePdaConfig.destacaProdutoQuantidadeMaximaVenda) {
			if (produtoTabPreco.itemTabelaPreco == null) {
				setInstanceItemTabelaPreco(produtoTabPreco);
			}
			produtoTabPreco.itemTabelaPreco.qtMaxVenda = rs.getDouble(i++);
		}
		if (produto.contaProdutosListados) {
			produtoTabPreco.qtProdutosListados = rs.getInt(i++);
		}
        return produtoTabPreco;
	}

	private static void setInstanceItemTabelaPreco(ProdutoTabPreco produtoTabPreco) {
		produtoTabPreco.itemTabelaPreco = new ItemTabelaPreco();
		produtoTabPreco.itemTabelaPreco.cdEmpresa = produtoTabPreco.cdEmpresa;
		produtoTabPreco.itemTabelaPreco.cdRepresentante = produtoTabPreco.cdRepresentante;
		produtoTabPreco.itemTabelaPreco.cdProduto = produtoTabPreco.cdProduto;
		produtoTabPreco.itemTabelaPreco.cdPrazoPagtoPreco = LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo() ? 1 : 0;
	}

	private void setInstanceEstoque(ProdutoTabPreco produto) {
		if (produto.estoque == null) {
			produto.estoque = new Estoque();
		}
	}
	
	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		ProdutoTabPreco produtoTabPreco = (ProdutoTabPreco) domain;
		if (LavenderePdaConfig.usaDestaqueItensVendidosMesCorrente) {
			sql.append(" FLVENDIDO = ").append(Sql.getValue(produtoTabPreco.flVendido));
		}
	}
	
	@Override
	protected void addJoinSummary(BaseDomain domainFilter, StringBuffer sql) {
		ProdutoBase domain = (ProdutoBase) domainFilter;
		if (fazJoinProduto(domain)) {
			DaoUtil.addJoinProduto(sql, DaoUtil.ALIAS_PRODUTO, true);
		}
		if (LavenderePdaConfig.usaControleEstoquePorRemessa && LavenderePdaConfig.isMostraEstoquePrevistoNaListaProdutosForaPedido()) {
			DaoUtil.addLeftJoinEmpresa(sql, DaoUtil.ALIAS_ESTOQUE_ERP);
			DaoUtil.getSelectSumEstoquePorRemessa(sql, domain.cdRepresentante);
		} else if (LavenderePdaConfig.isUsaLocalEstoque() || LavenderePdaConfig.isUsaFiltroEstoqueDisponivel()) {
			DaoUtil.addJoinEstoque(sql, DaoUtil.ALIAS_ESTOQUE_ERP, domain.estoque != null ? domain.estoque.cdLocalEstoque : null, Estoque.FLORIGEMESTOQUE_ERP);
			DaoUtil.addJoinEstoque(sql, DaoUtil.ALIAS_ESTOQUE_PDA, domain.estoque != null ? domain.estoque.cdLocalEstoque : null, Estoque.FLORIGEMESTOQUE_PDA);
		} else if (LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoForaPedido() || LavenderePdaConfig.isMostraEstoquePrevistoNaListaProdutosForaPedido()) {
			DaoUtil.addJoinEstoque(sql, DaoUtil.ALIAS_ESTOQUE_ERP, Estoque.CD_LOCAL_ESTOQUE_PADRAO, Estoque.FLORIGEMESTOQUE_ERP);
			DaoUtil.addJoinEstoque(sql, DaoUtil.ALIAS_ESTOQUE_PDA, Estoque.CD_LOCAL_ESTOQUE_PADRAO, Estoque.FLORIGEMESTOQUE_PDA);
		}  else if (LavenderePdaConfig.isMostraEstoquePrevistoNaListaProdutosForaPedido() || LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() || LavenderePdaConfig.destacaProdutoQtMinEstoqueLista) {
			DaoUtil.addJoinEstoque(sql, DaoUtil.ALIAS_ESTOQUE_ERP, LavenderePdaConfig.usaLocalEstoquePorCliente() ? domain.estoque != null ? domain.estoque.cdLocalEstoque : null : Estoque.CD_LOCAL_ESTOQUE_PADRAO , Estoque.FLORIGEMESTOQUE_ERP);
		} 

        if (LavenderePdaConfig.usaFiltroAtributoProduto && ValueUtil.isNotEmpty(domain.cdAtributoProduto) && ValueUtil.isNotEmpty(domain.cdAtributoOpcaoProduto)) {
        	DaoUtil.addJoinProdutoAtributo(sql, DaoUtil.ALIAS_PRODUTOATRIBUTO, domain.cdAtributoProduto, domain.cdAtributoOpcaoProduto);
        }
        if (isAddJoinItemTabelaPreco(domain)) {
        	DaoUtil.addJoinItemTabelaPreco(sql, DaoUtil.ALIAS_ITEMTABELAPRECO, domain.itemTabelaPreco, domain.fromListProduto);
        }
        if (LavenderePdaConfig.filtraProdutoPorGrupoCliente && ValueUtil.isNotEmpty(domain.cdGrupoPermProd)) {
        	DaoUtil.addJoinGrupoCliPermProd(sql, DaoUtil.ALIAS_GRUPOPERMPROD, domain.cdGrupoPermProd);
        }
        if ((LavenderePdaConfig.isUsaKitProduto()) && (domain.cdKit != null)) {
        	DaoUtil.addJoinItemKit(sql, DaoUtil.ALIAS_ITEMKIT, domain.cdKit);
        }
        if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido()) {
        	DaoUtil.addJoinProdutoUnidade(sql, DaoUtil.ALIAS_PRODUTOUNIDADE);
        }
       	if (usaCTETributacaoConfig() && domain.fromListItemPedido) {
			DaoUtil.addJoinCTETributacaoConfig(sql, DaoUtil.ALIAS_TRIBUTACAOCONFIG, domain, true);
        }
        if (LavenderePdaConfig.showInformacoesComplementaresInsercMultipla()) {
        	DaoUtil.addJoinGiroProduto(sql, DaoUtil.ALIAS_GIROPRODUTO, domain.cdCliente);
        }
        
        if (domain.isFazJoinCTEDescPromocional()) {
	        DaoUtil.addJoinCteDescPromocional(sql, domain);
        }
        if (domain.filtraDescMaxProdCli) {
        	DaoUtil.addJoinDescMaxProdCli(sql, domain.cdCliente, DaoUtil.ALIAS_PRODUTO);
        }
        if (domain.filtraFornecedorRep) {
        	DaoUtil.addJoinFornecedor(sql);
        }
        if (LavenderePdaConfig.usaDescQuantidadePorPacote && domain.pacoteFilter != null) {
        	DaoUtil.addJoinPacote(sql, domain.pacoteFilter);
        }
        if (LavenderePdaConfig.isUsaFiltroProdutosPorCentroCusto() && LavenderePdaConfig.isUsaCentroCustoInformacoesAdicionais()) {
        	DaoUtil.addJoinCentroCustoProd(sql, domain);
        }
        if (LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda() && domain.cdPlataformaVendaFilter != null) {
        	DaoUtil.addJoinPlataformaVendaProduto(sql, domain.cdPlataformaVendaFilter);
        }
        if (LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial) {
        	DaoUtil.addJoinCondComercialExcec(sql, domain);
        }
        if (LavenderePdaConfig.usaFiltroLocalEstoqueListaProduto()) {
        	DaoUtil.addJoinLocalEstoque(sql, domain.estoque.cdLocalEstoque);
        }
		if (domain.produtoIndustriaFilter != null) {
			DaoUtil.addJoinProdutoIndustria(sql, domain.produtoIndustriaFilter);
		}
        if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao || LavenderePdaConfig.apresentaMarcadorProdutoLista) {
        	DaoUtil.addJoinMarcadores(sql, DaoUtil.ALIAS_MARCPROD1, domain.fromListProduto);
        }
		if (LavenderePdaConfig.filtraProdutoPorTipoPedido && ValueUtil.isNotEmpty(domain.cdTipoPedidoFilter)) {
			DaoUtil.addJoinProdutoTipoPedido(sql, domain);
		}
        if (domain.filtraProdutosInseridos) {
        	DaoUtil.addJoinItemPedido(sql, DaoUtil.ALIAS_ITEM_PEDIDO,  domain.nuPedidoFilter, domain.flOrigemPedidoFilter, domain.filtraProdutosInseridos);
        }
        if (LavenderePdaConfig.apresentaFiltroDescQtd && LavenderePdaConfig.permiteVincularCliente && domain.filtraProdutoDescQtd) {
        	DaoUtil.addJoinDescQtdCliente(sql, domain.itemTabelaPreco.cdTabelaPreco);
        }
        if (LavenderePdaConfig.permiteFiltroCestaEmConjuntoOutrosFiltros()&& ValueUtil.isNotEmpty(domain.cdCesta)) {
        	DaoUtil.addJoinCestaProduto(sql, domain.cdCesta);
        }
		if (LavenderePdaConfig.isUsaGrupoDestaqueProduto()) {
			DaoUtil.addJoinProdutoDestaque(sql, LavenderePdaConfig.usaGrupoDestaqueItemTabPreco ? DaoUtil.ALIAS_ITEMTABELAPRECO : DaoUtil.ALIAS_PRODUTO);
			DaoUtil.addJoinCorSistema(sql);
		}
		if (LavenderePdaConfig.filtraProdutoClienteRepresentante && domain.produtoClienteFilter != null) {
			DaoUtil.addJoinCTEProdutoCliente(domain.produtoClienteFilter, sql);
		}
		if (LavenderePdaConfig.filtraClientePorProdutoRepresentante && domain.clienteProdutoFilter != null) {
			DaoUtil.addJoinCTEClienteProduto(domain.clienteProdutoFilter, sql);
		}
		if (LavenderePdaConfig.usaFiltroProdutoCondicaoPagamentoRepresentante && domain.produtoCondPagtoFilter != null) {
			DaoUtil.addJoinCTEProdutoCondPagto(domain.produtoCondPagtoFilter, sql);
		}
	}

	private static boolean isAddJoinItemTabelaPreco(BaseDomain domain) {
		ProdutoTabPreco produtoTabPreco = (ProdutoTabPreco) domain;
		return (LavenderePdaConfig.isFiltraItemTabelaPrecoListaProduto() 
				|| (LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial && LavenderePdaConfig.usaGradeProduto1()) 
				|| LavenderePdaConfig.usaFiltroComissao 
				|| LavenderePdaConfig.mostraBonusListaProduto
				|| LavenderePdaConfig.destacaProdutoQuantidadeMaximaVenda) 
				&& produtoTabPreco.itemTabelaPreco != null;
	}

	private boolean fazJoinProduto(ProdutoBase domain) {
		return ValueUtil.isNotEmpty(domain.dsAplicacaoProduto) || LavenderePdaConfig.usaDescProgressivoPersonalizado || LavenderePdaConfig.usaFamiliaPadraoPoliticaComercial()
				|| LavenderePdaConfig.usaFiltroCodigoAlternativoProduto ||  LavenderePdaConfig.grifaProdutoComVidaUtilLoteCriticoNaLista
				|| (LavenderePdaConfig.mostraQtdPorEmbalagemProduto && LavenderePdaConfig.mostraPrecoUnidadeItem)
				|| LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto || LavenderePdaConfig.usaPedidoProdutoCritico || LavenderePdaConfig.usaProdutoRestrito
				|| LavenderePdaConfig.isMostraCampoValorUnitarioEmbalagemListaProdutosDentroPedido()
				|| LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda()
				|| ((LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido()
						&& (LavenderePdaConfig.permiteAlterarValorItemComIPI) || LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado))
				|| domain.filtraDescMaxProdCli || LavenderePdaConfig.usaUnidadeAlternativa
				|| LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecial()
				|| LavenderePdaConfig.isUsaFlIgnoraValidaco()
				|| (LavenderePdaConfig.isInformaQtdSugeridaProdutoIndustria() && domain.produtoIndustriaFilter != null)
				|| LavenderePdaConfig.isUsaGrupoDestaqueProduto();
	}

	@Override
    public Vector findAllByExampleSummary(BaseDomain domain) throws SQLException {
        StringBuffer sql = getSqlBuffer();
        addCTESummary(domain, sql);
        sql.append(" select distinct ");
        addSelectSummaryColumns(domain, sql);
        sql.append(" from ");
        sql.append(tableName);
        sql.append(" tb ");
        addJoinSummary(domain, sql);
        addWhereByExample(domain, sql);
        addGroupBySummary(domain, sql);
	    addOrderBySummary(domain, sql);
	    addLimit(sql, domain);
	    addOffSet(sql, domain);
	    return findAllSummary(domain, sql.toString());
    }

	protected void addOrderBySummary(BaseDomain domain, StringBuffer sql) {
		if (LavenderePdaConfig.usaPriorizacaoPesquisaItemPedido) {
		    addOrderByPriorizaPesquisaItemPedido(sql, domain);
	    } else if (LavenderePdaConfig.isMostraOrdenacaoRelevanciaProduto()) {
	    	addCustomOrderByAlias(sql, domain, DaoUtil.ALIAS_PRODUTO);
	    } else {
			switch (StringUtil.getStringValue(domain.sortAtributte)) {
			case ProdutoBase.SORT_COLUMN_VLPCTCOMISSAO:
				if (isAddJoinItemTabelaPreco(domain)) {
					addCustomOrderByAlias(sql, domain, DaoUtil.ALIAS_ITEMTABELAPRECO);
					return;
				}
				break;
			case ProdutoBase.SORT_COLUMN_CDPRODUTO:
				if (LavenderePdaConfig.usaOrdemNumericaColunaCodigoProduto) {
					sql.append(" order by CAST(tb.CDPRODUTO AS INT)").append(ValueUtil.getBooleanValue(domain.sortAsc) ? " ASC" : " DESC");
					return;
				}
				break;
			default:
				break;
			}
			addOrderBy(sql, domain);
	    }
	}
    
    @Override
    protected void addGroupBySummary(BaseDomain domain, StringBuffer sql) {
    	super.addGroupBySummary(domain, sql);
    	ProdutoBase produto = (ProdutoBase) domain;
    	if (produto.joinDescPromocional || produto.joinCondComercialExcec) {
    		sql.append(" GROUP BY TB.CDEMPRESA, TB.CDREPRESENTANTE, TB.CDPRODUTO  ");
    	}
    }
    
    private String toUpper(String value) {
    	return value != null ? value.toUpperCase() : value;
    }

	protected void addOrderByPriorizaPesquisaItemPedido(StringBuffer sql, BaseDomain domain) {
		if (domain == null) return;
		ProdutoTabPreco produtoTabPreco = (ProdutoTabPreco) domain;
		sql.append(" order by ");
		if (produtoTabPreco.dsPrincipioAtivo != null) {
			String dsPrincipioAtivo = produtoTabPreco.dsPrincipioAtivo.replaceAll("%", "");
			sql.append("(CASE WHEN tb.dsPrincipioAtivo = '").append(dsPrincipioAtivo).append("' THEN 1 ")
					.append("WHEN tb.dsPrincipioAtivo LIKE '").append(dsPrincipioAtivo).append("%'").append("THEN 2 ")
					.append("ELSE 3 END)");
		} else if (produtoTabPreco.dsAplicacaoProduto != null) {
			String dsAplicacaoProduto = produtoTabPreco.dsAplicacaoProduto.replaceAll("%", "");
			sql.append("(CASE WHEN prod.dsAplicacaoProduto = '").append(dsAplicacaoProduto).append("' THEN 1 ")
					.append("WHEN prod.dsAplicacaoProduto LIKE '").append(dsAplicacaoProduto).append("%'").append("THEN 2 ")
					.append("ELSE 3 END)");
		} else {
			String dsFilter = ValueUtil.isEmpty(produtoTabPreco.dsProduto) ? "" : produtoTabPreco.dsProduto.replaceAll("%", "");
			if (ValueUtil.isEmpty(dsFilter) && produtoTabPreco.cdProduto != null)
				dsFilter = produtoTabPreco.cdProduto.replaceAll("%", "");
			sql.append("(CASE WHEN tb.dsProduto = '").append(dsFilter).append("' THEN 1")
					.append(" WHEN tb.cdProduto = '").append(dsFilter).append("' THEN 2")
					.append(" WHEN tb.dsProduto LIKE '").append(dsFilter).append("%' THEN 3")
					.append(" WHEN tb.cdProduto LIKE '").append(dsFilter).append("%' THEN 4")
					.append(" ELSE 5 END)");
		}
	}

    @Override
    protected Vector findAllSummary(BaseDomain domainFilter, String sql) throws SQLException {
    	if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao || LavenderePdaConfig.apresentaMarcadorProdutoLista) {
    		int queryCount = 0;
    		try (Statement st = getCurrentDriver().getStatement();
    				ResultSet rs = st.executeQuery(sql)) {
    			// Instanciado tamanho com numero primo, para diminuir colisoes no hashMap
    			Map<String, BaseDomain> mapResult = new HashMap<String, BaseDomain>(53);
    			Vector result = new Vector(50);
    			while (rs.next()) {
    				BaseDomain domain;
    				String cdProduto = rs.getString(3);
    				if ((domain = mapResult.get(cdProduto)) == null) {
    					domain = populateSummary(domainFilter, rs);
    					mapResult.put(cdProduto, domain);
    					result.addElement(domain);
    				} else {
    					populateMarcador(rs, domain);
    				}
    				if (++queryCount % 500 == 0) {
    					VmUtil.executeGarbageCollector();
    					queryCount = 0;	
    				}
    			}
    			return result;
    		}
    	}
    	return super.findAllSummary(domainFilter, sql);
    }
    
    private void populateMarcador(ResultSet rs, BaseDomain domain) throws SQLException {
    	ProdutoTabPreco produto = (ProdutoTabPreco)domain;
	    if (produto.cdMarcadores == null) produto.cdMarcadores = new Vector();
	    String cdMarcador = rs.getString("CDMARCADOR");
	    if (ValueUtil.isEmpty(cdMarcador)) return;
	    String[] listMarcadores = cdMarcador.split(",");
	    if (ValueUtil.isEmpty(listMarcadores)) return;
	    for (String cdMarcadorList : listMarcadores) {
		    produto.cdMarcadores.addElement(cdMarcadorList.replace("\"", ""));
	    }
    }
    
    @Override
    protected void addCTESummary(BaseDomain domainFilter, StringBuffer sql) {
    	ProdutoBase produto = (ProdutoBase) domainFilter;
		if (produto.isFazJoinCTEDescPromocional()) {
			ProdutoBase produtoClone = (ProdutoBase) produto.clone();
			produtoClone.cdLocalFilter = ValueUtil.isEmpty(produtoClone.cdLocalFilter) ? ValueUtil.VALOR_ZERO : produtoClone.cdLocalFilter;
			if (LavenderePdaConfig.isConfigValorMinimoDescPromocional()) {
				produtoClone.cdCliente = produtoClone.cdClienteVlMinDescPromoFilter;
				produtoClone.cdTabelaPreco = produtoClone.cdTabelaPrecoVlMinDescPromoFilter;
			} else {
				if (ValueUtil.isEmpty(produtoClone.cdTabelaPreco) && produtoClone.itemTabelaPreco != null && ValueUtil.isNotEmpty(produtoClone.itemTabelaPreco.cdTabelaPreco)) {
					produtoClone.cdTabelaPreco = produtoClone.itemTabelaPreco.cdTabelaPreco;
				}
			}
			DaoUtil.addCTEDescPromocional(produtoClone, sql);
		}
		if (usaCTETributacaoConfig() && produto.fromListItemPedido) {
			DaoUtil.addCTETributacaoConfig(sql, produto);
		}
		if (LavenderePdaConfig.filtraProdutoClienteRepresentante && produto.produtoClienteFilter != null) {
			DaoUtil.addCTEsProdutoCliente(sql, produto.produtoClienteFilter);
		}
		if (LavenderePdaConfig.filtraClientePorProdutoRepresentante && produto.clienteProdutoFilter != null) {
			DaoUtil.addCTEsClienteProduto(sql, produto.clienteProdutoFilter);
		}
		if (LavenderePdaConfig.usaFiltroProdutoCondicaoPagamentoRepresentante && produto.produtoCondPagtoFilter != null) {
			DaoUtil.addCTEsProdutoCondPagto(sql, produto.produtoCondPagtoFilter);
		}
    }
    
	private boolean usaCTETributacaoConfig() {
		return LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() && (LavenderePdaConfig.permiteAlterarValorItemComIPI || LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado);
	}
	
	public int countByExampleFullSQL(BaseDomain domain) throws SQLException {
		StringBuffer sql = new StringBuffer();
		addCTESummary(domain, sql);
		sql.append("select count(*) as qtde from ");
		sql.append(tableName);
		sql.append(" tb ");
		addJoinSummary(domain, sql);
		addWhereByExample(domain, sql);
		return getInt(sql.toString());
	}
    
}
