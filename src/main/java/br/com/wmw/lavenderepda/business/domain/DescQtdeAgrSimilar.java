package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class DescQtdeAgrSimilar extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPDESCQTDEAGRSIMILAR";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdTabelaPreco;
	public String cdProduto;
	public String cdProdutoSimilar;
	
	//Nao persistente
	public String cdProdutoExcept;
	public boolean desconsideraTabPreco;

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdTabelaPreco + ";" + cdProduto + ";" + cdProdutoSimilar;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DescQtdeAgrSimilar) {
			DescQtdeAgrSimilar desc = (DescQtdeAgrSimilar) obj;
			return ValueUtil.valueEquals(cdEmpresa, desc.cdEmpresa) &&
					ValueUtil.valueEquals(cdRepresentante, desc.cdRepresentante) &&
					ValueUtil.valueEquals(cdTabelaPreco, desc.cdTabelaPreco) &&
					ValueUtil.valueEquals(cdProduto, desc.cdProduto) &&
					ValueUtil.valueEquals(cdProdutoSimilar, desc.cdProdutoSimilar);
		}
		return false;
	}

}
