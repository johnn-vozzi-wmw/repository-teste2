package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ColunaTabelaPreco extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPCOLUNATABELAPRECO";
	public static final String NOME_COLUNA_DSCOLUNATABELAPRECO = "DSCOLUNATABELAPRECO";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public int cdColunaTabelaPreco;
	public String dsColunaTabelaPreco;

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdColunaTabelaPreco;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ColunaTabelaPreco) {
			ColunaTabelaPreco colunaTabelaPreco = (ColunaTabelaPreco) obj;
			return ValueUtil.valueEquals(cdEmpresa, colunaTabelaPreco.cdEmpresa) &&
					ValueUtil.valueEquals(cdRepresentante, colunaTabelaPreco.cdRepresentante) &&
					ValueUtil.valueEquals(cdColunaTabelaPreco, colunaTabelaPreco.cdColunaTabelaPreco);
		}
		return false;
	}

}
