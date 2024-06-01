package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class EmpresaEndereco extends BaseDomain {

	public static String TABLE_NAME = "TBLVPEMPRESAENDERECO";
	
	public static String DSDIASENTREGA_NENHUM = "N;N;N;N;N;N;N";
	
    public String cdEmpresa;
    public String dsCidade;
    public String dsBairro;
    public String dsDiasEntrega;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof EmpresaEndereco) {
            EmpresaEndereco empresaEndereco = (EmpresaEndereco) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, empresaEndereco.cdEmpresa) && 
                ValueUtil.valueEquals(dsCidade, empresaEndereco.dsCidade) &&
                ValueUtil.valueEquals(dsBairro, empresaEndereco.dsBairro);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(dsCidade);
        primaryKey.append(";");
        primaryKey.append(dsBairro);
        return primaryKey.toString();
    }

}