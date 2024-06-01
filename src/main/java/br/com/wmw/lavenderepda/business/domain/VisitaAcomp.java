package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;
import totalcross.util.Hashtable;

public class VisitaAcomp extends BaseDomain {

	public static String TABLE_NAME = "TBLVPVISITAACOMP";

    public String cdEmpresa;
    public String cdRepresentante;
    public Date dtRegistro;
    public int qtVisitasPositivas;
    public int qtVisitasNegativas;
    public int nuCarimbo;
 // Não persistente
    public boolean showLineTotalDeAgendas = true;
    public boolean showLineRealizadas = true;
    public boolean showLinePositivadas = true;
    public boolean showLineNegativadas = true;
    public boolean showLinesPrevisao = true;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof VisitaAcomp) {
            VisitaAcomp visitaAcomp = (VisitaAcomp) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, visitaAcomp.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, visitaAcomp.cdRepresentante) &&
                ValueUtil.valueEquals(dtRegistro, visitaAcomp.dtRegistro);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(dtRegistro);
        return primaryKey.toString();
    }

    public String prepareValues() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("showLineTotalDeAgendas").append(UiUtil.defaultSeparatorPairValue).append(StringUtil.getStringValue(showLineTotalDeAgendas)).append(UiUtil.defaultSeparatorValue);
		stringBuffer.append("showLineRealizadas").append(UiUtil.defaultSeparatorPairValue).append(StringUtil.getStringValue(showLineRealizadas)).append(UiUtil.defaultSeparatorValue);
		stringBuffer.append("showLinePositivadas").append(UiUtil.defaultSeparatorPairValue).append(StringUtil.getStringValue(showLinePositivadas)).append(UiUtil.defaultSeparatorValue);
		stringBuffer.append("showLineNegativadas").append(UiUtil.defaultSeparatorPairValue).append(StringUtil.getStringValue(showLineNegativadas)).append(UiUtil.defaultSeparatorValue);
		stringBuffer.append("showLinesPrevisao").append(UiUtil.defaultSeparatorPairValue).append(StringUtil.getStringValue(showLinesPrevisao)).append(UiUtil.defaultSeparatorValue);
		//--
		return stringBuffer.toString();
	}

    public void splitPairValueAndSetAtributes(String value) {
		Hashtable hash = StringUtil.splitPairValue(value);
		//--
		showLineTotalDeAgendas = ValueUtil.getBooleanValue((String) hash.get("showLineTotalDeAgendas"));
		showLineRealizadas = ValueUtil.getBooleanValue((String) hash.get("showLineRealizadas"));
		showLinePositivadas = ValueUtil.getBooleanValue((String) hash.get("showLinePositivadas"));
		showLineNegativadas = ValueUtil.getBooleanValue((String) hash.get("showLineNegativadas"));
		showLinesPrevisao = ValueUtil.getBooleanValue((String) hash.get("showLinesPrevisao"));
	}

}