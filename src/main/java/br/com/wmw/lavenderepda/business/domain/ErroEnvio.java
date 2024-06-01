package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ErroEnvio extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPERROENVIO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String nmEntidade;
    public String dsChave;
    public String dsErro;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ErroEnvio) {
            ErroEnvio erroEnvio = (ErroEnvio) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, erroEnvio.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, erroEnvio.cdRepresentante) && 
                ValueUtil.valueEquals(nmEntidade, erroEnvio.nmEntidade) && 
                ValueUtil.valueEquals(dsChave, erroEnvio.dsChave);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(nmEntidade);
        primaryKey.append(";");
        primaryKey.append(dsChave);
        return primaryKey.toString();
    }

}