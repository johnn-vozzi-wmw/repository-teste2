package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class FotoNovoCliente extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPFOTONOVOCLIENTE";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String nmFoto;
    public int nuTamanho;
    public Date dtModificacao;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof FotoNovoCliente) {
            FotoNovoCliente fotoNovoCliente = (FotoNovoCliente) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, fotoNovoCliente.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, fotoNovoCliente.cdRepresentante) && 
                ValueUtil.valueEquals(cdCliente, fotoNovoCliente.cdCliente) && 
                ValueUtil.valueEquals(nmFoto, fotoNovoCliente.nmFoto);
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
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(nmFoto);
        return primaryKey.toString();
    }
    
}