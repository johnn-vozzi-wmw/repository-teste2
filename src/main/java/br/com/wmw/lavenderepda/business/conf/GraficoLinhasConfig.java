package br.com.wmw.lavenderepda.business.conf;

import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Hashtable;


public class GraficoLinhasConfig {

	public boolean showPoints;
	public int pointSize;
	public int lineSize;

	public String prepareValues() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("showPoints").append(UiUtil.defaultSeparatorPairValue).append(StringUtil.getStringValue(showPoints)).append(UiUtil.defaultSeparatorValue);
		stringBuffer.append("pointSize").append(UiUtil.defaultSeparatorPairValue).append(pointSize).append(UiUtil.defaultSeparatorValue);
		stringBuffer.append("lineSize").append(UiUtil.defaultSeparatorPairValue).append(lineSize);
		//--
		return stringBuffer.toString();
	}

	public void splitPairValueAndSetAtributes(String value) {
		Hashtable hash = StringUtil.splitPairValue(value);
		//--
		showPoints = ValueUtil.getBooleanValue((String) hash.get("showPoints"));
		pointSize = ValueUtil.getIntegerValue((String) hash.get("pointSize"));
		lineSize = ValueUtil.getIntegerValue((String) hash.get("lineSize"));
	}
}