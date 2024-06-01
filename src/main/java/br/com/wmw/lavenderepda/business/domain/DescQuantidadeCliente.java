package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class DescQuantidadeCliente extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPDESCQUANTIDADECLIENTE";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdTabelaPreco;
	public String cdProduto;
	public String cdCliente;

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdTabelaPreco + ";" + cdProduto + ";" + cdCliente;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DescQuantidadeCliente) {
			DescQuantidadeCliente desc = (DescQuantidadeCliente) obj;
			return ValueUtil.valueEquals(cdEmpresa, desc.cdEmpresa) &&
					ValueUtil.valueEquals(cdRepresentante, desc.cdRepresentante) &&
					ValueUtil.valueEquals(cdTabelaPreco, desc.cdTabelaPreco) &&
					ValueUtil.valueEquals(cdProduto, desc.cdProduto) &&
					ValueUtil.valueEquals(cdCliente, desc.cdCliente);
		}
		return false;
	}

}
