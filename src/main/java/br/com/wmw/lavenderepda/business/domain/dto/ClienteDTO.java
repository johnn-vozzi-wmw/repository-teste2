package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import totalcross.util.Date;

public class ClienteDTO {

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
	public String flStatusCliente;
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
	public double vlMaxTitulosAtraso;
	public int nuDiasToleranciaAtraso;
	public String flBoletoAceite;

	public ClienteDTO() {
		super();
	}

    public ClienteDTO copy(final Cliente cliente) {
		try {
			FieldMapper.copy(cliente, this);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return this;
	}

	public String getCdEmpresa() {
		return cdEmpresa;
	}

	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}

	public String getCdRepresentante() {
		return cdRepresentante;
	}

	public void setCdRepresentante(String cdRepresentante) {
		this.cdRepresentante = cdRepresentante;
	}

	public String getCdCliente() {
		return cdCliente;
	}

	public void setCdCliente(String cdCliente) {
		this.cdCliente = cdCliente;
	}

	public String getNmRazaoSocial() {
		return nmRazaoSocial;
	}

	public void setNmRazaoSocial(String nmRazaoSocial) {
		this.nmRazaoSocial = nmRazaoSocial;
	}

	public String getNuCnpj() {
		return nuCnpj;
	}

	public void setNuCnpj(String nuCnpj) {
		this.nuCnpj = nuCnpj;
	}

	public String getNmFantasia() {
		return nmFantasia;
	}

	public void setNmFantasia(String nmFantasia) {
		this.nmFantasia = nmFantasia;
	}

	public String getCdCondicaoPagamento() {
		return cdCondicaoPagamento;
	}

	public void setCdCondicaoPagamento(String cdCondicaoPagamento) {
		this.cdCondicaoPagamento = cdCondicaoPagamento;
	}

	public String getCdTipoPagamento() {
		return cdTipoPagamento;
	}

	public void setCdTipoPagamento(String cdTipoPagamento) {
		this.cdTipoPagamento = cdTipoPagamento;
	}

	public String getCdTipoPedido() {
		return cdTipoPedido;
	}

	public void setCdTipoPedido(String cdTipoPedido) {
		this.cdTipoPedido = cdTipoPedido;
	}

	public String getCdTabelaPreco() {
		return cdTabelaPreco;
	}

	public void setCdTabelaPreco(String cdTabelaPreco) {
		this.cdTabelaPreco = cdTabelaPreco;
	}

	public double getVlLimiteCredito() {
		return vlLimiteCredito;
	}

	public void setVlLimiteCredito(double vlLimiteCredito) {
		this.vlLimiteCredito = vlLimiteCredito;
	}

	public String getNuFone() {
		return nuFone;
	}

	public void setNuFone(String nuFone) {
		this.nuFone = nuFone;
	}

	public String getDsBairroComercial() {
		return dsBairroComercial;
	}

	public void setDsBairroComercial(String dsBairroComercial) {
		this.dsBairroComercial = dsBairroComercial;
	}

	public String getDsCidadeComercial() {
		return dsCidadeComercial;
	}

	public void setDsCidadeComercial(String dsCidadeComercial) {
		this.dsCidadeComercial = dsCidadeComercial;
	}

	public String getCdEstadoComercial() {
		return cdEstadoComercial;
	}

	public void setCdEstadoComercial(String cdEstadoComercial) {
		this.cdEstadoComercial = cdEstadoComercial;
	}

	public String getDsEstadoComercial() {
		return dsEstadoComercial;
	}

	public void setDsEstadoComercial(String dsEstadoComercial) {
		this.dsEstadoComercial = dsEstadoComercial;
	}

	public String getDsUfPreco() {
		return dsUfPreco;
	}

	public void setDsUfPreco(String dsUfPreco) {
		this.dsUfPreco = dsUfPreco;
	}

	public String getCdRegiao() {
		return cdRegiao;
	}

