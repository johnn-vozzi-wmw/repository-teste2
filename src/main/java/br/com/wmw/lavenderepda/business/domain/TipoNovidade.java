package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class TipoNovidade extends BaseDomain {

    public static String TABLE_NAME = "TBLVPTIPONOVIDADE";

	public static final String TIPONOVIDADEPRODUTO_ENTRADA_ESTOQUE = "1";
	public static final String TIPONOVIDADEPRODUTO_AUMENTO_PRECO = "2";
	public static final String TIPONOVIDADEPRODUTO_ALTERACAO_DESCCOMIPROD = "3";
	public static final String TIPONOVIDADEPRODUTO_ALTERACAO_DESCCOMIGRUPO = "4";
	public static final String TIPONOVIDADEPRODUTO_NOVO_PRODUTO = "5";
	public static final String TIPONOVIDADECLIENTE_NOVO_CLIENTE = "6";
	public static final String TIPONOVIDADECLIENTE_NOVO_CLIENTE_NAO_INTEGRADO = "7";
	public static final String TIPONOVIDADENOVOCLIENTE_SEGUNDA_ETAPA = "8";
	public static final String TIPONOVIDADEPRODUTO_QUEDA_PRECO = "10";
	public static final String TIPONOVIDADEPRODUTO_ALTERACAO_PRECO_INTERPOLACAO = "11";
	public static final String TIPONOVIDADESOLAUTORIZACAO_RESPOSTA_AUTORIZACAO = "12";

	public String cdEmpresa;
	public String cdTipoNovidade;
	public String dsTipoNovidade;

	//Não persistente
	public int qtItensTipo;

	//Override
	public boolean equals(Object obj) {
		if (obj instanceof TipoNovidade) {
			TipoNovidade tipoNovidade = (TipoNovidade) obj;
			return
			ValueUtil.valueEquals(cdEmpresa, tipoNovidade.cdEmpresa) &&
			ValueUtil.valueEquals(cdTipoNovidade, tipoNovidade.cdTipoNovidade);
		}
		return false;
	}

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdTipoNovidade);
        return strBuffer.toString();
    }

	public String toString() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(dsTipoNovidade);
    	strBuffer.append(" (");
    	strBuffer.append(qtItensTipo);
    	strBuffer.append(")");
        return strBuffer.toString();
	}

}