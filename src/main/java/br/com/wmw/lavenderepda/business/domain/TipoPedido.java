package br.com.wmw.lavenderepda.business.domain;

import java.util.Arrays;
import java.util.List;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;

public class TipoPedido extends LavendereBaseDomain {

	public static final String TABLE_NAME = "TBLVPTIPOPEDIDO";
	
	public static final String TIPO_IMPRESSAO_PEDIDO = "1";
	public static final String TIPO_IMPRESSAO_NFE = "2";
	public static final String TIPO_IMPRESSAO_NFE_CONTINGENCIA = "3";
	public static final String TIPO_IMPRESSAO_NFCE = "4";
	public static final String TIPO_IMPRESSAO_BOLETO = "6";
	public static final String TIPO_IMPRESSAO_NOTA_PROMISSORIA = "7";
                 
	public static final String TIPO_PEDIDO_REMESSA_ESTOQUE = "1";
	public static final String TIPO_PEDIDO_EMPRESA_ESTOQUE = "2";
	             
	public static final String TIPO_PEDIDO_PRODUCAO = "1";
	public static final String TIPO_PEDIDO_VENDA_PRODUCAO = "2";
	public static final String TIPO_PEDIDO_PERMITE_CONSIGNACAO = "1";
	public static final String TIPO_PEDIDO_OBRIGA_CONSIGNACAO = "2";
	
	public static final String TIPO_CREDITO_FRETE = "F";
	public static final String TIPO_CREDITO_CONDICAO = "C";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTipoPedido;
    public String dsTipoPedido;
    public String flDefault;
    public String flBonificacao;
    public String flExigeSenha;
    public String flExcecaoCondPagto;
    public String flExcecaoGrupoProduto;
    public String flExcecaoProduto;
    public String flObrigaQtdProdutos;
    public double qtMinValorParcela;
    public String flVisitaPositivada;
    public String flConsisteConversaoUnidade;
    public String flIgnorarIndiceFinanCli;
    public String flIgnoraControleVerba;
    public String flIgnoraControleEstoque;
    public String flIgnoraLimiteCreditoCliente;
    public String flIgnoraClienteAtrasado;
    public double qtMinValor;
    public String flOportunidade;
    public String flSimulaControleVerba;
    public String flObrigaInfoComplementar;
    public String flIgnoraValorMinimoPedido;
    public String flGeraNfe;
    public String flIndicaKmTempo;
    public String flGeraBoleto;
    public String flRelacaoPedidoErp;
    public String flComplementar;
    public String flTipoCredito;
    public String flGeraAtendimento;
    public String flEnviaEmail;
    public String flInsereLote;
    public String flConsignaPedido;
    public double vlPctMaxAcrescimo;
    public double vlPctMaxDesconto;
    public String flIgnoraEstoqueReplicacao;
    public String flRevalidaEstoqueFechamento;
    public String flIndicaClienteEntrega;
    public String flIgnoraInsercMultItens;
    public String flTributacaoGeral;
    public String flGeraNfce;
	public String flBloqueiaReplicarPedido;
    public String dsNaturezaOperacao;
    private String flModoEstoque;
    public String flIgnoraDescontoItem;
    public String flIgnoraQuantidadeItem;
    public String flIndicaAguardaComplemento;
    public String flNaoRelacionaPedNaTrocaBonif;
    public String flExcecaoTabPreco;
    public String flIgnoraCalculoFrete;
    public String cdTipoPedidoFilter;
    public String flIgnoraMultEspecial;
    public String flIgnoraCalculoComissao; 
    public String flIgnoraPesoMinFrete;
    public String flUtilizaCondPgtoPadraoCli;
    public String cdMotivoPendenciaBonif;
	public String cdMotivoPendenciaTroca;
    public int nuOrdemLiberacaoBonif;
    public String flProducao;
    public String flIgnoraValorMinimoParcela;
    public int nuMinConversaoFob;
    public String flIgnoraEnvioErp;
    public String cdLocalEstoque;
	public String flBloqueiaMultRelacaoPedErp;
	public String flObrigaObservacao;
	public String flBonificacaoContaCorrente;
	public String flIgnoraPoliticaBonificacao;
	public String flIgnoraVlMinParcelaCondPagto;
	public String flFeira;
	
