package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;

public class TipoPedProdDescAcres extends BaseDomain {

	public static String TABLE_NAME = "TBLVPTIPOPEDPRODDESCACRES";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdTipoPedido;
	public String cdProduto;
	public double vlPctMaxDesconto;
	public double vlPctMaxAcrescimo;
	public double vlBase;

	public TipoPedProdDescAcres() {}

	public boolean isVlPctMaxDescontoValido() {
		return vlPctMaxDesconto >= 0 && vlPctMaxDesconto <= 100;
	}

	public boolean isVlPctMaxAcrescimoValido() {
		return vlPctMaxAcrescimo >= 0 && vlPctMaxAcrescimo <= 100;
	}

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdEmpresa);
		primaryKey.append(";");
		primaryKey.append(cdRepresentante);
		primaryKey.append(";");
		primaryKey.append(cdTipoPedido);
		primaryKey.append(";");
		primaryKey.append(cdProduto);
		return primaryKey.toString();
	}
}
