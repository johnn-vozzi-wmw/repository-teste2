package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class Verba extends BaseDomain {

    public static String TABLE_NAME = "TBLVPVERBA";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdVerba;
    public String cdTipoGeracao;
    public String cdContaCorrente;
    public String cdTipoMultiplo;
    public String dsVerba;
    public String flMixObrigatorio;

    public static final int TIPO_GERACAO_VALOR = 1;
    public static final int TIPO_GERACAO_PERCENTUAL = 2;
    public static final int TIPO_GERACAO_DESCDIFERENCIADO = 3;

    public static final int TIPO_MULTIPLO_VALOR = 1;
    public static final int TIPO_MULTIPLO_QUANTIDADE = 0;

    public static final String VERBA_PDA = "P";
	public static final String VERBA_ERP = "E";
	public static final String VERBA_WEB = "W";
	
	public static final String VERBA_NAO_CONSUMIDA = "0";
	public static final String VERBA_GRUPO_PRODUTO = "2";
	public static final String VERBA_COSUMIDA = "4";
	
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Verba) {
            Verba verba = (Verba) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, verba.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, verba.cdRepresentante) &&
                ValueUtil.valueEquals(cdVerba, verba.cdVerba);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdVerba);
        return strBuffer.toString();
    }

}