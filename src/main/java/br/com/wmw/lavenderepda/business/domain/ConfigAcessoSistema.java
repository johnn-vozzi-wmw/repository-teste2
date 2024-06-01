package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ConfigAcessoSistema extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPCONFIGACESSOSISTEMA";

    public String cdFuncao;
    public int nuDiaSemana;
    public String hrInicio;
    public String hrFim;
    //Nao persistentes
    public String hrFilter;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConfigAcessoSistema) {
            ConfigAcessoSistema configAcessoSistema = (ConfigAcessoSistema) obj;
            return
                ValueUtil.valueEquals(cdFuncao, configAcessoSistema.cdFuncao) 
                && ValueUtil.valueEquals(nuDiaSemana, configAcessoSistema.nuDiaSemana) 
                && ValueUtil.valueEquals(hrInicio, configAcessoSistema.hrInicio);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdFuncao);
        primaryKey.append(";");
        primaryKey.append(nuDiaSemana);
        primaryKey.append(";");
        primaryKey.append(hrInicio);
        return primaryKey.toString();
    }

}