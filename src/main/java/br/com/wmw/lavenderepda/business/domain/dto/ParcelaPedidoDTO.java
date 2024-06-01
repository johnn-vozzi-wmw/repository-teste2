package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.lavenderepda.business.domain.ParcelaPedido;
import totalcross.util.Date;

public class ParcelaPedidoDTO {

	public String cdEmpresa;
	public String cdRepresentante;
	public String flOrigemPedido;
	public String nuPedido;
	public String cdParcela;
	public double vlParcela;
	public Date dtVencimento;
	public int qtDiasPrazo;
	public double vlPctParcela;
	    	
	public ParcelaPedidoDTO() {
		super();
	}

	public ParcelaPedidoDTO copy(final ParcelaPedido parcelaPedido) {
		try {
			FieldMapper.copy(parcelaPedido, this);
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

	public String getFlOrigemPedido() {
		return flOrigemPedido;
	}

	public String getNuPedido() {
		return nuPedido;
	}

	public String getCdParcela() {
		return cdParcela;
	}

	public double getVlParcela() {
		return vlParcela;
	}

	public String getDtVencimento() {
		return dtVencimento == null ? null : DateUtil.formatDateDDMMYYYY(dtVencimento);
	}
	
	public int getQtDiasPrazo() {
		return qtDiasPrazo;
	}

	public double getVlPctParcela() {
		return vlPctParcela;
	}
	
}
