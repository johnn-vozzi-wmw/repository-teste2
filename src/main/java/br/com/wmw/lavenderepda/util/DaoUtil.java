package br.com.wmw.lavenderepda.util;

import java.sql.SQLException;

import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AgendaVisita;
import br.com.wmw.lavenderepda.business.domain.CestaPositProduto;
import br.com.wmw.lavenderepda.business.domain.CestaProduto;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ClienteProduto;
import br.com.wmw.lavenderepda.business.domain.ClienteSeg;
import br.com.wmw.lavenderepda.business.domain.CondComCliente;
import br.com.wmw.lavenderepda.business.domain.CondComCondPagto;
import br.com.wmw.lavenderepda.business.domain.CondComSegCli;
import br.com.wmw.lavenderepda.business.domain.CondPagtoCli;
import br.com.wmw.lavenderepda.business.domain.CondPagtoRep;
import br.com.wmw.lavenderepda.business.domain.CondPagtoSeg;
import br.com.wmw.lavenderepda.business.domain.CondPagtoTabPreco;
import br.com.wmw.lavenderepda.business.domain.CondTipoPagto;
import br.com.wmw.lavenderepda.business.domain.CondTipoPedido;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.GiroProduto;
import br.com.wmw.lavenderepda.business.domain.GrupoProdForn;
import br.com.wmw.lavenderepda.business.domain.GrupoProdTipoPed;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto1;
import br.com.wmw.lavenderepda.business.domain.ItemComboAgrSimilar;
import br.com.wmw.lavenderepda.business.domain.ItemKitAgrSimilar;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Kit;
import br.com.wmw.lavenderepda.business.domain.Pacote;
import br.com.wmw.lavenderepda.business.domain.PermissaoSolAut;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoCliente;
import br.com.wmw.lavenderepda.business.domain.ProdutoCondPagto;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.service.StatusPedidoPdaService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.StatusPedidoPdaPdbxDao;
import br.com.wmw.lavenderepda.business.domain.ProdutoIndustria;
import br.com.wmw.lavenderepda.business.domain.ProdutoMenuCategoria;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.domain.Regiao;
import br.com.wmw.lavenderepda.business.domain.StatusOrcamento;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.domain.TabPrecTipoPedido;
import br.com.wmw.lavenderepda.business.domain.TabelaPrecoCli;
import br.com.wmw.lavenderepda.business.domain.TabelaPrecoReg;
import br.com.wmw.lavenderepda.business.domain.TabelaPrecoRep;
import br.com.wmw.lavenderepda.business.domain.TabelaPrecoSeg;
import br.com.wmw.lavenderepda.business.domain.TipoCondPagtoCli;
import br.com.wmw.lavenderepda.business.domain.TipoFrete;
import br.com.wmw.lavenderepda.business.domain.TipoFreteCli;
import br.com.wmw.lavenderepda.business.domain.TipoPagamento;
import br.com.wmw.lavenderepda.business.domain.TipoPagtoCli;
import br.com.wmw.lavenderepda.business.domain.TipoPagtoTabPreco;
import br.com.wmw.lavenderepda.business.domain.TipoPedidoCli;
import br.com.wmw.lavenderepda.business.domain.TranspTipoPed;
import br.com.wmw.lavenderepda.business.domain.Transportadora;
import br.com.wmw.lavenderepda.business.domain.TributacaoConfig;
import br.com.wmw.lavenderepda.business.domain.Unidade;
import br.com.wmw.lavenderepda.business.service.RestricaoService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ClienteProdutoDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoClienteDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoCondPagtoDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RegiaoDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TributacaoConfigDao;
import totalcross.util.Vector;

public class DaoUtil {
	
	public static final int TIPO_COMPARACAO_PRODUTO_NA = 0;
	public static final int TIPO_COMPARACAO_PRODUTO_CDPRODUTO = 1;
	public static final int TIPO_COMPARACAO_PRODUTO_CDGRUPODESCPROD = 2;

	public static final String ALIAS_EMPRESA = "EMP";
	public static final String ALIAS_PRODUTO = "PROD";
	public static final String ALIAS_ESTOQUE_ERP = "ESTOQUEERP";
	public static final String ALIAS_ESTOQUE_PDA = "ESTOQUEPDA";
	public static final String ALIAS_PRODUTOATRIBUTO = "PRODATRIBUTO";
	public static final String ALIAS_ITEMTABELAPRECO = "ITPRECO";
	public static final String ALIAS_GRUPOPERMPROD = "GRPPRMPRD";
	public static final String ALIAS_CESTAPOSITPRODUTO = "CSTPOSPRD";
	public static final String ALIAS_ITEMKIT = "ITKIT";
	public static final String ALIAS_DESCPROMOCIONAL = "DESCPROMO";
	public static final String ALIAS_PRODUTOUNIDADE = "PRODUN";
	public static final String ALIAS_TRIBUTACAOCONFIG = "TRIBCONF";
	public static final String ALIAS_ESTOQUEREMESSA = "RE";
	public static final String ALIAS_CLIENTE = "CLI";
	public static final String ALIAS_GIROPRODUTO = "GIRO";
	public static final String ALIAS_DESCMAXPRODCLI = "DESCPRODCLI";
	public static final String ALIAS_REDECLIENTE = "REDE";
	public static final String ALIAS_STATUSPEDIDOPDA = "SPPDA";
	public static final String ALIAS_TIPOFRETE = "TPFRETE";
	public static final String ALIAS_FORNECEDOR = "FRN";
	public static final String ALIAS_FORNECEDORREP = "FREP";
	public static final String ALIAS_DESCONTOPACOTE = "DESCONTOPACOTE";
	public static final String ALIAS_CENTROCUSTOPROD = "CCPROD";
	public static final String ALIAS_PLATAFORMAVENDAPRODUTO = "PVP";
	public static final String ALIAS_PLATAFORMAVENDACLIENTE = "PVC";
	public static final String ALIAS_STATUSORCAMENTO = "SO";
	public static final String ALIAS_CONDCOMERCIALEXCEC = "CCE";
	public static final String ALIAS_LOCALESTOQUE = "LCE";
	public static final String ALIAS_TIPOPAGAMETO = "TIPOPAGAMENTO";
	public static final String ALIAS_PRODUTOINDUSTRIA = "PRODUTOINDUSTRIA";
	public static final String ALIAS_TIPOPEDIDO = "TIPOPED";
	public static final String ALIAS_CATEGORIA = "CATEG";
	public static final String ALIAS_REDE = "REDE";
	public static final String ALIAS_MARCPROD1 = "MARCPROD1";
	public static final String ALIAS_MARCPROD2 = "MARCPROD2";
	public static final String ALIAS_PRODUTOTIPOPED = "PRODTIPOPED";
	public static final String ALIAS_ITEM_PEDIDO = "ITP";
	public static final String ALIAS_COLECAO = "COL";
	public static final String ALIAS_COLECAO_STATUS = "COLSTATUS";
	public static final String ALIAS_ITEMCOMBOAGRSIMILAR = "ITEMCOMBOAGRSIMILAR";
	public static final String ALIAS_ITEMKITAGRSIMILAR = "ITEMKITAGRSIMILAR";
	public static final String ALIAS_VISITA = "VIS";
	public static final String ALIAS_BONIFCFG = "BCFG";
	public static final String ALIAS_BONIFCFGCATEGORIA = "BCFGCAT";
	public static final String ALIAS_BONIFCFGLINHA = "BCFGLIN";
	public static final String ALIAS_BONIFCFGREPRESENTANTE = "BCFGREP";
	public static final String ALIAS_BONIFCFGFAMILIA = "BCFGFAM";
	public static final String ALIAS_INSUMOPRODUTO = "INSUMO";
	public static final String ALIAS_BONIFCFGBRINDE = "BCFGBRIN";
	public static final String ALIAS_BONIFCFGPROD = "BCFGPRD";
	public static final String ALIAS_BONIFCFGCONJUNTO = "BCFGCON";
	public static final String ALIAS_BONIFCFGCLIENTE = "BCFGCLI";
	public static final String ALIAS_ITEMPEDIDOBONIFCFG = "ITBCFG";
	public static final String ALIAS_STATUSITEMPEDIDO = "STATUSITEMPED";
	public static final String ALIAS_CTETRIBUTACAOCONFIG = "CTETRIBCFG";
	public static final String ALIAS_ITEMPEDIDOERPDIF = "ITEMDIF";
	public static final String ALIAS_PRODUTODESTAQUE = "PRODDEST";
	public static final String ALIAS_CORSISTEMA = "CORSIS";
	public static final String ALIAS_CONDICAOPAGAMENTO = "CONDPAGTO";
	public static final String ALIAS_TIPOFRETECLI = "TFCLI";
	public static final String ALIAS_CTEPRODUTOCLIENTE = "CTEPRODCLI";
	public static final String ALIAS_CTEPROCLIFLTIPORELACAO = "CTEPRODCLITIPORELACAO";
	public static final String ALIAS_CTECLIENTEPRODUTO = "CTECLIPROD";
	public static final String ALIAS_CTECLIPROFLTIPORELACAO = "CTECLIPRODTIPORELACAO";
	public static final String ALIAS_CTEPRODCONDPAGTO = "CTEPRODCONDPAGTO";
	public static final String ALIAS_CTEPRODCONDPAGTOTIPORELACAO = "CTEPRODCONDPAGTOTIPORELACAO";
	public static final String ALIAS_REGIAO = "REG";
	public static final String ALIAS_UNIDADE = "UNI";

	public static void addLeftJoinEmpresa(StringBuffer sql, String aliasTable) {
		sql.append(" LEFT JOIN TBLVPEMPRESA ").append(ALIAS_EMPRESA);
		sql.append(" ON ").append(aliasTable).append(".CDEMPRESA = ").append(ALIAS_EMPRESA).append(".CDEMPRESA");
	}
	
	public static void addJoinEmpresa(StringBuffer sql, String aliasTable) {
		sql.append(" JOIN TBLVPEMPRESA ").append(aliasTable);
		sql.append(" ON ").append(aliasTable).append(".CDEMPRESA = ").append("TB.CDEMPRESA");
	}
	
	public static void addJoinRepresentante(StringBuffer sql, String aliasTable) {
		sql.append(" JOIN TBLVPREPRESENTANTE ").append(aliasTable);
		sql.append(" ON ").append(aliasTable).append(".CDREPRESENTANTE = ").append("TB.CDREPRESENTANTE");
	}

	public static void addJoinProduto(StringBuffer sql, String aliasTable, boolean isLeft) {
		if (isLeft) {
			sql.append(" LEFT ");
		}
		sql.append(" JOIN TBLVPPRODUTO ").append(aliasTable).append(" ON ");
		sql.append(aliasTable).append(".CDEMPRESA = tb.CDEMPRESA AND ").append(aliasTable).append(".CDREPRESENTANTE = tb.CDREPRESENTANTE AND ").append(aliasTable).append(".CDPRODUTO = tb.CDPRODUTO ");
	}
	
	public static void addJoinEstoque(StringBuffer sql, String aliasTable, String cdLocalEstoque, String flOrigemEstoque) {
		addJoinEstoque(sql, aliasTable, cdLocalEstoque, flOrigemEstoque, "TB");
	}
	
	public static void addJoinEstoque(StringBuffer sql, String aliasTable, String cdLocalEstoque, String flOrigemEstoque, String aliasTable2) {
		sql.append(" LEFT JOIN TBLVPESTOQUE ").append(aliasTable);
		sql.append(" ON ").append(aliasTable).append(".CDEMPRESA = ").append(aliasTable2).append(".CDEMPRESA");
		sql.append(" AND ").append(aliasTable).append(".CDREPRESENTANTE = ").append(aliasTable2).append(".CDREPRESENTANTE");
		sql.append(" AND ").append(aliasTable).append(".CDPRODUTO = ").append(aliasTable2).append(".CDPRODUTO");
		addGradeFilter(sql, aliasTable);
		if (ValueUtil.isNotEmpty(cdLocalEstoque)) {
			sql.append(" AND ").append(aliasTable).append(".CDLOCALESTOQUE = ").append(Sql.getValue(cdLocalEstoque));
		}
		sql.append(" AND ").append(aliasTable).append(".FLORIGEMESTOQUE = ").append(Sql.getValue(flOrigemEstoque));
		if (flOrigemEstoque.equals(Estoque.FLORIGEMESTOQUE_ERP) && LavenderePdaConfig.usaControleEstoquePorRemessa) {
			sql.append(" AND EXISTS(SELECT 1 FROM TBLVPREMESSAESTOQUE ").append(ALIAS_ESTOQUEREMESSA);
			sql.append(" WHERE ").append(ALIAS_ESTOQUE_ERP).append(".CDEMPRESA = ").append(ALIAS_ESTOQUEREMESSA).append(".CDEMPRESA");
			sql.append(" AND ").append(ALIAS_ESTOQUE_ERP).append(".CDREPRESENTANTE = ").append(ALIAS_ESTOQUEREMESSA).append(".CDREPRESENTANTE");
			sql.append(" AND ").append(ALIAS_ESTOQUE_ERP).append(".CDLOCALESTOQUE = ").append(ALIAS_ESTOQUEREMESSA).append(".CDLOCALESTOQUE");
			sql.append(" AND ").append(ALIAS_ESTOQUEREMESSA).append(".FLESTOQUELIBERADO = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
			sql.append(" LIMIT 1)");
		}
	}
	
	public static void addJoinCliente(StringBuffer sql) {
		addJoinCliente(sql, null);
	}

	public static void addJoinCliente(StringBuffer sql, Cliente clienteFilter) {
		sql.append(" JOIN TBLVPCLIENTE ").append(ALIAS_CLIENTE).append(" ON ");
		sql.append(ALIAS_CLIENTE).append(".CDEMPRESA = tb.CDEMPRESA AND ");
		sql.append(ALIAS_CLIENTE).append(".CDREPRESENTANTE = tb.CDREPRESENTANTE AND ");
		sql.append(ALIAS_CLIENTE).append(".CDCLIENTE = tb.CDCLIENTE ");
		
		if (clienteFilter != null && clienteFilter.includeExtraFilterJoin) {
			if (ValueUtil.isNotEmpty(clienteFilter.cdRede)) {
				sql.append(" AND ").append(ALIAS_CLIENTE).append(".CDREDE = ").append(Sql.getValue(clienteFilter.cdRede));		
			}
			if (ValueUtil.isNotEmpty(clienteFilter.cdCategoria)) {
				sql.append(" AND ").append(ALIAS_CLIENTE).append(".CDCATEGORIA = ").append(Sql.getValue(clienteFilter.cdCategoria));		
			}
			if (ValueUtil.isNotEmpty(clienteFilter.flTipoClienteRede)) {
		    	if (clienteFilter.isTipoClienteIndividual()) {
		    		sql.append(" AND COALESCE(").append(ALIAS_CLIENTE).append(".CDREDE, '') = ''"); 
		    	} else {
		    		sql.append(" AND COALESCE(").append(ALIAS_CLIENTE).append(".CDREDE, '') != ''");
		    	}
		    }
		}
	}

	public static void addLeftJoinCliente(StringBuffer sql) {
		sql.append(" LEFT ");
		addJoinCliente(sql);
	}

	private static void addJoinEstoqueRemessaEstoque(StringBuffer sql, String cdRepresentante) {
		sql.append(" JOIN TBLVPREMESSAESTOQUE ").append(ALIAS_ESTOQUEREMESSA);
		sql.append(" ON ").append(ALIAS_ESTOQUE_ERP).append(".CDEMPRESA = ").append(ALIAS_ESTOQUEREMESSA).append(".CDEMPRESA");
		sql.append(" AND ").append(ALIAS_ESTOQUE_ERP).append(".CDREPRESENTANTE = ").append(cdRepresentante);
		sql.append(" AND ").append(ALIAS_ESTOQUE_ERP).append(".CDLOCALESTOQUE = ").append(ALIAS_ESTOQUEREMESSA).append(".CDLOCALESTOQUE");
		sql.append(" AND ").append(ALIAS_ESTOQUEREMESSA).append(".FLESTOQUELIBERADO = 'S' ");
		sql.append(" AND (").append(ALIAS_ESTOQUEREMESSA).append(".FLFINALIZADA != 'S' OR ") .append(ALIAS_ESTOQUEREMESSA).append(".FLFINALIZADA IS NULL ) ");
		sql.append(" AND ifnull(").append(ALIAS_EMPRESA).append(".CDLOCALESTOQUE, '0') ").append(" <> ").append(ALIAS_ESTOQUEREMESSA).append(".CDLOCALESTOQUE ");
	}
	
	private static void addGradeFilter(StringBuffer sql, String aliasTable) {
		sql.append(" AND ").append(aliasTable).append(".CDITEMGRADE1 = ").append(Sql.getValue(ProdutoGrade.CD_ITEM_GRADE_PADRAO));
		sql.append(" AND ").append(aliasTable).append(".CDITEMGRADE2 = ").append(Sql.getValue(ProdutoGrade.CD_ITEM_GRADE_PADRAO));
		sql.append(" AND ").append(aliasTable).append(".CDITEMGRADE3 = ").append(Sql.getValue(ProdutoGrade.CD_ITEM_GRADE_PADRAO));
	}
	
	public static void addJoinProdutoAtributo(StringBuffer sql, String aliasTable, String cdAtributoProduto, String cdAtributoOpcaoProduto) {
		sql.append(" JOIN TBLVPPRODUTOATRIBUTO ").append(aliasTable);
		sql.append(" ON ").append(aliasTable).append(".CDEMPRESA = TB.CDEMPRESA");
		sql.append(" AND ").append(aliasTable).append(".CDREPRESENTANTE = TB.CDREPRESENTANTE");
		sql.append(" AND ").append(aliasTable).append(".CDPRODUTO = TB.CDPRODUTO");
		sql.append(" AND ").append(aliasTable).append(".CDATRIBUTOPRODUTO = ").append(Sql.getValue(cdAtributoProduto));
		sql.append(" AND ").append(aliasTable).append(".CDATRIBUTOOPCAOPRODUTO = ").append(Sql.getValue(cdAtributoOpcaoProduto));
	}
	
	public static void addJoinItemTabelaPreco(StringBuffer sql, String aliasTable, ItemTabelaPreco itemTabelaPreco, boolean fromListProduto) {
		addJoinItemTabelaPreco(sql, aliasTable, "TB", itemTabelaPreco, fromListProduto, false);
	}
	
	public static void addJoinItemTabelaPreco(StringBuffer sql, String aliasTable, String aliasProduto, ItemTabelaPreco itemTabelaPreco, boolean fromListProduto, boolean includeGradeFilter) {
		if (!LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco() && LavenderePdaConfig.ocultaTabelaPrecoPedido
				&& !LavenderePdaConfig.permiteTabPrecoItemDiferentePedido() && !LavenderePdaConfig.usaListaColunaPorTabelaPreco) {
			sql.append(" LEFT OUTER ");
		}
		if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco() && fromListProduto) {
			sql.append(" LEFT ");
		}
		sql.append(" JOIN TBLVPITEMTABELAPRECO ").append(aliasTable);
		sql.append(" ON ");
		addComparacaoChaveItemTabPreco(sql, aliasTable, aliasProduto, itemTabelaPreco);
		if (includeGradeFilter) {
			addGradeFilter(sql, aliasTable);
		}
	}