	public void setCdRegiao(String cdRegiao) {
		this.cdRegiao = cdRegiao;
	}

	public String getFlStatusCliente() {
		return flStatusCliente;
	}

	public void setFlStatusCliente(String flStatusCliente) {
		this.flStatusCliente = flStatusCliente;
	}

	public String getDtVencimentoAlvara() {
		return dtVencimentoAlvara == null ? null : DateUtil.formatDateDDMMYYYY(dtVencimentoAlvara);
	}

	public void setDtVencimentoAlvara(Date dtVencimentoAlvara) {
		this.dtVencimentoAlvara = dtVencimentoAlvara;
	}

	public String getFlEspecial() {
		return flEspecial;
	}

	public void setFlEspecial(String flEspecial) {
		this.flEspecial = flEspecial;
	}

	public int getQtDiasMaximoPagamento() {
		return qtDiasMaximoPagamento;
	}

	public void setQtDiasMaximoPagamento(int qtDiasMaximoPagamento) {
		this.qtDiasMaximoPagamento = qtDiasMaximoPagamento;
	}

	public String getCdRotaEntrega() {
		return cdRotaEntrega;
	}

	public void setCdRotaEntrega(String cdRotaEntrega) {
		this.cdRotaEntrega = cdRotaEntrega;
	}

	public double getVlIndiceFinanceiro() {
		return vlIndiceFinanceiro;
	}

	public void setVlIndiceFinanceiro(double vlIndiceFinanceiro) {
		this.vlIndiceFinanceiro = vlIndiceFinanceiro;
	}

	public String getCdCategoria() {
		return cdCategoria;
	}

	public void setCdCategoria(String cdCategoria) {
		this.cdCategoria = cdCategoria;
	}

	public String getCdContratoEspecial() {
		return cdContratoEspecial;
	}

	public void setCdContratoEspecial(String cdContratoEspecial) {
		this.cdContratoEspecial = cdContratoEspecial;
	}

	public String getCdTipoEntrega() {
		return cdTipoEntrega;
	}

	public void setCdTipoEntrega(String cdTipoEntrega) {
		this.cdTipoEntrega = cdTipoEntrega;
	}

	public String getFlConsisteConversaoUnidade() {
		return flConsisteConversaoUnidade;
	}

	public void setFlConsisteConversaoUnidade(String flConsisteConversaoUnidade) {
		this.flConsisteConversaoUnidade = flConsisteConversaoUnidade;
	}

	public String getCdTributacaoCliente() {
		return cdTributacaoCliente;
	}

	public void setCdTributacaoCliente(String cdTributacaoCliente) {
		this.cdTributacaoCliente = cdTributacaoCliente;
	}

	public String getCdTributacaoCliente2() {
		return cdTributacaoCliente2;
	}

	public void setCdTributacaoCliente2(String cdTributacaoCliente2) {
		this.cdTributacaoCliente2 = cdTributacaoCliente2;
	}

	public String getFlAplicaSt() {
		return flAplicaSt;
	}

	public void setFlAplicaSt(String flAplicaSt) {
		this.flAplicaSt = flAplicaSt;
	}

	public double getVlIndiceComissao() {
		return vlIndiceComissao;
	}

	public void setVlIndiceComissao(double vlIndiceComissao) {
		this.vlIndiceComissao = vlIndiceComissao;
	}

	public double getVlIndiceFinanceiroEspecial() {
		return vlIndiceFinanceiroEspecial;
	}

	public void setVlIndiceFinanceiroEspecial(double vlIndiceFinanceiroEspecial) {
		this.vlIndiceFinanceiroEspecial = vlIndiceFinanceiroEspecial;
	}

	public double getVlIndiceFinanceiroCondPagto() {
		return vlIndiceFinanceiroCondPagto;
	}

