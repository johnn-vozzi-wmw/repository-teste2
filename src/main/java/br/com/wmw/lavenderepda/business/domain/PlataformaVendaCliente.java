package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class PlataformaVendaCliente extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPPLATAFORMAVENDACLIENTE";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdPlataformaVenda;
    public String cdCentroCusto;

    //Não persistentes
    public boolean ignoreCliente;
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PlataformaVendaCliente) {
            PlataformaVendaCliente plataformavendacliente = (PlataformaVendaCliente) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, plataformavendacliente.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, plataformavendacliente.cdRepresentante) && 
                ValueUtil.valueEquals(cdCliente, plataformavendacliente.cdCliente) && 
                ValueUtil.valueEquals(cdPlataformaVenda, plataformavendacliente.cdPlataformaVenda) && 
                ValueUtil.valueEquals(cdCentroCusto, plataformavendacliente.cdCentroCusto);
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
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(cdPlataformaVenda);
        primaryKey.append(";");
        primaryKey.append(cdCentroCusto);
        return primaryKey.toString();
    }

}