package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class AreaVendaCliente extends BaseDomain {

	public static String TABLE_NAME = "TBLVPAREAVENDACLIENTE";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdAreavenda;
    public String cdCliente;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof AreaVendaCliente) {
            AreaVendaCliente areavendacliente = (AreaVendaCliente) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, areavendacliente.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, areavendacliente.cdRepresentante) &&
                ValueUtil.valueEquals(cdAreavenda, areavendacliente.cdAreavenda) &&
                ValueUtil.valueEquals(cdCliente, areavendacliente.cdCliente);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdAreavenda);
    	strBuffer.append(";");
    	strBuffer.append(cdCliente);
        return strBuffer.toString();
    }
}