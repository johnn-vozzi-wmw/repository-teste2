package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class FormulaCalculo extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPFORMULACALCULO";

    public String cdFormulaCalculo;
    public String dsFormulaCalculo;
    public String flTipoFormulaCalculo;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FormulaCalculo) {
            FormulaCalculo formulaCalculo = (FormulaCalculo) obj;
            return
                ValueUtil.valueEquals(cdFormulaCalculo, formulaCalculo.cdFormulaCalculo) && 
                ValueUtil.valueEquals(dsFormulaCalculo, formulaCalculo.dsFormulaCalculo);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdFormulaCalculo);
        primaryKey.append(";");
        primaryKey.append(dsFormulaCalculo);
        return primaryKey.toString();
    }

}