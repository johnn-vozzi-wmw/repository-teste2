package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import totalcross.util.Date;

public class PedidoDTO {

	public String cdEmpresa;
	public String cdRepresentante;
	public String nuPedido;
	public String flOrigemPedido;
	public String cdCliente;
	public String nuPedidoRelacionado;
	public String flOrigemPedidoRelacionado;
	public String cdStatusPedido;
	public Date dtEmissao;
	public String hrEmissao;
	public Date dtEntrega;
	public double vlTotalItens;
	public double vlTotalPedido;
	public String cdCondicaoPagamento;
	public String cdTabelaPreco;
	public String cdTipoPagamento;
	public String cdTipoPedido;
	public String cdSegmento;
	public String cdCondicaoComercial;
	public String cdTransportadora;
	public String cdTipoFrete;
	public double vlFrete;
	public String cdSupervisor;
	public double vlVerbaPedido;
	public double vlVerbaPedidoPositivo;
	public String hrFimEmissao;
	public String flPrecoLiberadoSenha;
	public String nuOrdemCompraCliente;
	public String cdRotaEntrega;
	public String flCreditoClienteLiberadoSenha;
	public String flPossuiDiferenca;
	public String cdSetor;
	public String cdOrigemSetor;
	public String cdTipoEntrega;
	public String nuVersaoSistemaOrigem;
	public String dsUrlEnvioServidor;
	public double vlDesconto;
	public double qtPeso;
	public String cdAreaVenda;
	public Date dtFechamento;
	public String hrFechamento;
	public Date dtTransmissaoPda;
	public String hrTransmissaoPda;
	public double vlTrocaRecolher;
	public double vlTrocaEntregar;
	public double vlPctDesconto;
	public double vlPctDescProgressivo;
	public double vlPctDescItem;
	public double vlBonificacaoPedido;
	public double vlRentabilidade;
	public String flPedidoNovoCliente;
	public String dsCondicaoPagamentoSemCadastro;
	public int qtPontosPedido;
	public String flBloqueadoEdicao;
	public double vlTotalBrutoItens;
	public double vlTotalBaseItens;
	public String flMaxVendaLiberadoSenha;
	public String flClienteAtrasadoLiberadoSenha;
	public String dsMotivoDesconto;
	public double vlPctFreteRepresentante;
	public double vlFreteRepresentante;
	public double vlFreteCliente;
	public String flImpresso;
	public String nuPedidoRelBonificacao;
	public String nuPedidoSugestao;
	public String flOrigemPedidoSugestao;
	public double vlTotalPedidoEstoquePositivo;
	public String cdCentroCusto;
	public String cdItemConta;
	public String cdClasseValor;
	public String flPedidoReplicado;
	public String nuPedidoOriginal;
	public String flSugestaoVendaLiberadoSenha;
	public String cdCargaPedido;
	public String flAbaixoRentabilidadeMinima;
	public double vlPctMargemMin;
	public String flEtapaVerba;
	public double vlPctComissao;
	public String flPagamentoAVista;
	public String flGeraNfe;
	public int nuKmInicial;
	public int nuKmFinal;
	public String hrInicialIndicado;
	public String hrFinalIndicado;
	public String flNfeImpressa;
	public String flLiberadoEntrega;
	public String cdEnderecoCliente;
	public String flGeraBoleto;
	public String flBoletoImpresso;
	public String flSituacaoReservaEst;
	public String nuPedidoRelTroca;
	public String nuPedidoComplementado;
	public String flKeyAccount;
	public String flPendente;
	public double vlTotalCreditoCondicao;
	public double vlTotalCreditoFrete;
	public String flGeraCreditoCondicao;
	public String flGeraCreditoFrete;
	public String cdMotivoCancelamento;
	public String flItemPendente;
	public String flPedidoLiberadoOutraOrdem;
	public double vlPctDescProgressivoMix;
	public double vlVolumePedido;
	public double vlPctDescFrete;
	public double vlPctDescCliente;
	public double vlPctDescontoCondicao;
	public Date dtEntregaLiberada;
	public double vlTotalTrocaPedido;
	public Date dtCarregamento;
	public String flCotaCondPagto;
	public String cdUsuarioLibEntrega;
	public String cdContato;
	public String nuPedCompRelacionado;
	public String flRestrito;
	public String cdCondNegociacao;
	public String oldCdCondicaoNegociacao;
	public String flMinVerbaLiberado;
	public String cdUnidade;
	public String dsObsOrcamento;
	public double vlFinalPedidoDescTribFrete;
	public ItemPedidoDTO[] itensPedidoErp;
	public double qtEncomenda;
	public double vlTotalEncomenda;
	public Date dtFaturamento;
	public double vlPctDesc2;
	public double vlPctDesc3;
	public String cdPlataformaVenda;
	public boolean useLayoutViaClientePDA;
	public Date dtPagamento;
	
