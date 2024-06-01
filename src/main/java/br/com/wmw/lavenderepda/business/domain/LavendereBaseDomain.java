package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;

public abstract class LavendereBaseDomain extends BaseDomain {
	
	public static final String NMCOLUNA_CDEMPRESA = "CDEMPRESA";
	public static final String NMCOLUNA_CDREPRESENTANTE= "CDREPRESENTANTE";
	public static final String NMCOLUNA_CDUSUARIO= "CDUSUARIO";
	
	public abstract String getCdDomain();
	public abstract String getDsDomain();

	public String toString() {
		if (LavenderePdaConfig.usaDescricaoCodigoNaVisualizacaoEntidades && ValueUtil.isNotEmpty(getDsDomain()) && !getDsDomain().equals(getCdDomain())) {
	    	StringBuffer strBuffer = new StringBuffer();
	    	strBuffer.append(getDsDomain());
	    	strBuffer.append(" [");
	    	strBuffer.append(getCdDomain());
	    	strBuffer.append("]");
	    	return strBuffer.toString();
		} else {
			return ValueUtil.isEmpty(getDsDomain()) ? StringUtil.getStringValue(getCdDomain()) : getDsDomain();
		}
	}
}
