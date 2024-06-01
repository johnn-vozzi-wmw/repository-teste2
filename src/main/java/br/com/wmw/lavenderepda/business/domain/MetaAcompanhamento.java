package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;
import totalcross.util.Hashtable;

public class MetaAcompanhamento extends BaseDomain {

	public static String TABLE_NAME = "TBLVPMETAACOMPANHAMENTO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String dsPeriodo;
    public Date dtRegistro;
    public double vlRealizadoMeta;
    public double vlRealizadoFlex;
    public int qtPedidosRealizado;
    public int nuCarimbo;
    // Não persistente
    public boolean showLineMetaVenda = true;
    public boolean showLineRealVenda = true;
    public boolean showLineMetaFlex = true;
    public boolean showLineRealFlex = true;
    public boolean showLinePrevisaoVenda = true;
    public boolean showLinePrevisaoFlex = true;

    public MetaAcompanhamento() {
    	
    }

    public MetaAcompanhamento(Meta meta, Date dtRegistro) {
		this.cdEmpresa = meta.cdEmpresa;
		this.cdRepresentante = meta.cdRepresentante;
		this.dsPeriodo = meta.dsPeriodo;
		this.dtRegistro = dtRegistro;
	}

	//Override
    public boolean equals(Object obj) {
        if (obj instanceof MetaAcompanhamento) {
            MetaAcompanhamento metaAcompanhamento = (MetaAcompanhamento) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, metaAcompanhamento.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, metaAcompanhamento.cdRepresentante) &&
                ValueUtil.valueEquals(dsPeriodo, metaAcompanhamento.dsPeriodo) &&
                ValueUtil.valueEquals(dtRegistro, metaAcompanhamento.dtRegistro);
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
        primaryKey.append(dsPeriodo);
        primaryKey.append(";");
        primaryKey.append(dtRegistro);
        return primaryKey.toString();
    }

	public String prepareValues() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("showLineMetaVenda").append(UiUtil.defaultSeparatorPairValue).append(StringUtil.getStringValue(showLineMetaVenda)).append(UiUtil.defaultSeparatorValue);
		stringBuffer.append("showLineRealVenda").append(UiUtil.defaultSeparatorPairValue).append(StringUtil.getStringValue(showLineRealVenda)).append(UiUtil.defaultSeparatorValue);
		stringBuffer.append("showLineMetaFlex").append(UiUtil.defaultSeparatorPairValue).append(StringUtil.getStringValue(showLineMetaFlex)).append(UiUtil.defaultSeparatorValue);
		stringBuffer.append("showLineRealFlex").append(UiUtil.defaultSeparatorPairValue).append(StringUtil.getStringValue(showLineRealFlex)).append(UiUtil.defaultSeparatorValue);
		stringBuffer.append("showLinePrevisaoVenda").append(UiUtil.defaultSeparatorPairValue).append(StringUtil.getStringValue(showLinePrevisaoVenda)).append(UiUtil.defaultSeparatorValue);
		stringBuffer.append("showLinePrevisaoFlex").append(UiUtil.defaultSeparatorPairValue).append(StringUtil.getStringValue(showLinePrevisaoFlex)).append(UiUtil.defaultSeparatorValue);
		//--
		return stringBuffer.toString();
	}

	public void splitPairValueAndSetAtributes(String value) {
		Hashtable hash = StringUtil.splitPairValue(value);
		//--
		showLineMetaVenda = ValueUtil.getBooleanValue((String) hash.get("showLineMetaVenda"));
		showLineRealVenda = ValueUtil.getBooleanValue((String) hash.get("showLineRealVenda"));
		showLineMetaFlex = ValueUtil.getBooleanValue((String) hash.get("showLineMetaFlex"));
		showLineRealFlex = ValueUtil.getBooleanValue((String) hash.get("showLineRealFlex"));
		showLinePrevisaoVenda = ValueUtil.getBooleanValue((String) hash.get("showLinePrevisaoVenda"));
		showLinePrevisaoFlex = ValueUtil.getBooleanValue((String) hash.get("showLinePrevisaoFlex"));
	}

}