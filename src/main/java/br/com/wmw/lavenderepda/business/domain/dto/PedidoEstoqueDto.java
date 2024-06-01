package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.DateUtil;
import totalcross.util.Date;
import totalcross.util.Vector;

public class PedidoEstoqueDto {
	
	public static final String TABLE_NAME = "TBLVPPEDIDOESTOQUE";
	
	public static final String TIPOREMESSA_R = "R";
	public static final String TIPOREMESSA_EMPRESA = "E";
	public static final String TIPOREMESSA_DEVOLUCAO = "D";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdPedidoEstoque;
	public String flTipoRemessa;
	public Date dtPedidoEstoque;
	public String hrPedidoEstoque;
	
	//Nao persistente
	public Vector itemPedidoEstoqueList;
	
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

	public String getCdPedidoEstoque() {
		return cdPedidoEstoque;
	}

	public void setCdPedidoEstoque(String cdPedidoEstoque) {
		this.cdPedidoEstoque = cdPedidoEstoque;
	}

	public String getFlTipoRemessa() {
		return flTipoRemessa;
	}

	public void setFlTipoRemessa(String flTipoRemessa) {
		this.flTipoRemessa = flTipoRemessa;
	}

	public String getDtPedidoEstoque() {
		return dtPedidoEstoque == null ? null : DateUtil.formatDateDDMMYYYY(dtPedidoEstoque);
	}

	public void setDtPedidoEstoque(Date dtPedidoEstoque) {
		this.dtPedidoEstoque = dtPedidoEstoque;
	}

	public String getHrPedidoEstoque() {
		return hrPedidoEstoque;
	}

	public void setHrPedidoEstoque(String hrPedidoEstoque) {
		this.hrPedidoEstoque = hrPedidoEstoque;
	}

}
