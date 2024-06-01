package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class SerieNfe extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPSERIENFE";
	public static final int MAX_PROX_NUMERO = 999999999;
	public static final String COLUNA_NUSERIENFE = "nuSerieNfe";
	public static final String COLUNA_NUPROXIMONUMERO = "nuProximoNumero";
	public static final int DIVISOR_DIGITO_VERIFICADOR = 11;
	public static final int SIZE_SERIENFE = 3;
	public static final int SIZE_NUNFE = 9;
	public static final int SIZE_RANDOM_NUMBER = 8;
	public static final int POS_DTEMISSAO = 2;
	public static final int POS_CNPJ = 6;
	public static final int POS_TIPO_NOTA = 20;
	public static final int POS_TIPO_EMISSAO = 34;
	public static final int POS_RANDOM_NUMBER = 35;
	
	public String cdEmpresa;
	public String cdRepresentante;
	public int nuSerieNfe;
	public String cdTipoNota;
	public int nuProximoNumero;
	public int nuProximoNumeroOrg;
	public String cdUsuarioAlteracao;
	public String flExclusivaCont;
	
	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + nuSerieNfe + ";" + cdTipoNota;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SerieNfe) {
			SerieNfe serieNfe = (SerieNfe) obj;
			return ValueUtil.valueEquals(cdEmpresa, serieNfe.cdEmpresa) &&
					ValueUtil.valueEquals(cdRepresentante, serieNfe.cdRepresentante) &&
					ValueUtil.valueEquals(nuSerieNfe, serieNfe.nuSerieNfe) &&
					ValueUtil.valueEquals(cdTipoNota, serieNfe.cdTipoNota);
		}
		return false;
	}

}
