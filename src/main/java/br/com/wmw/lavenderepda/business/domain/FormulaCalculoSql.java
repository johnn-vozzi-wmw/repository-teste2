package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class FormulaCalculoSql extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPFORMULACALCULOSQL";
	
	public static final String FLTIPOSISTEMA_PDA = "P";
	public static final String FLTIPOSISTEMA_WEB = "W";
	
    public String cdFormulaCalculo;
    public String cdFormulaCalculoSql;
    public String dsSql;
    public String flTipoFormulaCalculo;
    public String flTipoSistema;
    public int nuOrdem;
    
    public FormulaCalculoSql() {}
    
    public FormulaCalculoSql(String cdFormulaCalculo, String flTipoSistema) {
		this.cdFormulaCalculo = cdFormulaCalculo;
		this.flTipoSistema = flTipoSistema;
	}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FormulaCalculoSql) {
            FormulaCalculoSql formulaCalculoSql = (FormulaCalculoSql) obj;
            return
                ValueUtil.valueEquals(cdFormulaCalculo, formulaCalculoSql.cdFormulaCalculo) && 
                ValueUtil.valueEquals(cdFormulaCalculoSql, formulaCalculoSql.cdFormulaCalculoSql) &&
            	ValueUtil.valueEquals(flTipoSistema, formulaCalculoSql.flTipoSistema);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdFormulaCalculo);
        primaryKey.append(";");
        primaryKey.append(cdFormulaCalculoSql);
        primaryKey.append(";");
        primaryKey.append(flTipoSistema);
        return primaryKey.toString();
    }

}