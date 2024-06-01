package br.com.wmw.lavenderepda.business.domain;

import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;

public class ItemGrade extends BaseDomain {

	public static String TABLE_NAME = "TBLVPITEMGRADE";
	public static final String GRADE1 = "1";
	public static final String GRADE2 = "2";
	public static final String NMCOLUMN_QTITEMGRADE = "QTITEMGRADE";
	public static final String NMCOLUMN_QTESTOQUEITEM = "QTESTOQUEITEM";
	public static int GRADE_NIVEL_1 = 1;
	public static int GRADE_NIVEL_2 = 2;
	public static int ITEMGRADELIST_PROBLEMA_EM_TODAS_AS_GRADES = 0;
	public static int ITEMGRADELIST_PROBLEMA_EM_ALGUMAS_GRADES = 1;
	public static int ITEMGRADELIST_SEM_PROBLEMA_COM_GRADES = 2;

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTipoItemGrade;
    public String cdItemGrade;
    public String dsItemGrade;
    public int nuSequencia;
    
    //Nao persistente
    public double qtItemGrade;
    public double qtItemGradeErp;
    public List<String> cdItemGrade1ForValidation;
    
    public String cdItemGrade1;
    public String cdItemGrade2;
    public String cdItemGrade3;
    public String dsItemGrade1;
    public String dsItemGrade2;
    public String dsItemGrade3;
    public int nuOrdemLinha;
    public int nuOrdemColuna;
    
    public double qtEstoqueItem;
    public static String sortAttr;
    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemGrade) {
            ItemGrade itemGrade = (ItemGrade) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, itemGrade.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, itemGrade.cdRepresentante) &&
                ValueUtil.valueEquals(cdTipoItemGrade, itemGrade.cdTipoItemGrade) &&
                ValueUtil.valueEquals(cdItemGrade, itemGrade.cdItemGrade);
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
        primaryKey.append(cdTipoItemGrade);
        primaryKey.append(";");
        primaryKey.append(cdItemGrade);
        return primaryKey.toString();
    }

    public String toString() {
    	return dsItemGrade != null ? dsItemGrade : Messages.ITEMGRADE_SEM_DESCRICAO;
    }
    
    private int getNuOrdemLinhaOuColuna() {
    	if (isTipoItemGrade2()) {
    		return nuOrdemLinha;
    	} else if (isTipoItemGrade3()) {
    		return nuOrdemColuna;
    	}
    	return 0;
    }
    
    public boolean isTipoItemGrade1() {
    	return isTipoItemGrade(TipoItemGrade.GRADE_1);
    }
    
    public boolean isTipoItemGrade2() {
    	return isTipoItemGrade(TipoItemGrade.GRADE_2);
    }
    
    public boolean isTipoItemGrade3() {
    	return isTipoItemGrade(TipoItemGrade.GRADE_3);
    }
    
    public boolean isTipoItemGrade(String cdTipoItemGrade) {
    	return ValueUtil.valueEqualsIfNotNull(this.cdTipoItemGrade, cdTipoItemGrade);
    }
    
    @Override
    public int getSortIntValue() {
    	if (LavenderePdaConfig.usaGradeProduto5()) {
    		return getNuOrdemLinhaOuColuna();
    	} else {
    		if (LavenderePdaConfig.usaOrdenacaoNuSequenciaGradeProduto) {
    			return nuSequencia;
    		}
    	}
    	return super.getSortIntValue();
    }
    
    @Override
	public double getSortDoubleValue() {
    	if (NMCOLUMN_QTITEMGRADE.equals(sortAttr)) {
    		return (qtItemGrade == 0) ? -Double.MAX_VALUE : qtItemGrade;
    	}
		return qtEstoqueItem;
	}
    
    @Override
    public String getSortStringValue() {
    	return dsItemGrade;
    }

}