package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.lavenderepda.business.domain.VenctoPagamentoPedido;
import totalcross.util.Date;

public class VenctoPagamentoPedidoDto {
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String nuPedido;
	public String flOrigemPedido;
	public String cdPagamentoPedido;
	public int nuSeqVenctoPagamentoPedido;
	public Date dtVencimento;
	public double vlBoleto;
	
	public VenctoPagamentoPedidoDto(VenctoPagamentoPedido vencto) {
		try {
			FieldMapper.copy(vencto, this);
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
	
	public String getNuPedido() {
		return nuPedido;
	}
	
	public void setNuPedido(String nuPedido) {
		this.nuPedido = nuPedido;
	}
	
	public String getFlOrigemPedido() {
		return flOrigemPedido;
	}
	
	public void setFlOrigemPedido(String flOrigemPedido) {
		this.flOrigemPedido = flOrigemPedido;
	}
	
	public String getCdPagamentoPedido() {
		return cdPagamentoPedido;
	}
	
	public void setCdPagamentoPedido(String cdPagamentoPedido) {
		this.cdPagamentoPedido = cdPagamentoPedido;
	}
	
	public int getNuSeqVenctoPagamentoPedido() {
		return nuSeqVenctoPagamentoPedido;
	}
	
	public void setNuSeqVenctoPagamentoPedido(int nuSeqVenctoPagamentoPedido) {
		this.nuSeqVenctoPagamentoPedido = nuSeqVenctoPagamentoPedido;
	}
	
	public String getDtVencimento() {
		return dtVencimento == null ? null : DateUtil.formatDateDDMMYYYY(dtVencimento);
	}
	
	public void setDtVencimento(Date dtVencimento) {
		this.dtVencimento = dtVencimento;
	}
	
	public double getVlBoleto() {
		return vlBoleto;
	}
	
	public void setVlBoleto(double vlBoleto) {
		this.vlBoleto = vlBoleto;
	}
	
	

}
