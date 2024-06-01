package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class CestaPositCliente extends BaseDomain {

    public static String TABLE_NAME = "TBLVPCESTAPOSITCLIENTE";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCampanha;
    public String cdCesta;
    public String cdCliente;
    public double vlPctpositivacao;
    //--
    private Cesta cesta;
	public String nmRazaoSocial;
	public String nuCnpjFilter;
	public boolean efetivado;
	public double vlPctFaltante;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof CestaPositCliente) {
        	CestaPositCliente cestapositcliente = (CestaPositCliente) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, cestapositcliente.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, cestapositcliente.cdRepresentante) &&
                ValueUtil.valueEquals(cdCampanha, cestapositcliente.cdCampanha) &&
                ValueUtil.valueEquals(cdCesta, cestapositcliente.cdCesta) &&
                ValueUtil.valueEquals(cdCliente, cestapositcliente.cdCliente);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdCampanha);
    	strBuffer.append(";");
    	strBuffer.append(cdCesta);
    	strBuffer.append(";");
    	strBuffer.append(cdCliente);
        return  strBuffer.toString();
    }

	public Cesta getCesta() {
		if (cesta == null) {
			cesta = new Cesta();
		}
		return cesta;
	}

	public void setCesta(Cesta cesta) {
		this.cesta = cesta;
	}

}