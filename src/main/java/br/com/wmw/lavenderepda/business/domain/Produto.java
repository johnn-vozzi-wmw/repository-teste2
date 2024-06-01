package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.business.builder.ProdutoBuilder;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.service.ColecaoService;
import br.com.wmw.lavenderepda.business.service.ColecaoStatusService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.UnidadeService;
import totalcross.sys.Settings;
import totalcross.util.Date;
import totalcross.util.Vector;

public class Produto extends ProdutoBase {

    public static String TABLE_NAME = "TBLVPPRODUTO";
    public static final String SIMPLE_NAME = "Produto";
    
	public static final String APPOBJ_CAMPOS_FILTRO_PRODUTO = "Produto";

	public static final String TIPO_VALIDACAO_MULTIPLO_EMBALAGEM_BLOQUEAR = "Bloquear";
	public static final String TIPO_VALIDACAO_MULTIPLO_EMBALAGEM_AVISAR = "Avisar";
	public static final int GRUPOPRODUTO_NIVEL1 = 1;
	public static final int GRUPOPRODUTO_NIVEL2 = 2;
	public static final int GRUPOPRODUTO_NIVEL3 = 3;
	public static final int GRUPOPRODUTO_NIVEL4 = 4;

    public String dsUnidadeFisica;
    public String dsUnidadeFaturamento;
    public double nuConversaoUMCreditoAntecipado;
    public String flConsisteConversaoUnidade;
    public double qtPeso;
    public String flProdutoControlado;
    public double vlPrecoFabrica;
    public double vlPrecoMaximoConsumidor;
    public double vlPctDepreciacaoTroca;
    public String dsEmbalagem;
    public String dsFichaTecnica;
    public String flIndiceFinanceiroEspecial;
    public double nuMultiploEmbalagem;
    public double qtEmbalagemCompra;
    public double vlPrecoCusto;
    public double nuMultiploIdeal;
    public double qtMinimaVenda;
    public String flNeutro;
    public String cdClassificFiscal;
    public String cdGrupoBonificacao;
    public String cdProdutoBonificacao;
    public String flAutoBonifica;
    public double vlPctMaxBonificacao;
    public double vlPctFrete;
    public String flAplicaFreteApenasValorMinimo;
    public String cdRestricaoVendaProd;
    public String flUtilizaVerba;
    public double vlPctIpi;
    public double vlIpi;
    public double vlMinIpi;
    public double vlPctPis;
    public double vlPctCofins;
    public double vlMinPis;
    public double vlMinCofins;	
    public double vlPctMinRentabilidade;
    public double vlPctIcms;	
    public String flDebitaPisCofinsZonaFranca;
    public int nuRelevancia;
    public String flLoteObrigatorio;
    public String flPermiteEstoqueNegativo;
    public double vlVolume;
    public String flEspecial;
    public double vlPctMargemProduto;
    public String dsReferenciaProduto;
    public double vlPctCustoVariavel;
    public String flCigarro;
    public String dsNcmProduto;
    public String nuCfopProduto;
    public String flIgnoraValorMinPedido;
    public String dsCestProduto;
    public int nuNivelGradeObrigatorio;
    public String cdGrupoDescMaxProd;
    public double vlLarguraMin;
    public double vlLarguraMax;
    public double vlComprimentoMin;
    public double vlComprimentoMax;
    public int nuFracaoFornecedor;
    public double vlPctVariacaoComissao;
    public double vlPctMaxDescExtra;
    public String cdOrigemMercadoria;
    public String flCalculaPrecoVolume;
    public double vlAlturaMin;
    public double vlAlturaMax;
    public double vlGramatura;
    public double nuConversaoFob;
    public double vlMinAutorizacaoPreco;
    public String dsDimensoes;
    public String flIgnoraPeso;
    public String dsMoedaProduto;
    
	// Não persistentes
    public double qtItemPedido;
    public double qtFaltanteProdutoRelacionado;
    public double vlPctVendaMin;
    public String cdProdutoPrincipal;
    public Produto produtoRelacionado;
    public String cdSugestaoVenda;
    public String cdSugVendaPerson;
    public Combo combo;
    public double qtSolicitCargaProd;
    public Date dtInicialFilter;
    public Date dtFinalFilter;
    public boolean complementar;
    public String flTipoItemCombo;
    public String flAgrupadorSimilaridade;
    public Vector similaresList;
    public double nuMultiploEspecialUnidadeAlternativa;
    public Colecao colecao;
    public ColecaoStatus statusColecao;
    public String flTipoRelacao;
	
    public Produto() {
    	super(TABLE_NAME);
	}
    
    public Produto(ProdutoBuilder produtoBuilder) {
    	super(TABLE_NAME);
    	cdEmpresa = produtoBuilder.cdEmpresa;
		cdRepresentante = produtoBuilder.cdRepresentante;
		cdProduto = produtoBuilder.cdProduto;
		cdFornecedor = produtoBuilder.cdFornecedor;
    }
    
