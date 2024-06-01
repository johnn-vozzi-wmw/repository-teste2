package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class DescontoComissao extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPDESCONTOCOMISSAO";
	public static final String NMCOLUNA_VLFAIXADESCONTO = "VLFAIXADESCONTO"; 

	public String cdEmpresa;
	public String cdRepresentante;
	public double vlFaixaDesconto;
	public double vlPctComissao;
	public int qtItem;
	public double vlPctAplicado;
	public String flAplicaIndiceDesconto;
	public double vlIndiceDesconto;
	public double vlPctDescontoMin;
	public double vlPctComissaoMin;
	
	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + vlFaixaDesconto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DescontoComissao) {
			DescontoComissao descontoComissao = (DescontoComissao) obj;
			return ValueUtil.valueEquals(cdEmpresa, descontoComissao.cdEmpresa) &&
					ValueUtil.valueEquals(cdRepresentante, descontoComissao.cdRepresentante) &&
					ValueUtil.valueEquals(vlFaixaDesconto, vlFaixaDesconto);
		}
		return false;
	}
	
	public boolean isAplicaIndiceDesconto() {
		return ValueUtil.getBooleanValue(flAplicaIndiceDesconto);
	}

}
