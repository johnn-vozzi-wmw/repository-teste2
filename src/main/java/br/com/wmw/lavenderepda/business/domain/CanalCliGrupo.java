package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class CanalCliGrupo extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPCANALCLIGRUPO";
	
	public static String CD_CANAL_GRUPO_VAZIO = "0";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCanalCliente;
    public String cdCanalGrupo;
    public double vlPctMaxDesconto;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof CanalCliGrupo) {
            CanalCliGrupo canalCliGrupo = (CanalCliGrupo) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, canalCliGrupo.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, canalCliGrupo.cdRepresentante) && 
                ValueUtil.valueEquals(cdCanalCliente, canalCliGrupo.cdCanalCliente) && 
                ValueUtil.valueEquals(cdCanalGrupo, canalCliGrupo.cdCanalGrupo);
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
        primaryKey.append(cdCanalCliente);
        primaryKey.append(";");
        primaryKey.append(cdCanalGrupo);
        return primaryKey.toString();
    }

}