    public Produto(String cdEmpresa, String cdRepresentante) {
    	super(TABLE_NAME);
    	this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
    }
    
    public Produto(String cdEmpresa, String cdRepresentante, String cdProduto) {
    	this(cdEmpresa, cdRepresentante);
    	this.cdProduto = cdProduto;
    }

	@Override
    public String getDescription() {
    	return dsProduto;
    }

	public static String getPathImg() {
		return FotoUtil.getPathImg(Produto.class);
	}
	
	public boolean isNeutro() {
		return ValueUtil.VALOR_SIM.equals(flNeutro);
	}

	public boolean isIgnoraValorMinPedido() {
		return ValueUtil.VALOR_SIM.equals(flIgnoraValorMinPedido);
	}

	public String getCdGrupoProduto1Filter() {
		return getCdGrupoProdutoFilter(cdGrupoProduto1);
	}

	public String getCdGrupoProduto2Filter() {
		return getCdGrupoProdutoFilter(cdGrupoProduto2);
	}

	public String getCdGrupoProduto3Filter() {
		return getCdGrupoProdutoFilter(cdGrupoProduto3);
	}

	private String getCdGrupoProdutoFilter(String cdGrupoProduto) {
        return ValueUtil.isNotEmpty(cdGrupoProduto) ? cdGrupoProduto : ValueUtil.VALOR_ZERO;
    }

	public boolean isUtilizaVerba() {
		return ValueUtil.VALOR_SIM.equals(flUtilizaVerba);
	}
	
	public boolean isDebitaPisCofinsZonaFranca() {
		return ValueUtil.VALOR_SIM.equals(flDebitaPisCofinsZonaFranca);
	}

	public Produto getProdutoRelacionado() throws SQLException {
		if (!ValueUtil.isEmpty(cdProdutoPrincipal) && ((produtoRelacionado == null) || (!cdProdutoPrincipal.equals(produtoRelacionado.cdProduto)))) {
			produtoRelacionado = ProdutoService.getInstance().getProduto(cdProdutoPrincipal);
		}
		return produtoRelacionado;
	}
	
    public int getSortValueByNuRelevancia() {
    	return ValueUtil.getIntegerValue(nuRelevancia);
    }
    
    public boolean isPermiteEstoqueNegativo() {
    	return ValueUtil.VALOR_SIM.equalsIgnoreCase(flPermiteEstoqueNegativo);
    }
    
    public double getVlVolumeArrendondado() {
    	return ValueUtil.round(vlVolume, LavenderePdaConfig.nuCasasDecimaisVlVolume);
    }
    
    public boolean isEspecial() {
    	return ValueUtil.getBooleanValue(flEspecial);
    }
    
    public static String getDsFilePathPdfProduto() {
		String filePath = BaseDomain.getDirBaseArquivosInCard() + "PdfProduto/";
		if (VmUtil.isAndroid()) {
			return "/sdcard/" + filePath;
		} else {
			return Settings.appPath + "/" + filePath;
		}
	}
    
    public boolean isFlProdutoRestrito() {
    	return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flProdutoRestrito);
    }
	
	public boolean isApresentaInfoComplCalculoPrecoPorVolumePorProduto() throws SQLException {
		if (LavenderePdaConfig.calculaPrecoPorVolumeProduto && LavenderePdaConfig.usaInfoComplementarItemPedido()) {
			return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flCalculaPrecoVolume);
		}
		return false;
	}
	
	public Unidade getUnidade () throws SQLException {
		if(unidade == null && cdUnidade != null) {
			Unidade filter = new Unidade();
			filter.cdEmpresa = cdEmpresa;
			filter.cdRepresentante = cdRepresentante;
			filter.cdUnidade = cdUnidade;
			unidade = (Unidade) UnidadeService.getInstance().findByPrimaryKey(filter);
		}
		return unidade;
	}
	
	public Colecao getColecao() throws SQLException {
		if (cdColecao != null && colecao == null) {
			colecao = new Colecao(this);
			colecao = (Colecao) ColecaoService.getInstance().findByPrimaryKey(colecao);
			return colecao == null ? colecao = new Colecao() : colecao;
		}
		return colecao;
	}
	
	public ColecaoStatus getStatusColecao() throws SQLException {
		if (cdStatusColecao != null && statusColecao == null) {
			statusColecao = new ColecaoStatus(this);
			statusColecao =  (ColecaoStatus) ColecaoStatusService.getInstance().findByPrimaryKey(statusColecao);
			return statusColecao == null ? statusColecao = new ColecaoStatus() : statusColecao;
		}
		return statusColecao;
	}
	
	public String getKeyGrade() throws SQLException {
		return ProdutoGrade.getGradeKey(cdItemGrade1, cdItemGrade2, cdItemGrade3);
	}
    
}