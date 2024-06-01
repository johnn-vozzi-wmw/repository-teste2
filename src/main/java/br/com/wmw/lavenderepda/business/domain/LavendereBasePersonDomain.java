package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;

public abstract class LavendereBasePersonDomain extends BasePersonDomain {

	public LavendereBasePersonDomain(String dsTabela) {
		super(dsTabela);
	}

	public abstract String getCdDomain();
	public abstract String getDsDomain();

	public String toString() {
		if (LavenderePdaConfig.usaDescricaoCodigoNaVisualizacaoEntidades && !ValueUtil.valueEquals(getCdDomain(), getDsDomain())) {
	    	StringBuilder strBuffer = new StringBuilder();
	    	if (getDsDomain() != null) {
	    		strBuffer.append(getDsDomain());
	    	}
	    	if (ValueUtil.isNotEmpty(getCdDomain())) {
	    		strBuffer.append(" [");
	    		strBuffer.append(getCdDomain());
	    		strBuffer.append("]");
	    	}
	    	return strBuffer.toString();
		} else {
			return getDsDomain();
		}
	}
}
