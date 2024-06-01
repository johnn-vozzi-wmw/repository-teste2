package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;

public class Empresa extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPEMPRESA";

	public static final String EMPRESA_ZERO = "0";
	
    public String cdEmpresa;
    public String nmEmpresa;
    public String nmEmpresaCurto;
    public String nuCnpj;
    public String nuInscricaoEstadual;
    public String dsLogradouro;
    public String dsBairro;
    public String dsCep;
    public String dsCidade;
    public String dsEstado;
    public String nuFone;
    public double vlMaxPeso;
    public double vlMinPeso;
    public double vlPctPis;
    public double vlPctCofins;
    public double vlPctIrpj;
    public double vlPctCustoVariavel;
    public double vlPctCustoFixo;
    public double vlPctCsll;
    public double vlPctCpp;
    public String nuLogradouro;
    public String nuCip;
    public String cdLocalEstoque;
    public String cdLocal;
    public int nuDiasIntDevolEstoque;
    public double vlPctComissao;
    public double vlFatorFaceamento;

    public String dsUrlConsulta;
    public String nuCsc;
    public String cdEstado;
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Empresa) {
            Empresa empresa = (Empresa) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, empresa.cdEmpresa);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
    	return StringUtil.getStringValue(cdEmpresa);
    }

	public String getCdDomain() {
		return cdEmpresa;
	}

	public String getDsDomain() {
		return ValueUtil.isEmpty(nmEmpresaCurto) ? nmEmpresa : nmEmpresaCurto;
	}
}