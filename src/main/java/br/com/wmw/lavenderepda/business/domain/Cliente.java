package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.service.CategoriaService;
import br.com.wmw.lavenderepda.business.service.FichaFinanceiraService;
import br.com.wmw.lavenderepda.business.service.RedeService;
import br.com.wmw.lavenderepda.business.service.TipoPagamentoService;
import totalcross.util.Date;

public class Cliente extends LavendereBasePersonDomain {

	public static final String TABLE_NAME = "TBLVPCLIENTE";
	public static final String TABLE_NAME_WEB = "TBLVWCLIENTE";
	public static final String APPOBJ_CAMPOS_FILTRO_CLIENTE = "Cliente";
	public static final String NMCOLUNA_NUDIASSEMPESQUISA = "nuDiasSemPesquisa";
	public static final String NMCOLUNA_NUDIASSEMPESQUISAGONDOLA = "nuDiasSemPesquisaGondola";
	public static final String NMCOLUNA_FLSTATUSCLIENTE = "flStatusCliente";
	public static final String NMCOLUNA_FLTIPOCADASTRO = "flTipoCadastro";
	public static final String NMCOLUNA_CDCLIENTE = "cdCliente";
	public static final String NMCOLUNA_FLATUALIZACADASTROWMW = "flAtualizaCadastroWmw";
	public static final String NUDIVULGAINFO = "nuDivulgaInfo";
	public static final String NMCOLUNA_ROWKEY = "rowkey";
	public static final String NMCOLUNA_CDESTADOCOMERCIAL = "cdestadocomercial";
	public static final String NMCOLUNA_DSESTADOCOMERCIAL = "dsestadocomercial";
	public static final String NMCOLUNA_NULOGRADOUROCOMERCIAL = "nulogradourocomercial";
	public static final String FLSTATUSCLIENTE_BLOQUEADO = "1";
	public static final String FLSTATUSCLIENTE_ATRASADO = "3";
	public static final String FLSTATUSCLIENTE_BLOQUEADO_POR_ATRASO = "7";
	public static final String CLIENTE_FLATENDIDO_PDA = "P";
	public static final String CD_CLIENTE_DEFAULT_PARA_CADASTRO_COORDENADA = "!@#&%";
	public static final String CD_CLIENTE_DEFAULT_PARA_NOVA_PESQUISA_MERCADO = "&%$#@!";
	public static final String CD_NOVO_CLIENTE_DEFAULT_PARA_NOVO_PEDIDO = "!@#$%&";
	public static final String CD_CLIENTE_DEFAULT_PARA_NOVO_PEDIDO = "!@#$%2";
	public static final String NM_CLIENTE_DEFAULT = "Sem informações do cliente";
	public static final String NU_INSCRICAO_ESTADUAL_ISENTO = "ISENTO";
	public static final String TIPO_PROSPECTS = "P";
	public static final String TIPO_CLIENTE = "C";
	public static final String TIPO_TODOS = "T";
	public static final String TIPO_REDE = "R";
	public static final String TIPO_INDIVIDUAL = "I";
	public static final String INDICADOR_RECENTE = "1";
	public static final String INDICADOR_SEM_COMPRA_HIST = "2";
	public static final String INDICADOR_SEM_COMPRA_MES = "3";
	public static final String APRESENTA_LISTACLI = "1";
	public static final String APRESENTA_LISTAGENDA = "2";
	public static final String FLBOLETOACEITE_A = "A";
	public static final String FLPERMITEMULTIPLOSENDERECOS = "FLPERMITEMULTIPLOSENDERECOS";

	public static final int CLIENTE_ATRASADO_BLOQUEADO = 1;
	public static final int CLIENTE_ATRASADO_REDE_LIBERA_SENHA = 2;
	public static final int CLIENTE_ATRASADO_LIBERA_SENHA = 3;
	public static final int CLIENTE_ATRASADO_AVISA = 4;
	public static final int CLIENTE_ATRASADO_LIBERADO_PAGAMENTO = 5;
	public static final int CLIENTE_ATRASADO_REDE = 6;
	