	private static void addComparacaoChaveItemTabPreco(StringBuffer sql, String aliasTable, String aliasProduto, ItemTabelaPreco itemTabelaPreco) {
		sql.append(aliasTable).append(".CDEMPRESA = ").append(aliasProduto).append(".CDEMPRESA");
		sql.append(" AND ").append(aliasTable).append(".CDREPRESENTANTE = ").append(aliasProduto).append(".CDREPRESENTANTE");
		sql.append(" AND ").append(aliasTable).append(".CDPRODUTO = ").append(aliasProduto).append(".CDPRODUTO");
		if (ValueUtil.isNotEmpty(itemTabelaPreco.cdUf)){
			sql.append(" AND ").append(aliasTable).append(".CDUF = ").append(Sql.getValue(itemTabelaPreco.cdUf));
		}
		if (ValueUtil.isNotEmpty(itemTabelaPreco.cdTabelaPreco)) {
			sql.append(" AND ").append(aliasTable).append(".CDTABELAPRECO = ").append(Sql.getValue(itemTabelaPreco.cdTabelaPreco));
		}
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
			sql.append(" AND (").append(aliasTable).append(".CDUNIDADE = IFNULL(").append(aliasProduto).append(".CDUNIDADE, '0')").append(" OR ").append(aliasTable).append(".CDUNIDADE = '0')");
			sql.append(" AND ").append(aliasTable).append(".CDPRAZOPAGTOPRECO = ").append(Sql.getValue(itemTabelaPreco.cdPrazoPagtoPreco));
			sql.append(" AND ").append(aliasTable).append(".QTITEM = ").append("(").append(getSQLMinQtItemFromItemTabelaPreco()).append(")");
		} else {
			sql.append(" AND ").append(aliasTable).append(".CDUNIDADE = '0'");
			sql.append(" AND ").append(aliasTable).append(".CDPRAZOPAGTOPRECO = '0'");
			sql.append(" AND ").append(aliasTable).append(".QTITEM = '0'");
		}
	}
	
	public static void addJoinGrupoCliPermProd(StringBuffer sql, String aliasTable, String cdGrupoPermProd) {
		sql.append(" JOIN TBLVPGRUPOCLIPERMPROD ").append(aliasTable);
		sql.append(" ON ").append(aliasTable).append(".CDGRUPOCLIENTE = ").append(Sql.getValue(cdGrupoPermProd));
		sql.append(" AND ").append(aliasTable).append(".CDPRODUTO = TB.CDPRODUTO");
	}
	
	public static void addJoinItemKit(StringBuffer sql, String aliasTable, String cdKit) {
		sql.append(" JOIN TBLVPITEMKIT ").append(aliasTable);
		sql.append(" ON ").append(aliasTable).append(".CDEMPRESA = TB.CDEMPRESA");
		sql.append(" AND ").append(aliasTable).append(".CDREPRESENTANTE = TB.CDREPRESENTANTE");
		sql.append(" AND ").append(aliasTable).append(".CDPRODUTO = TB.CDPRODUTO"); 
		sql.append(" AND ").append(aliasTable).append(".CDKIT = ").append(Sql.getValue(cdKit));
	}
	
	public static void addJoinProdutoUnidade(StringBuffer sql, String aliasTable) {
		sql.append(" LEFT JOIN TBLVPPRODUTOUNIDADE ").append(aliasTable);
		sql.append(" ON ").append(aliasTable).append(".CDEMPRESA = TB.CDEMPRESA");
		sql.append(" AND ").append(aliasTable).append(".CDREPRESENTANTE = TB.CDREPRESENTANTE");
		sql.append(" AND ").append(aliasTable).append(".CDUNIDADE = TB.CDUNIDADE");
		sql.append(" AND ").append(aliasTable).append(".CDPRODUTO = TB.CDPRODUTO");
	}
	
	public static void addJoinCTETributacaoConfig(StringBuffer sql, String aliasTable, ProdutoBase produto, boolean isProdutoTabPreco) {
		sql.append(" LEFT JOIN (");
		sql.append("SELECT ");
		TributacaoConfigDao.getInstance().addSelectColumns(sql, aliasTable);
		sql.append(" FROM (");
		TributacaoConfigDao.getInstance().addJoinTributacaoConfigByPrioridadeProdutoTabPreco(sql, 1, ValueUtil.VALOR_ZERO, produto.cdClienteFilter, produto.cdProduto);
		sql.append(" UNION ");
		TributacaoConfigDao.getInstance().addJoinTributacaoConfigByPrioridadeProdutoTabPreco(sql, 2, ValueUtil.VALOR_ZERO, produto.cdClienteFilter, ValueUtil.VALOR_ZERO);
		sql.append(" UNION ");
		TributacaoConfigDao.getInstance().addJoinTributacaoConfigByPrioridadeProdutoTabPreco(sql, 3, produto.cdTributacaoClienteFilter, ValueUtil.VALOR_ZERO, produto.cdProduto);
		sql.append(" UNION ");
		TributacaoConfigDao.getInstance().addJoinTributacaoConfigByPrioridadeProdutoTabPreco(sql, 4, produto.cdTributacaoClienteFilter2, ValueUtil.VALOR_ZERO, produto.cdProduto);
		sql.append(" UNION ");
		TributacaoConfigDao.getInstance().addJoinTributacaoConfigByPrioridadeProdutoTabPreco(sql, 5, produto.cdTributacaoClienteFilter, ValueUtil.VALOR_ZERO, ValueUtil.VALOR_ZERO);
		sql.append(" UNION ");
		TributacaoConfigDao.getInstance().addJoinTributacaoConfigByPrioridadeProdutoTabPreco(sql, 6, produto.cdTributacaoClienteFilter2, ValueUtil.VALOR_ZERO, ValueUtil.VALOR_ZERO);
		sql.append(" UNION ");
		TributacaoConfigDao.getInstance().addJoinTributacaoConfigByPrioridadeProdutoTabPreco(sql, 7, ValueUtil.VALOR_ZERO, produto.cdClienteFilter, ValueUtil.VALOR_ZERO);
		sql.append(" UNION ");
		TributacaoConfigDao.getInstance().addJoinTributacaoConfigByPrioridadeProdutoTabPreco(sql, 8, ValueUtil.VALOR_ZERO, ValueUtil.VALOR_ZERO, produto.cdProduto);
		sql.append(" UNION ");
		TributacaoConfigDao.getInstance().addJoinTributacaoConfigByPrioridadeProdutoTabPreco(sql, 9, produto.cdTributacaoClienteFilter, ValueUtil.VALOR_ZERO, ValueUtil.VALOR_ZERO);
		sql.append(" UNION ");
		TributacaoConfigDao.getInstance().addJoinTributacaoConfigByPrioridadeProdutoTabPreco(sql, 10, produto.cdTributacaoClienteFilter2, ValueUtil.VALOR_ZERO, ValueUtil.VALOR_ZERO);
		sql.append(" UNION ");
		TributacaoConfigDao.getInstance().addJoinTributacaoConfigByPrioridadeProdutoTabPreco(sql, 11, ValueUtil.VALOR_ZERO, ValueUtil.VALOR_ZERO, ValueUtil.VALOR_ZERO);
		sql.append(" UNION ");
		TributacaoConfigDao.getInstance().addJoinTributacaoConfigByPrioridadeProdutoTabPreco(sql, 12, ValueUtil.VALOR_ZERO, ValueUtil.VALOR_ZERO, ValueUtil.VALOR_ZERO);
		sql.append(") ").append(aliasTable).append(" ORDER BY PRIORIDADE ASC LIMIT 1").append(")").append(" as ").append(aliasTable).append(" ON ");
		String aliasPrincipal = isProdutoTabPreco ? DaoUtil.ALIAS_PRODUTO : " TB";
		sql.append(aliasPrincipal).append(".CDTRIBUTACAOPRODUTO = ").append(aliasTable).append(".CDTRIBUTACAOPRODUTO ")
		.append(" AND ").append(aliasPrincipal).append(".CDEMPRESA = ").append(aliasTable).append(".CDEMPRESA")
		.append(" AND ").append(aliasPrincipal).append(".CDREPRESENTANTE = ").append(aliasTable).append(".CDREPRESENTANTE");
	}
	
	public static void addJoinGiroProduto(StringBuffer sql, String aliasTable, String cdCliente) {
		sql.append(" LEFT JOIN TBLVPGIROPRODUTO ").append(aliasTable).append(" ON ")
		.append(aliasTable).append(".CDEMPRESA = ").append("TB.CDEMPRESA")
		.append(" AND ").append(aliasTable).append(".CDREPRESENTANTE = ").append("TB.CDREPRESENTANTE")
		.append(" AND ").append(aliasTable).append(".CDCLIENTE = ").append(Sql.getValue(cdCliente))
		.append(" AND ").append(aliasTable).append(".CDPRODUTO = ").append("TB.CDPRODUTO")
		.append(" AND ").append(aliasTable).append(".CDITEMGRADE1 = ").append(ValueUtil.VALOR_ZERO)
		.append(" AND ").append(aliasTable).append(".CDITEMGRADE2 = ").append(ValueUtil.VALOR_ZERO)
		.append(" AND ").append(aliasTable).append(".CDITEMGRADE3 = ").append(ValueUtil.VALOR_ZERO);
	}
	
	public static void getSelectNmFotoProduto(StringBuffer sql) {
		sql.append(", (SELECT NMFOTO FROM TBLVPFOTOPRODUTO FOTOPROD WHERE FOTOPROD.CDPRODUTO = TB.CDPRODUTO AND FOTOPROD.CDUSUARIO = TB.CDUSUARIO LIMIT 1) AS NMFOTO");
	}
	
	public static void getSelectNmFotoProdutoEmp(StringBuffer sql) {
		sql.append(", (SELECT NMFOTO FROM TBLVPFOTOPRODUTOEMP FOTOPROD WHERE FOTOPROD.CDEMPRESA = TB.CDEMPRESA AND FOTOPROD.CDPRODUTO = TB.CDPRODUTO AND FOTOPROD.CDUSUARIO = TB.CDUSUARIO LIMIT 1) AS NMFOTO");
	}
	
	public static void getSelectEstoqueEmAlgumaGrade(StringBuffer sql) {
		getSelectEstoqueEmAlgumaGrade(sql, null);
	}

	public static void getSelectEstoqueEmAlgumaGrade(StringBuffer sql, ProdutoBase produto) {
		sql.append(",(SELECT E.QTESTOQUE FROM TBLVPESTOQUE E");
		sql.append(" WHERE E.CDEMPRESA = TB.CDEMPRESA");
		if (produto != null) {
			sql.append(" AND E.CDLOCALESTOQUE = ").append(Sql.getValue(produto.estoque.cdLocalEstoque));
		}
		sql.append(" AND E.CDREPRESENTANTE = TB.CDREPRESENTANTE AND E.CDPRODUTO = TB.CDPRODUTO AND (COALESCE(E.QTESTOQUE, 0) = 0  OR IFNULL(E.QTESTOQUE, -1))) QTESTOQUE_ALGUMA_GRADE");
	}
	
	
	public static void getSelectSomaQtEstoqueNenhumaGrade(StringBuffer sql) {
		getSelectSomaQtEstoqueNenhumaGrade(sql, null);
	}
	
	public static void getSelectSomaQtEstoqueNenhumaGrade(StringBuffer sql, ProdutoBase produto) {
		sql.append(",(SELECT SUM(E.QTESTOQUE) FROM TBLVPESTOQUE E");
		sql.append(" WHERE E.CDEMPRESA = TB.CDEMPRESA");
		if (produto != null) {
			sql.append(" AND E.CDLOCALESTOQUE = ").append(Sql.getValue(produto.estoque.cdLocalEstoque));
		}
		sql.append(" AND E.CDREPRESENTANTE = TB.CDREPRESENTANTE AND E.CDPRODUTO = TB.CDPRODUTO) QTESTOQUE_NENHUMA_GRADE");
	}
	
	public static void getSelectSumEstoquePorRemessa(StringBuffer sql, String cdRepresentante) {
		sql.append(", ( SELECT SUM(case when florigemestoque = 'E' then ").append(ALIAS_ESTOQUE_ERP).append(".QTESTOQUE else - ").append(ALIAS_ESTOQUE_ERP).append(".QTESTOQUE end) FROM TBLVPESTOQUE ").append(ALIAS_ESTOQUE_ERP);
		addLeftJoinEmpresa(sql, ALIAS_ESTOQUE_ERP);
		addJoinEstoqueRemessaEstoque(sql, cdRepresentante);
		sql.append(" WHERE ").append(ALIAS_ESTOQUE_ERP).append(".CDPRODUTO = TB.CDPRODUTO) QTESTOQUEREMESSA");
	}
	
    public static void getSelectHistoricoItemFisicoItemPedido(StringBuffer sql, String cdClienteFilter) {
    	sql.append(", (SELECT GROUP_CONCAT(QTITEMFISICO) FROM (")
    		.append(" SELECT IPERP.QTITEMFISICO, PED.DTEMISSAO, PED.HREMISSAO")
    		.append(" FROM TBLVPITEMPEDIDOERP IPERP")
			.append(" JOIN TBLVPPEDIDOERP PED")
			.append(" ON PED.CDEMPRESA = IPERP.CDEMPRESA")
			.append(" AND PED.CDREPRESENTANTE = IPERP.CDREPRESENTANTE")
			.append(" AND PED.NUPEDIDO = IPERP.NUPEDIDO")
			.append(" AND PED.FLORIGEMPEDIDO = IPERP.FLORIGEMPEDIDO")
			.append(" AND IPERP.CDPRODUTO = TB.CDPRODUTO")
			.append(" WHERE PED.CDCLIENTE = ").append(Sql.getValue(cdClienteFilter))
			.append(" UNION")
			.append(" SELECT A.QTITEMFISICO, PED.DTEMISSAO, PED.HREMISSAO")
			.append(" FROM TBLVPITEMPEDIDO A")
			.append(" JOIN TBLVPPEDIDO PED")
			.append(" ON PED.CDEMPRESA = A.CDEMPRESA")
			.append(" AND PED.CDREPRESENTANTE = A.CDREPRESENTANTE")
			.append(" AND PED.NUPEDIDO = A.NUPEDIDO")
			.append(" AND PED.FLORIGEMPEDIDO = A.FLORIGEMPEDIDO")
			.append(" AND PED.CDSTATUSPEDIDO <> ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoFechado))
			.append(" AND A.CDPRODUTO = TB.CDPRODUTO")
			.append(" WHERE PED.CDCLIENTE = ").append(Sql.getValue(cdClienteFilter))
			.append(" ORDER BY DTEMISSAO DESC, HREMISSAO DESC LIMIT 2")
			.append(" )) AS QTITEMFISICO");
    }
    
    public static void getSelectHistoricoEstoqueItemPedido(StringBuffer sql, String cdClienteFilter) {
    	sql.append(", (SELECT GROUP_CONCAT(QTESTOQUECLIENTE) from(")
			.append(" SELECT ITBERP.QTESTOQUECLIENTE, PED.DTEMISSAO, PED.HREMISSAO")
			.append(" FROM TBLVPITEMPEDIDOERP ITBERP")
			.append(" JOIN TBLVPPEDIDOERP PED")
			.append(" ON PED.CDEMPRESA = ITBERP.CDEMPRESA")
			.append(" AND PED.CDREPRESENTANTE = ITBERP.CDREPRESENTANTE")
			.append(" AND PED.NUPEDIDO = ITBERP.NUPEDIDO")
			.append(" AND PED.FLORIGEMPEDIDO = ITBERP.FLORIGEMPEDIDO")
			.append(" AND ITBERP.CDPRODUTO = TB.CDPRODUTO")
		    .append(" WHERE PED.CDCLIENTE = ").append(Sql.getValue(cdClienteFilter))
			.append(" UNION")
			.append(" SELECT ITB.QTESTOQUECLIENTE, PED.DTEMISSAO, PED.HREMISSAO")
			.append(" FROM TBLVPITEMPEDIDO ITB")
			.append(" JOIN TBLVPPEDIDO PED")
			.append(" ON PED.CDEMPRESA = ITB.CDEMPRESA")
			.append(" AND PED.CDREPRESENTANTE = ITB.CDREPRESENTANTE")
			.append(" AND PED.NUPEDIDO = ITB.NUPEDIDO")
			.append(" AND PED.FLORIGEMPEDIDO = ITB.FLORIGEMPEDIDO")
			.append(" AND ITB.CDPRODUTO = TB.CDPRODUTO")
			.append(" AND PED.CDSTATUSPEDIDO <> ").append(LavenderePdaConfig.cdStatusPedidoFechado)
		    .append(" WHERE PED.CDCLIENTE = ").append(Sql.getValue(cdClienteFilter))
			.append(" ORDER BY DTEMISSAO DESC, HREMISSAO DESC limit 2")
			.append(" )) AS QTESTOQUECLIENTE");
    }
    
    public static void addJoinCteDescPromocional(StringBuffer sql, ProdutoBase produto) {
    	boolean isLeftJoin = produto.excetoDescPromocional;
    	isLeftJoin |= (LavenderePdaConfig.usaDescPromo || LavenderePdaConfig.isConfigValorMinimoDescPromocional() &&  produto.filterToItemPedido) && !produto.joinDescPromocional;
    	if (isLeftJoin) {
    		sql.append(" LEFT ");
    	}
		sql.append(" JOIN ");
		sql.append(ALIAS_DESCPROMOCIONAL).append(" ON TB.CDEMPRESA = ").append(ALIAS_DESCPROMOCIONAL).append(".CDEMPRESA AND TB.CDREPRESENTANTE = ")
    		.append(ALIAS_DESCPROMOCIONAL).append(".CDREPRESENTANTE ")
		    .append(" AND (TB.CDPRODUTO = ").append(ALIAS_DESCPROMOCIONAL).append(".CDPRODUTO")
		    .append(" OR (DESCPROMO.CDPRODUTO = '0' AND TB.CDGRUPODESCPROD = ").append(ALIAS_DESCPROMOCIONAL).append(".CDGRUPODESCPROD )")
		    .append(")");
    	
    }
    
    private static void selectRegrasDescPromocional(StringBuffer sql, String cdCliente, String cdGrupoDescCli,
                                                    String cdGrupoDescProd, String cdTabelaPreco, String cdCondicaoComercial,
                                                    String cdLocal, int tipoComparacaoProduto) {
    	if (cdTabelaPreco == null || cdCondicaoComercial == null) return;
    	
    	SqlWhereClause sqlWhere = new SqlWhereClause();
    	sql.append(" SELECT CDEMPRESA, CDREPRESENTANTE, CDPRODUTO, CDGRUPODESCPROD, NUPROMOCAO")
    		.append(" FROM TBLVPDESCPROMOCIONAL DESCPR ");
		if (tipoComparacaoProduto == TIPO_COMPARACAO_PRODUTO_NA) {
			sqlWhere.addAndConditionEquals("DESCPR.CDPRODUTO", ValueUtil.VALOR_ZERO);
		}
		if (tipoComparacaoProduto != TIPO_COMPARACAO_PRODUTO_CDGRUPODESCPROD) {
			sqlWhere.addAndConditionEquals("DESCPR.CDGRUPODESCPROD", cdGrupoDescProd);
		}
		sqlWhere.addAndConditionEquals("DESCPR.CDGRUPODESCCLI", cdGrupoDescCli);
		sqlWhere.addAndConditionEquals("DESCPR.CDCLIENTE", cdCliente);
		sqlWhere.addAndConditionEquals("DESCPR.CDCONDICAOCOMERCIAL", cdCondicaoComercial);
		sqlWhere.addAndConditionEquals("DESCPR.CDTABELAPRECO", cdTabelaPreco);
		sqlWhere.addAndConditionEquals("DESCPR.CDLOCAL", cdLocal);
		sqlWhere.addAndCondition("DATE('now', 'localtime') BETWEEN DESCPR.DTINICIAL AND DESCPR.DTFINAL");
    	if (!LavenderePdaConfig.usaDescontoQtdeNoGrupoDescPromocional) {
    		sqlWhere.addAndConditionEquals("DESCPR.QTITEM", 0);
    	}
    	sql.append(sqlWhere.getSql());
    	sql.append(" UNION ALL ");
    }
    
    public static void addJoinDescMaxProdCli(StringBuffer sql, String cdCliente, String alias) {
    	sql.append(" JOIN TBLVPDESCMAXPRODCLI ").append(ALIAS_DESCMAXPRODCLI).append(" ON ")
    	.append(alias).append(".CDEMPRESA = ").append(ALIAS_DESCMAXPRODCLI).append(".CDEMPRESA AND ")
    	.append(alias).append(".CDREPRESENTANTE = ").append(ALIAS_DESCMAXPRODCLI).append(".CDREPRESENTANTE AND ")
    	.append(alias).append(".CDGRUPODESCMAXPROD = ").append(ALIAS_DESCMAXPRODCLI).append(".CDGRUPODESCMAXPROD AND ")
    	.append(ALIAS_DESCMAXPRODCLI).append(".CDCLIENTE = ").append(Sql.getValue(cdCliente));
    }
    
	public static void populateHistoricoItemFisicoCliente(String qtItemFisico, ProdutoBase produto) throws SQLException {
		if (ValueUtil.isNotEmpty(qtItemFisico)) {
			String[] qtsItensFisicosHistorico = qtItemFisico.split(",");
			produto.qtItemFisicoHistorico1 = ValueUtil.getDoubleValue(qtsItensFisicosHistorico[0]);
			if (qtsItensFisicosHistorico.length > 1) {
				produto.qtItemFisicoHistorico2 = ValueUtil.getDoubleValue(qtsItensFisicosHistorico[1]);
			}
		}
	}

	public static void populateHistoricoEstoqueCliente(String qtEstoqueCliente, ProdutoBase produto) throws SQLException {
		if (ValueUtil.isNotEmpty(qtEstoqueCliente)) {
			String[] qtsEstoquesHistoricoCliente = qtEstoqueCliente.split(",");
			produto.qtEstoqueClienteHistorico1 = ValueUtil.getDoubleValue(qtsEstoquesHistoricoCliente[0]);
			if (qtsEstoquesHistoricoCliente.length > 1) {
				produto.qtEstoqueClienteHistorico2 = ValueUtil.getDoubleValue(qtsEstoquesHistoricoCliente[1]);
			}
		}
	}
	
	public static void addJoinRedeClientes(StringBuffer sql, String mainAlias) {
		sql.append(" JOIN TBLVPREDE ").append(ALIAS_REDECLIENTE).append(" ON ").append(mainAlias).append(".CDEMPRESA = ").append(ALIAS_REDECLIENTE).append(".CDEMPRESA AND ")
		.append(mainAlias).append(".CDREPRESENTANTE = ").append(ALIAS_REDECLIENTE).append(".CDREPRESENTANTE AND ").append(mainAlias).append(".CDREDE = ").append(ALIAS_REDECLIENTE).append(".CDREDE ");
	}
	
	public static void addJoinStatusPedidoPda(StringBuffer sql) {
		sql.append(" JOIN TBLVPSTATUSPEDIDOPDA ").append(ALIAS_STATUSPEDIDOPDA)
		.append(" ON TB.CDSTATUSPEDIDO = ").append(ALIAS_STATUSPEDIDOPDA).append(".CDSTATUSPEDIDO");
	}
	
	public static void addJoinTipoPagamento(StringBuffer sql) {
		if (LavenderePdaConfig.isTipoPagamentoOcultoAndNaoSetaPadrao()) {
			sql.append(" left ");
		}
		sql.append(" JOIN TBLVPTIPOPAGAMENTO ").append(ALIAS_TIPOPAGAMETO)
			.append(" ON TB.CDEMPRESA = ").append(ALIAS_TIPOPAGAMETO).append(".CDEMPRESA")
			.append(" AND TB.CDREPRESENTANTE = ").append(ALIAS_TIPOPAGAMETO).append(".CDREPRESENTANTE")
			.append(" AND TB.CDTIPOPAGAMENTO = ").append(ALIAS_TIPOPAGAMETO).append(".CDTIPOPAGAMENTO");
	}

	public static void addJoinTipoFrete(StringBuffer sql) {
		sql.append(" JOIN TBLVPTIPOFRETE ").append(ALIAS_TIPOFRETE)
		.append(" ON TB.CDEMPRESA = ").append(ALIAS_TIPOFRETE).append(".CDEMPRESA")
		.append(" AND TB.CDREPRESENTANTE = ").append(ALIAS_TIPOFRETE).append(".CDREPRESENTANTE")
		.append(" AND TB.CDTIPOFRETE = ").append(ALIAS_TIPOFRETE).append(".CDTIPOFRETE");
	}
	
	public static void addJoinFornecedor(StringBuffer sql) {
		sql.append(" JOIN TBLVPFORNECEDOR ").append(ALIAS_FORNECEDOR)
		.append(" ON TB.CDEMPRESA = ").append(ALIAS_FORNECEDOR).append(".CDEMPRESA")
		.append(" AND TB.CDFORNECEDOR = ").append(ALIAS_FORNECEDOR).append(".CDFORNECEDOR")
		.append(" JOIN TBLVPFORNECEDORREP ").append(ALIAS_FORNECEDORREP)
		.append(" ON TB.CDEMPRESA = ").append(ALIAS_FORNECEDORREP).append(".CDEMPRESA")
		.append(" AND TB.CDREPRESENTANTE = ").append(ALIAS_FORNECEDORREP).append(".CDREPRESENTANTE")
		.append(" AND TB.CDFORNECEDOR = ").append(ALIAS_FORNECEDORREP).append(".CDFORNECEDOR");
	}

	public static void addJoinMarcadores(StringBuffer sql, String alias, boolean fromListProduto) {
		String cdCliente = SessionLavenderePda.getCliente() != null ? SessionLavenderePda.getCliente().cdCliente : null;
		sql.append(" LEFT JOIN (SELECT CDEMPRESA, CDREPRESENTANTE, CDPRODUTO,")
		.append(" GROUP_CONCAT('\"' || CDMARCADOR || '\"') as cdmarcador FROM (SELECT CDEMPRESA, CDREPRESENTANTE, ml.CDPRODUTO, m.CDMARCADOR FROM TBLVPMARCADOR m")
		.append(" INNER JOIN TBLVPMARCADORPRODUTO ml ON ml.CDMARCADOR = m.CDMARCADOR")
		.append(" WHERE (COALESCE(DTINICIOVIGENCIA, DATE()) <= DATE()")
		.append(" AND COALESCE(DTTERMINOVIGENCIA, DATE()) >= DATE() )");
		if (!fromListProduto) {
			sql.append(" AND (ml.CDCLIENTE = ").append(Sql.getValue(cdCliente))
			.append(" OR ml.CDCLIENTE = ").append(Sql.getValue(ValueUtil.VALOR_ZERO)).append(")");
		} else {
			sql.append(" group by CDEMPRESA, CDREPRESENTANTE, ml.CDPRODUTO, m.CDMARCADOR ");
		}
		sql.append(" ORDER BY M.NUSEQUENCIA || '-' || M.DSMARCADOR) ")
		.append(" GROUP BY CDEMPRESA, CDREPRESENTANTE, CDPRODUTO) ").append(alias).append(" ON TB.CDEMPRESA = ").append(alias).append(".CDEMPRESA")
		.append(" AND TB.CDREPRESENTANTE = ").append(alias).append(".CDREPRESENTANTE")
		.append(" AND TB.CDPRODUTO = ").append(alias).append(".CDPRODUTO");
	}

	public static void addAndLikeOrConditionMarcadores(SqlWhereClause sqlWhereClause, Vector cdMarcadores) {
		sqlWhereClause.addStartAndMultipleCondition();
		for (String marcador : (String[]) cdMarcadores.toObjectArray()) {
			sqlWhereClause.addOrLikeCondition(DaoUtil.ALIAS_MARCPROD1.concat(".CDMARCADOR"), "%\"".concat(marcador).concat("\"%"), false, true);
		}
		sqlWhereClause.addEndMultipleCondition();
	}

	public static void addJoinPacote(StringBuffer sql, Pacote pacoteFilter) {
		sql.append(" JOIN TBLVPDESCONTOPACOTE ").append(ALIAS_DESCONTOPACOTE)
		.append(" ON TB.CDEMPRESA = ").append(ALIAS_DESCONTOPACOTE).append(".CDEMPRESA")
		.append(" AND TB.CDREPRESENTANTE = ").append(ALIAS_DESCONTOPACOTE).append(".CDREPRESENTANTE")
		.append(" AND TB.CDPRODUTO = ").append(ALIAS_DESCONTOPACOTE).append(".CDPRODUTO")
		.append(" AND ").append(ALIAS_DESCONTOPACOTE).append(".CDPACOTE = ").append(Sql.getValue(pacoteFilter.cdPacote));
		if (ValueUtil.isNotEmpty(pacoteFilter.cdTabelaPrecoFilter)) {
			sql.append(" AND ").append(ALIAS_DESCONTOPACOTE).append(".CDTABELAPRECO = ").append(Sql.getValue(pacoteFilter.cdTabelaPrecoFilter));
		}
	}

	public static void addJoinCentroCustoProd(StringBuffer sql, ProdutoBase produto) {
		addJoinCentroCustoProd(sql, produto, "TB");
	}

	public static void addJoinCentroCustoProd(StringBuffer sql, ProdutoBase produto, String alias) {
		sql.append(" JOIN TBLVPCENTROCUSTOPROD ").append(ALIAS_CENTROCUSTOPROD).append(" ON ")
		.append(alias).append(".CDEMPRESA = ").append(ALIAS_CENTROCUSTOPROD).append(".CDEMPRESA").append(" AND ")
		.append(alias).append(".CDREPRESENTANTE = ").append(ALIAS_CENTROCUSTOPROD).append(".CDREPRESENTANTE")
		.append(" AND ").append(ALIAS_CENTROCUSTOPROD).append(".CDCENTROCUSTO = ").append(Sql.getValue(produto.cdCentroCustoProdFilter)).append(" AND ")
		.append(alias).append(".CDPRODUTO = ").append(ALIAS_CENTROCUSTOPROD).append(".CDPRODUTO");
	}

	public static void addJoinPlataformaVendaProduto(StringBuffer sql, String cdPlataformaVenda) {
		addJoinPlataformaVendaProduto(sql, cdPlataformaVenda, LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco() ? ALIAS_PRODUTO : "TB");
	}
	
	public static void addJoinPlataformaVendaProduto(StringBuffer sql, String cdPlataformaVenda, String aliasProduto) {
		sql.append(" JOIN TBLVPPLATAFORMAVENDAPRODUTO ").append(ALIAS_PLATAFORMAVENDAPRODUTO)
				.append(" ON  ").append(aliasProduto).append(".CDEMPRESA = ").append(ALIAS_PLATAFORMAVENDAPRODUTO).append(".CDEMPRESA")
				.append(" AND ").append(aliasProduto).append(".CDMARCA = ").append(ALIAS_PLATAFORMAVENDAPRODUTO).append(".CDMARCA")
				.append(" AND ").append(aliasProduto).append(".CDLINHA = ").append(ALIAS_PLATAFORMAVENDAPRODUTO).append(".CDLINHA")
				.append(" AND ").append(ALIAS_PLATAFORMAVENDAPRODUTO).append(".CDPLATAFORMAVENDA = ").append(Sql.getValue(cdPlataformaVenda));
	}

	public static void addJoinPlataformaVendaCliente(StringBuffer sql, String cdEmpresa, String cdCliente, String cdCentroCusto, boolean ignoreCliente) {
		sql.append(" JOIN TBLVPPLATAFORMAVENDACLIENTE ").append(ALIAS_PLATAFORMAVENDACLIENTE)
		.append(" ON TB.CDEMPRESA = ").append(ALIAS_PLATAFORMAVENDACLIENTE).append(".CDEMPRESA")
		.append(" AND TB.CDPLATAFORMAVENDA = ").append(ALIAS_PLATAFORMAVENDACLIENTE).append(".CDPLATAFORMAVENDA")
		.append(" WHERE ").append(ALIAS_PLATAFORMAVENDACLIENTE).append(".CDEMPRESA = ").append(Sql.getValue(cdEmpresa))
		.append(" AND ").append(ALIAS_PLATAFORMAVENDACLIENTE).append(".CDCENTROCUSTO = ").append(Sql.getValue(cdCentroCusto));
		if (ValueUtil.isNotEmpty(cdCliente) && !ignoreCliente) {
			sql.append(" AND ").append(ALIAS_PLATAFORMAVENDACLIENTE).append(".CDCLIENTE = ").append(Sql.getValue(cdCliente));
		}
	}

	public static void addJoinLocalEstoque(StringBuffer sql, String cdLocalEstoque) {
		if (SessionLavenderePda.getCliente() == null) {
			sql.append(" left");
		}
		sql.append(" JOIN TBLVPLOCALESTOQUE ").append(ALIAS_LOCALESTOQUE)
		.append(" ON  TB.CDEMPRESA = ").append(ALIAS_LOCALESTOQUE).append(".CDEMPRESA")
		.append(" AND TB.CDREPRESENTANTE = ").append(ALIAS_LOCALESTOQUE).append(".CDREPRESENTANTE")
		.append(" AND ").append(ALIAS_LOCALESTOQUE).append(".cdLocalEstoque = ").append(Sql.getValue(cdLocalEstoque))
		.append(" AND ").append(ALIAS_ESTOQUE_ERP).append(".cdLocalEstoque = ").append(Sql.getValue(cdLocalEstoque))
		.append(" OR ").append(ALIAS_ESTOQUE_PDA).append(".cdLocalEstoque = ").append(Sql.getValue(cdLocalEstoque));
	}

	public static void addJoinStatusOrcamento(StringBuffer sql) {
		sql.append(" LEFT JOIN TBLVPSTATUSORCAMENTO ").append(ALIAS_STATUSORCAMENTO)
			.append(" ON TB.CDEMPRESA = ").append(ALIAS_STATUSORCAMENTO).append(".CDEMPRESA")
			.append(" AND TB.CDREPRESENTANTE = ").append(ALIAS_STATUSORCAMENTO).append(".CDREPRESENTANTE")
			.append(" AND TB.CDSTATUSORCAMENTO = ").append(ALIAS_STATUSORCAMENTO).append(".CDSTATUSORCAMENTO ");
	}

	public static void addJoinCondComercialExcec(StringBuffer sql, ProdutoBase domain) {
		sql.append(" LEFT JOIN TBLVPCONDCOMERCIALEXCEC ").append(ALIAS_CONDCOMERCIALEXCEC)
				.append(" ON TB.CDEMPRESA = ").append(ALIAS_CONDCOMERCIALEXCEC).append(".CDEMPRESA")
				.append(" AND TB.CDREPRESENTANTE = ").append(ALIAS_CONDCOMERCIALEXCEC).append(".CDREPRESENTANTE")
				.append(" AND TB.CDPRODUTO = ").append(ALIAS_CONDCOMERCIALEXCEC).append(".CDPRODUTO ")
				.append(" AND ").append(ALIAS_CONDCOMERCIALEXCEC).append(".CDCONDICAOCOMERCIAL = ").append(Sql.getValue(domain.cdCondicaoComercialFilter));
		if (LavenderePdaConfig.usaGradeProduto1()) {
			sql.append(" AND ").append(ALIAS_CONDCOMERCIALEXCEC).append(".CDITEMGRADE1 = ").append(ALIAS_ITEMTABELAPRECO).append(".CDITEMGRADE1 ");
		}
		domain.joinCondComercialExcec = true;
	}

	public static void addJoinProdutoIndustria(StringBuffer sql, ProdutoIndustria produtoIndustria) {
		sql.append(" JOIN TBLVPPRODUTOINDUSTRIA ").append(ALIAS_PRODUTOINDUSTRIA)
		.append(" ON TB.CDEMPRESA = ").append(ALIAS_PRODUTOINDUSTRIA).append(".CDEMPRESA")
		.append(" AND TB.CDREPRESENTANTE = ").append(ALIAS_PRODUTOINDUSTRIA).append(".CDREPRESENTANTE")
		.append(" AND TB.CDPRODUTO = ").append(ALIAS_PRODUTOINDUSTRIA).append(".CDPRODUTO")
		.append(" AND ").append(ALIAS_PRODUTOINDUSTRIA).append(".CDREFPRODINDUSTRIA = ").append(Sql.getValue(produtoIndustria.cdRefProdIndustria))
		.append(" AND ").append(ALIAS_PRODUTOINDUSTRIA).append(".CDSUGESTAOVENDA = ").append(Sql.getValue(produtoIndustria.cdSugestaoVenda));
	}

	public static void addJoinPermissaoSolAut(StringBuffer sql, String aliasTable, PermissaoSolAut permissaoSolAut) {
		sql.append(" LEFT JOIN ").append(PermissaoSolAut.TABLE_NAME).append(" ").append(aliasTable).append(" ON ");
		sql.append(aliasTable).append(".CDEMPRESA = tb.CDEMPRESA AND ");
		sql.append(aliasTable).append(".CDUSUARIOPERMISSAO = ").append(Sql.getValue(permissaoSolAut.cdUsuarioPermissao)).append(" AND ");
		sql.append(aliasTable).append(".CDTIPOSOLAUTORIZACAO = tb.CDTIPOSOLAUTORIZACAO AND ");
		sql.append(aliasTable).append(".CDSISTEMA = ").append(Sql.getValue(permissaoSolAut.cdSistema));
	}

	public static void addJoinTipoPedido(StringBuffer sql) {
		sql.append(" LEFT JOIN TBLVPTIPOPEDIDO ").append(ALIAS_TIPOPEDIDO)
		.append(" ON TB.CDEMPRESA = ").append(ALIAS_TIPOPEDIDO).append(".CDEMPRESA")
		.append(" AND TB.CDREPRESENTANTE = ").append(ALIAS_TIPOPEDIDO).append(".CDREPRESENTANTE")
		.append(" AND TB.CDTIPOPEDIDO = ").append(ALIAS_TIPOPEDIDO).append(".CDTIPOPEDIDO ");
	}

	public static void addJoinCategoria(StringBuffer sql) {
		sql.append(" LEFT JOIN TBLVPCATEGORIA ").append(ALIAS_CATEGORIA)
		.append(" ON TB.CDEMPRESA = ").append(ALIAS_CATEGORIA).append(".CDEMPRESA")
		.append(" AND TB.CDCATEGORIA = ").append(ALIAS_CATEGORIA).append(".CDCATEGORIA ");
	}

	public static void addJoinProdutoRetirada(StringBuffer sql) {
		sql.append(" LEFT OUTER JOIN (");
		sql.append(" SELECT tbprodret.CDEMPRESA, tbprodret.CDREPRESENTANTE, tbprodret.CDCLIENTE, MAX(DTMAXRETIRADA) DTMAXRETIRADA");
		sql.append(" FROM TBLVPPRODUTORETIRADA tbprodret");
		sql.append(" GROUP BY tbprodret.CDEMPRESA, tbprodret.CDREPRESENTANTE, tbprodret.CDCLIENTE");
		sql.append(" ) TPRET ON TB.CDEMPRESA = TPRET.CDEMPRESA");
		sql.append(" AND TB.CDREPRESENTANTE = TPRET.CDREPRESENTANTE");
		sql.append(" AND TB.CDCLIENTE = TPRET.CDCLIENTE");
	}

	public static void addJoinRede(StringBuffer sql) {
		sql.append(" LEFT JOIN TBLVPREDE ").append(ALIAS_REDE)
		.append(" ON TB.CDEMPRESA = ").append(ALIAS_REDE).append(".CDEMPRESA")
		.append(" AND TB.CDREPRESENTANTE = ").append(ALIAS_REDE).append(".CDREPRESENTANTE ")
		.append(" AND TB.CDREDE = ").append(ALIAS_REDE).append(".CDREDE ");
	}

	public static void addJoinProdutoTipoPedido(StringBuffer sql, ProdutoBase domain) {
		addJoinProdutoTipoPedido(sql, "TB", domain);
	}
	
	public static void addJoinProdutoTipoPedido(StringBuffer sql, String aliasProduto, ProdutoBase domain) {
		sql.append(" LEFT JOIN TBLVPPRODUTOTIPOPED ").append(ALIAS_PRODUTOTIPOPED).append(" ON ")
		.append(aliasProduto).append(".CDEMPRESA = ").append(ALIAS_PRODUTOTIPOPED).append(".CDEMPRESA")
		.append(" AND ").append(aliasProduto).append(".CDREPRESENTANTE = ").append(ALIAS_PRODUTOTIPOPED).append(".CDREPRESENTANTE")
		.append(" AND ").append(aliasProduto).append(".CDPRODUTO = ").append(ALIAS_PRODUTOTIPOPED).append(".CDPRODUTO")
		.append(" AND ").append(ALIAS_PRODUTOTIPOPED).append(".CDTIPOPEDIDO = ").append(Sql.getValue(domain.cdTipoPedidoFilter));
	}

	public static void addNotExistsRestricaoProduto(StringBuffer sql, final String cdProduto, final String cdCliente, final String nuPedido, final double qtdItemInserindo, final String qtdDynamicFilter, final boolean dynamicProduto, final boolean dynamicCliente, final boolean dynamicQuantidade, boolean ocultaNot) {
		sql.append(" AND ").append(ocultaNot ? "" : " NOT ").append(" EXISTS (");
		sql.append(RestricaoService.getInstance().getSqlRestricao(cdProduto, cdCliente, nuPedido, qtdItemInserindo, qtdDynamicFilter, dynamicProduto, dynamicCliente, dynamicQuantidade));
		sql.append(" ) ");
	}

	public static void addJoinItemPedido(StringBuffer sql, String alias, String nuPedidoFilter, String flOrigemPedidoFilter, boolean filtraProdutosInseridos) {
		sql.append(" JOIN TBLVPITEMPEDIDO ").append(alias).append(" ON TB.CDEMPRESA = ").append(alias).append(".CDEMPRESA ")
		.append(" AND TB.CDPRODUTO = ").append(alias).append(".CDPRODUTO")
		.append(" AND ").append(alias).append(".NUPEDIDO = ").append(Sql.getValue(nuPedidoFilter))
		.append(" AND ").append(alias).append(".FLORIGEMPEDIDO = ").append(Sql.getValue(flOrigemPedidoFilter));
		if (!filtraProdutosInseridos) {
			sql.append(" AND TB.CDREPRESENTANTE = ").append(alias).append(".CDREPRESENTANTE");
		}
	}

	public static void addJoinItemPedidoAgrupador(StringBuffer sql, String alias, String nuPedidoFilter, String flOrigemPedidoFilter, String cdProdutoFilter, String cdAgrupadorSimilaridade) {
		sql.append(" LEFT JOIN TBLVPITEMPEDIDO ").append(alias).append(" ON TB.CDEMPRESA = ").append(alias).append(".CDEMPRESA ")
		.append(" AND TB.CDREPRESENTANTE = ").append(alias).append(".CDREPRESENTANTE")
		.append(" AND TB.CDAGRUPADORSIMILARIDADE = ").append(Sql.getValue(cdAgrupadorSimilaridade))
		.append(" AND TB.CDPRODUTO = ").append(alias).append(".CDPRODUTO")
		.append(" AND ").append(alias).append(".CDPRODUTO <> ").append(Sql.getValue(cdProdutoFilter))
		.append(" AND ").append(alias).append(".NUPEDIDO = ").append(Sql.getValue(nuPedidoFilter))
		.append(" AND ").append(alias).append(".FLORIGEMPEDIDO = ").append(Sql.getValue(flOrigemPedidoFilter));
	}

	public static void addJoinItemComboAgrSimilar(StringBuffer sql, ItemComboAgrSimilar itemComboAgrSimilar) {
		sql.append(" JOIN TBLVPITEMCOMBOAGRSIMILAR ").append(ALIAS_ITEMCOMBOAGRSIMILAR)
				.append(" ON TB.CDEMPRESA = ").append(ALIAS_ITEMCOMBOAGRSIMILAR).append(".CDEMPRESA")
				.append(" AND TB.CDREPRESENTANTE = ").append(ALIAS_ITEMCOMBOAGRSIMILAR).append(".CDREPRESENTANTE")
				.append(" AND ").append(ALIAS_ITEMCOMBOAGRSIMILAR).append(".CDPRODUTO = ").append(Sql.getValue(itemComboAgrSimilar.cdProduto))
				.append(" AND TB.CDPRODUTO = ").append(ALIAS_ITEMCOMBOAGRSIMILAR).append(".CDPRODUTOSIMILAR")
				.append(" AND ").append(ALIAS_ITEMCOMBOAGRSIMILAR).append(".CDCOMBO = ").append(Sql.getValue(itemComboAgrSimilar.cdCombo));
		if (ValueUtil.isNotEmpty(itemComboAgrSimilar.flTipoItemCombo)) {
			sql.append(" AND ").append(ALIAS_ITEMCOMBOAGRSIMILAR).append(".FLTIPOITEMCOMBO = ").append(Sql.getValue(itemComboAgrSimilar.flTipoItemCombo));
		}
	}

	public static void addJoinItemComboAgrSimilarBySimilar(StringBuffer sql, ItemComboAgrSimilar itemComboAgrSimilar) {
		sql.append(" JOIN TBLVPITEMCOMBOAGRSIMILAR ").append(ALIAS_ITEMCOMBOAGRSIMILAR)
				.append(" ON TB.CDEMPRESA = ").append(ALIAS_ITEMCOMBOAGRSIMILAR).append(".CDEMPRESA")
				.append(" AND TB.CDREPRESENTANTE = ").append(ALIAS_ITEMCOMBOAGRSIMILAR).append(".CDREPRESENTANTE")
				.append(" AND ").append(ALIAS_ITEMCOMBOAGRSIMILAR).append(".CDPRODUTOSIMILAR = ").append(Sql.getValue(itemComboAgrSimilar.cdProduto))
				.append(" AND TB.CDPRODUTO = ").append(ALIAS_ITEMCOMBOAGRSIMILAR).append(".CDPRODUTO")
				.append(" AND ").append(ALIAS_ITEMCOMBOAGRSIMILAR).append(".CDCOMBO = ").append(Sql.getValue(itemComboAgrSimilar.cdCombo));
		if (ValueUtil.isNotEmpty(itemComboAgrSimilar.flTipoItemCombo)) {
			sql.append(" AND ").append(ALIAS_ITEMCOMBOAGRSIMILAR).append(".FLTIPOITEMCOMBO = ").append(Sql.getValue(itemComboAgrSimilar.flTipoItemCombo));
		}
	}

	public static void addJoinItemKitAgrSimilar(StringBuffer sql, ItemKitAgrSimilar itemKitAgrSimilar) {
		sql.append(" JOIN TBLVPITEMKITAGRSIMILAR ").append(ALIAS_ITEMKITAGRSIMILAR)
				.append(" ON TB.CDEMPRESA = ").append(ALIAS_ITEMKITAGRSIMILAR).append(".CDEMPRESA")
				.append(" AND TB.CDREPRESENTANTE = ").append(ALIAS_ITEMKITAGRSIMILAR).append(".CDREPRESENTANTE")
				.append(" AND ").append(ALIAS_ITEMKITAGRSIMILAR).append(".CDPRODUTO = ").append(Sql.getValue(itemKitAgrSimilar.cdProduto))
				.append(" AND TB.CDPRODUTO = ").append(ALIAS_ITEMKITAGRSIMILAR).append(".CDPRODUTOSIMILAR")
				.append(" AND ").append(ALIAS_ITEMKITAGRSIMILAR).append(".CDKIT = ").append(Sql.getValue(itemKitAgrSimilar.cdKit));
				if (ValueUtil.isNotEmpty(itemKitAgrSimilar.flBonificado)) {
					sql.append(" AND FLBONIFICADO = ").append(Sql.getValue(itemKitAgrSimilar.flBonificado));
				}
		
	}
	
	public static void addJoinDescQtdCliente(StringBuffer sql, String cdTabelaPreco) {
		Cliente cliente = SessionLavenderePda.getCliente();
		if (cliente != null) {
			sql.append(" LEFT JOIN TBLVPDESCQUANTIDADECLIENTE descCliente ON ")
	    	.append(" tb.CDEMPRESA = descCliente.CDEMPRESA AND ")
	    	.append(" tb.CDREPRESENTANTE = descCliente.CDREPRESENTANTE AND ")
	    	.append(" descCliente.CDTABELAPRECO = ").append(Sql.getValue(cdTabelaPreco)).append("AND ")
	    	.append(" tb.CDPRODUTO = descCliente.CDPRODUTO AND ")
	    	.append(" descCliente.CDCLIENTE = ").append(Sql.getValue(cliente.cdCliente));
		}
	}

	public static void addJoinVisita(StringBuffer sql, String dtAgenda, String dtAgendaFinal) {
		sql.append(" LEFT JOIN TBLVPVISITA ").append(ALIAS_VISITA).append(" ON tb.CDEMPRESA = VIS.CDEMPRESA")
			.append(" AND tb.CDREPRESENTANTE = ").append(ALIAS_VISITA).append(".CDREPRESENTANTE AND")
			.append(" tb.CDCLIENTE = ").append(ALIAS_VISITA).append(".CDCLIENTE")
			.append(" AND (STRFTIME('%w', ").append(ALIAS_VISITA).append(".DTAGENDAVISITA) + 1) = tb.NUDIASEMANA")
			.append(" and (STRFTIME('%W', ").append(ALIAS_VISITA).append(".DTAGENDAVISITA))");
		if (!LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita) {
			sql.append("= STRFTIME('%W', DATE(").append(Sql.getValue(dtAgenda)).append("))");
		} else {
			sql.append(" >= STRFTIME('%W', DATE(").append(Sql.getValue(dtAgenda)).append("))")
				.append(" and (STRFTIME('%W', ").append(ALIAS_VISITA).append(".DTAGENDAVISITA))")
				.append(" <= STRFTIME('%W', DATE(").append(Sql.getValue(dtAgendaFinal)).append("))");
		}
		if (LavenderePdaConfig.permiteMultAgendasNoDiaMesmoCliente) {
			sql.append(" and tb.ROWKEY = ").append(ALIAS_VISITA).append(".CDCHAVEAGENDAORIGEM");
		}
	}

	public static void addJoinClienteAgendaVisita(AgendaVisita agendaVisitaFilter, StringBuffer sql) {
		sql.append(" JOIN TBLVPCLIENTE ").append(ALIAS_CLIENTE).append(" ON");
		sql.append(" TB.CDEMPRESA = ").append(ALIAS_CLIENTE).append(".CDEMPRESA")
			.append(" AND TB.CDREPRESENTANTE = ").append(ALIAS_CLIENTE).append(".CDREPRESENTANTE")
			.append(" AND TB.CDCLIENTE = ").append(ALIAS_CLIENTE).append(".CDCLIENTE");
		if (agendaVisitaFilter.cliente != null) {
			if (ValueUtil.isNotEmpty(agendaVisitaFilter.cliente.flTipoCadastro)) {
				sql.append(" AND (").append(ALIAS_CLIENTE).append(".FLTIPOCADASTRO = ").append(Sql.getValue(agendaVisitaFilter.cliente.flTipoCadastro))
					.append(" OR ").append(ALIAS_CLIENTE).append(".FLTIPOCADASTRO = ").append(Sql.getValue(Cliente.TIPO_TODOS)).append(")");
			}
			if (ValueUtil.valueNotEqualsIfNotNull(agendaVisitaFilter.cliente.flTipoClienteRede, Cliente.TIPO_TODOS)) {
				if (ValueUtil.valueEquals(agendaVisitaFilter.cliente.flTipoClienteRede, Cliente.TIPO_REDE)) {
					addWhereClienteRedeAgenda(sql);
				} else if (ValueUtil.valueEquals(agendaVisitaFilter.cliente.flTipoClienteRede, Cliente.TIPO_INDIVIDUAL)) {
					addWhereClienteIndividualAgenda(sql);
				} 
			}
		}
	}
	
	private static void addWhereClienteRedeAgenda(StringBuffer sql) {
		sql.append(" AND ").append(ALIAS_CLIENTE).append(".FLTIPOCADASTRO = ").append(Sql.getValue(Cliente.TIPO_REDE)).append(" AND ")
			.append(ALIAS_CLIENTE).append(".CDREDE IS NOT NULL");
	}
	
	private static void addWhereClienteIndividualAgenda(StringBuffer sql) {
		sql.append(" AND ").append(ALIAS_CLIENTE).append(".FLTIPOCADASTRO = ").append(Sql.getValue(Cliente.TIPO_INDIVIDUAL)).append(" AND ")
			.append(ALIAS_CLIENTE).append(".CDREDE IS NULL");
	}

	public static void addSelectMarcadores(StringBuffer sql) {
		sql.append(", (select GROUP_CONCAT(DISTINCT m.cdmarcador) marcadores ");
		sql.append(" FROM TBLVPMARCADOR m INNER JOIN TBLVPMARCADORCLIENTE ml on (ml.cdEmpresa = tb.cdEmpresa and ml.cdRepresentante = tb.cdRepresentante");
		sql.append(" and ml.cdCliente = tb.cdCliente and ml.cdMarcador = m.CDMARCADOR)");
		sql.append(" WHERE (COALESCE(DTINICIOVIGENCIA, DATE()) <= DATE() AND COALESCE(DTTERMINOVIGENCIA, DATE()) >= DATE()) ORDER BY m.NUSEQUENCIA, m.DSMARCADOR ");
		sql.append(" ) AS cdMarcadores ");
	}
	
	public static void addJoinProdutoClienteExclusivo(StringBuffer sql, String cdCliente, String alias) {
		sql.append(" LEFT JOIN TBLVPPRODUTOCLIENTE PRODCLIENTE ON ")
		.append(alias).append(".CDEMPRESA = PRODCLIENTE.CDEMPRESA AND ")
		.append(alias).append(".CDREPRESENTANTE = PRODCLIENTE.CDREPRESENTANTE AND ")
		.append(alias).append(".CDPRODUTO = PRODCLIENTE.CDPRODUTO AND ")
		.append(" PRODCLIENTE.CDCLIENTE = ").append(Sql.getValue(cdCliente)).append(" AND ")
		.append(" PRODCLIENTE.FLTIPORELACAO = 'X'");
		sql.append(" AND PRODCLIENTE.CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario));
	}
	
	public static void addJoinBonifCfg(StringBuffer sql, String aliasTable) {
		sql.append(" JOIN TBLVPBONIFCFG ").append(aliasTable).append(" ON ");
		sql.append(aliasTable).append(".CDEMPRESA = tb.CDEMPRESA AND ").append(aliasTable).append(".CDBONIFCFG = tb.CDBONIFCFG ");
	}
	
	public static void addJoinProdutoGrade(StringBuffer sql) {
		sql.append(" LEFT JOIN TBLVPPRODUTOGRADE PRODUTOGRADE ON ")
		.append(" TB.CDEMPRESA = PRODUTOGRADE.CDEMPRESA AND ")
		.append(" TB.CDREPRESENTANTE = PRODUTOGRADE.CDREPRESENTANTE AND ")
		.append(" TB.CDPRODUTO = PRODUTOGRADE.CDPRODUTO ");
	}
	
	public static void addJoinCestaProduto(StringBuffer sql, String cdCesta) {
		sql.append(" JOIN TBLVPCESTAPRODUTO cesta ON ")
		.append(" tb.CDPRODUTO = cesta.CDPRODUTO ")
        .append(" AND ").append(" tb.CDEMPRESA = cesta.CDEMPRESA ")
        .append(" AND ").append(" tb.CDREPRESENTANTE = cesta.CDREPRESENTANTE")
		.append(" AND ").append(" cesta.CDCESTA = ").append(cdCesta);
	}
	
	public static void addJoinBonifCfgBrinde(StringBuffer sql, String aliasTable) {
		sql.append(" LEFT JOIN TBLVPBONIFCFGBRINDE ").append(aliasTable).append(" ON ")
			.append(aliasTable).append(".CDEMPRESA = tb.CDEMPRESA AND ")
			.append(aliasTable).append(".CDBONIFCFG = tb.CDBONIFCFG AND ")
			.append(aliasTable).append(".CDPRODUTO = tb.CDPRODUTO ");
	}
	
	public static void addCTEDescPromocional(ProdutoBase produto, StringBuffer sql) {
    	final String CDPADRAO = ValueUtil.VALOR_ZERO;
    	addInicioSentencaCTE(sql);
    	
		sql.append(ALIAS_DESCPROMOCIONAL).append(" AS (")
			.append("SELECT DISTINCT CDEMPRESA, CDREPRESENTANTE, CDPRODUTO, CDGRUPODESCPROD FROM (");
    	//Regra 1
		selectRegrasDescPromocional(sql, produto.cdCliente, CDPADRAO, CDPADRAO, produto.cdTabelaPreco, produto.cdCondicaoComercialFilter, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_CDPRODUTO);
    	selectRegrasDescPromocional(sql, produto.cdCliente, CDPADRAO, CDPADRAO, produto.cdTabelaPreco, CDPADRAO, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_CDPRODUTO);
    	selectRegrasDescPromocional(sql, produto.cdCliente, CDPADRAO, CDPADRAO, CDPADRAO, produto.cdCondicaoComercialFilter, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_CDPRODUTO);
    	selectRegrasDescPromocional(sql, produto.cdCliente, CDPADRAO, CDPADRAO, CDPADRAO, CDPADRAO, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_CDPRODUTO);
    	//Regra 2
    	selectRegrasDescPromocional(sql, produto.cdCliente, CDPADRAO, produto.cdGrupoDescProd, produto.cdTabelaPreco, produto.cdCondicaoComercialFilter, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_NA);
    	selectRegrasDescPromocional(sql, produto.cdCliente, CDPADRAO, produto.cdGrupoDescProd, produto.cdTabelaPreco, CDPADRAO, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_NA);
    	selectRegrasDescPromocional(sql, produto.cdCliente, CDPADRAO, produto.cdGrupoDescProd, CDPADRAO, produto.cdCondicaoComercialFilter, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_NA);
    	selectRegrasDescPromocional(sql, produto.cdCliente, CDPADRAO, produto.cdGrupoDescProd, CDPADRAO, CDPADRAO, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_NA);
    	//Regra 3
    	selectRegrasDescPromocional(sql, CDPADRAO, produto.cdGrupoDescCliFilter, CDPADRAO, produto.cdTabelaPreco, produto.cdCondicaoComercialFilter, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_CDPRODUTO);
    	selectRegrasDescPromocional(sql, CDPADRAO, produto.cdGrupoDescCliFilter, CDPADRAO, produto.cdTabelaPreco, CDPADRAO, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_CDPRODUTO);
    	selectRegrasDescPromocional(sql, CDPADRAO, produto.cdGrupoDescCliFilter, CDPADRAO, CDPADRAO, produto.cdCondicaoComercialFilter, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_CDPRODUTO);
    	selectRegrasDescPromocional(sql, CDPADRAO, produto.cdGrupoDescCliFilter, CDPADRAO, CDPADRAO, CDPADRAO, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_CDPRODUTO);
    	//Regra 4
    	selectRegrasDescPromocional(sql, CDPADRAO, produto.cdGrupoDescCliFilter, produto.cdGrupoDescProd, produto.cdTabelaPreco, produto.cdCondicaoComercialFilter, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_NA);
    	selectRegrasDescPromocional(sql, CDPADRAO, produto.cdGrupoDescCliFilter, produto.cdGrupoDescProd, produto.cdTabelaPreco, CDPADRAO, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_NA);
    	selectRegrasDescPromocional(sql, CDPADRAO, produto.cdGrupoDescCliFilter, produto.cdGrupoDescProd, CDPADRAO, produto.cdCondicaoComercialFilter, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_NA);
    	selectRegrasDescPromocional(sql, CDPADRAO, produto.cdGrupoDescCliFilter, produto.cdGrupoDescProd, CDPADRAO, CDPADRAO, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_NA);
    	//Regra 5
    	selectRegrasDescPromocional(sql, produto.cdCliente, CDPADRAO, CDPADRAO, produto.cdTabelaPreco, produto.cdCondicaoComercialFilter, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_NA);
    	selectRegrasDescPromocional(sql, produto.cdCliente, CDPADRAO, CDPADRAO, produto.cdTabelaPreco, CDPADRAO, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_NA);
    	selectRegrasDescPromocional(sql, produto.cdCliente, CDPADRAO, CDPADRAO, CDPADRAO, produto.cdCondicaoComercialFilter, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_NA);
    	selectRegrasDescPromocional(sql, produto.cdCliente, CDPADRAO, CDPADRAO, CDPADRAO, CDPADRAO, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_NA);
    	//Regra 6
    	selectRegrasDescPromocional(sql, CDPADRAO, produto.cdGrupoDescCliFilter, CDPADRAO, produto.cdTabelaPreco, produto.cdCondicaoComercialFilter, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_NA);
    	selectRegrasDescPromocional(sql, CDPADRAO, produto.cdGrupoDescCliFilter, CDPADRAO, produto.cdTabelaPreco, CDPADRAO, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_NA);
    	selectRegrasDescPromocional(sql, CDPADRAO, produto.cdGrupoDescCliFilter, CDPADRAO, CDPADRAO, produto.cdCondicaoComercialFilter, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_NA);
    	selectRegrasDescPromocional(sql, CDPADRAO, produto.cdGrupoDescCliFilter, CDPADRAO, CDPADRAO, CDPADRAO, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_NA);
    	//Regra 7
    	selectRegrasDescPromocional(sql, CDPADRAO, CDPADRAO, CDPADRAO, produto.cdTabelaPreco, produto.cdCondicaoComercialFilter, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_CDPRODUTO);
    	selectRegrasDescPromocional(sql, CDPADRAO, CDPADRAO, CDPADRAO, produto.cdTabelaPreco, CDPADRAO, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_CDPRODUTO);
    	selectRegrasDescPromocional(sql, CDPADRAO, CDPADRAO, CDPADRAO, CDPADRAO, produto.cdCondicaoComercialFilter, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_CDPRODUTO);
    	selectRegrasDescPromocional(sql, CDPADRAO, CDPADRAO, CDPADRAO, CDPADRAO, CDPADRAO, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_CDPRODUTO);
    	//Regra 8
    	selectRegrasDescPromocional(sql, CDPADRAO, CDPADRAO, produto.cdGrupoDescProd, produto.cdTabelaPreco, produto.cdCondicaoComercialFilter, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_NA);
    	selectRegrasDescPromocional(sql, CDPADRAO, CDPADRAO, produto.cdGrupoDescProd, produto.cdTabelaPreco, CDPADRAO, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_NA);
    	selectRegrasDescPromocional(sql, CDPADRAO, CDPADRAO, produto.cdGrupoDescProd, CDPADRAO, produto.cdCondicaoComercialFilter, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_NA);
    	selectRegrasDescPromocional(sql, CDPADRAO, CDPADRAO, produto.cdGrupoDescProd, CDPADRAO, CDPADRAO, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_NA);
	    selectRegrasDescPromocional(sql, CDPADRAO, CDPADRAO, produto.cdGrupoDescProd, CDPADRAO, CDPADRAO, produto.cdLocalFilter, TIPO_COMPARACAO_PRODUTO_CDGRUPODESCPROD);
    	//
	    //Remove o ultimo UNION ALL
    	sql.delete(sql.length() - 11, sql.length());
		sql.append(")");
		if (produto.filterByNuPromocao) {
			sql.append(" WHERE NUPROMOCAO = ").append(Sql.getValue(produto.nuPromocaoFilter));
		}
		sql.append(")");
	}
	
	public static void addGrupoSimilarEstoqueExists(StringBuffer sql, Kit kit, String tableAlias, String cdProdutoColumn) {
		sql.append(" EXISTS (")
			.append("SELECT 1 FROM").append(" TBLVPPRODUTO prod")
			.append(" LEFT JOIN ")
			.append(tableAlias)
			.append(" item ON ")
			.append(" item.").append(cdProdutoColumn).append(" = prod.CDPRODUTO ")
			.append(" AND item.CDEMPRESA = prod.CDEMPRESA ")
			.append(" AND item.CDREPRESENTANTE = prod.CDREPRESENTANTE ")
			.append(" AND item.CDTABELAPRECO = ").append(Sql.getValue(kit.cdTabelaPreco))
			.append(" AND item.CDKIT = ").append(Sql.getValue(kit.cdKit))
			.append(" JOIN TBLVPESTOQUE est ON")
			.append(" est.CDEMPRESA = prod.CDEMPRESA")
			.append(" AND est.CDREPRESENTANTE = prod.CDREPRESENTANTE")
			.append(" AND est.CDPRODUTO = item.").append(cdProdutoColumn)
			.append(" AND est.CDITEMGRADE1 = '0'")
			.append(" AND est.CDITEMGRADE2 = '0'")
			.append(" AND est.CDITEMGRADE3 = '0'")
			.append(" AND est.CDLOCALESTOQUE = ").append(Sql.getValue(kit.cdLocalEstoque))
			.append(" where prod.CDEMPRESA = ").append(Sql.getValue(kit.cdEmpresa))
			.append(" AND prod.CDREPRESENTANTE = ").append(Sql.getValue(kit.cdRepresentante))
			.append(" GROUP BY prod.CDAGRUPADORSIMILARIDADE")
			.append(" HAVING SUM(COALESCE(CASE WHEN est.FLORIGEMESTOQUE = 'E' THEN est.QTESTOQUE ELSE 0 END, 0)")
			.append(" - COALESCE(CASE WHEN est.FLORIGEMESTOQUE = 'P' THEN est.QTESTOQUE ELSE 0 END, 0)) <= 0")
			.append(")");
	}

	public static void addJoinStatusItemPedido(StringBuffer sql) {
		sql.append(" LEFT JOIN TBLVPSTATUSITEMPEDIDO ").append(ALIAS_STATUSITEMPEDIDO).append(" ON ")
				.append(ALIAS_STATUSITEMPEDIDO).append(".CDEMPRESA = tb.CDEMPRESA AND ")
				.append(ALIAS_STATUSITEMPEDIDO).append(".CDSTATUSITEMPEDIDO = tb.CDSTATUSITEMPEDIDO ");
	}
	
	private static String getSQLMinQtItemFromItemTabelaPreco() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT MIN(QTITEM) FROM TBLVPITEMTABELAPRECO INNERPRECO");
		sql.append(" WHERE ITPRECO.CDEMPRESA = innerpreco.CDEMPRESA AND ITPRECO.CDREPRESENTANTE = innerpreco.CDREPRESENTANTE");
		sql.append(" AND ITPRECO.CDPRODUTO = innerpreco.CDPRODUTO AND ITPRECO.CDTABELAPRECO = innerpreco.CDTABELAPRECO");
		sql.append(" AND ITPRECO.CDUNIDADE = innerpreco.CDUNIDADE AND ITPRECO.CDITEMGRADE1 = innerpreco.CDITEMGRADE1");
		sql.append(" AND ITPRECO.CDITEMGRADE2 = innerpreco.CDITEMGRADE2 AND ITPRECO.CDITEMGRADE3 = innerpreco.CDITEMGRADE3");
		sql.append(" AND ITPRECO.CDUF = innerpreco.CDUF AND ITPRECO.CDPRAZOPAGTOPRECO = innerpreco.CDPRAZOPAGTOPRECO");
		return sql.toString();
	}
	public static void addJoinPedidoByStatus(StringBuffer sql, ItemPedido itemPedido, String cdStatusPedido) {
		sql.append(" JOIN TBLVPPEDIDO ped ON ")
				.append(" ped.CDEMPRESA = tb.CDEMPRESA ")
				.append(" AND ped.CDREPRESENTANTE = tb.CDREPRESENTANTE ")
				.append(" AND ped.NUPEDIDO = tb.NUPEDIDO ")
				.append(" AND ped.FLORIGEMPEDIDO = ").append(Sql.getValue(itemPedido.flOrigemPedido)).append(" ")
				.append(" AND ped.CDSTATUSPEDIDO = ").append(Sql.getValue(cdStatusPedido)).append(" ");
	}
	
	public static void addCTETributacaoConfig(StringBuffer sql, ProdutoBase produto) {
		addInicioSentencaCTE(sql);
		sql.append(ALIAS_CTETRIBUTACAOCONFIG).append(" AS (")
		.append("SELECT * FROM TBLVPTRIBUTACAOCONFIG WHERE CDEMPRESA = ").append(Sql.getValue(produto.cdEmpresa))
		.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(SessionLavenderePda.getCdRepresentanteFiltroDados(TributacaoConfig.class)))
		.append(" AND CDTIPOPEDIDO = ").append(Sql.getValue(produto.cdTipoPedidoFilter))
		.append(" AND CDUF = ").append(Sql.getValue(produto.cdUFFilter)).append(")");
	}
	

	public static void addJoinItemPedidoErpDif(StringBuffer sql) {
		sql.append(" LEFT JOIN TBLVPITEMPEDIDOERPDIF ").append(ALIAS_ITEMPEDIDOERPDIF).append(" ON ")
				.append(ALIAS_ITEMPEDIDOERPDIF).append(".CDEMPRESA = TB.CDEMPRESA AND ")
				.append(ALIAS_ITEMPEDIDOERPDIF).append(".CDREPRESENTANTE = TB.CDREPRESENTANTE AND ")
				.append(ALIAS_ITEMPEDIDOERPDIF).append(".FLORIGEMPEDIDO = TB.FLORIGEMPEDIDO AND ")
				.append(ALIAS_ITEMPEDIDOERPDIF).append(".NUPEDIDO = TB.NUPEDIDO AND ")
				.append(ALIAS_ITEMPEDIDOERPDIF).append(".FLTIPOITEMPEDIDO = TB.FLTIPOITEMPEDIDO AND ")
				.append(ALIAS_ITEMPEDIDOERPDIF).append(".CDPRODUTO = TB.CDPRODUTO AND ")
				.append(ALIAS_ITEMPEDIDOERPDIF).append(".NUSEQPRODUTO = TB.NUSEQPRODUTO AND ")
				.append(ALIAS_ITEMPEDIDOERPDIF).append(".CDUSUARIO = TB.CDUSUARIO ");
	}
	
	public static void addJoinProdutoDestaque(StringBuffer sql, String aliasProduto) {
		sql.append(" LEFT JOIN TBLVPPRODUTODESTAQUE ").append(ALIAS_PRODUTODESTAQUE).append(" ON ")
				.append(ALIAS_PRODUTODESTAQUE).append(".CDEMPRESA = ").append(aliasProduto).append(".CDEMPRESA AND ")
				.append(ALIAS_PRODUTODESTAQUE).append(".CDREPRESENTANTE = ").append(aliasProduto).append(".CDREPRESENTANTE AND ")
				.append(ALIAS_PRODUTODESTAQUE).append(".CDGRUPODESTAQUE = ").append(aliasProduto).append(".CDGRUPODESTAQUE AND ")
				.append(ALIAS_PRODUTODESTAQUE).append(".CDUSUARIO = ").append(aliasProduto).append(".CDUSUARIO ");
	}
	
	public static void addJoinCorSistema(StringBuffer sql) {
		sql.append(" LEFT JOIN TBLVPCORSISTEMA ").append(ALIAS_CORSISTEMA).append(" ON ")
		.append(ALIAS_CORSISTEMA).append(".CDESQUEMACOR = ").append(ALIAS_PRODUTODESTAQUE).append(".CDESQUEMACOR AND ")
		.append(ALIAS_CORSISTEMA).append(".CDCOR = ").append(ALIAS_PRODUTODESTAQUE).append(".CDCOR ");
	}
	
	public static void addExistsCondTipoPedido(StringBuffer sql, CondTipoPedido condTipoPedido) {
		sql.append(" AND (");
		if (condTipoPedido.excecaoCondPagtoFilter) {
			sql.append("NOT ");
		}
		sql.append("EXISTS (SELECT 1 FROM TBLVPCONDTIPOPEDIDO TAB WHERE TB.CDEMPRESA = TAB.CDEMPRESA")
				.append(" AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(condTipoPedido.cdRepresentante))
				.append(" AND TB.CDCONDICAOPAGAMENTO = TAB.CDCONDICAOPAGAMENTO AND TAB.CDTIPOPEDIDO = ")
				.append(Sql.getValue(condTipoPedido.cdTipoPedido)).append(")");
		sql.append(" OR NOT EXISTS (SELECT 1 FROM TBLVPCONDTIPOPEDIDO TAB WHERE").append(
				" TB.CDEMPRESA = TAB.CDEMPRESA AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(condTipoPedido.cdRepresentante)).append(" AND TAB.CDTIPOPEDIDO = ")
				.append(Sql.getValue(condTipoPedido.cdTipoPedido)).append("))");
	}
	
	public static void addExistsCondPagtoTabPreco(StringBuffer sql, CondPagtoTabPreco condPagtoTabPreco, boolean usaCondPagtoPorTabPreco) {
		sql.append(" AND (EXISTS (SELECT 1 FROM TBLVPCONDPAGTOTABPRECO TAB WHERE TB.CDEMPRESA = TAB.CDEMPRESA")
				.append(" AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(condPagtoTabPreco.cdRepresentante));
		
		if (usaCondPagtoPorTabPreco) {
			sql.append(" AND TB.CDCONDICAOPAGAMENTO = TAB.CDCONDICAOPAGAMENTO AND TAB.CDTABELAPRECO = ");
			sql.append(Sql.getValue(condPagtoTabPreco.cdTabelaPreco));
			if (LavenderePdaConfig.usaCondPagtoTabPrecoComVigencia) {
				sql.append(" AND DATE('now', 'localtime') BETWEEN COALESCE(TAB.DTINICIAL, DATE('now', 'localtime')) and COALESCE(TAB.DTFINAL, DATE('now', 'localtime')) ");
			}
			if (LavenderePdaConfig.usaValidacaoMinimosCondPagtoPorCondPagtoTabPreco) {
				boolean validaQtMinValorCondPagtoSeparado = LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagtoRetiraOpcoesInvalidasCombo();
				boolean validaQtMinProdutoCondPagtoSeparado = LavenderePdaConfig.usaQtdeMinimaProdutoPorCondPagamentoEQtProduto() && LavenderePdaConfig.isQtdeMinimaProdutoPorCondPagamentoFormaValidacaoPadrao();
				if (!validaQtMinValorCondPagtoSeparado && !validaQtMinProdutoCondPagtoSeparado && condPagtoTabPreco.qtMinValor != -1 && condPagtoTabPreco.qtMinProduto >= 0) {
					sql.append(" AND (TAB.QTMINVALOR <= ").append(Sql.getValue(condPagtoTabPreco.qtMinValor))
							.append(" OR TAB.QTMINPRODUTO <= ").append(Sql.getValue(condPagtoTabPreco.qtMinProduto))
							.append(")");
				} else {
					if (validaQtMinValorCondPagtoSeparado && condPagtoTabPreco.qtMinValor != -1) {
						sql.append(" AND TAB.QTMINVALOR <= ").append(Sql.getValue(condPagtoTabPreco.qtMinValor));
					}
					if (validaQtMinProdutoCondPagtoSeparado && condPagtoTabPreco.qtMinProduto != 0) {
						sql.append(" AND TAB.QTMINPRODUTO <= ").append(Sql.getValue(condPagtoTabPreco.qtMinProduto));
					}
				}
			}
		} else {
			sql.append(" AND TB.CDTABELAPRECO = TAB.CDTABELAPRECO AND TAB.CDCONDICAOPAGAMENTO = ");
			sql.append(Sql.getValue(condPagtoTabPreco.cdCondicaoPagamento));
		}
		sql.append("))");
	}
	
	public static void addExistsCondPagtoCli(StringBuffer sql, CondPagtoCli condPagtoCli) {
		sql.append(" AND (EXISTS (SELECT 1 FROM TBLVPCONDPAGTOCLI TAB WHERE TB.CDEMPRESA = TAB.CDEMPRESA")
				.append(" AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(condPagtoCli.cdRepresentante))
				.append(" AND TB.CDCONDICAOPAGAMENTO = TAB.CDCONDICAOPAGAMENTO AND TAB.CDCLIENTE = ")
				.append(Sql.getValue(condPagtoCli.cdCliente)).append(")");
		sql.append(" OR NOT EXISTS (SELECT 1 FROM TBLVPCONDPAGTOCLI TAB WHERE")
				.append(" TB.CDEMPRESA = TAB.CDEMPRESA AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(condPagtoCli.cdRepresentante)).append(" AND TAB.CDCLIENTE = ")
				.append(Sql.getValue(condPagtoCli.cdCliente)).append("))");
	}
	
	public static void addExistsTipoCondPagtoCli(StringBuffer sql, TipoCondPagtoCli tipoCondPagtoCli, boolean useCondPagtoPorTipoPagto) {
		sql.append(" AND (EXISTS (SELECT 1 FROM TBLVPTIPOCONDPAGTOCLI TAB WHERE TB.CDEMPRESA = TAB.CDEMPRESA")
				.append(" AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(tipoCondPagtoCli.cdRepresentante))
				.append(" AND TAB.CDCLIENTE = ").append(Sql.getValue(tipoCondPagtoCli.cdCliente));
		if (useCondPagtoPorTipoPagto) {
			sql.append(" AND TB.CDCONDICAOPAGAMENTO = TAB.CDCONDICAOPAGAMENTO ");
			if (ValueUtil.isNotEmpty(tipoCondPagtoCli.cdTipoPagamento)) {
				sql.append(" AND TAB.CDTIPOPAGAMENTO = ").append(Sql.getValue(tipoCondPagtoCli.cdTipoPagamento));
			}
		} else {
			sql.append(" AND TAB.CDTIPOPAGAMENTO = TB.CDTIPOPAGAMENTO");
			if (ValueUtil.isNotEmpty(tipoCondPagtoCli.cdCondicaoPagamento)) {
				sql.append(" AND TAB.CDCONDICAOPAGAMENTO = ").append(Sql.getValue(tipoCondPagtoCli.cdCondicaoPagamento));
			} else {
				sql.append(") OR NOT EXISTS (SELECT 1 FROM TBLVPTIPOCONDPAGTOCLI TAB WHERE")
					.append(" TB.CDEMPRESA = TAB.CDEMPRESA AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(tipoCondPagtoCli.cdRepresentante))
					.append(" AND TAB.CDCLIENTE = ").append(Sql.getValue(tipoCondPagtoCli.cdCliente));
			}
		}
		sql.append("))");
	}
	
	public static void addExistsCondPagtoSeg(StringBuffer sql, CondPagtoSeg condPagtoSeg) {
		sql.append(" AND (EXISTS (SELECT 1 FROM TBLVPCONDPAGTOSEG TAB WHERE TB.CDEMPRESA = TAB.CDEMPRESA")
				.append(" AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(condPagtoSeg.cdRepresentante))
				.append(" AND TB.CDCONDICAOPAGAMENTO = TAB.CDCONDICAOPAGAMENTO AND TAB.CDSEGMENTO = ")
				.append(Sql.getValue(condPagtoSeg.cdSegmento)).append("))");
	}
	
	public static void addExistsCondComCondPagto(StringBuffer sql, CondComCondPagto condComCondPagto, boolean useCondPagtoPorCondCom) {
		sql.append(" AND (EXISTS (SELECT 1 FROM TBLVPCONDCOMCONDPAGTO TAB WHERE TB.CDEMPRESA = TAB.CDEMPRESA")
				.append(" AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(condComCondPagto.cdRepresentante));
		if (useCondPagtoPorCondCom) {
			sql.append(" AND TB.CDCONDICAOPAGAMENTO = TAB.CDCONDICAOPAGAMENTO");
			sql.append(" AND TAB.CDCONDICAOCOMERCIAL = ").append(Sql.getValue(condComCondPagto.cdCondicaoComercial));
		} else {
			sql.append(" AND TB.CDCONDICAOCOMERCIAL = TAB.CDCONDICAOCOMERCIAL");
			sql.append(" AND TAB.CDCONDICAOPAGAMENTO = ").append(Sql.getValue(condComCondPagto.cdCondicaoPagamento));
		}
		sql.append("))");
	}
	
	public static void addExistsCondTipoPagto(StringBuffer sql, CondTipoPagto condTipoPagto, boolean useCondPagtoPorTipoPagto) {
		sql.append(" AND (EXISTS (SELECT 1 FROM TBLVPCONDTIPOPAGTO TAB WHERE TB.CDEMPRESA = TAB.CDEMPRESA")
				.append(" AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(condTipoPagto.cdRepresentante));
		if (useCondPagtoPorTipoPagto) {
			sql.append(" AND TB.CDCONDICAOPAGAMENTO = TAB.CDCONDICAOPAGAMENTO");
			sql.append(" AND TAB.CDTIPOPAGAMENTO = ").append(Sql.getValue(condTipoPagto.cdTipoPagamento));
		} else {
			sql.append(" AND TB.CDTIPOPAGAMENTO = TAB.CDTIPOPAGAMENTO");
			sql.append(" AND TAB.CDCONDICAOPAGAMENTO = ").append(Sql.getValue(condTipoPagto.cdCondicaoPagamento));
		}
		sql.append("))");
	}
	
	public static void addExistsCondPagtoRep(StringBuffer sql, CondPagtoRep condPagtoRep) {
		sql.append(" AND (EXISTS (SELECT 1 FROM TBLVPCONDPAGTOREP TAB WHERE TB.CDEMPRESA = TAB.CDEMPRESA")
				.append(" AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(condPagtoRep.cdRepresentante))
				.append(" AND TB.CDCONDICAOPAGAMENTO = TAB.CDCONDICAOPAGAMENTO )");
		sql.append(" OR NOT EXISTS (SELECT 1 FROM TBLVPCONDPAGTOREP TAB WHERE")
				.append(" TB.CDEMPRESA = TAB.CDEMPRESA AND TAB.CDREPRESENTANTE = ")
				.append(Sql.getValue(condPagtoRep.cdRepresentante)).append("))");
	}
	
	public static void addExistsTipoPedidoCli(StringBuffer sql, TipoPedidoCli tipoPedidoCli) {
		sql.append(" AND (EXISTS (SELECT 1 FROM TBLVPTIPOPEDIDOCLI TAB WHERE TB.CDEMPRESA = TAB.CDEMPRESA")
				.append(" AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(tipoPedidoCli.cdRepresentante))
				.append(" AND TB.CDTIPOPEDIDO = TAB.CDTIPOPEDIDO AND TAB.CDCLIENTE = ")
				.append(Sql.getValue(tipoPedidoCli.cdCliente)).append("))");
	}
	
	public static void addExistsTabelaPrecoCli(StringBuffer sql, TabelaPrecoCli tabelaPrecoCli) {
		sql.append(" AND (EXISTS (SELECT 1 FROM TBLVPTABELAPRECOCLI TAB WHERE TB.CDEMPRESA = TAB.CDEMPRESA")
			.append(" AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(tabelaPrecoCli.cdRepresentante))
			.append(" AND TB.CDTABELAPRECO = TAB.CDTABELAPRECO AND TAB.CDCLIENTE = ")
			.append(Sql.getValue(tabelaPrecoCli.cdCliente)).append("))");
	}
	
	public static void addExistsTabelaPrecoRep(StringBuffer sql, TabelaPrecoRep tabelaPrecoRep) {
		sql.append(" AND (EXISTS (SELECT 1 FROM TBLVPTABELAPRECOREP TAB WHERE TB.CDEMPRESA = TAB.CDEMPRESA")
			.append(" AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(tabelaPrecoRep.cdRepresentante))
			.append(" AND TB.CDTABELAPRECO = TAB.CDTABELAPRECO))");
	}
	
	public static void addExistsTabelaPrecoReg(StringBuffer sql, TabelaPrecoReg tabelaPrecoReg) {
		sql.append(" AND (EXISTS (SELECT 1 FROM TBLVPTABELAPRECOREG TAB WHERE TB.CDEMPRESA = TAB.CDEMPRESA")
		.append(" AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(tabelaPrecoReg.cdRepresentante))
		.append(" AND TB.CDTABELAPRECO = TAB.CDTABELAPRECO AND TAB.CDREGIAO = ")
		.append(Sql.getValue(tabelaPrecoReg.cdRegiao)).append("))");
	}
	
	public static void addExistsTabelaPrecoSeg(StringBuffer sql, TabelaPrecoSeg tabelaPrecoSeg) {
		sql.append(" AND (EXISTS (SELECT 1 FROM TBLVPTABELAPRECOSEG TAB WHERE TB.CDEMPRESA = TAB.CDEMPRESA")
		.append(" AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(tabelaPrecoSeg.cdRepresentante))
		.append(" AND TB.CDTABELAPRECO = TAB.CDTABELAPRECO AND TAB.CDSEGMENTO = ")
		.append(Sql.getValue(tabelaPrecoSeg.cdSegmento)).append("))");
	}
	
	public static void addExistsTabPrecTipoPedido(StringBuffer sql, TabPrecTipoPedido tabPrecTipoPedido) {
		sql.append(" AND (");
		if (tabPrecTipoPedido.excecaoTipoPedidoFilter) {
			sql.append("NOT ");
		}
		sql.append("EXISTS (SELECT 1 FROM TBLVPTABPRECTIPOPEDIDO TAB WHERE TB.CDEMPRESA = TAB.CDEMPRESA")
				.append(" AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(tabPrecTipoPedido.cdRepresentante))
				.append(" AND TB.CDTABELAPRECO = TAB.CDTABELAPRECO AND TAB.CDTIPOPEDIDO = ")
				.append(Sql.getValue(tabPrecTipoPedido.cdTipoPedido)).append(")");
		sql.append(" OR NOT EXISTS (SELECT 1 FROM TBLVPTABPRECTIPOPEDIDO TAB WHERE").append(
				" TB.CDEMPRESA = TAB.CDEMPRESA AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(tabPrecTipoPedido.cdRepresentante)).append(" AND TAB.CDTIPOPEDIDO = ")
				.append(Sql.getValue(tabPrecTipoPedido.cdTipoPedido)).append("))");
	}
	public static void addExistsTipoPagtoCli(StringBuffer sql, TipoPagtoCli tipoPagtoCli) {
		sql.append(" AND (EXISTS (SELECT 1 FROM TBLVPTIPOPAGTOCLI TAB WHERE TB.CDEMPRESA = TAB.CDEMPRESA")
				.append(" AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(tipoPagtoCli.cdRepresentante))
				.append(" AND TB.CDTIPOPAGAMENTO = TAB.CDTIPOPAGAMENTO AND TAB.CDCLIENTE = ")
				.append(Sql.getValue(tipoPagtoCli.cdCliente)).append(")");
		sql.append(" OR NOT EXISTS (SELECT 1 FROM TBLVPTIPOPAGTOCLI TAB WHERE")
				.append(" TB.CDEMPRESA = TAB.CDEMPRESA AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(tipoPagtoCli.cdRepresentante))
				.append(" AND TAB.CDCLIENTE = ").append(Sql.getValue(tipoPagtoCli.cdCliente)).append("))");
	}
	
	public static void addExistsTipoPagtoTabPreco(StringBuffer sql, TipoPagtoTabPreco tipoPagtoTabPreco) {
		sql.append(" AND (EXISTS (SELECT 1 FROM TBLVPTIPOPAGTOTABPRECO TAB WHERE TB.CDEMPRESA = TAB.CDEMPRESA")
			.append(" AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(tipoPagtoTabPreco.cdRepresentante))
			.append(" AND TB.CDTIPOPAGAMENTO = TAB.CDTIPOPAGAMENTO AND TAB.CDTABELAPRECO = ")
			.append(Sql.getValue(tipoPagtoTabPreco.cdTabelaPreco)).append(")");
		sql.append(" OR NOT EXISTS (SELECT 1 FROM TBLVPTIPOPAGTOTABPRECO TAB WHERE")
			.append(" TB.CDEMPRESA = TAB.CDEMPRESA AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(tipoPagtoTabPreco.cdRepresentante))
			.append(" AND TAB.CDTABELAPRECO = ").append(Sql.getValue(tipoPagtoTabPreco.cdTabelaPreco)).append("))");
	}
	
	public static void addTipoPagtoQuePossuiCondPagtoCondition(StringBuffer sql, TipoPagamento tipoPagamento) {
		StringBuffer sqlExistsClausule = new StringBuffer();
		sql.append(" AND EXISTS (SELECT 1 FROM TBLVPCONDTIPOPAGTO CONDTIPOPAGTO ");
		if (LavenderePdaConfig.usaGrupoCondPagtoCli && ValueUtil.isNotEmpty(tipoPagamento.condicaoPagamentoFilter.cdGrupoCondicao)) {
    		addJoinCondPagtoGrupoCondicao(tipoPagamento.condicaoPagamentoFilter, sql);
    	}
		addWhereTipoPagtoQuePossuiCondPagtoCondition(tipoPagamento.condicaoPagamentoFilter.condTipoPagtoFilter, tipoPagamento.condicaoPagamentoFilter, sqlExistsClausule);
		
		String result = sqlExistsClausule.toString();
		result = result.replaceAll("(?i)tb\\.", "CONDTIPOPAGTO.");
		result += " AND CONDTIPOPAGTO.CDTIPOPAGAMENTO = TB.CDTIPOPAGAMENTO)";
		sql.append(result);
	}
	
	private static void addWhereTipoPagtoQuePossuiCondPagtoCondition(CondTipoPagto domain, CondicaoPagamento condPagto, StringBuffer sql){
    	SqlWhereClause sqlWhereClause = new SqlWhereClause();
    	sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", domain.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", domain.cdRepresentante);
		sql.append(sqlWhereClause.getSql());
    	
    	if (LavenderePdaConfig.usaCondicaoPagamentoPorTipoPedido && condPagto.condTipoPedidoFilter != null) {
       		addExistsCondTipoPedido(sql, condPagto.condTipoPedidoFilter);
       	}
    	if (LavenderePdaConfig.usaCondicaoPagamentoRepresentante && condPagto.condPagtoRepFilter != null) {
        	addExistsCondPagtoRep(sql, condPagto.condPagtoRepFilter);
        }
    }
	
	private static void addJoinCondPagtoGrupoCondicao(CondicaoPagamento condPagto, StringBuffer sql) {
		SqlWhereClause sqlOnJoinClause = new SqlWhereClause();
		StringBuffer sbJoinCondPagto = new StringBuffer();
		sbJoinCondPagto.append(" JOIN TBLVPCONDICAOPAGAMENTO ").append(DaoUtil.ALIAS_CONDICAOPAGAMENTO)
			.append(" ON ").append(DaoUtil.ALIAS_CONDICAOPAGAMENTO).append(".CDEMPRESA = CONDTIPOPAGTO.CDEMPRESA")
			.append(" AND ").append(DaoUtil.ALIAS_CONDICAOPAGAMENTO).append(".CDREPRESENTANTE = ").append(Sql.getValue(condPagto.cdRepresentante))
			.append(" AND ").append(DaoUtil.ALIAS_CONDICAOPAGAMENTO).append(".CDCONDICAOPAGAMENTO = CONDTIPOPAGTO.CDCONDICAOPAGAMENTO ");
		sqlOnJoinClause.addJoin(sbJoinCondPagto.toString());
		addAndMultipleOrLikeCondition(sqlOnJoinClause, StringUtil.split(condPagto.cdGrupoCondicao, CondicaoPagamento.SEPARADOR_CAMPOS), DaoUtil.ALIAS_CONDICAOPAGAMENTO + ".CDGRUPOCONDICAO", StringUtil.getStringValue(CondicaoPagamento.SEPARADOR_CAMPOS));
		sql.append(sqlOnJoinClause.getSql());
	}
	
	public static void addExistsItemTabelaPreco(StringBuffer sql, String aliasTable, String aliasProduto, ItemTabelaPreco itemTabelaPreco, String customCompare) {
		sql.append(" EXISTS (SELECT 1 FROM TBLVPITEMTABELAPRECO ").append(aliasTable).append(" WHERE ");
		addComparacaoChaveItemTabPreco(sql, aliasTable, aliasProduto, itemTabelaPreco);
		sql.append(customCompare);
		sql.append(")");
	}

	public static void addJoinCondComCliente(StringBuffer sql, CondComCliente condComCliente) {
		sql.append(" JOIN TBLVPCONDCOMCLIENTE CONDCOMCLI ON TB.CDEMPRESA = CONDCOMCLI.CDEMPRESA")
				.append(" AND CONDCOMCLI.CDREPRESENTANTE = ").append(Sql.getValue(condComCliente.cdRepresentante))
				.append(" AND TB.CDCONDICAOCOMERCIAL = CONDCOMCLI.CDCONDICAOCOMERCIAL AND CONDCOMCLI.CDCLIENTE = ")
				.append(Sql.getValue(condComCliente.cdCliente));
	}
	
	public static void addJoinCondComSegCli(StringBuffer sql, CondComSegCli condComSegCli) {
		sql.append(" JOIN TBLVPCONDCOMSEGCLI CONDCOMSEGCLI ON TB.CDEMPRESA = CONDCOMSEGCLI.CDEMPRESA")
				.append(" AND CONDCOMSEGCLI.CDREPRESENTANTE = ").append(Sql.getValue(condComSegCli.cdRepresentante))
				.append(" AND TB.CDCONDICAOCOMERCIAL = CONDCOMSEGCLI.CDCONDICAOCOMERCIAL AND CONDCOMSEGCLI.CDCLIENTE = ")
				.append(Sql.getValue(condComSegCli.cdCliente))
				.append(" AND CONDCOMSEGCLI.CDSEGMENTO = ").append(Sql.getValue(condComSegCli.cdSegmento));
		
	}
	
	public static void addExistsClienteCondComCli(StringBuffer sql, CondComCliente condComCliente) {
		String cdRepresetanteCli = SessionLavenderePda.getCdRepresentanteFiltroDados(Cliente.class);
		sql.append(" AND (EXISTS (SELECT 1 FROM TBLVPCLIENTE CLI JOIN TBLVPCONDCOMCLIENTE CONDCOMCLI ON")
				.append(" CLI.CDEMPRESA = CONDCOMCLI.CDEMPRESA AND CONDCOMCLI.CDREPRESENTANTE = ")
				.append(Sql.getValue(condComCliente.cdRepresentante))
				.append(" AND TB.CDCONDICAOCOMERCIAL = CONDCOMCLI.CDCONDICAOCOMERCIAL AND CONDCOMCLI.CDCLIENTE = CLI.CDCLIENTE")
				.append(" WHERE CLI.CDEMPRESA = TB.CDEMPRESA AND CLI.CDREPRESENTANTE = ")
				.append(Sql.getValue(cdRepresetanteCli)).append(")");
		sql.append(" OR NOT EXISTS ( SELECT 1 FROM TBLVPCLIENTE CLI WHERE")
				.append(" CLI.CDEMPRESA = TB.CDEMPRESA AND CLI.CDREPRESENTANTE = ")
				.append(Sql.getValue(cdRepresetanteCli)).append("))");
	}
	
	public static void addJoinClienteSeg(StringBuffer sql, ClienteSeg clienteSeg) {
		sql.append(" JOIN TBLVPCLIENTESEG CLISEG ON TB.CDEMPRESA = CLISEG.CDEMPRESA")
				.append(" AND CLISEG.CDREPRESENTANTE = ").append(Sql.getValue(clienteSeg.cdRepresentante))
				.append(" AND TB.CDSEGMENTO = CLISEG.CDSEGMENTO AND CLISEG.CDCLIENTE = ")
				.append(Sql.getValue(clienteSeg.cdCliente));
	}
	
	public static void addExistsTranspTipoPed(StringBuffer sql, TranspTipoPed transpTipoPed) {
		sql.append(" AND (EXISTS (SELECT 1 FROM TBLVPTRANSPTIPOPED TAB WHERE TB.CDEMPRESA = TAB.CDEMPRESA")
			.append(" AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(transpTipoPed.cdRepresentante))
			.append(" AND TB.CDTRANSPORTADORA = TAB.CDTRANSPORTADORA AND TAB.CDTIPOPEDIDO = ")
			.append(Sql.getValue(transpTipoPed.cdTipoPedido)).append(")");
		sql.append(" OR NOT EXISTS (SELECT 1 FROM TBLVPTRANSPTIPOPED TAB WHERE")
			.append(" TB.CDEMPRESA = TAB.CDEMPRESA AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(transpTipoPed.cdRepresentante))
			.append(" AND TAB.CDTIPOPEDIDO = ").append(Sql.getValue(transpTipoPed.cdTipoPedido)).append("))");
	}
	
	public static String getQtMinimaItemPedidoCondition(double qtMinimaItem, boolean itemPedidoErp) {
		SqlWhereClause sqlW = new SqlWhereClause();
		StringBuffer sql = new StringBuffer();
		String tableName = itemPedidoErp ? ItemPedido.TABLE_NAME_ITEMPEDIDOERP : ItemPedido.TABLE_NAME_ITEMPEDIDO;
		sql.append(Sql.getValue(qtMinimaItem))
			.append(" <= (SELECT SUM(QTITEMFISICO) FROM ").append(tableName).append(" I ");
		sqlW.addAndCondition("I.CDEMPRESA = TB.CDEMPRESA");
		sqlW.addAndCondition("I.CDREPRESENTANTE = TB.CDREPRESENTANTE");
		sqlW.addAndCondition("I.FLORIGEMPEDIDO = TB.FLORIGEMPEDIDO");
		sqlW.addAndCondition("I.NUPEDIDO = TB.NUPEDIDO");
		sql.append(sqlW.getSql()).append(")");
		
		return sql.toString();
	}
	
	public static String getExistsGrupoProdTipoPedCondition(GrupoProdTipoPed grupoProdTipoPed, int nivelGrupoProd) {
		StringBuffer sql = new StringBuffer();
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sql.append("(");
		if (grupoProdTipoPed.excecaoGrupoProdutoFilter) {
			sql.append("NOT ");
		}
		sql.append("EXISTS (SELECT 1 FROM TBLVPGRUPOPRODTIPOPED TAB ");
		sqlWhereClause.addAndConditionEquals("TAB.CDEMPRESA", grupoProdTipoPed.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("TAB.CDREPRESENTANTE", grupoProdTipoPed.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("TAB.CDTIPOPEDIDO", grupoProdTipoPed.cdTipoPedido);	
		for (int i = 1; i <= nivelGrupoProd; i++) {
			String colunaGrupoProduto = "CDGRUPOPRODUTO" + i;
			sqlWhereClause.addAndCondition("TAB." + colunaGrupoProduto + " = TB." + colunaGrupoProduto);
		}
		if (grupoProdTipoPed.excecaoGrupoProdutoFilter && nivelGrupoProd < 3) {
			if (nivelGrupoProd < 2) {
				sqlWhereClause.addAndConditionEquals("TAB.CDGRUPOPRODUTO2",GrupoProduto1.CD_GRUPO_PRODUTO_VAZIO);
			}
			sqlWhereClause.addAndConditionEquals("TAB.CDGRUPOPRODUTO3",GrupoProduto1.CD_GRUPO_PRODUTO_VAZIO);
		}
		sql.append(sqlWhereClause.getSql());
		sqlWhereClause = new SqlWhereClause();
		
		sql.append(") OR NOT EXISTS (SELECT 1 FROM TBLVPGRUPOPRODTIPOPED TAB ");
		sqlWhereClause.addAndConditionEquals("TAB.CDEMPRESA", grupoProdTipoPed.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("TAB.CDREPRESENTANTE", grupoProdTipoPed.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("TAB.CDTIPOPEDIDO", grupoProdTipoPed.cdTipoPedido);
		if (nivelGrupoProd > 1) {
			sqlWhereClause.addAndConditionEquals("TAB.CDGRUPOPRODUTO1", grupoProdTipoPed.cdGrupoProduto1);
			if (nivelGrupoProd > 2) {
				sqlWhereClause.addAndConditionEquals("TAB.CDGRUPOPRODUTO2", grupoProdTipoPed.cdGrupoProduto2);
			}
		}
		sql.append(sqlWhereClause.getSql());
		sql.append("))");
		
		return sql.toString();
	}
	
	public static String getExistsGrupoProdFornCondition(GrupoProdForn grupoProdForn) {
		StringBuffer sql = new StringBuffer();
		sql.append("(EXISTS (SELECT 1 FROM TBLVPGRUPOPRODFORN TAB WHERE TAB.CDEMPRESA = ").append(Sql.getValue(grupoProdForn.cdEmpresa));
		if (ValueUtil.isNotEmpty(grupoProdForn.cdFornecedor)) {
			sql.append(" AND TAB.CDFORNECEDOR = ").append(Sql.getValue(grupoProdForn.cdFornecedor));
		}
		sql.append(" AND TB.CDGRUPOPRODUTO1 = TAB.CDGRUPOPRODUTO1)");
		sql.append(" OR NOT EXISTS (SELECT 1 FROM TBLVPGRUPOPRODFORN TAB WHERE")
				.append(" TAB.CDEMPRESA = ").append(Sql.getValue(grupoProdForn.cdEmpresa)).append("))");
		
		return sql.toString();
	}
	
	public static void addJoinTipoFreteCli(StringBuffer sql, TipoFreteCli tipoFreteCli) {
		sql.append(" JOIN TBLVPTIPOFRETECLI ").append(ALIAS_TIPOFRETECLI)
			.append(" ON TB.CDEMPRESA = ").append(ALIAS_TIPOFRETECLI).append(".CDEMPRESA")
			.append(" AND ").append(ALIAS_TIPOFRETECLI).append(".CDREPRESENTANTE = ").append(Sql.getValue(tipoFreteCli.cdRepresentante))
			.append(" AND TB.CDTIPOFRETE = ").append(ALIAS_TIPOFRETECLI).append(".CDTIPOFRETE")
			.append(" AND ").append(ALIAS_TIPOFRETECLI).append(".CDCLIENTE = ").append(Sql.getValue(tipoFreteCli.cdCliente));
	}
	
	public static String getNotExistsTipoFreteCliCondition(TipoFreteCli tipoFreteCli) {
		SqlWhereClause sqlWhereClause = new SqlWhereClause(); 
		StringBuffer sql = new StringBuffer();
		sql.append(" NOT EXISTS (SELECT 1 FROM TBLVPTIPOFRETECLI TAB ");
		sqlWhereClause.addAndCondition("TAB.CDEMPRESA = TB.CDEMPRESA");
		sqlWhereClause.addAndConditionEquals("TAB.CDREPRESENTANTE", tipoFreteCli.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("TAB.CDCLIENTE", tipoFreteCli.cdCliente);
		sql.append(sqlWhereClause.getSql()).append(")");
		
		return sql.toString();
	}
	
	public static String getCestaProdutoMetaNaoAtingidaCondition(CestaPositProduto cestaPositProduto) {
		StringBuffer sql = new StringBuffer();
		sql.append(" EXISTS (SELECT 1 FROM TBLVPCESTAPOSITPRODUTO TAB WHERE TAB.CDEMPRESA = TB.CDEMPRESA ");
		sql.append(" AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(cestaPositProduto.cdRepresentante));
		sql.append(" AND TAB.CDPRODUTO = TB.CDPRODUTO ");
		sql.append(" AND TAB.CDCESTA = ").append(Sql.getValue(cestaPositProduto.cdCesta));
		sql.append(" AND TAB.CDCLIENTE = ").append(Sql.getValue(cestaPositProduto.cdCliente));
		sql.append(" AND TAB.VLREALIZADO < TAB.VLMETA)");
		return sql.toString();	
	}
	
	public static String getCestaProdutoCondition(CestaProduto cestaProduto) {
		StringBuffer sql = new StringBuffer();
		sql.append(" EXISTS (SELECT 1 FROM TBLVPCESTAPRODUTO TAB WHERE TAB.CDEMPRESA = TB.CDEMPRESA ");
		sql.append(" AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(cestaProduto.cdRepresentante));
		sql.append(" AND TAB.CDCESTA = ").append(Sql.getValue(cestaProduto.cdCesta));
		sql.append(" AND TAB.CDPRODUTO = TB.CDPRODUTO)");
		return sql.toString();	
	}
	
	public static String getExistsGrupoProdTipoPedProdutoListCondition(GrupoProdTipoPed grupoProdTipoPed) {
		StringBuffer sql = new StringBuffer();
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sql.append("(");
		if (grupoProdTipoPed.excecaoGrupoProdutoFilter) {
			sql.append("NOT ");
		}
		sql.append("EXISTS (SELECT 1 FROM TBLVPGRUPOPRODTIPOPED TAB ");
		sqlWhereClause.addAndConditionEquals("TAB.CDEMPRESA", grupoProdTipoPed.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("TAB.CDREPRESENTANTE", grupoProdTipoPed.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("TAB.CDTIPOPEDIDO", grupoProdTipoPed.cdTipoPedido);	
		
		sqlWhereClause.addStartAndMultipleCondition();
		sqlWhereClause.addAndCondition("TAB.CDGRUPOPRODUTO1 = TB.CDGRUPOPRODUTO1");
		sqlWhereClause.addOrCondition("TAB.CDGRUPOPRODUTO1 = ", GrupoProduto1.CD_GRUPO_PRODUTO_VAZIO);
		sqlWhereClause.addEndMultipleCondition();
		
		sqlWhereClause.addStartAndMultipleCondition();
		sqlWhereClause.addAndCondition("TAB.CDGRUPOPRODUTO2 = TB.CDGRUPOPRODUTO2");
		sqlWhereClause.addOrCondition("TAB.CDGRUPOPRODUTO2 = ", GrupoProduto1.CD_GRUPO_PRODUTO_VAZIO);
		sqlWhereClause.addEndMultipleCondition();
		
		sqlWhereClause.addStartAndMultipleCondition();
		sqlWhereClause.addAndCondition("TAB.CDGRUPOPRODUTO3 = TB.CDGRUPOPRODUTO3");
		sqlWhereClause.addOrCondition("TAB.CDGRUPOPRODUTO3 = ", GrupoProduto1.CD_GRUPO_PRODUTO_VAZIO);
		sqlWhereClause.addEndMultipleCondition();
		
		sql.append(sqlWhereClause.getSql());
		sqlWhereClause = new SqlWhereClause();
		
		sql.append(") OR NOT EXISTS (SELECT 1 FROM TBLVPGRUPOPRODTIPOPED TAB ");
		sqlWhereClause.addAndConditionEquals("TAB.CDEMPRESA", grupoProdTipoPed.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("TAB.CDREPRESENTANTE", grupoProdTipoPed.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("TAB.CDTIPOPEDIDO", grupoProdTipoPed.cdTipoPedido);
		sqlWhereClause.addAndConditionEquals("TAB.CDGRUPOPRODUTO1", grupoProdTipoPed.cdGrupoProduto1);
		sqlWhereClause.addAndConditionEquals("TAB.CDGRUPOPRODUTO2", grupoProdTipoPed.cdGrupoProduto2);
		sqlWhereClause.addAndConditionEquals("TAB.CDGRUPOPRODUTO3", grupoProdTipoPed.cdGrupoProduto3);
		sql.append(sqlWhereClause.getSql());
		sql.append("))");
		
		return sql.toString();
	}
	
	public static String getExistsProdutoUnidadeCondition(ProdutoUnidade produtoUnidade) {
		StringBuffer sql = new StringBuffer();
		sql.append(" EXISTS (SELECT 1 FROM TBLVPPRODUTOUNIDADE TAB WHERE TAB.CDEMPRESA = TB.CDEMPRESA ");
		sql.append(" AND TAB.CDREPRESENTANTE = ").append(Sql.getValue(produtoUnidade.cdRepresentante));
		sql.append(" AND TAB.CDUNIDADE = ").append(Sql.getValue(produtoUnidade.cdUnidade));
		sql.append(" AND TAB.CDPRODUTO = TB.CDPRODUTO)");
		return sql.toString();	
	}
	
	public static void addCTEsProdutoCliente(StringBuffer sql, ProdutoCliente produtoCliente) {
		addInicioSentencaCTE(sql);
		ProdutoClienteDbxDao.getInstance().addCTEsProdutoTipoRelacao(sql, produtoCliente);
	}
	
	public static void addCTEsClienteProduto(StringBuffer sql, ClienteProduto clienteProduto) {
		addInicioSentencaCTE(sql);
		ClienteProdutoDbxDao.getInstance().addCTEsProdutoTipoRelacao(sql, clienteProduto);
	}
	
	public static void addCTEsProdutoCondPagto(StringBuffer sql, ProdutoCondPagto produtoCondPagto) {
		addInicioSentencaCTE(sql);
		ProdutoCondPagtoDbxDao.getInstance().addCTEsProdutoTipoRelacao(sql, produtoCondPagto);
	}
	
	private static void addInicioSentencaCTE(StringBuffer sql) {
		if (sql.toString().startsWith("WITH")) {
			sql.append(", ");
		} else {
			sql.append("WITH ");
		}
	}
	
	public static void addJoinCTEProdutoCliente(ProdutoCliente produtoClienteFilter, StringBuffer sql) {
		ProdutoClienteDbxDao.getInstance().addJoinCTEProdutoClienteBaseDomain(sql, produtoClienteFilter);
	}
	
	public static void addJoinCTEClienteProduto(ClienteProduto clienteProdutoFilter, StringBuffer sql) {
		ClienteProdutoDbxDao.getInstance().addJoinCTEProdutoClienteBaseDomain(sql, clienteProdutoFilter);
	}
	
	public static void addJoinCTEProdutoCondPagto(ProdutoCondPagto produtoCondPagtoFilter, StringBuffer sql) {
		ProdutoCondPagtoDbxDao.getInstance().addJoinCTEProdutoClienteBaseDomain(sql, produtoCondPagtoFilter);
	}
	
	public static String getExistsProdutoMenuCategoriaCondition(ProdutoMenuCategoria produtoMenuCategoria) {
		SqlWhereClause sqlWhereClause = new SqlWhereClause(); 
		StringBuffer sql = new StringBuffer();
		sql.append(" EXISTS (SELECT 1 FROM TBLVPPRODUTOMENUCATEGORIA TAB ");
		sqlWhereClause.addAndCondition("TAB.CDEMPRESA = TB.CDEMPRESA");
		sqlWhereClause.addAndConditionEquals("TAB.CDREPRESENTANTE", produtoMenuCategoria.cdRepresentante);
		sqlWhereClause.addAndCondition("TAB.CDPRODUTO = TB.CDPRODUTO");
		sqlWhereClause.addAndConditionIn("TAB.CDMENU", produtoMenuCategoria.cdMenuListFilter);
		sql.append(sqlWhereClause.getSql()).append(")");
		return sql.toString();
	}
	
	public static String getExistsItemPedidoOrItemPedidoErpCondition(ItemPedido itemPedido, boolean includeItemPedidoErp, boolean notExists) {
		StringBuffer sql = new StringBuffer();
		sql.append(" (");
		addSqlExistsItemPedidoCondition(sql, itemPedido, ItemPedido.TABLE_NAME_ITEMPEDIDO, notExists);
		if (includeItemPedidoErp) {
			if (!notExists) {
				sql.append(" OR");
			} else {
				sql.append(" AND");
			}
			addSqlExistsItemPedidoCondition(sql, itemPedido, ItemPedido.TABLE_NAME_ITEMPEDIDOERP, notExists);
		}
		sql.append(") ");
		return sql.toString();
	}

	private static void addSqlExistsItemPedidoCondition(StringBuffer sql, ItemPedido itemPedido, String tableName, boolean notExists) {
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		if (notExists) {
			sql.append(" NOT");
		}
		sql.append(" EXISTS (SELECT 1 FROM ").append(tableName).append(" TAB ");
		sqlWhereClause.addAndCondition("TAB.CDEMPRESA = TB.CDEMPRESA");
		sqlWhereClause.addAndConditionEquals("TAB.CDREPRESENTANTE", itemPedido.cdRepresentante);
		sqlWhereClause.addAndConditionIn("TAB.NUPEDIDO", itemPedido.nuPedidoListFilter);
		sqlWhereClause.addAndCondition("TAB.CDPRODUTO = TB.CDPRODUTO");
		sql.append(sqlWhereClause.getSql()).append(")");
	}
	
	public static void addAndMultipleOrLikeCondition(SqlWhereClause sqlWhereClause, String[] stringValueList, String columnName, String separatorValue) {
   		if (ValueUtil.isNotEmpty(stringValueList)){
   			sqlWhereClause.addStartAndMultipleCondition();
   			StringBuffer sb = new StringBuffer();
			sb.append(Sql.getValue(separatorValue))
				.append(" || ").append(columnName).append(" || ")
				.append(Sql.getValue(separatorValue));
			String columnWithSeparator = sb.toString();
   			for (int i = 0; i < stringValueList.length; i++) {
   				sb = new StringBuffer();
   				sb.append(separatorValue).append(StringUtil.getStringValue(stringValueList[i])).append(separatorValue);
   				String valueWithSeparator = sb.toString();
   				sqlWhereClause.addOrLikeCondition(columnWithSeparator, valueWithSeparator);
   			}
   			sqlWhereClause.addEndMultipleCondition();
   		}
	}
	
	public static void addJoinTransportadora(StringBuffer sql) {
		sql.append(" JOIN TBLVPTRANSPORTADORA TR ON TB.CDEMPRESA = TR.CDEMPRESA")
			.append(" AND TR.CDREPRESENTANTE = ").append(Sql.getValue(SessionLavenderePda.getCdRepresentanteFiltroDados(Transportadora.class)))
			.append(" AND TB.CDTRANSPORTADORA = TR.CDTRANSPORTADORA ");
	}
	
	public static void addJoinRegiao(StringBuffer sql) {
		sql.append(" LEFT JOIN TBLVPREGIAO ").append(ALIAS_REGIAO)
		.append(" ON TB.CDREGIAO = ").append(ALIAS_REGIAO).append(".CDREGIAO ");
	}
	
	public static void addJoinTipoFrete(StringBuffer sql, TipoFrete tipoFrete) {
		sql.append(" LEFT JOIN TBLVPTIPOFRETE TFRETE ON TB.CDEMPRESA = TFRETE.CDEMPRESA")
				.append(" AND TFRETE.CDREPRESENTANTE = ").append(Sql.getValue(tipoFrete.cdRepresentante))
				.append(" AND TB.CDTIPOFRETE = TFRETE.CDTIPOFRETE AND TFRETE.CDUF = ")
				.append(Sql.getValue(tipoFrete.cdUf));
	}
	
	public static void addJoinUnidade(StringBuffer sql, Unidade unidade) {
		sql.append(" LEFT JOIN TBLVPUNIDADE ").append(ALIAS_UNIDADE).append(" ON TB.CDEMPRESA = ").append(ALIAS_UNIDADE).append(".CDEMPRESA")
		.append(" AND ").append(ALIAS_UNIDADE).append(".CDREPRESENTANTE = ").append(Sql.getValue(unidade.cdRepresentante))
		.append(" AND TB.CDUNIDADE = ").append(ALIAS_UNIDADE).append(".CDUNIDADE");
	}
	
	public static String getRegiaoJoinConditions(Regiao regiao) {
		StringBuffer sql = new StringBuffer();
		RegiaoDbxDao.getInstance().addWhereByExampleInJoin(regiao, sql);
		return sql.toString();
	}
	
	public static String getExistsStatusOrcamentoCondition(StatusOrcamento statusOrcamento) {
		SqlWhereClause sqlWhereClause = new SqlWhereClause(); 
		StringBuffer sql = new StringBuffer();
		sql.append(" EXISTS (SELECT 1 FROM TBLVPSTATUSORCAMENTO TAB ");
		sqlWhereClause.addAndCondition("TAB.CDEMPRESA = TB.CDEMPRESA");
		sqlWhereClause.addAndConditionEquals("TAB.CDREPRESENTANTE", statusOrcamento.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("TAB.FLSTATUSPREORCAMENTO", statusOrcamento.flStatusPreOrcamento);
		sql.append(sqlWhereClause.getSql()).append(")");
		return sql.toString();
	}
	
	public static String getExistsGiroProdutoCondition(GiroProduto giroProduto) {
		SqlWhereClause sqlWhereClause = new SqlWhereClause(); 
		StringBuffer sql = new StringBuffer();
		sql.append(" EXISTS (SELECT 1 FROM TBLVPGIROPRODUTO TAB ");
		sqlWhereClause.addAndCondition("TAB.CDEMPRESA = TB.CDEMPRESA");
		sqlWhereClause.addAndConditionEquals("TAB.CDREPRESENTANTE", giroProduto.cdRepresentante);
		sqlWhereClause.addAndCondition("TAB.CDPRODUTO = TB.CDPRODUTO");
		sqlWhereClause.addAndConditionEquals("TAB.CDCLIENTE", giroProduto.cdCliente);
		sql.append(sqlWhereClause.getSql()).append(")");
		return sql.toString();
	}
	
	public static String getExistsInItemPedido(ItemPedido itemPedido, boolean itemPedidoErp) {
		SqlWhereClause sqlWhereClause = new SqlWhereClause(); 
		StringBuffer sql = new StringBuffer();
		String tableName = itemPedidoErp ? ItemPedido.TABLE_NAME_ITEMPEDIDOERP : ItemPedido.TABLE_NAME_ITEMPEDIDO;
		sql.append(" EXISTS (SELECT 1 FROM ").append(tableName).append(" TAB ");
		sqlWhereClause.addAndCondition("TAB.CDEMPRESA = TB.CDEMPRESA");
		sqlWhereClause.addAndCondition("TAB.CDREPRESENTANTE = TB.CDREPRESENTANTE");
		sqlWhereClause.addAndCondition("TAB.FLORIGEMPEDIDO = TB.FLORIGEMPEDIDO");
		sqlWhereClause.addAndCondition("TAB.NUPEDIDO = TB.NUPEDIDO");
		sqlWhereClause.addAndConditionEquals("TAB.CDPRODUTO", itemPedido.cdProduto);
		sql.append(sqlWhereClause.getSql()).append(")");
		return sql.toString();
	}
	
	public static void addAndFlExcecaoProdutoTipoPedCondition(ProdutoBase produto, SqlWhereClause sqlWhereClause) {
		if (ValueUtil.isNotEmpty(produto.cdTipoPedidoFilter)) {
			StringBuffer sb = new StringBuffer();
			sb.append("((").append(Sql.getValue(produto.flExcecaoProduto)).append(" = 'S' AND ").append(ALIAS_PRODUTOTIPOPED).append(".CDPRODUTO IS NULL) OR ")
			.append("(").append(Sql.getValue(produto.flExcecaoProduto)).append(" = 'N' AND ").append(ALIAS_PRODUTOTIPOPED).append(".CDPRODUTO IS NOT NULL))");
			sqlWhereClause.addAndCondition(sb.toString());
		}
	}
	
	public static String getExistsNfeCondition() {
		SqlWhereClause sqlWhereClause = new SqlWhereClause(); 
		StringBuffer sql = new StringBuffer();
		sql.append(" EXISTS (SELECT 1 FROM TBLVPNFE TAB ");
		sqlWhereClause.addAndCondition("TAB.CDEMPRESA = TB.CDEMPRESA");
		sqlWhereClause.addAndCondition("TAB.CDREPRESENTANTE = TB.CDREPRESENTANTE");
		sqlWhereClause.addAndCondition("TAB.NUPEDIDO = TB.NUPEDIDO");
		sqlWhereClause.addAndCondition("TAB.FLORIGEMPEDIDO = TB.FLORIGEMPEDIDO");
		sql.append(sqlWhereClause.getSql()).append(")");
		
		return sql.toString();
	}
	
	public static String getExistsSupervisorRepCondition(SupervisorRep supervisorRep) {
		SqlWhereClause sqlWhereClause = new SqlWhereClause(); 
		StringBuffer sql = new StringBuffer();
		sql.append(" (EXISTS (SELECT 1 FROM TBLVPSUPERVISORREP TAB ");
		sqlWhereClause.addAndCondition("TAB.CDEMPRESA = TB.CDEMPRESA");
		sqlWhereClause.addAndConditionEquals("TAB.CDSUPERVISOR", supervisorRep.cdSupervisor);
		sqlWhereClause.addAndCondition("TAB.CDREPRESENTANTE = TB.CDREPRESENTANTE");
		sql.append(sqlWhereClause.getSql()).append(")");

		sql.append(" OR TB.CDREPRESENTANTE = ").append(Sql.getValue(supervisorRep.cdSupervisor)).append(")");
		
		return sql.toString();
	}
	
	/* Verifica se o pedido aberto de orçamento emitido e copiado para a pedidoerp para ser enviado para os supervisores não está presente no aplicativo */
	public static String getNotExistsPedidoPdaAbertoByCopiaPedidoErp() {
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		StringBuffer sql = new StringBuffer();
		sql.append("NOT EXISTS (SELECT 1 FROM TBLVPPEDIDO PEDAUX ");
		sqlWhereClause.addAndCondition("PEDAUX.CDEMPRESA = TB.CDEMPRESA");
		sqlWhereClause.addAndCondition("PEDAUX.CDREPRESENTANTE = TB.CDREPRESENTANTE");
		sqlWhereClause.addAndCondition("PEDAUX.NUPEDIDO = TB.NUPEDIDORELACIONADO");
		sqlWhereClause.addAndConditionEquals("PEDAUX.CDSTATUSPEDIDO", LavenderePdaConfig.cdStatusPedidoAberto);
		sql.append(sqlWhereClause.getSql()).append(")");
		return sql.toString();
	}
}
