package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.builder.ItemTabelaPrecoBuilder;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.service.CondComercialExcecService;
import br.com.wmw.lavenderepda.business.service.CondPagtoProdService;
import br.com.wmw.lavenderepda.business.service.DescPromocionalService;
import br.com.wmw.lavenderepda.business.service.TipoPedProdDescAcresService;
import br.com.wmw.lavenderepda.business.service.DescontoCCPService;
import br.com.wmw.lavenderepda.business.service.DescontoGrupoService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.KitService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.PlataformaVendaCliFinService;
import br.com.wmw.lavenderepda.business.service.ProdutoMargemService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoUnidadeService;
import br.com.wmw.lavenderepda.business.service.TabelaPrecoRepService;
import br.com.wmw.lavenderepda.business.service.TabelaPrecoService;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ItemTabelaPreco extends BasePersonDomain {

	public static final String TABLE_NAME = "TBLVPITEMTABELAPRECO";

	public static final String CDUNIDADE_VALOR_PADRAO = "0";
	public static final String CDUF_VALOR_PADRAO = "0";
	public static final String CDITEMTABELAPRECOZERO = "0";
	public static final int QTITEM_VALOR_PADRAO = 0;
	public static final int CDPRAZOPAGTOPRECO_VALOR_PADRAO = 0;
	public static final String NOMECOLUNA_VLPCTPREVISAORENTABILIDADE = "VLPCTPREVISAORENTABILIDADE";
	public static final String NOMECOLUNA_CDTABELAPRECO = "cdtabelapreco";
	public static final String NOMECOLUNA_ROWKEY = "ROWKEY";
	public static final String NOMECOLUNA_VLUNITARIO = "VLUNITARIO";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdTabelaPreco;
	public String cdProduto;
	public String cdUf;
	public String cdItemGrade1;
	public String cdItemGrade2;
	public String cdItemGrade3;
	public String cdUnidade;
	public int qtItem;
	public int cdPrazoPagtoPreco;
	public String cdLinha;
	public double vlUnitario;
	public double vlUnitarioEspecial;
	public double vlPctMaxDesconto;
	public double vlPctMaxAcrescimo;
	public double vlMaxDesconto;
	public double vlMaxAcrescimo;
	public double vlPrecoEmbalagemPrimaria;
	public String flPromocao;
	public double vlBase;
	public double vlBaseEspecial;
	public double vlPctComissao;
	public double vlPctDescPromocional;
	public double vlPrecoFrete;
	public double vlReducaoOptanteSimples;
	public String flBloqueado;
	public double qtMaxVenda;
	public double vlPrecoEmbalagem;
	public double nuConversaoUnidade;
	public double vlOportunidade;
	public String flPrecoQueda;
	public String flUsaDesconto3;
	public double vlPctPrevisaRentabilidade;
	public double vlPctMargemRentabilidade;
	public int nuPromocao;
	public double vlDescontoPromo;
	public Date dtInicioPromocao;
	public int qtMinItensNormais;
	public double vlPctRentabilidadeEsp;
	public double vlPctRentabilidadeMin;
	public double vlPctMaxParticipacao;
	public double vlMinItemPedido;
	public Date dtVencimentoPreco;
	public double vlPctTaxa;
	public double vlPrecoMaximoConsumidor;
	public double vlPrecoCusto;
	public double vlPctDescValorBase;
	public double vlPctDescAprovado;
	public double qtMinimaVenda;
	public int cdPrazoIni;
	public int cdPrazoFim;
	public double vlUnitPrazoIni;
	public double vlUnitPrazoFim;
	public double vlPctDiretoria;
	public double vlPctVerbaPedido;
	public double vlMaxVerbaConsFor;
	public String flBloqueiaDescPromo;
	public String flBloqueiaDescPadrao;
	public String flBloqueiaDescCondicao;
	public String flBloqueiaDesc2;
	public String flBloqueiaDesc3;
	public double vlBasePontuacao;
	public double vlIndiceComissao;
	public double vlMaoDeObra;
	public double vlPctCustoComercial;
	public String vlNcm;
	public double vlPctIcms;
	public double vlPctSt;
	public double vlPctIpi;
	public double vlPctPis;
	public double vlPctCofins;
	public String vlCst;
	public double vlPctMva;
	public double vlCreditoImpostos;
	public String vlCfop;
	public double vlPctPisCofins;
	public double vlCotacaoMoeda;
	public double vlEmbalagemSt;

	//Não persistentes
	public Vector descontoQuantidadeList;
	private TabelaPreco tabelaPreco;
	private TabelaPrecoRep tabelaPrecoRep;
	private Produto produto;
	public int qtItemFisico;
	public CondComercialExcec condComercialExcec;
	public double vlManualBrutoItem = -1;
	public String cdSegmentoFilter;
	public String cdClienteSegmentoFilter;
	public double vlPctComissaoMinFilter;
	public double vlPctComissaoMaxFilter;
	public boolean filterByVlPctComissaoMinFilter;
	public boolean filterByVlPctComissaoMaxFilter;


	public ItemTabelaPreco() {
		super(null);
		condComercialExcec = new CondComercialExcec();
	}

    public ItemTabelaPreco(String tabelName) {
		super(tabelName);
	}
    
    public ItemTabelaPreco(ItemTabelaPrecoBuilder itemTabelaPrecoBuilder) {
    	super(null);
		this.cdEmpresa = itemTabelaPrecoBuilder.cdEmpresa;
		this.cdRepresentante = itemTabelaPrecoBuilder.cdRepresentante;
		this.cdTabelaPreco = itemTabelaPrecoBuilder.cdTabelaPreco;
		this.cdProduto = itemTabelaPrecoBuilder.cdProduto;
		this.cdUf = itemTabelaPrecoBuilder.cdUf;
		this.condComercialExcec = new CondComercialExcec();
    }
    
	//Override
	public boolean equals(Object obj) {
		if (obj instanceof ItemTabelaPreco) {
			ItemTabelaPreco itemTabelaPreco = (ItemTabelaPreco) obj;
			return
			ValueUtil.valueEquals(cdEmpresa, itemTabelaPreco.cdEmpresa) &&
			ValueUtil.valueEquals(cdRepresentante, itemTabelaPreco.cdRepresentante) &&
			ValueUtil.valueEquals(cdTabelaPreco, itemTabelaPreco.cdTabelaPreco) &&
			ValueUtil.valueEquals(cdUf, itemTabelaPreco.cdUf) &&
			ValueUtil.valueEquals(cdProduto, itemTabelaPreco.cdProduto) &&
			ValueUtil.valueEquals(cdItemGrade1, itemTabelaPreco.cdItemGrade1) &&
			ValueUtil.valueEquals(cdItemGrade2, itemTabelaPreco.cdItemGrade2) &&
			ValueUtil.valueEquals(cdItemGrade3, itemTabelaPreco.cdItemGrade3) &&
			ValueUtil.valueEquals(cdUnidade, itemTabelaPreco.cdUnidade) &&
			ValueUtil.valueEquals(qtItem, itemTabelaPreco.qtItem) &&
			ValueUtil.valueEquals(cdPrazoPagtoPreco, itemTabelaPreco.cdPrazoPagtoPreco);
		}
		return false;
	}

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdTabelaPreco);
    	strBuffer.append(";");
    	if (LavenderePdaConfig.usaPrecoPorUf) {
    		strBuffer.append(cdUf);
    	} else {
    		strBuffer.append(0);
    	}
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
    	strBuffer.append(";");
    	strBuffer.append(cdItemGrade1);
    	strBuffer.append(";");
    	strBuffer.append(cdItemGrade2);
    	strBuffer.append(";");
    	strBuffer.append(cdItemGrade3);
    	strBuffer.append(";");
    	strBuffer.append(cdUnidade);
    	strBuffer.append(";");
    	strBuffer.append(qtItem);
    	strBuffer.append(";");
    	strBuffer.append(cdPrazoPagtoPreco);
        return strBuffer.toString();
    }

	public TabelaPreco getTabelaPreco() throws SQLException {
		if (!ValueUtil.isEmpty(cdTabelaPreco) && ((tabelaPreco == null) || (!cdTabelaPreco.equals(tabelaPreco.cdTabelaPreco)))) {
			tabelaPreco = TabelaPrecoService.getInstance().getTabelaPreco(cdTabelaPreco);
		}
		return tabelaPreco;
	}

	public void setTabelaPreco(TabelaPreco tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}

	public TabelaPrecoRep getTabelaPrecoRep() throws SQLException {
		if (!ValueUtil.isEmpty(cdTabelaPreco) && ((tabelaPrecoRep == null) || (!cdTabelaPreco.equals(tabelaPreco.cdTabelaPreco)))) {
			tabelaPrecoRep = TabelaPrecoRepService.getInstance().getTabelaPrecoRep(cdTabelaPreco);
		}
		return tabelaPrecoRep;
	}

	public void setTabelaPreco(TabelaPrecoRep tabelaPrecoRep) {
		this.tabelaPrecoRep = tabelaPrecoRep;
	}

	public boolean isFlBloqueadoBoolean() {
		return ValueUtil.VALOR_SIM.equals(flBloqueado);
	}

	public boolean isFlPromocao() {
		return ValueUtil.VALOR_SIM.equals(flPromocao);
	}

	public double getNuConversaoUnidade() {
		return nuConversaoUnidade;
	}

	public void setNuConversaoUnidade(double nuConversaoUnidade) {
		if (nuConversaoUnidade == 0) {
			this.nuConversaoUnidade = 1.0;
		} else {
			this.nuConversaoUnidade = nuConversaoUnidade;
		}
	}

	public double getVlBaseRentabilidade(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		double vlBaseCalculo = 0;
		if (LavenderePdaConfig.isUsaVerba()) {
			vlBaseCalculo = itemPedido.getVlPrecoCusto();
		} else {
			vlBaseCalculo = getVlBasePrimario(pedido);
			if (LavenderePdaConfig.usaUnidadeAlternativa && LavenderePdaConfig.naoAplicaIndiceUnidadeAlternativaValorBaseRentabilidade) {
				vlBaseCalculo = getVlBase(pedido, itemPedido, vlBaseCalculo, 0, false, true);
			} else if (LavenderePdaConfig.usaUnidadeAlternativa) {
				vlBaseCalculo = getVlBase(pedido, itemPedido, vlBaseCalculo, 0, false);
			}
		}
		if (vlBaseCalculo == 0) {
			vlBaseCalculo = vlUnitario;
		}
		if (!LavenderePdaConfig.usaUnidadeAlternativa) {
			vlBaseCalculo = aplicaIndicesNoValorBase(pedido, itemPedido, vlBaseCalculo, true);
		}
		return vlBaseCalculo;
	}

	public double getVlBasePrimario(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaPrecoEspecialParaClienteEspecial && pedido.getCliente().isEspecial()) {
			return this.vlBaseEspecial;
		}
		return this.vlBase;
	}
	
	public double getVlBaseFlex(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto && itemPedido.qtdCreditoDesc > 0 && itemPedido.isFlTipoCadastroDesconto()) {
			return itemPedido.vlBaseFlex;
		}
		if (LavenderePdaConfig.usaVlBaseVerbaEDescMaximoPorRedutorCliente) {
			return getVlBaseFlexPorRedutorCliente(pedido.getCondicaoPagamento().vlIndiceFinanceiro, pedido.getCliente().vlPctMaxDesconto);
		}
		double vlBaseCalculo = 0;
		vlBaseCalculo = getVlBase(pedido, itemPedido, LavenderePdaConfig.naoAplicaIndiceClienteNoVlBaseVerba);
		if (LavenderePdaConfig.isUsaMargemProdutoAplicadaNoValorBaseVerba() && itemPedido.getProduto() != null) {
			vlBaseCalculo = vlBaseCalculo * (1 - itemPedido.getProduto().vlPctMargemProduto / 100);
		}
		if (LavenderePdaConfig.isUsaKitProdutoFechado() && itemPedido.isFazParteKitFechado() && !KitService.getInstance().isEditaDescontoKit(itemPedido.cdKit)) {
			vlBaseCalculo = vlBaseCalculo * (1 - itemPedido.getVlPctDescKitFechado() / 100);
		}
		if (LavenderePdaConfig.usaIndiceFinanceiroSupRep) {
			vlBaseCalculo = ItemTabelaPrecoService.getInstance().aplicaIndiceFinanceiroSupRep(vlBaseCalculo);
		}
		if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido && LavenderePdaConfig.isUsaDescontoPorTipoPedidoEProduto()){
			vlBaseCalculo = TipoPedProdDescAcresService.getInstance().getVlBaseProduto(itemPedido);
		}
		vlBaseCalculo = ValueUtil.round(vlBaseCalculo);
		if (LavenderePdaConfig.usaDescQuantidadePesoAplicaDescNoVlBaseFlex() && !ItemPedidoService.getInstance().produtoDescQuantidadePesoBloqueado(pedido, itemPedido)) {
			if (LavenderePdaConfig.usaFaixaPesoPorTabelaPreco()) {
				vlBaseCalculo = ItemPedidoService.getInstance().calculaDescVlBaseItemPorPesoItemPedidoTabelaPreco(pedido, itemPedido, vlBaseCalculo);
			} else {
				vlBaseCalculo = ItemPedidoService.getInstance().calculaDescVlBaseItemPorPesoPedido(pedido, itemPedido, vlBaseCalculo, pedido.isRecalculandoVlBaseFlexItens);
			}
		}
		return vlBaseCalculo;
	}

	//Metodo com test case
	public double getVlBaseFlexPorRedutorCliente(double vlIndiceFinanceiroCondPagto, double vlPctMaxDescontoCli) {
		vlIndiceFinanceiroCondPagto = vlIndiceFinanceiroCondPagto != 0 ? vlIndiceFinanceiroCondPagto : 1;
		double vlBase = vlUnitario * vlIndiceFinanceiroCondPagto;
		double vlDesc = vlPctMaxDescontoCli <= vlPctDescValorBase ? (vlPctDescValorBase - vlPctMaxDescontoCli) / 100 * vlBase : 0;
		return  ValueUtil.round(vlBase - vlDesc);
	}
	
	public double getVlBase(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		return getVlBase(pedido, itemPedido, false);
	}

	public double getVlBase(Pedido pedido, ItemPedido itemPedido, boolean naoAplicaIndiceCliente) throws SQLException {
		double vlBaseCalculo = getVlBasePrimario(pedido);
		return getVlBase(pedido, itemPedido, vlBaseCalculo, DescPromocionalService.getInstance().findVlFinalProdutoDescPromocional(itemPedido, vlBaseCalculo), naoAplicaIndiceCliente);
	}
	
	public double getVlBase(Pedido pedido, ItemPedido itemPedido, double vlBaseCalculo, double vlFinalProdutoGrupoDescPromo, boolean naoAplicaIndiceCliente) throws SQLException {
		return getVlBase(pedido, itemPedido, vlBaseCalculo, vlFinalProdutoGrupoDescPromo, naoAplicaIndiceCliente, false);
	}

	private double getVlBase(Pedido pedido, ItemPedido itemPedido, double vlBaseCalculo, double vlFinalProdutoGrupoDescPromo, boolean naoAplicaIndiceCliente, boolean naoAplicaIndiceVlBaseRentabilidade) throws SQLException {
		if (vlFinalProdutoGrupoDescPromo != 0) {
			boolean aplicouValorFinalDescPromocional = itemPedido.getDescPromocional() != null && itemPedido.getDescPromocional().vlProdutoFinal > 0;
			vlFinalProdutoGrupoDescPromo = vlFinalProdutoGrupoDescPromo != -1 ? vlFinalProdutoGrupoDescPromo : 0;
			if (aplicouValorFinalDescPromocional) {
				if (LavenderePdaConfig.aplicaIndicesNaUnidadePadraoParaUnidadeAlternativa) {
					ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
					if (produtoUnidade != null) {
						vlFinalProdutoGrupoDescPromo = ItemPedidoService.getInstance().aplicaMultiplicacaoDivisao(itemPedido,  produtoUnidade, vlFinalProdutoGrupoDescPromo);
					}
				}
				return vlFinalProdutoGrupoDescPromo;
			}
			return aplicaIndicesNaDescPromocional(pedido, itemPedido, vlFinalProdutoGrupoDescPromo);
		}
		String cdCondicaoComercial = LavenderePdaConfig.permiteCondComercialItemDiferentePedido ? itemPedido.cdCondicaoComercial : pedido.cdCondicaoComercial;
		String cdItemGrade1Ref = LavenderePdaConfig.usaGradeProduto1() ? itemPedido.cdItemGrade1 : ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		CondComercialExcec condComercialExcec = getCondComercialExcec(cdCondicaoComercial, cdItemGrade1Ref);
		double vlBaseCondComercialExcec = condComercialExcec != null ? condComercialExcec.vlBase : 0;
		if (vlBaseCondComercialExcec > 0) {
			vlBaseCalculo = vlBaseCondComercialExcec;
		}
		return aplicaIndicesNoValorBase(pedido, itemPedido, vlBaseCalculo, vlBaseCondComercialExcec == 0, naoAplicaIndiceCliente, naoAplicaIndiceVlBaseRentabilidade);
	}

	public double getVlBaseEspecial(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		return aplicaIndicesNoValorBase(pedido, itemPedido, this.vlBaseEspecial, true);
	}
	
	private double aplicaIndicesNaDescPromocional(Pedido pedido, ItemPedido itemPedido, double vlBaseCalculo) throws SQLException {
		if (LavenderePdaConfig.usaUnidadeAlternativa && (!LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo() || itemPedido.usandoPrecoUnidadePadraoConvertida)) {
			vlBaseCalculo = ItemPedidoService.getInstance().getVlBaseByVlUnidadeAlternativa(itemPedido, vlBaseCalculo);
		}
		if (LavenderePdaConfig.usaCondicaoComercialPedido) {
			if (!ItemPedidoService.getInstance().ignoraIndiceFinanceiroCondComercialTabPrecoOrOProdProm(itemPedido)) {
				CondicaoComercial condicaoComercial = LavenderePdaConfig.permiteCondComercialItemDiferentePedido ? itemPedido.getCondicaoComercial() : pedido.getCondicaoComercial();
				if (condicaoComercial != null) {
					double vlIndiceFinanceiro = condicaoComercial.vlIndiceFinanceiro;
					double vlIndiceFlex = condicaoComercial.vlIndiceVerba;
					//--
					if (vlIndiceFinanceiro > 0 && (condicaoComercial.isIndiceVerbaSequencial() || vlIndiceFlex == 0)) {
						vlBaseCalculo = roundUnidadeAlternativa(vlBaseCalculo * vlIndiceFinanceiro);
					}
					if (vlIndiceFlex > 0) {
						vlBaseCalculo = vlBaseCalculo * vlIndiceFlex;
					}
					vlBaseCalculo = roundUnidadeAlternativa(vlBaseCalculo);
				}
			}
		}
		if (!(LavenderePdaConfig.isAplicaMaiorDescontoNoItemPedido() && !"3".equals(LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido)) && !LavenderePdaConfig.isAplicaDescontoCategoria()) {
			double vlIndiceFinanceiro = ItemPedidoService.getInstance().getIndiceFinanceiroCliente(pedido);
			if (vlIndiceFinanceiro != 0) {
				vlBaseCalculo = roundUnidadeAlternativa(vlBaseCalculo * vlIndiceFinanceiro);
			}
		}
		if (!(ItemPedidoService.getInstance().isAnulaIndiceFinanceiroCondPagtoTabPrecoPromOrProdProm(itemPedido))) {
			boolean aplicouIndiceEspecialVlBase = false;
			if (LavenderePdaConfig.usaIndiceEspecialCondPagtoProd && LavenderePdaConfig.indiceFinanceiroCondPagtoVlBase) {
				double vlIndiceFinanceiro = CondPagtoProdService.getInstance().findVlIndiceFinanceiroCondPagtoPorProduto(pedido, itemPedido);
				if (vlIndiceFinanceiro != 0) {
					vlBaseCalculo = roundUnidadeAlternativa(vlBaseCalculo * vlIndiceFinanceiro);
					aplicouIndiceEspecialVlBase = true;
				} 
			}
			if (LavenderePdaConfig.isIndiceFinanceiroCondPagtoVlItemPedido() && LavenderePdaConfig.indiceFinanceiroCondPagtoVlBase && !aplicouIndiceEspecialVlBase && !LavenderePdaConfig.isAplicaDescontoCategoria()) {
				double vlIndiceFinanceiro = ItemPedidoService.getInstance().getIndiceFinanceiroCondPagtoVlItemPedido(pedido, itemPedido);
				if (vlIndiceFinanceiro == 0) {
					vlIndiceFinanceiro = 1;
				}
				vlBaseCalculo = roundUnidadeAlternativa(vlBaseCalculo * vlIndiceFinanceiro);
			}
		}
		if (LavenderePdaConfig.aplicaIndicesNaUnidadePadraoParaUnidadeAlternativa) {
			ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
			if (produtoUnidade != null) {
				vlBaseCalculo = ItemPedidoService.getInstance().aplicaMultiplicacaoDivisao(itemPedido,  produtoUnidade, vlBaseCalculo);
			} else {
				vlBaseCalculo = ValueUtil.round(vlBaseCalculo);
			}
		}
		if (vlBaseCalculo < 0) {
			vlBaseCalculo = 0;
		}
		return vlBaseCalculo;
	}

	public double roundUnidadeAlternativa(double value) {
		if (LavenderePdaConfig.aplicaIndicesNaUnidadePadraoParaUnidadeAlternativa) {
			return value;
		} else {
			return ValueUtil.round(value);
		}
	}
	
	private double aplicaIndicesNoValorBase(Pedido pedido, ItemPedido itemPedido, double vlBaseCalculo, boolean aplicaIndiceCondicaoComecial) throws SQLException {
		return aplicaIndicesNoValorBase(pedido, itemPedido, vlBaseCalculo, aplicaIndiceCondicaoComecial, false);
	}
	
	private double aplicaIndicesNoValorBase(Pedido pedido, ItemPedido itemPedido, double vlBaseCalculo, boolean aplicaIndiceCondicaoComecial, boolean naoAplicaIndiceCliente) throws SQLException {
		return aplicaIndicesNoValorBase(pedido, itemPedido, vlBaseCalculo, aplicaIndiceCondicaoComecial, naoAplicaIndiceCliente, false);
	}
	
	private double aplicaIndicesNoValorBase(Pedido pedido, ItemPedido itemPedido, double vlBaseCalculo, boolean aplicaIndiceCondicaoComecial, boolean naoAplicaIndiceCliente, boolean naoAplicaIndiceVlBaseRentabilidade) throws SQLException {
		if (LavenderePdaConfig.isUsaDescontoQtdPorGrupo() && LavenderePdaConfig.aplicaDescQtdPorGrupoProdFecharPedido && (pedido.onFechamentoPedido || !pedido.isPedidoAberto()) && !PedidoService.getInstance().possuiCondicaoComercial(pedido)) {
			double vlPctMaxDescGrupo = !LavenderePdaConfig.usaAplicacaoMaiorDescontoEmCascata ? DescontoGrupoService.getInstance().getPctMaxDescontoPorGrupoEQuantidade(pedido, itemPedido) : 0;
			if (vlPctMaxDescGrupo != 0) {
				vlBaseCalculo = roundUnidadeAlternativa(vlBaseCalculo * (1 - (vlPctMaxDescGrupo / 100)));
			}
		}
		if (LavenderePdaConfig.usaUnidadeAlternativa && (!LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo() || itemPedido.usandoPrecoUnidadePadraoConvertida)) {
			if (!LavenderePdaConfig.naoAplicaIndiceUnidadeAlternativaValorBaseRentabilidade) {
				vlBaseCalculo = ItemPedidoService.getInstance().getVlBaseByVlUnidadeAlternativa(itemPedido, vlBaseCalculo);
			} else {
				vlBaseCalculo = ItemPedidoService.getInstance().getVlBaseByVlUnidadeAlternativa(itemPedido, vlBaseCalculo, naoAplicaIndiceVlBaseRentabilidade);
			}
		}
		if (LavenderePdaConfig.aplicaDescontoCCPAposInserirItem) {
			double vlPctDescontoCCP = itemPedido.vlPctDescontoCCP;
			if (vlPctDescontoCCP == 0) {
				vlPctDescontoCCP = DescontoCCPService.getInstance().getDescCCPItemPedido(itemPedido, pedido.getCliente());
			}
			vlBaseCalculo = roundUnidadeAlternativa(vlBaseCalculo * (1 - (vlPctDescontoCCP / 100)));
		}
		if (LavenderePdaConfig.usaVariacaoPrecoProdutoPorCategoriaERegiaoCliente || LavenderePdaConfig.aplicaVariacaoPrecoProdutoPorCliente) {
			double vlVariacaoPreco = 1;
			if (LavenderePdaConfig.aplicaVariacaoPrecoProdutoPorCliente) {
				vlVariacaoPreco = ItemPedidoService.getInstance().getVlVariacaoPrecoProdCli(itemPedido);
			}
			if (LavenderePdaConfig.usaVariacaoPrecoProdutoPorCategoriaERegiaoCliente && vlVariacaoPreco == 1) {
				vlVariacaoPreco = ItemPedidoService.getInstance().getVlVariacaoPrecoProduto(itemPedido);
			}
			vlBaseCalculo = roundUnidadeAlternativa(vlBaseCalculo * vlVariacaoPreco);
		}
		if (LavenderePdaConfig.usaCondicaoComercialPedido && aplicaIndiceCondicaoComecial) {
			if (!ItemPedidoService.getInstance().ignoraIndiceFinanceiroCondComercialTabPrecoOrOProdProm(itemPedido)) {
				CondicaoComercial condicaoComercial = LavenderePdaConfig.permiteCondComercialItemDiferentePedido ? itemPedido.getCondicaoComercial() : pedido.getCondicaoComercial();
				if (condicaoComercial != null) {
					double vlIndiceFinanceiro = condicaoComercial.vlIndiceFinanceiro;
					double vlIndiceFlex = condicaoComercial.vlIndiceVerba;
					//--
					if (vlIndiceFinanceiro > 0 && (condicaoComercial.isIndiceVerbaSequencial() || vlIndiceFlex == 0)) {
						vlBaseCalculo = roundUnidadeAlternativa(vlBaseCalculo * vlIndiceFinanceiro);
					}
					if (vlIndiceFlex > 0) {
						vlBaseCalculo = vlBaseCalculo * vlIndiceFlex;
					}
					vlBaseCalculo = roundUnidadeAlternativa(vlBaseCalculo);
				}
			}
		}
		//--aplica indice financeiro do cliente
		if (!(LavenderePdaConfig.isAplicaMaiorDescontoNoItemPedido() && !"3".equals(LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido)	&& LavenderePdaConfig.usaAplicacaoMaiorDescontoEmCascata)
				&& !LavenderePdaConfig.isAplicaDescontoCategoria()) {
			if(naoAplicaIndiceCliente) {
				vlBaseCalculo = roundUnidadeAlternativa(vlBaseCalculo);
			} else {
				double vlIndiceFinanceiro = ItemPedidoService.getInstance().getIndiceFinanceiroCliente(pedido);
				if (vlIndiceFinanceiro != 0) {
					vlBaseCalculo = roundUnidadeAlternativa(vlBaseCalculo * vlIndiceFinanceiro);
				}
			}
		}

		//--aplica indice PlataformaVendaCliFin (igual itemPedidoService.applyIndiceFinanceiroPlataformaVendaCliFin)
		if (LavenderePdaConfig.usaPoliticaComercial() && itemPedido.politicaComercial != null && itemPedido.politicaComercial.acumulaIndices()) {
			PlataformaVendaCliFin plataformaVendaCliFin = (PlataformaVendaCliFin) PlataformaVendaCliFinService.getInstance().findByPrimaryKey(new PlataformaVendaCliFin(pedido, itemPedido.getProduto()));
			if (plataformaVendaCliFin != null) {
				double vlIndiceFinanceiro = plataformaVendaCliFin.vlIndiceFinanceiro;
				if (vlIndiceFinanceiro != 0) {
					vlBaseCalculo = roundUnidadeAlternativa(vlBaseCalculo * vlIndiceFinanceiro);
				}
			}
		}


		if ((LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples && !LavenderePdaConfig.aplicaReducaoSimplesAposCalculoValorItem) && pedido.getCliente().isOptanteSimples()) {
			vlBaseCalculo -= vlReducaoOptanteSimples;
		}
		if (LavenderePdaConfig.usaCondicaoPagamentoPorCondicaoComercial || LavenderePdaConfig.usaCondComercialPorCondPagto) {
			if (!ItemPedidoService.getInstance().ignoraIndiceFinanceiroCondComercialTabPrecoOrOProdProm(itemPedido)) {
				double vlIndiceFinanceiro = ItemPedidoService.getInstance().getIndiceFinanceiroCondPagamentoPorCondComercial(pedido, itemPedido.cdCondicaoComercial);
				if (vlIndiceFinanceiro == 0) {
					vlIndiceFinanceiro = 1;
				}
				vlBaseCalculo = roundUnidadeAlternativa(vlBaseCalculo * vlIndiceFinanceiro);
			}
		}
		if (!(ItemPedidoService.getInstance().isAnulaIndiceFinanceiroCondPagtoTabPrecoPromOrProdProm(itemPedido))) {
			boolean aplicouIndiceEspecialVlBase = false;
			if (LavenderePdaConfig.usaIndiceEspecialCondPagtoProd && LavenderePdaConfig.indiceFinanceiroCondPagtoVlBase) {
				double vlIndiceFinanceiro = CondPagtoProdService.getInstance().findVlIndiceFinanceiroCondPagtoPorProduto(pedido, itemPedido);
				if (vlIndiceFinanceiro != 0) {
					vlBaseCalculo = roundUnidadeAlternativa(vlBaseCalculo * vlIndiceFinanceiro);
					aplicouIndiceEspecialVlBase = true;
				} 
			}
			if (LavenderePdaConfig.isIndiceFinanceiroCondPagtoVlItemPedido() && LavenderePdaConfig.indiceFinanceiroCondPagtoVlBase && !aplicouIndiceEspecialVlBase && !LavenderePdaConfig.isAplicaDescontoCategoria()) {
				double vlIndiceFinanceiro = ItemPedidoService.getInstance().getIndiceFinanceiroCondPagtoVlItemPedido(pedido, itemPedido);
				if (PedidoService.getInstance().isPedidoAtingiuCota(pedido, true) && pedido.onFechamentoPedido) {
					vlIndiceFinanceiro = 1;
				}
				if (vlIndiceFinanceiro == 0) {
					vlIndiceFinanceiro = 1;
				}
				vlBaseCalculo = roundUnidadeAlternativa(vlBaseCalculo * vlIndiceFinanceiro);
			}
		}
		//--
		if (LavenderePdaConfig.isPermiteAlterarVlItemNaUnidadeElementar()) {
			//Quando edita o valor da unidade elementar deve calcular no final após aplicar todos os índices
			double nuConversaoUnidadesMedida = itemPedido.getProduto().nuConversaoUnidadesMedida;
			if (nuConversaoUnidadesMedida == 0) {
				nuConversaoUnidadesMedida = 1;
			}
			double vlFlexEmbalagemElementar;
			if (LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco) {
				double nuConversaoUnidadeItemTabelaPreco = itemPedido.getItemTabelaPreco().getNuConversaoUnidade();
				if (nuConversaoUnidadeItemTabelaPreco == 0) {
					nuConversaoUnidadeItemTabelaPreco = 1;
				}
				vlFlexEmbalagemElementar = roundUnidadeAlternativa(vlBaseCalculo / nuConversaoUnidadeItemTabelaPreco);
			} else {
				vlFlexEmbalagemElementar = ItemPedidoService.getInstance().calculaVlEmbalagemElementarPorProdutoUnidade(itemPedido.getProdutoUnidade(), nuConversaoUnidadesMedida, vlBaseCalculo, itemPedido);
			}
			vlBaseCalculo = roundUnidadeAlternativa(ItemPedidoService.getInstance().calculaVlItemByVlElementar(itemPedido, vlFlexEmbalagemElementar));
		}
		if ((LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples && LavenderePdaConfig.aplicaReducaoSimplesAposCalculoValorItem) && pedido.getCliente().isOptanteSimples()) {
			vlBaseCalculo -= vlReducaoOptanteSimples;
		}
		if (!LavenderePdaConfig.isMostraFlexPositivoPedido() && LavenderePdaConfig.getVlPctMargemDescontoItemPedido() > 0) {
			if (!DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocional(itemPedido) && !itemPedido.getProduto().isEspecial()) {
				double vlPctMargemDesconto = LavenderePdaConfig.getVlPctMargemDescontoItemPedido();
				ProdutoMargem produtoMargem = ProdutoMargemService.getInstance().getProdutoMargem(itemPedido.getProduto(), pedido.getCliente().cdRamoAtividade);
				if (produtoMargem != null) {
					vlPctMargemDesconto = produtoMargem.vlPctMargemDesconto;
				}
				vlBaseCalculo = ValueUtil.round(vlBaseCalculo / (1 - (vlPctMargemDesconto / 100)));
			}
		}
		
		//Manter o bloco abaixo por último
		if (LavenderePdaConfig.aplicaIndicesNaUnidadePadraoParaUnidadeAlternativa) {
			ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
			if (produtoUnidade != null) {
				vlBaseCalculo = ItemPedidoService.getInstance().aplicaMultiplicacaoDivisao(itemPedido,  produtoUnidade, vlBaseCalculo);
			} else {
				vlBaseCalculo = ValueUtil.round(vlBaseCalculo);
			}
		}
		//--
		if (vlBaseCalculo < 0) {
			vlBaseCalculo = 0;
		}
		return vlBaseCalculo;
	}
	

	
	public double getVlPctMaxDescontoItemTabelaPreco(Produto produto) {
		if (produto != null && LavenderePdaConfig.isUsaMargemProdutoAplicadaNoValorBaseVerba()) {
			return vlPctMaxDesconto + produto.vlPctMargemProduto;
		}
		return vlPctMaxDesconto;
	}
	
	
	public CondComercialExcec getCondComercialExcec(String cdCondicaoComercial, String cdItemGrade1) throws SQLException {
		if (LavenderePdaConfig.usaCondicaoComercialPedido && LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial) {
			if (condComercialExcec == null || (condComercialExcec.cdProduto == null || (cdProduto != null && !cdProduto.equals(condComercialExcec.cdProduto)))) {
				condComercialExcec = CondComercialExcecService.getInstance().findCondComercialExcec(cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, cdProduto, cdCondicaoComercial, cdItemGrade1);
			}
		}
		return condComercialExcec;
	}

	public Produto getProduto() throws SQLException {
		if (!ValueUtil.isEmpty(cdProduto) && ((produto == null) || (!cdProduto.equals(produto.cdProduto)))) {
			produto = ProdutoService.getInstance().getProduto(cdProduto);
		}
		return produto;
	}

	public boolean isChaveVazia() {
		return cdEmpresa == null || cdRepresentante == null || cdProduto == null || cdTabelaPreco == null
				|| cdUf == null || cdItemGrade1 == null || cdItemGrade2 == null || cdItemGrade3 == null ||
				cdUnidade == null;
	}

	public boolean isFlBloqueiaDescPromo() {
		return ValueUtil.VALOR_SIM.equals(flBloqueiaDescPromo);
	}
	
	public boolean isFlBloqueiaDescPadrao() {
		return ValueUtil.VALOR_SIM.equals(flBloqueiaDescPadrao);
	}
	
	public boolean isFlBloqueiaDescCondicao() {
		return ValueUtil.VALOR_SIM.equals(flBloqueiaDescCondicao);
	}
	
	public boolean isFlBloqueiaDesc2() {
		return ValueUtil.VALOR_SIM.equals(flBloqueiaDesc2);
	}
	
	public boolean isFlBloqueiaDesc3() {
		return ValueUtil.VALOR_SIM.equals(flBloqueiaDesc3);
	}
}