	public static final String DESCONTO_BLOQUEADO_MANUAL = "1";
	public static final String DESCONTO_BLOQUEADO_PROMOCIONAL = "2";
	public static final String DESCONTO_BLOQUEADO_QNT_ITEM = "3";
	
	public static final String ACRESCIMO_BLOQUEADO_MANUAL = "1";
	public static final String NMCOLUNA_DTCRIACAO = "dtcriacao";
	public static final String NMCOLUNA_HRCRIACAO = "hrcriacao";
	public static final String NMCOLUNA_NUCEPCOMERCIAL = "nucepcomercial";
	public static final String NMCOLUNA_NUINSCRICAOESTADUAL = "nuinscricaoestadual";
	public static final String NMCOLUNA_DTFIMPESQUISA = "dtfimpesquisa";


	public String cdEmpresa;
	public String cdRepresentante;
	public String cdCliente;
	public String nmRazaoSocial;
	public String nuCnpj;
	public String nmFantasia;
	public String cdCondicaoPagamento;
	public String cdTipoPagamento;
	public String cdTipoPedido;
	public String cdTabelaPreco;
	public double vlLimiteCredito;
	public String nuFone;
	public String dsBairroComercial;
	public String dsCidadeComercial;
	public String cdEstadoComercial;
	public String dsEstadoComercial;
	public String dsUfPreco;
	public String cdRegiao;
	public boolean isTotalizadorConsulta;
	public String flStatusCliente;
	public String flStatusClienteReal;
	public Date dtVencimentoAlvara;
	public String flEspecial;
	public int qtDiasMaximoPagamento;
	public String cdRotaEntrega;
	public double vlIndiceFinanceiro;
	public String cdCategoria;
	public String cdContratoEspecial;
	public String cdTipoEntrega;
	public String flConsisteConversaoUnidade;
	public String cdTributacaoCliente;
	public String cdTributacaoCliente2;
	public String flAplicaSt;
	public double vlIndiceComissao;
	public double vlIndiceFinanceiroEspecial;
	public double vlIndiceFinanceiroCondPagto;
	public String flCreditoAntecipado;
	public String flOptanteSimples;
	public int nuDiasPrevisaoEntrega;
	public int nuDiasSemPedido;
	public Date dtAtualizacaoCadastro;
	public int qtDiasPrazoPagtoEstendido;
	public String cdTransportadora;
	public String cdRamoAtividade;
	public String cdRestricaoVendaCli;
	public String cdCidadeComercial;
	public String cdLocalEstoque;
	public String flAtendido;
	public Date dtVencimentoAfe;
	public double vlMaxPedido;
	public double vlMinPedido;
	public String cdGrupoCondicao;
	public String flDivideVlMin;
	public double cdLatitude;
	public double cdLongitude;
	public String dsCepComercial;
	public String nuLogradouroComercial;
	public String dsLogradouroComercial;
	public String cdGrupoDescCli;
	public String cdRede;
	public double vlPctMaxDesconto;
	public double vlPctIcms;
	public String nuInscricaoEstadual;
	public String flDebitaPisCofinsZonaFranca;
	public String cdCanalCliente;
	public double vlPctContratoCli;
	public String flTipoCadastro;
	public String flKeyAccount;
	public String cdStatusRentCli;
	public String dsSituacao;
	public String flRecebeEmail;
	public String flRecebeSMS;
	public String cdGrupoPermProd;
	public double vlPctMinDesconto;
	public String dsEmail;
	public String flOculto;
	public double vlTonFrete;
	public String flConsignaPedido;
	public double vlLimiteCreditoConsig;
	public double vlPctDevolucaoConsig;
	public double vlVendaMensal;
	public String cdTransportadoraCif;
	public String cdTransportadoraFob;
	public double vlTicketMedio;
	public Date dtCadastro;
	public Date dtAbertura;
	public Date dtFundacao;
	public double vlMaxTitulosAtraso;
	public int nuDiasToleranciaAtraso;
	public String flBoletoAceite;
	public String flContribuinte;
	public String flConsumidorFinal;
	public String cdVencimentoAdicBoleto;
	public Date dtAtualizacaoCadastroWmw;
	public String flAtualizaCadastroWmw;
	public double vlPctDescHistoricoVendas;
	public String flFreteEmbutido;
	public double vlIndiceImpostos;

