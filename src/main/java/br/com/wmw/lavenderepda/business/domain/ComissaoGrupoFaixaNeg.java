package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ComissaoGrupoFaixaNeg extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPCOMISSAOGRUPOFAIXANEG";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdGrupoProduto1;
	public String cdGrupoProduto2;
	public String cdGrupoProduto3;
	public double vlIndice;
	public double vlPctComissao;
	
	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdGrupoProduto1 + ";" +cdGrupoProduto2 + ";" +cdGrupoProduto3 + ";" +vlIndice;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ComissaoGrupoFaixaNeg) {
			ComissaoGrupoFaixaNeg descontoComissao = (ComissaoGrupoFaixaNeg) obj;
			return ValueUtil.valueEquals(cdEmpresa, descontoComissao.cdEmpresa) &&
					ValueUtil.valueEquals(cdRepresentante, descontoComissao.cdRepresentante) &&
					ValueUtil.valueEquals(cdGrupoProduto1, descontoComissao.cdGrupoProduto1) &&
					ValueUtil.valueEquals(cdGrupoProduto2, descontoComissao.cdGrupoProduto2) &&
					ValueUtil.valueEquals(cdGrupoProduto3, descontoComissao.cdGrupoProduto3) &&
					ValueUtil.valueEquals(vlIndice, descontoComissao.vlIndice) &&
					ValueUtil.valueEquals(vlPctComissao, descontoComissao.vlPctComissao);
		}
		return false;
	}

}
