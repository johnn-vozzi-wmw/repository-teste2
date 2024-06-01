package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class TipoFreteTabPreco extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPTIPOFRETETABPRECO";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdTipoFrete;
	public String cdTabelaPreco;
	public String cdProduto;
	public double qtPeso;
	public String cdUnidade;
	public double vlPctFrete;
    public double vlPctCredito;
    public double vlFrete;

    // Não persistente
    public double qtPesoPedido;

	public TipoFreteTabPreco(double qtPesoPedido) {
		this.qtPesoPedido = qtPesoPedido;
	}
	
	public TipoFreteTabPreco() {
	}
	
	//Override
	public boolean equals(Object obj) {
		if (obj instanceof TipoFreteTabPreco) {
			TipoFreteTabPreco tipoFreteTabPreco = (TipoFreteTabPreco) obj;
			return ValueUtil.valueEquals(cdEmpresa, tipoFreteTabPreco.cdEmpresa) &&
					ValueUtil.valueEquals(cdRepresentante, tipoFreteTabPreco.cdRepresentante) &&
					ValueUtil.valueEquals(cdTipoFrete, tipoFreteTabPreco.cdTipoFrete) &&
					ValueUtil.valueEquals(qtPeso, tipoFreteTabPreco.qtPeso) &&
					ValueUtil.valueEquals(cdTabelaPreco, tipoFreteTabPreco.cdTabelaPreco) &&
					ValueUtil.valueEquals(cdProduto, tipoFreteTabPreco.cdProduto) &&
					ValueUtil.valueEquals(cdUnidade, tipoFreteTabPreco.cdUnidade);
		}
		return false;
	}

	//@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdEmpresa);
		primaryKey.append(";");
		primaryKey.append(cdRepresentante);
		primaryKey.append(";");
		primaryKey.append(cdTipoFrete);
		primaryKey.append(";");
		primaryKey.append(cdTabelaPreco);
		primaryKey.append(";");
		primaryKey.append(cdProduto);
		primaryKey.append(";");
		primaryKey.append(qtPeso);
		primaryKey.append(";");
		primaryKey.append(cdUnidade);
		return primaryKey.toString();
	}

}