	public double vlTotalPedidosValidateLimiteCredito;
	public double vlTotalPedidosValidateLimiteCreditoConsignado;
	public String flCreditoClienteLiberadoSenha;
	public String cdClienteDif;
	public String cdClienteExato;
	private String flStatusClienteFichaFin;
	public boolean flClienteAtrasadoLiberadoSenha;
	public String flClienteLiberadoPedidoAVista;
	public String cdEstadoComercialFilter;
	public String dsCidadeComercialFilter;
	public String dsBairroComercialFilter;
	public String cepInicialFilter;
	public String cepFinalFilter;
	public String flVisitaForaDaAgendaLiberada;
	public int nuSequenciaVisitaCliente;
	public boolean nuDiasSemPedidoInvalido;
	public String cdCondicaoComercial;
	public String cdCondicaoPagtoBloqueada;
	public String flPermiteProdutoRestrito;
	public double vlPctDescProdutoRestrito;
	public String dsObservacao;
	public int nuDivulgaInfo;
	public String cdSegmento;
	public String cdCanal;
	public String cdFaixaCli;
	public String flCodigoInternoCliente;

	//--
	public boolean isAbertura;
	public boolean isFundacao;
	private FichaFinanceira fichaFinanceira;
	private TipoPagamento tipoPagamento;
	public boolean useJoinFichaFinanceira;
	public String flStatusClienteFilter;
	public Categoria categoria;
	public boolean usaCodicaoEnderecoSeparadaPorAnd;
	public Rede rede;
	public String flTipoClienteRede;
	public String cdMarcador;
	public double vlPctVpc;
	public Date dtEmissao;
	public double vlUltimoPreco;
	public String flObrigaOrdemCompra;
	public String nmContatoCliente;
	public String cdContatoCliente;
	public String flFiltroStatusClienteTipoRequisicao;
	public String dsStatusClienteListTipoRequisicao;
	public String flStatusRetirada;
	public String dsDescontoBloqueadoList;
	public String dsAcrescimoBloqueadoList;
	public String nuLicencaProdutoControlado;
	public String flIgnoraVerbaGrpProd;
	public String cdClassificacao;
	
	//não persistentes
	public String cdMarcadores;
	public DescProgressivoConfig descProgressivoConfigFilter;
	public String cdFornecedor;
	public boolean includeExtraFilterJoin;
	public boolean somentePedidoPreOrcamento;
	public boolean convertendoOrcamentoEmPedido;
	
	public Cliente() {
		this(TABLE_NAME);
	}

