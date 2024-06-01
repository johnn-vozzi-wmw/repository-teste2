package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class MetaVenda extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPMETAVENDA";

	public static final String CDTIPOMETA_FAMILIA_PRODUTOS = "1";
	public static final String CDTIPOMETA_GERAL = "2";
	public static final String CDVARIAVELMETA_TONELADAS = "1";
	public static final String CDVARIAVELMETA_VALOR = "2";

    public String cdEmpresa;
    public String cdMetaVenda;
    public String dsMetaVenda;
    public String cdTipoMetaVenda;
    public String cdVariavelMetaVenda;
    public Date dtInicialVigencia;
    public Date dtFinalVigencia;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof MetaVenda) {
            MetaVenda metaVenda = (MetaVenda) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, metaVenda.cdEmpresa) &&
                ValueUtil.valueEquals(cdMetaVenda, metaVenda.cdMetaVenda);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdMetaVenda);
        return primaryKey.toString();
    }

    public int getNuDiasUteisPeriodo() {
    	int result = 0;
    	if (ValueUtil.isNotEmpty(dtInicialVigencia)) {
    		Date dtBase = DateUtil.getDateValue(StringUtil.getStringValue(dtInicialVigencia));
    		if (ValueUtil.isNotEmpty(dtBase) && ValueUtil.isNotEmpty(dtFinalVigencia)) {
    			while (!dtBase.isAfter(dtFinalVigencia)) {
    				if ((dtBase.getDayOfWeek() != 0) && (dtBase.getDayOfWeek() != 6)) {
    					result++;
    				}
    				dtBase.advance(1);
    			}
    		}
    	}
    	return result;
    }

    public int getNuDiaUtilHoje() {
    	int result = 0;
    	Date data = DateUtil.getCurrentDate();
    	if (ValueUtil.isNotEmpty(dtInicialVigencia) && ValueUtil.isNotEmpty(data)) {
    		Date dtBase = DateUtil.getDateValue(StringUtil.getStringValue(dtInicialVigencia));
    		if (ValueUtil.isNotEmpty(dtBase) && ValueUtil.isNotEmpty(dtFinalVigencia)) {
    			while (!dtBase.isAfter(dtFinalVigencia)) {
    				if ((dtBase.getDayOfWeek() != 0) && (dtBase.getDayOfWeek() != 6)) {
    					result++;
    				}
    				if (dtBase.equals(data)) {
    					return result;
    				}
    				dtBase.advance(1);
    			}
    		}
    	}
    	return result;
    }

	public String getCdDomain() {
		return cdMetaVenda;
	}

	public String getDsDomain() {
		String dsMeta = StringUtil.getStringValue(dsMetaVenda);
		if (ValueUtil.isNotEmpty(dtInicialVigencia)) {
			dsMeta += " " + StringUtil.getStringValue(dtInicialVigencia) + " - " + StringUtil.getStringValue(dtFinalVigencia);
		}
		return dsMeta;
	}

}