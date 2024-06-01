package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ComiTabPrecoGrupo extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPCOMITABPRECOGRUPO";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdTabelaPreco;
	public String cdGrupoProduto1;
	public double vlPctComissao;
	
	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdTabelaPreco + ";" + cdGrupoProduto1 + ";";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ComiTabPrecoGrupo) {
			ComiTabPrecoGrupo comiTabPrecoGrupo = (ComiTabPrecoGrupo) obj;
			return ValueUtil.valueEquals(cdEmpresa, comiTabPrecoGrupo.cdEmpresa) &&
					ValueUtil.valueEquals(cdRepresentante, comiTabPrecoGrupo.cdRepresentante) &&
					ValueUtil.valueEquals(cdTabelaPreco, comiTabPrecoGrupo.cdTabelaPreco) &&
					ValueUtil.valueEquals(cdGrupoProduto1, comiTabPrecoGrupo.cdGrupoProduto1);
		}
		return super.equals(obj);
	}

}
