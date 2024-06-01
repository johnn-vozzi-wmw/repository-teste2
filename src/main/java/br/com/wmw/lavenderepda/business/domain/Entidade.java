package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class Entidade extends BaseDomain {

    public static final String TABLE_NAME = "TBLVPENTIDADE";

    public int cdSistema;
    public String nmEntidade;
    public String nmDomain;
    public String dsEntidade;
    public String flAuditaIns;
    public String flAuditaUpd;
    public String flAuditaDel;
    public String flDinamico;
    public int nuCarimbo;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Entidade) {
            Entidade entidade = (Entidade) obj;
            return
                ValueUtil.valueEquals(cdSistema, entidade.cdSistema) &&
                ValueUtil.valueEquals(nmEntidade, entidade.nmEntidade);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdSistema);
        primaryKey.append(";");
        primaryKey.append(nmEntidade);
        return primaryKey.toString();
    }

}