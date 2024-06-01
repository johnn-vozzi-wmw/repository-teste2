package br.com.wmw.lavenderepda.business.domain.dto;

public class RetornoPedidoDTO {
	
	private PedidoDTO pedidoErp;
	private PedidoErpDifDTO pedidoErpDif;
	private NfeDTO nfe;
	private NfceDTO nfce;
	private PedidoBoletoDTO[] pedidoBoletoList;
	private String erroRetorno;
	private boolean retornoWs;
	
	public PedidoDTO getPedidoErp() {
		return pedidoErp;
	}
	
	public void setPedidoErp(PedidoDTO pedidoErp) {
		this.pedidoErp = pedidoErp;
	}
	
	public NfeDTO getNfe() {
		return nfe;
	}
	
	public void setNfe(NfeDTO nfe) {
		this.nfe = nfe;
	}
	
	public PedidoBoletoDTO[] getPedidoBoletoList() {
		return pedidoBoletoList;
	}
	
	public void setPedidoBoletoList(PedidoBoletoDTO[] pedidoBoletoList) {
		this.pedidoBoletoList = pedidoBoletoList;
	}

	public PedidoErpDifDTO getPedidoErpDif() {
		return pedidoErpDif;
	}

	public void setPedidoErpDif(PedidoErpDifDTO pedidoErpDif) {
		this.pedidoErpDif = pedidoErpDif;
	}

	public String getErroRetorno() {
		return erroRetorno;
	}

	public void setErroRetorno(String erroRetorno) {
		this.erroRetorno = erroRetorno;
	}

	public boolean isRetornoWs() {
		return retornoWs;
	}

	public void setRetornoWs(boolean retornoWs) {
		this.retornoWs = retornoWs;
	}
	

	public NfceDTO getNfce() {
		return nfce;
	}

	public void setNfce(NfceDTO nfce) {
		this.nfce = nfce;
	}

}
