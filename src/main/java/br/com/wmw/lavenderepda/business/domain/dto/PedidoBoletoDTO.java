package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.lavenderepda.business.domain.PedidoBoleto;
import totalcross.util.BigDecimal;
import totalcross.util.Date;

public class PedidoBoletoDTO {
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String flOrigemPedido;
	public String nuPedido;
	public int nuSequenciaBoletoPedido;
	public String cdPagamentoPedido;
	public String cdBoletoConfig;
	public String cdBarras;
	public String dsLinhasDigitavel;
	public Date dtVencimento;
	public String nuAgenciaCodigoCedente;
	public Date dtDocumento;
	public BigDecimal nuDocumento;
	public double vlBoleto;
	public String nuNossoNumero;
	public String dsLocalPagamento;
	public String nuCarteira;
	public String dsEspecieDocumento;
	public String dsObsCedente;
	public String flAceite;
	public String dsEspecie;
	public Date dtProcessamento;
	public String flTipoAlteracao;

	public PedidoBoletoDTO() {
	}
	
	public PedidoBoletoDTO(PedidoBoleto pedidoBoleto) {
		try {
			FieldMapper.copy(pedidoBoleto, this);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
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
	
	public String getFlOrigemPedido() {
		return flOrigemPedido;
	}
	
	public void setFlOrigemPedido(String flOrigemPedido) {
		this.flOrigemPedido = flOrigemPedido;
	}
	
	public String getNuPedido() {
		return nuPedido;
	}
	
	public void setNuPedido(String nuPedido) {
		this.nuPedido = nuPedido;
	}
	
	public int getNuSequenciaBoletoPedido() {
		return nuSequenciaBoletoPedido;
	}
	
	public void setNuSequenciaBoletoPedido(int nuSequenciaBoletoPedido) {
		this.nuSequenciaBoletoPedido = nuSequenciaBoletoPedido;
	}
	
	public String getCdBoletoConfig() {
		return cdBoletoConfig;
	}
	
	public void setCdBoletoConfig(String cdBoletoConfig) {
		this.cdBoletoConfig = cdBoletoConfig;
	}
	
	public String getCdBarras() {
		return cdBarras;
	}
	
	public void setCdBarras(String cdBarras) {
		this.cdBarras = cdBarras;
	}
	
	public String getDsLinhasDigitavel() {
		return dsLinhasDigitavel;
	}
	
	public void setDsLinhasDigitavel(String dsLinhasDigitavel) {
		this.dsLinhasDigitavel = dsLinhasDigitavel;
	}
	
	public String getDtVencimento() {
		return dtVencimento == null ? null : DateUtil.formatDateDDMMYYYY(dtVencimento);
	}
	
	public void setDtVencimento(Date dtVencimento) {
		this.dtVencimento = dtVencimento;
	}
	
	public String getNuAgenciaCodigoCedente() {
		return nuAgenciaCodigoCedente;
	}
	
	public void setNuAgenciaCodigoCedente(String nuAgenciaCodigoCedente) {
		this.nuAgenciaCodigoCedente = nuAgenciaCodigoCedente;
	}
	
	public String getDtDocumento() {
		return dtDocumento == null ? null : DateUtil.formatDateDDMMYYYY(dtDocumento);
	}
	
	public void setDtDocumento(Date dtDocumento) {
		this.dtDocumento = dtDocumento;
	}
	
	public String getNuDocumento() {
		return String.valueOf(nuDocumento);
	}
	
	public void setNuDocumento(int nuDocumento) {
		this.nuDocumento = new BigDecimal(nuDocumento);
	}
	
	public double getVlBoleto() {
		return vlBoleto;
	}
	
	public void setVlBoleto(double vlBoleto) {
		this.vlBoleto = vlBoleto;
	}
	
	public String getNuNossoNumero() {
		return nuNossoNumero;
	}
	
	public void setNuNossoNumero(String nuNossoNumero) {
		this.nuNossoNumero = nuNossoNumero;
	}
	
	public String getDsLocalPagamento() {
		return dsLocalPagamento;
	}
	
	public void setDsLocalPagamento(String dsLocalPagamento) {
		this.dsLocalPagamento = dsLocalPagamento;
	}
	
	public String getNuCarteira() {
		return nuCarteira;
	}
	
	public void setNuCarteira(String nuCarteira) {
		this.nuCarteira = nuCarteira;
	}
	
	public String getDsEspecieDocumento() {
		return dsEspecieDocumento;
	}
	
	public void setDsEspecieDocumento(String dsEspecieDocumento) {
		this.dsEspecieDocumento = dsEspecieDocumento;
	}
	
	public String getDsObsCedente() {
		return dsObsCedente;
	}
	
	public void setDsObsCedente(String dsObsCedente) {
		this.dsObsCedente = dsObsCedente;
	}
	
	public String getFlAceite() {
		return flAceite;
	}
	
	public void setFlAceite(String flAceite) {
		this.flAceite = flAceite;
	}
	
	public String getDsEspecie() {
		return dsEspecie;
	}
	
	public void setDsEspecie(String dsEspecie) {
		this.dsEspecie = dsEspecie;
	}
	
	public String getDtProcessamento() {
		return dtProcessamento == null ? null : DateUtil.formatDateDDMMYYYY(dtProcessamento);
	}
	
	public void setDtProcessamento(Date dtProcessamento) {
		this.dtProcessamento = dtProcessamento;
	}
	
	public String getFlTipoAltercao() {
		return flTipoAlteracao;
	}
	
	public void getFlTipoAltercao(String flTipoAlteracao) {
		this.flTipoAlteracao = flTipoAlteracao;
	}

	public String getCdPagamentoPedido() {
		return cdPagamentoPedido;
	}

	public void setCdPagamentoPedido(String cdPagamentoPedido) {
		this.cdPagamentoPedido = cdPagamentoPedido;
	}
	
}
