package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class TermoCorrecao extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPTERMOCORRECAO";

    public String dsTermo;
    public String dsTermocorrigido;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TermoCorrecao) {
            TermoCorrecao termocorrecao = (TermoCorrecao) obj;
            return
                ValueUtil.valueEquals(dsTermo, termocorrecao.dsTermo) && 
                ValueUtil.valueEquals(dsTermocorrigido, termocorrecao.dsTermocorrigido);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(dsTermo);
        primaryKey.append(";");
        primaryKey.append(dsTermocorrigido);
        return primaryKey.toString();
    }

}