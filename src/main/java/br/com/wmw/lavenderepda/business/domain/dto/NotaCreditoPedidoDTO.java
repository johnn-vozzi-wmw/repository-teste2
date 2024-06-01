package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.lavenderepda.business.domain.NotaCreditoPedido;

public class NotaCreditoPedidoDTO {

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemPedido;
    public String nuPedido;
    public String cdNotaCredito;
	
	public NotaCreditoPedidoDTO() {
	}
	
	
	public NotaCreditoPedidoDTO(NotaCreditoPedido notaCreditoPedido) {
		copy(notaCreditoPedido);
	}
	
	public NotaCreditoPedidoDTO copy(final NotaCreditoPedido notaCreditoPedido) {
		try {
			FieldMapper.copy(notaCreditoPedido, this);
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
	
	public String getCdNotaCredito() {
		return cdNotaCredito;
	}


	public void setCdNotaCredito(String cdNotaCredito) {
		this.cdNotaCredito = cdNotaCredito;
	}
	
	
	


	

	

}