	// Nao persistente
	public String flBonificacaoContaCorrenteDif;
	public TipoPedidoCli tipoPedidoCliFilter;
    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof TipoPedido) {
            TipoPedido tipoPedido = (TipoPedido) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, tipoPedido.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, tipoPedido.cdRepresentante) &&
                ValueUtil.valueEquals(cdTipoPedido, tipoPedido.cdTipoPedido);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdTipoPedido);
        return strBuffer.toString();
    }

    public boolean isDefault() {
    	return ValueUtil.VALOR_SIM.equals(flDefault);
    }

    public boolean isFlExcecaoCondPagto() {
    	return ValueUtil.VALOR_SIM.equals(flExcecaoCondPagto);
    }

    public boolean isFlExcecaoGrupoProduto() {
    	return ValueUtil.VALOR_SIM.equals(flExcecaoGrupoProduto);
    }

    public boolean isBonificacao() {
    	return ValueUtil.VALOR_SIM.equals(flBonificacao);
    }

    public boolean isFlExcecaoProduto() {
    	return ValueUtil.VALOR_SIM.equals(flExcecaoProduto);
    }

    public boolean isPermitePositivacaoVisita() {
    	return ValueUtil.VALOR_SIM.equals(flVisitaPositivada);
    }

    public boolean isObrigaQtdProdutos() {
    	return ValueUtil.VALOR_SIM.equals(flObrigaQtdProdutos);
    }

    public boolean isConsisteConversaoUnidade() {
    	return !ValueUtil.VALOR_NAO.equals(flConsisteConversaoUnidade);
    }

    public boolean isSimulaControleVerba() {
    	return ValueUtil.VALOR_SIM.equals(flSimulaControleVerba);
    }

    public boolean isIgnoraControleVerba() {
    	return ValueUtil.VALOR_SIM.equals(flIgnoraControleVerba);
    }

    public boolean isIgnoraControleEstoque() {
    	return ValueUtil.VALOR_SIM.equals(flIgnoraControleEstoque);
    }

    public boolean isIgnoraLimiteCreditoCliente() {
    	return ValueUtil.VALOR_SIM.equals(flIgnoraLimiteCreditoCliente);
    }

    public boolean isIgnoraClienteAtrasado() {
    	return ValueUtil.VALOR_SIM.equals(flIgnoraClienteAtrasado);
    }

    public boolean isOportunidade() {
    	return ValueUtil.VALOR_SIM.equals(flOportunidade);
	}

    public boolean isObrigaTodasInfoComplementar() {
    	return ValueUtil.VALOR_SIM.equals(flObrigaInfoComplementar);
	}

	public String getCdDomain() {
		return cdTipoPedido;
	}

	public String getDsDomain() {
		return dsTipoPedido;
	}

	public boolean isExigeSenha() {
		return ValueUtil.VALOR_SIM.equals(flExigeSenha);
	}

	public boolean isIgnoraVlMinPedido() {
		return ValueUtil.VALOR_SIM.equals(flIgnoraValorMinimoPedido);
	}
	
	public boolean isGeraNfe() {
		return ValueUtil.VALOR_SIM.equals(flGeraNfe);
	}
	
	public boolean isGeraBoleto() {
		return ValueUtil.VALOR_SIM.equals(flGeraBoleto);
	}
	
	public boolean isFlRelacaoPedidoErp() {
		return ValueUtil.VALOR_SIM.equals(flRelacaoPedidoErp);
	}
	
	public boolean isComplementar() {
		return ValueUtil.VALOR_SIM.equals(flComplementar);
	}
	
	public boolean isFlTipoCreditoCondicao() {
		return TIPO_CREDITO_CONDICAO.equals(flTipoCredito);
	}

	public boolean isFlTipoCreditoFrete() {
		return TIPO_CREDITO_FRETE.equals(flTipoCredito);
	}
	
	public boolean isFlTipoCreditoAlcada() {
		return !isFlTipoCreditoCondicao() && !isFlTipoCreditoFrete();
	}
	
	public boolean isGeraAtendimento() {
		return !ValueUtil.VALOR_NAO.equals(flGeraAtendimento);
	}
	
	public boolean isEnviaEmail() {
		return !ValueUtil.VALOR_NAO.equals(flEnviaEmail);
	}

	public boolean isInsereLote() {
		return !ValueUtil.VALOR_NAO.equals(flInsereLote);
	}
	
	public boolean isPermiteConsignacao() {
		return ValueUtil.getBooleanValue(flConsignaPedido) || ValueUtil.valueEquals(TIPO_PEDIDO_PERMITE_CONSIGNACAO, flConsignaPedido) || isObrigaConsignacao();
	}
	
	public boolean isVlPctMaxDescontoValido() {
		return vlPctMaxDesconto >= 0 && vlPctMaxDesconto <= 100;
	}
	
	public boolean isVlPctMaxAcrescimoValido() {
		return vlPctMaxAcrescimo >= 0 && vlPctMaxAcrescimo <= 100;
	}
	
	public boolean isIgnoraEstoqueReplicacao() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flIgnoraEstoqueReplicacao);
	}
	
	public boolean isRevalidaEstoqueFechamento() {
		return !ValueUtil.valueEquals(ValueUtil.VALOR_NAO, flRevalidaEstoqueFechamento);
	}
	
	public boolean isIndicaClienteEntrega() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flIndicaClienteEntrega);
	}
	
	public boolean isIgnoraInsercaoMultiplosItens() {
		return ValueUtil.VALOR_SIM.equals(flIgnoraInsercMultItens);
	}
	
	public boolean isUsaTributacaoGeral() {
		return ValueUtil.getBooleanValue(this.flTributacaoGeral);
	}
	
	public boolean isFlIgnorarIndiceFinanCli() {
		return ValueUtil.getBooleanValue(this.flIgnorarIndiceFinanCli);
	}
	
	public boolean isFlIndicaKmTempo() {
		return ValueUtil.getBooleanValue(this.flIndicaKmTempo);
	}
	
	public boolean isGeraNfce() {
		return ValueUtil.VALOR_SIM.equals(flGeraNfce);
	}
	
	public boolean isUsaBloqueioReplicacaoPedido() {
		return ValueUtil.getBooleanValue(this.flBloqueiaReplicarPedido);
	}

	public String getFlModoEstoque() {
		return flModoEstoque == null ? TIPO_PEDIDO_REMESSA_ESTOQUE : flModoEstoque;
	}

	public void setFlModoEstoque(String flModoEstoque) {
		this.flModoEstoque = flModoEstoque;
	}
	
	public boolean isRemessaEstoque() {
		return ValueUtil.valueEquals(TIPO_PEDIDO_REMESSA_ESTOQUE, flModoEstoque);
	}
	
	public boolean isFlIndicaAguardaComplemento() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flIndicaAguardaComplemento);
	}
	
	public boolean isTrocaRecolher(String cdTipoPedido) {
		List<String> cdTipoTrocaRecolherList = Arrays.asList(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher.split(";"));
		return cdTipoTrocaRecolherList.contains(cdTipoPedido);
	}
	
	public boolean isVenda(String cdTipoPedido) {
		return !isBonificacao() && !isTrocaRecolher(cdTipoPedido) && !isOportunidade() && !isComplementar();
	}
	
	public boolean isIgnoraCalculoFrete() {
		return ValueUtil.getBooleanValue(flIgnoraCalculoFrete);
	}
	
	public boolean isIgnoraMultEspecial() {
		return ValueUtil.getBooleanValue(flIgnoraMultEspecial);
	}
	
	public boolean isIgnoraCalculoComissao() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flIgnoraCalculoComissao);
	}
	
	public boolean isIgnoraPesoMinFrete() {
		return ValueUtil.getBooleanValue(flIgnoraPesoMinFrete);
	}

	public boolean isUtilizaCondPgtoPadraoCli() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flUtilizaCondPgtoPadraoCli);
	}
	
	public boolean isMarcaPedidoPendentePorPedidoBonificacao() {
		return isBonificacao() && isPossuiMotivoPendenciaBonif();
	}

	public boolean isMarcaPedidoPendentePorPedidoTroca() {
		return isTrocaRecolher(cdTipoPedido) && isPossuiMotivoPendenciaTroca();
	}
	
	public boolean isPossuiMotivoPendenciaBonif() {
		return ValueUtil.isNotEmpty(cdMotivoPendenciaBonif);
	}

	public boolean isPossuiMotivoPendenciaTroca() {
		return ValueUtil.isNotEmpty(cdMotivoPendenciaTroca);
	}
	
	public boolean isProducao() {
		return ValueUtil.valueEquals(TIPO_PEDIDO_PRODUCAO, flProducao);
	}
	
	public boolean isVendaProducao() {
		return ValueUtil.valueEquals(TIPO_PEDIDO_VENDA_PRODUCAO, flProducao);
	}
	
	public boolean isIgnoraVlMinimoParcela() {
		return ValueUtil.VALOR_SIM.equals(flIgnoraValorMinimoParcela);
	}
	
	public boolean isIgnoraVlMinimoParcelaCondPagto() {
		return ValueUtil.VALOR_SIM.equals(flIgnoraVlMinParcelaCondPagto);
	}
	
	public boolean isObrigaConsignacao() {
		return ValueUtil.valueEquals(TIPO_PEDIDO_OBRIGA_CONSIGNACAO, flConsignaPedido );
	}
	
	public boolean ignoraEnvioErp() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flIgnoraEnvioErp);
	}
	
	public boolean isExcecaoCondPagto() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flExcecaoCondPagto);
	}
	
	public boolean isTipoPedidoBonificacaoContaCorrente() {
		return ValueUtil.getBooleanValue(flBonificacaoContaCorrente);
	}

	public boolean isIgnoraPoliticaBonificacao() {
		return ValueUtil.getBooleanValue(flIgnoraPoliticaBonificacao);
	}
	
	public boolean isFeira() {
		return ValueUtil.getBooleanValue(flFeira);
	}

}
