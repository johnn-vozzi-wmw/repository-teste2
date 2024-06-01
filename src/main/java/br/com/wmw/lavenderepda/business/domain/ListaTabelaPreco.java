package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ListaTabelaPreco extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPLISTATABELAPRECO";
	public static final String NOME_COLUNA_DSLISTATABELAPRECO = "DSLISTATABELAPRECO";

	public String cdEmpresa;
	public String cdRepresentante;
	public int cdListaTabelaPreco;
	public String dsListaTabelaPreco;
	
	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdListaTabelaPreco;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ListaTabelaPreco) {
			ListaTabelaPreco listaTabelaPreco = (ListaTabelaPreco) obj;
			return ValueUtil.valueEquals(cdEmpresa, listaTabelaPreco.cdEmpresa) &&
					ValueUtil.valueEquals(cdRepresentante, listaTabelaPreco.cdRepresentante) &&
					ValueUtil.valueEquals(cdListaTabelaPreco, listaTabelaPreco.cdListaTabelaPreco);
		}
		return false;
	}

}
