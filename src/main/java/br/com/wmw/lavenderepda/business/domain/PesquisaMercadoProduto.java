package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;

public class PesquisaMercadoProduto extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPPESQUISAMERCADOPRODUTO";

	public String cdEmpresa;
	public String cdPesquisaMercadoConfig;
	public String cdProduto;

	//não persistentes
	public String dsFiltro;
	public String dsProduto;

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdPesquisaMercadoConfig + ";" + cdProduto;
	}

	@Override
	public String toString() {
		return MessageUtil.getMessage(Messages.PESQUISA_MERCADO_PROD_CONC_PRODUTO_CODIGO_LABEL, new String[] {cdProduto, dsProduto});
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PesquisaMercadoProduto) {
			PesquisaMercadoProduto other = (PesquisaMercadoProduto) obj;
			return ValueUtil.valueEquals(cdEmpresa, other.cdEmpresa)
					&& ValueUtil.valueEquals(cdPesquisaMercadoConfig, other.cdPesquisaMercadoConfig)
					&& ValueUtil.valueEquals(cdProduto, other.cdProduto);
		} else {
			return false;
		}
	}

}
