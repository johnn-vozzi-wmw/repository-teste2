package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;

public class TipoRanking {
	
	public static final int CDTIPORANKING_ORDENACAO_PADRAO = 0;
	public static final int CDTIPORANKING_EMPRESA = 1;
	public static final int CDTIPORANKING_REPRESENTANTE = 2;
	public static final int CDTIPORANKING_SUPERVISAO = 3;
	public static final int CDTIPORANKING_REGIONAL = 4;
	public static final String DSTIPORANKING_ORDENACAO_PADRAO = "Ordenação Padrão";
	public static final String DSTIPORANKING_EMPRESA = "Empresa";
	public static final String DSTIPORANKING_REPRESENTANTE = "Representante";
	public static final String DSTIPORANKING_SUPERVISAO = "Supervisão";
	public static final String DSTIPORANKING_REGIONAL = "Regional";
	
	public int cdTipoRanking;
	public String dsTipoRanking;
	
	public boolean equals(Object obj) {
        if (obj instanceof TipoRanking) {
        	TipoRanking tipoRanking = (TipoRanking) obj;
            return
                ValueUtil.valueEquals(cdTipoRanking, tipoRanking.cdTipoRanking);
        }
        return false;
    }
	
	//@Override
    public String getPrimaryKey() {
    	return StringUtil.getStringValue(cdTipoRanking);
    }

    //@Override
    public String toString() {
    	return dsTipoRanking;
    }

}
