package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class ProdutoBase extends LavendereBasePersonDomain {

	public static final String SORT_COLUMN_RELEVANCIA = "nuRelevanciaSort";
	public static final String SORT_COLUMN_ESTOQUE = "estoqueSort";
	public static final String SORT_COLUMN_VLPRECOPADRAO = "VLPRECOPADRAO";
	public static final String SORT_COLUMN_PRECO = "precoSort";
	public static final String SORT_COLUMN_DSPRODUTO = "dsProduto";
	public static final String SORT_COLUMN_CDPRODUTO = "CDPRODUTO";
	public static final String SORT_COLUMN_VLMEDIOHIST = "vlMedioHistorico";
	public static final String SORT_COLUMN_QTDMEDHIST = "qtdMediaHistorico";
	public static final String SORT_COLUMN_VLPCTCOMISSAO = "VLPCTCOMISSAO";
	public static final String SORT_COLUMN_QTESTOQUE = "QTESTOQUE";
	public static final String NMCOLUNA_CDPRODUTO = "CDPRODUTO";

	public static final String SORTCOLUMN_NURANKING = "NURANKING";
	public static final String NMCOLUMN_NURANKINGEMP = "NURANKINGEMP";
	public static final String NMCOLUMN_NURANKINGREP = "NURANKINGREP";
	public static final String NMCOLUMN_NURANKINGSUP = "NURANKINGSUP";
	public static final String NMCOLUMN_NURANKINGREG = "NURANKINGREG";
	public static final String FLFORMULACALCULO_JUROSSIMPLES = "1";
	public static final String FLFORMULACALCULO_JUROSCOMPOSTO = "2";
	public static final String CDCAMPO_QT_EMBALAGEM_ELEMENTAR = "21";

	public static final int FL_SITUACAO_PRODUTO_LIBERADO = 1;
	public static final int FL_SITUACAO_PRODUTO_BLOQUEADO = 2;
	public static final int FL_SITUACAO_PRODUTO_INATIVO = 3;
	public static final int FL_SITUACAO_PRODUTO_RESTRITO = 4;

    public String cdEmpresa;
    public String cdRepresentante;
	public String cdProduto;
    public String cdFornecedor;
    public String dsProduto;
    public String dsPrincipioAtivo;
    public String cdGrupoProduto1;
    public String cdGrupoProduto2;
    public String cdGrupoProduto3;
    public String flBonificacao;
    public String flPreAltaCusto;
    public String flKit;
    public String dsMarca;
    public String nuCodigoBarras;
    public String dsReferencia;
    public String cdOrigemSetor;
    public String cdGrupoDestaque;
    public String cdGrupoDescProd;
    public double vlPrecoPadrao;
    public String cdUnidade;
    public String cdFatorComum;
    public int flSituacao;
    public String flLoteProdutoCritico;
    public String cdTributacaoProduto;
    public int nuFracao;
    public String cdUnidadeFracao;
    public String flUsaUnidadeBaseDsFracao;
    public String flProdutoRestrito;
    public String cdUnidadePreferencial;
    public String flCritico;
    public String cdAgrupProdSimilar;
	public String cdFamiliaDescProg;
	public String cdFamiliaPadrao;
	public String cdGrupoProduto5;
	public String cdAgrupadorSimilaridade;
	public String flIgnoraValidacao;
	private String dsAgrupadorGrade;
	public String dsAgrupadorGradeComp;
	public String cdLinha;
	public String cdClasse;
	public String cdConjunto;
	public String cdMarca;
	public String cdColecao;
	public String cdStatusColecao;
	public double nuMultiploEspecial;
	public String flFormulaCalculoDeflator;
	public double vlPctJurosMensal;
	public int qtDiasCarenciaJuros;
	public double vlLitro;
	public String flVendido;
	public int qtVendasPeriodo;

	//-- Não Persistente
    public boolean filterToItemPedido;
    public String cdTipoItemGrade1Temp;
    public String cdItemGrade1Temp;
    public String dsReferenciaLikeFilter;
    public String cdProdutoLikeFilter;
    public static String sortAttr;
    public int nuRelevanciaSort;
    public double vlPrecoSort;
    public double qtEstoqueProduto;
    public boolean forceCdFatorComumFilter;
    public double vlProduto;
    public double vlProdutoIpi;
    public boolean possuiGrade;
    public boolean descPromocionalCarregado;
    public boolean possuiDescPromocional;
    public double vlBrutoItem;
    public List<String> cdsSugVendaPerson;
    public HashMap<String, String> codsSugVendaPerson;
    public double qtdMediaHistorico;
    public double vlMedioHistorico;
	public String cdAtributoProduto;
	public String cdAtributoOpcaoProduto;
	public String cdGrupoProduto4;
	public Estoque estoque;
	public ItemTabelaPreco itemTabelaPreco;
	public TributacaoConfig tributacaoConfig;
	public double vlIndiceVolume;
	public String dsTabPrecoList;
	public String cdStatusEstoque;
	public String flVendaEncerrada;
	public String cdGrupoPermProd;
	public String cdCliente;
	public String cdCesta;
	public String dsPositivados;
	public String cdKit;
	public String cdTabelaPreco;
	public String cdTributacaoClienteFilter;
	public String cdTributacaoClienteFilter2;
	public String cdTipoPedidoFilter;
	public String cdClienteFilter;
	public String cdUFFilter;
	public boolean fromListItemPedido;
	public boolean fromSugestaoPedido;
	public Double sumEstoqueGrades;
	public boolean isAlgumaGradeSemEstoque; 
	public Double qtItemFisicoHistorico1;
	public Double qtItemFisicoHistorico2;
	public Double qtEstoqueClienteHistorico1;
	public Double qtEstoqueClienteHistorico2;
	public FotoProduto fotoProduto;
	public boolean carregaFotoProduto;
	public double qtEmbalagemElementar;
	public double vlEmbalagemElementar;
	public boolean carregaValoresElementaresItem;
	public Vector produtoUnidadeList;
	public String dsPctComissao;
	public double nuConversaoUnidadesMedida;
	public double vlSt;
	public boolean joinDescPromocional;
	public boolean joinCondComercialExcec;
	public String cdGrupoDescCliFilter;
	public String cdCondicaoComercialFilter;
	public String cdLocalFilter;
	public String dsAplicacaoProduto;
    public String dsCodigoAlternativo;
    public boolean filtraDescMaxProdCli;
    public boolean filtraFornecedorRep;
	public Image imageProduto;
    public double vlPctComissao;
    public double vlBaseFlex;
    public Pacote pacoteFilter;
    public String cdCentroCustoProdFilter;
    public String cdPlataformaVendaFilter;
    public String cdClienteVlMinDescPromoFilter;
    public String cdTabelaPrecoVlMinDescPromoFilter;
    public ProdutoIndustria produtoIndustriaFilter;
    public ProdutoIndustria produtoIndustriaSugestao;
    public boolean findListCombo;
    public String cdLocalEstoqueFilter;
	public boolean fromListProduto;
	public DescProgressivoConfig descProgressivoConfigFilter;
	public DescProgConfigFam descProgConfigFamFilter;
	public boolean filtraProdutoDescQtd;
	public Vector marcadores;
    public Vector cdMarcadores;
	public String flExcecaoProduto;
	public boolean filtraProdutosInseridos;
	public String nuPedidoFilter;
	public String flOrigemPedidoFilter;
	public boolean itensAutorizados;
	public ItemKitAgrSimilar itemKitAgrSimilarFilter;
	public ItemComboAgrSimilar itemComboAgrSimilarFilter;
	public boolean restritoClienteProduto;
	public boolean filtrandoAgrupadorGrade;
	public String cdItemGrade1;
	public String cdItemGrade2;
	public String cdItemGrade3;
	public ProdutoDestaque produtoDestaque;
	public Unidade unidade;
    public boolean hideProductPicture;
    public boolean agrupadorGradeLoaded;

	public String flFiltraProdutoPromocional;
	
	public CestaProduto cestaProdutoFilter;
	public CestaPositProduto cestaPositProdutoFilter;
	public GrupoProdTipoPed grupoProdTipoPedFilter;
	public ProdutoUnidade produtoUnidadeFilter;
	public ProdutoCliente produtoClienteFilter;
	public ClienteProduto clienteProdutoFilter;
	public ProdutoCondPagto produtoCondPagtoFilter;
	public ProdutoMenuCategoria produtoMenuCategoriaFilter;
	public GiroProduto giroProdutoFilter;
	public ItemPedido itemPedidoFilter;
	public int nuPromocaoFilter;
	public boolean filterByNuPromocao;
	public boolean excetoDescPromocional;
	public boolean contaProdutosListados;
	public int qtProdutosListados;
	
    public ProdutoBase(String dsTabela) {
		super(dsTabela);
	}

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
        return strBuffer.toString();
    }

    //@Override
    public int getSortIntValue() {
    	if (SORT_COLUMN_RELEVANCIA.equals(sortAttr)) {
    		return ValueUtil.getIntegerValue(nuRelevanciaSort);
    	}
    	if (SORT_COLUMN_ESTOQUE.equals(sortAttr)) {
    		return ValueUtil.getIntegerValue(qtEstoqueProduto);
    	}
    	if (SORT_COLUMN_QTDMEDHIST.equals(sortAttr)) {
    		return ValueUtil.getIntegerValue(qtdMediaHistorico);
    	}
    	return ValueUtil.getIntegerValue(cdProduto);
    }
    
    @Override
    public double getSortDoubleValue() {
    	if (SORT_COLUMN_PRECO.equals(sortAttr)) {
    		return vlPrecoSort;
    	}
    	if (SORT_COLUMN_VLPRECOPADRAO.equals(sortAttr)) {
    		return vlProduto;
    	}
    	if (SORT_COLUMN_VLMEDIOHIST.equals(sortAttr)) {
    		return vlMedioHistorico;
    	}
    	return 0d;
    }
    
	@Override
    public String getSortStringValue() {
    	if (SORT_COLUMN_DSPRODUTO.toUpperCase().equals(sortAttr)) {
			return dsProduto;
		} else if (SORT_COLUMN_CDPRODUTO.equals(sortAttr)) {
			return cdProduto;
		} else if (SORT_COLUMN_VLPCTCOMISSAO.equals(sortAttr)) {
			return dsPctComissao;
		}
		return super.getSortStringValue();
    }
    @Override
    public String toString() {
    	if (SORT_COLUMN_DSPRODUTO.equals(sortAttr)) {
			return dsProduto != null ? dsProduto.toUpperCase() : "";
		}
    	return StringUtil.getStringValue(super.toString());
    }

    @Override
    public boolean equals(Object obj) {
    	if (obj instanceof ProdutoBase) {
    		ProdutoBase produto = (ProdutoBase) obj;
    		return
    				ValueUtil.valueEquals(cdEmpresa, produto.cdEmpresa) &&
    				ValueUtil.valueEquals(cdRepresentante, produto.cdRepresentante) &&
    				ValueUtil.valueEquals(cdProduto, produto.cdProduto);
    	}
        return false;
    }

	public boolean isPermiteBonificacao() {
		return ValueUtil.VALOR_SIM.equals(flBonificacao);
	}

	public boolean isKit() {
		return ValueUtil.getBooleanValue(flKit);
	}
	
	public String getCdDomain() {
		return cdProduto;
	}

	public String getDsDomain() {
		return dsProduto;
	}

	public String getCdProdutoOrDsReferenciaForFotoProduto() {
    	if (LavenderePdaConfig.usaDsReferenciaNmFoto) {
    		return dsReferencia;
    	} else {
    		return cdProduto;
    	}
    }
	
	public String getDsSituacao() {
		if (flSituacao == FL_SITUACAO_PRODUTO_LIBERADO) {
	   		return Messages.PRODUTO_DS_SITUACAO_PRODUTO_LIBERADO;
	   	} else if (flSituacao == FL_SITUACAO_PRODUTO_BLOQUEADO) {
			return Messages.PRODUTO_DS_SITUACAO_PRODUTO_BLOQUEADO;
	   	} else if (flSituacao == FL_SITUACAO_PRODUTO_INATIVO) {
			return Messages.PRODUTO_DS_SITUACAO_PRODUTO_BLOQUEADO;
	   	}
	   	return "";
	 }
	
	public boolean isProdutoRestrito() {
	    return ValueUtil.valueEquals(FL_SITUACAO_PRODUTO_RESTRITO, flSituacao);
	}
	
	public HashMap<String, String> getCodsSugVendaPerson() {
		if (codsSugVendaPerson == null) {
			codsSugVendaPerson = new HashMap<>();
		}
		return codsSugVendaPerson;
	}
    
	public boolean isIgnoraValidacao() {
		return ValueUtil.getBooleanValue(flIgnoraValidacao);
	}
	
    public boolean isProdutoCritico() {
    	return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flCritico);
    }
    
    public boolean isProdutoAgrupadorGrade() {
    	return ValueUtil.isNotEmpty(getDsAgrupadorGrade());
    }

	public String getDsAgrupadorGrade() {
		try {
			if (LavenderePdaConfig.usaGradeProduto5() && dsAgrupadorGrade == null && !agrupadorGradeLoaded && ValueUtil.isNotEmpty(this.cdProduto)) {
				dsAgrupadorGrade = ProdutoService.getInstance().getDsAgrupadorGradeByRowKey(this);
			}
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
		agrupadorGradeLoaded = true;
		return dsAgrupadorGrade;
	}

	public void setDsAgrupadorGrade(String dsAgrupadorGrade) {
		this.dsAgrupadorGrade = dsAgrupadorGrade;
	}
	
	public boolean isFormaCalculoDeflatorJurosSimples() {
		return ValueUtil.isEmpty(this.flFormulaCalculoDeflator) || ValueUtil.valueEquals(this.flFormulaCalculoDeflator, FLFORMULACALCULO_JUROSSIMPLES);
	}
	
	public boolean isFormaCalculoDeflatorJurosComposto() {
		return ValueUtil.valueEquals(this.flFormulaCalculoDeflator, FLFORMULACALCULO_JUROSCOMPOSTO);
	}
	
	public boolean isFazJoinCTEDescPromocional() {
		if (LavenderePdaConfig.isConfigValorMinimoDescPromocional()) {
			return this.joinDescPromocional || this.filterToItemPedido || this.excetoDescPromocional;
		}
		return this.joinDescPromocional || LavenderePdaConfig.usaDescPromo || this.excetoDescPromocional;
	}

	public boolean isProdutoPromocao() {
		return itemTabelaPreco != null && ValueUtil.valueEqualsIfNotNull(itemTabelaPreco.flPromocao, ValueUtil.VALOR_SIM);
	}

	public boolean isVendido() {
    	return OrigemPedido.FLORIGEMPEDIDO_PDA.equals(flVendido) || ValueUtil.VALOR_SIM.equals(flVendido);
    }
}