	public void setVlIndiceFinanceiroCondPagto(double vlIndiceFinanceiroCondPagto) {
		this.vlIndiceFinanceiroCondPagto = vlIndiceFinanceiroCondPagto;
	}

	public String getFlCreditoAntecipado() {
		return flCreditoAntecipado;
	}

	public void setFlCreditoAntecipado(String flCreditoAntecipado) {
		this.flCreditoAntecipado = flCreditoAntecipado;
	}

	public String getFlOptanteSimples() {
		return flOptanteSimples;
	}

	public void setFlOptanteSimples(String flOptanteSimples) {
		this.flOptanteSimples = flOptanteSimples;
	}

	public int getNuDiasPrevisaoEntrega() {
		return nuDiasPrevisaoEntrega;
	}

	public void setNuDiasPrevisaoEntrega(int nuDiasPrevisaoEntrega) {
		this.nuDiasPrevisaoEntrega = nuDiasPrevisaoEntrega;
	}

	public int getNuDiasSemPedido() {
		return nuDiasSemPedido;
	}

	public void setNuDiasSemPedido(int nuDiasSemPedido) {
		this.nuDiasSemPedido = nuDiasSemPedido;
	}

	public String getDtAtualizacaoCadastro() {
		return dtAtualizacaoCadastro == null ? null : DateUtil.formatDateDDMMYYYY(dtAtualizacaoCadastro);
	}

	public void setDtAtualizacaoCadastro(Date dtAtualizacaoCadastro) {
		this.dtAtualizacaoCadastro = dtAtualizacaoCadastro;
	}

	public int getQtDiasPrazoPagtoEstendido() {
		return qtDiasPrazoPagtoEstendido;
	}

	public void setQtDiasPrazoPagtoEstendido(int qtDiasPrazoPagtoEstendido) {
		this.qtDiasPrazoPagtoEstendido = qtDiasPrazoPagtoEstendido;
	}

	public String getCdTransportadora() {
		return cdTransportadora;
	}

	public void setCdTransportadora(String cdTransportadora) {
		this.cdTransportadora = cdTransportadora;
	}

	public String getCdRamoAtividade() {
		return cdRamoAtividade;
	}

	public void setCdRamoAtividade(String cdRamoAtividade) {
		this.cdRamoAtividade = cdRamoAtividade;
	}

	public String getCdRestricaoVendaCli() {
		return cdRestricaoVendaCli;
	}

	public void setCdRestricaoVendaCli(String cdRestricaoVendaCli) {
		this.cdRestricaoVendaCli = cdRestricaoVendaCli;
	}

	public String getCdCidadeComercial() {
		return cdCidadeComercial;
	}

	public void setCdCidadeComercial(String cdCidadeComercial) {
		this.cdCidadeComercial = cdCidadeComercial;
	}

	public String getCdLocalEstoque() {
		return cdLocalEstoque;
	}

	public void setCdLocalEstoque(String cdLocalEstoque) {
		this.cdLocalEstoque = cdLocalEstoque;
	}

	public String getFlAtendido() {
		return flAtendido;
	}

	public void setFlAtendido(String flAtendido) {
		this.flAtendido = flAtendido;
	}

	public String getDtVencimentoAfe() {
		return dtVencimentoAfe == null ? null : DateUtil.formatDateDDMMYYYY(dtVencimentoAfe);
	}

	public void setDtVencimentoAfe(Date dtVencimentoAfe) {
		this.dtVencimentoAfe = dtVencimentoAfe;
	}

	public double getVlMaxPedido() {
		return vlMaxPedido;
	}

	public void setVlMaxPedido(double vlMaxPedido) {
		this.vlMaxPedido = vlMaxPedido;
	}

	public double getVlMinPedido() {
		return vlMinPedido;
	}

	public void setVlMinPedido(double vlMinPedido) {
		this.vlMinPedido = vlMinPedido;
	}

	public String getCdGrupoCondicao() {
		return cdGrupoCondicao;
	}

