package br.com.wmw.lavenderepda.integration.dao.pdbx;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.BasePersonDomain;
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
import br.com.wmw.lavenderepda.business.domain.Combo;
import br.com.wmw.lavenderepda.business.domain.CondComercialExcec;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.EstoqueDisponivel;
import br.com.wmw.lavenderepda.business.domain.FotoProduto;
import br.com.wmw.lavenderepda.business.domain.ItemCombo;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConfig;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoProduto;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoReg;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoDestaque;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.ProdutoMenuCategoria;
import br.com.wmw.lavenderepda.business.domain.ProdutoSimilar;
import br.com.wmw.lavenderepda.business.domain.RemessaEstoque;
import br.com.wmw.lavenderepda.business.domain.TributacaoConfig;
import br.com.wmw.lavenderepda.business.domain.Unidade;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ProdutoPdbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Produto();
	}

    private static ProdutoPdbxDao instance;

	private boolean hasItemTabelaPreco = true;
	private boolean populandoCombo;
	private boolean isAddAliasItemCombo;
	private boolean consideraUltimoProdutoFilter;

    public ProdutoPdbxDao() {
        super(Produto.TABLE_NAME);
    }

    public static ProdutoPdbxDao getInstance() {
        if (instance == null) {
            instance = new ProdutoPdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
    	final Produto produto = (Produto)domainFilter;
        sql.append(" TB.rowKey,");
        sql.append(" TB.CDEMPRESA,");
        sql.append(" TB.CDREPRESENTANTE,");
        sql.append(" TB.CDPRODUTO,");
        sql.append(" TB.DSPRODUTO,");
		sql.append(" TB.CDGRUPOPRODUTO1,");
		sql.append(" TB.CDGRUPOPRODUTO2,");
		sql.append(" TB.CDGRUPOPRODUTO3,");
		sql.append(" TB.CDGRUPOPRODUTO4,");
		sql.append(" TB.CDGRUPOPRODUTO5,");
		sql.append(" TB.CDUNIDADE,");
		sql.append(" TB.DSUNIDADEFISICA,");
		sql.append(" TB.NUMULTIPLOEMBALAGEM,");
		sql.append(" TB.FLCONSISTECONVERSAOUNIDADE,");
		sql.append(" TB.NUCFOPPRODUTO,");
		sql.append(" TB.FLPERMITEESTOQUENEGATIVO,");
		sql.append(" TB.FLESPECIAL,");
		sql.append(" TB.FLCIGARRO,");
		sql.append(" TB.DSNCMPRODUTO,");
		sql.append(" TB.DSCESTPRODUTO,");
		sql.append(" TB.DSEMBALAGEM,");
		sql.append(" TB.VLPRECOCUSTO,");
		sql.append(" TB.QTMINIMAVENDA,");
		sql.append(" TB.NUMULTIPLOIDEAL,");
		sql.append(" TB.FLUTILIZAVERBA,");
	    sql.append(" TB.VLLARGURAMIN,");
	    sql.append(" TB.VLLARGURAMAX,");
	    sql.append(" TB.VLCOMPRIMENTOMIN,");
	    sql.append(" TB.VLCOMPRIMENTOMAX,");
	    sql.append(" TB.CDUNIDADEPREFERENCIAL,");
	    sql.append(" TB.CDORIGEMMERCADORIA,");
	    sql.append(" TB.CDAGRUPADORSIMILARIDADE,");
	    sql.append(" TB.FLIGNORAPESO,");
	    sql.append(" TB.FLIGNORAVALIDACAO");
		if (LavenderePdaConfig.usaFiltroFornecedor || LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor()) {
			sql.append(", TB.CDFORNECEDOR");
		}
        if (LavenderePdaConfig.usaConversaoUnidadesMedida && (!LavenderePdaConfig.ocultaQtItemFaturamento || LavenderePdaConfig.usaTelaAdicionarItemAoPedidoEstiloDesktop)) {
			sql.append(", TB.DSUNIDADEFATURAMENTO");
		}
        if (LavenderePdaConfig.isUsaBloqueiaProdutoBloqueadoNoPedido() || LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito()) {
        	sql.append(", TB.FLSITUACAO");
        }
        if (LavenderePdaConfig.isMostraValorDaUnidadePrecoPorEmbalagem() || LavenderePdaConfig.usaConversaoUnidadesMedida || LavenderePdaConfig.usaUnidadeAlternativa || LavenderePdaConfig.mostraQtdPorEmbalagemProduto || LavenderePdaConfig.isMostraCampoValorUnitarioEmbalagemListaProdutosDentroPedido() || LavenderePdaConfig.isMostraPrecoItemStNaListaProduto()
				|| LavenderePdaConfig.isMostraPrecoItemStNaListaProdutoDoPedido() || LavenderePdaConfig.isMostraPrecoItemStNoDetalheDoProduto()) {
        	sql.append(", TB.NUCONVERSAOUNIDADESMEDIDA");
        }
		if (LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecial() || LavenderePdaConfig.aplicaMultiploEspecialAutoItemPedido || LavenderePdaConfig.usaControleEspecialEdicaoUnidades) {
			sql.append(", TB.NUMULTIPLOESPECIAL NUMULTIPLOESPECIAL");
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedido() || LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao || ValueUtil.isNotEmpty(LavenderePdaConfig.usaAvisoControleInsercaoItemPedido)) {
			sql.append(", TB.QTPESO");
		}
		if (LavenderePdaConfig.isBloqueiaClienteSemAlvaraProdutoControlado() || LavenderePdaConfig.isBloqueiaClienteSemLicencaProdutoControlado()) {
			sql.append(", TB.FLPRODUTOCONTROLADO");
		}
		if (LavenderePdaConfig.mostraVlPrecoFabrica) {
			sql.append(", TB.VLPRECOFABRICA");
		}
		if (LavenderePdaConfig.mostraVlPrecoMaximoConsumidorProduto()) {
			sql.append(", TB.VLPRECOMAXIMOCONSUMIDOR");
		}
		if (LavenderePdaConfig.aplicaIndiceFinanceiroCondPagtoLinhaProdItemPedido > 0 || LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda() 
				|| LavenderePdaConfig.isUsaPoliticaBonificacaoLinha() || LavenderePdaConfig.usaConfigMargemRentabilidade() || LavenderePdaConfig.usaPoliticaComercial()) {
			sql.append(", TB.CDLINHA");
		}
		if (LavenderePdaConfig.aplicaDescontoCCPPorItemFinalPedido || LavenderePdaConfig.validaDescontoCCPPorItem || LavenderePdaConfig.aplicaDescontoCCPAposInserirItem || LavenderePdaConfig.usaPoliticaComercial() || LavenderePdaConfig.usaQtMinProdTabPrecoEClasse()) {
			sql.append(", TB.CDCLASSE");
		}
		if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto()) {
			sql.append(", TB.DSPRINCIPIOATIVO");
		}
		if (LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado || LavenderePdaConfig.usaCalculoVerbaComImpostoERentabilidade || LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento()) {
			sql.append(", TB.CDTRIBUTACAOPRODUTO");
			sql.append(", TB.CDCLASSIFICFISCAL");
		}
		if (LavenderePdaConfig.usaModuloTrocaNoPedido || !ValueUtil.isEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher)) {
			sql.append(", TB.VLPCTDEPRECIACAOTROCA");
		}
		if (LavenderePdaConfig.calculaStSimplificadaItemPedido || LavenderePdaConfig.isUsaCalculaStItemPedido() || LavenderePdaConfig.isMostraCampoValorUnitarioEmbalagemListaProdutosDentroPedido()) {
			sql.append(", TB.VLST");
		}
    	if (LavenderePdaConfig.aplicaIndiceFinanceiroEspClientePorItemFinalPedido) {
    		sql.append(", TB.FLINDICEFINANCEIROESPECIAL");
    	}
    	if (LavenderePdaConfig.isPermiteBonificarProduto()) {
    		sql.append(", TB.FLBONIFICACAO");
    	}
		if (LavenderePdaConfig.mostraEmbalagemCompraProduto) {
			sql.append(", TB.QTEMBALAGEMCOMPRA");
		}
		if (LavenderePdaConfig.usaFatorCUMEspecialClienteCreditoAntecipado) {
			sql.append(", TB.NUCONVERSAOUMCREDITOANTECIPADO");
		}
		if (LavenderePdaConfig.usaAvisoPreAlta) {
			sql.append(", TB.FLPREALTACUSTO");
		}
		if (LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
			sql.append(", TB.FLKIT");
		}
		if (LavenderePdaConfig.usaFiltroMarcaProduto || LavenderePdaConfig.apresentaMarcaNasListasNoPedido()) {
			sql.append(", TB.DSMARCA");
		}
		if (LavenderePdaConfig.isMostraDescricaoReferencia() || LavenderePdaConfig.usaDsReferenciaNmFoto || LavenderePdaConfig.usaPoliticaComercial()) {
			sql.append(", TB.DSREFERENCIA");
        }
		if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem()) {
			sql.append(", TB.FLNEUTRO");
		}
		if (LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao) {
			sql.append(", TB.CDGRUPOBONIFICACAO");
			sql.append(", TB.CDPRODUTOBONIFICACAO");
			sql.append(", TB.VLPCTMAXBONIFICACAO");
		}
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() || LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao) {
			sql.append(", TB.FLAUTOBONIFICA");
		}
		if (LavenderePdaConfig.usaFretePedidoPorTranspTipoPedProd) {
			sql.append(", TB.VLPCTFRETE");
			sql.append(", TB.FLAPLICAFRETEAPENASVALORMINIMO");
		}
		if (LavenderePdaConfig.usaRestricaoVendaProdutoPorCliente() || LavenderePdaConfig.usaBloqueioAlteracaoPrecoPedidoPorCliente) {
			sql.append(", TB.CDRESTRICAOVENDAPROD");
		}
		if (LavenderePdaConfig.filtraProdutosPorOrigemPedido) {
			sql.append(", TB.CDORIGEMSETOR");
        }
		if (LavenderePdaConfig.isUsaGrupoDestaqueProduto()) {
			sql.append(", TB.CDGRUPODESTAQUE");
        }
		if (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() || LavenderePdaConfig.isUsaGrupoDescPromocionalNoDescQtdPorGrupo()) {
			sql.append(", TB.CDGRUPODESCPROD");
		}
		if (LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
			sql.append(", TB.VLIPI");
			sql.append(", TB.VLPCTIPI");
			sql.append(", TB.VLMINIPI");
		}
		if (LavenderePdaConfig.usaRentabilidadeMinimaItemPedido) {
			sql.append(", TB.VLPCTMINRENTABILIDADE");
		}
		if (!ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.usaPrecoPadraoListaProdutos)) {
			sql.append(", TB.VLPRECOPADRAO");
		}
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			sql.append(", TB.VLPCTPIS");
			sql.append(", TB.VLPCTCOFINS");
			sql.append(", TB.VLMINPIS");
			sql.append(", TB.VLMINCOFINS");
			sql.append(", TB.VLPCTICMS");
			sql.append(", TB.FLDEBITAPISCOFINSZONAFRANCA");
		}
		if (!ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido)) {
			sql.append(", TB.NURELEVANCIA");
		}
		if (LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil) {
			sql.append(", TB.FLLOTEOBRIGATORIO");
		}
		if (LavenderePdaConfig.usaSugestaoVendaProdutosPorFoto > 0) {
			sql.append(", TB.CDFATORCOMUM");
		}
		if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
			sql.append(", TB.VLVOLUME");
		}
		if (LavenderePdaConfig.isUsaMargemProdutoAplicadaNoValorBaseVerba()) {
			sql.append(", TB.VLPCTMARGEMPRODUTO");
		}
		if (LavenderePdaConfig.isUsaImpressaoContingenciaNfeViaBluetooth()) {
			sql.append(", TB.DSREFERENCIAPRODUTO");
		}
		if (LavenderePdaConfig.grifaProdutoComVidaUtilLoteCriticoNaLista) {
			sql.append(", TB.FLLOTEPRODUTOCRITICO");
		}
		if (LavenderePdaConfig.isUsaCalculoGastoTotalVariavelPorProduto()) {
			sql.append(", TB.VLPCTCUSTOVARIAVEL");
		}
		if (LavenderePdaConfig.ignoraValidacaoValorMinPedidoConformeProdutoVendido) {
			sql.append(", TB.FLIGNORAVALORMINPEDIDO");
		}
		if (LavenderePdaConfig.isGradeProdutoModoLista()) {
			sql.append(", TB.NUNIVELGRADEOBRIGATORIO");
		}
		if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido()) {
        	DaoUtil.getSelectSomaQtEstoqueNenhumaGrade(sql);
        }
		if (LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto) {
			sql.append(", TB.CDUNIDADEFRACAO")
			.append(", TB.NUFRACAO")
			.append(", TB.FLUSAUNIDADEBASEDSFRACAO");
		}
		if(LavenderePdaConfig.isUsaFiltroAplicacaoDoProduto()) {
			sql.append(", TB.DSAPLICACAOPRODUTO");
		}
		if(LavenderePdaConfig.usaFiltroCodigoAlternativoProduto) {
			sql.append(", TB.DSCODIGOALTERNATIVO");
		}
		if (LavenderePdaConfig.usaDescMaxProdCli) {
			sql.append(", TB.CDGRUPODESCMAXPROD");
		}
		if(LavenderePdaConfig.usaProdutoRestrito) {
			sql.append(", TB.FLPRODUTORESTRITO");
		}
		if (LavenderePdaConfig.usaConfigCalculoComissao()) {
			sql.append(", TB.VLPCTVARIACAOCOMISSAO");
		}
		if (LavenderePdaConfig.exibeFracaoEmbalagemFornecedorItemPedido) {
			sql.append(", TB.NUFRACAOFORNECEDOR");
		}
		if (LavenderePdaConfig.usaDescontoExtra) {
			sql.append(", TB.VLPCTMAXDESCEXTRA");
		}
		if (LavenderePdaConfig.calculaPrecoPorVolumeProduto) {
			sql.append(", TB.FLCALCULAPRECOVOLUME");
			sql.append(", TB.VLALTURAMIN");
			sql.append(", TB.VLALTURAMAX");
		}
		if (LavenderePdaConfig.usaPoliticaComercial() || LavenderePdaConfig.isUsaPoliticaBonificacaoConjuntos()) {
			sql.append(", TB.CDCONJUNTO");
    }
		if (LavenderePdaConfig.usaPoliticaComercial()) {
			sql.append(", TB.CDCOLECAO");
			sql.append(", TB.CDSTATUSCOLECAO");
			sql.append(", TB.FLVENDAENCERRADA");
		} else if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(", TB.CDSTATUSCOLECAO");
		}
		if (!produto.hideProductPicture) {
		if (LavenderePdaConfig.usaFotoProdutoPorEmpresa) {
			DaoUtil.getSelectNmFotoProdutoEmp(sql);
		} else if (LavenderePdaConfig.mostraFotoProduto || LavenderePdaConfig.isLoadImagesOnProdutoList()) {
			DaoUtil.getSelectNmFotoProduto(sql);
        }
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
			sql.append(", TB.VLGRAMATURA");
		}
		if (LavenderePdaConfig.usaControlePontuacao || LavenderePdaConfig.isInformaQtdSugeridaProdutoIndustria()) {
			sql.append(", TB.VLLITRO");
		}
		if (LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda()) {
			sql.append(", TB.CDMARCA");
		}
		if (LavenderePdaConfig.usaPedidoProdutoCritico) {
			sql.append(", TB.FLCRITICO");
		}
		if (LavenderePdaConfig.usaValidaConversaoFOB()) {
			sql.append(", TB.NUCONVERSAOFOB");
		}
	    if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && LavenderePdaConfig.liberaComSenhaPrecoProduto) {
		    sql.append(", TB.VLMINAUTORIZACAOPRECO");
	    }
	    if (LavenderePdaConfig.usaSimilarVendaProduto && LavenderePdaConfig.usaAgrupadorProdutoSimilar) {
		    sql.append(", TB.CDAGRUPPRODSIMILAR");
	    }
	    if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
		    sql.append(", TB.CDFAMILIADESCPROG ");
	    }
	    if (LavenderePdaConfig.usaFamiliaPadraoPoliticaComercial() || LavenderePdaConfig.isUsaPoliticaBonificacaoFamilia()) {
	    	sql.append(", TB.CDFAMILIAPADRAO");
	    }
	    if (LavenderePdaConfig.mostraDimensoesNosDetalhes()) {
			sql.append(", DSDIMENSOES");
		}
	    if (LavenderePdaConfig.usaGradeProduto5() && produto.filtrandoAgrupadorGrade) {
			sql.append(", CASE WHEN PRODUTOGRADE.CDPRODUTO IS NOT NULL THEN TB.DSAGRUPADORGRADE ELSE NULL END DSAGRUPADORGRADE");
		}
	    if (LavenderePdaConfig.usaCotacaoMoedaProduto) {
	    	sql.append(", TB.DSMOEDAPRODUTO");
	    }
	    if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorItensFormulaJuros()) {
	    	sql.append(", TB.FLFORMULACALCULODEFLATOR");
	    	sql.append(", TB.VLPCTJUROSMENSAL");
            sql.append(", TB.QTDIASCARENCIAJUROS");
	    }
		if (LavenderePdaConfig.apresentaDsFichaTecnicaCapaItemPedido) {
			sql.append(", TB.DSFICHATECNICA");
		}
		if (LavenderePdaConfig.isMostraQtVendasProdutoNoPeriodo()) {
			sql.append(", TB.QTVENDASPERIODO");
		}
    }

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	final Produto filter = (Produto)domainFilter;
    	Produto produto = new Produto();
    	
        produto.rowKey = rs.getString(1);
        produto.cdEmpresa = rs.getString(2);
        produto.cdRepresentante = rs.getString(3);
        produto.cdProduto = rs.getString(4);
        produto.dsProduto = rs.getString(5);
        produto.cdGrupoProduto1 = rs.getString(6);
        produto.cdGrupoProduto2 = rs.getString(7);
        produto.cdGrupoProduto3 = rs.getString(8);
        produto.cdGrupoProduto4 = rs.getString(9);
        produto.cdGrupoProduto5 = rs.getString(10);
        produto.cdUnidade = rs.getString(11);
        produto.dsUnidadeFisica = rs.getString(12);
        produto.nuMultiploEmbalagem = rs.getDouble(13);
        produto.flConsisteConversaoUnidade = rs.getString(14);
        produto.nuCfopProduto = rs.getString(15);
        produto.flPermiteEstoqueNegativo = rs.getString(16);
        produto.flEspecial = rs.getString(17);
        produto.flCigarro = rs.getString(18);
        produto.dsNcmProduto = rs.getString(19);
        produto.dsCestProduto = rs.getString(20);
        produto.dsEmbalagem = rs.getString(21);
        produto.vlPrecoCusto = rs.getDouble(22);
        produto.qtMinimaVenda = rs.getDouble(23);
        produto.nuMultiploIdeal = rs.getDouble(24);
        produto.flUtilizaVerba = rs.getString(25);
	    produto.vlLarguraMin = rs.getDouble(26);
	    produto.vlLarguraMax = rs.getDouble(27);
	    produto.vlComprimentoMin = rs.getDouble(28);
	    produto.vlComprimentoMax = rs.getDouble(29);
	    produto.cdUnidadePreferencial = rs.getString(30);
	    produto.cdOrigemMercadoria = rs.getString(31);
	    produto.cdAgrupadorSimilaridade = rs.getString(32);
	    produto.flIgnoraPeso = rs.getString(33);
	    produto.flIgnoraValidacao = rs.getString(34);
        int i = 35;
        if (LavenderePdaConfig.usaFiltroFornecedor || LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor()) {
        	produto.cdFornecedor = rs.getString(i++);
        }
        if (LavenderePdaConfig.usaConversaoUnidadesMedida && (!LavenderePdaConfig.ocultaQtItemFaturamento  || LavenderePdaConfig.usaTelaAdicionarItemAoPedidoEstiloDesktop)) {
			produto.dsUnidadeFaturamento = rs.getString(i++);
		}
        if (LavenderePdaConfig.isUsaBloqueiaProdutoBloqueadoNoPedido() || LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito()) {
        	produto.flSituacao = rs.getInt(i++);
        }
        if (LavenderePdaConfig.isMostraValorDaUnidadePrecoPorEmbalagem() || LavenderePdaConfig.usaConversaoUnidadesMedida || LavenderePdaConfig.usaUnidadeAlternativa || LavenderePdaConfig.mostraQtdPorEmbalagemProduto || LavenderePdaConfig.isMostraCampoValorUnitarioEmbalagemListaProdutosDentroPedido() || LavenderePdaConfig.isMostraPrecoItemStNaListaProduto()
				|| LavenderePdaConfig.isMostraPrecoItemStNaListaProdutoDoPedido() || LavenderePdaConfig.isMostraPrecoItemStNoDetalheDoProduto()) {
        	produto.nuConversaoUnidadesMedida = rs.getDouble(i++);
        }
		if (LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecial() || LavenderePdaConfig.aplicaMultiploEspecialAutoItemPedido || LavenderePdaConfig.usaControleEspecialEdicaoUnidades) {
			produto.nuMultiploEspecial = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedido() || LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao || ValueUtil.isNotEmpty(LavenderePdaConfig.usaAvisoControleInsercaoItemPedido)) {
			produto.qtPeso = ValueUtil.round(rs.getDouble(i++));
		}
		if (LavenderePdaConfig.isBloqueiaClienteSemAlvaraProdutoControlado() || LavenderePdaConfig.isBloqueiaClienteSemLicencaProdutoControlado()) {
			produto.flProdutoControlado = rs.getString(i++);
		}
		if (LavenderePdaConfig.mostraVlPrecoFabrica) {
			produto.vlPrecoFabrica = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.mostraVlPrecoMaximoConsumidorProduto()) {
			produto.vlPrecoMaximoConsumidor = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.aplicaIndiceFinanceiroCondPagtoLinhaProdItemPedido > 0 || LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda() 
				|| LavenderePdaConfig.isUsaPoliticaBonificacaoLinha() || LavenderePdaConfig.usaConfigMargemRentabilidade() || LavenderePdaConfig.usaPoliticaComercial()) {
			produto.cdLinha = rs.getString(i++);
		}
		if (LavenderePdaConfig.aplicaDescontoCCPPorItemFinalPedido || LavenderePdaConfig.validaDescontoCCPPorItem || LavenderePdaConfig.aplicaDescontoCCPAposInserirItem || LavenderePdaConfig.usaPoliticaComercial() || LavenderePdaConfig.usaQtMinProdTabPrecoEClasse()) {
			produto.cdClasse = rs.getString(i++);
		}
		if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto()) {
			produto.dsPrincipioAtivo = rs.getString(i++);
		}
		if (LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado || LavenderePdaConfig.usaCalculoVerbaComImpostoERentabilidade || LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento()) {
			produto.cdTributacaoProduto = rs.getString(i++);
			produto.cdClassificFiscal = rs.getString(i++);
		}
		if (LavenderePdaConfig.usaModuloTrocaNoPedido || !ValueUtil.isEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher)) {
			produto.vlPctDepreciacaoTroca = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.calculaStSimplificadaItemPedido || LavenderePdaConfig.isUsaCalculaStItemPedido() || LavenderePdaConfig.isMostraCampoValorUnitarioEmbalagemListaProdutosDentroPedido()) {
			produto.vlSt = rs.getDouble(i++);
		}
    	if (LavenderePdaConfig.aplicaIndiceFinanceiroEspClientePorItemFinalPedido) {
    		produto.flIndiceFinanceiroEspecial = rs.getString(i++);
    	}
    	if (LavenderePdaConfig.isPermiteBonificarProduto()) {
    		produto.flBonificacao = rs.getString(i++);
    	}
		if (LavenderePdaConfig.mostraEmbalagemCompraProduto) {
			produto.qtEmbalagemCompra = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.usaFatorCUMEspecialClienteCreditoAntecipado) {
			produto.nuConversaoUMCreditoAntecipado = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.usaAvisoPreAlta) {
			produto.flPreAltaCusto = rs.getString(i++);
		}
		if (LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
			produto.flKit = rs.getString(i++);
		}
		if (LavenderePdaConfig.usaFiltroMarcaProduto || LavenderePdaConfig.apresentaMarcaNasListasNoPedido()) {
			produto.dsMarca = rs.getString(i++);
		}
		if (LavenderePdaConfig.isMostraDescricaoReferencia() || LavenderePdaConfig.usaDsReferenciaNmFoto || LavenderePdaConfig.usaPoliticaComercial()) {
			produto.dsReferencia = rs.getString(i++);
		}
		if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem()) {
			produto.flNeutro = rs.getString(i++);
		}
		if (LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao) {
			produto.cdGrupoBonificacao = rs.getString(i++);
			produto.cdProdutoBonificacao = rs.getString(i++);
			produto.vlPctMaxBonificacao = rs.getDouble(i++);
		}
		if ((LavenderePdaConfig.isUsaRentabilidadeNoPedido()) || LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao) {
			produto.flAutoBonifica = rs.getString(i++);
		}
		if (LavenderePdaConfig.usaFretePedidoPorTranspTipoPedProd) {
			produto.vlPctFrete = rs.getDouble(i++);
			produto.flAplicaFreteApenasValorMinimo = rs.getString(i++);
		}
		if (LavenderePdaConfig.usaRestricaoVendaProdutoPorCliente() || LavenderePdaConfig.usaBloqueioAlteracaoPrecoPedidoPorCliente) {
			produto.cdRestricaoVendaProd = rs.getString(i++);
		}
		if (LavenderePdaConfig.filtraProdutosPorOrigemPedido) {
			produto.cdOrigemSetor = rs.getString(i++);
        }
		if (LavenderePdaConfig.isUsaGrupoDestaqueProduto()) {
			produto.cdGrupoDestaque = rs.getString(i++);
        }
		if (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() || LavenderePdaConfig.isUsaGrupoDescPromocionalNoDescQtdPorGrupo()) {
			produto.cdGrupoDescProd = rs.getString(i++);
		}
		if (LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
			produto.vlIpi = rs.getDouble(i++);
			produto.vlPctIpi = rs.getDouble(i++);
			produto.vlMinIpi = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.usaRentabilidadeMinimaItemPedido) {
			produto.vlPctMinRentabilidade = rs.getDouble(i++);
		}
		if (!ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.usaPrecoPadraoListaProdutos)) {
			produto.vlPrecoPadrao = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			produto.vlPctPis = rs.getDouble(i++);
			produto.vlPctCofins = rs.getDouble(i++);
			produto.vlMinPis = rs.getDouble(i++);
			produto.vlMinCofins = rs.getDouble(i++);
			produto.vlPctIcms = rs.getDouble(i++);
			produto.flDebitaPisCofinsZonaFranca = rs.getString(i++);
		}
		if (!ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido)) {
			produto.nuRelevancia = rs.getInt(i++);
		}
		if (LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil) {
			produto.flLoteObrigatorio = rs.getString(i++);
		}
		if (LavenderePdaConfig.usaSugestaoVendaProdutosPorFoto > 0) {
			produto.cdFatorComum = rs.getString(i++);
		}
		if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
			produto.vlVolume = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.isUsaMargemProdutoAplicadaNoValorBaseVerba()) {
			produto.vlPctMargemProduto = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.isUsaImpressaoContingenciaNfeViaBluetooth()) {
			produto.dsReferenciaProduto = rs.getString(i++);
		}
		if (LavenderePdaConfig.grifaProdutoComVidaUtilLoteCriticoNaLista) {
			produto.flLoteProdutoCritico = rs.getString(i++);
		}
		if (LavenderePdaConfig.isUsaCalculoGastoTotalVariavelPorProduto()) {
			produto.vlPctCustoVariavel = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.ignoraValidacaoValorMinPedidoConformeProdutoVendido) {
			produto.flIgnoraValorMinPedido = rs.getString(i++);
		}
		if (LavenderePdaConfig.isGradeProdutoModoLista()) {
			produto.nuNivelGradeObrigatorio = rs.getInt(i++);
		}
		if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido()) {
        	produto.sumEstoqueGrades = rs.getDouble(i++);
        }
		if (LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto) {
			produto.cdUnidadeFracao = rs.getString(i++);
			produto.nuFracao = rs.getInt(i++);
			produto.flUsaUnidadeBaseDsFracao = rs.getString(i++);
		}
		if(LavenderePdaConfig.isUsaFiltroAplicacaoDoProduto()) {
			produto.dsAplicacaoProduto = rs.getString(i++);
		}
		if(LavenderePdaConfig.usaFiltroCodigoAlternativoProduto) {
			produto.dsCodigoAlternativo = rs.getString(i++);
		}
		if (LavenderePdaConfig.usaDescMaxProdCli) {
			produto.cdGrupoDescMaxProd = rs.getString(i++);
		}
		if(LavenderePdaConfig.usaProdutoRestrito) {
			produto.flProdutoRestrito = rs.getString(i++);
		}
		if (LavenderePdaConfig.usaConfigCalculoComissao()) {
			produto.vlPctVariacaoComissao = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.exibeFracaoEmbalagemFornecedorItemPedido) {
			produto.nuFracaoFornecedor = rs.getInt(i++);
		}
		if (LavenderePdaConfig.usaDescontoExtra) {
			produto.vlPctMaxDescExtra = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.calculaPrecoPorVolumeProduto) {
			produto.flCalculaPrecoVolume = rs.getString(i++);
			produto.vlAlturaMin = rs.getDouble(i++);
			produto.vlAlturaMax = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.usaPoliticaComercial() || LavenderePdaConfig.isUsaPoliticaBonificacaoConjuntos()) {
			produto.cdConjunto = rs.getString(i++);
		}
		if (LavenderePdaConfig.usaPoliticaComercial()) {
			produto.cdColecao = rs.getString(i++);
			produto.cdStatusColecao = rs.getString(i++);
			produto.flVendaEncerrada = rs.getString(i++);
		} else if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			produto.cdStatusColecao = rs.getString(i++);
		}
		if (!filter.hideProductPicture) {
		if (LavenderePdaConfig.usaFotoProdutoPorEmpresa || LavenderePdaConfig.mostraFotoProduto || LavenderePdaConfig.isLoadImagesOnProdutoList()) {
        	produto.fotoProduto = new FotoProduto();
        	produto.fotoProduto.nmFoto = rs.getString(i++);
        }
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
			produto.vlGramatura = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.usaControlePontuacao || LavenderePdaConfig.isInformaQtdSugeridaProdutoIndustria()) {
			produto.vlLitro = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda()) {
			produto.cdMarca = rs.getString(i++);
		}
		if (LavenderePdaConfig.usaPedidoProdutoCritico) {
			produto.flCritico = rs.getString(i++);
		}
	    if (LavenderePdaConfig.usaValidaConversaoFOB()) {
		    produto.nuConversaoFob = rs.getDouble(i++);
	    }
	    if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && LavenderePdaConfig.liberaComSenhaPrecoProduto) {
		    produto.vlMinAutorizacaoPreco = rs.getDouble(i++);
	    }
	    if (LavenderePdaConfig.usaSimilarVendaProduto && LavenderePdaConfig.usaAgrupadorProdutoSimilar) {
	    	produto.cdAgrupProdSimilar = rs.getString(i++);
	    }
	    if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
		    produto.cdFamiliaDescProg = rs.getString(i++);
	    }
	    if (LavenderePdaConfig.usaFamiliaPadraoPoliticaComercial() || LavenderePdaConfig.isUsaPoliticaBonificacaoFamilia()) {
		    produto.cdFamiliaPadrao = rs.getString(i++);
	    }
	    if (LavenderePdaConfig.mostraDimensoesNosDetalhes()) {
		    produto.dsDimensoes = rs.getString(i++);
	    }
	    if (LavenderePdaConfig.usaGradeProduto5() && filter.filtrandoAgrupadorGrade) {
	    	produto.setDsAgrupadorGrade(rs.getString(i++));
	    }
	    if (LavenderePdaConfig.usaCotacaoMoedaProduto) {
	    	produto.dsMoedaProduto = rs.getString(i++);
	    }
	    if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorItensFormulaJuros()) {
	    	produto.flFormulaCalculoDeflator = rs.getString(i++);
	    	produto.vlPctJurosMensal = rs.getDouble(i++);
	    	produto.qtDiasCarenciaJuros = rs.getInt(i++);
	    }
		if (LavenderePdaConfig.apresentaDsFichaTecnicaCapaItemPedido) {
			produto.dsFichaTecnica = rs.getString(i++);
		}
		if (LavenderePdaConfig.isMostraQtVendasProdutoNoPeriodo()) {
        	produto.qtVendasPeriodo = rs.getInt(i++);
        }
        return produto;
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Produto produto = (Produto) domain;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
       	sqlWhereClause.addAndCondition("TB.CDEMPRESA = ", produto.cdEmpresa);
       	sqlWhereClause.addAndCondition("TB.CDREPRESENTANTE = ", produto.cdRepresentante);
       	sqlWhereClause.addAndCondition("TB.CDFORNECEDOR = ", produto.cdFornecedor);
       	sqlWhereClause.addAndCondition("TB.CDGRUPOPRODUTO1 = ", produto.cdGrupoProduto1);
       	sqlWhereClause.addAndCondition("TB.CDGRUPOPRODUTO2 = ", produto.cdGrupoProduto2);
       	sqlWhereClause.addAndCondition("TB.CDGRUPOPRODUTO3 = ", produto.cdGrupoProduto3);
       	sqlWhereClause.addAndCondition("TB.CDGRUPOPRODUTO4 = ", produto.cdGrupoProduto4);
       	if (LavenderePdaConfig.isUsaGrupoDescPromocionalNoDescQtdPorGrupo()) {
       		sqlWhereClause.addAndCondition("TB.CDGRUPODESCPROD = ", produto.cdGrupoDescProd);
       	}
       	if (LavenderePdaConfig.isUsaGrupoDestaqueProduto()) {
	        String coluna = LavenderePdaConfig.usaGrupoDestaqueItemTabPreco ? DaoUtil.ALIAS_ITEMTABELAPRECO + ".CDGRUPODESTAQUE" : "TB.CDGRUPODESTAQUE";
	        sqlWhereClause.addAndConditionIn(coluna, StringUtil.split(produto.cdGrupoDestaque, ProdutoDestaque.SEPARADOR_CAMPOS));
       	}
       	sqlWhereClause.addAndLikeCondition("TB.DSPRINCIPIOATIVO", produto.dsPrincipioAtivo, false);
       	if (LavenderePdaConfig.usaAvisoPreAlta && (ValueUtil.VALOR_SIM.equals(produto.flPreAltaCusto))) {
       		sqlWhereClause.addAndCondition("TB.FLPREALTACUSTO = ", produto.flPreAltaCusto);
       	}
       	if (LavenderePdaConfig.isUsaKitBaseadoNoProduto() && (ValueUtil.VALOR_SIM.equals(produto.flKit))) {
       		sqlWhereClause.addAndCondition("TB.FLKIT = ", produto.flKit);
       	}
       	if (LavenderePdaConfig.filtraProdutosPorOrigemPedido && !ClienteSetorOrigem.CLIENTE_SETOR_ORIGEM_SEM_CONTRATO.equals(produto.cdOrigemSetor)) {
       		sqlWhereClause.addAndCondition("TB.CDORIGEMSETOR = ", produto.cdOrigemSetor);
        }
		if (produto.forceCdFatorComumFilter) {
			if (ValueUtil.isNotEmpty(produto.cdFatorComum)) {
       			sqlWhereClause.addAndConditionForced("TB.CDFATORCOMUM = ", produto.cdFatorComum);
       		} else {
       			sqlWhereClause.addStartAndMultipleCondition();
       			sqlWhereClause.addAndConditionForced("TB.CDFATORCOMUM is null or TB.CDFATORCOMUM = ", produto.cdFatorComum);
       			sqlWhereClause.addEndMultipleCondition();
       		}
		}
		if ((LavenderePdaConfig.usaDescPromo || (LavenderePdaConfig.isConfigValorMinimoDescPromocional() && produto.filterToItemPedido)) && !produto.joinDescPromocional) {
       		sqlWhereClause.addAndCondition(DaoUtil.ALIAS_DESCPROMOCIONAL + ".CDPRODUTO IS NULL");
       	}
		if (LavenderePdaConfig.usaProdutoRestrito ) {
			Cliente cliente = SessionLavenderePda.getCliente();
			if (cliente != null && !cliente.isPossuiDescontoExtraProdutoRestrito()) {
				sqlWhereClause.addStartAndMultipleCondition();
	   			sqlWhereClause.addOrCondition("TB.FLPRODUTORESTRITO is null");
	   			sqlWhereClause.addOrConditionForced("TB.FLPRODUTORESTRITO = ", "");
	   			sqlWhereClause.addOrCondition("TB.FLPRODUTORESTRITO = ", ValueUtil.VALOR_NAO);	   			
	   			sqlWhereClause.addEndMultipleCondition();
			}
		}
		if (LavenderePdaConfig.usaPedidoProdutoCritico && produto.filterToItemPedido) {
	        if (produto.isProdutoCritico()) {
	       		sqlWhereClause.addAndCondition("TB.FLCRITICO = ", produto.flCritico);
	       	} else if (LavenderePdaConfig.filtraProdutosPedidoNaoCritico){
	       		sqlWhereClause.addAndCondition("(TB.FLCRITICO = 'N' OR TB.FLCRITICO = '' OR TB.FLCRITICO IS NULL)");
	       	}
		}
	    if (LavenderePdaConfig.usaDescProgressivoPersonalizado && produto.descProgressivoConfigFilter != null) {
		    sqlWhereClause.addAndCondition(DescProgressivoConfigDbxDao.getInstance().getSqlCdProdutoInDescProgressivoConfig("TB.CDPRODUTO", produto.descProgressivoConfigFilter, produto.descProgConfigFamFilter));
	    }
		if (LavenderePdaConfig.filtraProdutoPorTipoPedido) {
			DaoUtil.addAndFlExcecaoProdutoTipoPedCondition(produto, sqlWhereClause);
		}
	    getProdutoSearchCondition("TB.", produto, sqlWhereClause, false);
	    if (LavenderePdaConfig.apresentaFiltroDescQtd && produto.filtraProdutoDescQtd) {
	    	Cliente cliente = SessionLavenderePda.getCliente();
	    	StringBuffer sb = getSqlBuffer();
	    	sb.append("(EXISTS (SELECT 1 FROM TBLVPDESCQUANTIDADE desc WHERE desc.CDEMPRESA = TB.CDEMPRESA AND")
	    	.append(" desc.CDREPRESENTANTE = TB.CDREPRESENTANTE AND ")
		    .append(" desc.CDPRODUTO = TB.CDPRODUTO ");
	    	if (LavenderePdaConfig.usaVigenciaDescQuantidade) {
	    		sb.append(" AND ").append(Sql.getValue(DateUtil.getCurrentDate())).append(" BETWEEN DTINICIALVIGENCIA AND DTFIMVIGENCIA");
	    	}
		    if (hasItemTabelaPreco) {
			    sb.append(" AND desc.CDTABELAPRECO = ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".CDTABELAPRECO");
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
			    	.append(" descCliente.CDTABELAPRECO = ").append(Sql.getValue(produto.itemTabelaPreco.cdTabelaPreco)).append(" AND ")
			    	.append(" d.CDPRODUTO = descCliente.CDPRODUTO AND ")
			    	.append(" descCliente.CDCLIENTE = ").append(Sql.getValue(cliente.cdCliente));
		    	}
		    	sb.append(" WHERE dq.CDEMPRESA = tb.CDEMPRESA AND ")
		    	.append(" dq.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
		    	.append(" dq.CDPRODUTOSIMILAR = tb.CDPRODUTO ");
		    	if (LavenderePdaConfig.usaVigenciaDescQuantidade) {
		    		sb.append(" AND ").append(Sql.getValue(DateUtil.getCurrentDate())).append(" BETWEEN DTINICIALVIGENCIA AND DTFIMVIGENCIA");
		    	}
		    	if (hasItemTabelaPreco) {
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
	    if ((LavenderePdaConfig.apresentaMarcadorProdutoLista || LavenderePdaConfig.apresentaMarcadorProdutoInsercao) && ValueUtil.isNotEmpty(produto.cdMarcadores)) {
			DaoUtil.addAndLikeOrConditionMarcadores(sqlWhereClause, produto.cdMarcadores);
	    }
	    if (ValueUtil.isNotEmpty(produto.cdStatusEstoque)) {
	    	addFilterStatusEstoque(sqlWhereClause, produto.cdStatusEstoque);
	    }
		if (LavenderePdaConfig.isUsaFiltraPorProdutosPromocionalListaProdutoEItemPedido() && ValueUtil.valueEqualsIfNotNull(ValueUtil.VALOR_SIM, produto.flFiltraProdutoPromocional)) {
			if (hasItemTabelaPreco) {
				sqlWhereClause.addAndCondition(DaoUtil.ALIAS_ITEMTABELAPRECO + ".FLPROMOCAO = ", ValueUtil.VALOR_SIM);
			} else {
				StringBuffer sbTemp = new StringBuffer();
				String customCompare = " AND " + DaoUtil.ALIAS_ITEMTABELAPRECO + ".FLPROMOCAO = " + Sql.getValue(ValueUtil.VALOR_SIM);
				DaoUtil.addExistsItemTabelaPreco(sbTemp, DaoUtil.ALIAS_ITEMTABELAPRECO, "TB", produto.itemTabelaPreco, customCompare);
				sqlWhereClause.addAndCondition(sbTemp.toString());
			}
		}
		if (LavenderePdaConfig.usaDestaqueItensVendidosMesCorrente && ValueUtil.VALOR_SIM.equals(produto.flVendido)) {
			sqlWhereClause.addStartAndMultipleCondition();
			sqlWhereClause.addOrCondition("TB.FLVENDIDO =", (produto.flVendido));
			sqlWhereClause.addOrCondition("TB.FLVENDIDO =", "P");
			sqlWhereClause.addEndMultipleCondition();
		}
       	if (produto.cestaPositProdutoFilter != null) {
       		sqlWhereClause.addAndCondition(DaoUtil.getCestaProdutoMetaNaoAtingidaCondition(produto.cestaPositProdutoFilter));
       	}
       	if (produto.cestaProdutoFilter != null) {
       		sqlWhereClause.addAndCondition(DaoUtil.getCestaProdutoCondition(produto.cestaProdutoFilter));
       	}
       	if (produto.grupoProdTipoPedFilter != null) {
       		sqlWhereClause.addAndCondition(DaoUtil.getExistsGrupoProdTipoPedProdutoListCondition(produto.grupoProdTipoPedFilter));
       	}
       	if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido() && produto.produtoUnidadeFilter != null) {
       		sqlWhereClause.addStartAndMultipleCondition();
       		sqlWhereClause.addAndConditionEquals("TB.CDUNIDADE", produto.produtoUnidadeFilter.cdUnidade);
       		sqlWhereClause.addOrCondition(DaoUtil.getExistsProdutoUnidadeCondition(produto.produtoUnidadeFilter));
       		sqlWhereClause.addEndMultipleCondition();
       	}
       	if (LavenderePdaConfig.usaFiltroComissao && hasItemTabelaPreco) {
       		if (produto.itemTabelaPreco.filterByVlPctComissaoMinFilter) {
       			sqlWhereClause.addAndCondition(DaoUtil.ALIAS_ITEMTABELAPRECO + ".VLPCTCOMISSAO >= ", produto.itemTabelaPreco.vlPctComissaoMinFilter);
       		}
       		if (produto.itemTabelaPreco.filterByVlPctComissaoMaxFilter) {
       			sqlWhereClause.addAndCondition(DaoUtil.ALIAS_ITEMTABELAPRECO + ".VLPCTCOMISSAO <= ", produto.itemTabelaPreco.vlPctComissaoMaxFilter);
       		}
       	}
       	if (LavenderePdaConfig.filtraProdutoClienteRepresentante && produto.produtoClienteFilter != null) {
       		sqlWhereClause.addAndCondition(ProdutoClienteDbxDao.getInstance().getFlTipoRelacaoCteCondition(false));
       	}
       	if (LavenderePdaConfig.filtraClientePorProdutoRepresentante && produto.clienteProdutoFilter != null) {
       		sqlWhereClause.addAndCondition(ClienteProdutoDbxDao.getInstance().getFlTipoRelacaoCteCondition(false));
       	}
       	if (LavenderePdaConfig.usaFiltroProdutoCondicaoPagamentoRepresentante && produto.produtoCondPagtoFilter != null) {
       		sqlWhereClause.addAndCondition(ProdutoCondPagtoDbxDao.getInstance().getFlTipoRelacaoCteCondition(false));
       	}
       	if ((LavenderePdaConfig.usaCategoriaInsercaoItem() || LavenderePdaConfig.usaCategoriaMenuProdutos()) && produto.produtoMenuCategoriaFilter != null) {
       		sqlWhereClause.addAndCondition(DaoUtil.getExistsProdutoMenuCategoriaCondition(produto.produtoMenuCategoriaFilter));
       	}
       	if (LavenderePdaConfig.naoConsideraProdutoDescPromocionalComoPromocional && produto.excetoDescPromocional) {
       		sqlWhereClause.addAndCondition(DaoUtil.ALIAS_DESCPROMOCIONAL.concat(".CDEMPRESA IS NULL"));
       	}
       	if (LavenderePdaConfig.usaApenasItemPedidoOriginalNaBonificacaoTroca && produto.itemPedidoFilter != null) {
       		sqlWhereClause.addAndCondition(DaoUtil.getExistsItemPedidoOrItemPedidoErpCondition(produto.itemPedidoFilter, true, false));
       	}
       	if (consideraUltimoProdutoFilter) {
			ProdutoGradeDbxDao.getInstance().addWhereProdutoGrade5(SessionLavenderePda.getUltimoProdutoFilter(), sqlWhereClause, "TB");
		}
       	if (LavenderePdaConfig.usaListaMultiplaInsercaoItensNoPedidoPorGiroProduto && produto.giroProdutoFilter != null) {
       		sqlWhereClause.addAndCondition(DaoUtil.getExistsGiroProdutoCondition(produto.giroProdutoFilter));
       	}
	    sql.append(sqlWhereClause.getSql());
		
	    if (LavenderePdaConfig.usaRestricaoVendaClienteProduto) {
	    	String cdCliente = SessionLavenderePda.getCliente() != null ? SessionLavenderePda.getCliente().cdCliente : null;
		    DaoUtil.addNotExistsRestricaoProduto(sql, "TB.CDPRODUTO", cdCliente, null, 1, null, true, false, false, false);
	    }
	    if (LavenderePdaConfig.usaGradeProduto5() && LavenderePdaConfig.filtraProdutoClienteRepresentante && produto.filtrandoAgrupadorGrade) {
	    	sql.append(" AND (PRODCLIENTE.FLTIPORELACAO IS NOT NULL OR")
	    	.append(" NOT EXISTS ")
	    	.append("(SELECT 1 FROM TBLVPPRODUTOCLIENTE WHERE")
	    	.append(" CDEMPRESA = TB.CDEMPRESA AND ")
	    	.append(" CDREPRESENTANTE = TB.CDREPRESENTANTE AND ")
	    	.append(" CDPRODUTO = TB.CDPRODUTO AND ")
	    	.append(" CDCLIENTE <> ").append(Sql.getValue(produto.cdCliente))
	    	.append("))");
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

	public static void getProdutoSearchCondition(String alias, Produto produto, SqlWhereClause sqlWhereClause, boolean addAndCondicion) {
		if (addAndCondicion) {
			sqlWhereClause.addStartMultipleCondition();
		} else {
			sqlWhereClause.addStartAndMultipleCondition();
		}
       	boolean adicionouInicioBloco = false;
		adicionouInicioBloco |= sqlWhereClause.addAndLikeCondition(alias + "DSPRODUTO", produto.dsProduto, false);
		adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition(alias + "CDPRODUTO", produto.cdProdutoLikeFilter, false);
		adicionouInicioBloco |= sqlWhereClause.addOrCondition(alias + "CDPRODUTO = ", produto.cdProduto);
		adicionouInicioBloco |= sqlWhereClause.addOrCondition(alias + "NUCODIGOBARRAS = ", produto.nuCodigoBarras);
		adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition(alias + "DSMARCA", produto.dsMarca, false);
		adicionouInicioBloco |= sqlWhereClause.addOrCondition(alias + "DSREFERENCIA = ", produto.dsReferencia);
		adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition(alias + "DSREFERENCIA", produto.dsReferenciaLikeFilter, false);
		if (LavenderePdaConfig.isUsaFiltroAplicacaoDoProduto()) {
			adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition(alias + "DSAPLICACAOPRODUTO", produto.dsAplicacaoProduto, false);
   		}
   		if (LavenderePdaConfig.usaFiltroCodigoAlternativoProduto) {
			adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition(alias + "DSCODIGOALTERNATIVO", produto.dsCodigoAlternativo, false);
   		}		
		if (LavenderePdaConfig.usaGradeProduto5() && produto.filtrandoAgrupadorGrade) {
			adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition(alias + "DSAGRUPADORGRADE", produto.getDsAgrupadorGrade(), false);
		}
   		if (adicionouInicioBloco) {
   			sqlWhereClause.addEndMultipleCondition();
   		} else {
   			sqlWhereClause.removeStartMultipleCondition();
   		}
    }

    protected void addSelectGridColumns(StringBuffer sql, BaseDomain domain) {
    	sql.append(" ROWKEY,");
        sql.append(" CDPRODUTO,");
        sql.append(" DSPRODUTO,");
        sql.append(" DSPRINCIPIOATIVO");
        if (LavenderePdaConfig.usaAvisoPreAlta) {
        	sql.append(", FLPREALTACUSTO");
        }
        if (((Produto)domain).filterToItemPedido) {
        	sql.append(" , FLBONIFICACAO");
        }
    }

	private String addDsProdutoColumn(ProdutoBase produto) {
		if (produto.filtrandoAgrupadorGrade) {
			return " CASE WHEN PRODUTOGRADE.CDPRODUTO IS NOT NULL AND TB.DSAGRUPADORGRADE IS NOT NULL THEN TB.DSAGRUPADORGRADE ELSE TB.DSPRODUTO END DSPRODUTO,";
		}
		return " TB.DSPRODUTO,";
	}

	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
		ProdutoBase produto = (ProdutoBase) domain;
        sql.append(" TB.CDPRODUTO,");
        sql.append(" TB.CDEMPRESA,");
        sql.append(" TB.CDREPRESENTANTE,");
        sql.append(addDsProdutoColumn(produto));
        sql.append(" TB.NUCODIGOBARRAS,");
        sql.append(" TB.CDUNIDADE,");
        sql.append(" TB.FLUTILIZAVERBA,");
        sql.append(" TB.FLPERMITEESTOQUENEGATIVO,");
        sql.append(" TB.CDGRUPOPRODUTO1,");
        sql.append(" TB.CDGRUPOPRODUTO2,");
        sql.append(" TB.CDGRUPOPRODUTO3,");
        sql.append(" TB.CDGRUPOPRODUTO4,");
        sql.append(" TB.CDGRUPOPRODUTO5,");
		sql.append(" TB.CDUNIDADEPREFERENCIAL,");
		sql.append(" TB.CDORIGEMMERCADORIA");
        if (LavenderePdaConfig.usaFiltroFornecedor || LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor()) {
        	sql.append(", TB.CDFORNECEDOR");
        }
        if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto() || LavenderePdaConfig.usaSugestaoVendaProdutoMesmoPrincipioAtivo) {
            sql.append(", TB.DSPRINCIPIOATIVO");
        }
        if (LavenderePdaConfig.isPermiteBonificarProduto()) {
            sql.append(", TB.FLBONIFICACAO");
        }
        if (LavenderePdaConfig.isMostraDescricaoReferencia() || LavenderePdaConfig.usaDsReferenciaNmFoto || LavenderePdaConfig.usaPoliticaComercial()) {
			sql.append(", TB.DSREFERENCIA");
		}
    	if (LavenderePdaConfig.filtraProdutosPorOrigemPedido) {
       		sql.append(", TB.CDORIGEMSETOR");
        }
    	if (LavenderePdaConfig.isUsaGrupoDestaqueProduto()) {
       		sql.append(", ").append(DaoUtil.ALIAS_PRODUTODESTAQUE).append(".CDGRUPODESTAQUE");
       		sql.append(", ").append(DaoUtil.ALIAS_CORSISTEMA).append(".CDESQUEMACOR CDESQUEMACORPRODEST");
       		sql.append(", ").append(DaoUtil.ALIAS_CORSISTEMA).append(".CDCOR CDCORPRODEST");
       		sql.append(", ").append(DaoUtil.ALIAS_CORSISTEMA).append(".VLR VLRPRODEST");
       		sql.append(", ").append(DaoUtil.ALIAS_CORSISTEMA).append(".VLG VLGPRODEST");
       		sql.append(", ").append(DaoUtil.ALIAS_CORSISTEMA).append(".VLB VLBPRODEST");
        }
    	if (LavenderePdaConfig.usaAvisoPreAlta) {
         	sql.append(", TB.FLPREALTACUSTO");
        }
    	if (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() || LavenderePdaConfig.isUsaGrupoDescPromocionalNoDescQtdPorGrupo()) {
			sql.append(", TB.CDGRUPODESCPROD");
		}
    	if (LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito()) {
    		sql.append(", TB.FLSITUACAO");
        }
    	if (LavenderePdaConfig.grifaProdutoComVidaUtilLoteCriticoNaLista) {
    		sql.append(", TB.FLLOTEPRODUTOCRITICO");
    	}
    	if (LavenderePdaConfig.usaConversaoUnidadesMedida || LavenderePdaConfig.mostraPrecoUnidadeItem || LavenderePdaConfig.isExibeComboMenuInferior() || LavenderePdaConfig.isMostraPrecoItemStNaListaProduto()
				|| LavenderePdaConfig.isMostraPrecoItemStNaListaProdutoDoPedido() || LavenderePdaConfig.isMostraPrecoItemStNoDetalheDoProduto()) {
        	sql.append(", TB.NUCONVERSAOUNIDADESMEDIDA");
        }
    	if (LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto) {
			sql.append(", TB.CDUNIDADEFRACAO")
			.append(", TB.NUFRACAO")
			.append(", TB.FLUSAUNIDADEBASEDSFRACAO");
		}
		if (LavenderePdaConfig.isUsaFlIgnoraValidaco()) {
			sql.append(", TB.FLIGNORAVALIDACAO");
		}
        if (usaCTETributacaoConfig()) {
        	sql.append(", TB.CDTRIBUTACAOPRODUTO");
        	if (produto != null && produto.fromListItemPedido) {
        		sql.append(", ").append(DaoUtil.ALIAS_TRIBUTACAOCONFIG).append(".FLFRETEBASEIPI");
        		sql.append(", ").append(DaoUtil.ALIAS_TRIBUTACAOCONFIG).append(".FLTIPOCALCULOPISCOFINS");
        		sql.append(", ").append(DaoUtil.ALIAS_TRIBUTACAOCONFIG).append(".FLCALCULAIPI");
        		sql.append(", ").append(DaoUtil.ALIAS_TRIBUTACAOCONFIG).append(".FLCALCULAICMS");
        	} else {
        		sql.append(", 0 AS ").append("FLFRETEBASEIPI");
        		sql.append(", 0 AS ").append("FLTIPOCALCULOPISCOFINS");
        		sql.append(", 0 AS ").append("FLCALCULAIPI");
        		sql.append(", 0 AS ").append("FLCALCULAICMS");
        	}
        }
        if (LavenderePdaConfig.apresentaMarcaNasListasNoPedido()) {
        	sql.append(", tb.DSMARCA");
        }
 		if (LavenderePdaConfig.usaControleEstoquePorRemessa && LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoForaPedido() && !produto.findListCombo) {
        	DaoUtil.getSelectSumEstoquePorRemessa(sql, produto.cdRepresentante);
        	if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() || LavenderePdaConfig.destacaProdutoQtMinEstoqueLista) {
        		sql.append(", ((ifnull(").append(DaoUtil.ALIAS_ESTOQUE_ERP).append(".QTESTOQUEMIN, 0))) AS QTESTOQUEMIN");
        	}
		} else if (LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoForaPedido() || LavenderePdaConfig.isMostraEstoquePrevistoNaListaProdutosForaPedido()) {
        	sql.append(", (");
        	if (produto.filtrandoAgrupadorGrade) sql.append("SUM");
        	sql.append("(ifnull(").append(DaoUtil.ALIAS_ESTOQUE_ERP).append(".QTESTOQUE, 0)) - ");
        	if (produto.filtrandoAgrupadorGrade) sql.append("SUM");
        	sql.append("(ifnull(").append(DaoUtil.ALIAS_ESTOQUE_PDA).append(".QTESTOQUE, 0))) AS QTESTOQUE");
        	if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() || LavenderePdaConfig.destacaProdutoQtMinEstoqueLista) {
        		sql.append(", ((ifnull(").append(DaoUtil.ALIAS_ESTOQUE_ERP).append(".QTESTOQUEMIN, 0))) AS QTESTOQUEMIN");
        	}
        } else if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() || LavenderePdaConfig.destacaProdutoQtMinEstoqueLista) {
        	sql.append(", (");
        	if (produto.filtrandoAgrupadorGrade) sql.append("SUM");
        	sql.append("(ifnull(").append(DaoUtil.ALIAS_ESTOQUE_ERP).append(".QTESTOQUE, 0))) AS QTESTOQUE");
        }
        if (LavenderePdaConfig.isMostraEstoquePrevistoNaListaProdutosForaPedido() || LavenderePdaConfig.usaControleEstoquePrevistoParcial()) {
	        sql.append(", ");
	        if (LavenderePdaConfig.usaGradeProduto5() && produto.filtrandoAgrupadorGrade) sql.append("SUM(");
	        sql.append(DaoUtil.ALIAS_ESTOQUE_ERP).append(".QTESTOQUEPREVISTO");
	        if (LavenderePdaConfig.usaGradeProduto5() && produto.filtrandoAgrupadorGrade) sql.append(") AS QTESTOQUEPREVISTO");
        }
        if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() && !produto.findListCombo) {
        	if (LavenderePdaConfig.isUsaLocalEstoque() && produto.estoque != null) {
        		DaoUtil.getSelectSomaQtEstoqueNenhumaGrade(sql, produto);
        } else {
        	DaoUtil.getSelectSomaQtEstoqueNenhumaGrade(sql);
        }
        }
        if (LavenderePdaConfig.grifaProdutoSemEstoqueEmUmaGradeNaLista && !produto.findListCombo) {
        	if (LavenderePdaConfig.isUsaLocalEstoque()) {
        		DaoUtil.getSelectEstoqueEmAlgumaGrade(sql,produto);
        	} else {
        	DaoUtil.getSelectEstoqueEmAlgumaGrade(sql);
        }
        }
        if (!produto.hideProductPicture) {
			if (LavenderePdaConfig.usaFotoProdutoPorEmpresa && !populandoCombo) {
				DaoUtil.getSelectNmFotoProdutoEmp(sql);
			} else if ((LavenderePdaConfig.mostraFotoProduto || LavenderePdaConfig.isLoadImagesOnProdutoList()) && !produto.findListCombo) {
        	DaoUtil.getSelectNmFotoProduto(sql);
        }
        }
        if (LavenderePdaConfig.showInformacoesComplementaresInsercMultipla() && LavenderePdaConfig.isPermiteInserirMultiplosItensPorVezNoPedido()) {
        	sql.append(", ").append(DaoUtil.ALIAS_GIROPRODUTO).append(".VLMEDIOHISTORICO")
        	.append(", ").append(DaoUtil.ALIAS_GIROPRODUTO).append(".QTDMEDIAHISTORICO");
        }
		if (LavenderePdaConfig.exibeHistoricoEstoqueCliente || LavenderePdaConfig.exibeHistoricoQtdeVendaDoItem) {
			DaoUtil.getSelectHistoricoEstoqueItemPedido(sql, produto.cdClienteFilter);
			DaoUtil.getSelectHistoricoItemFisicoItemPedido(sql, produto.cdClienteFilter);
		}
		if (LavenderePdaConfig.isMostraCampoValorUnitarioEmbalagemListaProdutosDentroPedido()) {
			sql.append(", TB.VLST");
			sql.append(", TB.NUCONVERSAOUNIDADESMEDIDA");
		}
		if (LavenderePdaConfig.exibeFracaoEmbalagemFornecedorItemPedido) {
			sql.append(", TB.NUFRACAOFORNECEDOR");
		}
		if(LavenderePdaConfig.isConfigCalculoPesoPedido() && !populandoCombo) {
			sql.append(", TB.VLGRAMATURA");
	}
		if (LavenderePdaConfig.usaPoliticaComercial() || LavenderePdaConfig.isUsaPoliticaBonificacaoConjuntos()) {
			sql.append(", TB.CDCONJUNTO");
		}
		if (LavenderePdaConfig.usaPoliticaComercial()) {
			sql.append(", TB.CDCOLECAO");
			sql.append(", TB.CDSTATUSCOLECAO");
			sql.append(", TB.FLVENDAENCERRADA");
			sql.append(", TB.CDLINHA");
			sql.append(", TB.CDCLASSE");
			sql.append(", TB.CDMARCA");
			if (LavenderePdaConfig.usaFamiliaPadraoPoliticaComercial()) {
				sql.append(", TB.CDFAMILIAPADRAO");
			}
		}
		if (LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial && ValueUtil.isNotEmpty(produto.cdCondicaoComercialFilter)) {
			sql.append(", ").append(DaoUtil.ALIAS_CONDCOMERCIALEXCEC).append(".CDEMPRESA AS CDEMPRESACONDCOMEXCEC");
			sql.append(", ").append(DaoUtil.ALIAS_CONDCOMERCIALEXCEC).append(".CDREPRESENTANTE AS CDREPRESENTANTECONDCOMEXCEC");
			sql.append(", ").append(DaoUtil.ALIAS_CONDCOMERCIALEXCEC).append(".CDPRODUTO AS CDPRODUTOCONDCOMEXCEC");
			sql.append(", ").append(DaoUtil.ALIAS_CONDCOMERCIALEXCEC).append(".CDCONDICAOCOMERCIAL AS CDCONDICAOCOMERCIALEXCEC");
			sql.append(", ").append(DaoUtil.ALIAS_CONDCOMERCIALEXCEC).append(".CDITEMGRADE1 AS CDITEMGRADE1CONDCOMEXCEC");
			sql.append(", ").append(DaoUtil.ALIAS_CONDCOMERCIALEXCEC).append(".VLUNITARIO AS VLUNITARIOCONDCOMEXCEC ");
		}
		if (LavenderePdaConfig.usaPedidoProdutoCritico) {
			sql.append(", TB.FLCRITICO");
		}
		if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao || LavenderePdaConfig.apresentaMarcadorProdutoLista) {
			sql.append(", ").append(DaoUtil.ALIAS_MARCPROD1).append(".CDMARCADOR");
		}
		if (LavenderePdaConfig.mostraDimensoesNosDetalhes()) {
			sql.append(", DSDIMENSOES");
		}
		if (LavenderePdaConfig.usaCotacaoMoedaProduto) {
			sql.append(", TB.DSMOEDAPRODUTO");
		}
	
		//deve ficar fora dos indices por possuir comportamento diferente
		if (LavenderePdaConfig.usaSimilarVendaProduto && LavenderePdaConfig.usaAgrupadorProdutoSimilar) {
			sql.append(", TB.CDAGRUPPRODSIMILAR");
		}
		if (LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecial()) {
			sql.append(", TB.NUMULTIPLOESPECIAL NUMULTIPLOESPECIAL ");
		}
		if (LavenderePdaConfig.usaGradeProduto5() && produto.filtrandoAgrupadorGrade) {
			sql.append(", CASE WHEN PRODUTOGRADE.CDPRODUTO IS NOT NULL THEN TB.DSAGRUPADORGRADE ELSE NULL END DSAGRUPADORGRADE")
					.append(", DSAGRUPADORGRADECOMP");
		}
		if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorItensFormulaJuros()) {
			sql.append(", TB.FLFORMULACALCULODEFLATOR");
			sql.append(", TB.VLPCTJUROSMENSAL");
            sql.append(", TB.QTDIASCARENCIAJUROS");
		}
		if (isFazJoinComItemTabPreco(produto)) {
			if ((LavenderePdaConfig.isFiltraItemTabelaPrecoListaProduto() ||  LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial || LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() || produto.findListCombo || LavenderePdaConfig.usaFiltroComissao)) {
	        	sql.append(", ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".CDTABELAPRECO");
	        	sql.append(", ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".VLUNITARIO");
				if (LavenderePdaConfig.usaFiltroComissao) {
					sql.append(", ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".VLPCTCOMISSAO");
				}
			}
			if (LavenderePdaConfig.destacaProdutoQuantidadeMaximaVenda) {
				sql.append(", ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".QTMAXVENDA");
			}
			if (LavenderePdaConfig.isUsaFiltraPorProdutosPromocionalListaProdutoEItemPedido()) {
				sql.append(", ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".FLPROMOCAO");
				sql.append(", ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".NUPROMOCAO");
			}
			if (LavenderePdaConfig.isFiltraItemTabelaPrecoListaProduto() && LavenderePdaConfig.isMostraProdutoDescPromocionalDestacadoTelaListaProdutoTipo3()) {
				sql.append(", ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".VLPCTDESCPROMOCIONAL");
			}
			if (LavenderePdaConfig.isMostraPrecoItemStNaListaProduto()) {
		    	sql.append(", ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".VLEMBALAGEMST");
			}
			if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
				sql.append(", ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".CDPRAZOPAGTOPRECO");
			}
		}
		
		if (LavenderePdaConfig.usaDestaqueItensVendidosMesCorrente) {
			sql.append(", TB.FLVENDIDO ");
		}
		if (LavenderePdaConfig.isMostraQtVendasProdutoNoPeriodo()) {
			sql.append(", TB.QTVENDASPERIODO");
		}
		if (LavenderePdaConfig.isPermiteSelecaoUnidadeAlternativa() && produto.unidade != null) {
			sql.append(", ").append(DaoUtil.ALIAS_UNIDADE).append(".DSUNIDADE");
			sql.append(", ").append(DaoUtil.ALIAS_UNIDADE).append(".DSUNIDADEPLURAL");
		}
		if (produto.contaProdutosListados) {
			sql.append(", COUNT(1) OVER() AS QTPRODUTOSLISTADOS");
		}
	}
	
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		Produto produtoFilter = (Produto) domainFilter;
        Produto produto = new Produto();
        produto.cdProduto = rs.getString(1);
        produto.cdEmpresa = rs.getString(2);
        produto.cdRepresentante = rs.getString(3);
        produto.dsProduto = rs.getString(4);
        produto.nuCodigoBarras = rs.getString(5);
        produto.cdUnidade = rs.getString(6);
        produto.flUtilizaVerba = rs.getString(7);
        produto.flPermiteEstoqueNegativo = rs.getString(8);
        produto.cdGrupoProduto1 = rs.getString(9);
        produto.cdGrupoProduto2 = rs.getString(10);
        produto.cdGrupoProduto3 = rs.getString(11);
        produto.cdGrupoProduto4 = rs.getString(12);
        produto.cdGrupoProduto5 = rs.getString(13);
		produto.cdUnidadePreferencial = rs.getString(14);
		produto.cdOrigemMercadoria = rs.getString(15);
        int i = 16;
        if (LavenderePdaConfig.usaFiltroFornecedor || LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor()) {
        	produto.cdFornecedor = rs.getString(i++);
        }
        if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto() || LavenderePdaConfig.usaSugestaoVendaProdutoMesmoPrincipioAtivo) {
        	produto.dsPrincipioAtivo = rs.getString(i++);
        }
        if (LavenderePdaConfig.isPermiteBonificarProduto()) {
            produto.flBonificacao = rs.getString(i++);
        }
		if (LavenderePdaConfig.isMostraDescricaoReferencia() || LavenderePdaConfig.usaDsReferenciaNmFoto || LavenderePdaConfig.usaPoliticaComercial()) {
			produto.dsReferencia = rs.getString(i++);
		}
		if (LavenderePdaConfig.filtraProdutosPorOrigemPedido) {
			produto.cdOrigemSetor = rs.getString(i++);
        }
		if (LavenderePdaConfig.isUsaGrupoDestaqueProduto()) {
			produto.cdGrupoDestaque = rs.getString(i++);
			produto.produtoDestaque = new ProdutoDestaque();
			produto.produtoDestaque.cdEmpresa = produto.cdEmpresa;
			produto.produtoDestaque.cdRepresentante = produto.cdRepresentante;
			produto.produtoDestaque.cdGrupoDestaque = produto.cdGrupoDestaque;
			produto.produtoDestaque.corSistema = new CorSistema();
			produto.produtoDestaque.corSistema.cdEsquemaCor = rs.getInt(i++);
			produto.produtoDestaque.corSistema.cdCor = rs.getInt(i++);
			produto.produtoDestaque.corSistema.vlR = rs.getInt(i++);
			produto.produtoDestaque.corSistema.vlG = rs.getInt(i++);
			produto.produtoDestaque.corSistema.vlB = rs.getInt(i++);
        }
		if (LavenderePdaConfig.usaAvisoPreAlta) {
			produto.flPreAltaCusto = rs.getString(i++);
		}
		if (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() || LavenderePdaConfig.isUsaGrupoDescPromocionalNoDescQtdPorGrupo()) {
			produto.cdGrupoDescProd = rs.getString(i++);
		}
		if (LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito()) {
			produto.flSituacao = rs.getInt(i++);
        }
		if (LavenderePdaConfig.grifaProdutoComVidaUtilLoteCriticoNaLista) {
			produto.flLoteProdutoCritico = rs.getString(i++);
		}
		if (LavenderePdaConfig.usaConversaoUnidadesMedida || LavenderePdaConfig.mostraPrecoUnidadeItem || LavenderePdaConfig.isExibeComboMenuInferior()|| LavenderePdaConfig.isMostraPrecoItemStNaListaProduto()
				|| LavenderePdaConfig.isMostraPrecoItemStNaListaProdutoDoPedido() || LavenderePdaConfig.isMostraPrecoItemStNoDetalheDoProduto()) {
			produto.nuConversaoUnidadesMedida = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto) {
			produto.cdUnidadeFracao = rs.getString(i++);
			produto.nuFracao = rs.getInt(i++);
			produto.flUsaUnidadeBaseDsFracao = rs.getString(i++);
		}
		if (LavenderePdaConfig.isUsaFlIgnoraValidaco()) {
			produto.flIgnoraValidacao = rs.getString(i++);
		}
        if (usaCTETributacaoConfig()) {
        	TributacaoConfig tribConf = new TributacaoConfig();
        	produto.cdTributacaoProduto = rs.getString(i++);
        	tribConf.flFreteBaseIpi = rs.getString(i++);
        	tribConf.flTipoCalculoPisCofins = rs.getString(i++);
        	tribConf.flCalculaIpi = rs.getString(i++);
        	tribConf.flCalculaIcms = rs.getString(i++);
        	produto.tributacaoConfig = tribConf;
        }
        if (LavenderePdaConfig.apresentaMarcaNasListasNoPedido()) {
        	produto.dsMarca = rs.getString(i++);
        }
        if(LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoForaPedido() || LavenderePdaConfig.isMostraEstoquePrevistoNaListaProdutosForaPedido() || LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() || LavenderePdaConfig.destacaProdutoQtMinEstoqueLista) {
			setInstanceEstoque(produto);
        	produto.estoque.qtEstoque = rs.getDouble(i++);	
        	if (LavenderePdaConfig.destacaProdutoQtMinEstoqueLista || LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido()) {
				produto.estoque.qtEstoqueMin = rs.getDouble(i++);
			}
        }
        if (LavenderePdaConfig.isMostraEstoquePrevistoNaListaProdutosForaPedido() || LavenderePdaConfig.usaControleEstoquePrevistoParcial()) {
			setInstanceEstoque(produto);
        	produto.estoque.qtEstoquePrevisto = rs.getDouble(i++);
        }
        if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() && !populandoCombo) {
        	produto.sumEstoqueGrades = rs.getDouble(i++);
        }
        if (LavenderePdaConfig.grifaProdutoSemEstoqueEmUmaGradeNaLista && !populandoCombo) {
        	produto.isAlgumaGradeSemEstoque = ValueUtil.VALOR_ZERO.equals(rs.getString(i++));
        }
        if (!produtoFilter.hideProductPicture) {
	        if ((LavenderePdaConfig.usaFotoProdutoPorEmpresa || LavenderePdaConfig.mostraFotoProduto || LavenderePdaConfig.isLoadImagesOnProdutoList()) && !populandoCombo) {
        	produto.fotoProduto = new FotoProduto();
        	produto.fotoProduto.nmFoto = rs.getString(i++);
        }
        }
        if (LavenderePdaConfig.showInformacoesComplementaresInsercMultipla() && LavenderePdaConfig.isPermiteInserirMultiplosItensPorVezNoPedido()) {
        	produto.qtdMediaHistorico = rs.getDouble(i++);
        	produto.vlMedioHistorico = rs.getDouble(i++);
        }
        if (LavenderePdaConfig.exibeHistoricoEstoqueCliente || LavenderePdaConfig.exibeHistoricoQtdeVendaDoItem) {
        	DaoUtil.populateHistoricoEstoqueCliente(rs.getString(i++), produto);
        	DaoUtil.populateHistoricoItemFisicoCliente(rs.getString(i++), produto);
        }
		if (LavenderePdaConfig.isMostraCampoValorUnitarioEmbalagemListaProdutosDentroPedido()) {
			produto.vlSt = rs.getDouble(i++);
			produto.nuConversaoUnidadesMedida = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.exibeFracaoEmbalagemFornecedorItemPedido) {
			produto.nuFracaoFornecedor = rs.getInt(i++);
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedido() && !populandoCombo) {
			produto.vlGramatura = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.usaPoliticaComercial() || LavenderePdaConfig.isUsaPoliticaBonificacaoConjuntos()) {
			produto.cdConjunto = rs.getString(i++);
		}
		if (LavenderePdaConfig.usaPoliticaComercial()) {
			produto.cdColecao = rs.getString(i++);
			produto.cdStatusColecao = rs.getString(i++);
			produto.flVendaEncerrada = rs.getString(i++);
			produto.cdLinha = rs.getString(i++);
			produto.cdClasse = rs.getString(i++);
			produto.cdMarca = rs.getString(i++);
			if (LavenderePdaConfig.usaFamiliaPadraoPoliticaComercial()) {
				produto.cdFamiliaPadrao = rs.getString(i++);
			}
		}
		if (LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial && ValueUtil.isNotEmpty(produtoFilter.cdCondicaoComercialFilter)) {
			instanceNewItemTabelaPrecoProduto(produto);
			produto.itemTabelaPreco.condComercialExcec = new CondComercialExcec();
			produto.itemTabelaPreco.condComercialExcec.cdEmpresa = rs.getString(i++);
			produto.itemTabelaPreco.condComercialExcec.cdRepresentante = rs.getString(i++);
			produto.itemTabelaPreco.condComercialExcec.cdProduto = rs.getString(i++);
			produto.itemTabelaPreco.condComercialExcec.cdCondicaoComercial = rs.getString(i++);
			produto.itemTabelaPreco.condComercialExcec.cdItemGrade1 = rs.getString(i++);
			produto.itemTabelaPreco.condComercialExcec.vlUnitario = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.usaPedidoProdutoCritico) {
			produto.flCritico = rs.getString(i++);
		}
		if ((LavenderePdaConfig.apresentaMarcadorProdutoInsercao || LavenderePdaConfig.apresentaMarcadorProdutoLista) && !populandoCombo) {
			populateMarcador(rs, produto);
			i++;
		}
		if (LavenderePdaConfig.mostraDimensoesNosDetalhes()) {
			produto.dsDimensoes = rs.getString(i++);
		}
		if (LavenderePdaConfig.usaCotacaoMoedaProduto) {
			produto.dsMoedaProduto = rs.getString(i++);
		}
		
		//deve ficar fora dos indices por possuir comportamento diferente
		if (LavenderePdaConfig.usaSimilarVendaProduto && LavenderePdaConfig.usaAgrupadorProdutoSimilar) {
			if (populandoCombo && isAddAliasItemCombo) {
				produto.cdAgrupProdSimilar = rs.getString("TB.CDAGRUPPRODSIMILAR");
			} else {
				produto.cdAgrupProdSimilar = rs.getString("CDAGRUPPRODSIMILAR");
			}
		}
		if (LavenderePdaConfig.usaGradeProduto5() && produtoFilter.filtrandoAgrupadorGrade) {
			produto.filtrandoAgrupadorGrade = true;
			produto.setDsAgrupadorGrade(rs.getString("DSAGRUPADORGRADE"));
			produto.dsAgrupadorGradeComp = rs.getString("DSAGRUPADORGRADECOMP");
			produto.agrupadorGradeLoaded = true;
		}
		if (LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecial()) {
			produto.nuMultiploEspecial = rs.getDouble("NUMULTIPLOESPECIAL");
		}
		if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorItensFormulaJuros()) {
			produto.flFormulaCalculoDeflator = rs.getString("FLFORMULACALCULODEFLATOR");
			produto.vlPctJurosMensal = rs.getDouble("VLPCTJUROSMENSAL");
			produto.qtDiasCarenciaJuros = rs.getInt("QTDIASCARENCIAJUROS");
		}
		if (hasItemTabelaPreco) {
			if (produto.itemTabelaPreco == null) {
				instanceNewItemTabelaPrecoProduto(produto);
			}
			if ((LavenderePdaConfig.isFiltraItemTabelaPrecoListaProduto() || LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial || LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() || populandoCombo || LavenderePdaConfig.usaFiltroComissao)) {
				produto.itemTabelaPreco.cdTabelaPreco = rs.getString("CDTABELAPRECO");
	        	produto.itemTabelaPreco.vlUnitario = ValueUtil.round(rs.getDouble("VLUNITARIO"));
				if( LavenderePdaConfig.usaFiltroComissao){
					produto.itemTabelaPreco.vlPctComissao = rs.getDouble("VLPCTCOMISSAO");
				}
	        }
			if (LavenderePdaConfig.destacaProdutoQuantidadeMaximaVenda && hasItemTabelaPreco) {
				produto.itemTabelaPreco.qtMaxVenda = rs.getDouble("QTMAXVENDA");
			}
			if (LavenderePdaConfig.isUsaFiltraPorProdutosPromocionalListaProdutoEItemPedido()) {
				produto.itemTabelaPreco.flPromocao = rs.getString("flPromocao");
				produto.itemTabelaPreco.nuPromocao = rs.getInt("nuPromocao");
			}
			if (LavenderePdaConfig.isFiltraItemTabelaPrecoListaProduto() && LavenderePdaConfig.isMostraProdutoDescPromocionalDestacadoTelaListaProdutoTipo3()) {
				produto.itemTabelaPreco.vlPctDescPromocional = rs.getDouble("vlPctDescPromocional");
			}
			if (LavenderePdaConfig.isMostraPrecoItemStNaListaProduto()) {
			 	produto.itemTabelaPreco.vlEmbalagemSt = rs.getDouble("VLEMBALAGEMST");
		    }
			if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
				produto.itemTabelaPreco.cdPrazoPagtoPreco = rs.getInt("CDPRAZOPAGTOPRECO");
			}
		}
		if (LavenderePdaConfig.usaDestaqueItensVendidosMesCorrente) {
			produto.flVendido = rs.getString("FLVENDIDO");
		}
		if (LavenderePdaConfig.isMostraQtVendasProdutoNoPeriodo()) {
			produto.qtVendasPeriodo = rs.getInt("QTVENDASPERIODO");
		}
		if (LavenderePdaConfig.isPermiteSelecaoUnidadeAlternativa() && produtoFilter.unidade != null) {
			produto.unidade = new Unidade();
			produto.unidade.cdEmpresa = produto.cdEmpresa;
			produto.unidade.cdRepresentante = produto.cdRepresentante;
			produto.unidade.cdUnidade = produto.cdUnidade;
			produto.unidade.dsUnidade = rs.getString("DSUNIDADE");
			produto.unidade.dsUnidadePlural = rs.getString("DSUNIDADEPLURAL");
		}
		if (produtoFilter.contaProdutosListados) {
			produto.qtProdutosListados = rs.getInt("QTPRODUTOSLISTADOS");
		}
        return produto;
	}
	
	private static void instanceNewItemTabelaPrecoProduto(Produto produto) {
		produto.itemTabelaPreco = new ItemTabelaPreco();
		produto.itemTabelaPreco.cdEmpresa = produto.cdEmpresa;
		produto.itemTabelaPreco.cdRepresentante = produto.cdRepresentante;
		produto.itemTabelaPreco.cdProduto = produto.cdProduto;
	}

	private void setInstanceEstoque(Produto produto) {
		if (produto.estoque == null) {
			produto.estoque = new Estoque();
		}
	}

	@Override
	protected void addJoinSummary(BaseDomain domainFilter, StringBuffer sql) {
		ProdutoBase domain = (ProdutoBase) domainFilter;
		addJoinEstoqueSummary(domain, sql);        
        if (LavenderePdaConfig.usaFiltroAtributoProduto && ValueUtil.isNotEmpty(domain.cdAtributoProduto) && ValueUtil.isNotEmpty(domain.cdAtributoOpcaoProduto)) {
			DaoUtil.addJoinProdutoAtributo(sql, DaoUtil.ALIAS_PRODUTOATRIBUTO, domain.cdAtributoProduto, domain.cdAtributoOpcaoProduto);
        }
        addJoinItemTabelaPreco(domain, sql);
        if (LavenderePdaConfig.filtraProdutoPorGrupoCliente && ValueUtil.isNotEmpty(domain.cdGrupoPermProd)) {
        	DaoUtil.addJoinGrupoCliPermProd(sql, DaoUtil.ALIAS_GRUPOPERMPROD, domain.cdGrupoPermProd);
        }
        if ((LavenderePdaConfig.isUsaKitProduto()) && (domain.cdKit != null)) {
        	DaoUtil.addJoinItemKit(sql, DaoUtil.ALIAS_ITEMKIT, domain.cdKit);
        }
        if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido()) {
        	DaoUtil.addJoinProdutoUnidade(sql, DaoUtil.ALIAS_PRODUTOUNIDADE);
        }
        if (usaCTETributacaoConfig()) {
			DaoUtil.addJoinCTETributacaoConfig(sql, DaoUtil.ALIAS_TRIBUTACAOCONFIG, domain, false);
			}
        if (LavenderePdaConfig.showInformacoesComplementaresInsercMultipla() && LavenderePdaConfig.isPermiteInserirMultiplosItensPorVezNoPedido()) {
        	DaoUtil.addJoinGiroProduto(sql, DaoUtil.ALIAS_GIROPRODUTO, domain.cdCliente);
        }
		if (domain.isFazJoinCTEDescPromocional()) {
			DaoUtil.addJoinCteDescPromocional(sql, domain);
        }
        if (domain.filtraDescMaxProdCli) {
        	DaoUtil.addJoinDescMaxProdCli(sql, domain.cdCliente, "tb");
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
		if (LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial && ValueUtil.isNotEmpty(domain.cdCondicaoComercialFilter)) {
			DaoUtil.addJoinCondComercialExcec(sql, domain);
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
        if (domain.itensAutorizados) {
        	DaoUtil.addJoinItemPedidoAgrupador(sql, DaoUtil.ALIAS_ITEM_PEDIDO,  domain.nuPedidoFilter, domain.flOrigemPedidoFilter, domain.cdProduto, domain.cdAgrupadorSimilaridade);
        }
        if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && LavenderePdaConfig.isExibeComboMenuInferior() && domain.itemComboAgrSimilarFilter != null) {
			DaoUtil.addJoinItemComboAgrSimilar(sql, domain.itemComboAgrSimilarFilter);
        }
        if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && LavenderePdaConfig.isUsaKitTipo3() && domain.itemKitAgrSimilarFilter != null) {
			DaoUtil.addJoinItemKitAgrSimilar(sql, domain.itemKitAgrSimilarFilter);
        }
        if (LavenderePdaConfig.apresentaFiltroDescQtd && LavenderePdaConfig.permiteVincularCliente && domain.filtraProdutoDescQtd) {
        	DaoUtil.addJoinDescQtdCliente(sql, domain.itemTabelaPreco.cdTabelaPreco);
        }
        if (LavenderePdaConfig.usaGradeProduto5() && LavenderePdaConfig.filtraProdutoClienteRepresentante && domain.filtrandoAgrupadorGrade) {
        	DaoUtil.addJoinProdutoClienteExclusivo(sql, domain.cdCliente, "TB");
        }
        if (LavenderePdaConfig.usaGradeProduto5() && domain.filtrandoAgrupadorGrade) {
        	DaoUtil.addJoinProdutoGrade(sql);
        }
		if (LavenderePdaConfig.isUsaGrupoDestaqueProduto()) {
			DaoUtil.addJoinProdutoDestaque(sql, LavenderePdaConfig.usaGrupoDestaqueItemTabPreco ? DaoUtil.ALIAS_ITEMTABELAPRECO : "TB");
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
		if (LavenderePdaConfig.isPermiteSelecaoUnidadeAlternativa() && domain.unidade != null) {
			DaoUtil.addJoinUnidade(sql, domain.unidade);
		}
	}

	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		if (LavenderePdaConfig.usaGradeProduto5() && ((Produto)domainFilter).filtrandoAgrupadorGrade) {
        	DaoUtil.addJoinProdutoGrade(sql);
        }
	}

    public Vector findAllCdProdutoWhereGrupoProduto(String cdGrupoProduto1, String cdGrupoProduto2, String cdGrupoProduto3, String cdGrupoProduto4) throws SQLException {
    	Vector listProduto = new Vector();
    	StringBuffer sql = new StringBuffer();
    	sql.append("SELECT CDPRODUTO FROM ").append(tableName);
    	SqlWhereClause sqlWhereClause = new SqlWhereClause();
    	sqlWhereClause.addAndCondition("CDGRUPOPRODUTO1 = ", cdGrupoProduto1);
    	sqlWhereClause.addAndCondition("CDGRUPOPRODUTO2 = ", cdGrupoProduto2);
    	sqlWhereClause.addAndCondition("CDGRUPOPRODUTO3 = ", cdGrupoProduto3);
    	sqlWhereClause.addAndCondition("CDGRUPOPRODUTO4 = ", cdGrupoProduto4);
    	sql.append(sqlWhereClause.getSql());
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
	    	while (rs.next()) {
	    		listProduto.addElement(rs.getString("CDPRODUTO"));
    	}
	    	return listProduto;
    	}
    	}

	public Vector findAllCdProdutoWhereGrupoProdutoEmpresaRepresentanteCliente(String cdGrupoProduto1, String cdGrupoProduto2, String cdGrupoProduto3, String cdGrupoProduto4, String empresa, String representante) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT TB.CDPRODUTO FROM TBLVPFACEAMENTO TB ");
		sql.append("LEFT JOIN TBLVPPRODUTO PROD ON TB.CDPRODUTO = PROD.CDPRODUTO");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO1 = ", cdGrupoProduto1);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO2 = ", cdGrupoProduto2);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO3 = ", cdGrupoProduto3);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO4 = ", cdGrupoProduto4);
		sqlWhereClause.addAndCondition("PROD.CDEMPRESA = ", empresa);
		sqlWhereClause.addAndCondition("PROD.CDREPRESENTANTE = ", representante);
		sql.append(sqlWhereClause.getSql());
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector listProduto = new Vector();
	    	while (rs.next()) {
				listProduto.addElement(rs.getString(1));
	    	}
	    	return listProduto;
    	}
    }
    
    public Vector findAllCdProdutoWhereGrupoDescProd(String cdGrupoDescProd) throws SQLException {
    	StringBuffer sql = new StringBuffer();
    	sql.append("SELECT CDPRODUTO FROM ").append(tableName);
    	if (ValueUtil.isNotEmpty(cdGrupoDescProd)) {
    		sql.append(" WHERE CDGRUPODESCPROD = ").append(Sql.getValue(cdGrupoDescProd));
    	}
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector listProduto = new Vector();
    		while (rs.next()) {
    			listProduto.addElement(rs.getString("CDPRODUTO"));
    		}
    		return listProduto;
    	}
    }

    protected BasePersonDomain populateColumnsDynFixas(ResultSet rs) throws java.sql.SQLException {
        Produto produto = new Produto();
        produto.cdEmpresa = rs.getString("cdEmpresa");
        produto.cdRepresentante = rs.getString("cdRepresentante");
		produto.cdProduto = rs.getString("cdProduto");
		produto.dsProduto = rs.getString("dsProduto");
		produto.flSituacao = rs.getInt("flSituacao");
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			produto.cdUnidade = rs.getString("cdUnidade");
			produto.nuConversaoUnidadesMedida = rs.getDouble("nuConversaoUnidadesMedida");
		}
		produto.qtMinimaVenda = rs.getDouble("qtMinimaVenda");
		produto.nuMultiploIdeal = rs.getDouble("nuMultiploIdeal");
		if (LavenderePdaConfig.isUsaKitProduto()) {
			produto.flKit = rs.getString("flKit");
		}
		if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
			produto.vlVolume = rs.getDouble("vlVolume");
		}
		if (LavenderePdaConfig.usaCotacaoMoedaProduto) {
			produto.dsMoedaProduto = rs.getString("dsMoedaProduto");
		}
        return produto;
    }
    
    public Produto findProdutoComTributacao(Produto produtoFilter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	addCTESummary(produtoFilter, sql);
    	sql.append(" SELECT ");
    	addSelectSummaryColumns(produtoFilter, sql);
    	sql.append(" FROM ").append(tableName).append(" TB");
    	addJoinSummary(produtoFilter, sql);
    	addWhereByExample(produtoFilter, sql);
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		if (rs.next()) {
    			return (Produto) populateSummary(produtoFilter, rs);
    		}
    	}
    	return null;
    }
    
    public Vector findProdutosByItemCombo(Produto produto, ItemCombo itemCombo, String nuPedido, String cdCliente, String flOrigemPedido, String cdCombo, String flTipoItemCombo, boolean isFechandoPedidoSugestaoCombo) throws SQLException {
    	StringBuffer sql =  getSqlBuffer();
    	addCTESummary(produto, sql);
    	sql.append("SELECT ");
    	addSelectSummaryColumns(produto, sql);
    	sql.append(", tb.CDAGRUPADORSIMILARIDADE, combo.cdCombo CDCOMBO, combo.dsCombo DSCOMBO,");
    	sql.append("itemcombo.FLTIPOITEMCOMBO, itemcombo.VLUNITARIOCOMBO,");
    	sql.append("itemcombo.QTITEMCOMBO, itemcombo.FLAGRUPADORSIMILARIDADE");
    	sql.append(" FROM ");
    	sql.append(tableName).append(" tb");
    	addJoinSummary(produto, sql);
	    addJoinItemCombo(produto, flTipoItemCombo, sql);
	    addJoinCombo(produto, sql);
	    addJoinItemPedido(itemCombo, nuPedido, flOrigemPedido, sql);
	    addJoinProdutoBloqueado(sql);
    	addWhereByExample(produto, sql);
    	if (!LavenderePdaConfig.isExibeComboMenuInferior()) {
    	sql.append(" AND item.CDEMPRESA IS NULL");
    	}
    	if (ValueUtil.isNotEmpty(cdCombo)) {
    		sql.append(" AND itemcombo.CDCOMBO = ").append(Sql.getValue(cdCombo));
    	}
    	if (produto.findListCombo && !LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
		    sql.append(" AND COALESCE(ESTOQUEERP.QTESTOQUE, 0) - COALESCE(ESTOQUEPDA.QTESTOQUE, 0) > 0");
	    }
    	if (LavenderePdaConfig.isUsaBloqueiaProdutoBloqueadoNoPedido() && LavenderePdaConfig.bloqueiaItemTabelaPrecoParaVenda) {
    		sql.append(" AND pb.CDPRODUTO IS NULL");
    	}
		if (LavenderePdaConfig.usaRestricaoVendaClienteProduto && LavenderePdaConfig.isExibeComboMenuInferior() && ItemCombo.TIPOITEMCOMBO_SECUNDARIO.equals(flTipoItemCombo)) {
			DaoUtil.addNotExistsRestricaoProduto(sql, "itemcombo.cdProduto", cdCliente, nuPedido,1, "itemcombo.qtItemCombo", true, false, true, false);
		}
	    sql.append(" AND (combo.DTVIGENCIAINICIAL <= date('now', 'localtime') OR (combo.DTVIGENCIAINICIAL IS NULL OR combo.DTVIGENCIAINICIAL = ''))")
			.append(" AND (combo.DTVIGENCIAFINAL >= date('now', 'localtime') OR (combo.DTVIGENCIAFINAL IS NULL or combo.DTVIGENCIAFINAL =''))");
    	addOrderBy(sql, produto);
        addLimit(sql, produto);
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector(50);
			populandoCombo = true;
			while (rs.next()) {
				result.addElement(populateProdutosByItemCombo(rs, isFechandoPedidoSugestaoCombo));
			}
			return result;
		} finally {
			populandoCombo = false;
			isAddAliasItemCombo = false;
		}
    }
    
	private void addJoinProdutoBloqueado(StringBuffer sql) {
		if (LavenderePdaConfig.isUsaBloqueiaProdutoBloqueadoNoPedido() && LavenderePdaConfig.bloqueiaItemTabelaPrecoParaVenda) {
			sql.append(" LEFT JOIN TBLVPPRODUTOBLOQUEADO pb ON ")
			.append("pb.CDEMPRESA = tb.CDEMPRESA AND ")
			.append("pb.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
			.append("pb.CDPRODUTO = tb.CDPRODUTO AND (")
			.append("pb.CDTABELAPRECO = itemcombo.CDTABELAPRECO OR pb.CDTABELAPRECO = '0')");
		}
	}

	private void addJoinCombo(Produto produto, StringBuffer sql) {
		sql.append(" JOIN TBLVPCOMBO combo ON")
		.append(" combo.CDEMPRESA = tb.CDEMPRESA AND")
		.append(" combo.CDREPRESENTANTE = itemcombo.CDREPRESENTANTE AND")
		.append(" combo.CDCOMBO = itemcombo.CDCOMBO");
		if (ValueUtil.isNotEmpty(produto.cdTabelaPreco)) {
			sql.append(" AND combo.CDTABELAPRECO = itemcombo.CDTABELAPRECO");
		}
	}

    private void addSqlJoinItensComboList(ItemCombo itemCombo, String nuPedido, String flOrigemPedido, StringBuffer sb) {
    	sb.append(" JOIN (")
    	.append(ComboDao.getInstance().getSqlComboItens(itemCombo, nuPedido, flOrigemPedido, false));
    	sb.append(") COMBOS ON itemcombo.CDCOMBO = COMBOS.CDCOMBO");
    }
    
	private BaseDomain populateProdutosByItemCombo(ResultSet rs, boolean isFechandoPedidoSugestaoCombo) throws SQLException {
		isAddAliasItemCombo = !isFechandoPedidoSugestaoCombo;
		Produto produto = (Produto) populateSummary(getBaseDomainDefault(), rs);
    	produto.combo = new Combo();
		produto.combo.cdCombo = rs.getString("cdCombo");
		produto.combo.dsCombo = rs.getString("dsCombo");
		if (!isFechandoPedidoSugestaoCombo) {
			produto.flTipoItemCombo = rs.getString("itemcombo.flTipoItemCombo");
			produto.vlProduto = rs.getDouble("itemcombo.vlUnitarioCombo");
			produto.qtItemPedido = rs.getDouble("itemcombo.qtItemCombo");
			produto.cdAgrupadorSimilaridade = rs.getString("tb.cdAgrupadorSimilaridade");
			produto.flAgrupadorSimilaridade = rs.getString("itemcombo.flAgrupadorSimilaridade");
		} else {
			produto.flTipoItemCombo = rs.getString("flTipoItemCombo");
			produto.vlProduto = rs.getDouble("vlUnitarioCombo");
			produto.qtItemPedido = rs.getDouble("qtItemCombo");
			produto.cdAgrupadorSimilaridade = rs.getString("cdAgrupadorSimilaridade");
			produto.flAgrupadorSimilaridade = rs.getString("flAgrupadorSimilaridade");
		}
		return produto;
    }
    
	public Vector findProdutosByItemComboSummary(Produto produto, ItemCombo itemCombo, String nuPedido, String cdCliente, String flOrigemPedido, String cdCombo, String flTipoItemCombo) throws SQLException {
		StringBuffer sql =  getSqlBuffer();
		sql.append("SELECT ");
		sql.append(" TB.CDPRODUTO,");
		sql.append(" TB.CDEMPRESA,");
		sql.append(" TB.CDREPRESENTANTE,");
		sql.append(" TB.DSPRODUTO,");
		sql.append(" TB.DSREFERENCIA,");
		sql.append(" TB.NUCONVERSAOUNIDADESMEDIDA");
		if (LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoForaPedido() || LavenderePdaConfig.isMostraEstoquePrevistoNaListaProdutosForaPedido()) {
			sql.append(", ((ifnull(").append(DaoUtil.ALIAS_ESTOQUE_ERP).append(".QTESTOQUE, 0)) - (ifnull(").append(DaoUtil.ALIAS_ESTOQUE_PDA).append(".QTESTOQUE, 0))) AS QTESTOQUE");
		} else if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() || LavenderePdaConfig.destacaProdutoQtMinEstoqueLista) {
			sql.append(", ((ifnull(").append(DaoUtil.ALIAS_ESTOQUE_ERP).append(".QTESTOQUE, 0))) AS QTESTOQUE");
		}
		sql.append(", COMBO.CDCOMBO");
		sql.append(", ITEMCOMBO.FLTIPOITEMCOMBO");
		sql.append(", ITEMCOMBO.VLUNITARIOCOMBO");
		sql.append(", ITEMCOMBO.QTITEMCOMBO");
		sql.append(", TB.CDAGRUPADORSIMILARIDADE");
		sql.append(", ITEMCOMBO.FLAGRUPADORSIMILARIDADE");
		sql.append(" FROM ");
		sql.append(tableName).append(" tb");

		if (ItemCombo.TIPOITEMCOMBO_SECUNDARIO.equals(flTipoItemCombo)) {
			addJoinItemTabelaPreco(produto, sql);
		}
		addJoinEstoqueSummary(produto, sql);
		addJoinItemCombo(produto, flTipoItemCombo, sql);
		addJoinCombo(produto, sql);
		addJoinItemPedido(itemCombo, nuPedido, flOrigemPedido, sql);
		addJoinProdutoBloqueado(sql);
    
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("TB.CDEMPRESA = ", produto.cdEmpresa);
		sqlWhereClause.addAndCondition("TB.CDREPRESENTANTE = ", produto.cdRepresentante);
		sqlWhereClause.addAndCondition("TB.CDFORNECEDOR = ", produto.cdFornecedor);
		sqlWhereClause.addAndCondition("TB.CDGRUPOPRODUTO1 = ", produto.cdGrupoProduto1);
		sqlWhereClause.addAndCondition("TB.CDGRUPOPRODUTO2 = ", produto.cdGrupoProduto2);
		sqlWhereClause.addAndCondition("TB.CDGRUPOPRODUTO3 = ", produto.cdGrupoProduto3);
		sqlWhereClause.addAndCondition("TB.CDGRUPOPRODUTO4 = ", produto.cdGrupoProduto4);
		sqlWhereClause.addAndLikeCondition("TB.DSPRINCIPIOATIVO", produto.dsPrincipioAtivo, false);
		if (LavenderePdaConfig.isUsaKitBaseadoNoProduto() && (ValueUtil.VALOR_SIM.equals(produto.flKit))) {
			sqlWhereClause.addAndCondition("TB.FLKIT = ", produto.flKit);
		}

		if (LavenderePdaConfig.usaProdutoRestrito) {
			Cliente cliente = SessionLavenderePda.getCliente();
			if (cliente != null && !cliente.isPossuiDescontoExtraProdutoRestrito()) {
				sqlWhereClause.addStartAndMultipleCondition();
				sqlWhereClause.addOrCondition("TB.FLPRODUTORESTRITO is null");
				sqlWhereClause.addOrConditionForced("TB.FLPRODUTORESTRITO = ", "");
				sqlWhereClause.addOrCondition("TB.FLPRODUTORESTRITO = ", ValueUtil.VALOR_NAO);
				sqlWhereClause.addEndMultipleCondition();
			}
		}
		getProdutoSearchCondition("TB.", produto, sqlWhereClause, false);
		sql.append(sqlWhereClause.getSql());


		if (!LavenderePdaConfig.isExibeComboMenuInferior()) {
			sql.append(" AND item.CDEMPRESA IS NULL");
		}
		if (ValueUtil.isNotEmpty(cdCombo)) {
			sql.append(" AND itemcombo.CDCOMBO = ").append(Sql.getValue(cdCombo));
		}
		if (produto.findListCombo && !LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
			sql.append(" AND COALESCE(ESTOQUEERP.QTESTOQUE, 0) - COALESCE(ESTOQUEPDA.QTESTOQUE, 0) > 0");
		}
		if (LavenderePdaConfig.isUsaBloqueiaProdutoBloqueadoNoPedido() && LavenderePdaConfig.bloqueiaItemTabelaPrecoParaVenda) {
			sql.append(" AND pb.CDPRODUTO IS NULL");
		}
		if (LavenderePdaConfig.usaRestricaoVendaClienteProduto && LavenderePdaConfig.isExibeComboMenuInferior() && ItemCombo.TIPOITEMCOMBO_SECUNDARIO.equals(flTipoItemCombo)) {
			DaoUtil.addNotExistsRestricaoProduto(sql, "itemcombo.cdProduto", cdCliente, nuPedido,1, "itemcombo.qtItemCombo", true, false, true, false);
		}

		if (ItemCombo.TIPOITEMCOMBO_SECUNDARIO.equals(flTipoItemCombo)) {
			sql.append(" AND EXISTS (SELECT 1 FROM (")
					.append(" SELECT estErp.QTESTOQUE QTE, estPda.QTESTOQUE QTA FROM TBLVPPRODUTO p JOIN (")
					.append(" SELECT p2.CDEMPRESA, p2.CDREPRESENTANTE, p2.CDPRODUTO, CASE WHEN ITEMCOMBO.FLAGRUPADORSIMILARIDADE = 'S' THEN p2.CDAGRUPADORSIMILARIDADE ELSE NULL END as CDAGRUPADORSIMILARIDADE")
					.append(" FROM TBLVPPRODUTO p2 ")
					.append(" WHERE p2.CDEMPRESA = tb.CDEMPRESA AND p2.CDREPRESENTANTE = tb.CDREPRESENTANTE  ")
					.append(" AND p2.CDPRODUTO = ITEMCOMBO.CDPRODUTO");
			sql.append(") a ON a.CDEMPRESA = p.CDEMPRESA AND a.CDREPRESENTANTE = p.CDREPRESENTANTE AND (a.CDPRODUTO = p.CDPRODUTO OR a.CDAGRUPADORSIMILARIDADE = p.CDAGRUPADORSIMILARIDADE)")
					.append("LEFT JOIN TBLVPESTOQUE estErp ON estErp.CDEMPRESA = p.CDEMPRESA AND ")
					.append("estErp.CDREPRESENTANTE = p.CDREPRESENTANTE AND ")
					.append("estErp.CDPRODUTO = p.CDPRODUTO AND ")
					.append("estErp.CDITEMGRADE1 = '0' AND ")
					.append("estErp.CDITEMGRADE2 = '0' AND ")
					.append("estErp.CDITEMGRADE3 = '0' AND ")
					.append("estErp.CDLOCALESTOQUE = '0' AND ")
					.append("estErp.FLORIGEMESTOQUE = 'E' ")
					.append("LEFT JOIN TBLVPESTOQUE estPda ON estPda.CDEMPRESA = p.CDEMPRESA AND ")
					.append("estPda.CDREPRESENTANTE = p.CDREPRESENTANTE AND ")
					.append("estPda.CDPRODUTO = p.CDPRODUTO AND ")
					.append("estPda.CDITEMGRADE1 = '0' AND ")
					.append("estPda.CDITEMGRADE2 = '0' AND ")
					.append("estPda.CDITEMGRADE3 = '0' AND ")
					.append("estPda.CDLOCALESTOQUE = '0' AND ")
					.append("estPda.FLORIGEMESTOQUE = 'P' ) tab")
					.append(" WHERE IFNULL(QTE, 0 ) - IFNULL(QTA, 0 ) > 0) ");
		}

		addOrderBy(sql, produto);
		addLimit(sql, produto);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector(50);
			populandoCombo = true;
			while (rs.next()) {
				result.addElement(populateProdutosByItemComboSummary(rs));
			}
			return result;
		} finally {
			populandoCombo = false;
		}
	}

	private void addJoinItemTabelaPreco(ProdutoBase produto, StringBuffer sql) {
		if (isFazJoinComItemTabPreco(produto)) {
			hasItemTabelaPreco = true;
			DaoUtil.addJoinItemTabelaPreco(sql, DaoUtil.ALIAS_ITEMTABELAPRECO, produto.itemTabelaPreco, produto.fromListProduto);
		} else {
			hasItemTabelaPreco = false;
		}
	}

	private void addJoinItemPedido(ItemCombo itemCombo, String nuPedido, String flOrigemPedido, StringBuffer sql) {
		if (!LavenderePdaConfig.isExibeComboMenuInferior()) {
			addSqlJoinItensComboList(itemCombo, nuPedido, flOrigemPedido, sql);
			sql.append(" LEFT JOIN TBLVPITEMPEDIDO item ON")
					.append(" tb.CDEMPRESA = item.CDEMPRESA AND")
					.append(" tb.CDREPRESENTANTE = item.CDREPRESENTANTE AND")
					.append(" item.NUPEDIDO = ").append(Sql.getValue(nuPedido))
					.append(" AND item.FLORIGEMPEDIDO = ").append(Sql.getValue(flOrigemPedido))
					.append(" AND item.CDPRODUTO = itemcombo.CDPRODUTO");
		}
	}

	private void addJoinItemCombo(Produto produto, String flTipoItemCombo, StringBuffer sql) {
		sql.append(" JOIN TBLVPITEMCOMBO itemcombo ON")
				.append(" tb.CDEMPRESA = itemcombo.CDEMPRESA AND")
				.append(" tb.CDREPRESENTANTE = itemcombo.CDREPRESENTANTE AND")
				.append(" tb.CDPRODUTO = itemcombo.CDPRODUTO");
		if (ValueUtil.isNotEmpty(produto.cdTabelaPreco)) {
			sql.append(" AND itemcombo.CDTABELAPRECO = ").append(Sql.getValue(produto.cdTabelaPreco));
		}
		if (ValueUtil.isNotEmpty(flTipoItemCombo)) {
			sql.append(" AND itemCombo.FLTIPOITEMCOMBO = ").append(Sql.getValue(flTipoItemCombo));
		}
	}

	private BaseDomain populateProdutosByItemComboSummary(ResultSet rs) throws SQLException {
		int i = 1;
		Produto produto = new Produto();
		produto.cdProduto = rs.getString(i++);
		produto.cdEmpresa = rs.getString(i++);
		produto.cdRepresentante = rs.getString(i++);
		produto.dsProduto = rs.getString(i++);
		produto.dsReferencia = rs.getString(i++);
		produto.nuConversaoUnidadesMedida = rs.getDouble(i++);
		produto.estoque = new Estoque();
		if(LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoForaPedido() || LavenderePdaConfig.isMostraEstoquePrevistoNaListaProdutosForaPedido() || LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() || LavenderePdaConfig.destacaProdutoQtMinEstoqueLista) {
			produto.estoque.qtEstoque = rs.getDouble(i++);
		}
		produto.combo = new Combo();
		produto.combo.cdCombo = rs.getString(i++);
		produto.flTipoItemCombo = rs.getString(i++);
		produto.vlProduto = rs.getDouble(i++);
		produto.qtItemPedido = rs.getDouble(i++);
		produto.cdAgrupadorSimilaridade = rs.getString(i++);
		produto.flAgrupadorSimilaridade = rs.getString(i++);
		return produto;
	}

	private void addJoinEstoqueSummary(ProdutoBase domain, StringBuffer sql) {
		if (!LavenderePdaConfig.usaControleEstoquePorRemessa || domain.findListCombo) {
			if ((LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoForaPedido() || LavenderePdaConfig.isMostraEstoquePrevistoNaListaProdutosForaPedido() || domain.findListCombo || LavenderePdaConfig.isUsaFiltroEstoqueDisponivel()) && domain.estoque != null) {
					DaoUtil.addJoinEstoque(sql, DaoUtil.ALIAS_ESTOQUE_ERP, domain.estoque.cdLocalEstoque, Estoque.FLORIGEMESTOQUE_ERP);
					DaoUtil.addJoinEstoque(sql, DaoUtil.ALIAS_ESTOQUE_PDA, domain.estoque.cdLocalEstoque, Estoque.FLORIGEMESTOQUE_PDA);
			} else if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() || LavenderePdaConfig.destacaProdutoQtMinEstoqueLista || domain.findListCombo || LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoForaPedido() ) {
				DaoUtil.addJoinEstoque(sql, DaoUtil.ALIAS_ESTOQUE_ERP, Estoque.CD_LOCAL_ESTOQUE_PADRAO, Estoque.FLORIGEMESTOQUE_ERP);
				DaoUtil.addJoinEstoque(sql, DaoUtil.ALIAS_ESTOQUE_PDA, Estoque.CD_LOCAL_ESTOQUE_PADRAO, Estoque.FLORIGEMESTOQUE_PDA);
			}
		}
	}
	
    public Vector findAllProdutoAndQtdVendaPeriodo(Produto produtoFilter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT TB.CDEMPRESA, TB.CDREPRESENTANTE, TB.CDPRODUTO, TB.DSPRODUTO, TB.DSREFERENCIA, COALESCE(SUM(ITEM.QTITEMFISICO), 0) + COALESCE(SUM(ITEMERP.QTITEMFISICO), 0) QTITEM ");
    	sql.append(" FROM TBLVPPRODUTO TB ");
    	sql.append(" LEFT OUTER JOIN TBLVPPEDIDO PED ON ");
    	sql.append(" 	TB.CDEMPRESA = PED.CDEMPRESA ");
    	sql.append(" 	AND TB.CDREPRESENTANTE = PED.CDREPRESENTANTE ");
    	sql.append(" 	AND PED.CDSTATUSPEDIDO != ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoAberto));
    	if (ValueUtil.isNotEmpty(produtoFilter.dtInicialFilter)) {
    		sql.append(" AND PED.DTEMISSAO >= ").append(Sql.getValue(produtoFilter.dtInicialFilter));
    	}
    	if (ValueUtil.isNotEmpty(produtoFilter.dtFinalFilter)) {
    		sql.append(" AND PED.DTEMISSAO <= ").append(Sql.getValue(produtoFilter.dtFinalFilter));
    	}
    	sql.append(" LEFT OUTER JOIN TBLVPITEMPEDIDO ITEM ON ");
    	sql.append(" 	PED.CDEMPRESA = ITEM.CDEMPRESA ");
    	sql.append(" 	AND PED.CDREPRESENTANTE = ITEM.CDREPRESENTANTE ");
    	sql.append(" 	AND PED.NUPEDIDO = ITEM.NUPEDIDO ");
    	sql.append(" 	AND PED.FLORIGEMPEDIDO = ITEM.FLORIGEMPEDIDO ");
    	sql.append(" 	AND TB.CDPRODUTO = ITEM.CDPRODUTO ");
    	
    	sql.append(" LEFT OUTER JOIN TBLVPPEDIDOERP PEDERP ON ");
    	sql.append(" 	TB.CDEMPRESA = PEDERP.CDEMPRESA ");
    	sql.append(" 	AND TB.CDREPRESENTANTE = PEDERP.CDREPRESENTANTE ");
    	sql.append(" 	AND PEDERP.CDSTATUSPEDIDO != ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoCancelado));
    	if (ValueUtil.isNotEmpty(produtoFilter.dtInicialFilter)) {
    		sql.append(" AND PEDERP.DTEMISSAO >= ").append(Sql.getValue(produtoFilter.dtInicialFilter));
    	}
    	if (ValueUtil.isNotEmpty(produtoFilter.dtFinalFilter)) {
    		sql.append(" AND PEDERP.DTEMISSAO <= ").append(Sql.getValue(produtoFilter.dtFinalFilter));
    	}
    	sql.append(" LEFT OUTER JOIN TBLVPITEMPEDIDOERP ITEMERP ON ");
    	sql.append(" 	PEDERP.CDEMPRESA = ITEMERP.CDEMPRESA ");
    	sql.append(" 	AND PEDERP.CDREPRESENTANTE = ITEMERP.CDREPRESENTANTE ");
    	sql.append(" 	AND PEDERP.NUPEDIDO = ITEMERP.NUPEDIDO ");
    	sql.append(" 	AND PEDERP.FLORIGEMPEDIDO = ITEMERP.FLORIGEMPEDIDO ");
    	sql.append(" 	AND TB.CDPRODUTO = ITEMERP.CDPRODUTO ");
    	addWhereByExample(produtoFilter, sql);
    	sql.append(" GROUP BY ");
    	sql.append(" TB.CDEMPRESA, TB.CDREPRESENTANTE, TB.CDPRODUTO, TB.DSPRODUTO, TB.DSREFERENCIA ");
    	addOrderBy(sql, produtoFilter);
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector listProduto = new Vector();
    		Produto produto;
    		while (rs.next()) {
    			produto = new Produto();
    			produto.cdEmpresa = rs.getString("CDEMPRESA");
    			produto.cdRepresentante = rs.getString("CDREPRESENTANTE");
    			produto.cdProduto = rs.getString("CDPRODUTO");
    			produto.dsProduto = rs.getString("DSPRODUTO");
    			produto.dsReferencia = rs.getString("DSREFERENCIA");
    			produto.qtItemPedido = rs.getDouble("QTITEM");
    			listProduto.addElement(produto);
    		}
    		return listProduto;
    	}
    }
    
    public Vector findProdutoListGeracaoRemessa(Produto filter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT tb.CDPRODUTO, tb.DSPRODUTO, SUM(est.QTESTOQUE)")
    	.append(" QTESTOQUE FROM TBLVPPRODUTO tb")
    	.append(" JOIN TBLVPESTOQUE est ON est.CDEMPRESA = tb.CDEMPRESA AND")
    	.append(" est.CDREPRESENTANTE = tb.CDREPRESENTANTE AND")
    	.append(" est.CDPRODUTO = tb.CDPRODUTO")
    	.append(" JOIN TBLVPREMESSAESTOQUE rem ON rem.CDEMPRESA = tb.CDEMPRESA AND")
    	.append(" rem.CDREPRESENTANTE = tb.CDREPRESENTANTE AND")
    	.append(" rem.CDLOCALESTOQUE = est.CDLOCALESTOQUE")
    	.append(" WHERE tb.CDEMPRESA = ").append(Sql.getValue(filter.cdEmpresa)).append(" AND")
    	.append(" tb.CDREPRESENTANTE = ").append(Sql.getValue(filter.cdRepresentante)).append(" AND")
    	.append(" (tb.DSPRODUTO LIKE '%").append(filter.dsProduto).append("%' OR")
    	.append(" tb.CDPRODUTO LIKE '%").append(filter.dsProduto).append("%' ) AND")
    	.append(" (rem.FLTIPOREMESSA <> 'R' OR rem.FLTIPOREMESSA IS NULL) AND")
    	.append(" (rem.FLESTOQUELIBERADO <> 'S' OR rem.FLESTOQUELIBERADO IS NULL) AND")
    	.append(" est.FLORIGEMESTOQUE = 'E'")
    	.append(" GROUP BY tb.CDPRODUTO, tb.DSPRODUTO")
    	.append(" HAVING SUM(est.QTESTOQUE) > 0");
    	Vector list = new Vector(50);
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		while (rs.next()) {
    			Produto produto = populateProdutoRemessa(rs);
    			list.addElement(produto);
    		}
		}
		return list;
    }

	private Produto populateProdutoRemessa(ResultSet rs) throws SQLException {
		Produto produto = new Produto();
		produto.cdProduto = rs.getString("cdProduto");
		produto.dsProduto = rs.getString("dsProduto");
		produto.qtEstoqueProduto = rs.getDouble("qtEstoque");
		return produto;
	}
    
    public Vector getProdutoListDevolucao(boolean parcial) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
		sql.append("SELECT tb.CDPRODUTO, prod.DSPRODUTO, SUM(")
		.append("tb.QTESTOQUE")
		.append(") QTESTOQUE FROM TBLVPESTOQUE tb ")
		.append("JOIN TBLVPREMESSAESTOQUE rem ON ")
		.append("tb.CDEMPRESA = rem.CDEMPRESA AND ")
		.append("tb.CDREPRESENTANTE = rem.CDREPRESENTANTE AND ")
		.append("tb.CDLOCALESTOQUE = rem.CDLOCALESTOQUE ")
		.append("JOIN TBLVPPRODUTO prod ON ")
		.append("tb.CDEMPRESA = prod.CDEMPRESA AND ")
		.append("tb.CDREPRESENTANTE = prod.CDREPRESENTANTE AND ")
		.append("tb.CDPRODUTO = prod.CDPRODUTO ")
		.append("WHERE tb.CDEMPRESA = ").append(Sql.getValue(SessionLavenderePda.cdEmpresa)).append(" AND ")
		.append("tb.CDREPRESENTANTE = ").append(Sql.getValue(SessionLavenderePda.getCdRepresentanteFiltroDados(RemessaEstoque.class))).append(" AND ");
		if (parcial) {
			sql.append("rem.FLTIPOREMESSA = 'R'");
		} else {
			sql.append("(rem.FLTIPOREMESSA <> 'R' OR rem.FLTIPOREMESSA IS NULL)");
		}
		sql.append(" GROUP BY tb.CDPRODUTO");
		Vector list = new Vector(50);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				list.addElement(populateProdutoRemessa(rs));
			}
		}
		return list;
	}
    
    @Override
    protected void addGroupBySummary(BaseDomain domain, StringBuffer sql) {
    	super.addGroupBySummary(domain, sql);
    	ProdutoBase produto = (ProdutoBase) domain;
    	if (produto.filtrandoAgrupadorGrade) {
    		sql.append(" GROUP BY TB.CDEMPRESA, TB.CDREPRESENTANTE, CASE WHEN PRODUTOGRADE.CDPRODUTO IS NOT NULL AND TB.DSAGRUPADORGRADE IS NOT NULL THEN TB.DSAGRUPADORGRADE ELSE tb.rowid END");
    	} else if (produto.joinDescPromocional || produto.joinCondComercialExcec || produto.filtraProdutosInseridos) {
    		sql.append(" GROUP BY TB.CDEMPRESA, TB.CDREPRESENTANTE, TB.CDPRODUTO  ");
    	}
    }
    
    public Produto findProdutoByCdBarras(Produto produto) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
		sql.append("select tb.cdProduto from tblvpfaceamento tb ");
		sql.append("left join tblvpproduto prod on tb.CDPRODUTO = prod.CDPRODUTO ");
		sql.append("where prod.nuCodigoBarras = ").append(Sql.getValue(produto.nuCodigoBarras));
		sql.append(" and tb.cdEmpresa = ").append(Sql.getValue(produto.cdEmpresa));
		sql.append(" and tb.cdRepresentante = ").append(Sql.getValue(produto.cdRepresentante));
		sql.append(" limit 1");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				produto.cdProduto = rs.getString("cdProduto");
			}
		}
		return produto;
    }
	
	public Vector findProdutoByDsPrincipioAtivoDsProduto(Produto produto, String cdGrupoProduto1, String cdGrupoProduto2, String cdGrupoProduto3, String cdGrupoProduto4, String empresa, String representante) throws SQLException {
		Vector listProduto = new Vector();
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT DISTINCT TB.CDPRODUTO FROM ");
		sql.append("(SELECT DISTINCT TB.CDPRODUTO FROM TBLVPFACEAMENTO TB ");
		sql.append("LEFT JOIN TBLVPPRODUTO PROD ON TB.CDPRODUTO = PROD.CDPRODUTO");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO1 = ", cdGrupoProduto1);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO2 = ", cdGrupoProduto2);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO3 = ", cdGrupoProduto3);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO4 = ", cdGrupoProduto4);
		sqlWhereClause.addAndCondition("PROD.CDEMPRESA = ", empresa);
		sqlWhereClause.addAndCondition("PROD.CDREPRESENTANTE = ", representante);
		sql.append(sqlWhereClause.getSql());
		sql.append(")");
		sql.append(" TB ");
		sql.append("LEFT JOIN TBLVPPRODUTO PROD ON PROD.CDPRODUTO = TB.CDPRODUTO ");
		if (produto.dsPrincipioAtivo != null) {
			String dsPrincipioAtivo = ValueUtil.isEmpty(produto.dsPrincipioAtivo) ? "" : produto.dsPrincipioAtivo.replaceAll("%", "");
			sql.append("WHERE PROD.DSPRINCIPIOATIVO LIKE '%").append(dsPrincipioAtivo).append("%'");
		} else if (produto.cdProduto != null) {
			String cdProduto = ValueUtil.isEmpty(produto.cdProduto) ? "" : produto.cdProduto.replaceAll("%", "");
			sql.append("WHERE PROD.CDPRODUTO LIKE '%").append(cdProduto).append("%'");
		} else {
			String dsFilter = ValueUtil.isEmpty(produto.dsProduto) ? "" : produto.dsProduto.replaceAll("%", "");
			sql.append("WHERE PROD.DSPRODUTO LIKE '%").append(dsFilter).append("%'");
		}
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				listProduto.addElement(rs.getString("CDPRODUTO"));
			}
			return listProduto;
		}
	}

	public Vector findProdutoByCategoria(StringBuffer cdProdutoListIn) throws SQLException {
		BaseDomain domainFilter = getBaseDomainDefault();
    	StringBuffer sql = getSqlBuffer();
		sql.append("select ");
		addSelectColumns(domainFilter, sql);
		sql.append(" from ").append(Produto.TABLE_NAME).append(" TB");
		sql.append(" join ");
		sql.append(" (select * from ").append(ProdutoMenuCategoria.TABLE_NAME);
		sql.append(" where CDMENU in (").append(cdProdutoListIn.toString()).append(")) c");
		sql.append(" on c.CDPRODUTO = TB.CDPRODUTO");
		sql.append(" and c.CDREPRESENTANTE = TB.CDREPRESENTANTE");
		sql.append(" and c.CDEMPRESA = TB.CDEMPRESA");
		Vector list = new Vector(50);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				list.addElement(populate(domainFilter, rs));
			}
		}
		return list;
	}

	public Vector findProdutosNotInPesquisaMercado(PesquisaMercadoConfig pesquisaMercadoConfig, String dsFiltro, String sortAtributte, String sortAsc) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		Produto produto = new Produto();
		produto.sortAtributte = sortAtributte;
		produto.sortAsc = sortAsc;
		addCTESummary(produto, sql);
		sql.append("select ");
		addSelectSummaryColumns(produto, sql);
		sql.append(" from ");
		sql.append(Produto.TABLE_NAME);
		sql.append(" tb left join (select CDPRODUTO from (select pmp.CDPRODUTO from ");
		sql.append(PesquisaMercadoProduto.TABLE_NAME);
		sql.append(" pmp join ");
		sql.append(PesquisaMercadoConfig.TABLE_NAME);
		sql.append(" pmc on pmp.CDEMPRESA = pmc.CDEMPRESA");
		sql.append(" and pmp.CDPESQUISAMERCADOCONFIG = pmc.CDPESQUISAMERCADOCONFIG ");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" pmc.CDEMPRESA = ", pesquisaMercadoConfig.cdEmpresa);
		sqlWhereClause.addAndCondition(" pmc.CDREPRESENTANTE = ", pesquisaMercadoConfig.cdRepresentante);
		sqlWhereClause.addAndCondition(" pmc.CDPESQUISAMERCADOCONFIG = ", pesquisaMercadoConfig.cdPesquisaMercadoConfig);
		sql.append(sqlWhereClause.getSql());
		sql.append(" union all select CDPRODUTO from ");
		sql.append(PesquisaMercadoReg.TABLE_NAME);
		sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" CDEMPRESA = ", pesquisaMercadoConfig.cdEmpresa);
		sqlWhereClause.addAndCondition(" CDREPRESENTANTE = ", pesquisaMercadoConfig.cdRepresentante);
		sqlWhereClause.addAndCondition(" CDPESQUISAMERCADOCONFIG = ", pesquisaMercadoConfig.cdPesquisaMercadoConfig);
		sqlWhereClause.addAndConditionOr(" CDCLIENTE = ", new String[] {"0", SessionLavenderePda.getCliente().cdCliente});
		sql.append(sqlWhereClause.getSql());
		sql.append(")) sub on tb.cdproduto = sub.cdproduto ");
		sqlWhereClause = new SqlWhereClause();
		addJoinSummary(produto, sql);
		sqlWhereClause.addAndCondition(" sub.CDPRODUTO is null ");
		sqlWhereClause.addAndCondition(" tb.CDREPRESENTANTE = ", pesquisaMercadoConfig.cdRepresentante);
		sqlWhereClause.addAndCondition(" tb.CDEMPRESA = ", pesquisaMercadoConfig.cdEmpresa);
		sqlWhereClause.addAndOrLikeCondition(" tb.CDPRODUTO", " tb.DSPRODUTO", dsFiltro);
		sql.append(sqlWhereClause.getSql());
		addOrderBy(sql, produto);
		Vector list = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				list.addElement(populateSummary(produto, rs));
			}
		}
		return list;
	}
    
    @Override
    protected Vector findAllSummary(BaseDomain domainFilter, String sql) throws SQLException {
    	if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao || LavenderePdaConfig.apresentaMarcadorProdutoLista) {
    		int queryCount = 0;
    		try (Statement st = getCurrentDriver().getStatement();
    				ResultSet rs = st.executeQuery(sql)) {
    			Map<String, BaseDomain> mapResult = new HashMap<String, BaseDomain>(53);
    			Vector result = new Vector(50);
    			while (rs.next()) {
    				BaseDomain domain;
    				String cdProduto = rs.getString(1);
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
		Produto produto = (Produto) domain;
		if (produto.cdMarcadores == null) produto.cdMarcadores = new Vector();
		String cdMarcador = rs.getString("CDMARCADOR");
		if (ValueUtil.isEmpty(cdMarcador)) return;
		String[] listMarcadores = cdMarcador.split(",");
		if (ValueUtil.isEmpty(listMarcadores)) return;
		for (String cdMarcadorList : listMarcadores) {
			produto.cdMarcadores.addElement(cdMarcadorList.replace("\"", ""));
		}
	}
	
	public Vector findProdutosAgrupador(Produto produto, boolean fromKit, String dsFiltro, Pedido pedido, boolean consideraEstoque) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		produto.findListCombo = true;
		if (!fromKit && produto.itemTabelaPreco != null) {
			produto.itemTabelaPreco.cdUf = ItemTabelaPreco.CDUF_VALOR_PADRAO;
		}
		addCTESummary(produto, sql);
		sql.append("select ");
		addSelectSummaryColumns(produto, sql);
		sql.append(" FROM TBLVPPRODUTO tb");
		addJoinSummary(produto, sql);
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", produto.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", produto.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDAGRUPADORSIMILARIDADE = ", produto.cdAgrupadorSimilaridade);
		sqlWhereClause.addAndOrLikeCondition("tb.DSPRODUTO", "tb.CDPRODUTO", dsFiltro);
		if (produto.itensAutorizados) {
			sqlWhereClause.addAndCondition(DaoUtil.ALIAS_ITEM_PEDIDO + ".CDEMPRESA IS NULL");
		}
		if (consideraEstoque) {
			sqlWhereClause.addAndCondition("(IFNULL(ESTOQUEERP.QTESTOQUE, 0 ) - IFNULL(ESTOQUEPDA.QTESTOQUE, 0 ) > 0 ");
		}
		sql.append(sqlWhereClause.getSql());
		if (pedido != null) {
			if (consideraEstoque) {
				sql.append(" OR ");
			} else {
				sql.append("(");
			}
			sql.append(" tb.CDPRODUTO = ").append(Sql.getValue(produto.cdProduto))
			.append(" OR EXISTS ( SELECT 1 FROM TBLVPITEMPEDIDOAGRSIMILAR WHERE")
			.append(" CDEMPRESA = tb.CDEMPRESA AND CDREPRESENTANTE = tb.CDREPRESENTANTE AND")
			.append(" FLORIGEMPEDIDO = ").append(Sql.getValue(pedido.flOrigemPedido)).append(" AND ")
			.append(" NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido)).append(" AND ")
			.append(" CDPRODUTOSIMILAR = tb.CDPRODUTO ");
			sql.append(")");
		}
		if (consideraEstoque || pedido != null) {
			sql.append(")");
		}
		Vector list = new Vector();
		populandoCombo = true;
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				list.addElement(populateSummary(produto, rs));
			}
		} finally {
			populandoCombo = false;
		}
		return list;
	}

	public Vector findAllProdutoSimilarByAgrupador(Produto produtoFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		addCTESummary(produtoFilter, sql);
		sql.append("select ");
		addSelectSummaryColumns(produtoFilter, sql);
		sql.append(" FROM TBLVPPRODUTO tb");
		addJoinSummary(produtoFilter, sql);
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", produtoFilter.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", produtoFilter.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO <> ", produtoFilter.cdProduto);
		sqlWhereClause.addAndCondition("tb.CDAGRUPPRODSIMILAR = ", produtoFilter.cdAgrupProdSimilar);
		sql.append(sqlWhereClause.getSql());
		if (LavenderePdaConfig.usaRestricaoVendaClienteProduto) {
			DaoUtil.addNotExistsRestricaoProduto(sql, "tb.CDPRODUTO", produtoFilter.cdCliente, null, 1, null, true, false, false, false);
		}
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector list = new Vector();
			ProdutoSimilar produtoSimilar;
			while (rs.next()) {
				produtoSimilar = new ProdutoSimilar();
				produtoSimilar.produto = (Produto) populateSummary(produtoFilter, rs);
				produtoSimilar.cdEmpresa = produtoSimilar.produto.cdEmpresa;
				produtoSimilar.cdRepresentante = produtoSimilar.produto.cdRepresentante;
				produtoSimilar.cdProduto = produtoFilter.cdProduto;
				produtoSimilar.cdProdutoSimilar = produtoSimilar.produto.cdProduto;
				produtoSimilar.buscouProduto = true;
				produtoSimilar.qtEstoque = produtoSimilar.produto.estoque != null ? produtoSimilar.produto.estoque.qtEstoque : 0;
				list.addElement(produtoSimilar);
			}
			return list;
		}
	}


	public Vector findAllProdutoByFamiliaProd(Produto filter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		boolean filtraProdutoPorCliente = LavenderePdaConfig.filtraProdutoClienteRepresentante && filter.produtoClienteFilter != null;
		if (filtraProdutoPorCliente) {
			DaoUtil.addCTEsProdutoCliente(sql, filter.produtoClienteFilter);
		}
		sql.append("SELECT tb.CDPRODUTO, tb.DSPRODUTO");
		if (filtraProdutoPorCliente) {
			sql.append(", ").append(ProdutoClienteDbxDao.getInstance().getFlTipoRelacaoCteCondition(true)).append(" RESTRICAOPRODCLI");
		}
		sql.append(" FROM TBLVPPRODUTO tb ");
		if (filtraProdutoPorCliente) {
			DaoUtil.addJoinCTEProdutoCliente(filter.produtoClienteFilter, sql);
		}
		sqlWhereClause.addAndConditionEquals("tb.CDEMPRESA", filter.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("tb.CDREPRESENTANTE", filter.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("tb.CDFAMILIADESCPROG", filter.cdFamiliaDescProg);
		sql.append(sqlWhereClause.getSql());
		
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector list = new Vector();
			Produto produto;
			while (rs.next()) {
				produto = new Produto();
				produto.cdEmpresa = filter.cdEmpresa;
				produto.cdRepresentante = filter.cdRepresentante;
				produto.cdFamiliaDescProg = filter.cdFamiliaDescProg;
				produto.cdProduto = rs.getString(1);
				produto.dsProduto = rs.getString(2);
				if (filtraProdutoPorCliente) {
					produto.restritoClienteProduto = rs.getInt("RESTRICAOPRODCLI") == 0;
				}
				list.addElement(produto);
			}
			return list;
		}
	}
	
	public boolean isPossuiFamiliasDescProg(Pedido pedido, String cdFamiliaDescProg) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT COUNT(1) FROM TBLVPPRODUTO tb ");
		if (ValueUtil.isEmpty(cdFamiliaDescProg)) {
			sql.append(" JOIN TBLVPITEMPEDIDO it ON ")
			.append(" tb.CDEMPRESA = it.CDEMPRESA AND ")
			.append(" tb.CDREPRESENTANTE = it.CDREPRESENTANTE AND ")
			.append(" it.NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido)).append(" AND ")
			.append(" it.FLORIGEMPEDIDO = ").append(Sql.getValue(pedido.flOrigemPedido));
		}
		sql.append(" JOIN TBLVPDESCPROGCONFIGFAM dpcf ON ")
		.append(" tb.CDEMPRESA = dpcf.CDEMPRESA AND ")
		.append(" tb.CDREPRESENTANTE = dpcf.CDREPRESENTANTE AND ")
		.append(" tb.CDFAMILIADESCPROG = dpcf.CDFAMILIADESCPROG ")
		.append(" JOIN TBLVPDESCPROGCONFIGFACLI dpcc ON ")
		.append(" dpcc.CDDESCPROGRESSIVO = dpcf.CDDESCPROGRESSIVO AND ")
		.append(" tb.CDEMPRESA = dpcc.CDEMPRESA AND ")
		.append(" tb.CDREPRESENTANTE = dpcc.CDREPRESENTANTE AND (")
		.append(" dpcc.CDCLIENTE = ").append(Sql.getValue(pedido.getCliente().cdCliente)).append(" OR ");
		
		String cdFaixaCli = pedido.getCliente().cdFaixaCli;
		
		if (ValueUtil.isEmpty(cdFaixaCli)) {
			sql.append("dpcc.CDFAIXACLI = FAIXACLIENTE.CDFAIXACLI )")
			.append(" LEFT JOIN TBLVPFAIXACLIENTE FAIXACLIENTE ON FAIXACLIENTE.CDCLIENTE = ").append(Sql.getValue(pedido.getCliente().cdCliente))
			.append(" AND FAIXACLIENTE.CDFAIXACLI = dpcc.CDFAIXACLI ")
			.append(" AND FAIXACLIENTE.CDEMPRESA = tb.CDEMPRESA ")
			.append(" AND FAIXACLIENTE.CDREPRESENTANTE = tb.CDREPRESENTANTE ");
		} else {
			sql.append("dpcc.CDFAIXACLI = " + Sql.getValue(cdFaixaCli)).append(")");
		}
		
		sql.append(" JOIN TBLVPDESCPROGRESSIVOCONFIG dp ON ")
		.append(" tb.CDEMPRESA = dp.CDEMPRESA AND ")
		.append(" tb.CDREPRESENTANTE = dp.CDREPRESENTANTE AND ")
		.append(" dp.CDDESCPROGRESSIVO = dpcf.CDDESCPROGRESSIVO AND ")
		.append(Sql.getValue(DateUtil.getCurrentDate())).append(" BETWEEN dp.DTINICIALVIGENCIA AND dp.DTFIMVIGENCIA")
		.append(" WHERE tb.CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa)).append(" AND ")
		.append(" tb.CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante));
		
		if (ValueUtil.isNotEmpty(cdFamiliaDescProg)) {
			sql.append(" AND tb.CDFAMILIADESCPROG = ").append(Sql.getValue(cdFamiliaDescProg));
		}
		return getInt(sql.toString()) > 0;
	}

	@Override
	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		if (LavenderePdaConfig.usaGradeProduto5() && ((ProdutoBase) domain).filtrandoAgrupadorGrade) {
			if (addOrderByAgrupadorGrade(sql, domain)) {
				return;
			}
		}
		if (LavenderePdaConfig.usaPriorizacaoPesquisaItemPedido) {
			addOrderByPriorizaPesquisaItemPedido(sql, domain);
		} else {
			switch (StringUtil.getStringValue(domain.sortAtributte)) {
			case ProdutoBase.SORT_COLUMN_QTESTOQUE:
				addOrderByWithoutAlias(sql, domain);
				break;
			case ProdutoBase.SORT_COLUMN_VLPCTCOMISSAO:
				if (hasItemTabelaPreco) {
					addCustomOrderByAlias(sql, domain, DaoUtil.ALIAS_ITEMTABELAPRECO);
				}
				break;
			case ProdutoBase.SORT_COLUMN_CDPRODUTO:
				if (LavenderePdaConfig.usaOrdemNumericaColunaCodigoProduto) {
					boolean possuiTB = sql.indexOf(" tb ") > -1 || sql.indexOf("tb.") > -1 || sql.indexOf(" TB ") > -1 || sql.indexOf("TB.") > -1;
		    		sql.append(" order by CAST(");
		    		if (possuiTB) {
		    			sql.append("tb.");
		    		}
		    		sql.append("CDPRODUTO AS INT)").append(ValueUtil.getBooleanValue(domain.sortAsc) ? " ASC" : " DESC");
				} else {
					super.addOrderBy(sql, domain);
				}
				break;
			default:
				super.addOrderBy(sql, domain);
				break;
			}
		}
	}

	protected void addOrderByPriorizaPesquisaItemPedido(StringBuffer sql, BaseDomain domain) {
		if (domain == null) {
			return;
		}
		Produto produto = (Produto) domain;
		sql.append(" order by ");
		if (produto.dsPrincipioAtivo != null) {
			String dsPrincipioAtivo = produto.dsPrincipioAtivo.replaceAll("%", "");
			sql.append("(CASE WHEN tb.dsPrincipioAtivo = '").append(dsPrincipioAtivo).append("' THEN 1 ")
					.append("WHEN tb.dsPrincipioAtivo LIKE '").append(dsPrincipioAtivo).append("%'").append("THEN 2 ")
					.append("ELSE 3 END)");
		} else if (produto.dsAplicacaoProduto != null) {
			String dsAplicacaoProduto = produto.dsAplicacaoProduto.replaceAll("%", "");
			sql.append("(CASE WHEN tb.dsAplicacaoProduto = '").append(dsAplicacaoProduto).append("' THEN 1 ")
					.append("WHEN tb.dsAplicacaoProduto LIKE '").append(dsAplicacaoProduto).append("%'").append("THEN 2 ")
					.append("ELSE 3 END)");
		} else {
			String dsFilter = ValueUtil.isEmpty(produto.dsProduto) ? "" : produto.dsProduto.replaceAll("%", "");
			if (ValueUtil.isEmpty(dsFilter) && produto.cdProduto != null) {
				dsFilter = produto.cdProduto.replaceAll("%", "");
			}
			sql.append("(CASE WHEN tb.dsProduto = '").append(dsFilter).append("' THEN 1")
					.append(" WHEN tb.cdProduto = '").append(dsFilter).append("' THEN 2")
					.append(" WHEN tb.dsProduto LIKE '").append(dsFilter).append("%' THEN 3")
					.append(" WHEN tb.cdProduto LIKE '").append(dsFilter).append("%' THEN 4")
					.append(" ELSE 5 END)");
		}
	}

	private boolean addOrderByAgrupadorGrade(StringBuffer sql, BaseDomain domain) {
		if (ProdutoBase.SORT_COLUMN_DSPRODUTO.equalsIgnoreCase(domain.sortAtributte)) {
			sql.append(" order by CASE WHEN PRODUTOGRADE.CDPRODUTO IS NOT NULL AND TB.DSAGRUPADORGRADE IS NOT NULL THEN TB.DSAGRUPADORGRADE ELSE tb.DSPRODUTO END")
			.append(ValueUtil.getBooleanValue(domain.sortAsc) ? " ASC " : " DESC ");
			return true;
		} else if (ProdutoBase.SORT_COLUMN_CDPRODUTO.equalsIgnoreCase(domain.sortAtributte)) {
			sql.append(" order by CASE WHEN PRODUTOGRADE.CDPRODUTO IS NOT NULL AND TB.DSAGRUPADORGRADE IS NOT NULL THEN TB.DSAGRUPADORGRADE ELSE tb.CDPRODUTO END")
			.append(ValueUtil.getBooleanValue(domain.sortAsc) ? " ASC " : " DESC ");
			return true;
		}
		return false;
	}
	
	public Produto getProdutoByGrade(Produto produto) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ");
		addSelectColumns(produto, sql);
		sql.append(" FROM TBLVPPRODUTO TB ")
		.append(" JOIN TBLVPPRODUTOGRADE PRODUTOGRADE ON ")
		.append(" TB.CDEMPRESA = PRODUTOGRADE.CDEMPRESA AND ")
		.append(" TB.CDREPRESENTANTE = PRODUTOGRADE.CDREPRESENTANTE AND ")
		.append(" TB.CDPRODUTO = PRODUTOGRADE.CDPRODUTO ");
		addWhereByExample(produto, sql);
		sql.append(" AND TB.DSAGRUPADORGRADE = ").append(Sql.getValue(produto.getDsAgrupadorGrade())).append(" AND ")
		.append(" PRODUTOGRADE.CDITEMGRADE1 = ").append(Sql.getValue(produto.cdItemGrade1)).append(" AND ")
		.append(" PRODUTOGRADE.CDITEMGRADE2 = ").append(Sql.getValue(produto.cdItemGrade2)).append(" AND ")
		.append(" PRODUTOGRADE.CDITEMGRADE3 = ").append(Sql.getValue(produto.cdItemGrade3));
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {
				return (Produto) populate(produto, rs);
			}
			return null;
		}
	}
	
	public Vector findProdutoByGrade(Produto filter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		ProdutoBase ultimoProdutoFilter = SessionLavenderePda.getUltimoProdutoFilter();
		sql.append("SELECT ");
		addSelectColumns(filter, sql);
		sql.append(" , PRODUTOGRADE.CDITEMGRADE1, PRODUTOGRADE.CDITEMGRADE2, PRODUTOGRADE.CDITEMGRADE3");
		if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao || LavenderePdaConfig.apresentaMarcadorProdutoLista) {
			sql.append(", ").append(DaoUtil.ALIAS_MARCPROD1).append(".CDMARCADOR");
		}
		sql.append(" FROM TBLVPPRODUTO TB ");
		if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPrecoTipo3() && filter.itemTabelaPreco != null) {
			DaoUtil.addJoinItemTabelaPreco(sql, DaoUtil.ALIAS_ITEMTABELAPRECO, filter.itemTabelaPreco, filter.fromListProduto);
		}
		if (ultimoProdutoFilter != null) {
			consideraUltimoProdutoFilter = true;
			if (LavenderePdaConfig.filtraProdutoPorTipoPedido && ValueUtil.isNotEmpty(ultimoProdutoFilter.cdTipoPedidoFilter)) {
				DaoUtil.addJoinProdutoTipoPedido(sql, ultimoProdutoFilter);
			}
			if (LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda() && ultimoProdutoFilter.cdPlataformaVendaFilter != null) {
				DaoUtil.addJoinPlataformaVendaProduto(sql, ultimoProdutoFilter.cdPlataformaVendaFilter, "TB");
			}
			if (LavenderePdaConfig.isUsaFiltroProdutosPorCentroCusto() && LavenderePdaConfig.isUsaCentroCustoInformacoesAdicionais() && ValueUtil.isNotEmpty(ultimoProdutoFilter.cdCentroCustoProdFilter)) {
				DaoUtil.addJoinCentroCustoProd(sql, ultimoProdutoFilter, "TB");
			}
			if (ValueUtil.isNotEmpty(ultimoProdutoFilter.cdStatusEstoque)) {
				addJoinEstoqueSummary(ultimoProdutoFilter, sql);
			}
			filter.cdMarcadores = ultimoProdutoFilter.cdMarcadores;
		}
		if (LavenderePdaConfig.filtraProdutoClienteRepresentante) {
			DaoUtil.addJoinProdutoClienteExclusivo(sql, SessionLavenderePda.getCliente().cdCliente, "TB");
		}
		sql.append(" JOIN TBLVPPRODUTOGRADE PRODUTOGRADE ON ")
		.append(" TB.CDEMPRESA = PRODUTOGRADE.CDEMPRESA AND ")
		.append(" TB.CDREPRESENTANTE = PRODUTOGRADE.CDREPRESENTANTE AND ")
		.append(" TB.CDPRODUTO = PRODUTOGRADE.CDPRODUTO ");
		if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao || LavenderePdaConfig.apresentaMarcadorProdutoLista) {
        	DaoUtil.addJoinMarcadores(sql, DaoUtil.ALIAS_MARCPROD1, filter.fromListProduto);
        }
		filter.cdStatusEstoque = ultimoProdutoFilter != null ? ultimoProdutoFilter.cdStatusEstoque : null;
		addWhereByExample(filter, sql);
		sql.append(" AND TB.DSAGRUPADORGRADE = ").append(Sql.getValue(filter.getDsAgrupadorGrade())).append(" AND ")
		.append(" PRODUTOGRADE.CDITEMGRADE1 = ").append(Sql.getValue(filter.cdItemGrade1));
		
		if (LavenderePdaConfig.filtraProdutoClienteRepresentante) {
        	sql.append(" AND (PRODCLIENTE.FLTIPORELACAO IS NOT NULL OR")
	    	.append(" NOT EXISTS ")
	    	.append("(SELECT 1 FROM TBLVPPRODUTOCLIENTE WHERE")
	    	.append(" CDEMPRESA = TB.CDEMPRESA AND ")
	    	.append(" CDREPRESENTANTE = TB.CDREPRESENTANTE AND ")
	    	.append(" CDPRODUTO = TB.CDPRODUTO AND ")
	    	.append(" CDCLIENTE <> ").append(Sql.getValue(SessionLavenderePda.getCliente().cdCliente))
	    	.append(" AND FLTIPORELACAO = 'X'")
	    	.append("))");
        }
		consideraUltimoProdutoFilter = false;
		Vector list = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				Produto produto = (Produto) populate(filter, rs);
				produto.cdItemGrade1 = rs.getString("cdItemGrade1");
				produto.cdItemGrade2 = rs.getString("cdItemGrade2");
				produto.cdItemGrade3 = rs.getString("cdItemGrade3");
				if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao || LavenderePdaConfig.apresentaMarcadorProdutoLista) {
					populateMarcador(rs, produto);
				}
				list.addElement(produto);
			}
			return list;
		}
	}
	
	public String getDsAgrupadorGradeByRowKey(ProdutoBase produto) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT CASE WHEN PRODUTOGRADE.CDPRODUTO IS NOT NULL THEN TB.DSAGRUPADORGRADE ELSE NULL END DSAGRUPADORGRADE ")
		.append(" FROM TBLVPPRODUTO TB");
		DaoUtil.addJoinProdutoGrade(sql);
		sql.append(" WHERE TB.ROWKEY = ").append(Sql.getValue(produto.getRowKey()));
		return getString(sql.toString());
	}
	
	public Vector findProdutoComNmFotoByItemPedido(ItemPedido itemPedido, String cdRepresentante) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		Produto produto = itemPedido.getProduto();
		ItemTabelaPreco itemTabPreco = itemPedido.getItemTabelaPreco();
		
		sql.append("SELECT PROD.CDEMPRESA, PROD.CDREPRESENTANTE, PROD.CDPRODUTO");
		if (LavenderePdaConfig.usaFotoProdutoPorEmpresa) {
			DaoUtil.getSelectNmFotoProdutoEmp(sql);
		} else if (LavenderePdaConfig.mostraFotoProduto || LavenderePdaConfig.isLoadImagesOnProdutoList()) {
			DaoUtil.getSelectNmFotoProduto(sql);
        }
		sql.append(" FROM TBLVPPRODUTOGRADE TB ");
		DaoUtil.addJoinProduto(sql, DaoUtil.ALIAS_PRODUTO, false);
		if (itemTabPreco != null) {
			DaoUtil.addJoinItemTabelaPreco(sql, DaoUtil.ALIAS_ITEMTABELAPRECO, DaoUtil.ALIAS_PRODUTO, itemTabPreco, false, false);
		}
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("PROD.CDEMPRESA = ", itemPedido.cdEmpresa);
		sqlWhereClause.addAndCondition("PROD.CDREPRESENTANTE = ", cdRepresentante);
		sqlWhereClause.addAndCondition("TB.CDTABELAPRECO = ", LavenderePdaConfig.usaGradeProdutoPorTabelaPreco ? itemPedido.cdTabelaPreco : ProdutoGrade.CDTABELAPRECO_PADRAO);
		sqlWhereClause.addAndCondition("PROD.DSAGRUPADORGRADE = ", produto.getDsAgrupadorGrade());
		sql.append(sqlWhereClause.getSql());
		Vector produtoList = new Vector(0);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				Produto produtoResult = new Produto();
				produtoResult.fotoProduto = new FotoProduto();
				produtoResult.cdEmpresa = rs.getString("CDEMPRESA");
				produtoResult.cdRepresentante = rs.getString("CDREPRESENTANTE");
				produtoResult.cdProduto = rs.getString("CDPRODUTO");
				produtoResult.fotoProduto.nmFoto = rs.getString("NMFOTO");
				produtoList.addElement(produtoResult);
			}
			return produtoList;
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
		
		if (usaCTETributacaoConfig()) {
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
		return (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() && LavenderePdaConfig.permiteAlterarValorItemComIPI) || LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado;
	}
	private boolean isFazJoinComItemTabPreco(ProdutoBase produto) {
		return (ProdutoService.getInstance().isParametrizadoJoinComItemTabelaPreco() || produto.findListCombo) && produto.itemTabelaPreco != null && ValueUtil.isNotEmpty(produto.itemTabelaPreco.cdTabelaPreco);
	}
	
	public void updateFlVendidoProduto(Produto produto, String flVendido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE ").append(tableName).append(" SET");
		sql.append(" FLVENDIDO = ").append(Sql.getValue(flVendido));
		sql.append(" where CDEMPRESA = ").append(Sql.getValue(produto.cdEmpresa));
		sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(produto.cdRepresentante));
		sql.append(" and CDPRODUTO = ").append(Sql.getValue(produto.cdProduto));
		executeUpdate(sql.toString());
	}

	public Vector findProdutoGradeComDsProdutoByItemPedido(ItemPedido itemPedido, String cdRepresentante) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		Produto produto = itemPedido.getProduto();
		ItemTabelaPreco itemTabPreco = itemPedido.getItemTabelaPreco();
		sql.append("SELECT DISTINCT ");
		sql.append(DaoUtil.ALIAS_PRODUTO + ".CDEMPRESA,").append(DaoUtil.ALIAS_PRODUTO + ".CDREPRESENTANTE,");
		sql.append(DaoUtil.ALIAS_PRODUTO + ".CDPRODUTO,").append(DaoUtil.ALIAS_PRODUTO + ".DSPRODUTO");
		sql.append(" FROM TBLVPPRODUTOGRADE TB ");
		DaoUtil.addJoinProduto(sql, DaoUtil.ALIAS_PRODUTO, false);
		if (itemTabPreco != null) {
			DaoUtil.addJoinItemTabelaPreco(sql, DaoUtil.ALIAS_ITEMTABELAPRECO, DaoUtil.ALIAS_PRODUTO, itemTabPreco, false, false);
		}
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(DaoUtil.ALIAS_PRODUTO + ".CDEMPRESA = ", itemPedido.cdEmpresa);
		sqlWhereClause.addAndCondition(DaoUtil.ALIAS_PRODUTO + ".CDREPRESENTANTE = ", cdRepresentante);
		sqlWhereClause.addAndCondition("TB.CDTABELAPRECO = ", LavenderePdaConfig.usaGradeProdutoPorTabelaPreco ? itemPedido.cdTabelaPreco : ProdutoGrade.CDTABELAPRECO_PADRAO);
		sqlWhereClause.addAndCondition(DaoUtil.ALIAS_PRODUTO + ".DSAGRUPADORGRADE = ", produto.getDsAgrupadorGrade());
		sql.append(sqlWhereClause.getSql());
		Vector produtoList = new Vector(0);
		try (Statement st = getCurrentDriver().getStatement();
			 ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				Produto produtoResult = new Produto();
				produtoResult.cdEmpresa = rs.getString("CDEMPRESA");
				produtoResult.cdRepresentante = rs.getString("CDREPRESENTANTE");
				produtoResult.cdProduto = rs.getString("CDPRODUTO");
				produtoResult.dsProduto = rs.getString("DSPRODUTO");
				produtoList.addElement(produtoResult);
			}
			return produtoList;
		}
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