	public PedidoDTO() {
	}
	
	public ItemPedidoDTO[] getItensPedidoErp() {
		return itensPedidoErp;
	}

	public void setItensPedidoErp(ItemPedidoDTO[] itemPedidoList) {
		this.itensPedidoErp = itemPedidoList;
	}

	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}

	public void setCdRepresentante(String cdRepresentante) {
		this.cdRepresentante = cdRepresentante;
	}

	public void setNuPedido(String nuPedido) {
		this.nuPedido = nuPedido;
	}

	public void setFlOrigemPedido(String flOrigemPedido) {
		this.flOrigemPedido = flOrigemPedido;
	}

	public void setCdCliente(String cdCliente) {
		this.cdCliente = cdCliente;
	}

	public void setNuPedidoRelacionado(String nuPedidoRelacionado) {
		this.nuPedidoRelacionado = nuPedidoRelacionado;
	}

	public void setFlOrigemPedidoRelacionado(String flOrigemPedidoRelacionado) {
		this.flOrigemPedidoRelacionado = flOrigemPedidoRelacionado;
	}

	public void setCdStatusPedido(String cdStatusPedido) {
		this.cdStatusPedido = cdStatusPedido;
	}

	public void setDtEmissao(Date dtEmissao) {
		this.dtEmissao = dtEmissao;
	}

	public void setHrEmissao(String hrEmissao) {
		this.hrEmissao = hrEmissao;
	}

	public void setDtEntrega(Date dtEntrega) {
		this.dtEntrega = dtEntrega;
	}

	public void setVlTotalItens(double vlTotalItens) {
		this.vlTotalItens = vlTotalItens;
	}

	public void setVlTotalPedido(double vlTotalPedido) {
		this.vlTotalPedido = vlTotalPedido;
	}

	public void setCdCondicaoPagamento(String cdCondicaoPagamento) {
		this.cdCondicaoPagamento = cdCondicaoPagamento;
	}

	public void setCdTabelaPreco(String cdTabelaPreco) {
		this.cdTabelaPreco = cdTabelaPreco;
	}

	public void setCdTipoPagamento(String cdTipoPagamento) {
		this.cdTipoPagamento = cdTipoPagamento;
	}

	public void setCdTipoPedido(String cdTipoPedido) {
		this.cdTipoPedido = cdTipoPedido;
	}

	public void setCdSegmento(String cdSegmento) {
		this.cdSegmento = cdSegmento;
	}

	public void setCdCondicaoComercial(String cdCondicaoComercial) {
		this.cdCondicaoComercial = cdCondicaoComercial;
	}

	public void setCdTransportadora(String cdTransportadora) {
		this.cdTransportadora = cdTransportadora;
	}

	public void setCdTipoFrete(String cdTipoFrete) {
		this.cdTipoFrete = cdTipoFrete;
	}

	public void setVlFrete(double vlFrete) {
		this.vlFrete = vlFrete;
	}

	public void setCdSupervisor(String cdSupervisor) {
		this.cdSupervisor = cdSupervisor;
	}

	public void setVlVerbaPedido(double vlVerbaPedido) {
		this.vlVerbaPedido = vlVerbaPedido;
	}

	public void setVlVerbaPedidoPositivo(double vlVerbaPedidoPositivo) {
		this.vlVerbaPedidoPositivo = vlVerbaPedidoPositivo;
	}

	public void setHrFimEmissao(String hrFimEmissao) {
		this.hrFimEmissao = hrFimEmissao;
	}

	public void setFlPrecoLiberadoSenha(String flPrecoLiberadoSenha) {
		this.flPrecoLiberadoSenha = flPrecoLiberadoSenha;
	}

	public void setNuOrdemCompraCliente(String nuOrdemCompraCliente) {
		this.nuOrdemCompraCliente = nuOrdemCompraCliente;
	}

	public void setCdRotaEntrega(String cdRotaEntrega) {
		this.cdRotaEntrega = cdRotaEntrega;
	}

	public void setFlCreditoClienteLiberadoSenha(String flCreditoClienteLiberadoSenha) {
		this.flCreditoClienteLiberadoSenha = flCreditoClienteLiberadoSenha;
	}

	public void setFlPossuiDiferenca(String flPossuiDiferenca) {
		this.flPossuiDiferenca = flPossuiDiferenca;
	}

	public void setCdSetor(String cdSetor) {
		this.cdSetor = cdSetor;
	}

	public void setCdOrigemSetor(String cdOrigemSetor) {
		this.cdOrigemSetor = cdOrigemSetor;
	}

	public void setCdTipoEntrega(String cdTipoEntrega) {
		this.cdTipoEntrega = cdTipoEntrega;
	}

	public void setNuVersaoSistemaOrigem(String nuVersaoSistemaOrigem) {
		this.nuVersaoSistemaOrigem = nuVersaoSistemaOrigem;
	}

	public void setDsUrlEnvioServidor(String dsUrlEnvioServidor) {
		this.dsUrlEnvioServidor = dsUrlEnvioServidor;
	}

	public void setVlDesconto(double vlDesconto) {
		this.vlDesconto = vlDesconto;
	}

	public void setQtPeso(double qtPeso) {
		this.qtPeso = qtPeso;
	}

	public void setCdAreaVenda(String cdAreaVenda) {
		this.cdAreaVenda = cdAreaVenda;
	}

	public void setDtFechamento(Date dtFechamento) {
		this.dtFechamento = dtFechamento;
	}

	public void setHrFechamento(String hrFechamento) {
		this.hrFechamento = hrFechamento;
	}

	public void setDtTransmissaoPda(Date dtTransmissaoPda) {
		this.dtTransmissaoPda = dtTransmissaoPda;
	}

	public void setHrTransmissaoPda(String hrTransmissaoPda) {
		this.hrTransmissaoPda = hrTransmissaoPda;
	}

	public void setVlTrocaRecolher(double vlTrocaRecolher) {
		this.vlTrocaRecolher = vlTrocaRecolher;
	}

	public void setVlTrocaEntregar(double vlTrocaEntregar) {
		this.vlTrocaEntregar = vlTrocaEntregar;
	}

	public void setVlPctDesconto(double vlPctDesconto) {
		this.vlPctDesconto = vlPctDesconto;
	}

	public void setVlPctDescProgressivo(double vlPctDescProgressivo) {
		this.vlPctDescProgressivo = vlPctDescProgressivo;
	}

	public void setVlPctDescItem(double vlPctDescItem) {
		this.vlPctDescItem = vlPctDescItem;
	}

	public void setVlBonificacaoPedido(double vlBonificacaoPedido) {
		this.vlBonificacaoPedido = vlBonificacaoPedido;
	}

	public void setVlRentabilidade(double vlRentabilidade) {
		this.vlRentabilidade = vlRentabilidade;
	}

	public void setFlPedidoNovoCliente(String flPedidoNovoCliente) {
		this.flPedidoNovoCliente = flPedidoNovoCliente;
	}

	public void setDsCondicaoPagamentoSemCadastro(String dsCondicaoPagamentoSemCadastro) {
		this.dsCondicaoPagamentoSemCadastro = dsCondicaoPagamentoSemCadastro;
	}

	public void setQtPontosPedido(int qtPontosPedido) {
		this.qtPontosPedido = qtPontosPedido;
	}

	public void setFlBloqueadoEdicao(String flBloqueadoEdicao) {
		this.flBloqueadoEdicao = flBloqueadoEdicao;
	}

	public void setVlTotalBrutoItens(double vlTotalBrutoItens) {
		this.vlTotalBrutoItens = vlTotalBrutoItens;
	}

	public void setVlTotalBaseItens(double vlTotalBaseItens) {
		this.vlTotalBaseItens = vlTotalBaseItens;
	}

	public void setFlMaxVendaLiberadoSenha(String flMaxVendaLiberadoSenha) {
		this.flMaxVendaLiberadoSenha = flMaxVendaLiberadoSenha;
	}

	public void setFlClienteAtrasadoLiberadoSenha(String flClienteAtrasadoLiberadoSenha) {
		this.flClienteAtrasadoLiberadoSenha = flClienteAtrasadoLiberadoSenha;
	}

	public void setDsMotivoDesconto(String dsMotivoDesconto) {
		this.dsMotivoDesconto = dsMotivoDesconto;
	}

	public void setVlPctFreteRepresentante(double vlPctFreteRepresentante) {
		this.vlPctFreteRepresentante = vlPctFreteRepresentante;
	}

	public void setVlFreteRepresentante(double vlFreteRepresentante) {
		this.vlFreteRepresentante = vlFreteRepresentante;
	}

	public void setVlFreteCliente(double vlFreteCliente) {
		this.vlFreteCliente = vlFreteCliente;
	}

	public void setFlImpresso(String flImpresso) {
		this.flImpresso = flImpresso;
	}

	public void setNuPedidoRelBonificacao(String nuPedidoRelBonificacao) {
		this.nuPedidoRelBonificacao = nuPedidoRelBonificacao;
	}

	public void setNuPedidoSugestao(String nuPedidoSugestao) {
		this.nuPedidoSugestao = nuPedidoSugestao;
	}

	public void setFlOrigemPedidoSugestao(String flOrigemPedidoSugestao) {
		this.flOrigemPedidoSugestao = flOrigemPedidoSugestao;
	}

	public void setVlTotalPedidoEstoquePositivo(double vlTotalPedidoEstoquePositivo) {
		this.vlTotalPedidoEstoquePositivo = vlTotalPedidoEstoquePositivo;
	}

	public void setCdCentroCusto(String cdCentroCusto) {
		this.cdCentroCusto = cdCentroCusto;
	}

	public void setCdItemConta(String cdItemConta) {
		this.cdItemConta = cdItemConta;
	}

	public void setCdClasseValor(String cdClasseValor) {
		this.cdClasseValor = cdClasseValor;
	}

	public void setFlPedidoReplicado(String flPedidoReplicado) {
		this.flPedidoReplicado = flPedidoReplicado;
	}

	public void setNuPedidoOriginal(String nuPedidoOriginal) {
		this.nuPedidoOriginal = nuPedidoOriginal;
	}

	public void setFlSugestaoVendaLiberadoSenha(String flSugestaoVendaLiberadoSenha) {
		this.flSugestaoVendaLiberadoSenha = flSugestaoVendaLiberadoSenha;
	}

	public void setCdCargaPedido(String cdCargaPedido) {
		this.cdCargaPedido = cdCargaPedido;
	}

	public void setFlAbaixoRentabilidadeMinima(String flAbaixoRentabilidadeMinima) {
		this.flAbaixoRentabilidadeMinima = flAbaixoRentabilidadeMinima;
	}

	public void setVlPctMargemMin(double vlPctMargemMin) {
		this.vlPctMargemMin = vlPctMargemMin;
	}

	public void setFlEtapaVerba(String flEtapaVerba) {
		this.flEtapaVerba = flEtapaVerba;
	}

	public void setVlPctComissao(double vlPctComissao) {
		this.vlPctComissao = vlPctComissao;
	}

	public void setFlPagamentoAVista(String flPagamentoAVista) {
		this.flPagamentoAVista = flPagamentoAVista;
	}

	public void setFlGeraNfe(String flGeraNfe) {
		this.flGeraNfe = flGeraNfe;
	}

	public void setNuKmInicial(int nuKmInicial) {
		this.nuKmInicial = nuKmInicial;
	}

	public void setNuKmFinal(int nuKmFinal) {
		this.nuKmFinal = nuKmFinal;
	}

	public void setHrInicialIndicado(String hrInicialIndicado) {
		this.hrInicialIndicado = hrInicialIndicado;
	}

	public void setHrFinalIndicado(String hrFinalIndicado) {
		this.hrFinalIndicado = hrFinalIndicado;
	}

	public void setFlNfeImpressa(String flNfeImpressa) {
		this.flNfeImpressa = flNfeImpressa;
	}

	public void setFlLiberadoEntrega(String flLiberadoEntrega) {
		this.flLiberadoEntrega = flLiberadoEntrega;
	}

	public void setCdEnderecoCliente(String cdEnderecoCliente) {
		this.cdEnderecoCliente = cdEnderecoCliente;
	}

	public void setFlGeraBoleto(String flGeraBoleto) {
		this.flGeraBoleto = flGeraBoleto;
	}

	public void setFlBoletoImpresso(String flBoletoImpresso) {
		this.flBoletoImpresso = flBoletoImpresso;
	}

	public void setFlSituacaoReservaEst(String flSituacaoReservaEst) {
		this.flSituacaoReservaEst = flSituacaoReservaEst;
	}

	public void setNuPedidoRelTroca(String nuPedidoRelTroca) {
		this.nuPedidoRelTroca = nuPedidoRelTroca;
	}

	public void setNuPedidoComplementado(String nuPedidoComplementado) {
		this.nuPedidoComplementado = nuPedidoComplementado;
	}

	public void setFlKeyAccount(String flKeyAccount) {
		this.flKeyAccount = flKeyAccount;
	}

	public void setFlPendente(String flPendente) {
		this.flPendente = flPendente;
	}

	public void setVlTotalCreditoCondicao(double vlTotalCreditoCondicao) {
		this.vlTotalCreditoCondicao = vlTotalCreditoCondicao;
	}

	public void setVlTotalCreditoFrete(double vlTotalCreditoFrete) {
		this.vlTotalCreditoFrete = vlTotalCreditoFrete;
	}

	public void setFlGeraCreditoCondicao(String flGeraCreditoCondicao) {
		this.flGeraCreditoCondicao = flGeraCreditoCondicao;
	}

	public void setFlGeraCreditoFrete(String flGeraCreditoFrete) {
		this.flGeraCreditoFrete = flGeraCreditoFrete;
	}

	public void setCdMotivoCancelamento(String cdMotivoCancelamento) {
		this.cdMotivoCancelamento = cdMotivoCancelamento;
	}

	public void setFlItemPendente(String flItemPendente) {
		this.flItemPendente = flItemPendente;
	}

	public void setFlPedidoLiberadoOutraOrdem(String flPedidoLiberadoOutraOrdem) {
		this.flPedidoLiberadoOutraOrdem = flPedidoLiberadoOutraOrdem;
	}

	public void setVlPctDescProgressivoMix(double vlPctDescProgressivoMix) {
		this.vlPctDescProgressivoMix = vlPctDescProgressivoMix;
	}

	public void setVlVolumePedido(double vlVolumePedido) {
		this.vlVolumePedido = vlVolumePedido;
	}

	public void setVlPctDescFrete(double vlPctDescFrete) {
		this.vlPctDescFrete = vlPctDescFrete;
	}

	public void setVlPctDescCliente(double vlPctDescCliente) {
		this.vlPctDescCliente = vlPctDescCliente;
	}

	public void setVlPctDescontoCondicao(double vlPctDescontoCondicao) {
		this.vlPctDescontoCondicao = vlPctDescontoCondicao;
	}

	public void setDtEntregaLiberada(Date dtEntregaLiberada) {
		this.dtEntregaLiberada = dtEntregaLiberada;
	}

	public void setVlTotalTrocaPedido(double vlTotalTrocaPedido) {
		this.vlTotalTrocaPedido = vlTotalTrocaPedido;
	}

	public void setDtCarregamento(Date dtCarregamento) {
		this.dtCarregamento = dtCarregamento;
	}

	public void setFlCotaCondPagto(String flCotaCondPagto) {
		this.flCotaCondPagto = flCotaCondPagto;
	}

	public void setCdUsuarioLibEntrega(String cdUsuarioLibEntrega) {
		this.cdUsuarioLibEntrega = cdUsuarioLibEntrega;
	}

	public void setCdContato(String cdContato) {
		this.cdContato = cdContato;
	}

	public void setNuPedCompRelacionado(String nuPedCompRelacionado) {
		this.nuPedCompRelacionado = nuPedCompRelacionado;
	}

	public void setFlRestrito(String flRestrito) {
		this.flRestrito = flRestrito;
	}

	public void setCdCondNegociacao(String cdCondNegociacao) {
		this.cdCondNegociacao = cdCondNegociacao;
	}

	public void setOldCdCondicaoNegociacao(String oldCdCondicaoNegociacao) {
		this.oldCdCondicaoNegociacao = oldCdCondicaoNegociacao;
	}

	public void setFlMinVerbaLiberado(String flMinVerbaLiberado) {
		this.flMinVerbaLiberado = flMinVerbaLiberado;
	}

	public void setCdUnidade(String cdUnidade) {
		this.cdUnidade = cdUnidade;
	}

	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}

	//Dinâmicos
	public String dsObservacao;

	public PedidoDTO(final Pedido pedido) {
		super();
		try {
			FieldMapper.copy(pedido, this);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public String getCdEmpresa() {
		return cdEmpresa;
	}
	
	public String getCdRepresentante() {
		return cdRepresentante;
	}
	
	public String getNuPedido() {
		return nuPedido;
	}
	
	public String getFlOrigemPedido() {
		return flOrigemPedido;
	}
	
	public String getCdCliente() {
		return cdCliente;
	}
	
	public String getNuPedidoRelacionado() {
		return nuPedidoRelacionado;
	}
	
	public String getFlOrigemPedidoRelacionado() {
		return flOrigemPedidoRelacionado;
	}
	
	public String getCdStatusPedido() {
		return cdStatusPedido;
	}
	
	public String getDtEmissao() {
		return dtEmissao == null ? null : DateUtil.formatDateDDMMYYYY(dtEmissao);
	}
	public String getHrEmissao() {
		return hrEmissao;
	}
	public String getDtEntrega() {
		return dtEntrega == null ? null : DateUtil.formatDateDDMMYYYY(dtEntrega);
	}
	public double getVlTotalItens() {
		return ValueUtil.round(vlTotalItens);
	}
	public double getVlTotalPedido() {
		return ValueUtil.round(vlTotalPedido);
	}
	public String getCdCondicaoPagamento() {
		return cdCondicaoPagamento;
	}
	public String getCdTabelaPreco() {
		return cdTabelaPreco;
	}
	public String getCdTipoPagamento() {
		return cdTipoPagamento;
	}
	public String getCdTipoPedido() {
		return cdTipoPedido;
	}
	public String getCdSegmento() {
		return cdSegmento;
	}
	public String getCdCondicaoComercial() {
		return cdCondicaoComercial;
	}
	public String getCdTransportadora() {
		return cdTransportadora;
	}
	public String getCdTipoFrete() {
		return cdTipoFrete;
	}
	public double getVlFrete() {
		return ValueUtil.round(vlFrete);
	}
	public String getCdSupervisor() {
		return cdSupervisor;
	}
	public double getVlVerbaPedido() {
		return ValueUtil.round(vlVerbaPedido);
	}
	public double getVlVerbaPedidoPositivo() {
		return ValueUtil.round(vlVerbaPedidoPositivo);
	}
	public String getHrFimEmissao() {
		return hrFimEmissao;
	}
	public String getFlPrecoLiberadoSenha() {
		return flPrecoLiberadoSenha;
	}
	public String getNuOrdemCompraCliente() {
		return nuOrdemCompraCliente;
	}
	public String getCdRotaEntrega() {
		return cdRotaEntrega;
	}
	public String getFlCreditoClienteLiberadoSenha() {
		return flCreditoClienteLiberadoSenha;
	}
	public String getFlPossuiDiferenca() {
		return flPossuiDiferenca;
	}
	public String getCdSetor() {
		return cdSetor;
	}
	public String getCdOrigemSetor() {
		return cdOrigemSetor;
	}
	public String getCdTipoEntrega() {
		return cdTipoEntrega;
	}
	public String getNuVersaoSistemaOrigem() {
		return nuVersaoSistemaOrigem;
	}
	public String getDsUrlEnvioServidor() {
		return dsUrlEnvioServidor;
	}
	public double getVlDesconto() {
		return ValueUtil.round(vlDesconto);
	}
	public double getQtPeso() {
		return ValueUtil.round(qtPeso);
	}
	public String getCdAreaVenda() {
		return cdAreaVenda;
	}
	public String getDtFechamento() {
		return dtFechamento == null ? null : DateUtil.formatDateDDMMYYYY(dtFechamento);
	}
	public String getHrFechamento() {
		return hrFechamento;
	}
	public String getDtTransmissaoPda() {
		return dtTransmissaoPda == null ? null : DateUtil.formatDateDDMMYYYY(dtTransmissaoPda);
	}
	public String getHrTransmissaoPda() {
		return hrTransmissaoPda;
	}
	public double getVlTrocaRecolher() {
		return ValueUtil.round(vlTrocaRecolher);
	}
	public double getVlTrocaEntregar() {
		return ValueUtil.round(vlTrocaEntregar);
	}
	public double getVlPctDesconto() {
		return ValueUtil.round(vlPctDesconto);
	}
	public double getVlPctDescProgressivo() {
		return ValueUtil.round(vlPctDescProgressivo);
	}
	public double getVlPctDescItem() {
		return ValueUtil.round(vlPctDescItem);
	}
	public double getVlBonificacaoPedido() {
		return ValueUtil.round(vlBonificacaoPedido);
	}
	public double getVlRentabilidade() {
		return ValueUtil.round(vlRentabilidade);
	}
	public String getFlPedidoNovoCliente() {
		return flPedidoNovoCliente;
	}
	public String getDsCondicaoPagamentoSemCadastro() {
		return dsCondicaoPagamentoSemCadastro;
	}
	public int getQtPontosPedido() {
		return qtPontosPedido;
	}
	public String getFlBloqueadoEdicao() {
		return flBloqueadoEdicao;
	}
	public double getVlTotalBrutoItens() {
		return ValueUtil.round(vlTotalBrutoItens);
	}
	public double getVlTotalBaseItens() {
		return ValueUtil.round(vlTotalBaseItens);
	}
	public String getFlMaxVendaLiberadoSenha() {
		return flMaxVendaLiberadoSenha;
	}
	public String getFlClienteAtrasadoLiberadoSenha() {
		return flClienteAtrasadoLiberadoSenha;
	}
	public String getDsMotivoDesconto() {
		return dsMotivoDesconto;
	}
	public double getVlPctFreteRepresentante() {
		return ValueUtil.round(vlPctFreteRepresentante);
	}
	public double getVlFreteRepresentante() {
		return ValueUtil.round(vlFreteRepresentante);
	}
	public double getVlFreteCliente() {
		return ValueUtil.round(vlFreteCliente);
	}
	public String getFlImpresso() {
		return flImpresso;
	}
	public String getNuPedidoRelBonificacao() {
		return nuPedidoRelBonificacao;
	}
	
	public String getNuPedidoSugestao() {
		return nuPedidoSugestao;
	}
	
	public String getFlOrigemPedidoSugestao() {
		return flOrigemPedidoSugestao;
	}
	
	public double getVlTotalPedidoEstoquePositivo() {
		return ValueUtil.round(vlTotalPedidoEstoquePositivo);
	}
	
	public String getCdCentroCusto() {
		return cdCentroCusto;
	}
	
	public String getCdItemConta() {
		return cdItemConta;
	}
	
	public String getCdClasseValor() {
		return cdClasseValor;
	}
	
	public String getFlPedidoReplicado() {
		return flPedidoReplicado;
	}
	
	public String getNuPedidoOriginal() {
		return nuPedidoOriginal;
	}
	
	public String getFlSugestaoVendaLiberadoSenha() {
		return flSugestaoVendaLiberadoSenha;
	}
	
	public String getCdCargaPedido() {
		return cdCargaPedido;
	}
	
	public String getFlAbaixoRentabilidadeMinima() {
		return flAbaixoRentabilidadeMinima;
	}
	
	public double getVlPctMargemMin() {
		return ValueUtil.round(vlPctMargemMin);
	}
	
	public String getFlEtapaVerba() {
		return flEtapaVerba;
	}
	
	public double getVlPctComissao() {
		return ValueUtil.round(vlPctComissao);
	}
	
	public String getFlPagamentoAVista() {
		return flPagamentoAVista;
	}
	
	public String getFlGeraNfe() {
		return flGeraNfe;
	}
	
	public int getNuKmInicial() {
		return nuKmInicial;
	}
	
	public int getNuKmFinal() {
		return nuKmFinal;
	}
	
	public String getHrInicialIndicado() {
		return hrInicialIndicado;
	}
	
	public String getHrFinalIndicado() {
		return hrFinalIndicado;
	}
	
	public String getFlNfeImpressa() {
		return flNfeImpressa;
	}
	
	public String getFlLiberadoEntrega() {
		return flLiberadoEntrega;
	}
	
	public String getCdEnderecoCliente() {
		return cdEnderecoCliente;
	}
	
	public String getFlGeraBoleto() {
		return flGeraBoleto;
	}
	
	public String getFlBoletoImpresso() {
		return flBoletoImpresso;
	}
	
	public String getFlSituacaoReservaEst() {
		return flSituacaoReservaEst;
	}
	
	public String getNuPedidoRelTroca() {
		return nuPedidoRelTroca;
	}
	
	public String getNuPedidoComplementado() {
		return nuPedidoComplementado;
	}
	
	public String getFlKeyAccount() {
		return flKeyAccount;
	}
	
	public String getFlPendente() {
		return flPendente;
	}
	
	public double getVlTotalCreditoCondicao() {
		return ValueUtil.round(vlTotalCreditoCondicao);
	}
	
	public double getVlTotalCreditoFrete() {
		return ValueUtil.round(vlTotalCreditoFrete);
	}
	
	public String getFlGeraCreditoCondicao() {
		return flGeraCreditoCondicao;
	}
	
	public String getFlGeraCreditoFrete() {
		return flGeraCreditoFrete;
	}
	
	public String getCdMotivoCancelamento() {
		return cdMotivoCancelamento;
	}
	
	public String getFlItemPendente() {
		return flItemPendente;
	}
	
	public String getFlPedidoLiberadoOutraOrdem() {
		return flPedidoLiberadoOutraOrdem;
	}
	
	public double getVlPctDescProgressivoMix() {
		return ValueUtil.round(vlPctDescProgressivoMix);
	}
	
	public double getVlVolumePedido() {
		return ValueUtil.round(vlVolumePedido);
	}
	
	public double getVlPctDescFrete() {
		return ValueUtil.round(vlPctDescFrete);
	}
	
	public double getVlPctDescCliente() {
		return ValueUtil.round(vlPctDescCliente);
	}
	
	public double getVlPctDescontoCondicao() {
		return ValueUtil.round(vlPctDescontoCondicao);
	}
	
	public String getDtEntregaLiberada() {
		return dtEntregaLiberada == null ? null : DateUtil.formatDateDDMMYYYY(dtEntregaLiberada);
	}
	
	public double getVlTotalTrocaPedido() {
		return ValueUtil.round(vlTotalTrocaPedido);
	}
	
	public String getDtCarregamento() {
		return dtCarregamento == null ? null : DateUtil.formatDateDDMMYYYY(dtCarregamento);
	}
	
	public String getFlCotaCondPagto() {
		return flCotaCondPagto;
	}
	
	public String getCdUsuarioLibEntrega() {
		return cdUsuarioLibEntrega;
	}
	
	public String getCdContato() {
		return cdContato;
	}
	
	public String getNuPedCompRelacionado() {
		return nuPedCompRelacionado;
	}
	
	public String getFlRestrito() {
		return flRestrito;
	}
	
	public String getCdCondNegociacao() {
		return cdCondNegociacao;
	}
	
	public String getOldCdCondicaoNegociacao() {
		return oldCdCondicaoNegociacao;
	}
	
	public String getFlMinVerbaLiberado() {
		return flMinVerbaLiberado;
	}
	
	public String getCdUnidade() {
		return cdUnidade;
	}

	public String getDsObservacao() {
		return dsObservacao;
	}

	public String getDsObsOrcamento() {
		return dsObsOrcamento;
	}

	public void setDsObsOrcamento(String dsObsOrcamento) {
		this.dsObsOrcamento = dsObsOrcamento;
	}

	public double getVlFinalPedidoDescTribFrete() {
		return vlFinalPedidoDescTribFrete;
	}

	public void setVlFinalPedidoDescTribFrete(double vlFinalPedidoDescTribFrete) {
		this.vlFinalPedidoDescTribFrete = vlFinalPedidoDescTribFrete;
	}

	public double getQtEncomenda() {
		return qtEncomenda;
	}

	public void setQtEncomenda(double qtEncomenda) {
		this.qtEncomenda = qtEncomenda;
	}

	public double getVlTotalEncomenda() {
		return vlTotalEncomenda;
	}

	public void setVlTotalEncomenda(double vlTotalEncomenda) {
		this.vlTotalEncomenda = vlTotalEncomenda;
	}
	
	public String getDtFaturamento() {
		return dtFaturamento == null ? null : DateUtil.formatDateDDMMYYYY(dtFaturamento);
	}

	public void setDtFaturamento(Date dtFaturamento) {
		this.dtFaturamento = dtFaturamento;
	}
	
	public double getVlPctDesc2() {
		return vlPctDesc2;
	}

	public void setVlPctDesc2(double vlPctDesc2) {
		this.vlPctDesc2 = vlPctDesc2;
	}

	public double getVlPctDesc3() {
		return vlPctDesc3;
	}

	public void setVlPctDesc3(double vlPctDesc3) {
		this.vlPctDesc3 = vlPctDesc3;
	}

	public String getCdPlataformaVenda() {
		return cdPlataformaVenda;
	}

	public void setCdPlataformaVenda(String cdPlataformaVenda) {
		this.cdPlataformaVenda = cdPlataformaVenda;
	}

	public boolean isUseLayoutViaClientePDA() {
		return useLayoutViaClientePDA;
	}

	public void setUseLayoutViaClientePDA(boolean useLayoutViaClientePDA) {
		this.useLayoutViaClientePDA = useLayoutViaClientePDA;
	}

	public String getDtPagamento() {
		return dtPagamento == null ? null : DateUtil.formatDateDDMMYYYY(dtPagamento);
	}

	public void setDtPagamento(Date dtPagamento) {
		this.dtPagamento = dtPagamento;
	}
}