	public void setCdGrupoCondicao(String cdGrupoCondicao) {
		this.cdGrupoCondicao = cdGrupoCondicao;
	}

	public String getFlDivideVlMin() {
		return flDivideVlMin;
	}

	public void setFlDivideVlMin(String flDivideVlMin) {
		this.flDivideVlMin = flDivideVlMin;
	}

	public double getCdLatitude() {
		return cdLatitude;
	}

	public void setCdLatitude(double cdLatitude) {
		this.cdLatitude = cdLatitude;
	}

	public double getCdLongitude() {
		return cdLongitude;
	}

	public void setCdLongitude(double cdLongitude) {
		this.cdLongitude = cdLongitude;
	}

	public String getDsCepComercial() {
		return dsCepComercial;
	}

	public void setDsCepComercial(String dsCepComercial) {
		this.dsCepComercial = dsCepComercial;
	}

	public String getNuLogradouroComercial() {
		return nuLogradouroComercial;
	}

	public void setNuLogradouroComercial(String nuLogradouroComercial) {
		this.nuLogradouroComercial = nuLogradouroComercial;
	}

	public String getDsLogradouroComercial() {
		return dsLogradouroComercial;
	}

	public void setDsLogradouroComercial(String dsLogradouroComercial) {
		this.dsLogradouroComercial = dsLogradouroComercial;
	}

	public String getCdGrupoDescCli() {
		return cdGrupoDescCli;
	}

	public void setCdGrupoDescCli(String cdGrupoDescCli) {
		this.cdGrupoDescCli = cdGrupoDescCli;
	}

	public String getCdRede() {
		return cdRede;
	}

	public void setCdRede(String cdRede) {
		this.cdRede = cdRede;
	}

	public double getVlPctMaxDesconto() {
		return vlPctMaxDesconto;
	}

	public void setVlPctMaxDesconto(double vlPctMaxDesconto) {
		this.vlPctMaxDesconto = vlPctMaxDesconto;
	}

	public double getVlPctIcms() {
		return vlPctIcms;
	}

	public void setVlPctIcms(double vlPctIcms) {
		this.vlPctIcms = vlPctIcms;
	}

	public String getNuInscricaoEstadual() {
		return nuInscricaoEstadual;
	}

	public void setNuInscricaoEstadual(String nuInscricaoEstadual) {
		this.nuInscricaoEstadual = nuInscricaoEstadual;
	}

	public String getFlDebitaPisCofinsZonaFranca() {
		return flDebitaPisCofinsZonaFranca;
	}

	public void setFlDebitaPisCofinsZonaFranca(String flDebitaPisCofinsZonaFranca) {
		this.flDebitaPisCofinsZonaFranca = flDebitaPisCofinsZonaFranca;
	}

	public String getCdCanalCliente() {
		return cdCanalCliente;
	}

	public void setCdCanalCliente(String cdCanalCliente) {
		this.cdCanalCliente = cdCanalCliente;
	}

	public double getVlPctContratoCli() {
		return vlPctContratoCli;
	}

	public void setVlPctContratoCli(double vlPctContratoCli) {
		this.vlPctContratoCli = vlPctContratoCli;
	}

	public String getFlTipoCadastro() {
		return flTipoCadastro;
	}

	public void setFlTipoCadastro(String flTipoCadastro) {
		this.flTipoCadastro = flTipoCadastro;
	}

	public String getFlKeyAccount() {
		return flKeyAccount;
	}

	public void setFlKeyAccount(String flKeyAccount) {
		this.flKeyAccount = flKeyAccount;
	}

	public String getCdStatusRentCli() {
		return cdStatusRentCli;
	}

	public void setCdStatusRentCli(String cdStatusRentCli) {
		this.cdStatusRentCli = cdStatusRentCli;
	}

	public String getDsSituacao() {
		return dsSituacao;
	}