	public Cliente(String tabelName) {
		super(tabelName);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Cliente) {
			Cliente cliente = (Cliente) obj;
			return
			ValueUtil.valueEquals(cdEmpresa, cliente.cdEmpresa) &&
			ValueUtil.valueEquals(cdRepresentante, cliente.cdRepresentante) &&
			ValueUtil.valueEquals(cdCliente, cliente.cdCliente);
		}
		return false;
	}

	public String getCdDomain() {
		return cdCliente;
	}

	public String getDsDomain() {
		return nmRazaoSocial;
	}

    @Override
    public int getSortIntValue() {
    	return ValueUtil.getIntegerValue(cdCliente);
    }
    
    public String getPrimaryKey() {
    	StringBuilder strBuffer = new StringBuilder();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdCliente);
        return strBuffer.toString();
    }

    @Override
    public String toString() {
    	StringBuilder description = new StringBuilder();
		if (!isNovoClienteDefaultParaNovoPedido() && !isClienteDefaultParaNovoPedido() && (ValueUtil.isNotEmpty(nmRazaoSocial) || isNovoCliente())) {
			description.append(StringUtil.getStringValue(nmRazaoSocial));
			switch (LavenderePdaConfig.getConfigDescricaoEntidadesCliente()) {
			case 1:
				if (ValueUtil.isNotEmpty(nuCnpj)) {
					description.append(" [").append(StringUtil.getStringValue(nuCnpj)).append("]");
				}
				break;
			case 2:
				if (ValueUtil.isNotEmpty(cdCliente) && ValueUtil.valueNotEqualsIfNotNull(CD_CLIENTE_DEFAULT_PARA_NOVA_PESQUISA_MERCADO, cdCliente)) {
					description.append(" [").append(cdCliente).append("]");
				}
				break;
			default:
				break;
			}
		}
		return description.toString();
    }

    public boolean isAtrasado() throws SQLException {
    	return FLSTATUSCLIENTE_ATRASADO.equals(getFlStatusCliente());
    }

	public String getFlStatusCliente() throws SQLException {
		if (LavenderePdaConfig.usarFlStatusClienteDaFichaFinanceira) {
			if (ValueUtil.isEmpty(flStatusClienteFichaFin)) {
				FichaFinanceira fichaFinanceiraFilter = new FichaFinanceira();
				fichaFinanceiraFilter.cdEmpresa = cdEmpresa;
				fichaFinanceiraFilter.cdRepresentante = cdRepresentante;
				fichaFinanceiraFilter.cdCliente = cdCliente;
				String result = FichaFinanceiraService.getInstance().findColumnByRowKey(fichaFinanceiraFilter.getRowKey(), "FLSTATUSCLIENTE");
				if (result != null) {
					flStatusClienteFichaFin = result;
				}
			}
			return flStatusClienteFichaFin;
		}
		return this.flStatusCliente;
	}

	public boolean isStatusAtrasado() throws SQLException {
		return StatusCliente.STATUSCLIENTE_CDATRASADO.equals(getFlStatusCliente());
	}

	public boolean isStatusBloqueado() throws SQLException {
		return StatusCliente.STATUSCLIENTE_CDBLOQUEADO.equals(getFlStatusCliente());
	}

	public boolean isStatusBloqueadoPorAtraso() throws SQLException {
		return StatusCliente.STATUSCLIENTE_CDBLOQUEADOPORATRASO.equals(getFlStatusCliente());
	}
	
	public void loadStatusCliente() throws SQLException {
		if (LavenderePdaConfig.usarFlStatusClienteDaFichaFinanceira) {
			flStatusCliente = getFichaFinanceira().flStatusCliente;
		}
	}

	public void setFlStatusCliente(String flStatusCliente) {
		this.flStatusCliente = flStatusCliente;
	}

	public void setFlStatusClienteFichaFin(String flStatusClienteFichaFin){
		this.flStatusClienteFichaFin = this.flStatusCliente;
	}

    public boolean isAlvaraVigente() {
    	return ValueUtil.isEmpty(dtVencimentoAlvara) || (DateUtil.getDaysBetween(DateUtil.getCurrentDate(), dtVencimentoAlvara) <= 0);
    }

    public boolean isAfeVigente() {
    	return ValueUtil.isEmpty(dtVencimentoAfe) || (DateUtil.getDaysBetween(DateUtil.getCurrentDate(), dtVencimentoAfe) <= 0);
    }

	public boolean isNovoCliente() {
		if (ValueUtil.isEmpty(cdCliente) || ValueUtil.isEmpty(nuCnpj)) {
			return Cliente.CD_NOVO_CLIENTE_DEFAULT_PARA_NOVO_PEDIDO.equals(cdCliente);
		}
		return (cdCliente.equals(nuCnpj) || Cliente.CD_NOVO_CLIENTE_DEFAULT_PARA_NOVO_PEDIDO.equals(cdCliente)) && !TIPO_PROSPECTS.equals(flTipoCadastro);
	}
	
	public boolean isNovoClienteDefaultParaNovoPedido() {
		return CD_NOVO_CLIENTE_DEFAULT_PARA_NOVO_PEDIDO.equals(cdCliente) && NM_CLIENTE_DEFAULT.equals(nmRazaoSocial);
	}

	public boolean isClienteDefaultParaNovoPedido() {
		return CD_CLIENTE_DEFAULT_PARA_NOVO_PEDIDO.equals(cdCliente) && NM_CLIENTE_DEFAULT.equals(nmRazaoSocial);
	}

	public boolean isClienteDefaultParaNovaPesquisaMercado() {
		return CD_CLIENTE_DEFAULT_PARA_NOVA_PESQUISA_MERCADO.equals(cdCliente) && NM_CLIENTE_DEFAULT.equals(nmRazaoSocial);
	}

	public boolean isClienteDefaultParaCadCoordenadas() {
		return CD_CLIENTE_DEFAULT_PARA_CADASTRO_COORDENADA.equals(cdCliente) && NM_CLIENTE_DEFAULT.equals(nmRazaoSocial);
	}

	public boolean isEspecial() {
		return ValueUtil.VALOR_SIM.equals(flEspecial);
	}

	public boolean isPessoaFisica() {
		int count = 0;
		if (ValueUtil.isNotEmpty(nuCnpj)) {
			char[] chars = nuCnpj.toCharArray();
			int len = chars.length;
			for (int i = 0; i < len; i++) {
				char caracter = chars[i];
				if (ValueUtil.isValidNumberChar(caracter)) {
					count++;
				}
			}
		}
		return count == 11;
	}

	public double getIndiceFinanceiro(boolean indiceEspecialCliente) {
		if (LavenderePdaConfig.anulaDescontoPessoaFisica && !indiceEspecialCliente && isPessoaFisica()) {
			return 0;
		}
		return indiceEspecialCliente ? vlIndiceFinanceiroEspecial : vlIndiceFinanceiro;
	}

    public boolean isFlAplicaSt() {
    	return ValueUtil.VALOR_SIM.equals(flAplicaSt);
    }

    public boolean isOptanteSimples() {
    	return ValueUtil.VALOR_SIM.equals(flOptanteSimples);
    }

    public boolean isCreditoAntecipado() {
    	return ValueUtil.VALOR_SIM.equals(flCreditoAntecipado);
    }

    public boolean isClienteRedeContratoEspecial() {
    	return ValueUtil.isNotEmpty(cdContratoEspecial) && !"0".equals(cdContratoEspecial) && !cdContratoEspecial.equals(cdCliente);
    }

    public boolean isClienteContratoEspecial() {
    	return ValueUtil.isNotEmpty(cdContratoEspecial) && !"0".equals(cdContratoEspecial) && cdContratoEspecial.equals(cdCliente);
    }

    public boolean isPossuiRamoAtividade() {
    	return ValueUtil.isNotEmpty(cdRamoAtividade);
    }

    public boolean isAtendido() {
    	return ValueUtil.VALOR_SIM.equals(flAtendido) || (CLIENTE_FLATENDIDO_PDA.equals(flAtendido));
    }

    public boolean isDivideVlMin() {
    	return ValueUtil.VALOR_SIM.equals(flDivideVlMin);
    }
    
    public boolean isDebitaPisCofinsZonaFranca() {
    	return ValueUtil.VALOR_SIM.equals(flDebitaPisCofinsZonaFranca);
    }
    
    public boolean isModoDeProspeccao() {
    	return LavenderePdaConfig.usaClienteEmModoProspeccao && TIPO_PROSPECTS.equals(flTipoCadastro);
    }
    
    public boolean isTipoClienteRede() {
    	return ValueUtil.valueEquals(TIPO_REDE, flTipoClienteRede);
    }
    
    public boolean isTipoClienteIndividual() {
    	return ValueUtil.valueEquals(TIPO_INDIVIDUAL, flTipoClienteRede);
    }

    public TipoPagamento getTipoPagamento() throws SQLException {
		if (!ValueUtil.isEmpty(cdTipoPagamento) && ((tipoPagamento == null) || (!cdTipoPagamento.equals(tipoPagamento.cdTipoPagamento)))) {
			tipoPagamento = TipoPagamentoService.getInstance().getTipoPagamento(cdTipoPagamento);
		}
		return tipoPagamento;
	}
    
    public FichaFinanceira getFichaFinanceira() throws SQLException {
    	if (!ValueUtil.isEmpty(cdCliente) && ((fichaFinanceira == null) || (!cdCliente.equals(fichaFinanceira.cdCliente)))) {
    		fichaFinanceira = FichaFinanceiraService.getInstance().getFichaFinanceira(this);
    	}
    	return fichaFinanceira;
    }

    public Categoria getCategoria() throws SQLException {
    	if (ValueUtil.isNotEmpty(cdCategoria) && (categoria == null || !cdCategoria.equals(categoria.cdCategoria))) {
			categoria = CategoriaService.getInstance().getCategoria(cdEmpresa, cdCategoria);
		}
		return categoria;
    }
    
    public Rede getRede() throws SQLException {
    	if (ValueUtil.isNotEmpty(cdRede) && (rede == null || !cdRede.equals(rede.cdRede))) {
			rede = RedeService.getInstance().findRedeByCliente(this);
		}
		return rede;
    }
    
	public static String getPathImg() {
		return FotoUtil.getPathImg(Cliente.class);
	}
	
	public String getCdCliente() {
		return cdCliente;
	}
	
	public void setCdCliente(String value) {
		cdCliente = value;
	}
	
	public boolean isPermiteConsignacao() {
		return ValueUtil.getBooleanValue(flConsignaPedido);
	}
	
	public double getVlIndiceFinanceiro() {
		return vlIndiceFinanceiro <= 0 ? 1 : vlIndiceFinanceiro;
	}
	
	public boolean isClienteRecente() {
		if (dtCadastro != null && !isModoDeProspeccao()) {
			int diasCadastro = DateUtil.getDaysBetween(DateUtil.getCurrentDate(), dtCadastro);
			return diasCadastro <= LavenderePdaConfig.nuPeriodoDiasClienteRecenteNaBase;
		}
		return false;
	}
	
	public boolean isClienteSemComprasHist() {
		return (nuDiasSemPedidoInvalido || nuDiasSemPedido < 0) && !isModoDeProspeccao();
	}
	
	public boolean isClienteSemCompraMes() {
		return !ValueUtil.getBooleanValue(flAtendido) && !isModoDeProspeccao();
	}
	
	public String getFlBoletoAceite() {
		return ValueUtil.isNotEmpty(flBoletoAceite) ? flBoletoAceite : FLBOLETOACEITE_A;
	}
	
	public String getNmClienteImpressao() {
		return LavenderePdaConfig.isInverteNomeFantasiaRazaoSocialCliente() ? nmFantasia : nmRazaoSocial;
	}
	
	public boolean isPossuiDescontoExtraProdutoRestrito() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flPermiteProdutoRestrito);
	}
	
	public boolean isAtualizaCadastroWmw() {
		return ValueUtil.getBooleanValue(flAtualizaCadastroWmw);
	}
	
	public boolean isFreteEmbutido() {
		return ValueUtil.getBooleanValue(flFreteEmbutido);
	}
	
	public boolean visualizouSlideDivulgacao() {
		return nuDivulgaInfo > 0;
	}
	
	public boolean isClientePossuiCoordenada() {
		return cdLatitude != 0 && cdLongitude != 0;
	}
	
	public boolean isUsaCodigoInterno() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flCodigoInternoCliente);
	}
	
	public boolean isFlIgnoraVerbaGrpProd() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flIgnoraVerbaGrpProd);
	}
	
}
