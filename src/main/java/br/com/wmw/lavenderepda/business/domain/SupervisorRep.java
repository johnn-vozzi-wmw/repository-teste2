package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class SupervisorRep extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPSUPERVISORREP";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdSupervisor;

    public Representante representante;
    public int nuClientesSemPedidos;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof SupervisorRep) {
            SupervisorRep supervisorRep = (SupervisorRep) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, supervisorRep.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, supervisorRep.cdRepresentante) &&
                ValueUtil.valueEquals(cdSupervisor, supervisorRep.cdSupervisor);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdSupervisor);
        return strBuffer.toString();
    }

	public String getCdDomain() {
		return cdRepresentante;
	}

	public String getDsDomain() {
		return representante.nmRepresentante;
	}

}