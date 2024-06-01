package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class FornecedorRep extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPFORNECEDORREP";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdFornecedor;

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdFornecedor;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FornecedorRep) {
			FornecedorRep fornecedorRep = (FornecedorRep)obj;
			return ValueUtil.valueEquals(cdEmpresa, fornecedorRep.cdEmpresa) &&
					ValueUtil.valueEquals(cdRepresentante, fornecedorRep.cdRepresentante) &&
					ValueUtil.valueEquals(cdFornecedor, fornecedorRep.cdFornecedor);
		}
		return false;
	}

}
