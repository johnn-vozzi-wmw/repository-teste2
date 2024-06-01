package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;

public class CentroCusto extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPCENTROCUSTO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCentroCusto;
    public String dsCentroCusto;
    public String cdLocalEstoque;
    public String flOcultaEstListProd;
    
    //Não persistentes
    public String cdCliente;
    public boolean ignoreCliente;
    
    public CentroCusto() { }
    
    public CentroCusto(String cdCentroCusto) {
    	this.cdEmpresa = SessionLavenderePda.cdEmpresa;
		this.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		this.cdCentroCusto = cdCentroCusto;
	}

    public CentroCusto(String cdEmpresa, String cdRepresentante, String cdCentroCusto) {
    	this.cdEmpresa = cdEmpresa;
    	this.cdRepresentante = cdRepresentante;
    	this.cdCentroCusto = cdCentroCusto;
    }
    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof CentroCusto) {
            CentroCusto centroCusto = (CentroCusto) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, centroCusto.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, centroCusto.cdRepresentante) &&
                ValueUtil.valueEquals(cdCentroCusto, centroCusto.cdCentroCusto);
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
        primaryKey.append(cdCentroCusto);
        return primaryKey.toString();
    }

	//@Override
	public String getCdDomain() {
		return cdCentroCusto;
	}

	//@Override
	public String getDsDomain() {
		return dsCentroCusto;
	}

}