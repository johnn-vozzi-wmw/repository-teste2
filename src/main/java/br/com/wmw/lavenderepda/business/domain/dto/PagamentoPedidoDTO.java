package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.lavenderepda.business.domain.PagamentoPedido;
import totalcross.util.Date;

public class PagamentoPedidoDTO {

	public String cdEmpresa;
    public String cdRepresentante;
    public String cdPagamentoPedido;
    public String nuPedido;
    public String flOrigemPedido;
    public String cdTipoPagamento;
    public String cdCondicaoPagamento;
    public double vlPagamentoPedido;
    public double vlDesconto;
    public String nuBanco;
    public String nuComplemento;
    public String nuAgencia;
    public String nuConta;
    public String nuCheque;
    public String flChequeTerceiro;
    public String dsEmitente;
    public String dsReferenteCheque;
    public Date dtVencimento;
    
    public PagamentoPedidoDTO() {
		super();
	}
    
    public PagamentoPedidoDTO(PagamentoPedido pagamentoPedido) {
		copy(pagamentoPedido);
	}
	

	public PagamentoPedidoDTO copy(final PagamentoPedido pagamentoPedido) {
		try {
			FieldMapper.copy(pagamentoPedido, this);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return this;
	}

	public String getCdEmpresa() {
		return cdEmpresa;
	}

	public String getCdRepresentante() {
		return cdRepresentante;
	}

	public String getCdPagamentoPedido() {
		return cdPagamentoPedido;
	}

	public String getNuPedido() {
		return nuPedido;
	}

	public String getFlOrigemPedido() {
		return flOrigemPedido;
	}

	public String getCdTipoPagamento() {
		return cdTipoPagamento;
	}

	public String getCdCondicaoPagamento() {
		return cdCondicaoPagamento;
	}

	public double getVlPagamentoPedido() {
		return vlPagamentoPedido;
	}

	public double getVlDesconto() {
		return vlDesconto;
	}

	public String getNuBanco() {
		return nuBanco;
	}

	public String getNuComplemento() {
		return nuComplemento;
	}

	public String getNuAgencia() {
		return nuAgencia;
	}

	public String getNuConta() {
		return nuConta;
	}

	public String getNuCheque() {
		return nuCheque;
	}

	public String getFlChequeTerceiro() {
		return flChequeTerceiro;
	}

	public String getDsEmitente() {
		return dsEmitente;
	}

	public String getDsReferenteCheque() {
		return dsReferenteCheque;
	}

	public String getDtVencimento() {
		return dtVencimento == null ? null : DateUtil.formatDateDDMMYYYY(dtVencimento);
	}
	
}