	public void setDsSituacao(String dsSituacao) {
		this.dsSituacao = dsSituacao;
	}

	public String getFlRecebeEmail() {
		return flRecebeEmail;
	}

	public void setFlRecebeEmail(String flRecebeEmail) {
		this.flRecebeEmail = flRecebeEmail;
	}

	public String getCdGrupoPermProd() {
		return cdGrupoPermProd;
	}

	public void setCdGrupoPermProd(String cdGrupoPermProd) {
		this.cdGrupoPermProd = cdGrupoPermProd;
	}

	public double getVlPctMinDesconto() {
		return vlPctMinDesconto;
	}

	public void setVlPctMinDesconto(double vlPctMinDesconto) {
		this.vlPctMinDesconto = vlPctMinDesconto;
	}

	public String getDsEmail() {
		return dsEmail;
	}

	public void setDsEmail(String dsEmail) {
		this.dsEmail = dsEmail;
	}

	public String getFlOculto() {
		return flOculto;
	}

	public void setFlOculto(String flOculto) {
		this.flOculto = flOculto;
	}

	public double getVlTonFrete() {
		return vlTonFrete;
	}

	public void setVlTonFrete(double vlTonFrete) {
		this.vlTonFrete = vlTonFrete;
	}

	public String getFlConsignaPedido() {
		return flConsignaPedido;
	}

	public void setFlConsignaPedido(String flConsignaPedido) {
		this.flConsignaPedido = flConsignaPedido;
	}

	public double getVlLimiteCreditoConsig() {
		return vlLimiteCreditoConsig;
	}

	public void setVlLimiteCreditoConsig(double vlLimiteCreditoConsig) {
		this.vlLimiteCreditoConsig = vlLimiteCreditoConsig;
	}

	public double getVlPctDevolucaoConsig() {
		return vlPctDevolucaoConsig;
	}

	public void setVlPctDevolucaoConsig(double vlPctDevolucaoConsig) {
		this.vlPctDevolucaoConsig = vlPctDevolucaoConsig;
	}

	public double getVlVendaMensal() {
		return vlVendaMensal;
	}

	public void setVlVendaMensal(double vlVendaMensal) {
		this.vlVendaMensal = vlVendaMensal;
	}

	public String getCdTransportadoraCif() {
		return cdTransportadoraCif;
	}

	public void setCdTransportadoraCif(String cdTransportadoraCif) {
		this.cdTransportadoraCif = cdTransportadoraCif;
	}

	public String getCdTransportadoraFob() {
		return cdTransportadoraFob;
	}

	public void setCdTransportadoraFob(String cdTransportadoraFob) {
		this.cdTransportadoraFob = cdTransportadoraFob;
	}

	public double getVlTicketMedio() {
		return vlTicketMedio;
	}

	public void setVlTicketMedio(double vlTicketMedio) {
		this.vlTicketMedio = vlTicketMedio;
	}

	public String getDtCadastro() {
		return dtCadastro == null ? null : DateUtil.formatDateDDMMYYYY(dtCadastro);
	}

	public void setDtCadastro(Date dtCadastro) {
		this.dtCadastro = dtCadastro;
	}

	public double getVlMaxTitulosAtraso() {
		return vlMaxTitulosAtraso;
	}

	public void setVlMaxTitulosAtraso(double vlMaxTitulosAtraso) {
		this.vlMaxTitulosAtraso = vlMaxTitulosAtraso;
	}

	public int getNuDiasToleranciaAtraso() {
		return nuDiasToleranciaAtraso;
	}

	public void setNuDiasToleranciaAtraso(int nuDiasToleranciaAtraso) {
		this.nuDiasToleranciaAtraso = nuDiasToleranciaAtraso;
	}

	public String getFlBoletoAceite() {
		return flBoletoAceite;
	}

	public void setFlBoletoAceite(String flBoletoAceite) {
		this.flBoletoAceite = flBoletoAceite;
	}
	
}
